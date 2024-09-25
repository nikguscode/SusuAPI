package com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.dao.configuration.variable.VariableDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.BaseRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public abstract class AuthenticationRequest extends BaseRequest {
    private final RequestDao requestDao;
    private final VariableDao variableDao;
    private CookieManager userCookie;

    public AuthenticationRequest(Configurator configurator,
                                 RequestBuilder requestBuilder,
                                 @Qualifier("jdbcRequestDao") RequestDao requestDao,
                                 @Qualifier("jdbcVariableDao") VariableDao variableDao) {
        super(configurator, requestBuilder);
        this.requestDao = requestDao;
        this.variableDao = variableDao;
    }

    protected HttpClient.Builder createClient() {
        HttpClient.Builder client = super.createClient();
        return client.cookieHandler(userCookie = new CookieManager());
    }

    protected List<HttpCookie> authorize(StudentDto studentDto, String authenticationId) {
        try (HttpClient client = createClient().build()) {
            Request requestConfiguration = requestDao.get(authenticationId);
            Map<String, String> variablesConfiguration = variableDao.get(requestConfiguration.getEntityId());
            String body = super.createBody(setParameters(studentDto, variablesConfiguration, client));
            HttpRequest request = super.createPostRequest(body, requestConfiguration.getUrl());

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response code: {}", response.statusCode());

            return userCookie.getCookieStore().getCookies();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Map<String, String> setParameters(
            StudentDto studentDto,
            Map<String, String> variables,
            HttpClient client
    );

    public String getCookies(StudentDto studentDto, String authenticationId) {
        List<HttpCookie> cookies = authorize(studentDto, authenticationId);
        StringBuilder cookieString = new StringBuilder();

        for (HttpCookie cookie : cookies) {
            cookieString.append(cookie.toString());
            cookieString.append("; ");
        }

        return (!cookieString.isEmpty()) ? cookieString.substring(0, cookieString.length() - 2) : null;
    }
}
