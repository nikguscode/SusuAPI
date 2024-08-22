package com.nikguscode.SusuAPI.model.service.querymanager.requests;

import com.nikguscode.SusuAPI.model.dao.configuration.parser.ParserDao;
import com.nikguscode.SusuAPI.model.entities.configuration.Parser;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestManager;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.springframework.stereotype.Service;
import static com.nikguscode.SusuAPI.constants.ConfigConstants.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class SubjectPercentageRequest extends RequestManager implements Request {
    private final ParserDao parserDao;

    private SubjectPercentageRequest(Configurator configurator,
                                     RequestBuilder requestBuilder,
                                     ParserDao parserDao) {
        super(configurator, requestBuilder);
        this.parserDao = parserDao;
    }

    @Override
    public String send(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            Parser parser = parserDao.get(SUBJECT_PERCENTAGE_ROW);
            HttpResponse<String> response = client.send(
                    super.createGetRequest(cookie, parser.getUrl()),
                    HttpResponse.BodyHandlers.ofString()
            );

            logger.info("Response code: {}", response.statusCode());
            return super.extractJson(response, parser.getFindPattern());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}