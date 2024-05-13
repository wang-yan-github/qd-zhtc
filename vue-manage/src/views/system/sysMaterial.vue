<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-button v-permission="['park_sysMaterial_add', 'road_sysMaterial_add']" type="primary" size="small"
                        icon="el-icon-plus" @click="handleEdit(0, null, true, null)">添加
                    </el-button>
                    <el-button v-permission="['road_sysMaterial_deleteAll', 'park_sysMaterial_deleteAll']" type="danger"
                        size="small" icon="el-icon-delete" @click="handleDeleteAll()">批量删除
                    </el-button>
                </div>
                <div class="right-panel">
                    <el-input @keyup.enter="handleSearch()" size="small" v-model="query.title" placeholder="标题"
                        class="handle-input mr10"></el-input>
                    <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询
                    </el-button>
                </div>
                <div class="clear"></div>
            </div>
            <el-table :data="tableData" border class="table" ref="multipleTable" :max-height="tableH"
                header-cell-class-name="table-header" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column label="序号" width="55" align="center">
                    <template #default="scope">
                        {{ (query.pageIndex - 1) * query.pageSize + scope.$index + 1 }}
                    </template>
                </el-table-column>

                <el-table-column prop="title" label="标题" align="center"></el-table-column>
                <el-table-column width="100" label="类型" align="center">
                    <template #default="scope">
                        <p v-if="scope.row.type == '1'">图片</p>
                        <p v-if="scope.row.type == '2'">视频</p>
                    </template>
                </el-table-column>
                <el-table-column label="启用" align="center" width="100">
                    <template #default="scope">
                        <el-tag size="small" type="success" v-if="scope.row.state == '1'">启用</el-tag>
                        <el-tag size="small" type="danger" v-else>禁用</el-tag>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="220" align="center">
                    <template #default="scope">
                        <el-button v-permission="['park_sysMaterial_edit', 'road_sysMaterial_edit']" size="mini" type="text"
                            icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row, false)">编辑
                        </el-button>

                        <el-button v-permission="['road_sysMaterial_delete', 'park_sysMaterial_delete']" size="mini"
                            type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                    :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

        <!-- 新增编辑弹出框 -->
        <el-dialog :title="dialogT" v-model="editVisible" width="500px">
            <el-form label-width="70px" size="small" :rules="formRules" ref="formId" :model="form">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入标题"></el-input>
                </el-form-item>

                <el-form-item label="类型">
                    <el-radio-group v-model="form.type">
                        <el-radio label="1">图片</el-radio>
                        <el-radio label="2">视频</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="文件">
                    <el-upload class="upload-demo" :action="uploadUrl" :on-preview="handlePreview" :on-remove="handleRemove"
                        :limit="1" :on-exceed="handleExceed" :file-list="fileList2" :on-success="handleSuccess"
                        :before-upload="beforeAvatarUpload">
                        <el-button size="small" type="primary">点击上传</el-button>
                    </el-upload>
                </el-form-item>

                <el-form-item label="是否启用" prop="">
                    <el-radio-group v-model="form.state">
                        <el-radio label="1">是</el-radio>
                        <el-radio label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>

            </el-form>

            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="editVisible = false">取 消</el-button>
                    <el-button type="primary" @click="saveEdit('formId')">确 定</el-button>
                </span>
            </template>
        </el-dialog>

        <el-dialog v-model="dialogVisible">
            <div class="customWidth">
                <img class="imgWidth" :src="dialogImageUrl" alt="" />
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
    delAll,
    delImgFile,
    detailsSysMaterial,
    editSysMaterial,
    sysMaterialList,
} from "../../api/sysMaterial";

import File_URL from '../../file_url';

