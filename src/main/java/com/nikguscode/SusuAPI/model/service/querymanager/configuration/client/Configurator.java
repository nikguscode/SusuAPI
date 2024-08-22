package com.nikguscode.SusuAPI.model.service.querymanager.configuration.client;

import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.Map;

@Service
public interface Configurator {
    public HttpClient.Builder createClient();
    public String createBody(Map<String, String> bodyParameters);
}
