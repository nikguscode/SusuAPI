package com.nikguscode.SusuAPI.model.service.parsers;

import com.nikguscode.SusuAPI.model.service.Parser;
import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class UserInfoParser extends Parser implements ParserInterface {
    public UserInfoParser(ConfiguratorInterface configurator,
                          ExecutorInterface extractor) {
        super(configurator, extractor);
    }

    private String extractInfo(String page) {
        Document body = Jsoup.parse(page);
        Element element = body.selectFirst(".info");
        return super.extractByMatcher(element.text(), "(?<=Группа:\\s*)\\S+", this.getClass().getName());
    }

    @Override
    public String execute(String cookie, String link) {
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
