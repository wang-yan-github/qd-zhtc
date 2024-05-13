package com.jsdc.zhtc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.dao.ScreenTaskDao;
import com.jsdc.zhtc.mapper.ScreenTaskMapper;
import com.jsdc.zhtc.model.Screen;
import com.jsdc.zhtc.model.ScreenTask;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @projectName: zhtc
 * @className: ScreenTaskService
 * @author: wp
 * @description:
 * @date: 2023/3/24 10:47
 */
@Service
public class ScreenTaskService extends BaseService<ScreenTaskDao, ScreenTask> {
    @Autowired
    private ScreenTaskMapper mapper;

    public int save(ScreenTask screenTask) {
        screenTask.setIs_del(GlobalData.ISDEL_NO);
        screenTask.setCreate_time(new Date());
        screenTask.setHas_deal(0);
        return insert(screenTask);
    }

    public PageInfo getPage(Integer pageIndex, Integer pageSize, Integer screenId) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ScreenTask> list = mapper.getPage(screenId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public ResultInfo getTaskEntity(Screen screen) {
        int id = screen.getId();
        ScreenTask task = selectById(id);
        return ResultInfo.success(task);
    }

    public ResultInfo deleteTask(int id) {
        ScreenTask task = selectById(id);
        task.setIs_del(GlobalData.ISDEL_YES);
        updateById(task);
        return ResultInfo.success();
    }
}
