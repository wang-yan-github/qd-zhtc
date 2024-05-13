package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * @author 王严
 * @version 1.0
 * @description: 发票管理
 */
@Data
public class InvoicesManagementVo {
    /**
     * 停车区类型 0：路段 1：停车场
     */
    public String parking_type;
    /**
     * 车牌号
     */
    public String car_no;
    /**
     * 订单号
     */
    public String order_no;
    /**
     * 手机号
     */
    public String phone;
    /**
     * 发票类型 待开票 0 已开票 1
     */
    private String invoice_type;
}
