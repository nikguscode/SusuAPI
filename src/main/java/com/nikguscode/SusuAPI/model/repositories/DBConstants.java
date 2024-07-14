package com.nikguscode.SusuAPIs.model.repositories;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConstants {
    public static final String CSRF_VAR = "csrfVariable";
    public static final String URL_VAR = "urlVariable";
    public static final String USERNAME_VAR = "userNameVariable";
    public static final String PASSWORD_VAR = "passwordVariable";
    public static final String FIND_PATTERN = "findPattern";

    public static final String C_AUTH_TABLE = "config.auth";
    public static final String C_GRADE_TABLE = "config.subject_grade";
    public static final String C_PERCENTAGE_TABLE = "config.subject_percentage";

    public static final String CSRF_DB = "csrf_var";
    public static final String USERNAME_DB = "username_var";
    public static final String PASSWORD_DB = "password_var";
    public static final String URL_DB = "url_var";
    public static final String FIND_PATTERN_DB = "find_pattern";
}
