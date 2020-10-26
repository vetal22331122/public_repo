package com.vetal22331122.webfluxproject;

import org.springframework.web.reactive.function.client.WebClient;

public class TestClient {
    private WebClient client = WebClient.create("http://localhost:8080");

    public static void main(String[] args) {
        TestClient webClient = new TestClient();
        System.out.println("hi");
    }


}
