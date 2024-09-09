package com.nikguscode.SusuAPI.model.service.querymanager.requests;

import com.nikguscode.SusuAPI.model.service.querymanager.RequestSender;
import com.nikguscode.SusuAPI.model.service.querymanager.BaseRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class SubjectWorkProgramBaseRequest extends BaseRequest implements RequestSender {
    private SubjectWorkProgramBaseRequest(Configurator configurator,
                                          RequestBuilder requestBuilder) {
        super(configurator, requestBuilder);
    }

    @Override
    public String send(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            HttpResponse<String> response = client.send(
                    super.createGetRequest(cookie, link),
                    HttpResponse.BodyHandlers.ofString()
            );

            logger.info("Response code: {}", response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}