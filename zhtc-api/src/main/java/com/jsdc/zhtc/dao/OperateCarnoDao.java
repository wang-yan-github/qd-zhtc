package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.vo.OperateCarnoVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: OperateCarnoDao
 * Description:车牌管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class OperateCarnoDao extends BaseDao<OperateCarno> {


    public String toList(OperateCarnoVo operateCarnoVo) {
        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT oc.id,oc.bind_type, oc.car_natures, oc.car_no, oc.car_type, oc.company_id, oc.create_time, oc.create_user, oc.is_del, oc.member_id, oc.roster_type, oc.update_time, oc.update_user, oc.whitelist_type, oc.reason, oc.bind_date, oc.company_name, oc.cut_off_date, oc.deformity_cert, oc.deformity_picture_id, oc.name, mm.phone from operate_carno oc ");
        sql.append("SELECT oc.id,oc.bind_type, oc.car_no, oc.car_type, oc.company_id, oc.create_time, oc.create_user, " +
                "oc.is_del, oc.member_id, oc.roster_type, oc.update_time, oc.update_user, oc.whitelist_type, oc.reason, " +
                "oc.bind_date, oc.company_name, oc.cut_off_date, oc.deformity_cert, oc.deformity_picture_id, " +
                "oc.name, oc.phone phone2, mm.phone, mm.nick_name bindName, u.user_name userName, " +
                "oc.free_time_start, oc.free_time_end, oc.white_time_type " +
                " from operate_carno oc ");
        sql.append("LEFT JOIN member_manage mm ON oc.member_id=mm.id and mm.is_del=0");
        sql.append("LEFT JOIN sys_user u ON u.id = oc.create_user ");
        if (StringUtils.isNotNull(operateCarnoVo.getParkId())) {
            sql.append(" INNER JOIN whitecarno_park wp ON wp.carno_id = oc.id \n" +
                    "\tAND wp.park_type= '1'\n" +
                    "\tINNER JOIN park p ON wp.park_id = p.id ");
            sql.append(" and p.id = ").append(operateCarnoVo.getParkId());
        }

        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(operateCarnoVo.getPhone())) {
            sql.append(" AND mm.phone like '%" + operateCarnoVo.getPhone() + "%'");
        }
        if (StringUtils.isNotEmpty(operateCarnoVo.getCar_no())) {
            sql.append(" AND oc.car_no like '%" + operateCarnoVo.getCar_no() + "%'");
        }
        if (StringUtils.isNotEmpty(operateCarnoVo.getCar_type())) {
            sql.append(" AND oc.car_type = '" + operateCarnoVo.getCar_type() + "'");
        }
        if (StringUtils.isNotEmpty(operateCarnoVo.getCompanyName())) {
            sql.append(" AND oc.company_name like '%" + operateCarnoVo.getCompanyName() + "%'");
        }

        if (StringUtils.isNotEmpty(operateCarnoVo.getRoster_type())) {
            // 白名单未生效
            if (operateCarnoVo.getRoster_type().equals("99")) {
                //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
                sql.append(" AND oc.roster_type = '1' ");
                // 白名单免费类型 1永久 2期限
                sql.append(" AND oc.white_time_type = '2' ");
//                sql.append(" AND oc.free_time_start is not null ");
//                sql.append(" AND oc.free_time_end is not null ");
                sql.append(" AND oc.free_time_start >= GETDATE() ");
                sql.append(" AND oc.free_time_end >= GETDATE() ");
            } else {
                sql.append(" AND oc.roster_type = '").append(operateCarnoVo.getRoster_type()).append("'");
            }
        }

        if (StringUtils.isNotEmpty(operateCarnoVo.getWhitelist_type())) {
            sql.append(" AND oc.whitelist_type = '" + operateCarnoVo.getWhitelist_type() + "'");
        }
        if (operateCarnoVo.getMember_id() != null) {
            sql.append(" AND oc.member_id=" + operateCarnoVo.getMember_id());
        }

        if (StringUtils.isNotEmpty(operateCarnoVo.getName())) {
            sql.append(" AND oc.name like '%" + operateCarnoVo.getName() + "%'");
        }
        if (StringUtils.isNotEmpty(operateCarnoVo.getUserName())) {
            sql.append(" AND u.user_name like '%" + operateCarnoVo.getUserName() + "%'");
        }

        if (operateCarnoVo.getCarnoIds() != null && operateCarnoVo.getCarnoIds().size() > 0) {
            String ids = "";
            for (Integer carnoId : operateCarnoVo.getCarnoIds()) {
                ids = ids + carnoId + ",";
            }
            ids = ids.substring(0, ids.length() - 1);
            sql.append(" AND oc.id in (" + ids + ")");
        }
        sql.append(" AND oc.is_del=0 ");
        sql.append(" order by oc.create_time desc");

        return sql.toString();
    }

    public String getWhiteCarnoParks(Integer carnoId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" 	p.park_name ");
        sql.append(" FROM");
        sql.append(" 	whitecarno_park wp");
        sql.append(" 	LEFT JOIN park p ON wp.park_id = p.id ");
        sql.append(" WHERE");
        sql.append(" 	carno_id = " + carnoId);
        sql.append(" 	AND park_type = 1");
        return sql.toString();
    }

    public String getParkCarno(Integer parkId) {
        String sql = "select c.carno_id from paymonthly_config a\n" +
                " left join paymonthly_parkingplace b\n" +
                " on a.id=b.paymonthly_config_id\n" +
                " left join monthly_management c\n" +
                " on c.paymonthly_config_id = a.id and c.is_del='0'\n" +
                " where a.is_del = '0'\n" +
                " and a.parking_type='1'\n" +
                " and b.parkingplace_id = " + parkId +
                " and c.end_time >= GETDATE()";

        return sql;
    }

}
