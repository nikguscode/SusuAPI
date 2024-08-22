package com.nikguscode.SusuAPI.model.dao.configuration.discipline;

import com.nikguscode.SusuAPI.model.entities.configuration.Discipline;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DisciplineDao {
    void add(Discipline discipline);
    Discipline get(String subjectId);
    Discipline get(String subjectName, String studentGroup);
}
