import request from '../utils/request';
/**
 * 申诉列表
 * @author zonglina
 */

// 分页列表
export const operateappealListData = query => {
    return request({
        url: '/operateappeal/selectPageList.json',
        method: 'post',
        params: query
    });
};
//修改
export const operateappealSaveData = query => {
    return request({
        url: '/operateappeal/saveParkingOrder.json',
        method: 'post',
        params: query
    });
};

// --------------------------------------------申诉订单管理--------------------------------------
export const operateappealPageData = query => {
    return request({
        url: '/operateappeal/selectByPage.json',
        method: 'post',
        params: query
    });
};
//详情功能
export const operateappealDetailsData = query => {
    return request({
        url: '/operateappeal/detailsParkingOrder.json',
        method: 'post',
        params: query
    });
};
//保存功能
export const appealHandleRecordSaveData = query => {
    return request({
        url: '/appealHandleRecord/diversePattern.json',
        method: 'post',
        params: query
    });
};
//通知巡检员保存方法
export const appealNoticeFeedbackSave = query => {
    return request({
        url: '/appealNoticeFeedback/save.json',
        method: 'post',
        params: query
    });
};
//处理申诉功能
export const diversePattern = query => {
    return request({
        url: '/appealHandleRecord/diversePattern.json',
        method: 'post',
        params: query
    });
};

export const saveok = query => {
    return request({
        url: '/appealHandleRecord/approve.json',
        method: 'post',
        params: query
    });
};
//申诉锁定
export const upApprove = query => {
    return request({
        url: '/appealHandleRecord/upApprove.json',
        method: 'post',
        params: query
    });
};
//运营人员且是停车场状态下显示此页面
export const parkApprove = query => {
    return request({
        url: '/appealHandleRecord/parkApprove.json',
        method: 'post',
        params: query
    });
};

export const parkingApprove = query => {
    return request({
        url: '/appealHandleRecord/parkingApprove.json',
        method: 'post',
        params: query
    });
};

//导出列表
export const exportshensuData = query => {
    return request({
        url: '/operateappeal/exportOperateAppeal.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};
// ---------------------------------------------反馈管理-----------------------------------------
//反馈信息
export const operatefeedbackListData = query => {
    return request({
        url: '/operatefeedback/selectPageList.json',
        method: 'post',
        params: query
    });
};

//反馈表状态更新，反馈回复表新增
export const operatefeedbackUpdData = query => {
    return request({
        url: '/operatefeedback/updFeedBack.json',
        method: 'post',
        params: query
    });
};
//反馈获取详情信息
export const operatefeedbackDetails = query => {
    return request({
        url: '/operatefeedback/details.json',
        method: 'post',
        params: query
    });
};
//反馈导出列表
export const exportFeedBackData = query => {
    return request({
        url: '/operatefeedback/exportOperateAppeal.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};