package com.jsdc.zhtc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.enums.ChargeTypeEnum;
import com.jsdc.zhtc.model.*;
import com.jsdc.zhtc.utils.RedisUtils;
import com.jsdc.zhtc.vo.ChargeVo;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CacheDataService
 * @Description TODO
 * @Author xujian
 * @Date 2022/1/6 14:42
 * @Version 1.0
 */
@Component
public class CacheDataService {


    @Autowired
    private SysDictService sysDictService;


    @Autowired
    private ParkService parkService;

    @Autowired
    private ChargeProgrammeService chargeProgrammeService;

    @Autowired
    private ChargeIntervalConfigService chargeIntervalConfigService;
    @Autowired
    private ChargeTimeConfigService chargeTimeConfigService;

    /**
     * 缓存数据初始化
     */
    public void dataInit(){
        //字典初始化
        dictInit();
        //停车场
        parkInit();
        //路段、停车场收费方案
        parkChargeInit();
    }

    /**
     * 字典初始化
     */
    public void dictInit(){
        List<SysDict> sysDicts = sysDictService.selectList(new QueryWrapper<SysDict>().eq("is_del","0"));
        Map<String, HashMap> dicData = new HashMap<>();
        sysDicts.forEach(x->{
            if(dicData.containsKey(x.getDict_type())){
                HashMap hashMap = dicData.get(x.getDict_type());
                hashMap.put(x.getDc_value(),x);
            }else{
                HashMap hashMap = new HashMap();
                hashMap.put(x.getDc_value(),x);
                dicData.put(x.getDict_type(),hashMap);
            }
        });
        RedisUtils.setBeanValue("dictData",dicData);
    }

    /**
     * 停车场
     */
    public void parkInit(){
        List<Park> parks = parkService.selectList(new QueryWrapper<Park>().eq("is_del","0"));
        HashMap hashMap = new HashMap();
        parks.forEach(x->{
            hashMap.put(x.getId(),x);
        });
        RedisUtils.setBeanValue("parkData",hashMap);
    }

