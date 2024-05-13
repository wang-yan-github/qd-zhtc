<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-button
                            type="danger"
                            size="small"
                            icon="el-icon-delete"
                            @click="handleDeleteAll()"
                    >全部核销
                    </el-button
                    >
                </div>
                <div class="right-panel">
                    <el-select
                            @change='handleChange'
                            v-model="query.inspect_id"
                            filterable
                            size="small"
                            placeholder="巡检员">
                        <el-option key="" label="选择巡检员" value=""></el-option>
                        <el-option
                                v-for="item in form.inspectManages"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"></el-option>
                    </el-select>
                    <el-button
                            size="small"
                            type="primary"
                            icon="el-icon-search"
                            @click="handleSearch"
                    >查询
                    </el-button
                    >
                </div>
                <div class="clear"></div>
            </div>
            <el-table
                    :data="tableData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column
                        type="selection"
                        width="55"
                        align="center"
                ></el-table-column>
                <el-table-column type="index" label="ID" width="55" align="center">
                </el-table-column>
                <el-table-column
                        prop="order_no"
                        label="订单号"
                        width="200"
                ></el-table-column>
                <el-table-column
                        prop="road_name"
                        label="路内"
                        width="130"
                ></el-table-column>
                <el-table-column prop="berth" label="泊位编号"></el-table-column>
                <el-table-column prop="car_no" label="车牌号码"></el-table-column>
                <el-table-column
                        prop="drivein_time"
                        label="停入时间"
                        width="200"
                ></el-table-column>
                <el-table-column
                        prop="driveout_time"
                        label="离场时间"
                        width="200"
                ></el-table-column>
                <el-table-column
                        prop="resitime"
                        width="100"
                        label="停留时间"
                ></el-table-column>
                <el-table-column prop="sum_amount" label="停车费用"></el-table-column>
                <el-table-column prop="state_name" label="状态"></el-table-column>
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
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    // import { fetchData } from "../../api/index";
    import Ueditor from "../../components/UE.vue";
    import {
        selectHxPageListData,
        hxAll,
        inspectManageSelectList,
    } from "../../api/roadParkingOrder.js";

    export default {
        name: "roadxjhxlist",
        components: {
            Ueditor,
        },
        data() {
            return {
                // 表单验证
                // formRules: {
                //   title: [{required: true, message: "必填项", trigger: "blur"}],
                // },
            };
        },
        setup() {
            const query = reactive({
                inspect_id: "",
                pageIndex: 1,
                pageSize: 10,
            });

            const tableData = ref([]);
            const pageTotal = ref(0);
            // 获取表格数据
            const getData = () => {
                selectHxPageListData(query).then((res) => {
                    // console.log(res);
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
                query,
                tableData,
                pageTotal,
                getData,
                handleSearch,
                handlePageChange,
                selectVal,
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
                } else {
                    ElMessageBox.confirm(msg, "提示", {
                        type: "warning",
                    }).then(() => {
                        hxAll({xjyId: that.query.inspect_id}).then((res) => {
                            ElMessage.success("核销成功");
                            // that.query.inspect_id = "";
                            that.getData();
                        })
                    }).catch(() => {
                    });
                }
            },
        },
    };
</script>
