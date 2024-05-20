package com.jsdc.zhtc.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;


@Data
public class UploadCarOutDataVo {


    private String uid;
    private String endID;
    private String plateNo;
    private String carType;
    private String parkingCode;
    private String totalBerthNum;
    private String openBerthNum;
    private String freeBerthNum;
    private String arriveTime;
    private String parkingType;
    private String endTime;
    private String entryNum;
    private String outNum;
    private String plateColor;
    private String uploadTime;
    private String chargeFee;
    private String shouldPay;

}
