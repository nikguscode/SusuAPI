package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.AuthenticationRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubjectGradeController {
    private final AuthenticationRequest authenticationRequest;
    private final Request request;

    public SubjectGradeController(AuthenticationRequest authenticationRequest,
                                  @Qualifier("subjectGradeRequest") Request request) {
        this.authenticationRequest = authenticationRequest;
        this.request = request;
    }

    @PostMapping("/grades")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handle(@RequestBody StudentDto studentDto) {
        String cookie = authenticationRequest.getCookies(studentDto);
        return request.send(cookie, "https://studlk.susu.ru/ru/Checkout/CheckoutPartialCustom");
    }
}
