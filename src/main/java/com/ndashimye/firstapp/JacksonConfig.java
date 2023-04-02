package com.ndashimye.firstapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeJson() {
        return builder -> builder.indentOutput(true);
    }

//
//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//    }
}
