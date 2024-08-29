package com.nikguscode.SusuAPI.model.dao.configuration.regex;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;

@Service
public interface RegexDao {
    Map<String, String> get(UUID id);
}
