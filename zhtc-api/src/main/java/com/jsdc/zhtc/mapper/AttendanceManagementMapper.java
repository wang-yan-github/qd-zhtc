package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.AttendanceManagementDao;
import com.jsdc.zhtc.model.AttendanceManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 考勤管理
 *
 * @Author thr
 * @create 2022-08-24 14:08:08
 */
@Mapper
public interface AttendanceManagementMapper extends BaseMapper<AttendanceManagement> {

    @SelectProvider(method = "toList", type = AttendanceManagementDao.class)
    List<AttendanceManagement> toList(AttendanceManagement beanParam);

    @SelectProvider(method = "toReport", type = AttendanceManagementDao.class)
    List<AttendanceManagement> toReport(AttendanceManagement beanParam);
}
