package com.nikguscode.SusuAPI.model.entities.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Discipline {
    private UUID id;
    private String subjectName;
    private String subjectId;
    private String studentGroup;
    private String htmlPage;
}
