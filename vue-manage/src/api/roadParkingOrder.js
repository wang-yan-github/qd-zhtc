import request from '../utils/request';
/**
 * 路内现金核销
 * @author thr
 */

// 现金核销订单
export const selectHxPageListData = query => {
    return request({
        url: '/roadParkingOrder/selectHxPageList.json',
        method: 'post',
        params: query
    });
};

// 现金核销 全部核销
export const hxAll = query => {
    return request({
        url: '/roadParkingOrder/hxAll.do',
        method: 'post',
        params: query
    });
};
//获取巡检员下拉框列表
export const inspectManageSelectList = query => {
    return request({
        url: '/inspectManage/selectList.json',
        method: 'post',
        dataType: "json",
        params: query
    });
};
//**********************************路内zln******************************/
// 路内停车列表
export const roadParkingOrderListData = query => {
    return request({
        url: '/roadParkingOrder/page.json',
        method: 'post',
        params: query
    });
};

//详情列表
export const roadParkingOrderDetailsData = query => {
    return request({
        url: '/roadParkingOrder/orderDetails.json',
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

export const mergeOrder = query => {
    return request({
        url: '/roadParkingOrder/getMergeOrder.do',
        method: 'post',
        params: query
    });
};
//导出列表
export const exportRoadData = query => {
    return request({
        url: '/roadParkingOrder/exportRoad.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};


//**********************************停车场zln******************************/
// 停车列表
export const parkOrderListData = query => {
    return request({
        url: '/parkingOrder/page.json',
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
export const getArrearsList = query => {
    return request({
        url: '/roadParkingOrder/getArrearsList.do',
        method: 'post',
        params: query
    });
};

//停车订单删除
export const parkOrderDel = query => {
    return request({
        url: '/parkingOrder/del.json',
        method: 'post',
        params: query
    });
};

//停车订单人工放行
export const updStatus = query => {
    return request({
        url: '/parkingOrder/updStatus.json',
        method: 'post',
        params: query
    });
};

//路内订单删除
export const roadParkOrderDel = query => {
    return request({
        url: '/roadParkingOrder/del.json',
        method: 'post',
        params: query
    });
};

//路内订单 选用二次识别车牌号码
export const updCarNo = query => {
    return request({
        url: '/roadParkingOrder/updCarNo.json',
        method: 'post',
        params: query
    });
};
