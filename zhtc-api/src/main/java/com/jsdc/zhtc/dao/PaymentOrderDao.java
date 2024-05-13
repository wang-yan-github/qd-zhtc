package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.vo.PaymentOrderVo;
import com.jsdc.zhtc.vo.RoadParkListVo;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 * @author 王严
 * @version 1.0
 * @description: 支付订单管理
 */
@Repository
public class PaymentOrderDao extends BaseDao<PaymentOrder> {

    /**
     * 分页查询
     */
    public String toList(PaymentOrderVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * from payment_order ");
        sql.append(" where is_del = 0 ");
        if (notEmpty(vo)) {
            //支付方式(1包月 2微信 3支付宝 4钱包 5现金  6银行卡 7商家支付 8聚合支付 9:交通账户)
            if (notEmpty(vo.getPaymentType())) {
                sql.append(" and payment_type = '").append(vo.getPaymentType()).append("' ");
            }
            //支付来源(1包月、2会员充值、3停车订单支付、4商家充值)
            if (notEmpty(vo.getPaymentResource())) {
                sql.append(" and payment_resource = '").append(vo.getPaymentResource()).append("' ");
            }
            //支付状态 1待支付 2已支付 3支付失败
            if (notEmpty(vo.getStatus())) {
                sql.append(" and status = '").append(vo.getStatus()).append("' ");
            }
            //支付流水号
            if (notEmpty(vo.getPayment_serialno())) {
                sql.append(" and payment_serialno like '%").append(vo.getPayment_serialno()).append("%' ");
            }
            //支付订单号
            if (notEmpty(vo.getPayment_no())) {
                sql.append(" and payment_no like '%").append(vo.getPayment_no()).append("%' ");
            }
            if (notEmpty(vo.getStartTime())) {
                sql.append(" AND CONVERT(VARCHAR(100),create_time,23) >= '").append(vo.getStartTime()).append("' ");
            }
            if (notEmpty(vo.getEndTime())) {
                sql.append(" AND CONVERT(VARCHAR(100),create_time,23) <= '").append(vo.getEndTime()).append("' ");
            }
            if (notEmpty(vo.getIs_refund())) {
                if (vo.getIs_refund().equals("1")) {
                    sql.append(" and is_refund = '").append(vo.getIs_refund()).append("' ");
                } else {
                    sql.append(" and is_refund is null");
                }
            }
        }
        sql.append(" order by id desc");
        return sql.toString();
    }

    public String listPaymentOrder(PaymentOrderVo vo) {
        String sql = "SELECT\n" +
                "po.id,\n" +
                "o.sum_amount,\n" +
                "o.order_no payment_no,\n" +
                "CONVERT ( VARCHAR, po.pay_time, 120 ) AS pay_time,\n" +
                "po.payment_serialno,\n" +
                "po.payment_type,\n" +
                "po.amount,\n" +
                "po.is_union_pay,\n";
        if (StringUtils.isNotEmpty(vo.getParkingType()) && vo.getParkingType().equals("1")) {
            sql += "  p.park_name name \n";
        } else {
            sql += "  r.road_name name\n";
        }
        sql += " FROM dbo.payment_order AS po INNER JOIN\n";

        sql += "     dbo.parking_order AS o\n";

        sql += "ON po.id = o.payment_id\n";
        if (StringUtils.isNotEmpty(vo.getParkingType()) && vo.getParkingType().equals("1")) {
            sql += "  left join park p on o.park_id = p.id \n";
        } else {
            sql += "  left join road r on o.road_id = r.id\n";
        }
        sql += "where po.is_del='0' and po.status='2'\n";
        sql += " and o.is_del = 0\n";
        if (vo.getPark_id() != null) {
            sql += " and o.park_id =" + vo.getPark_id();
        }
        if (StringUtils.isNotEmpty(vo.getPaymentNoOrSerialNo())) {
            sql += " and (o.order_no like '%" + vo.getPaymentNoOrSerialNo() + "%' or po.payment_serialno like '%" + vo.getPaymentNoOrSerialNo() + "%')";
        }
        if (null != vo.getAreaId()) {
            sql += " and o.area_id =" + vo.getAreaId();
        }
        if (null != vo.getStreetId()) {
            sql += " and o.street_id =" + vo.getStreetId();
        }
        if (null != vo.getRoadId()) {
            sql += " and o.road_id =" + vo.getRoadId();
        }

        if (StringUtils.isNotEmpty(vo.getPaymentType())) {
            sql += " and po.payment_type =" + vo.getPaymentType();
        }
        if (StringUtils.isNotEmpty(vo.getStartTime()) && StringUtils.isNotEmpty(vo.getEndTime())) {
            sql += " AND CONVERT(VARCHAR(100),po.create_time,23) BETWEEN '" + vo.getStartTime() + "' AND '" + vo.getEndTime() + "'";
        }
//        sql += " group by\n" +
//                "po.id,\n" +
//                "o.sum_amount,\n" +
//                "o.order_no,\n" +
//                "po.pay_time,\n" +
//                "po.payment_serialno,\n" +
//                "po.payment_type,\n" +
//                "po.amount,\n" +
//                "po.is_union_pay";

        sql += " order by pay_time desc";
        return sql;
    }

