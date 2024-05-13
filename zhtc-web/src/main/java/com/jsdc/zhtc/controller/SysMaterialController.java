package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.SysMaterial;
import com.jsdc.zhtc.service.FileManageService;
import com.jsdc.zhtc.service.SysMaterialService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 素材管理
 *
 * @Author thr
 * @create 2022-12-01 10:52:56
 */
@Controller
@RequestMapping("/sysMaterial")
public class SysMaterialController extends BaseController {

    @Autowired
    private SysMaterialService sysMaterialService;
    @Autowired
    private FileManageService fileManageService;


    /**
     * 分页查询
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, SysMaterial beanParam) {
        PageInfo<SysMaterial> page = sysMaterialService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }

    /**
     * ID查询
     */
    @RequestMapping(value = "getById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id) {
        SysMaterial bean = sysMaterialService.selectById(id);
        bean.setFileList(fileManageService.selectByFiles(bean.getFile_id()));
        return ResultInfo.success(bean);
    }

    @RequestMapping(value = "saveOrUpd.json", method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(value = LogEnums.LOG_RESOURCE)
    public ResultInfo saveOrUpd(@RequestBody SysMaterial bean) {
        return sysMaterialService.edit(bean);
    }

    /**
     * 批量删除
     */
    @RequestMapping("delAll.do")
    @ResponseBody
    public ResultInfo delAll(String ids) {
        return sysMaterialService.delAll(ids);
    }
}
