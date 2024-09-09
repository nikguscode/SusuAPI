package com.nikguscode.SusuAPI.model.dao.configuration.extractor;

import com.nikguscode.SusuAPI.model.entities.configuration.Extractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JdbcExtractorDao implements ExtractorDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcExtractorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Extractor get(String id) {
        String query = "SELECT * FROM config.extractor WHERE id = (?)";
        return jdbcTemplate.queryForObject(query, Extractor.class, id);
    }
}
