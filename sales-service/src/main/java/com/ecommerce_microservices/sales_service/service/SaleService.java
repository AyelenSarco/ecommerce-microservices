package com.ecommerce_microservices.sales_service.service;


import com.ecommerce_microservices.sales_service.dto.cart.CartStatusEnum;
import com.ecommerce_microservices.sales_service.dto.cart.CartViewDTO;
import com.ecommerce_microservices.sales_service.dto.product.StockErrorDTO;
import com.ecommerce_microservices.sales_service.dto.product.StockRequestDTO;
import com.ecommerce_microservices.sales_service.dto.product.StockValidationResponseDTO;
import com.ecommerce_microservices.sales_service.dto.sale.SaleDTO;
import com.ecommerce_microservices.sales_service.exception.ConflictException;
import com.ecommerce_microservices.sales_service.exception.InsufficientStockException;
import com.ecommerce_microservices.sales_service.exception.NotFoundException;
import com.ecommerce_microservices.sales_service.mapper.SaleMapper;
import com.ecommerce_microservices.sales_service.model.Sale;
import com.ecommerce_microservices.sales_service.model.Snapshot;
import com.ecommerce_microservices.sales_service.repository.CartServiceClient;
import com.ecommerce_microservices.sales_service.repository.ISaleRepository;
import com.ecommerce_microservices.sales_service.repository.ProductServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SaleService implements ISaleService {

    private final ISaleRepository saleRepository;

    private final SaleMapper saleMapper;

    private final CartServiceClient cartService;
    private final ProductServiceClient productService;

    @CircuitBreaker(name="carts-service", fallbackMethod = "fallBackGetCart")
    @Retry(name = "carts-service")
    public CartViewDTO getCart(Long cartId){
        return cartService.getCart(cartId).getData();
    }


    @CircuitBreaker(name="products-service", fallbackMethod = "fallBackValidateStock")
    @Retry(name = "products-service")
    public List<StockValidationResponseDTO> validateStock(List<StockRequestDTO> stockRequests){

        // Validate stock
        List<StockValidationResponseDTO> stockResponses = productService.validateStock(stockRequests).getData();

        //Check stock

        boolean allAvaliable = stockResponses.stream()
                .allMatch(StockValidationResponseDTO::isAvailable);

        if (!allAvaliable) {
            List<StockValidationResponseDTO> unavaliable = stockResponses
                    .stream()
                    .filter(u -> !u.isAvailable())
                    .toList();

            List<StockErrorDTO> errors = unavaliable
                    .stream()
                    .map(r -> StockErrorDTO.builder()
                            .productId(r.getProductId())
                            .requestedQuantity(r.getRequestedQuantity())
                            .availableQuantity(r.getAvailableQuantity())
                            .build()
                    )
                    .toList();

            throw new InsufficientStockException(errors);

        }

        return stockResponses;

    }


    @CircuitBreaker(name="products-service",
            fallbackMethod = "fallBackProductsService")
    @Retry(name = "products-service")
    public void decrementStock(List<StockRequestDTO> stockRequests){
        productService.decrementStock(stockRequests);
    }

    @CircuitBreaker(name="carts-service",
            fallbackMethod = "fallBackCartsService")
    @Retry(name="carts-service")
    public void closeCart(Long cartId){
        cartService.closeCart(cartId);
    }

    /**
     * This method creates a sale with the given cartId if it has OPEN status.
     * It validates stock, creates snapshots and sale data, decreases stock and change cart status.
     * **/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaleDTO createSale(Long cartId) {

        // Obtain the cart
        CartViewDTO cart = this.getCart(cartId);

        if (cart.getStatus() == CartStatusEnum.CLOSED) {
            throw new ConflictException("Cart is closed");
        }

        // Create list to validate Stock
        List<StockRequestDTO> stockRequests = cart.getCartItems()
                .stream()
                .map( item -> new StockRequestDTO(item.getProductId(), item.getQuantity()))
                .toList();


        // Validate stock
        List<StockValidationResponseDTO> stockResponses = this.validateStock(stockRequests);

        // Create sale

        Sale sale =  Sale.builder()
                .cartId(cartId)
                .date(LocalDate.now())
                .build();

        // Create snapshots
        List<Snapshot> snapshots = cart.getCartItems()
                .stream()
                .map(item -> {

                            StockValidationResponseDTO stock = stockResponses.stream()
                                    .filter(p -> p.getProductId().equals(item.getProductId()))
                                    .findFirst()
                                    .orElseThrow();


                            return Snapshot.builder()
                                    .productId(item.getProductId())
                                    .productName(item.getProductName())
                                    .unitPrice(stock.getUnitPrice())
                                    .quantity(stock.getRequestedQuantity())
                                    .subtotal(stock.getUnitPrice() *  stock.getRequestedQuantity())
                                    .sale(sale)
                                    .build();
                        }
                        )
                .toList();

        sale.setSnapshots(snapshots);

        // Calculate total

        double total = snapshots.stream().mapToDouble(Snapshot::getSubtotal).sum();
        sale.setTotal(total);

        saleRepository.save(sale);

        // Decrement stock --> Only at this point in order to do it only if every thing it's okey.
        // to do --> for example: try decrement, catch increment and throw exception
        this.decrementStock(stockRequests);

        // Close cart
        this.closeCart(cartId);

        return saleMapper.toSaleDTO(sale);
    }

    @Override
    public SaleDTO getSale(Long saleId) {

        Sale sale = saleRepository.findById(saleId).orElseThrow( () -> new NotFoundException("Sale not found"));
        return saleMapper.toSaleDTO(sale);
    }

    @Override
    public List<SaleDTO> getSales() {

        List<Sale> sales = saleRepository.findAll();
        List<SaleDTO> salesDTOS = new ArrayList<>();

        for (Sale sale : sales) {
            salesDTOS.add(saleMapper.toSaleDTO(sale));
        }

        return salesDTOS;
    }



    public CartViewDTO fallBackGetCart(Long cartId, Throwable throwable) {
        throw new RuntimeException("Cart service unavailable");
    }

    public List<StockValidationResponseDTO> fallBackValidateStock(List<StockRequestDTO> stockRequest ,Throwable throwable) {
        throw new RuntimeException("Product service unavailable");
    }

    public void fallBackProductsService(List<StockRequestDTO> stockRequest ,Throwable throwable) {
        throw new RuntimeException("Product service unavailable");
    }

    public void fallBackCartsService(Long cartId, Throwable throwable) {
        throw new RuntimeException("Cart service unavailable");
    }


}
