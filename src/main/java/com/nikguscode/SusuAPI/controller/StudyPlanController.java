package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.model.entities.Student;
import com.nikguscode.SusuAPI.model.service.AuthenticationService;
import com.nikguscode.SusuAPI.model.service.parsers.ParserInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudyPlanController {
    private final AuthenticationService authService;
    private final ParserInterface parserInterface;

    public StudyPlanController(AuthenticationService authService,
                                       @Qualifier("studyPlanParser") ParserInterface parserInterface) {
        this.authService = authService;
        this.parserInterface = parserInterface;
    }

    @PostMapping("/study-plan")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handle(@RequestBody Student student) {
        String cookie = authService.getCookies(student);
        return parserInterface.execute(cookie, "https://studlk.susu.ru/ru/StudyPlan/StudyPlanGridPartialCustom");
    }

}
