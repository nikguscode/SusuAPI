package com.nikguscode.SusuAPI.model.entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class StudentInfo {
    private UUID id;
    private String studentGroup;
    private LocalDate parsedInfoDate;
}
