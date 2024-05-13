import request from '../utils/request';

//翻译查询
export const screenList = query => {
    return request({
        url: '/screen/toList.do',
        method: 'post',
        params: query
    });
};

export const getTask = query => {
    return request({
        url: '/screen/getTask.do',
        method: 'post',
        params: query
    });
};

export const getTaskEntity = query => {
    return request({
        url: '/screen/getTaskEntity.do',
        method: 'post',
        params: query
    });
};

export const deleteTask = query => {
    return request({
        url: '/screen/deleteTask.do',
        method: 'post',
        params: query
    });
};

//详情数据
export const detailsScreen = query => {
    return request({
        url: '/screen/getById.do',
        method: 'post',
        params: query
    });
};

// 新增编辑更新
export const editScreen = query => {
    return request({
        url: '/screen/saveOrUpd.json',
        method: 'post',
        data: query
    });
};

// 发布
export const fbUpd = query => {
    return request({
        url: '/screen/fbUpd.json',
        method: 'post',
        data: query
    });
};

export const publishTask = query => {
    return request({
        url: '/screen/publishTask.do',
        method: 'post',
        data: query
    });
};

