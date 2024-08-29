package com.nikguscode.SusuAPI.model.service.extractors.core;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class ExtractorByMatcher {
    public String extract(String dataForExtract, String regex, String callingClass) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dataForExtract);

        if (matcher.find()) {
            log.debug("Matcher: {} for {} class. \nBody: {}", matcher.group().trim(), callingClass, dataForExtract);
            log.info("Service code: 200. Extracted");
            return matcher.group().trim();
        } else {
            log.warn("Matcher not found for {} class.", callingClass);
            throw new RuntimeException("No match found. Response error in class " + callingClass + ".");
        }
    }

    public List<String> extract(String dataForExtract, String regex, int amountOfRegex, String callingClass) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dataForExtract);
        List<String> extractedGroups = new ArrayList<>();

        for (int currentRegex = 0; currentRegex < amountOfRegex; currentRegex++) {
            if (matcher.find()) {
                log.warn(matcher.groupCount());
                log.debug("Matcher: {} for {} class. \nBody: {}", matcher.group().trim(), callingClass, dataForExtract);

                if (matcher.group(0) != null) {
                    extractedGroups.add(matcher.group(0).trim());
                } else {
                    throw new RuntimeException("Match group not found. Response error in class " + callingClass + ".");
                }
            } else {
                log.warn("Matcher not found for {} class.", callingClass);
                throw new RuntimeException("No match found. Response error in class " + callingClass + ".");
            }
        }

        return extractedGroups;
    }
}
