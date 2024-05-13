package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.model.RechargeManagement;
import com.jsdc.zhtc.service.RechargeManagementService;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 类 名: 充值管理
 * 描 述: RechargeManagementController
 * 作 者: lw
 * 创 建：2022/1/4 9:40
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Controller
@RequestMapping("/rechargemanagement")
public class RechargeManagementController {

    @Autowired
    private RechargeManagementService service;


    /**
     * 描 述： TODO(充值管理详细信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return json
     */
    @PostMapping(value = "/getRechargeMInfo")
    @ResponseBody
    public ResultInfo getRechargeMInfo(@RequestBody CommonVo data) {
        ResultInfo json = service.getRechargeMInfo(data);
        return json;
    }

    /**
     * 描 述： TODO(充值管理详细信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return json
     */
    @RequestMapping(value = "/downRechargeMInfoExcel")
    public void downRechargeMInfoExcel(@RequestBody CommonVo data, HttpServletResponse response) {
        service.downRechargeMInfoExcel(data, response);
    }

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
    public ResultInfo getAll(@RequestBody PageVo<RechargeManagement> data) {
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
    @RequestMapping(value = "getbyid", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(@RequestBody RechargeManagement bean) {
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
    public ResultInfo save(RechargeManagement bean) {
        ResultInfo json = service.onSave(bean);
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
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo update(@RequestBody RechargeManagement bean) {
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
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo delete(@RequestBody RechargeManagement bean) {
        ResultInfo json = service.deleById(bean);
        return json;
    }

    @RequestMapping("getRechargeRecords.do")
    @ResponseBody
    public ResultInfo getRechargeRecords(@RequestParam(defaultValue = "1") Integer pageIndex,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         Integer memberId) {
        return service.getRechargeRecords(memberId, pageIndex, pageSize);
    }

    /**
     * 充值记录列表
     */
    @RequestMapping(value = "getRechargeDataByPaymentId.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getRechargeDataByPaymentId(RechargeManagement bean) {
        return ResultInfo.success(service.getRechargeDataByPaymentId(bean.getPayment_id()));
    }
}
