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
        System.out.println(currentPage.body().text());
        return extractorByMatcher.extract(currentPage.body().text(), regex, this.getClass().toString());
    }
}
