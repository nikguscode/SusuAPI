package com.nikguscode.SusuAPI.model.entities.configuration;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Variable {
    UUID id;
    String variableKey;
    String variableValue;
    UUID entityId;
}
