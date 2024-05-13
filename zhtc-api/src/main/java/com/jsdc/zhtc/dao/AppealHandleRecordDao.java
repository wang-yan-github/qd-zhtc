package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.model.AppealHandleRecord;
import org.springframework.stereotype.Repository;

/**
 * ClassName: OperateHandleRecordDao
 * Description:申诉处理记录表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class AppealHandleRecordDao extends BaseDao<AppealHandleRecord> {

    public String selectParm(Integer id) {
        String sql = "select * from appeal_handle_record where id = '" + id + "' and is_del = 0 ";
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

    /**
     * 根据条件查询
     *
     * @return
     */
    public String selectByAppealHandleRecord(Integer id) {
        String sql = "select * from appeal_handle_record where approve_status = '" + GlobalData.OPERATE_APPEAL_DCL + "' and is_del = '0' and id ='" + id + "'";
        return getQueryHandler(sql).getSql();
    }

    /**
     * 订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 4.修正车牌处理
     */
    public String getCountByType() {
        String sql = "SELECT COUNT\n" +
                "\t( t.id ) counts,\n" +
                "\tCASE\n" +
                "\t\thandle_type \n" +
                "\t\tWHEN '1' THEN\n" +
                "\t\t'按结束时间处理' \n" +
                "\t\tWHEN '2' THEN\n" +
                "\t\t'按订单费用处理' \n" +
                "\t\tWHEN '3' THEN\n" +
                "\t\t'按退款处理' \n" +
                "\t\tWHEN '5' THEN\n" +
                "\t\t'按修正车牌处理' " +
//                "ELSE '其他' \n" +
                "\tEND AS name \n" +
                "FROM\n" +
                "\tappeal_handle_record t \n" +
                "WHERE\n" +
                "\tt.is_del = 0 \n" +
                "\t and handle_type is not null \n" +
//                "\t and CONVERT(varchar, t.create_time, 23) = '" + new DateTime().toString("yyyy-MM-dd") + "'" +
                " GROUP BY\n" +
                "\tt.handle_type";
        return getQueryHandler(sql).getSql();
    }
}
