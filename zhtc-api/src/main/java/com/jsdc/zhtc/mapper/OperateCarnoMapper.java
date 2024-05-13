package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.OperateCarnoDao;
import com.jsdc.zhtc.model.OperateCarno;
import com.jsdc.zhtc.vo.OperateCarnoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface OperateCarnoMapper extends BaseMapper<OperateCarno> {
    @SelectProvider(method = "toList", type = OperateCarnoDao.class)
    List<OperateCarnoVo> toList(OperateCarnoVo operateCarnoVo);

    @SelectProvider(method = "getWhiteCarnoParks", type = OperateCarnoDao.class)
    List<String> getWhiteCarnoParks(Integer carnoId);

    @SelectProvider(method = "getParkCarno", type = OperateCarnoDao.class)
    List<Integer> getParkCarno(Integer parkId);

}
