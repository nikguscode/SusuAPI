package com.nikguscode.SusuAPIs.controller;

import com.nikguscode.SusuAPIs.model.entities.Student;
import com.nikguscode.SusuAPIs.model.service.AuthenticationService;
import com.nikguscode.SusuAPIs.model.service.parsers.ParserInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubjectPercentageController {
    private final AuthenticationService authService;
    private final ParserInterface parserInterface;

    public SubjectPercentageController(AuthenticationService authService,
                                       @Qualifier("subjectPercentageService") ParserInterface parserInterface) {
        this.authService = authService;
        this.parserInterface = parserInterface;
    }

    @PostMapping("/percentage")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handle(@RequestBody Student student) {
        String cookie = authService.getCookies(student);

        return parserInterface.execute(cookie, "https://studlk.susu.ru/ru/Checkout/RatingDetail");
    }
}
