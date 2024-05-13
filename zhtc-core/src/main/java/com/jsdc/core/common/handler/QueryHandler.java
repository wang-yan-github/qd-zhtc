package com.jsdc.core.common.handler;

import com.jsdc.core.common.utils.BaseUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryHandler extends BaseUtils {

    public static final String COUNT_SQL = "select count(*) ";
    public static final String KEYWORD_FROM = " from ";
    public static final String KEYWORD_ORDER = " order by ";
    public static final String KEYWORD_GROUP = " group by ";
    public static final String KEYWORD_WHERE = " where ";

    private boolean whereFlag = true;
    private boolean orderFlag = true;
    private boolean groupFlag = true;

    private StringBuilder sqlBuilder;
    private Map<String, Object> map;
    private Map<String, List<Object>> arrayMap;

    private Integer firstResult;
    private Integer maxResults;

    public QueryHandler(String sql) {
        this.sqlBuilder = new StringBuilder(" ");
        sqlBuilder.append(sql);
    }

    public QueryHandler() {
        this.sqlBuilder = new StringBuilder();
    }

    public Query getQuery(EntityManager entityManager) {
        return getQuery(entityManager, getSql());
    }

    public Query getCountQuery(EntityManager entityManager) {
        return getQuery(entityManager, getCountSql());
    }

    public QueryHandler condition(String condition) {
        if (whereFlag) {
            whereFlag = false;
            sqlBuilder.append(" where ");
        } else {
            sqlBuilder.append(" and ");
        }
        sqlBuilder.append(condition);
        return this;
    }

    public QueryHandler order(String sqlString) {
        if (orderFlag) {
            orderFlag = false;
            append(KEYWORD_ORDER);
        } else {
            sqlBuilder.append(',');
        }
        sqlBuilder.append(sqlString);
        return this;
    }

    public QueryHandler group(String sqlString) {
        if (groupFlag) {
            groupFlag = false;
            sqlBuilder.append(KEYWORD_GROUP);
        } else {
            sqlBuilder.append(',');
        }
        sqlBuilder.append(sqlString);
        return this;
    }

    public QueryHandler append(String sqlString) {
        sqlBuilder.append(" ");
        sqlBuilder.append(sqlString);
        return this;
    }

    public QueryHandler setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public QueryHandler setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public QueryHandler setParameter(String key, Object value) {
        if (empty(map)) {
            map = new HashMap<>();
        }
        map.put(key, value);
        return this;
    }

    public QueryHandler setParameter(String key, Object[] value) {
        if (empty(arrayMap)) {
            arrayMap = new HashMap<>();
        }
        List<Object> objectList = new ArrayList<>();
        for (Object o : value) {
            objectList.add(o);
        }
        arrayMap.put(key, objectList);
        return this;
    }

    private Query getQuery(EntityManager entityManager, String sql) {
        Query query = entityManager.createQuery(sql);
        if (notEmpty(map)) {
            for (String key : map.keySet()) {
                query.setParameter(key, map.get(key));
            }
        }
        if (notEmpty(arrayMap)) {
            for (String key : arrayMap.keySet()) {
                query.setParameter(key, arrayMap.get(key));
            }
        }
        if (notEmpty(firstResult)) {
            query.setFirstResult(firstResult);
        }
        if (notEmpty(maxResults)) {
            query.setMaxResults(maxResults);
        }
        return query;
    }


    public String getSql() {
        String sql = sqlBuilder.toString();
        if (notEmpty(map)) {
            for (String key : map.keySet()) {
                sql = sql.replaceFirst(":" + key, map.get(key).toString());
            }
        }
        if (notEmpty(arrayMap)) {
            for (String key : arrayMap.keySet()) {
                sql = sql.replaceFirst(":" + key, arrayMap.get(key).toString());
            }
        }
        return sql;
    }

    public String getCountSql() {
        String sql = getSql();
        sql = sql.substring(sql.toLowerCase().indexOf(KEYWORD_FROM));
        int groupIndex = sql.toLowerCase().indexOf(KEYWORD_GROUP);
        int orderIndex = sql.toLowerCase().indexOf(KEYWORD_ORDER);
        if (-1 != groupIndex) {
            sql = sql.substring(0, groupIndex);
        }
        if (-1 != orderIndex) {
            sql = sql.substring(0, orderIndex);
        }
        return COUNT_SQL + sql;
    }
}
