package com.nikguscode.SusuAPI.dto.iternal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StudentDto {
    private UUID id;
    private String username;
    private String password;
    private String cookie;
    private String studentGroup;
}
