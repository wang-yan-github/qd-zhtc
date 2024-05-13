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
 * 考勤管理
 *
 * @Author thr
 * @create 2022-08-24 14:42:07
 */
@Entity
@TableName("attendance_management")
@Table(name = "attendance_management")
@DynamicInsert
@DynamicUpdate
@Data
public class AttendanceManagement extends Model<AttendanceManagement> implements Serializable {
    //主键
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //上班打卡时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start_time;
    //下班打卡时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end_time;

    //上班打卡时间 分配
    private String fp_start_time;
    //下班打卡时间 分配
    private String fp_end_time;

    // 是否迟到 0否 1是
    private String is_late;

    // 是否早退 0否 1是
    private String is_leave_early;

    // 是否旷工 0否 1是
    private String is_absenteeism;

    // 类型 0 路段 1停车场
    private String type;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    //创建人、打卡收费员/巡检员
    private Integer create_user;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    // 更新人
    private Integer update_user;

    // 是否删除
    private String is_del;

    //分配路段、停车场名称
    @Transient
    @TableField(exist = false)
    private String roadParkNames;

    //收费员/巡检员名称
    @Transient
    @TableField(exist = false)
    private String userName;

    //上班打卡时间
    @Transient
    @TableField(exist = false)
    private String sb_start_time;
    @Transient
    @TableField(exist = false)
    private String sb_end_time;
    //下班打卡时间
    @Transient
    @TableField(exist = false)
    private String xb_start_time;
    @Transient
    @TableField(exist = false)
    private String xb_end_time;
    //考勤打卡时间
    @Transient
    @TableField(exist = false)
    private String create_start_time;
    @Transient
    @TableField(exist = false)
    private String create_end_time;

    //打卡类型 1上班、签到 2下班、签退
    @Transient
    @TableField(exist = false)
    private String qdType;

    //按天查询时间
    @Transient
    @TableField(exist = false)
    private String strTime;

    //考勤天数
    @Transient
    @TableField(exist = false)
    private String days;
    //迟到天数
    @Transient
    @TableField(exist = false)
    private String lateDays;
    //早退天数
    @Transient
    @TableField(exist = false)
    private String leaveEarlyDays;
    //旷工天数
    @Transient
    @TableField(exist = false)
    private String absenteeismDays;

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

    //上班图片路径
    @Transient
    @TableField(exist = false)
    private List<FileManage> sbFileList;
    //下班图片路径
    @Transient
    @TableField(exist = false)
    private List<FileManage> xbFileList;

}
