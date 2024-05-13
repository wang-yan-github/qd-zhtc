package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.*;
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
 * 类 名: 包月管理
 * 描 述: MonthlyManagement
 * 作 者: lw
 * 创 建：2022/1/4 10:11
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Entity
@TableName("monthly_management")
@Table(name = "monthly_management")
@DynamicInsert
@DynamicUpdate
@Data
public class MonthlyManagement extends Model<MonthlyManagement> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    //主键
    private Integer id;
    //姓名
    private String name;
    //手机号码
    private String phone;
    //车牌ID
    private Integer carno_id;
    //是否公司1个人  2公司
    private String isTheCompany;
    //公司ID
    private Integer companyId;
    //单位名称
    private String dwName;
    //支付订单号
    private Integer payment_id;
    //包月配置ID
    private Integer paymonthly_config_id;
    //月数
    private Integer months;
    //起始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start_time;
    //结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end_time;
    //总费用
    private String total_cost = "0";

    // 办理段类型 0 运营端 1 移动端 2 岗亭端
    private String transact_style;
    //包月编号
    private String monthly_code;

    //预付款包月数，此字段用在包月预支付发起阶段，此时尚未支付，支付成功回调后将此字段回写到months，若支付失败则不操作
    private Integer pre_month;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date create_time;

    //创建人
    private Integer create_user;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date update_time;

    // 更新人
    private Integer update_user;

    // 是否删除
    private String is_del;

    //公司包月数量
    private Integer num;

    private String transferSerialNumber;

    @TableField(exist = false)
    @Transient
    private String car_no;

}
