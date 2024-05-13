<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline size="small">
                        <el-input size="small" class="handle-input mr10" placeholder="请输入支付流水号" clearable
                                  v-model="query.payment_serialno" @keyup.enter="handleSearch()"></el-input>
                        <el-input size="small" class="handle-input mr10" placeholder="请输入支付订单号" clearable
                                  v-model="query.payment_no" @keyup.enter="handleSearch()"></el-input>
                        <span class="mar5 color999"></span>
                        <el-select clearable filterable size="small" class="w100" placeholder="支付来源"
                                   v-model="query.paymentResource">
                            <el-option label="包月" value="1"></el-option>
                            <el-option label="会员充值" value="2"></el-option>
                            <el-option label="停车订单支付" value="3"></el-option>
                            <el-option label="商家充值" value="4"></el-option>
                        </el-select>
                        <span class="mar5 color999"></span>
                        <el-select clearable filterable size="small" class="w100" placeholder="支付方式"
                                   v-model="query.paymentType">
                            <el-option v-for="item in paymentTypeData" :key="item.dc_value" :label="item.label"
                                       :value="item.dc_value"></el-option>
                        </el-select>
                        <span class="mar5 color999"></span>
                        <el-select clearable filterable size="small" class="w100" placeholder="支付状态"
                                   v-model="query.status">
                            <el-option label="待支付" value="1"></el-option>
                            <el-option label="支付成功" value="2"></el-option>
                            <el-option label="支付失败" value="3"></el-option>
                        </el-select>
                        <span class="mar5 color999"></span>
                        <el-select clearable filterable size="small" class="w100" placeholder="是否退款"
                                   v-model="query.is_refund">
                            <el-option label="否" value="0"></el-option>
                            <el-option label="是" value="1"></el-option>
                        </el-select>
                        <span class="dispinline ml5 font14 color666">创建时间：</span>
                        <el-date-picker size="small" class="datepicker" type="daterange"
                                        range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                                        v-model="form.time" @change="getQueryDate">
                        </el-date-picker>
                        <span class="dispinline ml5"></span>
                        <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">
                            查询
                        </el-button>
                        <span class="dispinline ml5"></span>

                        <el-button size="small" type="success" icon="el-icon-upload2" @click="exportExcel"
                                   v-permission="['road_paymentOrderList_excel', 'park_paymentOrderList_excel']">导出
                        </el-button>
                    </el-form>
                </div>
                <div class="clear"></div>
            </div>
            <el-table
                    :data="tableData" ref="multipleTable" @selection-change="handleSelectionChange"
                    border class="table" :max-height="tableH" header-cell-class-name="table-header">
                <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
                <el-table-column label="流水号" align="center" prop="payment_serialno"></el-table-column>
                <el-table-column label="订单号" align="center" width="200" prop="payment_no"></el-table-column>
                <el-table-column label="支付来源" align="center" width="120">
                    <template #default="scope">
                        {{ scope.row.payment_resource == 1 ? "包月" : "" }}
                        {{ scope.row.payment_resource == 2 ? "会员充值" : "" }}
                        {{ scope.row.payment_resource == 3 ? "停车订单支付" : "" }}
                        {{ scope.row.payment_resource == 4 ? "商家充值" : "" }}
                    </template>
                </el-table-column>
                <el-table-column label="支付方式" align="center" width="120">
                    <template #default="scope">
                        {{ scope.row.payment_type == 1 ? "包月" : "" }}
                        {{ scope.row.payment_type == 2 ? "微信" : "" }}
                        {{ scope.row.payment_type == 3 ? "支付宝" : "" }}
                        {{ scope.row.payment_type == 4 ? "钱包" : "" }}
                        {{ scope.row.payment_type == 5 ? "现金" : "" }}
                        {{ scope.row.payment_type == 6 ? "银行卡" : "" }}
                        {{ scope.row.payment_type == 7 ? "商家支付" : "" }}
                        {{ scope.row.payment_type == 8 ? "聚合支付" : "" }}
                        {{ scope.row.payment_type == 9 ? "交通账户" : "" }}
                    </template>
                </el-table-column>
                <el-table-column label="支付状态" align="center" width="120">
                    <template #default="scope">
                        <el-tag type="warning" v-if="scope.row.status == 1">待支付</el-tag>
                        <el-tag type="success" v-if="scope.row.status == 2">支付成功</el-tag>
                        <el-tag type="danger" v-if="scope.row.status == 3">支付失败</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="联合支付" align="center" width="100">
                    <template #default="scope">
                        {{ scope.row.is_union_pay == 1 ? "是" : "否" }}
                    </template>
                </el-table-column>
                <el-table-column label="金额" align="center" width="100" prop="amount"></el-table-column>
                <el-table-column label="创建时间" align="center" width="180" prop="create_time"></el-table-column>
                <el-table-column label="支付完成时间" align="center" width="180" prop="pay_time"></el-table-column>
                <el-table-column label="是否退款" align="center" width="100">
                    <template #default="scope">
                        <div class="no-warp">
                            <el-tag size="small" class="mar5" @click="handleRefundList(scope.$index, scope.row)">
                                {{ scope.row.is_refund == 1 ? "是" : "否" }}
                            </el-tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="退款时间" align="center" width="180" prop="refund_time"></el-table-column>
                <el-table-column label="退款金额" align="center" width="100" prop="refund_amount"></el-table-column>
                <el-table-column label="操作" width="150" align="center">
                    <template #default="scope">
                        <!--支付流水明细详情-->
                        <el-button size="mini" type="text" icon="el-icon-view"
                                   @click="handleView(scope.$index, scope.row)">详情
                        </el-button>
                        <!--关联订单详情 支付来源(1包月、2会员充值、3停车订单支付、4商家充值)-->
                        <el-button size="mini" type="text" icon="el-icon-connection"
                                   @click="handleList(scope.$index, scope.row)">关联订单
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                               :page-size="query.pageSize" :total="pageTotal"
                               @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

        <!-- 支付流水详情 start -->
        <el-dialog title="支付流水详情" v-model="paymentOrderVisible" width="40%">
            <div class="mt20"></div>
            <table class="desctable mgb20 w">
                <tr>
                    <td class="tit" width="100">支付订单ID</td>
                    <td>{{ paymentOrderData.id }}</td>
                    <td class="tit" width="100">支付金额</td>
                    <td>{{ paymentOrderData.amount }}</td>
                </tr>
                <tr>
                    <td class="tit" width="100">支付订单号</td>
                    <td>{{ paymentOrderData.payment_no }}</td>
                    <td class="tit" width="100">支付流水号</td>
                    <td>{{ paymentOrderData.payment_serialno }}</td>
                </tr>
                <tr>
                    <td class="tit" width="100">支付方式</td>
                    <td>
                        {{ paymentOrderData.payment_type == 1 ? "包月" : "" }}
                        {{ paymentOrderData.payment_type == 2 ? "微信" : "" }}
                        {{ paymentOrderData.payment_type == 3 ? "支付宝" : "" }}
                        {{ paymentOrderData.payment_type == 4 ? "钱包" : "" }}
                        {{ paymentOrderData.payment_type == 5 ? "现金" : "" }}
                        {{ paymentOrderData.payment_type == 6 ? "银行卡" : "" }}
                        {{ paymentOrderData.payment_type == 7 ? "商家支付" : "" }}
                        {{ paymentOrderData.payment_type == 8 ? "聚合支付" : "" }}
                        {{ paymentOrderData.payment_type == 9 ? "交通账户" : "" }}
                    </td>
                    <td class="tit" width="100">支付来源</td>
                    <td>
                        {{ paymentOrderData.payment_resource == 1 ? "包月" : "" }}
                        {{ paymentOrderData.payment_resource == 2 ? "会员充值" : "" }}
                        {{ paymentOrderData.payment_resource == 3 ? "停车订单支付" : "" }}
                        {{ paymentOrderData.payment_resource == 4 ? "商家充值" : "" }}
                    </td>
                </tr>
                <tr>
                    <td class="tit" width="100">转账流水号</td>
                    <td>{{ paymentOrderData.transferSerialNumber }}</td>
                    <td class="tit" width="100">支付状态</td>
                    <td>
                        <el-tag type="warning" v-if="paymentOrderData.status == 1">待支付</el-tag>
                        <el-tag type="success" v-if="paymentOrderData.status == 2">支付成功</el-tag>
                        <el-tag type="danger" v-if="paymentOrderData.status == 3">支付失败</el-tag>
                    </td>
                </tr>
                <tr>
                    <td class="tit" width="100">创建时间</td>
                    <td>{{ paymentOrderData.create_time }}</td>
                    <td class="tit" width="100">支付完成时间</td>
                    <td>{{ paymentOrderData.pay_time }}</td>
                </tr>
                <tr>
                    <td class="tit" width="100">联合支付</td>
                    <td>{{ paymentOrderData.is_union_pay == 1 ? "是" : "否" }}</td>
                    <td class="tit" width="100">账单时间</td>
                    <td>{{ paymentOrderData.billDate }}</td>
                </tr>
                <tr>
                    <td class="tit" width="100">包月月数</td>
                    <td>{{ paymentOrderData.months }}</td>
                    <td class="tit" width="100">包月单价</td>
                    <td>{{ paymentOrderData.months_unit_price }}</td>
                </tr>
                <tr>
                    <td class="tit" width="100">备注</td>
                    <td colspan="3">{{ paymentOrderData.remarks }}</td>
                </tr>
            </table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="paymentOrderVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>
        <!-- 支付流水详情 end -->

        <!-- 关联包月订单详情 start -->
        <el-dialog title="关联包月记录" v-model="monthlyVisible" width="60%">
            <el-table :data="monthlyData" border class="table" header-cell-class-name="table-header">
                <el-table-column prop="carNo" width="90" label="车牌号码" align="center"></el-table-column>
                <el-table-column prop="name" width="90" label="姓名" align="center"></el-table-column>
                <el-table-column prop="phone" width="110" label="手机号码" align="center"></el-table-column>
                <el-table-column prop="dwName" width="100" label="单位名称" align="center"></el-table-column>
                <el-table-column prop="start_time" label="开始时间" width="160" align="center"></el-table-column>
                <el-table-column prop="end_time" label="结束时间" width="160" align="center"></el-table-column>
                <el-table-column prop="months" label="月数" width="90" align="center"></el-table-column>
                <el-table-column prop="price" label="单价" width="90" align="center"></el-table-column>
                <el-table-column prop="total_cost" label="总费用" width="100" align="center"></el-table-column>
                <el-table-column prop="configName" label="包月方案" align="center"></el-table-column>
                <el-table-column prop="create_time" label="创建时间" width="160" align="center"></el-table-column>
                <el-table-column prop="update_time" label="更新时间" width="160" align="center"></el-table-column>
            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="monthlyVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>
        <!-- 关联包月订单详情 end -->

        <!-- 关联车主充值记录 start -->
        <el-dialog title="关联车主充值记录" v-model="rechargeVisible" width="60%">
            <el-table :data="rechargeData" border class="table" header-cell-class-name="table-header">
                <el-table-column prop="nickName" label="微信昵称" align="center"></el-table-column>
                <el-table-column prop="phone" label="手机号" width="110" align="center"></el-table-column>
                <el-table-column prop="balance" label="账户余额" width="110" align="center"></el-table-column>
                <el-table-column prop="recharge_amount" width="90" label="充值金额" align="center"></el-table-column>
                <el-table-column prop="gift_amount" width="110" label="赠送金额" align="center"></el-table-column>
                <el-table-column prop="recharge_time" width="160" label="充值时间" align="center"></el-table-column>
            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="rechargeVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>
        <!-- 关联车主充值记录 end -->

        <!-- 关联停车订单 start -->
        <el-dialog title="关联停车订单" v-model="parkingOrderVisible" width="60%">
            <el-table :data="parkingOrderData" border class="table" header-cell-class-name="table-header">
                <el-table-column prop="orderNo" label="订单号" width="200" align="center"></el-table-column>
                <el-table-column prop="placeName" label="停车场/路内" align="center"></el-table-column>
                <el-table-column prop="paid_amount" width="100" label="支付金额" align="center"></el-table-column>
                <el-table-column prop="sum_amount" width="100" label="订单金额" align="center"></el-table-column>
                <el-table-column prop="driveinTime" label="驶入时间" width="160" align="center"></el-table-column>
                <el-table-column prop="driveoutTime" label="驶离时间" width="160" align="center"></el-table-column>
                <el-table-column prop="pay_time" label="支付时间" width="160" align="center"></el-table-column>
                <el-table-column prop="status_name" label="状态" width="100" align="center"></el-table-column>
                <el-table-column prop="resitime" label="停车时长" width="120" align="center"></el-table-column>
            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="parkingOrderVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>
        <!-- 关联停车订单 end -->

        <!-- 关联商户充值记录 start -->
        <el-dialog title="关联商户充值记录" v-model="storeRechargeVisible" width="60%">
            <el-table :data="storeRechargeData" border class="table" header-cell-class-name="table-header">
                <el-table-column prop="payment_no" label="支付订单号" width="200" align="center"></el-table-column>
                <el-table-column prop="payment_serialno" label="支付流水号" align="center"></el-table-column>
                <el-table-column prop="recharge_money" label="充值金额" width="100" align="center"></el-table-column>
                <el-table-column prop="credit" label="赠送金额" width="100" align="center"></el-table-column>
                <el-table-column prop="recharge_time" label="充值时间" width="160" align="center"></el-table-column>
                <el-table-column prop="create_time" label="创建时间" width="160" align="center"></el-table-column>
                <el-table-column prop="status" label="支付状态" width="100" align="center">
                    <template #default="scope">
                        <el-tag type="warning" v-if="scope.row.status == 1">待支付</el-tag>
                        <el-tag type="success" v-if="scope.row.status == 2">支付成功</el-tag>
                        <el-tag type="danger" v-if="scope.row.status == 3">支付失败</el-tag>
                    </template>
                </el-table-column>
            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="storeRechargeVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>
        <!-- 关联商户充值记录 end -->

        <!-- 关联退款记录 start -->
        <el-dialog title="关联退款记录" v-model="refundVisible" width="60%">
            <el-table :data="refundData" border class="table" header-cell-class-name="table-header">
                <el-table-column prop="orderNo" label="停车订单号" width="200" align="center"></el-table-column>
                <el-table-column prop="payment_no" label="订单号" width="200" align="center"></el-table-column>
                <el-table-column prop="payment_serialno" label="流水号" align="center"></el-table-column>
                <el-table-column prop="refund_amount" label="退款金额" width="100" align="center"></el-table-column>
                <el-table-column prop="refund_time" label="退款时间" width="160" align="center"></el-table-column>
                <el-table-column prop="create_time" label="创建时间" width="160" align="center"></el-table-column>
                <el-table-column prop="update_time" label="更新时间" width="160" align="center"></el-table-column>
            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="refundVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>
        <!-- 关联退款记录 end -->

    </div>
