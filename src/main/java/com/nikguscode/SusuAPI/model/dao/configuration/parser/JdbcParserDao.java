package com.nikguscode.SusuAPI.model.dao.configuration.parser;

import com.nikguscode.SusuAPI.model.entities.configuration.Parser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import static com.nikguscode.SusuAPI.constants.ConfigConstants.*;

@Service
public class JdbcParserDao implements ParserDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcParserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Parser get(String id) {
        String query = "SELECT * FROM " +  PARSER_TABLE + " WHERE id = (?)";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> new Parser(
                id,
                rs.getString("url_var"),
                rs.getString("dx_callback_var"),
                rs.getString("dx_callback_val"),
                rs.getString("find_pattern")
        ), id);
    }
}
