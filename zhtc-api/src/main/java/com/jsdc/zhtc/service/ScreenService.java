package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.ScreenDao;
import com.jsdc.zhtc.mapper.ScreenMapper;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.vo.ResultInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 诱导屏管理
 *
 * @Author thr
 * @create 2022-11-17 14:13:33
 */
@Service
@Transactional
public class ScreenService extends BaseService<ScreenDao, Screen> {

    @Autowired
    private ScreenMapper screenMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ScreenParkRoadService screenParkRoadService;

    @Autowired
    private SysMaterialService sysMaterialService;
    @Autowired
    private FileManageService fileManageService;
    @Autowired
    private GuidTaskService guidTaskService;
    @Autowired
    private ScreenTaskService screenTaskService;


    public List<Screen> getList(Screen beanParam) {

        List<Screen> screenVos = screenMapper.toList(beanParam);

        return screenVos;
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<Screen> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        Screen screen = selectOne(queryWrapper);

        //关联路段、停车场
        LambdaQueryWrapper<ScreenParkRoad> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScreenParkRoad::getScreen_id, screen.getId());
        wrapper.eq(ScreenParkRoad::getType, "0");
        List<ScreenParkRoad> roadList = screenParkRoadService.selectList(wrapper);

        LambdaQueryWrapper<ScreenParkRoad> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(ScreenParkRoad::getScreen_id, screen.getId());
        wrapper2.eq(ScreenParkRoad::getType, "1");
        List<ScreenParkRoad> parkList = screenParkRoadService.selectList(wrapper2);

        List<Integer> roadIds = roadList.stream().map(x -> x.getPk_id()).collect(Collectors.toList());
        screen.setRoadIdList(roadIds);

        List<Integer> parkIds = parkList.stream().map(x -> x.getPk_id()).collect(Collectors.toList());
        screen.setParkIdList(parkIds);
        return ResultInfo.success(screen);
    }

    /**
     * 添加、编辑
     */
    public ResultInfo saveOrUpd(Screen bean) {
        if (Base.notEmpty(bean.getId())) {
            // 修改者
            bean.setUpdate_user(sysUserService.getUser().getId());
            // 修改时间
            bean.setUpdate_time(new Date());
            updateById(bean);

            if (bean.getIs_del().equals("0")) {
                //删除关联路段、停车场
                screenParkRoadService.delete(Wrappers.<ScreenParkRoad>lambdaQuery().eq(ScreenParkRoad::getScreen_id, bean.getId()));
            }
        } else {
            // 删除状态
            bean.setIs_del(String.valueOf(0));
            // 创建时间
            bean.setCreate_time(new Date());
            // 创建者
            bean.setCreate_user(sysUserService.getUser().getId());
            bean.setUpdate_time(new Date());
            insert(bean);
        }
        //新增关联路段
        if (Base.notEmpty(bean.getRoadIds())) {
            String[] ids = bean.getRoadIds().split(",");
            for (String str : ids) {
                ScreenParkRoad screenParkRoad = new ScreenParkRoad();
                screenParkRoad.setScreen_id(bean.getId());
                screenParkRoad.setType("0");
                screenParkRoad.setPk_id(Integer.parseInt(str));
                screenParkRoadService.addScreenParkRoad(screenParkRoad);
            }
        }
        //新增关联停车场
        if (Base.notEmpty(bean.getParkIds())) {
            String[] ids = bean.getParkIds().split(",");
            for (String str : ids) {
                ScreenParkRoad screenParkRoad = new ScreenParkRoad();
                screenParkRoad.setScreen_id(bean.getId());
                screenParkRoad.setType("1");
                screenParkRoad.setPk_id(Integer.parseInt(str));
                screenParkRoadService.addScreenParkRoad(screenParkRoad);
            }
        }
        return ResultInfo.success();
    }

    public ResultInfo publishTask(Screen bean) {
        if (null == bean.getId()) {
            return ResultInfo.error("请选择诱导屏");
        }
        ScreenTask screenTask = new ScreenTask();
        screenTask.setScreen_id(bean.getId());
        screenTask.setIs_del(GlobalData.ISDEL_NO);
        screenTask.setMateria_id(bean.getMaterialId());
        screenTask.setStart_time(bean.getStart_time());
        screenTask.setTask_name(bean.getTaskName());
        screenTask.setPlay_type(bean.getPlay_type());
        screenTaskService.save(screenTask);
        return ResultInfo.success();
    }

    /**
     * 发布
     */
    public ResultInfo fbUpd(Screen bean) {

        if (1 == bean.getPlay_type()) {
            if (CollectionUtils.isNotEmpty(bean.getScreenList())) {
                for (Screen screen : bean.getScreenList()) {
                    LambdaUpdateWrapper<GuidTask> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.set(GuidTask::getIs_del, GlobalData.ISDEL_YES)
                            .eq(GuidTask::getPid, screen.getCode());
                    guidTaskService.update(null, updateWrapper);
                    screen.setPlay_type(1);
                    screen.setUpdate_time(new Date());
                    updateById(screen);
                    GuidTask guidTask = new GuidTask();
                    guidTask.setPid(screen.getCode());
                    guidTask.setIs_header(1);
                    guidTask.setCreate_time(new Date());
                    guidTask.setUpdate_time(new Date());
                    guidTask.setIs_del(GlobalData.ISDEL_NO);
                    guidTask.setPlay_state(1);
                    guidTask.setDownload_state(1);
                    guidTaskService.insert(guidTask);
                }
            }
        } else {
            SysMaterial material = sysMaterialService.selectById(bean.getMaterialId());
            FileManage fileManage = fileManageService.selectById(material.getFile_id());
            String fileUrl = fileManage.getFile_url();
            String fileName = fileManage.getFile_name();
            String fileType = material.getType();
            for (Screen screen : bean.getScreenList()) {
                LambdaUpdateWrapper<GuidTask> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(GuidTask::getIs_del, GlobalData.ISDEL_YES)
                        .eq(GuidTask::getPid, screen.getCode());
                guidTaskService.update(null, updateWrapper);
                GuidTask guidTask = new GuidTask();
                guidTask.setPid(screen.getCode());
                guidTask.setFile_url(fileUrl);
                guidTask.setFile_name(fileName);
                guidTask.setFile_type(fileType);
                guidTask.setDownload_state(1);
                guidTask.setPlay_state(1);
                guidTask.setCreate_time(new Date());
                guidTask.setUpdate_time(new Date());
                guidTask.setIs_del(GlobalData.ISDEL_NO);
                guidTask.setIs_header(0);
                guidTaskService.insert(guidTask);
                screen.setPlay_type(2);
                screen.setUpdate_time(new Date());
                updateById(screen);
            }
        }


        return ResultInfo.success();
    }
}
