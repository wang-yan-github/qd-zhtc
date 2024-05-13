package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * ClassName: OrderIncomeVo
 * Description: 资金统计收入报表
 * date: 2022/1/7 10:35
 *
 * @author wh
 **/
@Data
public class OrderIncomeVo {
    //区域名称
    private String areaName;
    //街道名称
    private String streetName;
    //路段名称
    private String roadName;
    //应收
    private String receivable;
    //实收
    private String netReceipts;
}
