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
import java.util.Date;

/**
 * ClassName: ParkingOrderMerge
 * Description:
 * date: 2022/1/4 15:34
 *
 * @author wh
 */
@Entity
@TableName("parking_order_merge")
@Table(name = "parking_order_merge")
@DynamicInsert
@DynamicUpdate
@Data
public class ParkingOrderMerge extends Model<ParkingOrderMerge> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //停车订单合并后ID
    private Integer ordermerge_id;
    // 被合并订单ID
    private Integer parking_order_id;
    /**
     * 创建时间
     */
    private Date create_time;
    /**
     * 创建人
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    private Date update_time;
    /**
     * 更新人
     */
    private Integer update_user;
    /**
     * 是否删除
     */
    private Integer is_del;
}
