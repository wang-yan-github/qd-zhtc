package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.MemberManageDao;
import com.jsdc.zhtc.model.MemberManage;
import com.jsdc.zhtc.vo.MemberManageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface MemberManageMapper extends BaseMapper<MemberManage> {
    @SelectProvider(method = "toList", type = MemberManageDao.class)
    List<MemberManageVo> toList(MemberManageVo memberManageVo);


    @SelectProvider(method = "getTodayAddCount", type = MemberManageDao.class)
    int getTodayAddCount();

}
