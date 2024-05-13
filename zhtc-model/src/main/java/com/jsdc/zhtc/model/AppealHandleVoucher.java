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
 * ClassName: AppealHandleVoucher
 * Description:申诉驳回凭证表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Entity
@TableName("appeal_handle_voucher")
@Table(name = "appeal_handle_voucher")
@DynamicInsert
@DynamicUpdate
@Data
public class AppealHandleVoucher extends Model<AppealHandleVoucher> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //处理记录表id
    private Integer handle_record_id;
    //凭证图片id
    private Integer picture_id;
    //是否删除
    private Integer is_del;
}
