<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline label-width="80" size="small">
                        <el-form-item label="流水号">
                            <el-input v-model="query.payment_serialno" placeholder="流水号" @keyup.enter="handleSearch"></el-input>
                        </el-form-item>
                        <el-form-item label="订单编号">
                            <el-input v-model="query.payment_no" placeholder="订单编号" @keyup.enter="handleSearch"></el-input>
                        </el-form-item>
                        <el-form-item label="姓名">
                            <el-input v-model="query.business_name" placeholder="姓名" @keyup.enter="handleSearch"></el-input>
                        </el-form-item>

                        <el-form-item label="电话">
                            <el-input v-model="query.business_phone" placeholder="电话" @keyup.enter="handleSearch"></el-input>
                        </el-form-item>

                        <!-- <el-form-item label="充值类型">
                            <el-select v-model="form.selvalue" filterable size="small" placeholder="充值类型">
                                <el-option v-for="item in form.czroptions" :key="item.value" :label="item.label"
                                    :value="item.value"></el-option>
                            </el-select>
                        </el-form-item> -->
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
                        {{ (query.pageIndex - 1) * query.pageSize + scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column prop="payment_serialno" label="流水号" align="center" width="180"></el-table-column>
                <el-table-column prop="payment_no" label="订单编号" align="center" width="200"></el-table-column>
                <el-table-column prop="business_name" label="姓名" align="center" width="100"> </el-table-column>
                <el-table-column prop="business_phone" label="电话" align="center" width="120"> </el-table-column>
                <el-table-column prop="business_shop_name" label="店铺名称" align="center" minWidth="180"> </el-table-column>
                <!-- <el-table-column label="充值类型" align="center" width="120">
                    <template #default="scope">
                        <el-tag type="success" size="small">标签二</el-tag>
                    </template>
                </el-table-column> -->
                <el-table-column prop="recharge_money" label="充值金额" align="center" width="180"> </el-table-column>
                <el-table-column prop="recharge_time" label="充值时间" align="center" width="180"> </el-table-column>

            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                    :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

    </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { storeRechargeList } from "../../api/store";

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
            address: "",
            name: "",
            payment_no: "",
            payment_serialno: "",
            checked: false,
            pageIndex: 1,
            pageSize: 10,
        });
        const tableData = ref([]);
        const pageTotal = ref(0);
        // 获取表格数据
        const getData = () => {
            storeRechargeList(query).then((res) => {
                tableData.value = res.data.list;
                pageTotal.value = res.data.total || 0;
            });
        };
        getData();
        let form = reactive({
            name: "",
            address: "",
            czroptions: [{
                value: "1",
                label: "平台管理员",
            },
            {
                value: "2",
                label: "管理员",
            },
            {
                value: "3",
                label: "车场",
            },
            ],
        });

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
            pageTotal,
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
