<template>
    <div>
        <!-- <div class="crumbs">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>
              <i class="el-icon-lx-cascades"></i> 用户管理
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div> -->
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-button
                            type="primary"
                            size="small"
                            icon="el-icon-plus"
                            @click="handleEdit(0, null, true)"
                            v-permission="['road_arealist_add', 'park_arealist_add']"
                    >添加</el-button
                    >
                    <el-button
                            type="danger"
                            size="small"
                            icon="el-icon-delete"
                            @click="handleDeleteAll()"
                            v-permission="['road_arealist_deleteAll', 'park_arealist_deleteAll']"
                    >批量删除</el-button
                    >
                </div>
                <div class="right-panel">
                    <el-input
                            @keyup.enter="handleSearch()"
                            size="small"
                            v-model="query.area_name"
                            placeholder="名称"
                            class="handle-input mr10"
                    ></el-input>
                    <el-button
                            size="small"
                            type="primary"
                            icon="el-icon-search"
                            @click="handleSearch"
                    >查询</el-button
                    >
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
                        type="selection"
                        width="55"
                        align="center"
                ></el-table-column>
                <el-table-column type="index" label="序号" width="55" align="center">

                </el-table-column>
                <el-table-column
                        prop="area_name"
                        label="区域名称"

                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="area_level"
                        label="区域等级"

                        align="center"
                >
                    <template #default="scope">
                        <p v-if="scope.row.area_level==0">一类</p>
                        <p v-if="scope.row.area_level==1">二类</p>
                        <p v-if="scope.row.area_level==2">三类</p>
                    </template>
                </el-table-column>
                <!-- <el-table-column label="头像(查看大图)" align="center">
                            <template #default="scope">
                                <el-image class="table-td-thumb" :src="scope.row.thumb" :preview-src-list="[scope.row.thumb]">
                                </el-image>
                            </template>
                        </el-table-column> -->
                <!-- <el-table-column
                        prop="city"
                        label="所属城市"
                        align="center"
                        width="180"
                ></el-table-column> -->
                <el-table-column
                        prop="street_num"
                        label="街道数量"
                        width="100"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="create_time"
                        label="添加时间"

                        align="center"
                ></el-table-column>
                <el-table-column label="状态" align="center" width="100">
                    <template #default="scope">
                        <el-tag
                                size="small"
                                :type="
                scope.row.status === '0'
                  ? 'success'
                  : scope.row.status === '1'
                  ? 'danger'
                  : ''
              "
                                v-if="scope.row.status==0">启用</el-tag
                        >
                        <el-tag
                                size="small"
                                :type="
                scope.row.status === '0'
                  ? 'success'
                  : scope.row.status === '1'
                  ? 'danger'
                  : ''
              "
                                v-else>禁用</el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="220" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row, false)"
                                v-permission="['road_arealist_edit', 'park_arealist_edit']"
                        >编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-circle-close"
                                @click="handleStop(scope.$index, scope.row,'1')"
                                v-if="scope.row.status=='0'"
                                class="red"
                                v-permission="['road_arealist_status', 'park_arealist_status']"
                        >停用
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-circle-check"
                                @click="handleStop(scope.$index, scope.row,'0')"
                                v-permission="['road_arealist_status', 'park_arealist_status']"
                                v-if="scope.row.status=='1'"
                        >启用
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-delete"
                                class="red"
                                @click="handleDelete(scope.$index, scope.row)"
                                v-permission="['road_arealist_delete', 'park_arealist_delete']"
                        >删除</el-button
                        >
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

        <!-- 新增弹出框 -->
        <el-dialog  :title="result.dialogT" v-model="editVisible" width="30%">
            <el-form label-width="90px" :rules="formRules"  :model="form">
                <el-form-item label="区域名称" prop="area_name">
                    <el-input v-model="form.area_name" placeholder="请输入区域名称"></el-input>
                </el-form-item>
                <el-form-item label="区域等级" prop="area_level">
                    <el-select v-model="form.area_level" placeholder="请选择" class="w">

                        <el-option v-for="(item,i) in result.area_levels" :key="i" :label="item.label" :value="item.dc_value"></el-option>

                    </el-select>
                </el-form-item>
            </el-form>

            <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    import { ref, reactive } from "vue";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { areaList,addArea,dictData,editArea,delAreaAll } from "../../api/index";

    export default {
        name: "arealist",
        data(){
            return{
                tableH:0,
                formRules: {
                    area_name: [{ required: true, message: "必填项", trigger: "blur" }],
                    area_level: [{ required: true, message: "必填项", trigger: "blur" }],
                },
            };
        },
        setup() {
            const query = reactive({
                area_name: "",
                pageIndex: 1,
                pageSize: 15,
            });


            let result = reactive({
                area_levels:[],
                dialogT:""
            });


            const tableData = ref([]);
            const pageTotal = ref(0);
            // 获取表格数据
            const getData = () => {
                areaList(query).then((res) => {
                    console.log(res.data);
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


            // 查询区域等级
            let dict = reactive({
                is_del:"0",
                dict_type:"areaLevel",

            });
            const getAreaLevel = () => {
                dictData(dict).then((res) => {
                    console.log(res);
                    result.area_levels=res.data;
                });
            };

            getAreaLevel();



            // 删除操作
            const handleDelete = (index,row) => {
                // 二次确认删除
                ElMessageBox.confirm("确定要删除吗？", "提示", {
                    type: "warning",
                }).then(() => {
                    let edit_area= reactive({
                        is_del:"1",
                        id:row.id
                    });
                    editArea(edit_area).then((res) => {
                        console.log(res);
                        ElMessage.success("删除成功");
                    }).then(() =>{
                        getData();
                    });
                })
                    .catch(() => {});
            };

            // 停用操作
            const handleStop = (index,row,sta) => {
                // 二次确认停用
                ElMessageBox.confirm("请确定当前操作？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        let edit_area= reactive({
                            status:sta,
                            id:row.id
                        });
                        editArea(edit_area).then((res) => {
                            console.log(res);
                            ElMessage.success("操作成功");
                        }).then(() =>{
                            getData();
                        });
                    })
                    .catch(() => {});
            };




            // 表格编辑时弹窗和保存
            const editVisible = ref(false);

            let form = reactive({
                area_name: "",
                area_level:"",
                status: "0",
                is_del: "0",
            });




            let idx = "";

            const handleEdit = (index, row, type) => {
                // 获取区域信息

                if (type) {
                    result.dialogT='新增'
                    idx = "";
                    form.area_name ="";
                    form.area_level  ="";
                } else {
                    result.dialogT='编辑';
                    idx = row.id;
                    Object.keys(form).forEach((item) => {
                        form[item] = row[item];
                    });
                }

                editVisible.value = true;
            };

            const saveEdit = () => {


                // 不可为空
                if(form.area_name==null||form.area_name==""||form.area_level==null||form.area_level==""){
                    ElMessage.error("参数不可为空！");
                    return false;
                }




                // 新增
                if(!idx){
                    addArea(form).then((res) => {
                        console.log(res.data);
                    }).then(()=>{
                        query.pageIndex = 1;
                        getData();
                    });
                }else{ // 编辑
                    let edit_area= reactive({
                        id:idx,
                        area_name:form.area_name,
                        area_level:form.area_level
                    });
                    editArea(edit_area).then((res) => {
                        console.log(res);
                        ElMessage.success("修改成功");
                    }).then(()=>{
                        getData();
                    });


                }
                  editVisible.value = false;


            };

            return {

                query,
                tableData,
                pageTotal,
                editVisible,
                result,
                form,
                dict,

                handleSearch,
                handlePageChange,
                getData,
                handleDelete,
                handleEdit,
                handleStop,
                saveEdit,
                multipleSelection: [],
                value: true,
                activeName: "first",
            };
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + 'px';
        },
        methods: {
            handleSelectionChange(data) {
                this.selectedData = data;
            },
            handleDeleteAll() {
                ElMessageBox.confirm("确定要删除吗？", "提示", {
                    type: "warning",
                }).then(() => {
                    var that = this;
                    var val = this.selectedData;
                    var ids = "";
                    if (val) {
                        val.forEach(function (item, index) {
                            //alert(item.id);
                            ids = ids +  item.id + ",";
                        });
                        delAreaAll({ areaIds: ids }).then((res) => {
                            ElMessage.success("删除成功");
                            that.getData();
                        })
                    } else {
                        ElMessage.warning(`请选择一条记录`);
                    }
                }).catch(() => {});

            },
        },
        mounted() {
            // this.getData();
            //this.initMap();
        },
    };
</script>
