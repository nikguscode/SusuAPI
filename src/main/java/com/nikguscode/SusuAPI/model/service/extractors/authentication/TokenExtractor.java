package com.nikguscode.SusuAPI.model.service.extractors.authentication;

import org.springframework.stereotype.Service;

import java.net.http.HttpClient;

@Service
public interface TokenExtractor {
    String extract(HttpClient client);
}
