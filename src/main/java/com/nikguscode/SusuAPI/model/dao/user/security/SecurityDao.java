package com.nikguscode.SusuAPI.model.dao.user.security;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.entities.user.StudentSecurity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecurityDao {
    void addUser(StudentSecurity studentSecurity);
    void deleteUser(UUID id);
    StudentSecurity getUser(StudentDto studentDto);
    UUID getUserId(String username);
}
