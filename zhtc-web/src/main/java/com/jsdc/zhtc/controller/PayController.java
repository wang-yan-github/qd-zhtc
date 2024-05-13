package com.jsdc.zhtc.controller;


import com.jsdc.zhtc.pay.AlPay;
import com.jsdc.zhtc.utils.pay.HttpsPostUtil;
import com.jsdc.zhtc.vo.AlWxOrder;
import com.jsdc.zhtc.vo.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 类 名: 回调demo
 * 描 述: PayController
 * 作 者: lw
 * 创 建：2022/1/4 11:33
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private AlPay alPay;

    Logger logger = LoggerFactory.getLogger(PayController.class);

    @RequestMapping(value = "/alpay")
    public ResultInfo alpay(HttpServletRequest request) throws Exception {
        String agent = request.getHeader("user-agent");
        AlWxOrder alWxOrder = new AlWxOrder();
        alPay.wapPay(alWxOrder);
        return ResultInfo.success(alWxOrder.getAlWxPayInfo());
    }

    @RequestMapping(value = "/wxPayNotify.do")
    public ResultInfo wxPayNotify(HttpServletRequest request) {
        String readData = HttpsPostUtil.readData(request);
        System.out.println("收到微信回调接口数据：" + readData);
        logger.info("微信支付成功回调pay/wxPayNotify.do方法接口数据：" + readData);
        logger.info("微信支付成功回调pay/wxPayNotify.do方法");
        return ResultInfo.success();
    }


}
