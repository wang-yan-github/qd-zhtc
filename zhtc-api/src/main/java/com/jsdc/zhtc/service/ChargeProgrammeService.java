package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.dao.ChargeProgrammeDao;
import com.jsdc.zhtc.mapper.ChargeProgrammeMapper;
import com.jsdc.zhtc.model.ChargeProgramme;
import com.jsdc.zhtc.model.ChargeTimeConfig;
import com.jsdc.zhtc.vo.ChargeProgrammeData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * ClassName: ChargeProgrammeService <br/>
 * Description: <br/>
 * date: 2021/12/30 11:03<br/>
 *
 * @author bn<br       />
 */
@Service
@Transactional
public class ChargeProgrammeService extends BaseService<ChargeProgrammeDao, ChargeProgramme> {

    @Autowired
    private ChargeProgrammeMapper chargeProgrammeMapper;


    @Autowired
    private ChargeIntervalConfigService chargeIntervalConfigService;

    @Autowired
    private ChargeTimeConfigService chargeTimeConfigService;


    /**
     * 全收费方案列表
     *
     * @param chargeProgramme
     * @return
     */
    public List<ChargeProgramme> toList(ChargeProgramme chargeProgramme) {
        QueryWrapper<ChargeProgramme> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(chargeProgramme.getProgramme_name())) {
            queryWrapper.eq("programme_name", chargeProgramme.getProgramme_name());
        }
        queryWrapper.eq("is_del", 0);
        return selectList(queryWrapper);
    }


    public ChargeProgrammeData getChargeProgramme(ChargeProgramme chargeProgramme) {
        ChargeProgrammeData chargeProgrammeData = new ChargeProgrammeData();

        ChargeProgramme charge = selectById(chargeProgramme.getId());
        BeanUtils.copyProperties(charge, chargeProgrammeData);

        // 白天
        chargeProgrammeData.setDays(chargeIntervalConfigService.selectById(charge.getDay_interval_config_id()));
        // 黑夜
        chargeProgrammeData.setNight(chargeIntervalConfigService.selectById(charge.getNight_interval_config_id()));
        // 白天时段
        chargeProgrammeData.setDaysTime(chargeTimeConfigService.selectList(
                new QueryWrapper<ChargeTimeConfig>().eq("interval_config_id",
                        chargeProgrammeData.getDays().getId()).eq("is_del", 0)));
        // 黑天时段
        chargeProgrammeData.setNightTime(chargeTimeConfigService.selectList(
                new QueryWrapper<ChargeTimeConfig>().eq("interval_config_id",
                        chargeProgrammeData.getNight().getId()).eq("is_del", 0)));

        return chargeProgrammeData;
    }


}
