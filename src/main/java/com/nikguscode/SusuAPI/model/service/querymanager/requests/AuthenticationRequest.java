package com.nikguscode.SusuAPI.model.service.querymanager.requests;

import com.nikguscode.SusuAPI.dto.StudentDto;
import com.nikguscode.SusuAPI.exceptions.NullChecker;
import com.nikguscode.SusuAPI.model.dao.configuration.authenticatioin.AuthenticationDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Authentication;
import com.nikguscode.SusuAPI.model.service.extractors.CsrfTokenExtractor;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestManager;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class AuthenticationRequest extends RequestManager {
    private final CsrfTokenExtractor extractor;
    private final AuthenticationDao authenticationDao;
    private CookieManager userCookie;

    @Autowired
    public AuthenticationRequest(Configurator configurator,
                                 RequestBuilder requestBuilder,
                                 CsrfTokenExtractor extractor,
                                 @Qualifier("jdbcAuthenticationDao") AuthenticationDao authenticationDao) {
        super(configurator, requestBuilder);
        this.extractor = extractor;
        this.authenticationDao = authenticationDao;
    }

    @Override
    protected HttpClient.Builder createClient() {
        HttpClient.Builder client = super.createClient();
        return client.cookieHandler(userCookie = new CookieManager());
    }

    private Map<String, String> setParameters(StudentDto studentDto, Authentication authentication, HttpClient client) {
        Map<String, String> body = new HashMap<>();

        body.put(authentication.getUsernameVariable(), studentDto.getUsername());
        body.put(authentication.getPasswordVariable(), studentDto.getPassword());
        body.put(authentication.getCsrfVariable(), extractor.getCsrfToken(client));

        NullChecker.checkNotNull(body);
        return body;
    }

    private List<HttpCookie> authorize(StudentDto studentDto) {
        try (HttpClient client = createClient().build()) {
            Authentication authenticationConfiguration = authenticationDao.get();
            String body = super.createBody(setParameters(studentDto, authenticationConfiguration, client));
            HttpRequest request = super.createPostRequest(body, authenticationConfiguration.getUrlVariable());

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
