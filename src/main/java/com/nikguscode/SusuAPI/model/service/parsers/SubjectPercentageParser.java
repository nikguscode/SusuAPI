package com.nikguscode.SusuAPI.model.service.parsers;

import static com.nikguscode.SusuAPI.model.repositories.DBVariablesConstants.FIND_PATTERN;

import com.nikguscode.SusuAPI.model.repositories.VariableMapper;
import com.nikguscode.SusuAPI.model.service.Parser;
import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class SubjectPercentageParser extends Parser implements ParserInterface {
    private final VariableMapper mapper;

    private SubjectPercentageParser(ConfiguratorInterface configurator,
                                    ExecutorInterface executor,
                                    @Qualifier("subjectPercentageVariables") VariableMapper mapper) {
        super(configurator, executor);
        this.mapper = mapper;
    }

    @Override
    public String execute(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            HttpResponse<String> response = client.send(
                    super.createGetRequest(cookie, link),
                    HttpResponse.BodyHandlers.ofString()
            );

            logger.info("Response code: {}", response.statusCode());
            return super.createJson(response, mapper.getVariables().get(FIND_PATTERN));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}