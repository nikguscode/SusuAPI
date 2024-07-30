package com.nikguscode.SusuAPI.model.service;

import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.nikguscode.SusuAPI.model.repositories.SelectConstants.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Parser {
    private final ConfiguratorInterface configurator;
    private final ExecutorInterface executor;
    protected final Logger logger;

    @Autowired
    public Parser (ConfiguratorInterface configurator,
                   ExecutorInterface executor) {
        this.configurator = configurator;
        this.executor = executor;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    protected HttpClient.Builder createClient() {
        return configurator.createClient();
    }

    protected String createBody(Map<String, String> bodyParameters) {
        return configurator.createBody(bodyParameters);
    }

    protected HttpRequest createPostRequest(String body, String link) {
        return executor.createPostRequest(body, link);
    }

    protected HttpRequest createPostRequest(String body, String cookie, String link) {
        return executor.createPostRequest(body, cookie, link);
    }

    protected HttpRequest createGetRequest(String cookie, String link) {
        return executor.createGetRequest(cookie, link);
    }

    protected Map<String, String> setRequestFormParameters(Map<String, String> variables) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(variables.get(DX_CALLBACK_DB), variables.get(DX_CALLBACK_VALUE_DB));

        return parameters;
    }

    protected String createJson(HttpResponse<String> response, String findPattern) {
        Pattern pattern = Pattern.compile(findPattern);
        Matcher matcher = pattern.matcher(response.body());

        if (matcher.find()) {
            if (!matcher.group(1).isEmpty()) {
                return matcher.group(1);
            }
        }

        throw new RuntimeException(matcher.find() ? "JSON creating error" : "No match found. Response error");
    }
}
