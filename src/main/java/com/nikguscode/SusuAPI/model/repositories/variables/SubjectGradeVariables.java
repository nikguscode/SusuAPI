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
public class SubjectGradeVariables extends DBVariablesQueries implements VariableMapper {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SubjectGradeVariables(JdbcTemplate jdbcTemplate) {
        super(SUBJECT_GRADE_ROW);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, String> getVariables() {
        Map<String, String> variables = new HashMap<>();

        variables.put(URL_VAR, jdbcTemplate.queryForObject(
                super.executeSelectQuery(URL_DB), String.class));
        variables.put(FIND_PATTERN, jdbcTemplate.queryForObject(
                super.executeSelectQuery(FIND_PATTERN_DB), String.class));
        variables.put(DX_CALLBACK_VAR, jdbcTemplate.queryForObject(
                super.executeSelectQuery(DX_CALLBACK_DB), String.class));
        variables.put(DX_CALLBACK_VAL, jdbcTemplate.queryForObject(
                super.executeSelectQuery(DX_CALLBACK_VALUE_DB), String.class));

        return variables;
    }
}