export default {
    name: "sysMateriallist",
    components: {},
    data() {
        return {
            tableH: 0,
            uploadUrl: File_URL.file_img_url,//图片上传路径
            imgViewUrl: File_URL.file_hx_img_url,//图片回显路径
            // 表单验证
            formRules: {
                title: [{ required: true, message: "必填项", trigger: "blur" }],
            },
        };
    },
    setup() {

        const query = reactive({
            title: "",
            pageIndex: 1,
            pageSize: 15,
        });
        const tableData = ref([]);
        const pageTotal = ref(0);
        // 获取表格数据
        const getData = () => {
            sysMaterialList(query).then((res) => {
                console.log(res);
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

        // 删除操作
        const handleDelete = (index, row) => {
            // 二次确认删除
            ElMessageBox.confirm("确定要删除吗？", "提示", {
                type: "warning",
            })
                .then(() => {
                    let edit_resource = reactive({
                        is_del: "1",
                        id: row.id
                    });
                    editSysMaterial(edit_resource).then((res) => {
                        console.log(res);
                        ElMessage.success("删除成功");
                    }).then(() => {
                        getData();
                    });
                })
                .catch(() => {
                });
        };
        let dialogT = ref("编辑");

        // 表格编辑时弹窗和保存
        const editVisible = ref(false);
        const dialogVisible = ref(false);
        const form = ref({ type: '1', state: '1' });
        const fileIds = ref([]);
        const fileList2 = ref([]);
        const imageUrl = ref("");
        const imageId = ref("");

        let idx = -1;
        const handleEdit = (index, row, type) => {
            editVisible.value = true;

            form.value = { type: '1', state: '1' };
            fileIds.value = [];//图片上传ids 初始化
            fileList2.value = [];//图片上传列表 初始化
            imageUrl.value = "";//图片回显路径 初始化
            imageId.value = "";//图片id 初始化
            if (type) {
                dialogT.value = "新增";
            } else {
                dialogT.value = "编辑";
                idx = index;
                //获取数据信息
                detailsSysMaterial(reactive({ id: row.id })).then((res) => {
                    form.value = res.data;
                    if (res.data.file_id != "" && res.data.file_id != null) {
                        fileIds.value = res.data.file_id.split(",");
                        imageId.value = res.data.file_id;
                    } else {
                        fileIds.value = [];
                        imageId.value = "";
                    }
                    if (res.data.fileList != "" && res.data.fileList != null) {
                        fileList2.value = res.data.fileList;
                        imageUrl.value = res.data.fileList[0].file_url;
                    } else {
                        fileList2.value = [];
                        imageUrl.value = "";
                    }
                });
            }
        };

        return {
            query,
            tableData,
            pageTotal,
            editVisible,
            form,
            fileIds,
            fileList2,
            imageId,
            imageUrl,
            // uploadUrl,
            // imgViewUrl,
            dialogT,
            getData,
            handleSearch,
            handlePageChange,
            handleDelete,
            handleEdit,
            multipleSelection: [],
            dialogImageUrl: "",
            dialogVisible,
            // tableData: [{
            //     state: '启用',
            //     type: '图片',
            //     title: '上海市普陀区金沙江路 1518 弄'
            // }, {
            //     state: '禁用',
            //     type: '视频',
            //     title: '上海市普陀区金沙江路 1517 弄'
            // }],
            fileList: []
        };
    },
    created() {
        let h = document.documentElement.clientHeight || document.body.clientHeight;
        this.tableH = h - 308 + 'px';
    },
    methods: {
        //上传图片操作
        handleSuccess(response, file, fileList) {
            this.fileIds.push(response.id + "");
            this.imageId = response.id + "";
            file.id = response.id;
            this.imageUrl = URL.createObjectURL(file.raw);
            // console.log("imageUrl", this.imageUrl);
            // console.log(response, file, fileList);
        },
        handlePreview(file) {
            // console.log(file);
        },
        handleExceed(files, fileList) {
            this.$message.warning(`只能上传一个文件`);
        },
        // beforeRemove(file, fileList) {
        //     return this.$confirm(`确定移除 ${file.name}？`);
        // },
        beforeAvatarUpload(file) {
            // console.log(this.form.type)
            //图片
            if (this.form.type == "1") {
                const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
                // const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG) {
                    this.$message.error('上传图片只能是 JPG 和 PNG 格式!');
                }
                // if (!isLt2M) {
                //     this.$message.error('上传头像图片大小不能超过 2MB!');
                // }
                // return isJPG && isLt2M;
                return isJPG;
            } else {
                //视频
                // console.log("文件格式")
                // console.log(file.type)
                const isJPG = file.type === 'video/mp4' || file.type === 'video/avi';
                // const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG) {
                    this.$message.error('上传视频只能是 MP4 / AVI 格式!');
                }
                // if (!isLt2M) {
                //     this.$message.error('上传头像图片大小不能超过 2MB!');
                // }
                return isJPG;
            }
        },
        //删除图片
        handleRemove(file, fileList) {
            // console.log("删除图片");
            // console.log(file, fileList);
            let index = this.fileIds.indexOf(file.id + "");
            if (index == -1) {
                index = this.fileIds.indexOf(file.id);
            }
            let flag = 0;
            if (index != -1) {
                delImgFile(reactive({ id: file.id }))
                    .then((res) => {
                        flag = res.success;
                    })
                    .then(() => {
                        if (flag == 0) {
                            ElMessage.success("操作成功");
                            //移除删除id
                            this.fileIds.splice(index, 1);
                        } else if (flag == -1) {
                            ElMessage.error("操作失败!");
                        }
                    });

            }
        },
        //图片预览
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        },
        //图片回显
        imgurl: function (url) {
            if (url != "" && url != null) {
                return url;
            }
        },
        handleSelectionChange(data) {
            this.selectedData = data;
        },
        handleDeleteAll() {
            ElMessageBox.confirm("确定要删除吗？", "提示", {
                type: "warning",
            }).then(() => {
                var that = this;
                var val = this.selectedData;
                // console.log(val);
                var ids = "";
                if (val) {
                    val.forEach(function (item, index) {
                        ids = ids + item.id + ",";
                    });
                    delAll({ ids: ids }).then((res) => {
                        ElMessage.success("删除成功");
                        that.getData();
                    })
                } else {
                    ElMessage.warning(`请选择一条记录`);
                }
            }).catch(() => {
            });
        },
        saveEdit(formName) {
            //保存方法
            let falg = 0;
            let that = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    that.form.fileList = [];
                    if (that.fileIds.length > 0) {
                        that.form.file_id = that.fileIds.join(",");
                    } else {
                        that.form.file_id = "";
                    }

                    that.form.file_id = that.imageId;

                    editSysMaterial(that.form)
                        .then((res) => {
                            falg = res.code;
                        })
                        .then(() => {
                            if (falg == 0) {
                                ElMessage.success("操作成功");
                                that.editVisible = false;
                                that.query.pageIndex = 1;
                                that.form.fileList = [];//图片文件数组
                                that.fileIds = [];//图片上传ids
                                that.imageId = "";
                                that.imageUrl = "";
                                that.getData();
                            } else if (falg == -1) {
                                ElMessage.error("操作失败!");
                            }
                        });

                }
            });
        },
    },
};
</script>
<style scoped>
::v-deep .el-upload--text {
    width: auto;
    height: auto;
    border: none;
    border-radius: 0;
}
</style>
