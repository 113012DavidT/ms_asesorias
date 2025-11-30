package com.uteq.alumnos;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
public class MsAlumnosApplication {
  public static void main(String[] args){ SpringApplication.run(MsAlumnosApplication.class, args); }
}
