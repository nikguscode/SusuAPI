package com.nikguscode.SusuAPIs.model.service.parsers;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SubjectGradeService extends Parser implements ParserInterface {
    private Map<String, String> setParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("DXCallbackName", "gvCheckout");
        parameters.put("__DXCallbackArgument", "c0:KV|2;[];CT|2;{};GB|19;14|CUSTOMCALLBACK0|;");

        return parameters;
    }

    private String createJson(HttpResponse<String> response) {
        Pattern pattern = Pattern.compile("'cpModel':'(.*?)','cpCurTerm'");
        Matcher matcher = pattern.matcher(response.body());

        if (matcher.find()) {
            if (!matcher.group(1).isEmpty()) {
                return matcher.group(1);
            }
        }

        throw new RuntimeException(matcher.find() ? "JSON creating error" : "No match found");
    }

    @Override
    public String execute(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            String body = super.createBody(setParameters());
            HttpRequest request = super.createPostRequest(body, cookie, link);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response code: {}", response.statusCode());

            return createJson(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
