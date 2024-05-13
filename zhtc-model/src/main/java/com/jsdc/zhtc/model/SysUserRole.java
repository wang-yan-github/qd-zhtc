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
 * ClassName: SysUserRole
 * Description:
 * date: 2021/12/30 9:19
 *
 * @author wp
 */
@Entity
@TableName("sys_user_role")
@Table(name = "sys_user_role")
@DynamicInsert
@DynamicUpdate
@Data
public class SysUserRole extends Model<SysUserRole> implements Serializable {
    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 系统用户id
     */
    private Integer user_id;

    /**
     * 角色id
     */
    private Integer role_id;

    private String is_del;

}
