package com.nikguscode.SusuAPI.model.service.extractors.requests.subjectpercentage;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class TotalPercentageExtractor {
    private final ExtractorByMatcher extractorByMatcher;

    public TotalPercentageExtractor (ExtractorByMatcher extractorByMatcher) {
        this.extractorByMatcher = extractorByMatcher;
    }

    private String parse(String htmlPage, Map<String, String> regex, String callingClass) {
        Document currentPage = Jsoup.parse(htmlPage);
        Elements percentageBlock = currentPage.select("#MainBody");
        Element totalPercentage = percentageBlock.select("strong").first();

        if (totalPercentage != null) {
            return totalPercentage.text();
        } else {
            throw new NullPointerException("Error, while extracting total percentage in " + this.getClass().getName() + "\n"
            + "Calling class " + callingClass);
        }
    }

    public Map<String, Double> extract(String htmlPage, Map<String, String> regex, String callingClass) {
        String percentageRow = parse(htmlPage, regex, callingClass);

        List<String> percentagesList =  extractorByMatcher.extract(
                percentageRow,
                regex.get(TOTAL_SUBJECT_PERCENTAGE_PATTERN_DB), // сделать с БД
                2,
                this.getClass().getName()
        );

        Map<String, Double> percentagesMap = new HashMap<>();
        try {
            log.warn(percentagesList.get(0));
            log.warn(percentagesList.get(1));
            percentagesMap.put(FIRST_SEMESTER_PERCENTAGE, Double.parseDouble(percentagesList.get(0).replace(",", ".")));
            percentagesMap.put(TOTAL_PERCENTAGE, Double.parseDouble(percentagesList.get(1).replace(",", ".")));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error, while casting to Double in " + this.getClass().getName() + "\n"
            + "Calling class " + callingClass);
        }

        return percentagesMap;
    }
}
