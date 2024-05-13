package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.AppealHandleRecord;
import lombok.Data;

/**
 * ClassName: AppealHandleRecord
 * Description:申诉处理记录表
 * date: 2021/12/30 9:48
 *
 * @author zonglina
 */
@Data
public class AppealHandleRecordVo extends AppealHandleRecord {

    //根据车牌号查询
    private String car_no;
    //原来车牌类型
    private String yc_car_type;
    //原来车牌号
    private String yc_car_no;
    //图片ids
    private String fileIds;
    //订单状态
    private String appeal_order_status;
    //用户id
    private Integer member_id;
    //订单号
    private String order_no;
}
