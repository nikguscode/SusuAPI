package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.model.entities.Student;
import com.nikguscode.SusuAPI.model.service.AuthenticationService;
import com.nikguscode.SusuAPI.model.service.parsers.ParserInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SubjectWorkProgramController {
    private final AuthenticationService authenticationService;
    private final ParserInterface parserInterface;

    public SubjectWorkProgramController(AuthenticationService authenticationService,
                                        @Qualifier("subjectWorkProgramParser") ParserInterface parserInterface) {
        this.authenticationService = authenticationService;
        this.parserInterface = parserInterface;
    }

    @PostMapping("/rpd")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handle(@RequestBody Map<String, String> json, Student student) {
        student.setUsername(json.get("username"));
        student.setPassword(json.get("password"));

        String cookie = authenticationService.getCookies(student);
        return parserInterface.execute(cookie, "https://studlk.susu.ru/ru/Reference/SubjectProgram/"
                + json.get("subject-id")
                + "?discType=RPD");
    }

}
