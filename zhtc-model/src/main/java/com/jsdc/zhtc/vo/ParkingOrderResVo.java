package com.jsdc.zhtc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: ParkingOrderResVo
 * Description:
 * date: 2022/1/24 10:32
 *
 * @author wp
 */
@Data
public class ParkingOrderResVo {
    private Integer poId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drivein_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date driveout_time;
    private String park_name;
    private String address;
    private String status;
    private String paid_amount;
    private Integer invid;
    private String car_no;
    private String order_no;
    private String resitime;
    private String is_invoice;
}
