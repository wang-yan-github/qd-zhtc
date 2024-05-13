package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * 类 名: 分页查询入参vo
 * 描 述: PageVo
 * 作 者: lw
 * 创 建：2022/1/4 13:45
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class PageVo<T> {

    private T bean;
    //页大小
    private Integer pageSize = 10;
    //页码
    private Integer pageNum;
    //起始时间
    private String startTime;
    //截至时间
    private String endTime;
    //查询状态是否使用部分独立条件
    private Boolean flag;
    //独立条件等
    private String str;

}
