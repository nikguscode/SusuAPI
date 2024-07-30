package com.nikguscode.SusuAPI.model.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationVariablesManager {
    private final SelectQueriesManager selectQueriesManager;

    @Autowired
    public AuthenticationVariablesManager(SelectQueriesManager selectQueriesManager) {
        this.selectQueriesManager = selectQueriesManager;
    }

    public Map<String, String> getVariables() {
        Map<String, Object> query = selectQueriesManager.executeSelectQuery();
        Map<String, String> variables = new HashMap<>();

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            variables.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        return variables;
    }
}
