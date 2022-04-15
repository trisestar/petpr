package com.petpr.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class Webclient {

    @Autowired
    @Qualifier("withEureka")
    private WebClient.Builder internalWebClient;

    @Autowired
    @Qualifier("withoutEureka")
    private WebClient.Builder externalWebClient;

    public Mono executeInternal(Map<String, Object> request) {

        return internalWebClient.build()
                .method(HttpMethod.valueOf((String) request.get("method")))
                .uri((String) request.get("uri"))
                .bodyValue(request.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono executeExternal(Map<String, Object> request) {

        return externalWebClient.build()
                .method(HttpMethod.valueOf((String) request.get("method")))
                .uri((String) request.get("uri"))
                .bodyValue(request.get("body"))
                .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                .retrieve()
                .bodyToMono(Object.class);
    }
}
