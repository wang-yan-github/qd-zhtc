package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.OperateCarno;
import lombok.Data;

/**
 * 车牌管理
 *
 * @author thr
 */
@Data
public class OperateCarnoVo extends OperateCarno {
    //车牌类型名称 (1蓝牌、2绿牌、3黄牌)
    private String carTypeName;
    //车辆类型名称 (1 固定车辆-非家属院居住 2 固定车辆-家属院居住 3 月租车辆 4 业务往来车辆 5 临时车辆)
    private String rosterTypeName;

    //创建人姓名
    private String userName;

    //车主姓名
    private String name;

    //车主手机号码
    private String phone;
    private String parkId;
    private String carNo;

    private Integer pageNo;
    private Integer pageSize;


}
