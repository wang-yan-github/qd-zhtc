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
 * ClassName: SysRole
 * Description: 系统角色表
 * date: 2021/12/30 9:48
 *
 * @author wp
 */
@Entity
@TableName("sys_role")
@Table(name = "sys_role")
@DynamicInsert
@DynamicUpdate
@Data

public class SysRole extends Model<SysRole> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名
     */
    private String role_name;

    /**
     * 角色类型 1：停车场角色 0：非停车场角色
     */
    private String role_type;

    /**
     * 备注
     */
    private String remark;

    @Transient
    @TableField(exist = false)
    private String menuIds;

    @Transient
    @TableField(exist = false)
    private List<SysMenu> menus;

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

    @Transient
    @TableField(exist = false)
    private List<String> menuNames;

    @Transient
    @TableField(exist = false)
    private List<Integer> menuIdList;

    @Transient
    @TableField(exist = false)
    private List<Integer> roadMenuList;

    @Transient
    @TableField(exist = false)
    private List<Integer> parkMenuList;

}
