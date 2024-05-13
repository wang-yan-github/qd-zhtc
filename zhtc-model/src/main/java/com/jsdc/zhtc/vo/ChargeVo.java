package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.model.ChargeIntervalConfig;
import com.jsdc.zhtc.model.ChargeProgramme;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ChargeVo
 * @Description TODO
 * @Author xujian
 * @Date 2022/1/6 10:55
 * @Version 1.0
 */
@Data
public class ChargeVo implements Serializable {
    /**
     * 收费方案
     */
    private ChargeProgramme chargeProgramme;
    /**
     * 白天收费配置
     */
    private ChargeIntervalConfig dayConfig;
    /**
     * 夜间收费配置
     */
    private ChargeIntervalConfig nightConfig;

    public ChargeVo(ChargeProgramme chargeProgramme, ChargeIntervalConfig dayConfig, ChargeIntervalConfig nightConfig) {
        this.chargeProgramme = chargeProgramme;
        this.dayConfig = dayConfig;
        this.nightConfig = nightConfig;
    }
}
