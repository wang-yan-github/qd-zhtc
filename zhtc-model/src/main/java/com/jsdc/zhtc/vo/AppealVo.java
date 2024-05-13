package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: AppealVo
 * Description:
 * date: 2022/1/21 14:49
 *
 * @author wp
 */
@Data
public class AppealVo {
    private String notice_content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date appeal_time;

    private String status;

    private String order_no;

    private Integer pid;

    private Integer aid;

    private Integer oid;

    private String content;

    private String phone;

    /**
     * 车牌号
     */
    private String car_no;
    /**
     * 反馈时间
     */
    private String feedback_time;
    /**
     * 反馈内容
     */
    private String feedback_content;

    private String road_name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drivein_time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveout_time;
}
