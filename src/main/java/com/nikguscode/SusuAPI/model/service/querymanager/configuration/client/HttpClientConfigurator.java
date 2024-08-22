package com.nikguscode.SusuAPI.model.service.querymanager.configuration.client;

import org.springframework.stereotype.Service;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

@Service
public class HttpClientConfigurator implements Configurator {
    public HttpClient.Builder createClient() {
        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10));
    }

    public String createBody(Map<String, String> bodyParameters) {
        StringBuilder body = new StringBuilder();

        for (Map.Entry<String, String> parameter : bodyParameters.entrySet()) {
            body.append(parameter.getKey()).append("=");
            body.append(parameter.getValue()).append("&");
        }

        return body.substring(0, body.length() - 1);
    }
}
