import request from '../utils/request';

//登录
export const queryLogin = query => {
    return request({

        url: '/login.do',
        method: 'post',
        params: query
    });
};

//登出
export const logout = query => {
    return request({
        url: '/logout.do',
        method: 'post',
        params: query
    });
};

// 消息
export const toManager= () => {
    return request({

        url: '/pcMsg/toManager.do',
        method: 'get',
    });
};

// 当前所属模式查询 停车场，路内
export const isModel = () => {
    return request({

        url: '/sysuser/isModel.do',
        method: 'get',
    });
};
// 当前登录用户
export const loginUser = () => {
    return request({

        url: '/sysuser/loginUser.do',
        method: 'get',
    });
};

// 临时通行证列表 停车场
export const provisionalPassList = query => {
    return request({
        url: '/pass/toList.do',
        method: 'post',
        params: query
    });
};

// 临时通行证列表 下载
export const passImg = from => {

    return request({
        url: '/pass/passImg.do',
        method: 'post',
        data: from,
        dataType:'json',
        contentType: 'application/json',
        responseType: 'blob'
    });
};

// 临时通行证添加 停车场
export const provisionalPassAdd = query => {
    return request({
        url: '/pass/toAdd.do',
        method: 'post',
        params: query
    });
};

// 临时通行证二维码查询
export const passQrCode = query => {
    return request({
        url: '/pass/getQrCode.do',
        method: 'post',
        params: query
    });
};

// 临时通行证编辑 停车场
export const provisionalPassEdit = query => {
    return request({
        url: '/pass/toEdit.do',
        method: 'post',
        params: query
    });
};

// 流动停车支付统计 停车场
export const parkingOrderCount = query => {
    return request({
        url: '/parkingOrder/toCount.do',
        method: 'post',
        params: query
    });
};

