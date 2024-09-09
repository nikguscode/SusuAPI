package com.nikguscode.SusuAPI.model.dao.user.studlk;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.model.entities.user.StudentInfo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;


@Service
public class JdbcStudlkDao implements StudlkDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public JdbcStudlkDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                         JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(StudentDto studentDto) {
        String query = "INSERT INTO \"user\".studlk_info (id, student_group, parsed_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, studentDto.getId(), studentDto.getStudentGroup(), LocalDate.now());
    }

    @Override
    public void delete(UUID id) {
        String query = "DELETE FROM \"user\".studlk_info WHERE id = (?)";
        jdbcTemplate.update(query, id);
    }

    @Override
    public StudentInfo get(UUID id) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM \"user\".studlk_info WHERE id = (?)";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            String studentGroup = rs.getString("student_group");
            LocalDate parsedInfoDate = rs.getDate("parsed_at").toLocalDate();
            return new StudentInfo(id, studentGroup, parsedInfoDate);
        }, id);
    }
}
