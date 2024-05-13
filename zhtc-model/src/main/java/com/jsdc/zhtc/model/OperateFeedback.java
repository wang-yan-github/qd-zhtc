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
import java.util.List;

/**
 * ClassName: OperateFeedback
 * Description:反馈管理表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("operate_feedback")
@Table(name = "operate_feedback")
@DynamicInsert
@DynamicUpdate
@Data
public class OperateFeedback extends Model<OperateFeedback> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //车牌号
    private String carno;
    //会员
    private Integer member_id;
    //反馈时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date feedback_time;
    //反馈状态 1、待回复 2、已回复
    private Integer feedback_status;
    //反馈内容
    private String feedback_content;
    //创建时间
    private Date create_time;
    //创建人
    private Integer create_user;
    //更新时间
    private Date update_time;
    // 更新人
    private Integer update_user;
    // 是否删除
    private Integer is_del;

    //文件上传ids
    @TableField(exist = false)
    @Transient
    private String files;
    //图片路径
    @TableField(exist = false)
    @Transient
    private List<String> fileList;
    //回复内容列表
    @TableField(exist = false)
    @Transient
    private List<FeedbackReply> replyList;
    //手机号
    @TableField(exist = false)
    @Transient
    private String tel_phone;
}
