package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.ParkingOrder;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.ParkingOrderVo;
import com.jsdc.zhtc.vo.RoadOrParkingCommentVo;
import com.jsdc.zhtc.vo.RoadParkListVo;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * ClassName: ParkingOrderDao
 * Description:
 * date: 2022/1/4 15:56
 *
 * @author wh
 */
@Repository
public class ParkingOrderDao extends BaseDao<ParkingOrder> {


    public String toList(ParkingOrderVo parkingOrderVo) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT po.pay_time,'停车订单' AS order_type,pao.amount paid_amount,r.park_name, ");
        sql.append(" CASE pao.payment_type when '1' then '包月' when '2' then '微信' ");
        sql.append(" when '3' then '支付宝' when '4' then '钱包' when '5' then '现金' when '8' then '聚合支付' else '其他' END as type_name, ");
        sql.append("po.pay_type,'/' AS pay_channel,'/' AS pay_terminal,'停车场' AS pay_scene, ");
        sql.append("pao.payment_serialno,'个人账户' AS account_type,im.name AS inspect_name, ");
        sql.append("'/' AS shopee FROM parking_order po ");
        sql.append("LEFT JOIN inspect_manage im ON po.inspect_id = im.id ");
        sql.append("LEFT JOIN park r ON r.id = po.park_id ");
        sql.append("INNER JOIN payment_order pao ON po.payment_id = pao.id and pao.is_del='0' and pao.status='2'");

        sql.append("where po.is_del=0 ");
        if (parkingOrderVo.getPark_id() != null) {
            sql.append(" AND po.park_id=" + parkingOrderVo.getPark_id());
        }
        if (StringUtils.isNotEmpty(parkingOrderVo.getPay_type())) {
            sql.append(" AND pao.payment_type in (" + parkingOrderVo.getPay_type() + ")");
        }

        if (StringUtils.isNotEmpty(parkingOrderVo.getStart_time())) {
            sql.append("  and CONVERT(varchar, pao.pay_time, 23) >=  '").append(parkingOrderVo.getStart_time()).append("' ");

        }

        if (StringUtils.isNotEmpty(parkingOrderVo.getEnd_time())) {
            sql.append("  and CONVERT(varchar, pao.pay_time, 23) <=  '").append(parkingOrderVo.getEnd_time()).append("' ");

        }

