package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.OperateAppeal;
import com.jsdc.zhtc.vo.RoadOrParkingCommentVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: OperateAppealDao
 * Description:申诉管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class OperateAppealDao extends BaseDao<OperateAppeal> {

    //分页
    public String selectByPage(RoadOrParkingCommentVo bean, String redisRoadOrPark) {
        String parmate = " parking_order r";

        String sql = " select o.id,o.parking_order_id,o.appeal_time,r.resitime,r.sum_amount as unpaid_amount,c.car_no,c.car_type, r.status as appeal_order_status,appeal_status,r.order_no as order_no,o.is_verify " +
                " ,r.drivein_time ,r.driveout_time from operate_appeal o  " +
                " LEFT JOIN " + parmate + " on o.parking_order_id = r.id LEFT JOIN operate_carno c on r.carno_id = c.id " +
                " where o.is_del = '0' and o.appeal_type = " + redisRoadOrPark;
        if (notEmpty(bean)) {
            if (notEmpty(bean.getUserType()) && GlobalData.PARKING_TYPE_PLAT.equals(bean.getUserType())) {
                if (notEmpty(bean.getPark_id())) {
                    sql += " and r.park_id  = '" + bean.getPark_id() + "'";
                }
            }

            if (notEmpty(bean.getRoadIds())) {
                sql += " and r.park_id  in (" + bean.getRoadIds() + ")";
            }

            if (notEmpty(bean.getAppeal_status())) {
                sql += " and o.appeal_status = " + bean.getAppeal_status();
            }
            if (notEmpty(bean.getKeys())) {
                sql += " and (r.order_no like '" + bean.getKeys() + "' or c.car_no like '" + bean.getKeys() + "') ";
            }
            if (notEmpty(bean.getStartTime())) {
                sql += " and o.appeal_time >= '" + bean.getStartTime() + "'";
            }
            if (notEmpty(bean.getEndTime())) {
                // 增加一天endtime o.appeal_time
                sql += " and o.appeal_time <= '" + bean.getEndTime() + " 23:59:59'";
//                sql += " and r.create_time <=  '"+bean.getEndTime()+"'" ;
            }
        }
        QueryHandler queryHandler = getQueryHandler(sql);
        queryHandler.order("appeal_status,o.appeal_time desc");
        return queryHandler.getSql();
    }

    /**
     * 申诉订单申诉状态统计
     */
    public String getAppealStatusCount(RoadOrParkingCommentVo bean, String redisRoadOrPark) {
        String parmate = " parking_order r";

        String sql = " select count(1) counts, o.appeal_status name from operate_appeal o  " +
                " LEFT JOIN " + parmate + " on o.parking_order_id = r.id " +
                " LEFT JOIN operate_carno c on r.carno_id = c.id " +
                " where o.is_del = '0' " +
                " and o.appeal_type = " + redisRoadOrPark;
        if (notEmpty(bean)) {
            if (notEmpty(bean.getUserType()) && GlobalData.PARKING_TYPE_PLAT.equals(bean.getUserType())) {
                if (notEmpty(bean.getPark_id())) {
                    sql += " and r.park_id  = '" + bean.getPark_id() + "'";
                }
            }

            if (notEmpty(bean.getRoadIds())) {
                sql += " and r.park_id  in (" + bean.getRoadIds() + ")";
            }

            if (notEmpty(bean.getAppeal_status())) {
                sql += " and o.appeal_status = " + bean.getAppeal_status();
            }
            if (notEmpty(bean.getKeys())) {
                sql += " and (r.order_no like '" + bean.getKeys() + "' or c.car_no like '" + bean.getKeys() + "') ";
            }
            if (notEmpty(bean.getStartTime())) {
                sql += " and o.appeal_time >= '" + bean.getStartTime() + "'";
            }
            if (notEmpty(bean.getEndTime())) {
                // 增加一天endtime o.appeal_time
                sql += " and o.appeal_time <= '" + bean.getEndTime() + " 23:59:59'";
//                sql += " and r.create_time <=  '"+bean.getEndTime()+"'" ;
            }
        }
        sql += " GROUP BY o.appeal_status";
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

    /**
     * 近七日服务类型趋势 订单申诉 最近7日/数量
     */
    public String getDaysCount() {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT\n" +
                "\tisnull( b.count, 0 ) AS counts,\n" +
                "\tc.click_date AS name \n" +
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
                "\tSELECT COUNT\n" +
                "\t\t( id ) AS COUNT,\n" +
                "\t\tCONVERT ( VARCHAR ( 100 ), pr.create_time, 23 ) AS days \n" +
                "\tFROM\n" +
                "\t\toperate_appeal pr \n" +
                "\tWHERE\n" +
                "\t\tis_del = 0 \n" +
                "\tGROUP BY\n" +
                "\t\tCONVERT ( VARCHAR ( 100 ), pr.create_time, 23 ) \n" +
                "\t) b ON c.click_date = b.days \n" +
                "ORDER BY\n" +
                "\tc.click_date");
        return sqlBuffer.toString();
    }
}
