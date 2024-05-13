package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.vo.CommonVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: ParkDao <br/>
 * Description: <br/>
 * date: 2021/12/30 11:00<br/>
 *
 * @author bn<br                                                                                                                               />
 */
@Repository
public class ParkDao extends BaseDao<Park> {


    public String toList(Park park) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT park.*, area.area_name,street.street_name FROM park ");
        sql.append("LEFT JOIN area ON park.area_id = area.id ");
        sql.append("LEFT JOIN street ON park.street_id = street.id ");
        sql.append("where 1=1 ");

        if (park.getId() != null) {
            sql.append(" AND park.id=" + park.getId());
        }

        if (StringUtils.isNotEmpty(park.getPark_name())) {
            sql.append(" AND park.park_name LIKE '%" + park.getPark_name() + "%'");
        }
        if (park.getArea_id() != null) {
            sql.append(" AND park.area_id=" + park.getArea_id());
        }
        if (park.getStreet_id() != null) {
            sql.append(" AND park.street_id=" + park.getStreet_id());
        }
        sql.append(" AND park.is_del=0");
        return sql.toString();
    }

    /**
     * 描 述： TODO(查询获取停车场信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link String}
     */
    public String getSumParkNum(CommonVo data) {

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select sum(park_num) total from park ");
        sqlbd.append(" where is_del = ").append(GlobalData.ISDEL_NO);
        sqlbd.append(" and status = 0 ");
        if (data.getVariance() != null)
            sqlbd.append(" and area_id = ").append(data.getVariance());
        if (data.getVariance2() != null)
            sqlbd.append(" and street_id = ").append(data.getVariance2());
        if (data.getVariance3() != null)
            sqlbd.append(" and id = ").append(data.getVariance3());

        if (StringUtils.isNotBlank(data.getCarNo())) {
            sqlbd.append(" and id in ( select park_id from parking_order  ");
            sqlbd.append(" where carno_id in ( select id from operate_carno where car_no = '").append(data.getCarNo()).append("' )  ) ");
        }
        System.out.println(" getSumParkNum -- sql---->: " + sqlbd.toString());

        return sqlbd.toString();
    }

    //计算总泊位号
    public String berthSumData(String type) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT sum(p.park_num) as amount from park p where p.status='0' and p.is_del='0' ");
        return sqlBuffer.toString();
    }
}
