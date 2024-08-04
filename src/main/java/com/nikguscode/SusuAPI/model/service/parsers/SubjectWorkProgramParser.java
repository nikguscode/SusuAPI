package com.nikguscode.SusuAPI.model.service.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikguscode.SusuAPI.exceptions.NoMatchFoundException;
import com.nikguscode.SusuAPI.model.entities.SubjectWorkProgram;
import com.nikguscode.SusuAPI.model.service.Parser;
import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class SubjectWorkProgramParser extends Parser implements ParserInterface {

    private SubjectWorkProgramParser(ConfiguratorInterface configurator,
                                     ExecutorInterface executor) {
        super(configurator, executor);
    }

    private String extractSubjectName(String page) {
        Document currentPage = Jsoup.parse(page);
        Element divWithName = currentPage.select("#subject-program-text").first();
        String text = super.extractByMatcher(divWithName.text(),
                "(?<=РАБОЧАЯ ПРОГРАММА дисциплины\\s)(.*?)(?=\\sдля направления)");
        System.out.println("test: " + text);
        return null;
    }

//    private String extractRpd(String page) throws NoMatchFoundException {
//        Pattern pattern = Pattern.compile("(?s)Контрольные\\sмероприятия\\s\\(КМ\\).*?</table>");
//        Matcher matcher = pattern.matcher(page);
//
//        if (!matcher.find()) {
//            throw new NoMatchFoundException(this.getClass().getName());
//        }
//
//        Document currentBody = Jsoup.parse(matcher.group());
//        Elements tableRows = currentBody.select("tr");
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> json = new ArrayList<>();
//
//        for (Element row : tableRows) {
//            Elements rowCells = row.select("td");
//            List<String> currentValues = new ArrayList<>();
//
//            for (Element cell : rowCells) {
//                currentValues.add(cell.text());
//            }
//
//            try {
//                json.add(objectMapper.writeValueAsString(currentValues));
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        json.removeFirst();
//
//        StringBuilder jsonResponse = new StringBuilder();
//        jsonResponse.append("[");
//
//        for (String currentJson : json) {
//            jsonResponse.append(currentJson);
//        }
//
//        jsonResponse.append("]");
//
//        return jsonResponse.toString();
//    }

//    private String createJson() {
//
//    }

    @Override
    public String execute(String cookie, String link) {
        try (HttpClient client = super.createClient().build()) {
            HttpResponse<String> response = client.send(
                    super.createGetRequest(cookie, link),
                    HttpResponse.BodyHandlers.ofString()
            );

            logger.info("Response code: {}", response.statusCode());

            extractSubjectName(response.body());

            //return extractData(response.body());
            return null;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
