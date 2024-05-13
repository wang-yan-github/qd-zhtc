package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.RechargeManagement;
import com.jsdc.zhtc.vo.CommonVo;
import org.springframework.stereotype.Repository;

/**
 * 类 名: 充值管理
 * 描 述: RechargeManagementDao
 * 作 者: lw
 * 创 建：2022/1/4 9:34
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Repository
public class RechargeManagementDao extends BaseDao<RechargeManagement> {

    /**
     * 描 述： TODO(充值管理详细信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getRechargeMInfo(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select t2.payment_no paymentNo,t2.payment_type,t3.nick_name nickName , t3.phone phone, t1.* from recharge_management t1 ");
        sqlbd.append(" left join payment_order t2 on t1.payment_id = t2.id ");
        sqlbd.append(" left join member_manage t3 on t1.member_id = t3.id ");
        sqlbd.append(" where 1=1 ");
        if (StringUtils.isNotBlank(data.getStr())) {
            sqlbd.append(" and t3.phone like '%").append(data.getStr()).append("%' ");
        }
        if (StringUtils.isNotBlank(data.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, t1.recharge_time, 23) >= '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotBlank(data.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, t1.recharge_time, 23) <= '").append(data.getEndTime()).append("' ");
        }
        if (notEmpty(data.getPayment_type())) {
            sqlbd.append(" and t2.payment_type= '").append(data.getPayment_type()).append("' ");
        }

        sqlbd.append(" and t1.is_del = 0  and t2.is_del='0' and t2.status='2'");
        sqlbd.append(" order by t1.id desc ");
        return sqlbd.toString();
    }

    /**
     * 充值收入
     */
    public String getSumCzMoney(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select sum( CONVERT(decimal( 15 , 2 ) , t1.recharge_amount ) ) from recharge_management t1 ");
        sqlbd.append(" where t1.is_del = 0 ");
        if (StringUtils.isNotBlank(data.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, t1.recharge_time, 23) >= '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotBlank(data.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, t1.recharge_time, 23) <= '").append(data.getEndTime()).append("' ");
        }
        return sqlbd.toString();
    }

    public String getrechargeRecords(Integer memeberId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.payment_no paymentNo, p.payment_type paymentType, r.recharge_amount, r.gift_amount,\n" +
                "\tr.recharge_amount czMoney,\n" +
                "\tp.amount money,\n" +
                "CASE\n" +
                "\t\tp.payment_type \n" +
                "\t\tWHEN '1' THEN\n" +
                "\t\t'包月' \n" +
                "\t\tWHEN '2' THEN\n" +
                "\t\t'微信' \n" +
                "\t\tWHEN '3' THEN\n" +
                "\t\t'支付宝' \n" +
                "\t\tWHEN '4' THEN\n" +
                "\t\t'钱包' \n" +
                "\t\tWHEN '5' THEN\n" +
                "\t\t'现金' ELSE '其他' \n" +
                "\tEND AS payment_type,\n" +
                "\tr.recharge_time ");
        sql.append(" FROM");
        sql.append(" recharge_management r");
        sql.append(" LEFT JOIN payment_order p ON r.payment_id = p.id");
        sql.append(" where r.is_del = '0'");
        sql.append(" and r.member_id = " + memeberId);
        sql.append(" and p.is_del='0' and p.status='2'");
        sql.append(" ORDER BY r.recharge_time desc ");
        return sql.toString();
    }

    //充值记录总钱数
    public String sumRecharge(Integer member_id) {
        String sql = "select sum( CONVERT(decimal( 15 , 2 ) , recharge_amount ) ) from recharge_management r left join payment_order po on r.payment_id = po.id where member_id='" + member_id + "' and po.is_del='0' and po.status='2' GROUP BY member_id ";
        return getQueryHandler(sql).getSql();
    }


    /**
     * 充值记录
     */
    public String getRechargeDataByPaymentId(Integer paymentId) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select " +
                "t3.nick_name nickName," +
                "t3.phone phone," +
                "t3.balance balance," +
                " t1.* from recharge_management t1 ");
        sqlbd.append(" left join member_manage t3 on t1.member_id = t3.id ");
        sqlbd.append(" where t1.is_del = 0 ");
        if (notEmpty(paymentId)) {
            sqlbd.append(" and t1.payment_id = ").append(paymentId);
        }
        return sqlbd.toString();
    }
}
