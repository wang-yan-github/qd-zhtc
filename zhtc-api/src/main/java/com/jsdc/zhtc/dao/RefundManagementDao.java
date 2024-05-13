package com.jsdc.zhtc.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.RefundManagement;
import com.jsdc.zhtc.vo.CommonVo;
import com.jsdc.zhtc.vo.RefundManagementVo;
import org.springframework.stereotype.Repository;

/**
 * 类 名: 退款管理
 * 描 述: RefundManagementDao
 * 作 者: lw
 * 创 建：2021/12/30 14:46
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Repository
public class RefundManagementDao extends BaseDao<RefundManagement> {

    /**
     * 描 述： TODO(查询退款记录)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param commonVo str 订单号 str2 手机号  str3 车牌号
     * @return {@link String}
     */
    public String selectAll(CommonVo commonVo) {
        StringBuffer sqlbd = new StringBuffer();
        sqlbd.append("select road.order_no  as orderNo,payment_serialno,refund_amount,refund_channel,refund_time,oc.car_no  as carNo,mg.phone" +
                " ,oc.car_type AS car_type from refund_management mt ");
        sqlbd.append(" LEFT JOIN operate_carno oc on oc.id=mt.carno_id LEFT JOIN member_manage mg on mg.id=mt.member_id");

        sqlbd.append(" LEFT JOIN parking_order road on road.id = mt.parking_order_id ");
        sqlbd.append(" where mt.is_del=0  and park_type='1'");

        if (StringUtils.isNotBlank(commonVo.getCarNo()))
            sqlbd.append(" and oc.car_no like '%" + commonVo.getCarNo() + "%'");
        if (StringUtils.isNotBlank(commonVo.getPhone()))
            sqlbd.append(" and mg.phone like '%" + commonVo.getPhone() + "%'");
        if (StringUtils.isNotBlank(commonVo.getOrderNo()))
            sqlbd.append(" and road.order_no like '%" + commonVo.getOrderNo() + "%' ");
        sqlbd.append(" order by mt.refund_time desc");
        return sqlbd.toString();
    }


    /**
     * 支付流水明细关联退款记录
     *
     * @author thr
     */
    public String getRefundList(RefundManagementVo vo) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT\n" +
                "\tt1.*,\n" +
                "\trp.orderNo \n" +
                "FROM\n" +
                "\trefund_management t1\n" +
                "\tLEFT JOIN road_park_list rp ON t1.parking_order_id = rp.id \n" +
                "\tAND t1.park_type = rp.type \n" +
                "WHERE\n" +
                "\tt1.is_del = '0'");
        if (notEmpty(vo.getPayment_id())) {
            sql.append(" and t1.payment_id = ").append(vo.getPayment_id());
        }
        sql.append(" order by t1.create_time desc");
        return sql.toString();
    }
}
