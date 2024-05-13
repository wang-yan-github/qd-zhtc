package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsdc.zhtc.model.ParkingOrder;
import com.jsdc.zhtc.model.ParkingOrderPics;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * ClassName: ParkingOrderVo <br/>
 * Description: <br/>
 * date: 2022/1/4 9:18<br/>
 *
 * @author zln<br />
 */
@Data
public class ParkingOrderVo extends ParkingOrder {
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //展示时分
    private String resTime;
    //开始时间str
    private String startDate;
    //结束时间str
    private String endDate;

    //放入停车图片表
    private List<ParkingOrderPics> details;
    //车牌号
    private String car_no;
    //修改标识
    private String direction;
    //ids
    private Integer ids;
    //判断
    private String flag;
    //路段名称
    private String road_name;
    //停车场名称
    private String park_name;
    //状态
    private String state_name;
    //车牌类型
    private String car_type;
    //订单来源
    private String source_type;
    //订单来源
    private String company_name;
    //手机号
    private String phone;
    //标识黄牌是否勾选
    private Integer checked;
    //车牌号id
    private String car_id;
    //记录类型
    private String eventType;
    // 支付时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pay_time;

    // 缴费类型
    private String order_type;

    //  支付通道
    private String pay_channel;

    // 支付终端
    private String pay_terminal;

    // 支付场景
    private String pay_scene;

    // 移动支付编号
    private String payment_serialno;

    // 结算账户类型
    private String account_type;

    // 收费员
    private String inspect_name;

    // 运营商户
    private String shopee;

    // 开始时间

    private String start_time;

    // 结束时间

    private String end_time;

    private String type_name;

    // 车次
    public String car_num;

    // 金额
    private String car_price;

    private Integer mergeId;

    private String parkCode;

    private String outChannelId;

    private String sumAmount;
    private String paidAmount;
    private String unpaidAmount;


}
