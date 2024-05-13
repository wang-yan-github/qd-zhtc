package com.jsdc.zhtc.payPolymerize.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @projectName: zhtc
 * @className: RefundDto
 * @author: wp
 * @description:
 * @date: 2023/5/25 10:07
 */
@Data
@Builder
public class RefundDto {

    private String msgId;
    private String requestTimestamp;
    private String srcReserve;
    private String mid;
    private String tid;
    private String instMid;
    private String billNo;
    private String billDate;
    private String refundOrderId;
    private Integer refundAmount;
    private Integer platformAmount;
}
