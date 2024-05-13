package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.AttendanceManagementPicDao;
import com.jsdc.zhtc.model.AttendanceManagementPic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @Author thr
 * @create 2022-08-30 10:38:29
 */
@Mapper
public interface AttendanceManagementPicMapper extends BaseMapper<AttendanceManagementPic> {

    @SelectProvider(method = "toList", type = AttendanceManagementPicDao.class)
    List<AttendanceManagementPic> toList(AttendanceManagementPic beanParam);
}
