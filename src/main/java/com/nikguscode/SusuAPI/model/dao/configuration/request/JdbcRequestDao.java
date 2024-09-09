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
                rs.getString("url_var"),
                UUID.fromString(rs.getString("regex_id")),
                rs.getString("dx_callback_var"),
                rs.getString("dx_callback_val")
        ), id);
    }
}
