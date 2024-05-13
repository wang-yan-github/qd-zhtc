package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: InvoicesProvideRecord
 * Description: 发票发放记录
 * date: 2022/1/17 11:40
 *
 * @author bn
 */

@Entity
@TableName("invoices_provide_record")
@Table(name = "invoices_provide_record")
@DynamicInsert
@DynamicUpdate
@Data
public class InvoicesProvideRecord extends Model<InvoicesProvideRecord> implements Serializable {


    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 订单号
    private String order_no;
    //订单ID
    private Integer order_id;
    //订单类型 0：路段 1：停车场
    private String parking_type;
    //订单金额
    private String order_amount;
    //发票金额
    private String invoice_amount;
    //巡检员id
    private Integer inspect_id;
    //发放时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date provide_time;

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


}
