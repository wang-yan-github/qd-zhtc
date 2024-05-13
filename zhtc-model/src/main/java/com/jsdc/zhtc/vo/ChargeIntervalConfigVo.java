package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.ChargeIntervalConfig;
import com.jsdc.zhtc.model.ChargeTimeConfig;
import lombok.Data;

import java.util.List;

/**
 * ClassName: ChargeIntervalConfigVo <br/>
 * Description: <br/>
 * date: 2022/1/4 10:55<br/>
 *
 * @author bn<br   />
 */
@Data
public class ChargeIntervalConfigVo extends ChargeIntervalConfig {

    // 收费时长
    private List<ChargeTimeConfig> chargeTimeConfigs;

}
