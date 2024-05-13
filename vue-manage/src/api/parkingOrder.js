import request from '../utils/request';
/**
 * 停车场现金核销
 * @author thr
 */

// 现金核销订单
export const selectHxPageListData = query => {
    return request({
        url: '/parkingOrder/selectHxPageList.json',
        method: 'post',
        params: query
    });
};

// 现金核销 全部核销
export const hxAll = query => {
    return request({
        url: '/parkingOrder/hxAll.do',
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

//出场收费列表
export const appearanceFee = query => {
    return request({
        url: '/parkingOrder/appearanceFee',
        method: 'post',
        dataType: "json",
        params: query
    });
};

//出场收费详情
export const appearanceFeeInfo = query => {
    return request({
        url: '/parkingOrder/appearanceFeeInfo',
        method: 'post',
        dataType: "json",
        params: query
    });
};

//现金收费
export const cashShoufei = query => {
    return request({
        url: '/parkingOrder/cashShoufei',
        method: 'post',
        dataType: "json",
        params: query
    });
};

export const mergeOrder = query => {
    return request({
        url: '/parkingOrder/getMergeOrder.do',
        method: 'post',
        params: query
    });
};

