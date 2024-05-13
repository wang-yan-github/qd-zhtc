import request from '../utils/request';
import File_URL from '../file_url';


//公司列表
export const getList = query => {
    return request({
        url: '/operatecompany/getall',
        method: 'post',
        data: query,
    });
};

//查询详情
export const getDetail = query => {
    return request({
        url: '/operatecompany/getDetail',
        method: 'post',
        params: query,
    });
};

//新增
export const saveCompany = query => {
    return request({
        url: '/operatecompany/save',
        method: 'post',
        params: query,
    });
};
//新增
export const updateCompany = query => {
    return request({
        url: '/operatecompany/update',
        method: 'post',
        params: query,
    });
};
//获取车牌详情
export const getCarnoDetail = query => {
    return request({
        url: '/operatecarno/getDetail',
        method: 'post',
        params: query,
    });
};

//绑定车牌
export const bindCar = query => {
    return request({
        url: '/carnocompany/bindCompanyCar',
        method: 'post',
        params: query,
    });
};
//批量绑定车牌
export const batchBindCars = query => {
    return request({
        url: '/carnocompany/batchBindCars',
        method: 'post',
        data: query,
    });
};
//解绑车牌
export const unbindCar = query => {
    return request({
        url: '/carnocompany/unbindCompanyCar',
        method: 'post',
        params: query,
    });
};

//获取企业绑定车辆信息
export const getCompanyCars = query => {
    return request({
        url: '/carnocompany/getCompanyCars',
        method: 'post',
        params: query,
    });
};
//获取企业绑定车辆信息  wzn
export function getCompanyCars2(companyId, pageIndex, pageSize) {
    return request({
        url: '/carnocompany/getCompanyCars?companyId=' + companyId + '&pageIndex=' + pageIndex + '&pageSize=' + pageSize,
        method: 'post',
    })
}

export function getByCompanyCars(monthly_code, pageIndex, pageSize) {
    return request({
        url: '/carnocompany/getByCompanyCars?monthly_code=' + monthly_code + '&pageIndex=' + pageIndex + '&pageSize=' + pageSize,
        method: 'post',
    })
}
export function byExport(isTheCompany, parkingType) {
    return request({
        url: '/operationreport/byExport?isTheCompany=' + isTheCompany + '&parkingType=' + parkingType,
        method: 'post',
    })
}

export function dowload(fileName) {
    window.location.href = File_URL.file_down + "/common/exceldownload?fileName=" + encodeURI(fileName) + "&delete=" + true + "&name=" + "包月信息.xlsx";
}



//获取企业绑定车辆信息  wzn
export function getCompanyCars3(companyId) {
    return request({
        url: '/carnocompany/getCompanyCars2?companyId=' + companyId,
        method: 'post',
    })
}

export function info(monthly_code) {
    return request({
        url: '/monthlymanagement/info?monthly_code=' + monthly_code,
        method: 'post',
    })
}

export function num(monthly_code, strs) {
    return request({
        url: '/monthlymanagement/num?monthly_code=' + monthly_code + '&strs=' + strs,
        method: 'post',
    })
}

export function allNum(monthly_code) {
    return request({
        url: '/monthlymanagement/allNum?monthly_code=' + monthly_code,
        method: 'post',
    })
}






//删除企业绑定车辆信息
export const delCompany = query => {
    return request({
        url: '/operatecompany/delCompany',
        method: 'post',
        params: query,
    });
};



//公司导出
export const exportGSUserData = query => {
    return request({
        url: '/operatecompany/exportCompany.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

export const exportCompanyCars = query => {
    return request({
        url: '/carnocompany/exportCompanyCars',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};
