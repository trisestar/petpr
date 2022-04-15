package com.petpr.webclient.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {


    @Bean
    @LoadBalanced
    @Qualifier("withEureka")
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier("withoutEureka")
    public WebClient.Builder loadWebClientBuilder() {
        return WebClient.builder();
    }
}
