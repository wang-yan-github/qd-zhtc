package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.PaymonthlyConfigDao;
import com.jsdc.zhtc.model.PaymonthlyConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface PaymonthlyConfigMapper extends BaseMapper<PaymonthlyConfig> {

    @SelectProvider(method = "getList", type = PaymonthlyConfigDao.class)
    List<PaymonthlyConfig> getList(PaymonthlyConfig bean);
}
