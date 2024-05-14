package com.jsdc.zhtc.utils;

import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.vo.ChargeVo;

import java.util.*;

/**
 * 获取redis缓存数据(字典、区域、街道、路段、停车场、路段(停车场)收费方案)
 *
 * @ClassName DcCacheDataUtil
 * @Description TODO
 * @Author xujian
 * @Date 2022/1/6 15:30
 * @Version 1.0
 */
public class DcCacheDataUtil {

    /**
     * 根据字典类型获取字典集合
     *
     * @param dictType 字典类型
     * @return
     */
    public static List<SysDict> getDictByDictType(String dictType) {
        List<SysDict> sysDicts = new ArrayList<>();
        Map<String, HashMap> dicData = (Map<String, HashMap>) RedisUtils.getBeanValue("dictData");
        HashMap dicts = dicData.get(dictType);
        Iterator iterator = dicts.values().iterator();
        while (iterator.hasNext()) {
            SysDict sysDict = (SysDict) iterator.next();
            sysDicts.add(sysDict);
        }
        return sysDicts;
    }

    /**
     * 根据字典类型获取对应Hash字典，用于根据key翻译字典值
     *
     * @param dictType 字典类型 value 字典value
     * @return
     */
    public static HashMap<String, SysDict> getMapDicts(String dictType) {
        Map<String, HashMap> dicData = (Map<String, HashMap>) RedisUtils.getBeanValue("dictData");
        HashMap<String, SysDict> dictMap = dicData.get(dictType);
        return dictMap;
    }


    /**
     * 获取停车场的map对象，可根据停车场ID作为key获取停车场信息
     *
     * @return
     */
    public static HashMap<Integer, Park> getMapParks() {
        HashMap<Integer, Park> parkMap = (HashMap<Integer, Park>) RedisUtils.getBeanValue("parkData");
        return parkMap;
    }


    /**
     * 获取所有停车场信息
     *
     * @return
     */
    public static List<Park> getAllParks() {
        List<Park> parkList = new ArrayList<>();
        HashMap<Integer, Park> parkMap = (HashMap<Integer, Park>) RedisUtils.getBeanValue("parkData");
        Iterator iterator = parkMap.values().iterator();
        while (iterator.hasNext()) {
            parkList.add((Park) iterator.next());
        }
        return parkList;
    }

    /**
     * 通过路段(停车场)ID和收费类型获取对应的收费方案
     *
     * @param road_park_id 路段或者停车场ID
     * @param chargeType   1收费类型 普牌（蓝牌）、2绿牌(绿牌)、3大车(黄牌)
     * @param parkType     停车类型 0：路段 1：停车场
     * @return
     */
    public static ChargeVo getChargeById(String road_park_id, String parkType, String chargeType) {
        HashMap parkCharge = (HashMap) RedisUtils.getBeanValue("parkCharge");
        if (parkCharge.containsKey(road_park_id + parkType + chargeType)) {
            ChargeVo chargeVo = (ChargeVo) parkCharge.get(road_park_id + parkType + chargeType);
            return chargeVo;
        } else {
            return null;
        }
    }

}
