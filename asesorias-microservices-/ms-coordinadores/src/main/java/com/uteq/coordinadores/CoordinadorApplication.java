package com.uteq.coordinadores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CoordinadorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoordinadorApplication.class, args);
    }
}
