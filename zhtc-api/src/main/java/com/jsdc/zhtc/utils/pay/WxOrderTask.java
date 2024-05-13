package com.jsdc.zhtc.utils.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.PaymentOrder;
import com.jsdc.zhtc.model.WxPayConfig;
import com.jsdc.zhtc.service.PaymentOrderService;
import com.jsdc.zhtc.service.WxPayConfigService;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @projectName: zhtc
 * @className: WxOrderTask
 * @author: wp
 * @description:
 * @date: 2022/11/14 15:08
 */
@Component
public class WxOrderTask {

    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private WxPayConfigService wxPayConfigService;

    /**
     * 查询微信支付订单状态
     */
    //@Scheduled(cron = "*/30 * * * * ?")
    @SneakyThrows
    public void flushWxOrderState() {
        //查询所有待支付的订单
        List<PaymentOrder> staypayList = paymentOrderService.selectList(Wrappers.<PaymentOrder>lambdaQuery()
                .eq(PaymentOrder::getStatus, "1")
                .eq(PaymentOrder::getIs_del, "0")
                .eq(PaymentOrder::getPayment_type, GlobalData.PARKING_ORDER_TYPE_WX));
        WxPayConfig payConfig = wxPayConfigService.selectOne(Wrappers.<WxPayConfig>lambdaQuery().eq(WxPayConfig::getIs_default, 0).eq(WxPayConfig::getIs_del, GlobalData.ISDEL_NO));
        for (PaymentOrder paymentOrder : staypayList) {
            String outTradNo = paymentOrder.getPayment_no();
            String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/" + outTradNo + "?mchid=" + payConfig.getMch_id();
            String postForObject = HttpsPostUtil.doGet("GET", url, "", payConfig);
            System.out.println(postForObject);
            JSONObject result = JSON.parseObject(postForObject);
            if (StringUtils.equals("SUCCESS", result.getString("trade_state"))) {
                paymentOrder.setStatus("2");
                paymentOrder.setUpdate_time(new Date());
                paymentOrder.setPayment_serialno(result.getString("transaction_id"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = format.parse(result.getString("success_time").substring(0, 18).replaceAll("T", " "));
                paymentOrder.setPay_time(date);
                paymentOrderService.updateById(paymentOrder);
            } else if (StringUtils.equals("CLOSED", result.getString("trade_state"))) {
                paymentOrder.setStatus("3");
                paymentOrder.setUpdate_time(new Date());
                paymentOrderService.updateById(paymentOrder);
            }
        }

    }
}
