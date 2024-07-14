package com.nikguscode.SusuAPIs.model.service.parsers;

import static com.nikguscode.SusuAPIs.model.repositories.DBConstants.*;
import com.nikguscode.SusuAPIs.model.repositories.VariableMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SubjectPercentageService extends Parser implements ParserInterface {
    private final VariableMapper mapper;

    public SubjectPercentageService(@Qualifier("subjectPercentageVariables") VariableMapper mapper) {
        this.mapper = mapper;
    }
//
//    private String createJson(HttpResponse<String> response) {
//        Pattern pattern = Pattern.compile("'cpModel':'(.*?)','callBacksEnabled'");
//        Matcher matcher = pattern.matcher(response.body());
//
//        if (matcher.find()) {
//            if (!matcher.group(1).isEmpty()) {
//                return matcher.group(1);
//            }
//        }
//
//        throw new RuntimeException(matcher.find() ? "JSON creating error" : "No match found");
//    }

    @Override
    public String execute(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            HttpRequest request = createGetRequest(cookie, link);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return super.createJson(response, mapper.getVariables().get(FIND_PATTERN));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}