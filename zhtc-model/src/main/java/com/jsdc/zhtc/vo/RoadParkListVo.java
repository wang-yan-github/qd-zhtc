package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ClassName: RoadParkListVo <br/>
 * Description: <br/>
 * date: 2022/1/4 9:18<br/>
 *
 * @author zln
 */
@Data
public class RoadParkListVo {
    //免费类型 0：包月 1：白名单 2：免费时段 3：特殊开闸
    private String free_type;
    //车牌id
    private Integer carId;
    private Integer carId2;
    //车牌号码
    private String carNo;
    //订单号
    private String orderNo;
    //应付金额
    private String sum_amount;
    //优惠金额
    private String discount_amount;
    //多个车牌号
    private List<Integer> car_list;
    //多个车牌号
    private List<String> carno_list;
    //已付金额
    private String paid_amount;
    //车牌类型
    private String car_type;
    //待付金额
    private String unpaid_amount;
    //申诉状态
    private String appeal_status;
    //0、正常 1、遥控  null未离场
    private String operation_type_in;
    //0、正常 1、遥控  null未离场
    private String operation_type_out;
    //路段id||停车场id
    private Integer placeId;
    //路段名称||停车场名称
    private String placeName;
    //驶入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveinTime;
    //驶出时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveoutTime;
    //驶入时间
    private String driveinTimeStr;
    //驶出时间
    private String driveoutTimeStr;
    //状态
    private String status;
    //状态名称
    private String status_name;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    //车主手机号
    private String phone;
    //停车场、路段标识（0.路段1停车场2包月）
    private String type;
    //泊位号
    private String berth;
    //订单id
    private Integer id;
    //停车场id
    private Integer pId;
    //会员id
    private Integer member_id;
    //判断开始时间是否为空
    private Integer timeIsNull;
    //时间类型
    private Integer timeType;
    //停留时间
    private String resitime;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //标识黄牌是否勾选
    private String checked;
    //标识二次识别是否勾选
    private String checked2;
    //车牌类型
    private String carType;
    //关键字
    private String keys;

    private String is_merge;

    //路段停车订单ids
    private String roadOrderIds;
    //停车场订单ids
    private String parkOrderIds;
    //路段停车订单数量
    private Integer roadOrderCount;
    //停车场订单数量
    private Integer parkOrderCount;
    //路段停车订单金额
    private String roadOrderMoney;
    //停车场订单金额
    private String parkOrderMoney;
    //0停车场、1岗亭
    private String userType;

    //支付订单id
    private String payment_id;
    //备注
    private String remarks;
    /**
     * 支付方式(1包月 2微信 3支付宝 4钱包 5现金)
     */
    private String payment_type;
    /**
     * 支付完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pay_time;
    private String payTime;

    //人工放行理由
    private String fxReason;

    /**
     * 商家id
     */
    private Integer business_id;

    /**
     * 商家电话
     */
    private String business_phone;

    /**
     * 商家扣款金额
     */
    private String business_amount;

    //是否享受商家优惠(0是1否)
    private String is_discount;

    //商家优惠金额
    private String discount_money;


    //包月收入
    private String byAmount;
    //进出类型
    private String inOutType;

    public void setSum_amount(String sum_amount) {
        if (StringUtils.isBlank(sum_amount) || !compareTo2(sum_amount, "0")) {
            this.sum_amount = "0.00";
        } else {
            this.sum_amount = new BigDecimal(sum_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setDiscount_amount(String discount_amount) {
        if (StringUtils.isBlank(discount_amount) || !compareTo2(discount_amount, "0")) {
            this.discount_amount = "0.00";
        } else {
            this.discount_amount = new BigDecimal(discount_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setPaid_amount(String paid_amount) {
        if (StringUtils.isBlank(paid_amount) || !compareTo2(paid_amount, "0")) {
            this.paid_amount = "0.00";
        } else {
            this.paid_amount = new BigDecimal(paid_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setUnpaid_amount(String unpaid_amount) {
        if (StringUtils.isBlank(unpaid_amount) || !compareTo2(unpaid_amount, "0")) {
            this.unpaid_amount = "0.00";
        } else {
            this.unpaid_amount = new BigDecimal(unpaid_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public void setDiscount_money(String discount_money) {
        if (StringUtils.isBlank(discount_money) || !compareTo2(discount_money, "0")) {
            this.discount_money = "0.00";
        } else {
            this.discount_money = new BigDecimal(discount_money).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public static boolean compareTo2(Object num1, Object num2) {
        BigDecimal a = new BigDecimal(num1.toString());
        BigDecimal b = new BigDecimal(num2.toString());
        if (a.compareTo(b) > 0) {
            return true;
        }
        return false;
    }
}
