package com.jsdc.zhtc.vo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

@Data
public class DeviceInfoVo {

    private Integer id;
    //订单id
    private Integer order_id;
    //订单号
    private String order_no;
    //车位编号
    private String berth_code;
    //车位状态
    private String status;
    //欠费金额
    private String arrearage;
    //欠费金额
    private Integer arrearageFlag = 0;
    //会员id
    private Integer member_id;
    //会员说明
    private Integer memberFlag = 0;
    //车牌编号
    private String car_no;
    //车牌类型
    private String car_type;
    //车牌类型名称
    private String berthTypeName;
    //包月状态
    private String byStatus;
    //入场时间
    private String driveinTime;
    //停车场名称
    private String packName;

    public void setArrearage(String arrearage) {
        if (StringUtils.isNotBlank(arrearage)) {
            this.arrearage = setScale1(arrearage);
            if (Double.parseDouble(arrearage) > 0)
                arrearageFlag = 1;
        } else {
            this.arrearage = null;
        }
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
        if (member_id != null)
            memberFlag = 1;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
        if (StringUtils.isNotBlank(car_type)) {
            if (car_type.equals("1"))
                berthTypeName = "蓝色";
            else if (car_type.equals("1"))
                berthTypeName = "绿牌";
            else if (car_type.equals("1"))
                berthTypeName = "黄牌";
            else
                berthTypeName = "其他";
        }
    }


    /**
     * 描 述： TODO( 保留小数标准 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param num1
     * @return {@link BigDecimal}
     */
    public String setScale1(Object num1) {
        if (num1 == null)
            return "0.0";
        else
            return new BigDecimal(num1.toString()).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }


}
