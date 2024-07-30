package com.nikguscode.SusuAPI.model.service.parsers;

import com.nikguscode.SusuAPI.model.repositories.SelectQueriesManager;
import com.nikguscode.SusuAPI.model.service.Parser;
import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.springframework.stereotype.Service;
import static com.nikguscode.SusuAPI.model.repositories.SelectConstants.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
public class SubjectPercentageParser extends Parser implements ParserInterface {
    private final SelectQueriesManager selectQueriesManager;

    private SubjectPercentageParser(ConfiguratorInterface configurator,
                                    ExecutorInterface executor,
                                    SelectQueriesManager selectQueriesManager) {
        super(configurator, executor);
        this.selectQueriesManager = selectQueriesManager;
    }

    @Override
    public String execute(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            HttpResponse<String> response = client.send(
                    super.createGetRequest(cookie, link),
                    HttpResponse.BodyHandlers.ofString()
            );

            Map<String, String> variables = selectQueriesManager.executeSelectQuery(SUBJECT_PERCENTAGE_ROW);
            logger.info("Response code: {}", response.statusCode());

            return super.createJson(response, variables.get(FIND_PATTERN_DB));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}