    /**
     * 更新字典缓存
     *
     * @param sysDict
     * @return
     */
    public ResultInfo updateDictCache(SysDict sysDict) {
        try {
            //获取缓存数据
            Map<String, HashMap> dictData = (Map<String, HashMap>) RedisUtils.getBeanValue("dictData");
            if (dictData.containsKey(sysDict.getDict_type())) {
                if ("1".equals(sysDict.getIs_del())) {
                    HashMap dictMap = dictData.get(sysDict.getDict_type());
                    dictMap.remove(sysDict.getDc_value());
                } else {
                    HashMap dictMap = dictData.get(sysDict.getDict_type());
                    dictMap.put(sysDict.getDc_value(), sysDict);
                }

            } else {
                HashMap hashMap = new HashMap();
                hashMap.put(sysDict.getDc_value(), sysDict);
                dictData.put(sysDict.getDict_type(), hashMap);
            }
            RedisUtils.setBeanValue("dictData", dictData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfo.error("字典缓存更新失败！");
        }

        return ResultInfo.success();
    }


    /**
     *  更新区域、街道、路段、停车场缓存数据
     * @param o
     * @return
     */
    public ResultInfo updateLocationCache(Object o){
        if(o == null) ResultInfo.error("更新对象为空！");
        try{
            if(o instanceof Park){
                HashMap parkMap = (HashMap) RedisUtils.getBeanValue("parkData");
                Park park = (Park) o;
                if("1".equals(park.getIs_del())){
                    parkMap.remove(park.getId(),park);
                }else{
                    parkMap.put(park.getId(),park);
                }
                RedisUtils.setBeanValue("parkData",parkMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultInfo.error("字典更新失败！");
        }
        return ResultInfo.success();
    }


    /**
     * 路段、停车场 收费方案缓存更新
     */
    public void updChargeByRoad_ParkId(Integer parkId){
        //获取收费方案
        List<ChargeProgramme> chargeProgrammes = chargeProgrammeService.selectList(new QueryWrapper<ChargeProgramme>().eq("is_del",0));
        HashMap chargeMap = new HashMap();
        if(chargeProgrammes != null && chargeProgrammes.size()>0){
            for (ChargeProgramme cp : chargeProgrammes) {
                //白天收费方案
                ChargeIntervalConfig dayConfig = chargeIntervalConfigService.selectById(cp.getDay_interval_config_id());
                List<ChargeTimeConfig> day_chargeTimeConfigs = chargeTimeConfigService.selectList(new QueryWrapper<ChargeTimeConfig>().eq("is_del","0").eq("interval_config_id",dayConfig.getId()));
                dayConfig.setChargeTimeConfigs(day_chargeTimeConfigs);
                //夜间收费方案
                ChargeIntervalConfig nightConfig = chargeIntervalConfigService.selectById(cp.getNight_interval_config_id());
                List<ChargeTimeConfig> night_chargeTimeConfigs = chargeTimeConfigService.selectList(new QueryWrapper<ChargeTimeConfig>().eq("is_del","0").eq("interval_config_id",nightConfig.getId()));
                nightConfig.setChargeTimeConfigs(night_chargeTimeConfigs);
                ChargeVo chargeVo = new ChargeVo(cp,dayConfig,nightConfig);
                chargeMap.put(cp.getId(),chargeVo);
            }
        }
        //获取当前缓存
        HashMap parkCharge = (HashMap) RedisUtils.getBeanValue("parkCharge");
        //获取路段数据
       if(parkId != null){
            Park park = parkService.selectById(parkId);
            if(park!=null && GlobalData.ISDEL_NO.equals(park.getIs_del())){
                //停车方案绑定
                //普牌（蓝牌）
                parkCharge.put(park.getId()+"1"+ ChargeTypeEnum.BLUE.getValue(),chargeMap.get(park.getBlue_charge_id()));
                //绿牌(绿牌)
                parkCharge.put(park.getId()+"1"+ChargeTypeEnum.GREEN.getValue(),chargeMap.get(park.getGreen_charge_id()));
                //大车(黄牌)
                parkCharge.put(park.getId()+"1"+ChargeTypeEnum.YELLOW.getValue(),chargeMap.get(park.getYellow_charge_id()));
            }else{
                parkCharge.remove(park.getId()+"1"+ ChargeTypeEnum.BLUE.getValue());
                parkCharge.remove(park.getId()+"1"+ ChargeTypeEnum.GREEN.getValue());
                parkCharge.remove(park.getId()+"1"+ ChargeTypeEnum.YELLOW.getValue());
            }
        }
        //路段收费方案
        RedisUtils.setBeanValue("parkCharge",parkCharge);
    }


    /**
     * 路段、停车场 收费方案
     */
    public void parkChargeInit(){
        //获取收费方案
        List<ChargeProgramme> chargeProgrammes = chargeProgrammeService.selectList(new QueryWrapper<ChargeProgramme>().eq("is_del",0));
        HashMap chargeMap = new HashMap();
        if(chargeProgrammes != null && chargeProgrammes.size()>0){
            for (ChargeProgramme cp : chargeProgrammes) {
                //白天收费方案
                ChargeIntervalConfig dayConfig = chargeIntervalConfigService.selectById(cp.getDay_interval_config_id());
                List<ChargeTimeConfig> day_chargeTimeConfigs = chargeTimeConfigService.selectList(new QueryWrapper<ChargeTimeConfig>().eq("is_del","0").eq("interval_config_id",dayConfig.getId()));
                dayConfig.setChargeTimeConfigs(day_chargeTimeConfigs);
                //夜间收费方案
                ChargeIntervalConfig nightConfig = chargeIntervalConfigService.selectById(cp.getNight_interval_config_id());
                List<ChargeTimeConfig> night_chargeTimeConfigs = chargeTimeConfigService.selectList(new QueryWrapper<ChargeTimeConfig>().eq("is_del","0").eq("interval_config_id",nightConfig.getId()));
                nightConfig.setChargeTimeConfigs(night_chargeTimeConfigs);
                ChargeVo chargeVo = new ChargeVo(cp,dayConfig,nightConfig);
                chargeMap.put(cp.getId(),chargeVo);
            }
        }
        HashMap parkCharge = new HashMap();
        //停车场数据
        List<Park> parks = parkService.selectList(new QueryWrapper<Park>().eq("is_del","0"));
        for (Park park : parks) {
            //路段 停车方案绑定
            //普牌（蓝牌）
            parkCharge.put(park.getId()+"1"+ ChargeTypeEnum.BLUE.getValue(),chargeMap.get(park.getBlue_charge_id()));
            //绿牌(绿牌)
            parkCharge.put(park.getId()+"1"+ChargeTypeEnum.GREEN.getValue(),chargeMap.get(park.getGreen_charge_id()));
            //大车(黄牌)
            parkCharge.put(park.getId()+"1"+ChargeTypeEnum.YELLOW.getValue(),chargeMap.get(park.getYellow_charge_id()));
        }
        //路段收费方案
        RedisUtils.setBeanValue("parkCharge",parkCharge);
    }

}
