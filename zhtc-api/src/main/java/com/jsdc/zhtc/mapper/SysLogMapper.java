package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.SysLogDao;
import com.jsdc.zhtc.model.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
    @SelectProvider(method = "getPage", type = SysLogDao.class)
    List<SysLog> getPage(SysLog sysLog);

    @SelectProvider(method = "getUserList", type = SysLogDao.class)
    List<Map<String, String>> getUserList();
}
