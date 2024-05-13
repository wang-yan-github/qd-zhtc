package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.RechargeActivityDao;
import com.jsdc.zhtc.mapper.RechargeActivityMapper;
import com.jsdc.zhtc.model.RechargeActivity;
import com.jsdc.zhtc.model.RechargeActivityConfig;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.vo.ResultInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ClassName: RechargeActivityService
 * Description:
 * date: 2021/12/30 10:55
 *
 * @author wh
 */
@Service
@Transactional
public class RechargeActivityService extends BaseService<RechargeActivityDao, RechargeActivity> {
    @Autowired
    private RechargeActivityMapper rechargeActivityMapper;
    @Autowired
    private RechargeActivityConfigService rechargeActivityConfigService;
    @Autowired
    private SysUserService userService;


    /**
     * create by wh at 2022/1/4 1:30
     * description: 新增充值活动
     *
     * @param rechargeActivity
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    @Transactional
    public ResultInfo add(RechargeActivity rechargeActivity) {
//        if(!verify(rechargeActivity)){
//            return ResultInfo.error("必填字段不能为空！");
//        }
        SysUser sysUser = userService.getUser();
        rechargeActivity.setCreate_time(new Date());
        rechargeActivity.setCreate_user(sysUser.getId());
        rechargeActivity.setUpdate_time(new Date());
        rechargeActivity.setUpdate_user(sysUser.getId());
        rechargeActivity.setIs_del(GlobalData.ISDEL_NO);
        rechargeActivity.setStatus("0");
        if (insert(rechargeActivity) > 0) {
            rechargeActivityConfigService.addMap(rechargeActivity.getId(), rechargeActivity.getRechargeActivityConfigs());
            return ResultInfo.success("新增充值活动成功！");
        } else {
            return ResultInfo.error("新增充值活动失败！");
        }
    }

    /**
     * create by wh at 2022/1/4 1:30
     * description: 修改充值活动信息
     *
     * @param rechargeActivity
     * @return com.jsdc.zhtc.vo.ResultInfo
     */
    public ResultInfo edit(RechargeActivity rechargeActivity) {
//        if(!verify(rechargeActivity)){
//            return ResultInfo.error("必填字段不能为空！");
//        }
        SysUser sysUser = userService.getUser();
        rechargeActivity.setUpdate_user(sysUser.getId());
        rechargeActivity.setUpdate_time(new Date());
        if (updateById(rechargeActivity) > 0) {
            if (rechargeActivity.getRechargeActivityConfigs() != null) {
                rechargeActivityConfigService.editMap(rechargeActivity.getId(), rechargeActivity.getRechargeActivityConfigs());
            }
            return ResultInfo.success("修改充值活动成功！");
        } else {
            return ResultInfo.error("修改充值活动失败！");
        }
    }

    public ResultInfo toDelAll(String ids) {
        List<String> areaList = Arrays.asList(ids.split(","));
        UpdateWrapper<RechargeActivity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().set(RechargeActivity::getIs_del, GlobalData.ISDEL_YES)
                .in(RechargeActivity::getId, areaList);
        if (update(null, wrapper) > 0) {
            return ResultInfo.success("删除充值活动成功！");
        } else {
            return ResultInfo.error("删除充值活动失败！");
        }
    }

    /**
     * create by wh at 2022/1/4 2:30
     * description: 根据id查询
     *
     * @param id
     * @return RechargeActivity
     */
    public RechargeActivity getById(Integer id) {
        QueryWrapper<RechargeActivity> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        queryWrapper.eq("is_del", 0);
        RechargeActivity rechargeActivity = selectOne(queryWrapper);
        QueryWrapper<RechargeActivityConfig> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("recharge_activity_id", id).eq("is_del", 0);
        List<RechargeActivityConfig> rechargeActivityConfigs = rechargeActivityConfigService.selectList(queryWrapper2);
        rechargeActivity.setRechargeActivityConfigs(rechargeActivityConfigs);
        return rechargeActivity;
    }

    /**
     * create by wh at 2022/1/4 2:30
     * description: 分页查询
     *
     * @param rechargeActivity
     * @param pageIndex
     * @param pageSize
     * @return PageInfo
     */
    public PageInfo<RechargeActivity> toList(Integer pageIndex, Integer pageSize, RechargeActivity rechargeActivity) {
        PageHelper.startPage(pageIndex, pageSize);
        QueryWrapper<RechargeActivity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(rechargeActivity.getActivity_name())) {
            queryWrapper.like("activity_name", rechargeActivity.getActivity_name());
        }
        queryWrapper.eq("is_del", 0);
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByAsc("start_time");
        List<RechargeActivity> rechargeActivities = selectList(queryWrapper);
        PageInfo<RechargeActivity> page = new PageInfo<>(rechargeActivities);
        return page;
    }


}
