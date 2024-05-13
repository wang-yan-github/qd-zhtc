package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * ClassName: OrderPaymentMethodVo
 * Description:资金统计缴费方式统计
 * date: 2022/1/7 11:02
 *
 * @author wh
 **/
@Data
public class OrderPaymentMethodVo {
    //缴费方式
    private String paymentMethod;
    //数量
    private Integer num;
    //总金额
    private String totalAmount;
    //占比
    private String proportion;
}
