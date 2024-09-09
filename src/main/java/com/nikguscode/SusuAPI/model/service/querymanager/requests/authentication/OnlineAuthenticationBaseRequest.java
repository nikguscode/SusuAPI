package com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.exceptions.NullChecker;
import com.nikguscode.SusuAPI.model.dao.configuration.authenticatioin.AuthenticationDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Authentication;
import com.nikguscode.SusuAPI.model.service.extractors.authentication.CsrfTokenExtractor;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@Service
public class OnlineAuthenticationBaseRequest extends AuthenticationBaseRequest implements AuthenticationRequestSender {
    private final CsrfTokenExtractor csrfExtractor;

    public OnlineAuthenticationBaseRequest(Configurator configurator,
                                           RequestBuilder requestBuilder,
                                           AuthenticationDao authenticationDao,
                                           CsrfTokenExtractor csrfExtractor) {
        super(configurator, requestBuilder, authenticationDao);
        this.csrfExtractor = csrfExtractor;
    }

    @Override
    protected Map<String, String> setParameters(StudentDto studentDto, Authentication authentication, HttpClient client) {
        Map<String, String> body = new HashMap<>();

        body.put(authentication.getUsernameVariable(), studentDto.getUsername());
        body.put(authentication.getPasswordVariable(), studentDto.getPassword());
        body.put(authentication.getCsrfVariable(), csrfExtractor.extract(client));

        NullChecker.checkNotNull(body);
        return body;
    }

    @Override
    public String send(StudentDto studentDto) {
        return super.getCookies(studentDto);
    }
}
