package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.CwZzlData;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 * @Author thr
 * @create 2023-01-18 11:00:08
 */
@Repository
public class CwZzlDataDao extends BaseDao<CwZzlData> {

    public String toList(CwZzlData beanParam) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM cwzzl_data");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }

    /**
     * 最近7天周转率
     */
    public String getDaysZzlList(CwZzlData beanParam) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT isnull( b.zzl, 0 ) AS zzl,c.click_date as time  " +
                " FROM ( ");
        for (int i = 6; i >= 0; i--) {
            sqlBuffer.append(" SELECT CONVERT( VARCHAR ( 100 ), dateadd( DAY, -" + i + ", getdate()), 23 )").append(" AS click_date");
            if (i > 0) {
                sqlBuffer.append("  UNION ALL ");
            }
        }

        sqlBuffer.append(" ) c  ");

        sqlBuffer.append(" LEFT JOIN ( " +
                " select " +
                " MAX ( zzl ) zzl," +
                " time " +
                " from ");
        sqlBuffer.append(" cwzzl_data ");
        sqlBuffer.append(" where is_del = 0 ");
        if (notEmpty(beanParam)) {
//            if (notEmpty(beanParam.getTime())) {
//                sqlBuffer.append(" and time = '").append(beanParam.getTime()).append("'");
//            }

            sqlBuffer.append(" and time >= '").append(new DateTime().plusDays(-6).toString("yyyy-MM-dd")).append("'");
            sqlBuffer.append(" and time <= '").append(new DateTime().toString("yyyy-MM-dd")).append("'");
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
        sqlBuffer.append(" GROUP BY time " +
                " ) b ");

        sqlBuffer.append(" ON c.click_date = b.time");
        return sqlBuffer.toString();
    }

}
