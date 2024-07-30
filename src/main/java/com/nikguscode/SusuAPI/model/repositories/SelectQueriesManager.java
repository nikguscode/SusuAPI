package com.nikguscode.SusuAPI.model.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import static com.nikguscode.SusuAPI.model.repositories.SelectConstants.*;

@Service
public class SelectQueriesManager {
    private final JdbcTemplate jdbcTemplate;
    private final String authenticationSelectQuery =
            "SELECT * FROM " + AUTH_TABLE + " WHERE id = ?";
    private final String parserSelectQuery =
            "SELECT * FROM " + VARIABLES_TABLE + " WHERE id = ?";

    public SelectQueriesManager(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, String> executeSelectQuery(String parserId) {
        Map<String, Object> query = jdbcTemplate.queryForMap(parserSelectQuery, parserId);
        Map<String, String> variables = new HashMap<>();

        for (Map.Entry<String, Object> entry : query.entrySet()) {
            variables.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        return variables;
    }

    public Map<String, Object> executeSelectQuery() {
        return jdbcTemplate.queryForMap(authenticationSelectQuery, 1);
    }
}
