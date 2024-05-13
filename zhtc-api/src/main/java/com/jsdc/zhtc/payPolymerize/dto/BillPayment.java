package com.jsdc.zhtc.payPolymerize.dto;

import lombok.Data;

/**
 * @projectName: zhtc
 * @className: BillPayment
 * @author: wp
 * @description:
 * @date: 2023/5/24 11:02
 */
@Data
public class BillPayment {
    private String billBizType;
    private Integer invoiceAmount;
    private String merOrderId;
    private String paySeqId;
    private Integer totalAmount;
    private Integer buyerPayAmount;
    private Integer discountAmount;
    private Integer couponAmount;
    private String buyerId;
    private String buyerUsername;
    private String payDetail;
    private String payTime;
    private String settleDate;
    private String status;
    private String targetOrderId;
    private String targetSys;
    private Integer receiptAmount;
    private String authIdRespCd;
}
