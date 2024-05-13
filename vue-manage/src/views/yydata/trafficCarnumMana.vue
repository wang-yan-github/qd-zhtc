<template>
    <div>
        <div class="container">
            <el-table
                    :data="tableData"
                    border
                    class="table"
                    :max-height="tableH"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="index" label="序号" width="55" align="center">
                </el-table-column>
                <el-table-column
                        prop="car_no"
                        label="车牌号"
                        width="200"
                        align="center"
                >
                    <template #default="scope">
                        <el-tag
                                size="small"
                                v-if="scope.row.car_type == '1'"
                                v-text = "scope.row.car_no"
                        ></el-tag
                        >
                        <el-tag
                                size="small"
                                type="success"
                                v-else-if="scope.row.car_type == '2'"
                                v-text = "scope.row.car_no"
                        ></el-tag
                        >
                        <el-tag
                                size="small"
                                type="warning"
                                v-else-if="scope.row.car_type == '3'"
                                v-text = "scope.row.car_no"
                        ></el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column
                        prop="carTypeName"
                        label="车牌样式"
                        width="200"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="member_id"
                        label="会员车牌（是/否）"
                        width="200"
                        align="center"
                        :formatter="formatCarNo"
                ></el-table-column>
                <el-table-column
                        prop="rosterTypeName"
                        label="名单类型"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="phone"
                        label="绑定账号"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="bind_date"
                        label="绑定时间"
                        align="center"
                        :formatter="dateFormat"
                ></el-table-column>
                <el-table-column
                        prop="reason"
                        label="理由说明"
                        align="center"
                ></el-table-column>
                <el-table-column v-if="isControl" label="操作" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-warning-outline"
                                @click="bindRoad(scope.$index, scope.row)"
                                v-permission="'raod_nummana1_wsxx'"
                        >完善信息
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
                <!--<div id="mapDiv"></div>-->
            </div>
        </div>






    </div>
</template>

