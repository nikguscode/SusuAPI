package com.nikguscode.SusuAPI.constants;

/**
 * A class that contains constants for working with a database that contains the actual variable names for the source site
 *
 * @author nikguscode
 * @since 27/07/2024
 */
public class ConfigConstants {
    /**
     * Variables which contains names of table
     */
    public static final String AUTH_TABLE = "config.auth";
    public static final String VARIABLES_TABLE = "config.parser";
    public static final String DISCIPLINE_TABLE = "config.discipline";

    /**
     * Variables which contains id of each parser
     */
    public static final String SUBJECT_WORK_PROGRAM_ROW = "subject_work_program";
    public static final String SUBJECT_PERCENTAGE_ROW = "subject_percentage";
    public static final String SUBJECT_GRADE_ROW = "subject_grade";
    public static final String STUDY_PLAN_ROW = "study_plan";

    /**
     * Variables which contains names of table columns, also these values are keys
     */
    public static final String CSRF_DB = "csrf_var";
    public static final String USERNAME_DB = "username_var";
    public static final String PASSWORD_DB = "password_var";
    public static final String URL_DB = "url_var";
    public static final String FIND_PATTERN_DB = "find_pattern";
    public static final String DX_CALLBACK_DB = "dx_callback_var";
    public static final String DX_CALLBACK_VALUE_DB = "dx_callback_val";
    public static final String SUBJECT_NAME_DB = "subject_name";
    public static final String SUBJECT_ID_DB = "subject_id";
    public static final String STUDENT_GROUP_DB = "student_group";
}
