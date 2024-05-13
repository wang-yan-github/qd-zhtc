package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.CarnoBindRecordDao;
import com.jsdc.zhtc.model.CarnoBindRecord;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @ClassName CarnoBindRecordService
 * @Description TODO
 * @Author xujian
 * @Date 2022/1/10 15:27
 * @Version 1.0
 */
@Transactional
@Service
public class CarnoBindRecordService extends BaseService<CarnoBindRecordDao, CarnoBindRecord> {

    public CarnoBindRecord getRecord(Integer carId, Integer memberId) {
        LambdaQueryWrapper<CarnoBindRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarnoBindRecord::getCar_id, carId).eq(CarnoBindRecord::getMember_id, memberId);
        wrapper.eq(CarnoBindRecord::getIs_del, GlobalData.ISDEL_NO).eq(CarnoBindRecord::getBind_type, "0");
        wrapper.orderByDesc(CarnoBindRecord::getCreate_time);
        List<CarnoBindRecord> list = selectList(wrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
