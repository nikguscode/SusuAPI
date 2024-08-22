package com.nikguscode.SusuAPI.model.entities.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Parser {
    String id;
    String url;
    String dxCallbackVariable;
    String dxCallbackValue;
    String findPattern;
}
