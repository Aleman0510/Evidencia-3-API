package com.ecommerce.compositeapidemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Value("${microservices.base-url}")
    private String microservicesBaseUrl;

    @Value("${exchange.api.base-url}")
    private String exchangeBaseUrl;

    @Value("${http.client.connect-timeout-ms:2000}")
    private int connectTimeoutMs;

    @Value("${http.client.read-timeout-ms:3000}")
    private int readTimeoutMs;

    private SimpleClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeoutMs);
        factory.setReadTimeout(readTimeoutMs);
        return factory;
    }

    /** Cliente HTTP para los microservicios propios (catalogo e inventario). */
    @Bean
    public RestClient microservicesClient() {
        return RestClient.builder()
                .baseUrl(microservicesBaseUrl)
                .requestFactory(requestFactory())
                .build();
    }

    /** Cliente HTTP para la API externa de divisas (Frankfurter). */
    @Bean
    public RestClient exchangeClient() {
        return RestClient.builder()
                .baseUrl(exchangeBaseUrl)
                .requestFactory(requestFactory())
                .build();
    }

    /** Pool de hilos para ejecutar las 3 peticiones en paralelo. */
    @Bean(destroyMethod = "shutdown")
    public ExecutorService ioExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}