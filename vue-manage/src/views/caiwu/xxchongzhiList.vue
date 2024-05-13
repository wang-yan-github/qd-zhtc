<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-button
                            type="primary"
                            size="small"
                            icon="el-icon-plus"
                            @click="handleRecharge()"
                            v-permission="['road_xxchongzhilist_cz', 'park_xxchongzhilist_cz']"
                    >充值
                    </el-button>
                </div>
                <div class="right-panel">
                    <el-form inline size="small">
                        <el-input
                                size="small"
                                v-model="query.str"
                                @keyup.enter="handleSearch()"
                                placeholder="支持手机号查询"
                                class="handle-input mr10"
                        ></el-input>
                        <span class="dispinline ml5 font14 color666">充值时间：</span>
                        <el-date-picker
                                size="small"
                                v-model="form.time"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                class="datepicker"
                                @change="getQueryDate"
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
                        <el-button size="small" type="success" icon="el-icon-upload2" @click="exportExcel"
                        v-permission="['road_xxchongzhilist_excel', 'park_xxchongzhilist_excel']"
                        >导出
                        </el-button
                        >

                    </el-form>
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
                <!-- <el-table-column
                  type="selection"
                  width="55"
                  align="center"
                ></el-table-column> -->

                <el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
                <!-- <el-table-column pro="ID" label="ID" width="55" align="center">
                  <template #default="scope"> {{ scope.row.id }} </template> </el-table-column>-->
                <el-table-column prop="nickName" label="微信昵称" align="center"></el-table-column>

                <el-table-column prop="phone" label="账号" width="200" align="center"></el-table-column>

                <el-table-column prop="recharge_amount" label="充值金额" align="center" width="180"></el-table-column>

                <el-table-column prop="payment_type" label="类型" width="150" align="center"></el-table-column>

                <el-table-column prop="recharge_time" label="充值时间" width="200" align="center"></el-table-column>

                <el-table-column prop="paymentNo" label="交易号" width="200" align="center"></el-table-column>

                <el-table-column label="操作" width="120" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-view"
                                @click="handleView(scope.$index, scope.row)"
                                v-permission="['road_xxchongzhilist_details', 'park_xxchongzhilist_details']"
                        >查看
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        background
                        layout="total, prev, pager, next"
                        :current-page="query.pageNum"
                        :page-size="query.pageSize"
                        :total="pageTotal"
                        @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>

        <!-- 编辑弹出框 -->
        <!--<el-dialog title="修改金额" v-model="editVisible" width="20%">-->
        <!--<el-form>-->
        <!--<el-form-item>-->
        <!--<el-input placeholder="输入金额" v-model="form.recharge_amount"-->
        <!--oninput="if(isNaN(value)) { value = null } if(value.indexOf('.')>0){value=value.slice(0,value.indexOf('.')+3)}"></el-input>-->
        <!--</el-form-item>-->
        <!--</el-form>-->
        <!--<template #footer>-->
        <!--<span class="dialog-footer">-->
        <!--<el-button @click="editVisible = false">取 消</el-button>-->
        <!--<el-button type="primary" @click="saveEdit">确 定</el-button>-->
        <!--</span>-->
        <!--</template>-->
        <!--</el-dialog>-->

        <!--详情弹出框 -->
        <el-dialog title="充值详情" v-model="viewVisible" width="46%">
            <el-descriptions
                    class="margin-top handle-box"
                    title=""
                    :column="2"
                    :size="size"
                    border
            >
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-user"></i>
                        微信昵称
                    </template>
                    {{rechargeManagement.nickName}}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-postcard"></i>
                        账号
                    </template>
                    {{rechargeManagement.phone}}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-bank-card"></i>
                        充值金额
                    </template>
                    {{rechargeManagement.recharge_amount}}元
                </el-descriptions-item>

                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-price-tag"></i>
                        类型
                    </template>
                    {{rechargeManagement.typeName}}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-time"></i>
                        充值时间
                    </template>
                    {{rechargeManagement.recharge_time}}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-money"></i>
                        交易号
                    </template>
                    {{rechargeManagement.paymentNo}}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-picture-outline"></i>
                        照片
                    </template>
                    <template v-for="(item, i) in rechargeManagement.fileList" :key="i">
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

            </el-descriptions>

            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="viewVisible = false">取 消</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 充值弹出框 -->
        <el-dialog title="充值" v-model="rechargeVisible" width="1000px">
            <div class="main-box">
                <el-form label-width="100px" :inline="true" class="demo-form-inline" size="small">
                    <el-form-item label="查询手机号">
                        <el-input v-model="phone" placeholder="手机号"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="searchWxUserList()">查询</el-button>
                    </el-form-item>
                </el-form>

                <el-empty description="暂无数据，请输入正确手机号查询！" v-if="wxUserList.length==0"></el-empty>

                <!--可循环此描述列-->
                <el-descriptions column="4" v-for="(item, index) in wxUserList" :key="index">
                    <el-descriptions-item>
                        <template #label>
                            <i class="el-icon-user"></i>
                            微信昵称：
                        </template>
                        {{item.nick_name}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <i class="el-icon-document"></i>
                            openID：
                        </template>
                        {{item.openid}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <i class="el-icon-bank-card"></i>
                            账号余额：
                        </template>
                        {{item.balance}}
                    </el-descriptions-item>
                    <el-descriptions-item label="">
                        <el-button type="success" @click="goCz(item.id)" size="small">充 值</el-button>
                    </el-descriptions-item>
                </el-descriptions>

                <div class="money-box" v-show="moneyCz">
                    <p class="money-title">线下充值</p>
                    <el-form label-width="80px" :model="form" ref="priceform" size="small">
                        <!-- 隐藏一个input -->
                        <el-form-item label="金额" prop="valueTest">
                            <el-input v-model="price" style="width: 300px;" oninput="value=value.replace(/[^\d.]/g,'')">
                                <template #append>元</template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="pic">
                            <el-upload
                                    :action="fileurl"
                                    list-type="picture-card"
                                    ref="upload"
                                    limit="5"
                                    :on-preview="handlePictureCardPreview"
                                    :on-remove="handleRemove"
                                    :on-success="handleSuccess"
                                    :file-list="fileList"
                            >
                                <i class="el-icon-plus"></i>
                            </el-upload>
                            上传凭证图片（可选）
                        </el-form-item>

                        <!--<el-form-item label="凭证" prop="">-->
                        <!--<el-upload name="portrait" ref="upload" :class="{ disabled: uploadDisabled }"-->
                        <!--action="http://192.168.43.13/users/"-->
                        <!--accept=".jpg,.jpeg,.png,.gif,.bmp,.JPG,.JPEG,.PBG,.GIF,.BMP"-->
                        <!--:auto-upload="false" :headers="headerObj" :on-remove="handleRemove"-->
                        <!--:on-success="handleAvatarSuccess"-->
                        <!--:on-change="handleChange" :before-upload="beforeAvatarUpload"-->
                        <!--list-type="picture-card" :limit="1">-->
                        <!--<img v-if="imageUrl" :src="imageUrl" class="avatar" title="点击修改头像">-->
                        <!--<i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
                        <!--</el-upload>-->
                        <!--</el-form-item>-->
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit">立即充值</el-button>
                        </el-form-item>


                    </el-form>
                </div>

            </div>

            <template #footer>
        <span class="dialog-footer">
          <el-button @click="rechargeVisible = false">取 消</el-button>
        </span>
            </template>
        </el-dialog>

        <el-dialog v-model="dialogVisible">
            <div class="customWidth">
                <img class="imgWidth" :src="dialogImageUrl" alt=""/>
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
</style>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import {
        chongzhijiluList,
        chongzhijlUP,
        exportChongzhijilu,
        getmemberManageList,
        rechargemanagementSave
    } from "../../api/index";
    import File_URL from '../../file_url'
    import {delImgFile} from "../../api/sysResourceIndex.js";

    export default {
        name: "xxchongzhilist",
        components: {},
        data() {
            return {
                tableH: 0,
                valueTest: '',
                imageUrl: '',
                // 当前用户的真实姓名
                realname: '',
                // 当前用户的电话
                telphone: '',
                // 设置上传的请求头部
                headerObj: {
                    Authorization: 'Token' + ' ' + window.sessionStorage.getItem('token')
                },
                // 控制隐藏上传头像+号
                uploadDisabled: false,
                moneyCz: false,
                fileurl: File_URL.file_img_url,
                member_id: "",
                rechargeVisible: false,

            };
        },
        setup() {

            const query = reactive({
                startTime: '',
                endTime: '',
                pageNum: 1,
                pageSize: 15,
                str: '',
                payment_type: '5',
            });


            const tableData = ref([]);
            const pageTotal = ref(0);
            // 获取表格数据
            const getData = () => {
                chongzhijiluList(query).then((res) => {
                    var data = res.data;
                    tableData.value = data.list;
                    pageTotal.value = data.total;
                });
            };
            getData();

            // 查询操作
            const handleSearch = () => {
                query.pageNum = 1;
                getData();
            };
            //导出excel
            const exportExcel = () => {
                exportChongzhijilu(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]))
                    const link = document.createElement('a')
                    link.href = url
                    link.setAttribute('download', '充值记录.xlsx')
                    document.body.appendChild(link)
                    link.click()
                })
            }
            // 分页导航
            const handlePageChange = (val) => {
                query.pageNum = val;
                getData();
            };

            // // 删除操作
            // const handleDelete = (index) => {
            //     // 二次确认删除
            //     ElMessageBox.confirm("确定要删除吗？", "提示", {
            //         type: "warning",
            //     })
            //         .then(() => {
            //             ElMessage.success("删除成功");
            //             tableData.value.splice(index, 1);
            //         })
            //         .catch(() => {
            //         });
            // };
            const dialogT = "编辑";

            // 表格编辑时弹窗和保存
            const editVisible = ref(false);
            const viewVisible = ref(false);
            const ppVisible = ref(false);
            let form = reactive({
                id: "",
                recharge_amount: "",
            });
            let idx = -1;
            // const handleEdit = (index, row, type) => {
            //     if (type) {
            //         //dialogT='新增'
            //     } else {
            //         //dialogT='编辑';
            //         idx = index;
            //         console.log(row.id);
            //         Object.keys(form).forEach((item) => {
            //             form.id = row.id;
            //             form.recharge_amount = row.recharge_amount;
            //         });
            //     }
            //
            //     editVisible.value = true;
            // };

            let rechargeManagement = reactive({
                id: "",
                paymentNo: "",
                phone: "",
                paymentType: "",
                nickName: "",
                typeName: "",
                czMoney: "",
                amount: "",
                payment_type: "",
                recharge_time: "",
                recharge_amount: "",
                fileList: [],
            });

            const imgurl = (url) => {
                if (url != "" && url != null) {
                    return url;
                }
            };
            //详情
            const handleView = (index, row) => {
                idx = index;
                Object.keys(rechargeManagement).forEach((item) => {
                    rechargeManagement[item] = row[item];
                });
                viewVisible.value = true;
            };

            // const saveEdit = () => {
            //     editVisible.value = false;
            //     chongzhijlUP(form).then((res) => {
            //         editVisible.value = false;
            //         if (res.code == 0)
            //             ElMessage.success(res.logMsg);
            //         else
            //             ElMessage.error(res.logMsg);
            //         getData();
            //     })
            // };

            //日期控件change方法
            const getQueryDate = () => {
                console.log(form.time);
                if (form.time != null && form.time != undefined && form.time.length > 0) {
                    query.startTime = dataFormat(form.time[0]);
                    query.endTime = dataFormat(form.time[1]);
                } else {
                    query.startTime = "";
                    query.endTime = "";
                }

            }
            //日期格式化 yyyy-MM-dd
            const dataFormat = (time) => {

                return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()}`;
            }
            //日期格式化 yyyy-MM-dd HH:mm:ss
            const dateFormat = (time) => {
                return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()} ${time.getHours() >= 10 ? time.getHours() : '0' + time.getHours()} : ${time.getMinutes() >= 10 ? time.getMinutes() : '0' + time.getMinutes()} : ${time.getSeconds() >= 10 ? time.getSeconds() : '0' + time.getSeconds()}`;
            }


            const phone = ref("");
            const wxUserList = ref([]);
            const price = ref(0);
            // 线下充值
            const rechargeVisible = ref(false);
            const moneyCz = ref(false);
            const fileList = ref([]);
            const fileIds = ref([]);
            const member_id = ref("");
            const dialogVisible = ref(false);

            // //打卡充值弹窗页面
            // const handleRecharge = () => {
            //     rechargeVisible.value = true;
            //     moneyCz.value = false;
            //     phone.value = "";
            //     fileList.value = [];
            //     fileIds.value = [];
            //     member_id.value = "";
            //     price.value = 0;
            //     wxUserList.value = [];
            //     console.log(fileIds.value)
            //     console.log(fileList.value)
            // };

            //根据手机号码查询车主列表信息
            const searchWxUserList = () => {
                if (phone.value != null && phone.value != undefined && phone.value != "") {
                    getmemberManageList(reactive({phone: phone.value})).then((res) => {
                        console.log(res)
                        wxUserList.value = res.data
                    })
                } else {
                    ElMessage.error("请输入手机号");
                }
            };

            //充值提交
            // const onSubmit = () => {
            //     if (price.value != null && price.value != undefined && price.value != "" && price.value > 0) {
            //         var flag = 0;
            //         var msg = "";
            //
            //         let form2 = ref({
            //             fileIds: [],
            //             recharge_amount: price.value,
            //             member_id: member_id.value,
            //         });
            //         fileList.value = [];
            //
            //         if (fileIds.value.length > 0) {
            //             form2.value.fileIds = fileIds.value.join(",");
            //         } else {
            //             form2.value.fileIds = "";
            //         }
            //         rechargemanagementSave(form2.value).then((res) => {
            //             flag = res.code;
            //             msg = res.msg;
            //         }).then(() => {
            //             if (flag == 0) {
            //                 ElMessage.success(msg);
            //                 fileList.value = []; //图片文件数组
            //                 fileIds.value = []; //图片上传ids
            //                 member_id.value = "";
            //                 price.value = "";
            //                 phone.value = "";
            //                 rechargeVisible.value = false;
            //                 moneyCz.value = true;
            //                 wxUserList.value = [];
            //                 getData();
            //             } else if (flag == -1) {
            //                 ElMessage.error(msg);
            //             }
            //         });
            //     } else {
            //         ElMessage.error("请输入金额");
            //         return false;
            //     }
            // }

            return {
                query,
                getQueryDate,
                tableData,
                pageTotal,
                editVisible,
                viewVisible,
                form,
                dialogT,
                handleSearch,
                handlePageChange,
                // handleDelete,
                // handleEdit,
                handleView,
                // saveEdit,
                multipleSelection: [],
                dialogImageUrl: "",
                ppVisible: false,
                exportExcel,
                rechargeVisible,
                // handleRecharge,
                phone,
                searchWxUserList,
                wxUserList,
                price,
                fileList,
                fileIds,
                member_id,
                // onSubmit,
                dialogVisible,
                moneyCz,
                imgurl,
                getData,
                rechargeManagement
            };
        },
        methods: {
            //图片预览
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.ppVisible = true;
            },
            goCz(id) {//显示充值页面
                this.moneyCz = true;
                this.member_id = id;
                this.fileList = [];
                this.fileIds = [];
                this.price = 0;
                this.$refs.upload.clearFiles();
                console.log(this.fileList)
                console.log(this.fileIds)
            },
            handleRecharge() { //打卡充值弹窗页面
                this.rechargeVisible = true;
                this.moneyCz = false;
                this.phone = "";
                this.fileList = [];
                this.fileIds = [];
                this.member_id = "";
                this.price = 0;
                this.wxUserList = [];
            },
            //上传图片操作
            handleSuccess(response, file, fileList) {
                this.fileIds.push(response.id + "");
                file.id = response.id;
            },
            //删除图片
            handleRemove(file, fileList) {

                let index = this.fileIds.indexOf(file.id + "");
                if (index == -1) {
                    index = this.fileIds.indexOf(file.id);
                }
                let flag = 0;
                if (index != -1) {
                    delImgFile(reactive({id: file.id}))
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
            handleCommand(command) {
                this.$message("click on item " + command);
            },
            handleDeleteAll() {
                var that = this;
                var val = this.selectedData;
                if (val) {
                    val.forEach(function (item, index) {
                        that.tableData.forEach(function (itemI, indexI) {
                            if (item === itemI) {
                                // ElMessageBox.confirm("确定要删除吗？", "提示", {
                                //   type: "warning",
                                // })
                                //   .then(() => {
                                //     ElMessage.success("删除成功");
                                //     that.tableData.splice(indexI, 1);
                                //   })
                                //   .catch(() => {
                                //     ElMessage.info("取消删除");
                                //   });
                                that.tableData.splice(indexI, 1);
                            }
                        });
                    });
                    ElMessage.success("删除成功");
                    this.$refs.multipleTable.clearSelection();
                } else {
                    ElMessage.warning(`请选择一条记录`);
                }
            },
            onSubmit() {
                console.log("充值保存")

                var that = this;
                if (that.price != null && that.price != undefined && that.price != "" && that.price > 0) {
                    var flag = 0;
                    var msg = "";

                    let form2 = {
                        fileIds: [],
                        recharge_amount: that.price,
                        member_id: that.member_id,
                    };
                    that.fileList = [];

                    if (that.fileIds.length > 0) {
                        form2.fileIds = that.fileIds.join(",");
                    } else {
                        form2.fileIds = "";
                    }
                    rechargemanagementSave(form2).then((res) => {
                        flag = res.code;
                        msg = res.msg;
                    }).then(() => {
                        if (flag == 0) {
                            ElMessage.success(msg);
                            that.fileList = []; //图片文件数组
                            that.fileIds = []; //图片上传ids
                            that.member_id = "";
                            that.price = 0;
                            that.phone = "";
                            that.rechargeVisible = false;
                            that.moneyCz = true;
                            that.wxUserList = [];
                            that.$refs.upload.clearFiles();
                            that.getData();
                        } else if (flag == -1) {
                            ElMessage.error(msg);
                        }
                    });
                } else {
                    ElMessage.error("请输入金额");
                    return false;
                }
            },
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + 'px';
        },
    };
</script>
<style scoped>
    .el-descriptions {
        border-bottom: 1px solid #eee;
    }

    .disabled .el-upload--picture-card {
        display: none !important;
    }

    .el-image {
        margin: 4px 4px 0 4px;
    }

    .money-box {
        margin-top: 20px;
        padding: 20px;
        border: 1px dashed #ddd;
        border-radius: 4px;
    }

    .money-title {
        margin-bottom: 20px;
        font-weight: bold;
    }

    .money-title:before {
        content: '';
        display: inline-block;
        width: 4px;
        height: 14px;
        background-color: #00a2ff;
        margin-right: 6px;
        vertical-align: middle;
    }

    .main-box {
        min-height: 500px;
    }
</style>
