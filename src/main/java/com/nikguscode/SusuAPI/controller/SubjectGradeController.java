package com.nikguscode.SusuAPIs.controller;

import com.nikguscode.SusuAPIs.model.entities.Student;
import com.nikguscode.SusuAPIs.model.service.AuthenticationService;
import com.nikguscode.SusuAPIs.model.service.parsers.ParserInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubjectGradeController {
    private final AuthenticationService authService;
    private final ParserInterface parserInterface;

    private SubjectGradeController(AuthenticationService authService,
                                   @Qualifier("subjectGradeService") ParserInterface parserInterface) {
        this.authService = authService;
        this.parserInterface = parserInterface;
    }

    @PostMapping("/grades")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handle(@RequestBody Student student) {
        String cookie = authService.getCookies(student);

        return parserInterface.execute(cookie, "https://studlk.susu.ru/ru/Checkout/CheckoutPartialCustom");
    }
}
