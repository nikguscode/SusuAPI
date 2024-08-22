package com.nikguscode.SusuAPI.model.service.querymanager.configuration.request;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpRequest;

@Service
public class HttpClientRequestBuilder implements RequestBuilder {
    private HttpRequest.Builder createRequest(String link) {
        return HttpRequest.newBuilder()
                .uri(URI.create(link))
                .header("Content-Type", "application/x-www-form-urlencoded");
    }

    @Override
    public HttpRequest createPostRequest(String body, String link) {
        return createRequest(link)
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    @Override
    public HttpRequest createPostRequest(String body, String cookie, String link) {
        return createRequest(link)
                .header("Cookie", cookie)
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    @Override
    public HttpRequest createGetRequest(String cookie, String link) {
        return createRequest(link)
                .header("Cookie", cookie)
                .GET().build();
    }
}