        return sql.toString();
    }

    public String toCount(ParkingOrderVo parkingOrderVo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(*) as car_num,sum(CAST(po.paid_amount AS NUMERIC(12,2))) as car_price　");
        sql.append("FROM parking_order po LEFT JOIN inspect_manage im ON po.inspect_id = im.id ");
        sql.append("INNER JOIN payment_order pao ON po.payment_id = pao.id and pao.is_del='0' and pao.status='2'");
        sql.append("where po.is_del=0 ");
        if (parkingOrderVo.getPark_id() != null) {
            sql.append(" AND po.park_id=" + parkingOrderVo.getPark_id());
        }
        if (StringUtils.isNotEmpty(parkingOrderVo.getPay_type())) {
            sql.append(" AND pao.payment_type in (" + parkingOrderVo.getPay_type() + ")");
        }

        if (StringUtils.isNotEmpty(parkingOrderVo.getStart_time())) {
            sql.append("  and CONVERT(varchar, pao.pay_time, 23) >=  '").append(parkingOrderVo.getStart_time()).append("' ");

        }

        if (StringUtils.isNotEmpty(parkingOrderVo.getEnd_time())) {
            sql.append("  and CONVERT(varchar, pao.pay_time, 23) <=  '").append(parkingOrderVo.getEnd_time()).append("' ");

        }

        return sql.toString();

    }

    public String getSumByArea(String areaId, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select");
        sql.append(" r.sum_amount sumAmount, r.paid_amount paiedAmount");
        sql.append(" from parking_order r");
        sql.append(" where r.area_id = ").append(areaId);
        sql.append(" and r.is_del = '0'");
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and CONVERT(varchar, r.driveout_time, 23) >=  '").append(startTime).append("' ");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and CONVERT(varchar, r.driveout_time, 23) <=  '").append(endTime).append("' ");
        }
        return sql.toString();
    }


    //分页
    public String selectPageList(RoadOrParkingCommentVo bean) {
        String sql = "select r.* ,ro.park_name as road_name,c.id as car_id,c.car_no as car_no,dic.label as state_name ,d.label as car_type," +
                " s.label as source_type , m.company_name as company_name,m.phone as phone " +
                " from parking_order r  " +
                " LEFT JOIN operate_carno c on r.carno_id = c.id  " +
                " LEFT JOIN park ro on ro.id = r.park_id " +
                " LEFT JOIN member_manage m on m.id=r.member_id " +
                " LEFT JOIN sys_dict dic on dic.dc_value=r.status and dic.dict_type='roadStatus' " +
                " LEFT JOIN sys_dict d on d.dc_value = c.car_type and d.dict_type='car_type' " +
                " LEFT JOIN sys_dict s on s.dc_value = r.source and s.dict_type='source_type' " +
                "where r.is_del = 0 ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getPark_id())) {
                sql += " and r.park_id =  '" + bean.getPark_id() + "'";
            }
            if (notEmpty(bean.getCar_no())) {
                sql += " and c.car_no like  '%" + bean.getCar_no() + "%'";
            }
            if (notEmpty(bean.getStartTime())) {
                sql += " and r.drivein_time  >= " + bean.getStartTime();
            }
            if (notEmpty(bean.getEndTime())) {
                sql += " and r.drivein_time <= " + bean.getEndTime();
            }
            if (notEmpty(bean.getOrder_no())) {
                sql += " and r.order_no like  '%" + bean.getOrder_no() + "%'";
            }
            if (notEmpty(bean.getPhone())) {
                sql += " and m.phone like  '%" + bean.getPhone() + "%'";
            }
            if (notEmpty(bean.getStatus())) {
                sql += " and r.status =  '" + bean.getStatus() + "'";
            }
            if (notEmpty(bean.getIs_invoice())) {
                sql += " and r.is_invoice = " + bean.getIs_invoice();
            }
        }
        sql += "  order by r.update_time desc";
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

    /**
     * 现金核销
     * 分页查询
     * thr
     *
     * @param bean
     * @return
     */
    public String selectHxPageList(ParkingOrderVo bean) {
        String sql = "select r.* ,ro.park_name as park_name,c.id as car_id,c.car_no as car_no,dic.label as state_name ,d.label as car_type,dic.label as roster_type," +
                " s.label as source_type , m.company_name as company_name,m.phone as phone " +
                " from payment_order po " +
                " LEFT JOIN parking_order r ON r.payment_id = po.id " +
                " LEFT JOIN operate_carno c on r.carno_id = c.id  " +
                " LEFT JOIN park ro on ro.id = r.park_id " +
                " LEFT JOIN member_manage m on m.id=r.member_id " +
                " LEFT JOIN sys_dict dic on dic.dc_value=r.status and dic.dict_type='roadStatus' " +
                " LEFT JOIN sys_dict d on d.dc_value = c.car_type and d.dict_type='car_type' " +
                " LEFT JOIN sys_dict s on s.dc_value = r.source and s.dict_type='source_type' " +
                " LEFT JOIN sys_dict rt on rt.dc_value = r.source and s.dict_type='rosterType' " +
                " where r.is_del = 0 " +
                " and po.is_del = 0 " +
                " and po.status = '2' ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getPark_id())) {
                sql += " and r.park_id = '" + bean.getPark_id() + "'";
            }
            if (notEmpty(bean.getIs_verification())) {
                sql += " and r.is_verification = '" + bean.getIs_verification() + "'";
            }
            if (notEmpty(bean.getInspect_id())) {
                sql += " and po.inspect_id = '" + bean.getInspect_id() + "'";
            }
            if (notEmpty(bean.getPay_type())) {
                sql += " and r.pay_type = '" + bean.getPay_type() + "'";
                sql += " and po.payment_type = '" + bean.getPay_type() + "'";
            }
            if (notEmpty(bean.getStartDate())) {
                sql += " and r.pay_time >= '" + bean.getStartDate() + "'";
            }
            if (notEmpty(bean.getEndDate())) {
                sql += " and r.pay_time < '" + new DateTime(bean.getEndDate().replace(" ", "T")).plusDays(1).toString("yyy-MM-dd") + "'";
            }
        }
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

    /**
     * 总金额
     */
    public String getSumMoney(ParkingOrderVo bean) {
        String sql = "select isnull(sum(cast(r.sum_amount AS DECIMAL)),0) " +
                " from payment_order po " +
                " LEFT JOIN parking_order r ON r.payment_id = po.id " +
                " LEFT JOIN operate_carno c on r.carno_id = c.id  " +
                " LEFT JOIN park ro on ro.id = r.park_id " +
                " LEFT JOIN member_manage m on m.id=r.member_id " +
                " LEFT JOIN sys_dict dic on dic.dc_value=r.status and dic.dict_type='roadStatus' " +
                " LEFT JOIN sys_dict d on d.dc_value = c.car_type and d.dict_type='car_type' " +
                " LEFT JOIN sys_dict s on s.dc_value = r.source and s.dict_type='source_type' " +
                " LEFT JOIN sys_dict rt on rt.dc_value = r.source and s.dict_type='rosterType' " +
                " where r.is_del = 0 " +
                " and po.is_del = 0 " +
                " and po.status = '2' ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getIs_verification())) {
                sql += " and r.is_verification = '" + bean.getIs_verification() + "'";
            }
            if (notEmpty(bean.getInspect_id())) {
                sql += " and po.inspect_id = '" + bean.getInspect_id() + "'";
            }
            if (notEmpty(bean.getPay_type())) {
                sql += " and r.pay_type = '" + bean.getPay_type() + "'";
                sql += " and po.payment_type = '" + bean.getPay_type() + "'";
            }
            if (notEmpty(bean.getStartDate())) {
                sql += " and r.pay_time >= '" + bean.getStartDate() + "'";
            }
            if (notEmpty(bean.getEndDate())) {
                sql += " and r.pay_time < '" + new DateTime(bean.getEndDate().replace(" ", "T")).plusDays(1).toString("yyy-MM-dd") + "'";
            }
        }
        return sql;
    }

    /**
     * 现金核销
     * 核销
     * thr
     */
    public String updHx(ParkingOrderVo bean) {
        String sql = "update parking_order set is_verification = '1' " +
                " where is_del = 0 ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getIs_verification())) {
                sql += " and is_verification = '" + bean.getIs_verification() + "'";
            }
            if (notEmpty(bean.getPay_type())) {
                sql += " and pay_type = '" + bean.getPay_type() + "'";
            }
            if (notEmpty(bean.getStartDate())) {
                sql += " and pay_time >= '" + bean.getStartDate() + "'";
            }
            if (notEmpty(bean.getEndDate())) {
                sql += " and pay_time < '" + new DateTime(bean.getEndDate().replace(" ", "T")).plusDays(1).toString("yyy-MM-dd") + "'";
            }
        }
        sql += " and payment_id in " +
                " (select po.id from payment_order po " +
                " where po.is_del = 0 " +
                " and po.status = '2' ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getInspect_id())) {
                sql += " and po.inspect_id = '" + bean.getInspect_id() + "'";
            }
            if (notEmpty(bean.getPay_type())) {
                sql += " and po.payment_type = '" + bean.getPay_type() + "'";
            }
        }
        sql += " )";
        return sql;
    }

    /**
     * 描 述： TODO(应收款)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getShouldCharge(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select sum(convert(decimal(15, 2), sum_amount)) as sums from parking_order ");
        sqlbd.append(" where 1=1 ");

        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and CONVERT(varchar, drivein_time, 23) = '").append(data.getStr()).append("' ");

        sqlbd.append(" and status in ('2' , '3' , '4' ) ");
        sqlbd.append(" and is_del = 0 ");
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(实收款)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getActualCharge(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select sum(convert(decimal(15, 2), paid_amount)) as sums from parking_order ");
        sqlbd.append(" where 1=1 ");

        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and CONVERT(varchar, drivein_time, 23) = '").append(data.getStr()).append("' ");

        sqlbd.append(" and status in ( '3' , '4' ) ");
        sqlbd.append(" and is_del = 0 ");
        return sqlbd.toString();
    }

    /**
     * create by wh at 2022/1/7 10:41
     * description: 订单统计基础数据
     *
     * @param area_id
     * @param street_id
     * @param road_id
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getParkingOrderBasicData(Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.area_name as cityName,s.street_name as areaName,r.park_name as roadName,");
        sql.append(" count(*) as totalOrders,");
        sql.append(" sum( case when o.status in (3,4) then 1 else 0 end) as totalPayment");
        sql.append(" from ");
        sql.append(" parking_order o");
        sql.append(" LEFT JOIN area a ON o.area_id= a.id");
        sql.append(" LEFT JOIN street s ON o.street_id= s.id");
        sql.append(" LEFT JOIN park r ON o.park_id= r.id ");
        sql.append(" where 1=1");
        if (area_id != null) {
            sql.append(" and o.area_id= " + area_id);
        }
        if (street_id != null) {
            sql.append(" and o.street_id= " + street_id);
        }
        if (road_id != null) {
            sql.append(" and o.park_id= " + road_id);
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and o.drivein_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and o.drivein_time <= '" + endTime + "'");
        }
        sql.append(" and o.is_del = '0'");
        sql.append(" GROUP BY a.area_name,s.street_name,r.park_name");
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 10:41
     * description: 订单统计订单增长
     *
     * @param area_id
     * @param street_id
     * @param road_id
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getParkingOrderGrowth(Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" CONVERT(VARCHAR ( 100 ), drivein_time, 23) AS time,");
        sql.append(" COUNT ( * ) AS orderNum,");
        sql.append(" sum( case when status in (3,4) then 1 else 0 end) AS payNum,");
        sql.append(" ltrim(CONVERT (NUMERIC ( 9, 2 ),(sum( case when status in (3,4) then 1 else 0 end) ");
        sql.append(" ) * 100.0 / COUNT ( * ))) + '%' AS Proportion ");
        sql.append(" from ");
        sql.append(" parking_order");
        sql.append(" where 1=1");
        if (area_id != null) {
            sql.append(" and area_id= " + area_id);
        }
        if (street_id != null) {
            sql.append(" and street_id= " + street_id);
        }
        if (road_id != null) {
            sql.append(" and park_id= " + road_id);
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and drivein_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and drivein_time <= '" + endTime + "'");
        }
        sql.append(" and is_del = '0'");
        sql.append(" GROUP BY CONVERT ( VARCHAR ( 100 ), drivein_time, 23 )");
        sql.append(" ORDER BY CONVERT(VARCHAR ( 100 ), drivein_time, 23) DESC");
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 10:41
     * description: 近八天订单增长分析
     *
     * @param area_id
     * @param street_id
     * @param road_id
     * @return java.lang.String
     */
    public String getParkingNearly8DaysOrderGrowth(Integer area_id, Integer street_id, Integer road_id, String user_type) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.time,isnull(b.orderNum,0) as orderNum,isnull(b.payNum,0) as payNum ");
        sql.append(" from (");
        sql.append(" select CONVERT ( VARCHAR ( 100 ), GETDATE(), 23 ) AS TIME ");
        sql.append(" UNION ALL select CONVERT ( VARCHAR ( 100 ), DATEADD(day, -1, GETDATE()), 23 ) AS TIME ");
        sql.append(" UNION ALL select CONVERT ( VARCHAR ( 100 ), DATEADD(day, -2, GETDATE()), 23 ) AS TIME ");
        sql.append(" UNION ALL select CONVERT ( VARCHAR ( 100 ), DATEADD(day, -3, GETDATE()), 23 ) AS TIME ");
        sql.append(" UNION ALL select CONVERT ( VARCHAR ( 100 ), DATEADD(day, -4, GETDATE()), 23 ) AS TIME ");
        sql.append(" UNION ALL select CONVERT ( VARCHAR ( 100 ), DATEADD(day, -5, GETDATE()), 23 ) AS TIME ");
        sql.append(" UNION ALL select CONVERT ( VARCHAR ( 100 ), DATEADD(day, -6, GETDATE()), 23 ) AS TIME ");
        sql.append(" UNION ALL select CONVERT ( VARCHAR ( 100 ), DATEADD(day, -7, GETDATE()), 23 ) AS TIME ");
        sql.append(" ) a left join ");
        sql.append("( select CONVERT(VARCHAR ( 100 ), drivein_time, 23) AS time,");
        sql.append(" COUNT ( * ) AS orderNum,");
        sql.append(" SUM ( CASE WHEN status IN ( 3, 4 ) THEN 1 ELSE 0 END ) AS payNum ");
        sql.append(" from ");

        //停车场
        sql.append(" parking_order");

        sql.append(" where 1=1");
        if (area_id != null) {
            sql.append(" and area_id= " + area_id);
        }
        if (street_id != null) {
            sql.append(" and street_id= " + street_id);
        }
        if (road_id != null) {
            //停车场
            sql.append(" and park_id= " + road_id);

        }
        sql.append(" and is_del = '0'");
        sql.append(" GROUP BY CONVERT ( VARCHAR ( 100 ), drivein_time, 23 ))");
        sql.append(" b on a.time= b.time order by a.time");
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 10:41
     * description: 订单统计订单状态占比
     *
     * @param area_id
     * @param street_id
     * @param road_id
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getParkingOrderStatus(Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" CASE status WHEN '1' THEN '在停' WHEN '2' THEN '待缴费' WHEN '3' THEN '已缴费' WHEN '4' THEN '已完成' END as status,");
        sql.append(" count(*) as num,");
        sql.append(" ltrim(CONVERT (NUMERIC ( 9, 2 ),(count(*) * 100.0 / (select count(*) from parking_order where is_del='0')))) + '%' as proportion");
        sql.append(" from ");
        sql.append(" parking_order");
        sql.append(" where 1=1");
        if (area_id != null) {
            sql.append(" and area_id= " + area_id);
        }
        if (street_id != null) {
            sql.append(" and street_id= " + street_id);
        }
        if (road_id != null) {
            sql.append(" and park_id= " + road_id);
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and drivein_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and drivein_time <= '" + endTime + "'");
        }
        sql.append(" and is_del = '0'");
        sql.append(" group by status");
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 10:41
     * description: 资金统计收入报表
     */
    public String getParkingOrderIncome(Integer area_id, Integer street_id, Integer park_id, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.area_name as areaName,s.street_name as streetName,r.park_name as roadName,");
        sql.append(" SUM (CAST ( sum_amount AS decimal(10, 2))) as receivable,");
        sql.append(" SUM (CAST ( paid_amount AS decimal(10, 2))) as netReceipts");
        sql.append(" from ");
        sql.append(" parking_order o");
        sql.append(" LEFT JOIN area a ON o.area_id= a.id");
        sql.append(" LEFT JOIN street s ON o.street_id= s.id");
        sql.append(" LEFT JOIN park r ON o.park_id= r.id ");
        sql.append(" where 1=1");
        if (area_id != null) {
            sql.append(" and o.area_id= " + area_id);
        }
        if (street_id != null) {
            sql.append(" and o.street_id= " + street_id);
        }
        if (park_id != null) {
            sql.append(" and o.park_id= " + park_id);
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and o.drivein_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and o.drivein_time <= '" + endTime + "'");
        }
        sql.append(" and o.is_del = '0'");
        sql.append(" GROUP BY a.area_name,s.street_name,r.park_name");
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 10:41
     * description: 资金统计充值增长
     *
     * @param timeType
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getParkingRechargeGrowth(String timeType, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        if (timeType.equals("day")) {
            sql.append(" CONVERT(VARCHAR ( 100 ), create_time, 23) AS time,");
        } else if (timeType.equals("month")) {
            sql.append(" CONVERT(VarChar(7), create_time, 120) AS time,");
        } else if (timeType.equals("year")) {
            sql.append(" YEAR( create_time) AS time,");
        }
        sql.append(" sum(CAST ( recharge_amount AS decimal(10, 2) )) as rechargeAmount,");
        sql.append(" count(*) as num");
        sql.append(" from ");
        sql.append(" recharge_management");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and create_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and create_time <= '" + endTime + "'");
        }
        sql.append(" and is_del = '0'");
        sql.append(" group by");
        if (timeType.equals("day")) {
            sql.append(" CONVERT(VARCHAR ( 100 ), create_time, 23)");
        } else if (timeType.equals("month")) {
            sql.append(" CONVERT(VarChar(7), create_time, 120)");
        } else if (timeType.equals("year")) {
            sql.append(" YEAR( create_time)");
        }
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 11:00
     * description: 资金统计缴费方式统计
     *
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getParkingPaymentMethod(String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" sum(r.resitime)");
        sql.append(" from ");
        sql.append(" parking_order r");
        sql.append(" where 1=1");

        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and r.drivein_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and r.drivein_time <= '" + endTime + "'");
        }
        sql.append(" and r.is_del = '0'");
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 11:05
     * description: 资金统计免单统计
     *
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getParkingFree(String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" case free_type when '1' then '白名单' when '3' then '公务开闸' end as freeType,");
        sql.append(" count(distinct(carno_id)) as carNum,");
        sql.append("  sum(CAST(sum_amount AS DECIMAL (10, 1))) as freeAmount,");
        sql.append(" count(*) as freeNum,");
        sql.append(" ltrim(CONVERT (NUMERIC ( 9, 2 ),(count(*) * 100.0 / (select count(*) from parking_order where is_del='0')))) + '%' as freeProportion");
        sql.append(" from ");
        sql.append(" parking_order");
        sql.append(" where 1=1");
        sql.append("  and free_type in('1','3')");
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and drivein_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and drivein_time <= '" + endTime + "'");
        }
        sql.append(" and is_del = '0'");
        sql.append(" group by free_type");
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 1:35
     * description: 开票统计
     *
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getParkingInvoicing(String phone, Integer area_id, String invoice_type, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" sum(r.resitime)");
        sql.append(" from ");
        sql.append(" parking_order r");
        sql.append(" where 1=1");

        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and r.drivein_time >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and r.drivein_time <= '" + endTime + "'");
        }
        sql.append(" and r.is_del = '0'");
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 1:35
     * description: 大额欠费
     *
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getParkingLargeArrears(String money, Integer area_id, Integer street_id, Integer road_id, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" r.carno_id as carNoId,o.car_no AS carNo,MAX(r.drivein_time) AS inTime,MAX(r.driveout_time) AS outTime,");
        sql.append(" SUM (CAST ( r.unpaid_amount AS DECIMAL ( 10, 2 ) )) AS totalArrears,");
        sql.append(" MAX(m.company_name) AS name,MAX(m.phone) as phone");
        sql.append(" from ");
        sql.append(" parking_order r");
        sql.append(" LEFT JOIN operate_carno o ON r.carno_id= o.id");
        sql.append(" LEFT JOIN member_manage m ON r.member_id = m.id");
        sql.append(" where 1=1");
        if (area_id != null) {
            sql.append(" and r.area_id= " + area_id);
        }
        if (street_id != null) {
            sql.append(" and r.street_id= " + street_id);
        }
        if (road_id != null) {
            sql.append(" and r.park_id= " + road_id);
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and CONVERT(varchar, r.drivein_time, 23) >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and CONVERT(varchar, r.drivein_time, 23) <= '" + endTime + "'");
        }
        sql.append(" and r.status=2");
        sql.append(" and o.roster_type != 3");
        sql.append(" and r.is_del = '0'");
        sql.append(" GROUP BY o.car_no,r.carno_id ");
        if (StringUtils.isNotEmpty(money)) {
            sql.append(" HAVING SUM (CAST ( r.unpaid_amount AS DECIMAL ( 10, 2 ) )) > " + money);
        } else {
            sql.append(" HAVING SUM (CAST ( r.unpaid_amount AS DECIMAL ( 10, 2 ) )) > 0");
        }
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/7 1:35
     * description: 大额欠费欠费订单
     *
     * @param carno_id
     * @return java.lang.String
     */
    public String getParkingLargeArrearsOrders(Integer carno_id, String money, Integer areaId, Integer streetId, Integer roadId, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" order_no as orderNo,unpaid_amount as amount, CONVERT(VARCHAR ( 100 ), drivein_time, 120) as inTime, CONVERT(VARCHAR ( 100 ), driveout_time, 120) as outTime, resitime");
        sql.append(" from ");
        sql.append(" parking_order");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(money)) {
            sql.append(" and  CAST( unpaid_amount AS DECIMAL ( 10, 2 )) > " + money);
        } else {
            sql.append(" and  CAST( unpaid_amount AS DECIMAL ( 10, 2 )) > 0");
        }
        if (notEmpty(carno_id)) {
            sql.append(" and carno_id = " + carno_id);
        }
        if (areaId != null) {
            sql.append(" and area_id= " + areaId);
        }
        if (streetId != null) {
            sql.append(" and street_id= " + streetId);
        }
        if (roadId != null) {
            sql.append(" and park_id= " + roadId);
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and CONVERT(varchar, drivein_time, 23) >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and CONVERT(varchar, drivein_time, 23) <= '" + endTime + "'");
        }
        sql.append(" and is_del = '0'");
        return sql.toString();
    }

    /**
     * 描 述： TODO(根据条件获取订单)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getParkingOrder(CommonVo data) {


        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select * from parking_order where is_del = 0 ");

        if (StringUtils.isBlank(data.getCarNo())) {
            sqlbd.append(" and carno_id in ( ");
            sqlbd.append("   select id from operate_carno  where is_del=0 ");
            sqlbd.append("   and car_no = '").append(data.getCarNo()).append("' ");
            sqlbd.append("  ) ");
        }
        List<Integer> listInt = data.getListInt();
        if (listInt != null && listInt.size() > 0) {
            String inId = "";
            for (Integer num : listInt) {
                inId += num + " ,";
            }
            inId = inId.substring(0, inId.length() - 1);
            sqlbd.append(" and id in ( ").append(inId).append(" ) ");
        }
        sqlbd.append(" and is_del = ").append(GlobalData.ISDEL_NO);
        return sqlbd.toString();

    }

    /**
     * create by wp at 2022/1/18 8:46
     * description: 根据停车场查询该区域指定时间段内的停车时长总和
     *
     * @param parkId
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public String getSumTimeByPark(Integer parkId, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" sum(p.resitime)");
        sql.append(" from ");
        sql.append(" parking_order p");
        sql.append(" where 1=1");
        sql.append(" and p.park_id = " + parkId);
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and CONVERT(VARCHAR (10), p.drivein_time, 120 ) >= '" + startTime + "'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and CONVERT(VARCHAR (10), p.drivein_time, 120 ) <= '" + endTime + "'");
        }
        sql.append(" and p.is_del = '0'");
        return sql.toString();
    }

    /**
     * 描 述： TODO(获取订单信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link String}
     */
    public String getUnpaidOrder(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select t1.id , t1.carno_id carnoId , t3.car_no carNo ,   ");
        sqlbd.append(" t1.order_no orderNo, t1.park_id placeId , t2.park_name placeName , ");
        sqlbd.append(" t1.drivein_time driveinTime ,  ");
        sqlbd.append(" t1.driveout_time driveoutTime, ");
        sqlbd.append(" t1.sum_amount sumAmount , 1 type,t1.status as status ");
        sqlbd.append(" from parking_order t1 ");
        sqlbd.append(" left join park t2 on t1.park_id = t2.id ");
        sqlbd.append(" left join operate_carno t3 on t1.carno_id = t3.id  where 1=1 ");

        if (data.getId() != null)
            sqlbd.append(" and t1.id = ").append(data.getId());

        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and t1.order_no = '").append(data.getStr()).append("' ");

        if (data.getVariance() != null) {
            if (data.getVariance() == 1)
                sqlbd.append(" and t1.status in (1 , 2) ");
        }
        if (data.getVariance2() != null)
            sqlbd.append(" and t1.member_id = ").append(data.getVariance2());

        if (StringUtils.isNotBlank(data.getCarNo()))
            sqlbd.append("  and t3.car_no = '").append(data.getCarNo()).append("' ");

        if (StringUtils.isNotBlank(data.getCarType()))
            sqlbd.append("  and t3.car_type = '").append(data.getCarType()).append("' ");

        // sqlbd.append(" and convert(decimal(15,5), t1.sum_amount) > 0 ");
        sqlbd.append(" and t1.is_del = 0 ");

        return sqlbd.toString();
    }


    /**
     * create by zonglina at 2022/1/20 9:33
     * description:
     * 路段和停车场订单信息
     *
     * @return : String
     * @param:RoadParkListVo
     */
    public String selectWXPage(RoadParkListVo bean) {
        String sql = "select * from parking_order where 1=1 ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getUserType()) && GlobalData.PARKING_TYPE_PLAT.equals(bean.getUserType())) {
                sql += " and park_id = '" + bean.getId() + "'";
            }
            if (null != bean.getPlaceId()) {
                sql += " and park_id = '" + bean.getPlaceId() + "'";
            }
            if (notEmpty(bean.getStatus())) {
                if (!"0".equals(bean.getStatus())) {
                    sql += " and status = '" + bean.getStatus() + "'";
                }
            }
            if (notEmpty(bean.getFree_type())) {
                sql += " and free_type = '" + bean.getFree_type() + "'";
            }
            if (notEmpty(bean.getAppeal_status())) {
                sql += " and appeal_status = '" + bean.getAppeal_status() + "'";
            }
            if (notEmpty(bean.getChecked()) && "true".equals(bean.getChecked())) {
                sql += " and car_type = '" + GlobalData.CARTYPE + "'";
            }
            if (null != bean.getCar_list() && bean.getCar_list().size() > 0 && null != bean.getCar_list()) {
                List<Integer> car_list = bean.getCar_list();
                Integer num = 0;
                for (Integer car : car_list) {
                    num++;
                    if (num == 1) {
                        sql += "and ( carno_id = '" + car + "' ";
                    } else {
                        sql += " or carno_id = '" + car + "' ";
                    }
                }
                sql += " ) ";
            }
            if (notEmpty(bean.getCarNo())) {
                sql += " and car_no like '%" + bean.getCarNo() + "%'";
            }
            if (notEmpty(bean.getBerth())) {
                sql += " and berth like '%" + bean.getBerth() + "%'";
            }
            if (notEmpty(bean.getOrderNo())) {
                sql += " and order_no like '%" + bean.getOrderNo() + "%'";
            }
            if (notEmpty(bean.getPhone())) {
                sql += " and phone like '%" + bean.getPhone() + "%'";
            }
            /*if (notEmpty(bean.getStartTime())) {
                sql += " and CONVERT(varchar(10),driveinTime,120) >= '" + bean.getStartTime() + "'";
            }
            if (notEmpty(bean.getEndTime())) {
                sql += " and CONVERT(varchar(10),driveinTime,120) <= '" + bean.getEndTime() + "'";
            }*/
            if (notEmpty(bean.getStartTime())) {
                String s = bean.getStartTime().substring(0, 10) + " 00:00:00";
                sql += " and drivein_time >= '" + s + "'";
            }
            if (notEmpty(bean.getEndTime())) {
                String e = bean.getEndTime().substring(0, 10) + " 23:59:59";
                sql += " and drivein_time <= '" + e + "'";
            }
            if (notEmpty(bean.getMember_id())) {
                sql += " and member_id = '" + bean.getMember_id() + "'";
            }
            if (notEmpty(bean.getIs_discount())) {
                    sql += " and is_discount = '" + bean.getIs_discount() + "'";
            }
            //二次识别
            if (notEmpty(bean.getChecked2()) && "true".equals(bean.getChecked2())) {
                sql += " and status in ('1','2')";
            }
            if (null != bean.getCarno_list() && bean.getCarno_list().size() > 0 && null != bean.getCarno_list()) {
                List<String> car_list = bean.getCarno_list();
                int num = 0;
                for (String car : car_list) {
                    num++;
                    if (num == 1) {
                        sql += "and ( car_no = '" + car + "' ";
                    } else {
                        sql += " or car_no = '" + car + "' ";
                    }
                }
                sql += " ) ";
            }
        }
        sql += " ORDER BY status,drivein_time desc";
        return getQueryHandler(sql).getSql();
    }

    public String getMergeOrder(Integer orderId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append("    rpo.id parking_order_id,");
        sql.append(" 	p.park_name parkName,");
        sql.append(" 	rpo.order_no orderNo,");
        sql.append(" 	rpo.berth,");
        sql.append(" 	rpo.drivein_time driveinTime,");
        sql.append(" 	rpo.driveout_time driveoutTime,");
        sql.append(" 	rpo.resitime,");
        sql.append(" 	rpo.sum_amount,");
        sql.append(" 	oc.car_no carNo,");
        sql.append(" 	oc.car_type");
        sql.append(" FROM");
        sql.append(" 	parking_order rpo");
        sql.append(" 	LEFT JOIN park p ON rpo.park_id = p.id");
        sql.append(" 	LEFT JOIN operate_carno oc ON rpo.carno_id = oc.id ");
        sql.append(" WHERE");
        sql.append(" 	rpo.id = " + orderId);
        sql.append(" 	OR ( rpo.pre_node = " + orderId + " AND rpo.middle_record = '1' ) ");
        sql.append(" ORDER BY");
        sql.append(" 	rpo.create_time ASC");

        return sql.toString();
    }

    /**
     * 微信小程序路段和停车场订单查询
     * 待开发票数据
     * 分页查询
     *
     * @author thr
     */
    public String selectFpPage(RoadParkListVo bean) {
        String sql = "select * from road_park_list where invoiceId is null ";
        sql += " and (status = '" + GlobalData.PARKING_ORDER_ALREADYPAY + "'" +
                " or status = '" + GlobalData.PARKING_ORDER_COMPLETE + "')";

        sql += " and (( " +
                " type = '1' " +
                " AND id NOT IN ( SELECT distinct o.parking_order_id FROM operate_appeal o where o.parking_order_id = id and is_del = '0' and appeal_type = '1' and appeal_status='1')) " +
                " OR ( " +
                " type = '0' " +
                " AND id NOT IN ( SELECT distinct o.parking_order_id FROM operate_appeal o where o.parking_order_id = id and is_del = '0' and appeal_type = '0' and appeal_status='1'))) ";

        if (notEmpty(bean)) {
            if (notEmpty(bean.getMember_id())) {
                sql += " and carId in (select id from operate_carno where is_del = '0' and member_id = " + bean.getMember_id() + ") ";
            }
            if (notEmpty(bean.getCarId())) {
                sql += " and carId = " + bean.getCarId();
            }
            if (notEmpty(bean.getCarNo())) {
                sql += " and carNo = '" + bean.getCarNo() + "'";
            }
            if (notEmpty(bean.getTimeType())) {
                if (bean.getTimeType().compareTo(0) == 0) {
                    //上月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 1";
                } else if (bean.getTimeType().compareTo(1) == 0) {
                    //本月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 0";
                }
            } else {
                if (notEmpty(bean.getStartTime())) {
                    sql += " and create_time >= '" + bean.getStartTime() + "'";
                }
                if (notEmpty(bean.getEndTime())) {
                    sql += " and create_time < '" + new DateTime(bean.getEndTime().replace(" ", "T")).plusDays(1).toString("yyy-MM-dd") + "'";
                }
            }
        }
        sql += " and cast(sum_amount as numeric(18,2))>0 ";
        sql += " ORDER BY create_time desc";
        return getQueryHandler(sql).getSql();
    }

    /**
     * 微信小程序路段订单ids
     * 待开发票数据
     * 停车场、路段标识（0.路段1停车场）
     *
     * @author thr
     */
    public String getRoadParkOrderIds(RoadParkListVo bean) {
        String sql = "SELECT " +
                " STUFF( " +
                "( SELECT ',' + convert(varchar,id) FROM road_park_list " +
                " WHERE type = '0' " +
                " and invoiceId is null " +
                " and (status = '" + GlobalData.PARKING_ORDER_ALREADYPAY + "'" +
                " or status = '" + GlobalData.PARKING_ORDER_COMPLETE + "')";

        sql += " AND id NOT IN ( SELECT distinct o.parking_order_id FROM operate_appeal o where o.parking_order_id = id and is_del = '0' and appeal_type = '0' and appeal_status='1') ";

        if (notEmpty(bean)) {
            if (notEmpty(bean.getMember_id())) {
                sql += " and carId in (select id from operate_carno where is_del = '0' and member_id = " + bean.getMember_id() + ") ";
            }
            if (notEmpty(bean.getCarId())) {
                sql += " and carId = " + bean.getCarId();
            }
            if (notEmpty(bean.getCarNo())) {
                sql += " and carNo = '" + bean.getCarNo() + "'";
            }
            if (notEmpty(bean.getTimeType())) {
                if (bean.getTimeType().compareTo(0) == 0) {
                    //上月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 1";
                } else if (bean.getTimeType().compareTo(1) == 0) {
                    //本月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 0";
                }
            } else {
                if (notEmpty(bean.getStartTime())) {
                    sql += " and create_time >= '" + bean.getStartTime() + "'";
                }
                if (notEmpty(bean.getEndTime())) {
                    sql += " and create_time < '" + new DateTime(bean.getEndTime().replace(" ", "T")).plusDays(1).toString("yyy-MM-dd") + "'";
                }
            }
        }
        sql += " FOR xml path ( '' ) ), 1, 1, '' " +
                ") AS roadOrderIds, " +
                " COUNT(id) roadOrderCount," +
                " SUM(CAST ( sum_amount AS DECIMAL ( 10, 2 ) )) as roadOrderMoney " +
                " FROM " +
                " road_park_list " +
                " WHERE " +
                " type = '0' " +
                " and invoiceId is null ";
        sql += " and (status = '" + GlobalData.PARKING_ORDER_ALREADYPAY + "'" +
                " or status = '" + GlobalData.PARKING_ORDER_COMPLETE + "')";

        sql += " AND id NOT IN ( SELECT distinct o.parking_order_id FROM operate_appeal o where o.parking_order_id = id and is_del = '0' and appeal_type = '0' and appeal_status='1') ";

        if (notEmpty(bean)) {
            if (notEmpty(bean.getMember_id())) {
                sql += " and carId in (select id from operate_carno where is_del = '0' and member_id = " + bean.getMember_id() + ") ";
            }
            if (notEmpty(bean.getCarId())) {
                sql += " and carId = " + bean.getCarId();
            }
            if (notEmpty(bean.getCarNo())) {
                sql += " and carNo = '" + bean.getCarNo() + "'";
            }
            if (notEmpty(bean.getTimeType())) {
                if (bean.getTimeType().compareTo(0) == 0) {
                    //上月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 1";
                } else if (bean.getTimeType().compareTo(1) == 0) {
                    //本月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 0";
                }
            } else {
                if (notEmpty(bean.getStartTime())) {
                    sql += " and create_time >= '" + bean.getStartTime() + "'";
                }
                if (notEmpty(bean.getEndTime())) {
                    sql += " and create_time < '" + new DateTime(bean.getEndTime().replace(" ", "T")).plusDays(1).toString("yyy-MM-dd") + "'";
                }
            }
        }
        return getQueryHandler(sql).getSql();
    }

    /**
     * 微信小程序停车场订单ids
     * 待开发票数据
     * 停车场、路段标识（0.路段1停车场）
     *
     * @author thr
     */
    public String getParkOrderIds(RoadParkListVo bean) {
        String sql = "SELECT " +
                " STUFF( " +
                "( SELECT ',' + convert(varchar,id) FROM road_park_list " +
                " WHERE type = '1' " +
                " and invoiceId is null " +
                " and (status = '" + GlobalData.PARKING_ORDER_ALREADYPAY + "'" +
                " or status = '" + GlobalData.PARKING_ORDER_COMPLETE + "')";

        sql += " AND id NOT IN ( SELECT distinct o.parking_order_id FROM operate_appeal o where o.parking_order_id = id and is_del = '0' and appeal_type = '1' and appeal_status='1')";

        if (notEmpty(bean)) {
            if (notEmpty(bean.getMember_id())) {
                sql += " and carId in (select id from operate_carno where is_del = '0' and member_id = " + bean.getMember_id() + ") ";
            }
            if (notEmpty(bean.getCarId())) {
                sql += " and carId = " + bean.getCarId();
            }
            if (notEmpty(bean.getCarNo())) {
                sql += " and carNo = '" + bean.getCarNo() + "'";
            }
            if (notEmpty(bean.getTimeType())) {
                if (bean.getTimeType().compareTo(0) == 0) {
                    //上月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 1";
                } else if (bean.getTimeType().compareTo(1) == 0) {
                    //本月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 0";
                }
            } else {
                if (notEmpty(bean.getStartTime())) {
                    sql += " and create_time >= '" + bean.getStartTime() + "'";
                }
                if (notEmpty(bean.getEndTime())) {
                    sql += " and create_time < '" + new DateTime(bean.getEndTime().replace(" ", "T")).plusDays(1).toString("yyy-MM-dd") + "'";
                }
            }
        }
        sql += " FOR xml path ( '' ) ), 1, 1, '' " +
                ") AS parkOrderIds, " +
                " COUNT(id) parkOrderCount," +
                " SUM(CAST ( sum_amount AS DECIMAL ( 10, 2 ) )) as parkOrderMoney " +
                " FROM " +
                " road_park_list " +
                " WHERE " +
                " type = '1' " +
                " and invoiceId is null ";
        sql += " and (status = '" + GlobalData.PARKING_ORDER_ALREADYPAY + "'" +
                " or status = '" + GlobalData.PARKING_ORDER_COMPLETE + "')";

        sql += " AND id NOT IN ( SELECT distinct o.parking_order_id FROM operate_appeal o where o.parking_order_id = id and is_del = '0' and appeal_type = '0' and appeal_status='1') ";

        if (notEmpty(bean)) {
            if (notEmpty(bean.getMember_id())) {
                sql += " and carId in (select id from operate_carno where is_del = '0' and member_id = " + bean.getMember_id() + ") ";
            }
            if (notEmpty(bean.getCarId())) {
                sql += " and carId = " + bean.getCarId();
            }
            if (notEmpty(bean.getCarNo())) {
                sql += " and carNo = '" + bean.getCarNo() + "'";
            }
            if (notEmpty(bean.getTimeType())) {
                if (bean.getTimeType().compareTo(0) == 0) {
                    //上月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 1";
                } else if (bean.getTimeType().compareTo(1) == 0) {
                    //本月
                    sql += " and DateDiff(mm, create_time, GetDate()) = 0";
                }
            } else {
                if (notEmpty(bean.getStartTime())) {
                    sql += " and create_time >= '" + bean.getStartTime() + "'";
                }
                if (notEmpty(bean.getEndTime())) {
                    sql += " and create_time < '" + new DateTime(bean.getEndTime().replace(" ", "T")).plusDays(1).toString("yyy-MM-dd") + "'";
                }
            }
        }
        return getQueryHandler(sql).getSql();
    }

    /**
     * 描 述： TODO(查询各停车场在停订单统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param status
     * @return {@link String}
     */
    public String getParkingOrderCount(String status) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select park_id parkId , count(1) counts from parking_order  ");
        sqlbd.append(" where is_del = ").append(GlobalData.ISDEL_NO);
        sqlbd.append(" and status = ").append(status);
        sqlbd.append(" group by park_id ");

        return sqlbd.toString();
    }

    //根据id查询订单信息(微信端)
    public String selectByWxId(Integer id) {
        String sql = "select o.member_id,o.id,order_no,car.car_no,r.park_name as road_name,m.phone,o.berth ,o.resitime,o.source,o.create_time,m.phone,o.sum_amount,o.discount_amount,o.paid_amount,o.unpaid_amount,o.driveout_time,o.drivein_time," +
                " o.payment_id,o.invoice_id,(select label from sys_dict d where d.dict_type='roadStatus' and o.status= d.dc_value) as statusName,o.status," +
                " (select label from sys_dict d where d.dict_type='car_type' and car.car_type= d.dc_value) as carType,car.car_type as car_type,free_type,fxReason from parking_order o " +
                " LEFT JOIN operate_carno car on o.carno_id=car.id" +
                " LEFT JOIN member_manage m on m.id=o.member_id " +
                " LEFT JOIN park r on r.id=o.park_id" +
                " where 1=1 and o.id =" + id;
        return getQueryHandler(sql).getSql();
    }

    public String getUnGrantInvoice(String parkids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" ");
        sql.append(" SELECT");
        sql.append(" p.*");
        sql.append(" FROM");
        sql.append(" parking_order p");
        sql.append(" LEFT JOIN invoices_management i ON p.invoice_id = i.id");
        sql.append(" LEFT JOIN operate_appeal o ON p.id = o.parking_order_id");
        sql.append(" WHERE");
        sql.append(" 1 = 1");
        sql.append(" AND i.id IS NULL");
        sql.append(" AND o.appeal_status != '1'");
        sql.append(" AND p.is_invoice = '0'");
        sql.append(" AND p.id in (" + parkids + ")");
        sql.append(" AND p.is_del = '0'");
        return sql.toString();
    }

    /**
     * 描 述： TODO(查询流动订单总数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    public String getStreamingOrderCount(String dateStr, Integer park_id) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select count(1) counts from parking_order ");
        sqlbd.append(" where is_del = ").append(GlobalData.ISDEL_NO);
        sqlbd.append(" and ( free_type not in ( ").append(GlobalData.FREETYPEBY).append(" , ")
                .append(GlobalData.FREETYPEQYBY).append(" ) ");
        sqlbd.append("  or free_type is null ) ");
        sqlbd.append(" and CONVERT(varchar, drivein_time, 23) = '").append(dateStr).append("' ");
        if (park_id != null)
            sqlbd.append(" and park_id = ").append(park_id);

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(查询流动订单总收入)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    public String getStreamingOrderSumAmount(String dateStr, Integer park_id) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select sum( CONVERT(decimal( 15 , 2 ) , paid_amount ) ) sumAmount from parking_order ");
        sqlbd.append(" where is_del = ").append(GlobalData.ISDEL_NO);
        sqlbd.append(" and ( free_type not in ( ").append(GlobalData.FREETYPEBY).append(" , ")
                .append(GlobalData.FREETYPEQYBY).append(" ) ");
        sqlbd.append("  or free_type is null ) ");
        if (StringUtils.isNotBlank(dateStr))
            sqlbd.append(" and CONVERT(varchar, driveout_time, 23) = '").append(dateStr).append("' ");

        if (park_id != null)
            sqlbd.append(" and park_id = ").append(park_id);

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(查询流动订单应收)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param dateStr
     * @return {@link String}
     */
    public String getStreamingOrderReceivable(String dateStr, Integer park_id) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select sum( CONVERT(decimal( 15 , 2 ) , sum_amount ) ) sumAmount from parking_order ");
        sqlbd.append(" where is_del = ").append(GlobalData.ISDEL_NO);
        sqlbd.append(" and ( free_type not in ( ").append(GlobalData.FREETYPEBY).append(" , ")
                .append(GlobalData.FREETYPEQYBY).append(" ) ");
        sqlbd.append("  or free_type is null ) ");

        if (StringUtils.isNotBlank(dateStr))
            sqlbd.append(" and CONVERT(varchar, driveout_time, 23) = '").append(dateStr).append("' ");

        if (park_id != null)
            sqlbd.append(" and park_id = ").append(park_id);

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(今日停车订单数排名)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     */
    public String getParkingOrderRanking(CommonVo data) {
        String sql = " '-' as zzl ";
        if (StringUtils.isNotEmpty(data.getStartTime()) && StringUtils.isNotEmpty(data.getEndTime())) {
            LocalDate startDate = LocalDate.parse(data.getStartTime());
            LocalDate endDate = LocalDate.parse(data.getEndTime());
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
//            System.out.println("两个日期之间的天数差异：" + daysBetween);

            sql = " case when MAX ( t2.park_num ) <> 0 then\n" +
                    "  ROUND(convert(FLOAT,COUNT ( p.id ))/convert(FLOAT,( MAX ( t2.park_num ) ))/convert(FLOAT," + daysBetween + "),2)\n" +
                    "  else 0 end as zzl ";
        }

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" SELECT\n" +
                "\tt2.id,\n" +
                "\tMAX ( t2.park_name ) title,\n" +
                "\tMAX ( t2.park_num ) stopcarnum,\n" +
                "\tCOUNT ( p.id ) counts,\n" +
                "\tSUM ( case when p.is_del = '0' and p.free_type is null then 1 else 0 end ) counts2,\n" +
//                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), p.sum_amount )), 0 ) sumAmount ,\n" +
//                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), p.paid_amount ) ), 0 ) paidAmount ,\n" +
//                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), p.unpaid_amount ) ), 0 ) unpaidAmount \n" +
                "\tisnull( SUM ( case when p.is_del = '0' and p.free_type is null then CONVERT ( DECIMAL ( 15, 2 ), p.sum_amount ) else 0 end ), 0 ) sumAmount,\n" +
                "\tisnull( SUM ( case when p.is_del = '0' and p.free_type is null then CONVERT ( DECIMAL ( 15, 2 ), p.paid_amount ) else 0 end ), 0 ) paidAmount,\n" +

                "\tisnull( SUM ( case when p.is_del = '0' and p.free_type is null and p.status=1 then CONVERT ( DECIMAL ( 15, 2 ), p.unpaid_amount ) else 0 end ), 0 ) unpaidAmount,\n" +
                "\tisnull( SUM ( case when p.is_del = '0' and p.free_type is null and p.status=2 then CONVERT ( DECIMAL ( 15, 2 ), p.unpaid_amount ) else 0 end ), 0 ) unpaidAmount2,\n" +

                "\tisnull( SUM ( CASE WHEN p.is_del = '0' AND p.free_type IS NULL AND p.status IN ( '3', '4' ) AND p.sum_amount > '0.00' AND p.pay_time IS NOT NULL AND p.driveout_time IS NOT NULL \n" +
                "\tAND CONVERT ( VARCHAR, p.pay_time, 23 ) <> CONVERT ( VARCHAR, p.driveout_time, 23 ) THEN 1 ELSE 0 END ), 0 ) bjcs ,\n" +

                "\tisnull( SUM ( CASE WHEN p.is_del = '0' AND p.free_type IS NULL AND p.status IN ( '3', '4' ) AND p.sum_amount > '0.00' AND p.pay_time IS NOT NULL AND p.driveout_time IS NOT NULL \n" +
                "\tAND CONVERT ( VARCHAR, p.pay_time, 23 ) <> CONVERT ( VARCHAR, p.driveout_time, 23 ) THEN CONVERT ( DECIMAL ( 15, 2 ), p.paid_amount ) ELSE 0 END ), 0 ) bjje ,\n" +

                "\tisnull( SUM ( case when p.is_del = '0' and p.free_type is null and p.status in ('3','4') and p.sum_amount > '0.00' then 1 else 0 end ), 0 ) jfs,\n" +
//                "\tisnull( SUM ( case when p.is_del='0' and p.sum_amount > '0.00' then 1 else 0 end ), 0 ) zs,\n" +

                "\tcase when isnull( SUM ( case when p.is_del = '0' and p.free_type is null then 1 else 0 end ), 0 ) <> 0 then \n" +
                "\tROUND(convert(FLOAT,isnull( SUM ( case when p.is_del = '0' and p.free_type is null and p.status in ('3','4') and p.sum_amount > '0.00' then 1 else 0 end ), 0 ))/convert(FLOAT,isnull( SUM ( case when p.is_del = '0' and p.free_type is null then 1 else 0 end ), 0 )),2)\n" +
                "\telse 0 end as jfl,\n" +
                sql +
                "FROM\n" +
                "\tpark t2\n" +
                "\tLEFT JOIN parking_order p ON p.park_id = t2.id \n" +
                "\tAND p.is_del= '0' \n");
        if (StringUtils.isNotEmpty(data.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.drivein_time, 23) >=  '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(data.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.drivein_time, 23) <=  '").append(data.getEndTime()).append("' ");
        }
        sqlbd.append("WHERE\n" +
                "\tt2.is_del = '0' \n");
