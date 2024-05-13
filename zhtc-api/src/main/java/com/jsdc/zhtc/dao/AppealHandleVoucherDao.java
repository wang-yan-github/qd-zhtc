package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.zhtc.model.AppealHandleVoucher;
import org.springframework.stereotype.Repository;

/**
 * ClassName: AppealHandleVoucherDao
 * Description:申诉驳回凭证表
 * date: 2021/12/30 10:33
 *
 * @author zln
 */
@Repository
public class AppealHandleVoucherDao extends BaseDao<AppealHandleVoucher> {

    //根据id获取所有图片
    public String selectByPid(Integer handle_record_id) {
        String sql = "select picture_id from appeal_handle_voucher mag where is_del = 0 ";
        if (notEmpty(handle_record_id)) {
            sql += " and mag.handle_record_id  =" + handle_record_id;
        }
        QueryHandler queryHandler = getQueryHandler(sql);
        return queryHandler.getSql();
    }

}
