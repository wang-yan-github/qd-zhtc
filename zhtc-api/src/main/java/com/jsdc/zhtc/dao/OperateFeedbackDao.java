package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.model.OperateFeedback;
import com.jsdc.zhtc.vo.OperateFeedbackVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: OperateFeedbackDao
 * Description:反馈管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class OperateFeedbackDao extends BaseDao<OperateFeedback> {

    /**
     * 分页查询
     *
     * @param bean
     * @return
     */
    public String selectPageList(OperateFeedbackVo bean) {
        String sql = "select o.*,m.phone as phone from operate_feedback o " +
                " LEFT JOIN member_manage m on m.id = o.member_id where o.is_del=0 ";
        if (notEmpty(bean)) {
            if (notEmpty(bean.getFeedback_content())) {
                sql += " and o.feedback_content like '%" + bean.getFeedback_content() + "%'";
            }
            if (notEmpty(bean.getCarno())) {
                sql += " and o.carno like '%" + bean.getCarno() + "%'";
            }
            if (notEmpty(bean.getMember_id())) {
                sql += " and o.member_id = " + bean.getMember_id();
            }
        }
        sql += " order by o.id desc";
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

    /**
     * 近七日服务类型趋势 车主反馈 最近7日/数量
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
                "\t\toperate_feedback pr \n" +
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
