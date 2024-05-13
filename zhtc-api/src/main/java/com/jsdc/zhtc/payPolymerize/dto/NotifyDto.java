package com.jsdc.zhtc.payPolymerize.dto;

import lombok.Data;

/**
 * @projectName: zhtc
 * @className: NotifyDto
 * @author: wp
 * @description:
 * @date: 2023/5/24 10:53
 */
@Data
public class NotifyDto {
    private String mid;
    private String tid;
    private String instMid;
    private String billNo;
    private String billQRCode;
    private String billDate;
    private String createTime;
    private String billStatus;
    private String billDesc;
    private Integer totalAmount;
    private String memberId;
    private String counterNo;
    private String merName;
    private String memo;
    private String notifyId;
    private String secureStatus;
    private String completeAmount;
    private String sign;
    private String bankInfo;
    private String bankCardNo;
    private String seqId;
    private Integer receiptAmount;
    private Integer couponMerchantContribute;
    private Integer couponOtherContribute;
    private String extraBuyerInfo;
    private String activityIds;
    private Integer refundAmount;
    private String refundDesc;
    private String mchntUuid;
    private String subInst;
    private String srcReserve;
    private String qrCodeType;
    private String authIdRespCd;
    private String billPayment;
}
