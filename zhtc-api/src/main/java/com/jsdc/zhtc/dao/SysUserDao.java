package com.jsdc.zhtc.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserDao extends BaseDao<SysUser> {


    public QueryWrapper<SysUser> queryByName(String name, String pass) {
        QueryWrapper<SysUser> queryHandler = new QueryWrapper<>();
        if (notEmpty(name)) {
            queryHandler.eq("login_name", name);
        }
        if (notEmpty(pass)) {
            queryHandler.eq("password", pass);
        }
        queryHandler.eq("is_del", "0");
        queryHandler.eq("status", "1");
        return queryHandler;
    }

    public String getRoles(int userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" sr.id,");
        sql.append(" sr.role_name");
        sql.append(" FROM");
        sql.append(" sys_user u");
        sql.append(" LEFT JOIN sys_user_role r ON u.id = r.user_id");
        sql.append(" LEFT JOIN sys_role sr ON sr.id = r.role_id");
        sql.append(" where 1=1");
        sql.append(" and u.id = " + userId);
        sql.append(" and r.is_del = '0' ");
        return sql.toString();
    }
}
