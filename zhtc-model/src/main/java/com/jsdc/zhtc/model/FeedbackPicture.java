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
 * ClassName: OperatePicture
 * Description:反馈图片表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("feedback_picture")
@Table(name = "feedback_picture")
@DynamicInsert
@DynamicUpdate
@Data
public class FeedbackPicture extends Model<FeedbackPicture> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //反馈表id
    private Integer feedback_id;
    //图片id
    private Integer picture_id;

    public FeedbackPicture() {
    }

    public FeedbackPicture(Integer feedback_id, Integer picture_id) {
        this.feedback_id = feedback_id;
        this.picture_id = picture_id;
    }
}
