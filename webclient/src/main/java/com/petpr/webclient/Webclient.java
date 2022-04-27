package com.petpr.webclient;

import com.petpr.webclient.config.Config;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class Webclient {

    @Autowired
    @Qualifier("withEureka")
    private WebClient.Builder internalWebClient;

    @Autowired
    @Qualifier("withoutEureka")
    private WebClient.Builder externalWebClient;

    public Mono<Object> execute(Map<String, Object> request) {
/*        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("name");
        if (request.get("internal").equals(true)) {
            return performCall(request, internalWebClient)
                    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
        } else {
            return performCall(request, externalWebClient)
                    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
        }*/

        return Mono.empty();

    }

    public Mono<Object> performCall(Map<String, Object> request) {
        return externalWebClient.build()
                .method(HttpMethod.valueOf((String) request.get("method")))
                .uri((String) request.get("uri"))
                .bodyValue(request.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .bodyToMono(Object.class);
    }

/*    public Mono<Object> executeInternal(Map<String, Object> request) {

        return internalWebClient.build()
                .method(HttpMethod.valueOf((String) request.get("method")))
                .uri((String) request.get("uri"))
                .bodyValue(request.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<Object> executeExternal(Map<String, Object> request) {

        return externalWebClient.build()
                .method(HttpMethod.valueOf((String) request.get("method")))
                .uri((String) request.get("uri"))
                .bodyValue(request.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .bodyToMono(Object.class);
    }*/
}
