<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline size="small">
                        <el-input
                                size="small"
                                @keyup.enter="handleSearch()"
                                v-model="query.paymentNoOrSerialNo"
                                placeholder="请输入订单号/流水号"
                                class="handle-input mr10"
                        ></el-input>
                        <el-select
                                clearable
                                v-model="query.areaId"
                                filterable
                                size="small"
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
                                size="small"
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
                                v-model="query.parkId"
                                filterable
                                size="small"
                                placeholder="所有停车场"
                                class="w100"
                        >
                            <el-option
                                    v-for="item in form.parks"
                                    :key="item.id"
                                    :label="item.park_name"
                                    :value="item.id"
                            ></el-option>
                        </el-select>
                        <span class="mar5 color999"></span>
                        <el-select
                                clearable
                                filterable
                                size="small"
                                v-model="query.paymentType"
                                placeholder="支付方式"
                                class="w100"
                        >
                            <!-- <el-option key="monthlyPayment" label="包月" value="1"></el-option> -->
                            <el-option key="weChat" label="微信" value="2"></el-option>
                            <el-option key="alipay" label="支付宝" value="3"></el-option>
                            <el-option key="wallet" label="钱包" value="4"></el-option>
                            <el-option key="cash" label="现金" value="5"></el-option>
                            <el-option key="jhzf" label="聚合支付" value="8"></el-option>
                        </el-select>
                        <span class="dispinline ml5 font14 color666">时间：</span>
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
                        <span class="dispinline ml5"></span>
                        <el-button
                                size="small"
                                type="primary"
                                icon="el-icon-search"
                                @click="handleSearch"
                        >查询
                        </el-button>
                        <span class="dispinline ml5"></span>
                        <el-button
                                size="small"
                                type="success"
                                icon="el-icon-upload2"
                                @click="exportExcel"
                                v-permission="['road_yingshoumanaA_excel', 'park_yingshoumanaA_excel']"
                        >导出
                        </el-button>
                    </el-form>
                </div>
                <div class="clear"></div>
            </div>
            <el-table
                    :data="tableData"
                    border
                    class="table"
                    ref="multipleTable"
                    :max-height="tableH"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column
                        type="index"
                        label="序号"
                        width="55"
                        align="center"
                ></el-table-column>
                <el-table-column label="流水号" align="center">
                    <template #default="scope">{{ scope.row.payment_serialno }}</template>
                </el-table-column>
                <el-table-column label="订单号" align="center">
                    <template #default="scope">{{ scope.row.payment_no }}</template>
                </el-table-column>
                <el-table-column label="停车场" align="center" prop="name">
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
                    </template>
                </el-table-column>
                <el-table-column label="金额" align="center" width="120">
                    <template #default="scope">{{ scope.row.sum_amount }}</template>
                </el-table-column>
                <el-table-column label="营收时间" align="center" width="180">
                    <template #default="scope">{{ scope.row.pay_time }}</template>
                </el-table-column>
                <el-table-column label="操作" width="220" align="center">
                    <template #default="scope">
                        <el-button
                                v-if="scope.row.is_union_pay != 1"
                                size="mini"
                                type="text"
                                icon="el-icon-view"
                                @click="handleView(scope.$index, scope.row)"
                                v-permission="['road_yingshoumanaA_details', 'park_yingshoumanaA_details']"
                        >
                            支付详情{{ scope.row.amount }}元
                        </el-button>
                        <el-button
                                v-if="scope.row.is_union_pay == 1"
                                size="mini"
                                type="text"
                                icon="el-icon-view"
                                @click="handleView(scope.$index, scope.row)"
                                v-permission="['park_yingshoumanaA_lhzf', 'road_yingshoumanaA_lhzf']"
                        >
                            联合支付{{ scope.row.amount }}元
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

        <!-- 详情弹出框 -->
        <el-dialog :title="dialogT" v-model="viewVisible" width="1000px">
            <el-table
                    :data="tablePaymentData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column
                        prop="order_no"
                        width="200"
                        label="订单号"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="paid_amount"
                        width="100"
                        label="支付金额"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="sum_amount"
                        width="100"
                        label="订单金额"
                        align="center"
                ></el-table-column>
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
                        prop="resitime"
                        label="停车时长"
                        align="center"
                ></el-table-column>
            </el-table>
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
    import {ElMessage, ElMessageBox} from "element-plus";
    import {
        jointPayment,
        queryAreaData,
        queryPaymentOrder,
        queryParkData,
        queryStreetData,
    } from "../../../api/index";
    import axios from "axios";
    import File_URL from "../../../file_url";

    export default {
        name: "userList",
        components: {},
        data() {
            return {
                tableH: 0,
            };
        },
        setup() {
            const query = reactive({
                paymentNoOrSerialNo: "",
                areaId: "",
                streetId: "",
                parkId: "",
                paymentType: "",
                startTime: "",
                endTime: "",
                pageIndex: 1,
                pageSize: 15,
            });
            const tableData = ref([]);
            const parkingType = ref(1);
            const tablePaymentData = ref([]);
            const pageTotal = ref(0);
            // 获取表格数据
            const getData = () => {
                queryPaymentOrder(query).then((res) => {
                    tableData.value = res.data.page.list;
                    pageTotal.value = res.data.page.total;
                    parkingType.value = res.data.parkingType;
                });
            };
            getData();

            // 获取联合支付表格数据
            const getPaymentData = () => {
                jointPayment(form).then((res) => {
                    tablePaymentData.value = res.data.list;
                });
            };

            //获取区域下拉框数据
            const queryArea = reactive({});
            const getArea = () => {
                queryAreaData(queryArea).then((res) => {
                    form.areas = res.data;
                });
            };
            getArea();
            //获取街道下拉框数据
            let queryStreet = reactive({
                areaId: query.areaId,
            });
            const getStreet = () => {
                if (query.areaId == "") {
                    form.streets = [];
                    form.parks = [];
                    query.streetId = "";
                    query.parkId = "";
                } else {
                    query.streetId = "";
                    query.parkId = "";
                    form.parks = [];
                    queryStreet.areaId = query.areaId;
                    queryStreetData(queryStreet).then((res) => {
                        form.streets = res.data;
                    });
                }
            };
            //获取停车场下拉框数据
            const getPark = () => {
                if (query.streetId == "") {
                    form.parks = [];
                    query.parkId = "";
                } else {
                    form.parks = [];
                    query.parkId = "";
                    queryParkData(reactive({streetId: query.streetId})).then((res) => {
                        form.parks = res.data;
                    });
                }
            };

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

            //导出excel
            const exportExcel = () => {
                axios({
                    url: File_URL.file_down + "excel/exportPaymentOrder?parkingType=" + parkingType.value
                        + "&paymentNoOrSerialNo=" + query.paymentNoOrSerialNo + "&areaId=" + query.areaId
                        + "&streetId=" + query.streetId + "&roadId=" + query.parkId + "&paymentType=" + query.paymentType
                        + "&startTime=" + query.startTime + "&endTime=" + query.endTime,
                    method: "GET",
                    headers: {
                        "Content-Type": "multipart/form-data",
                        Authorization: localStorage.getItem("token_value"),
                    },
                    responseType: "blob", // important
                }).then((response) => {
                    const url = window.URL.createObjectURL(new Blob([response.data]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "营收管理.xls");
                    document.body.appendChild(link);
                    link.click();
                });
                // paymentOrderExcelWriter(query).then((res) => {
                //   const url = window.URL.createObjectURL(new Blob([res.data]));
                //   const link = document.createElement('a');
                //   link.href = url;
                //   link.setAttribute('download', 'file.xls');
                //   document.body.appendChild(link);
                //   link.click();
                // })
            };

            // 删除操作
            const handleDelete = (index) => {
                // 二次确认删除
                ElMessageBox.confirm("确定要删除吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        ElMessage.success("删除成功");
                        tableData.value.splice(index, 1);
                    })
                    .catch(() => {
                    });
            };
            const dialogT = ref("联合支付的订单");

            // 表格编辑时弹窗和保存
            const editVisible = ref(false);
            const viewVisible = ref(false);
            let form = reactive({
                areas: [],
                streets: [],
                parks: [],
                name: "",
                time: "",
                paymentId: "",
            });
            let idx = -1;
            const handleView = (index, row) => {
                if (row.is_union_pay != 1) {
                    dialogT.value = "支付详情的订单";
                }
                idx = index;
                Object.keys(form).forEach((item) => {
                    form[item] = row[item];
                });
                form.paymentId = row.id;
                getPaymentData();
                viewVisible.value = true;
            };

            const saveEdit = () => {
                editVisible.value = false;
                ElMessage.success(`修改第 ${idx + 1} 行成功`);
                Object.keys(form).forEach((item) => {
                    tableData.value[idx][item] = form[item];
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
            };
            //日期格式化
            const dateFormat = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
            };

            return {
                getQueryDate,
                getArea,
                getStreet,
                getPark,
                exportExcel,
                query,
                tableData,
                parkingType,
                tablePaymentData,
                pageTotal,
                editVisible,
                viewVisible,
                form,
                dialogT,
                handleSearch,
                handlePageChange,
                handleDelete,
                handleView,
                saveEdit,
                multipleSelection: [],
                dialogImageUrl: "",
                ppVisible: false,
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
