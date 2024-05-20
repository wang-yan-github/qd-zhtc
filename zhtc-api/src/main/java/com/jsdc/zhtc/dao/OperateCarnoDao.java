package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.vo.OperateCarnoVo;
import org.springframework.stereotype.Repository;

/**
 * 车牌管理表
 *
 * @author thr
 */
@Repository
public class OperateCarnoDao extends BaseDao<OperateCarno> {


    public String toList(OperateCarnoVo operateCarnoVo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT oc.*, c.name,c.phone,u.user_name userName,d.label carTypeName, dr.label rosterTypeName ");
        sql.append(" from operate_carno oc");
        sql.append(" LEFT JOIN car_owner c ON c.id = oc.car_owner_id");//车主管理
        sql.append(" LEFT JOIN sys_user u ON u.id = oc.create_user ");//录入人
        sql.append(" LEFT JOIN sys_dict d on d.dc_value = oc.car_type and d.dict_type='car_type' "); //车牌类型
        sql.append(" LEFT JOIN sys_dict dr on dr.dc_value = oc.roster_type and d.dict_type='roster_type' ");//车辆类型

        sql.append(" where oc.is_del = '0' ");

        //录入人
        if (StringUtils.isNotEmpty(operateCarnoVo.getUserName())) {
            sql.append(" AND u.user_name like '%").append(operateCarnoVo.getUserName()).append("%'");
        }
        //车主姓名
        if (StringUtils.isNotEmpty(operateCarnoVo.getName())) {
            sql.append(" AND c.name like '%").append(operateCarnoVo.getName()).append("%'");
        }
        //车主手机号码
        if (StringUtils.isNotEmpty(operateCarnoVo.getPhone())) {
            sql.append(" AND c.phone like '%").append(operateCarnoVo.getPhone()).append("%'");
        }
        //车牌号码
        if (StringUtils.isNotEmpty(operateCarnoVo.getCar_no())) {
            sql.append(" AND oc.car_no like '%").append(operateCarnoVo.getCar_no()).append("%'");
        }
        //车牌类型(1蓝牌、2绿牌、3黄牌)
        if (StringUtils.isNotEmpty(operateCarnoVo.getCar_type())) {
            sql.append(" AND oc.car_type =  '").append(operateCarnoVo.getCar_type()).append("'");
        }
        //车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
        if (StringUtils.isNotEmpty(operateCarnoVo.getRoster_type())) {
            sql.append(" AND oc.roster_type = '").append(operateCarnoVo.getRoster_type()).append("'");
        }

        sql.append(" order by oc.create_time desc");

        return sql.toString();
    }


    public String carList(String parkId, String name, String carNo){
        String sql = "SELECT\n" +
                "\toc.car_no,c.name\n" +
                "FROM\n" +
                "\twhite_list wl\n" +
                "left join operate_carno oc on wl.car_id=oc.id\n" +
                "\t \n" +
                "\tLEFT JOIN car_owner c ON c.id = oc.car_owner_id where 1=1" ;
        if(StringUtils.isNotEmpty(name)){
            sql += " AND c.name like '%"+name+"%'";
        }

        if(StringUtils.isNotEmpty(carNo)){
            sql+= " and oc.car_no like '%"+carNo+"%'" ;
        }

        if(StringUtils.isNotEmpty(parkId)){
            sql+= " and wl.park_id = '"+parkId+"'" ;
        }

        return sql ;
    }

}
