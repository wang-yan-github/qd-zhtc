package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: ArrearsOrderVo
 * Description:欠费订单
 * date: 2022/1/7 1:49
 *
 * @author wh
 **/
@Data
public class ArrearsOrderVo {
    //订单号
    private String orderNo;
    //金额
    private String amount;
    //驶入时间
    private String inTime;
    //驶出时间
    private String outTime;
    //	停车时长
    private String resitime;

    private String unpaidAmount;
    //0 路段 1停车场
    private String type;
    //0 路段 1停车场
    private String typeName;

    private String roadName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveinTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveoutTime;

    private String status;
}
