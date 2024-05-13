package com.jsdc.zhtc.payPolymerize.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @projectName: zhtc
 * @className: RequestQrcode
 * @author: wp
 * @description:
 * @date: 2023/5/23 10:43
 */
@Data
@Builder
public class RequestQrcode {

    private String msgId;

    private String requestTimeStamp;

    private String srcReserve;

    private String mid;

    private String tid;

    private String instMid;

    private String billNo;

    private String billQRCode;

    private String billDate;

    private String billDesc;

    private String billStatus;

    private Integer totalAmount;

    private String notifyId;

    private Boolean divisionFlag;

    private String expireTime;

    private String notifyUrl;

    private String returnUrl;

    private String qrCodeId;

    private String systemId;

    private Good good;

    private Boolean attachRefund;


}
