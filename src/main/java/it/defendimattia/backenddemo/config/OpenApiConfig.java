package it.defendimattia.backenddemo.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Luxury Watches REST API", version = "1.0", description = "REST API for luxury watches catalog management", contact = @Contact(name = "Mattia")))
public class OpenApiConfig {

}