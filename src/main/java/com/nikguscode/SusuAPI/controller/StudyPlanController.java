package com.nikguscode.SusuAPI.controller;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.model.dao.configuration.parser.ParserDao;
import com.nikguscode.SusuAPI.model.dao.configuration.regex.RegexDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Parser;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.AuthenticationRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;

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
    private final AuthenticationRequest authenticationRequest;
    private final Request request;
    private final ParserDao parserDao;
    private final RegexDao regexDao;
    private final ExtractorByMatcher extractorByMatcher;

    public StudyPlanController(AuthenticationRequest authenticationRequest,
                               @Qualifier("studyPlanRequest") Request request,
                               ParserDao parserDao,
                               RegexDao regexDao,
                               ExtractorByMatcher extractorByMatcher) {
        this.authenticationRequest = authenticationRequest;
        this.request = request;
        this.parserDao = parserDao;
        this.regexDao = regexDao;
        this.extractorByMatcher = extractorByMatcher;
    }

    @PostMapping("/study-plan")
    public ResponseEntity<String> handle(@RequestBody StudentDto studentDto) {
        Parser parser = parserDao.get(STUDY_PLAN_ROW_DB);
        Map<String, String> regex = regexDao.get(parser.getRegexId());
        String htmlPage = request.send(authenticationRequest.getCookies(studentDto), parser.getUrl());
        String extractedData = extractorByMatcher.extract(htmlPage, regex.get(STUDY_PLAN_PATTERN_DB), this.getClass().getName());

        log.info("Response code: 200");

        return ResponseEntity.ok(extractedData);
    }
}
