package com.uteq.divisiones;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsDivisionesApplication {
  public static void main(String[] args){ SpringApplication.run(MsDivisionesApplication.class, args); }
}
