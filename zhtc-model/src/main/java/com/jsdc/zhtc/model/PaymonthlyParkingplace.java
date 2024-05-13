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
 * ClassName: PaymonthlyParkingplace
 * Description: 包月停车区，作为字表关联主表paymonthly_config
 * date: 2021/12/30 10:23
 *
 * @author wp
 */
@Entity
@TableName("paymonthly_parkingplace")
@Table(name = "paymonthly_parkingplace")
@DynamicInsert
@DynamicUpdate
@Data
public class PaymonthlyParkingplace extends Model<PaymonthlyParkingplace> implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 包月配置id
     */
    private Integer paymonthly_config_id;

    /**
     * 路段或停车场id
     */
    private Integer parkingplace_id;

    private String is_del;

}
