package com.nikguscode.SusuAPI.model.dao.configuration.regex;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JdbcRegexDao implements RegexDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcRegexDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, String> get(UUID id) {
        String query = "SELECT * FROM config.regex WHERE entity_id = (?)";
        return jdbcTemplate.query(query, rs -> {
            Map<String, String> map = new HashMap<>();

            while(rs.next()) {
                map.put(rs.getString("pattern_id"), rs.getString("pattern"));
            }

            return map;
        }, id);
    }
}
