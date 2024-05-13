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
 * ClassName: SysDict
 * Description: 字典表
 * date: 2021/12/30 10:27
 *
 * @author wp
 */

@Entity
@TableName("sys_dict")
@Table(name = "sys_dict")
@DynamicInsert
@DynamicUpdate
@Data
public class SysDict extends Model<SysDict> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 鼎驰键值
     */
    private String dc_value;

    /**
     * 鼎器键值
     */
    private String dq_value;

    /**
     * 捷顺键值
     */
    private String js_value;

    /**
     * 交控键值
     */
    private String jk_value;

    /**
     * 值
     */
    private String label;

    /**
     * 字典类型
     */
    private String dict_type;

    /**
     * 字典描述
     */
    private String dict_desc;

    /**
     * 父级字典
     */
    private String parent_id;

    /**
     * 排序
     */
    private String sort;

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
