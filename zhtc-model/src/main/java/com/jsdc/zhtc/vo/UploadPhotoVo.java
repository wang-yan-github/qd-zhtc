package com.jsdc.zhtc.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;


@Data
public class UploadPhotoVo {


    private String uid;
    private String file;
    private String parkingCode;
    private String photoID;
    private String time;
    private String type;
    private String name;
    private String endTime;
    private String entryNum;
    private String outNum;
    private String plateColor;
    private String uploadTime;
    private String chargeFee;
    private String shouldPay;

}
