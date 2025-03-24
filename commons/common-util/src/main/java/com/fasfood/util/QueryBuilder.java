package com.fasfood.util;

import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder {

    @Getter
    @RequiredArgsConstructor
    private enum JoinType {
        JOIN("JOIN"),
        LEFT_JOIN("LEFT JOIN"),
        RIGHT_JOIN("RIGHT JOIN"),
        INNER_JOIN("INNER JOIN"),
        FULL_JOIN("FULL JOIN");

        private final String value;
    }

    private final StringBuilder query;
    // Retrieve parameters for query execution
    @Getter
    private final Map<String, Object> parameters;
    private Integer page = 1;   // Default page is 1
    private Integer size = 10;  // Default size is 10
    private String aliasTableName = "default_alias";
    private static final String ALIAS_LIST = "list_";

    private QueryBuilder(String baseQuery, String aliasTableName) {
        this.query = new StringBuilder(baseQuery);
        if (aliasTableName != null) {
            this.aliasTableName = aliasTableName;
        }
        this.parameters = new HashMap<>();
    }

    // Start building query for SELECT
    public static QueryBuilder select(List<String> columns, String tableName, String aliasTableName) {
        if (tableName == null) {
            throw new NullPointerException("tableName is null");
        }
        if (aliasTableName == null) aliasTableName = tableName;
        return new QueryBuilder("SELECT " + String.join(", ", columns) + " FROM " + tableName + " " + aliasTableName + " "
                , aliasTableName);
    }

    public static QueryBuilder select(List<String> columns, String tableName) {
        return select(columns, tableName, null);
    }

    public static QueryBuilder select(String query, String tableName, String aliasTableName) {
        return select(List.of(query), tableName, aliasTableName);
    }

    public static QueryBuilder select(String query, String tableName) {
        return select(List.of(query), tableName, null);
    }

    public static QueryBuilder selectAllFrom(String tableName) {
        return selectAllFrom(tableName, null);
    }

    public static QueryBuilder selectCountFrom(String tableName) {
        return selectCountFrom(tableName, null);
    }

    public static QueryBuilder selectAllFrom(String tableName, String aliasTableName) {
        return select(List.of(tableName), tableName, aliasTableName);
    }

    public static QueryBuilder selectCountFrom(String tableName, String aliasTableName) {
        return select(List.of("COUNT("+aliasTableName+".id)"), tableName, aliasTableName);
    }

    // Add JOIN clause
    private QueryBuilder join(JoinType joinType, String tableName, String aliasTableName, String onLeftTableName, String onRightTableName) {
        if (tableName == null || aliasTableName == null || onLeftTableName == null || onRightTableName == null) {
            throw new NullPointerException("tableName or aliasTableName or onLeftTableName or onRightTableName is null");
        }
        this.query.append(" ").append(joinType.getValue()).append(" ").append(tableName).append(" ").append(aliasTableName)
                .append(" ON ").append(this.aliasTableName).append(".").append(onLeftTableName).append(" = ")
                .append(aliasTableName).append(".").append(onRightTableName);
        return this;
    }

    public QueryBuilder join(String tableName, String aliasTableName, String onLeftTableName, String onRightTableName) {
        return join(JoinType.JOIN, tableName, aliasTableName, onLeftTableName, onRightTableName);
    }

    public QueryBuilder leftJoin(String tableName, String aliasTableName, String onLeftTableName, String onRightTableName) {
        return join(JoinType.LEFT_JOIN, tableName, aliasTableName, onLeftTableName, onRightTableName);
    }

    public QueryBuilder rightJoin(String tableName, String aliasTableName, String onLeftTableName, String onRightTableName) {
        return join(JoinType.RIGHT_JOIN, tableName, aliasTableName, onLeftTableName, onRightTableName);
    }

    public QueryBuilder innerJoin(String tableName, String aliasTableName, String onLeftTableName, String onRightTableName) {
        return join(JoinType.INNER_JOIN, tableName, aliasTableName, onLeftTableName, onRightTableName);
    }

    public QueryBuilder fullJoin(String tableName, String aliasTableName, String onLeftTableName, String onRightTableName) {
        return join(JoinType.FULL_JOIN, tableName, aliasTableName, onLeftTableName, onRightTableName);
    }

    // Add conditions to the WHERE clause
    public QueryBuilder where(String column, Object value) {
        if (value != null) {
            String paramName = String.join("", column.split("\\."));
            String condition = column + " = :" + paramName;
            this.appendWhereClause(condition);
            this.parameters.put(paramName, value);
        }
        return this;
    }

    // Add WHERE IN condition
    public <T> QueryBuilder whereIn(String column, List<T> values) {
        if (values != null && !values.isEmpty()) {
            String condition = column + " IN (:" + ALIAS_LIST + column + ")";
            this.appendWhereClause(condition);
            this.parameters.put(ALIAS_LIST + column, values);
        }
        return this;
    }

    // Add WHERE NOT IN condition
    public <T> QueryBuilder whereNotIn(String column, List<T> values) {
        if (values != null && !values.isEmpty()) {
            String condition = column + " NOT IN (:" + ALIAS_LIST + column + ")";
            this.appendWhereClause(condition);
            this.parameters.put(ALIAS_LIST + column, values);
        }
        return this;
    }

    public QueryBuilder whereTime(String column, Object startValue, Object endValue) {
        if (startValue != null && endValue != null) {
            String condition = this.aliasTableName + "." + column + " BETWEEN :" + column + "Start AND :" + column + "End";
            this.appendWhereClause(condition);
            this.parameters.put(column + "Start", startValue);
            this.parameters.put(column + "End", endValue);
        }
        return this;
    }

    // Handling LIKE clause for case-insensitive searches with unaccent if needed
    public QueryBuilder like(String column, String value) {
        return this.like(List.of(column), value);
    }

    public QueryBuilder like(List<String> columns, String value) {
        if (columns == null || columns.isEmpty() || value == null || value.trim().isEmpty()) {
            return this;
        }
        // Bắt đầu xây dựng câu lệnh với LOWER và UNACCENT
        StringBuilder condition = new StringBuilder("LOWER(CAST(UNACCENT(");
        // Sử dụng CONCAT để nối các cột
        condition.append("CONCAT(");
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                condition.append(", ' ', ");
            }
            condition.append("COALESCE(CAST(").append(columns.get(i)).append(" AS String), '')"); // Thêm COALESCE để thay thế NULL
        }
        condition.append(")");

        // Kết thúc hàm UNACCENT và LOWER
        condition.append(") AS String))");

        // Thêm mệnh đề LIKE
        condition.append(" LIKE CONCAT('%', CAST(UNACCENT(:")
                .append("keyword")
                .append(") AS String), '%') ");

        // Thêm giá trị tham số
        this.parameters.put("keyword", value.trim().toLowerCase());

        // Append vào WHERE clause
        this.appendWhereClause(condition.toString());
        return this;
    }

    // Add ORDER BY clause, defaulting to "id DESC"
    public QueryBuilder orderBy(String sortBy) {
        String sortColumn = this.aliasTableName + ".lastModifiedAt";
        String sortDirection = PagingQuery.DESC_SYMBOL;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            String[] sortClause = sortBy.trim().split("\\.");

            // Determine the sort column, defaulting to "createdAt" if sortClause[0] is empty
            if (sortClause.length > 0 && !sortClause[0].isEmpty()) {
                sortColumn = this.aliasTableName + "." + sortClause[0];
            }
            // Determine the sort direction, defaulting to DESC
            if (sortClause.length > 1 && PagingQuery.ASC_SYMBOL.equalsIgnoreCase(sortClause[1])) {
                sortDirection = PagingQuery.ASC_SYMBOL;
            }
        }

        // Append the ORDER BY clause to the query
        this.query.append(" ORDER BY ")
                .append(sortColumn)
                .append(" ")
                .append(sortDirection);

        return this;
    }

    // group by clause
    public QueryBuilder groupBy(List<String> columns) {
        if (columns != null && !columns.isEmpty()) {
            List<String> columnsList = new ArrayList<>(columns);
            this.query.append(" GROUP BY ");
            String joinedColumns = String.join(", ", columnsList);
            this.query.append(joinedColumns);
        }
        return this;
    }

    // Utility function to add clauses to the WHERE section
    private void appendWhereClause(String clause) {
        if (this.query.toString().contains("WHERE")) {
            this.query.append(" AND ").append(clause);
        } else {
            this.query.append(" WHERE ").append(this.aliasTableName).append(".").append(clause);
        }
    }

    public void appendClause(String clause) {
        if (clause != null && !clause.trim().isEmpty()) {
            this.query.append(" ").append(clause);
        }
    }

    // LIMIT clause
    public QueryBuilder limit(Integer page, Integer size) {
        if (page != null) {
            this.page = page;
        }
        if (size != null) {
            this.size = size;
        }
        int offset = (this.page - 1) * this.size;
        this.query.append(" LIMIT ").append(this.size).append(" OFFSET ").append(offset);
        return this;
    }

    // Build the final query string
    public String build() {
        return this.query.toString();
    }
}
