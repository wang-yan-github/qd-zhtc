package com.jsdc.zhtc.dao;

import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.CarnoAppeal;
import com.jsdc.zhtc.vo.CarnoAppealVo;
import org.springframework.stereotype.Repository;

/**
 * @author 王严
 * @version 1.0
 * @description: 车牌申诉
 */
@Repository
public class CarnoAppealDao extends BaseDao<CarnoAppeal> {

    public String listCarnoAppeal(CarnoAppealVo vo) {
        String sql = "SELECT ca.id,\n" +
                "       ca.carno_id,\n" +
                "       ca.create_user,\n" +
                "       CONVERT(varchar,ca.create_time,120) AS create_time,\n" +
                "       ca.driveringlic_id,\n" +
                "       ca.driverlic_id,\n" +
                "       ca.is_del,\n" +
                "       ca.member_id,\n" +
                "       ca.reject_reason,\n" +
                "       ca.remark,\n" +
                "       ca.status,\n" +
                "       CONVERT(varchar,ca.update_time,120) AS update_time,\n" +
                "       ca.update_user,\n" +
                "       ca.verifier,\n" +
                "       oc.bind_type,\n" +
//                "       oc.car_natures,\n" +
                "       oc.car_no,\n" +
                "       oc.car_type,\n" +
                "       oc.roster_type,\n" +
                "       mm.phone,\n" +
                "       su.login_name,\n" +
                "       difm.file_url AS driveringlic_url,\n" +
                "       dlfm.file_url AS driverlic_url\n" +
                "FROM dbo.carno_appeal AS ca\n" +
                "         LEFT JOIN\n" +
                "     dbo.operate_carno AS oc\n" +
                "     ON\n" +
                "         ca.carno_id = oc.id\n" +
                "         LEFT JOIN\n" +
                "     dbo.member_manage AS mm\n" +
                "     ON\n" +
                "         ca.member_id = mm.id\n" +
                "         LEFT JOIN\n" +
                "     dbo.file_manage AS difm\n" +
                "     ON\n" +
                "         ca.driveringlic_id = difm.id\n" +
                "         LEFT JOIN\n" +
                "     dbo.file_manage AS dlfm\n" +
                "     ON\n" +
                "         ca.driverlic_id = dlfm.id\n" +
                "         LEFT JOIN\n" +
                "     dbo.sys_user AS su\n" +
                "     ON\n" +
                "         ca.verifier = su.id\n" +
                "where 1 = 1\n";
        if (StringUtils.isNotEmpty(vo.getCarNo())) {
            sql += " and oc.car_no like '%" + vo.getCarNo() + "%' ";
        }
        if (Base.notEmpty(vo.getStatus())) {
            if (vo.getStatus().equals("-1")) {
                sql += " and (ca.status = " + vo.getStatus() + " OR ca.status is null)";
            } else {
                sql += " and ca.status = " + vo.getStatus();
            }
        }
        if (vo.getIsYellowCard() == true) {
            sql += " and oc.car_type = 3 ";
        }
        if (StringUtils.isNotEmpty(vo.getPhone())) {
            sql += " and mm.phone like '%" + vo.getPhone() + "%' ";
        }
        sql += "order by ca.create_time desc";
        return sql;
    }

    /**
     * 微信小程序
     * 车牌申诉分页查询
     *
     * @author thr
     */
    public String getPageList(CarnoAppeal bean) {
        String sql = "SELECT ca.*,\n" +
                " oc.car_no,\n" +
                " f.file_url AS xszPicUrl,\n" +
                " fm.file_url AS jszPicUrl\n" +
                " FROM dbo.carno_appeal AS ca\n" +
                " LEFT JOIN\n" +
                " dbo.operate_carno AS oc\n" +
                " ON\n" +
                " ca.carno_id = oc.id\n" +
                " LEFT JOIN\n" +
                " dbo.file_manage AS f\n" +
                " ON\n" +
                " ca.driveringlic_id = f.id\n" +
                " LEFT JOIN\n" +
                " dbo.file_manage AS fm\n" +
                " ON\n" +
                " ca.driverlic_id = fm.id\n" +
                " where 1 = 1\n";
        if (Base.notEmpty(bean.getMember_id())) {
            sql += " and ca.member_id = " + bean.getMember_id();
        }
        sql += " order by ca.status,ca.create_time desc";
        return sql;
    }

    /**
     * 近七日服务类型趋势 车牌申诉 最近7日/数量
     */
    public String getDaysCount() {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT\n" +
                "\tisnull( b.count, 0 ) AS counts,\n" +
                "\tc.click_date AS name \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY, 0, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 1, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 2, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 3, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 4, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 5, getdate()), 23 ) AS click_date UNION ALL\n" +
                "\tSELECT CONVERT\n" +
                "\t\t( VARCHAR ( 100 ), dateadd( DAY,- 6, getdate()), 23 ) AS click_date \n" +
                "\t) c\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT COUNT\n" +
                "\t\t( id ) AS COUNT,\n" +
                "\t\tCONVERT ( VARCHAR ( 100 ), pr.create_time, 23 ) AS days \n" +
                "\tFROM\n" +
                "\t\tcarno_appeal pr \n" +
                "\tWHERE\n" +
                "\t\tis_del = 0 \n" +
                "\tGROUP BY\n" +
                "\t\tCONVERT ( VARCHAR ( 100 ), pr.create_time, 23 ) \n" +
                "\t) b ON c.click_date = b.days \n" +
                "ORDER BY\n" +
                "\tc.click_date");
        return sqlBuffer.toString();
    }
}
