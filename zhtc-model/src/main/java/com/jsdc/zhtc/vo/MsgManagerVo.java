package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * ClassName: MsgManagerVo
 * Description:
 * date: 2022/2/16 11:55
 *
 * @author bn
 */
@Data
public class MsgManagerVo {

    // 身份 停车场管理员 0，巡检员路段 1，巡检员停车场 2
    private String card_no;

    // 申诉订单数量
    private Integer operate_order_num;

    // 申诉车牌数量
    private Integer operate_car_no_num;

    // 车主反馈建议数量
    private Integer operate_feedback_num;

    // 巡检员上报数量
    private Integer inspect_feedback_num;

    // 停车场id
    private Integer park_id;
    // （0:路段，1 ：停车）
    private String model;

}
