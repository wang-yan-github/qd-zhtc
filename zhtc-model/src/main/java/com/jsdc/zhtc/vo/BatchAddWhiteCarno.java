package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 车牌管理
 * 批量录入车牌
 */
@Data
public class BatchAddWhiteCarno {
    /**
     * 蓝牌集合
     */
    List<String> blueCars;
    /**
     * 黄牌集合
     */
    List<String> yellowCars;
    /**
     * 绿牌集合
     */
    List<String> greenCars;

    /**
     * 车辆类型(1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
     */
    Integer whitelist_type;

    /**
     * 理由说明
     */
    String reason;

    //车主id
    private Integer car_owner_id;

    // 白名单免费类型 1永久 2期限
    private String white_time_type;

    //内部车白名单 免费开始时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date free_time_start;
    //内部车白名单 免费截止时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date free_time_end;

}
