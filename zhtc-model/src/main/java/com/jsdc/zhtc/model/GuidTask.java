package com.jsdc.zhtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @projectName: zhtc
 * @className: GuidTask
 * @author: wp
 * @description: 诱导屏播放任务表
 * @date: 2022/12/8 9:23
 */
@TableName("guid_task")
@Entity
@Table(name = "guid_task")
@Data
public class GuidTask {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备编码
     */
    private String pid;

    /**
     * 文件名称
     */
    private String file_name;

    /**
     * 文件类型 1：图片 2：视频
     */
    private String file_type;

    /**
     * 文件下载地址
     */
    private String file_url;

    /**
     * 下载状态 1：未下载 2：已下载
     */
    private Integer download_state;

    /**
     * 下载结果
     */
    private String download_result;

    /**
     * 播放状态 1：未播放 2：已播放
     */
    private Integer play_state;

    /**
     * 播放结果
     */
    private String play_result;

    /**
     * 是否是播放头节目 0否 1是
     */
    private Integer is_header;

    /**
     * 删除状态
     */
    private String is_del;

    private Date create_time;

    private Date update_time;
}
