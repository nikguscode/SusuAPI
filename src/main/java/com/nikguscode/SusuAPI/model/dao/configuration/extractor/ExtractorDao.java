package com.nikguscode.SusuAPI.model.dao.configuration.extractor;

import com.nikguscode.SusuAPI.model.entities.configuration.Extractor;
import org.springframework.stereotype.Service;

@Service
public interface ExtractorDao {
    Extractor get(String id);
}
