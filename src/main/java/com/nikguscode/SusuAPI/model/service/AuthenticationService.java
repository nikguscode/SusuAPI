package com.nikguscode.SusuAPI.model.service;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.exceptions.NullChecker;
import com.nikguscode.SusuAPI.model.repositories.AuthenticationVariablesManager;
import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import static com.nikguscode.SusuAPI.constants.ConfigConstants.*;

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
    private final SecurityManager securityManager;
    private final CsrfTokenExtractor extractor;
    private final AuthenticationVariablesManager variables;
    private CookieManager userCookie;

    @Autowired
    public AuthenticationService(SecurityManager securityManager,
                                 ConfiguratorInterface configurator,
                                 ExecutorInterface executor,
                                 CsrfTokenExtractor extractor,
                                 AuthenticationVariablesManager variables) {
        super(configurator, executor);
        this.securityManager = securityManager;
        this.extractor = extractor;
        this.variables = variables;
    }

    @Override
    protected HttpClient.Builder createClient() {
        HttpClient.Builder client = super.createClient();
        return client.cookieHandler(userCookie = new CookieManager());
    }

    private Map<String, String> setParameters(StudentDto studentDto, Map<String, String> variablesName, HttpClient client) {
        Map<String, String> body = new HashMap<>();

        body.put(variablesName.get(USERNAME_DB), studentDto.getUsername());
        body.put(variablesName.get(PASSWORD_DB), studentDto.getPassword());
        body.put(variablesName.get(CSRF_DB), extractor.getCsrfToken(client));

        NullChecker.checkNotNull(body);
        return body;
    }

    private List<HttpCookie> authorize(StudentDto studentDto) {
        try (HttpClient client = createClient().build()) {
            Map<String, String> variables = this.variables.getVariables();
            String body = super.createBody(setParameters(studentDto, variables, client));
            HttpRequest request = super.createPostRequest(body, variables.get(URL_DB));

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response code: {}", response.statusCode());

            return userCookie.getCookieStore().getCookies();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCookies(StudentDto studentDto) {
        List<HttpCookie> cookies = authorize(studentDto);
        StringBuilder cookieString = new StringBuilder();

        for (HttpCookie cookie : cookies) {
            cookieString.append(cookie.toString());
            cookieString.append("; ");
        }

        return (!cookieString.isEmpty()) ? cookieString.substring(0, cookieString.length() - 2) : null;
    }
}
