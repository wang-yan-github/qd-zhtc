package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.ScreenParkRoadDao;
import com.jsdc.zhtc.mapper.ScreenParkRoadMapper;
import com.jsdc.zhtc.model.ScreenParkRoad;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author thr
 * @create 2022-11-17 14:13:33
 */
@Service
@Transactional
public class ScreenParkRoadService extends BaseService<ScreenParkRoadDao, ScreenParkRoad> {

    @Autowired
    private ScreenParkRoadMapper screenParkRoadMapper;
    @Autowired
    private SysUserService sysUserService;

    public PageInfo<ScreenParkRoad> toList(Integer pageIndex, Integer pageSize, ScreenParkRoad beanParam) {
        PageHelper.startPage(pageIndex, pageSize);

        List<ScreenParkRoad> screenParkRoadVos = screenParkRoadMapper.toList(beanParam);

        PageInfo<ScreenParkRoad> page = new PageInfo<>(screenParkRoadVos);

        return page;
    }

    public List<ScreenParkRoad> getList(ScreenParkRoad beanParam) {

        List<ScreenParkRoad> screenParkRoadVos = screenParkRoadMapper.toList(beanParam);

        return screenParkRoadVos;
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<ScreenParkRoad> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        ScreenParkRoad screenParkRoad = selectOne(queryWrapper);
        return ResultInfo.success(screenParkRoad);
    }

    /**
     * 添加
     */
    public ResultInfo addScreenParkRoad(ScreenParkRoad bean) {
        // 删除状态
        bean.setIs_del(String.valueOf(0));
        // 创建时间
        bean.setCreate_time(new Date());
        // 创建者
        bean.setCreate_user(sysUserService.getUser().getId());
        insert(bean);
        return ResultInfo.success();
    }

}
