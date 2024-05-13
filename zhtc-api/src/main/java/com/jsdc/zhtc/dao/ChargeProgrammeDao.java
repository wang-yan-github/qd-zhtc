package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.ChargeProgramme;
import com.jsdc.zhtc.vo.ChargeProgrammeVo;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * ClassName: ChargeProgrammeDao <br/>
 * Description: <br/>
 * date: 2021/12/30 11:04<br/>
 *
 * @author bn<br               />
 */
@Repository
public class ChargeProgrammeDao extends BaseDao<ChargeProgramme> {


    public String toList(ChargeProgrammeVo chargeProgrammeVo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cp.*,su.user_name as name from charge_programme cp left join sys_user su on cp.create_user=su.id where 1=1 ");
        if (StringUtils.isNotEmpty(chargeProgrammeVo.getProgramme_name())) {
            sql.append(" AND cp.programme_name like '%" + chargeProgrammeVo.getProgramme_name() + "%' ");
        }
        if (!CollectionUtils.isEmpty(chargeProgrammeVo.getIds())) {
            sql.append(" AND cp.id in ( ");
            for (Integer id : chargeProgrammeVo.getIds()) {
                // 判断是否为最后一个
                if (id.equals(chargeProgrammeVo.getIds().get(chargeProgrammeVo.getIds().size() - 1))) {
                    sql.append(" " + id);
                } else {
                    sql.append(id + ", ");
                }
            }
            sql.append(") ");
        }
        sql.append(" AND cp.is_del=0 ");
        return sql.toString();
    }
}
