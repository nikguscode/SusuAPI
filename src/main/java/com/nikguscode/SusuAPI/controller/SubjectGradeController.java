package com.nikguscode.SusuAPI.controller;

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;
import static com.nikguscode.SusuAPI.constants.InstanceConstants.*;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.dao.configuration.regex.RegexDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.StudlkAuthenticationBaseRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
public class SubjectGradeController {
    private final StudlkAuthenticationBaseRequest studlkAuthenticationRequest;
    private final RequestSender requestSender;
    private final RequestDao requestDao;
    private final RegexDao regexDao;
    private final ExtractorByMatcher extractorByMatcher;

    public SubjectGradeController(StudlkAuthenticationBaseRequest studlkAuthenticationRequest,
                                  @Qualifier(SUBJECT_GRADE_REQUEST_INSTANCE) RequestSender requestSender,
                                  RequestDao requestDao,
                                  RegexDao regexDao,
                                  ExtractorByMatcher extractorByMatcher) {
        this.studlkAuthenticationRequest = studlkAuthenticationRequest;
        this.requestSender = requestSender;
        this.requestDao = requestDao;
        this.regexDao = regexDao;
        this.extractorByMatcher = extractorByMatcher;
    }

    @PostMapping("/grades")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> handle(@RequestBody StudentDto studentDto) {
        Request requestConfiguration = requestDao.get(SUBJECT_GRADE_REQUEST_ID);
        Map<String, String> regex = regexDao.get(requestConfiguration.getRegexId());
        String htmlPage = requestSender.send(studlkAuthenticationRequest.send(studentDto), requestConfiguration.getUrl());
        String extractedData = extractorByMatcher.extract(
                htmlPage,
                regex.get(SUBJECT_GRADE_REGEX),
                this.getClass().getName()
        );

        log.info("Response code: 200");

        return ResponseEntity.ok(extractedData);
    }
}
