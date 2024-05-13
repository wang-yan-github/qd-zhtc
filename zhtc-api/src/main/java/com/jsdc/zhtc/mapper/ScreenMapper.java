package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.ScreenDao;
import com.jsdc.zhtc.model.Screen;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 诱导屏管理
 *
 * @Author thr
 * @create 2022-11-17 14:13:33
 */
@Mapper
public interface ScreenMapper extends BaseMapper<Screen> {

    @SelectProvider(method = "toList", type = ScreenDao.class)
    List<Screen> toList(Screen beanParam);
}
