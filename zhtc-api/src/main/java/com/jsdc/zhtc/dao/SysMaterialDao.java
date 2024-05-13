package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.SysMaterial;
import org.springframework.stereotype.Repository;

/**
 * 素材管理
 *
 * @Author thr
 * @create 2022-12-01 10:52:56
 */
@Repository
public class SysMaterialDao extends BaseDao<SysMaterial> {

    public String toList(SysMaterial beanParam) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM sys_materia");
        sql.append(" where is_del = 0 ");
        if (notEmpty(beanParam)) {
            if (notEmpty(beanParam.getTitle())) {
                sql.append(" and title like '%").append(beanParam.getTitle()).append("%'");
            }
            if (notEmpty(beanParam.getType())) {
                sql.append(" and type = '").append(beanParam.getType()).append("'");
            }
            if (notEmpty(beanParam.getState())) {
                sql.append(" and state = '").append(beanParam.getState()).append("'");
            }
        }
        return sql.toString();
    }
}
