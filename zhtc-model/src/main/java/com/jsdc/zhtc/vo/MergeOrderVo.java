package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @projectName: zhtc
 * @className: MergeOrderVo
 * @author: wp
 * @description:
 * @date: 2022/6/22 14:49
 */
@Data
public class MergeOrderVo {
    private Integer ordermerge_id;

    private Integer parking_order_id;

    private String orderNo;

    private String roadName;

    private String parkName;

    private String berth;

    private String carNo;

    private String car_type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveinTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveoutTime;

    private String resitime;

    private String sum_amount;

    private String is_merge;

    private String status;
}
