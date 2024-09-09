package com.nikguscode.SusuAPI.model.dao.configuration.authenticatioin;

import com.nikguscode.SusuAPI.model.entities.configuration.Authentication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class JdbcAuthenticationDao implements AuthenticationDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAuthenticationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Authentication get() {
        String query = "SELECT * FROM config.auth WHERE id = 1";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> new Authentication(
                rs.getLong("id"),
                rs.getString("csrf_var"),
                rs.getString("username_var"),
                rs.getString("password_var"),
                rs.getString("url_var")
        ));
    }
}
