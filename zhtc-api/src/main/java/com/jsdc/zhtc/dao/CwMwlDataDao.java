package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.CwMwlData;
import org.springframework.stereotype.Repository;

/**
 * @Author thr
 * @create 2023-01-17 16:42:01
 */
@Repository
public class CwMwlDataDao extends BaseDao<CwMwlData> {

    public String toList(CwMwlData beanParam) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM cwmwl_data");
        sql.append(" where is_del = 0 ");
        if (notEmpty(beanParam)) {
            if (notEmpty(beanParam.getTime())) {
                sql.append(" and time = '").append(beanParam.getTime()).append("'");
            }
            // 类型 0路测 1停车场 2（路测+停车场）总和
            if (notEmpty(beanParam.getType())) {
                sql.append(" and type = '").append(beanParam.getType()).append("'");
            }
            // 路段/停车场id
            if (notEmpty(beanParam.getRoad_park_id())) {
                sql.append(" and road_park_id = ").append(beanParam.getRoad_park_id());
            } else {
                sql.append(" and road_park_id is null");
            }
        }
        return sql.toString();
    }

    public String getHourMwlList(CwMwlData beanParam) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT isnull( b.mwl, 0 ) AS mwl,c.click_date as hour  " +
                " FROM ( ");
        for (int i = 0; i <= 23; i++) {
            sqlBuffer.append(" SELECT '").append(i).append("' AS click_date");
            if (i < 23) {
                sqlBuffer.append("  UNION ALL ");
            }
        }

        sqlBuffer.append(" ) c  ");

        sqlBuffer.append(" LEFT JOIN ( " +
                " select " +
                " MAX ( mwl ) mwl," +
                " hours " +
                " from ");
        sqlBuffer.append(" cwmwl_data ");
        sqlBuffer.append(" where is_del = 0 ");
        if (notEmpty(beanParam)) {
            if (notEmpty(beanParam.getTime())) {
                sqlBuffer.append(" and time = '").append(beanParam.getTime()).append("'");
            }
            // 类型 0路测 1停车场 2（路测+停车场）总和
            if (notEmpty(beanParam.getType())) {
                sqlBuffer.append(" and type = '").append(beanParam.getType()).append("'");
            }
            // 路段/停车场id
            if (notEmpty(beanParam.getRoad_park_id())) {
                sqlBuffer.append(" and road_park_id = ").append(beanParam.getRoad_park_id());
            } else {
                sqlBuffer.append(" and road_park_id is null");
            }
        }
        sqlBuffer.append(" GROUP BY hours " +
                " ) b ");

        sqlBuffer.append(" ON c.click_date = b.hours");
        sqlBuffer.append(" ORDER BY CONVERT(INT, LEFT(c.click_date, LEN(c.click_date)-1)) ASC, RIGHT(c.click_date, 1)");
        return sqlBuffer.toString();
    }

}
