package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.InspectManage;
import com.jsdc.zhtc.vo.InspectManageVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: InspectManageDao
 * Description:巡检管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class InspectManageDao extends BaseDao<InspectManage> {


    public String manageParkDetail(Integer id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT park.park_name,park.park_num, ");
        sql.append("(SELECT COUNT(po.id) FROM parking_order po WHERE po.park_id=park.id AND po.status='1' AND po.is_del = 0) as occupy_num ");
        sql.append("FROM inspect_manage im ");
        sql.append("LEFT JOIN section_inspector si ON im.id = si.inspect_id ");
        sql.append("AND si.parking_type = '1'  ");
        sql.append("LEFT JOIN park ON si.allocated_section_id=park.id ");
        sql.append("where im.id= " + id);

        return sql.toString();
    }

    /**
     * 关联路段巡检员表、分配路段表、路段管理表
     *
     * @param bean
     * @return
     */
    public String selectPageList(InspectManageVo bean, String redisRoadOrPark) {
        String sql = " select DISTINCT mag.id, mag.job_no,mag.name,mag.age,mag.gender,mag.phone,mag.is_del,mag.idCard,mag.personType,mag.login_name,mag.invoice_balance from inspect_manage mag " +
                " LEFT JOIN section_inspector s on mag.id = s.inspect_id ";
        if (GlobalData.PARKING_TYPE_ROAD.equals(redisRoadOrPark)) {
            sql += " LEFT JOIN road aec on aec.id = s.allocated_section_id where mag.is_del = '0' and mag.personType=" + redisRoadOrPark;
        } else {
            sql += " LEFT JOIN park aec on aec.id = s.allocated_section_id where mag.is_del = '0' and mag.personType=" + redisRoadOrPark;
        }
        if (notEmpty(bean)) {
            if (notEmpty(bean.getName())) {
                sql += " and (mag.phone like '%" + bean.getName() + "%' or mag.name like '%" + bean.getName() + "%')";
            }
            if (notEmpty(bean.getArea_id())) {
                sql += " and aec.area_id = " + bean.getArea_id();
            }
            if (notEmpty(bean.getStreet_id())) {
                sql += " and aec.street_id = " + bean.getStreet_id();
            }
            if (notEmpty(bean.getRoad_id())) {
                sql += " and s.allocated_section_id = " + bean.getRoad_id();
            }
        }
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

    /**
     * create by zonglina at 2022/1/5 16:39
     * description:
     * 根据路段、巡检员统计停车位总数group
     *
     * @return : null
     * @param:null
     */
    public String selectByGroupBerthnum(String parkType, Integer inspect_id) {
        String sql = "";
        if (notEmpty(parkType)) {
            if ("0".equals(parkType)) {
                sql += "select sum(a.berth_num) from (\n" +
                        "SELECT (SELECT COUNT (d.id) FROM device d WHERE d.road_id = r.id and d.is_del='0' and d.is_use='1') AS berth_num \n" +
                        "FROM\n" +
                        "\tsection_inspector t\n" +
                        "\tLEFT JOIN road r ON r.id = t.allocated_section_id\n";
                sql += "  LEFT JOIN inspect_manage m on m.id=t.inspect_id  " +
                        " WHERE r.is_del=0 and t.inspect_id=" + inspect_id + " and m.personType='" + parkType + "'";
                sql += ") a\n";
            } else {
                sql += "select SUM(park_num) as berth_num  from section_inspector t  LEFT JOIN park r on r.id = t.allocated_section_id ";
                sql += "  LEFT JOIN inspect_manage m on m.id=t.inspect_id  " +
                        " WHERE r.is_del=0 and t.inspect_id=" + inspect_id + " and m.personType='" + parkType + "'";
            }
        }

        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

    /**
     * create by zonglina at 2022/1/5 16:39
     * description:
     * 计算余额
     *
     * @return : null
     * @param:null
     */
    public String afterAccountBalance(Integer inject_id) {
        String sql = "select top 1  p.after_account_balance  from patrol_recharge_management p where p.inject_id='" + inject_id + "' ORDER BY p.create_time DESC ";
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }


    /**
     * create by wp at 2022/1/5 14:16
     * description: 巡检统计查询巡检人员
     *
     * @param areaId
     * @param streetId
     * @param roadId
     * @param name
     * @param phone
     * @return java.lang.String
     */
    public String getPageForStatistics(Integer areaId, Integer streetId, Integer roadId, String name, String phone) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT");
        sql.append(" i.id inspect_id,");
        sql.append(" i.name,");
        sql.append(" i.phone,");
        sql.append(" i.job_no");
        sql.append(" FROM");
        sql.append(" inspect_manage i");
        sql.append(" LEFT JOIN section_inspector s ON i.id = s.inspect_id");
        sql.append(" LEFT JOIN road r ON r.id = s.allocated_section_id");
        sql.append(" where 1=1");
        if (null != areaId) {
            sql.append(" and r.area_id = " + areaId);
        }
        if (null != streetId) {
            sql.append(" and r.street_id = " + streetId);
        }
        if (null != roadId) {
            sql.append(" and r.id = " + roadId);
        }
        if (StringUtils.isNotEmpty(name)) {
            sql.append(" and i.name like '%" + name + "%'");
        }
        if (StringUtils.isNotEmpty(phone)) {
            sql.append(" and i.phone like '%" + phone + "%'");
        }
        sql.append(" and i.is_del = '0'");
        sql.append(" and s.is_del = '0'");
        sql.append(" and r.is_del = '0'");
        sql.append(" and i.personType = '0'");
        return sql.toString();
    }

    public String getPageForParkStatistics(Integer areaId, Integer streetId, Integer parkId, String name, String phone) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT");
        sql.append(" i.id inspect_id,");
        sql.append(" i.name,");
        sql.append(" i.phone,");
        sql.append(" i.job_no");
        sql.append(" FROM");
        sql.append(" inspect_manage i");
        sql.append(" LEFT JOIN section_inspector s ON i.id = s.inspect_id");
        sql.append(" LEFT JOIN park p ON p.id = s.allocated_section_id");
        sql.append(" where 1=1");
        if (null != areaId) {
            sql.append(" and p.area_id = " + areaId);
        }
        if (null != streetId) {
            sql.append(" and p.street_id = " + streetId);
        }
        if (null != parkId) {
            sql.append(" and p.id = " + parkId);
        }
        if (StringUtils.isNotEmpty(name)) {
            sql.append(" and i.name like '%" + name + "%'");
        }
        if (StringUtils.isNotEmpty(phone)) {
            sql.append(" and i.phone like '%" + phone + "%'");
        }
        sql.append(" and i.is_del = '0'");
        sql.append(" and s.is_del = '0'");
        sql.append(" and p.is_del = '0'");
        sql.append(" and i.personType = '1'");
        return sql.toString();
    }
//    public String getPageForStatistics(Integer areaId, Integer streetId, Integer roadId, String name, String phone) {
//        StringBuilder sql = new StringBuilder();
//        sql.append(" select");
//        sql.append(" s.inspect_id , i.name, i.job_no");
//        sql.append(" from");
//        sql.append(" allocated_section a");
//        sql.append(" LEFT JOIN section_inspector s on a.id = s.allocated_section_id");
//        sql.append(" LEFT JOIN inspect_manage i on s.inspect_id = i.id");
//        sql.append(" where 1=1");
//        if (null != areaId) {
//            sql.append(" and a.area_id = " + areaId);
//        }
//        if (null != streetId) {
//            sql.append(" and a.street_id = " + streetId);
//        }
//        if (null != roadId) {
//            sql.append(" and a.road_id = " + roadId);
//        }
//        if (StringUtils.isNotEmpty(name)) {
//            sql.append(" and i.name like '%" + name + "%'");
//        }
//        if (StringUtils.isNotEmpty(phone)) {
//            sql.append("and i.phone like '%" + phone + "%'");
//        }
//        return sql.toString();
//    }

    /**
     * create by wp at 2022/1/5 14:34
     * description: 查询巡检人员绑定的路段数量
     *
     * @param inspectId
     * @return java.lang.String
     */
    public String getRoadCount(Integer inspectId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" DISTINCT s.allocated_section_id");
        sql.append(" FROM");
        sql.append(" section_inspector s");
        sql.append(" WHERE");
        sql.append(" s.inspect_id = " + inspectId);
        sql.append(" AND s.is_del = '0'");
        return sql.toString();
    }

    public String getParkingOrders(String status, String startTime, String endTime, String carno, Integer inspectId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" po.id poId,");
        sql.append(" po.drivein_time,");
        sql.append(" po.driveout_time,");
        sql.append(" po.is_invoice,");
        sql.append(" p.park_name,");
        sql.append(" p.address,");
        sql.append(" po.status,");
        sql.append(" po.paid_amount,");
        sql.append(" inv.id invid,");
        sql.append(" oc.car_no,");
        sql.append(" po.order_no,");
        sql.append(" po.resitime");
        sql.append(" FROM");
        sql.append(" inspect_manage im");
        sql.append(" LEFT JOIN section_inspector si ON im.id = si.inspect_id");
        sql.append(" LEFT JOIN parking_order po ON si.allocated_section_id = po.park_id");
        sql.append(" LEFT JOIN park p ON si.allocated_section_id = p.id");
        sql.append(" LEFT JOIN invoices_management inv ON po.invoice_id = inv.id");
        sql.append(" LEFT JOIN operate_appeal o  ON po.id = o.parking_order_id");
        sql.append(" LEFT JOIN operate_carno oc ON po.carno_id = oc.id");
        sql.append(" WHERE");
        sql.append(" 1 = 1");
        sql.append(" AND im.id = " + inspectId);
        sql.append(" AND po.status IN ('3', '4')");
        sql.append(" AND po.sum_amount > '0'");
        if (StringUtils.equals(status, "0")) {//未领取发票
            sql.append(" AND po.is_invoice = '0'");
            sql.append(" AND inv.id IS NULL");
            sql.append(" AND (o.appeal_status != '1' or o.appeal_status is null)");
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" AND po.drivein_time >= '" + startTime + "'");

        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" AND po.drivein_time <= '" + endTime + "'");

        }
        if (StringUtils.isNotEmpty(carno)) {
            sql.append(" AND oc.car_no = '" + carno + "'");
        }
        return sql.toString();
    }
}