//        sqlbd.append(" and p.free_type is null ");
        if (data.getParkId() != null) {
            sqlbd.append(" and t2.id = ").append(data.getParkId());
        }
        if (StringUtils.isNotEmpty(data.getName())) {
            sqlbd.append(" and t2.park_name like '%").append(data.getName()).append("%' ");
        }

        sqlbd.append("GROUP BY\n" +
                "\tt2.id \n" +
                "ORDER BY\n" +
                "\tcounts DESC, title");
        return sqlbd.toString();
    }

    /**
     * 补缴金额
     */
    public String getParkingOrderBjje(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" SELECT\n" +
                "\tisnull( SUM ( CASE WHEN p.is_del = '0' AND p.free_type IS NULL AND p.status IN ( '3', '4' ) AND p.sum_amount > '0.00' AND p.pay_time IS NOT NULL AND p.driveout_time IS NOT NULL \n" +
                "\tAND CONVERT ( VARCHAR, p.pay_time, 23 ) <> CONVERT ( VARCHAR, p.driveout_time, 23 ) THEN CONVERT ( DECIMAL ( 15, 2 ), p.paid_amount ) ELSE 0 END ), 0 ) bjje \n" +
                "FROM\n" +
                "\tpark t2\n" +
                "\tLEFT JOIN parking_order p ON p.park_id = t2.id \n" +
                "\tAND p.is_del= '0' \n");
        if (StringUtils.isNotEmpty(data.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.drivein_time, 23) >=  '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(data.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.drivein_time, 23) <=  '").append(data.getEndTime()).append("' ");
        }
        sqlbd.append("WHERE\n" +
                "\tt2.is_del = '0' \n");
