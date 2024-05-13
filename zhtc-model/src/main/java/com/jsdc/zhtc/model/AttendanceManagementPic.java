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
 * 考勤管理关联图片
 *
 * @Author thr
 * @create 2022-08-24 14:42:07
 */
@Entity
@TableName("attendance_management_pic")
@Table(name = "attendance_management_pic")
@DynamicInsert
@DynamicUpdate
@Data
public class AttendanceManagementPic extends Model<AttendanceManagementPic> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 巡检反馈id
    private Integer attendance_management_id;
    // 图片id
    private Integer picture_id;
    // 图片类型 1上班 2下班
    private String type;
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
