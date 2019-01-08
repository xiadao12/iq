package com.zcy.iqserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class IqserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(IqserverApplication.class, args);
    }

}

