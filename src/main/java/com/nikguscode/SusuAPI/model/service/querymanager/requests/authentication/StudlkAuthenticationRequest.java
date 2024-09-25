package com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.exceptions.NullChecker;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.dao.configuration.variable.VariableDao;
import com.nikguscode.SusuAPI.model.service.extractors.authentication.TokenExtractor;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@Service
@RequestScope
public class StudlkAuthenticationRequest extends AuthenticationRequest implements AuthenticationRequestSender {
    private final TokenExtractor tokenExtractor;

    public StudlkAuthenticationRequest(Configurator configurator,
                                       RequestBuilder requestBuilder,
                                       @Qualifier("jdbcRequestDao") RequestDao requestDao,
                                       @Qualifier("jdbcVariableDao") VariableDao variableDao,
                                       @Qualifier("csrfTokenExtractor") TokenExtractor tokenExtractor) {
        super(configurator, requestBuilder, requestDao, variableDao);
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    protected Map<String, String> setParameters(StudentDto studentDto, Map<String, String> variables, HttpClient client) {
        Map<String, String> body = new HashMap<>();

        body.put(variables.get("username_var"), studentDto.getUsername());
        body.put(variables.get("password_var"), studentDto.getPassword());
        body.put(variables.get("csrf_var"), tokenExtractor.extract(client));

        NullChecker.checkNotNull(body);
        return body;
    }

    @Override
    public String send(StudentDto studentDto) {
        return super.getCookies(studentDto, "studlk_susu_auth");
    }
}
