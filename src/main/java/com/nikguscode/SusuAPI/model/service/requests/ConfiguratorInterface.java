package com.nikguscode.SusuAPI.model.service.requests;

import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.Map;

@Service
public interface ConfiguratorInterface {
    public HttpClient.Builder createClient();
    public String createBody(Map<String, String> bodyParameters);
}
