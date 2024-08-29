package com.nikguscode.SusuAPI.model.service.extractors.requests.subjectworkprogram;

import static com.nikguscode.SusuAPI.constants.ConfigurationConstants.*;

import com.nikguscode.SusuAPI.model.service.extractors.core.RequestExtractor;
import com.nikguscode.SusuAPI.model.service.extractors.core.ExtractorByMatcher;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
public class SubjectWorkTitleExtractor implements RequestExtractor {
    private final ExtractorByMatcher extractorByMatcher;

    public SubjectWorkTitleExtractor(ExtractorByMatcher extractorByMatcher) {
        this.extractorByMatcher = extractorByMatcher;
    }

    @Override
    public String extract(String htmlPage, Map<String, String> regex, String callingClass) {
        Document currentPage = Jsoup.parse(htmlPage);
        String extractedTitleWithSubjectCode = extractorByMatcher.extract(
                currentPage.body().text(),
                regex.get(SUBJECT_NAME_WITH_CODE_PATTERN_DB),
                this.getClass().toString()
        );
        String removeCodeRegex = regex.get(CODE_REMOVER_PATTERN_DB);

        log.info("Service response: 200. Title extracted");

        return extractedTitleWithSubjectCode.replaceAll(removeCodeRegex, "").trim();
    }
}
