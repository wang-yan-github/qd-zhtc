package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.AttendanceManagementPicDao;
import com.jsdc.zhtc.mapper.AttendanceManagementPicMapper;
import com.jsdc.zhtc.model.AttendanceManagementPic;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author thr
 * @create 2022-08-30 10:38:29
 */
@Service
@Transactional
public class AttendanceManagementPicService extends BaseService<AttendanceManagementPicDao, AttendanceManagementPic> {

    @Autowired
    private AttendanceManagementPicMapper attendanceManagementPicMapper;
    @Autowired
    private SysUserService sysUserService;

    public PageInfo<AttendanceManagementPic> toList(Integer pageIndex, Integer pageSize, AttendanceManagementPic beanParam) {
        PageHelper.startPage(pageIndex, pageSize);

        List<AttendanceManagementPic> attendanceManagementPicVos = attendanceManagementPicMapper.toList(beanParam);

        PageInfo<AttendanceManagementPic> page = new PageInfo<>(attendanceManagementPicVos);

        return page;
    }

    public List<AttendanceManagementPic> getList(AttendanceManagementPic beanParam) {

        List<AttendanceManagementPic> attendanceManagementPicVos = attendanceManagementPicMapper.toList(beanParam);

        return attendanceManagementPicVos;
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<AttendanceManagementPic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        AttendanceManagementPic attendanceManagementPic = selectOne(queryWrapper);
        return ResultInfo.success(attendanceManagementPic);
    }

    /**
     * 添加
     */
    public ResultInfo addAttendanceManagementPic(AttendanceManagementPic bean) {
        // 删除状态
        bean.setIs_del(String.valueOf(0));
        // 创建时间
        bean.setCreate_time(new Date());
        // 创建者
        bean.setCreate_user(sysUserService.getUser().getId());
        insert(bean);
        return ResultInfo.success();
    }

    /**
     * 编辑
     */
    public ResultInfo editAttendanceManagementPic(AttendanceManagementPic bean) {
        // 修改者
        bean.setUpdate_user(sysUserService.getUser().getId());
        // 修改时间
        bean.setUpdate_time(new Date());
        updateById(bean);
        return ResultInfo.success();
    }
}
