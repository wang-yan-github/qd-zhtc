package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: LargeArrearsVo
 * Description:大额欠费
 * date: 2022/1/7 1:49
 *
 * @author wh
 **/
@Data
public class LargeArrearsVo {
    //车牌号id
    private Integer carNoId;
    //车牌号
    private String carNo;
    //最近的驶入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inTime;
    //最近的驶出时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outTime;
    //欠费总额(元)
    private String totalArrears;
    //车主姓名
    private String name;
    //	车主电话
    private String phone;
}
