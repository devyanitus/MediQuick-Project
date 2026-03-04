package com.mediquick.consultant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsultantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultantServiceApplication.class, args);
    }
}