package com.example.hexagonal.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIntegrationTest {

        private static final Pattern ORDER_ID_PATTERN = Pattern.compile("\\\"orderId\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

        @LocalServerPort
        private int port;

    @Test
        void should_create_and_get_order() throws IOException, InterruptedException {
                var client = HttpClient.newHttpClient();

        String payload = """
                {
                  "customerId": "customer-42",
                  "items": [
                    {"productCode": "BOOK-1", "quantity": 2, "unitPrice": 12.50, "currency": "EUR"}
                  ]
                }
                """;

                var createRequest = HttpRequest.newBuilder()
                                .uri(URI.create(baseUrl() + "/api/orders"))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(payload))
                                .build();

                var createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
                assertEquals(201, createResponse.statusCode());
                assertTrue(createResponse.body().contains("\"status\":\"CREATED\""));

                String orderId = extractOrderId(createResponse.body());

                var getRequest = HttpRequest.newBuilder()
                                .uri(URI.create(baseUrl() + "/api/orders/" + orderId))
                                .GET()
                                .build();

                var getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, getResponse.statusCode());
                assertTrue(getResponse.body().contains("\"orderId\":\"" + orderId + "\""));
                assertTrue(getResponse.body().contains("\"customerId\":\"customer-42\""));
    }

        private String baseUrl() {
                return "http://localhost:" + port;
        }

        private String extractOrderId(String body) {
                Matcher matcher = ORDER_ID_PATTERN.matcher(body);
                if (!matcher.find()) {
                        throw new IllegalStateException("orderId not found in response: " + body);
                }
                return matcher.group(1);
        }
}
