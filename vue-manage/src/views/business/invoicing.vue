<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline label-width="80" size="small">
                        <el-form-item label="姓名">
                            <el-input v-model="query.business_name" placeholder="姓名" @keyup.enter="handleSearch"></el-input>
                        </el-form-item>

                        <el-form-item label="电话">
                            <el-input v-model="query.business_phone" placeholder="电话"
                                @keyup.enter="handleSearch"></el-input>
                        </el-form-item>

                        <el-form-item label="开票状态">
                            <el-select v-model="query.invoicing_state" filterable size="small" placeholder="所有" clearable>
                                <el-option v-for="item in form.czroptions" :key="item.value" :label="item.label"
                                    :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="">
                            <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询
                            </el-button>
                        </el-form-item>

                    </el-form>
                </div>

                <!-- <div class="clear"></div> -->
            </div>
            <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
                <el-table-column pro="ID" label="序号" width="55" align="center">
                    <template #default="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>

                <el-table-column prop="business_name" label="姓名" align="center" width="100"> </el-table-column>
                <el-table-column prop="business_phone" label="电话" align="center" width="120"> </el-table-column>
                <el-table-column prop="store_name" label="店铺名称" align="center" minWidth="180"> </el-table-column>
                <el-table-column prop="duty_paragraph" label="税号" align="center" width="180"></el-table-column>
                <el-table-column prop="invoicing_money" label="金额" align="center" width="160"></el-table-column>
                <el-table-column prop="invoicing_address" label="收票人地址" align="center" minWidth="180"></el-table-column>
                <el-table-column prop="invoicing_state" label="开票状态" align="center" width="160"></el-table-column>
                <el-table-column label="操作" width="160" align="center" fixed="right">
                    <template #default="scope">
                        <el-button size="mini" type="text" icon="el-icon-connection"
                            v-permission="['road_business_gldd', 'park_business_gldd']"
                            @click="handleRecord(scope.$index, scope.row)">关联订单
                        </el-button>
                        <el-button size="mini" v-if="scope.row.invoicing_state != '已开票'" type="text"
                            icon="el-icon-circle-check" v-permission="['road_business_tg', 'park_business_tg']"
                            @click="handleView(scope.$index, scope.row)">通过
                        </el-button>
                    </template>
                </el-table-column>


            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                    :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

        <!-- 关联订单 -->
        <el-dialog title="查看关联订单" v-model="recordVisible" width="30%">

            <el-table :data="tableData1" border class="table" ref="multipleTable" header-cell-class-name="table-header">
                <!-- <el-table-column pro="ID" label="序号" width="55" align="center">
                   <template #default="scope">
                        {{ (query.pageIndex - 1) * query.pageSize + scope.$index + 1 }}
                    </template>
                </el-table-column> -->
                <el-table-column align="center" label="序号" type="index" width="180"></el-table-column>
                <el-table-column prop="recharge_time" label="充值时间" align="center"></el-table-column>
                <el-table-column prop="recharge_money" label="充值金额" align="center"></el-table-column>

            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="recordVisible = false">关 闭</el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { storeInvoiceList, storeInvoiceStatus, storeInvoiceRecharge } from "../../api/store";

export default {
    name: "record",

    components: {
    },
    data() {
        return {

        }
    },
    setup() {
        const query = reactive({
            business_phone: "",
            business_name: "",
            checked: false,
            pageIndex: 1,
            pageSize: 10,
        });
        const tableData = ref([]);
        const tableData1 = ref([]);
        const queryInTable = ref([]);
        const pageTotal = ref(0);
        // 获取表格数据
        const getData = () => {
            storeInvoiceList(query).then((res) => {
                tableData.value = res.data.list;
                pageTotal.value = res.data.total || 0;
            });
        };
        getData();
        let form = reactive({
            name: "",
            address: "",
            czroptions: [
                {
                    value: "0",
                    label: "未开票",
                },
                {
                    value: "1",
                    label: "已开票",
                },

            ],
        });

        const recordVisible = ref(false)

        let idx = -1;
        const handleRecord = (index, row) => {
            idx = index;
            // Object.keys(form).forEach((item) => {
            //     form[item] = row[item];
            // });
            getRecordData(row.id);
            recordVisible.value = true;

        };

        const handleView = (index, row) => {
            ElMessageBox.confirm("确定审核通过吗？", "提示", {
                type: "warning",
            })
                .then(() => {
                    storeInvoiceStatus(row).then((res) => {
                        if (res.code === 0) {
                            ElMessage.success("通过审核");
                            tableData.value.splice(index, 1);
                            getData();
                        } else {
                            ElMessage.error(res.msg);
                        }
                    });

                })
                .catch(() => {
                    ElMessage.info("取消审核");
                });

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

        // 获取表格充值记录
        const getRecordData = (id) => {
            queryInTable.invoice_id = id;
            storeInvoiceRecharge(queryInTable).then((res) => {
                tableData1.value = res.data;
                // pageTotal.value = res.data.total || 0;
            });
        };


        return {
            queryInTable,
            query,
            tableData,
            tableData1,
            pageTotal,
            recordVisible,
            handleRecord,
            handleView,
            form,
            handleSearch,
            handlePageChange,

        };
    },
    methods: {




    },
};
</script>
<style scoped>
.el-image {
    margin: 4px 4px 0 4px;
}
</style>
