package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.CwZzlDataDao;
import com.jsdc.zhtc.model.CwZzlData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @Author thr
 * @create 2023-01-18 11:00:08
 */
@Mapper
public interface CwZzlDataMapper extends BaseMapper<CwZzlData> {

    @SelectProvider(method = "toList", type = CwZzlDataDao.class)
    List<CwZzlData> toList(CwZzlData beanParam);

    /**
     * 柱状图：日均车位周转率：最近7天/日均车位周转率
     */
    @SelectProvider(method = "getDaysZzlList", type = CwZzlDataDao.class)
    List<CwZzlData> getDaysZzlList(CwZzlData beanParam);

}
