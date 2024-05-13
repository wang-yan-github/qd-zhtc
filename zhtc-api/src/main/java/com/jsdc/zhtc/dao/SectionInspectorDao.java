package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.model.SectionInspector;
import org.springframework.stereotype.Repository;

/**
 * @author 王严
 * @version 1.0
 * @description: 路段巡检员关联
 */
@Repository
public class SectionInspectorDao extends BaseDao<SectionInspector> {

    /**
     * 获取巡检员信息
     *
     * @param allocatedSectionIds
     * @return
     */
    public String getInspectManageList(String allocatedSectionIds, String parking_type) {
        String sql = "SELECT i.id, si.allocated_section_id, i.name, i.phone, CONVERT(varchar,i.create_time,120) AS create_time\n" +
                "FROM section_inspector si\n" +
                "         INNER JOIN inspect_manage i on si.inspect_id = i.id AND i.is_del = 0 AND si.parking_type = " + parking_type + "\n" +
                "WHERE si.allocated_section_id = " + allocatedSectionIds + "\n";
        return sql;
    }


    /**
     * create by zonglina at 2022/1/13 11:32
     * description:根据巡检员查询路段信息
     *
     * @return : null
     * @param:personType 区域类型
     * @param:inspect_id 巡检员id
     */
    public String selectByRoadOrParkList(String personType, Integer inspect_id) {
        String sql = "";
        sql += "select s.allocated_section_id,r.park_name as road_name,begin_time,end_time from section_inspector s  LEFT JOIN park r on r.id = s.allocated_section_id ";
        sql += " LEFT JOIN inspect_manage m on  m.id = s.inspect_id and m.personType='" + personType + "' where m.is_del = 0 and inspect_id = '" + inspect_id + "' and r.id is not null";
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }
}
