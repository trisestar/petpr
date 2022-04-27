package com.petpr.webclient.controller;


import com.petpr.webclient.Webclient;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class Controller2 {
    private ReactiveCircuitBreakerFactory circuitBreakerFactory;
    private Webclient webclient;

    public Controller2(ReactiveCircuitBreakerFactory circuitBreakerFactory, Webclient webclient) {
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.webclient = webclient;
    }

    /*    @Autowired
    TestSupplier testSupplier;*/

/*    @PostMapping("/testsup")
    public Object testsup1() {
        return testSupplier.test1();
    }

    @PostMapping("/testsupweb")
    public Object request(@RequestBody Map<String, Object> data) throws ExecutionException, InterruptedException {
        System.out.println("webclient called post " + LocalDateTime.now());
        return testSupplier.test4(data);
    }*/

    @PostMapping("/testsupweb")
    public Object request(@RequestBody Map<String, Object> data) throws ExecutionException, InterruptedException {
        System.out.println("webclient called post " + LocalDateTime.now());
        return circuitBreakerFactory.create("qwe").run(webclient.performCall(data), throwable -> {
            System.out.println("fallback");
            return Mono.just(throwable.getMessage());
        });
    }
}
