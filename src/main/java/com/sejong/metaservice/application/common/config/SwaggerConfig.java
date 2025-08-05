package com.sejong.metaservice.application.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
                @Server(url = "/chat-service"),
                @Server(url = "/")
        },
        info = @Info(
                title = "Chatting API",
                version = "v1",
                description = "Chatting API 문서입니다."
        )
)
@Configuration
public class SwaggerConfig {


}