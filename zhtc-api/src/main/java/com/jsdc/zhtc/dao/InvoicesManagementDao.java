package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.InvoicesManagement;
import org.springframework.stereotype.Repository;

/**
 * @author 王严
 * @version 1.0
 * @description: 发票管理
 */
@Repository
public class InvoicesManagementDao extends BaseDao<InvoicesManagement> {
    public String listInvoicesManagement(String phone, String invoice_mode, String parking_type) {
        String sql = "select a.*,b.nick_name memberName,b.phone from invoices_management a\n" +
                "left join member_manage b\n" +
                "on a.member_id = b.id\n" +
                "where 1=1\n";
        if (StringUtils.isNotEmpty(phone)) {
            sql += "and b.phone like '%" + phone + "%'\n";
        }
        if (StringUtils.isNotEmpty(invoice_mode)) {
            sql += "and a.invoice_mode='" + invoice_mode + "'\n";
        }
        sql += " order by a.application_time desc";
        return sql;
    }

    /**
     * 分页查询
     * <p>
     * thr
     *
     * @param bean
     * @return
     */
    public String getPageList(InvoicesManagement bean) {
        String sql = "select a.*, b.nick_name memberName, b.phone from invoices_management a\n" +
                "left join member_manage b\n" +
                "on a.member_id = b.id\n" +
                "where b.is_del = '0' \n";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getPhone())) {
                sql += "and b.phone like '%" + bean.getPhone() + "%'\n";
            }
            if (notEmpty(bean.getInvoice_mode())) {
                sql += "and a.invoice_mode = '" + bean.getInvoice_mode() + "'\n";
            }
            if (notEmpty(bean.getMember_id())) {
                sql += "and a.member_id=" + bean.getMember_id();
            }
        }
        sql += " order by a.application_time desc";
        return sql;
    }

    public String invoicesDetailsById(String invoicesId, String parkingType) {
        String sql = "";
        sql += "SELECT o.id,\n" +
                "       o.area_id,\n" +
                "       o.berth,\n" +
                "       o.carno_id,\n" +
                "       o.company_id,\n" +
                "       CONVERT(varchar,o.create_time,120) AS create_time,\n" +
                "       o.create_user,\n" +
                "       o.discount_amount,\n" +
                "       CONVERT(varchar,o.drivein_time,120) AS drivein_time,\n" +
                "       CONVERT(varchar,o.driveout_time,120) AS driveout_time,\n" +
                "       o.inspect_id,\n" +
                "       o.is_del,\n" +
                "       o.is_invoice,\n" +
                "       o.is_merge,\n" +
                "       o.is_upload,\n" +
                "       o.member_id,\n" +
                "       o.order_no,\n" +
                "       o.paid_amount,\n" +
                "       o.pay_type,\n" +
                "       o.payment_id,\n" +
                "       CONVERT(varchar,o.resitime,120) AS resitime\n" +
                "FROM dbo.parking_order AS o\n";
        sql += "WHERE o.status in('3','4')   and o.invoice_id = " + invoicesId + "\n"; //订单状态 在停、待缴费、已缴费、已完成
        return sql;
    }
}
