<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline label-width="80" size="small">
                        <el-form-item label="车牌号">
                            <el-input
                                @keyup.enter="handleSearch()"
                                size="small"
                                v-model="query.carNo"
                                placeholder="车牌号"
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="">
                            <el-checkbox v-model="form.checked">黄牌</el-checkbox>
                        </el-form-item>
                        <el-form-item label="订单状态">
                            <el-select
                                    clearable
                                    v-model="query.status"
                                    filterable
                                    size="small"
                            >
                                <el-option key="1" label="在停 " value="1"></el-option>
                                <el-option key="2" label="待缴费 " value="2"></el-option>
                                <el-option key="3" label="已缴费 " value="3"></el-option>
                                <el-option key="4" label="已完成 " value="4"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="商家电话">
                            <el-input
                                @keyup.enter="handleSearch()"
                                size="small"
                                v-model="query.business_phone"
                                placeholder="手机号"
                            ></el-input>
                        </el-form-item>

                        <!-- <el-form-item label="扣款类型">
                            <el-select v-model="form.selvalue" filterable size="small" placeholder="免单类型" class="w100">
                                <el-option v-for="item in form.czroptions" :key="item.value" :label="item.label"
                                    :value="item.value"></el-option>
                            </el-select>
                        </el-form-item> -->
                        <el-form-item label="">
                            <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询
                            </el-button>
                        </el-form-item>

                    </el-form>
                </div>

                <!-- <div class="clear"></div> -->
            </div>
            <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
                <el-table-column pro="ID" label="序号" width="55" align="center">
                    <template #default="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column prop="orderNo" label="订单号" align="center" width="190"></el-table-column>
                <el-table-column prop="placeName" label="路内" align="center" min-width="200"></el-table-column>
                <el-table-column prop="carNo" label="车牌号" align="center" width="110">
                    <template #default="scope">
                        <el-tag size="small" v-if="scope.row.car_type == '1'" v-text="scope.row.carNo"></el-tag>
                        <el-tag size="small" type="success" v-else-if="scope.row.car_type == '2'"
                            v-text="scope.row.carNo"></el-tag>
                        <el-tag size="small" type="warning" v-else-if="scope.row.car_type == '3'"
                            v-text="scope.row.carNo"></el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="phone" label="手机号" align="center" width="120"> </el-table-column>
                <el-table-column prop="driveinTime" label="停入时间" align="center" width="170"> </el-table-column>
                <el-table-column prop="driveoutTime" label="离场时间" align="center" width="170"> </el-table-column>
                <el-table-column prop="resitime" label="停留时间" align="center" width="150"> </el-table-column>
                <el-table-column prop="status_name" label="状态" align="center" width="100">
                    <template #default="scope">
                        <el-tag size="small" v-if="scope.row.status == '1'" v-text="scope.row.status_name"></el-tag>
                        <el-tag size="small" type="success" v-else-if="scope.row.status == '2'"
                            v-text="scope.row.status_name"></el-tag>
                        <el-tag size="small" type="warning" v-else-if="scope.row.status == '3'"
                            v-text="scope.row.status_name"></el-tag>
                        <el-tag size="small" type="danger" v-else-if="scope.row.status == '4'"
                            v-text="scope.row.status_name"></el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="sum_amount" label="停车费用" align="center" width="100"></el-table-column>
                <el-table-column prop="business_amount" label="商家扣款" align="center" width="100">
                  <template #default="scope">
                    <span size="small" v-if="scope.row.is_discount != '0'" >0</span>
                    <span size="small" v-if="scope.row.is_discount == '0'" v-text="scope.row.business_amount"></span>
                  </template>
                </el-table-column>
                <el-table-column label="商家优惠" align="center" width="100">
                  <template #default="scope">
                    <el-tag size="small" type="success" v-if="scope.row.is_discount == '0'">是</el-tag>
                    <el-tag size="small" type="warning" v-if="scope.row.is_discount != '0'" >否</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="business_phone" label="商家电话" align="center" width="120">
                </el-table-column>

                <el-table-column label="操作" width="100" align="center" fixed="right">
                    <template #default="scope">
                        <el-button size="mini" type="text" icon="el-icon-view"   v-permission="['road_businessOrder_details', 'park_businessOrder_details']"
                            @click="handleEdit(scope.$index, scope.row)">详情
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                    :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

        <el-dialog title="查看详情" v-model="editVisible" width="40%">
            <el-tabs type="card" v-model="activeName">
                <el-tab-pane label="基本信息" name="first">
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <tr>
                            <td class="tit" width="60">订单号</td>
                            <td>{{ parkDetails.order_no }}</td>
                            <td class="tit" width="80">订单状态</td>
                            <td>
                                <el-tag type="success">{{ parkDetails.statusName }}</el-tag>
                            </td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">停留时间</td>
                            <td>{{ parkDetails.resitime }}</td>
                            <td class="tit" width="80">费用</td>
                            <td>
                                <el-tag type="danger">{{ parkDetails.sum_amount ? parkDetails.sum_amount : '0' }}</el-tag>
                            </td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">停车地点</td>
                            <td colspan="3">{{ parkDetails.road_name }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">车牌号码</td>
                            <td colspan="3">
                                <!-- <div v-show="isShowInfo">
                                    <span>{{ parkDetails.car_no }}</span>
                                    <el-button size="small" class="ml5" type="primary" icon="el-icon-edit"
                                        @click="changeisShow">修改车牌号码</el-button>
                                </div>

                                <div v-show="isShow">
                                    <el-input v-model="parkDetails.car_no" class="w100" size="small"></el-input>
                                    <el-select v-model="parkDetails.car_type" filterable size="small" placeholder="车牌分类"
                                        class="w100 ml5">
                                        <el-option v-for="item in carType" :key="item.dc_value" :label="item.label"
                                            :value="item.dc_value"></el-option>
                                    </el-select>
                                    <el-button size="small" class="ml5" type="primary" icon="el-icon-check"
                                        @click="changeisShowInfo">确定
                                    </el-button>
                                    <el-button size="small" icon="el-icon-close" @click="changeisShowInfo">取消
                                    </el-button>
                                </div> -->
                                <div v-show="isShowInfo">
                                <span>{{ parkDetails.car_no }}</span>
                                <el-button
                                    size="small"
                                    class="ml5"
                                    type="primary"
                                    icon="el-icon-edit"
                                    @click="carUpdDate"
                                    >修改车牌号码</el-button
                                >
                                </div>
                                <div v-show="isShow">
                                <el-input
                                    v-model="parkDetails.car_no"
                                    class="w100"
                                    size="small"
                                ></el-input>
                                <el-select
                                    v-model="parkDetails.car_type"
                                    filterable
                                    size="small"
                                    placeholder="车牌分类"
                                    class="w100 ml5"
                                >
                                    <el-option
                                    v-for="item in carType"
                                    :key="item.dc_value"
                                    :label="item.label"
                                    :value="item.dc_value"
                                    ></el-option>
                                </el-select>
                                <el-button
                                    size="small"
                                    class="ml5"
                                    type="primary"
                                    icon="el-icon-check"
                                    @click="changeisShowInfo(1)"
                                    >确定
                                </el-button>
                                <el-button
                                    size="small"
                                    icon="el-icon-close"
                                    @click="changeisShowInfo(2)"
                                    >取消</el-button
                                >
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">车牌类型</td>
                            <td>{{ parkDetails.carType }}</td>
                            <td class="tit" width="80">车主手机号</td>
                            <td>{{ parkDetails.phone }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">订单来源</td>
                            <td v-if="parkDetails.source == '1'">视频采集</td>
                            <td v-else>人工</td>
                            <td class="tit" width="80">创建时间</td>
                            <td>{{ parkDetails.create_time }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">进场时间</td>
                            <td>{{ parkDetails.drivein_time }}</td>
                            <td class="tit" width="80">离场时间</td>
                            <td>{{ parkDetails.driveout_time }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">商家优惠</td>
                            <td>
                                <el-tag>{{ parkDetails.is_discount == 0?"是":"否" }}</el-tag>
                            </td>
                            <td class="tit" width="80">商家电话</td>
                            <td>{{ parkDetails.business_phone }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">实际扣款</td>
                            <td>
                                <el-tag type="danger">{{ parkDetails.is_discount == 0?parkDetails.business_amount:0 }}</el-tag>
                            </td>
                            <td class="tit" width="80">扣款类型</td>
                            <td>
                                <el-tag type="success">钱包余额</el-tag>
                            </td>
                        </tr>

                    </table>
                </el-tab-pane>
                <el-tab-pane label="进出场信息" name="second">
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <tr>
                            <td class="tit" width="60">进场照片</td>
                            <td colspan="3">
                                <template v-for="(item, i) in parkDetails.scList" :key="i">
                                    <el-image hide-on-click-modal="true" preview-teleported="true" style="width: 100px; height: 100px" :src="imgurl(item.file_url)"
                                        class="ml5" :preview-src-list="[imgurl(item.file_url)]">
                                    </el-image>
                                </template>
                            </td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">出场照片</td>
                            <td colspan="3">
                                <template v-for="(item, i) in parkDetails.slList" :key="i">
                                    <el-image hide-on-click-modal="true" preview-teleported="true" style="width: 100px; height: 100px" :src="imgurl(item.file_url)"
                                        class="ml5" :preview-src-list="[imgurl(item.file_url)]">
                                    </el-image>
                                </template>
                            </td>
                        </tr>
                    </table>
                </el-tab-pane>
            </el-tabs>

            <el-form v-show="stopTime">
                <el-form-item label="结束计时时间">
                    <el-date-picker v-model="value1" type="datetime" placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>


            <template #footer>
                <span class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="endTimeMethod" v-show="surevb"
                    >确 定</el-button
                >
                <el-button
                    type="danger"
                    @click="endTimeTK"
                    v-if="
                    (form.driveout_time == null ||
                        form.driveout_time == undefined ||
                        form.driveout_time == '') &&
                    parkDetails.status == 1
                    "
                    v-show="!surevb"
                    >结束计时</el-button
                >
                </span>
                <!-- <span class="dialog-footer">
                    <el-button @click="editVisible = false">取 消</el-button>
                    <el-button type="danger" @click="stopTimeShow">结束计时</el-button>
                </span> -->
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { storeOrderList, storeOrderDetailsData } from "../../api/store";
import { dictData, isModel, orderOpengateList } from "../../api/index.js";
import {
  roadParkingOrderCarData,
  roadParkingOrderClosureData,
  roadParkingOrderDetailsData,
  roadParkingOrderListData,
  mergeOrder,
} from "../../api/roadParkingOrder.js";

export default {
    name: "order",

    components: {
    },
    data() {
        return {
            isShow: false,
            isShowInfo: true,
            stopTime: false,
            value1: '',
            carNum: "青A-20893",
        }
    },
    setup() {
        const query = reactive({
            address: "",
            name: "",
            checked: false,
            pageIndex: 1,
            pageSize: 10,
        });
        const tableData = ref([]);
        const pageTotal = ref(0);
        // 获取表格数据
        const getData = () => {
            storeOrderList(query).then((res) => {
                console.log(res)
                tableData.value = res.data.list;
                pageTotal.value = res.data.total || 0;
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
        const viewVisible = ref(false);
        const parkDetails = ref({});

        const isShow = ref(false);
        const isShowInfo = ref(true);

        let form = reactive({
            id: "",
            car_id: "",
            order_no: "",
            car_type: "",
            road_name: "",
            berth: "",
            car_no: "",
            drivein_time: "",
            driveout_time: "",
            resitime: "",
            sum_amount: "",
            state_name: "",
            checked: false,
            startTime: "",
            endTime: "",
            source_type: "",
            company_name: "",
            create_time: "",
        });
        //详情获取
        const handleEdit = (index, row) => {
            isShow.value = false;
            isShowInfo.value = true;
            storeOrderDetailsData(reactive({ id: row.id })).then((res) => {
                res.data.is_discount = row.is_discount==null?1:0;
                parkDetails.value = res.data;
            });
            editVisible.value = true;
        };

        const carType = ref([]);
        //字典列表
        dictData(reactive({ dict_type: "car_type" })).then((res) => {
            carType.value = res.data;
        });


        const handleView = (index, row) => {
            ElMessageBox.confirm(
                "将解除车牌现有绑定者，重新绑定到申诉用户，确定审核通过吗？",
                "提示", {
                type: "warning",
            }
            )
                .then(() => {
                    ElMessage.success("解除成功");
                    that.tableData.splice(indexI, 1);
                })
                .catch(() => {
                    ElMessage.info("取消解除");
                });
        };

        const saveEdit = () => {
            editVisible.value = false;
            ElMessage.success(`修改第 ${idx + 1} 行成功`);
            Object.keys(form).forEach((item) => {
                tableData.value[idx][item] = form[item];
            });
        };


        return {
            carType,
            query,
            tableData,
            pageTotal,
            editVisible,
            viewVisible,
            parkDetails,
            form,
            isShow,
            isShowInfo,
            handleSearch,
            handlePageChange,
            handleEdit,
            handleView,
            saveEdit,
            multipleSelection: [],
            dialogImageUrl: "",
            ppVisible: false,
            isActive: false,
            activeName: "first",
            url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
            srcList: [
                'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
                'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
            ]
        };
    },
    methods: {
        //上传图片操作
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        //图片预览
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.ppVisible = true;
        },
        //图片回显
        imgurl: function (url) {
            if (url != "" && url != null) {
                return url;
            }
        },
        //修改车牌(车牌id,新车牌号，车牌类型(下拉选项))
        carUpdDate() {
            this.isShow = !this.isShow;//取反
            this.isShowInfo = !this.isShowInfo;//取反
        },
        // 修改车牌确定取消
        changeisShowInfo(val) {
            let _this = this;
            this.isShow = !this.isShow;//取反
            this.isShowInfo = !this.isShowInfo;//取反
            if (val == 1) {
                roadParkingOrderCarData(
                    reactive({
                        id: _this.parkDetails.id,
                        car_id: _this.parkDetails.car_id,
                        car_no: _this.parkDetails.car_no,
                        car_type: _this.parkDetails.car_type,
                    })
                ).then((res) => {
                    if (res.code == 0) {
                        ElMessage.success(res.msg);
                        _this.editVisible = false;
                        _this.getData();
                    } else {
                        ElMessage.error(res.msg);
                    }
                });
            }
        },
        bindRoad() {
            var that = this;
            var val = this.selectedData;
            if (val) {
                val.forEach(function (item, index) {
                    that.tableData.forEach(function (itemI, indexI) {
                        if (item === itemI) { }
                    });
                });
                this.viewVisible = true;
                //ElMessage.success("分配成功");
                this.$refs.multipleTable.clearSelection();
            } else {
                ElMessage.warning(`请选择一条记录`);
            }
        },
        changeisShow() {
            this.isShow = !this.isShow;//取反
            this.isShowInfo = !this.isShowInfo;//取反
        },
        changeisShowInfo() {
            this.isShow = !this.isShow;//取反
            this.isShowInfo = !this.isShowInfo;//取反
        },
        stopTimeShow() {
            this.stopTime = !this.stopTime;
        },
        endTimeMethod() {
            let _this = this;
            let query = reactive({
                id: _this.parkDetails.id,
                driveout_time: _this.form.startTime,
            });
            roadParkingOrderClosureData(query).then((res) => {
                if (res.code == 0) {
                ElMessage.success(res.msg);
                _this.editVisible = false;
                _this.getData();
                } else {
                ElMessage.success(res.msg);
                }
            });
        },
        endTimeTK() {
            this.stopTime = !this.stopTime;
            this.surevb = true;
        },
    },
};
</script>
<style scoped>
.el-image {
    margin: 4px 4px 0 4px;
}
</style>
