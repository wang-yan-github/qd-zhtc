package com.jsdc.zhtc.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.service.PaymentOrderService;
import com.jsdc.zhtc.vo.PaymentOrderVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * @author 王严
 * @version 1.0
 * @description: 支付订单管理
 */
@Controller
@RequestMapping("/paymentOrder")
public class PaymentOrderController extends BaseController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    /**
     * 查询查看
     */
    @RequestMapping("/queryPaymentOrder.json")
    @ResponseBody
    public ResultInfo queryPaymentOrder(@RequestParam(defaultValue = "1") Integer pageIndex,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        PaymentOrderVo vo) {
        return paymentOrderService.listPaymentOrder(pageIndex, pageSize, vo);
    }

    /**
     * 联合支付
     */
    @RequestMapping("/jointPayment.json")
    @ResponseBody
    public ResultInfo jointPayment(PaymentOrderVo vo) {
        return paymentOrderService.listRoadParkingOrder(vo);
    }

    /**
     * 支付流水明细分页查询
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer pageIndex,
                             @RequestParam(defaultValue = "20") Integer pageSize,
                             PaymentOrderVo vo) {
        PageInfo<PaymentOrder> page = paymentOrderService.toList(pageIndex, pageSize, vo);
        return ResultInfo.success(page);
    }

    /**
     * 支付流水明细详情
     */
    @RequestMapping(value = "toView.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toView(PaymentOrderVo vo) {
        return ResultInfo.success(paymentOrderService.toView(vo));
    }

    /**
     * 支付流水明细导出Excel
     */
    @RequestMapping("exportPaymentOrder.do")
    public void exportPaymentOrder(@RequestBody PaymentOrderVo vo, HttpServletResponse response) {
        paymentOrderService.exportPaymentOrder(vo, response);
    }
}
