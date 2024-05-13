<template>
    <div>
        <div class="container">
            <el-row>
                <el-col :span="24">
                    <div class="top-panel">
                        <el-form label-width="80px" inline size="small">
                            <el-form-item>
                                <el-input placeholder="" v-model="query.money" class="w40"></el-input>
                                <span class="color666 ml5 dispinline">元以上欠费</span>
                            </el-form-item>
                            <el-form-item label="驶入时间">
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
                                            @change="getPark"
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
                                                size="small"
                                                placeholder="所有停车场"
                                                class="w100"
                                        >
                                            <el-option v-if="menuType == '1'"
                                                       v-for="item in form.roads"
                                                       :key="item.id"
                                                       :label="item.park_name"
                                                       :value="item.id"
                                            ></el-option>
                                        </el-select>
                            </el-form-item>

                            <el-form-item>
                                <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch"
                                >查询
                                </el-button
                                >
                                <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
                            </el-form-item>
                            <el-form-item>
                                <el-button size="small" type="success" icon="el-icon-upload2" @click="exportExcel"
                                >导出
                                </el-button
                                >
                                <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
            </el-row>

            <el-table
                    :data="tableData"
                    border
                    class="table"
                    :max-height="tableH"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
            >
                <el-table-column type="index" label="序号" width="55" align="center">

                </el-table-column>
                <el-table-column
                        prop="carNo"
                        label="车牌号"
                        width="200"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="inTime"
                        label="最近的驶入时间"
                        align="center"
                >
                </el-table-column>
                <el-table-column prop="outTime" label="最近的驶出时间" align="center"></el-table-column>

                <el-table-column
                        prop="totalArrears"
                        label="欠费总额(元)"
                        width="200"
                        align="center"
                >
                </el-table-column>
                <el-table-column v-if="false" prop="carNoId" label="车牌id" width="200" align="center">
                </el-table-column>
                <!-- <el-table-column prop="name" label="车主姓名" width="200" align="center">
                </el-table-column> -->
                <el-table-column prop="phone" label="车主电话" width="200" align="center">
                </el-table-column>
                <el-table-column prop="" label="操作项" width="160" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-view"
                                @click="handleView(scope.$index, scope.row)"
                        >详情
                        </el-button>
                    </template>
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
        </div>
        <!--查看弹出框-->
        <el-dialog title="查看详情" v-model="viewVisible" width="50%">


            <el-row>
                <el-col :span="24">

                    <el-table
                            :data="tableData2"
                            border
                            class="table"
                            ref="multipleTable"
                            header-cell-class-name="table-header"
                    >
                        <el-table-column prop="orderNo" label="订单号" align="center">
                        </el-table-column>
                        <el-table-column prop="roadName" :label="typeName" align="center">
                        </el-table-column>
                        <el-table-column
                                prop="amount"
                                label="金额"
                                align="center"
                                width="100"
                        ></el-table-column>
                        <el-table-column
                                prop="inTime"
                                label="驶入时间"
                                width="180"
                                align="center"
                        ></el-table-column>
                        <el-table-column width="180" prop="outTime" label="驶出时间" align="center">

                        </el-table-column>
                        <el-table-column prop="resitime" label="停车时长" align="center" width="180">
                        </el-table-column>

                    </el-table>
                    <!-- <div class="pagination">
                      <el-pagination
                        background
                        layout="total, prev, pager, next"
                        :total="pageTotal2"
                      ></el-pagination>
                    </div> -->
                </el-col>
            </el-row>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewVisible = false">关 闭</el-button>
        </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import { useRoute, useRouter } from 'vue-router'
    import {ElMessage, ElMessageBox} from "element-plus";
    import {
        getRoadParkingLargeArrears,
        exporLargeArrears,
        getRoadParkingLargeArrearsOrders,
        queryAreaData,
        queryStreetData,
        queryRoadData,
        queryParkData
    } from "../../api/index";

    export default {
        name: "roadTongji",
        data() {
            return {
                tableH: 0,
            };
        },
        setup() {
            // 获取当前路由
            const route = useRoute()
            // 获取路由实例
            const router = useRouter()

            const query2 = route.query;
            console.log(query2);
            const menuType = ref("")
            const startTime1 = ref("")
            const endTime1 = ref("")
            const time1 = ref([])
            if (typeof query2 != 'undefined' && query2 != null && query2 != '') {
                if (typeof query2.menuType != 'undefined' && query2.menuType != null && query2.menuType != '') {
                    menuType.value = query2.menuType;
                } else {
                    menuType.value = localStorage.getItem("menuType");
                }

                if (typeof query2.startTime != 'undefined' && query2.startTime != null && query2.startTime != '') {
                    startTime1.value = query2.startTime;
                    time1.value = [query2.startTime,query2.startTime];
                }
                if (typeof query2.endTime != 'undefined' && query2.endTime != null && query2.endTime != '') {
                    endTime1.value = query2.endTime;
                    time1.value = [query2.endTime1,query2.endTime1];
                }

                if (query2.startTime != 'undefined' && query2.startTime != null && query2.startTime != '' && query2.endTime != 'undefined' && query2.endTime != null && query2.endTime != '') {
                    time1.value = [query2.startTime,query2.endTime1];
                }
            } else {
                menuType.value = localStorage.getItem("menuType");
            }
            console.log(menuType.value);

            const query = reactive({
                money: 0,
                areaId: "",
                streetId: "",
                roadId: "",
                startTime: startTime1,
                endTime: endTime1,
                pageIndex: 1,
                pageSize: 15,
                menuType: menuType.value,
            });
            const tableData = ref([]);
            const tableData2 = ref([]);
            const viewVisible = ref(false);
            const pageTotal = ref(0);
            const typeName = ref("");
            let form = reactive({
                time: time1,
                phone: "",
                areaId: "",
                streetId: "",
                roadId: "",
                areas: [],
                streets: [],
                roads: [],
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
            }

            //获取停车场下拉框数据
            const getPark = () => {
                query.roadId = "";
                queryRoad.streetId = query.streetId;
                queryParkData(queryRoad).then((res) => {
                    form.roads = res.data;
                });
            };

            //导出excel
            const exportExcel = () => {
                exporLargeArrears(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]))
                    const link = document.createElement('a')
                    link.href = url
                    link.setAttribute('download', '大额欠费.xls')
                    document.body.appendChild(link)
                    link.click()
                })
            }

            //详情
            let road = reactive({
                carNoId: "",
                menuType: "",
                money: 0,
                areaId: "",
                streetId: "",
                roadId: "",
                startTime: startTime1,
                endTime: endTime1,
                pageIndex: 1,
                pageSize: 15,
            });

            const handleView = (index, row) => {
                // idx = index;
                road.carNoId = row.carNoId;
                road.menuType = menuType.value;
                road.areaId = query.areaId;
                road.streetId = query.streetId;
                road.roadId = query.roadId;
                road.startTime = query.startTime;
                road.endTime = query.endTime;
                getRoadParkingLargeArrearsOrders(road).then((res) => {
                    console.log(res)
                    tableData2.value = res.data.list;
                }).then(() => {
                    viewVisible.value = true;
                });
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

            // 获取表格数据
            const getData = () => {
                getRoadParkingLargeArrears(query).then((res) => {
                    tableData.value = res.data.list.list;
                    pageTotal.value = res.data.list.total;
                    typeName.value = res.data.typeName;
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

            return {
                query,
                tableData,
                tableData2,
                typeName,
                form,
                road,
                pageTotal,
                viewVisible,
                exportExcel,
                exporLargeArrears,
                handleView,
                handleSearch,
                getQueryDate,
                getArea,
                getStreet,
                getRoad,
                getPark,
                handlePageChange,
                menuType,
            };
        },
        methods: {},
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + 'px';
        },
    };
</script>

