<template>
    <div>
        <div class="container">

            <div class="handle-box">
                <div class="left-panel">
                    <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd()"
                        v-permission="['road_discount_add', 'park_discount_add']">添加
                    </el-button>
                </div>
                <!--<div class="right-panel">
                    <el-input size="small" v-model="query.money" placeholder="金额" @keyup.enter="handleSearch()" class="handle-input mr10"></el-input>
                    <span class="ml5"></span>
                    <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
                </div>-->
                <div class="clear"></div>
            </div>
            <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
                <el-table-column pro="ID" label="序号" width="55" align="center">
                    <template #default="scope">
                        {{ (query.pageIndex - 1) * query.pageSize + scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="名称" align="center" minWidth="180"></el-table-column>
                <el-table-column prop="money" label="金额" align="center" width="200"></el-table-column>
                <el-table-column prop="credit" label="赠送金额" align="center" width="200"> </el-table-column>
                <el-table-column prop="create_time" label="创建时间" align="center" width="200"> </el-table-column>
                <el-table-column prop="create_user_name" label="创建人" align="center" width="200"> </el-table-column>
                <el-table-column label="操作" width="150" align="center" fixed="right">
                    <template #default="scope">
                        <el-button size="small" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)"
                            v-permission="['road_discount_edit', 'park_discount_edit']">编辑</el-button>
                        <el-button size="mini" type="text" icon="el-icon-delete"
                            @click="handleRemove(scope.$index, scope.row)"
                            v-permission="['road_discount_delete', 'park_discount_delete']">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                    :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

        <!-- 新增弹出框 -->
        <el-dialog title="新增" v-model="addVisible" width="500px">
            <el-form label-width="100px" :model="form" :rules="formRules" ref="priceform" size="small">
                <!-- 隐藏一个input -->
                <el-form-item prop="id" style="display: none;">
                    <el-input v-model="form.id"></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="name">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="金额" prop="money">
                    <el-input v-model.number="form.money">
                        <template #append>元</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="赠送金额" prop="credit">
                    <el-input v-model.number="form.credit">
                        <template #append>元</template>
                    </el-input>
                </el-form-item>

            </el-form>

            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="addVisible = false">取 消</el-button>
                    <el-button type="primary" @click="saveAdd" :disabled="codeDisabled.value">确 定</el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>

<script>
import { ref, reactive, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { storeDiscountList, storeDiscountAdd, storeDiscountRemove } from "../../api/store";

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
            money: "",
            name: "",
            checked: false,
            pageIndex: 1,
            pageSize: 10,
        });
        const tableData = ref([]);
        const pageTotal = ref(0);
        // 获取表格数据
        const getData = () => {
            storeDiscountList(query).then((res) => {
                tableData.value = res.data.list;
                pageTotal.value = res.data.total || 0;
            });
        };
        getData();
        let form = ref({});


        const addVisible = ref(false);

        const handleAdd = () => {

            form.value = {
                name: "",
                money: "",
                credit: "",
            };

            nextTick(() => {
                priceform.value.clearValidate();
            });

            addVisible.value = true;
        }
        const handleEdit = (row) => {
            console.log(row)
            addVisible.value = true;
            form.value = {
                id: row.id,
                name: row.name,
                money: row.money - 0,
                credit: row.credit - 0,
            };
            nextTick(() => {
                priceform.value.clearValidate();
            });

        }


        const saveAdd = () => {
            if (codeDisabled.value) {
                return;
            }
            codeDisabled.value = true;
            setTimeout(() => {
                codeDisabled.value = false   //点击一次时隔两秒后才能再次点击
            }, 2000)
            priceform.value.validate((valid) => {
                if (valid) {
                    console.log('submit!');

                    storeDiscountAdd(form.value).then((res) => {
                        if (res.code == 0) {
                            ElMessage.success(res.msg);
                            addVisible.value = false;

                            getData();
                        } else {

                            ElMessage.error(res.msg);
                            // ElMessageBox.alert(res.msg);
                        }
                    });
                    addVisible.value = false;
                } else {
                    console.log('error submit!')
                    return false
                }
            })
        };

        const priceform = ref(null);
        const codeDisabled = ref(false);

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
        const handleRemove = (index, row) => {
            ElMessageBox.confirm('确认删除该条记录吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                storeDiscountRemove(row).then((res) => {
                    if (res.code == 0) {
                        getData();
                    } else {
                        ElMessageBox.alert(res.msg);
                    }
                });
            }).catch(() => {
                // 取消
            });
        };

        const formRules = {
            name: [
                { required: true, message: '名称不能为空', trigger: "blur" }
            ],
            money: [
                { required: true, message: '金额不能为空', trigger: "blur" },
                { type: 'number', message: '金额必须为数字值' }
            ],
            credit: [
                { required: true, message: '赠送金额不能为空', trigger: "blur" },
                { type: 'number', message: '金额必须为数字值' }
            ],

        };


        return {
            query,
            tableData,
            pageTotal,
            addVisible,
            handleAdd,
            saveAdd,
            form,
            formRules,
            priceform,
            codeDisabled,
            handleSearch,
            handlePageChange,
            handleRemove,
            handleEdit,

        };
    },
    methods: {




    },
};
</script>
<style scoped>
.el-image {
    margin: 4px 4px 0 4px;
}</style>
