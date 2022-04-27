/*
package com.petpr.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CBExecutor {
    @Autowired
    @Qualifier("withoutEureka")
    private WebClient.Builder externalWebClient;

    private CircuitBreakerFactory circuitBreakerFactory;

    public CBExecutor(CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }


    public Mono<Object> call(Map<String,Object> data){
        return externalWebClient.build()
                .method(HttpMethod.valueOf((String) data.get("method")))
                .uri((String) data.get("uri"))
                .bodyValue(data.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .bodyToMono(Object.class)
                .transform(it -> {
                    ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("customer-service");
                    return rcb.run(it, throwable -> {
                        System.out.println(System.currentTimeMillis() - time);
                        System.out.println(throwable.getMessage());
                        return Mono.just(throwable.getMessage());
                    });
                });
    }
}
*/
