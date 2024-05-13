import request from '../utils/request';

/**
 * 支付流水明细分页查询
 */
export const paymentOrderPageList = query => {
    return request({
        url: '/paymentOrder/toList.do',
        method: 'post',
        params: query
    });
};

/**
 * 支付流水明细详情
 */
export const paymentOrderView = query => {
    return request({
        url: '/paymentOrder/toView.do',
        method: 'post',
        params: query
    });
};

/**
 * 支付流水明细关联 包月订单
 */
export const getMonthlyDataByPaymentId = query => {
    return request({
        url: '/monthlymanagement/getMonthlyDataByPaymentId.do',
        method: 'post',
        params: query
    });
};

/**
 * 支付流水明细关联 充值记录列表
 */
export const getRechargeDataByPaymentId = query => {
    return request({
        url: '/rechargemanagement/getRechargeDataByPaymentId.do',
        method: 'post',
        params: query
    });
};

/**
 * 支付流水明细关联 停车订单列表
 */
export const getOrderDataByPaymentId = query => {
    return request({
        url: '/parkingOrder/getOrderDataByPaymentId.do',
        method: 'post',
        params: query
    });
};


/**
 * 支付流水明细关联 商户充值记录
 */
export const getStoreRechargeDataByPaymentId = query => {
    return request({
        url: '/storeRechargeRecord/getStoreRechargeDataByPaymentId.do',
        method: 'post',
        params: query
    });
};

/**
 * 支付流水明细关联 退款记录
 */
export const getRefundDataByPaymentId = query => {
    return request({
        url: '/refundmanagement/getRefundDataByPaymentId.do',
        method: 'post',
        params: query
    });
};

/**
 * 支付流水明细导出Excel
 */
export const exportPaymentOrder = query => {
    return request({
        url: '/paymentOrder/exportPaymentOrder.do',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};


