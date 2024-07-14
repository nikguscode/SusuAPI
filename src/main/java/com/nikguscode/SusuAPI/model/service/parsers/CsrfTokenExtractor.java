package com.nikguscode.SusuAPIs.model.service.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CsrfTokenExtractor {
    private String getToken(HttpResponse<String> response) {
        Document doc = Jsoup.parse(response.body());
        Element tokenElement = doc.selectFirst("input[name=__RequestVerificationToken]");

        if (tokenElement == null) {
            throw new IllegalArgumentException("CSRF token not found in the response");
        }

        String token = tokenElement.val();

        if (!token.matches("^[a-zA-Z0-9-_.]+$")) {
            throw new IllegalArgumentException("Invalid CSRF token format");
        }

        return token;
    }

    private String getResponseBody(HttpClient client) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://studlk.susu.ru/Account/Login"))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        int statusCode = response.statusCode();

        if (statusCode == 200 || statusCode == 302 || statusCode == 303) {
            return getToken(response);
        } else {
            throw new RuntimeException("CSRF token extract exception, status code: " + statusCode);
        }
    }

    public String getCsrfToken(HttpClient client) {
       return getResponseBody(client);
    }
}
