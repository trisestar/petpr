package com.petpr.webclient.config;

import com.petpr.webclient.model.Banana;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
@RefreshScope
public class Config {

    @Value("${test}")
    private String propertyObj;

    public String getPropertyObj() {
        return propertyObj;
    }

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

    @Bean
    @RefreshScope
    public Banana createTestBean() {
        Banana banana = new Banana();
        banana.setSize(propertyObj);
        return banana;
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        CircuitBreakerRegistry cbr = CircuitBreakerRegistry.ofDefaults();
        return factory -> {
            factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3)).build())
                    .circuitBreakerConfig(CircuitBreakerConfig.custom()
                            .failureRateThreshold(50)
                            .minimumNumberOfCalls(5)
                            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                            .slidingWindowSize(5)
                            .waitDurationInOpenState(Duration.ofSeconds(30))
                            .build()).build());
        };
    }


}
