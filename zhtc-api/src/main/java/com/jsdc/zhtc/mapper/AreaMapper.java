package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.AreaDao;
import com.jsdc.zhtc.model.Area;
import com.jsdc.zhtc.vo.AreaVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {

    @SelectProvider(method = "getAll", type = AreaDao.class)
    List<AreaVo> getAll(Integer id);

    @SelectProvider(method = "toList", type = AreaDao.class)
    List<AreaVo> toList(Area area);

}
