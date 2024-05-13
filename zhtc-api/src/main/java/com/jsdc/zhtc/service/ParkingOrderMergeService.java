package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.ParkingOrderMergeDao;
import com.jsdc.zhtc.model.ParkingOrderMerge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * ClassName: ParkingOrderMergeService
 * Description:
 * date: 2022/1/4 15:56
 *
 * @author wh
 */
@Service
@Transactional
public class ParkingOrderMergeService extends BaseService<ParkingOrderMergeDao, ParkingOrderMerge> {


    /**
     * @param orderIds       停车订单合并后ID
     * @param ordermerge_ids 被合并订单ID
     * @return
     */
    public void save(String orderIds, Integer ordermerge_ids) {
        if (StringUtils.isNotEmpty(orderIds)) {
            String[] ids = orderIds.split(",");
            for (String id : ids) {
                if (StringUtils.isNotEmpty(id)) {
                    ParkingOrderMerge merge = new ParkingOrderMerge();
                    merge.setCreate_time(new Date());
                    //merge.setCreate_user(sysUserService.getUser().getId());
                    merge.setIs_del(0);
                    merge.setOrdermerge_id(ordermerge_ids);
                    merge.setParking_order_id(Integer.valueOf(id));
                    insert(merge);
                }
            }
        }
    }
}
