package com.nikguscode.SusuAPI.controller;

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.model.dao.configuration.discipline.DisciplineDao;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.dao.configuration.regex.RegexDao;
import com.nikguscode.SusuAPI.model.dao.user.security.SecurityDao;
import com.nikguscode.SusuAPI.model.dao.user.studlk.StudlkDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Discipline;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import com.nikguscode.SusuAPI.model.entities.user.StudentInfo;
import com.nikguscode.SusuAPI.model.service.extractors.core.RequestExtractor;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestSender;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.StudlkAuthenticationRequest;
import com.nikguscode.SusuAPI.model.service.SecurityManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@Log4j2
public class SubjectWorkProgramController {
    private final StudlkAuthenticationRequest studlkAuthenticationRequest;
    private final SecurityManager securityManager;
    private final Map<String, RequestSender> requests;
    private final Map<String, RequestExtractor> extractors;
    private final RequestDao requestDao;
    private final RegexDao regexDao;
    private final StudlkDao studlkDao;
    private final DisciplineDao disciplineDao;
    private final SecurityDao securityDao;
    private final ExtractorByMatcher extractorByMatcher;

    @Autowired
    public SubjectWorkProgramController(StudlkAuthenticationRequest studlkAuthenticationRequest,
                                        SecurityManager securityManager,
                                        Map<String, RequestSender> requests,
                                        Map<String, RequestExtractor> extractors,
                                        @Qualifier("jdbcRequestDao") RequestDao requestDao,
                                        @Qualifier("jdbcRegexDao") RegexDao regexDao,
                                        @Qualifier("jdbcStudlkDao") StudlkDao studlkDao,
                                        @Qualifier("jdbcSecurityDao") SecurityDao securityDao,
                                        @Qualifier("jdbcDisciplineDao") DisciplineDao disciplineDao,
                                        ExtractorByMatcher extractorByMatcher) {
        this.studlkAuthenticationRequest = studlkAuthenticationRequest;
        this.securityManager = securityManager;
        this.requests = requests;
        this.extractors = extractors;
        this.requestDao = requestDao;
        this.regexDao = regexDao;
        this.studlkDao = studlkDao;
        this.securityDao = securityDao;
        this.disciplineDao = disciplineDao;
        this.extractorByMatcher = extractorByMatcher;
    }

    private void getUserInfoFromStudlk(StudentDto studentDto) {
        if (securityManager.apiAuthenticate(studentDto)) {
            Request requestConfiguration = requestDao.get(MAIN_PAGE_REQUEST_ID);
            UUID userId = securityDao.getUserId(studentDto.getUsername());
            studentDto.setId(userId);


            try {
                StudentInfo studentInfo = studlkDao.get(userId);
                studentDto.setStudentGroup(studentInfo.getStudentGroup());
            } catch (EmptyResultDataAccessException e) {
                String studentGroup = requests.get("mainPageRequest").send(studentDto.getCookie(), requestConfiguration.getUrl());
                studentDto.setStudentGroup(studentGroup);

                studlkDao.add(studentDto);
            }
        }
    }

    private ResponseEntity<String> getSubjectWorkProgramFromRequest(StudentDto studentDto, Map<String, String> regex,
                                                                    String url, String subjectId) {
        String htmlPage = requests.get("subjectWorkProgramRequest").send(studentDto.getCookie(), url);
        disciplineDao.add(new Discipline(
                UUID.randomUUID(),
                extractors.get("subjectWorkTitleExtractor").extract(htmlPage, regex, this.getClass().getName()),
                subjectId,
                studentDto.getStudentGroup(),
                htmlPage
        ));

        return ResponseEntity.ok(extractors.get("subjectWorkProgramExtractor").extract(htmlPage, regex, this.getClass().getName()));
    }

    @PostMapping("/rpd")
    public ResponseEntity<String> handle(@RequestBody Map<String, String> requestJson, StudentDto studentDto) {
        studentDto.setUsername(requestJson.get("username"));
        studentDto.setPassword(requestJson.get("password"));
        studentDto.setCookie(studlkAuthenticationRequest.send(studentDto));

        Request requestConfiguration = requestDao.get(SUBJECT_WORK_PROGRAM_REQUEST_ID);
        Map<String, String> regex = regexDao.get(requestConfiguration.getEntityId());

        if (requestJson.containsKey("subject-id")) {
            String url = requestConfiguration.getUrl() + requestJson.get("subject-id") + "?discType=RPD";
            getUserInfoFromStudlk(studentDto);

            try {
                Discipline subjectWorkProgram = disciplineDao.get(requestJson.get("subject-id"));
                return ResponseEntity.ok(extractors.get("subjectWorkProgramExtractor").extract(
                        subjectWorkProgram.getHtmlPage(),
                        regex,
                        this.getClass().getName()
                ));
            } catch (EmptyResultDataAccessException e) {
                return getSubjectWorkProgramFromRequest(studentDto, regex, url, requestJson.get("subject-id"));
            }
        } else if (requestJson.containsKey("subject-url")) {
            String subjectId = extractorByMatcher.extract(
                    requestJson.get("subject-url"),
                    regex.get(SUBJECT_ID_FROM_URL_REGEX),
                    this.getClass().getName()
            );
            getUserInfoFromStudlk(studentDto);

            try {
                Discipline subjectWorkProgram = disciplineDao.get(subjectId);
                return ResponseEntity.ok(extractors.get("subjectWorkProgramExtractor").extract(
                        subjectWorkProgram.getHtmlPage(),
                        regex,
                        this.getClass().getName()
                ));
            } catch (EmptyResultDataAccessException e) {
                return getSubjectWorkProgramFromRequest(studentDto, regex, requestJson.get("subject-url"), subjectId);
            }
        } else if (requestJson.containsKey("subject-name")) {
            getUserInfoFromStudlk(studentDto);

            try {
                Discipline subjectWorkProgram = disciplineDao.get(
                        requestJson.get("subject-name"),
                        studentDto.getStudentGroup()
                );

                return ResponseEntity.ok(extractors.get("subjectWorkProgramExtractor").extract(
                        subjectWorkProgram.getHtmlPage(),
                        regex,
                        this.getClass().getName()
                ));
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        } else {
            return ResponseEntity.badRequest().body("Parameter requests are incorrect, check out the documentation " +
                    "at the link: https://github.com/nikguscode/Susu_API");
        }
    }
}
