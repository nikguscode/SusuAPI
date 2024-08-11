package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.service.AuthenticationService;
import com.nikguscode.SusuAPI.model.service.SecurityManager;
import com.nikguscode.SusuAPI.model.service.parsers.ParserInterface;
import com.nikguscode.SusuAPI.model.service.parsers.UserInfoParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SubjectWorkProgramController {
    private final AuthenticationService authenticationService;
    private final SecurityManager securityManager;
    private final ParserInterface parserInterface;

    public SubjectWorkProgramController(AuthenticationService authenticationService,
                                        SecurityManager securityManager,
                                        @Qualifier("subjectWorkProgramParser") ParserInterface parserInterface) {
        this.authenticationService = authenticationService;
        this.securityManager = securityManager;
        this.parserInterface = parserInterface;
    }

    @PostMapping("/rpd")
    public ResponseEntity<String> handle(@RequestBody Map<String, String> json, StudentDto studentDto, UserInfoParser userInfoParser) {
        studentDto.setUsername(json.get("username"));
        studentDto.setPassword(json.get("password"));
        String cookie = authenticationService.getCookies(studentDto);

        if (securityManager.apiAuthenticate(studentDto)) {

        } else if (!securityManager.apiAuthenticate(studentDto)) {
            String studentGroup = userInfoParser.execute(cookie, "https://studlk.susu.ru/ru");
        }

        if (json.containsKey("subject-id")) {
            String url =
                    "https://studlk.susu.ru/ru/Reference/SubjectProgram/"
                    + json.get("subject-id")
                    + "?discType=RPD";

            return ResponseEntity.ok(parserInterface.execute(cookie, url));
        } else if (json.containsKey("subject-url")) {
            return ResponseEntity.ok(parserInterface.execute(cookie, json.get("subject-url")));
        } else if (json.containsKey("subject-name")) {
            return null;
            //return parserInterface.execute(cookie, )
        } else {
            return ResponseEntity.badRequest().body("Parameter requests are incorrect, check out the documentation " +
                    "at the link: https://github.com/nikguscode/Susu_API");
        }
    }

}
