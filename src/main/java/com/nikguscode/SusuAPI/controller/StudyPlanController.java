package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.AuthenticationRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudyPlanController {
    private final AuthenticationRequest authenticationRequest;
    private final Request request;

    public StudyPlanController(AuthenticationRequest authenticationRequest,
                               @Qualifier("studyPlanRequest") Request request) {
        this.authenticationRequest = authenticationRequest;
        this.request = request;
    }

    @PostMapping("/study-plan")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handle(@RequestBody StudentDto studentDto) {
        String cookie = authenticationRequest.getCookies(studentDto);
        return request.send(cookie, "https://studlk.susu.ru/ru/StudyPlan/StudyPlanGridPartialCustom");
    }

}
