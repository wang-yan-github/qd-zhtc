<template>
    <div>
        <div class="container">
            <el-row class="mt20">
                        <el-col :span="24">
                            <div class="top-panel">
                                <el-form inline size="small">
                                    <el-form-item label="区域">
                                        <el-select
                                                clearable
                                                v-model="yszlQuery.area_id"
                                                filterable
                                                size="small"
                                                placeholder="所有区域"
                                                class="w100"
                                                @change="getStreet"
                                        >
                                            <el-option
                                                    v-for="item in formqjl.areas"
                                                    :key="item.id"
                                                    :label="item.area_name"
                                                    :value="item.id"
                                            ></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item label="街道">
                                        <el-select
                                                clearable
                                                v-model="yszlQuery.street_id"
                                                filterable
                                                size="small"
                                                placeholder="所有街道"
                                                class="w100"
                                                @change="getRoad"
                                        >
                                            <el-option
                                                    v-for="item in formqjl.streets"
                                                    :key="item.id"
                                                    :label="item.street_name"
                                                    :value="item.id"
                                            ></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item label="路内">
                                        <el-select
                                                v-model="yszlQuery.variance"
                                                filterable
                                                size="small"
                                                placeholder="所有路内"
                                                class="w100"
                                        >
                                            <el-option value="">全部</el-option>
                                            <el-option
                                                    v-for="(item, i) in formqjl.roads"
                                                    :key="i"
                                                    :label="item.name"
                                                    :value="item.id"
                                            ></el-option>
                                        </el-select>
                                    </el-form-item>

                                    <el-form-item label="时间">
                                        <el-date-picker
                                                size="small"
                                                v-model="yszlQuery.yszlTime"
                                                type="daterange"
                                                range-separator="至"
                                                start-placeholder="开始日期"
                                                end-placeholder="结束日期"
                                                class="datepicker"
                                                @change="getYszlQueryDate"
                                        >
                                        </el-date-picker>
                                    </el-form-item>

                                    <el-form-item>
                                        <el-button
                                                size="small"
                                                type="primary"
                                                icon="el-icon-search"
                                                @click="handleSearchyszl"
                                        >查询
                                        </el-button
                                        >
                                    </el-form-item>
                                    <el-form-item>
                                        <el-button
                                                size="small"
                                                type="success"
                                                icon="el-icon-upload2"
                                                @click="downSORExcel"
                                        >导出
                                        </el-button>
                                    </el-form-item>
                                </el-form>
                            </div>
                        </el-col>
                    </el-row>
                    <el-table :data="yszlList" style="width: 100%">
                        <el-table-column type="index" label="序号" align="center">
                            <template #default="scope">
                                {{ scope.$index + 1 }}
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="roadName"
                                label="路内"
                                align="center"
                        ></el-table-column>
                        <el-table-column prop="" label="月租车卡" align="center">
                            <el-table-column
                                    prop="yzCarNum"
                                    label="车辆"
                                    align="center"
                            ></el-table-column>
                            <el-table-column
                                    prop="yzTotalCost"
                                    label="营收"
                                    align="center"
                            ></el-table-column>
                        </el-table-column>
                        <el-table-column label="流动车" align="center">
                            <!--<el-table-column-->
                            <!--prop="ldOrderNum"-->
                            <!--label="车次"-->
                            <!--align="center"-->
                            <!--&gt;</el-table-column>-->
                            <el-table-column label="支付方式" align="center">
                                <el-table-column
                                        prop="ldWxAmount"
                                        label="微信"
                                        align="center"
                                ></el-table-column>
                                <el-table-column
                                        prop="ldZfbAmount"
                                        label="支付宝"
                                        align="center"
                                ></el-table-column>
                                <el-table-column
                                        prop="ldDsfAmount"
                                        label="第三方"
                                        align="center"
                                ></el-table-column>
                                <el-table-column
                                        prop="ldXjAmount"
                                        label="现金支付"
                                        align="center"
                                ></el-table-column>
                                <el-table-column
                                        prop="ldQbAmount"
                                        label="钱包支付"
                                        align="center"
                                ></el-table-column>
                            </el-table-column>
                            <el-table-column
                                    prop="ldSumPaidAmount"
                                    label="营收"
                                    align="center"
                            ></el-table-column>
                        </el-table-column>

                        <el-table-column prop="" label="欠费" align="center">
                            <el-table-column
                                    prop="tdCounts"
                                    label="单数"
                                    align="center"
                            ></el-table-column>
                            <el-table-column
                                    prop="tdSumAmount"
                                    label="费用"
                                    align="center"
                            ></el-table-column>
                        </el-table-column>
                        <el-table-column prop="" label="免费车/辆" align="center">
                            <el-table-column
                                    prop="mfNbCount"
                                    label="内部车"
                                    align="center"
                            ></el-table-column>
                            <el-table-column
                                    prop="mfQyCount"
                                    label="企业（税免）车"
                                    align="center"
                            ></el-table-column>
                        </el-table-column>
                        <el-table-column label="总计" align="center">
                            <!--<el-table-column-->
                            <!--prop="zjCarNum"-->
                            <!--label="车次"-->
                            <!--align="center"-->
                            <!--&gt;</el-table-column>-->
                            <el-table-column
                                    prop="zjAmount"
                                    label="营收"
                                    align="center"
                            ></el-table-column>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                background
                                layout="total, prev, pager, next"
                                :current-page="yszlQuery.pageNum"
                                :page-size="yszlQuery.pageSize"
                                :total="yszlTotal"
                                @current-change="handlePageChangeyszl"
                        ></el-pagination>
                    </div>
        </div>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import {
        journalData,
        roadOrderData,
        queryAreaData,
        queryRoadData,
        getRoadMergeList,
        queryStreetData,
        getInnerDetailData,
        getFreeDetailData,
        roadOrderCount,
        roadOrderExcel,
        parkingOrderExcelNm,
        roadSORList2,
        summaryORExcel2,
        getRoadMonthCarStatistics,
        monthCarRentalDetailList,
        monthCarRentalStatistics,
        monthCarRentalExcel,
        mcrcStatisticsExcel,
        iflqStatistics,
    } from "../../api/index";

    export default {
        name: "reportFormL",
        setup() {
            const yszlQuery = reactive({
                yszlTime: "",
                startTime: "",
                pageNum: 1,
                pageSize: 15,
                endTime: "",
                variance: "",
                variance2: "0",
                area_id: "",
                street_id: "",
            });

            const query = reactive({
                road_id: "",
                // area_id: "",
                // street_id: "",
                name: "",
                time: "",
            });
            const result = reactive({
                road_list: [],
                orderWx: {},
                orderZfb: {},
                orderXj: {},
                orderSf: {},
                orderZj: {car_num: 0, car_price: 0},
            });
            const tableData1 = reactive([{}, {}, {}, {}, {}, {}]);
            const tableData2 = reactive([
                {
                    style: "运营中心",
                },
                {
                    style: "移动端",
                },
            ]);
            const tableData3 = reactive([{}, {}, {}, {}, {}, {}]);
            const tableData4 = reactive([{}, {}, {}, {}, {}, {}]);
            const tableData5 = reactive([{}, {}, {}, {}, {}, {}]);
            const tableData6 = reactive([{}, {}, {}, {}, {}, {}]);
            const options = reactive([
                {
                    value: "选项1",
                    label: "停车场名称1",
                },
                {
                    value: "选项2",
                    label: "停车场名称2",
                },
                {
                    value: "选项3",
                    label: "停车场名称3",
                },
                {
                    value: "选项4",
                    label: "停车场名称4",
                },
                {
                    value: "选项5",
                    label: "停车场名称5",
                },
            ]);

            const getQueryDate = () => {
                if (query.time == null || query.time == "") {
                    // query3Wx.start_time = "";
                    // query3Wx.end_time = "";
                    // query3Zfb.start_time = "";
                    // query3Zfb.end_time = "";
                    // query3Other.start_time = "";
                    // query3Other.end_time = "";
                    // query3Xj.start_time = "";
                    // query3Xj.end_time = "";
                    query3ldc.start_time = "";
                    query3ldc.end_time = "";
                    //
                    // queryNb.start_time = "";
                    // queryNb.end_time = "";
                    // queryMs.start_time = "";
                    // queryMs.end_time = "";
                    // queryNm.start_time = "";
                    // queryNm.end_time = "";
                    return false;
                }

                // query3Wx.start_time = dateFormat(query.time[0]) + " ";
                // query3Wx.end_time = dateFormat(query.time[1]) + " ";
                // query3Zfb.start_time = dateFormat(query.time[0]) + " ";
                // query3Zfb.end_time = dateFormat(query.time[1]) + " ";
                // query3Other.start_time = dateFormat(query.time[0]) + " ";
                // query3Other.end_time = dateFormat(query.time[1]) + " ";
                // query3Xj.start_time = dateFormat(query.time[0]) + " ";
                // query3Xj.end_time = dateFormat(query.time[1]) + " ";
                query3ldc.start_time = dateFormat(query.time[0]) + " ";
                query3ldc.end_time = dateFormat(query.time[1]) + " ";
                //
                // queryNb.startTime = dateFormat(query.time[0]) + " ";
                // queryNb.endTime = dateFormat(query.time[1]) + " ";
                // queryMs.startTime = dateFormat(query.time[0]) + " ";
                // queryMs.endTime = dateFormat(query.time[1]) + " ";
                // queryNm.startTime = dateFormat(query.time[0]) + " ";
                // queryNm.endTime = dateFormat(query.time[1]) + " ";

                // console.log(query3Wx.start_time);
            };

            const dateFormat = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
            };

            // 流动车流水订单
            const query3ldc = reactive({
                start_time: "",
                end_time: "",
                road_id: "",
                area_id: "",
                street_id: "",
            });

            const handleSearch = () => {
                // query3Wx.pageIndex = 1;
                //
                // query3Wx.road_id = query.road_id;
                // query3Wx.area_id = yszlQuery.area_id;
                // query3Wx.street_id = yszlQuery.street_id;
                // query3Zfb.pageIndex = 1;
                // query3Zfb.road_id = query.road_id;
                // query3Zfb.area_id = yszlQuery.area_id;
                // query3Zfb.street_id = yszlQuery.street_id;
                // query3Other.pageIndex = 1;
                // query3Other.road_id = query.road_id;
                // query3Other.area_id = yszlQuery.area_id;
                // query3Other.street_id = yszlQuery.street_id;
                // query3Xj.pageIndex = 1;
                // query3Xj.road_id = query.road_id;
                // query3Xj.area_id = yszlQuery.area_id;
                // query3Xj.street_id = yszlQuery.street_id;

                result.orderZj.car_num = 0;
                result.orderZj.car_price = 0.0;

                console.log(result.orderZj);
                // getData3Wx();
                // getData3Zfb();
                // getData3Other();
                // getData3Xj();

                setTimeout(function () {
                    result.orderZj.car_price = parseFloat(result.orderWx.car_price == null ? 0.0 : result.orderWx.car_price)
                        + parseFloat(result.orderZfb.car_price == null ? 0.0 : result.orderZfb.car_price)
                        + parseFloat(result.orderSf.car_price == null ? 0.0 : result.orderSf.car_price)
                        + parseFloat(result.orderXj.car_price == null ? 0.0 : result.orderXj.car_price)
                        + ".00";
                }, 500);
            };

            let formqjl = reactive({
                time: "",
                areaId: "",
                streetId: "",
                roadId: "",
                areas: [],
                streets: [],
                roads: [],
            });
            //获取区域下拉框数据
            const queryArea = reactive({});
            const getArea = () => {
                queryAreaData(queryArea).then((res) => {
                    formqjl.areas = res.data;
                });
            };
            getArea();
            //获取街道下拉框数据
            let queryStreet = reactive({
                areaId: yszlQuery.area_id,
            });
            const getStreet = () => {
                queryStreet.areaId = yszlQuery.area_id;
                yszlQuery.street_id = "";
                yszlQuery.variance = "";
                query.road_id = "";
                queryStreetData(queryStreet).then((res) => {
                    formqjl.streets = res.data;
                });
            };
            //获取路内下拉框数据
            const queryRoad = reactive({
                street_id: yszlQuery.street_id,
            });
            const getRoad = () => {
                queryRoad.street_id = yszlQuery.street_id;
                yszlQuery.variance = "";
                query.road_id = "";
                getRoadMergeList(queryRoad).then((res) => {
                    formqjl.roads = res.data;
                });
            };
            // const getRoadList = () => {
            //   // queryRoadData(query).then((res) => {
            //   //   console.log(res.data);
            //   //   result.road_list = res.data;
            //   // });
            // };

            // // 流动车流水订单微信
            // const query3Wx = reactive({
            //   pay_type: "2",
            //   start_time: "",
            //   end_time: "",
            //   road_id: "",
            //     area_id: "",
            //     street_id: "",
            //   pageIndex: 1,
            //   pageSize: 10,
            // });
            // const tableData3Wx = ref([]);
            // const pageTotal3Wx = ref(0);
            // const handlePageChange3Wx = (val) => {
            //   query3Wx.pageIndex = val;
            //   getData3Wx();
            // };
            // const getData3Wx = () => {
            //   roadOrderData(query3Wx).then((res) => {
            //     tableData3Wx.value = res.data.list;
            //     pageTotal3Wx.value = res.data.total;
            //   });
            //
            //   roadOrderCount(query3Wx).then((res) => {
            //     console.log(res.data);
            //     result.orderWx = res.data;
            //     result.orderZj.car_num += parseInt(result.orderWx.car_num);
            //
            //     // result.orderZj.car_price += parseFloat(
            //     //   result.orderWx.car_price == null ? 0.0 : result.orderWx.car_price
            //     // );
            //   });
            // };
            //
            // // 流动车流水订单支付宝
            // const query3Zfb = reactive({
            //   pay_type: "3",
            //   start_time: "",
            //   end_time: "",
            //   road_id: "",
            //     area_id: "",
            //     street_id: "",
            //   pageIndex: 1,
            //   pageSize: 10,
            // });
            // const tableData3Zfb = ref([]);
            // const pageTotal3Zfb = ref(0);
            // const handlePageChange3Zfb = (val) => {
            //   query3Zfb.pageIndex = val;
            //   getData3Zfb();
            // };
            // const getData3Zfb = () => {
            //   roadOrderData(query3Zfb).then((res) => {
            //     tableData3Zfb.value = res.data.list;
            //     pageTotal3Zfb.value = res.data.total;
            //   });
            //   roadOrderCount(query3Zfb).then((res) => {
            //     console.log(res.data);
            //     result.orderZfb = res.data;
            //     result.orderZj.car_num += parseInt(result.orderZfb.car_num);
            //     // result.orderZj.car_price += parseFloat(
            //     //   result.orderZfb.car_price == null ? 0.0 : result.orderZfb.car_price
            //     // );
            //   });
            // };
            //
            // // 流动车流水订单其他
            // const query3Other = reactive({
            //   pay_type: "1,4,8",
            //   start_time: "",
            //   end_time: "",
            //   road_id: "",
            //     area_id: "",
            //     street_id: "",
            //   pageIndex: 1,
            //   pageSize: 10,
            // });
            // const tableData3Other = ref([]);
            // const pageTotal3Other = ref(0);
            // const handlePageChange3Other = (val) => {
            //   query3Other.pageIndex = val;
            //   getData3Other();
            // };
            // const getData3Other = () => {
            //   roadOrderData(query3Other).then((res) => {
            //     tableData3Other.value = res.data.list;
            //     pageTotal3Other.value = res.data.total;
            //   });
            //   roadOrderCount(query3Other).then((res) => {
            //     console.log(res.data);
            //     result.orderSf = res.data;
            //     result.orderZj.car_num += parseInt(result.orderSf.car_num);
            //     // result.orderZj.car_price += parseFloat(
            //     //   result.orderSf.car_price == null ? 0.0 : result.orderSf.car_price
            //     // );
            //   });
            // };
            //
            // // 流动车流水订单现金
            //
            // const query3Xj = reactive({
            //   pay_type: "5",
            //   start_time: "",
            //   end_time: "",
            //   road_id: "",
            //     area_id: "",
            //     street_id: "",
            //   pageIndex: 1,
            //   pageSize: 10,
            // });
            // const tableData3Xj = ref([]);
            // const pageTotal3Xj = ref(0);
            // const handlePageChange3Xj = (val) => {
            //   query3Xj.pageIndex = val;
            //   getData3Xj();
            // };
            // const getData3Xj = () => {
            //   roadOrderData(query3Xj).then((res) => {
            //     tableData3Xj.value = res.data.list;
            //     pageTotal3Xj.value = res.data.total;
            //   });
            //   roadOrderCount(query3Xj).then((res) => {
            //     console.log(res.data);
            //     result.orderXj = res.data;
            //     result.orderZj.car_num += parseInt(result.orderXj.car_num);
            //     // result.orderZj.car_price += parseFloat(
            //     //   result.orderXj.car_price == null ? 0.0 : result.orderXj.car_price
            //     // );
            //     // result.orderZj.car_price = result.orderZj.car_price + ".00";
            //   });
            // };
            //
            // const exportOrderExcel = () => {
            //   // console.log(12233);
            //   query3ldc.road_id = query.road_id;
            //   query3ldc.area_id = yszlQuery.area_id;
            //   query3ldc.street_id = yszlQuery.street_id;
            //   roadOrderExcel(query3ldc).then((res) => {
            //     const url = window.URL.createObjectURL(new Blob([res]));
            //     const link = document.createElement("a");
            //     link.href = url;
            //     link.setAttribute("download", "流动车支付流水.xlsx");
            //     document.body.appendChild(link);
            //     link.click();
            //   });
            // };
            //
            // // ******************** 内部车、免费车明细 ********************
            // // ----------- 内部车、免费车明细查询按钮 -----------
            // const handleSearchNm = () => {
            //   queryNb.pageNum = 1;
            //   queryMs.pageNum = 1;
            //   getDataNb();
            //   getDataMs();
            //   getIFLQStatistics();
            // };
            // // ----------- 内部车、免费车明细Excel -----------
            // const queryNm = reactive({
            //   startTime: "",
            //   endTime: "",
            //   variance: "",
            //   variance2: "0",
            //     area_id: "",
            //     street_id: "",
            // });
            // const exportOrderExcelNm = () => {
            //   queryNm.variance = query.road_id;
            //   queryNm.area_id = yszlQuery.area_id;
            //   queryNm.street_id = yszlQuery.street_id;
            //   parkingOrderExcelNm(queryNm).then((res) => {
            //     const url = window.URL.createObjectURL(new Blob([res]));
            //     const link = document.createElement("a");
            //     link.href = url;
            //     link.setAttribute("download", "内部车、免费车明细.xlsx");
            //     document.body.appendChild(link);
            //     link.click();
            //   });
            // };
            // // ----------- 内部车、免费车明细统计 -----------
            // const nmStatistics = reactive({
            //   nbc: {},
            //   mfc: {},
            // });
            // const getIFLQStatistics = () => {
            //   queryNm.variance = query.road_id;
            //   queryNm.area_id = yszlQuery.area_id;
            //   queryNm.street_id = yszlQuery.street_id;
            //   iflqStatistics(queryNm).then((res) => {
            //     nmStatistics.nbc = res.data.nbc;
            //     nmStatistics.mfc = res.data.mfc;
            //   });
            // };
            // // ----------- 内部车明细 -----------
            // const queryNb = reactive({
            //   variance: "",
            //   variance2: "0",
            //   startTime: "",
            //   endTime: "",
            //   pageNum: 1,
            //   pageSize: 10,
            //     area_id: "",
            //     street_id: "",
            // });
            // const tableDataNb = ref([]);
            // const pageTotalNb = ref(0);
            // const handlePageChangeNb = (val) => {
            //   queryNb.pageNum = val;
            //   getDataNb();
            // };
            // const getDataNb = () => {
            //   queryNb.variance = query.road_id;
            //   queryNb.area_id = yszlQuery.area_id;
            //   queryNb.street_id = yszlQuery.street_id;
            //   getInnerDetailData(queryNb).then((res) => {
            //     tableDataNb.value = res.data.list;
            //     pageTotalNb.value = res.data.total;
            //   });
            // };
            // // ----------- 免费免税车明细 -----------
            // const queryMs = reactive({
            //   variance: "",
            //   variance2: "0",
            //   startTime: "",
            //   endTime: "",
            //   pageNum: 1,
            //   pageSize: 10,
            //   area_id: "",
            //   street_id: "",
            // });
            // const tableDataMs = ref([]);
            // const pageTotalMs = ref(0);
            // const handlePageChangeMs = (val) => {
            //   queryMs.pageNum = val;
            //   getDataMs();
            // };
            // const getDataMs = () => {
            //   queryMs.variance = query.road_id;
            //   tableDataMs.area_id = yszlQuery.area_id;
            //   tableDataMs.street_id = yszlQuery.street_id;
            //   getFreeDetailData(queryMs).then((res) => {
            //     tableDataMs.value = res.data.list;
            //     pageTotalMs.value = res.data.total;
            //   });
            // };
            // ******************** 内部车、免费车明细 ********************

            const yszlList = ref([]);
            const yszlTotal = ref(0);
            // 分页导航
            const handlePageChangeyszl = (val) => {
                yszlQuery.pageNum = val;
                getRoadSORList();
            };
            // 查询操作
            const handleSearchyszl = () => {
                yszlQuery.pageNum = 1;
                getRoadSORList();
            };
            // 营收总览查询
            const getRoadSORList = () => {
                roadSORList2(yszlQuery).then((res) => {
                    var data = res.data;
                    yszlList.value = data.list;
                    yszlTotal.value = data.total;
                });
            };
            // 营收总览Excel
            const downSORExcel = () => {
                summaryORExcel2(yszlQuery).then((res) => {
                    console.log(res.data);
                    console.log(document.createElement("a"));
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "路内营收统计.xlsx");
                    document.body.appendChild(link);
                    link.click();
                });
            };

            getRoadSORList();
            // getRoadList();

            // 分页导航
            const handlePageChangeYzcmx = (val) => {
                yszlQuery.pageNum = val;
                // getMonthCarRentalDetail();
            };

            // const yzcmxList = ref([]);
            // // 月租车明细
            // const getMonthCarRentalDetail = () => {
            //   monthCarRentalDetailList(yszlQuery).then((res) => {
            //     var data = res.data;
            //     yzcmxList.value = data.list;
            //     yszlTotal.value = data.total;
            //   });
            // };
            // const yzcmxStatistics = reactive({
            //   carCount: "",
            //   sumAmount: "",
            // });
            // // 月租车统计
            // const getMonthCarRentalStatistics = () => {
            //   monthCarRentalStatistics(yszlQuery).then((res) => {
            //     var data = res.data;
            //     yzcmxStatistics.carCount = data.carCount;
            //     yzcmxStatistics.sumAmount = data.sumAmount;
            //   });
            // };
            // const getMCRDQuery = () => {
            //   getMonthCarRentalDetail();
            //   getMonthCarRentalStatistics();
            // };
            // // 月租车明细Excel
            // const downMCRExcel = () => {
            //   monthCarRentalExcel(yszlQuery).then((res) => {
            //     console.log(res.data);
            //     console.log(document.createElement("a"));
            //     const url = window.URL.createObjectURL(new Blob([res]));
            //     const link = document.createElement("a");
            //     link.href = url;
            //     link.setAttribute("download", "路内月租车明细.xlsx");
            //     document.body.appendChild(link);
            //     link.click();
            //   });
            // };
            //
            // const yzkList = ref([]);
            // const yzkStatistics = reactive({
            //   frequency: "",
            //   carNumAll: "",
            //   amount: "",
            // });
            // // 月租卡统计
            // const getRoadMonthCarData = () => {
            //   getRoadMonthCarStatistics(yszlQuery).then((res) => {
            //     var data = res.data;
            //     yzkList.value = data;
            //     yzkStatistics.frequency = data[0].frequency;
            //     yzkStatistics.carNumAll = data[0].carNumAll;
            //     yzkStatistics.amount = data[0].amount;
            //   });
            // };
            // // const zykStatistics = reactive({
            // // 	carCount:"",
            // // 	sumAmount:"",
            //
            // // });
            // // // 月租车统计
            // // const getMonthCarRentalStatistics = ()=>{
            // // 	monthCarRentalStatistics(yszlQuery).then((res) => {
            // // 		var data = res.data;
            // // 		zykStatistics.carCount = data.carCount;
            // // 		zykStatistics.sumAmount = data.sumAmount;
            // // 	});
            // // };
            //
            // // 月租车卡统计Excel
            // const downMCRCSExcel = () => {
            //   mcrcStatisticsExcel(yszlQuery).then((res) => {
            //     console.log(res.data);
            //     const url = window.URL.createObjectURL(new Blob([res]));
            //     const link = document.createElement("a");
            //     link.href = url;
            //     link.setAttribute("download", "路内月租车卡统计.xlsx");
            //     document.body.appendChild(link);
            //     link.click();
            //   });
            // };

            //日期控件change方法
            const getYszlQueryDate = () => {
                console.log(yszlQuery.yszlTime);
                if (yszlQuery.yszlTime == null || yszlQuery.yszlTime == "" || yszlQuery.yszlTime == undefined) {
                    yszlQuery.startTime = "";
                    yszlQuery.endTime = "";
                } else {
                    yszlQuery.startTime = dataFormat(yszlQuery.yszlTime[0]);
                    yszlQuery.endTime = dataFormat(yszlQuery.yszlTime[1]);
                }
            };
            //日期格式化 yyyy-MM-dd
            const dataFormat = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
            };

            const handleTab = (tab, event) => {
                query.time = "";
                query.road_id = "";
                yszlQuery.yszlTime = "";
                yszlQuery.startTime = "";
                yszlQuery.endTime = "";
                yszlQuery.variance = "";
                yszlQuery.street_id = "";
                yszlQuery.area_id = "";
                console.log(tab.props.name);

                getRoadSORList();
                // getRoadList();

                // if (tab.props.name == "flowPay") {
                //   // 流水号
                //   result.orderZj.car_num = 0;
                //   result.orderZj.car_price = 0.0;
                //   // getData3Wx();
                //   // getData3Zfb();
                //   // getData3Other();
                //   // getData3Xj();
                //   getRoadList();
                //
                //   setTimeout(function () {
                //     result.orderZj.car_price = parseFloat(result.orderWx.car_price == null ? 0.0 : result.orderWx.car_price)
                //             + parseFloat(result.orderZfb.car_price == null ? 0.0 : result.orderZfb.car_price)
                //             + parseFloat(result.orderSf.car_price == null ? 0.0 : result.orderSf.car_price)
                //             + parseFloat(result.orderXj.car_price == null ? 0.0 : result.orderXj.car_price)
                //             + ".00";
                //   }, 500);
                // } else if (tab.props.name == "detailFree") {
                //   // getDataNb();
                //   // getDataMs();
                //   // getIFLQStatistics();
                //   getRoadList();
                // } else if (tab.props.name == "yszlSor") {
                //   getRoadSORList();
                //   getRoadList();
                // } else if (tab.props.name == "yzcmxDetail") {
                //   // getMonthCarRentalDetail();
                //   // getMonthCarRentalStatistics();
                //   getRoadList();
                // } else if (tab.props.name == "yzkDetail") {
                //   // getRoadMonthCarData();
                // }
            };

            return {
                query,
                result,
                // queryNm,
                getQueryDate,
                // handleSearchNm,
                // exportOrderExcelNm,
                formqjl,
                getArea,
                getStreet,
                getRoad,
                // getRoadList,
                handleSearch,
                handlePageChangeyszl,
                handleSearchyszl,
                tableData1,
                query3ldc,
                tableData2,
                tableData3,
                tableData4,
                tableData5,
                tableData6,
                // tableData3Wx,
                // pageTotal3Wx,
                // handlePageChange3Wx,
                // getData3Wx,
                // query3Wx,
                // tableData3Zfb,
                // pageTotal3Zfb,
                // handlePageChange3Zfb,
                // getData3Zfb,
                // query3Zfb,
                // tableData3Other,
                // pageTotal3Other,
                // handlePageChange3Other,
                // getData3Other,
                // query3Other,
                // tableData3Xj,
                // pageTotal3Xj,
                // handlePageChange3Xj,
                // getData3Xj,
                // query3Xj,
                handleTab,
                // exportOrderExcel,
                // tableDataNb,
                // pageTotalNb,
                // handlePageChangeNb,
                // getDataNb,
                // queryNb,
                // tableDataMs,
                // pageTotalMs,
                // handlePageChangeMs,
                // getDataMs,
                // queryMs,
                options,
                // yzkList,
                // yzkStatistics,
                yszlQuery,
                yszlList,
                yszlTotal,
                getRoadSORList,
                downSORExcel,
                getYszlQueryDate,

                // yzcmxList,
                // getRoadMonthCarData,
                // getMonthCarRentalDetail,
                // yzcmxStatistics,
                // getMonthCarRentalStatistics,
                // getMCRDQuery,
                // downMCRExcel,
                //
                // downMCRCSExcel,
                // getIFLQStatistics,
                // nmStatistics,
                handlePageChangeYzcmx,
            };
        },
        methods: {},
        data() {
            return {
                activeTab: "yszlSor",
            };
        },
    };
