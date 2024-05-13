package com.jsdc.zhtc.vo;

import lombok.Data;

@Data
public class StatisticsVo {
    //设备类型
    private String deviceTypeName;
    //支付类型
    private String payTypeName;
    //路段编号
    private Integer roadId;
    //停车场编号
    private Integer parkId;

    private Integer counts;
    private String sumAmount;
}
