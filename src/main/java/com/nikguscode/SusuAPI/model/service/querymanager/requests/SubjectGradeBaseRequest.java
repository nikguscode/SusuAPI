package com.nikguscode.SusuAPI.model.service.querymanager.requests;

import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestSender;
import com.nikguscode.SusuAPI.model.service.querymanager.BaseRequest;
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

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;

@Service
public class SubjectGradeBaseRequest extends BaseRequest implements RequestSender {
    private final RequestDao requestDao;

    @Autowired
    public SubjectGradeBaseRequest(Configurator configurator,
                                   RequestBuilder requestBuilder,
                                   RequestDao requestDao) {
        super(configurator, requestBuilder);
        this.requestDao = requestDao;
    }

    @Override
    public String send(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            Request parser = requestDao.get(SUBJECT_GRADE_REQUEST_ID);
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