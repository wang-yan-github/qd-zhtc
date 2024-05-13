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
 * ClassName: InspectFeedback
 * Description:申诉通知反馈
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("appeal_notice_feedback")
@Table(name = "appeal_notice_feedback")
@DynamicInsert
@DynamicUpdate
@Data
public class AppealNoticeFeedback extends Model<AppealNoticeFeedback> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 停车订单号
    private Integer parking_order_id;
    // 路段||停车场id
    private Integer rp_id;
    // 巡检员id
    private Integer inspect_id;
    // 申诉管理id
    private Integer operate_appeal_id;
    // 通知内容
    private String notice_content;
    // 反馈内容（按结束时间处理、按订单费用处理、按退款处理、按订单状态处理、按修正车牌处理）
    private String feedback_content;
    // 通知时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date notice_time;
    // 反馈时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date feedback_time;
    // 状态（1、通知，2、已反馈）
    private String status;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    //创建人
    private Integer create_user;
    //更新时间
    private Date update_time;
    // 更新人
    private Integer update_user;
    // 是否删除
    private Integer is_del;
    //类型 ：0路段 1停车场
    private String type;

    @TableField(exist = false)
    @Transient
    // 路段名
    public String road_name;
    // 巡检名称
    @TableField(exist = false)
    @Transient
    public String inspect_name;
}
