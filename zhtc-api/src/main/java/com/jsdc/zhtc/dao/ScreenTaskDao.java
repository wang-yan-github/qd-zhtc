package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.ScreenTask;
import org.springframework.stereotype.Repository;

/**
 * @projectName: zhtc
 * @className: ScreenTaskDao
 * @author: wp
 * @description:
 * @date: 2023/3/24 10:47
 */
@Repository
public class ScreenTaskDao extends BaseDao<ScreenTask> {

    public String getPage(Integer screenId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT s.*, m.title, m.type ");
        sql.append(" FROM ");
        sql.append(" screen_task s ");
        sql.append(" LEFT JOIN sys_materia m on s.materia_id = m.id ");
        sql.append(" where s.screen_id = " + screenId);
        sql.append(" and s.is_del = 0 ");
        return sql.toString();
    }
}
