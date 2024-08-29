package com.nikguscode.SusuAPI.enumirations;

import lombok.Getter;

@Getter
public enum PercentageRequestType {
    UNDEFINED("undefined"), TOTAL("total"), BY_SUBJECT("by-subject");

    private final String value;

    PercentageRequestType(String value) {
        this.value = value;
    }
}
