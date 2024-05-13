package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.Area;
import org.springframework.stereotype.Repository;

/**
 * 区域
 */
@Repository
public class AreaDao extends BaseDao<Area> {

    /**
     * 描 述： TODO(获取区信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param id
     * @return {@link String}
     */
    public String getAll(Integer id) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select * from area where 1=1 ");
        if (id != null)
            sqlbd.append(" and id = ").append(id);

        sqlbd.append(" and is_del = ").append(GlobalData.ISDEL_NO);
        sqlbd.append(" and status = 0");
        System.out.println(sqlbd.toString());
        return sqlbd.toString();
    }

    public String toList(Area area) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT *, ");
        sql.append("(SELECT count(*) from street where street.area_id=area.id and street.is_del= '0' and street.status='0') street_num from area ");
        sql.append("where 1=1 ");
        if (StringUtils.isNotEmpty(area.getArea_name())) {
            sql.append(" AND area.area_name LIKE '%" + area.getArea_name() + "%'");
        }
        sql.append(" AND area.is_del=0");

        return sql.toString();
    }
}
