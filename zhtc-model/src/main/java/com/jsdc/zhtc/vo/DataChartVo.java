package com.jsdc.zhtc.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DataChartVo
 * @Description 数据统计vo
 * @Author zln
 * @Date 2022/1/6 10:55
 * @Version 1.0
 */
@Data
public class DataChartVo implements Serializable {
    //名称
    private String name;
    //金额
    private String amount;
    //退款金额
    private String amount2;
    //年收费总金额
    private String yearAmount;
    //日收费金额
    private String dayAmount;
    //报警次数
    private String bjAmount;
    //总车位
    private String totalPark;
    //空余车位
    private String freePark;
    //停车收费率
    private String chargeRate;
}
