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
 * ClassName: SysMenu
 * Description: 系统菜单表
 * date: 2021/12/30 9:30
 *
 * @author wp
 */
@Entity
@TableName("sys_menu")
@Table(name = "sys_menu")
@DynamicInsert
@DynamicUpdate
@Data
public class SysMenu extends Model<SysMenu> implements Serializable {
    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父级菜单id
     */
    private Integer parent_id;

    /**
     * 菜单名称
     */
    private String menu_name;

    /**
     * 菜单类型 0：路段 1：停车场
     */
    private String menu_type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单链接
     */
    private String href;

    /**
     * 图表
     */
    private String icon;

    /**
     * 是否显示 0：隐藏 1：显示
     */
    private String is_show;

    /**
     * 是否是按钮 0：否 1：是
     */
    private Integer is_button;

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
     * 按钮code
     */
    private String code;
}
