<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline size="small">

                        <div class="top-con">
                            <span class="dispinline ml5 font14 color666">人员姓名：</span>
                            <el-input
                                    size="small"
                                    @keyup.enter="handleSearch"
                                    v-model="query.userName"
                                    placeholder="请输入姓名"
                                    class="handle-input mr10"
                                    clearable
                            ></el-input>
                            <el-select
                                    v-model="query.is_absenteeism"
                                    filterable
                                    size="small"
                                    placeholder="是否旷工"
                                    class="w100"
                                    clearable
                            >
                                <el-option label="否" value="0" key="0"></el-option>
                                <el-option label="是" value="1" key="1"></el-option>
                            </el-select>
                            <span class="dispinline ml5"></span>
                            <el-select
                                    v-model="query.is_late"
                                    filterable
                                    size="small"
                                    placeholder="是否迟到"
                                    class="w100"
                                    clearable
                            >
                                <el-option label="否" value="0" key="0"></el-option>
                                <el-option label="是" value="1" key="1"></el-option>
                            </el-select>
                            <span class="dispinline ml5"></span>
                            <el-select
                                    v-model="query.is_leave_early"
                                    filterable
                                    size="small"
                                    placeholder="是否早退"
                                    class="w100"
                                    clearable
                            >
                                <el-option label="否" value="0" key="0"></el-option>
                                <el-option label="是" value="1" key="1"></el-option>
                            </el-select>
                        </div>
                        <div>
                            <span class="dispinline ml5 font14 color666">考勤日期：</span>
                            <el-date-picker
                                    size="small"
                                    v-model="query2.date1"
                                    type="daterange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    style="width:300px;"
                                    @change="getQueryDate"
                                    clearable
                            >
                            </el-date-picker>
                            <span class="dispinline ml5 font14 color666">上班打卡时间：</span>
                            <el-date-picker
                                    size="small"
                                    v-model="query2.date2"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    style="width:300px;"
                                    @change="getQueryDate2"
                                    clearable
                            >
                            </el-date-picker>
                            <span class="dispinline ml5 font14 color666">下班打卡时间：</span>
                            <el-date-picker
                                    size="small"
                                    v-model="query2.date3"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    style="width:300px;"
                                    @change="getQueryDate3"
                                    clearable
                            >
                            </el-date-picker>
                            <span class="dispinline ml5"></span>
                            <el-button
                                    size="small"
                                    type="primary"
                                    icon="el-icon-search"
                                    @click="handleSearch"
                            >查询
                            </el-button
                            >
                            <span class="dispinline ml5"></span>
                            <el-button size="small" type="success"  icon="el-icon-upload2" @click="exportExcel"
                            v-permission="['road_attendance_excel', 'park_attendance1_excel']"
                            >导出
                            </el-button
                            >

                        </div>

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
                        width="180"
                        align="center"
                ></el-table-column>
                <el-table-column
                        v-if="menuType == '0'"
                        prop="userName"
                        label="巡检员"
                        width="180"
                        align="center"
                ></el-table-column>
                <el-table-column
                        v-if="menuType == '1'"
                        prop="roadParkNames"
                        label="收费停车场"
                        align="center"
                ></el-table-column>
                <el-table-column
                        v-if="menuType == '0'"
                        prop="roadParkNames"
                        label="收费路内"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="create_time"
                        label="考勤日期"
                        align="center"
                        width="200"
                ></el-table-column>
                <el-table-column
                        prop="start_time"
                        label="上班打卡时间"
                        width="200"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="end_time"
                        label="下班打卡时间"
                        width="200"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="is_late"
                        label="是否迟到"
                        width="100"
                        align="center"
                >
                    <template #default="scope">
                        <p v-if="scope.row.is_late == '0'">否</p>
                        <p v-if="scope.row.is_late == '1'">是</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="is_leave_early"
                        label="是否早退"
                        width="100"
                        align="center"
                >
                    <template #default="scope">
                        <p v-if="scope.row.is_leave_early == '0'">否</p>
                        <p v-if="scope.row.is_leave_early == '1'">是</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="is_absenteeism"
                        label="是否旷工"
                        width="100"
                        align="center"
                >
                    <template #default="scope">
                        <p v-if="scope.row.is_absenteeism == '0'">否</p>
                        <p v-if="scope.row.is_absenteeism == '1'">是</p>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="120" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-view"
                                @click="handleView(scope.$index, scope.row)"
                                v-permission="['road_attendance_details', 'park_attendance1_details']"
                        >查看
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

        <!--详情弹出框 -->
        <el-dialog title="考勤详情" v-model="viewVisible" width="46%">
            <el-descriptions
                    class="margin-top handle-box"
                    title=""
                    :column="2"
                    :size="size"
                    border
            >
                <el-descriptions-item>
                    <template v-slot:label v-if="menuType == '1'">
                        <i class="el-icon-user"></i>
                        收费员
                    </template>
                    <template v-slot:label v-if="menuType == '0'">
                        <i class="el-icon-user"></i>
                        巡检员
                    </template>
                    {{form.userName}}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-date"></i>
                        考勤日期
                    </template>
                    {{form.create_time}}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-time"></i>
                        上班打卡时间
                    </template>
                    {{form.start_time}}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-picture-outline"></i>
                        上班打卡照片
                    </template>
                    <template v-for="(item, i) in form.sbFileList" :key="i">
                        <el-image
                                style="width: 80px; height: 80px"
                                hide-on-click-modal="true" preview-teleported="true"
                                class="ml5"
                                :src="imgurl(item.file_url)"
                                :preview-src-list="[imgurl(item.file_url)]"
                        >
                        </el-image>
                    </template>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-time"></i>
                        下班打卡时间
                    </template>
                    {{form.end_time}}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-picture-outline"></i>
                        下班打卡照片
                    </template>
                    <template v-for="(item, i) in form.xbFileList" :key="i">
                        <el-image
                                style="width: 80px; height: 80px"
                                class="ml5"
                                hide-on-click-modal="true" preview-teleported="true"
                                :src="imgurl(item.file_url)"
                                :preview-src-list="[imgurl(item.file_url)]"
                        >
                        </el-image>
                    </template>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-circle-check"></i>
                        是否迟到
                    </template>
                    <template #default="scope">
                        {{ form.is_late == 0 ? "否" : "是" }}
                    </template>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-circle-check"></i>
                        是否早退
                    </template>
                    <template #default="scope">
                        {{ form.is_leave_early == 0 ? "否" : "是" }}
                    </template>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-circle-check"></i>
                        是否旷工
                    </template>
                    <template #default="scope">
                        {{ form.is_absenteeism == 0 ? "否" : "是" }}
                    </template>
                </el-descriptions-item>

                <el-descriptions-item>
                    <template v-slot:label  v-if="menuType == '1'">
                        <i class="el-icon-map-location"></i>
                        收费停车场
                    </template>
                    <template v-slot:label  v-if="menuType == '0'">
                        <i class="el-icon-map-location"></i>
                        收费路内
                    </template>
                    <el-tag size="small" class="mar5 mb5" v-for="(item, index) in form.roadParkNames.split(',')" :key="index">{{item}}</el-tag>
                    <!--{{form.roadParkNames}}-->
                </el-descriptions-item>

            </el-descriptions>

            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="viewVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import {attendanceManagementList, attendanceManagementExportExcel} from "../../api/index";

    export default {
        name: "attendance",
        components: {},
        setup() {
            const menuType = localStorage.getItem("menuType");

            const query = reactive({
                userName: "",
                pageIndex: 1,
                pageSize: 10,
                sb_start_time: "",
                sb_end_time: "",
                xb_start_time: "",
                xb_end_time: "",
                create_start_time: "",
                create_end_time: "",
                is_late: "",
                is_leave_early: "",
                is_absenteeism: "",
                type: "0",
            });
            const query2 = reactive({
                date1: "",
                date2: "",
                date3: "",
            });
            const getStartTime = () => {
                var now = new Date(); //当前日期
                query2.date1 = [now, now];
                var date = now.getDate();
                var month = now.getMonth() + 1;
                if (month < 10) {
                    month = "0" + month;
                }
                if (date < 10) {
                    date = "0" + date;
                }
                query.create_start_time = now.getFullYear().toString() + "-" + month.toString() + "-" + date.toString();
                query.create_end_time = query.create_start_time;
            };
            getStartTime();

            const tableData = ref([]);
            const pageTotal = ref(0);
            // 获取表格数据
            const getData = () => {
                attendanceManagementList(query).then((res) => {
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

            //日期控件change方法 考勤日期
            const getQueryDate = () => {
                console.log(query2.date1);
                if (query2.date1 != null && query2.date1 != undefined && query2.date1.length > 0) {
                    query.create_start_time = dateFormat(query2.date1[0]);
                    query.create_end_time = dateFormat(query2.date1[1]);
                } else {
                    query.create_start_time = "";
                    query.create_end_time = "";
                }
            }
            //日期控件change方法 上班打卡时间
            const getQueryDate2 = () => {
                console.log(query2.date2);
                if (query2.date2 != null && query2.date2 != undefined && query2.date2.length > 0) {
                    query.sb_start_time = dateFormat2(query2.date2[0]);
                    query.sb_end_time = dateFormat2(query2.date2[1]);
                } else {
                    query.sb_start_time = "";
                    query.sb_end_time = "";
                }
            }
            //日期控件change方法 下班打卡时间
            const getQueryDate3 = () => {
                console.log(query2.date3);
                if (query2.date3 != null && query2.date3 != undefined && query2.date3.length > 0) {
                    query.xb_start_time = dateFormat2(query2.date3[0]);
                    query.xb_end_time = dateFormat2(query2.date3[1]);
                } else {
                    query.xb_start_time = "";
                    query.xb_end_time = "";
                }
            }

            //日期格式化 yyyy-MM-dd
            const dateFormat = (time) => {
                return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()}`;
            }
            //日期格式化 yyyy-MM-dd HH:mm:ss
            const dateFormat2 = (time) => {
                return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()} ${time.getHours() >= 10 ? time.getHours() : '0' + time.getHours()}:${time.getMinutes() >= 10 ? time.getMinutes() : '0' + time.getMinutes()}:${time.getSeconds() >= 10 ? time.getSeconds() : '0' + time.getSeconds()}`;
            }

            //导出excel
            const exportExcel = () => {
                attendanceManagementExportExcel(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]))
                    const link = document.createElement('a')
                    link.href = url
                    link.setAttribute('download', '考勤记录.xlsx')
                    document.body.appendChild(link)
                    link.click()
                })
            }

            const imgurl = (url) => {
                if (url != "" && url != null) {
                    return url;
                }
            };
            let idx = -1;
            const viewVisible = ref(false);
            let form = reactive({
                id: "",
                userName: "",
                create_time: "",
                start_time: "",
                end_time: "",
                is_late: "",
                is_leave_early: "",
                is_absenteeism: "",
                roadParkNames: "",
                sbFileList: [],
                xbFileList: [],
            });
            //详情
            const handleView = (index, row) => {
                idx = index;
                Object.keys(form).forEach((item) => {
                    form[item] = row[item];
                });
                console.log(form);
                viewVisible.value = true;
            };

            return {
                query,
                query2,
                tableData,
                pageTotal,
                handleSearch,
                handlePageChange,
                getQueryDate,
                getQueryDate2,
                getQueryDate3,
                exportExcel,
                menuType,
                viewVisible,
                handleView,
                imgurl,
                form
            };
        },
        methods: {},
    };
</script>
<style scoped>
    .top-con {
        margin-bottom: 10px;
    }
    ::v-deep .el-descriptions__label.is-bordered-label{
        width: 120px;
    }
    ::v-deep .el-descriptions__content{
        min-width: 200px;
    }
</style>
