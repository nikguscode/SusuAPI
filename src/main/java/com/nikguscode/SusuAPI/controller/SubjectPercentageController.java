package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.service.AuthenticationService;
import com.nikguscode.SusuAPI.model.service.parsers.ParserInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubjectPercentageController {
    private final AuthenticationService authenticationService;
    private final ParserInterface parserInterface;

    public SubjectPercentageController(AuthenticationService authenticationService,
                                       @Qualifier("subjectPercentageParser") ParserInterface parserInterface) {
        this.authenticationService = authenticationService;
        this.parserInterface = parserInterface;
    }

    @PostMapping("/percentage")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handle(@RequestBody StudentDto studentDto) {
        String cookie = authenticationService.getCookies(studentDto);
        return parserInterface.execute(cookie, "https://studlk.susu.ru/ru/Checkout/RatingDetail");
    }
}
