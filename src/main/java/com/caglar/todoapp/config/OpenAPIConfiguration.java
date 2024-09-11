package com.caglar.todoapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8005");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("caglarersino@outlook.com");
        contact.setName("Caglar Ersin");

        Info info = new Info()
                .title("Todo App APIs")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to register and create todo list.");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
