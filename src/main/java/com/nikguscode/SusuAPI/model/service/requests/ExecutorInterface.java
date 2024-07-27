package com.nikguscode.SusuAPI.model.service.requests;

import com.nikguscode.SusuAPI.model.entities.Student;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Map;

@Service
public interface ExecutorInterface {
    public HttpRequest createPostRequest(String body, String link);
    public HttpRequest createPostRequest(String body, String cookie, String link);
    public HttpRequest createGetRequest(String cookie, String link);
}
