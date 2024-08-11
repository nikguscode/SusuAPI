package com.nikguscode.SusuAPI.model.dao.config.discipline;

import com.nikguscode.SusuAPI.model.entities.Discipline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.nikguscode.SusuAPI.constants.ConfigConstants.*;

@Service
@Slf4j
public class JdbcDisciplineDao implements DisciplineDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcDisciplineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Discipline discipline) {
        String query = "INSERT INTO " + DISCIPLINE_TABLE + " (id, " + SUBJECT_NAME_DB + ", " + SUBJECT_ID_DB + ", "
                + STUDENT_GROUP_DB + ") VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, UUID.randomUUID(), discipline.getSubjectName(),
                discipline.getSubjectId(), discipline.getStudentGroup());
    }

    @Override
    public boolean isExists(String subjectId, String studentGroup) {
        String query = "SELECT count(*) FROM " + DISCIPLINE_TABLE + " WHERE id = (?) AND " + STUDENT_GROUP_DB + " = (?)";
        Integer countOfRecords = jdbcTemplate.queryForObject(query, Integer.class, subjectId);
        return countOfRecords != null && countOfRecords > 0;
    }

    @Override
    public Discipline get(String subjectId, String studentGroup) {
        String query = "SELECT (*) FROM " + DISCIPLINE_TABLE + " WHERE id = (?) AND " + STUDENT_GROUP_DB + " = (?)";

        try {
            return jdbcTemplate.queryForObject(query, Discipline.class, subjectId, studentGroup);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Subject with subjectId: {} and studentGroup: {} is not found", subjectId, studentGroup);
            return null;
        }
    }

//    @Override
//    void delete(UUID id) {
//
//    }
//
//    @Override
//    void delete(String subjectId) {
//
//    }
}
