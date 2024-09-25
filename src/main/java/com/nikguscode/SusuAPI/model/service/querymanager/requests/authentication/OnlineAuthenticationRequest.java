package com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import com.nikguscode.SusuAPI.exceptions.NullChecker;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.dao.configuration.variable.VariableDao;
import com.nikguscode.SusuAPI.model.service.extractors.authentication.MachineTokenExtractor;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@Service
public class OnlineAuthenticationRequest extends AuthenticationRequest implements AuthenticationRequestSender {
    private final MachineTokenExtractor tokenExtractor;

    public OnlineAuthenticationRequest(Configurator configurator,
                                       RequestBuilder requestBuilder,
                                       RequestDao requestDao,
                                       VariableDao variableDao,
                                       MachineTokenExtractor tokenExtractor) {
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
        return super.getCookies(studentDto, "online_susu_auth");
    }
}
