package com.nikguscode.SusuAPI.model.dao.configuration.variable;

import java.util.Map;
import java.util.UUID;

public interface VariableDao {
    Map<String, String> get(UUID entityId);
}
