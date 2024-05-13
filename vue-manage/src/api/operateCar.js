import request from '../utils/request';


// 车牌管理列表查询
export const operateCarnoList = query => {
    return request({
        url: '/operatecarno/toList.do',
        method: 'post',
        params: query
    });
};
// 车牌编辑
export const editOperateCarno = query => {
    return request({
        url: '/operatecarno/toEdit.do',
        method: 'post',
        data: query
    });
};
// 车牌详情
export const viewOperateCarno = query => {
    return request({
        url: '/operatecarno/view.json',
        method: 'post',
        data: query
    });
};
// 删除车牌
export const delOperateCarno = query => {
    return request({
        url: '/operatecarno/del.json',
        method: 'post',
        data: query
    });
};

// 车牌绑定记录或车牌全数据
export const operateCarnoData = query => {
    return request({
        url: '/operatecarno/getAll.do',
        method: 'post',
        params: query
    });
};
//获取字典
export const getDictsByType = query => {
    return request({
        url: '/operatecarno/getDictsByType',
        method: 'post',
        params: query
    });
};
//解绑
export const relieveBind = query => {
    return request({
        url: '/operatecarno/relieveBind',
        method: 'post',
        params: query
    });
};

//解绑
export const bindCarPerson = query => {
    return request({
        url: '/operatecarno/bindCarPerson.do',
        method: 'post',
        params: query
    });
};

//批量增加白名單
export const saveBeathWhiteCarNo = query => {
    return request({
        url: '/operatecarno/saveBeathWhiteCarNo',
        method: 'post',
        data: query
    });
};

//Excel白名單模板下载
export const downloadWhite = query => {
    return request({
        url: '/operatecarno/downloadWhite',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};

//Excel白名單导入
export const importWhiteList = query => {
    return request({
        url: '/operatecarno/importWhiteList',
        method: 'post',
        data: query
    });
};


//获取市交传输过来的信息
export const operateCarnoTrafficList = query => {
    return request({
        url: '/operateCarnoTraffic/toTrafficList.do',
        method: 'post',
        params: query
    });
};

//车牌管理导出
export const exportCarNo = query => {
    return request({
        url: '/operatecarno/export.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

// ------------------------------------------系统日志接口api end-------------------------------------
