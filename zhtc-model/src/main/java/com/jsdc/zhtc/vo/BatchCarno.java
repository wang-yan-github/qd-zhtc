package com.jsdc.zhtc.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName BatchCarno
 * @Description TODO
 * @Author xujian
 * @Date 2022/2/17 17:55
 * @Version 1.0
 */
@Data
public class BatchCarno {
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

    Integer companyId;
}