    public String listRoadParkingOrder(PaymentOrderVo vo) {
        String sql = "SELECT o.order_no,\n" +
                "       o.paid_amount,\n" +
                "       o.sum_amount,\n" +
                "       CONVERT(varchar,o.drivein_time,120) AS drivein_time,\n" +
                "       CONVERT(varchar,o.driveout_time,120) AS driveout_time,\n" +
                "       o.resitime\n";
        sql += "FROM dbo.parking_order AS o\n";
        sql += "WHERE o.payment_id = '" + vo.getPaymentId() + "'";
        return sql;
    }

    /**
     * 柱状图 支付方式/金额
     */
    public String getMoneysByType(RoadParkListVo bean) {
        String sql = "SELECT SUM ( CAST ( amount AS NUMERIC ( 12, 2 ))) AS amount,\n" +
                "\tCASE\n" +
                "\t\tpayment_type \n" +
                "\t\tWHEN '1' THEN\n" +
                "\t\t'包月' \n" +
                "\t\tWHEN '2' THEN\n" +
                "\t\t'微信' \n" +
                "\t\tWHEN '3' THEN\n" +
                "\t\t'支付宝' \n" +
                "\t\tWHEN '4' THEN\n" +
                "\t\t'钱包' \n" +
                "\t\tWHEN '5' THEN\n" +
                "\t\t'现金' \n" +
//                "\t\tWHEN '6' THEN\n" +
//                "\t\t'银行卡' \n" +
                "\t\tWHEN '7' THEN\n" +
                "\t\t'商家支付'" +
//                " ELSE '其他' \n" +
                "\tEND AS payment_type \n" +
                "FROM\n" +
                "\tpayment_order \n" +
                " where payment_type is not null and is_del='0' and status='2'\n" +
                " GROUP BY\n" +
                "\tpayment_type";
        if (notEmpty(bean)) {
        }
        return getQueryHandler(sql).getSql();
    }

