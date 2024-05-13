package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.common.aop.logaop.LogInfo;
import com.jsdc.zhtc.enums.LogEnums;
import com.jsdc.zhtc.model.InvoicesManagement;
import com.jsdc.zhtc.service.InvoicesManagementService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author 王严
 * @version 1.0
 * @description: 发票管理
 */
@Controller
@RequestMapping("/invoicesManagement")
public class InvoicesManagementController extends BaseController {

    @Autowired
    private InvoicesManagementService invoicesManagementService;

    /**
     * 查询查看
     *
     * @param pageIndex
     * @param pageSize
     * @param
     * @return
     */
    @RequestMapping("/queryInvoicesManagement.json")
    @ResponseBody
    public ResultInfo queryInvoicesManagement(@RequestParam(defaultValue = "1") Integer pageIndex,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              String phone, String invoice_mode) {
        PageInfo pageInfo = invoicesManagementService.listInvoicesManagement(pageIndex, pageSize, phone, invoice_mode);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 开票审核
     *
     * @param
     * @return
     */
    @RequestMapping("/invoicingApproval.json")
    @ResponseBody
    @LogInfo(LogEnums.LOG_INVOICECASH)
    public ResultInfo invoicingApproval(@RequestParam(defaultValue = "0") String id) {
        InvoicesManagement invoicesManagement = invoicesManagementService.selectById(id);
        if (invoicesManagement != null && !"1".equals(invoicesManagement.getInvoice_mode())) {
            invoicesManagement.setInvoice_mode("1");
            invoicesManagement.setUpdate_time(new Date());
            invoicesManagement.updateById();
            String logMsg = invoicesManagement.getReceiver() + "的发票申请,已成功开票";
            return ResultInfo.success("开票审核完成", logMsg);
        } else {
            return ResultInfo.error("该发票已开票完成！");
        }
    }

    /**
     * 发票详情
     *
     * @param invoicesId
     * @param parkingType
     * @return
     */
    @RequestMapping(value = "getInvoiceDetails.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getInvoiceDetails(String invoicesId, String parkingType) {
        return invoicesManagementService.invoiceDetailsById(invoicesId, parkingType);
    }
}
