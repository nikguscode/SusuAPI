package com.nikguscode.SusuAPI.model.service.extractors.requests.subjectworkprogram;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikguscode.SusuAPI.dto.SubjectWorkProgram;
import com.nikguscode.SusuAPI.model.service.extractors.core.RequestExtractor;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class SubjectWorkProgramExtractor implements RequestExtractor {
    private final ExtractorByMatcher extractorByMatcher;

    public SubjectWorkProgramExtractor(ExtractorByMatcher extractorByMatcher) {
        this.extractorByMatcher = extractorByMatcher;
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

    private List<SubjectWorkProgram> parseTableRows(Map<String, String> regex, Elements tableRows) {
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
                        .replaceAll(regex.get(HYPHEN_WITH_SPACE_BETWEEN_LETTERS_PATTERN_DB), ""));
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

    @Override
    public String extract(String page, Map<String, String> regex, String callingClass) {
        Document currentBody = Jsoup.parse(extractorByMatcher.extract(page, regex.get(SUBJECT_WORK_PROGRAM_PATTERN_DB),
                this.getClass().getName()));
        Elements tableRows = currentBody.select("tr");

        List<SubjectWorkProgram> programs = parseTableRows(regex, tableRows);
        List<String> jsonList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (SubjectWorkProgram program : programs) {
            try {
                jsonList.add(objectMapper.writeValueAsString(program));
            } catch (JsonProcessingException e) {
                log.error("Error serializing SubjectWorkProgram: {}", program, e);
                throw new RuntimeException("Error serializing SubjectWorkProgram", e);
            }
        }

        return "[" + String.join(", ", jsonList) + "]";
    }
}
