package com.jobapp;

import com.jobapp.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.jobapp.repository")
@EntityScan("com.jobapp.model")
@EnableConfigurationProperties(FileStorageProperties.class)
public class JobAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobAppApplication.class, args);
    }
}
