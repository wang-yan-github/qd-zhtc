package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.StreetDao;
import com.jsdc.zhtc.model.Street;
import com.jsdc.zhtc.vo.StreetVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface StreetMapper extends BaseMapper<Street> {

    @SelectProvider(method = "getAll", type = StreetDao.class)
    List<StreetVo> getAll(Integer id);

    @SelectProvider(method = "toList", type = StreetDao.class)
    List<StreetVo> toList(Street street);
}
