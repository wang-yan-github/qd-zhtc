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
 * @author 王严
 * @version 1.0
 * @description: 发票管理
 */
@Entity
@TableName("invoices_management")
@Table(name = "invoices_management")
@DynamicInsert
@DynamicUpdate
@Data
public class InvoicesManagement extends Model<InvoicesManagement> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 金额
     */
    private String amount;
    /**
     * 开票/申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date application_time;
    /**
     * 发票类型 字典 0：个人 1：企业
     */
    private String invoice_type;
    /**
     * 发票抬头
     */
    private String invoice_header;
    /**
     * 税号
     */
    private String duty_paragraph;
    /**
     * 地址
     */
    private String address;
    /**
     * 联系方式
     */
    private String contact_info;
    /**
     * 开户行
     */
    private String bank_of_deposit;
    /**
     * 银行账户
     */
    private String bank_account;
    /**
     * 开票状态 0未开票 1已开票
     */
    private String invoice_mode;
    /**
     * 开票人
     */
    private Integer member_id;
    /**
     * 收票人
     */
    private String receiver;
    /**
     * 收票人手机号
     */
    private String receive_phone;
    /**
     * 收票人地址
     */
    private String receive_address;
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

    @TableField(exist = false)
    @Transient
    private String memberName;

    @TableField(exist = false)
    @Transient
    private String phone;
    //订单id
    @TableField(exist = false)
    @Transient
    private Integer orderId;
    //路段停车订单ids
    @TableField(exist = false)
    @Transient
    private String roadOrderIds;
    //停车场订单ids
    @TableField(exist = false)
    @Transient
    private String parkOrderIds;
}
