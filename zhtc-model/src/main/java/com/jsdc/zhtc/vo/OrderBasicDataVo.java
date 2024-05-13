package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * ClassName: OrderBasicDataVo
 * Description:订单统计基础数据
 * date: 2022/1/7 11:28
 *
 * @author wh
 **/
@Data
public class OrderBasicDataVo {
    //城市名称
    private String cityName;
    //区域名称
    private String areaName;
    //路段名称
    private String roadName;
    //订单总量
    private String totalOrders;
    //缴费总量
    private String totalPayment;
}
