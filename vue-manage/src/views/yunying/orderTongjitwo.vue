<template>
    <div>
        <div class="container">
            <div class="top-panel">
                <div class="left-panel">
                    <el-button-group>
                        <el-button icon="el-icon-s-grid" @click="goTo()"
                        >基础数据
                        </el-button
                        >
                        <el-button type="primary" icon="el-icon-s-marketing"
                        >订单增长
                        </el-button
                        >
                        <el-button icon="el-icon-s-order" @click="goTo2()"
                        >订单状态占比
                        </el-button
                        >
                    </el-button-group>
                </div>
                <div class="right-panel">
                    <el-form label-width="80px" inline size="small">
                        <el-form-item label="停入时间">
                            <el-date-picker
                                    size="small"
                                    v-model="form.time"
                                    type="daterange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    class="datepicker"
                                    @change="getQueryDate"
                            >
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item label="选择层级" v-if="menuType == '0'">
                            <el-select
                                    clearable
                                    v-model="query.areaId"
                                    filterable
                                    placeholder="所有区域"
                                    class="w100"
                                    @change="getStreet"
                            >
                                <el-option
                                        v-for="item in form.areas"
                                        :key="item.id"
                                        :label="item.area_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                            <span class="mar5 color999"></span>
                            <el-select
                                    clearable
                                    v-model="query.streetId"
                                    filterable
                                    placeholder="所有街道"
                                    class="w100"
                                    @change="getRoad"
                            >
                                <el-option
                                        v-for="item in form.streets"
                                        :key="item.id"
                                        :label="item.street_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                            <span class="mar5 color999"></span>
                            <el-select
                                    clearable
                                    v-model="query.roadId"
                                    filterable
                                    placeholder="所有路内"
                                    class="w100"
                            >
                                <el-option
                                        v-for="item in form.roads"
                                        :key="item.id"
                                        :label="item.road_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="选择层级" v-if="menuType == '1'">
                            <el-select
                                    clearable
                                    v-model="query.areaId"
                                    filterable
                                    placeholder="所有区域"
                                    class="w100"
                                    @change="getStreet"
                            >
                                <el-option
                                        v-for="item in form.areas"
                                        :key="item.id"
                                        :label="item.area_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                            <span class="mar5 color999"></span>
                            <el-select
                                    clearable
                                    v-model="query.streetId"
                                    filterable
                                    placeholder="所有街道"
                                    class="w100"
                                    @change="getParks"
                            >
                                <el-option
                                        v-for="item in form.streets"
                                        :key="item.id"
                                        :label="item.street_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                            <span class="mar5 color999"></span>
                            <el-select
                                    clearable
                                    v-model="query.parkId"
                                    filterable
                                    placeholder="所有停车场"
                                    class="w100"
                            >
                                <el-option
                                        v-for="item in form.roads"
                                        :key="item.id"
                                        :label="item.park_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button
                                    size="small"
                                    type="primary"
                                    icon="el-icon-search"
                                    @click="handleSearch"
                            >查询
                            </el-button
                            >
                            <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
                        </el-form-item>
                        <el-form-item>
                            <el-button
                                    size="small"
                                    type="success"
                                    icon="el-icon-upload2"
                                    @click="exportExcel"
                            >导出
                            </el-button
                            >
                            <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
                        </el-form-item>
                    </el-form>
                </div>
                <div class="clear"></div>
            </div>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-card shadow="hover">
                        <el-table
                                :data="tableData"
                                border
                                class="table"
                                ref="multipleTable"
                                header-cell-class-name="table-header"
                                @selection-change="handleSelectionChange"
                        >
                            <el-table-column
                                    type="index"
                                    label="序号"
                                    width="55"
                                    align="center"
                            >
                            </el-table-column>
                            <el-table-column
                                    prop="time"
                                    label="停入时间"
                                    align="center"
                            ></el-table-column>
                            <el-table-column
                                    prop="orderNum"
                                    label="订单数量"
                                    align="center"
                            >
                            </el-table-column>
                            <el-table-column prop="payNum" label="缴费数量" align="center">
                            </el-table-column>

                            <el-table-column
                                    prop="proportion"
                                    label="缴费占比"
                                    align="center"
                            >
                            </el-table-column>

                        </el-table>

                        <div class="pagination">
                            <el-pagination
                                    background
                                    layout="total, prev, pager, next"
                                    :current-page="query.pageIndex"
                                    :page-size="query.pageSize"
                                    :total="pageTotal"
                                    @current-change="handlePageChange"
                            ></el-pagination>
                        </div>

                    </el-card>
                </el-col>
                <el-col :span="12">
                    <el-card shadow="hover">
                        <div id="main" class="schartmain"></div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import * as echarts from "echarts";
    import {
        getRoadParkingOrderGrowth,
        getRoadParkingNearly8DaysOrderGrowth,
        exportOrderGrowth,
        queryAreaData,
        queryStreetData,
        queryRoadData,
        queryParkData,
    } from "../../api/index";

    export default {
        name: "orderTongji",
        setup() {
            const menuType = localStorage.getItem("menuType");

            const query = reactive({
                areaId: "",
                streetId: "",
                roadId: "",
                parkId: "",
                startTime: "",
                endTime: "",
                pageIndex: 1,
                pageSize: 15,
            });
            const tableData = ref([]);

            const table = reactive({
                time: "",

            });

            let form = ref({
                time: "",
                areaId: "",
                streetId: "",
                roadId: "",
                areas: [],
                streets: [],
                roads: [],
                timeList: [],
                orderNumList: [],
                payNumList: [],
            });

            const tableValue = reactive({
                timeList: [],
                orderNumList: [],
                payNumList: [],

            });

            //获取区域下拉框数据
            const queryArea = reactive({});
            const getArea = () => {

                queryAreaData(queryArea).then((res) => {
                    form.value.areas = res.data;
                });
            };
            getArea();

            //获取街道下拉框数据
            let queryStreet = reactive({
                areaId: query.areaId,
            });
            const getStreet = () => {
                queryStreet.areaId = query.areaId;
                query.roadId = "";
                query.streetId = "";
                queryStreetData(queryStreet).then((res) => {
                    form.value.streets = res.data;
                });
            };

            //获取路内下拉框数据
            const queryRoad = reactive({
                streetId: query.streetId,
            });
            const getRoad = () => {
                query.roadId = "";
                queryRoad.streetId = query.streetId;
                queryRoadData(queryRoad).then((res) => {
                    form.value.roads = res.data;
                });
            };
            //获取停车场下拉框数据
            const getParks = () => {
                queryRoad.streetId = query.streetId;
                query.parkId = "";
                queryParkData(queryRoad).then((res) => {
                    form.value.roads = res.data;
                });
            };

            let temp = ref([]);
            const pageTotal = ref(0);

            // 获取表格数据
            const getData = () => {
                getRoadParkingOrderGrowth(query).then((res) => {
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
                getRoadParkingNearly8DaysOrderGrowth(query).then((res) => {

                    // options4.xAxis = res.data.timeList;
                    options4.xAxis[0].data = res.data.timeList;
                    options4.series[0].data = res.data.orderNumList;
                    options4.series[1].data = res.data.payNumList;
                    var chartDom = document.getElementById("main");
                    var myChart = echarts.init(chartDom);
                    myChart.setOption(options4);
                    console.log(options4.xAxis);
                });
            };
            getData();

            // 查询操作
            const handleSearch = () => {
                query.pageIndex = 1;
                getData();
            };
            //导出excel
            const exportExcel = () => {
                exportOrderGrowth(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "订单增长.xls");
                    document.body.appendChild(link);
                    link.click();
                });
            };

            //日期控件change方法
            const getQueryDate = () => {
                if (
                    null == form.value.time ||
                    [] == form.value.time ||
                    "" == form.value.time
                ) {
                    query.startTime = "";
                    query.endTime = "";
                } else {
                    query.startTime = dateFormat(form.value.time[0]);
                    query.endTime = dateFormat(form.value.time[1]);
                }
                query.startTime = dateFormat(form.value.time[0]);
                query.endTime = dateFormat(form.value.time[1]);
            };
            //日期格式化
            const dateFormat = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
            };

            // 分页导航
            const handlePageChange = (val) => {
                query.pageIndex = val;
                getData();
            };
            const options4 = {
                title: {
                    text: "近八天订单增长分析",
                    show: true,
                    textStyle: {
                        fontSize: 16,
                        fontWeight: "normal",
                        color: "#303133",
                    },
                },
                tooltip: {
                    trigger: "axis",
                    axisPointer: {
                        type: "cross",
                        label: {
                            backgroundColor: "#6a7985",
                        },
                    },
                },
                legend: {
                    data: ["订单总数", "缴费订单数"],
                },
                grid: {
                    left: "20px",
                    right: "36px",
                    bottom: "3%",
                    containLabel: true,
                },
                xAxis: [
                    {
                        type: "category",
                        boundaryGap: false,
                        data: ["1", "2"],
                    },
                ],
                yAxis: [
                    {
                        type: "value",
                    },
                ],
                series: [
                    {
                        name: "订单总数",
                        type: "line",
                        // stack: "Total",
                        areaStyle: {},
                        emphasis: {
                            focus: "series",
                        },
                        // data: [120, 132, 101, 134, 90, 230, 210],
                        data: tableValue.orderNumList,
                    },
                    {
                        name: "缴费订单数",
                        type: "line",
                        // stack: "Total",
                        areaStyle: {},
                        emphasis: {
                            focus: "series",
                        },
                        // data: [220, 182, 191, 234, 290, 330, 310],
                        data: tableValue.payNumList,
                    },
                ],


            };

            // console.log(2222);
            // console.log(temp);


            return {
                query,
                pageTotal,
                tableData,
                form,
                tableValue,
                queryArea,

                queryStreet,
                queryRoad,
                options4,
                temp,
                menuType,
                getArea,
                getStreet,
                getQueryDate,
                getRoad,
                getParks,
                handleSearch,
                handlePageChange,
                exportExcel,
            };
        },
        methods: {
            goTo() {
                //直接跳转
                this.$router.push("/orderstatic");

                //带参数跳转
                // this.$router.push({ path: "/testDemo", query: { setid: 123456 } });
                // this.$router.push({ name: "testDemo", params: { setid: 111222 } });
            },
            goTo2() {
                //直接跳转
                this.$router.push("/orderstaticthree");

                //带参数跳转
                // this.$router.push({ path: "/testDemo", query: { setid: 123456 } });
                // this.$router.push({ name: "testDemo", params: { setid: 111222 } });
            },
            init() {
                var chartDom = document.getElementById("main");
                chartDom.removeAttribute("_echarts_instance_");
                var myChart = echarts.init(chartDom);

                myChart.setOption(this.options4);
            },
        },
        mounted() {
            this.init();
        },
    };
</script>

<style scoped>
    .schartmain {
        width: 100%;
        height: 603px;
    }
</style>
