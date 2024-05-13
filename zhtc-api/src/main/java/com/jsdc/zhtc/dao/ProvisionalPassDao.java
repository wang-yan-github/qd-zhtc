package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.ProvisionalPass;
import com.jsdc.zhtc.vo.ProvisionalPassVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: ProvisionalPassDao
 * Description:
 * date: 2022/2/24 9:48
 *
 * @author bn
 */
@Repository
public class ProvisionalPassDao extends BaseDao<ProvisionalPass> {

    public String toList(ProvisionalPassVo passVo) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT pp.*, p.park_name,p.address ");
        sql.append("FROM provisional_pass pp LEFT JOIN park p ON pp.park_id = p.id ");
        sql.append("where pp.is_del='0' ");
        if (notEmpty(passVo.getCompanyName())) {
            sql.append(" AND pp.companyName like '%" + passVo.getCompanyName() + "%' ");
        }
        if (notEmpty(passVo.getPark_id())) {
            sql.append(" AND pp.park_id= " + passVo.getPark_id());
        }


        return sql.toString();
    }
}
