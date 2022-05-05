package com.petpr.webclient.controller;

import com.petpr.webclient.Webclient;
import com.petpr.webclient.config.Config;
import com.petpr.webclient.model.Banana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RefreshScope
public class Controller {

    @Autowired
    private Webclient webclient;

    @Autowired
    private Banana banana;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Config config;

    @GetMapping("/")
    public Mono<Object> get() {
        System.out.println("webclient called get " + LocalDateTime.now());
        return Mono.just(config.getPropertyObj());
    }

    @PostMapping("/webclient")
    public Mono<Object> request(@RequestBody Map<String, Object> data) {
        System.out.println("webclient called post " + LocalDateTime.now());
        return webclient.execute(data);
    }

    @PostMapping("/banana")
    public Object request() {
        System.out.println("banana called post " + banana + "\n" + LocalDateTime.now());
        return 1;
    }

    @PostMapping("/getprop")
    public Mono<Object> getprop() {
        System.out.println("getprop called post " + LocalDateTime.now());
        return Mono.just(config.getPropertyObj());
    }

    @PostMapping("/reload")
    public Mono<Object> reload() {
        System.out.println("reload called post " + LocalDateTime.now());

        Banana newBanana = new Banana();
        newBanana.setSize(config.getPropertyObj());
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
        beanFactory.destroyBean(Banana.class);

        return Mono.just(1);
    }

}
