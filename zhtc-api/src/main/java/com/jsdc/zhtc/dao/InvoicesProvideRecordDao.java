package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.InvoicesProvideRecord;
import com.jsdc.zhtc.vo.InvoicesProvideRecordVo;
import org.springframework.stereotype.Repository;

/**
 * ClassName: InvoicesProvideRecordDao
 * Description:
 * date: 2022/1/17 11:48
 *
 * @author bn
 */
@Repository
public class InvoicesProvideRecordDao extends BaseDao<InvoicesProvideRecord> {

    public String toList(InvoicesProvideRecordVo invoicesProvideRecord) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT record.*,im.name");

        sql.append(",ope.car_no,ope.car_type,o.drivein_time,o.driveout_time,o.resitime,o.sum_amount,o.status ");
        sql.append(",o.paid_amount,o.pay_type,o.berth ");

        sql.append(",park.park_name ");
        sql.append("FROM invoices_provide_record record ");
        sql.append("INNER JOIN parking_order o ON record.order_no=o.order_no ");
        sql.append("LEFT JOIN park ON o.park_id=park.id ");
        sql.append("LEFT JOIN operate_carno ope ON o.carno_id=ope.id ");
        sql.append("LEFT JOIN inspect_manage im on record.inspect_id=im.id ");
        sql.append(" where 1=1 ");
        if (invoicesProvideRecord.getPark_id() != null) {
            sql.append(" AND o.park_id= " + invoicesProvideRecord.getPark_id());
        }

        sql.append(" AND record.parking_type='" + invoicesProvideRecord.getParking_type() + "'");


        if (StringUtils.isNotEmpty(invoicesProvideRecord.getCar_no())) {
            sql.append(" AND ope.car_no = '" + invoicesProvideRecord.getCar_no() + "'");
        }
        if (invoicesProvideRecord.getArea_id() != null) {
            sql.append(" AND o.area_id= " + invoicesProvideRecord.getArea_id());
        }

        if (invoicesProvideRecord.getStreet_id() != null) {
            sql.append(" AND o.street_id=" + invoicesProvideRecord.getStreet_id());
        }

        if (StringUtils.isNotEmpty(invoicesProvideRecord.getOrder_no())) {
            sql.append(" AND (record.order_no like '%" + invoicesProvideRecord.getOrder_no() + "%'");
            sql.append(" OR im.name like '%" + invoicesProvideRecord.getOrder_no() + "%')");
        }

        if (StringUtils.isNotEmpty(invoicesProvideRecord.getStart_time())) {
            sql.append("  and CONVERT(varchar, record.provide_time, 23) >=  '").append(invoicesProvideRecord.getStart_time()).append("' ");

        }
        if (StringUtils.isNotEmpty(invoicesProvideRecord.getEnd_time())) {
            sql.append("  and CONVERT(varchar, record.provide_time, 23) <=  '").append(invoicesProvideRecord.getEnd_time()).append("' ");

        }

        sql.append(" order by provide_time desc ");

        return sql.toString();

    }

}
