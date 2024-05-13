package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.MemberManage;
import com.jsdc.zhtc.model.OperateCarno;
import lombok.Data;

import java.util.List;

/**
 * ClassName: MemberManageVo <br/>
 * Description: <br/>
 * date: 2022/1/4 13:44<br/>
 *
 * @author bn<br   />
 */
@Data
public class MemberManageVo extends MemberManage {


    // 车主车牌
    private List<OperateCarno> operateCarnos;

    // 车牌
    private String car_no;

    // 注册开始时间
    private String start_time;

    // 注册结束时间
    private String end_time;
}
