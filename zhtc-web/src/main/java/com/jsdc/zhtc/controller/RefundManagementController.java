package com.jsdc.zhtc.controller;


import com.jsdc.zhtc.model.RefundManagement;
import com.jsdc.zhtc.service.RefundManagementService;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.PageVo;
import com.jsdc.zhtc.vo.RefundManagementVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 类 名: 退款管理
 * 描 述: RefundManagementController
 * 作 者: lw
 * 创 建：2021/12/31 10:14
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Controller
@RequestMapping("/refundmanagement")
public class RefundManagementController {

    @Autowired
    private RefundManagementService service;

    /**
     * 描 述： TODO(分页查新退款信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param data
     * @return {@link ResultInfo}
     */
    @PostMapping(value = "/selectAll")
    @ResponseBody
    public ResultInfo selectAll(@RequestBody CommonVo data) {

        ResultInfo json = service.selectAll2(data);
        return json;
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
    public ResultInfo getAll(@RequestBody PageVo<RefundManagement> data) {
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
    public ResultInfo getById(@RequestBody RefundManagement bean) {
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
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo save(@RequestBody RefundManagement bean) {
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
    public ResultInfo update(@RequestBody RefundManagement bean) {
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
    public ResultInfo delete(@RequestBody RefundManagement bean) {
        ResultInfo json = service.deleById(bean);
        return json;
    }


    /**
     * 退款记录导出
     *
     * @return
     */
    @RequestMapping(value = "/exportRefund.do", method = RequestMethod.POST)
    @ResponseBody
    public void exportRefund(CommonVo vo, HttpServletResponse response) {
        service.exportRefund(vo, response);
    }


    /**
     * 支付流水明细关联退款记录
     *
     * @author thr
     */
    @PostMapping(value = "/getRefundDataByPaymentId.do")
    @ResponseBody
    public ResultInfo getRefundDataByPaymentId(RefundManagementVo vo) {
        return ResultInfo.success(service.getRefundList(vo));
    }
}
