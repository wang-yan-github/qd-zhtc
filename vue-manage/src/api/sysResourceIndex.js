import request from '../utils/request';


//系统资源查询
export const sysResourceList = query => {
    return request({
        url: '/sysResource/toList.do',
        method: 'post',
        params: query
    });
};
//详情数据
export const detailsSysResource = query => {
    return request({
        url: '/sysResource/details.json',
        method: 'post',
        params: query
    });
};
//常见问题详情数据
export const questDetails = query => {
    return request({
        url: '/sysResource/questDetails.json',
        method: 'post',
        params: query
    });
};
// 系统资源增加保存
export const addSysResource = query => {
    return request({
        url: '/sysResource/toAdd.do',
        method: 'post',
        params: query
    });
};
// 系统资源编辑删除更新
export const editSysResource = query => {
    return request({
        url: '/sysResource/saveOrUpd.json',
        method: 'post',
        data: query
    });
};
//批量删除
export const delAll = query => {
    return request({
        url: '/sysResource/delAll.do',
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
