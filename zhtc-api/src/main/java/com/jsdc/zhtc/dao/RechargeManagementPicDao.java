package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.RechargeManagementPic;
import org.springframework.stereotype.Repository;

/**
 * @Author thr
 * @create 2022-08-24 17:54:50
 */
@Repository
public class RechargeManagementPicDao extends BaseDao<RechargeManagementPic> {

    public String toList(RechargeManagementPic beanParam) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM attendance_management_pic");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
