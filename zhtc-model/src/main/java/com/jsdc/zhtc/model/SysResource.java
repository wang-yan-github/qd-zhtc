package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author wh
 * @description 系统资源
 */
@TableName("sys_resource")
@Entity
@Table(name = "sys_resource")
@Data
public class SysResource {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;//
    //标题
    private String title;
    //类别 0：文档 1：操作指南 2:常见问题 3轮播图 4 公告资讯
    private String category;
    //文档类别 0：用户协议、1：关于我们、2：退款协议
    private String doc_type;
    //图片ids
    private String picture_id;
    //摘要
    private String resource_abstract;
    //排序
    private Integer sort;
    //内容
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    /**
     * 创建人id
     */
    private Integer create_user;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    /**
     * 更新人id
     */
    private Integer update_user;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;
    /**
     * 是否推送 0：未推送 1：已推送
     */
    private String is_push;

    //图片路径
    @TableField(exist = false)
    @Transient
    private String pic_url;
    //图片路径
    @TableField(exist = false)
    @Transient
    private List<FileManage> fileList;
}
