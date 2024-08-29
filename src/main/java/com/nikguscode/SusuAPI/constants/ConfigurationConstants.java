package com.nikguscode.SusuAPI.constants;

/**
 * A class that contains constants for working with a database that contains the actual variable names for the source site
 *
 * @author nikguscode
 * @since 27/07/2024
 */
public class ConfigurationConstants {

    /**
     * Variables which contains names of table
     */
    public static final String AUTH_TABLE_DB = "config.auth";
    public static final String PARSER_TABLE_DB = "config.parser";
    public static final String DISCIPLINE_TABLE_DB = "config.discipline";
    public static final String REGEX_TABLE_DB = "config.regex";

    /**
     * Variables which contains id of each parser in parser table
     */
    public static final String MAIN_PAGE_ROW_DB = "main_page";
    public static final String SUBJECT_WORK_PROGRAM_ROW_DB = "subject_work_program";
    public static final String SUBJECT_PERCENTAGE_ROW_DB = "subject_percentage";
    public static final String SUBJECT_GRADE_ROW_DB = "subject_grade";
    public static final String STUDY_PLAN_ROW_DB = "study_plan";

    /**
     * Variables which contains names of table columns in database or variables names for each table
     */

    // auth table
    public static final String CSRF_DB = "csrf_var";
    public static final String USERNAME_DB = "username_var";
    public static final String PASSWORD_DB = "password_var";

    // many tables
    public static final String URL_DB = "url_var";

    // parser table
    public static final String REGEX_ID_DB = "regex_id";
    public static final String DX_CALLBACK_DB = "dx_callback_var";
    public static final String DX_CALLBACK_VALUE_DB = "dx_callback_val";

    /// percentage parser
    public static final String FIRST_SEMESTER_PERCENTAGE = "firstSemesterPercentage";
    public static final String TOTAL_PERCENTAGE = "totalPercentage";

    // discipline table
    public static final String SUBJECT_NAME_DB = "subject_name";
    public static final String SUBJECT_ID_DB = "subject_id";
    public static final String STUDENT_GROUP_DB = "student_group";
    public static final String HTML_PAGE_DB = "html_page";

    // regex table
    public static final String PARSER_ID_DB = "parser_id";
    public static final String PATTERN_DB = "pattern";
    public static final String PATTERN_ID_DB = "pattern_id";

    /**
     * Variables which contains id of all regex patterns
     */
    public static final String STUDY_PLAN_PATTERN_DB = "study_plan";
    public static final String SUBJECT_GRADE_PATTERN_DB = "subject_grade";
    public static final String TOTAL_SUBJECT_PERCENTAGE_PATTERN_DB = "total_subject_percentage";
    public static final String BY_SUBJECT_PERCENTAGE_PATTERN_DB = "by_subject_percentage";
    public static final String SUBJECT_WORK_PROGRAM_PATTERN_DB = "subject_work_program";
    public static final String HYPHEN_WITH_SPACE_BETWEEN_LETTERS_PATTERN_DB = "hyphen_with_space_between_letters";
    public static final String SUBJECT_NAME_WITH_CODE_PATTERN_DB = "subject_name_with_code";
    public static final String CODE_REMOVER_PATTERN_DB = "code_remover";
}