    /**
     * 营收金额
     * 0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
     * 支付来源(1包月、2会员充值、3停车订单支付、4商家充值)
     */
    public String getSumMoneys(PaymentOrderVo vo) {
        String sql = "SELECT\n" +
                " isnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), po.amount ) ), 0 ) amount,\n" +
                " isnull( SUM ( case when po.payment_resource = '1' then CONVERT ( DECIMAL ( 15, 2 ), po.amount ) else 0 end ), 0 ) amount1,\n" +
                "\tisnull( SUM ( case when po.payment_resource = '2' then CONVERT ( DECIMAL ( 15, 2 ), po.amount ) else 0 end ), 0 ) amount2,\n" +
                "\tisnull( SUM ( case when po.payment_resource = '3' then CONVERT ( DECIMAL ( 15, 2 ), po.amount ) else 0 end ), 0 ) amount3,\n" +
                "\tisnull( SUM ( case when po.payment_resource = '4' then CONVERT ( DECIMAL ( 15, 2 ), po.amount ) else 0 end ), 0 ) amount4,\n" +
                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), m.refund_amount ) ), 0 ) amount5";
        sql += " FROM payment_order AS po \n";
        sql += " left join refund_management m on m.payment_id = po.id and m.is_del='0'\n";
        if (notEmpty(vo.getParkingType())) {
            sql += " inner join\n";
            sql += " parking_order AS o\n";
            sql += " ON po.id = o.payment_id and o.is_del='0'\n";
        }
        sql += "where po.is_del='0' and po.status='2'\n";
        if (notEmpty(vo.getParkingType())) {
            if (notEmpty(vo.getPark_id())) {
                sql += " and o.park_id =" + vo.getPark_id();
            }
            if (notEmpty(vo.getRoadId())) {
                sql += " and o.road_id =" + vo.getRoadId();
            }
        }
        if (notEmpty(vo.getStartTime())) {
            sql += " AND CONVERT(VARCHAR(100),po.create_time,23) = '" + vo.getStartTime() + "'";
        }
        return sql;
    }

    /**
     * 今日营收 折线图
     * 数据分析页面 数据总览使用
     * 今日0时至当前时间各个小时整点营收金额统计
     */
    public String getMoneyDataByHours(PaymentOrderVo vo) {
        //获取当前时间小时
        int hour = new DateTime().getHourOfDay();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT isnull(b.amount, 0) AS amount,isnull(b.amount2, 0) AS amount2,c.click_date as name  " +
                " FROM ( ");
        for (int i = 0; i <= hour; i++) {
            sqlBuffer.append(" SELECT '").append(String.format("%02d", i)).append("' AS click_date");
            if (i < hour) {
                sqlBuffer.append("  UNION ALL ");
            }
        }

        sqlBuffer.append(" ) c  ");

        sqlBuffer.append(" LEFT JOIN ( " +
                " select " +
                " isnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), po.amount ) ), 0 ) amount," +
                " isnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), m.refund_amount ) ), 0 ) amount2," +
                " DATEPART( HH, po.create_time ) as hours " +
                " FROM payment_order AS po ");
        sqlBuffer.append(" left join refund_management m on m.payment_id = po.id and m.is_del='0'");
        if (notEmpty(vo.getParkingType())) {
            sqlBuffer.append(" inner join ");
            sqlBuffer.append(" parking_order AS o ");
            sqlBuffer.append(" ON po.id = o.payment_id and o.is_del='0'");
        }
        sqlBuffer.append(" where po.is_del='0' and po.status='2'");
        if (notEmpty(vo.getParkingType())) {
            if (notEmpty(vo.getPark_id())) {
                sqlBuffer.append(" and o.park_id = ").append(vo.getPark_id());
            }
            if (notEmpty(vo.getRoadId())) {
                sqlBuffer.append(" and o.road_id = ").append(vo.getRoadId());
            }
        }
        if (notEmpty(vo.getStartTime())) {
            sqlBuffer.append(" AND CONVERT(VARCHAR(100),po.create_time,23) = '").append(vo.getStartTime()).append("'");
        }
        sqlBuffer.append(" GROUP BY DATEPART( HH, po.create_time) " +
                " ) b ");

        sqlBuffer.append(" ON c.click_date = b.hours");
        sqlBuffer.append(" ORDER BY CONVERT(INT, LEFT(c.click_date, LEN(c.click_date)-1)) ASC, RIGHT(c.click_date, 1)");
        return sqlBuffer.toString();
    }

}
