package com.jsdc.zhtc.mapper;

import lombok.Data;

@Data
public class DeviceStatisticsVo {
    //正常车位数
    String normal;
    //异常车位数
    String abnormal;
    //占用车位数
    String inuse;
    //空闲车位数
    String free;
}
