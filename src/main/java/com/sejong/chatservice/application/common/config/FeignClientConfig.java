package com.sejong.chatservice.application.common.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.sejong.chatservice.infrastructure.client")
public class FeignClientConfig {
}
