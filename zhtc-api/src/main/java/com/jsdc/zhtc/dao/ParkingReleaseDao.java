package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.ParkingRelease;
import com.jsdc.zhtc.vo.ParkingReleaseVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * ClassName: InspectManageDao
 * Description:上线收费配置表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class ParkingReleaseDao extends BaseDao<ParkingRelease> {

    /**
     * 分页功能
     *
     * @param bean
     * @return
     */
    public String selectPageList(ParkingReleaseVo bean, String redisRoadOrPark) {
        String sql = " select DISTINCT p.* from parking_release p left join release_parking_place r on p.id = r.parking_release_id where p.is_del = 0  and p.parking_type = '" + redisRoadOrPark + "'";
        sql += " and r.is_del = '0'";
        if (StringUtils.isNotEmpty(bean.getRoad_id())) {
            sql += " and r.parkingplace_id = " + bean.getRoad_id();
        }
        return getQueryHandler(sql).getSql();
    }


    public String selectByParmCount(String parking_type, Integer parkingplace_id, String start_time) {
        String sql = "select count(1) from parking_release  ss LEFT JOIN release_parking_place pp " +
                " on ss.id = pp.parking_release_id and pp.is_del = 0 where ss.is_del = 0 and ss.status='1' ";
        if (notEmpty(parking_type)) {
            sql += " and ss.parking_type='" + parking_type + "'";
        }
        if (notEmpty(parkingplace_id)) {
            sql += " and pp.parkingplace_id='" + parkingplace_id + "'";
        }
        if (notEmpty(start_time)) {
            sql += " and ss.start_time <= '" + start_time + "'";
        }
        return getQueryHandler(sql).getSql();
    }
}
