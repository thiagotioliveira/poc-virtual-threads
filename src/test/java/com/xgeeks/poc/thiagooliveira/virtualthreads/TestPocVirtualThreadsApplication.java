package com.xgeeks.poc.thiagooliveira.virtualthreads;

import org.springframework.boot.SpringApplication;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;

public class TestPocVirtualThreadsApplication {

    private static final Boolean RUN_WITH_VIRTUAL_THREADS = true;

    public static void main(String[] args) {
        var port = 8080;
        var httpbin = new GenericContainer<>("mccutchen/go-httpbin")
                .withExposedPorts(port)
                .waitingFor(new WaitAllStrategy());
        httpbin.start();

        var threads = Integer.toString(Runtime.getRuntime().availableProcessors());
        System.out.println("Number of available processors: " +threads);
        System.setProperty("server.tomcat.threads.max", threads);
        System.setProperty("server.tomcat.threads.min-spare", threads);
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", threads);
        System.setProperty("spring.threads.virtual.enabled", Boolean.toString(RUN_WITH_VIRTUAL_THREADS));
        System.setProperty("httpbin.url", "http://"+httpbin.getHost() + ":" + httpbin.getMappedPort(port));

        SpringApplication.run(PocVirtualThreadsApplication.class);
    }

}
