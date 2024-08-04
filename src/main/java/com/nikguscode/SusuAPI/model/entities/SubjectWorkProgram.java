package com.nikguscode.SusuAPI.model.entities;

public class SubjectWorkProgram {
    private final String id;
    private final String subjectName;
    private final int semester;
    private final String controlType;
    private final String controlEventName;
    private final double ratingWeight;
    private final double maximumScore;
    private final String academicPointsCriteria;
    private final String assessmentType;

    public SubjectWorkProgram(String id, String subjectName, int semester, String controlType, String controlEventName,
                              double ratingWeight, double maximumScore, String academicPointsCriteria, String assessmentType) {
        this.id = id;
        this.subjectName = subjectName;
        this.semester = semester;
        this.controlType = controlType;
        this.controlEventName = controlEventName;
        this.ratingWeight = ratingWeight;
        this.maximumScore = maximumScore;
        this.academicPointsCriteria = academicPointsCriteria;
        this.assessmentType = assessmentType;
    }

    public String getId() {
        return id;
    }

    public String getSubjectName() { return subjectName; }

    public int getSemester() {
        return semester;
    }

    public String getControlType() {
        return controlType;
    }

    public String getControlEventName() {
        return controlEventName;
    }

    public double getRatingWeight() {
        return ratingWeight;
    }

    public double getMaximumScore() {
        return maximumScore;
    }

    public String getAcademicPointsCriteria() {
        return academicPointsCriteria;
    }

    public String getAssessmentType() {
        return assessmentType;
    }
}
