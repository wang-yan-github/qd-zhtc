package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ParkingOrderPicsDao;
import com.jsdc.zhtc.model.ParkingOrderPics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ParkingOrderPicsMapper extends BaseMapper<ParkingOrderPics> {

    @SelectProvider(type = ParkingOrderPicsDao.class, method = "selectByPid")
    List<String> selectByPid(Integer parking_order_id, String picture_type);
}
