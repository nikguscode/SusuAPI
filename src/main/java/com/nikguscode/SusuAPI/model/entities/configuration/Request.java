package com.nikguscode.SusuAPI.model.entities.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Request {
    String id;
    String url;
    UUID regexId;
    String dxCallbackVariable;
    String dxCallbackValue;
}
