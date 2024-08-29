package com.nikguscode.SusuAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Percentages {
    @JsonProperty("first-semester-percentage")
    private Double firstSemesterPercentage;
    @JsonProperty("total-percentage")
    private Double totalPercentage;
}
