package com.nikguscode.SusuAPI.model.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Discipline {
    private UUID id;
    private String subjectName;
    private String subjectId;
    private String studentGroup;
}