//        sqlbd.append(" and p.free_type is null ");
        if (data.getParkId() != null) {
            sqlbd.append(" and t2.id = ").append(data.getParkId());
        }
        if (StringUtils.isNotEmpty(data.getName())) {
            sqlbd.append(" and t2.park_name like '%").append(data.getName()).append("%' ");
        }
        return sqlbd.toString();
    }

    /**
     * 停车订单列表
     */
    public String getParkingOrderData(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" SELECT\n" +
                "\tp.* ,\n" +
                "\toc.car_no,\n" +
                "\toc.car_type,\n" +
                "\tstg.label state_name \n" +
                "FROM\n" +
                "\tparking_order p\n" +
                "\tLEFT JOIN operate_carno oc ON p.carno_id = oc.id\n" +
                "\tLEFT JOIN sys_dict stg ON stg.dc_value = p.status \n" +
                "\tAND stg.dict_type = 'roadStatus' \n" +
                "WHERE\n" +
                "\tp.is_del = '0' \n");
        if (StringUtils.isNotEmpty(data.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.drivein_time, 23) >=  '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(data.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.drivein_time, 23) <=  '").append(data.getEndTime()).append("' ");
        }
        if (StringUtils.isNotNull(data.getParkId())) {
            sqlbd.append(" and p.park_id = ").append(data.getParkId());
        }
        // 1应收金额 2实收金额 3欠费金额
        if (StringUtils.isNotNull(data.getPageType())) {
            sqlbd.append(" AND p.free_type IS NULL ");
            if (data.getPageType() == 1) {
                sqlbd.append(" and cast(p.sum_amount as numeric(15,2)) > 0 ");
            } else if (data.getPageType() == 2) {
                sqlbd.append(" and cast(p.paid_amount as numeric(15,2)) > 0 ");
            } else if (data.getPageType() == 3) {
                sqlbd.append(" and p.status='1' and cast(p.unpaid_amount as numeric(15,2)) > 0 ");
            } else if (data.getPageType() == 4) {
                sqlbd.append(" and p.status='2' and cast(p.unpaid_amount as numeric(15,2)) > 0 ");
            }

        }
        return sqlbd.toString();
    }

    /**
     * 临停收入、临停欠费金额
     */
    public String getSumLtsr(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" SELECT\n" +
                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), p.sum_amount )), 0 ) sumAmount ,\n" +
                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), p.paid_amount ) ), 0 ) paidAmount ,\n" +
//                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), po.amount )), 0 ) paidAmount ,\n" +
//                "\tisnull( SUM ( case when p.status in ('3','4') then CONVERT ( DECIMAL ( 15, 2 ), po.amount ) else 0 end ), 0 ) paidAmount, \n" +
                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), p.unpaid_amount ) ), 0 ) unpaidAmount \n" +
                "FROM\n" +
                "\tpark t2\n" +
                "\tLEFT JOIN parking_order p ON p.park_id = t2.id \n" +
                "\tLEFT JOIN operate_carno o ON p.carno_id= o.id \n");
