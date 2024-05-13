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
 * 诱导屏管理
 */
@Entity
@TableName("screen")
@Table(name = "screen")
@DynamicInsert
@DynamicUpdate
@Data
public class Screen extends Model<Screen> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * ip
     */
    private String ip;

    /**
     * 诱导屏编号
     */
    private String pid;

    /**
     * 标题
     */
    private String title;

    /**
     * 播放类型
     * 1：停车信息 2：多媒体
     */
    private Integer play_type;

    /**
     * 当前播放的多媒体类型
     * 文本（1） 图片（2） 视频（3） 默认（4）
     * 默认即为停车信息
     */
    private Integer video_type;

    /**
     * 当前播放内容
     * video_type == 1 为文本内容
     * video_type == 2 为图片url
     * video_type == 3 为视频url
     * video_type == 4 为空
     */
    private String content;

    /**
     * 是否启用 0否 1是
     */
    private Integer is_use;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    /**
     * 创建人
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    /**
     * 更新人
     */
    private Integer update_user;
    /**
     * 是否删除
     */
    private String is_del;

    //停车场、路段ids
    @Transient
    @TableField(exist = false)
    private List<Integer> roadIdList;
    //停车场、路段名称
    @Transient
    @TableField(exist = false)
    private List<Integer> parkIdList;

    //停车场、路段ids
    @Transient
    @TableField(exist = false)
    private String roadIds;
    //停车场、路段名称
    @Transient
    @TableField(exist = false)
    private String parkIds;

    //停车场、路段名称
    @Transient
    @TableField(exist = false)
    private String roadNames;
    //停车场、路段名称
    @Transient
    @TableField(exist = false)
    private String parkNames;

    //诱导屏数组
    @Transient
    @TableField(exist = false)
    private List<Screen> screenList;

    @Transient
    @TableField(exist = false)
    private Integer materialId;

    @Transient
    @TableField(exist = false)
    private String start_time;

    @Transient
    @TableField(exist = false)
    private String taskName;
}
