package com.artostapyshyn.data.retrival;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@OpenAPIDefinition(
        info = @Info(title = "Data retrieval service API", version = "v1")
)
@SpringBootApplication
@EnableDiscoveryClient
public class DataRetrievalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataRetrievalServiceApplication.class, args);
    }
}
