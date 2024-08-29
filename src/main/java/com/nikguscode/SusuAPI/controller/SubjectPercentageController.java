package com.nikguscode.SusuAPI.controller;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;
import static com.nikguscode.SusuAPI.enumirations.PercentageRequestType.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.dto.Percentages;
import com.nikguscode.SusuAPI.model.dao.configuration.parser.ParserDao;
import com.nikguscode.SusuAPI.model.dao.configuration.regex.RegexDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Parser;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.extractors.requests.subjectpercentage.TotalPercentageExtractor;
import com.nikguscode.SusuAPI.model.service.querymanager.requests.AuthenticationRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
public class SubjectPercentageController {
    private final AuthenticationRequest authenticationRequest;
    private final Request request;
    private final ParserDao parserDao;
    private final RegexDao regexDao;
    private final ExtractorByMatcher extractorByMatcher;
    private final TotalPercentageExtractor totalPercentageExtractor;
    public SubjectPercentageController(AuthenticationRequest authenticationRequest,
                                       @Qualifier("subjectPercentageRequest") Request request,
                                       ParserDao parserDao,
                                       RegexDao regexDao,
                                       ExtractorByMatcher extractorByMatcher,
                                       TotalPercentageExtractor totalPercentageExtractor) {
        this.authenticationRequest = authenticationRequest;
        this.request = request;
        this.parserDao = parserDao;
        this.regexDao = regexDao;
        this.extractorByMatcher = extractorByMatcher;
        this.totalPercentageExtractor = totalPercentageExtractor;
    }

    @PostMapping("/percentage")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> handle(@RequestBody Map<String, String> requestJson, StudentDto studentDto) {
        studentDto.setUsername(requestJson.get("username"));
        studentDto.setPassword(requestJson.get("password"));
        studentDto.setCookie(authenticationRequest.getCookies(studentDto));

        if (requestJson.get("mode").equals(TOTAL.getValue())) {
            Parser parser = parserDao.get(SUBJECT_PERCENTAGE_ROW_DB);
            Map<String, String> regex = regexDao.get(parser.getRegexId());
            String htmlPage = request.send(studentDto.getCookie(), parser.getUrl());
            Map<String, Double> extractedData = totalPercentageExtractor.extract(
                    htmlPage,
                    regex,
                    this.getClass().getName()
            );

            ObjectMapper objectMapper = new ObjectMapper();
            Percentages percentages = new Percentages(
                    extractedData.get("firstSemesterPercentage"),
                    extractedData.get("totalPercentage")
            );

            try {
                return ResponseEntity.ok(objectMapper.writeValueAsString(percentages));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else if (requestJson.get("mode").equals(BY_SUBJECT.getValue())) {
            Parser parser = parserDao.get(SUBJECT_PERCENTAGE_ROW_DB);
            Map<String, String> regex = regexDao.get(parser.getRegexId());
            String htmlPage = request.send(studentDto.getCookie(), parser.getUrl());
            String extractedData = extractorByMatcher.extract(
                    htmlPage,
                    regex.get(BY_SUBJECT_PERCENTAGE_PATTERN_DB),
                    this.getClass().getName()
            );

            log.info("Response code: 200");

            return ResponseEntity.ok(extractedData);
        } else {
            return ResponseEntity.badRequest().body("Parameter requests are incorrect, check out the documentation " +
                    "at the link: https://github.com/nikguscode/Susu_API");
        }
    }
}
