package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.ParkingReleasePlace;
import org.springframework.stereotype.Repository;

/**
 * ClassName: InspectManageDao
 * Description:上线收费配置表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class ParkingReleasePlaceDao extends BaseDao<ParkingReleasePlace> {

    //根据收费id查询信息
    public String selectByPReleaseId(Integer parking_release_id, String redisRoadOrPark) {
        String sql;
        if (GlobalData.PARKING_TYPE_ROAD.equals(redisRoadOrPark)) {
            sql = "select r.road_name as road_name,p.* from release_parking_place p LEFT JOIN road r on p.parkingplace_id = r.id  " +
                    "where p.is_del=0 and  p.parking_release_id = '" + parking_release_id + "' ";
        } else {
            sql = "select r.park_name as road_name,p.* from release_parking_place p LEFT JOIN park r on p.parkingplace_id = r.id " +
                    "where p.is_del=0 and  p.parking_release_id = '" + parking_release_id + "' ";
        }
        return getQueryHandler(sql).getSql();
    }
}
