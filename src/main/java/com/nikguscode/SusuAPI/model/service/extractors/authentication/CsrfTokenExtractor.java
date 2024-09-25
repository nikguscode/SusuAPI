package com.nikguscode.SusuAPI.model.service.extractors.authentication;

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;

import com.nikguscode.SusuAPI.model.dao.configuration.regex.RegexDao;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
public class CsrfTokenExtractor extends BaseTokenExtractor implements TokenExtractor {
    private final RequestDao requestDao;
    private final RegexDao regexDao;

    public CsrfTokenExtractor(RequestDao requestDao,
                              RegexDao regexDao) {
        this.requestDao = requestDao;
        this.regexDao = regexDao;
    }

    @Override
    public String extract(HttpClient client) {
        Request request = requestDao.get(CSRF_TOKEN_EXTRACTOR_ID);
        Map<String, String> regex = regexDao.get(request.getEntityId());
        HttpResponse<String> response = super.getResponseBody(client, request.getUrl());

        if (response.statusCode() == 200 || response.statusCode() == 302 || response.statusCode() == 303) {
            return super.getToken(response, regex);
        } else {
            throw new RuntimeException("CSRF token extract exception, status code: " + response.statusCode());
        }
    }
}
