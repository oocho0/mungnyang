package com.mungnyang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        HttpClient client = HttpClientBuilder.create().setMaxConnTotal(50).setMaxConnPerRoute(20).build();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        factory.setHttpClient(client);

        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
        restTemplate.setInterceptors(Collections.singletonList(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                ClientHttpResponse response = execution.execute(request, body);
                log.info("URL 요청");
                return response;
            }
        }));
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
