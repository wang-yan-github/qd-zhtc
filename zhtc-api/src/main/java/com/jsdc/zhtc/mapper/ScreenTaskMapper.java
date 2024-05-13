package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ScreenTaskDao;
import com.jsdc.zhtc.model.ScreenTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ScreenTaskMapper extends BaseMapper<ScreenTask> {
    @SelectProvider(method = "getPage", type = ScreenTaskDao.class)
    List<ScreenTask> getPage(Integer screenId);
}
