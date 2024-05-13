package com.jsdc.zhtc.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.zhtc.dao.SectionInspectorDao;
import com.jsdc.zhtc.mapper.SectionInspectorMapper;
import com.jsdc.zhtc.model.SectionInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * ClassName: SectionInspectorService
 * Description:
 * date: 2022/1/4 13:39
 *
 * @author wp
 */
@Service
@Transactional
public class SectionInspectorService extends BaseService<SectionInspectorDao, SectionInspector> {

    @Autowired
    private SectionInspectorMapper mapper;


    /**
     * 获取巡检员信息
     */
    public List<Map<String, String>> getInspectManageList(String allocatedSectionIds, String parking_type) {
        List<Map<String, String>> result = mapper.getInspectManageList(allocatedSectionIds, parking_type);
        return result;
    }

    /**
     * create by zonglina at 2022/1/13 11:32
     * description:根据巡检员查询路段信息
     *
     * @return : null
     * @param:personType 区域类型
     * @param:inspect_id 巡检员id
     */
    public List<SectionInspector> selectByRoadOrParkList(String personType, Integer inspect_id) {
        return mapper.selectByRoadOrParkList(personType, inspect_id);
    }


}
