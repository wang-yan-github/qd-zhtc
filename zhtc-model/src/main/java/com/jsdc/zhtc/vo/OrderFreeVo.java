package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * ClassName: OrderFreeVo
 * Description:资金统计免单统计
 * date: 2022/1/7 11:18
 *
 * @author wh
 **/
@Data
public class OrderFreeVo {
    //免单类型
    private String freeType;
    //免单车牌数
    private Integer carNum;
    //免单金额
    private String freeAmount;
    //免单数量
    private Integer freeNum;
    //占总订单数比例
    private String freeProportion;
}
