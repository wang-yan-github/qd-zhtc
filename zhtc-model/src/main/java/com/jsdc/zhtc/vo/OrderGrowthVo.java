package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * ClassName: OrderRechargeGrowthVo
 * Description:订单统计订单增长
 * date: 2022/1/7 11:30
 *
 * @author wh
 **/
@Data
public class OrderGrowthVo {
    //时间
    private String time;
    //订单数量
    private Integer orderNum;
    //缴费数量
    private Integer payNum;
    //缴费占比
    private String Proportion;
}
