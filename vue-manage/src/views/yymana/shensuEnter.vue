<template>
    <div>
        <div class="handle-title mgb12">
            <i class="el-icon-s-opportunity"></i>订单查询
        </div>
        <div class="container">
            <div class="handle-box">
                <el-form inline size="small" class="lineH0">
                    <el-form-item label="车牌号" class="search-mb0">
                        <el-input
                                @keyup.enter="handleSearch()"
                                size="small"
                                v-model="query.carNo"
                                placeholder="车牌号"
                                class="handle-input mr10 w170"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="创建时间" class="search-mb0">
                        <el-date-picker
                                v-model="form.time"
                                placeholder=""
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                @change="getQueryDate"
                        ></el-date-picker>
                    </el-form-item>
                    <el-form-item class="search-mb0">
                        <el-button
                                size="small"
                                type="primary"
                                icon="el-icon-search"
                                @click="handleSearch"
                        >查询
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>
            <el-table
                    :data="tableData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column label="序号" width="55" align="center">
                    <template #default="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="orderNo"
                        label="订单号"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="placeName"
                        label="停车区域"
                        align="center"
                ></el-table-column>
                <el-table-column prop="driveinTime" align="center" label="驶入时间">
                </el-table-column>
                <el-table-column
                        prop="driveoutTime"
                        label="驶离时间"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="resitime"
                        align="center"
                        label="停留时长"
                ></el-table-column>
                <el-table-column
                        prop="carNo"
                        align="center"
                        label="车牌号码"
                ></el-table-column>
                <el-table-column
                        prop="sum_amount"
                        align="center"
                        label="停车费用"
                ></el-table-column>
                <el-table-column
                        prop="status_name"
                        align="center"
                        label="状态"
                ></el-table-column>
                <el-table-column
                        prop="orderNo"
                        align="center"
                        v-if="liHide"
                ></el-table-column>
                <el-table-column prop="member_id" label="是否会员" align="center">
                    <template #default="scope">
                        <el-tag v-if="scope.row.member_id == null">否</el-tag>
                        <el-tag v-else>否</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row, false)"
                        >申诉录入
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
        <div class="handle-title mgb12 mt20">
            <i class="el-icon-s-opportunity"></i>录入投诉信息
        </div>
        <div class="container">
            <el-form label-width="70px" :model="form" ref="formId">
                <el-form-item label="订单号">
                    <el-input placeholder="" v-model="form.order_no" disabled></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <el-input
                            placeholder=""
                            v-model="form.content"
                            type="textarea"
                            :rows="5"
                    ></el-input>
                </el-form-item>
                <el-form-item label="联系方式" prop="phone">
                    <el-input placeholder="" v-model="form.phone"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="saveEdit('formId')" :disabled="isDisabl"
                    >确 定
                    </el-button
                    >
                    <el-button @click="resetForm('formId')">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import {reactive, ref} from "vue";
    import {ElMessage} from "element-plus";
    import {
        operateappealListData,
        operateappealSaveData,
    } from "../../api/operateAppeal.js";

    export default {
        name: "operateAppeal",
        data() {
            return {
                liHide: false,
                isDisabl: false,
            };
        },
        setup() {
            const query = reactive({
                car_no: "",
                startTime: "",
                endTime: "",
                pageIndex: 1,
                pageSize: 10,
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            // 获取表格数据
            const getData = () => {
                operateappealListData(query).then((res) => {
                    if (res.code != -1) {
                        tableData.value = res.data.list;
                        pageTotal.value = res.data.total;
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

            // 表格编辑时弹窗和保存
            const editVisible = ref(false);
            let form = ref({
                time: "",
                order_no: "",
                phone: "",
                content: "",
                parking_order_id: "",
                placeId: "",
                appeal_order_status: "",
            });
            const handleEdit = (index, row, type) => {
                console.log(row);
                form.value.parking_order_id = row.id;
                form.value.placeId = row.placeId;
                form.value.order_no = row.orderNo;
                form.value.phone = row.phone;
                form.value.appeal_order_status = row.status;
                editVisible.value = true;
            };

            //日期控件change方法
            const getQueryDate = () => {
                if (
                    null == form.value.time ||
                    [] == form.value.time ||
                    "" == form.value.time
                ) {
                    query.startTime = "";
                    query.endTime = "";
                } else {
                    query.startTime = dateFormat(form.value.time[0]);
                    query.endTime = dateFormat(form.value.time[1]);
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
                query,
                tableData,
                pageTotal,
                editVisible,
                form,
                getQueryDate,
                handleSearch,
                handlePageChange,
                handleEdit,
                multipleSelection: [],
                value: true,
                activeName: "first",
            };
        },

        methods: {
            handleSelectionChange(data) {
                this.selectedData = data;
            },
            saveEdit(formName) {
                //保存方法
                let falg = 0;
                let msg = "";
                let _that = this;
                console.log(_that.form);
                _that.isDisabl = true;
                operateappealSaveData(_that.form)
                    .then((res) => {
                        falg = res.code;
                        msg = res.msg;
                    })
                    .then(() => {
                        if (falg == 0) {
                            _that.editVisible = false;

                            setTimeout(() => {
                                _that.isDisabl = false;
                            }, 2000);

                            ElMessage.success(msg);
                            this.$refs[formName].resetFields();
                        } else {
                            setTimeout(() => {
                                _that.isDisabl = false;
                            }, 2000);
                            ElMessage.error(msg);
                        }
                    });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
        },
    };
</script>
<style scoped>
    .handle-title {
        font-size: 16px;
        font-weight: normal;
        color: #303133;
    }

    .mgb12 {
        margin-bottom: 12px;
    }
</style>
