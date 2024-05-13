package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.CwMwlDataDao;
import com.jsdc.zhtc.model.CwMwlData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @Author thr
 * @create 2023-01-17 16:42:01
 */
@Mapper
public interface CwMwlDataMapper extends BaseMapper<CwMwlData> {

    @SelectProvider(method = "toList", type = CwMwlDataDao.class)
    List<CwMwlData> toList(CwMwlData beanParam);

    @SelectProvider(method = "getHourMwlList", type = CwMwlDataDao.class)
    List<CwMwlData> getHourMwlList(CwMwlData beanParam);
}
