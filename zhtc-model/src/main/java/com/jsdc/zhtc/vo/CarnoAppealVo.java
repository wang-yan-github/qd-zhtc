package com.jsdc.zhtc.vo;

import lombok.Data;

/**
 * @author 王严
 * @version 1.0
 * @description: 车牌申诉
 */
@Data
public class CarnoAppealVo {
    /**
     * id
     */
    public Integer id;
    /**
     * 车牌号
     */
    public String carNo;
    /**
     * 是否是黄牌 车牌类型(蓝牌、绿牌、黄牌)
     */
    public Boolean isYellowCard;
    /**
     * 手机号
     */
    public String phone;
    /**
     * 申诉状态(通过、拒绝、申诉中) 1 0 -1
     */
    private String status;
    /**
     * 拒绝原因
     */
    private String rejectReason;
}
