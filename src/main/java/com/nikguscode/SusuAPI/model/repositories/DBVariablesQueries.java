package com.nikguscode.SusuAPI.model.repositories;

import org.springframework.stereotype.Service;
import static com.nikguscode.SusuAPI.model.repositories.DBVariablesConstants.*;

@Service
public class DBVariablesQueries {
    private String currentVariableStrategy;

    public DBVariablesQueries() {
    }

    public DBVariablesQueries(String currentVariableStrategy) {
        this.currentVariableStrategy = currentVariableStrategy;
    }

    protected StringBuilder createSelectQuery(String table, String dbName) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ").append(table);
        queryBuilder.append(" FROM ").append(dbName);

        return queryBuilder;
    }

    protected String executeSelectQuery(String table) {
        System.out.println(createSelectQuery(table, VARIABLES_TABLE)
                .append(" WHERE id = ")
                .append(currentVariableStrategy)
                .toString());
        return createSelectQuery(table, VARIABLES_TABLE)
                .append(" WHERE id = ")
                .append(currentVariableStrategy)
                .toString();
    }

    protected String executeSelectQuery(String table, String dbName) {
        return createSelectQuery(table, dbName).toString();
    }
}
