import request from '../../utils/request';

// 设备列表查询
export const parkDeviceList = query => {
    return request({
        url: '/parkDevice/toList.do',
        method: 'post',
        params: query
    });
};
// 设备查询
export const getParkDevice = query => {
    return request({
        url: '/parkDevice/getParkDevice.do',
        method: 'post',
        params: query
    });
};
// 设备支付二维码查询
export const getQrCode = query => {
    return request({
        url: '/parkDevice/getQrCode.do',
        method: 'post',
        params: query
    });
};
// 设备批量删除
export const delParkDeviceAll = query => {
    return request({
        url: '/parkDevice/delDeviceAll.do',
        method: 'post',
        params: query
    });
};
// 设备增加
export const addParkDevice = form => {
    return request({
        url: '/parkDevice/toAdd.do',
        method: 'post',
        params: form
    });
};
// 设备编辑删除
export const editParkDevice = query => {
    return request({
        url: '/parkDevice/toEdit.do',
        method: 'post',
        params: query
    });
};
//日志列表
export const opengateList = query => {
    return request({
        url: '/opengate/pageList.json',
        method: 'post',
        params: query
    });
};

//遥控日志
export const exportOpengateData = query => {
    return request({
        url: '/opengate/exportParkingLog.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};


//停车场设备导出列表
export const exportEquipmentData = query => {
    return request({
        url: '/parkDevice/exportEquipment.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

export const playVideo = query => {
    return request({
        url: '/dc/startRtmp.do',
        method: 'post',
        params: query,
    });
};

export const getDeviceTreeApi = query => {
    return request({
        url: '/parkDevice/getDeviceTree.do',
        method: 'post',
        params: query,
    })
}

// 人工开闸
export const openGate = query => {
    return request({
        url: '/parkDevice/openGate',
        method: 'post',
        params: query
    });
};

// 动态订阅主题
export const addTopic = query => {
    return request({
        url: '/parkDevice/addTopic.do',
        method: 'post',
        params: query
    });
};
