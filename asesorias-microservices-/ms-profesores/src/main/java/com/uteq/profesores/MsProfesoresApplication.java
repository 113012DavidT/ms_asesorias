package com.uteq.profesores;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication

@EnableFeignClients
public class MsProfesoresApplication {
  public static void main(String[] args){ SpringApplication.run(MsProfesoresApplication.class, args); }
}
