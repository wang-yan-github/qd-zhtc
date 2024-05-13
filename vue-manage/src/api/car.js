import request from '../utils/request';
/**
 * 车牌录入
 * @author zonglina
 */

// 分页列表
export const operatecarnoSaveData = query => {
    return request({
        url: '/operatecarno/save.json',
        method: 'post',
        data: query
    });
};




// id查询
export const getOneTraffic = query => {
    return request({
        url: '/operateCarnoTraffic/' + query + '/getOneTraffic.do',
        method: 'post',

    });
};