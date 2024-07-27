package com.nikguscode.SusuAPI.model.repositories;

import org.springframework.context.annotation.Configuration;

/**
 * A class that contains constants for working with a database that contains the actual variable names for the source site
 *
 * @author nikguscode
 * @since 27/07/2024
 */
@Configuration
public class DBVariablesConstants {
    /**
     * Variables which contains names of keys for every table column. A single key is sufficient for each column, since
     * a single column can only contain one value
     */
    public static final String CSRF_VAR = "csrfVariable";
    public static final String URL_VAR = "urlVariable";
    public static final String USERNAME_VAR = "userNameVariable";
    public static final String PASSWORD_VAR = "passwordVariable";
    public static final String FIND_PATTERN = "findPattern";
    public static final String DX_CALLBACK_VAR = "dxCallback";
    public static final String DX_CALLBACK_VAL = "dxCallbackValue";

    /**
     * Variables which contains names of table
     */
    public static final String C_AUTH_TABLE = "config.auth";
    public static final String C_GRADE_TABLE = "config.subject_grade";
    public static final String C_PERCENTAGE_TABLE = "config.subject_percentage";

    /**
     * Variables which contains names of table columns
     */
    public static final String CSRF_DB = "csrf_var";
    public static final String USERNAME_DB = "username_var";
    public static final String PASSWORD_DB = "password_var";
    public static final String URL_DB = "url_var";
    public static final String FIND_PATTERN_DB = "find_pattern";
    public static final String DX_CALLBACK_DB = "dx_callback_var";
    public static final String DX_CALLBACK_VALUE_DB = "dx_callback_val";
}
