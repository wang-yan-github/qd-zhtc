<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-button v-permission="['road_movepic_add', 'park_movepic_add']" type="primary" size="small"
                        icon="el-icon-plus" @click="handleEdit(0, null, true, null)">添加
                    </el-button>
                    <el-button v-permission="['road_movepic_deleteAll', 'park_movepic_deleteAll']" type="danger" size="small"
                        icon="el-icon-delete" @click="handleDeleteAll()">批量删除
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
                <el-table-column label="图片(查看大图)" width="200" align="center">
                    <template #default="scope">
                        <el-image class="table-td-thumb" :src="imgurl(scope.row.pic_url)" hide-on-click-modal="true" preview-teleported="true"
                            :preview-src-list="[imgurl(scope.row.pic_url)]">
                        </el-image>
                    </template>
                </el-table-column>
                <el-table-column prop="title" label="标题" align="center"></el-table-column>
                <el-table-column prop="sort" width="100" label="排序" align="center"></el-table-column>
                <!--<el-table-column label="类别" width="200" align="center">-->
                <!--<template #default="scope">-->
                <!--<p v-if="scope.row.category == '0'">文档</p>-->
                <!--<p v-if="scope.row.category == '1'">操作指南</p>-->
                <!--<p v-if="scope.row.category == '2'">常见问题</p>-->
                <!--<p v-if="scope.row.category == '3'">轮播图</p>-->
                <!--<p v-if="scope.row.category == '4'">公告资讯</p>-->
                <!--</template>-->
                <!--</el-table-column>-->
                <el-table-column label="创建时间" prop="create_time" width="180" align="center">
                </el-table-column>

                <el-table-column prop="update_time" label="更新时间" align="center" width="180"></el-table-column>
                <!-- <el-table-column label="启用" align="center" width="100">
                  <template #default="scope">
                    <el-tag
                      size="small"
                      :type="
                        scope.row.is_del === '启用'
                          ? 'success'
                          : scope.row.is_del === '禁用'
                          ? 'danger'
                          : ''
                      "
                      >{{ scope.row.state }}</el-tag
                    >
                  </template>
                </el-table-column>
                <el-table-column label="推送" align="center" width="100">
                  <template #default="scope">
                    <el-tag
                      size="small"
                      :type="
                        scope.row.state === '启用'
                          ? 'success'
                          : scope.row.state === '禁用'
                          ? 'danger'
                          : ''
                      "
                      >{{ scope.row.state }}</el-tag
                    >
                  </template>
                </el-table-column> -->
                <el-table-column label="操作" width="220" align="center">
                    <template #default="scope">
                        <!-- <el-button
                          size="mini"
                          type="text"
                          icon="el-icon-circle-close"
                          @click="handleEdit(scope.$index, scope.row, false)"
                          >禁用
                        </el-button> -->
                        <el-button v-permission="['park_movepic_edit', 'road_movepic_edit']" size="mini" type="text"
                            icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row, false)">编辑
                        </el-button>

                        <el-button v-permission="['road_movepic_delete', 'park_movepic_delete']" size="mini" type="text"
                            icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除
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
        <el-dialog :title="dialogT" v-model="editVisible" width="35%" top="2vh">
            <el-form label-width="70px" size="small" :rules="formRules" ref="formId" label-position="top" :model="form">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入标题"></el-input>
                </el-form-item>

                <el-form-item label="封面">
                    <!-- limit 上传文件数量 -->
                    <!--<el-upload-->
                    <!--:action="uploadUrl"-->
                    <!--list-type="picture-card"-->
                    <!--limit="1"-->
                    <!--:on-preview="handlePictureCardPreview"-->
                    <!--:on-remove="handleRemove"-->
                    <!--:on-success="handleSuccess"-->
                    <!--:file-list="fileList2"-->
                    <!--&gt;-->
                    <!--<i class="el-icon-plus"></i>-->
                    <!--</el-upload>-->

                    <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                        :on-preview="handlePictureCardPreview" :on-remove="handleRemove" :on-success="handleSuccess">
                        <img v-if="imageUrl" :src="imageUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>

                <!--<el-form-item label="文档类别" prop="doc_type">-->
                <!--<el-input v-model="form.doc_type"></el-input>-->
                <!--</el-form-item>-->
                <!--<el-form-item label="摘要" prop="resource_abstract">-->
                <!--<el-input-->
                <!--type="textarea"-->
                <!--:rows="4"-->
                <!--v-model="form.resource_abstract"-->
                <!--&gt;</el-input>-->
                <!--</el-form-item>-->
                <!--<el-form-item label="类型" prop="category">-->
                <!--<el-select v-model="form.category" placeholder="请选择" class="w">-->
                <!--<el-option key="0" label="文档" value="0"></el-option>-->
                <!--<el-option key="1" label="操作指南" value="1"></el-option>-->
                <!--<el-option key="2" label="常见问题" value="2"></el-option>-->
                <!--<el-option key="3" label="轮播图" value="3"></el-option>-->
                <!--<el-option key="4" label="公告资讯" value="4"></el-option>-->
                <!--</el-select>-->
                <!--</el-form-item>-->
                <el-form-item label="排序" prop="sort">
                    <el-input v-model="form.sort" placeholder="请输入排序" oninput="value=value.replace(/[^\d]/g,'')"></el-input>
                </el-form-item>
                <el-form-item label="详情">
                    <Ueditor @ready="editorReady" ref="ue" :value="form.content" :ueditorConfig="uform.config"></Ueditor>
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
<style>
.customWidth {
    padding: 20px;
}

