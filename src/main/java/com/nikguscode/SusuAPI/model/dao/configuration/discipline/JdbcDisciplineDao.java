package com.nikguscode.SusuAPI.model.dao.configuration.discipline;

import com.nikguscode.SusuAPI.model.entities.configuration.Discipline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;

@Service
@Slf4j
public class JdbcDisciplineDao implements DisciplineDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcDisciplineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Discipline discipline) {
        String query = "INSERT INTO " + DISCIPLINE_TABLE + " (id, " + SUBJECT_NAME_DB + ", " + SUBJECT_ID_DB + ", "
                + STUDENT_GROUP_DB + ", " + HTML_PAGE_DB + ") VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, UUID.randomUUID(), discipline.getSubjectName(),
                discipline.getSubjectId(), discipline.getStudentGroup(), discipline.getHtmlPage());
    }

    @Override
    public Discipline get(String subjectId) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM " + DISCIPLINE_TABLE + " WHERE subject_id = (?)";
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
        String query = "SELECT (*) FROM " + DISCIPLINE_TABLE + " WHERE " + SUBJECT_NAME_DB + " = (?) AND "
                + STUDENT_GROUP_DB + " = (?)";
        return jdbcTemplate.queryForObject(query, Discipline.class, subjectName, studentGroup);
    }
}