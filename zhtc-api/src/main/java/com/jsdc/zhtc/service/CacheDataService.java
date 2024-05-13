package com.jsdc.zhtc.service;

import com.jsdc.zhtc.model.SysDict;
import com.jsdc.zhtc.utils.RedisUtils;
import com.jsdc.zhtc.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
    private AreaService areaService;
    @Autowired
    private StreetService streetService;


    @Autowired
    private ParkService parkService;


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


}
