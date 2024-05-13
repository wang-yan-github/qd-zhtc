package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ParkingReleaseDao;
import com.jsdc.zhtc.model.ParkingRelease;
import com.jsdc.zhtc.vo.ParkingReleaseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ParkingReleaseMapper extends BaseMapper<ParkingRelease> {

    //分页功能
    @SelectProvider(type = ParkingReleaseDao.class, method = "selectPageList")
    List<ParkingReleaseVo> selectPageList(ParkingReleaseVo bean, String redisRoadOrPark);


    @SelectProvider(type = ParkingReleaseDao.class, method = "selectByParmCount")
    Integer selectByParmCount(String parking_type, Integer parkingplace_id, String start_time);
}
