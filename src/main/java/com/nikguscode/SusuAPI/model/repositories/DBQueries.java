package com.nikguscode.SusuAPI.model.repositories;

import org.springframework.stereotype.Service;

@Service
public class DBQueries {
    protected String createSelectQuery(String table, String dbName) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ").append(table);
        queryBuilder.append(" FROM ").append(dbName);

        return queryBuilder.toString();
    }
}
