package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * ClassName: OrderPaymentMethodVo
 * Description:订单统计订单状态占比
 * date: 2022/1/7 11:30
 *
 * @author wh
 **/
@Data
public class OrderStatusVo {
    //订单状态
    private String status;
    //数量
    private Integer num;
    //占比
    private String proportion;
}
