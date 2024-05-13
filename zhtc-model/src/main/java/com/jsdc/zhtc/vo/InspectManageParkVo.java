package com.jsdc.zhtc.vo;

import lombok.Data;

import java.util.List;

/**
 * ClassName: InspectManageParkVo
 * Description: 巡检员停车场信息
 * date: 2022/1/27 11:57
 *
 * @author bn
 */
@Data
public class InspectManageParkVo {

    // 停车场名称
    private String park_name;
    // 泊位总数
    private Integer park_num;
    // 占用泊位
    private Integer occupy_num;
    // 空闲泊位
    private Integer free_num;
    // 占用率
    private String occupancy_rate;


    List<ParkingOrderVo> list;


}
