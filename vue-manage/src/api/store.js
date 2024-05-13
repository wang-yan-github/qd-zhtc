import request from '../utils/request';

//**********************************路内zln******************************/
// 路内停车列表
export const storeOrderList = query => {
    return request({
        url: '/storeOrder/page.json',
        method: 'post',
        params: query
    });
};

//详情列表
export const storeOrderDetailsData = query => {
    return request({
        url: '/storeOrder/orderDetails.json',
        method: 'post',
        params: query
    });
};
//修改车牌号
export const roadParkingOrderCarData = query => {
    return request({
        url: '/roadParkingOrder/updateByCar.json',
        method: 'post',
        params: query
    });
};

//结束计时
export const roadParkingOrderClosureData = query => {
    return request({
        url: '/roadParkingOrder/closureParkingOrder.json',
        method: 'post',
        params: query
    });
};

//**********************************停车场zln******************************/
// 停车列表
export const storeParkOrderList = query => {
    return request({
        url: '/storeOrder/1/page.json',
        method: 'post',
        params: query
    });
};

//停车详情列表
export const parkOrderDetailsData = query => {
    return request({
        url: '/parkingOrder/orderDetails.json',
        method: 'post',
        params: query
    });
};
//停车修改车牌号
export const parkOrderCarData = query => {
    return request({
        url: '/parkingOrder/updateByCar.json',
        method: 'post',
        params: query
    });
};

//停车结束计时
export const parkOrderClosureData = query => {
    return request({
        url: '/parkingOrder/closureParkingOrder.json',
        method: 'post',
        params: query
    });
};
//**********************************充值记录******************************/
// 充值记录列表
export const storeRechargeList = query => {
    return request({
        url: '/storeRechargeRecord/page.json',
        method: 'post',
        params: query
    });
}

//**********************************优惠配置******************************/
// 优惠配置列表
export const storeDiscountList = query => {
    return request({
        url: '/storePreferentialConfig/page.json',
        method: 'post',
        params: query
    });
}
// 优惠配置新增
export const storeDiscountAdd = query => {
    return request({
        url: '/storePreferentialConfig/toAdd.do',
        method: 'post',
        params: query
    });
}
// 优惠配置删除
export const storeDiscountRemove = query => {
    return request({
        url: '/storePreferentialConfig/delete.do',
        method: 'post',
        params: query
    });
}

//**********************************开票管理******************************/
// 开票管理列表
export const storeInvoiceList = query => {
    return request({
        url: '/storeInvoicingManage/page.json',
        method: 'post',
        params: query
    });
}
// 开票管理 通过改变开票状态
export const storeInvoiceStatus = query => {
    return request({
        url: '/storeInvoicingManage/changeStatus.do',
        method: 'post',
        params: query
    });
}
// 根据发票id 查询充值记录
export const storeInvoiceRecharge = query => {
    return request({
        url: '/storeRechargeRecord/findByInvoiceId.json',
        method: 'post',
        params: query
    });
}
