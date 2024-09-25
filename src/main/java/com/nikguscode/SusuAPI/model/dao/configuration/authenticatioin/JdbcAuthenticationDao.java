package com.nikguscode.SusuAPI.model.dao.configuration.authenticatioin;

import com.nikguscode.SusuAPI.model.entities.configuration.Authentication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class JdbcAuthenticationDao implements AuthenticationDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAuthenticationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Authentication get() {
        String query = "SELECT * FROM config.auth WHERE id = 1";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> new Authentication(
                rs.getString("id"),
                UUID.fromString(rs.getString("variable_id")),
                rs.getString("url")
        ));
    }
}
