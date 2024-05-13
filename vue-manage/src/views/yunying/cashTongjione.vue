<template>
    <div>
        <div class="container">
            <div class="top-panel f-cb">
                <div class="left-panel">
                    <el-button-group>
                        <el-button type="primary" icon="el-icon-s-grid">收入报表</el-button>
                        <el-button icon="el-icon-s-marketing" @click="goTo()"
                        >充值增长
                        </el-button
                        >
                        <el-button icon="el-icon-money" @click="goTo2()"
                        >缴费方式统计
                        </el-button
                        >
                        <el-button icon="el-icon-s-custom" @click="goTo3()"
                        >免单统计
                        </el-button
                        >
                    </el-button-group>
                </div>
                <div class="right-panel">
                    <el-form label-width="80px" inline size="small">
                        <el-form-item label="时间" class="mb0">
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

                        <el-form-item class="mb0">
                            <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch"
                            >查询
                            </el-button
                            >
                            <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
                        </el-form-item>
                        <el-form-item class="mb0">
                            <el-button size="small" type="success" icon="el-icon-upload2" @click="exportExcel"
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
                <el-col :span="24">
                    <el-card shadow="hover">
                        <el-table
                                :data="tableData"
                                border
                                class="table"
                                :max-height="tableH"
                                ref="multipleTable"
                                header-cell-class-name="table-header"
                                @selection-change="handleSelectionChange"
                        >
                            <el-table-column type="index" label="序号" width="55" align="center">

                            </el-table-column>
                            <el-table-column
                                    prop="areaName"
                                    label="区域"
                                    width="300"
                                    align="center"
                            ></el-table-column>
                            <el-table-column
                                    prop="streetName"
                                    label="街道"
                                    width="300"
                                    align="center"
                            >
                            </el-table-column>
                            <el-table-column prop="roadName" label="路内" align="center" v-if="menuType == '0'">
                            </el-table-column>
                            <el-table-column prop="roadName" label="停车场" align="center" v-if="menuType == '1'">
                            </el-table-column>

                            <el-table-column
                                    prop="receivable"
                                    label="应收"
                                    width="200"
                                    align="center"
                            >
                            </el-table-column>
                            <el-table-column
                                    prop="netReceipts"
                                    label="实收"
                                    width="200"
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
            </el-row>
        </div>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import {
        getRoadParkingOrderIncome,
        exporOrderIncome,
        queryAreaData,
        queryStreetData,
        queryRoadData,
        queryParkData,
    } from "../../api/index";

    export default {
        name: "orderTongji",
        data() {
            return {
                tableH: 0,
            };
        },
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
                pageSize: 16,
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            let form = reactive({
                time: "",
                areaId: "",
                streetId: "",
                roadId: "",
                areas: [],
                streets: [],
                roads: []
            })

            //获取区域下拉框数据
            const queryArea = reactive({});
            const getArea = () => {

                queryAreaData(queryArea).then((res) => {
                    form.areas = res.data;
                })
            }
            getArea();

            //获取街道下拉框数据
            let queryStreet = reactive({
                areaId: query.areaId
            });
            const getStreet = () => {

                queryStreet.areaId = query.areaId;
                query.roadId = "";
                query.streetId = "";
                queryStreetData(queryStreet).then((res) => {
                    form.streets = res.data;
                })
            }

            //获取路内下拉框数据
            const queryRoad = reactive({
                streetId: query.streetId
            });
            const getRoad = () => {
                query.roadId = "";
                queryRoad.streetId = query.streetId;
                queryRoadData(queryRoad).then((res) => {
                    form.roads = res.data;
                })
            };
            //获取停车场下拉框数据
            const getParks = () => {
                queryRoad.streetId = query.streetId;
                query.parkId = "";
                queryParkData(queryRoad).then((res) => {
                    form.roads = res.data;
                });
            };

            //导出excel
            const exportExcel = () => {
                exporOrderIncome(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]))
                    const link = document.createElement('a')
                    link.href = url
                    link.setAttribute('download', '收入报表.xls')
                    document.body.appendChild(link)
                    link.click()
                })
            }

            // 获取表格数据
            const getData = () => {
                getRoadParkingOrderIncome(query).then((res) => {
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
            };
            getData();

            // 查询操作
            const handleSearch = () => {
                query.pageIndex = 1;
                getData();
            };
            // 分页导航
            const handlePageChange = (val) => {
                query.pageIndex = val;
                getData();
            };

            //日期控件change方法
            const getQueryDate = () => {
                if (null == form.time || [] == form.time || "" == form.time) {
                    query.startTime = "";
                    query.endTime = "";
                } else {
                    query.startTime = dateFormat(form.time[0]);
                    query.endTime = dateFormat(form.time[1]);
                }
            }
            //日期格式化
            const dateFormat = (time) => {
                return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()}`
            }

            return {
                query,
                tableData,
                form,
                pageTotal,
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
                this.$router.push("/cashstatictwo");

                //带参数跳转
                // this.$router.push({ path: "/testDemo", query: { setid: 123456 } });
                // this.$router.push({ name: "testDemo", params: { setid: 111222 } });
            },
            goTo2() {
                //直接跳转
                this.$router.push("/cashstaticthree");
            },
            goTo3() {
                //直接跳转
                this.$router.push("/cashstaticfour");
            },
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + 'px';
        },
    };
</script>

