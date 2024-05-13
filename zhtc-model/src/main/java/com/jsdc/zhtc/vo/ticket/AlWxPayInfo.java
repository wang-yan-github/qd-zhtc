package com.jsdc.zhtc.vo.ticket;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.model.PaymentOrder;
import lombok.Data;

@Data
public class AlWxPayInfo {

    // 状态 200 正常 201无变化 202其他异常
    private Integer code;
    //说明
    private String msg;
    //错误代码
    private String subCode;

    //wap 调起支付 支付地址
    private String payURL;
    //其它返回值
    private JSONObject jsonData;
    //订单入库信息
    private PaymentOrder paymentOrder;


}
