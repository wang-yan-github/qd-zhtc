package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 停车场停车订单
 */
@Entity
@TableName("parking_order")
@Table(name = "parking_order")
@DynamicInsert
@DynamicUpdate
@Data
public class ParkingOrder extends Model<ParkingOrder> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //订单号
    private String order_no;

    //流水号 上传交控使用的唯一标识
    private String uid;

    //订单来源 1视频采集、2人工
    private String source;

    //停车场id
    private Integer park_id;

    //车牌id
    private Integer carno_id;

    //进场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drivein_time;

    //出场时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveout_time;

    //停留时长 分钟
    private Integer resitime;

    //订单状态 1在停、2 待缴费、3已缴费、4已完成
    private String status;

    //应收金额
    private String sum_amount;

    //优惠金额
    private String discount_amount;

    //已付金额/实收金额
    private String paid_amount;

    //待付金额/欠费金额
    private String unpaid_amount;

    //是否享受商家优惠(0是1否)
    private String is_discount;

    //商家优惠金额
    private String discount_money;

    //是否上传交管 0否 1是
    private String is_upload;

    // 免单类型 0:个人包月 1:白名单 2:未达到免费时长 3:特殊开闸 4:企业包月 5:临时通行
    private String free_type;

    //驶入闸机设备编号
    private String drivein_gate;

    //驶出闸机设备编号payment_order
    private String driveout_gate;

    //出入类型 0驶入 1驶出
    private String entry_type;

    //0、正常 1、遥控  null未离场
    private String operation_type_in;

    //0、正常 1、遥控  null未离场
    private String operation_type_out;

    //订单支付方式  字典 1包月、2微信、3支付宝、4钱包、5现金、6银行卡、7商家支付、8聚合支付
    private String pay_type;

    //支付订单号
    private Integer payment_id;

    /**
     * 支付完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pay_time;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    /**
     * 创建人
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    /**
     * 更新人
     */
    private Integer update_user;
    /**
     * 是否删除
     */
    private String is_del;

    /**
     * 2023-12-06 新增字段
     * 备注
     * 定时任务修改后存为【定时任务：订单状态1变更为XX】
     */
    private String remark;

    //是否开闸 0否 1是
    private String isKz;

    //临停驶离截止时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tempDriveOutTime;

    //临时通行证id
    private Integer provisionalPassId;

    //人工放行理由
    private String fxReason;

    @Transient
    @TableField(exist = false)
    private String road_name;

    @Transient
    @TableField(exist = false)
    private PaymentOrder paymentOrder;

    @Transient
    @TableField(exist = false)
    //停留时长 分钟
    private String stayTime;

    @Transient
    @TableField(exist = false)
    private String carNo;


    /**
     * 以下为未使用的字段
     */
//    //区域
//    private Integer area_id;
//
//    //街道
//    private Integer street_id;
//
//    //会员id
//    private Integer member_id;
//
//    //公司id
//    private Integer company_id;
//
//    //开单巡检人
//    private Integer inspect_id;
//    //订单类型
//    private String is_merge;
//
//    //是否领取发票
//    private String is_invoice;
//    //发票ID
//    private Integer invoice_id;
//    //核销状态 0未核销 1已核销
//    private String is_verification;
//
//    //支付途径 1:本系统 2:交控
//    private String payroute;
//
//    //是否为合并订单生成的过程记录订单 1：是  0：否
//    private String middle_record;
//
//    private Integer pre_node;
//
//    //泊位号
//    private String berth;
//
//    //人工开闸类型 1:公务开闸 2：故障开闸 3：临时通行
//    private String artificial_open;
//
//    //发票类型（0：线上、1：线下）
//    private String invoice_type;
}
