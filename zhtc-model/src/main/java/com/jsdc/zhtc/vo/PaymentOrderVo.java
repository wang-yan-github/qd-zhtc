package com.jsdc.zhtc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王严
 * @version 1.0
 * @description: 支付订单管理
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderVo {
    /**
     * 支付订单号
     */
    private Integer paymentId;
    /**
     * 停车场 park 表主键
     */
    private Integer park_id;
    /**
     * 停车区类型 0：路段 1：停车场
     */
    public String parkingType;
    /**
     * 支付订单号/支付流水号
     */
    private String paymentNoOrSerialNo;
    /**
     * 区域id
     */
    private Integer areaId;
    /**
     * 街道id
     */
    private Integer streetId;
    /**
     * 路段id
     */
    private Integer roadId;
    /**
     * 支付方式(微信、支付宝、账户支付、现金)
     */
    private String paymentType;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 支付来源(1包月、2会员充值、3停车订单支付、4商家充值 24充值（2会员充值+4商家充值）)
     */
    private String paymentResource;
    /**
     * 支付状态 1待支付 2已支付 3支付失败
     */
    private String status;
    /**
     * 支付订单号
     */
    private String payment_no;
    /**
     * 支付流水号
     */
    private String payment_serialno;

    /**
     * 是否退款 空为否 1是
     */
    private String is_refund;

}
