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
 * ClassName: AppealHandleRecord
 * Description:申诉处理记录表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("appeal_handle_record")
@Table(name = "appeal_handle_record")
@DynamicInsert
@DynamicUpdate
@Data
public class AppealHandleRecord extends Model<AppealHandleRecord> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //申诉订单id
    private Integer operate_appeal_id;
    //停车订单号
    private Integer parking_order_id;
    //处理状态（通过、驳回）
    private String handle_status;
    //驳回原因
    private String reject_reason;
    //处理类型(1按结束时间处理、2按订单费用处理、4按退款处理、按订单状态处理、5按修正车牌处理)
    private String handle_type;
    //结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end_time;
    //订单费用
    private String order_amount;
    //退款金额
    private String refund_amount;
    //订单状态（在停、预约驶离、僵尸车、待缴费、待罚单、已罚单、坏账、已缴费、申请发票、已完成）
    private String order_status;
    //正确车牌(关联车牌的id)
    private String carno_id;
    //1待处理 2通过 3驳回 4已审核
    private String approve_status;
    //车牌类型（蓝牌、绿牌、黄牌）
    private String carno_type;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    //创建人
    private Integer create_user;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    // 更新人
    private Integer update_user;
    //是否领取发票
    private String is_invoice;
    // 是否删除
    private Integer is_del;
    @Transient
    @TableField(exist = false)
    private String approve_status_name;

}
