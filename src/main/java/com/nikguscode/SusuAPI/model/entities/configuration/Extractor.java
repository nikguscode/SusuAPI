package com.nikguscode.SusuAPI.model.entities.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Getter
@Setter
public class Extractor {
    private String id;
    private UUID regexId;
    private String description;
}