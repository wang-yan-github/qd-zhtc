package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ChargeProgrammeDao;
import com.jsdc.zhtc.model.ChargeProgramme;
import com.jsdc.zhtc.vo.ChargeProgrammeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ChargeProgrammeMapper extends BaseMapper<ChargeProgramme> {

    @SelectProvider(method = "toList", type = ChargeProgrammeDao.class)
    List<ChargeProgrammeVo> toList(ChargeProgrammeVo chargeProgrammeVo);
}
