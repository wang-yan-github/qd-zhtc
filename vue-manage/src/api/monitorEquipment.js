import request from '../utils/request';


// -----------------------------------------监控管理接口api begin---------------------------------------------
//获取监控设备列表
export const monitorList = query => {
    return request({
        url: '/monitorEquipment/selectList',
        method: 'post',
        params: query
    });
};
//新增、修改监控设备
export const addMonitorEquipment = query => {
    return request({
        url: '/monitorEquipment/addMonitorEquipment',
        method: 'post',
        params: query
    });
};
//获取监控设备详情
export const getMonitorEquipment = query => {
    return request({
        url: '/monitorEquipment/getMonitorEquipment',
        method: 'post',
        params: query
    });
};
//删除监控设备
export const delMonitorEquipment = query => {
    return request({
        url: '/monitorEquipment/delMonitorEquipment',
        method: 'post',
        params: query
    });
};
//查询
export const getAreas = query => {
    return request({
        url: '/monitorEquipment/getAreas',
        method: 'post',
        params: query
    });
};
// -----------------------------------------监控管理接口api end---------------------------------------------