import request from "../utils/request";

//版本列表
export const sysPosappManagerList = query => {
    return request({
        url: '/sysPosappManager/getPage',
        method: 'post',
        params: query
    });
};
//版本新增
export const sysPosappManagerSave = query => {
    return request({
        url: '/sysPosappManager/save',
        method: 'post',
        params: query
    });
};
//版本启用禁用
export const sysPosappManagerEnableDisable = query => {
    return request({
        url: '/sysPosappManager/enableDisable',
        method: 'post',
        params: query
    });
};
//版本详情
export const detailsSysPosappManager = query => {
    return request({
        url: '/sysPosappManager/getById',
        method: 'post',
        params: query
    });
};

//版本详情
export const delSysPosappManager = query => {
    return request({
        url: '/sysPosappManager/del',
        method: 'post',
        params: query
    });
};

//系统资源删除图片
export const delImgFile = query => {
    return request({
        url: '/fileManage/delImgFile.json',
        method: 'post',
        params: query
    });
};

//系统资源回显图片
export const getImgView = query => {
    return request({
        url: '/fileManage/getImgView.do',
        method: 'get',
        params: query
    });
};