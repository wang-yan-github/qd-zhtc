import request from '../utils/request';

//素材管理
export const sysMaterialList = query => {
    return request({
        url: '/sysMaterial/toList.do',
        method: 'post',
        params: query
    });
};
//详情数据
export const detailsSysMaterial = query => {
    return request({
        url: '/sysMaterial/getById.do',
        method: 'post',
        params: query
    });
};
// 素材管理编辑删除更新
export const editSysMaterial = query => {
    return request({
        url: '/sysMaterial/saveOrUpd.json',
        method: 'post',
        data: query
    });
};
//批量删除
export const delAll = query => {
    return request({
        url: '/sysMaterial/delAll.do',
        method: 'post',
        params: query
    });
};

// 系统资源删除图片
export const delImgFile = query => {
    return request({
        url: '/fileManage/delImgFile.json',
        method: 'post',
        params: query
    });
};

// 系统资源回显图片
export const getImgView = query => {
    return request({
        url: '/fileManage/getImgView.do',
        method: 'get',
        params: query
    });
};
