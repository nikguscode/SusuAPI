package com.nikguscode.SusuAPI.model.repositories;

import com.nikguscode.SusuAPI.model.repositories.SelectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationVariables {
    private final SelectRepository selectRepository;

    @Autowired
    public AuthenticationVariables(SelectRepository selectRepository) {
        this.selectRepository = selectRepository;
    }

    public Map<String, String> getVariables() {
        Map<String, Object> query = selectRepository.executeSelectQuery();
        Map<String, String> variables = new HashMap<>();

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            variables.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        return variables;
    }
}
