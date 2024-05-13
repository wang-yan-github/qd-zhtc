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
                        v-permission="'park_passlist_add'"
                        >添加
                    </el-button>
                </div>
                <div class="right-panel">
                    <el-select
                        v-model="query.park_id"
                        filterable
                        size="small"
                        placeholder="所有停车场"
                        class="w100 mr10"
                        clearable
                    >
                        <el-option
                            v-for="(item, i) in result.park_list"
                            :key="i"
                            :label="item.park_name"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                    <!-- <el-input
                        @keyup.enter="handleSearch()"
                        size="small"
                        v-model="query.companyName"
                        placeholder="公司名称"
                        class="handle-input mr10"
                    ></el-input> -->

                    <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询 </el-button>
                </div>
                <div class="clear"></div>
            </div>
            <el-table
                :data="tableData"
                border
                class="table"
                :max-height="tableH"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
            >
                <el-table-column type="index" label="序号" width="55" align="center"> </el-table-column>
                <el-table-column prop="park_name" label="停车场名称" align="center"> </el-table-column>

                <el-table-column prop="address" label="停车场地址" align="center"></el-table-column>
                <el-table-column prop="hxCount" label="剩余使用次数" align="center"></el-table-column>
                <el-table-column prop="limit_time" label="使用限制" align="center">
                    <template #default="scope">
                        {{ scope.row.type == 2 && scope.row.repeat_type == 1 ? '同车牌使用一次' : '无' }}
                    </template>
                </el-table-column>
                <el-table-column prop="limit_time" label="限制离场时间(分钟)" align="center"> </el-table-column>
                <el-table-column prop="create_time" label="添加时间" align="center"></el-table-column>
                <el-table-column prop="expire_time" label="失效时间" align="center"></el-table-column>

                <!-- <el-table-column prop="expire_time" label="备注" align="center"></el-table-column> -->

                <el-table-column label="状态" align="center" width="100">
                    <template #default="scope">
                        <el-tag
                            size="small"
                            :type="scope.row.status === '1' ? 'success' : scope.row.status === '0' ? 'danger' : ''"
                            v-if="scope.row.status == 1"
                            >有效
                        </el-tag>
                        <el-tag
                            size="small"
                            :type="scope.row.status === '1' ? 'success' : scope.row.status === '0' ? 'danger' : ''"
                            v-else
                            >失效
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="320" align="center">
                    <template #default="scope">
                        <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.$index, scope.row, false)"
                            v-permission="'park_passlist_edit'"
                            >编辑
                        </el-button>

                        <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-view"
                            @click="handleView(scope.$index, scope.row)"
                            v-permission="'park_passlist_qrcode'"
                            >通行二维码
                        </el-button>
                        <!--<el-button-->
                        <!--size="mini"-->
                        <!--type="text"-->
                        <!--icon="el-icon-circle-check"-->
                        <!--@click="handleStop(scope.$index, scope.row, '1')"-->
                        <!--v-if="scope.row.is_use === '0'"-->
                        <!--v-permission="'park_passlist_status'"-->
                        <!--&gt;启用-->
                        <!--</el-button>-->
                        <!--<el-button-->
                        <!--size="mini"-->
                        <!--type="text"-->
                        <!--icon="el-icon-circle-close"-->
                        <!--@click="handleStop(scope.$index, scope.row, '0')"-->
                        <!--v-if="scope.row.is_use === '1'"-->
                        <!--class="red"-->
                        <!--v-permission="'park_passlist_status'"-->
                        <!--&gt;停用-->
                        <!--</el-button>-->
                        <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            class="red"
                            @click="handleDelete(scope.$index, scope.row)"
                            v-permission="'park_passlist_delete'"
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

        <!-- 新增弹出框 -->
        <el-dialog :title="result.dialogT" v-model="editVisible" width="30%">
            <el-form label-width="110px" :rules="rules" :model="form" ref="elForm">
                <el-form-item label="可用停车场" prop="park_id">
                    <el-select v-model="form.park_id" placeholder="请选择" class="w">
                        <el-option
                            v-for="(item, i) in result.park_list"
                            :key="i"
                            :label="item.park_name"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <!-- <el-form-item label="公司名称" prop="companyName">
                    <el-input v-model="form.companyName" placeholder="请输入公司名称"></el-input>
                </el-form-item> -->
                <el-form-item label="失效日期" prop="expire_time">
                    <el-date-picker
                        style="width: 100%"
                        v-model="form.expire_time"
                        placeholder="请选择失效时间"
                        type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss"
                    />
                </el-form-item>
                <el-form-item label="可以使用次数" prop="hxCount">
                    <div>
                        <el-radio-group v-model="form.type" style="margin-right: 50px" @change="handlerTypeChange">
                            <el-radio label="1">单次核销</el-radio>
                            <el-radio label="2">多次核销</el-radio>
                        </el-radio-group>
                        <el-input-number
                            placeholder="请输入次数"
                            v-model="form.hxCount"
                            :disabled="form.type == 1"
                            :min="1"
                            :max="99"
                        />
                    </div>
                </el-form-item>
                <el-form-item label="使用限制" prop="repeat_type" v-if="form.type == 2">
                    <el-checkbox v-model="form.repeat_type">同一车牌仅可使用一次</el-checkbox>
                </el-form-item>
                <el-form-item label="限时离场时间" prop="limit_time">
                    <el-input v-model="form.limit_time" placeholder="请输入限时离场时间">
                        <template #append>分钟</template>
                    </el-input>
                </el-form-item>
            </el-form>

            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="handleCancel">取 消</el-button>
                    <el-button type="primary" @click="saveEdit()">确 定</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 详情弹出框 -->
        <el-dialog title="优惠二维码" v-model="viewVisible" width="460px">
            <div style="margin-bottom: 20px">
                <h4 style="margin-bottom: 10px">{{ result.park_name }}</h4>
                <span>失效时间: {{ result.expire_time }}</span>
            </div>
            <div style="margin: 0 auto 30px;height: 250px;overflow: hidden;">
                <img style="width: 100%" :src="'data:image/png;base64,' + result.qrCode" alt="" />
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="viewVisible = false">关 闭</el-button>
                    <el-button type="primary" @click="printImg()">下 载</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
    provisionalPassList,
    provisionalPassAdd,
    dictData,
    provisionalPassEdit,
    passQrCode,
    passImg,
    delAreaAll,
    queryParkData
} from '../../../api/index.js'

