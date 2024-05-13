package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.model.InspectFeedback;
import com.jsdc.zhtc.vo.InspectFeedbackVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: InspectFeedbackDao
 * Description:巡检反馈管理表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class InspectFeedbackDao extends BaseDao<InspectFeedback> {

    public String selectPageList(InspectFeedbackVo bean, String redisRoadOrPark) {
        String sql = "select mag.*,mt.name as name from inspect_feedback mag LEFT JOIN inspect_manage mt  on mag.inspect_id = mt.id " +
                " LEFT JOIN section_inspector aec on mag.id = aec.id  ";

        sql += " LEFT JOIN parking_order r on r.park_id  where mag.is_del = 0 and mt.personType='" + redisRoadOrPark + "' ";

        if (notEmpty(bean)) {
            if (notEmpty(bean.getInspect_id())) {
                sql += " and mag.inspect_id = " + bean.getInspect_id();
            }
            if (notEmpty(bean.getKeyword())) {
                sql += " and mt.job_no like '%" + bean.getKeyword() + "%' or mt.name like '%" + bean.getKeyword() + "%'";
            }
            if (notEmpty(bean.getArea_id())) {
                sql += " and r.area_id = " + bean.getArea_id();
            }
            if (notEmpty(bean.getStreet_id())) {
                sql += " and r.street_id = " + bean.getArea_id();
            }

            if (notEmpty(bean.getStart())) {
                sql += " and aec.start >= " + bean.getStart();
            }
            if (notEmpty(bean.getEnd())) {
                sql += " and aec.end <= " + bean.getEnd();
            }
        }
        sql += " order by mag.fkstate asc, mag.feedback_time desc";

        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

    public String selectInspectFeedback(InspectFeedbackVo bean, String redisRoadOrPark) {
        String sql = "select mag.*,mt.name as name from inspect_feedback mag LEFT JOIN inspect_manage mt  on mag.inspect_id = mt.id " +
                " LEFT JOIN section_inspector aec on mag.id = aec.id  ";

        sql += " LEFT JOIN parking_order r on r.park_id  where mag.is_del = 0 and mt.personType='" + redisRoadOrPark + "' ";

        if (notEmpty(bean)) {
            if (notEmpty(bean.getId())) {
                sql += " and mag.id = " + bean.getId();
            }
            if (notEmpty(bean.getInspect_id())) {
                sql += " and mag.inspect_id = " + bean.getInspect_id();
            }
            if (notEmpty(bean.getKeyword())) {
                sql += " and mt.job_no like '%" + bean.getKeyword() + "%' or mt.name like '%" + bean.getKeyword() + "%'";
            }
            if (notEmpty(bean.getArea_id())) {
                sql += " and r.area_id = " + bean.getArea_id();
            }
            if (notEmpty(bean.getStreet_id())) {
                sql += " and r.street_id = " + bean.getArea_id();
            }

            if (notEmpty(bean.getStart())) {
                sql += " and aec.start >= " + bean.getStart();
            }
            if (notEmpty(bean.getEnd())) {
                sql += " and aec.end <= " + bean.getEnd();
            }
        }
        sql += " order by fkstate ";
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }


    public String selectByFeedbackId(Integer id) {
        String sql = "select mag.*,mt.name as name,mt.job_no as job_no from inspect_feedback mag " +
                " LEFT JOIN inspect_manage mt  on mag.inspect_id=mt.id  where mag.is_del = 0 ";
        if (notEmpty(id)) {
            sql += " and mag.id  =" + id;
        }
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }
}
