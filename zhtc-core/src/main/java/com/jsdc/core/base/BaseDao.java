package com.jsdc.core.base;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jsdc.core.common.handler.QueryHandler;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import static com.jsdc.core.common.utils.BaseUtils.empty;
import static com.jsdc.core.common.utils.BaseUtils.notEmpty;

/**
 * BaseService
 */
@Transactional
public class BaseDao<T> extends Base {

    private Class<T> clazz;

    private Class<T> getEntityClass() {
        return empty(clazz) ? this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0] : clazz;
    }

    private String propertyDescriptors(){
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(getEntityClass());
        String zd = "";
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (!propertyDescriptor.getName().equals("class")){
                zd += " bean." + propertyDescriptor.getName() + ",";
            }
        }
        return notEmpty(zd) ? zd.substring(0, zd.length() - 1) + " " : "";
    }

    private String getTableName(){
        this.clazz = getEntityClass();
        TableName table = clazz.getAnnotation(TableName.class);
        return notEmpty(table) ? table.value() : "";
    }

    private String getField(T bean) {
        String str = "";
        if (notEmpty(bean)) {
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Object value = null;
                try {
                    value = propertyDescriptor.getReadMethod().invoke(bean);
                    if (notEmpty(value) && !propertyDescriptor.getName().equals("class")) {
                        str += " AND bean." + propertyDescriptor.getName() + "=#{" + propertyDescriptor.getName() + "}";
                    }
                } catch (Throwable throwable) {
                    //e.printStackTrace();
                }
            }
        }
        return str.replaceFirst("AND", "") + " ";
    }


    public QueryHandler getQueryHandler() {
        return new QueryHandler();
    }


    public QueryHandler getQueryHandler(String sql) {
        if (notEmpty(sql) && !sql.toLowerCase().split("from")[0].contains("select")){
            return new QueryHandler("select " + propertyDescriptors() + sql);
        }
        return new QueryHandler(sql);
    }

    public QueryHandler getQueryHandler(T bean) {
        String sql = "from " + getTableName() + " bean ";
        if (notEmpty(getField(bean))){
            sql += " where" + getField(bean);
        } else {
            sql += getField(bean);
        }
        return new QueryHandler("select " + propertyDescriptors() + sql);
    }

    public QueryHandler getCountQueryHandler(String sql) {
        return getQueryHandler("select count(*)").append(sql);
    }


    public String like(String var) {
        return "%" + var + "%";
    }


    public String likeStart(String var) {
        return var + "%";
    }


    public String likeEnd(String var) {
        return "%" + var;
    }


    public String getEntitys(Serializable[] ids) {
        return getEntitys(ids, "id");
    }

    private String getEntitys(Serializable[] ids, String pk) {
        QueryHandler queryHandler = new QueryHandler();
        if (notEmpty(ids)) {
            queryHandler = getQueryHandler("select " + propertyDescriptors() +  " from ").append(getTableName() + " ").append(" bean");
            queryHandler.condition(" bean." + (notEmpty(pk) ? pk : "id")).append("in (:ids)").setParameter("ids", ids);
        }
        return queryHandler.getSql();
    }

    public String getEntity(Serializable id, String pk) {
        QueryHandler queryHandler = getQueryHandler("select " + propertyDescriptors() +  " from ").append(getTableName()).append("bean");
        queryHandler.condition(" bean." + (notEmpty(pk) ? pk : "id")).append("= :id").setParameter("id", id);
        return queryHandler.getSql();
    }

    public String count(QueryHandler queryHandler) {
        return queryHandler.getSql();
    }

}