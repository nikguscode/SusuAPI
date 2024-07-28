package com.nikguscode.SusuAPI.model.repositories.variables;

import static com.nikguscode.SusuAPI.model.repositories.DBVariablesConstants.*;
import com.nikguscode.SusuAPI.model.repositories.DBVariablesQueries;
import com.nikguscode.SusuAPI.model.repositories.VariableMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationVariables extends DBVariablesQueries implements VariableMapper {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthenticationVariables(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, String> getVariables() {
        Map<String, String> variables = new HashMap<>();

        variables.put(CSRF_VAR, jdbcTemplate.queryForObject(
                super.executeSelectQuery(CSRF_DB, AUTH_TABLE), String.class));
        variables.put(USERNAME_VAR, jdbcTemplate.queryForObject(
                super.executeSelectQuery(USERNAME_DB, AUTH_TABLE), String.class));
        variables.put(PASSWORD_VAR, jdbcTemplate.queryForObject(
                super.executeSelectQuery(PASSWORD_DB, AUTH_TABLE), String.class));
        variables.put(URL_VAR, jdbcTemplate.queryForObject(
                super.executeSelectQuery(URL_DB, AUTH_TABLE), String.class));

        return variables;
    }
}
