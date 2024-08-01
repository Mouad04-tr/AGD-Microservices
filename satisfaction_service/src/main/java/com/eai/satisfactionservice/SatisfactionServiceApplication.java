package com.eai.satisfactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.eai.openfeignservice")
public class SatisfactionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SatisfactionServiceApplication.class, args);
    }
}
