/*
package com.petpr.webclient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@Component
public class TestSupplier {

    private WebClient.Builder externalWebClient;

    private static Map<String, ReactiveCircuitBreaker> registryMap = new HashMap<>();

    private CircuitBreakerRegistry beanRegistry;

    private ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    static CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

    @Autowired
    public TestSupplier(@Qualifier("withoutEureka") WebClient.Builder externalWebClient, CircuitBreakerRegistry beanRegistry, ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        this.externalWebClient = externalWebClient;
        this.beanRegistry = beanRegistry;
        this.reactiveCircuitBreakerFactory = reactiveCircuitBreakerFactory;
    }

    private static CircuitBreakerRegistry circuitBreakerRegistry;

    static {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(3)
                .failureRateThreshold(50)
                .permittedNumberOfCallsInHalfOpenState(3)
                .minimumNumberOfCalls(3)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build();

        circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);



        ReactiveCircuitBreaker reactiveCircuitBreaker = new ReactiveResilience4JCircuitBreaker("name", circuitBreakerConfig, circuitBreakerRegistry, null);
    }

    public CompletableFuture<Object> createFuture(Map<String, Object> data) throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            return externalWebClient.build()
                    .method(HttpMethod.valueOf((String) data.get("method")))
                    .uri((String) data.get("uri"))
                    .bodyValue(data.get("body"))
                    .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                    .retrieve()
                    .toEntity(Object.class)
                    .map(objectResponseEntity -> {
                        return objectResponseEntity.getBody();
                    });
        });
    }

    public Mono<Object> callWebclient(Map<String, Object> data) {
        return externalWebClient.build()
                .method(HttpMethod.valueOf((String) data.get("method")))
                .uri((String) data.get("uri"))
                .bodyValue(data.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Object callWebclientObj(Map<String, Object> data) {
        return externalWebClient.build()
                .method(HttpMethod.valueOf((String) data.get("method")))
                .uri((String) data.get("uri"))
                .bodyValue(data.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .toEntity(Object.class)
                .map(objectResponseEntity -> {
                    return objectResponseEntity.getBody();
                });
    }

    public Boolean test1() {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

        CheckedFunction0<String> checkedSupplier =
                CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {
                    throw new RuntimeException("BAM!");
                });
        Try<String> result;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time;
        for (int i = 0; i < 20; i++) {
            time = System.currentTimeMillis();
            result = Try.of(checkedSupplier)
                    .recover(throwable -> "Hello Recovery");
            System.out.println(System.currentTimeMillis() - time + "   " + result.get());
        }
        return true;
    }

    public Mono<Object> test2(Map<String, Object> data) {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");
        CheckedFunction0<Mono<Object>> checkedSupplier =
                CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {
                    return externalWebClient.build()
                            .method(HttpMethod.valueOf((String) data.get("method")))
                            .uri((String) data.get("uri"))
                            .bodyValue(data.get("body"))
                            .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                            .retrieve()
                            .bodyToMono(Object.class);
                });

        return Try.of(checkedSupplier)
                .recover(throwable -> Mono.just(throwable.getMessage()))
                .get();
    }

    public Mono<Map> test3(Map<String, Object> data) {

        return Mono.fromSupplier(() -> {
            Map response = new HashMap();
            externalWebClient.build()
                    .method(HttpMethod.valueOf((String) data.get("method")))
                    .uri((String) data.get("uri"))
                    .bodyValue(data.get("body"))
                    .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                    .retrieve()
                    .toEntity(Object.class)
                    .map(responseEntity -> response.put("response", responseEntity.getBody()));
            return response;
        });
        // .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
    }


    public Mono<Object> test4(Map<String, Object> data) {
        String name = "customer-service";
        Long time = System.currentTimeMillis();
*/
/*        for (int i = 0; i < 150; i++) {
            time = System.currentTimeMillis();
            externalWebClient.build()
                    .method(HttpMethod.valueOf((String) data.get("method")))
                    .uri((String) data.get("uri"))
                    .bodyValue(data.get("body"))
                    .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                    .retrieve()
                    .bodyToMono(Object.class)
                    .transform(it -> {
                        ReactiveCircuitBreaker rcb = getFromReg("customer-service");
                        return rcb.run(it, throwable -> {
                            System.out.println(throwable.getMessage());
                            return Mono.just(throwable.getMessage());
                        });
                    });
            System.out.println(i + "   " + (System.currentTimeMillis() - time));
        }*//*

        return externalWebClient.build()
                .method(HttpMethod.valueOf((String) data.get("method")))
                .uri((String) data.get("uri"))
                .bodyValue(data.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .bodyToMono(Object.class)
                .transform(it -> {
                    ReactiveCircuitBreaker rcb = getFromReg(name);
                    return rcb.run(it, throwable -> {
                        System.out.println(System.currentTimeMillis() - time);
                        System.out.println(throwable.getMessage());
                        return Mono.just(throwable.getMessage());
                    });
                });


    }

    private ReactiveCircuitBreaker getFromReg(String name) {

        if (!registryMap.containsKey(name)) {

            ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create(name);
            registryMap.put(name, rcb);
        }
        return registryMap.get(name);
    }


    public Mono<Object> test5(Map<String, Object> data) {
        ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("customer-service");
        Supplier<Mono<Object>> decoratedSupplier = CircuitBreaker
                .decorateSupplier(beanRegistry.circuitBreaker("customer-service"), () -> callWebclient(data));

        Mono<Object> result = Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> Mono.just(throwable.getMessage())).get();
        return result;
    }


    public Mono<Object> test6(Map<String, Object> data) throws ExecutionException, InterruptedException {

        return reactiveCircuitBreakerFactory.create("completablefuturedelay")
                .run(Mono.fromFuture(createFuture(data)), fallback -> {
                            return Mono.just("fallback");
                        }
                );
    }

    public Object test7(Map<String, Object> data) throws ExecutionException, InterruptedException {

        return createFuture(data).get();
    }

    public Mono<Object> test8(Map<String, Object> data) {
        return Mono.fromCallable(() -> callWebclientObj(data))
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
    }


}
*/
