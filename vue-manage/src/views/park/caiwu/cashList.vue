<template>
    <div>
        <div class="container">
            <el-row>
                <el-col :xs="24" :sm="24" :md="24" :lg="18" :xl="18">
                    <div class="top-panel">
                        <el-form label-width="80px" inline size="small">
                            <el-form-item label="巡检员">
                                <el-select
                                        @change='handleChange'
                                        v-model="query.inspect_id"
                                        filterable
                                        size="small"
                                        placeholder="巡检员">
                                    <el-option key="" label="请选择" value=""></el-option>
                                    <el-option
                                            v-for="item in form.inspectManages"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="核销状态">
                                <el-select
                                        @change='handleChange'
                                        v-model="query.is_verification"
                                        filterable
                                        size="small"
                                        placeholder="核销状态">
                                    <el-option key="0" label="未核销 " value="0"></el-option>
                                    <el-option key="1" label="已核销" value="1"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="支付时间">
                                <el-date-picker size="small" v-model="queryTime.time" type="daterange"
                                                range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                                                class="datepicker" @change="getQueryDate">
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item>
                                <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询
                                </el-button>
                                <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
                <!-- 点击查询条件后 显示核销金额及按钮 -->
                <el-col :xs="24" :sm="24" :md="24" :lg="6" :xl="6" v-if="isControl">
                    <div class="tip">
                        <p><span>待核销金额：</span><b>{{money}}</b> <span>(元)</span></p>
                        <el-button size="small" type="success" icon="el-icon-bank-card" @click="handleDeleteAll()">核销
                        </el-button>
                    </div>
                </el-col>
            </el-row>

            <el-table :data="tableData" border class="table" ref="multipleTable" :max-height="tableH" header-cell-class-name="table-header"
            >
                <el-table-column label="序号" width="55" align="center">
                    <template #default="scope">
                        {{scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="order_no"
                        label="订单号"
                        width="200"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="park_name"
                        label="停车场"
                ></el-table-column>
                <el-table-column prop="berth" label="泊位编号" width="200" align="center"></el-table-column>
                <el-table-column prop="car_no" label="车牌号码" width="100" align="center"></el-table-column>
                <el-table-column
                        prop="drivein_time"
                        label="停入时间"
                        width="180"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="driveout_time"
                        label="离场时间"
                        width="180"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="resTime"
                        width="100"
                        label="停留时间"
                        align="center"
                ></el-table-column>
                <el-table-column prop="sum_amount" label="停车费用" width="100" align="center"></el-table-column>
                <el-table-column prop="state_name" label="状态" width="120" align="center"></el-table-column>

            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                               :page-size="query.pageSize" :total="pageTotal"
                               @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

    </div>
</template>

<script>
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
// import { fetchData } from "../../api/index";
import {hxAll, inspectManageSelectList, selectHxPageListData,} from "../../../api/parkingOrder.js";

export default {
        name: "cashList",
        data() {
            return {
                tableH:0,
            };
        },
        setup() {
            const queryTime = reactive({
                time: "",
            });
            const query = reactive({
                inspect_id: "",
                is_verification: "0",
                startDate: "",
                endDate: "",
                pageIndex: 1,
                pageSize: 15,
            });

            const tableData = ref([]);
            const pageTotal = ref(0);
            const money = ref("0");
            const isControl = ref(true);
            var user_type = localStorage.getItem("user_type");
            if(user_type == 1){
              isControl.value=false;
            }
            // 获取表格数据
            const getData = () => {
                selectHxPageListData(query).then((res) => {
                    console.log(res);
                    if (query.inspect_id == "" || query.inspect_id == null) {
                        ElMessage.warning(`请选择巡检员`);
                    } else {
                        tableData.value = res.data.pageInfo.list;
                        pageTotal.value = res.data.pageInfo.total;
                        //未核销状态才可显示金额，否则只显示0
                        if (query.is_verification == "0") {
                            money.value = res.data.money;
                        } else {
                            money.value = "0";
                        }
                    }
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

            //初始化下拉框数据
            let form = reactive({
                inspectManages: [],
            });

            //获取巡检员下拉框列表
            const getInspectManageSelectList = () => {
                inspectManageSelectList().then((res) => {
                    form.inspectManages = res.data;
                })
            };

            getInspectManageSelectList();

            const selectVal = ref("");

            return {
                form,
                isControl,
                queryTime,
                query,
                tableData,
                pageTotal,
                getData,
                handleSearch,
                handlePageChange,
                getQueryDate,
                selectVal,
                money,
            };
        },
        methods: {
            handleSelectionChange(data) {
                this.selectedData = data;
            },
            handleChange(data) {
                let obj = {};
                obj = this.form.inspectManages.find((item) => {
                    return item.id === data;
                });
                if (obj != null && obj != "" && obj != undefined) {
                    //获取下拉框选中label值
                    this.selectVal = obj.name;
                }
            },
            handleDeleteAll() {
                const msg = "确定要核销" + this.selectVal + "的所有现金订单吗？";
                var that = this;
                if (that.query.inspect_id == "" || that.query.inspect_id == null) {
                    ElMessage.warning(`请选择巡检员`);
                } else if (that.query.is_verification == "" || that.query.is_verification == null) {
                    ElMessage.warning(`请选择核销状态`);
                    // } else if (that.query.startDate == "" || that.query.startDate == null) {
                    // 	ElMessage.warning(`请选择开始时间`);
                    // } else if (that.query.endDate == "" || that.query.endDate == null) {
                    // 	ElMessage.warning(`请选择结束时间`);
                } else {
                    if (that.query.is_verification == "1") {
                        ElMessage.warning(`选择未核销状态才可核销`);
                    } else {
                        ElMessageBox.confirm(msg, "提示", {
                            type: "warning",
                        }).then(() => {
                            hxAll({
                                xjyId: that.query.inspect_id,
                                startDate: that.query.startDate,
                                endDate: that.query.endDate,
                            }).then((res) => {
                                ElMessage.success("核销成功");
                                // that.query.inspect_id = "";
                                that.getData();
                            })
                        }).catch(() => {
                        });
                    }
                }
            },
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + 'px';
        },
    };
</script>
<style scoped>
    .tip {
        padding: 0.625rem;
        background: rgb(217, 236, 255) none repeat scroll 0% 0%;
        border: 1px solid #409eff;
        margin-top: -18px;
        border-radius: 4px;
        text-align: center;
    }

    .tip p {
        display: inline-block;
        margin-right: 30px;
        font-size: 16px;
        color: #0a7bf0;
    }

    .tip p span {
        vertical-align: middle;
    }

    .tip p b {
        font-size: 24px;
        vertical-align: middle;
    }

</style>
