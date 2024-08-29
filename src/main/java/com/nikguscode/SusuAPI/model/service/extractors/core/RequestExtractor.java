package com.nikguscode.SusuAPI.model.service.extractors.core;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RequestExtractor {
    String extract(String dataForExtract, Map<String, String> regex, String callingClass);
}
