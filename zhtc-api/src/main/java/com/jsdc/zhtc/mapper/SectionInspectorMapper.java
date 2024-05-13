package com.jsdc.zhtc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.zhtc.dao.SectionInspectorDao;
import com.jsdc.zhtc.model.SectionInspector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @author 王严
 * @version 1.0
 * @description: 路段巡检员关联
 */
@Mapper
public interface SectionInspectorMapper extends BaseMapper<SectionInspector> {

    @SelectProvider(method = "getInspectManageList", type = SectionInspectorDao.class)
    List<Map<String, String>> getInspectManageList(String allocatedSectionIds, String parking_type);

    // 根据巡检员查询路段信息
    @SelectProvider(method = "selectByRoadOrParkList", type = SectionInspectorDao.class)
    List<SectionInspector> selectByRoadOrParkList(String personType, Integer inspect_id);


}
