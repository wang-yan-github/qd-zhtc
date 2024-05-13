package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.PatrolRechargeManagement;
import com.jsdc.zhtc.vo.PatrolRechargeManagementVo;
import org.springframework.stereotype.Repository;

/**
 * @author 王严
 * @version 1.0
 * @description: 巡检充值管理
 */
@Repository
public class PatrolRechargeManagementDao extends BaseDao<PatrolRechargeManagement> {

    public String listPatrolRechargeManagement(PatrolRechargeManagementVo vo) {
        String sql = "SELECT prm.id,\n" +
                "       prm.after_account_balance,\n" +
                "       prm.before_account_balance,\n" +
                "       CONVERT(varchar,prm.create_time,120) AS create_time,\n" +
                "       prm.create_user,\n" +
                "       prm.inject_id,\n" +
                "       prm.is_del,\n" +
                "       prm.receive_paper_invoice,\n" +
                "       prm.recharge_amount,\n" +
                "       prm.recharge_mode,\n" +
                "       CONVERT(varchar,prm.recharge_time,120) AS recharge_time,\n" +
                "       CONVERT(varchar,prm.update_time,120) AS update_time,\n" +
                "       prm.update_user,\n" +
                "       im.name,\n" +
                "       im.phone,\n" +
                "       im.logpwd,\n" +
                "       im.job_no\n" +
                "FROM dbo.patrol_recharge_management AS prm\n" +
                "         LEFT JOIN\n" +
                "     dbo.inspect_manage AS im\n" +
                "     ON\n" +
                "         prm.inject_id = im.id AND im.is_del = 0\n" +
                "WHERE prm.is_del = 0\n";
        sql = getSQuery(vo, sql);
        sql += "ORDER BY prm.recharge_time DESC";
        return sql;
    }

    public String countPatrolRechargeManagement(PatrolRechargeManagementVo vo) {
        String sql = "SELECT count(distinct im.name) AS recharge_pens, count(1) AS transactions_number, ISNULL(sum(prm.recharge_amount),0) AS recharge_amount\n" +
                "FROM dbo.patrol_recharge_management AS prm\n" +
                "         LEFT JOIN\n" +
                "     dbo.inspect_manage AS im\n" +
                "     ON\n" +
                "         prm.inject_id = im.id AND im.is_del = 0\n" +
                "WHERE prm.is_del = 0\n";
        sql = getSQuery(vo, sql);
        return sql;
    }

    public String countPatrolRechargeManagementTime(PatrolRechargeManagementVo vo) {
        String sql = "SELECT CONVERT(varchar,Min(prm.recharge_time),111)    AS recharge_time,\n" +
                "       count(distinct im.name)               AS recharge_pens,\n" +
                "       count(1)                 AS transactions_number,\n" +
                "       ISNULL(sum(prm.recharge_amount),0) AS recharge_amount\n" +
                "FROM dbo.patrol_recharge_management AS prm\n" +
                "         LEFT JOIN\n" +
                "     dbo.inspect_manage AS im\n" +
                "     ON\n" +
                "         prm.inject_id = im.id AND im.is_del = 0\n" +
                "WHERE prm.is_del = 0\n";
        sql = getSQuery(vo, sql);
        sql += "group by datediff(day, prm.recharge_time, getdate())\n";
        return sql;
    }

    public String countPatrolRechargeManagementInject(PatrolRechargeManagementVo vo) {
        String sql = "SELECT im.name,\n" +
                "       im.phone,\n" +
                "       count(distinct im.name)               AS recharge_pens,\n" +
                "       count(1)                 AS transactions_number,\n" +
                "       ISNULL(sum(prm.recharge_amount),0) AS recharge_amount\n" +
                "FROM dbo.patrol_recharge_management AS prm\n" +
                "         LEFT JOIN\n" +
                "     dbo.inspect_manage AS im\n" +
                "     ON\n" +
                "         prm.inject_id = im.id AND im.is_del = 0\n" +
                "WHERE prm.is_del = 0\n" +
                "  and im.name is not null\n" +
                "  and im.phone is not null\n";
        sql = getSQuery(vo, sql);
        sql += "group by im.name, im.phone\n";
        return sql;
    }

    private String getSQuery(PatrolRechargeManagementVo vo, String sql) {
        if (StringUtils.isNotEmpty(vo.getParking_type())) {
            sql += "and im.personType = '" + vo.getParking_type() + "'\n";
        }
        if (StringUtils.isNotEmpty(vo.getNameOrPhone())) {
            sql += "and (im.phone like '%" + vo.getNameOrPhone() + "%' or im.name like '%" + vo.getNameOrPhone() + "%')\n";
        }
        if (null != vo.getArea_id()) {
            sql += "and prm.inject_id = '" + vo.getArea_id() + "'\n";
        }
        if (StringUtils.isNotEmpty(vo.getReceivePaperInvoice())) {
            sql += "and prm.receive_paper_invoice = '" + vo.getReceivePaperInvoice() + "'\n";
        }
        if (StringUtils.isNotEmpty(vo.getStartTime()) && StringUtils.isNotEmpty(vo.getEndTime())) {
            sql += " AND CONVERT(VARCHAR(100),prm.recharge_time,23) BETWEEN '" + vo.getStartTime() + "' AND '" + vo.getEndTime() + "'";
        }
        return sql;
    }
}
