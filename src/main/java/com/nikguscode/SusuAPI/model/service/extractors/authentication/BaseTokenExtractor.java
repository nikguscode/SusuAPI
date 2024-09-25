package com.nikguscode.SusuAPI.model.service.extractors.authentication;

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public abstract class BaseTokenExtractor {
    protected String getToken(HttpResponse<String> response, Map<String, String> regex) {
        Document doc = Jsoup.parse(response.body());
        Element tokenElement = doc.selectFirst(regex.get(TOKEN_REGEX));

        if (tokenElement == null) {
            throw new IllegalArgumentException("CSRF token not found in the response");
        }

        if (!tokenElement.val().matches(regex.get(TOKEN_VALIDITY_REGEX))) {
            throw new IllegalArgumentException("Invalid CSRF token format");
        }

        return tokenElement.val();
    }

    protected HttpResponse<String> getResponseBody(HttpClient client, String requestUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract String extract(HttpClient client);
}
