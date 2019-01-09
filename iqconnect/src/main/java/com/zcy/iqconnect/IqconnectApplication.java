package com.zcy.iqconnect;

import com.zcy.iqconnect.service.IqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class IqconnectApplication {

    @Autowired
    private static IqService iqService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(IqconnectApplication.class, args);
    }
}

