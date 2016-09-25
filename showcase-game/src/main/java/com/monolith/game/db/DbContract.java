package com.monolith.game.db;

import android.provider.BaseColumns;

public class DbContract {

    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String COMMA_SEP = ",";
    public static final String TEXT = " TEXT";
    public static final String INTEGER = " INTEGER";
    public static final String PRIMARY_KEY = " PRIMARY KEY";

    public static String getDropQuery(String tableName) {
        StringBuilder builder = new StringBuilder();
        builder.append("DROP TABLE IF EXISTS ").append(tableName);
        return builder.toString();
    }

    public static abstract class Scores implements BaseColumns {
        public static final String TABLE_NAME = "high_scores";
        public static final String COLUMN_NAME_SCORE = "score";

        public static String getCreateQuery() {
            return getCreateQuery(TABLE_NAME);
        }

        protected static String getCreateQuery(String tableName) {
            StringBuilder builder = new StringBuilder();
            builder.append(CREATE_TABLE).append(tableName).append(" (")
                    .append(_ID).append(INTEGER).append(PRIMARY_KEY).append(COMMA_SEP)
                    .append(COLUMN_NAME_SCORE).append(INTEGER)
                    .append(" )");
            return builder.toString();
        }
    }
}
