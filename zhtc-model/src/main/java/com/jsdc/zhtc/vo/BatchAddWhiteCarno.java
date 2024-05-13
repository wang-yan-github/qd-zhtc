package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName BatchAddWhiteCarno
 * @Description TODO
 * @Author xuaolong
 * @Date 2022/2/21 09:55
 * @Version 1.0
 */
@Data
public class BatchAddWhiteCarno {
    /**
     * 蓝牌集合
     */
    List<String> blueCars;
    /**
     * 黄牌集合
     */
    List<String> yellowCars;
    /**
     * 绿牌集合
     */
    List<String> greenCars;

    /**
     * 白名单类型
     * 0:内部车
     * 1:企业(免税)车
     */
    Integer whitelist_type;

    /**
     * 理由说明
     */
    String reason;

    /**
     * 姓名
     */
    String name;

    /**
     * 电话
     */
    String phone;

    /**
     * 公司名称
     */
    String company_name;


    /**
     * 免费截止时间
     */
    Date cut_off_date;

    List<Integer> parkIds;

    List<Integer> roadIds;

    // 白名单免费类型 1永久 2期限
    private String white_time_type;

    //内部车白名单 免费开始时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date free_time_start;
    //内部车白名单 免费截止时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date free_time_end;

}
