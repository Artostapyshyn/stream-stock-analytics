package com.artostapyshyn.data.retrival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DataRetrievalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataRetrievalServiceApplication.class, args);
    }
}
