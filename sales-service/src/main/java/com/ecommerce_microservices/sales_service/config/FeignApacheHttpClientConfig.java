package com.ecommerce_microservices.sales_service.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.httpclient.ApacheHttpClient;
@Configuration
public class FeignApacheHttpClientConfig {
    @Bean
    public ApacheHttpClient httpClient() {
        return new ApacheHttpClient(HttpClientBuilder.create().build());
    }
}