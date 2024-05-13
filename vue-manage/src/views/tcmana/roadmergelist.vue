<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-button
                            type="primary"
                            size="small"
                            icon="el-icon-plus"
                            @click="handleEdit(0, null, '添加')"
                    >添加
                    </el-button>
                </div>
                <div class="right-panel">
                    <el-form inline size="small">
                        <el-input
                                @keyup.enter="handleSearch()"
                                size="small"
                                v-model="query.name"
                                placeholder="路内合并名称"
                                class="handle-input mr10"
                        ></el-input>
                        <el-button
                                size="small"
                                type="primary"
                                icon="el-icon-search"
                                @click="handleSearch"
                        >查询
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
                    @selection-change="handleSelectionChange"
            >
                <el-table-column
                        type="selection"
                        width="55"
                        align="center"
                ></el-table-column>
                <el-table-column type="index" label="序号" width="55" align="center">
                </el-table-column>
                <el-table-column
                        prop="name"
                        align="center"
                        label="路内合并名称"
                        min-width="200"
                ></el-table-column>
                <el-table-column
                        prop="roadNames"
                        label="合并路内"
                        min-width="200"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="create_time"
                        label="创建时间"
                        align="center"
                        width="200"
                ></el-table-column>
                <el-table-column label="操作" width="200" fixed="right" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-view"
                                @click="handleEdit(scope.$index, scope.row, '查看')"
                        >查看
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row,'编辑')"
                        >编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-circle-close"
                                @click="handleDelete(scope.$index, scope.row)"
                                v-if="scope.row.status == '0'"
                                class="red"
                        >删除
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

        <!-- 新增/编辑弹出框 -->
        <el-dialog
                :title="result.dialogT"
                v-model="editVisible"
                width="800px"
                top="10vh"
                :close-on-click-modal="false"
                destroy-on-close="true"
                @closed="cancleEdit"
        >
            <el-form
                    label-width="120px"
                    size="small"
                    :rules="formRules"
                    :model="form"
            >
                <el-form-item label="路内合并名称：" prop="name">
                    <el-input :disabled="viewDetail" v-model="form.name" placeholder="请输入" class="w"></el-input>
                </el-form-item>
                <el-form-item label="所属区域：" prop="area_id">
                    <el-select
                            :disabled="viewDetail"
                            v-model="form.area_id"
                            filterable
                            size="small"
                            placeholder="请选择"
                            class="w"
                            @change="getStreetData"
                    >
                        <el-option
                                v-for="(item, i) in result.area_list"
                                :key="i"
                                :label="item.area_name"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="所属街道：" prop="street_id">
                    <el-select
                            :disabled="viewDetail"
                            v-model="form.street_id"
                            filterable
                            size="small"
                            placeholder="请选择"
                            class="w"
                            @change="getRoad"
                    >
                        <el-option
                                v-for="(item, i) in result.street_list"
                                :key="i"
                                :label="item.street_name"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="请选择合并路内：" prop="roadIds" style="display: block;">
                    <el-transfer
                            class="road-wrap"
                            filterable
                            :titles="['待选择区域', '已选择区域']"
                            filter-placeholder="请输入路内名"
                            v-model="roadList"
                            :data="result.road_list"
                    >
                    </el-transfer>
                </el-form-item>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancleEdit">取 消</el-button>
          <el-button v-if="!viewDetail" type="primary" @click="saveEdit">确定</el-button>
        </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import {
        queryAreaData,
        queryStreetData,
        queryRoadData,
        toList,
        toView,
        toAdd,
        toEdit,
        toDel
    } from "../../api/index";
    import {useRouter} from "vue-router";

    export default {
        name: "Roadmergelist",
        components: {},
        data() {
            return {
                formRules: {
                    name: [{required: true, message: "必填项", trigger: "blur"}],
                    area_id: [{required: true, message: "必填项", trigger: "blur"}],
                    street_id: [{required: true, message: "必填项", trigger: "blur"}],
                    roadIds: [{required: true, message: "必填项", trigger: "blur"}],
                },
            };
        },
        setup() {
            const query = reactive({
                name: "",
                pageIndex: 1,
                pageSize: 20,
            });
            let form = reactive({
                name: "",
                area_id: "",
                street_id: "",
                roadIds: '',
                id: "",
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            const router = useRouter();
            // 获取表格数据
            const getData = () => {
                toList(query).then((res) => {
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
            };
            getData();

            // 结果集
            let result = reactive({
                area_list: [],
                street_list: [],
                road_list: [],
                dialogT: "",
            });

            let roadList = ref([])

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

            // 删除操作
            const handleDelete = (index, row) => {
                // 二次确认删除
                ElMessageBox.confirm("确定要删除吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        let edit_road = reactive({
                            is_del: "1",
                            id: row.id,
                        });
                        toDel(edit_road)
                            .then((res) => {
                                // console.log(res);
                                ElMessage.success("删除成功");
                            })
                            .then(() => {
                                getData();
                            });
                    })
                    .catch(() => {
                    });
            };

            // 表格编辑时弹窗和保存
            const editVisible = ref(false);
            let idx = "";
            let viewDetail = ref(false);
            // 新增编辑
            const handleEdit = (index, row, type) => {
                result.street_list = [];
                if (row) {
                    roadList.value = row.roadIds
                    //查询街道
                    queryStreetData({areaId: row.areaId}).then((res) => {
                        // console.log(res);
                        result.street_list = res.data;
                    });
                    // 查询路内
                    queryRoadData({streetId: row.street_id}).then((res) => {
                        const resData = []
                        res.data.forEach((item, index) => {
                            resData.push({
                                label: item.road_name,
                                key: item.id,
                                value: item.id,
                                disabled: viewDetail.value,
                            });
                        });
                        result.road_list = resData
                        // console.log('222222222222222', res.data, result.road_list, roadList)
                    });
                }

                if (type === '添加') {
                    viewDetail.value = false
                    result.dialogT = "添加合并路内";
                    idx = "";
                    form.id = "";
                    form.name = "";
                    form.area_id = "";
                    form.street_id = "";
                    form.roadIds = "";
                } else if (type === '编辑') {
                    result.dialogT = "编辑合并路内";
                    viewDetail.value = false
                    idx = row.id;

                    // console.log(form);
                    Object.keys(form).forEach((item) => {
                        form[item] = row[item];
                    });
                } else {
                    result.dialogT = "查看合并路内";
                    viewDetail.value = true
                    idx = row.id;
                    Object.keys(form).forEach((item) => {
                        form[item] = row[item];
                    });
                }

                editVisible.value = true;
            };
            const saveEdit = () => {
                // console.log('222222222222222', roadList)
                form.roadIds = roadList.value.join(',');
                // 不可为空
                if (
                    form.name == null ||
                    form.name == "" ||
                    form.area_id == null ||
                    form.area_id == "" ||
                    form.street_id == null ||
                    form.street_id == "" ||
                    form.roadIds == null ||
                    form.roadIds == ""
                ) {
                    ElMessage.error("参数不可为空！");
                    return false;
                }
                // console.log('baocunbaocun', form)
                if (!idx) {
                    toAdd(form).then((res) => {
                        // console.log(res);
                        if (res.code == 0) {
                            ElMessage.success("添加成功");
                            editVisible.value = false;
                            query.pageIndex = 1;
                            getData();
                        } else {
                            ElMessage.error(res.msg);
                        }
                    });
                } else {
                    toEdit(form).then((res) => {
                        // console.log(res);
                        if (res.code == 0) {
                            ElMessage.success("修改成功");
                            roadList.value = [];
                            editVisible.value = false;
                            getData();
                        } else {
                            ElMessage.error(res.msg);
                        }
                    });
                }
            };
            const cancleEdit = () => {
                editVisible.value = false;
                roadList.value = [];
                result.road_list = []
            };

            const handleSelectionChange = (data) => {
                this.selectedData = data;
            }

            // 获取区域
            const getAreaData = () => {
                queryAreaData().then((res) => {
                    result.area_list = res.data;
                });
            };
            getAreaData();

            // 获取街道
            let queryStreet = reactive({
                areaId: form.area_id,
            });
            const getStreetData = () => {
                // console.log("form", form);
                queryStreet.areaId = form.area_id;
                form.street_id = "";
                queryStreetData(queryStreet).then((res) => {
                    // console.log(res);
                    result.street_list = res.data;
                });
            };
            //获取路内下拉框数据
            const getRoad = () => {
                // console.log('1111111111111111', form)
                roadList.value = []
                queryRoadData({streetId: form.street_id}).then((res) => {
                    const resData = []
                    res.data.forEach((item, index) => {
                        resData.push({
                            label: item.road_name,
                            key: item.id,
                            value: item.id
                        });
                    });
                    result.road_list = resData
                    // console.log('222222222222222', res.data, result.road_list)
                });
            };

            // 穿梭框改变数据
            const handleChange = (val) => {
                // console.log('33333333333333333333333', val)
                roadList.value = val
            }
            return {
                query,
                tableData,
                pageTotal,
                editVisible,
                viewDetail,
                form,
                roadList,
                result,
                handleSearch,
                handlePageChange,
                handleSelectionChange,
                getData,
                handleDelete,
                handleEdit,
                saveEdit,
                cancleEdit,
                getStreetData,
                getRoad,
                handleChange
            };
        },
        methods: {},
    };
</script>
<style scoped>
    ::v-deep .road-wrap .el-transfer-panel {
        width: 290px;
    }
</style>
