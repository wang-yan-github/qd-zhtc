import request from '../utils/request';

// 查询所有已经通过审核的商家
export const toPassList = query => {
    return request({
        url: '/business/toPassList.do',
        method: 'post',
        params: query
    });
};

// 查询所有待审批的商家
export const getAuditInfoByPage = query => {
    return request({
        url: '/business/getAuditInfoByPage.do',
        method: 'post',
        params: query
    });
};

// 审批通过
export const approved  = query => {
    return request({
        url: '/business/approved.do?id='+query,
        method: 'get',
    });
};

// 审批驳回
export const reject = query => {
    return request({
        url: '/business/reject.do',
        method: 'post',
        params: query
    });
};

// 查询扣款记录
export const getBusinessDeductionById = query => {
    return request({
        url: '/business/getBusinessDeductionById.do',
        method: 'post',
        params: query
    });
};

//导出扣款记录
export const exportBusinessDeduction = query => {
    return request({
        url: '/business/exportBusinessDeduction.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};


//查看商家详情
export const getOneById = query => {
    return request({
        url: '/business/getOneById.do?id='+query,
        method: 'get',
    });
};