//        sqlbd.append(" LEFT JOIN payment_order po ON p.payment_id = po.id " +
//                "AND po.payment_type IN ( 2, 3, 5, 8 ) " +
//                "AND po.is_del = '0' " +
//                "and po.status='2'");

        sqlbd.append("WHERE\n" +
                "\tt2.is_del = '0' \n");
        sqlbd.append(" and p.is_del = '0' ");
        sqlbd.append(" and p.free_type is null ");
        if (StringUtils.isNotEmpty(data.getStartTime())) {
            sqlbd.append(" and CONVERT(varchar, p.drivein_time, 23) >=  '").append(data.getStartTime()).append("' ");
//            sqlbd.append(" and CONVERT(varchar, po.pay_time, 23) >=  '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(data.getEndTime())) {
            sqlbd.append(" and CONVERT(varchar, p.drivein_time, 23) <=  '").append(data.getEndTime()).append("' ");
//            sqlbd.append(" and CONVERT(varchar, po.pay_time, 23) <=  '").append(data.getEndTime()).append("' ");
        }
        if (notEmpty(data.getParkId())) {
            sqlbd.append(" and t2.id = ").append(data.getParkId());
        }
        if (notEmpty(data.getName())) {
            sqlbd.append(" and t2.park_name like '%").append(data.getName()).append("%' ");
        }
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(查询订单应收)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getOrderReceivable(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select sum( CONVERT(decimal( 15 , 2 ) , sum_amount ) ) sumAmount from parking_order ");
        sqlbd.append(" where is_del = ").append(GlobalData.ISDEL_NO);

        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and CONVERT(varchar, drivein_time, 23) = '").append(data.getStr()).append("' ");

        if (data.getVariance() != null)
            sqlbd.append(" and area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance3());


        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(查询订单总收入)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getOrderSumAmount(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select sum( CONVERT(decimal( 15 , 2 ) , paid_amount ) ) sumAmount from payment_order o  LEFT JOIN parking_order ps on o.id = ps.payment_id ");
        sqlbd.append(" where o.is_del='0' and o.status='2' ");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and CONVERT(varchar, o.pay_time, 23) = '").append(data.getStr()).append("' ");
        if (data.getVariance() != null)
            sqlbd.append(" and area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance3());

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(获取出入场统计数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getLeaveEntryHourCount(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select count(1) quantity , ");
        if (StringUtils.isBlank(data.getStr2()) || data.getStr2().equals("1")) {
            sqlbd.append(" subString( convert(varchar, drivein_time, 20)  , 12, 2) hourStr ");
        } else {
            sqlbd.append(" subString( convert(varchar, driveout_time, 20)  , 12, 2) hourStr ");
        }

        sqlbd.append(" from parking_order where 1=1 ");

        if (StringUtils.isBlank(data.getStr2()) || data.getStr2().equals("1")) {
            sqlbd.append(" and convert(varchar, drivein_time, 23) = '").append(data.getStr()).append("' ");
        } else {
            sqlbd.append(" and convert(varchar, driveout_time, 23) = '").append(data.getStr()).append("' ");
        }

        if (data.getVariance() != null)
            sqlbd.append(" and area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance3());

        sqlbd.append(" and is_del = ").append(GlobalData.ISDEL_NO);
        if (StringUtils.isBlank(data.getStr2()) || data.getStr2().equals("1")) {
            sqlbd.append(" GROUP BY subString( convert(varchar, drivein_time, 20)  , 12, 2)   ");
        } else {
            sqlbd.append(" GROUP BY subString( convert(varchar, driveout_time, 20)  , 12, 2)   ");
        }
        sqlbd.append(" ORDER BY hourStr ");
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO( 根据收款类型统计 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getCollectionsStatistics(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select CASE o.payment_type   ");
        sqlbd.append(" WHEN '1' THEN '包月' ");
        sqlbd.append(" WHEN '2' THEN '微信' ");
        sqlbd.append(" WHEN '3' THEN '支付宝' ");
        sqlbd.append(" WHEN '4' THEN '钱包' ");
        sqlbd.append(" WHEN '5' THEN '现金' ");
        sqlbd.append(" else '其他'  ");
        sqlbd.append(" end payTypeName , sum(convert(decimal(15,2) , r.paid_amount) ) sumAmount ");
        sqlbd.append(" from parking_order r LEFT JOIN payment_order o on r.payment_id = o.id and o.is_del='0' and o.status='2'");
        sqlbd.append(" where 1=1 ");
//        sqlbd.append(" and r.status in (3,4)");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and convert(varchar, o.pay_time, 23) = '").append(data.getStr()).append("' ");

        if (data.getVariance() != null)
            sqlbd.append(" and r.area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and r.street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and r.park_id = ").append(data.getVariance3());

        sqlbd.append(" and r.is_del = 0 ");
        sqlbd.append(" group by  o.payment_type ");
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(获取欠费停车数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getArrearageParkCount(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select count(distinct carno_id) counts from parking_order ");
        sqlbd.append(" where status = 1 ");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and convert(varchar, create_time, 23) = '").append(data.getStr()).append("' ");
        sqlbd.append(" and carno_id in ( ");
        sqlbd.append("    select carno_id   from parking_order ");
        sqlbd.append("    where status = 2 ");
        if (data.getVariance() != null)
            sqlbd.append(" and area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance3());

        sqlbd.append(" ) and is_del = 0");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(查询停车场订单信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getParkDeviceInfo(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select  t1.order_no, t1.member_id , convert(varchar(19),t1.drivein_time,120) driveinTime , ");
        sqlbd.append(" t2.car_no , t3.park_name packName, ");
        sqlbd.append(" (select sum( convert(decimal(15, 2), sum_amount) ) from parking_order t2 where status = 2 and t2.carno_id = t1.carno_id )  arrearage  ");
        sqlbd.append(" from parking_order t1 ");
        sqlbd.append(" left join operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append(" left join park t3 on t1.park_id = t3.id ");

        sqlbd.append(" where t1.is_del = ").append(GlobalData.ISDEL_NO);
        sqlbd.append(" and t1.status = ").append(GlobalData.PARKING_ORDER_STOP);

        if (data.getVariance() != null)
            sqlbd.append(" and t1.area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and t1.street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and t1.park_id = ").append(data.getVariance3());

        if (StringUtils.isNotBlank(data.getCarNo()))
            sqlbd.append(" and t2.car_no = '").append(data.getCarNo()).append("' ");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 流动车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getMobileCartCount(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select p.park_id parkId, count(p.id) ldCarNum,  ");
        sqlbd.append(" sum(convert(decimal(15,5) ,po.amount) ) ldSumPaidAmount ");
        sqlbd.append(" from parking_order p left join payment_order po on p.payment_id = po.id  and po.is_del='0' and po.status='2' where p.is_del = 0 ");
        sqlbd.append(" and (p.status = 3 or p.status = 4) ");
        sqlbd.append(" and po.payment_type in (2,3,4,5,8) ");
        sqlbd.append(" and (p.free_type IS NULL or p.free_type =").append(GlobalData.FREETYPETIME).append(") ");
//        sqlbd.append(" and p.free_type IS NULL ");
//        sqlbd.append(" AND po.is_del = '0'  and po.status='2' ");

        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append(" and ( convert(varchar , po.pay_time , 20) >= '").append(data.getStartTime()).append(" 00:00:00").append("'  )");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append(" and (convert(varchar , po.pay_time , 20) <= '").append(data.getEndTime()).append(" 23:59:59").append("' )");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and (convert(varchar , po.pay_time , 20) < '").append(data.getStr()).append(" 23:59:59").append("' )");

        if (data.getVariance() != null)
            sqlbd.append(" and p.park_id = ").append(data.getVariance());

        sqlbd.append(" group by p.park_id ");

        return sqlbd.toString();
    }

    public String getAllCartCount(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select park_id parkId,count(1) zjCarNum");
        sqlbd.append(" from parking_order where is_del = 0 ");
        sqlbd.append(" and (status = 3 or status = 4)");
        sqlbd.append(" and (free_type IS NULL or free_type =").append(GlobalData.FREETYPETIME).append("or free_type =").append(GlobalData.FREETYPEBY).append(") ");

        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append(" and ( convert(varchar , pay_time , 20) >= '").append(data.getStartTime()).append(" 00:00:00").append("' or pay_time is NULL )");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append(" and (convert(varchar , pay_time , 20) <= '").append(data.getEndTime()).append(" 23:59:59").append("' or pay_time is NULL)");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and (convert(varchar , pay_time , 20) < '").append(data.getStr()).append(" 23:59:59").append("' or pay_time is NULL)");

        if (data.getVariance() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance());

        sqlbd.append(" group by park_id ");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 流动车各收费方式收费总额)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getMobileCartPayStatistics(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select p.park_id parkId, ");
        sqlbd.append(" SUM( CASE po.payment_type WHEN '2' THEN convert(decimal(15,5) , po.amount ) ELSE 0 END  )AS ldWxAmount, ");
        sqlbd.append(" SUM( CASE po.payment_type WHEN '3' THEN convert(decimal(15,5) , po.amount ) ELSE 0 END ) AS ldZfbAmount, ");
        sqlbd.append(" SUM( CASE po.payment_type WHEN '4' THEN convert(decimal(15,5) , po.amount ) ELSE 0 END ) AS ldQbAmount, ");
        sqlbd.append(" SUM( CASE po.payment_type WHEN '5' THEN convert(decimal(15,5) , po.amount ) ELSE 0 END )AS ldXjAmount, ");
        sqlbd.append(" SUM( CASE po.payment_type WHEN '8' THEN convert(decimal(15,5) , po.amount ) ELSE 0 END )AS ldDsfAmount ");
        sqlbd.append(" from payment_order po left join parking_order p on p.payment_id = po.id   and po.is_del='0' and po.status='2' where p.is_del = 0  ");
        sqlbd.append(" and (p.status = 3 or p.status = 4) ");
        sqlbd.append(" and ( p.free_type not in ( ").append(GlobalData.FREETYPEBY).append(" , ")
                .append(GlobalData.FREETYPEQYBY).append(" ) ");
        sqlbd.append("  or p.free_type is null ) ");
        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append(" and convert(varchar , po.pay_time , 20) >= '").append(data.getStartTime()).append(" 00:00:00").append("' ");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append(" and convert(varchar , po.pay_time , 20) <= '").append(data.getEndTime()).append(" 23:59:59").append("' ");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and convert(varchar , po.pay_time , 20) < '").append(data.getStr()).append(" 23:59:59").append("' ");

        if (data.getVariance() != null)
            sqlbd.append(" and p.park_id = ").append(data.getVariance());

        sqlbd.append(" group by p.park_id ");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 人工抬杆统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getArtificialLiftRodCount(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select  park_id  parkId, ");
        sqlbd.append(" count(CASE artificial_open WHEN '0' THEN 1 END ) AS ldGwOrderCount , ");
        sqlbd.append(" SUM(CASE artificial_open WHEN '0' THEN convert(decimal(15,5) , sum_amount ) ELSE 0 END) AS ldGwAmount, ");
        sqlbd.append(" count(CASE artificial_open WHEN '1' THEN 1 END ) AS ldGzOrderCount, ");
        sqlbd.append(" SUM(CASE artificial_open WHEN '1' THEN convert(decimal(15,5) , paid_amount ) ELSE 0 END) AS ldGzAmount ");
        sqlbd.append(" from parking_order where is_del = 0 ");

        sqlbd.append(" and (status = 3 or status = 4) ");
        sqlbd.append(" and artificial_open is not null  ");

        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) >= '").append(data.getStartTime()).append("' ");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) <= '").append(data.getEndTime()).append("' ");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) < '").append(data.getStr()).append("' ");

        if (data.getVariance() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance());

        sqlbd.append(" group by park_id ");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 逃费统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getEscapeFeeStatistics(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select park_id parkId , count(1) tdCounts ,  ");
        sqlbd.append(" sum( convert(decimal(15,5) , sum_amount )) tdSumAmount ");
        sqlbd.append(" from parking_order where is_del = 0 ");
        sqlbd.append(" and status = 2 ");

        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) >= '").append(data.getStartTime()).append("' ");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) <= '").append(data.getEndTime()).append("' ");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) < '").append(data.getStr()).append("' ");

        if (data.getVariance() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance());

        sqlbd.append(" group by park_id ");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 免费停车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getFreeParkingCount(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select t1.park_id parkId , ");
        sqlbd.append(" count( CASE t2.whitelist_type WHEN '0' THEN 1 END ) AS mfNbCount, ");
        sqlbd.append(" count( CASE t2.whitelist_type WHEN '1' THEN 1 END ) AS mfQyCount ");
        sqlbd.append(" from parking_order t1 ");
        sqlbd.append(" left join operate_carno t2 on t1.carno_id = t2.id ");
        sqlbd.append(" where t1.is_del = 0  ");
        sqlbd.append(" and (t1.status = 3 or t1.status = 4)");
        sqlbd.append(" and t1.free_type = 1 ");

        if (StringUtils.isNotBlank(data.getStartTime()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) >= '").append(data.getStartTime()).append("' ");
        if (StringUtils.isNotBlank(data.getEndTime()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) <= '").append(data.getEndTime()).append("' ");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and convert(varchar , driveout_time , 20) < '").append(data.getStr()).append("' ");

        if (data.getVariance() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance());

        sqlbd.append(" group by t1.park_id ");

        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(停车场 运营报表 营收总览 免费停车统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getWhiteCarnoCount(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" SELECT\n" +
                "\tt1.park_id parkId,\n" +
                "\tCOUNT ( CASE t2.whitelist_type WHEN '0' THEN 1 END ) AS mfNbCount,\n" +
                "\tCOUNT ( CASE t2.whitelist_type WHEN '1' THEN 1 END ) AS mfQyCount \n" +
                "FROM\n" +
                "\twhitecarno_park t1\n" +
                "\tLEFT JOIN operate_carno t2 ON t1.carno_id = t2.id \n" +
                "\tAND t2.is_del = 0 \n" +
                "WHERE\n" +
                "\tt1.is_del = 0 \n" +
                "\tAND t1.park_type = 1 \n");
        if (data.getVariance() != null) {
            sqlbd.append(" and t1.park_id = ").append(data.getVariance());
        }
        sqlbd.append("GROUP BY\n" +
                "\tt1.park_id");
        return sqlbd.toString();
    }

    /**
     * create by wh at 2022/1/28 15:35
     * description: 人工抬杆/车辆总数
     *
     * @param
     * @return java.lang.String
     */
    public String getOpenGateCarNums(Integer park_id, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" count(distinct(carno_id)) count");
        sql.append(" from ");
        sql.append(" parking_order");
        sql.append(" where 1=1");
        sql.append(" and is_del = '0'");
        sql.append(" AND artificial_open IS NOT NULL");
        if (notEmpty(startTime)) {
            sql.append(" and drivein_time <= '" + startTime + "'");
        }
        if (notEmpty(endTime)) {
            sql.append(" and drivein_time >= '" + endTime + "'");
        }
        if (notEmpty(park_id)) {
            sql.append(" and park_id = '" + park_id + "'");
        }
        return sql.toString();
    }

    /**
     * create by wh at 2022/1/28 15:35
     * description: 人工抬杆/车辆订单欠费总额
     *
     * @param
     * @return java.lang.String
     */
    public String getOpenGateOrderArrears(Integer park_id, String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" SUM (CAST ( unpaid_amount AS decimal(10, 2) )) sum");
        sql.append(" from ");
        sql.append(" parking_order");
        sql.append(" where 1=1");
        sql.append(" and is_del = '0'");
        sql.append(" AND artificial_open IS NOT NULL");
        if (notEmpty(startTime)) {
            sql.append(" and drivein_time <= '" + startTime + "'");
        }
        if (notEmpty(endTime)) {
            sql.append(" and drivein_time >= '" + endTime + "'");
        }
        if (notEmpty(park_id)) {
            sql.append(" and park_id = '" + park_id + "'");
        }
        return sql.toString();
    }

    /**
     * 出场收费列表
     * Author wzn
     * Date 2022/1/28 10:09
     */
    public String appearanceFee(RoadParkListVo bean) {
        String sql = "SELECT\n" +
                " po.id ," +
                "\tpo.order_no orderNo,\n" +
                "\toc.car_no carNo,\n" +
                "\tpo.resitime resitime,\n" +
                "\tpo.sum_amount sum_amount\n" +
                "FROM\n" +
                "\tparking_order po\n" +
                "LEFT JOIN operate_carno oc ON oc.id = po.carno_id\n" +
                "where po.status = 2 and po.driveout_gate is not null";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getCarNo())) {
                sql += " and oc.car_no like '%" + bean.getCarNo() + "%'";
            }
            if (notEmpty(bean.getPlaceId())) {
                sql += " and po.park_id = " + bean.getPlaceId();
            }
            if (notEmpty(bean.getOrderNo())) {
                sql += " and po.order_no like '%" + bean.getOrderNo() + "%'";
            }
        }
        sql += " ORDER BY po.update_time ";
        return getQueryHandler(sql).getSql();
    }


    /**
     * 出场收费详情
     * Author wzn
     * Date 2022/1/28 10:09
     */
    public String appearanceFeeInfo(RoadParkListVo bean) {
        String sql = "SELECT\n" +
                "\tpo.order_no orderNo,\n" +
                "\tpo.id ,\n" +
                "\toc.car_no carNo,\n" +
                "\toc.car_type carType,\n" +
                "\tpo.resitime ,\n" +
                "\tpo.sum_amount ,\n" +
                "\tpo.discount_amount,\n" +
                "po.paid_amount,\n" +
                "po.unpaid_amount,\n" +
                "po.drivein_time driveinTime,\n" +
                "po.driveout_time driveoutTime\n" +
                "FROM\n" +
                "\tparking_order po\n" +
                "LEFT JOIN operate_carno oc ON oc.id = po.carno_id\n" +
                "where po.order_no = '" + bean.getOrderNo() + "'";
        return getQueryHandler(sql).getSql();
    }

    /**
     * 描 述： TODO(内部车明细/企业（税免）车明细)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @param whitelistTypeName 免费类型名称（内部车 、 企业免税车）
     * @param whitelistType     免费类型
     * @return {@link String}
     */
    public String toInnerFreeListQyNb(CommonVo data, String whitelistTypeName, String whitelistType) {
        String selectPRName = " t3.park_name as prName , ";
        String t1TableName = " from parking_order  t1 ";
        String leftPRTable = " LEFT JOIN park t3  on t1.park_id = t3.id ";
        String wherePRId = " and t1.park_id=";

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select '" + whitelistTypeName + "'  as prType , ");
        sqlbd.append(selectPRName);
        sqlbd.append("  t2.car_no as carNo , ");
        sqlbd.append(" t1.drivein_time as driveinTime,t1.driveout_time as driveoutTime ,  ");
        sqlbd.append(" t4.nick_name as carOwner ,t4.phone as phone  ");
        sqlbd.append(" ,(STUFF((select ',' + t6.company_name FROM carno_company t5,operate_company t6 where t5.company_id = t6.id and t5.carno_id = t1.carno_id ),1,1,'')) as enterpriseName ");

        sqlbd.append(t1TableName);

        sqlbd.append(" LEFT JOIN operate_carno t2 on t1.carno_id = t2.id ");

        sqlbd.append(leftPRTable);

        sqlbd.append(" LEFT JOIN member_manage t4 on t1.member_id  = t4.id ");
        sqlbd.append(" where t1.is_del = 0 ");
        sqlbd.append(" and t2.whitelist_type= ").append(whitelistType);


        if (StringUtils.isNotEmpty(data.getStartTime())) {
            sqlbd.append("  and CONVERT(varchar, t1.drivein_time, 23) >=  '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(data.getEndTime())) {
            sqlbd.append("  and CONVERT(varchar, t1.drivein_time, 23) <=  '").append(data.getEndTime().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(data.getStr())) {
            sqlbd.append("  and CONVERT(varchar, t1.drivein_time, 23) <= '").append(data.getStr()).append("' ");
        }

        if (StringUtils.isNotNull(data.getVariance())) {
            sqlbd.append(wherePRId).append(data.getVariance());
        }
        if (StringUtils.isNotNull(data.getStreet_id())) {
            sqlbd.append(" and t1.street_id=").append(data.getStreet_id());
        }
        if (StringUtils.isNotNull(data.getArea_id())) {
            sqlbd.append(" and t1.area_id=").append(data.getArea_id());
        }
        return sqlbd.toString();
    }


    /**
     * 描 述： TODO(内部车明细/企业（税免）车明细统计)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @param whitelistType 免费类型
     * @return {@link String}
     */
    public String toInnerFreeListQyStatistics(CommonVo data, String whitelistType) {

        String t1TableName = " from parking_order  t1 ";
        String leftPRTable = " LEFT JOIN park t3  on t1.park_id = t3.id ";
        String wherePRId = " and t1.park_id=";

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select  COUNT( distinct carno_id ) carCount , sum(convert(decimal(15 ,5) ,  sum_amount )) sumAmount  ");

        sqlbd.append(t1TableName);

        sqlbd.append(" LEFT JOIN operate_carno t2 on t1.carno_id = t2.id ");

        sqlbd.append(leftPRTable);

        sqlbd.append(" LEFT JOIN member_manage t4 on t1.member_id  = t4.id ");
        sqlbd.append(" where t1.is_del = 0 ");
        sqlbd.append(" and t2.whitelist_type= ").append(whitelistType);


        if (StringUtils.isNotEmpty(data.getStartTime())) {
            sqlbd.append("  and CONVERT(varchar, t1.drivein_time, 23) >=  '").append(data.getStartTime()).append("' ");
        }
        if (StringUtils.isNotEmpty(data.getEndTime())) {
            sqlbd.append("  and CONVERT(varchar, t1.drivein_time, 23) <=  '").append(data.getEndTime().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(data.getStr())) {
            sqlbd.append("  and CONVERT(varchar, t1.drivein_time, 23) <= '").append(data.getStr()).append("' ");
        }

        if (StringUtils.isNotNull(data.getVariance())) {
            sqlbd.append(wherePRId).append(data.getVariance());
        }
        if (StringUtils.isNotNull(data.getStreet_id())) {
            sqlbd.append(" and t1.street_id=").append(data.getStreet_id());
        }
        if (StringUtils.isNotNull(data.getArea_id())) {
            sqlbd.append(" and t1.area_id=").append(data.getArea_id());
        }
        return sqlbd.toString();
    }

    /**
     * 描 述： TODO(查询停车场在停车数量)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link String}
     */
    public String getParkOrderCount() {

        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select park_id parkId, count(1) counts  from parking_order ");
        sqlbd.append(" where is_del = 0 ");
        sqlbd.append(" and status = 1 ");
        sqlbd.append(" GROUP BY park_id ");

        return sqlbd.toString();
    }

    //会员非会员count个数
    public String getMemberIdCount(CommonVo data) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append("select COUNT (DISTINCT o.carno_id) as sumAmount,0 payTypeName   from parking_order o LEFT JOIN operate_carno car on o.carno_id = car.id where car.member_id is not null  ");
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and CONVERT(varchar, o.create_time, 23) = '").append(data.getStr()).append("' ");
        if (data.getVariance() != null)
            sqlbd.append(" and area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance3());
        sqlbd.append(" UNION all ");
        sqlbd.append(" select COUNT (DISTINCT o.carno_id) as sumAmount,1 payTypeName from parking_order o LEFT JOIN operate_carno car on o.carno_id = car.id where car.member_id is  null  ");
        sqlbd.append(" and o.is_del = ").append(GlobalData.ISDEL_NO);
        if (StringUtils.isNotBlank(data.getStr()))
            sqlbd.append(" and CONVERT(varchar, o.create_time, 23) = '").append(data.getStr()).append("' ");
        if (data.getVariance() != null)
            sqlbd.append(" and area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and park_id = ").append(data.getVariance3());
        return sqlbd.toString();
    }

    /**
     * 各区域收费总额排名TOP5
     * 0 路段 1停车场
     * 作 者： thr
     */
    public String getTotalChargesTop5Data(String type, String timeType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT TOP\n" +
                "\t5 a.id,\n" +
                "\tMAX ( a.park_name ) AS name,\n" +
                "\tisnull( SUM ( CAST ( p.paid_amount AS NUMERIC ( 12 ))), 0 ) AS amount \n" +
                "FROM\n" +
                "\tpark a\n" +
                "\tLEFT JOIN parking_order p ON p.park_id = a.id \n" +
                "\tAND p.is_del= '0' \n" +
                "\tAND p.status IN ( '3', '4' ) \n");
        if (notEmpty(timeType)) {
            sql.append("\tAND DateDiff(\n" +
                    "\t\tyy,\n" +
                    "\t\tp.create_time,\n" +
                    "\tgetdate()) = 0 \n");
        }

        sql.append("WHERE\n" +
                "\ta.is_del= '0' \n" +
                "GROUP BY\n" +
                "\ta.id \n" +
                "ORDER BY\n" +
                "\tamount DESC");
        return sql.toString();
    }

    /**
     * 今日收费总额趋势分析
     * type 0 路段 1停车场
     * dayType 0 今日 -1 昨日
     * 作 者： thr
     */
    public String getTotalChargesData(String type, int dayType) {
        String name = "parking_order";

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "c.click_date as name,\n" +
                "isnull(b.amount, 0) AS amount\n" +
                "FROM\n" +
                "(\n" +
                "SELECT '1' AS click_date\n" +
                "UNION ALL\n" +
                "SELECT '2'  AS click_date\n" +
                "UNION ALL\n" +
                "SELECT '3'  AS click_date\n" +
                "UNION ALL\n" +
                "SELECT '4' AS click_date\n" +
                "UNION ALL\n" +
                "SELECT '5'  AS click_date\n" +
                "UNION ALL\n" +
                "SELECT '6'  AS click_date\n" +
                ") c\n" +
                "LEFT JOIN (\n" +
                "SELECT count(a.amount) as amount,a.name from \n" +
                "(\n" +
                "--0-4小时\n" +
                "select pr.id as amount,create_time,'1' as name from " + name + " pr where is_del='0' and datepart(hh,create_time) between 0 and 4\n" +
                "UNION ALL\n" +
                "--4-8小时\n" +
                "select pr.id as amount,create_time,'2' as name from " + name + " pr where is_del='0' and datepart(hh,create_time) between 4 and 8 \n" +
                "UNION ALL\n" +
                "--8-12小时\n" +
                "select pr.id as amount,create_time,'3' as name from " + name + " pr where is_del='0' and datepart(hh,create_time) between 8 and 12 \n" +
                "UNION ALL\n" +
                "--12-16小时\n" +
                "select pr.id as amount,create_time,'4' as name from " + name + " pr where is_del='0' and datepart(hh,create_time) between 12 and 16\n" +
                "UNION ALL\n" +
                "--16-20小时\n" +
                "select pr.id as amount,create_time,'5' as name from " + name + " pr where is_del='0' and datepart(hh,create_time) between 16 and 20 \n" +
                "UNION ALL\n" +
                "--20-24小时\n" +
                "select pr.id as amount,create_time,'6' as name from " + name + " pr where is_del='0' and datepart(hh,create_time) between 20 and 24\n" +
                ") a where CONVERT(varchar(100), dateadd(day," + dayType + ",getdate()), 23)=convert(varchar(10),create_time,120) GROUP BY a.name\n" +
                ") b ON c.click_date = b.name\n" +
                "order by c.click_date\n");
        return sql.toString();
    }

    /**
     * 各区域在停车辆总数排名TOP5
     * 数据分析页面使用
     *
     * @param type PARKING_TYPE_ROAD=路段  反之停车场
     * @return
     */
    public String getCarTop5Data(String type, String timeType) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT TOP\n" +
                "\t5 a.id,\n" +
                "\tMAX ( a.park_name ) AS name,\n" +
                "\tCOUNT ( p.id ) AS amount \n" +
                "FROM\n" +
                "\tpark a\n" +
                "\tLEFT JOIN parking_order p ON p.park_id= a.id \n" +
                "\tAND p.is_del= '0' \n");
        if (notEmpty(timeType)) {
            //今年
            sqlBuffer.append(" AND DateDiff( yy, p.create_time, getdate()) = 0 ");
        }
        sqlBuffer.append(" WHERE\n" +
                "\ta.is_del= '0' \n" +
                "GROUP BY\n" +
                "\ta.id \n" +
                "ORDER BY\n" +
                "\tamount DESC");
        return sqlBuffer.toString();
    }

    /**
     * 昨日停车占比
     * 数据分析页面使用
     *
     * @param type PARKING_TYPE_ROAD=路段  反之停车场
     * @return
     */
    public String getParkTimeData(String type) {
        StringBuffer sqlBuffer = new StringBuffer();
        String typeName = " parking_order ";

        sqlBuffer.append(" SELECT isnull(b.amount, 0) AS amount,c.click_date as hourType FROM( SELECT '1' AS click_date UNION ALL SELECT '2' AS click_date UNION ALL SELECT '3' AS " +
                " click_date UNION ALL SELECT '4' AS click_date UNION ALL SELECT '5' AS click_date) c LEFT JOIN ( SELECT count(a.amount) as amount,a.hourType from ( select pr.id " +
                " as amount,create_time,'1' as hourType from " + typeName + " pr where is_del='0' and resitime between 0 and 120 UNION ALL select pr.id as " +
                " amount,create_time,'2' as hourType from " + typeName + " pr where is_del='0' and resitime between 120 and 240 UNION ALL select pr.id as " +
                " amount,create_time,'3' as hourType from " + typeName + " pr where is_del='0' and resitime between 480 and 720 UNION ALL  select pr.id as " +
                " amount,create_time,'4' as hourType from " + typeName + " pr where is_del='0' and resitime between 720 and 960 UNION ALL  select pr.id as " +
                " amount,create_time,'5' as hourType from " + typeName + " pr where is_del='0' and resitime > 960 ) a where CONVERT(varchar(100), " +
                " dateadd(day,-1,getdate()), 23) = convert(varchar(10),create_time,120) GROUP BY a.hourType ) b ON c.click_date = b.hourType order by c.click_date ");
        return sqlBuffer.toString();
    }

    //统计昨日停车占比总次数
    public String getParkTimeCountData(String type) {
        StringBuffer sqlBuffer = new StringBuffer();
        String typeName = " parking_order ";

        sqlBuffer.append("select COUNT(1) from " + typeName + " pr where is_del='0' and" +
                "  CONVERT(varchar(100), dateadd(day,-1,getdate()), 23)=convert(varchar(10),create_time,120) ");
        return sqlBuffer.toString();
    }

    /**
     * 近五日车位占用量及趋势
     * 数据分析页面使用
     *
     * @param type
     * @return
     */
    public String getParticipatingData(String type) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT isnull(b.amount, 0) AS amount,c.click_date as days  FROM( SELECT CONVERT(varchar(100), dateadd(day,0,getdate()), 23) AS click_date " +
                " UNION ALL SELECT CONVERT(varchar(100), dateadd(day,-1,getdate()), 23) AS click_date " +
                " UNION ALL SELECT CONVERT(varchar(100), dateadd(day,-2,getdate()), 23) AS click_date " +
                " UNION ALL  SELECT CONVERT(varchar(100), dateadd(day,-3,getdate()), 23) AS click_date " +
                " UNION ALL SELECT CONVERT(varchar(100), dateadd(day,-4,getdate()), 23) AS click_date) c  " +
                " LEFT JOIN ( select COUNT(pr.id) as amount,CONVERT(varchar(100), create_time, 23) as days from ");

        sqlBuffer.append(" parking_order pr ");

        sqlBuffer.append("where is_del='0' GROUP BY CONVERT(varchar(100), create_time, 23) ) b ON c.click_date = b.days order by c.click_date ");
        return sqlBuffer.toString();
    }

    /**
     * 今年总收费额
     *
     * @param year 1年 2日
     * @param type 0.路段 1.停车场
     * @return
     */
    public String chargesYear(String type, String year) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT isnull( SUM( CAST (p.paid_amount AS NUMERIC(12))), 0 ) AS amount FROM ");

        sqlBuffer.append(" parking_order p ");

        if ("1".equals(year)) {
            sqlBuffer.append(" WHERE DateDiff(yy, p.create_time, getdate()) = 0 ");
        } else {
            sqlBuffer.append(" where CONVERT(varchar(10),GETDATE(),120)=convert(varchar(10),p.create_time,120) ");
        }
        return sqlBuffer.toString();
    }

    //占用车位
    public String occupationData(String type) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT count(o.id) as amount from parking_order o where o.status = '1' and o.is_del='0'  ");
        return sqlBuffer.toString();
    }

    //停车实际收费
    public String actualParkingChargeData(String type) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("select isnull( SUM( CAST (paid_amount AS NUMERIC(12))), 0 ) as amount from parking_order where is_del='0' and status IN ( '3', '4' )");
        return sqlBuffer.toString();
    }

    //停车应该收费
    public String parkingShouldChargedData(String type) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("select isnull( SUM( CAST (sum_amount AS NUMERIC(12))), 0 ) as amount from parking_order where is_del='0' and status IN ( '3', '4' )");
        return sqlBuffer.toString();
    }

    /**
     * 支付方式占比
     *
     * @return
     */
    public String paymentTypeStatistics() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" CASE");
        sql.append(" 		pao.payment_type ");
        sql.append(" 		WHEN '1' THEN");
        sql.append(" 		'包月' ");
        sql.append(" 		WHEN '2' THEN");
        sql.append(" 		'微信' ");
        sql.append(" 		WHEN '3' THEN");
        sql.append(" 		'支付宝' ");
        sql.append(" 		WHEN '4' THEN");
        sql.append(" 		'钱包' ");
        sql.append(" 		WHEN '5' THEN");
        sql.append(" 		'现金' ELSE '其他' ");
        sql.append(" 	END payTypeName,");
        sql.append(" 	SUM ( CONVERT ( DECIMAL ( 12, 2 ), pao.amount ) ) sumAmount ");
        sql.append(" FROM");
        sql.append(" 	parking_order po");
        sql.append(" 	INNER JOIN payment_order pao ON po.payment_id = pao.id ");
        sql.append(" WHERE");
        sql.append(" 	po.status IN ( '3', '4' ) ");
        sql.append(" 	AND po.is_del = '0'  and pao.is_del='0' and pao.status='2'");
        sql.append(" GROUP BY");
        sql.append(" 	pao.payment_type");
        return sql.toString();
    }

    /**
     * 停车场收费排行
     *
     * @return
     */
    public String parkChargeRank() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" 	isnull( SUM ( CAST ( po.sum_amount AS NUMERIC ( 12, 2 ) ) ), 0.00 ) AS amount,");
        sql.append(" 	po.park_id ,");
        sql.append(" 	p.park_name ");
        sql.append(" FROM");
        sql.append(" 	parking_order po");
        sql.append(" 	LEFT JOIN park p ON po.park_id = p.id ");
        sql.append(" WHERE");
        sql.append(" 	1 = 1 ");
        sql.append(" 	AND po.is_del = '0' ");
        sql.append(" 	AND po.status IN ( '3', '4' ) ");
        sql.append(" GROUP BY");
        sql.append(" 	po.park_id,");
        sql.append(" 	p.park_name ");
        sql.append(" ORDER BY");
        sql.append(" 	amount DESC");
        return sql.toString();
    }

    /**
     * 停车场在停车辆排行
     *
     * @return
     */
    public String parkParkingCount() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT");
        sql.append(" 	( po.id ) as count,");
        sql.append(" 	p.park_name ");
        sql.append(" FROM");
        sql.append(" 	parking_order po");
        sql.append(" 	LEFT JOIN park p ON po.park_id = p.id ");
        sql.append(" WHERE");
        sql.append(" 	po.status = '1' ");
        sql.append(" 	AND po.is_del = '0' ");
        sql.append(" GROUP BY");
        sql.append(" 	po.park_id,");
        sql.append(" 	p.park_name ");
        sql.append(" ORDER BY");
        sql.append(" COUNT DESC");
        return sql.toString();
    }

    /**
     * 总优惠金额
     */
    public String sumDiscountMoney(ParkingOrderVo vo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT\n" +
                "\tisnull( SUM ( CAST ( t.discount_amount AS DECIMAL )), 0 ) discount_amount,\n" +
                "\tisnull( SUM ( CAST ( t.discount_money AS DECIMAL )), 0 ) discount_money \n" +
                "FROM\n" +
                "\troad_park_list t " +
                "WHERE 1=1\n");
        if (notEmpty(vo)) {
            if (notEmpty(vo.getStartDate())) {
                sql.append(" and CONVERT(varchar, t.create_time, 23) = '").append(vo.getStartDate()).append("'");
            }
        }
        return sql.toString();
    }

    /**
     * 今日营收 折线图
     * 数据分析页面 数据总览使用
     * 今日0时至当前时间各个小时整点营收金额统计
     */
    public String getMoneyDataByHours(ParkingOrderVo vo) {
        //获取当前时间小时
        int hour = new DateTime().getHourOfDay();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT isnull(b.amount, 0) AS amount,c.click_date as hours  " +
                " FROM ( ");
        for (int i = 0; i <= hour; i++) {
            sqlBuffer.append(" SELECT '").append(String.format("%02d", i)).append("' AS click_date");
            if (i <= hour) {
                sqlBuffer.append("  UNION ALL ");
            }
        }

        sqlBuffer.append(" ) c  ");

        sqlBuffer.append(" LEFT JOIN ( " +
                " select " +
                " isnull( SUM ( CAST ( pr.paid_amount AS DECIMAL )), 0 ) AS amount," +
                " DATEPART( HH, pr.create_time ) as hours " +
                " from ");
        sqlBuffer.append(" road_park_list pr ");
        sqlBuffer.append(" where 1=1 ");
        sqlBuffer.append(" and CONVERT ( VARCHAR, pr.create_time, 23 ) = '").append(new DateTime().toString("yyyy-MM-dd")).append("'");
        if (notEmpty(vo)) {
            // 类型 0路测 1停车场
            if (notEmpty(vo.getType_name())) {
                sqlBuffer.append(" and pr.type = ").append(vo.getType_name());
            }
            if (notEmpty(vo.getPark_id())) {
                sqlBuffer.append(" and pr.pId = ").append(vo.getPark_id());
            }
        }
        sqlBuffer.append(" GROUP BY DATEPART( HH, pr.create_time) " +
                " ) b ");

        sqlBuffer.append(" ON c.click_date = b.hours");
        sqlBuffer.append(" ORDER BY CONVERT(INT, LEFT(c.click_date, LEN(c.click_date)-1)) ASC, RIGHT(c.click_date, 1)");
        return sqlBuffer.toString();
    }

    /**
     * 欠费分析 折线图
     * 数据分析页面 路侧/停车场详情页面使用
     * 今日0时至当前时间各个小时整点欠费金额统计
     */
    public String getArrearsMoneyDataByHours(ParkingOrderVo vo) {
        //获取当前时间小时
        int hour = new DateTime().getHourOfDay();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT isnull( CAST ( b.amount AS VARCHAR ( 20 )), 0 ) AS amount,c.click_date as name  " +
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
                " isnull( SUM ( case when o.roster_type != 3 then CONVERT ( DECIMAL ( 15, 2 ), pr.unpaid_amount ) else 0 end ), 0 ) AS amount," +
                " DATEPART( HH, pr.create_time ) as hours " +
                " from ");
        sqlBuffer.append(" road_park_list pr ");
        sqlBuffer.append(" LEFT JOIN operate_carno o ON pr.carId = o.id ");
        sqlBuffer.append(" where 1=1 ");
        sqlBuffer.append(" and CONVERT ( VARCHAR, pr.create_time, 23 ) = '").append(new DateTime().toString("yyyy-MM-dd")).append("'");
        if (notEmpty(vo)) {
            // 类型 0路测 1停车场
            if (notEmpty(vo.getType_name())) {
                sqlBuffer.append(" and pr.type = ").append(vo.getType_name());
            }
            if (notEmpty(vo.getPark_id())) {
                sqlBuffer.append(" and pr.pId = ").append(vo.getPark_id());
            }
        }
        sqlBuffer.append(" GROUP BY DATEPART( HH, pr.create_time) " +
                " ) b ");

        sqlBuffer.append(" ON c.click_date = b.hours");
        sqlBuffer.append(" ORDER BY CONVERT(INT, LEFT(c.click_date, LEN(c.click_date)-1)) ASC, RIGHT(c.click_date, 1)");
        return sqlBuffer.toString();
    }

    /**
     * 在停车辆
     */
    public String getZtCount(ParkingOrderVo vo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT\n" +
                "\tCOUNT ( CASE t.status WHEN '1' THEN 1 END ) AS ztCount ,\n" +
                "\tcount(t.id) counts\n" +
                "FROM\n" +
                "\troad_park_list t " +
                "WHERE 1=1\n");
        if (notEmpty(vo)) {
            // 类型 0路测 1停车场
            if (notEmpty(vo.getType_name())) {
                sql.append(" and t.type = ").append(vo.getType_name());
            }
            if (notEmpty(vo.getStartDate())) {
                sql.append(" and CONVERT ( VARCHAR, t.create_time, 23 ) = '").append(vo.getStartDate()).append("'");
            }
        }
        return sql.toString();
    }

    /**
     * 在停车辆
     * 各个路侧/停车场
     */
    public String getZtCountByIds(ParkingOrderVo vo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT\n" +
                "\tCOUNT ( CASE t.status WHEN '1' THEN 1 END ) AS ztCount ,\n" +
                "\tcount(t.id) counts,\n" +
                "\tt.pId placeId\n" +
                "FROM\n" +
                "\troad_park_list t " +
                "WHERE 1=1\n");
        if (notEmpty(vo)) {
            // 类型 0路测 1停车场
            if (notEmpty(vo.getType_name())) {
                sql.append(" and t.type = ").append(vo.getType_name());
            }
            if (notEmpty(vo.getStartDate())) {
                sql.append(" and CONVERT ( VARCHAR, t.create_time, 23 ) = '").append(vo.getStartDate()).append("'");
            }
        }
        sql.append(" GROUP BY\n" +
                "\tt.pId");
        return sql.toString();
    }

    /**
     * 在停数量排名TOP3
     */
    public String getZtCountTop5(ParkingOrderVo vo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TOP\n" +
                "\t3 COUNT ( t.id ) ztCount,\n" +
                "\tt.type,\n" +
                "\tt.placeId,\n" +
                "\tMAX ( t.placeName ) placeName \n" +
                "FROM\n" +
                "\troad_park_list t \n" +
                "WHERE\n" +
                "\tt.status= '1' \n" +
                "GROUP BY\n" +
                "\tt.type,\n" +
                "\tt.placeId \n" +
                "ORDER BY\n" +
                "\tztCount DESC\n");
        return sql.toString();
    }

    /**
     * 停车时长占比
     */
    public String getTodayParkTimeData(String type, Integer id) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT isnull(b.amount, 0) AS amount,c.click_date as name FROM( SELECT '1' AS click_date UNION ALL SELECT '2' AS click_date UNION ALL SELECT '3' AS " +
                " click_date UNION ALL SELECT '4' AS click_date UNION ALL SELECT '5' AS click_date) c LEFT JOIN ( SELECT count(a.amount) as amount,a.hourType from ( select pr.id " +
                " as amount,create_time,'1' as hourType from road_park_list pr where resitime between 0 and 120 ");
        if (notEmpty(type)) {
            sqlBuffer.append(" and pr.type = ").append(type);
        }
        if (notEmpty(id)) {
            sqlBuffer.append(" and pr.pId = ").append(id);
        }
        sqlBuffer.append(" UNION ALL select pr.id as " +
                " amount,create_time,'2' as hourType from road_park_list pr where resitime between 120 and 240 ");
        if (notEmpty(type)) {
            sqlBuffer.append(" and pr.type = ").append(type);
        }
        if (notEmpty(id)) {
            sqlBuffer.append(" and pr.pId = ").append(id);
        }
        sqlBuffer.append(" UNION ALL select pr.id as " +
                " amount,create_time,'3' as hourType from road_park_list pr where resitime between 480 and 720 ");
        if (notEmpty(type)) {
            sqlBuffer.append(" and pr.type = ").append(type);
        }
        if (notEmpty(id)) {
            sqlBuffer.append(" and pr.pId = ").append(id);
        }
        sqlBuffer.append(" UNION ALL  select pr.id as " +
                " amount,create_time,'4' as hourType from road_park_list pr where resitime between 720 and 960 ");
        if (notEmpty(type)) {
            sqlBuffer.append(" and pr.type = ").append(type);
        }
        if (notEmpty(id)) {
            sqlBuffer.append(" and pr.pId = ").append(id);
        }
        sqlBuffer.append(" UNION ALL  select pr.id as " +
                " amount,create_time,'5' as hourType from road_park_list pr where resitime > 960 ");
        if (notEmpty(type)) {
            sqlBuffer.append(" and pr.type = ").append(type);
        }
        if (notEmpty(id)) {
            sqlBuffer.append(" and pr.pId = ").append(id);
        }
        sqlBuffer.append(" ) a where 1=1" +
//                " and CONVERT(varchar(100), getdate(), 23) = convert(varchar(10),create_time,120) " +
                " GROUP BY a.hourType ) b ON c.click_date = b.hourType order by c.click_date ");
        return sqlBuffer.toString();
    }

    /**
     * 统计今日停车占比总次数
     */
    public String getTodayParkTimeCount(String type, Integer id) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("select COUNT(1) from road_park_list pr where 1=1");
