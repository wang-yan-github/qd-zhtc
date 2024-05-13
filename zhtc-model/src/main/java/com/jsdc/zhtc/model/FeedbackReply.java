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
 * ClassName: FeedbackReply
 * Description:反馈回复表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("feedback_reply")
@Table(name = "feedback_reply")
@DynamicInsert
@DynamicUpdate
@Data
public class FeedbackReply extends Model<FeedbackReply> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //反馈表id
    private Integer feedback_id;
    //回复内容
    private String reply_content;
    //回复时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reply_time;

    public FeedbackReply() {
    }

    public FeedbackReply(Integer feedback_id, String reply_content) {
        this.feedback_id = feedback_id;
        this.reply_content = reply_content;
    }
}
