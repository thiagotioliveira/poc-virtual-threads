package com.xgeeks.poc.thiagooliveira.virtualthreads;

import static org.springframework.web.servlet.function.RouterFunctions.route;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class PocVirtualThreadsApplication {

    public static void main(String[] args) {

        SpringApplication.run(PocVirtualThreadsApplication.class, args);
    }

    @Bean
    RestClient restClient(RestClient.Builder builder, @Value("${httpbin.url}") String url) {
        return builder.baseUrl(url).build();
    }

    @Bean
    RouterFunction<ServerResponse> endpoint(RestClient restClient) {
        var log = LoggerFactory.getLogger(getClass());

        return route().GET("/{seconds}", request -> {
            var seconds = request.pathVariable("seconds");
            var response = restClient.get().uri("/delay/" + seconds).retrieve().toEntity(String.class);

            log.info("{} on {}", response.getStatusCode(), Thread.currentThread());

            return ServerResponse.ok().body(Map.of("status", "ok"));
        }).build();
    }

}
