<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="24">
                <div class="list-box">
                    <div class="list-item">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-1">
                                <i class="el-icon-s-custom grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{ census.money }}</div>
                                    <div>总营收(元)</div>
                                </div>
                            </div>
                        </el-card>
                    </div>
                    <div class="list-item">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-2">
                                <i class="el-icon-s-finance grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <el-link type="danger" href="javascript:;" @click="handleView2()">
                                        <div class="grid-num">{{ census.byAmount }}</div>
                                    </el-link>
                                    <div>包月收入(元)</div>
                                </div>
                            </div>
                        </el-card>
                    </div>
                    <div class="list-item">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-6">
                                <i class="el-icon-s-order grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{ census.sumAmount }}</div>
                                    <!--<div class="grid-bfb">-->
                                    <!--<i class="dangertext">{{census.bl}}</i>-->
                                    <!--</div>-->
                                    <div>临停应收(元)</div>
                                    <el-tooltip
                                            class="item"
                                            effect="dark"
                                            content="临停欠费金额(元)"
                                            placement="bottom"
                                    >
                                        <el-link
                                                type="danger"
                                                href="javascript:;"
                                                @click="moneyLink()"
                                        >欠费 {{ census.unpaidAmount }}元
                                        </el-link
                                        >
                                    </el-tooltip>
                                </div>
                            </div>
                        </el-card>
                    </div>
                    <div class="list-item">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-3">
                                <i class="el-icon-success grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{census.paidAmount}}</div>
                                    <div>临停实收(元)</div>
                                </div>
                            </div>
                        </el-card>
                    </div>
                    <!--<div class="list-item">-->
                    <!--<el-card shadow="hover" :body-style="{ padding: '0px' }">-->
                    <!--<div class="grid-content grid-con-4">-->
                    <!--<i class="el-icon-circle-plus grid-con-icon"></i>-->
                    <!--<div class="grid-cont-right">-->
                    <!--<div class="grid-num">{{ census.czAmount }}</div>-->
                    <!--<div>充值收入(元)</div>-->
                    <!--</div>-->
                    <!--</div>-->
                    <!--</el-card>-->
                    <!--</div>-->
                </div>
                <el-card shadow="hover">
                    <template #header>
                        <div class="clearfix">
                            <span>今日应收款排名(该页面以车辆停入时间统计停车订单收入金额)</span>
                        </div>
                    </template>
                    <div class="handle-box">
                        <div class="left-panel">
                            <el-form inline label-width="80" size="small" class="lineH0">
                                <el-form-item label="路内名称" class="search-mb0">
                                    <el-input
                                            v-model="query.name"
                                            @keyup.enter="handleSearch"
                                            size="small"
                                            placeholder="路内名称"
                                            clearable
                                            @clear="clearPark"
                                    ></el-input>
                                </el-form-item>

                                <el-form-item label="停入时间" class="search-mb0">
                                    <el-date-picker
                                            v-model="queryTime.time"
                                            placeholder=""
                                            type="daterange"
                                            range-separator="至"
                                            start-placeholder="开始日期"
                                            end-placeholder="结束日期"
                                            @change="getQueryDate"
                                            clearable
                                    ></el-date-picker>
                                </el-form-item>
                                <el-form-item label="" class="search-mb0">
                                    <el-button
                                            size="small"
                                            type="primary"
                                            icon="el-icon-search"
                                            @click="handleSearch"
                                    >查询
                                    </el-button>
                                </el-form-item>
                                <el-form-item label="" class="search-mb0">
                                    <el-button
                                            size="small"
                                            type="success"
                                            icon="el-icon-upload2"
                                            @click="exportSearch"
                                    >导出
                                    </el-button>
                                </el-form-item>
                            </el-form>
                        </div>

                        <div class="clear"></div>
                    </div>

                    <el-table :show-header="true" height="380" :data="todoList" border class="table" ref="multipleTable"
                              header-cell-class-name="table-header" :span-method="objectSpanMethod">
                        <!--<el-table-column type="index" label="序号" width="60" align="center">-->
                        <!--</el-table-column>-->
                        <el-table-column label="路内名称" prop="title" align="center">
                            <template #default="scope">
                                <el-link type="primary" href="javascript:;" @click="parkLink(scope.row)">
                                    {{ scope.row.title }}
                                </el-link>
                            </template>
                        </el-table-column>
                        <el-table-column label="泊位数" width="100" align="center" prop="stopcarnum">
                        </el-table-column>
                        <el-table-column label="月租车辆" align="center">
                            <el-table-column label="单价" width="100" align="center" prop="months_unit_price">
                                <template #default="scope">
                                    <p>{{ scope.row.months_unit_price ? scope.row.months_unit_price : "0.00" }}</p>
                                </template>
                            </el-table-column>
                            <el-table-column label="数量" width="100" align="center" prop="months">
                                <template #default="scope">
                                    <p>{{ scope.row.months ? scope.row.months : 0 }}</p>
                                </template>
                            </el-table-column>
                            <el-table-column label="金额" width="100" align="center" prop="amount">
                                <template #default="scope">
                                    <p>{{ scope.row.amount ? scope.row.amount : "0.00" }}</p>
                                </template>
                            </el-table-column>
                        </el-table-column>
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
                                    <el-link type="primary" href="javascript:;"
                                             @click="handleView(scope.$index, scope.row, 3)"> {{ scope.row.unpaidAmount
                                        }}
                                    </el-link>
                                </template>
                            </el-table-column>
                            <el-table-column prop="unpaidAmount2" label="待缴费金额" width="100" align="center">
                                <template #default="scope">
                                    <el-link type="primary" href="javascript:;"
                                             @click="handleView(scope.$index, scope.row, 4)"> {{ scope.row.unpaidAmount2
                                        }}
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
                        <el-pagination
                                background
                                layout="total, prev, pager, next"
                                :current-page="query.pageNum"
                                :page-size="query.pageSize"
                                :total="pageTotal"
                                @current-change="handlePageChange"
                        ></el-pagination>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-card shadow="hover">
                    <!-- <schart ref="bar" class="schart" canvasId="bar" :options="options"></schart> -->
                    <div id="main" class="schart"></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="hover">
                    <!-- <schart
                                ref="line"
                                class="schart"
                                canvasId="line"
                                :options="options2"
                              ></schart> -->
                    <div id="mainline" class="schart"></div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 停车订单列表弹出框 -->
        <el-dialog :title="dialogT" v-model="viewVisible" width="1200px">
            <el-table
                    :data="orderData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
            >
                <el-table-column
                        prop="order_no"
                        label="订单号"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="car_no"
                        label="车牌号"
                        width="120"
                        align="center"
                >
                    <template #default="scope">
                        <el-tag
                                size="small"
                                v-if="scope.row.car_type == '1'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="success"
                                v-else-if="scope.row.car_type == '2'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="warning"
                                v-else-if="scope.row.car_type == '3'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="drivein_time"
                        label="驶入时间"
                        width="180"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="driveout_time"
                        label="驶离时间"
                        width="180"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="resTime"
                        label="停车时长"
                        width="100"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="sum_amount"
                        width="100"
                        label="应收金额"
                        align="center"
                >
                    <template #default="scope">
                        <p>{{ Number(scope.row.sum_amount).toFixed(2) }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="paid_amount"
                        width="100"
                        label="实收金额"
                        align="center"
                >
                    <template #default="scope">
                        <p>{{ Number(scope.row.paid_amount).toFixed(2) }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="unpaid_amount"
                        width="100"
                        label="在停/待缴费金额"
                        align="center"
                >
                    <template #default="scope">
                        <p>{{ Number(scope.row.unpaid_amount).toFixed(2) }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="status_name"
                        label="状态"
                        width="100"
                        align="center"
                >
                    <template #default="scope">
                        <el-tag
                                size="small"
                                v-if="scope.row.status == '1'"
                                v-text="scope.row.state_name"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="success"
                                v-else-if="scope.row.status == '2'"
                                v-text="scope.row.state_name"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="warning"
                                v-else-if="scope.row.status == '3'"
                                v-text="scope.row.state_name"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="danger"
                                v-else-if="scope.row.status == '4'"
                                v-text="scope.row.state_name"
                        ></el-tag>
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

        <!-- 包月收入明细列表弹出框 -->
        <el-dialog :title="dialogT2" v-model="viewVisible2" width="1200px">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline label-width="80" size="small" class="lineH0">
                        <!--<el-form-item label="" class="search-mb0">-->
                            <el-button size="small" type="success" icon="el-icon-upload2" @click="exportBymx">
                                导出
                            </el-button>
                        <!--</el-form-item>-->
                    </el-form>
                </div>
                <div class="clear"></div>
            </div>

            <el-table
                    :data="byData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
            >
                <!--<el-table-column-->
                <!--prop="isTheCompany"-->
                <!--label="类型"-->
                <!--width="100"-->
                <!--align="center"-->
                <!--&gt;<template #default="scope">-->
                <!--<p v-if="scope.row.isTheCompany == 1">个人</p>-->
                <!--<p v-if="scope.row.isTheCompany == 2">公司</p>-->
                <!--</template></el-table-column>-->
                <el-table-column
                        prop="carNo"
                        label="车牌"
                        width="100"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="create_time"
                        label="包月时间"
                        align="center"
                ></el-table-column>
                <!--<el-table-column-->
                        <!--prop="start_time"-->
                        <!--label="开始时间"-->
                        <!--align="center"-->
                <!--&gt;</el-table-column>-->
                <!--<el-table-column-->
                <!--prop="end_time"-->
                <!--label="结束时间"-->
                <!--align="center"-->
                <!--&gt;</el-table-column>-->
                <el-table-column
                        prop="months_unit_price"
                        label="单价"
                        align="center"
                >
                    <template #default="scope">
                        <p>{{ Number(scope.row.months_unit_price).toFixed(2) }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="months"
                        label="月数"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="amount"
                        label="总费用"
                        align="center"
                >
                    <template #default="scope">
                        <p>{{ Number(scope.row.amount).toFixed(2) }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="configName"
                        label="包月方案"
                        align="center"
                ></el-table-column>
            </el-table>

            <div class="pagination">
                <el-pagination
                        background
                        layout="total, prev, pager, next"
                        :current-page="byQuery.pageNum"
                        :page-size="byQuery.pageSize"
                        :total="pageTotal3"
                        @current-change="handlePageChange3"
                ></el-pagination>
            </div>

            <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewVisible2 = false">取 消</el-button>
        </span>
            </template>
        </el-dialog>

    </div>
</template>

<script>
    import {reactive, ref} from "vue";
    import {ElMessage, ElMessageBox, ElScrollbar} from "element-plus";
    //合并表格公共js
    import {useColSpan} from "../api/ColSpan";
    // 全局引入
    import * as echarts from "echarts";
    import {
        countByRoad,
        memberTotality,
        shouldBillRanking,
        shouldFactReceive,
        tenDayOrderTrend,
        todayActualCharge,
        todayAddCount,
        todayOrderForm,
        todayShouldCharge,
        getRoadOrderList,
        getMonthlyPaymentData,
        exportShouldBillRanking,
        exportBy,
    } from "../api/index";

    export default {
        name: "dashboard",
        setup() {
            const census = ref({
                userAddCount: 0,
                userCount: 0,
                tof: 0,
                tsc: 0,
                tac: 0,
                bl: "",
                cbr: 0,
                money: 0, // 总金额
                sumAmount: 0, // 应收金额
                paidAmount: 0, // 已付金额 临停收入
                unpaidAmount: 0, // 待付金额 临停欠费金额
                byAmount: 0, // 包月收入
                czAmount: 0, // 充值收入
            });
            //表格数据
            const todoList = ref([]);

            //合并表格方法
            const {objectSpanMethod, getSpanArr} = useColSpan();

            const queryTime = reactive({
                time: "",
            });
            const query = reactive({
                pageNum: 1,
                pageSize: 10,
                name: "",
                startTime: "",
                endTime: "",
                time: "",
                roadId: "",
                type: "0",//停车区类型 0：路段 1：停车场
                parking_type: "0",
            });
            const query2 = reactive({
                variance: 1,
                pageType: 1,
                roadId: "",
            });

            const rowclick = (row) => {
                var title = row.title;
                query.name = title;
                getShouldBillRanking();
            };
            const getStartTime = () => {
                var now = new Date(); //当前日期
                queryTime.time = [now, now];
                var date = now.getDate();
                var month = now.getMonth() + 1;
                if (month < 10) {
                    month = "0" + month;
                }
                if (date < 10) {
                    date = "0" + date;
                }
                query.startTime =
                    now.getFullYear().toString() +
                    "-" +
                    month.toString() +
                    "-" +
                    date.toString();
                query.endTime = query.startTime;
            };
            getStartTime();

            //日期控件change方法
            const getQueryDate = () => {
                if (null == queryTime.time || [] == queryTime.time || "" == queryTime.time) {
                    query.startTime = "";
                    query.endTime = "";
                } else {
                    // console.log(dataFormat(queryTime.time[0]));
                    query.startTime = dataFormat(queryTime.time[0]);
                    query.endTime = dataFormat(queryTime.time[1]);
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

            const tableData = ref([]);
            const pageTotal = ref(0);

            // 分页导航
            const handlePageChange = (val) => {
                query.pageNum = val;
                getShouldBillRanking();
            };
            // 查询操作
            const handleSearch = () => {
                query.pageNum = 1;
                if (query.name == "" || query.name == null || query.name == undefined) {
                    query.roadId = "";
                    query2.roadId = "";
                }
                getShouldBillRanking();
            };

            const exportSearch = () => {
                ElMessage.success("正在下载中·····");

                exportShouldBillRanking(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "路内概览.xls");
                    document.body.appendChild(link);
                    link.click();
                });
            };

            const exportBymx = () => {
                ElMessage.success("正在下载中·····");

                exportBy(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "包月收入明细.xls");
                    document.body.appendChild(link);
                    link.click();
                });
            };

            //点击 今日停车订单数排名 停车场名称，停车场概览所有统计根据停车场重新查询条件
            const parkLink = (row) => {
                if (row != undefined && row != null && row != "") {
                    query.roadId = row.id;
                    query2.roadId = row.id;
                    query.name = row.title;
                }

                //今日停车订单数排名
                handleSearch();
                tenDayShouldFactReceive(); //近十天应收实收款
                getTenDayOrderTrend(); //近十天订单趋势
            };

            // 清除
            const clearPark = () => {
                query.roadId = "";
                query2.roadId = "";
                parkLink();
            };

            // // 当日注册人数
            // const getTodayAddCount = () => {
            //     todayAddCount(query).then((res) => {
            //         console.log(res)
            //         census.userAddCount = res.data;
            //     });
            // };
            //
            // // 用户总数
            // const getMemberTotality = () => {
            //     memberTotality(query).then((res) => {
            //         census.userCount = res.data;
            //     });
            // };
            // // 今日订单数
            // const getTodayOrderForm = () => {
            //     todayOrderForm(query2).then((res) => {
            //         census.tof = res.data;
            //     });
            // };
            // // 今日应收款
            // const getTodayShouldCharge = () => {
            //     todayShouldCharge(query2).then((res) => {
            //         census.tsc = res.data;
            //     });
            // };
            // // 今日实收款
            // const getTodayActualCharge = () => {
            //     todayActualCharge(query2).then((res) => {
            //         census.tac = res.data;
            //         census.bl = res.logMsg;
            //     });
            // };
            // // 泊位总数
            // const getCountByRoad = () => {
            //     countByRoad(query).then((res) => {
            //         census.cbr = res.data;
            //     });
            // };

            // 今日应收排名
            const getShouldBillRanking = () => {
                shouldBillRanking(query).then((res) => {
                    const list = [];
                    res.data.data.list.forEach(item => {
                        if (item.paymentOrderList.length == 0) {
                            list.push(
                                {
                                    ...item,
                                    months_unit_price: "0.00",
                                    amount: "0.00",
                                    months: 0
                                }
                            )
                        } else {
                            item.paymentOrderList.forEach(it => {
                                const {months_unit_price, amount, months} = it
                                list.push(
                                    {
                                        ...item,
                                        months_unit_price,
                                        amount,
                                        months
                                    }
                                )
                            })
                        }
                    });
                    todoList.value = list;

                    //合并表格
                    getSpanArr(list);

                    // todoList.value = res.data.data.list;
                    pageTotal.value = res.data.data.total;
                    census.value = res.data.vo;
                });
            };
            // 近十天应收实收款
            const tenDayShouldFactReceive = () => {
                shouldFactReceive(query2).then((res) => {
                    // console.log(res.data.dates);
                    options3.xAxis[0].data = res.data.dates;
                    options3.series[0].data = res.data.ysList;
                    options3.series[1].data = res.data.ssList;
                    var chartDom = document.getElementById("main");
                    chartDom.removeAttribute("_echarts_instance_");
                    var myChart = echarts.init(chartDom);
                    myChart.setOption(options3);
                    // console.log(options3.xAxis);
                });
            };

            // 近十天订单趋势
            const getTenDayOrderTrend = () => {
                tenDayOrderTrend(query2).then((res) => {
                    // console.log(res.data.dates);
                    options4.xAxis[0].data = res.data.dates;
                    options4.series[0].data = res.data.zong;
                    options4.series[1].data = res.data.yingj;
                    options4.series[2].data = res.data.yij;
                    var chartDom = document.getElementById("mainline");
                    chartDom.removeAttribute("_echarts_instance_");
                    var myChart = echarts.init(chartDom);
                    myChart.setOption(options4);
                    // console.log(options4.xAxis);
                });
            };

            // getTodayAddCount();//当日注册人数
            // getMemberTotality();//用户总数
            // getTodayOrderForm();//今日订单数
            // getTodayShouldCharge();//今日应收款
            // getTodayActualCharge();//今日是首款
            // getCountByRoad();//泊位总数
            getShouldBillRanking(); //今日应收排名
            tenDayShouldFactReceive(); //近十天应收实收款
            getTenDayOrderTrend(); //近十天订单趋势

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
                getRoadOrderList(orderQuery).then((res) => {
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
                getRoadOrderList(orderQuery).then((res) => {
                    orderData.value = res.data.list;
                    pageTotal2.value = res.data.total;
                });
            };

            //定时任务 每隔60秒
            let timerSearch = setInterval(() => {
                // console.log("定时任务 每隔60秒 start");
                //今日停车订单数排名
                handleSearch();
                // console.log("定时任务 每隔60秒 end")
            }, 60000);

            //点击包月收入数 弹窗包月明细列表
            const viewVisible2 = ref(false);
            const dialogT2 = ref("包月收入明细");
            const byData = ref([]);
            const byQuery = reactive({
                pageNum: 1,
                pageSize: 10,
                startTime: "",
                endTime: "",
                roadId: "",
                pageType: "",
                parking_type: "0",
            });
            const pageTotal3 = ref(0);
            const handleView2 = () => {
                byQuery.pageNum = 1;
                byQuery.startTime = query.startTime;
                byQuery.endTime = query.endTime;
                byQuery.roadId = query.roadId;
                byQuery.pageType = query.type;
                // 订单表格数据
                getMonthlyPaymentData(byQuery).then((res) => {
                    byData.value = res.data.list;
                    pageTotal3.value = res.data.total;
                });
                viewVisible2.value = true;
            };
            const handlePageChange3 = (val) => {
                byQuery.pageNum = val;
                byQuery.startTime = query.startTime;
                byQuery.endTime = query.endTime;
                byQuery.roadId = query.roadId;
                byQuery.pageType = query.type;
                // 订单表格数据
                getMonthlyPaymentData(byQuery).then((res) => {
                    byData.value = res.data.list;
                    pageTotal3.value = res.data.total;
                });
            };

            const name = localStorage.getItem("ms_username");
            const role = name === "admin" ? "超级管理员" : "普通用户";

            const data = reactive([
                {
                    name: "2018/09/04",
                    value: 1083,
                },
                {
                    name: "2018/09/05",
                    value: 941,
                },
                {
                    name: "2018/09/06",
                    value: 1139,
                },
                {
                    name: "2018/09/07",
                    value: 816,
                },
                {
                    name: "2018/09/08",
                    value: 327,
                },
                {
                    name: "2018/09/09",
                    value: 228,
                },
                {
                    name: "2018/09/10",
                    value: 1065,
                },
            ]);
            const options = {
                type: "bar",
                title: {
                    text: "近十天应收实收款",
                    textStyle: {
                        fontSize: 16,
                        fontWeight: "normal",
                        color: "#303133",
                    },
                },
                xRorate: 25,
                labels: [
                    "12-19",
                    "12-20",
                    "12-21",
                    "12-22",
                    "12-23",
                    "12-24",
                    "12-25",
                    "12-26",
                    "12-27",
                    "12-28",
                ],
                datasets: [
                    {
                        label: "应收款",
                        data: [234, 278, 270, 190, 230, 240, 260, 270, 290, 245],
                    },
                    {
                        label: "实收款",
                        data: [164, 178, 190, 135, 160, 150, 180, 190, 200, 147],
                    },
                ],
            };
            const options2 = {
                type: "line",
                title: {
                    text: "最近十天订单趋势",
                    textStyle: {
                        fontSize: 16,
                        fontWeight: "normal",
                        color: "#303133",
                    },
                },
                labels: [
                    "12-19",
                    "12-20",
                    "12-21",
                    "12-22",
                    "12-23",
                    "12-24",
                    "12-25",
                    "12-26",
                    "12-27",
                    "12-28",
                ],
                datasets: [
                    {
                        label: "订单总数",
                        data: [234, 278, 270, 190, 230, 240, 260, 270, 290, 245],
                    },
                    {
                        label: "应缴费数",
                        data: [164, 178, 190, 135, 160, 150, 180, 190, 200, 147],
                    },
                    {
                        label: "已缴费数",
                        data: [74, 118, 150, 105, 140, 100, 130, 154, 182, 95],
                    },
                ],
            };

            const options3 = {
                title: {
                    text: "近十天应收实收款",
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
                    data: ["应收款", "实收款"],
                },
                toolbox: {
                    feature: {
                        saveAsImage: {},
                    },
                },
                grid: {
                    left: "10px",
                    right: "16px",
                    bottom: "3%",
                    containLabel: true,
                },
                xAxis: [
                    {
                        type: "category",
                        axisTick: {show: false},
                        data: [],
                    },
                ],
                yAxis: [
                    {
                        type: "value",
                    },
                ],
                series: [
                    {
                        name: "应收款",
                        type: "bar",
                        data: [],
                    },
                    {
                        name: "实收款",
                        type: "bar",
                        data: [],
                    },
                ],
            };

            const options4 = {
                title: {
                    text: "近十天订单趋势",
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
                    data: ["订单总数", "应缴费数", "已缴费数"],
                },
                toolbox: {
                    feature: {
                        saveAsImage: {},
                    },
                },
                grid: {
                    left: "10px",
                    right: "16px",
                    bottom: "3%",
                    containLabel: true,
                },
                xAxis: [
                    {
                        type: "category",
                        boundaryGap: false,
                        data: [],
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
                        //stack: "Total",
                        areaStyle: {},
                        emphasis: {
                            focus: "series",
                        },
                        data: [],
                    },
                    {
                        name: "应缴费数",
                        type: "line",
                        //stack: "Total",
                        areaStyle: {},
                        emphasis: {
                            focus: "series",
                        },
                        data: [],
                    },
                    {
                        name: "已缴费数",
                        type: "line",
                        // stack: "Total",
                        areaStyle: {},
                        emphasis: {
                            focus: "series",
                        },
                        data: [],
                    },
                ],
            };

            return {
                query,
                query2,
                queryTime,
                census,
                name,
                data,
                options,
                options2,
                options3,
                options4,
                todoList,
                role,
                tableData,
                pageTotal,
                rowclick,
                handlePageChange,
                getQueryDate,
                handleSearch,
                exportSearch,
                exportBymx,
                parkLink,
                clearPark,
                viewVisible,
                viewVisible2,
                dialogT,
                dialogT2,
                handleView,
                handleView2,
                orderData,
                byData,
                orderQuery,
                byQuery,
                pageTotal2,
                pageTotal3,
                handlePageChange2,
                handlePageChange3,
                timerSearch,
                objectSpanMethod,
            };
        },
        methods: {
            moneyLink() {
                //路由跳转，至欠费统计
                // this.$router.push('/arrearsstatic')
                this.$router.push({
                    path: "/arrearsstatic",
                    query: {
                        menuType: "0",
                        startTime: this.query.startTime,
                        endTime: this.query.endTime,
                    },
                });
            },
            // init() {
            //   var chartDom = document.getElementById("main");
            //   var myChart = echarts.init(chartDom);
            //   myChart.setOption(this.options3);
            //   var chartDoms = document.getElementById("mainline");
            //   var myCharts = echarts.init(chartDoms);
            //   myCharts.setOption(this.options4);
            // },
        },
        mounted() {
            // this.init();
        },
    };
</script>

<style scoped>
    .el-row {
        margin-bottom: 20px;
    }

    .grid-content {
        display: flex;
        align-items: center;
        height: 100px;
    }

    .grid-cont-right {
        flex: 1;
        text-align: center;
        font-size: 14px;
        color: #999;
    }

    .grid-num {
        font-size: 24px;
        font-weight: bold;
    }

    .grid-bfb {
        font-size: 14px;
        font-weight: normal;
    }

    .grid-con-icon {
        font-size: 50px;
        width: 100px;
        height: 100px;
        text-align: center;
        line-height: 100px;
        color: #fff;
    }

    .grid-con-1 .grid-con-icon {
        background: rgb(45, 140, 240);
    }

    .grid-con-1 .grid-num {
        color: rgb(45, 140, 240);
    }

    .grid-con-2 .grid-con-icon {
        background: rgb(100, 213, 114);
    }

    .grid-con-2 .grid-num {
        color: rgb(100, 213, 114);
    }

    .grid-con-3 .grid-con-icon {
        background: rgb(242, 94, 67);
    }

    .grid-con-3 .grid-num {
        color: rgb(242, 94, 67);
    }

    .grid-con-4 .grid-con-icon {
        background: #e6a23c;
    }

    .grid-con-4 .grid-num {
        color: #e6a23c;
    }

    .grid-con-5 .grid-con-icon {
        background: #67c23a;
    }

    .grid-con-5 .grid-num {
        color: #67c23a;
    }

    .grid-con-6 .grid-con-icon {
        background: #92a3c6;
    }

    .grid-con-6 .grid-num {
        color: #92a3c6;
    }

    .user-info {
        display: flex;
        align-items: center;
        padding-bottom: 20px;
        border-bottom: 2px solid #ccc;
        margin-bottom: 20px;
    }

    .user-avator {
        width: 120px;
        height: 120px;
        border-radius: 50%;
    }

    .user-info-cont {
        padding-left: 50px;
        flex: 1;
        font-size: 14px;
        color: #999;
    }

    .user-info-cont div:first-child {
        font-size: 30px;
        color: #222;
    }

    .user-info-list {
        font-size: 14px;
        color: #999;
        line-height: 25px;
    }

    .user-info-list span {
        margin-left: 70px;
    }

    .mgb20 {
        margin-bottom: 20px;
    }

    .todo-item {
        font-size: 14px;
    }

    .todo-item-del {
        font-weight: bold;
    }

    .circal {
        background: #409eff;
        color: #fff;
        border-radius: 10px;
        width: 20px;
        margin: auto;
    }

    .circal-top {
        background: #f56c6c;
    }

    .schart {
        width: 100%;
        height: 300px;
    }

    .list-box {
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
    }

    .list-item {
        flex: 1;
        margin-right: 20px;
    }

    .list-item:last-child {
        margin-right: 0;
    }
</style>
