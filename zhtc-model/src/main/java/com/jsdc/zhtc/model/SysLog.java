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
 * ClassName: SysLog
 * Description: 系统日志
 * date: 2021/12/30 10:06
 *
 * @author wp
 */
@Entity
@TableName("sys_log")
@Table(name = "sys_log")
@DynamicInsert
@DynamicUpdate
@Data
public class SysLog extends Model<SysLog> implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer user_id;

    /**
     * 日志内容
     */
    private String log_content;

    /**
     * 操作时间
     */
    private Date operation_time;

    /**
     * 日志类型
     */
    private String log_type;

    /**
     * 操作ip
     */
    private String ip;

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
     * 1:运营人员 2：巡检人员
     */
    private String user_type;

    @Transient
    @TableField(exist = false)
    private String user_name;

    @Transient
    @TableField(exist = false)
    private String log_user_type;

    @Transient
    @TableField(exist = false)
    private String start_time;

    @Transient
    @TableField(exist = false)
    private String end_time;
    @Transient
    @TableField(exist = false)
    private String role_name;
}
