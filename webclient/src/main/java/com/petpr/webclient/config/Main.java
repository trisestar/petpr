package com.petpr.webclient.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

public class Main {
    public static void main(String[] args) {
// Given
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

// When I decorate my function and invoke the decorated function
        CheckedFunction0<String> checkedSupplier =
                CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {
                    throw new RuntimeException("BAM!");
                });
        Try<String> result;
        long time;
        for (int i = 0; i < 20; i++) {
            time = System.currentTimeMillis();
            result = Try.of(checkedSupplier)
                    .recover(throwable -> "Hello Recovery");
            System.out.println(System.currentTimeMillis() - time + "   " + result.get());
        }

    }
}
