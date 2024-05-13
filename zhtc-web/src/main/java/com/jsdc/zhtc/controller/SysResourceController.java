package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.common.utils.AjaxResult;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.SysResource;
import com.jsdc.zhtc.service.FileManageService;
import com.jsdc.zhtc.service.SysResourceService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: SysResourceController
 * Description: 系统资源
 * date: 2021/12/30 14:00
 *
 * @author wh
 */
@Controller
@RequestMapping("sysResource")
public class SysResourceController extends BaseController {

    @Autowired
    private SysResourceService sysResourceService;
    @Autowired
    private FileManageService fileManageService;

    /**
     * 添加
     *
     * @param sysResource
     * @author wh
     * @returndd
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(SysResource sysResource) {
        return sysResourceService.add(sysResource);
    }

    /**
     * 编辑删除
     *
     * @param bean
     * @author wh
     * @returndd
     */
    @RequestMapping(value = "saveOrUpd.json", method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(value = LogEnums.LOG_RESOURCE)
    public ResultInfo saveOrUpd(@RequestBody SysResource bean) {
        return sysResourceService.edit(bean);
    }

    /**
     * 详情
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "details.json", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult details(SysResource bean) {
        bean = sysResourceService.selectById(bean.getId());
        bean.setFileList(fileManageService.selectByFiles(bean.getPicture_id()));
        return AjaxResult.success(bean);
    }

    /**
     * 列表查询
     *
     * @param pageIndex
     * @param pageSize
     * @param sysResource
     * @return
     * @author wh
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "10") Integer pageSize, SysResource sysResource) {
        List<SysResource> list = sysResourceService.selectPageList(sysResource, pageIndex, pageSize);
        return AjaxResult.success(new PageInfo<>(list));
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("delAll.do")
    @ResponseBody
    @LogInfo(value = LogEnums.LOG_RESOURCE)
    public ResultInfo delAll(String ids) {
        return sysResourceService.delAll(ids);
    }

    /**
     * 常见问题
     * 编辑页面数据
     *
     * @return
     */
    @RequestMapping(value = "questDetails.json", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult questDetails() {
        SysResource bean = new SysResource();
        //类别 0:文档 1：操作指南 2:常见问题 3轮播图 4 公告资讯
        bean.setCategory("2");
        return AjaxResult.success(sysResourceService.selectBean(bean));
    }

}
