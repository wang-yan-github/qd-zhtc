package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.ChargeIntervalConfig;
import com.jsdc.zhtc.model.ChargeTimeConfig;
import lombok.Data;

import java.util.List;

/**
 * ClassName: ChargeProgrammeData <br/>
 * Description: 收费方案前端接收<br/>
 * date: 2022/1/10 14:10<br/>
 *
 * @author bn<br   />
 */
@Data
public class ChargeProgrammeData {

    private Integer id;

    //收费方案名称
    private String programme_name;

    //24小时限价设定 0：不设置24小时限价 1：设置
    private Integer is_limit_price;

    //24小时限价金额
    private String limit_price_amount;

    //黑夜
    private ChargeIntervalConfig night;
    //白天
    private ChargeIntervalConfig days;
    // 黑夜接收
    private List<ChargeTimeConfig> nightTime;
    // 白天接收
    private List<ChargeTimeConfig> daysTime;

    //excel 编码
    private String excelCode;


}
