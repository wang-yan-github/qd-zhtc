package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ProvisionalPassDao;
import com.jsdc.zhtc.model.ProvisionalPass;
import com.jsdc.zhtc.vo.ProvisionalPassVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ProvisionalPassMapper extends BaseMapper<ProvisionalPass> {


    @SelectProvider(method = "toList", type = ProvisionalPassDao.class)
    List<ProvisionalPassVo> toList(ProvisionalPassVo passVo);
}
