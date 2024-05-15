package com.jsdc.zhtc.vo;

import lombok.Data;


@Data
public class PayVo {

    //返回提醒
    private String msg;
    //返回值
    private Integer code;
    //支付订单id
    private Integer pay_id;
    //支付金额
    private String payMoney;

    public PayVo() {
    }

    public PayVo(Integer code, String msg, String payMoney) {
        this.code = code;
        this.msg = msg;
        this.payMoney = payMoney;
    }

    public PayVo(Integer code, String msg, Integer pay_id, String payMoney) {
        this.msg = msg;
        this.code = code;
        this.pay_id = pay_id;
        this.payMoney = payMoney;
    }
}
