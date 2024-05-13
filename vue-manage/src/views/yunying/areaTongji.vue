<template>
    <div>
        <div class="container">
            <el-row>
                <el-col :span="24">
                    <div class="top-panel">
                        <el-form inline size="small">
                            <el-form-item label="驶离时间">
                                <el-date-picker
                                        size="small"
                                        v-model="form.time"
                                        type="daterange"
                                        clearable="false"
                                        range-separator="至"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                        class="datepicker"
                                        @change="getQueryDate"
                                >
                                </el-date-picker>
                            </el-form-item>

                            <el-form-item>
                                <el-button
                                        size="small"
                                        type="primary"
                                        icon="el-icon-search"
                                        @click="getData"
                                >查询
                                </el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
            </el-row>

            <el-table
                    :data="tableData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="index" label="序号" width="55" align="center">

                </el-table-column>
                <el-table-column
                        prop="areaName"
                        label="区域"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="streetCount"
                        label="街道数量"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="roadCount"
                        label="路内数量"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="deviceCount"
                        label="车位数量"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="inspectCount"
                        label="巡检数量"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="orderCOunt"
                        label="订单数"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="sumAmount"
                        label="应收金额"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="paiedAmount"
                        label="实收金额"
                        align="center"
                ></el-table-column>
            </el-table>
            <!-- <div class="pagination">
              <el-pagination
                background
                layout="total, prev, pager, next"
                :current-page="query.pageIndex"
                :page-size="query.pageSize"
                :total="pageTotal"
                @current-change="handlePageChange"
              ></el-pagination>
            </div> -->
        </div>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import {areaTjData} from "../../api/index";

    export default {
        name: "areaTongji",
        setup() {
            const query = reactive({
                startTime: "",
                endTime: "",
                // areaId: "",
                // streetId: "",
                // parkId: "",
                // pageIndex: 1,
                // pageSize: 15,
            });
            const tableData = ref([]);
            let form = reactive({
                time: "",
            });
            // 获取表格数据
            const getData = () => {
                areaTjData(query).then((res) => {
                    tableData.value = res.data;
                });
            };
            getData();

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
                form,
                getQueryDate,
                getData,
                query,
                tableData
            };
        },
        methods: {},
    };
</script>

