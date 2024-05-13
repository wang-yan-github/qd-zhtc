package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.Screen;
import org.springframework.stereotype.Repository;

/**
 * 诱导屏管理
 *
 * @Author thr
 * @create 2022-11-17 14:13:33
 */
@Repository
public class ScreenDao extends BaseDao<Screen> {

    public String toList(Screen beanParam) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.* FROM screen t");
        sql.append(" where t.is_del = 0 ");
        if (notEmpty(beanParam)) {
            if (notEmpty(beanParam.getCode())) {
                sql.append(" and t.code like '%").append(beanParam.getCode()).append("%'");
            }
            if (notEmpty(beanParam.getName())) {
                sql.append(" and t.name like '%").append(beanParam.getName()).append("%'");
            }
        }
        sql.append(" order by t.create_time desc");
        return sql.toString();
    }
}
