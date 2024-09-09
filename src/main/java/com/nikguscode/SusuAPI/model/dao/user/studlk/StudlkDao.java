package com.nikguscode.SusuAPI.model.dao.user.studlk;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.model.entities.user.StudentInfo;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface StudlkDao {
    void add(StudentDto studentDto);
    void delete(UUID id);
    StudentInfo get(UUID id);
}
