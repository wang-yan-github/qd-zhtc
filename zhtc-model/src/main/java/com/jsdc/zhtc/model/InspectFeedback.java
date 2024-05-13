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
 * Description:巡检反馈管理表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("inspect_feedback")
@Table(name = "inspect_feedback")
@DynamicInsert
@DynamicUpdate
@Data
public class InspectFeedback extends Model<InspectFeedback> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 巡检员ID
    private Integer inspect_id;
    // 联系电话
    private String phone;
    // 内容
    private String content;
    // 反馈内容
    private String feedback;
    // 反馈类型
    private String feedback_type;
    // 反馈时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date feedback_time;
    // 是否反馈（0.未反馈1.已反馈）
    private Integer fkstate;
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
    // 是否删除
    private Integer is_del;

    //巡检员名称
    @Transient
    @TableField(exist = false)
    private String name;

    /**
     * 上传文件
     */
    @Transient
    @TableField(exist = false)
    private String imageBase64;
    /**
     * 上传文件名
     */
    @Transient
    @TableField(exist = false)
    private String fileName;
}
