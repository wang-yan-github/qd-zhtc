package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ScreenParkRoadDao;
import com.jsdc.zhtc.model.ScreenParkRoad;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @Author thr
 * @create 2022-11-17 14:13:33
 */
@Mapper
public interface ScreenParkRoadMapper extends BaseMapper<ScreenParkRoad> {

    @SelectProvider(method = "toList", type = ScreenParkRoadDao.class)
    List<ScreenParkRoad> toList(ScreenParkRoad beanParam);
}
