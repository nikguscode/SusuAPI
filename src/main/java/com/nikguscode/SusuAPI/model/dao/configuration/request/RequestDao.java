package com.nikguscode.SusuAPI.model.dao.configuration.request;

import com.nikguscode.SusuAPI.model.entities.configuration.Request;
import org.springframework.stereotype.Service;

@Service
public interface RequestDao {
    Request get(String id);
}
