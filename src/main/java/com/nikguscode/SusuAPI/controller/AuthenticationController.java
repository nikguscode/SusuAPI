package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.StudlkAuthenticationBaseRequest;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuthenticationController {
    private final StudlkAuthenticationBaseRequest studlkAuthenticationRequest;

    public AuthenticationController(StudlkAuthenticationBaseRequest studlkAuthenticationRequest) {
        this.studlkAuthenticationRequest = studlkAuthenticationRequest;
    }

    @PostMapping("/authentication")
    @ResponseStatus(HttpStatus.CREATED)
    public String handle(@RequestBody StudentDto studentDto) {
        return studlkAuthenticationRequest.send(studentDto);
    }
}