.imgWidth {
    vertical-align: middle;
    display: inline-block;
    width: 100%;
}

.el-upload--text {
    width: 178px;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    /*width: 100%;*/
    /*height: 100%;*/
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    /*width: 100%;*/
    /*height: 100%;*/
    width: 178px;
    height: 178px;
    display: block;
}
</style>
<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
// import { fetchData } from "../../api/index";
import Ueditor from "../../components/UE.vue";
import { delAll, delImgFile, detailsSysResource, editSysResource, sysResourceList, } from "../../api/sysResourceIndex.js";

import File_URL from '../../file_url';

export default {
    name: "movePicList",
    components: {
        Ueditor,
    },
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
            category: "3",//轮播图
            pageIndex: 1,
            pageSize: 15,
        });
        const tableData = ref([]);
        const pageTotal = ref(0);
        // 获取表格数据
        const getData = () => {
            sysResourceList(query).then((res) => {
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
                    editSysResource(edit_resource).then((res) => {
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
        const form = ref({});
        const fileIds = ref([]);
        const fileList2 = ref([]);
        const imageUrl = ref("");
        const imageId = ref("");

        // //上传图片方法路径
        // const uploadUrl = "http://127.0.0.1:8080/fileManage/imgFileSave.json";
        // //图片回显方法路径
        // // const imgViewUrl = "http://127.0.0.1:8080/fileManage/getImgView.do?id=";
        // const imgViewUrl = "http://192.168.0.93:82/";

        //文本编辑器初始化样式
        let uform = reactive({
            config: { initialFrameHeight: 300, initialFrameWidth: "100%" },
        });

        let idx = -1;
        const handleEdit = (index, row, type) => {
            editVisible.value = true;

            form.value = {};
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
                detailsSysResource(reactive({ id: row.id })).then((res) => {
                    form.value = res.data;
                    if (res.data.picture_id != "" && res.data.picture_id != null) {
                        fileIds.value = res.data.picture_id.split(",");
                        imageId.value = res.data.picture_id;
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
            uform,
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
            console.log("imageUrl", this.imageUrl);
            console.log(response, file, fileList);
        },
        //删除图片
        handleRemove(file, fileList) {
            console.log("删除图片");
            console.log(file, fileList);
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
                console.log(val);
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
        editorReady(instance) {
            this.$refs.ue.setText(this.form.content);
        },
        saveEdit(formName) {
            //保存方法
            let falg = 0;
            let that = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //获取文本编辑器内容
                    that.form.content = this.$refs.ue.getUEContent();
                    if (that.form.content == "" || that.form.content == null || that.form.content == undefined) {
                        ElMessage.error("请填写内容!");
                    } else {
                        //类型为 3 轮播图
                        that.form.category = "3";
                        that.form.fileList = [];
                        if (that.fileIds.length > 0) {
                            that.form.picture_id = that.fileIds.join(",");
                        } else {
                            that.form.picture_id = "";
                        }

                        that.form.picture_id = that.imageId;

                        editSysResource(that.form)
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
                                    this.$refs.ue.setText("");//文本编辑器内容初始化
                                    that.getData();
                                } else if (falg == -1) {
                                    ElMessage.error("操作失败!");
                                }
                            });
                    }
                }
            });
        },
    },
};
</script>
