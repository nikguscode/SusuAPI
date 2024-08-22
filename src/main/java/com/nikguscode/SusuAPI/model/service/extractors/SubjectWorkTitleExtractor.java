package com.nikguscode.SusuAPI.model.service.extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class SubjectWorkTitleExtractor {
    private final ExtractorByMatcher extractorByMatcher;

    public SubjectWorkTitleExtractor(ExtractorByMatcher extractorByMatcher) {
        this.extractorByMatcher = extractorByMatcher;
    }

    public String extract(String htmlPage, String regex) {
        Document currentPage = Jsoup.parse(htmlPage);
        String extractedTitleWithSubjectCode = extractorByMatcher.extract(currentPage.body().text(), regex, this.getClass().toString());
        String removeCodeRegex = "^\\S+\\s";
        return extractedTitleWithSubjectCode.replaceAll(removeCodeRegex, "").trim();
    }
}
