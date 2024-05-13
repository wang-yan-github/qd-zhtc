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

/**
 * @projectName: zhtc
 * @className: WhiteCarnoPark
 * @author: wp
 * @description:
 * @date: 2022/8/9 15:43
 */
@Entity
@TableName("whitecarno_park")
@Table(name = "whitecarno_park")
@DynamicInsert
@DynamicUpdate
@Data
public class WhiteCarnoPark extends Model<WhiteCarnoPark> {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 白名单车辆id
     */
    private Integer carno_id;

    /**
     * 路侧或停车场id
     */
    private Integer park_id;

    /**
     * 停车场类型  0：路测 1：停车场
     */
    private String park_type;

    /**
     * 逻辑删除
     */
    private String is_del;


}
