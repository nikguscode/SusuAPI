package com.nikguscode.SusuAPI.model.dao.config.discipline;

import com.nikguscode.SusuAPI.model.entities.Discipline;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DisciplineDao {
    void create(Discipline discipline);
    boolean isExists(String subjectId, String studentGroup);
    Discipline get(String subjectId, String studentGroup);
//    void delete(UUID id);
//    void delete(String subjectId);
}
