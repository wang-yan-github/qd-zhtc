package com.jsdc.zhtc.controller;


import com.jsdc.zhtc.model.CarOwner;
import com.jsdc.zhtc.service.CarOwnerService;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 车主管理
 */
@Controller
@RequestMapping("/carOwner")
public class CarOwnerController {

    @Autowired
    private CarOwnerService service;


    /**
     * 分页查询
     */
    @PostMapping(value = "/getall")
    @ResponseBody
    public ResultInfo getAll(@RequestBody PageVo<CarOwner> data) {
        ResultInfo json = ResultInfo.success(service.selectAll(data));
        return json;
    }


    /**
     * 根据id查询
     */
    @RequestMapping(value = "/getbyid", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(@RequestBody CarOwner bean) {
        ResultInfo json = service.selectById(bean);
        return json;
    }

    /**
     * 新增数据
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo save(@RequestBody CarOwner bean) {
        ResultInfo json = service.saveData(bean);
        return json;
    }

    /**
     * 更新数据
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo update(@RequestBody CarOwner bean) {
        ResultInfo json = service.updateInfo(bean);
        return json;
    }

    /**
     * 删除数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo delete(@RequestBody CarOwner bean) {
        ResultInfo json = service.deleById(bean);
        return json;
    }

    /**
     * 车牌管理模板导入
     *
     */
    @RequestMapping("/templateImport")
    @ResponseBody
    public ResultInfo templateImport(MultipartFile file){
        ResultInfo json = service.templateImport(file);
        return json;
    }

    /**
     * 车牌管理导出
     *
     */
    @RequestMapping("/templateExport")
    @ResponseBody
    public ResultInfo templateExport(@RequestBody PageVo<CarOwner> data){
        ResultInfo json = service.templateExport(data);
        return json;
    }
}
