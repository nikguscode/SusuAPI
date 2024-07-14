package com.nikguscode.SusuAPIs.model.repositories;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface VariableMapper {
    Map<String, String> getVariables();
}