<script>
    import {ref, reactive,nextTick} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import { editOperateCarno, getDictsByType,relieveBind,downloadWhite,saveBeathWhiteCarNo ,operateCarnoTrafficList} from "../../api/operateCar.js";
    import {useRouter} from "vue-router";
    import axios from "axios";
    import File_URL from '../../file_url';
    export default {
        name: "trafficcarnummana",
        components: {},
        data() {
            return {
                uploadUrl: File_URL.file_img_url,//图片上传路径
                imgViewUrl: File_URL.file_hx_img_url,//图片回显路径
                tableH:0,
            };
        },
        setup() {
            const query = reactive({
                phone: "",
                isCheck: false,
                car_type: "",
                car_no: "",
                roster_type: "",
                pageIndex: 1,
                pageSize: 15,
            });
            const optionsC = ref([{
                value: '选项1',
                label: '黄金糕'
            }, {
                value: '选项2',
                label: '双皮奶'
            }, {
                value: '选项3',
                label: '蚵仔煎'
            }, {
                value: '选项4',
                label: '龙须面'
            }, {
                value: '选项5',
                label: '北京烤鸭'
            }]);

            const file_img = ref([]);

            const viewVisibleExcel = ref(false);
            const excelImport =()=>{
                viewVisibleExcel.value = true;
            }
            const  valueC = ref('');
            const timeC = ref('');
            const tableData = ref([]);
            const pageTotal = ref(0);
            const isControl = ref(true);
            var user_type = localStorage.getItem("user_type");
            if(user_type == 1){
                isControl.value=false;
            }
            const router = useRouter();
            // 获取表格数据
            const getData = () => {
                if(query.isCheck){
                    query.car_type = "3";
                }else{
                    query.car_type = "";
                }
                operateCarnoTrafficList(query).then((res) => {
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
            };
            getData();
            const formatCarNo = (row) => {
                if (row.member_id != null && row.member_id) {
                    return "是";
                } else {
                    return "否";
                }
            }
            const dateFormat = (row, column) => {
                var date = row.bind_date;
                if (date != undefined && date != null){
                    var t = new Date(date);//row 表示一行数据, updateTime 表示要格式化的字段名称
                    var year = t.getFullYear(),
                        month = t.getMonth() + 1,
                        day = t.getDate(),
                        hour = t.getHours(),
                        min = t.getMinutes(),
                        sec = t.getSeconds();
                    var newTime = year + '-' +
                        (month < 10 ? '0' + month : month) + '-' +
                        (day < 10 ? '0' + day : day) + ' ' +
                        (hour < 10 ? '0' + hour : hour) + ':' +
                        (min < 10 ? '0' + min : min) + ':' +
                        (sec < 10 ? '0' + sec : sec);
                    return newTime;
                }
            }



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
            const handleDelete = (index) => {
                // 二次确认删除
                ElMessageBox.confirm("确定要删除吗？", "提示", {
                    type: "warning",
                }).then(() => {
                    ElMessage.success("删除成功");
                    tableData.value.splice(index, 1);
                })
                    .catch(() => {
                    });
            };
            const dialogT = "编辑";

            // 批量加入白名单
            const batchVisible = ref(false);
            let tagsType = ref([]);
            let blueTags = ref([]);
            let blueTagsValue = ref([]);
            let yellowTags = ref([]);
            let yellowTagsValue = ref([]);
            let greenTags = ref([]);
            let greenTagsValue = ref([]);
            let inputVisible= ref(false);
            let inputValue= ref('');
            let inputVisibleY= ref(false);
            let inputValueY= ref('');
            let inputVisibleG= ref(false);
            let inputValueG= ref('');
            let saveTagInputY =ref(null);
            let saveTagInput =ref(null);
            let saveTagInputG =ref(null);
            // 添加姓名手机号--start
            const viewVisibleInner = ref(false)
            const carName = ref('');
            const carPhone = ref('');
            let focusName =ref(null);
            let focusPhone =ref(null);
            let tags = ref('');
            const focusPhoneInput=() =>{
                nextTick(_ => {
                    focusPhone.value.input.focus();
                });
            };
            const carInfoSure=() =>{

                if (carName.value && carPhone.value) {
                    viewVisibleInner.value = false;
                    if (tagsType.value == 'blueTags') {
                        blueTagsValue.value.push(blueTags.value + "-" + carName.value + "-" + carPhone.value);
                    } else if (tagsType.value == 'yellowTags') {
                        yellowTagsValue.value.push(yellowTags.value + "-" + carName.value + "-" + carPhone.value);
                    } else if (tagsType.value == 'greenTags') {
                        greenTagsValue.value.push(greenTags.value + "-" + carName.value + "-" + carPhone.value);
                    }
                }else{
                    ElMessage({
                        showClose: true,
                        message: '姓名和手机号不能为空！',
                        type: 'error',
                    })
                }
            };
            const innerClose=()=>{
                viewVisibleInner.value = false;
                tags.value.pop();
                ElMessage({
                    showClose: true,
                    message: '取消添加车牌信息！',
                    type: 'error',
                })
            }
            // 添加姓名手机号--end

            let batchForm = reactive({
                eg1: "",
                eg2: "",
            });

            const batchAdd = () => {
                batchVisible.value = true;
                yellowTags.value = [];
                blueTags.value= [];
                greenTags.value= [];
                batchForm.eg1 = "0";
                batchForm.eg2 = "" ;
                inputVisible.value=false;
                inputVisibleY.value=false;
                inputVisibleG.value=false;
                valueC.value = "";
                timeC.value = "";
                blueTagsValue.value = [];
                yellowTagsValue.value = [];
                greenTagsValue.value = [];
            };

            const download = () => {
                downloadWhite( reactive({}) ).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', '白名单登记表.xlsx');
                    document.body.appendChild(link);
                    link.click();
                });
            };

            let file = reactive({
                excel:""
            });

            //模拟上传按钮点击
            const inputFileClick = () => {
                var e = document.createEvent('MouseEvents');
                e.initEvent('click', true, true);
                document.getElementById('inputFile').dispatchEvent(e);
            }

            //导入excel
            const exportImport = () => {
                var formData = new FormData();
                formData.append("file",document.getElementById('inputFile').files[0]);
                axios({
                    url: File_URL.file_down + 'operatecarno/importWhiteList',
                    method: 'POST',
                    data: formData,
                    headers: {'Content-Type': 'multipart/form-data', 'Authorization': localStorage.getItem("token_value")},
                }).then(res => {
                    var data = res.data;
                    if(data.code == 0){
                        ElMessage({
                            showClose: true,
                            message: data.data,
                            type: 'success',
                        });

                        viewVisibleExcel.value = false;
                        getData();
                    }else{
                        ElMessage({
                            showClose: true,
                            message: data.msg,
                            type: 'error',
                        })
                    }
                    //清空
                    document.getElementById('inputFile').value = '';
                });

            }

            //blue
            const handleClose =(tag)=> {
                blueTags.value.splice(blueTags.value.indexOf(tag), 1);
                if (tagsType.value == 'blueTags') {
                    blueTagsValue.value.splice(blueTagsValue.value.indexOf(tag), 1);
                } else if (tagsType.value == 'yellowTags') {
                    yellowTagsValue.value.splice(yellowTagsValue.value.indexOf(tag), 1);
                } else if (tagsType.value == 'greenTags') {
                    greenTagsValue.value.splice(greenTagsValue.value.indexOf(tag), 1);
                }

            };

            const showInput=() =>{
                inputVisible.value = true;
                nextTick(_ => {
                    saveTagInput.value.input.focus();
                });
            };

            const handleInputConfirm=()=> {
                tagsType.value = 'blueTags';
                //let inputValue = inputValue.value;
                if (inputValue.value) {
                    blueTags.value.push(inputValue.value);
                }
                inputVisible.value = true;
                inputValue.value = '';
                viewVisibleInner.value = true; //填写姓名手机弹窗
                carName.value = '';//清空姓名手机号
                carPhone.value = '';//清空手机号
                tags = blueTags;
                setTimeout(()=>{
                    nextTick(_ => {
                        focusName.value.input.focus();
                    });
                },0);
            };
            const handleInputConfirmBlur=()=> {
                //let inputValue = inputValue.value;
                if (inputValue.value) {
                    blueTags.value.push(inputValue.value);
                    inputVisible.value = true;
                    inputValue.value = '';
                    viewVisibleInner.value = true; //填写姓名手机弹窗
                    carName.value = '';//清空姓名手机号
                    carPhone.value = '';//清空手机号
                    tags = blueTags;
                    setTimeout(()=>{
                        nextTick(_ => {
                            focusName.value.input.focus();
                        });
                    },0);
                }else {
                    return false;
                }

            };
            // yellow
            const handleCloseY=(tag)=> {
                yellowTags.value.splice(yellowTags.value.indexOf(tag), 1);
            };

            const showInputY=() =>{
                inputVisibleY.value = true;
                nextTick(_ => {
                    saveTagInputY.value.input.focus();
                });
            };

            const handleInputConfirmY=()=> {
                tagsType.value = 'yellowTags';
                let inputValue = inputValueY.value;
                if (inputValue) {
                    yellowTags.value.push(inputValue);
                }
                inputVisibleY.value = true;
                inputValueY.value = '';
                viewVisibleInner.value = true; //填写姓名手机弹窗
                carName.value = '';//清空姓名手机号
                carPhone.value = '';//清空手机号
                tags = yellowTags;
                setTimeout(()=>{
                    nextTick(_ => {
                        focusName.value.input.focus();
                    });
                },0);
            };
            const handleInputConfirmYBlur=()=> {
                let inputValue = inputValueY.value;
                if (inputValue != '') {
                    yellowTags.value.push(inputValue);
                    inputVisibleY.value = true;
                    inputValueY.value = '';
                    viewVisibleInner.value = true; //填写姓名手机弹窗
                    carName.value = '';//清空姓名手机号
                    carPhone.value = '';//清空手机号
                    tags = yellowTags;
                    setTimeout(()=>{
                        nextTick(_ => {
                            focusName.value.input.focus();
                        });
                    },0);
                }else {
                    return false;
                }

            };
            // Green
            const handleCloseG=(tag) =>{
                greenTags.value.splice(greenTags.value.indexOf(tag), 1);
            };

            const showInputG=() =>{
                inputVisibleG.value = true;
                nextTick(_ => {
                    saveTagInputG.value.input.focus();
                });
            };

            const handleInputConfirmG=() =>{
                tagsType.value = 'greenTags';
                let inputValue = inputValueG.value;
                if (inputValue) {
                    greenTags.value.push(inputValue);
                }
                inputVisibleG.value = true;
                inputValueG.value = '';
                viewVisibleInner.value = true; //填写姓名手机弹窗
                carName.value = '';//清空姓名手机号
                carPhone.value = '';//清空手机号
                tags = greenTags;
                setTimeout(()=>{
                    nextTick(_ => {
                        focusName.value.input.focus();
                    });
                },0);
            };
            const handleInputConfirmGBlur=() =>{
                let inputValue = inputValueG.value;
                if (inputValue!='') {
                    greenTags.value.push(inputValue);
                    inputVisibleG.value = true;
                    inputValueG.value = '';
                    viewVisibleInner.value = true; //填写姓名手机弹窗
                    carName.value = '';//清空姓名手机号
                    carPhone.value = '';//清空手机号
                    tags = greenTags;
                    setTimeout(()=>{
                        nextTick(_ => {
                            focusName.value.input.focus();
                        });
                    },0);
                }else {
                    return false;
                }

            }

            // 表格编辑时弹窗和保存
            const editVisible = ref(false);
            const viewVisible = ref(false);
            const bmdVisible = ref(false);
            let form = reactive({
                name: "",
                address: "",
                czroptions: [],
            });
            let idx = -1;
            const handleEdit = (index, row, type) => {
                router.push("/roadedit");
            };

            const handleView = (index, row) => {
                idx = index;
                Object.keys(form).forEach((item) => {
                    form[item] = row[item];
                });
                viewVisible.value = true;
            };
            const getDicts = () => {
                getDictsByType({type: "rosterType"}).then((res) => {
                    form.czroptions = res.data;
                });
            }
            getDicts();
            // 编辑 名单修改
            let operateCarnoParams = reactive({
                id: "",
                roster_type: "",
                whitelist_type:"0",
                reason:"",
                name: "",
                phone: "",
                company_name: "",
                cut_off_date: "",
                deformity_cert: "",
                deformity_picture_id: "",


            });
            const whiteType = ref(false);
            const disableType = ref(false);




            const msginfo = reactive({
                title:"",
                carNo:"",
                msg:""
            });
            let upload = ref(null);
            const handleCommand = (type, row) => {
                nextTick(_ => {
                    upload.value.clearFiles();
                });
                operateCarnoParams.id = row.id;
                operateCarnoParams.roster_type = type;
                msginfo.carNo = row.car_no;
                operateCarnoParams.whitelist_type = "0";
                operateCarnoParams.reason = "";
                operateCarnoParams. name = "";
                operateCarnoParams.phone = "";
                operateCarnoParams.company_name = "";
                operateCarnoParams.cut_off_date = "";
                operateCarnoParams.deformity_cert = "";

                fileList2.value = [];//图片上传列表 初始化
                imageUrl.value = "";//图片回显路径 初始化
                imageId.value = "";//图片id 初始化
                fileIds.value = []; //图片上传ids 初始化

                if("3" == type){
                    whiteType.value=true;
                    disableType.value=false;
                    operateCarnoParams.whitelist_type=row.whitelist_type;
                    msginfo.title="加入白名单";
                    msginfo.msg = "加入白名单，停车将不会再产生费用";
                }else if("4" == type){
                    whiteType.value=false;
                    disableType.value=true;
                    msginfo.title="加入残疾人名单";
                    msginfo.msg="加入残疾人名单";
                }else{
                    whiteType.value=false;
                    disableType.value=false;
                    switch(type){
                        case "1":
                            msginfo.title="加入普通名单";
                            msginfo.msg="恢复为普通名单";
                            break;
                        case "2":
                            msginfo.title="加入黑名单";
                            msginfo.msg="加入黑名单";
                            break;

                    }
                }
                bmdVisible.value=true;
            };


            //日期格式化
            const dateToFormat = (time) => {
                return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()}`
            }
            const saveEdit = () => {
                // 二次确认删除
                ElMessageBox.confirm("确定变更名单类型吗？", "提示", {
                    type: "warning",
                }).then(() => {

                    if (operateCarnoParams.cut_off_date != ""){
                        operateCarnoParams.cut_off_date = dateToFormat(operateCarnoParams.cut_off_date);

                    }
                    editOperateCarno(operateCarnoParams).then((res) => {
                        if(res.code == 0){
                            ElMessage.success("操作成功！");
                            bmdVisible.value=false;
                            getData();
                        }else{
                            ElMessage.error(res.msg);
                        }
                    });
                })
                    .catch(() => {
                    });



            };

            //完善信息
            const bindRoad = (index, row) => {
                // router.push('/carnumenter')
                router.push({ path: '/carnumenter', query: { id: row.id }})

            };

            //批量添加白名单
            const saveBatch = (index) => {

                if (null == blueTagsValue.value){
                    blueTagsValue = blueTags.value;
                    yellowTagsValue = yellowTags.value;
                    greenTagsValue = greenTags.value;
                }
                if ("" == blueTags.value && "" == yellowTags.value && "" == greenTags.value){
                    ElMessage({
                        showClose: true,
                        message: '车牌、姓名、手机号不能为空！',
                        type: 'error',
                    })

                } else {

                    let bindInfoParams = reactive({
                        blueCars: blueTagsValue.value,
                        yellowCars: yellowTagsValue.value,
                        greenCars: greenTagsValue.value,
                        whitelist_type: batchForm.eg1,
                        company_name: valueC,
                        cut_off_date: timeC.value,
                        reason: batchForm.eg2
                    });
                    console.log(bindInfoParams)

                    saveBeathWhiteCarNo(bindInfoParams).then((res) => {
                        console.log(res);
                        if (res.code == 0) {
                            ElMessage({
                                showClose: true,
                                message: "新增成功！",
                                type: 'success',
                            })
                            getData();
                            batchVisible.value = false;
                        } else {
                            ElMessage({
                                showClose: true,
                                message: res.msg,
                                type: 'error',
                            })
                            batchVisible.value = true;
                        }
                    });

                }



            };


            const fileIds = ref([]);
            const fileList2 = ref([]);
            const imageUrl = ref("");
            const imageId = ref("");

            // const verifyShow = (row) => {
            //     if (row.member_id != null && row.member_id) {
            //         return true;
            //     } else {
            //         return false;
            //     }
            //
            //     // console.info(memberId);
            //     // if(memberId != null){
            //     //     return true;
            //     // }else{
            //     //     return false;
            //     // }
            // };

            return {
                dateToFormat,
                fileIds,
                fileList2,
                imageUrl,
                imageId,
                file_img,
                fileurl:File_URL.file_img_url,
                query,
                upload,
                tableData,
                pageTotal,
                isControl,
                editVisible,
                viewVisible,
                msginfo,
                whiteType,
                disableType,
                handleCommand,
                form,
                formatCarNo,
                dateFormat,
                operateCarnoParams,
                dialogT,
                handleSearch,
                handlePageChange,
                handleDelete,
                handleEdit,
                inputFileClick,
                handleView,
                bindRoad,
                // verifyShow,
                saveEdit,
                multipleSelection: [],
                dialogImageUrl: "",
                ppVisible: false,
                activeName: "first",
                map: null,
                saveBatch,
                bmdVisible,
                batchVisible,
                batchForm,
                batchAdd,
                blueTags,
                yellowTags,
                greenTags,
                inputVisible,
                inputValue,
                inputVisibleY,
                inputValueY,
                inputVisibleG,
                inputValueG,
                saveTagInput,
                saveTagInputY,
                saveTagInputG,
                handleClose,
                showInput,
                download,
                handleInputConfirm,
                handleCloseY,
                showInputY,
                handleInputConfirmY,
                handleCloseG,
                showInputG,
                exportImport,
                handleInputConfirmG,
                viewVisibleInner,
                carName,
                carPhone,
                focusPhoneInput,
                focusName,
                focusPhone,
                carInfoSure,
                innerClose,
                tags,
                handleInputConfirmBlur,
                handleInputConfirmYBlur,
                handleInputConfirmGBlur,
                valueC,
                optionsC,
                timeC,
                viewVisibleExcel,
                excelImport,

            };
        },
        methods: {
            handleSelectionChange(data) {
                this.selectedData = data;
            },

            handleDeleteAll() {
                var that = this;
                var val = this.selectedData;
                if (val) {
                    val.forEach(function (item, index) {
                        that.tableData.forEach(function (itemI, indexI) {
                            if (item === itemI) {
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



            //上传图片操作
            handleSuccess(response, file, fileList) {
                this.fileIds.push(response.id + "");
                this.imageId = response.id + "";
                file.id = response.id;
                this.operateCarnoParams.deformity_picture_id = response.id;
                this.imageUrl = URL.createObjectURL(file.raw);
                console.log("imageUrl", this.imageUrl);
                console.log(response, file, fileList);
            },
            //删除图片
            handleRemove(file, fileList) {
                console.log(file, fileList);
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


            //     //上传图片操作
            //     handleSuccess(response, file, fileList) {
            //         this.fileIds.push(response.id + "");
            //         file.id = response.id;
            //         this.file_img = file;
            //     },

            //     handlePictureCardPreview(file) {//图片预览
            //         this.dialogImageUrl = file.url;
            //         this.dialogVisible = true;
            //    },
            //   //删除图片
            //     handleRemove(file, fileList) {
            //         let index = this.fileIds.indexOf(file.id + "");
            //         if (index == -1) {
            //             index = this.fileIds.indexOf(file.id);
            //         }else{
            //        //移除删除id
            //        this.fileIds.splice(index, 1);
            //       }
            //    },
            // 地图初始化函数，本例取名为init，开发者可根据实际情况定义
            /*initMap() {
                //console.log(window); // 通过window获取
                var center = new window.TMap.LatLng(39.98412, 116.307484);
                //初始化地图
                var map = new window.TMap.Map("mapDiv", {
                    zoom: 18, //设置地图缩放级别
                    center: center, //设置地图中心点坐标
                    baseMap: {
                        // 设置卫星地图
                        //type: "satellite",
                    },
                });
                //console.log(map);
            },*/
        },
        mounted() {
            //this.initMap();
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + 'px';
        },
    };
</script>
<style scoped>

    .el-tag--light {
        margin-right: 10px;
    }
    .button-new-tag {
        height: 32px;
        line-height: 30px;
        padding-top: 0;
        padding-bottom: 0;
    }
    .input-new-tag {
        width: 90px;
        vertical-align: middle;
    }
    .txtcenter {
        text-align: center;
    }
    .excel-form .el-form-item{
        border: 1px dashed #ddd;
        padding: 10px;
        border-radius: 2px;
    }
</style>
