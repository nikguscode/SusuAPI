package com.nikguscode.SusuAPI.model.service.parsers;

import org.springframework.stereotype.Service;

@Service
public interface ParserInterface {
    String execute(String cookie, String link);
}
