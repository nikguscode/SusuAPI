package com.nikguscode.SusuAPI.model.entities.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Authentication {
    String id;
    UUID variableId;
    String url;
}
