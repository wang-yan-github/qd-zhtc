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
 * ClassName: CarnoCompany
 * Description:车牌企业映射表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("carno_company")
@Table(name = "carno_company")
@DynamicInsert
@DynamicUpdate
@Data
public class CarnoCompany extends Model<CarnoCompany> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //车牌id
    private Integer carno_id;
    //企业id
    private Integer company_id;

}
