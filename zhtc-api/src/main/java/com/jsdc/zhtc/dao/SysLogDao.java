package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.SysLog;
import org.springframework.stereotype.Repository;

/**
 * ClassName: SysLogDao
 * Description:
 * date: 2021/12/30 10:10
 *
 * @author wp
 */
@Repository
public class SysLogDao extends BaseDao<SysLog> {

    public String getPage(SysLog sysLog) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select l.*, u.user_name, u.role_name, u.type");
        sql.append(" from sys_log l");
        sql.append(" left join (");
        sql.append(" select u.id, user_name, '运营人员' role_name, '1' type");
        sql.append(" from sys_user u");
//        sql.append(" where u.is_del = '0'");
        sql.append(" union all");
        sql.append(" select i.id, i.name user_name, '巡检员' role_name,'2' type");
        sql.append(" from inspect_manage i");
//        sql.append(" where i.is_del = '0'");
        sql.append(" ) u on l.create_user = u.id and l.user_type = u.type");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(sysLog.getLog_type())) {
            sql.append(" and l.log_type = '" + sysLog.getLog_type() + "'");
        }
        if (StringUtils.isNotEmpty(sysLog.getLog_user_type())) {
            sql.append(" and u.type = '" + sysLog.getLog_user_type() + "'");
        }
        if (StringUtils.isNotEmpty(sysLog.getStart_time())) {
            sql.append(" and CONVERT(VARCHAR (10), l.create_time, 120 ) >= '" + sysLog.getStart_time() + "'");
        }
        if (StringUtils.isNotEmpty(sysLog.getEnd_time())) {
            sql.append(" and CONVERT(VARCHAR (10), l.create_time, 120 ) <= '" + sysLog.getEnd_time() + "'");
        }
        if (null != sysLog.getCreate_user()) {
            sql.append(" and l.create_user = " + sysLog.getCreate_user());
        }
        if (StringUtils.isNotEmpty(sysLog.getLog_content())) {
            sql.append(" and l.log_content like '%" + sysLog.getLog_content() + "%'");
        }
        sql.append(" and l.is_del = '0'");
        sql.append(" order by l.operation_time desc");
        return sql.toString();
    }

    public String getUserList() {
        String sql = "SELECT u.id,\n" +
                "       user_name,\n" +
                "       '运营人员' role_name,\n" +
                "       '1'    type\n" +
                "FROM sys_user u\n" +
                "WHERE u.is_del = '0'\n" +
                "UNION ALL\n" +
                "SELECT i.id,\n" +
                "       i.name user_name,\n" +
                "       '巡检员'  role_name,\n" +
                "       '2'    type\n" +
                "FROM inspect_manage i\n" +
                "WHERE i.is_del = '0'";
        return sql;
    }
}
