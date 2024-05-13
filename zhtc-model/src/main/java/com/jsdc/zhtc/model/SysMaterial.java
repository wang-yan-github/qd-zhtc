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
import java.util.List;

/**
 * 素材管理
 */
@TableName("sys_materia")
@Entity
@Table(name = "sys_materia")
@Data
public class SysMaterial {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;//
    //标题
    private String title;
    //文件类型 1 图片 2 视频
    private String type;
    //文件id
    private String file_id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    /**
     * 创建人id
     */
    private Integer create_user;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    /**
     * 更新人id
     */
    private Integer update_user;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;
    /**
     * 状态 1 启用 2 禁用
     */
    private String state;

    //文件路径
    @TableField(exist = false)
    @Transient
    private String file_url;
    //文件路径
    @TableField(exist = false)
    @Transient
    private List<FileManage> fileList;
}
