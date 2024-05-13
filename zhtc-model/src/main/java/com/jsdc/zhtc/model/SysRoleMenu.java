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

/**
 * ClassName: SysRoleMenu
 * Description: 角色菜单表
 * date: 2021/12/30 10:01
 *
 * @author wp
 */
@Entity
@TableName("sys_role_menu")
@Table(name = "sys_role_menu")
@DynamicInsert
@DynamicUpdate
@Data
public class SysRoleMenu extends Model<SysRoleMenu> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色id
     */
    private Integer role_id;

    /**
     * 菜单id
     */
    private Integer menu_id;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;

}
