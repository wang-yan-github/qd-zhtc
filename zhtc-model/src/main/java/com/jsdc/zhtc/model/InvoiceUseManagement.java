package com.jsdc.zhtc.model;

/**
 * ClassName: InvoiceUseManagement
 * Description:
 * date: 2022/1/13 13:35
 *
 * @author wp
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@TableName("invoice_use_management")
@Table(name = "invoice_use_management")
@DynamicInsert
@DynamicUpdate
@Data
public class InvoiceUseManagement {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 巡检员id
     */
    private String inject_id;
    /**
     * 订单id
     */
    private String order_id;
    /**
     * 订单编号
     */
    private String order_code;
    /**
     * 订单金额
     */
    private String order_amount;
    /**
     * create by wp at 2022/1/13 13:37
     * description: 发票金额
     *
     * @param null
     * @return null
     */
    private String invoice_amount;
    /**
     * 发放时间
     */
    private String after_account_balance;

}
