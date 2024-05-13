package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.Street;
import org.springframework.stereotype.Repository;

/**
 * ClassName: StreetDao <br/>
 * Description: <br/>
 * date: 2021/12/30 10:53<br/>
 *
 * @author bn<br       />
 */
@Repository
public class StreetDao extends BaseDao<Street> {

    /**
     * 描 述： TODO(获取街道信息)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param id
     * @return {@link String}
     */
    public String getAll(Integer id) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append(" select * from street where 1=1 ");
        if (id != null)
            sqlbd.append(" and area_id = ").append(id);
        sqlbd.append(" and is_del = ").append(GlobalData.ISDEL_NO);
        sqlbd.append(" and status = 0");
        System.out.println(sqlbd.toString());
        return sqlbd.toString();
    }

    public String toList(Street street) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT street.*,area.city,area.area_name, ");
        sql.append("from street LEFT JOIN area on street.area_id=area.id where 1=1  ");
        if (StringUtils.isNotEmpty(street.getStreet_name())) {
            sql.append("AND street.street_name like '%" + street.getStreet_name() + "%' ");
        }
        if (street.getArea_id() != null) {
            sql.append("AND street.area_id=" + street.getArea_id());
        }
        sql.append("AND street.is_del=0");

        return sql.toString();


    }

}
