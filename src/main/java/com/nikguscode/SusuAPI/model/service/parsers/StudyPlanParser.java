package com.nikguscode.SusuAPI.model.service.parsers;

import com.nikguscode.SusuAPI.model.repositories.VariableMapper;
import com.nikguscode.SusuAPI.model.service.Parser;
import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.nikguscode.SusuAPI.model.repositories.DBVariablesConstants.FIND_PATTERN;

@Service
public class StudyPlanParser extends Parser implements ParserInterface {
    private final VariableMapper mapper;

    @Autowired
    public StudyPlanParser(ConfiguratorInterface configurator,
                           ExecutorInterface executor,
                           @Qualifier("studyPlanVariables") VariableMapper mapper) {
        super(configurator, executor);
        this.mapper = mapper;
    }

    @Override
    public String execute(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            String requestBody = super.createBody(super.setRequestFormParameters(mapper.getVariables()));
            HttpRequest request = super.createPostRequest(requestBody, cookie, link);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response code: {}", response.statusCode());

            return super.createJson(response, mapper.getVariables().get(FIND_PATTERN));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}