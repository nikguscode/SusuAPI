package com.nikguscode.SusuAPI.exceptions;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NullChecker {
    public static void checkNotNull(Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (entry.getValue() == null) {
                throw new NullPointerException(entry.getKey() + " is null!");
            }
        }
    }
}
