package com.nikguscode.SusuAPI.model.dao.configuration.authenticatioin;

import com.nikguscode.SusuAPI.model.entities.configuration.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationDao {
    Authentication get();
}
