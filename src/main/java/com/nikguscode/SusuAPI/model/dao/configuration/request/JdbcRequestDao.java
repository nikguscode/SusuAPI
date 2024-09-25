package com.nikguscode.SusuAPI.model.dao.configuration.request;

import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JdbcRequestDao implements RequestDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcRequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Request get(String id) {
        String query = "SELECT * FROM config.request WHERE id = (?)";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> new Request(
                id,
                UUID.fromString(rs.getString("entity_id")),
                rs.getString("url")
        ), id);
    }
}
