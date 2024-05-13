import request from '../utils/request';
/**
 * 发票管理
 * @author zonglina
 */

// 分页列表
export const getFapiaoList = query => {
    return request({
        url: '/invoicesManagement/queryInvoicesManagement.json',
        method: 'post',
        params: query
    });
};

//发票审核
export const invoicingApproval = query => {
    return request({
        url: '/invoicesManagement/invoicingApproval.json',
        method: 'post',
        params: query
    });
};

//发票详情
export const getInvoiceDetails = query => {
    return request({
        url: '/invoicesManagement/getInvoiceDetails.json',
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