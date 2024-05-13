package com.jsdc.zhtc.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jsdc.zhtc.model.FileManage;
import com.jsdc.zhtc.model.OperateAppeal;
import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * ClassName: ParkingOrderVo <br/>
 * Description: <br/>
 * date: 2022/1/4 9:18<br/>
 *
 * @author zln
 */
@Data
public class OperateAppealVo extends OperateAppeal {

    //停车时长
    private String resitime;
    //待支付金额
    private String unpaid_amount;
    //订单号
    private String order_no;
    //车牌号
    private String car_no;
    // 车牌样式
    private String car_type;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //进场时间
    private Date drivein_time;
    //出场时间
    private Date driveout_time;
    //关键字
    private String keywords;
    //处理状态
    private String handle_status;
    //驳回原因
    private String reject_reason;
    //处理类型
    private String handle_type;
    private String order_status_name;
    //申诉处理记录表ID
    private Integer handleId;
    //停车场/路段id
    private Integer placeId;
    //图片路径
    @TableField(exist = false)
    @Transient
    private List<FileManage> fileList;

}
