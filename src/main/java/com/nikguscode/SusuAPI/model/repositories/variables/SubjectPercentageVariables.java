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
public class SubjectPercentageVariables extends DBVariablesQueries implements VariableMapper {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SubjectPercentageVariables(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, String> getVariables() {
        Map<String, String> variables = new HashMap<>();

        variables.put(URL_VAR, jdbcTemplate.queryForObject(
                super.createSelectQuery(URL_DB, C_PERCENTAGE_TABLE), String.class));
        variables.put(FIND_PATTERN, jdbcTemplate.queryForObject(
                super.createSelectQuery(FIND_PATTERN_DB, C_PERCENTAGE_TABLE), String.class));

        return variables;
    }
}
