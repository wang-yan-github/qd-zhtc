package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * ClassName: OrderRechargeGrowthVo
 * Description:订单统计充值增长
 * date: 2022/1/7 10:51
 *
 * @author wh
 **/
@Data
public class OrderRechargeGrowthVo {
    //时间
    private String time;
    //充值金额
    private String rechargeAmount;
    //笔数
    private Integer num;
    //同比
    private String tongbi;
}
