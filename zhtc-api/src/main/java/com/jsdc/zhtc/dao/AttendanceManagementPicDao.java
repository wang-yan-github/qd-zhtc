package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.AttendanceManagementPic;
import org.springframework.stereotype.Repository;

/**
 * @Author thr
 * @create 2022-08-30 10:38:29
 */
@Repository
public class AttendanceManagementPicDao extends BaseDao<AttendanceManagementPic> {

    public String toList(AttendanceManagementPic beanParam) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM attendance_management_pic");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
