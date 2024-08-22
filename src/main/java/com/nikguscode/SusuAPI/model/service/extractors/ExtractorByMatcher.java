package com.nikguscode.SusuAPI.model.service.extractors;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

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
            return matcher.group().trim();
        } else {
            log.warn("Matcher not found for {} class.", callingClass);
            return "Matcher not found";
        }
    }
}
