package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.dao.configuration.regex.RegexDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.StudlkAuthenticationRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestSender;

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;
import static com.nikguscode.SusuAPI.constants.InstanceConstants.*;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Log4j2
public class StudyPlanController {
    private final StudlkAuthenticationRequest studlkAuthenticationRequest;
    private final RequestSender requestSender;
    private final RequestDao requestDao;
    private final RegexDao regexDao;
    private final ExtractorByMatcher extractorByMatcher;

    public StudyPlanController(StudlkAuthenticationRequest studlkAuthenticationRequest,
                               @Qualifier(STUDY_PLAN_REQUEST_INSTANCE) RequestSender requestSender,
                               RequestDao requestDao,
                               RegexDao regexDao,
                               ExtractorByMatcher extractorByMatcher) {
        this.studlkAuthenticationRequest = studlkAuthenticationRequest;
        this.requestSender = requestSender;
        this.requestDao = requestDao;
        this.regexDao = regexDao;
        this.extractorByMatcher = extractorByMatcher;
    }

    @PostMapping("/study-plan")
    public ResponseEntity<String> handle(@RequestBody StudentDto studentDto) {
        Request requestConfiguration = requestDao.get(STUDY_PLAN_REQUEST_ID);
        Map<String, String> regex = regexDao.get(requestConfiguration.getEntityId());
        String htmlPage = requestSender.send(studlkAuthenticationRequest.send(studentDto), requestConfiguration.getUrl());
        String extractedData = extractorByMatcher.extract(htmlPage, regex.get(STUDY_PLAN_REGEX), this.getClass().getName());

        log.info("Response code: 200");

        return ResponseEntity.ok(extractedData);
    }
}
