package com.nikguscode.SusuAPI.model.service.querymanager.requests;

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.dao.configuration.variable.VariableDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestSender;
import com.nikguscode.SusuAPI.model.service.querymanager.BaseRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


@Service
public class StudyPlanRequest extends BaseRequest implements RequestSender {
    private final VariableDao variableDao;
    private final RequestDao requestDao;
    @Autowired
    public StudyPlanRequest(Configurator configurator,
                            RequestBuilder requestBuilder,
                            VariableDao variableDao,
                            @Qualifier("jdbcRequestDao") RequestDao requestDao) {
        super(configurator, requestBuilder);
        this.variableDao = variableDao;
        this.requestDao = requestDao;
    }

    @Override
    public String send(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            Request parser = requestDao.get(STUDY_PLAN_REQUEST_ID);
            Map<String, String> requestBodyParameters = variableDao.get(parser.getEntityId());
            requestBodyParameters.put(requestBodyParameters.get("dx_callback_var"), requestBodyParameters.get("dx_callback_val"));

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