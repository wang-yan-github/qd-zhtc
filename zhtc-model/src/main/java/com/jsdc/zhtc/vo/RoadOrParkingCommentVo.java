package com.jsdc.zhtc.vo;

import lombok.Data;


/**
 * ClassName: RoadOrParkingCommentVo <br/>
 * Description: <br/>
 * date: 2022/1/4 9:18<br/>
 * 路段停车场公用字段，查询用
 *
 * @author zln
 */
@Data
public class RoadOrParkingCommentVo {

    private Integer id;
    //申诉状态
    private String appeal_status;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //车牌号
    private String car_no;
    //手机号
    private String phone;
    //订单号
    private String order_no;
    //标识黄牌是否勾选
    private Integer checked;
    //车牌号id
    private String car_id;
    //关键字
    private String keys;
    //停车场id
    private Integer park_id;
    // 申诉类型
    private String appeal_type;
    // 停车状态
    private String status;

    // 停车状态 in 赛选条件
    private String statusIn;

    /**
     * 是否领取发票
     */
    private String is_invoice;
    /**
     * 应收金额
     */
    private String sum_amount;
    /**
     * 巡检员id
     */
    private Integer inspect_id;

    /**
     * 停车区类型 0：路段 1：停车场
     */
    public String parking_type;
    /**
     * 分配路段Ids
     */
    private String roadIds;

    private String userType;

    /**
     * 开单巡检人
     */
    public RoadOrParkingCommentVo() {
    }

    public RoadOrParkingCommentVo(Integer id) {
        this.id = id;
    }
}
