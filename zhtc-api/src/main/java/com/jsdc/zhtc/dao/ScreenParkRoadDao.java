package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.ScreenParkRoad;
import org.springframework.stereotype.Repository;

/**
 * @Author thr
 * @create 2022-11-17 14:13:33
 */
@Repository
public class ScreenParkRoadDao extends BaseDao<ScreenParkRoad> {

    public String toList(ScreenParkRoad beanParam) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM screen_park_road");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
