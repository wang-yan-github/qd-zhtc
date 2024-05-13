package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsdc.zhtc.model.ParkingOrder;
import lombok.Data;

import java.util.Date;

/**
 * @Author libin
 * @create 2022/1/29 8:45
 */
@Data
public class ParkingOrderIDetailsVo extends ParkingOrder {

    private String prType; //业务类型
    private String prName;//停车场/路段
    private String carNo;//车牌号码
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveinTime;//开始日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveoutTime;//结束日期
    private String carOwner;//车主
    private String phone;//联系电话
    private String enterpriseName;//企业

    // 路段
    private Integer road_id;

    private String start_time;

    private String end_time;

    private boolean isPark;//true 停车场  false路段

}
