package com.nikguscode.SusuAPI.model.dao.configuration.parser;

import com.nikguscode.SusuAPI.model.entities.configuration.Parser;
import org.springframework.stereotype.Service;

@Service
public interface ParserDao {
    Parser get(String id);
}
