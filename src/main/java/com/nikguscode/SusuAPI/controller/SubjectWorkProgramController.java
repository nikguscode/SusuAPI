package com.nikguscode.SusuAPI.controller;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;
import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.dao.configuration.discipline.DisciplineDao;
import com.nikguscode.SusuAPI.model.dao.configuration.parser.ParserDao;
import com.nikguscode.SusuAPI.model.dao.configuration.regex.RegexDao;
import com.nikguscode.SusuAPI.model.dao.user.security.SecurityDao;
import com.nikguscode.SusuAPI.model.dao.user.studlk.StudlkDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Discipline;
import com.nikguscode.SusuAPI.model.entities.configuration.Parser;
import com.nikguscode.SusuAPI.model.entities.user.StudentInfo;
import com.nikguscode.SusuAPI.model.service.extractors.core.RequestExtractor;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.AuthenticationRequest;
import com.nikguscode.SusuAPI.model.service.SecurityManager;
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
    private final Map<String, Request> requests;
    private final Map<String, RequestExtractor> extractors;
    private final ParserDao parserDao;
    private final RegexDao regexDao;
    private final StudlkDao studlkDao;
    private final DisciplineDao disciplineDao;
    private final SecurityDao securityDao;
    private final ExtractorByMatcher extractorByMatcher;

    @Autowired
    public SubjectWorkProgramController(AuthenticationRequest authenticationRequest,
                                        SecurityManager securityManager,
                                        Map<String, Request> requests,
                                        Map<String, RequestExtractor> extractors,
                                        @Qualifier("jdbcParserDao") ParserDao parserDao,
                                        @Qualifier("jdbcRegexDao") RegexDao regexDao,
                                        @Qualifier("jdbcStudlkDao") StudlkDao studlkDao,
                                        @Qualifier("jdbcSecurityDao") SecurityDao securityDao,
                                        @Qualifier("jdbcDisciplineDao") DisciplineDao disciplineDao,
                                        ExtractorByMatcher extractorByMatcher) {
        this.authenticationRequest = authenticationRequest;
        this.securityManager = securityManager;
        this.requests = requests;
        this.extractors = extractors;
        this.parserDao = parserDao;
        this.regexDao = regexDao;
        this.studlkDao = studlkDao;
        this.securityDao = securityDao;
        this.disciplineDao = disciplineDao;
        this.extractorByMatcher = extractorByMatcher;
    }

    private void getStudlkInfo(StudentDto studentDto) {
        if (securityManager.apiAuthenticate(studentDto)) {
            Parser parser = parserDao.get(MAIN_PAGE_ROW_DB);
            UUID userId = securityDao.getUserId(studentDto.getUsername());
            studentDto.setId(userId);


            try {
                StudentInfo studentInfo = studlkDao.get(userId);
                studentDto.setStudentGroup(studentInfo.getStudentGroup());
            } catch (EmptyResultDataAccessException e) {
                String studentGroup = requests.get("mainPageRequest").send(studentDto.getCookie(), parser.getUrl());
                studentDto.setStudentGroup(studentGroup);

                studlkDao.add(studentDto);
            }
        }
    }

    @PostMapping("/rpd")
    public ResponseEntity<String> handle(@RequestBody Map<String, String> requestJson, StudentDto studentDto) {
        studentDto.setUsername(requestJson.get("username"));
        studentDto.setPassword(requestJson.get("password"));
        studentDto.setCookie(authenticationRequest.getCookies(studentDto));

        Parser parser = parserDao.get(SUBJECT_WORK_PROGRAM_ROW_DB);
        Map<String, String> regex = regexDao.get(parser.getRegexId());

        if (requestJson.containsKey("subject-id")) {
            String url = parser.getUrl() + requestJson.get("subject-id") + "?discType=RPD";
            getStudlkInfo(studentDto);

            try {
                Discipline discipline = disciplineDao.get(requestJson.get("subject-id"));
                return ResponseEntity.ok(extractors.get("subjectWorkProgramExtractor").extract(
                        discipline.getHtmlPage(),
                        regex,
                        this.getClass().getName()
                ));
            } catch (EmptyResultDataAccessException e) {
                String htmlPage = requests.get("subjectWorkProgramRequest").send(studentDto.getCookie(), url);
                disciplineDao.add(new Discipline(
                        UUID.randomUUID(),
                        extractors.get("subjectWorkTitleExtractor").extract(htmlPage, regex, this.getClass().getName()),
                        requestJson.get("subject-id"),
                        studentDto.getStudentGroup(),
                        htmlPage
                ));

                return ResponseEntity.ok(extractors.get("subjectWorkProgramExtractor").extract(htmlPage, regex, this.getClass().getName()));
            }
        } else if (requestJson.containsKey("subject-url")) {
            //return ResponseEntity.ok(parsers.get("subjectWorkProgramParser").send(cookie, requestJson.get("subject-url")));
        } else if (requestJson.containsKey("subject-name")) {
            return null;
            //return parserInterface.execute(cookie, )
        } else {
            return ResponseEntity.badRequest().body("Parameter requests are incorrect, check out the documentation " +
                    "at the link: https://github.com/nikguscode/Susu_API");
        }

        return null;
    }

}
