package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: SysConfig
 * Description: 系统配置
 * date: 2021/12/30 10:12
 *
 * @author wp
 */
@Entity
@TableName("sys_config")
@Table(name = "sys_config")
@DynamicInsert
@DynamicUpdate
@Data
public class SysConfig extends Model<SysConfig> implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 系统配置键
     */
    private String config_key;

    /**
     * 系统配置值
     */
    private String config_value;

    /**
     * 系统配置名称
     */
    private String config_name;

    /**
     * 系统配置英文名称
     */
    private String config_name_en;

    /**
     * 系统配置描述
     */
    private String config_desc;

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
}
