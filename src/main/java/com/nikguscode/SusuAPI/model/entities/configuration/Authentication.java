package com.nikguscode.SusuAPI.model.entities.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Authentication {
    long id;
    String csrfVariable;
    String usernameVariable;
    String passwordVariable;
    String urlVariable;
}
