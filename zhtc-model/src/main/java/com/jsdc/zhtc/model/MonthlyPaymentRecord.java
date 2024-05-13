package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 类 名: 包月管理和支付订单关联表
 * 描 述: MonthlyPaymentRecord
 * 作 者: thr
 * 创 建：2022/2/23 10:11
 */
@Entity
@TableName("monthly_payment_record")
@Table(name = "monthly_payment_record")
@DynamicInsert
@DynamicUpdate
@Data
public class MonthlyPaymentRecord extends Model<MonthlyPaymentRecord> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    //主键
    private Integer id;
    //支付订单id
    private Integer payment_id;
    //包月管理ID
    private Integer monthly_management_id;
    private Integer is_complete;
    private String ext;

}
