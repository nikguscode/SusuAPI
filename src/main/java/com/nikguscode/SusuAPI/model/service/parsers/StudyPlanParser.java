package com.nikguscode.SusuAPI.model.service.parsers;

import com.nikguscode.SusuAPI.model.repositories.SelectQueriesManager;
import com.nikguscode.SusuAPI.model.service.Parser;
import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.nikguscode.SusuAPI.constants.ConfigConstants.*;

@Service
public class StudyPlanParser extends Parser implements ParserInterface {
    private final SelectQueriesManager selectQueriesManager;
    private final UserInfoParser userInfoParser;

    @Autowired
    public StudyPlanParser(ConfiguratorInterface configurator,
                           ExecutorInterface executor,
                           SelectQueriesManager selectQueriesManager,
                           UserInfoParser userInfoParser) {
        super(configurator, executor);
        this.selectQueriesManager = selectQueriesManager;
        this.userInfoParser = userInfoParser;
    }

    @Override
    public String execute(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            Map<String, String> variables = selectQueriesManager.executeSelectQuery(STUDY_PLAN_ROW);
            String requestBody = super.createBody(super.setRequestFormParameters(variables));
            HttpRequest request = super.createPostRequest(requestBody, cookie, link);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response code: {}", response.statusCode());

            return super.createJson(response, variables.get(FIND_PATTERN_DB));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}