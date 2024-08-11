package com.nikguscode.SusuAPI.model.service.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikguscode.SusuAPI.model.dao.config.discipline.JdbcDisciplineDao;
import com.nikguscode.SusuAPI.model.entities.SubjectWorkProgram;
import com.nikguscode.SusuAPI.model.repositories.SelectQueriesManager;
import com.nikguscode.SusuAPI.model.service.Parser;
import com.nikguscode.SusuAPI.model.service.requests.ConfiguratorInterface;
import com.nikguscode.SusuAPI.model.service.requests.ExecutorInterface;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.nikguscode.SusuAPI.constants.ConfigConstants.*;

@Service
public class SubjectWorkProgramParser extends Parser implements ParserInterface {
    private static final Logger logger = LoggerFactory.getLogger(SubjectWorkProgramParser.class);
    private final JdbcDisciplineDao jdbcDisciplineDao;
    private final UserInfoParser userInfoParser;
    private final SelectQueriesManager selectQueriesManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SubjectWorkProgramParser(ConfiguratorInterface configurator,
                                     ExecutorInterface executor,
                                     SelectQueriesManager selectQueriesManager,
                                     JdbcDisciplineDao jdbcDisciplineDao,
                                     UserInfoParser userInfoParser) {
        super(configurator, executor);
        this.selectQueriesManager = selectQueriesManager;
        this.jdbcDisciplineDao = jdbcDisciplineDao;
        this.userInfoParser = userInfoParser;
    }

    private Number parseNumberOrDefault(String value, Number defaultValue) {
        try {
            if (defaultValue instanceof Integer) {
                return Integer.valueOf(value);
            } else {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private List<SubjectWorkProgram> parseTableRows(Elements tableRows) {
        List<SubjectWorkProgram> programs = new ArrayList<>();
        boolean isFirstRow = true;

        for (Element row : tableRows) {
            if (isFirstRow) {
                isFirstRow = false;
                continue;
            }

            Elements rowCells = row.select("td");
            List<String> currentValues = new ArrayList<>();

            for (Element cell : rowCells) {
                currentValues.add(cell.text()
                        .replaceAll("(?<=\\p{L})- (?=\\p{L})", ""));
            }

            var subjectWorkProgram = new SubjectWorkProgram(
                    Optional.ofNullable(!currentValues.isEmpty() ? currentValues.get(0) : null).orElse(""),
                    parseNumberOrDefault(Optional.ofNullable(currentValues.size() > 1 ? currentValues.get(1) : null)
                            .orElse("0"), 0).intValue(),
                    Optional.ofNullable(currentValues.size() > 2 ? currentValues.get(2) : null).orElse(""),
                    Optional.ofNullable(currentValues.size() > 3 ? currentValues.get(3) : null).orElse(""),
                    parseNumberOrDefault(Optional.ofNullable(currentValues.size() > 4 ? currentValues.get(4) : null)
                            .orElse("-"), 0.0).doubleValue(),
                    parseNumberOrDefault(Optional.ofNullable(currentValues.size() > 5 ? currentValues.get(5) : null)
                            .orElse("-"), 0.0).doubleValue(),
                    Optional.ofNullable(currentValues.size() > 6 ? currentValues.get(6) : null).orElse(""),
                    Optional.ofNullable(currentValues.size() > 7 ? currentValues.get(7) : null).orElse("")
            );

            programs.add(subjectWorkProgram);
        }

        return programs;
    }

    private String extractRpd(String page) {
        Document currentBody = Jsoup.parse(super.extractByMatcher(page, "(?s)Контрольные\\sмероприятия\\s\\(КМ\\).*?</table>",
                this.getClass().getName()));
        Elements tableRows = currentBody.select("tr");

        List<SubjectWorkProgram> programs = parseTableRows(tableRows);
        List<String> jsonList = new ArrayList<>();

        for (SubjectWorkProgram program : programs) {
            try {
                jsonList.add(objectMapper.writeValueAsString(program));
            } catch (JsonProcessingException e) {
                logger.error("Error serializing SubjectWorkProgram: {}", program, e);
                throw new RuntimeException("Error serializing SubjectWorkProgram", e);
            }
        }

        return "[" + String.join(", ", jsonList) + "]";
    }

    private boolean checkIfSubjectIdExistsInDatabase(String cookie, String link) {
        String studentGroup = userInfoParser.execute(cookie, "https://studlk.susu.ru/ru");
        return jdbcDisciplineDao.isExists(link, studentGroup);
    }

    @Override
    public String execute(String cookie, String link) {
        //boolean isSubjectIdExistsInDatabase = checkIfSubjectIdExistsInDatabase(cookie, link);

        try (HttpClient client = super.createClient().build()) {
            Map<String, String> variables = selectQueriesManager.executeSelectQuery(SUBJECT_WORK_PROGRAM_ROW);

            HttpResponse<String> response = client.send(
                    super.createGetRequest(cookie, link),
                    HttpResponse.BodyHandlers.ofString()
            );

            logger.info("Response code: {}", response.statusCode());
            return extractRpd(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}