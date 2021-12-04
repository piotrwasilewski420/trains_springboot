package com.springjpa.datajpa;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.springjpa.datajpa.UrlLinks.UrlLinks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpHeaders;

@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }
    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(UrlLinks.BASE_URL).build();
    }
}
