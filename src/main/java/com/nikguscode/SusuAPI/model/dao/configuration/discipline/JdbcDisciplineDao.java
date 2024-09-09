package com.nikguscode.SusuAPI.model.dao.configuration.discipline;

import com.nikguscode.SusuAPI.model.entities.configuration.Discipline;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Log4j2
public class JdbcDisciplineDao implements DisciplineDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcDisciplineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Discipline discipline) {
        String query = "INSERT INTO config.discipline (id, subject_name, subject_id, student_group, html_page) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, UUID.randomUUID(), discipline.getSubjectName(),
                discipline.getSubjectId(), discipline.getStudentGroup(), discipline.getHtmlPage());
    }

    @Override
    public Discipline get(String subjectId) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM config.discipline WHERE subject_id = (?)";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> new Discipline(
                UUID.fromString(rs.getString("id")),
                rs.getString("subject_name"),
                rs.getString("subject_id"),
                rs.getString("student_group"),
                rs.getString("html_page")
        ), subjectId);
    }

    @Override
    public Discipline get(String subjectName, String studentGroup) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM config.discipline WHERE subject_name = (?) AND student_group = (?)";
        return jdbcTemplate.queryForObject(query, Discipline.class, subjectName, studentGroup);
    }
}