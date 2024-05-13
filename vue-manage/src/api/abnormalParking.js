import request from '../utils/request';
/**
 * 问题停车订单
 * @author zonglina
 */

// 问题停车列表
export const abnormalParkingListData = query => {
    return request({
        url: '/abnormalParking/selectPageList.json',
        method: 'post',
        params: query
    });
};

//详情列表
export const abnormalParkingDetailsData = query => {
    return request({
        url: '/abnormalParking/details.json',
        method: 'post',
        params: query
    });
};
//修改车牌号
export const abnormalParkingCarData = query => {
    return request({
        url: '/abnormalParking/updateByCar.json',
        method: 'post',
        params: query
    });
};

//结束计时
export const abnormalParkingClosureData = query => {
    return request({
        url: '/abnormalParking/closureParkingOrder.json',
        method: 'post',
        params: query
    });
};
//导出列表
export const exportAbnormalParkingData = query => {
    return request({
        url: '/abnormalParking/exportAbnormalParking.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};