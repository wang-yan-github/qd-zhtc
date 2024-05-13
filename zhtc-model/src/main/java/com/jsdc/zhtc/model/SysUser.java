package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ClassName: SysUser
 * Description: 系统用户表
 * date: 2021/12/30 9:10
 *
 * @author wp
 */
@Entity
@TableName("sys_user")
@Table(name = "sys_user")
@Data
public class SysUser extends Model<SysUser> implements Serializable {
    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 登录名
     */
    private String login_name;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String user_name;

    /**
     * 手机号
     */
    private String phone;

    private String recharge_pwd;

    /**
     * 身份证号
     */
    private String id_card;

    /**
     * 性别
     */
    private String sex;

    /**
     * 用户类型
     * 0：运营人员
     * 1：停车场管理员
     */
    private String user_type;

    /**
     * 所属停车场
     */
    private Integer park_id;

    /**
     * 状态 0：禁用 1：启用
     */
    private String status;

    /**
     * 上次登录ip
     */
    private String login_ip;

    /**
     * 上次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date login_date;

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
    private Integer roleId;

    @Transient
    @TableField(exist = false)
    private SysRole sysRole;

    @Transient
    @TableField(exist = false)
    private List<SysRole> sysRoles;

}
