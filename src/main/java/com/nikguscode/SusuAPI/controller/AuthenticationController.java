package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.service.AuthenticationService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authentication")
    @ResponseStatus(HttpStatus.CREATED)
    public String handle(@RequestBody StudentDto studentDto) {
        return authenticationService.getCookies(studentDto);
    }
}
