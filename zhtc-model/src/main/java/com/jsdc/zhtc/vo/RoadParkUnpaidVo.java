package com.jsdc.zhtc.vo;

import lombok.Data;

import java.util.List;

/**
 * 类 名: 未支付订单vo
 * 描 述: UnpaidOrderVo
 * 作 者: lw
 * 创 建：2022/1/19 19:05
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class RoadParkUnpaidVo {
    // 订单id
    private Integer id;
    // 会员id
    private String member_id;
    // 订单号
    private String str;
    // 订单类型
    private String park_type;
    //车牌号码
    private String carNo;
    //总费用
    private String total_cost;
    //支付类型 4我的钱包 2微信
    private String payType;
    private String openId;
    //联合查询
    private List<UnpaidOrderVo> voList;

    //是否开闸 0否 1是
    private String isKz;
}
