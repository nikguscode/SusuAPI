package com.nikguscode.SusuAPI.controller;

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;
import static com.nikguscode.SusuAPI.enumirations.PercentageRequestType.*;
import static com.nikguscode.SusuAPI.constants.InstanceConstants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.dto.response.PercentagesDto;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.dao.configuration.regex.RegexDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.extractors.requests.subjectpercentage.TotalPercentageExtractor;
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
public class SubjectPercentageController {
    private final StudlkAuthenticationBaseRequest studlkAuthenticationRequest;
    private final RequestSender requestSender;
    private final RequestDao requestDao;
    private final RegexDao regexDao;
    private final ExtractorByMatcher extractorByMatcher;
    private final TotalPercentageExtractor totalPercentageExtractor;
    public SubjectPercentageController(StudlkAuthenticationBaseRequest studlkAuthenticationRequest,
                                       @Qualifier(PERCENTAGE_REQUEST_INSTANCE) RequestSender requestSender,
                                       RequestDao requestDao,
                                       RegexDao regexDao,
                                       ExtractorByMatcher extractorByMatcher,
                                       TotalPercentageExtractor totalPercentageExtractor) {
        this.studlkAuthenticationRequest = studlkAuthenticationRequest;
        this.requestSender = requestSender;
        this.requestDao = requestDao;
        this.regexDao = regexDao;
        this.extractorByMatcher = extractorByMatcher;
        this.totalPercentageExtractor = totalPercentageExtractor;
    }

    @PostMapping("/percentage")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> handle(@RequestBody Map<String, String> requestJson, StudentDto studentDto) {
        studentDto.setUsername(requestJson.get("username"));
        studentDto.setPassword(requestJson.get("password"));
        studentDto.setCookie(studlkAuthenticationRequest.send(studentDto));

        if (requestJson.get("mode").equals(TOTAL.getValue())) {
            Request requestConfiguration = requestDao.get(PERCENTAGE_REQUEST_ID);
            Map<String, String> regex = regexDao.get(requestConfiguration.getRegexId());
            String htmlPage = requestSender.send(studentDto.getCookie(), requestConfiguration.getUrl());
            Map<String, Double> extractedData = totalPercentageExtractor.extract(
                    htmlPage,
                    regex,
                    this.getClass().getName()
            );

            ObjectMapper objectMapper = new ObjectMapper();
            PercentagesDto percentagesDto = new PercentagesDto(
                    extractedData.get("firstSemesterPercentage"),
                    extractedData.get("totalPercentage")
            );

            try {
                return ResponseEntity.ok(objectMapper.writeValueAsString(percentagesDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else if (requestJson.get("mode").equals(BY_SUBJECT.getValue())) {
            Request requestConfiguration = requestDao.get(PERCENTAGE_REQUEST_ID);
            Map<String, String> regex = regexDao.get(requestConfiguration.getRegexId());
            String htmlPage = requestSender.send(studentDto.getCookie(), requestConfiguration.getUrl());
            String extractedData = extractorByMatcher.extract(
                    htmlPage,
                    regex.get(BY_SUBJECT_PERCENTAGE_REGEX),
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
