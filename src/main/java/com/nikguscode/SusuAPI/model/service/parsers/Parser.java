package com.nikguscode.SusuAPIs.model.service.parsers;

import com.nikguscode.SusuAPIs.model.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    protected static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    protected HttpClient.Builder createClient() {
        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10));
    }

    protected String createJson(HttpResponse<String> response, String findPattern) {
        Pattern pattern = Pattern.compile(findPattern);
        Matcher matcher = pattern.matcher(response.body());

        if (matcher.find()) {
            if (!matcher.group(1).isEmpty()) {
                return matcher.group(1);
            }
        }

        throw new RuntimeException(matcher.find() ? "JSON creating error" : "No match found");
    }

    protected String createBody(Map<String, String> bodyParameters) {
        StringBuilder body = new StringBuilder();

        for (Map.Entry<String, String> parameter : bodyParameters.entrySet()) {
            body.append(parameter.getKey()).append("=");
            body.append(parameter.getValue()).append("&");
        }

        return body.substring(0, body.length() - 1);
    }

    private HttpRequest.Builder createRequest(String link) {
        return HttpRequest.newBuilder()
                .uri(URI.create(link))
                .header("Content-Type", "application/x-www-form-urlencoded");
    }

    protected HttpRequest createPostRequest(String body, String link) {
        return createRequest(link)
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    protected HttpRequest createPostRequest(String body, String cookie, String link) {
        return createRequest(link)
                .header("Cookie", cookie)
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    protected HttpRequest createGetRequest(String cookie, String link) {
        return createRequest(link)
                .header("Cookie", cookie)
                .GET().build();
    }
}
