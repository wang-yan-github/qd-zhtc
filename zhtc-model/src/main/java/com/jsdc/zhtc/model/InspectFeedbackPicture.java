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
import java.util.Date;

/**
 * ClassName: InspectFeedbackPicture
 * Description:巡检反馈图片表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("inspect_feedback_picture")
@Table(name = "inspect_feedback_picture")
@DynamicInsert
@DynamicUpdate
@Data
public class InspectFeedbackPicture extends Model<InspectFeedbackPicture> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 巡检反馈id
    private Integer inspect_feedback_id;
    // 图片id
    private Integer picture_id;
    //创建时间
    private Date create_time;
    //创建人
    private Integer create_user;
    //更新时间
    private Date update_time;
    // 更新人
    private Integer update_user;
    // 是否删除
    private String is_del;
}
