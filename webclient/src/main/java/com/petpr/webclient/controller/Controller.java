package com.petpr.webclient.controller;

import com.petpr.webclient.Webclient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private Webclient webclient;

    @GetMapping("/")
    public Mono get() {
        System.out.println("webclient called get " + LocalDateTime.now());
        return Mono.just("123");
    }

    @PostMapping("/webclient")
    public Mono request(@RequestBody Map<String, Object> data) {
        System.out.println("webclient called post " + LocalDateTime.now());
        if (data.get("internal").equals(true)) {
            return webclient.executeInternal(data);
        } else {
            return webclient.executeExternal(data);
        }
    }

}
