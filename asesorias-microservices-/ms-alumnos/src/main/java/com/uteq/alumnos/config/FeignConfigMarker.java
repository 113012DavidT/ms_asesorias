
package com.uteq.alumnos.config;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
@Configuration @EnableFeignClients(basePackages = {"com.uteq.alumnos.snapshot.client"})
public class FeignConfigMarker {}
