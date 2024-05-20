package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: ParkingOrderPics
 * Description: 停车场订单上传图片表，区别于parking_order_details,两表内容完全一致，但parking_order_pics针对停车场订单，另一个针对路段订单
 * date: 2022/1/24 13:37
 *
 * @author wp
 */
@Entity
@TableName("parking_order_pics")
@Table(name = "parking_order_pics")
@DynamicInsert
@DynamicUpdate
@Data
public class ParkingOrderPics extends Model<ParkingOrderPics> implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //图片路径
    private String picture_url;
    //图片类型 1驶入 2驶出
    private String picture_type;
    //停车订单ID
    private Integer parking_order_id;
    //图片id
    private Integer picture_id;
    @Transient
    @TableField(exist = false)
    private String base64;

    @Transient
    @TableField(exist = false)
    private InputStream in;

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
    private String is_del;
}
