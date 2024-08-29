package com.nikguscode.SusuAPI.model.service.querymanager.requests;

import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import com.nikguscode.SusuAPI.model.service.querymanager.Request;
import com.nikguscode.SusuAPI.model.service.querymanager.RequestManager;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.client.Configurator;
import com.nikguscode.SusuAPI.model.service.querymanager.configuration.request.RequestBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class MainPageRequest extends RequestManager implements Request {
    private final ExtractorByMatcher extractorByMatcher;

    @Autowired
    public MainPageRequest(Configurator configurator,
                           RequestBuilder requestBuilder,
                           ExtractorByMatcher extractorByMatcher) {
        super(configurator, requestBuilder);
        this.extractorByMatcher = extractorByMatcher;
    }

    private String extractInfo(String page) {
        Document body = Jsoup.parse(page);
        Element element = body.selectFirst(".info");
        return extractorByMatcher.extract(element.text(), "(?<=Группа:\\s*)\\S+", this.getClass().getName());
    }

    @Override
    public String send(String cookie, String link) {
        try(HttpClient client = super.createClient().build()) {
            HttpResponse<String> response = client.send(
              super.createGetRequest(cookie, link),
              HttpResponse.BodyHandlers.ofString()
            );

            logger.info("Response code: {}", response.statusCode());

            return extractInfo(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
