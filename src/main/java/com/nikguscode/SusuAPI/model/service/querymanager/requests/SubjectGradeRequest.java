package com.nikguscode.SusuAPI.model.service.querymanager.requests;

import com.nikguscode.SusuAPI.model.dao.configuration.parser.ParserDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Parser;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestManager;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;

@Service
public class SubjectGradeRequest extends RequestManager implements Request {
    private final ParserDao parserDao;

    @Autowired
    public SubjectGradeRequest(Configurator configurator,
                               RequestBuilder requestBuilder,
                               ParserDao parserDao) {
        super(configurator, requestBuilder);
        this.parserDao = parserDao;
    }

    @Override
    public String send(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            Parser parser = parserDao.get(SUBJECT_GRADE_ROW_DB);
            Map<String, String> requestBodyParameters = new HashMap<>();
            requestBodyParameters.put(parser.getDxCallbackVariable(), parser.getDxCallbackValue());

            String requestBody = super.createBody(super.setRequestFormParameters(requestBodyParameters));
            HttpRequest request = super.createPostRequest(requestBody, cookie, link);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response code: {}", response.statusCode());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}