package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.model.WxPayConfig;
import com.jsdc.zhtc.service.WxPayConfigService;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 类 名: 微信支付配置
 * 描 述: WxPayConfigContrller
 * 作 者: lw
 * 创 建：2021/12/31 10:14
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Controller
@RequestMapping("/wxpayconfig")
public class WxPayConfigController {

    @Autowired
    private WxPayConfigService service;


    /**
     * 描 述： TODO(分页查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return json
     */
    @PostMapping(value = "/getall")
    @ResponseBody
    public ResultInfo getAll(@RequestBody PageVo<WxPayConfig> data) {
        ResultInfo json = service.selectAll(data);
        return json;
    }


    /**
     * 描 述： TODO(根据id查询)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "/getbyid", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(@RequestBody WxPayConfig bean) {
        ResultInfo json = service.selectById(bean);
        return json;
    }

    /**
     * 描 述： TODO(新增数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo save(@RequestBody WxPayConfig bean) {
        ResultInfo json = service.saveData(bean);
        return json;
    }

    /**
     * 描 述： TODO(更新数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo update(@RequestBody WxPayConfig bean) {
        ResultInfo json = service.updateInfo(bean);
        return json;
    }

    /**
     * 描 述： TODO(删除数据)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param bean
     * @return json
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo delete(@RequestBody WxPayConfig bean) {
        ResultInfo json = service.deleById(bean);
        return json;
    }


}
