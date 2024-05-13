package com.jsdc.zhtc.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommonVo<T> {
    // 页面分类 1 平台概述
    private Integer pageType;
    // 对象
    private T bean;
    //是否是会员 0 不是 1 是
    private Integer is_member;
    //驶入时间
    private String drivein_time;
    //车位编号
    private String berth_code;
    //id
    private Integer id;
    //页大小
    private Integer pageSize = 10;
    //页码
    private Integer pageNum;
    // 起始时间
    private String startTime;
    // 截止时间
    private String endTime;
    private String name;
    //车牌
    private String carNo;
    private String[] carNoArray[];
    //订单号
    private String orderNo;
    //手机号
    private String phone;
    private String carNos;
    private String carTypes;
    //是否公司1个人  2公司
    private String isTheCompany;
    //公司ID
    private Integer companyId;
    //公司名称
    private String company_name;
    //车牌类型
    private String carType;
    // 经度
    private Double longitude;
    // 纬度
    private Double latitude;
    //停车场id
    private Integer parkId;
    // 金额
    private String amount;
    //路段id
    private Integer roadId;
    private Integer area_id;
    private Integer street_id;
    //巡检员负责路段id
    private String roadIds;

    private String monthly_code;

    /**
     * 停车区类型 0：路段 1：停车场 2:场站
     */
    private String parking_type;


    // 枚举参数String
    private String str;
    private String str2;
    private String str3;
    // 枚举参数int
    private Integer variance;
    private Integer variance2;
    private Integer variance3;

    //月数
    private Integer months;

    // 枚举参数boolean
    private boolean flag;
    // 枚举参数boolean
    private boolean flag2;

    private List<Integer> listInt;

    /**
     * 支付方式(1包月 2微信 3支付宝 4钱包 5现金  6银行卡)
     */
    private String payment_type;


    //单位名称
    private String dwName;

    public CommonVo() {
    }

    public CommonVo(String carNo, String str, Integer variance) {
        this.carNo = carNo;
        this.str = str;
        this.variance = variance;
    }

    //转账流水号
    private String transferSerialNumber;

    private String sbstatu;//设备状态
}
