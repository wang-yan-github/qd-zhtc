package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ParkingReleasePlaceDao;
import com.jsdc.zhtc.model.ParkingReleasePlace;
import com.jsdc.zhtc.vo.ParkingReleasePlaceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ParkingReleasePlaceMapper extends BaseMapper<ParkingReleasePlace> {
    //根据收费id查询信息
    @SelectProvider(type = ParkingReleasePlaceDao.class, method = "selectByPReleaseId")
    List<ParkingReleasePlaceVo> selectByPReleaseId(Integer parking_release_id, String redisRoadOrPark);
}
