package com.genebridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.genebridge")
public class GeneBridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneBridgeApplication.class, args);
    }
}
