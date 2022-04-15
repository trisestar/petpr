package com.mainapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class Controller {

    @GetMapping("/get")
    public Mono get() {
        System.out.println("mainapp called get " + LocalDateTime.now());
        return Mono.just("123");
    }

    @PostMapping("/post")
    public Mono request(@RequestBody Map<String, String> data) {
        System.out.println("mainapp called post " + LocalDateTime.now());
        return Mono.just("321");
    }

}
