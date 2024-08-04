package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.model.entities.Student;
import com.nikguscode.SusuAPI.model.service.AuthenticationService;
import com.nikguscode.SusuAPI.model.service.parsers.ParserInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubjectGradeController {
    private final AuthenticationService authenticationService;
    private final ParserInterface parserInterface;

    public SubjectGradeController(AuthenticationService authenticationService,
                                   @Qualifier("subjectGradeParser") ParserInterface parserInterface) {
        this.authenticationService = authenticationService;
        this.parserInterface = parserInterface;
    }

    @PostMapping("/grades")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handle(@RequestBody Student student) {
        String cookie = authenticationService.getCookies(student);
        return parserInterface.execute(cookie, "https://studlk.susu.ru/ru/Checkout/CheckoutPartialCustom");
    }
}
