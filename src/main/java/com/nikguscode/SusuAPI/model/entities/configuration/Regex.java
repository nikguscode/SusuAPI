package com.nikguscode.SusuAPI.model.entities.configuration;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Regex {
    UUID id;
    String pattern;
    String patternId;
    UUID entityId;
}
