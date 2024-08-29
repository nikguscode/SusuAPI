package com.nikguscode.SusuAPI.model.dao.configuration.parser;

import com.nikguscode.SusuAPI.model.entities.configuration.Parser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;

@Service
public class JdbcParserDao implements ParserDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcParserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Parser get(String id) {
        String query = "SELECT * FROM " + PARSER_TABLE_DB + " WHERE id = (?)";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> new Parser(
                id,
                rs.getString("url_var"),
                UUID.fromString(rs.getString("regex_id")),
                rs.getString("dx_callback_var"),
                rs.getString("dx_callback_val")
        ), id);
    }
}
