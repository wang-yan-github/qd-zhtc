<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline size="small">
                        <el-input size="small" class="handle-input mr10" placeholder="路内名称" clearable
                                  v-model="query.name" @keyup.enter="handleSearch()"></el-input>
                        <span class="dispinline ml5 font14 color666">停入时间：</span>
                        <el-date-picker size="small" class="datepicker" type="daterange"
                                        range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                                        v-model="form.time" @change="getQueryDate">
                        </el-date-picker>
                        <span class="dispinline ml5"></span>
                        <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">
                            查询
                        </el-button>
                        <span class="dispinline ml5"></span>
                        <el-button size="small" type="success" icon="el-icon-upload2" @click="exportExcel">
                            导出
                        </el-button>
                    </el-form>
                </div>
                <div class="clear"></div>
            </div>
            <el-table
                    :data="tableData" ref="multipleTable" @selection-change="handleSelectionChange"
                    border class="table" :max-height="tableH" header-cell-class-name="table-header">
                <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
                <el-table-column label="路内名称" align="center" prop="title"></el-table-column>
                <el-table-column label="泊位数" align="center" width="200" prop="stopcarnum"></el-table-column>
                <el-table-column label="临停车辆" align="center">
                    <el-table-column prop="counts" label="订单数" width="100" align="center">
                        <template #default="scope">
                            <el-link type="primary" href="javascript:;"
                                     @click="handleView(scope.$index, scope.row)"> {{ scope.row.counts }}
                            </el-link>
                        </template>
                    </el-table-column>
                    <el-table-column prop="counts2" label="非零订单数" width="100" align="center">
                        <template #default="scope">
                            <el-link type="primary" href="javascript:;"
                                     @click="handleView(scope.$index, scope.row, 0)"> {{ scope.row.counts2 }}
                            </el-link>
                        </template>
                    </el-table-column>
                    <el-table-column prop="sumAmount" label="应收金额" width="100" align="center">
                        <template #default="scope">
                            <el-link type="primary" href="javascript:;"
                                     @click="handleView(scope.$index, scope.row, 1)"> {{ scope.row.sumAmount }}
                            </el-link>
                        </template>
                    </el-table-column>
                    <el-table-column prop="paidAmount" label="实收金额" width="100" align="center">
                        <template #default="scope">
                            <el-link type="primary" href="javascript:;"
                                     @click="handleView(scope.$index, scope.row, 2)"> {{ scope.row.paidAmount }}
                            </el-link>
                        </template>
                    </el-table-column>
                    <el-table-column prop="unpaidAmount" label="在停金额" width="100" align="center">
                        <template #default="scope">
                            <el-link type="primary" href="javascript:;" @click="handleView(scope.$index, scope.row, 3)">
                                {{ scope.row.unpaidAmount }}
                            </el-link>
                        </template>
                    </el-table-column>
                    <el-table-column prop="unpaidAmount2" label="待缴费金额" width="100" align="center">
                        <template #default="scope">
                            <el-link type="primary" href="javascript:;" @click="handleView(scope.$index, scope.row, 4)">
                                {{ scope.row.unpaidAmount2 }}
                            </el-link>
                        </template>
                    </el-table-column>
                </el-table-column>
                <el-table-column label="补缴" align="center">
                    <el-table-column prop="bjcs" label="补缴次数" width="100" align="center">
                        <template #default="scope">
                            <p>{{ scope.row.bjcs }}</p>
                        </template>
                    </el-table-column>
                    <el-table-column prop="bjje" label="补缴金额" width="100" align="center">
                        <template #default="scope">
                            <p>{{ scope.row.bjje }}</p>
                        </template>
                    </el-table-column>
                </el-table-column>

                <el-table-column label="收费率" align="center">
                    <el-table-column prop="jfs" label="缴费数" width="100" align="center">
                        <template #default="scope">
                            <p>{{ scope.row.jfs }}</p>
                        </template>
                    </el-table-column>
                    <el-table-column prop="jfl" label="缴费率" width="100" align="center">
                        <template #default="scope">
                            <p>{{ scope.row.jfl }}</p>
                        </template>
                    </el-table-column>
                    <el-table-column prop="zzl" label="周转率" width="100" align="center">
                        <template #default="scope">
                            <p>{{ scope.row.zzl }}</p>
                        </template>
                    </el-table-column>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageNum"
                               :page-size="query.pageSize" :total="pageTotal"
                               @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

        <!-- 停车订单列表弹出框 -->
        <el-dialog :title="dialogT" v-model="viewVisible" width="1200px">
            <el-table :data="orderData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
                <el-table-column prop="order_no" label="订单号" align="center"></el-table-column>
                <el-table-column prop="car_no" label="车牌号" width="120" align="center">
                    <template #default="scope">
                        <el-tag size="small" v-if="scope.row.car_type == '1'" v-text="scope.row.car_no"></el-tag>
                        <el-tag size="small" type="success" v-else-if="scope.row.car_type == '2'"
                                v-text="scope.row.car_no"></el-tag>
                        <el-tag size="small" type="warning" v-else-if="scope.row.car_type == '3'"
                                v-text="scope.row.car_no"></el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="drivein_time" label="驶入时间" width="180" align="center"></el-table-column>
                <el-table-column prop="driveout_time" label="驶离时间" width="180" align="center"></el-table-column>
                <el-table-column prop="resTime" label="停车时长" width="100" align="center"></el-table-column>
                <el-table-column prop="sum_amount" width="100" label="应收金额" align="center">
                    <template #default="scope"><p>{{ Number(scope.row.sum_amount).toFixed(2) }}</p></template>
                </el-table-column>
                <el-table-column prop="paid_amount" width="100" label="实收金额" align="center">
                    <template #default="scope"><p>{{ Number(scope.row.paid_amount).toFixed(2) }}</p></template>
                </el-table-column>
                <el-table-column prop="unpaid_amount" width="100" label="在停/待缴费金额" align="center">
                    <template #default="scope"><p>{{ Number(scope.row.unpaid_amount).toFixed(2) }}</p></template>
                </el-table-column>
                <el-table-column prop="status_name" label="状态" width="100" align="center">
                    <template #default="scope">
                        <el-tag size="small" v-if="scope.row.status == '1'" v-text="scope.row.state_name"></el-tag>
                        <el-tag size="small" type="success" v-else-if="scope.row.status == '2'"
                                v-text="scope.row.state_name"></el-tag>
                        <el-tag size="small" type="warning" v-else-if="scope.row.status == '3'"
                                v-text="scope.row.state_name"></el-tag>
                        <el-tag size="small" type="danger" v-else-if="scope.row.status == '4'"
                                v-text="scope.row.state_name"></el-tag>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        background
                        layout="total, prev, pager, next"
                        :current-page="orderQuery.pageNum"
                        :page-size="orderQuery.pageSize"
                        :total="pageTotal2"
                        @current-change="handlePageChange2"
                ></el-pagination>
            </div>
            <template #footer>
                <span class="dialog-footer">
                  <el-button @click="viewVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<script>
    import {reactive, ref} from "vue";
    import {
        roadReportJfl,
        getRoadParkingOrderList,
        exportRoadReportJfl,
    } from "../../api/index";

    export default {
        name: "roadReportJfl",
        components: {},
        data() {
            return {
                tableH: 0,
            };
        },
        setup() {
            const query = reactive({
                name: "",
                startTime: "",
                endTime: "",
                pageNum: 1,
                pageSize: 20,
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            let form = reactive({
                time: "",
            });
            // 获取表格数据
            const getData = () => {
                roadReportJfl(query).then((res) => {
                    // console.log(res)
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
            };
            getData();

            // 查询操作
            const handleSearch = () => {
                query.pageNum = 1;
                getData();
            };
            // 分页导航
            const handlePageChange = (val) => {
                query.pageNum = val;
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
            };
            //日期格式化
            const dateFormat = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
            };


            //导出excel
            const exportExcel = () => {
                exportRoadReportJfl(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', '路内缴费率统计.xls');
                    document.body.appendChild(link);
                    link.click()
                })
            };

            //点击订单数 弹窗订单列表
            const viewVisible = ref(false);
            const dialogT = ref("订单列表");
            const orderData = ref([]);
            const orderQuery = reactive({
                pageNum: 1,
                pageSize: 10,
                startTime: "",
                endTime: "",
                roadId: "",
                pageType: "",
                parking_type: "0",
            });
            const pageTotal2 = ref(0);
            const handleView = (index, row, type) => {
                dialogT.value = row.title + "订单列表";

                orderQuery.pageNum = 1;
                orderQuery.startTime = query.startTime;
                orderQuery.endTime = query.endTime;
                orderQuery.roadId = row.id;
                orderQuery.pageType = type;
                // 订单表格数据
                getRoadParkingOrderList(orderQuery).then((res) => {
                    orderData.value = res.data.list;
                    pageTotal2.value = res.data.total;
                });
                viewVisible.value = true;
            };
            const handlePageChange2 = (val) => {
                orderQuery.pageNum = val;
                orderQuery.startTime = query.startTime;
                orderQuery.endTime = query.endTime;
                // 订单表格数据
                getRoadParkingOrderList(orderQuery).then((res) => {
                    orderData.value = res.data.list;
                    pageTotal2.value = res.data.total;
                });
            };

            return {
                handleSearch,
                handlePageChange,
                getQueryDate,
                query,
                form,
                tableData,
                pageTotal,
                exportExcel,
                viewVisible,
                dialogT,
                orderData,
                orderQuery,
                pageTotal2,
                handleView,
                handlePageChange2,
            };
        },
        methods: {
            handleSelectionChange(data) {
                this.selectedData = data;
            },
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + "px";
        },
    };
</script>
<style scoped>
</style>
