package com.nikguscode.SusuAPI.model.dao.user.security;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.enumirations.UserAccess;
import com.nikguscode.SusuAPI.model.entities.user.StudentSecurity;


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
        String query = "INSERT INTO \"user\".user_security (id, username, hash, user_access, salt) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                query,
                UUID.randomUUID(), studentSecurity.getUsername(), studentSecurity.getHash(), studentSecurity.getUserAccess().toString(), studentSecurity.getSalt()
        );
    }

    @Override
    public void deleteUser(UUID id) {
        String query = "DELETE FROM \"user\".user_security WHERE id = (?)";
        jdbcTemplate.update(query, id);
    }

    @Override
    public StudentSecurity getUser(StudentDto studentDto) {
        UUID id = getUserId(studentDto.getUsername());
        String query = "SELECT * FROM \"user\".user_security WHERE id = (?)";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            String username = rs.getString("username");
            String hash = rs.getString("hash");
            UserAccess userAccess = UserAccess.valueOf(rs.getString("user_access"));
            String salt = rs.getString("salt");
            return new StudentSecurity(id, username, hash, userAccess, salt);
        }, id);
    }

    @Override
    public UUID getUserId(String username) {
        String query = "SELECT id FROM \"user\".user_security WHERE username = (?)";
        log.info(query);

        try {
            return jdbcTemplate.queryForObject(query, UUID.class, username);
        } catch (EmptyResultDataAccessException e) {
            log.info("User not found: {}", username);
            return null;
        }
    }

}
