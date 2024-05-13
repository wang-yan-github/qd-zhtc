package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.PaymonthlyConfig;
import org.springframework.stereotype.Repository;

/**
 * ClassName: PaymonthlyConfigDao
 * Description:
 * date: 2021/12/30 10:21
 *
 * @author wp
 */
@Repository
public class PaymonthlyConfigDao extends BaseDao<PaymonthlyConfig> {

    /**
     * 微信小程序
     * 根据类型和停车场/路段id查询包月配置方案列表
     * thr
     */
    public String getList(PaymonthlyConfig bean) {
        String sql = "SELECT c.* from paymonthly_config c " +
                " left join paymonthly_parkingplace pp on pp.paymonthly_config_id = c.id";
        sql += " where c.is_del = '0' and pp.is_del = '0' and c.monthType = '1'";
        //停车区类型 0：路段 1：停车场
        if (notEmpty(bean.getParking_type())) {
            sql += " and c.parking_type = '" + bean.getParking_type() + "'";
        }
        //停车场/路段id
        if (notEmpty(bean.getParkingIds())) {
            sql += " and pp.parkingplace_id = '" + bean.getParkingIds() + "'";
        }
        if (notEmpty(bean.getId())) {
            sql += " and c.id = " + bean.getId();
        }
        return sql;
    }
}