</script>
<style scoped>
    .amount-box {
        padding: 0.375rem 1rem;
        background-color: #f2f5fb;
        color: #4d5f77;
    }

    .amount-box h6 {
        display: block;
        margin-right: 0.625rem;
        float: left;
        width: 2.5rem;
        text-align: center;
        line-height: 2.5rem;
        font-size: 0.85rem;
        background-color: #b5d1ee;
        color: #fff;
        border-radius: 50%;
    }

    .amount-box .amount-des {
        float: left;
        font-size: 0.875rem;
    }

    .line-des .amount-des {
        margin-top: 10px;
        margin-right: 14px;
    }

    .amount-box p {
        line-height: 1.08rem;
        font-size: 0.875rem;
        font-weight: bold;
        margin-top: 1px;
    }

    .amount-box p span {
        margin-right: 0.25rem;
        color: #409eff;
        font-size: 1.2rem;
    }

    .handle-box {
        margin-bottom: 0;
    }

    .right-panel {
        margin-top: -1.25rem;
    }

    .table-title {
        float: left;
        font-weight: bold;
        font-size: 16px;
    }

    .table-title:before {
        content: "";
        display: inline-block;
        width: 3px;
        height: 16px;
        margin-right: 10px;
        background-color: #409eff;
        vertical-align: middle;
    }

    .amount-samll-box {
        float: right;
    }

    .amount-samll-box b {
        display: inline-block;
        margin-right: 0.625rem;
        text-align: center;
        width: 1.875rem;
        line-height: 1.875rem;
        font-size: 0.875rem;
        background-color: #b5d1ee;
        color: #fff;
        border-radius: 50%;
    }

    .amount-samll-box p {
        display: inline-block;
        font-weight: bold;
        font-size: 14px;
        color: #4d5f77;
    }

    .amount-samll-box p span {
        margin-right: 6px;
        color: #409eff;
        font-size: 18px;
    }

    .small-title {
        margin: 0.625rem 0;
    }

    .mr-20 {
        margin-right: 20px;
    }

    .mt40 {
        margin-top: 2.5rem;
    }
</style>
