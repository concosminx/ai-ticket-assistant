package com.nimsoc.ticket.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimsoc.ticket.model.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class JsonParserConfig {

    private final ResourceLoader resourceLoader;

    public JsonParserConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //@Bean
    public CommandLineRunner parseJson() {
        return args -> {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules(); // Register JSR-310 module for LocalDateTime
            Resource resource = resourceLoader.getResource("classpath:fake-data/incidente.json");
            try (InputStream inputStream = resource.getInputStream()) {
                List<Ticket> tickets = objectMapper.readValue(inputStream, new TypeReference<List<Ticket>>() {});
                tickets.forEach(ticket -> log.info(ticket.toString()));
            } catch (IOException e) {
                log.error("Failed to parse incidente.json", e);
            }
        };
    }
}