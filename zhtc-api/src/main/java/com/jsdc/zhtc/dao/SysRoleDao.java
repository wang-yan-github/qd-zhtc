package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.SysRole;
import org.springframework.stereotype.Repository;

/**
 * ClassName: SysRoleDao
 * Description:
 * date: 2021/12/30 9:59
 *
 * @author wp
 */
@Repository
public class SysRoleDao extends BaseDao<SysRole> {

    /**
     * create by wp at 2022/1/8 10:42
     * description: 查询
     *
     * @param roleId
     * @return java.lang.String
     */
    public String getMenusByRoleId(Integer roleId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.menu_name, m.id ");
        sql.append(" from sys_role r");
        sql.append(" LEFT JOIN sys_role_menu rm on r.id = rm.role_id");
        sql.append(" LEFT JOIN sys_menu m on rm.menu_id = m.id");
        sql.append(" where 1=1");
        sql.append(" and r.id = " + roleId);
        sql.append(" and rm.is_del = '0'");
        return sql.toString();
    }
}
