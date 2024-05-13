package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.MemberManage;
import com.jsdc.zhtc.vo.MemberManageVo;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName: MemberManageDao <br/>
 * Description: <br/>
 * date: 2022/1/4 11:57<br/>
 *
 * @author bn<br                                                               />
 */
@Repository
public class MemberManageDao extends BaseDao<MemberManage> {

    public String toList(MemberManageVo memberManageVo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct mm.* from member_manage mm ");
        if (StringUtils.isNotEmpty(memberManageVo.getCar_no())) {
            sql.append("LEFT JOIN operate_carno oc ON mm.id=oc.member_id  ");
        }
//        sql.append("LEFT JOIN operate_carno oc ON mm.id=oc.member_id  ");
        sql.append("where 1=1 ");
        if (StringUtils.isNotEmpty(memberManageVo.getPhone())) {
            sql.append(" AND mm.phone like '%" + memberManageVo.getPhone() + "%'");
        }
        if (StringUtils.isNotEmpty(memberManageVo.getCar_no())) {
            sql.append(" AND oc.car_no like '%" + memberManageVo.getCar_no() + "%'");
        }
        if (StringUtils.isNotEmpty(memberManageVo.getStart_time())) {
            sql.append(" and CONVERT(varchar(10),mm.register_time,120) >= '" + memberManageVo.getStart_time() + "'");

        }
        if (StringUtils.isNotEmpty(memberManageVo.getEnd_time())) {
            sql.append(" and CONVERT(varchar(10),mm.register_time,120)<= '" + memberManageVo.getEnd_time() + "'");

        }

        sql.append(" AND mm.is_del=0");
        sql.append(" order by mm.register_time desc");
        return sql.toString();
    }

    /**
     * 描 述： TODO(查询当日注册人数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param
     * @return {@link String}
     */
    public String getTodayAddCount() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatTime = sdf.format(new Date());

        StringBuffer sqlbd = new StringBuffer();

        sqlbd.append(" select count(1) counts from member_manage ");
        sqlbd.append(" where CONVERT(varchar, register_time, 23) =  '").append(formatTime).append("' ");


        return sqlbd.toString();
    }
}
