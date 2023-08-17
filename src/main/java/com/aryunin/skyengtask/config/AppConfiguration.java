package com.aryunin.skyengtask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OpenAPI myOpenAPI() {
        Info info = new Info()
                .title("Postal service API")
                .description("Test task for SkyEng");

        return new OpenAPI().info(info);
    }
}
