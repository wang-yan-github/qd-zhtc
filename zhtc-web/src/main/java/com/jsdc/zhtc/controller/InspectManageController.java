package com.jsdc.zhtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.AjaxResult;
import com.jsdc.zhtc.model.InspectManage;
import com.jsdc.zhtc.model.SectionInspector;
import com.jsdc.zhtc.service.FileManageService;
import com.jsdc.zhtc.service.InspectManageService;
import com.jsdc.zhtc.service.SectionInspectorService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.InspectManageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * ClassName: InspectManageController
 * Description:巡检管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Controller
@RequestMapping("/inspectManage")
public class InspectManageController extends BaseController {

    @Autowired
    private InspectManageService manageService;
    @Autowired
    private FileManageService fileManageService;
    @Autowired
    private SectionInspectorService inspectorService;
    @Autowired
    private SysUserService sysUserService;

    //分页查询功能
    @RequestMapping(value = "selectPageList.json", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult selectPageList(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, InspectManageVo bean) {
        List<InspectManageVo> list = manageService.selectPageList(pageIndex, pageSize, bean);
        return AjaxResult.success(new PageInfo<>(list));
    }

    //巡检员下拉框数据
    @RequestMapping(value = "selectList.json", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult selectList() {
        LambdaQueryWrapper<InspectManage> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(InspectManage::getPersonType, sysUserService.redisRoadOrPark());
        queryWrapper.eq(InspectManage::getIs_del, GlobalData.ISDEL_NO);
        List<InspectManage> list = manageService.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    //根据id，获取详情信息
    @RequestMapping(value = "details.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo details(InspectManage bean) {
        JSONObject object = new JSONObject();
        InspectManage manages = manageService.selectById(bean.getId());
        manages.setFileList(fileManageService.selectByFiles(manages.getPicture_id()));
        if (notEmpty(manages)) {
            List<SectionInspector> list = inspectorService.selectByRoadOrParkList(manages.getPersonType(), manages.getId());
            object.put("list", list);
        }
        object.put("manages", manages);
        return ResultInfo.success(object);
    }

    //新增、修改保存方法
    @RequestMapping(value = "save.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo save(InspectManage bean) {
        return manageService.save(bean);
    }

    //删除功能
    @RequestMapping(value = "delete.json", method = RequestMethod.POST)
    @ResponseBody
    public Integer delete(InspectManage bean) {
        bean.setIs_del(1);
        return manageService.updateById(bean);
    }

    //领发票登记
    @RequestMapping(value = "receipt.json", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult receipt(InspectManage bean) {
        return AjaxResult.success();
    }

}