//        sqlBuffer.append(" and CONVERT(varchar(100), getdate(), 23)=convert(varchar(10),create_time,120) ");
        if (notEmpty(type)) {
            sqlBuffer.append(" and pr.type = ").append(type);
        }
        if (notEmpty(id)) {
            sqlBuffer.append(" and pr.pId = ").append(id);
        }
        return sqlBuffer.toString();
    }

    /**
     * 折线图： 出入趋势双折线：7天内出入次数
     */
    public String getInOutCount(String type, String inOutType) {
        String timeType = "";
        if (notEmpty(inOutType)) {
            if (inOutType.equals("in")) {
                timeType = "driveinTime";
            } else {
                timeType = "driveoutTime";
            }
        }
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT ");
        sqlBuffer.append(" isnull( b.amount, 0 ) AS amount, ");
        sqlBuffer.append(" c.click_date AS name \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY, 0, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 1, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 2, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 3, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 4, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 5, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 6, getdate()), 23 ) AS click_date \n" +
                "\t) c\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT");
        sqlBuffer.append(" COUNT( CASE WHEN pr." + timeType + " IS NOT NULL THEN 1 END ) AS amount, ");
        sqlBuffer.append(" CONVERT ( VARCHAR ( 100 ), pr." + timeType + ", 23 ) AS days ");
        sqlBuffer.append(" FROM\n" +
                "\t\troad_park_list pr \n" +
                "\tWHERE\n" +
                "\t\t1 = 1 \n");
        if (notEmpty(type)) {
            sqlBuffer.append(" and pr.type = ").append(type);
        }
        sqlBuffer.append(" GROUP BY CONVERT ( VARCHAR ( 100 ), pr." + timeType + ", 23 ) ");
        sqlBuffer.append("\t) b ON c.click_date = b.days \n" +
                "ORDER BY\n" +
                "\tc.click_date");
        return sqlBuffer.toString();
    }

    /**
     * 自助停车率（排除人工订单和人工抬杆的所有状态订单）
     */
    public String getCountBySource(RoadParkListVo vo) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT COUNT\n" +
                "\t( id ) AS amount,\n" +
                "source name\n" +
                "FROM\n" +
                "\troad_park_list \n" +
                "WHERE\n" +
                "\tsource is not null\n");
        if (notEmpty(vo)) {
            if (notEmpty(vo.getType())) {
                sqlBuffer.append(" and type = ").append(vo.getType());
            }
            if (notEmpty(vo.getPId())) {
                sqlBuffer.append(" and pId = ").append(vo.getPId());
            }
        }

        sqlBuffer.append(" GROUP BY\n" +
                "\tsource");
        return sqlBuffer.toString();
    }

    /**
     * 订单收费率（已缴费和已完成的订单数量/总数量）
     */
    public String getCountByStatus(RoadParkListVo vo) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT COUNT\n" +
                "\t( id ) AS amount ,\n" +
                "status name\n" +
                "FROM\n" +
                "\troad_park_list \n" +
                "WHERE\n" +
                "\t1=1\n");
        if (notEmpty(vo)) {
            if (notEmpty(vo.getType())) {
                sqlBuffer.append(" and type = ").append(vo.getType());
            }
            if (notEmpty(vo.getPId())) {
                sqlBuffer.append(" and pId = ").append(vo.getPId());
            }
        }
        sqlBuffer.append(" GROUP BY\n" +
                "\tstatus");
        return sqlBuffer.toString();
    }

    /**
     * 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）
     * 缴费时间-出场时间<15分钟的数量
     */
    public String getCountByLt15Min(RoadParkListVo vo) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT COUNT\n" +
                "\t( pr.id ) AS amount \n" +
                "FROM\n" +
                "\troad_park_list pr \n" +
                "WHERE\n" +
                "\tDATEDIFF( MINUTE, pr.driveoutTime, pr.pay_time ) < 15");
        if (notEmpty(vo)) {
            if (notEmpty(vo.getType())) {
                sqlBuffer.append(" and pr.type = ").append(vo.getType());
            }
            if (notEmpty(vo.getPId())) {
                sqlBuffer.append(" and pr.pId = ").append(vo.getPId());
            }
        }
        return sqlBuffer.toString();
    }

    /**
     * 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）3
     * 总已缴费、总已完成状态的订单数量
     */
    public String getYjfYwcCount(RoadParkListVo vo) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT COUNT\n" +
                "\t( pr.id ) AS amount \n" +
                "FROM\n" +
                "\troad_park_list pr \n" +
                "WHERE\n" +
                "\t(pr.status = '3' \n" +
                "\tOR pr.status = '4')");
        if (notEmpty(vo)) {
            if (notEmpty(vo.getType())) {
                sqlBuffer.append(" and pr.type = ").append(vo.getType());
            }
            if (notEmpty(vo.getPId())) {
                sqlBuffer.append(" and pr.pId = ").append(vo.getPId());
            }
        }
        return sqlBuffer.toString();
    }

    /**
     * 超24H占位率（已缴费+已完成的超24H停车时长/总订单数量）
     */
    public String getCountByGt24h(RoadParkListVo vo) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT COUNT\n" +
                "\t( pr.id ) AS amount \n" +
                "FROM\n" +
                "\troad_park_list pr \n" +
                "WHERE\n" +
                "\t( pr.status = '3' OR pr.status = '4' ) \n" +
                "\tAND pr.resitime > 1440");
        if (notEmpty(vo)) {
            if (notEmpty(vo.getType())) {
                sqlBuffer.append(" and pr.type = ").append(vo.getType());
            }
            if (notEmpty(vo.getPId())) {
                sqlBuffer.append(" and pr.pId = ").append(vo.getPId());
            }
        }
        return sqlBuffer.toString();
    }

    /**
     * 发票发放率
     */
    public String getFpCount(RoadParkListVo vo) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT COUNT\n" +
                "\t( pr.id ) AS amount \n" +
                "FROM\n" +
                "\troad_park_list pr \n" +
                "WHERE\n" +
                "\tpr.invoiceId IS NOT NULL");
        if (notEmpty(vo)) {
            if (notEmpty(vo.getType())) {
                sqlBuffer.append(" and pr.type = ").append(vo.getType());
            }
            if (notEmpty(vo.getPId())) {
                sqlBuffer.append(" and pr.pId = ").append(vo.getPId());
            }
        }
        return sqlBuffer.toString();
    }

    /**
     * 欠费车辆排名TOP10
     */
    public String getSumAmountTop10ByCarNo(String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TOP\n" +
                "\t10 ISNULL( SUM ( CAST ( po.sum_amount AS NUMERIC ( 12, 2 ) ) ), 0 ) sum_amount,\n" +
                "\tpo.carNo car_no\n" +
                "FROM\n" +
                "\troad_park_list po \n" +
                "WHERE\n" +
                "\tpo.carNo IS NOT NULL \n" +
                "\tAND po.status = '2' \n");
        if (notEmpty(type)) {
            sql.append(" AND po.type = ").append(type);
        }
        sql.append("GROUP BY\n" +
                "\tpo.carNo \n" +
                "ORDER BY\n" +
                "\tsum_amount desc");
        return sql.toString();
    }

    /**
     * 路段和停车场订单信息
     * 查询视图
     * 今日进出记录
     */
    public String selectRoadParkList(RoadParkListVo bean) {
        String sql = "select top 20 * from road_park_list where 1=1 ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getPId())) {
                sql += " and pId = " + bean.getPId();
            }
            if (notEmpty(bean.getType())) {
                sql += " and type = " + bean.getType();
            }
            if (notEmpty(bean.getDriveinTimeStr())) {
                sql += " and ( CONVERT(varchar, driveinTime, 23) = '" + bean.getDriveinTimeStr() + "' or CONVERT(varchar, driveoutTime, 23) = '" + bean.getDriveinTimeStr() + "')";
            }
        }
        sql += " ORDER BY driveinTime desc";
        return getQueryHandler(sql).getSql();
    }

    /**
     * sum_amount 应收金额
     * paid_amount 已付金额/营收金额
     * unpaid_amount 待付金额/欠费金额
     */
    public String getSumAmounts(RoadParkListVo vo) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" SELECT\n" +
                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), p.sum_amount )), 0 ) sum_amount ,\n" +
                "\tisnull( SUM ( CONVERT ( DECIMAL ( 15, 2 ), p.paid_amount ) ), 0 ) paid_amount ,\n" +
                "\tisnull( SUM ( case when o.roster_type != 3 then CONVERT ( DECIMAL ( 15, 2 ), p.unpaid_amount ) else 0 end ), 0 ) unpaid_amount \n" +
                "FROM\n" +
                "\troad_park_list p \n" +
                "\tLEFT JOIN operate_carno o ON p.carId = o.id \n");
        if (notEmpty(vo.getDriveinTimeStr())) {
            stringBuffer.append(" and CONVERT(varchar, p.driveinTime, 23) = '").append(vo.getDriveinTimeStr()).append("' ");
        }
        if (notEmpty(vo.getDriveoutTimeStr())) {
            stringBuffer.append(" and CONVERT(varchar, p.driveoutTime, 23) = '").append(vo.getDriveoutTimeStr()).append("' ");
        }
        //创建时间（计算待付金额/欠费金额）
        if (notEmpty(vo.getStartTime())) {
            stringBuffer.append(" and CONVERT(varchar, p.create_time, 23) = '").append(vo.getStartTime()).append("' ");
        }
        //支付时间（计算已付金额/营收金额）
        if (notEmpty(vo.getPayTime())) {
            stringBuffer.append(" and CONVERT(varchar, p.pay_time, 23) = '").append(vo.getPayTime()).append("' ");
        }
        stringBuffer.append("WHERE\n" +
                "\t1=1 \n");
        if (notEmpty(vo.getPId())) {
            stringBuffer.append(" and p.pId = ").append(vo.getPId());
        }
        if (notEmpty(vo.getType())) {
            stringBuffer.append(" and p.type = ").append(vo.getType());
        }
        return stringBuffer.toString();
    }

    /**
     * 今日进出 折线图
     * 数据分析页面
     * 今日0时至当前时间各个小时整点进出次数统计
     */
    public String getInOutDataByHours(RoadParkListVo vo) {
        String timeType = "";
        if (vo.getInOutType().equals("in")) {
            timeType = "driveinTime";
        } else {
            timeType = "driveoutTime";
        }
        //获取当前时间小时
        int hour = new DateTime().getHourOfDay();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT isnull(b.amount, 0) AS amount,c.click_date as name  " +
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
                " COUNT( CASE WHEN pr." + timeType + " IS NOT NULL THEN 1 END ) AS amount," +
                " DATEPART( HH, pr." + timeType + " ) as hours " +
                " from ");
        sqlBuffer.append(" road_park_list pr ");
        sqlBuffer.append(" where 1=1 ");
        sqlBuffer.append(" and CONVERT ( VARCHAR, pr." + timeType + ", 23 ) = '").append(new DateTime().toString("yyyy-MM-dd")).append("'");
        if (notEmpty(vo)) {
            // 类型 0路测 1停车场
            if (notEmpty(vo.getType())) {
                sqlBuffer.append(" and pr.type = ").append(vo.getType());
            }
            if (notEmpty(vo.getPId())) {
                sqlBuffer.append(" and pr.pId = ").append(vo.getPId());
            }
        }
        sqlBuffer.append(" GROUP BY DATEPART( HH, pr." + timeType + ") " +
                " ) b ");

        sqlBuffer.append(" ON c.click_date = b.hours");
        sqlBuffer.append(" ORDER BY CONVERT(INT, LEFT(c.click_date, LEN(c.click_date)-1)) ASC, RIGHT(c.click_date, 1)");
        return sqlBuffer.toString();
    }

    /**
     * 近30日进出 折线图
     */
    public String getInOutDaysData(RoadParkListVo vo) {
        String timeType = "";
        if (notEmpty(vo.getInOutType())) {
            if (vo.getInOutType().equals("in")) {
                timeType = "driveinTime";
            } else {
                timeType = "driveoutTime";
            }
        }

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT ");
        sqlBuffer.append(" isnull( b.amount, 0 ) AS amount, ");
        sqlBuffer.append(" c.click_date AS name \n" +
                "FROM\n" +
                "\t(\n");
        for (int i = 0; i <= 29; i++) {
            sqlBuffer.append(" SELECT CONVERT( VARCHAR ( 100 ), dateadd( DAY, -" + i + ", getdate()), 23 )").append(" AS click_date");
            if (i < 29) {
                sqlBuffer.append("  UNION ALL ");
            }
        }
        sqlBuffer.append(" ) c\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT");
        sqlBuffer.append(" COUNT( CASE WHEN pr." + timeType + " IS NOT NULL THEN 1 END ) AS amount, ");
        sqlBuffer.append(" CONVERT ( VARCHAR ( 100 ), pr." + timeType + ", 23 ) AS days ");
        sqlBuffer.append(" FROM\n" +
                "\t\troad_park_list pr \n" +
                "\tWHERE\n" +
                "\t\t1 = 1 \n");
        sqlBuffer.append(" and CONVERT ( VARCHAR ( 100 ), pr." + timeType + ", 23 ) >= '").append(new DateTime().plusDays(-29).toString("yyyy-MM-dd")).append("'");
        sqlBuffer.append(" and CONVERT ( VARCHAR ( 100 ), pr." + timeType + ", 23 ) <= '").append(new DateTime().toString("yyyy-MM-dd")).append("'");
        if (notEmpty(vo.getType())) {
            sqlBuffer.append(" and pr.type = ").append(vo.getType());
        }
        if (notEmpty(vo.getPId())) {
            sqlBuffer.append(" and pr.pId = ").append(vo.getPId());
        }
        sqlBuffer.append(" GROUP BY CONVERT ( VARCHAR ( 100 ), pr." + timeType + ", 23 ) ");
        sqlBuffer.append("\t) b ON c.click_date = b.days \n" +
                "ORDER BY\n" +
                "\tc.click_date");
        return sqlBuffer.toString();
    }

    public String getOrderByParkAndCarNo(Integer parkId, Integer carType, String carNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" 	po.*, oc.car_no, p.address park_name");
        sql.append(" FROM");
        sql.append(" 	parking_order po");
        sql.append(" 	LEFT JOIN operate_carno oc ON po.carno_id = oc.id ");
        sql.append(" 	LEFT JOIN park p ON po.park_id = p.id  ");
        sql.append(" WHERE");
        sql.append(" 	1 = 1 ");
        sql.append(" 	AND po.park_id = " + parkId);
        sql.append(" 	AND oc.car_no = '" + carNo + "'");
        sql.append(" 	AND oc.car_type = " + carType);
        sql.append(" 	AND po.status = '1'");
        sql.append("    AND po.is_del = 0");
        return sql.toString();
    }

    /**
     * 支付记录关联 路段和停车场订单信息
     */
    public String getOrderDataByPaymentId(RoadParkListVo bean) {
        String sql = "select * from road_park_list where 1=1 ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getPayment_id())) {
                sql += " and payment_id = " + bean.getPayment_id();
            }
        }
        return getQueryHandler(sql).getSql();
    }
}
