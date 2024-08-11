package com.nikguscode.SusuAPI.model.service;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.dao.user.security.SecurityDao;
import com.nikguscode.SusuAPI.model.entities.StudentSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

import static com.nikguscode.SusuAPI.enums.UserAccess.*;
import static com.nikguscode.SusuAPI.constants.StudentConstants.*;

@Service
@Slf4j
public class SecurityManager {
    private final SecurityDao securityDao;

    public SecurityManager(SecurityDao securityDao) {
        this.securityDao = securityDao;
    }

    private byte[] createSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        return salt;
    }

    private String createHash(byte[] salt, String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(CODING_ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private void addUser(StudentDto studentDto) {
        String username = studentDto.getUsername();
        String password = studentDto.getPassword();
        byte[] salt = createSalt();
        String hash = createHash(salt, password);

        var user = new StudentSecurity(
                UUID.randomUUID(), username, hash, STANDARD, Base64.getEncoder().encodeToString(salt)
        );

        securityDao.addUser(user);
        log.info("User: {} successfully added in database", username);
    }

    private boolean isPasswordCorrect(StudentDto studentDto) {
        StudentSecurity studentSecurity = securityDao.getUser(studentDto);
        byte[] salt = Base64.getDecoder().decode(studentSecurity.getSalt());
        return studentSecurity.getHash().equals(createHash(salt, studentDto.getPassword()));
    }

    private UUID isUserExists(String username) {
        return securityDao.getUserId(username);
    }

    public boolean apiAuthenticate(StudentDto studentDto) {
        if (isUserExists(studentDto.getUsername()) != null) {
            log.info("User {} exists in the system", studentDto.getUsername());

            if (isPasswordCorrect(studentDto)) {
                log.info("User {} successfully authenticated", studentDto.getUsername());
                return true;
            } else {
                log.info("User {} failed authentication: incorrect password", studentDto.getUsername());
                return false;
            }
        } else {
            addUser(studentDto);
            log.info("New user {} added to the system and authenticated", studentDto.getUsername());
            return true;
        }
    }
}
