package com.nikguscode.SusuAPI.model.service.requests;

import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;

@Service
public interface ExecutorInterface {
    public HttpRequest createPostRequest(String body, String link);
    public HttpRequest createPostRequest(String body, String cookie, String link);
    public HttpRequest createGetRequest(String cookie, String link);
}
