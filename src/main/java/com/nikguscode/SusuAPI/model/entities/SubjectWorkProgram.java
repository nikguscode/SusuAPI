package com.nikguscode.SusuAPI.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubjectWorkProgram {
    @JsonProperty("subject-site-id")
    private String subjectSiteId;

    @JsonProperty("semester")
    private int semester;

    @JsonProperty("control-type")
    private String controlType;

    @JsonProperty("control-event-name")
    private String controlEventName;

    @JsonProperty("rating-weight")
    private double ratingWeight;

    @JsonProperty("maximum-score")
    private double maximumScore;

    @JsonProperty("academic-points-criteria")
    private String academicPointsCriteria;

    @JsonProperty("assessment-type")
    private String assessmentType;
}
