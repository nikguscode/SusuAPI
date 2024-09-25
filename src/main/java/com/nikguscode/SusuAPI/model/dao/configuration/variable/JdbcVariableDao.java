package com.nikguscode.SusuAPI.model.dao.configuration.variable;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JdbcVariableDao implements VariableDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcVariableDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, String> get(UUID entityId) {
        String query = "SELECT * FROM config.variable WHERE id = (?)";
        return jdbcTemplate.query(query, rs -> {
            Map<String, String> variables = new HashMap<>();

            while (rs.next()) {
                variables.put(rs.getString("variable_key"), rs.getString("variable_value"));
            }

            return variables;
        }, entityId);
    }
}
