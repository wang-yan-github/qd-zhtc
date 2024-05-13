package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ParkDeviceDao;
import com.jsdc.zhtc.model.ParkDevice;
import com.jsdc.zhtc.vo.ParkDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ParkDeviceMapper extends BaseMapper<ParkDevice> {

    @SelectProvider(method = "toList", type = ParkDeviceDao.class)
    List<ParkDeviceVo> toList(ParkDeviceVo parkDeviceVo);

    @SelectProvider(method = "getParkDevice", type = ParkDeviceDao.class)
    ParkDeviceVo getParkDevice(ParkDeviceVo parkDeviceVo);
}
