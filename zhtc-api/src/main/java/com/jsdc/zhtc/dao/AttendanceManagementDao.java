package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.model.AttendanceManagement;
import org.springframework.stereotype.Repository;

/**
 * 考勤管理
 *
 * @Author thr
 * @create 2022-08-24 14:08:08
 */
@Repository
public class AttendanceManagementDao extends BaseDao<AttendanceManagement> {

    public String toList(AttendanceManagement bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.*,m.name userName FROM attendance_management t");
        sql.append(" left join inspect_manage m on m.id = t.create_user");
        sql.append(" where t.is_del = 0 and m.is_del = 0");
        if (notEmpty(bean)) {
            if (notEmpty(bean.getCreate_user())) {
                sql.append(" and t.create_user = ").append(bean.getCreate_user());
            }
            if (notEmpty(bean.getUserName())) {
                sql.append(" and m.name like '%").append(bean.getUserName()).append("%'");
            }
            if (notEmpty(bean.getIs_late())) {
                sql.append(" and t.is_late = '").append(bean.getIs_late()).append("'");
            }
            if (notEmpty(bean.getIs_leave_early())) {
                sql.append(" and t.is_leave_early = '").append(bean.getIs_leave_early()).append("'");
            }
            if (notEmpty(bean.getIs_absenteeism())) {
                sql.append(" and t.is_absenteeism = '").append(bean.getIs_absenteeism()).append("'");
            }
            if (notEmpty(bean.getType())) {
                sql.append(" and t.type = '").append(bean.getType()).append("'");
            }
            if (notEmpty(bean.getSb_start_time())) {
                sql.append(" and CONVERT(varchar, t.start_time, 20) >= '").append(bean.getSb_start_time()).append("' ");
            }
            if (notEmpty(bean.getSb_end_time())) {
                sql.append(" and CONVERT(varchar, t.start_time, 20) <= '").append(bean.getSb_end_time()).append("' ");
            }
            if (notEmpty(bean.getXb_start_time())) {
                sql.append(" and CONVERT(varchar, t.end_time, 20) >= '").append(bean.getXb_start_time()).append("' ");
            }
            if (notEmpty(bean.getXb_end_time())) {
                sql.append(" and CONVERT(varchar, t.end_time, 20) <= '").append(bean.getXb_end_time()).append("' ");
            }
            if (notEmpty(bean.getCreate_start_time())) {
                sql.append(" and CONVERT(varchar, t.create_time, 23) >= '").append(bean.getCreate_start_time()).append("' ");
            }
            if (notEmpty(bean.getCreate_end_time())) {
                sql.append(" and CONVERT(varchar, t.create_time, 23) <= '").append(bean.getCreate_end_time()).append("' ");
            }
            if (notEmpty(bean.getStrTime())) {
                sql.append(" and CONVERT(varchar, t.create_time, 23) = '").append(bean.getStrTime()).append("' ");
            }

//            select convert(varchar,GetDate(),23) as Date yyyy-MM-dd
//            select convert(varchar,GetDate(),20) as Date yyyy-MM-dd HH:mm:ss
//            select convert(char(16),GetDate(),120) as Date --截取年月日 yyyy-MM-dd HH:mm
        }
        sql.append(" order by t.create_time desc,t.id desc");
        return sql.toString();
    }

    /**
     * 考勤统计
     */
    public String toReport(AttendanceManagement bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tm.id,\n" +
                "\tMAX ( m.name ) userName,\n" +
                "\tCOUNT ( t.id ) days,\n" +
                "\tSUM ( CASE WHEN is_late = 1 THEN 1 ELSE 0 END ) lateDays,\n" +
                "\tSUM ( CASE WHEN is_leave_early = 1 THEN 1 ELSE 0 END ) leaveEarlyDays,\n" +
                "\tSUM ( CASE WHEN is_absenteeism = 1 THEN 1 ELSE 0 END ) absenteeismDays\n" +
                "\tFROM\n" +
                "\t\tattendance_management t\n" +
                "\t\tLEFT JOIN inspect_manage m ON m.id = t.create_user \n" +
                "\tWHERE\n" +
                "\t\tt.is_del = 0 \n" +
                "\t\tAND m.is_del = 0 \n");
        if (notEmpty(bean)) {
            if (notEmpty(bean.getType())) {
                sql.append(" and t.type = '").append(bean.getType()).append("'");
            }
            //yyyy-MM
            if (notEmpty(bean.getStrTime())) {
                sql.append(" and CONVERT(char(7), t.create_time, 120) = '").append(bean.getStrTime()).append("' ");
            }
        }
        sql.append("GROUP BY\n" +
                "\tm.id");
        return sql.toString();
    }
}
