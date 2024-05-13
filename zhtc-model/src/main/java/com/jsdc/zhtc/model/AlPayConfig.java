package com.jsdc.zhtc.model;


import com.baomidou.mybatisplus.annotation.*;
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
 * 类 名: 阿里支付信息配置
 * 描 述: AlPayConfig
 * 作 者: lw
 * 创 建：2021/12/30 14:34
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Entity
@TableName("al_pay_config")
@Table(name = "al_pay_config")
@DynamicInsert
@DynamicUpdate
@Data
public class AlPayConfig extends Model<AlPayConfig> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //名称
    private String name;

    //访问地址 url 正式环境 https://openapi.alipay.com/gateway.do 沙箱环境 https://openapi.alipaydev.com/gateway.do
    private String pay_url = "https://openapi.alipaydev.com/gateway.do";

    //appid
    private String app_id = "2021000119641863";

    //应用私钥
    private String app_private_id = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDU3GlBIiXnNSG/N414/1WUBn6S4L1jwXz7pbcayrhVpD4SmIFq/448ZrmqzEDiKNW5kFc4bEjfsKiWZmWiWoq5r155n9AspsBc8449CEJneXjwZ5F7/+mdzrV1qCfpDuZaMpqL1xFrZs/23HtQtMEdt1YjoGw3WRRBWVZwl1D2K2y/7RO33oJMJgEnmTFSyTyklJrM0rZbDkTJvGlOO9GeiiSDJpLCYw1pMoARzhTeWfFvgLu3QmiX5hp9GdHBYU70ETOXmAusu2qJyYTYH2ALIPMXmWDwxh8h1PU8yJnv6cXrUVKUFH41DznQdUZIeA9fUGlmTM/NgB50jW1DVMMrAgMBAAECggEAMTxWYEl8yPEiQC+iWqV87wNdja3MQOHFfhPDub/ASZb3AKgj71LzXIdKVPZv36AgZ466jgOGC6+6HyfKRHZ69h5ZVPqhVWKwxg9UDEv3yCVx0vGx1iPIrsbcdD2UFbVrlNt1ofj6P4SFgA1ZvL5CmRQKKa12uoqxQ7hdZSerA3V1ln2nDjsoi8RFXXw4+v7psSLHSWMH/aPyUK4QstJQrnoLfG79oZzEBqDSBKWfRRrXb+lR7xMxygKOq9q9BNiY6ZXio93OEKLHMoznGmyWTueZ2SNqAgSKf6+K4kLVQ9yfcdcefrk5DnNF9kkG1jU3ikLOAkCBqxIm7aGvPAnVoQKBgQD9JbRYm/yjQy10PMZkL0ML11UMMhVindiiC1PEkKRXODTeaYQnRgIAnM3+tuhGKxLyZchej93YsKjM+rGFLlH4nq10iWKx4kJv0Kf7UvElHXT0e14bACpRhrV3WYeig1dtw8W3daw3lnHqwl9/pkXfgZWhzUUfBWf1/a5hJUlDWwKBgQDXQnx1Q3afGYHLUkAbdcTVDN9+L9XXc5SUMWPxPlwgW7cjcEbnkPEKTyxXyWIAKzp9fzYySU0S3RWJqAJiXdYkkAKWeokDa4QT0Au9tucwNpx2TCmoKwfXdupJN+qYGTL90IfVliVE9AbVM33NRDcfV4jSSWVidruQJC3QSgCYcQKBgCA18vuvZXcUrizzKr9GsD5KePzjRotU0GLWFCOX0AF1Gm0Oeg1RQCH50dBOT8HV60RCpTs7DRxsZh4m6/2pRi7faezBKni99HwrzaQSCrMRS3dsVy4SWFAlVuPH1ZVdJvvFhFvysiYkk3wunV4ttwhYgcSjzN7EnlTToaOkzeyvAoGBAKWj1Z1uYlMfD49jJU18HBBNLZUuPgDiQYyfTOaQM9f6ezxb41PBZpmmPtr2beDvYry7LPsZCKPhV/drPxpuVqAf6L3eYJwvGZ0dVEuR+OhSvacSCGQNaKVWLgZVbsHHckN5OW0Tcm5S0Qcx7jYLrdcCRph6Pfe8oavTCyXiK/8hAoGBAPP3qp0rXeEZ15JuoFtzJnyW8xbAYVDAMWK7EVOU7ACF4/GwhBBaPJ9ifmFfa2SgEto4zWoPjj05lZ2DyuCH+xQeeYeD42Bin3eI9gdelQv7W9xTdWKLvwRm4Wl8DGPgXSJgzC56N75SxGIL6h14GLlqXplPc2QEnAbR2gYpClV+";

    //应用公钥
    private String app_public_id = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1NxpQSIl5zUhvzeNeP9VlAZ+kuC9Y8F8+6W3Gsq4VaQ+EpiBav+OPGa5qsxA4ijVuZBXOGxI37ColmZlolqKua9eeZ/QLKbAXPOOPQhCZ3l48GeRe//pnc61dagn6Q7mWjKai9cRa2bP9tx7ULTBHbdWI6BsN1kUQVlWcJdQ9itsv+0Tt96CTCYBJ5kxUsk8pJSazNK2Ww5EybxpTjvRnookgyaSwmMNaTKAEc4U3lnxb4C7t0Jol+YafRnRwWFO9BEzl5gLrLtqicmE2B9gCyDzF5lg8MYfIdT1PMiZ7+nF61FSlBR+NQ850HVGSHgPX1BpZkzPzYAedI1tQ1TDKwIDAQAB";

    //字符类型
    private String charset = "utf-8";

    //支付宝公钥
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkyWcqrqB4yWA6BYuXoBJr4q3kRitZAKS63HI5EEhY6sCszIj7jRPq8x9QmHhVB7CNaCPLydk5ak8p6cI4jomfvhiZUDtcKf6VGcajQh1MGBDjiXKfoRY+e7dwdttxWsh6yHzHSIlw5ialYV4wcRuAal2cQpjPYgRR5/+g9TLf3xn5fBTlV2T/n6OmsQhWIAVMcY3bOLfAYEZYHxGUT6jYvyV6yJ3cGq9V4c7M7VPpxRDZb+/1qTFCf061WcO5UP6MQC5z1HFazHHL4lVNiv4lYSDpIfMOlb49gIdwZOkwFppRfkXNzGGVGdzCf8V1HXN+nRKZHr3wuN6Zs8F5wdSHwIDAQAB\n";

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date create_time;

    //创建人
    private Integer create_user;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date update_time;

    // 更新人
    private Integer update_user;

    //是否为默认 0 默认  1 非默认
    private Integer is_default;

    // 是否删除
    private String is_del;


}
