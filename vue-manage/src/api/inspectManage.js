import request from '../utils/request';
/**
 * 巡检管理
 * @author zonglina
 */
// 巡检列表json
export const inspectManageListData = query => {
    return request({
        url: '/inspectManage/selectPageList.json',
        method: 'post',
        params: query
    });
};

//修改功能
export const inspectManageUpdData = query => {
    return request({
        url: '/inspectManage/details.json',
        method: 'post',
        params: query
    });
};
//保存功能
export const inspectManageSaveData = query => {
    return request({
        url: '/inspectManage/save.json',
        method: 'post',
        params: query
    });
};

//删除操作
export const inspectManageDeleteData = query => {
    return request({
        url: '/inspectManage/delete.json',
        method: 'post',
        params: query
    });
};

// --------------------------------巡检反馈----------------------------
// 巡检反馈列表
export const inspectFeedbackListData = query => {
    return request({
        url: '/inspectFeedback/selectPageList.json',
        method: 'post',
        params: query
    });
};

//详情列表
export const inspectFeedbackDetailsData = query => {
    return request({
        url: '/inspectFeedback/details.json',
        method: 'post',
        params: query
    });
};
//反馈功能
export const inspectFeedbackData = query => {
    return request({
        url: '/inspectFeedback/feedbackContent.json',
        method: 'post',
        params: query
    });
};

//删除
export const inspectFeedbackDeleteData = query => {
    return request({
        url: '/inspectFeedback/delete.json',
        method: 'post',
        params: query
    });
};
