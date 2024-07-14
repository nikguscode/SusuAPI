package com.nikguscode.SusuAPIs.model.repositories.variables;

import static com.nikguscode.SusuAPIs.model.repositories.DBConstants.*;
import com.nikguscode.SusuAPIs.model.repositories.DBQueries;
import com.nikguscode.SusuAPIs.model.repositories.VariableMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SubjectGradeVariables extends DBQueries implements VariableMapper {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SubjectGradeVariables(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, String> getVariables() {
        Map<String, String> variables = new HashMap<>();

        variables.put(URL_VAR, jdbcTemplate.queryForObject(
                super.createSelectQuery(URL_DB, C_GRADE_TABLE), String.class));
        variables.put(FIND_PATTERN, jdbcTemplate.queryForObject(
                super.createSelectQuery(FIND_PATTERN_DB, C_GRADE_TABLE), String.class));

        return variables;
    }
}
