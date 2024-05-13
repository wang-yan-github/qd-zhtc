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
 * @author wh
 * @description 文件管理
 */
@TableName("file_manage")
@Entity
@Table(name = "file_manage")
@Data
public class FileManage {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //文件名
    private String file_name;
    //文件地址
    private String file_url;
    //文件大小
    private String file_size;
    //存储文件名
    private String store_name;
    //文件类型
    private String file_type;
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


    //文件地址
    @TableField(exist = false)
    @Transient
    private String url;

    @TableField(exist = false)
    @Transient
    private String name;
}
