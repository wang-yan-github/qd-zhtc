package com.jsdc.zhtc.controller;


import com.jsdc.zhtc.model.RuleConfiguration;
import com.jsdc.zhtc.service.RuleConfigurationService;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 车主管理
 */
@Controller
@RequestMapping("/RuleConfiguration")
public class RuleConfigurationController {

    @Autowired
    private RuleConfigurationService service;


    /**
     * 分页查询
     */
    @PostMapping(value = "/getAll")
    @ResponseBody
    public ResultInfo getAll(@RequestBody PageVo<RuleConfiguration> data) {
        ResultInfo json = ResultInfo.success(service.selectAll(data));
        return json;
    }


    /**
     * 根据id查询
     */
    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(@RequestBody RuleConfiguration bean) {
        ResultInfo json = service.selectById(bean);
        return json;
    }

    /**
     * 新增数据
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo save(@RequestBody RuleConfiguration bean) {
        ResultInfo json = service.saveData(bean);
        return json;
    }

    /**
     * 更新数据
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo update(@RequestBody RuleConfiguration bean) {
        ResultInfo json = service.updateInfo(bean);
        return json;
    }

    /**
     * 删除数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo delete(@RequestBody RuleConfiguration bean) {
        ResultInfo json = service.deleById(bean);
        return json;
    }

}
