
package com.uteq.asesorias.config;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
@Configuration @EnableFeignClients(basePackages = {"com.uteq.asesorias.snapshot.client"})
public class FeignConfigMarker {}
