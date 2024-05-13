package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.model.ParkingOrderPics;
import org.springframework.stereotype.Repository;

/**
 * ClassName: ParkingOrderPicsDao
 * Description:
 * date: 2022/1/24 14:40
 *
 * @author wp
 */
@Repository
public class ParkingOrderPicsDao extends BaseDao<ParkingOrderPics> {

    //根据订单id获取所有图片
    public String selectByPid(Integer parking_order_id, String picture_type) {
        String sql = "select picture_id as file_url from parking_order_pics mag where is_del = 0 ";
        if (notEmpty(parking_order_id)) {
            sql += " and mag.parking_order_id  =" + parking_order_id + " and picture_type= '" + picture_type + "'";
        }
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }
}
