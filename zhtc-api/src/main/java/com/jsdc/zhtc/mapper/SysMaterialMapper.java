package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.SysMaterialDao;
import com.jsdc.zhtc.model.SysMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 素材管理
 *
 * @Author thr
 * @create 2022-12-01 10:52:56
 */
@Mapper
public interface SysMaterialMapper extends BaseMapper<SysMaterial> {

    @SelectProvider(method = "toList", type = SysMaterialDao.class)
    List<SysMaterial> toList(SysMaterial beanParam);
}
