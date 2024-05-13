package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * 统计dataVo
 */
@Data
public class ReportVo {

    //0总金额 1包月、2会员充值、3停车订单支付、4商家充值 5退款金额
    private String amount;
    private String amount1;
    private String amount2;
    private String amount3;
    private String amount4;
    private String amount5;

    private String name;
    private Integer ztCount;
    private Integer counts;
    private String type;
    private Integer placeId;
    private String placeName;

}
