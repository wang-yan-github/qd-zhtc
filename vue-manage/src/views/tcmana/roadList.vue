<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-button
                            type="primary"
                            size="small"
                            icon="el-icon-plus"
                            @click="handleEdit(0, null, true)"
                            v-permission="'road_roadlist_add'"
                    >添加
                    </el-button>
                    <el-button
                            type="danger"
                            size="small"
                            icon="el-icon-delete"
                            @click="handleDeleteAll()"
                            v-permission="'road_roadlist_deleteAll'"
                    >批量删除
                    </el-button>
                </div>
                <div class="right-panel">
                    <el-form inline size="small">
                        <el-input
                                @keyup.enter="handleSearch()"
                                size="small"
                                v-model="query.road_name"
                                placeholder="请输入路内名"
                                class="handle-input mr10"
                        ></el-input>
                        <el-select
                                v-model="query.area_id"
                                filterable
                                size="small"
                                placeholder="所有区域"
                                class="w100"
                                @change="getStreetData('a')"
                        >
                            <el-option value="">全部</el-option>
                            <el-option
                                    v-for="(item, i) in result.area_list"
                                    :key="i"
                                    :label="item.area_name"
                                    :value="item.id"
                            ></el-option>
                        </el-select>
                        <span class="dispinline ml5"></span>
                        <el-select
                                v-model="query.street_id"
                                filterable
                                size="small"
                                placeholder="所有街道"
                                class="w100"
                        >
                            <el-option value="">全部</el-option>

                            <el-option
                                    v-for="(item, i) in result.query_street_list"
                                    :key="i"
                                    :label="item.street_name"
                                    :value="item.id"
                            ></el-option>
                        </el-select>
                        <span class="dispinline ml5"></span>

                        <el-button
                                size="small"
                                type="primary"
                                icon="el-icon-search"
                                @click="handleSearch"
                        >查询
                        </el-button>
                        <el-button size="small" type="success" icon="el-icon-upload2" @click="exportRoad"
                                   v-permission="'road_roadlist_excel'"
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
                        prop="park_code"
                        label="路内编号"
                        width="100"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="traffic_park_code"
                        label="市交编号"
                        width="100"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="road_name"
                        align="center"
                        label="路内名称"
                        min-width="200"
                ></el-table-column>
                <!--<el-table-column-->
                <!--prop="road_level"-->
                <!--label="路内等级"-->
                <!--width="100"-->
                <!--align="center"-->
                <!--&gt;-->
                <!--<template #default="scope">-->
                <!--<p v-if="scope.row.road_level == 1">核心区</p>-->
                <!--<p v-if="scope.row.road_level == 2">一类区</p>-->
                <!--<p v-if="scope.row.road_level == 3">二类区</p>-->
                <!--<p v-if="scope.row.road_level == 4">三类区</p>-->
                <!--</template>-->
                <!--</el-table-column>-->
                <!-- <el-table-column
                  prop="city"
                  label="所属城市"
                  width="100"
                  align="center"
                ></el-table-column> -->
                <el-table-column
                        prop="area_name"
                        label="所属区域"
                        min-width="80"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="street_name"
                        label="所属街道"
                        min-width="100"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="device_num"
                        label="泊位数量"
                        width="80"
                        align="center"
                ></el-table-column>

                <!--<el-table-column-->
                <!--prop="longitude,latitude"-->
                <!--label="坐标"-->
                <!--width="180"-->
                <!--align="center"-->
                <!--&gt;-->
                <!--<template #default="scope">-->
                <!--{{ scope.row.latitude }},{{ scope.row.longitude }}-->
                <!--</template>-->
                <!--</el-table-column>-->

                <el-table-column
                        prop="address"
                        label="地址"
                        width="200"
                        align="center"
                ></el-table-column>
                <!--<el-table-column-->
                <!--prop="create_time"-->
                <!--label="添加时间"-->
                <!--align="center"-->
                <!--width="170"-->
                <!--&gt;</el-table-column>-->
                <el-table-column label="状态" align="center" width="80">
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
                                v-if="scope.row.status == 0"
                        >启用
                        </el-tag>
                        <el-tag
                                size="small"
                                :type="
                scope.row.status === '0'
                  ? 'success'
                  : scope.row.status === '1'
                  ? 'danger'
                  : ''
              "
                                v-else
                        >禁用
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="320" fixed="right" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-view"
                                @click="handleView(scope.$index, scope.row)"
                                v-permission="'road_roadlist_details'"
                        >查看泊位
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row)"
                                v-permission="'road_roadlist_edit'"
                        >编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-circle-close"
                                @click="handleStop(scope.$index, scope.row, '1')"
                                v-if="scope.row.status == '0'"
                                class="red"
                                v-permission="'road_roadlist_status'"
                        >停用
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-circle-check"
                                @click="handleStop(scope.$index, scope.row, '0')"
                                v-if="scope.row.status == '1'"
                                v-permission="'road_roadlist_status'"
                        >启用
                        </el-button>

                        <el-dropdown class="ml10 red" @command="handleCommand">
              <span class="el-dropdown-link">
                更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
                            <template v-slot:dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item
                                            icon="el-icon-delete"
                                            command="a"
                                            @click="handleDelete(scope.$index, scope.row)"
                                            v-permission="'road_roadlist_delete'"
                                    >删除
                                    </el-dropdown-item>
                                    <el-dropdown-item
                                            icon="el-icon-money"
                                            command="b"
                                            @click="handleRemark(scope.$index, scope.row)"
                                            v-permission="'road_roadlist_sfsm'"
                                    >收费说明
                                    </el-dropdown-item>

                                    <!--<el-dropdown-item icon="el-icon-refresh-right" command="e"    v-permission="'road_roadlist_cqsbAll'"-->
                                    <!--&gt;批量重启设备-->
                                    <!--</el-dropdown-item>-->
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
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

        <!-- 编辑弹出框 -->
        <!--<div v-if="editVisible">-->
        <el-dialog
                :title="result.dialogT"
                v-model="editVisible"
                width="650px"
                top="2vh"
                :close-on-click-modal="false"
                destroy-on-close="true"
                @closed="cancleEdit"
        >
            <el-form
                    label-width="80px"
                    size="small"
                    :rules="formRules"
                    ref="editform"
                    :model="form"
            >
                <el-tabs type="card" v-model="activeName">
                    <el-tab-pane label="路内信息" name="first">
                        <div class="mt10"></div>

                        <el-row :gutter="20">
                            <el-col :span="24">
                                <el-form-item label="路内名称" prop="road_name">
                                    <el-input v-model="form.road_name"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="路内编号" prop="park_code">
                                    <el-input
                                            v-model="form.park_code"
                                            placeholder="输入路内编号"
                                    ></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="路内等级" prop="road_level">
                                    <el-select
                                            v-model="form.road_level"
                                            filterable
                                            size="small"
                                            placeholder="所有路内等级"
                                            class="w"
                                    >
                                        <el-option
                                                v-for="(item, i) in result.road_levels"
                                                :key="i"
                                                :label="item.label"
                                                :value="item.dc_value"
                                        ></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="所属区域" prop="area_id">
                                    <el-select
                                            v-model="form.area_id"
                                            filterable
                                            size="small"
                                            placeholder="所有区域"
                                            class="w"
                                            @change="getStreetData('b')"
                                    >
                                        <el-option
                                                v-for="(item, i) in result.area_list"
                                                :key="i"
                                                :label="item.area_name"
                                                :value="item.id"
                                        ></el-option>
                                    </el-select>
                                    <p class="color999 lh20 mt10">
                                        操作提示：如没有您要的区域，请先添加区域！
                                    </p>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="所属街道" prop="street_id">
                                    <el-select
                                            v-model="form.street_id"
                                            filterable
                                            size="small"
                                            placeholder="所有街道"
                                            class="w"
                                    >
                                        <el-option
                                                v-for="(item, i) in result.street_list"
                                                :key="i"
                                                :label="item.street_name"
                                                :value="item.id"
                                        ></el-option>
                                    </el-select>
                                    <p class="color999 lh20 mt10">
                                        操作提示：如没有您要的街道，请先添加街道！
                                    </p>
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="路内地址" prop="address">
                                    <el-input
                                            v-model="form.address"
                                            placeholder=""
                                            type="textarea"
                                            :rows="5"
                                    ></el-input>
                                </el-form-item>
                            </el-col>

                            <el-col :span="24">
                                <el-form-item label="坐标范围">
                                    <el-input
                                            v-model="form.coordinate"
                                            placeholder=""
                                            disabled
                                    ></el-input>
                                    <span class="color999 lh20"
                                    >提示：http://lbs.qq.com/tool/getpoint/index.html，以腾讯位置服务坐标拾取为准。</span
                                    >
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <el-form-item label="路内查询">
                                    <el-input v-model="form.searchkey"
                                    >
                                        <template v-slot:append
                                        >
                                            <el-button @click="searchMaps()"
                                            >查询
                                            </el-button
                                            >
                                        </template
                                        >
                                    </el-input
                                    >
                                </el-form-item>
                            </el-col>
                            <el-col :span="24">
                                <div class="map">
                                    <div id="has-map" class="has-map"/>
                                </div>
                            </el-col>
                        </el-row>
                        <!--</el-form>-->
                    </el-tab-pane>

                    <el-tab-pane label="收费方案" name="second">
                        <div class="mt10"></div>
                        <!--<el-form-->
                        <!--label-width="160px"-->
                        <!--size="small"-->
                        <!--:rules="formRules"-->
                        <!--ref="editform"-->
                        <!--:model="form"-->
                        <!--&gt;-->
                        <el-form-item label="选择收费方案(蓝牌)" prop="blue_charge_id" label-width="160px">
                            <el-select
                                    v-model="form.blue_charge_id"
                                    filterable
                                    size="small"
                                    placeholder="所有收费方案"
                                    class="w"
                            >
                                <el-option
                                        v-for="(item, i) in result.chargeProgramme_list"
                                        :key="i"
                                        :label="item.programme_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="选择收费方案(绿牌)" prop="green_charge_id" label-width="160px">
                            <el-select
                                    v-model="form.green_charge_id"
                                    filterable
                                    size="small"
                                    placeholder="所有收费方案"
                                    class="w"
                            >
                                <el-option
                                        v-for="(item, i) in result.chargeProgramme_list"
                                        :key="i"
                                        :label="item.programme_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="选择收费方案(黄牌)" prop="yellow_charge_id" label-width="160px">
                            <el-select
                                    v-model="form.yellow_charge_id"
                                    filterable
                                    size="small"
                                    placeholder="所有收费方案"
                                    class="w"
                            >
                                <el-option
                                        v-for="(item, i) in result.chargeProgramme_list"
                                        :key="i"
                                        :label="item.programme_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>

                    </el-tab-pane>
                </el-tabs>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancleEdit">取 消</el-button>
          <el-button type="primary" @click="saveEdit">提 交</el-button>
        </span>
            </template>
        </el-dialog>
        <!--</div>-->

        <!-- 路内收费说明 -->
        <el-dialog title="路内收费说明" v-model="ppVisible" width="560px">
            <el-form label-width="70px">
                <el-form-item label="路内名称">
                    <el-tag>{{ result.road.road_name }}</el-tag>
                </el-form-item>
                <el-form-item label="收费说明">
                    <el-input
                            type="textarea"
                            :rows="6"
                            v-model="result.road.charge_remark"
                    ></el-input>
                </el-form-item>
            </el-form>

            <template #footer>
        <span class="dialog-footer">
          <el-button @click="ppVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveRemark">确 定</el-button>
        </span>
            </template>
        </el-dialog>
        <!-- 详情弹出框 -->
        <el-dialog title="" v-model="viewVisible" width="40%">
            <el-descriptions
                    class="margin-top handle-box"
                    title="基本信息"
                    :column="3"
                    :size="size"
                    border
            >
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-user"></i>
                        姓名
                    </template>
                    赵冬梅
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-help"></i>
                        性别
                    </template>
                    女
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-time"></i>
                        年龄
                    </template>
                    40
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-mobile-phone"></i>
                        联系电话
                    </template>
                    15352925945
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-bank-card"></i>
                        身份证号
                    </template>
                    320323198107070620
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-picture-outline"></i>
                        照片
                    </template>
                    无
                </el-descriptions-item>
            </el-descriptions>
            <el-descriptions
                    class="margin-top"
                    title="值班信息"
                    :column="1"
                    :size="size"
                    border
            >
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-tickets"></i>
                        负责停车场
                    </template>
                    <el-tag size="small"
                    >川垣三路川垣三路南段西侧B（08:00:00 - 20:00:00）
                    </el-tag>
                </el-descriptions-item>
                <el-descriptions-item>
                    <template v-slot:label>
                        <i class="el-icon-office-building"></i>
                        负责总泊位数
                    </template>
                    25
                </el-descriptions-item>
            </el-descriptions>
        </el-dialog>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import {
        roadList,
        queryAreaData,
        delRoadAll,
        queryStreetData,
        editRoad,
        addRoad,
        chargeProgrammeData,
        dictData,
        exportRoadListData,
    } from "../../api/index";
    import {useRouter} from "vue-router";

    export default {
        name: "roadlist",
        components: {},
        data() {
            return {
                formRules: {
                    park_code: [{required: true, message: "必填项", trigger: "blur"}],
                    road_name: [{required: true, message: "必填项", trigger: "blur"}],
                    road_level: [{required: true, message: "必填项", trigger: "change"}],
                    area_id: [{required: true, message: "必填项", trigger: "change"}],
                    street_id: [{required: true, message: "必填项", trigger: "change"}],
                    address: [{required: true, message: "必填项", trigger: "blur"}],
                    // coordinate: [{ required: true, message: "必填项", trigger: "blur" }],
                    blue_charge_id: [
                        {required: true, message: "必填项", trigger: "change"},
                    ],
                    green_charge_id: [
                        {required: true, message: "必填项", trigger: "change"},
                    ],
                    yellow_charge_id: [
                        {required: true, message: "必填项", trigger: "change"},
                    ],
                },
                // charge: {
                //   blue_charge_id: [
                //     { required: true, message: "必填项", trigger: "blur" },
                //   ],
                //   green_charge_id: [
                //     { required: true, message: "必填项", trigger: "blur" },
                //   ],
                //   yellow_charge_id: [
                //     { required: true, message: "必填项", trigger: "blur" },
                //   ],
                // },
            };
        },
        setup() {
            const query = reactive({
                road_name: "",
                area_id: "",
                street_id: "",
                pageIndex: 1,
                pageSize: 15,
            });
            let form = reactive({
                park_code: "",
                traffic_park_code: "",
                road_name: "",
                road_level: "",
                area_id: "",
                street_id: "",
                blue_charge_id: "",
                green_charge_id: "",
                yellow_charge_id: "",
                coordinate: "",
                longitude: "",
                latitude: "",
                address: "",
                id: "",
                searchkey: "",
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            const router = useRouter();
            // 获取表格数据
            const getData = () => {
                roadList(query).then((res) => {
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
            };
            getData();

            // 结果集
            let result = reactive({
                area_list: [],
                street_list: [],
                query_street_list: [],
                road_levels: [],
                chargeProgramme_list: [],
                road: {},
                dialogT: "",
            });
            // 获取区域
            const getAreaData = () => {
                queryAreaData(query).then((res) => {
                    // console.log(res);
                    result.area_list = res.data;
                    // console.log(res.data);
                });
            };
            getAreaData();

            // 获取街道
            let queryStreet = reactive({
                areaId: query.area_id,
            });
            const getStreetData = (type) => {
                console.log("form");
                console.log(form);
                if (type == "a") {
                    queryStreet.areaId = query.area_id;
                    query.street_id = "";
                } else if (type == "b") {
                    queryStreet.areaId = form.area_id;
                    form.street_id = "";
                } else {
                    queryStreet.areaId = form.area_id;
                }
                console.log(queryStreet);
                queryStreetData(queryStreet).then((res) => {
                    console.log(res);
                    if (type == "a") {
                        result.query_street_list = res.data;
                    } else {
                        result.street_list = res.data;
                    }

                    // console.log(res.data);
                });
            };

            // 获取收费方案
            const getChargeProgrammeData = () => {
                chargeProgrammeData(query).then((res) => {
                    // console.log(res);
                    result.chargeProgramme_list = res.data;
                    //console.log(res.data);
                });
            };

            // 查询区域等级
            let dict = reactive({
                is_del: "0",
                dict_type: "roadLevel",
            });
            const getRoadLevel = () => {
                dictData(dict).then((res) => {
                    //console.log(res);
                    result.road_levels = res.data;
                });
            };

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
                        editRoad(edit_road)
                            .then((res) => {
                                console.log(res);
                                ElMessage.success("删除成功");
                            })
                            .then(() => {
                                getData();
                            });
                    })
                    .catch(() => {
                    });
            };

            // 停用操作
            const handleStop = (index, row, sta) => {
                // 二次确认停用
                ElMessageBox.confirm("确定要停用吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        let edit_road = reactive({
                            status: sta,
                            id: row.id,
                        });
                        editRoad(edit_road)
                            .then((res) => {
                                console.log(res);
                                ElMessage.success("停用成功");
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
            const ppVisible = ref(false);
            const viewVisible = ref(false);

            let idx = "";
            var timer = "";
            var map;
            // 新增编辑
            const handleEdit = (index, row, type) => {
                result.street_list = [];
                getAreaData();

                getRoadLevel();
                getChargeProgrammeData();

                if (type) {
                    result.dialogT = "新增";
                    idx = "";
                    form.park_code = "";
                    form.traffic_park_code = "";
                    form.id = "";
                    form.road_name = "";
                    form.road_level = "";
                    form.area_id = "";
                    form.street_id = "";
                    form.blue_charge_id = "";
                    form.green_charge_id = "";
                    form.yellow_charge_id = "";
                    form.longitude = "";
                    form.coordinate = "";
                    form.latitude = "";
                    form.address = "";
                } else {
                    result.dialogT = "编辑";
                    idx = row.id;

                    console.log(form);
                    Object.keys(form).forEach((item) => {
                        form[item] = row[item];
                    });
                    form.coordinate = form.latitude + "," + form.longitude;
                    getStreetData("c");
                    //console.log("form2");
                    //console.log(form);
                }
                timer = setTimeout(function () {
                    initMaps();
                }, 500);

                editVisible.value = true;
            };

            const initMaps = () => {
                if (document.getElementById("has-map")) {
                    var point = [34.24277, 117.184547];
                    if (form.coordinate != "" && form.coordinate != null) {
                        point[0] = form.coordinate.split(",")[0];
                        point[1] = form.coordinate.split(",")[1];
                    }
                    //console.log(point);
                    const center = new window.TMap.LatLng(point[0], point[1]);
                    // 初始化地图
                    map = new window.TMap.Map(document.getElementById("has-map"), {
                        rotation: 0, // 设置地图旋转角度
                        pitch: 0, // 设置俯仰角度（0~45）
                        zoom: 16, // 设置地图缩放级别
                        center: center, // 设置地图中心点坐标
                    });
                    var geocoder = new window.TMap.service.Geocoder(); // 新建一个正逆地址解析类
                    geocoder
                        .getAddress({location: center}) // 将给定的坐标位置转换为地址
                        .then((result) => {
                            form.searchkey = result.result.address;
                            // 显示查询到的地址
                        });
                    var markers = new window.TMap.MultiMarker({
                        map: map,
                        geometries: [
                            {
                                id: "main",
                                position: center, // 将得到的坐标位置用点标记标注在地图上
                            },
                        ],
                    });
                    // 创建信息窗口
                    const info = new window.TMap.InfoWindow({
                        map,
                        position: map.getCenter(),
                        offset: {x: 0, y: -52},
                    });
                    info.close();

                    //点击地图事件
                    map.on("click", (evt) => {
                        //console.log(evt)
                        info.open();
                        info.setPosition(evt.latLng);
                        var lat = evt.latLng.getLat().toFixed(6);
                        var lng = evt.latLng.getLng().toFixed(6);
                        info.setContent("当前坐标：" + lat + "," + lng);
                        form.coordinate = lat + "," + lng;
                        markers.setGeometries([]); // 将给定的地址转换为坐标位置
                        markers.updateGeometries([
                            {
                                id: "main",
                                position: evt.latLng, // 将得到的坐标位置用点标记标注在地图上
                            },
                        ]);
                        geocoder
                            .getAddress({location: evt.latLng}) // 将给定的坐标位置转换为地址
                            .then((result) => {
                                form.searchkey = result.result.address;
                                // 显示查询到的地址
                            });
                        //map.setCenter(evt.latLng);
                    });

                    //clearInterval(timer);
                    // }, 500);
                }
            };
            const searchMaps = () => {
                if (form.searchkey == "" || form.searchkey == null) {
                    ElMessage.warning("查询地址不能为空");
                } else {
                    if (form.searchkey.indexOf("徐州") == -1) {
                        form.searchkey = "徐州" + form.searchkey;
                    }

                    document.getElementById("has-map").innerHTML = "";
                    var point = [34.24277, 117.184547];
                    if (form.coordinate != "" && form.coordinate != null) {
                        point[0] = form.coordinate.split(",")[0];
                        point[1] = form.coordinate.split(",")[1];
                    }
                    //console.log(point);
                    const center = new window.TMap.LatLng(point[0], point[1]);
                    // 初始化地图
                    map = new window.TMap.Map(document.getElementById("has-map"), {
                        rotation: 0, // 设置地图旋转角度
                        pitch: 0, // 设置俯仰角度（0~45）
                        zoom: 16, // 设置地图缩放级别
                        center: center, // 设置地图中心点坐标
                    });

                    var geocoder = new window.TMap.service.Geocoder(); // 新建一个正逆地址解析类
                    var markers = new window.TMap.MultiMarker({
                        map: map,
                        geometries: [
                            {
                                id: "main",
                                position: center, // 将得到的坐标位置用点标记标注在地图上
                            },
                        ],
                    });
                    // 创建信息窗口
                    const info = new window.TMap.InfoWindow({
                        map,
                        position: map.getCenter(),
                        offset: {x: 0, y: -52},
                    });
                    info.close();
                    markers.setGeometries([]); // 将给定的地址转换为坐标位置
                    geocoder.getLocation({address: form.searchkey}).then((result) => {
                        markers.updateGeometries([
                            {
                                id: "main",
                                position: result.result.location, // 将得到的坐标位置用点标记标注在地图上
                            },
                        ]);
                        info.open();
                        info.setPosition(result.result.location);
                        var lat = result.result.location.getLat().toFixed(6);
                        var lng = result.result.location.getLng().toFixed(6);
                        info.setContent("当前坐标：" + lat + "," + lng);
                        map.setCenter(result.result.location);
                        form.coordinate = result.result.location.toString();
                        // 显示坐标数值
                        //console.log(form.coordinate);
                    });

                    //点击地图事件
                    map.on("click", (evt) => {
                        //console.log(evt)
                        info.open();
                        info.setPosition(evt.latLng);
                        var lat = evt.latLng.getLat().toFixed(6);
                        var lng = evt.latLng.getLng().toFixed(6);
                        info.setContent("当前坐标：" + lat + "," + lng);
                        form.coordinate = lat + "," + lng;
                        markers.setGeometries([]); // 将给定的地址转换为坐标位置
                        markers.updateGeometries([
                            {
                                id: "main",
                                position: evt.latLng, // 将得到的坐标位置用点标记标注在地图上
                            },
                        ]);
                        geocoder
                            .getAddress({location: evt.latLng}) // 将给定的坐标位置转换为地址
                            .then((result) => {
                                form.searchkey = result.result.address;
                                // 显示查询到的地址
                            });
                        //map.setCenter(evt.latLng);
                    });
                }

                //map.destroy();
            };
            //编辑
            const rEdit = (index, row) => {
                console.log(row.id);
                router.push({path: "/roadEdit", query: {id: row.id}});
            };
            // 收费说明
            const handleRemark = (index, row) => {
                result.road = row;

                ppVisible.value = true;
            };
            const handleView = (index, row) => {
                router.push({
                    path: "equipmentList",
                    query: {
                        road_id: row.id,
                        area_id: row.area_id,
                        street_id: row.street_id,
                        routerAlive: false,
                    },
                });
            };

            const cancleEdit = () => {
                map.destroy();
                editVisible.value = false;
            };
            const editform = ref(null);
            const saveEdit = () => {
                editform.value.validate((v) => {
                    if (v) {
                        form.longitude = form.coordinate.split(",")[1];
                        form.latitude = form.coordinate.split(",")[0];
                        console.log(form.coordinate.split(","));
                        if (form.longitude == null || form.longitude == "" ||
                            form.latitude == null || form.latitude == "") {
                            ElMessage.error("坐标不可为空！");
                            return false;
                        }
                        if (!idx) {
                            addRoad(form)
                                .then((res) => {
                                    console.log(res);
                                    ElMessage.success(res.msg);
                                })
                                .then(() => {
                                    query.pageIndex = 1;
                                    getData();
                                });
                        } else {
                            editRoad(form)
                                .then((res) => {
                                    console.log(res);
                                    ElMessage.success(res.msg);
                                })
                                .then(() => {
                                    getData();
                                });
                        }

                        editVisible.value = false;
                    }
                });

                // // 不可为空
                // if (
                //     form.park_code == null ||
                //     form.park_code == "" ||
                //   form.road_name == null ||
                //   form.road_name == "" ||
                //   form.address == null ||
                //   form.address == "" ||
                //   form.road_level == null ||
                //   form.road_level == "" ||
                //   form.area_id == null ||
                //   form.area_id == "" ||
                //   form.street_id == null ||
                //   form.street_id == "" ||
                //   form.yellow_charge_id == null ||
                //   form.yellow_charge_id == "" ||
                //   form.blue_charge_id == null ||
                //   form.blue_charge_id == "" ||
                //   form.green_charge_id == null ||
                //   form.green_charge_id == "" ||
                //   form.coordinate == null ||
                //   form.coordinate == ""
                // ) {
                //   ElMessage.error("参数不可为空！");
                //   return false;
                // }

            };
            const saveRemark = () => {
                ppVisible.value = false;

                let edit_road = reactive({
                    id: result.road.id,
                    charge_remark: result.road.charge_remark,
                });

                editRoad(edit_road).then((res) => {
                    console.log(res);
                    ElMessage.success("修改成功");
                });
            };

            return {
                query,
                timer,
                tableData,
                pageTotal,
                editVisible,
                saveRemark,
                ppVisible,
                viewVisible,
                queryStreet,
                form,
                result,
                searchMaps,
                handleStop,
                handleSearch,
                getStreetData,
                handlePageChange,
                getData,
                handleRemark,
                handleDelete,
                handleEdit,
                handleView,
                rEdit,
                saveEdit,
                cancleEdit,
                map,
                initMaps,
                multipleSelection: [],
                dialogImageUrl: "",
                activeName: "first",
                editform
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

            handleSelectionChange(data) {
                this.selectedData = data;
            },
            handleCommand(command) {
                // this.$message("click on item " + command);
            },
            handleDeleteAll() {
                ElMessageBox.confirm("确定要删除吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        var that = this;
                        var val = this.selectedData;
                        //console.log(val);
                        var ids = "";
                        if (val) {
                            val.forEach(function (item, index) {
                                //alert(item.id);
                                ids = ids + item.id + ",";
                            });
                            delRoadAll({roadIds: ids}).then((res) => {
                                ElMessage.success("删除成功");
                                that.getData();
                            });
                        } else {
                            ElMessage.warning(`请选择一条记录`);
                        }
                    })
                    .catch(() => {
                    });
            },
            exportRoad() {
                ElMessage.success("正在下载中·····");
                exportRoadListData(this.query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "路内列表.xls");
                    document.body.appendChild(link);
                    link.click();
                });
            },
        },
    };
</script>
