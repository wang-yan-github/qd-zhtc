package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.MonthlyManagement;
import com.jsdc.zhtc.model.Park;
import lombok.Data;

import java.util.List;

@Data
public class MonthlyManagementVo extends MonthlyManagement {

    // 车牌ids
    private String carIds;
    // 车牌id
    private String carNo;
    private String carId;
    // 车牌类型
    private String carType;
    //公司名称
    private String company_name;
    // 车牌类型名称
    private String carTypeName;
    // 路名列表
    private String roadNames;
    // 创建人名称
    private String userName;
    // 时间类型 0使用中 1已失效
    private String timeType;
    // 车主id
    private Integer member_id;

    private String paymentType;
    private String paymentTypeName;
    private String paymentNo;
    private String amount;

    private String name;

    private String startTime;

    private String endTime;

    private String createTime;


    /**
     * 包月配置字段
     */
    //配置名称
    private String configName;
    /**
     * 车辆类型 0：小车 1：大车
     */
    private String category;
    /**
     * 停车区类型 0：路段 1：停车场
     */
    private String parking_type;
    /**
     * 价格
     */
    private String price;
    /**
     * 包月时间段(全天00:00-24:00)开始时间
     */
    private String sTime;
    /**
     * 包月时间段(全天00:00-24:00)结束时间
     */
    private String eTime;


    private List<Park> parkList;

    //支付方式 0我的钱包 1微信
    private String payType;

    private Integer num;

    //转账流水号
    private String transferSerialNumber;

    private String openid;

    private List<String> roadNameList;
    private List<String> parkNameList;

    /**
     * 路段或停车场id
     */
    private String parkingIds;

    //包月单价
    private String months_unit_price;

    public void setCarType(String carType) {
        this.carType = carType;
        switch (carType) {
            case "2":
                this.carTypeName = "绿牌";
                break;
            case "3":
                this.carTypeName = "黄牌";
                break;
            default:
                this.carTypeName = "蓝牌";
                break;
        }
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;

        switch (paymentType) {
            case "1":
                this.paymentTypeName = "包月";
                break;
            case "2":
                this.paymentTypeName = "微信";
                break;
            case "3":
                this.paymentTypeName = "支付宝";
                break;
            case "4":
                this.paymentTypeName = "钱包";
                break;
            case "5":
                this.paymentTypeName = "现金";
                break;
            case "6":
                this.paymentTypeName = "银行卡";
                break;
            case "7":
                this.paymentTypeName = "商家支付";
                break;
            case "8":
                this.paymentTypeName = "聚合支付";
                break;
            default:
                this.paymentTypeName = "未知";
                break;
        }

    }
}
