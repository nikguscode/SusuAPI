package com.nikguscode.SusuAPI.model.service.querymanager.requests;

import static com.nikguscode.SusuAPI.constants.DatabaseConstants.*;
import com.nikguscode.SusuAPI.model.dao.configuration.request.RequestDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestSender;
import com.nikguscode.SusuAPI.model.service.querymanager.BaseRequest;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class PercentageBaseRequest extends BaseRequest implements RequestSender {
    private final RequestDao requestDao;

    private PercentageBaseRequest(Configurator configurator,
                                  RequestBuilder requestBuilder,
                                  RequestDao requestDao) {
        super(configurator, requestBuilder);
        this.requestDao = requestDao;
    }

    @Override
    public String send(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            Request requestConfiguration = requestDao.get(PERCENTAGE_REQUEST_ID);
            HttpResponse<String> response = client.send(
                    super.createGetRequest(cookie, requestConfiguration.getUrl()),
                    HttpResponse.BodyHandlers.ofString()
            );

            logger.info("Response code: {}", response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}