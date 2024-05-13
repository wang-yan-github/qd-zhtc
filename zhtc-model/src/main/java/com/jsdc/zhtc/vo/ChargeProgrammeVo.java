package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.ChargeIntervalConfig;
import com.jsdc.zhtc.model.ChargeProgramme;
import com.jsdc.zhtc.model.Park;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * ClassName: ChargeProgrammeVo <br/>
 * Description: <br/>
 * date: 2022/1/4 9:18<br/>
 *
 * @author bn<br   />
 */
@Data
public class ChargeProgrammeVo extends ChargeProgramme {


    // 应用停车场
    private List<Park> parks;

    private Integer road_id;

    //0 路侧 1 停车场
    private String menuType;


    // 收费时段
    private List<ChargeIntervalConfigVo> chargeIntervalConfigVos;
    //黑夜
    private ChargeIntervalConfig night;
    //白天
    private ChargeIntervalConfig days;

    // 1、白天 2、黑夜 3、全部
    private Integer distinguish;
    //进场时间
    private Date drivein_time;
    //出场时间
    private Date driveout_time;

    // 添加人
    private String name;

    private List<Integer> ids;

}
