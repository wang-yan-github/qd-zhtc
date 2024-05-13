package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.CarnoCompany;
import org.springframework.stereotype.Repository;

/**
 * 类 名: 车牌企业映射
 * 描 述: CarnoCompanyDao
 * 作 者: lw
 * 创 建：2022/1/4 14:09
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Repository
public class CarnoCompanyDao extends BaseDao<CarnoCompany> {


    public String getCompanyCars(Integer companyId) {
        StringBuffer sqlStr = new StringBuffer("");
        sqlStr.append("select oc.company_name companyName,b.*,c.nick_name memberName from carno_company a\n" +
                "left join operate_carno b\n" +
                "on a.carno_id = b.id \n" +
                "left join member_manage c\n" +
                "on b.member_id = c.id \n" +
                "left join operate_company oc on a.company_id = oc.id where 1=1\n"
        );
        if (companyId != null) {
            sqlStr.append("and a.company_id=" + companyId);
        }
        return sqlStr.toString();
    }

    public String getByCompanyCars(String monthly_code) {
        StringBuffer sqlStr = new StringBuffer("");
        sqlStr.append("select b.car_no car_no,b.car_type car_type,b.roster_type roster_type from monthly_management a\n" +
                "left join operate_carno b\n" +
                "on a.carno_id = b.id \n" +
                " where 1=1\n"
        );
        if (StringUtils.isNotEmpty(monthly_code)) {
            sqlStr.append("and a.monthly_code= '" + monthly_code + "'");
        }
        return sqlStr.toString();
    }


}
