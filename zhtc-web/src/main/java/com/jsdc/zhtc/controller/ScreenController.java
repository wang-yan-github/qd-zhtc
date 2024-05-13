package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.Screen;
import com.jsdc.zhtc.service.ScreenService;
import com.jsdc.zhtc.service.ScreenTaskService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 诱导屏管理
 *
 * @Author thr
 * @create 2022-11-17 14:13:33
 */
@RestController
@RequestMapping("/screen")
public class ScreenController extends BaseController {

    @Autowired
    private ScreenService screenService;
    @Autowired
    private ScreenTaskService screenTaskService;


    /**
     * ID查询
     */
    @RequestMapping(value = "getById.do", method = RequestMethod.POST)
    public ResultInfo getById(Integer id) {
        return screenService.getById(id);
    }

    /**
     * 添加、编辑
     */
    @RequestMapping(value = "saveOrUpd.json", method = RequestMethod.POST)
    public ResultInfo saveOrUpd(@RequestBody Screen screen) {
        return screenService.saveOrUpd(screen);
    }

    /**
     * 发布
     */
    @RequestMapping(value = "fbUpd.json", method = RequestMethod.POST)
    public ResultInfo fbUpd(@RequestBody Screen screen) {
        return screenService.fbUpd(screen);
    }

    @RequestMapping(value = "publishTask.do", method = RequestMethod.POST)
    public ResultInfo publishTask(@RequestBody Screen screen) {
        return screenService.publishTask(screen);
    }

    @RequestMapping(value = "getTaskEntity.do", method = RequestMethod.POST)
    public ResultInfo getTaskEntity(Screen screen) {
        return screenTaskService.getTaskEntity(screen);
    }

    @RequestMapping(value = "getTask.do", method = RequestMethod.POST)
    public ResultInfo getTask(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, Screen screen) {
        PageInfo pageInfo = screenTaskService.getPage(pageIndex, pageSize, screen.getId());
        return ResultInfo.success(pageInfo);
    }

    @RequestMapping(value = "deleteTask.do", method = RequestMethod.POST)
    public ResultInfo deleteTask(Integer id) {
        return screenTaskService.deleteTask(id);
    }
}
