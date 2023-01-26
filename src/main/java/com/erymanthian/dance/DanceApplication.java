package com.erymanthian.dance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
@ConfigurationPropertiesScan("com.erymanthian.dance.configurationproperties")
public class DanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanceApplication.class, args);
    }

    @Bean
    Path path() throws IOException {
        return Files.createDirectories(Path.of("dance"));
    }
}
