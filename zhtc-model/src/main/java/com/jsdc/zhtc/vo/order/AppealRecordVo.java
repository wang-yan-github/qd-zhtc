package com.jsdc.zhtc.vo.order;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: AppealRecordVo
 * Description:
 * date: 2022/1/25 15:02
 *
 * @author zonglina
 */
@Data
public class AppealRecordVo {

    //路段、停车场ID
    private String id;
    //驶离时间
    private Date endTime;
    //车牌id
    private Integer car_id;

    private Integer type;

    private Integer appealId;

    public AppealRecordVo() {
    }

    public AppealRecordVo(String id, Date endTime) {
        this.id = id;
        this.endTime = endTime;
    }

    public AppealRecordVo(String id) {
        this.id = id;
    }

    public AppealRecordVo(String id, Date endTime, Integer car_id) {
        this.id = id;
        this.endTime = endTime;
        this.car_id = car_id;
    }
}
