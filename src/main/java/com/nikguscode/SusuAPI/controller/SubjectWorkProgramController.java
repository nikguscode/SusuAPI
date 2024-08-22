package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.dao.configuration.discipline.DisciplineDao;
import com.nikguscode.SusuAPI.model.dao.user.security.SecurityDao;
import com.nikguscode.SusuAPI.model.dao.user.studlk.StudlkDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Discipline;
import com.nikguscode.SusuAPI.model.entities.user.StudentInfo;
import com.nikguscode.SusuAPI.model.service.extractors.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.extractors.SubjectWorkProgramExtractor;
import com.nikguscode.SusuAPI.model.service.extractors.SubjectWorkTitleExtractor;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.AuthenticationRequest;
import com.nikguscode.SusuAPI.model.service.SecurityManager;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.SubjectWorkProgramRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class SubjectWorkProgramController {
    private final AuthenticationRequest authenticationRequest;
    private final SecurityManager securityManager;
    private final Map<String, Request> parsers;
    private final SubjectWorkProgramRequest subjectWorkProgramParser;
    private final StudlkDao studlkDao;
    private final DisciplineDao disciplineDao;
    private final SecurityDao securityDao;
    private final ExtractorByMatcher extractorByMatcher;
    private final SubjectWorkProgramExtractor subjectWorkProgramExtractor;
    private final SubjectWorkTitleExtractor subjectWorkTitleExtractor;

    @Autowired
    public SubjectWorkProgramController(AuthenticationRequest authenticationRequest,
                                        SecurityManager securityManager,
                                        Map<String, Request> parsers,
                                        SubjectWorkProgramRequest subjectWorkProgramParser,
                                        @Qualifier("jdbcStudlkDao") StudlkDao studlkDao,
                                        @Qualifier("jdbcSecurityDao") SecurityDao securityDao,
                                        @Qualifier("jdbcDisciplineDao") DisciplineDao disciplineDao,
                                        SubjectWorkProgramExtractor subjectWorkProgramExtractor,
                                        ExtractorByMatcher extractorByMatcher,
                                        SubjectWorkTitleExtractor subjectWorkTitleExtractor) {
        this.authenticationRequest = authenticationRequest;
        this.securityManager = securityManager;
        this.parsers = parsers;
        this.subjectWorkProgramParser = subjectWorkProgramParser;
        this.studlkDao = studlkDao;
        this.securityDao = securityDao;
        this.disciplineDao = disciplineDao;
        this.subjectWorkProgramExtractor = subjectWorkProgramExtractor;
        this.extractorByMatcher = extractorByMatcher;
        this.subjectWorkTitleExtractor = subjectWorkTitleExtractor;
    }

    private void getStudlkInfo(StudentDto studentDto, String cookie) {
        if (securityManager.apiAuthenticate(studentDto)) {
            UUID userId = securityDao.getUserId(studentDto.getUsername());
            studentDto.setId(userId);

            try {
                StudentInfo studentInfo = studlkDao.get(userId);
                studentDto.setStudentGroup(studentInfo.getStudentGroup());
            } catch (EmptyResultDataAccessException e) {
                String studentGroup = parsers.get("userInfoParser").send(cookie, "https://studlk.susu.ru/ru");
                studentDto.setStudentGroup(studentGroup);

                studlkDao.add(studentDto);
            }
        }
    }

    @PostMapping("/rpd")
    public ResponseEntity<String> handle(@RequestBody Map<String, String> json, StudentDto studentDto) {
        studentDto.setUsername(json.get("username"));
        studentDto.setPassword(json.get("password"));
        String cookie = authenticationRequest.getCookies(studentDto);

        if (json.containsKey("subject-id")) {
            String url = "https://studlk.susu.ru/ru/Reference/SubjectProgram/" + json.get("subject-id") + "?discType=RPD";
            getStudlkInfo(studentDto, cookie);

            try {
                Discipline discipline = disciplineDao.get(json.get("subject-id"));
                return ResponseEntity.ok(subjectWorkProgramExtractor.extract(discipline.getHtmlPage()));
            } catch (EmptyResultDataAccessException e) {
                String htmlPage = parsers.get("subjectWorkProgramRequest").send(cookie, url);
                disciplineDao.add(new Discipline(
                        UUID.randomUUID(),
                        subjectWorkTitleExtractor.extract(htmlPage, "(?<=РАБОЧАЯ ПРОГРАММА дисциплины\\s)(.*?)(?=\\sдля направления)"),
                        json.get("subject-id"),
                        studentDto.getStudentGroup(),
                        htmlPage
                ));

                return ResponseEntity.ok(subjectWorkProgramExtractor.extract(htmlPage));
            }
        } else if (json.containsKey("subject-url")) {
            return ResponseEntity.ok(parsers.get("subjectWorkProgramParser").send(cookie, json.get("subject-url")));
        } else if (json.containsKey("subject-name")) {
            return null;
            //return parserInterface.execute(cookie, )
        } else {
            return ResponseEntity.badRequest().body("Parameter requests are incorrect, check out the documentation " +
                    "at the link: https://github.com/nikguscode/Susu_API");
        }
    }

}
