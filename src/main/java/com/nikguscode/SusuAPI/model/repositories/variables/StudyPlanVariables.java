package com.nikguscode.SusuAPI.model.repositories.variables;

import com.nikguscode.SusuAPI.model.repositories.DBVariablesQueries;
import com.nikguscode.SusuAPI.model.repositories.VariableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.nikguscode.SusuAPI.model.repositories.DBVariablesConstants.*;
import static com.nikguscode.SusuAPI.model.repositories.DBVariablesConstants.C_GRADE_TABLE;

@Service
public class StudyPlanVariables extends DBVariablesQueries implements VariableMapper {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudyPlanVariables(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, String> getVariables() {
        Map<String, String> variables = new HashMap<>();

        variables.put(URL_VAR, jdbcTemplate.queryForObject(
                super.createSelectQuery(URL_DB, C_GRADE_TABLE), String.class));
        variables.put(FIND_PATTERN, jdbcTemplate.queryForObject(
                super.createSelectQuery(FIND_PATTERN_DB, C_GRADE_TABLE), String.class));
        variables.put(DX_CALLBACK_VAR, jdbcTemplate.queryForObject(
                super.createSelectQuery(DX_CALLBACK_DB, C_GRADE_TABLE), String.class));
        variables.put(DX_CALLBACK_VAL, jdbcTemplate.queryForObject(
                super.createSelectQuery(DX_CALLBACK_VALUE_DB, C_GRADE_TABLE), String.class));

        return variables;
    }
}
