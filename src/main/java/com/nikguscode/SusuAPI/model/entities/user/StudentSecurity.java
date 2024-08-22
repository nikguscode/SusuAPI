package com.nikguscode.SusuAPI.model.entities.user;

import com.nikguscode.SusuAPI.enumirations.UserAccess;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StudentSecurity {
    public StudentSecurity(UUID id, String username, String hash, UserAccess userAccess, String salt) {
        this.id = id;
        this.username = username;
        this.hash = hash;
        this.userAccess = userAccess;
        this.salt = salt;
    }

    private UUID id;
    private String username;
    private String hash;
    private UserAccess userAccess;
    private String salt;
}
