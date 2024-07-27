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
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static com.nikguscode.SusuAPI.model.repositories.DBVariablesConstants.*;

@Service
public class SubjectGradeParser extends Parser implements ParserInterface {
    private final VariableMapper mapper;

    @Autowired
    public SubjectGradeParser(ConfiguratorInterface configurator,
                              ExecutorInterface executor,
                              @Qualifier("subjectGradeVariables") VariableMapper mapper) {
        super(configurator, executor);
        this.mapper = mapper;
    }

    private Map<String, String> setParameters(Map<String, String> variables) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(variables.get(DX_CALLBACK_VAR), variables.get(DX_CALLBACK_VAL));

        return parameters;
    }

    @Override
    public String execute(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            HttpResponse<String> response = client.send(
                    super.createPostRequest(super.createBody(setParameters(mapper.getVariables())), cookie, link),
                    HttpResponse.BodyHandlers.ofString()
            );

            logger.info("Response code: {}", response.statusCode());
            return super.createJson(response, mapper.getVariables().get(FIND_PATTERN));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
