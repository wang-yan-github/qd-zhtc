<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline size="small">

                        <span class="dispinline ml5 font14 color666">考勤日期：</span>
                        <el-date-picker
                                size="small"
                                v-model="query.strTime"
                                type="month"
                                format="YYYY-MM"
                                value-format="YYYY-MM"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
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
                        <el-button size="small" type="success" icon="el-icon-upload2" @click="exportExcel">导出
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
                    header-cell-class-name="table-header"
            >
                <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
                <el-table-column
                        v-if="menuType == '1'"
                        prop="userName"
                        label="收费员"
                        align="center"
                ></el-table-column>
                <el-table-column
                        v-if="menuType == '0'"
                        prop="userName"
                        label="巡检员"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="days"
                        label="考勤天数"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="leaveEarlyDays"
                        label="早退天数"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="lateDays"
                        label="迟到天数"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="absenteeismDays"
                        label="缺勤天数"
                        align="center"
                ></el-table-column>

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
    import {attendanceReportList, attendanceReportExportExcel} from "../../api/index";

    export default {
        name: "attendancePerson",
        components: {},
        setup() {
            const menuType = localStorage.getItem("menuType");

            const query = reactive({
                pageIndex: 1,
                pageSize: 10,
                strTime: "",
                type: "1",
            });
            const query2 = reactive({
                date1: "",
            });
            const getStartTime = () => {
                var now = new Date(); //当前日期
                var month = now.getMonth() + 1;
                if (month < 10) {
                    month = "0" + month;
                }
                query.strTime = now.getFullYear().toString() + "-" + month.toString();
            };
            getStartTime();

            const tableData = ref([]);
            const pageTotal = ref(0);
            // 获取表格数据
            const getData = () => {
                attendanceReportList(query).then((res) => {
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

            //导出excel
            const exportExcel = () => {
                attendanceReportExportExcel(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]))
                    const link = document.createElement('a')
                    link.href = url
                    link.setAttribute('download', '考勤统计.xlsx')
                    document.body.appendChild(link)
                    link.click()
                })
            }

            return {
                query,
                query2,
                tableData,
                pageTotal,
                handleSearch,
                handlePageChange,
                exportExcel,
                menuType,
            };
        },
        methods: {},
    };
</script>

