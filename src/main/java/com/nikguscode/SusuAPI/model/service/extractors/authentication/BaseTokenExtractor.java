package com.nikguscode.SusuAPI.model.service.extractors.authentication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class BaseTokenExtractor {
    protected String getToken(HttpResponse<String> response) {
        Document doc = Jsoup.parse(response.body());
        Element tokenElement = doc.selectFirst("input[name=__RequestVerificationToken]");

        if (tokenElement == null) {
            throw new IllegalArgumentException("CSRF token not found in the response");
        }

        if (!tokenElement.val().matches("^[a-zA-Z0-9-_.]+$")) {
            throw new IllegalArgumentException("Invalid CSRF token format");
        }

        return tokenElement.val();
    }

    protected String getResponseBody(HttpClient client, String requestUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
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

    public abstract String extract(HttpClient client);
}
