package com.nikguscode.SusuAPIs.model.service;

import com.nikguscode.SusuAPIs.model.entities.Student;
import com.nikguscode.SusuAPIs.model.service.parsers.CsrfTokenExtractor;
import com.nikguscode.SusuAPIs.model.service.parsers.Parser;
import com.nikguscode.SusuAPIs.model.repositories.variables.AuthenticationVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequestScope
public class AuthenticationService extends Parser {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final CsrfTokenExtractor extractor;
    private final AuthenticationVariables authenticationVariables;
    private CookieManager userCookie;

    public AuthenticationService(CsrfTokenExtractor extractor,
                                 AuthenticationVariables authenticationVariables) {
        this.extractor = extractor;
        this.authenticationVariables = authenticationVariables;
    }

    @Override
    protected HttpClient.Builder createClient() {
        HttpClient.Builder client = super.createClient();
        return client.cookieHandler(userCookie = new CookieManager());
    }

    private Map<String, String> setParameters(Student student, Map<String, String> variablesName, HttpClient client) {
        Map<String, String> body = new HashMap<>();
        body.put(variablesName.get("userNameVariable"), student.getUsername());
        body.put(variablesName.get("passwordVariable"), student.getPassword());
        body.put(variablesName.get("csrfVariable"), extractor.getCsrfToken(client));

        return body;
    }

    private List<HttpCookie> authorize(Student student) {
        try (HttpClient client = createClient().build()){
            Map<String, String> variables = authenticationVariables.getVariables();
            String body = super.createBody(setParameters(student, variables, client));
            HttpRequest request = super.createPostRequest(body, variables.get("urlVariable"));

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response code: {}", response.statusCode());

            return userCookie.getCookieStore().getCookies();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCookies(Student student) {
        List<HttpCookie> cookies = authorize(student);
        StringBuilder cookieString = new StringBuilder();

        for (HttpCookie cookie : cookies) {
            cookieString.append(cookie.toString());
            cookieString.append("; ");
        }

        return (!cookieString.isEmpty()) ? cookieString.substring(0, cookieString.length() - 2) : null;
    }
}
