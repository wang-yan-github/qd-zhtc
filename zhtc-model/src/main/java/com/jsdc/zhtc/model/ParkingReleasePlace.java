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
 * ClassName: AppealHandleRecord
 * Description:上线收费停车区域配置
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("release_parking_place")
@Table(name = "release_parking_place")
@DynamicInsert
@DynamicUpdate
@Data
public class ParkingReleasePlace extends Model<ParkingReleasePlace> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //上线收费配置ID
    private Integer parking_release_id;
    //停车区域ID
    private Integer parkingplace_id;
    //删除
    private Integer is_del;
    //停车类型
    private String parking_type;

}