// 流动停车支付明细 停车场
export const parkingOrderData = query => {
    return request({
        url: '/parkingOrder/toList.do',
        method: 'post',
        params: query
    });
};
// 流动车导出excel 停车场
export const parkingOrderExcel = query => {
    return request({
        url: '/parkingOrder/exportExcel.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};
// 人工抬杆、欠费明细导出导出excel 停车场
export const parkingOrderExcelTG = query => {
    return request({
        url: '/parkingOrder/exportExcelTG.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};
// 流动停车支付统计
export const roadOrderCount = query => {
    return request({
        url: '/roadParkingOrder/toCount.do',
        method: 'post',
        params: query
    });
};



// 流动车导出excel 路测
export const roadOrderExcel = query => {
    return request({
        url: '/roadParkingOrder/exportExcel.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};
// 流动停车支付明细 路测
export const roadOrderData = query => {
    return request({
        url: '/roadParkingOrder/toList.do',
        method: 'post',
        params: query
    });
};



export const fetchData = query => {
    return request({
        url: './table.json',
        method: 'get',
        params: query
    });
};
//充值活动查询
export const rechargeActivityData = query => {
    return request({
        url: '/rechargeActivity/toList.do',
        method: 'post',
        params: query
    });
};

//充值活动id查询
export const getRechargeActivityById = query => {
    return request({
        url: '/rechargeActivity/getById.do',
        method: 'post',
        params: query
    });
};

// 新增充值活动
export const addRechargeActivity = form => {
    return request({
        url: '/rechargeActivity/toAdd.do',
        method: 'post',
        // params: form
        data:form
    });
};
// 编辑充值活动
export const editRechargeActivity = edit => {
    return request({
        url: '/rechargeActivity/toEdit.do',
        method: 'post',
        data: edit
    });
};

//删除启用禁用充值活动
export const delRechargeActivity = query => {
    return request({
        url: '/rechargeActivity/toDel.do',
        method: 'post',
        params: query
    });
};

// 批量删除充值活动
export const delAllRechargeActivity = query => {
    return request({
        url: '/rechargeActivity/toDelAll.do',
        method: 'post',
        params: query
    });
};

export const roleData = query => {
    return request({
        url: './roleTable.json',
        method: 'get',
        params: query
    });
};

export const journalData = query => {
    return request({
        url: './journalTable.json',
        method: 'get',
        params: query
    });
};

// 特殊开闸记录列表
export const orderOpengateList = query => {
    return request({
        url: '/parkingOrderOpengate/toList.do',
        method: 'post',
        params: query
    });
};

//区域列表查询
export const areaList = query => {
    return request({
        url: '/area/toList.do',
        method: 'post',
        params: query
    });
};
// 区域增加
export const addArea = form => {
    return request({
        url: '/area/toAdd.do',
        method: 'post',
        params: form
    });
};
// 区域编辑删除
export const editArea = edit_area => {
    return request({
        url: '/area/toEdit.do',
        method: 'post',
        params: edit_area
    });
};
// 区域批量删除
export const delAreaAll = query => {
    return request({
        url: '/area/delAreaAll.do',
        method: 'post',
        params: query
    });
};
// 获取字典数据
export const dictData = dict => {
    return request({
        url: '/dict/toList.do',
        method: 'get',
        params: dict
    });
};
// 获取字典数据
export const addDict = dict => {
    return request({
        url: '/dict/toAdd.do',
        method: 'post',
        params: dict
    });
};
// 编辑字典数据
export const editDict = dict => {
    return request({
        url: '/dict/toEdit.do',
        method: 'post',
        params: dict
    });
};
// 字典列表数据查询
export const dictList = dict => {
    return request({
        url: '/dict/toList.do',
        method: 'post',
        params: dict
    });
};

// 街道列表查询
export const streetList = query => {
    return request({
        url: '/street/toList.do',
        method: 'post',
        params: query
    });
};
// 街道增加
export const addStreet = form => {
    return request({
        url: '/street/toAdd.do',
        method: 'post',
        params: form
    });
};

// 街道批量删除
export const delStreetAll = query => {
    return request({
        url: '/street/delStreetAll.do',
        method: 'post',
        params: query
    });
};

// 街道编辑删除
export const editStreet = edit_street => {
    return request({
        url: '/street/toEdit.do',
        method: 'post',
        params: edit_street
    });
};
// 路内列表查询
export const roadList = query => {
    return request({
        url: '/road/toList.do',
        method: 'post',
        params: query
    });
};
// 路内编辑删除--
export const editRoad = edit_road => {
    return request({
        url: '/road/toEdit.do',
        method: 'post',
        params: edit_road
    });
};
// 路内批量删除
export const delRoadAll = query => {
    return request({
        url: '/road/delRoadAll.do',
        method: 'post',
        params: query
    });
};
// 路内增加
export const addRoad = form => {
    return request({
        url: '/road/toAdd.do',
        method: 'post',
        params: form
    });
};
// 根据id获取路内信息
export const getRoad = form => {
    return request({
        url: '/road/getRoad.do',
        method: 'post',
        params: form
    });
};
// 根据id获取路内信息
export const getRoadMergeList = form => {
    return request({
        url: '/roadMerge/getList.do',
        method: 'post',
        params: form
    });
};

// 路段合并管理
// 列表查询
export const toList = form => {
    return request({
        url: '/roadMerge/toList.do',
        method: 'post',
        params: form
    });
};
// 列表查询
export const toView = form => {
    return request({
        url: '/roadMerge/toView.do',
        method: 'post',
        params: form
    });
};
// 添加
export const toAdd = form => {
    return request({
        url: '/roadMerge/toAdd.do',
        method: 'post',
        params: form
    });
};
// 编辑
export const toEdit = form => {
    return request({
        url: '/roadMerge/toEdit.do',
        method: 'post',
        params: form
    });
};
// 删除
export const toDel = form => {
    return request({
        url: '/roadMerge/toDel.do',
        method: 'post',
        params: form
    });
};

// 设备列表查询
export const deviceList = query => {
    return request({
        url: '/device/toList.do',
        method: 'post',
        params: query
    });
};
// 设备查询
export const getDevice = query => {
    return request({
        url: '/device/getDevice.do',
        method: 'post',
        params: query
    });
};
// 设备批量删除
export const delDeviceAll = query => {
    return request({
        url: '/device/delDeviceAll.do',
        method: 'post',
        params: query
    });
};
// 设备增加
export const addDevice = form => {
    return request({
        url: '/device/toAdd.do',
        method: 'post',
        data: form
    });
};
// 设备编辑
export const editDevice = query => {
    return request({
        url: '/device/toEdit.do',
        method: 'post',
        data: query
    });
};
// 设备删除
export const delDevice = query => {
    return request({
        url: '/device/delDevice.do',
        method: 'post',
        params: query
    });
};

// 停车场列表查询
export const parkList = query => {
    return request({
        url: '/park/toList.do',
        method: 'post',
        params: query
    });
};

//根据街道id查询所有停车场
export const queryParkData = query => {
    return request({
        url: '/park/getAll.do',
        method: 'post',
        params: query
    });
};

// 停车场增加
export const addPark = form => {
    return request({
        url: '/park/toAdd.do',
        method: 'post',
        params: form
    });
};
// 停车场批量删除
export const delParkAll = form => {
    return request({
        url: '/park/delParkAll.do',
        method: 'post',
        params: form
    });
};

// 设备编辑删除
export const editPark= query => {
    return request({
        url: '/park/toEdit.do',
        method: 'post',
        params: query
    });
};

//收费方案列表查询
export const chargeProgrammeList = query => {
    return request({
        url: '/chargeCrogramme/toList.do',
        method: 'post',
        params: query
    });
};
// 收费方案添加
export const addChargeProgramme = query => {

    return request({
        url: '/chargeCrogramme/toAdd.do',
        method: 'post',
        dataType:'json',
        contentType: 'application/json',
        data: query
    });
};
// 收费方案编辑
export const editChargeProgramme = query => {

    return request({
        url: '/chargeCrogramme/toEdit.do',
        method: 'post',
        dataType:'json',
        contentType: 'application/json',
        data: query
    });
};
// 收费方案删除
export const delChargeProgramme = query => {

    return request({
        url: '/chargeCrogramme/toDel.do',
        method: 'post',
        params: query
    });
};
// 收费方案批量删除
export const delProgrammeAll = query => {

    return request({
        url: '/chargeCrogramme/delProgrammeAll.do',
        method: 'post',
        params: query
    });
};
// 下载白名单导入xls模板
export const downloadWhite = query => {
    return request({
        url: '/chargeCrogramme/downloadWhite',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};
// 下载个人包月导入xls模板
export const downloadMonthly = query => {
    return request({
        url: '/monthlymanagement/downloadMonthly',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};
// 根据id查询收费方案
export const getChargeProgramme = query => {
    return request({
        url: '/chargeCrogramme/getChargeProgramme.do',
        method: 'post',
        params: query
    });
};

// 收费方案全数据
export const chargeProgrammeData = query => {
    return request({
        url: '/chargeCrogramme/getAll.do',
        method: 'post',
        params: query
    });
};
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
        params: query
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

// 车主信息列表查询
export const memberManageList = query => {
    return request({
        url: '/membermanage/toList.do',
        method: 'post',
        params: query
    });
};

// 车主信息列表查询
export const getmemberManageList = query => {
    return request({
        url: '/membermanage/getList.do',
        method: 'post',
        params: query
    });
};

// 消费订单列表查询
export const queryOrder = query => {
    return request({
        url: '/roadParkingOrder/getOrderByMemberId.do',
        method: 'post',
        params: query
    });
};

// 充值记录列表查询
export const queryChargeRecords = query => {
    return request({
        url: '/rechargemanagement/getRechargeRecords.do',
        method: 'post',
        params: query
    });
};

/* ----------------------------------- lw ----------------------------------- */
/*------------------------------- 平台概览 -------------------------------*/
/*-------------------- 路测 --------------------*/
// 当日注册人数
export const todayAddCount = query => {
    return request({
        url: '/platformsurvey/getTodayAddCount',
        method: 'post',
        data: query,
    });
};
// 用户总数
export const memberTotality = query => {
    return request({
        url: '/platformsurvey/getMemberTotality',
        method: 'post',
        data: query,
    });
};
// 今日订单数
export const todayOrderForm = query => {
    return request({
        url: '/platformsurvey/getTodayOrderForm',
        method: 'post',
        data: query,
    });
};
// 今日应收款
export const todayShouldCharge = query => {
    return request({
        url: '/platformsurvey/getTodayShouldCharge',
        method: 'post',
        data: query,
    });
};
// 今日实收款
export const todayActualCharge = query => {
    return request({
        url: '/platformsurvey/getTodayActualCharge',
        method: 'post',
        data: query,
    });
};
// 泊位总数
export const countByRoad = query => {
    return request({
        url: '/platformsurvey/getCountByRoad',
        method: 'post',
        data: query,
    });
};

// 今日应收款排名
export const shouldBillRanking = query => {
    return request({
        url: '/platformsurvey/getShouldBillRanking',
        method: 'post',
        data: query,
    });
};

// 今日应收款排名 导出
export const exportShouldBillRanking = query => {
    return request({
        url: '/platformsurvey/exportShouldBillRanking',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};
// 路段缴费率统计
export const roadReportJfl = query => {
    return request({
        url: '/platformsurvey/roadReportJfl',
        method: 'post',
        data: query,
    });
};

// 路段缴费率统计 导出
export const exportRoadReportJfl = query => {
    return request({
        url: '/platformsurvey/exportRoadReportJfl',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};
// 近十天应收实收款
export const shouldFactReceive = query => {

    return request({
        url: '/platformsurvey/tenDayShouldFactReceive',
        method: 'post',
        data: query,
    });
};
// 近十天订单趋势
export const tenDayOrderTrend = query => {

    return request({
        url: '/platformsurvey/getTenDayOrderTrend',
        method: 'post',
        data: query,
    });
};
/*-------------------- 停车场 --------------------*/
// 车辆包月数、车辆包月收入、流动车订单数、流动车收入
export const parkingStatistics = query => {
    return request({
        url: '/parkingplatformsurvey/getParkingStatistics',
        method: 'post',
        data: query,
    });
};
//今日停车订单数排名
export const parkingOrderRanking = query => {
    return request({
        url: '/parkingplatformsurvey/getParkingOrderRanking',
        method: 'post',
        data: query,
    });
};
//今日停车订单数排名 导出
export const exportParkingOrderRanking = query => {
    return request({
        url: '/parkingplatformsurvey/exportParkingOrderRanking',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};
//近十天包月趋势
export const daysMonthlyTrend = query => {
    return request({
        url: '/parkingplatformsurvey/tenDaysMonthlyTrend',
        method: 'post',
        data: query,
    });
};
//流动车近十天趋势
export const daysCurrentOrderTrends = query => {
    return request({
        url: '/parkingplatformsurvey/tenDaysCurrentOrderTrends',
        method: 'post',
        data: query,
    });
};

//停车订单列表
export const getOrderList = query => {
    return request({
        url: '/parkingplatformsurvey/getOrderList',
        method: 'post',
        data: query,
    });
};

//路内停车订单列表 合并路段
export const getRoadOrderList = query => {
    return request({
        url: '/platformsurvey/getRoadOrderList',
        method: 'post',
        data: query,
    });
};
//路内停车订单列表
export const getRoadParkingOrderList = query => {
    return request({
        url: '/platformsurvey/getRoadParkingOrderList',
        method: 'post',
        data: query,
    });
};

//包月收入明细
export const getMonthlyPaymentData = query => {
    return request({
        url: '/parkingplatformsurvey/getMonthlyPaymentData',
        method: 'post',
        data: query,
    });
};
//包月收入明细 导出
export const exportBy = query => {
    return request({
        url: '/parkingplatformsurvey/exportBy',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};

/*------------------------------- 平台概览 -------------------------------*/

/*------------------------------- 路内实时监控 -------------------------------*/
/*-------------------- 路内 --------------------*/
//路内列表
export const treeStructureList = query => {

    return request({
        url: '/area/getTreeStructure',
        method: 'post',
        data: query,
    });
};
export const monitoringCount = query => {
    return request({
        url: '/roadsectmonitor/getCount',
        method: 'post',
        data: query,
    });
};
/*-------------------- 停车场 --------------------*/
//停车场列表
export const parkingTreeStructure = query => {
    return request({
        url: '/area/getParkingTreeStructure',
        method: 'post',
        data: query,
    });
};
//查询订单应收
export const orderReceivable = query => {
    return request({
        url: '/parkingsectmonitor/getOrderReceivable',
        method: 'post',
        data: query,
    });
};
//查询订单总收入
export const orderSumAmount = query => {
    return request({
        url: '/parkingsectmonitor/getOrderSumAmount',
        method: 'post',
        data: query,
    });
};
//查询车位总数、剩余数、使用率、周转率
export const utilizeInfo = query => {
    return request({
        url: '/parkingsectmonitor/berthUtilizeInfo',
        method: 'post',
        data: query,
    });
};
//查询会员停车书非会员停车数
export const hyFhyParkingOrderCount = query => {
    return request({
        url: '/parkingsectmonitor/getHyFhyParkingOrderCount',
        method: 'post',
        data: query,
    });
};
//获取出入场统计数据
export const parkLeaveEntryHourCount = query => {
    return request({
        url: '/parkingsectmonitor/getLeaveEntryHourCount',
        method: 'post',
        data: query,
    });
};
//收款统计
export const parkCollectionsStatistics = query => {
    return request({
        url: '/parkingsectmonitor/getCollectionsStatistics',
        method: 'post',
        data: query,
    });
};

/*------------------------------- 路内实时监控 -------------------------------*/

/*------------------------------- 实时停车 -------------------------------*/
/*-------------------- 路内 --------------------*/
//实时停车统计
export const getRoadsectmonitorStatisticsList = query => {
    return request({
        url: '/roadsectmonitor/getDeviceStatistics',
        method: 'post',
        data: query,
    });
};
//列表展示
export const deviceInfoList = query => {
    return request({
        url: '/realtimepark/getDeviceInfo',
        method: 'post',
        data: query,
    });
};
//获取停车订单详情
export const roadParkingOrderInfo = queryInfo => {
    return request({
        url: '/roadParkingOrder/getRoadParkingOrderInfo',
        method: 'post',
        data: queryInfo,
    });
};
/*-------------------- 停车场 --------------------*/
//实施停车统计
export const psmcParkingStatistics = query => {
    return request({
        url: '/parkingsectmonitor/getParkingStatistics',
        method: 'post',
        data: query,
    });
};
//实施停车统计
export const parkDeviceInfo = query => {
    return request({
        url: '/parkingsectmonitor/getParkDeviceInfo',
        method: 'post',
        data: query,
    });
};


/*------------------------------- 实时停车 -------------------------------*/
/*------------------------------- 地图路内 -------------------------------*/
//实施停车统计
export const RoadGPCountList = query => {
    return request({
        url: '/realtimepark/getRoadGPCount',
        method: 'post',
        data: query,
    });
};

//实施停车统计
export const ParkGPCountList = query => {
    return request({
        url: '/realtimepark/getParkGPCount',
        method: 'post',
        data: query,
    });
};



/*------------------------------- 地图路内 -------------------------------*/

/*------------- 充值记录 -------------*/
// 充值记录查询
export const chongzhijiluList = query => {
    return request({
        url: '/rechargemanagement/getRechargeMInfo',
        method: 'post',
        data: query,
    });
};
// 修改金额
export const chongzhijlUP = from => {
    return request({
        url: '/rechargemanagement/update',
        method: 'post',
        data: from,
    });
};
// 充值记录excel
export const exportChongzhijilu = from => {
    return request({
        url: '/rechargemanagement/downRechargeMInfoExcel',
        method: 'post',
        data: from,
        responseType: 'blob'
    });
};
/*------------- 退款记录 -------------*/
// 退款记录查询
export const tkDataList = from => {
    return request({
        url: '/refundmanagement/selectAll',
        method: 'post',
        data: from,
    });
};

/*------------- 包月用户管理 -------------*/
// 查询在包月车辆总数
export const normalMPCount = query => {
    return request({
        url: '/monthlymanagement/getNormalMPCount',
        method: 'post',
        data: query,
    });
};
// 包月用户管理列表
export const baoyueyonghuglList = query => {
    return request({
        url: '/monthlymanagement/selectManageInfo',
        method: 'post',
        data: query,
    });
};

//企业包月
export const baoyueyonghuglList2 = query => {
    return request({
        url: '/monthlymanagement/selectManageInfo2',
        method: 'post',
        data: query,
    });
};

// 查询全部包月配置
export function paymonthlyAllList(isTheCompany,str2) {
    const data = {
        isTheCompany,
        str2
    }
    return request({
        url: '/paymonthly/getAll',
        method: 'post',
        data: data
    });
};
// 获取一个包月配置信息
export const paymonthlyConfig = pcquery => {
    return request({
        url: '/paymonthly/findPaymonthlyConfig',
        method: 'post',
        data: pcquery,
    });
};
// 新增 更新
export const baoyueyonghuglSave = form => {
    return request({
        url: '/monthlymanagement/save',
        method: 'post',
        data: form,
    });
};

export const xf = form => {
    return request({
        url: '/monthlymanagement/xf',
        method: 'post',
        data: form,
    });
};


// 删除包月用户
export const paymonthlyDel = query => {
    return request({
        url: '/monthlymanagement/delete',
        method: 'post',
        data: query,
    });
};
// 删除包月用户
export const paymonthlyDelList = query => {
    return request({
        url: '/monthlymanagement/deleteList',
        method: 'post',
        data: query,
    });
};
/*------------- 包月用户统计 -------------*/
//包月充值统计 按日期
export const timeLicenceCensusList = query => {
    return request({
        url: '/monthlymanagement/getTimeLicenceCensus',
        method: 'post',
        data: query,
    });
};
// //包月充值统计 按车牌
// export const licenceCensusListData = query => {
//     return request({
//         url: '/monthlymanagement/getLicenceCensus',
//         method: 'post',
//         data: query,
//     });
// };


//车牌总数
export const licenceCountData = query => {
    return request({
        url: '/monthlymanagement/getLicenceCount',
        method: 'post',
        data: query,
    });
};
//充值笔数
export const rechargeCountData = query => {
    return request({
        url: '/monthlymanagement/getRechargeCount',
        method: 'post',
        data: query,
    });
};
//充值总额
export const rechargeAmountData = query => {
    return request({
        url: '/monthlymanagement/getRechargeAmount',
        method: 'post',
        data: query,
    });
};


/*------------- 公司用户 -------------*/
//公司用户
export const gongsiyonghuList = query => {
    return request({
        url: '/operatecompany/getall',
        method: 'post',
        data: query,
    });
};


/*------------------------------- 运营报表 -------------------------------*/
/*------------- 路内 -------------*/
// 营收总览查询
export const roadSORList = yszlQuery => {
    return request({
        url: '/operationreport/getRoadSOR',
        method: 'post',
        data: yszlQuery,
    });
};
// 营收统计查询
export const roadSORList2 = yszlQuery => {
    return request({
        url: '/operationreport/getRoadSOR2',
        method: 'post',
        data: yszlQuery,
    });
};
//营收总览Excel
export const summaryORExcel = yszlQuery => {
    return request({
        url: '/operationreport/summaryORExcel',
        method: 'post',
        data: yszlQuery,
        responseType: 'blob'
    });
};
//营收统计Excel
export const summaryORExcel2 = yszlQuery => {
    return request({
        url: '/operationreport/summaryORExcel2',
        method: 'post',
        data: yszlQuery,
        responseType: 'blob'
    });
};
//月租车卡统计
export const getRoadMonthCarStatistics = query => {
    return request({
        url: '/operationreport/getRoadMonthCarStatistics.do',
        method: 'post',
        data: query,
    });
};
//月租车卡统计Excel
export const mcrcStatisticsExcel = yszlQuery => {
    return request({
        url: '/operationreport/mcrcStatisticsExcel',
        method: 'post',
        data: yszlQuery,
        responseType: 'blob'
    });
};

/*------------- 停车场 -------------*/
// 营收总览查询
export const summaryOfRevenueList = yszlQuery => {
    return request({
        url: '/operationreport/getSummaryOfRevenue',
        method: 'post',
        data: yszlQuery,
    });
};

// 应收总览excel
export const summaryOfRevenueExcel = yszlQuery => {
    return request({
        url: '/operationreport/summaryOfRevenueExcel',
        method: 'post',
        data: yszlQuery,
        responseType: 'blob'
    });
};

/*------------- 停车场与路内 -------------*/
//月租车详情
export const monthCarRentalDetailList = yszlQuery => {
    return request({
        url: '/operationreport/getMonthCarRentalDetail',
        method: 'post',
        data: yszlQuery,
    });
};
//月租车统计
export const monthCarRentalStatistics = yszlQuery => {
    return request({
        url: '/operationreport/getMonthCarRentalStatistics',
        method: 'post',
        data: yszlQuery,
    });
};
//月租车excel
export const monthCarRentalExcel = yszlQuery => {
    return request({
        url: '/operationreport/MonthCarRentalExcel',
        method: 'post',
        data: yszlQuery,
        responseType: 'blob'
    });
};

// 内部车excel
export const parkingOrderExcelNm = query => {
    return request({
        url: '/operationreport/exportExcelNm.do',
        method: 'post',
        data: query,
        responseType: 'blob'
    });
};
//内部车
export const getInnerDetailData = query => {
    return request({
        url: '/operationreport/toInnerList.do',
        method: 'post',
        data: query
    });
};
//企业免税车
export const getFreeDetailData = query => {
    return request({
        url: '/operationreport/toFreeEnterpriseList.do',
        method: 'post',
        data: query
    });
};

//企业免税车
export const iflqStatistics = query => {
    return request({
        url: '/operationreport/getIFLQStatistics',
        method: 'post',
        data: query
    });
};



/* ----------------------------------- lw ----------------------------------- */



// -----------------------------------------运营分析接口api begin---------------------------------------------
//区域统计
export const areaTjData = query => {
    return request({
        url: '/operateAnalysis/areaStatistics.do',
        method: 'get',
        params: query
    });
};

//路内统计
export const roadTjData = query => {
    return request({
        url: '/operateAnalysis/roadStatistics.do',
        method: 'post',
        params: query
    });
};

//导出路内统计excel
export const roadTjExport = query => {
    return request({
        url: '/operateAnalysis/exportRoadStatics.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

//巡检统计
export const inspectTjData = query => {
    return request({
        url: '/operateAnalysis/inspectStatistics.do',
        method: 'post',
        params: query
    });
};

//导出巡检统计excel
export const inspectTjExport = query => {
    return request({
        url: '/operateAnalysis/exportinspectStatics.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};



//查询所有区域
export const queryAreaData = query => {
    return request({
        url: '/area/getAll.do',
        method: 'get',
        params: query
    });
};

//根据区域id查询所有街道
export const queryStreetData = query => {
    return request({
        url: '/street/getAll.do',
        method: 'post',
        params: query
    });
};

//根据街道id查询所有路内
export const queryRoadData = query => {
    return request({
        url: '/road/getAll.do',
        method: 'post',
        params: query
    });
};

//订单统计基础数据
export const getRoadParkingOrderBasicData = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingOrderBasicData.do',
        method: 'post',
        params: query
    });
};

//导出基础数据excel
export const exportOrderBasicData = query => {
    return request({
        url: '/roadParkingOrder/exportOrderBasicData.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

//订单统计订单增长
export const getRoadParkingOrderGrowth = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingOrderGrowth.do',
        method: 'post',
        params: query
    });
};

//近八天订单增长分析
export const getRoadParkingNearly8DaysOrderGrowth = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingNearly8DaysOrderGrowth.do',
        method: 'post',
        params: query
    });
};

//导出订单增长excel
export const exportOrderGrowth = query => {
    return request({
        url: '/roadParkingOrder/exportOrderGrowth.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

//订单统计订单状态占比
export const getRoadParkingOrderStatus = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingOrderStatus.do',
        method: 'post',
        params: query
    });
};

//资金统计收入报表
export const getRoadParkingOrderIncome = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingOrderIncome.do',
        method: 'post',
        params: query
    });
};

//导出资金统计收入报表Excel
export const exporOrderIncome = query => {
    return request({
        url: '/roadParkingOrder/exporOrderIncome.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

//资金统计充值增长
export const getRoadParkingRechargeGrowth = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingRechargeGrowth.do',
        method: 'post',
        params: query
    });
};

//导出资金统计充值增长Excel
export const exporRechargeGrowth = query => {
    return request({
        url: '/roadParkingOrder/exporRechargeGrowth.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};


//资金统计缴费方式统计
export const getRoadParkingPaymentMethod = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingPaymentMethod.do',
        method: 'post',
        params: query
    });
};

//资金统计免单统计
export const getRoadParkingFree = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingFree.do',
        method: 'post',
        params: query
    });
};

//开票统计
export const getRoadParkingInvoicing = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingInvoicing.do',
        method: 'post',
        params: query
    });
};

//统计发票总额
export const getInvoiceAmount = query => {
    return request({
        url: '/roadParkingOrder/getInvoiceAmount.do',
        method: 'post',
        params: query
    });
};

//大额欠费
export const getRoadParkingLargeArrears = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingLargeArrears.do',
        method: 'post',
        params: query
    });
};

//导出大额欠费Excel
export const exporLargeArrears = query => {
    return request({
        url: '/roadParkingOrder/exporLargeArrears.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

//大额欠费欠费订单
export const getRoadParkingLargeArrearsOrders = query => {
    return request({
        url: '/roadParkingOrder/getRoadParkingLargeArrearsOrders.do',
        method: 'post',
        params: query
    });
};

// 人工抬杆/车辆明细
export const getOpenGateDetails = query => {
    return request({
        url: '/operationreport/getOpenGateDetails.do',
        method: 'post',
        params: query
    });
};
//人工抬杆/车辆总数
export const getOpenGateCarNums = query => {
    return request({
        url: '/operationreport/getOpenGateCarNums.do',
        method: 'post',
        params: query
    });
};
// 人工抬杆/车辆订单欠费总额
export const getOpenGateOrderArrears = query => {
    return request({
        url: '/operationreport/getOpenGateOrderArrears.do',
        method: 'post',
        params: query
    });
};
// -----------------------------------------运营分析接口api end---------------------------------------------

// -----------------------------------------运营管理接口api begin---------------------------------------------
//获取所有人员信息
export const dealInspectorList = query => {
    return request({
        url: '/road/dealInspectorList.json',
        method: 'post',
        dataType : "json",
        params: query
    });
};
//分配路内分配巡检员
export const dealSectionInspector = query => {
    return request({
        url: '/road/dealSectionInspector.json',
        method: 'post',
        params: query
    });
};
//分配路内解除分配巡检员列表
export const listInspector = query => {
    return request({
        url: '/road/listInspector.json',
        method: 'post',
        params: query
    });
};
//分配路内解除分配
export const dealRelieve = query => {
    return request({
        url: '/road/dealRelieve.json',
        method: 'post',
        params: query
    });
};
// -----------------------------------------运营管理接口api end---------------------------------------------

// -----------------------------------------财务管理接口api begin---------------------------------------------
//营收管理查询查看
export const queryPaymentOrder = query => {
    return request({
        url: '/paymentOrder/queryPaymentOrder.json',
        method: 'post',
        params: query
    });
};
//营收管理联合支付
export const jointPayment = query => {
    return request({
        url: '/paymentOrder/jointPayment.json',
        method: 'post',
        params: query
    });
};
//巡检管理查询查看
export const listPatrolRechargeManagement = query => {
    return request({
        url: '/patrolRechargeManagement/queryPatrolRechargeManagement.json',
        method: 'post',
        params: query
    });
};
//巡检管理表头统计
export const patrolRechargeManagementCount = query => {
    return request({
        url: '/patrolRechargeManagement/count.json',
        method: 'post',
        params: query
    });
};
//巡检管理巡检充值验证
export const rechargeVerification = query => {
    return request({
        url: '/patrolRechargeManagement/rechargeVerification.json',
        method: 'post',
        params: query
    });
};
//巡检管理巡检充值
export const patrolRecharge = query => {
    return request({
        url: '/patrolRechargeManagement/patrolRecharge.json',
        method: 'post',
        params: query
    });
};
//巡检管理发票领取管理
export const invoiceReceiving = query => {
    return request({
        url: '/patrolRechargeManagement/invoiceReceiving.json',
        method: 'post',
        params: query
    });
};
//巡检管理删除
export const deletePatrolRechargeManagement = query => {
    return request({
        url: '/patrolRechargeManagement/deletePatrolRechargeManagement.json',
        method: 'post',
        params: query
    });
};
export const InspectionRechargeStatistics = query => {
    return request({
        url: '/patrolRechargeManagement/InspectionRechargeStatistics.json',
        method: 'post',
        params: query
    });
};

// 发票发放记录
export const InvoicesProvideRecordList = query => {
    return request({
        url: '/invoicesProvideRecord/toList.do',
        method: 'post',
        params: query
    });
};

// -----------------------------------------财务管理接口api end---------------------------------------------


// ------------------------------------------系统角色管理接口api begin-------------------------------------
//分页查询角色列表
export const queryRoleList = query => {
    return request({
        url: '/sysRole/getPage.do',
        method: 'post',
        params: query
    });
};

export const queryRoleAll = query => {
    return request({
        url: '/sysRole/getList.do',
        method: 'post',
        params: query
    });
};

//查询菜单树形结构
export const queryMenuTree = query => {
    return request({
        url: '/sysmenu/getMenuTree.do',
        method: 'get',
        data: query
    });
};

//新增角色
export const saveRole = query => {
    return request({
        url: '/sysRole/save.do',
        method: 'post',
        params: query
    });
};

//编辑角色
export const editRole = query => {
    return request({
        url: '/sysRole/edit.do',
        method: 'post',
        params: query
    });
};

//删除角色
export const delRole = query => {
    return request({
        url: '/sysRole/del.do',
        method: 'post',
        params: query
    });
};

//批量删除角色
export const delRoleAll = query => {
    return request({
        url: '/sysRole/delAll.do',
        method: 'post',
        params: query
    });
};

//查询角色详情
export const queryeRole = query => {
    return request({
        url: '/sysRole/getRole.do',
        method: 'post',
        params: query
    });
};
// ------------------------------------------系统角色管理接口api end-------------------------------------

// ------------------------------------------系统用户管理接口api begin-------------------------------------
//分页查询用户列表
export const queryuser = query => {
    return request({
        url: '/sysuser/getPage.do',
        method: 'post',
        params: query
    });
};

//查询用户列表
export const queryuserAll = query => {
    return request({
        url: '/sysuser/getList.do',
        method: 'post',
        params: query
    })
}

//查询日志用户列表
export const getUserList = query => {
    return request({
        url: '/log/getUserList.do',
        method: 'post',
        params: query
    })
}


//新增用户
export const adduser = query => {
    return request({
        url: '/sysuser/save.do',
        method: 'post',
        params: query
    });
};

//编辑用户
export const edituser = query => {
    return request({
        url: '/sysuser/edit.do',
        method: 'post',
        params: query
    });
};

//修改密码
export const updPass = query => {
    return request({
        url: '/sysuser/updPass.do',
        method: 'post',
        params: query
    });
};

//启用、禁用
export const handleStatus = query => {
    return request({
        url: '/sysuser/handleStatus.do',
        method: 'post',
        params: query
    })
}

//查询用户
export const getuser = query => {
    return request({
        url: '/sysuser/getUser.do',
        method: 'post',
        params: query
    });
};

//用户登录名查重
export const validateLoginName = query => {
    return request({
        url: '/sysuser/validateLoginName.do',
        method: 'post',
        params: query
    });
};

//删除用户
export const deluser = query => {
    return request({
        url: '/sysuser/del.do',
        method: 'post',
        params: query
    });
};

//批量删除用户
export const deluserAll = query => {
    return request({
        url: '/sysuser/delAll.do',
        method: 'post',
        params: query
    });
};
// ------------------------------------------系统用户管理接口api end-------------------------------------

// ------------------------------------------系统日志接口api begin-------------------------------------

//列表查询
export const queryLog = query => {
    return request({
        url: '/log/getPage.do',
        method: 'post',
        params: query
    });
};

// ------------------------------------------系统日志接口api end-------------------------------------

// ------------------------------------------系统参数接口api begin-------------------------------------

//参数查询
export const querySysConfig = query => {
    return request({
        url: '/sysconfig/getMap.do',
        method: 'post',
        params: query
    });
};

//参数更新
export const editSysConfig = query => {
    return request({
        url: '/sysconfig/edit.do',
        method: 'post',
        params: query
    });
};
// ------------------------------------------系统参数接口api end-------------------------------------

// ------------------------------------------系统菜单接口api begin-------------------------------------
//查询菜单
export const queryMenus = query => {
    return request({
        url: '/sysmenu/getMenus.do',
        method: 'post',
        params: query
    });
};

// ------------------------------------------系统菜单接口api end-------------------------------------

// ------------------------------------------包月配置管理接口api begin-------------------------------------
//分页查询
export const queryMonthly = query => {
    return request({
        url: '/paymonthly/getPage.do',
        method: 'post',
        params: query
    });
};

//新增
export const addMonthly = query => {
    return request({
        url: '/paymonthly/save.do',
        method: 'post',
        params: query
    });
};

//编辑
export const editMonthly = query => {
    return request({
        url: '/paymonthly/edit.do',
        method: 'post',
        params: query
    });
};

//删除
export const delMonthly = query => {
    return request({
        url: '/paymonthly/del.do',
        method: 'post',
        params: query
    });
};

//批量删除
export const delMonthlyAll = query => {
    return request({
        url: '/paymonthly/delAll.do',
        method: 'post',
        params: query
    });
};

//查询路内关联关系
export const queryTranfer = query => {
    return request({
        url: '/paymonthly/getTransferInfo.do',
        method: 'post',
        params: query
    });
};

//查询路内关联关系
export const queryConfig = query => {
    return request({
        url: '/paymonthly/getConfig.do',
        method: 'post',
        params: query
    });
};

//获取包月配置类型字典
export const queryCategory = query => {
    return request({
        url: '/paymonthly/getCategory.do',
        method: 'post',
        params: query
    });
};
// -------------------------------------线上收费配置-------------------------------------------
// 系统管理：线上收费配置zln
export const parkingReleaseDataList = query => {
    return request({
        url: '/parkingRelease/selectPageList.json',
        method: 'post',
        params: query
    });
};
//收费保存功能
export const parkingReleaseSave = query => {
    return request({
        url: '/parkingRelease/save.json',
        method: 'post',
        params: query
    });
};
//删除||启用\禁用
export const parkingReleaseDelete = query => {
    return request({
        url: '/parkingRelease/delete.json',
        method: 'post',
        params: query
    });
};
//获取路内、停车场数据
export const parkingReleaseListData = query => {
    return request({
        url: '/parkingRelease/selectByListData.json',
        method: 'post',
        params: query
    });
};

// ------------------------------------------包月配置管理接口api end-------------------------------------

//切换停车场、路内
export const switchModel = query => {
    return request({
        url: '/sysuser/switchModel.do',
        method: 'post',
        params: query
    });
};

// -----------------------------------------api.js删除之前补充---------------------------------------------
//车牌申诉查询查看
export const queryCarnoAppeal = query => {
    return request({
        url: '/carnoAppeal/queryCarnoAppeal.json',
        method: 'post',
        params: query
    });
};
//车牌申诉申诉处理
export const complaintHandling = query => {
    return request({
        url: '/carnoAppeal/complaintHandling.json',
        method: 'post',
        params: query
    });
};
//设备报警查询查看
export const queryDeviceAlarm = query => {
    return request({
        url: '/deviceAlarm/queryDeviceAlarm.json',
        method: 'post',
        params: query
    });
};
//设备离线日志查询查看
export const queryDeviceOfflineRecord = query => {
    return request({
        url: '/deviceOfflineRecord/queryDeviceOfflineRecord.json',
        method: 'post',
        params: query
    });
};
//分配路内查询查看
export const queryAllocatedSection = query => {
    return request({
        url: '/road/queryAllocatedSection.json',
        method: 'post',
        dataType : "json",
        params: query
    });
};
//获取所有人员信息
export const inspectManageSelectList = query => {
    return request({
        url: '/inspectManage/selectList.json',
        method: 'post',
        dataType : "json",
        params: query
    });
};

//导出列表
export const exportRoadListData = query => {
    return request({
        url: '/road/exportRoadList.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};
//新代码2022-06-20
export const selectRoadDataList = query => {
    return request({
        url: '/roadParkingOrder/selectRoadDataList.json',
        method: 'post',
        dataType : "json",
        params: query
    });
};

//退款导出列表
export const exportRefundListData = query => {
    return request({
        url: '/refundmanagement/exportRefund.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

//停车场管理导出列表
export const exportParkingData = query => {
    return request({
        url: '/park/exportParking.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};


//路内设备导出列表
export const exportEquipmentRoadData = query => {
    return request({
        url: '/device/exportEquipmentRoad.do',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

// 获取离线报警设备列表 停车场
export const parkDeviceList = query => {
    return request({
        url: '/parkDevice/getList.do',
        method: 'post',
        params: query
    });
};
// 获取离线报警设备列表 路内
export const roadDeviceList = query => {
    return request({
        url: '/device/getList.do',
        method: 'post',
        params: query
    });
};

// 线下充值
export const rechargemanagementSave = query => {
    return request({
        url: '/rechargemanagement/save',
        method: 'post',
        params: query
    });
};

// 考勤管理
export const attendanceManagementList = query => {
    return request({
        url: '/attendanceManagement/toList.do',
        method: 'post',
        params: query
    });
};
// 考勤管理 导出
export const attendanceManagementExportExcel = query => {
    return request({
        url: '/attendanceManagement/downExcel',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};
// 考勤统计
export const attendanceReportList = query => {
    return request({
        url: '/attendanceManagement/toPageReport.do',
        method: 'post',
        params: query
    });
};
// 考勤统计 导出
export const attendanceReportExportExcel = query => {
    return request({
        url: '/attendanceManagement/reportDownExcel',
        method: 'post',
        params: query,
        responseType: 'blob'
    });
};

// -----------------------------------------运营管理接口api end---------------------------------------------
