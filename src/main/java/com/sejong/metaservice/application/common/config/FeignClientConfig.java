package com.sejong.metaservice.application.common.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.sejong.metaservice.infrastructure.client")
public class FeignClientConfig {
}