</template>

<script>
    import {reactive, ref} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import {
        paymentOrderPageList,
        paymentOrderView,
        getMonthlyDataByPaymentId,
        getRechargeDataByPaymentId,
        getOrderDataByPaymentId,
        getStoreRechargeDataByPaymentId,
        getRefundDataByPaymentId,
        exportPaymentOrder,
    } from "../../api/paymentOrder";
    import {
        dictData,
    } from "../../api/index";
    import axios from "axios";
    import File_URL from "../../file_url";

    export default {
        name: "paymentOrderList",
        components: {},
        data() {
            return {
                tableH: 0,
            };
        },
        setup() {
            const query = reactive({
                payment_serialno: "",
                payment_no: "",
                paymentType: "",
                paymentResource: "",
                status: "",
                startTime: "",
                endTime: "",
                is_refund: "",
                pageIndex: 1,
                pageSize: 20,
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            let form = reactive({
                time: "",
            });
            // 获取表格数据
            const getData = () => {
                paymentOrderPageList(query).then((res) => {
                    // console.log(res)
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
            };
            //日期格式化
            const dateFormat = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
            };

            //获取支付方式字典
            const paymentTypeData = ref({});
            dictData(reactive({dict_type: "pay_type"})).then((res) => {
                paymentTypeData.value = res.data;
            });

            //支付流水明细详情 显示/隐藏
            const paymentOrderVisible = ref(false);
            //支付流水明细详情 数据
            const paymentOrderData = ref({});
            //支付流水明细详情
            const handleView = (index, row) => {
                paymentOrderView(reactive({paymentId: row.id})).then((res) => {
                    paymentOrderData.value = res.data;
                });
                paymentOrderVisible.value = true;
            };

            //包月订单详情 显示/隐藏
            const monthlyVisible = ref(false);
            //包月订单 数据
            const monthlyData = ref([]);
            //充值记录详情 显示/隐藏
            const rechargeVisible = ref(false);
            //充值记录详情 数据
            const rechargeData = ref([]);
            //停车订单详情 显示/隐藏
            const parkingOrderVisible = ref(false);
            //停车订单详情 数据
            const parkingOrderData = ref([]);
            //商户充值记录 显示/隐藏
            const storeRechargeVisible = ref(false);
            //商户充值记录 数据
            const storeRechargeData = ref([]);
            //退款记录 显示/隐藏
            const refundVisible = ref(false);
            //退款记录 数据
            const refundData = ref([]);
            /**
             * 关联订单
             * payment_resource 支付来源(1包月、2会员充值、3停车订单支付、4商家充值)
             */
            const handleList = (index, row) => {
                if (row.payment_resource == "1") {
                    //1包月订单
                    getMonthlyDataByPaymentId(reactive({id: row.id})).then((res) => {
                        monthlyData.value = res.data;
                    });
                    monthlyVisible.value = true;
                } else if (row.payment_resource == "2") {
                    //2会员充值
                    getRechargeDataByPaymentId(reactive({payment_id: row.id})).then((res) => {
                        rechargeData.value = res.data;
                    });
                    rechargeVisible.value = true;
                } else if (row.payment_resource == "3") {
                    //3停车订单支付
                    getOrderDataByPaymentId(reactive({payment_id: row.id})).then((res) => {
                        parkingOrderData.value = res.data;
                    });
                    parkingOrderVisible.value = true;
                } else if (row.payment_resource == "4") {
                    //4商家充值
                    getStoreRechargeDataByPaymentId(reactive({payment_no: row.payment_no})).then((res) => {
                        storeRechargeData.value = res.data;
                    });
                    storeRechargeVisible.value = true;
                }
            };
            const handleRefundList = (index, row) => {
                //退款记录
                getRefundDataByPaymentId(reactive({payment_id: row.id})).then((res) => {
                    refundData.value = res.data;
                });
                refundVisible.value = true;
            };

            //导出excel
            const exportExcel = () => {
                exportPaymentOrder(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', '支付流水明细.xls');
                    document.body.appendChild(link);
                    link.click()
                })
            };

            return {
                handleSearch,
                handlePageChange,
                getQueryDate,
                query,
                form,
                tableData,
                pageTotal,
                paymentTypeData,
                paymentOrderVisible,
                paymentOrderData,
                handleView,
                monthlyVisible,
                monthlyData,
                rechargeVisible,
                rechargeData,
                parkingOrderVisible,
                parkingOrderData,
                storeRechargeVisible,
                storeRechargeData,
                refundVisible,
                refundData,
                handleList,
                exportExcel,
                handleRefundList,
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
