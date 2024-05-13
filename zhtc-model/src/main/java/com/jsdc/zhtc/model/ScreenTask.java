package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @projectName: zhtc
 * @className: ScreenTask
 * @author: wp
 * @description:
 * @date: 2023/3/24 10:36
 */
@TableName("screen_task")
@Entity
@Table(name = "screen_task")
@Data
public class ScreenTask {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //任务名称
    private String task_name;

    //诱导屏id
    private Integer screen_id;

    //任务类型 1：停车信息 2：多媒体
    private Integer play_type;

    //素材id
    private Integer materia_id;

    //开始播放时间
    private String start_time;

    //播放结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end_time;

    private String is_del;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    //是否已处理 0：否 1：是
    private Integer has_deal;

    //上一次发布日期
    private Date last_publish_time;

    @Transient
    @TableField(exist = false)
    private String title;
    @Transient
    @TableField(exist = false)
    private String type;
}
