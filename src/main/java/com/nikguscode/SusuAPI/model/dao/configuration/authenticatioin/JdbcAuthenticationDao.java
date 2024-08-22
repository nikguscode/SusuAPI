package com.nikguscode.SusuAPI.model.dao.configuration.authenticatioin;

import com.nikguscode.SusuAPI.model.entities.configuration.Authentication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;

@Service
public class JdbcAuthenticationDao implements AuthenticationDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAuthenticationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Authentication get() {
        String query = "SELECT * FROM " + AUTH_TABLE + " WHERE id = 1";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> new Authentication(
                rs.getLong("id"),
                rs.getString(CSRF_DB),
                rs.getString(USERNAME_DB),
                rs.getString(PASSWORD_DB),
                rs.getString(URL_DB)
        ));
    }
}
