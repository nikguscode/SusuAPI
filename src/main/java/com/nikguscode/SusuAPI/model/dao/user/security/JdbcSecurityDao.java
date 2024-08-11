package com.nikguscode.SusuAPI.model.dao.user.security;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.enums.UserAccess;
import com.nikguscode.SusuAPI.model.entities.StudentSecurity;

import static com.nikguscode.SusuAPI.constants.StudentConstants.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class JdbcSecurityDao implements SecurityDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcSecurityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(StudentSecurity studentSecurity) {
        String query = "INSERT INTO " + USER_SECURITY_TABLE + " (id, " + USERNAME_DB + ", " + HASH_DB + ", " +
                USER_ACCESS_DB + ", " + SALT_DB + ") VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                query,
                UUID.randomUUID(), studentSecurity.getUsername(), studentSecurity.getHash(), studentSecurity.getUserAccess().toString(), studentSecurity.getSalt()
        );
    }

    @Override
    public void deleteUser(UUID id) {
        String query = "DELETE FROM " + USER_SECURITY_TABLE + " WHERE id = (?)";
        jdbcTemplate.update(query, id);
    }

    @Override
    public void updateUser(StudentSecurity studentSecurity) {
        //String query = "UPDATE FROM " + USER_SECURITY_TABLE + " "
    }

    @Override
    public StudentSecurity getUser(StudentDto studentDto) {
        UUID id = getUserId(studentDto.getUsername());
        String query = "SELECT * FROM " + USER_SECURITY_TABLE + " WHERE id = (?)";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            UUID studentId = UUID.fromString(rs.getString("id"));
            String username = rs.getString("username");
            String hash = rs.getString("hash");
            UserAccess userAccess = UserAccess.valueOf(rs.getString("user_access"));
            String salt = rs.getString("salt");
            return new StudentSecurity(studentId, username, hash, userAccess, salt);
        }, id);
    }

    @Override
    public UUID getUserId(String username) {
        String query = "SELECT id FROM " + USER_SECURITY_TABLE + " WHERE " + USERNAME_DB + " = (?)";
        log.info(query);

        try {
            return jdbcTemplate.queryForObject(query, UUID.class, username);
        } catch (EmptyResultDataAccessException e) {
            log.info("User not found: {}", username);
            return null;
        }
    }

}