export default {
    name: 'provisionalPasslist',
    data() {
        return {
            tableH: 0
        }
    },
    setup() {
        const query = reactive({
            pageIndex: 1,
            pageSize: 15
        })

        let result = reactive({
            park_list: [],
            dialogT: '',
            qrCode: '',
            park_name: '',
            expire_time: ''
        })

        const tableData = ref([])
        const pageTotal = ref(0)
        // 获取表格数据
        const getData = () => {
            provisionalPassList(query).then((res) => {
                console.log(res.data)
                tableData.value = res.data.list
                pageTotal.value = res.data.total
            })
        }
        getData()

        const getParkList = () => {
            queryParkData(reactive({})).then((res) => {
                result.park_list = res.data
            })
        }
        getParkList()

        // 查询操作
        const handleSearch = () => {
            query.pageIndex = 1
            getData()
        }
        // 分页导航
        const handlePageChange = (val) => {
            query.pageIndex = val
            getData()
        }

        const printImg = () => {
            let sss = reactive({
                base64: JSON.stringify(result.qrCode)
            })
            console.log(sss.base64)

            passImg(
                reactive({
                    base64: JSON.stringify(result.qrCode)
                })
            ).then((res) => {
                const url = window.URL.createObjectURL(new Blob([res]))
                const link = document.createElement('a')
                link.href = url
                link.setAttribute('download', '临时通行证.jpg')
                document.body.appendChild(link)
                link.click()
            })
        }

        const handlerTypeChange = (e) => {
            if (e == 1) {
                form.hxCount = 1
                form.repeat_type = false
            }
        }

        // 删除操作
        const handleDelete = (index, row) => {
            // 二次确认删除
            ElMessageBox.confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    let edit_pass = reactive({
                        is_del: '1',
                        id: row.id
                    })
                    provisionalPassEdit(edit_pass)
                        .then((res) => {
                            console.log(res)
                            ElMessage.success('删除成功')
                        })
                        .then(() => {
                            getData()
                        })
                })
                .catch(() => {})
        }

        // 停用操作
        const handleStop = (index, row, sta) => {
            // 二次确认停用
            ElMessageBox.confirm('请确定当前操作？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    let edit_pass = reactive({
                        is_use: sta,
                        id: row.id
                    })
                    provisionalPassEdit(edit_pass)
                        .then((res) => {
                            console.log(res)
                            ElMessage.success('操作成功')
                        })
                        .then(() => {
                            getData()
                        })
                })
                .catch(() => {})
        }

        // 表格编辑时弹窗和保存
        const editVisible = ref(false)
        const viewVisible = ref(false)
        const elForm = ref()
        const rules = reactive({
            expire_time: [{ required: true, message: '必填项', trigger: 'blur' }],
            park_id: [{ required: true, message: '必填项', trigger: 'blur' }],
            hxCount: [
                { required: true, message: '必填项', trigger: 'blur' },
                { type: 'number', message: '必填项', min: 1 }
            ],
            limit_time: [{ required: true, message: '必填项', trigger: 'blur' }]
        })

        let form = reactive({
            park_id: '',
            expire_time: '',
            type: '1',
            repeat_type: false,
            limit_time: '',
            hxCount: 1,
            is_del: 0,
            status: '',
            is_use: 1
        })

        const clearFrom = () => {
            form.park_id = ''
            form.expire_time = ''
            form.type = '1'
            form.repeat_type = false
            form.limit_time = ''
            form.hxCount = 1
        }

        const handleCancel = () => {
            elForm.value.resetFields()
            clearFrom()
            editVisible.value = false
        }

        //二维码功能
        const handleView = (index, row) => {
            let pass_qrcode = reactive({
                id: row.id,
                park_name: row.park_name,
                hxCount: row.hxCount,
                companyName: row.companyName
            })
            passQrCode(pass_qrcode).then((res) => {
                result.qrCode = res.data
                result.park_name = row.park_name
                result.expire_time = row.expire_time
            })
            viewVisible.value = true
        }

        let idx = ''

        const handleEdit = (index, row, type) => {
            // 获取区域信息

            if (type) {
                result.dialogT = '新增'
                idx = ''
                form.park_id = ''
                form.hxCount = 1
            } else {
                result.dialogT = '编辑'
                idx = row.id
                Object.keys(form).forEach((item) => {
                    form[item] = row[item]
                })
                form.repeat_type = row.repeat_type == 1
            }

            editVisible.value = true
        }

        const saveEdit = () => {
            // 不可为空
            // if (
            //     form.companyName == null ||
            //     form.companyName == '' ||
            //     form.park_id == null ||
            //     form.park_id == '' ||
            //     form.hxCount == null
            // ) {
            //     ElMessage.error('参数不可为空！')
            //     return false
            // }

            elForm.value.validate((valid) => {
                if (valid) {
                    // 新增
                    if (!idx) {
                        provisionalPassAdd({ ...form, repeat_type: form.repeat_type ? 1 : 0 })
                            .then((res) => {
                                console.log(res.data)
                            })
                            .then(() => {
                                query.pageIndex = 1
                                elForm.value.resetFields()
                                editVisible.value = false
                                ElMessage.success('添加成功')
                                getData()
                            })
                    } else {
                        // 编辑
                        let edit_pass = reactive({
                            id: idx,
                            ...form,
                            repeat_type: form.repeat_type ? 1 : 0
                        })
                        provisionalPassEdit(edit_pass)
                            .then((res) => {
                                console.log(res)
                                ElMessage.success('修改成功')
                                elForm.value.resetFields()
                                editVisible.value = false
                            })
                            .then(() => {
                                getData()
                            })
                    }
                }
            })
        }

        return {
            query,
            tableData,
            pageTotal,
            editVisible,
            result,
            form,
            rules,
            viewVisible,
            elForm,
            printImg,
            handleView,
            handleSearch,
            handlePageChange,
            getData,
            handleDelete,
            handleEdit,
            handleStop,
            saveEdit,
            handlerTypeChange,
            multipleSelection: [],
            handleCancel,
            value: true,
            activeName: 'first'
        }
    },
    methods: {
        handleSelectionChange(data) {
            this.selectedData = data
        },
        handleDeleteAll() {
            ElMessageBox.confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    var that = this
                    var val = this.selectedData
                    var ids = ''
                    if (val) {
                        val.forEach(function (item, index) {
                            //alert(item.id);
                            ids = ids + item.id + ','
                        })
                        delAreaAll({ areaIds: ids }).then((res) => {
                            ElMessage.success('删除成功')
                            that.getData()
                        })
                    } else {
                        ElMessage.warning(`请选择一条记录`)
                    }
                })
                .catch(() => {})
        }
    },
    mounted() {
        // this.getData();
        //this.initMap();
    },
    created() {
        let h = document.documentElement.clientHeight || document.body.clientHeight
        this.tableH = h - 308 + 'px'
    }
}
</script>
