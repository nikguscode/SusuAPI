package com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.model.dao.configuration.authenticatioin.AuthenticationDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Authentication;
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
public abstract class AuthenticationBaseRequest extends BaseRequest {
    private final AuthenticationDao authenticationDao;
    private CookieManager userCookie;

    public AuthenticationBaseRequest(Configurator configurator,
                                     RequestBuilder requestBuilder,
                                     @Qualifier("jdbcAuthenticationDao") AuthenticationDao authenticationDao) {
        super(configurator, requestBuilder);
        this.authenticationDao = authenticationDao;
    }

    protected HttpClient.Builder createClient() {
        HttpClient.Builder client = super.createClient();
        return client.cookieHandler(userCookie = new CookieManager());
    }

    protected List<HttpCookie> authorize(StudentDto studentDto) {
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

    protected abstract Map<String, String> setParameters(
            StudentDto studentDto,
            Authentication authentication,
            HttpClient client
    );

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
