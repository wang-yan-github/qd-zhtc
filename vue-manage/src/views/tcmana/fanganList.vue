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
                            v-permission="['road_fanganlist_add', 'park_fanganlist_add']"
                    >添加
                    </el-button
                    >
                    <el-button
                            type="danger"
                            size="small"
                            icon="el-icon-delete"
                            @click="handleDeleteAll()"
                            v-permission="['road_fanganlist_deleteAll', 'park_fanganlist_deleteAll']"
                    >批量删除
                    </el-button
                    >
                    <el-button
                            type="success"
                            size="small"
                            icon="el-icon-document"
                            @click="excelImport()"
                            v-permission="['road_fanganlist_import', 'park_fanganlist_import']"
                    >导入
                    </el-button>
                </div>
                <div class="right-panel">
                    <el-form inline size="small">
                        <el-form-item label="市区">
                            <el-select
                                    clearable
                                    v-model="query.area_id"
                                    filterable
                                    size="small"
                                    placeholder="所有区域"
                                    class="w100"
                                    @change="getStreet"
                            >
                                <el-option
                                        v-for="item in formqjl.areas"
                                        :key="item.id"
                                        :label="item.area_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="街道" v-if="menuType == '0'">
                            <el-select
                                    clearable
                                    v-model="query.street_id"
                                    filterable
                                    size="small"
                                    placeholder="所有街道"
                                    class="w100"
                                    @change="getRoad"
                            >
                                <el-option
                                        v-for="item in formqjl.streets"
                                        :key="item.id"
                                        :label="item.street_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="街道" v-if="menuType == '1'">
                            <el-select
                                    clearable
                                    v-model="query.street_id"
                                    filterable
                                    size="small"
                                    placeholder="所有街道"
                                    class="w100"
                                    @change="getPark"
                            >
                                <el-option
                                        v-for="item in formqjl.streets"
                                        :key="item.id"
                                        :label="item.street_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="路内" v-if="menuType == '0'">
                            <el-select
                                    clearable
                                    v-model="query.road_id"
                                    filterable
                                    size="small"
                                    placeholder="所有路内"
                                    class="w100"
                            >
                                <el-option
                                        v-for="item in formqjl.roads"
                                        :key="item.id"
                                        :label="item.road_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="停车场" v-if="menuType == '1'">
                            <el-select
                                    clearable
                                    v-model="query.road_id"
                                    filterable
                                    size="small"
                                    placeholder="所有停车场"
                                    class="w100"
                            >
                                <el-option
                                        v-for="item in formqjl.roads"
                                        :key="item.id"
                                        :label="item.park_name"
                                        :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item>
                            <el-input
                                    @keyup.enter="handleSearch()"
                                    size="small"
                                    v-model="query.programme_name"
                                    placeholder="请输入方案名称"
                                    class="handle-input mr10"
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="">
                            <el-button
                                    size="small"
                                    type="primary"
                                    icon="el-icon-search"
                                    @click="handleSearch"
                            >查询
                            </el-button>
                        </el-form-item>

                    </el-form>
                </div>
                <!-- <div class="clear"></div> -->
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
                        prop="programme_name"
                        label="收费方案名称"
                        align="center"
                ></el-table-column>
                <!-- <el-table-column
                        prop="roads"
                        label="应用路内"
                >
                    <template #default="scope">
                        <span v-for="(u,j) in scope.row.roads" :key="j">{{u.road_name}}&nbsp;&nbsp;</span>
                    </template>
                </el-table-column> -->

                <el-table-column prop="roads" label="应用路内">
                    <template #default="scope">
                        <div class="no-warp">
                            <el-tag
                                    size="small"
                                    v-for="(item, index) in scope.row.roads"
                                    :key="index"
                                    class="mar5"
                                    @click="handleView(scope.$index, scope.row,0)">{{ item.road_name }}
                            </el-tag>
                        </div>


                    </template>
                </el-table-column>

                <!-- <el-table-column
                        prop="parks"
                        label="应用停车场"
                >
                    <template #default="scope">
                        <span v-for="(u,j) in scope.row.parks" :key="j">{{u.park_name}}&nbsp;&nbsp;</span>
                    </template>
                </el-table-column> -->

                <el-table-column prop="parks" label="应用停车场">
                    <template #default="scope">
                        <div class="no-warp">
                            <el-tag
                                size="small"
                                v-for="(item, index) in scope.row.parks"
                                :key="index"
                                class="mar5"
                                @click="handleView(scope.$index, scope.row,1)"
                            >{{ item.park_name }}
                            </el-tag>
                        </div>

                    </template>
                </el-table-column>

                <el-table-column
                        prop="limit_price_amount"
                        label="24小时限价"
                        width="100"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="create_time"
                        label="添加时间"
                        width="180"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="name"
                        label="添加人"
                        width="100"
                        align="center"
                ></el-table-column>

                <el-table-column label="操作" width="180" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row, false)"
                                v-permission="['road_fanganlist_edit', 'park_fanganlist_edit']"
                        >编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-delete"
                                @click="handleDelete(scope.$index, scope.row)"
                                class="red"
                                v-permission="['road_fanganlist_delete', 'park_fanganlist_delete']"
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

        <!-- 编辑弹出框 -->
        <el-dialog :title="form.dialogT" v-model="editVisible" width="45%" top="2vh">
            <el-form label-width="120px" size="small" :rules="formRules" :model="form">
                <el-form-item label="收费方案名称" prop="programme_name">

                    <el-input v-model="form.programme_name" request></el-input>
                </el-form-item>

                <el-form-item label="24小时限价设定">
                    <el-radio-group v-model="form.is_limit_price" @change="changeHandler">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item label="设置限价金额" prop="limit_price_amount">
                    <el-input v-model="form.limit_price_amount" placeholder="请输入价格"
                              :disabled="form.is_limit_price==0 ? true:false"
                    >
                        <template v-slot:append>元</template>
                    </el-input
                    >
                </el-form-item>
                <el-tabs type="card" v-model="activeName">
                    <el-tab-pane name="first">
                        <template v-slot:label
                        ><i class="el-icon-sunny"></i> 白天时段
                        </template
                        >
                        <el-form-item label="收费时段" prop="startTime">
                            <el-time-picker
                                    placeholder="起始时间"
                                    v-model="form.startTime"
                                    format="HH:mm"
                                    value-format="HH:mm"
                                    :picker-options="{
                 selectableRange: '18:30 - 20:30'
                }"
                            >
                            </el-time-picker>
                            <span class="ml5 dispinline"></span>
                            <el-time-picker
                                    placeholder="结束时间"
                                    v-model="form.endTime"
                                    format="HH:mm"
                                    value-format="HH:mm"
                                    :picker-options="{
                 selectableRange: '18:30 - 20:30'
                }"
                            >
                            </el-time-picker>
                        </el-form-item>
                        <el-form-item>
                            提示：白天开始时间等于夜晚结束时间，白天结束时间等于夜晚开始时间。
                        </el-form-item>
                        <el-form-item label="免费停车时长" prop="free_time">
                            <el-input v-model="form.free_time"
                            >
                                <template v-slot:append>分</template>
                            </el-input
                            >
                        </el-form-item>
                        <el-form-item label="分段限价设定" prop="interval_limit_price">
                            <el-input placeholder="设置为0时表示不设置分段限价" v-model="form.interval_limit_price"
                            >
                                <template v-slot:append>元</template>
                            </el-input
                            >
                        </el-form-item>
                        <el-form-item label="收费方式">
                            <el-radio-group v-model="form.charge_type">
                                <el-radio :label="1">分时</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="收费单位" prop="charge_unit">
                            <el-input v-model="form.charge_unit"
                            >
                                <template v-slot:append>分</template>
                            </el-input
                            >
                        </el-form-item>
                        <el-form-item label="时长价格设定">
                            <table class="w desctable" :key="keysone">
                                <colgroup>
                                    <col/>
                                    <col/>
                                    <col/>
                                </colgroup>
                                <thead>
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>价格</th>
                                <th>
                                    <el-button
                                            round
                                            size="mini"
                                            type="primary"
                                            icon="el-icon-plus"
                                            @click="addItem('a')"
                                    ></el-button>
                                </th>
                                </thead>
                                <tbody>
                                <tr v-for="(item, index) in formArr" :key="index">
                                    <td>
                                        <el-input v-model="item.start_minute"
                                        >
                                            <template v-slot:prepend>从</template>
                                            <template v-slot:append>分</template>
                                        </el-input
                                        >
                                    </td>
                                    <td>

                                        <el-input v-model="item.end_minute"
                                        >
                                            <template v-slot:prepend>至</template>
                                            <template v-slot:append>分</template>
                                        </el-input
                                        >

                                    </td>
                                    <td>
                                        <el-input v-model="item.price" oninput="
                                           if (isNaN(value)) { value = null }
                                           if (value.charAt(0) === '0' && value.charAt(1) !== '' && value.charAt(1) !== '.') {
                                            value = '0'
                                           }
                                           if (value.indexOf('.') > 0) { value = value.slice(0, value.indexOf('.') + 3)}"
                                        >
                                            <template v-slot:append>元</template>
                                        </el-input>
                                    </td>
                                    <td align="center">
                                        <el-button
                                                round
                                                size="mini"
                                                type="danger"
                                                icon="el-icon-delete"
                                                @click="delItem('a',index)"
                                        ></el-button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </el-form-item>
                    </el-tab-pane>
                    <el-tab-pane name="second">
                        <template v-slot:label
                        ><i class="el-icon-moon-night"></i> 夜间时段
                        </template
                        >
                        <el-form-item label="收费时段" prop="startTime1">
                            <el-time-picker
                                    placeholder="起始时间"
                                    v-model="form.startTime1"
                                    format="HH:mm"
                                    value-format="HH:mm"
                                    :picker-options="{
                 selectableRange: '18:30 - 20:30'
                }"
                            >
                            </el-time-picker>
                            <span class="ml5 dispinline"></span>
                            <el-time-picker
                                    placeholder="结束时间"
                                    v-model="form.endTime1"
                                    format="HH:mm"
                                    value-format="HH:mm"
                                    :picker-options="{
                 selectableRange: '18:30 - 20:30'
                }"
                            >
                            </el-time-picker>
                        </el-form-item>
                        <el-form-item>
                            提示：1.白天开始时间等于夜晚结束时间，白天结束时间等于夜晚开始时间。<br/>
                            2.或者白天开始时间等于结束时间。3.或者夜晚开始时间等于夜晚结束时间。
                        </el-form-item>
                        <el-form-item label="免费停车时长" prop="free_time1">
                            <el-input v-model="form.free_time1"
                            >
                                <template v-slot:append>分</template>
                            </el-input
                            >
                        </el-form-item>
                        <el-form-item label="分段限价设定" prop="interval_limit_price1">
                            <el-input v-model="form.interval_limit_price1"
                            >
                                <template v-slot:append>元</template>
                            </el-input
                            >
                        </el-form-item>
                        <el-form-item label="收费方式">
                            <el-radio-group v-model="form.charge_type1">
                                <el-radio :label="1">分时</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="收费单位" prop="charge_unit1">
                            <el-input v-model="form.charge_unit1"
                            >
                                <template v-slot:append>分</template>
                            </el-input
                            >
                        </el-form-item>
                        <el-form-item label="时长价格设定">
                            <table class="w desctable" :key="keysone1">
                                <colgroup>
                                    <col/>
                                    <col/>
                                    <col/>
                                </colgroup>
                                <thead>
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>价格</th>
                                <th>
                                    <el-button
                                            round
                                            size="mini"
                                            type="primary"
                                            icon="el-icon-plus"
                                            @click="addItem('b')"
                                    ></el-button>
                                </th>
                                </thead>
                                <tbody>
                                <tr v-for="(item, index) in formArr1" :key="index">
                                    <td>
                                        <el-input v-model="item.start_minute"
                                        >
                                            <template v-slot:prepend>从</template>
                                            <template v-slot:append>分</template>
                                        </el-input
                                        >
                                    </td>
                                    <td>

                                        <el-input v-model="item.end_minute"
                                        >
                                            <template v-slot:prepend>至</template>
                                            <template v-slot:append>分</template>
                                        </el-input
                                        >

                                    </td>
                                    <td>
                                        <el-input type="text" v-model="item.price" oninput="
                                           if (isNaN(value)) { value = null }
                                           if (value.charAt(0) === '0' && value.charAt(1) !== '' && value.charAt(1) !== '.') {
                                            value = '0'
                                           }
                                           if (value.indexOf('.') > 0) { value = value.slice(0, value.indexOf('.') + 3)}"
                                        >
                                            <template v-slot:append>元</template>
                                        </el-input
                                        >
                                    </td>
                                    <td align="center">
                                        <el-button
                                                round
                                                size="mini"
                                                type="danger"
                                                icon="el-icon-delete"
                                                @click="delItem('b',index)"
                                        ></el-button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </el-form-item>
                    </el-tab-pane>
                </el-tabs>
            </el-form>

            <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
            </template>
        </el-dialog>

        <!-- 详情弹出框 -->
        <el-dialog title="地址列表" v-model="viewVisible" @close="closeAddress" width="840px">
            <div class="index-bar-content">
                <el-scrollbar style="height: 100%" ref="scrollbar">
                    <div :id="key" class="main-list" v-for="(value, key) in indexData" :key="key" ref="listGroup">
                    <div class="title-key">{{ key }}</div>
                    <div class="content-container">
                        <div class="content-item" v-for="(val, index) in value" :key="index">
                            <el-tag size="small">{{ val.name }}</el-tag>

                        </div>
                    </div>
                    </div>
                </el-scrollbar>
                <!-- 右侧字母列表 -->
                <ul class="char-list">
                    <li
                    v-for="(item,index) in indexList"
                    :key="item.index"
                    @click="scrollToLetter(item)"
                    :class="{'active':currentIndex === item.id}"
                    >
                    {{ item.key }}
                    <!-- {{ item.id }}
                    {{currentIndex}} -->
                    </li>

                </ul>
            </div>
        </el-dialog>
        <el-dialog
                width="650px"
                title="收费方案模板"
                v-model="viewVisibleExcel"
                :close-on-click-modal="false"
        >
            <el-form label-width="110px" class="excel-form">
                <el-form-item label="模板下载">
                    <el-button
                            type="success"
                            size="small"
                            icon="el-icon-download"
                            @click="download()"
                    >Excel 下载
                    </el-button>
                </el-form-item>
                <el-form-item label="批量导入">
                    <input type="file" id="inputFile" @change="exportImport()" hidden/>
                    <el-button size="small" ref="inputFile" @click="inputFileClick()"
                    >Excel导入
                    </el-button
                    >
                </el-form-item>
            </el-form>
            <template #footer>
                <!-- <span class="dialog-footer">
                                <el-button @click="viewVisibleExcel = false">取 消</el-button>
                                <el-button type="primary" @click="viewVisibleExcel = false">确 定</el-button>
                            </span> -->
            </template>
        </el-dialog>

    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance, nextTick,onMounted,computed } from "vue";
    // import Vue from "vue";
    import axios from "axios";
    import { ElMessage, ElMessageBox ,ElScrollbar} from "element-plus";
    import pinyin from "js-pinyin"
    import {
        chargeProgrammeList,
        delProgrammeAll,
        addChargeProgramme,
        getChargeProgramme,
        delChargeProgramme,
        editChargeProgramme,
        downloadWhite,
        queryStreetData, queryRoadData, queryParkData, queryAreaData
    } from "../../api/index";
    import File_URL from "../../file_url";
    // import {downloadWhite} from "../../api/operateCar";
    // Vue.forceUpdate();

    export default {
        name: "fanganlist",
        components: {ElScrollbar},
        data() {
            return {
                tableH: 0,
                formRules: {
                    programme_name: [{required: true, message: "必填项", trigger: "blur"}],
                    limit_price_amount: [{required: true, message: "必填项", trigger: "blur"}],

                    startTime: [{required: true, message: "必填项", trigger: "blur"}],

                    free_time: [{required: true, message: "必填项", trigger: "blur"}],
                    interval_limit_price: [{required: true, message: "必填项", trigger: "blur"}],
                    charge_unit: [{required: true, message: "必填项", trigger: "blur"}],
                    // formArr: [{ required: true, message: "必填项", trigger: "blur" }],
                    startTime1: [{required: true, message: "必填项", trigger: "blur"}],

                    free_time1: [{required: true, message: "必填项", trigger: "blur"}],
                    interval_limit_price1: [{required: true, message: "必填项", trigger: "blur"}],
                    charge_unit1: [{required: true, message: "必填项", trigger: "blur"}],
                    // formArr1: [{ required: true, message: "必填项", trigger: "blur" }],

                },
            };
        },
        setup() {
            const { proxy } = getCurrentInstance();
            const menuType = localStorage.getItem("menuType");

            const query = reactive({
                programme_name: "",
                pageIndex: 1,
                pageSize: 15,
                menuType: menuType,
            });
            const tableData = ref([]);
            const tableData11 = ref([]);
            const pageTotal = ref(0);
            const startTime = ref();
            // 获取表格数据
            const getData = () => {
                chargeProgrammeList(query).then((res) => {
                    console.log(res.data);
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
            };


            getData();
            const formArr = ref([{id: "", start_minute: "", end_minute: "", price: ""}]);
            const formArr1 = ref([{id: "", start_minute: "", end_minute: "", price: ""}]);
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

            //获取街道下拉框数据
            let queryStreet = reactive({
                areaId: query.area_id,
            });
            let formqjl = reactive({
                time: "",
                areaId: "",
                streetId: "",
                roadId: "",
                areas: [],
                streets: [],
                roads: [],
            });
            const getStreet = () => {
                queryStreet.areaId = query.area_id;
                query.street_id = "";
                query.road_id = "";
                queryStreetData(queryStreet).then((res) => {
                    formqjl.streets = res.data;
                });
            };
            //获取区域下拉框数据
            const queryArea = reactive({});
            const getArea = () => {
                queryAreaData(queryArea).then((res) => {
                    formqjl.areas = res.data;
                });
            };
            getArea();
            //获取路内下拉框数据
            const queryRoad = reactive({
                streetId: query.street_id,
            });
            const getRoad = () => {
                queryRoad.streetId = query.street_id;
                query.road_id = "";
                queryRoadData(queryRoad).then((res) => {
                    formqjl.roads = res.data;
                });
            };
            //获取停车场下拉框数据
            const getPark = () => {
                queryRoad.streetId = query.street_id;
                query.road_id = "";
                queryParkData(queryRoad).then((res) => {
                    formqjl.roads = res.data;
                });
            };

            // 删除操作
            const handleDelete = (index, row) => {
                // 二次确认删除
                ElMessageBox.confirm("确定要删除吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        console.log(row.id);
                        let del_chargeProgramme = reactive({
                            id: row.id,
                            is_del: "1",
                        });
                        delChargeProgramme(del_chargeProgramme).then((res) => {
                            ElMessage.success("删除成功");
                        }).then(() => {
                            getData();
                        });
                    })
                    .catch(() => {
                    });
            };

            const viewVisibleExcel = ref(false);
            // 导入
            const excelImport = () => {
                viewVisibleExcel.value = true;
            };
            const download = () => {
                downloadWhite(reactive({})).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "收费方案配置申请表.xlsx");
                    document.body.appendChild(link);
                    link.click();
                });
            };

            let file = reactive({
                excel: "",
            });
            //模拟上传按钮点击
            const inputFileClick = () => {
                var e = document.createEvent("MouseEvents");
                e.initEvent("click", true, true);
                document.getElementById("inputFile").dispatchEvent(e);
            };
            //导入excel
            const exportImport = () => {
                var formData = new FormData();
                formData.append("file", document.getElementById("inputFile").files[0]);
                axios({
                    url: File_URL.file_down + "chargeCrogramme/importChargeProgramme.do",
                    method: "POST",
                    data: formData,
                    headers: {
                        "Content-Type": "multipart/form-data",
                        Authorization: localStorage.getItem("token_value"),
                    },
                }).then((res) => {
                    var data = res.data;
                    if (data.code == 0) {
                        ElMessage({
                            showClose: true,
                            message: data.data,
                            type: "success",
                        });

                        viewVisibleExcel.value = false;
                        getData();
                    } else {
                        ElMessage({
                            showClose: true,
                            message: data.msg,
                            type: "error",
                        });
                    }
                    //清空
                    document.getElementById("inputFile").value = "";
                });
            };


            // 表格编辑时弹窗和保存
            const editVisible = ref(false);

            const ppVisible = ref(false);
            let form = reactive({
                name: "",
                address: "",
                dialogT: "",
                czroptions: [
                    {
                        value: "1",
                        label: "平台管理员",
                    },
                    {
                        value: "2",
                        label: "管理员",
                    },
                    {
                        value: "3",
                        label: "车场",
                    },
                ],
                radio2: "1",
                radio1: "时分",
                programme_name: "",
                is_limit_price: "",
                limit_price_amount: "",
                startTime: "",
                startTime1: "",
                endTime: "",
                endTime1: "",
                free_time: "",
                free_time1: "",
                interval_limit_price: "",
                interval_limit_price1: "",
                charge_type: "",
                charge_type1: "",
                charge_unit: "",
                charge_unit1: ""
            });
            let idx = "";
            const changeHandler = (value) => {
                if (value == 0) {
                    form.limit_price_amount = 0;
                }
                if (value == 1) {
                    form.limit_price_amount = "";
                }
            }
            const handleEdit = (index, row, type) => {
                if (type) {
                    form.dialogT = '新增'
                    idx = "";
                    form.programme_name = "";
                    form.is_limit_price = 1;
                    form.limit_price_amount = "";
                    form.startTime = "";
                    form.startTime1 = "";
                    form.endTime = "";
                    form.endTime1 = "";
                    form.free_time = "";
                    form.free_time1 = "";
                    form.interval_limit_price = "";
                    form.interval_limit_price1 = "";
                    form.charge_type = 1;
                    form.charge_type1 = 1;
                    form.charge_unit = "";
                    form.charge_unit1 = "";
                    formArr.value = [{id: "", start_minute: "", end_minute: "", price: ""}];
                    formArr1.value = [{id: "", start_minute: "", end_minute: "", price: ""}];
                } else {
                    form.dialogT = '编辑';
                    idx = row.id;
                    getChargeProgramme(reactive({id: row.id})).then((res) => {
                        console.log(res.data);
                        form.programme_name = res.data.programme_name;
                        console.log();
                        form.is_limit_price = res.data.is_limit_price;
                        form.limit_price_amount = res.data.limit_price_amount;
                        form.startTime = res.data.days.start_time;
                        form.startTime1 = res.data.night.start_time;
                        form.endTime = res.data.days.end_time;
                        form.endTime1 = res.data.night.end_time;
                        form.free_time = res.data.days.free_time;
                        form.free_time1 = res.data.night.free_time;
                        form.interval_limit_price = res.data.days.interval_limit_price;
                        form.interval_limit_price1 = res.data.night.interval_limit_price;
                        form.charge_type = res.data.days.charge_type;
                        form.charge_type1 = res.data.night.charge_type;
                        form.charge_unit = res.data.days.charge_unit;
                        form.charge_unit1 = res.data.night.charge_unit;

                        formArr.value = res.data.daysTime;
                        formArr1.value = res.data.nightTime;

                    });

                }

                editVisible.value = true;
            };



            const saveEdit = () => {
                if (form.startTime == null) {
                    form.startTime = "";
                }
                if (form.startTime1 == null) {
                    form.startTime1 = "";
                }
                if (form.endTime == null) {
                    form.endTime = "";
                }
                if (form.endTime1 == null) {
                    form.endTime1 = "";
                }

                let flag = true;

                if (form.programme_name == null || form.programme_name === "" || form.limit_price_amount == null ||
                    form.limit_price_amount === "") {
                    ElMessage.error("参数不可为空！");
                    flag = false;
                }
                console.log(form.limit_price_amount <= 0);
                if (form.is_limit_price == 1 && form.limit_price_amount <= 0) {
                    ElMessage.error("24小时限价金额大于等于0！");
                    flag = false;
                }


                // 参数都为空
                if (form.startTime === "" && form.endTime === "" && form.startTime1 === "" && form.endTime1 === "") {

                    ElMessage.error("收费时段不符合规则！");
                    flag = false;

                } else { // 参数不为空
                    //1. 白天开始不等于夜晚结束，白天结束不等于夜晚开始
                    if ((form.startTime != form.endTime1) || (form.endTime != form.startTime1)) {
                        console.log("--" + form.startTime + "--");
                        console.log("--" + form.endTime + "--");
                        // 2. 白天开始时间不等于白天结束或夜晚开始不等于夜晚结束
                        if ((form.startTime != form.endTime) || (form.startTime1 != form.endTime1)) {
                            ElMessage.error("收费时段不符合规则！");
                            flag = false;

                        }

                        // 3.白天时段为空，说明设置的夜晚时段
                        if (form.startTime === "" || form.startTime == null) {
                            formArr1.value.forEach(function (item) {

                                if (item.price == null || item.price === "" || item.start_minute == null || item.start_minute === "" ||
                                    item.end_minute == null || item.end_minute === "") {
                                    console.log(item);

                                    ElMessage.error("收费时长不符合规则！");
                                    flag = false;
                                }
                            })
                        } else {
                            formArr.value.forEach(function (item) {

                                if (item.price == null || item.price === "" || item.start_minute == null || item.start_minute === "" ||
                                    item.end_minute == null || item.end_minute === "") {
                                    console.log(item);

                                    ElMessage.error("收费时长不符合规则！");
                                    flag = false;
                                }
                            })
                        }

                    } else {// 符合白天开始等于夜晚结束，夜晚开始等于白天结束规则，开始校验时段

                        formArr.value.forEach(function (item) {
                            console.log(0 == "");
                            if (item.price == null || item.price === "" || item.start_minute == null || item.start_minute === "" ||
                                item.end_minute == null || item.end_minute === "") {
                                console.log(item);

                                ElMessage.error("收费时长不符合规则！");
                                flag = false;
                            }
                        })

                        formArr1.value.forEach(function (item) {
                            console.log(0 == "");
                            if (item.price == null || item.price === "" || item.start_minute == null || item.start_minute === "" ||
                                item.end_minute == null || item.end_minute === "") {
                                console.log(item);

                                ElMessage.error("收费时长不符合规则！");
                                flag = false;
                            }
                        })

                    }

                }

                if (!flag) {
                    return false;
                }


                //
                // formArr.forEach(function(item){
                //     console.log(item);
                // })


                // 白天
                let day_s = {
                    inteval_type: "0",
                    start_time: form.startTime,
                    end_time: form.endTime,
                    free_time: form.free_time,
                    interval_limit_price: form.interval_limit_price,
                    charge_type: form.charge_type,
                    charge_unit: form.charge_unit
                };
                // 黑夜
                let night_s = {
                    inteval_type: "1",
                    start_time: form.startTime1,
                    end_time: form.endTime1,
                    free_time: form.free_time1,
                    interval_limit_price: form.interval_limit_price1,
                    charge_type: form.charge_type,
                    charge_unit: form.charge_unit1
                };

                // 收费时段

                let formArray = JSON.stringify(formArr.value);

                console.log(111);
                console.log(formArr);

                console.log(idx);


                let charge_programme = {
                    id: idx,
                    programme_name: form.programme_name,
                    is_limit_price: form.is_limit_price,
                    limit_price_amount: form.limit_price_amount,
                    days: day_s,
                    night: night_s,
                    nightTime: formArr1.value,
                    daysTime: formArr.value
                };

                console.log(charge_programme);

                if (!idx) {
                    addChargeProgramme(reactive({charge_programme: JSON.stringify(charge_programme)})).then((res) => {
                        console.log(res.data);

                    }).then(() => {
                        query.pageIndex = 1;
                        getData();
                    });
                } else {
                    editChargeProgramme(reactive({charge_programme: JSON.stringify(charge_programme)})).then((res) => {
                        console.log(res.data);

                    }).then(() => {
                        query.pageIndex = 1;
                        getData();
                    });
                }


                editVisible.value = false;


                // console.log(form);
                // console.log(formArr);
                // console.log(formArr1);
                // editVisible.value = false;
                // ElMessage.success(`修改第 ${idx + 1} 行成功`);
                // Object.keys(form).forEach((item) => {
                //   tableData.value[idx][item] = form[item];
                // });
            };

            // 详情地址
            let listGroup = ref(null);
            let scrollbar = ref(null);
            let charList = ref([]);
            let listHeight = ref([]);
            let currentIndex = ref(0);
            let indexPage = ref(1);
            let indexLimit = ref(24);
            let indexData = ref({});

            const viewVisible = ref(false);
            const handleView = (index,row,type) => {
                nextTick(() => {
                    calculateHeight()
                    handleScroll()
                });

                // 获取roadsList 对象中的road_name 并生成新的数组 键值对 为 name：road_name
                let roadsList = [];
                if(type == 0){
                    row.roads.forEach(function (item) {
                        roadsList.push(item.road_name);
                    });
                }else{
                    row.parks.forEach(function (item) {
                        roadsList.push(item.park_name);
                    });
                }

                // 使得roadsList 为对象为name：road_name
                roadsList = roadsList.map(function (item) {
                    return {name: item}
                });
                pinyin.setOptions({ checkPolyphone: false, charCase: 0 })
                    let alphabet = []
                    let _charList = []
                    for (let i = 0; i < roadsList.length; i++) {
                        // 获取原数组每一项的 name 值
                        let name = roadsList[i].name
                        // 获取每一个name值第一个字的大写首字母（传入的 name 是中文时默认得到大写字母，name 是英文时按照原字符串输出，可能是小写）
                        let initial = pinyin.getCamelChars(name).substring(0, 1).toUpperCase()
                        // 给数组每一项增加名为 initial 的 key，值就是第一个字的大写首字母
                        roadsList[i].initial = initial
                        // 获取用于索引的字母
                        if (alphabet.indexOf(initial) === -1) {
                        alphabet.push(initial)
                        }
                    }
                    // 按字母表顺序排序
                    alphabet.sort()

                    // 给每个字母增加唯一标识，后面定位时会用到
                    for (var i = 0; i < alphabet.length; i++) {
                        _charList.push({
                        id: i,
                        key: alphabet[i]
                        })
                    }



                    // 生成 charList.value 为id 从0开始， key 从A-Z  的数组
                    charList.value = []
                    for (var i = 0; i < 26; i++) {
                        charList.value.push({
                        id:'',
                        key: String.fromCharCode(65 + i)
                        })
                    }

                    // 对比charList.value 和 _charList，如果_charList中的key值和charList.value中的key值相同，则将_charList中的id值赋值给charList.value 中的id值
                    for (var i = 0; i < _charList.length; i++) {
                        for (var j = 0; j < charList.value.length; j++) {
                        if (_charList[i].key === charList.value[j].key) {
                            charList.value[j].id = _charList[i].id
                        }
                        }
                    }

                    let resultData = {}
                    // 将源数据按照首字母分类
                    for (let i = 0; i < alphabet.length; i++) {
                        resultData[alphabet[i]] = roadsList.filter((item) => {
                        return item.initial === alphabet[i]
                        })
                    }
                    // 得到最终结果
                    indexData.value = resultData

                    // getDataList(sourdata);

                    viewVisible.value = true;
                };

                const calculateHeight = () => {
                    listHeight = []
                    nextTick(() => {
                        const list = listGroup.value
                        let height = 0
                        listHeight.push(height)
                        if (list) {
                            for (let i = 0; i < list.length; i++) {
                                let item = list[i]
                                height += item.clientHeight
                                listHeight.push(height)
                            }
                        }

                    })

                };
                const scrollToLetter = (item) => {
                    // 判断item.id是否为空，为空则不执行
                    if (item.id === '' || item.id === undefined) {
                        return
                    }
                    nextTick(() => {
                        scrollbar.value.$el.querySelector('.el-scrollbar__wrap').scrollTop = listHeight[item.id]
                        currentIndex.value = item.id
                    })

                };
                const handleScroll = () => {
                    nextTick(() => {
                        const scrollEle = scrollbar.value.$el.querySelector('.el-scrollbar__wrap');
                        scrollEle.onscroll = () => {
                            let newY = scrollEle.scrollTop
                            const listHeightSet = listHeight
                            // 在中间部分滚动
                            for (let i = 0; i < listHeightSet.length - 1; i++) {
                            let height1 = listHeightSet[i]
                            let height2 = listHeightSet[i + 1]
                            if (height1 <= newY && newY < height2) {
                                currentIndex.value = i
                                // console.log(this.currentIndex, "currentIndex")
                                let currentPage = Math.floor(i / indexLimit) + 1
                                indexPage = currentPage
                                return
                            }
                            }
                        }

                    })

                };

                const closeAddress = () =>{
                    scrollbar.value.$el.querySelector('.el-scrollbar__wrap').scrollTop = 0
                    currentIndex.value = 0
                    viewVisible.value = false;
                };


            return {
                query,
                tableData,
                tableData11,
                formArr,
                formArr1,
                pageTotal,
                editVisible,
                form,
                startTime,
                changeHandler,
                getData,
                getArea,
                getStreet,
                getRoad,
                getPark,
                handleSearch,
                handlePageChange,
                handleDelete,
                handleEdit,
                saveEdit,
                multipleSelection: [],
                dialogImageUrl: "",
                ppVisible: false,
                activeName: "first",
                keysone: 0,
                keysone1: 0,
                formqjl,
                viewVisibleExcel,
                excelImport,
                inputFileClick,
                exportImport,
                download,
                menuType,
                // 详情地址
                viewVisible,
                handleView,
                charList,
                listHeight,
                currentIndex,
                indexPage,
                indexLimit,
                indexData,
                listGroup,
                scrollbar,
                calculateHeight,
                scrollToLetter,
                handleScroll,
                closeAddress,
            };
        },
        computed: {
            indexList() {
                return this.charList
            }
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + 'px';
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
                }).then(() => {
                    var that = this;
                    var val = this.selectedData;
                    console.log(val);
                    var ids = "";
                    if (val) {
                        val.forEach(function (item, index) {
                            //alert(item.id);
                            ids = ids + item.id + ",";
                        });
                        delProgrammeAll({programmeIds: ids}).then((res) => {
                            ElMessage.success("删除成功");
                            that.getData();
                        })
                    } else {
                        ElMessage.warning(`请选择一条记录`);
                    }
                }).catch(() => {
                });
            },
            addItem(type) {
                var that = this;
                if (type == 'a') {
                    console.log(type);
                    that.formArr.push({
                        id: "",
                        start_minute: "",
                        end_minute: "",
                        price: "",
                    });
                    this.keysone += 1;
                } else {
                    console.log(type);
                    that.formArr1.push({
                        id: "",
                        start_minute: "",
                        end_minute: "",
                        price: "",
                    });
                    this.keysone1 += 1;
                }

                //that.$refs.addTable.clearSelection();
                //console.log(this.formArr);
                // this.$nextTick(function () {
                //   this.formArr;
                // });

                //this.$forceUpdate
            },
            delItem(type, index) {
                var that = this;
                if (type == 'a') {
                    if (that.formArr.length == 1) {
                        ElMessage.warning(`至少保留一条记录`);
                    } else {
                        that.formArr.splice(index, 1);
                        console.log(that.formArr);
                    }
                    this.keysone -= 1;
                } else {
                    if (that.formArr1.length == 1) {
                        ElMessage.warning(`至少保留一条记录`);
                    } else {
                        that.formArr1.splice(index, 1);
                    }
                    this.keysone1 -= 1;
                }

                //that.$refs.addTable.clearSelection();
                //this.$forceUpdate

                // this.$nextTick(function () {
                //   this.formArr;
                // });
            },
        },
    };
</script>
<style scoped>
.index-bar-content {
  position: relative;
  width: 800px;
  height: 600px;
}
.el-scrollbar__wrap {
    overflow-x: hidden;

  }
  .el-scrollbar__view {
      padding: 0 20px;
    }
    .main-list {
    padding-top: 10px;

  }
  .title-key {
      padding-bottom: 12px;
      font-size: 14px;
      font-weight: bold;
    }
    .content-container {
      display: flex;
      flex-wrap: wrap;
      padding-right: 80px;
      box-sizing: border-box;
    }
    .content-item {
        margin-right: 16px;
        margin-bottom: 12px;
        font-size: 12px;
      }
.el-popover {
  padding: 0;
}
.char-list {
  z-index: 99;
  width: 24px;
  height: 100%;
  background: #fafbfe;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  list-style: none;
  display: flex;
  flex-direction: column;
  text-align: center;

}
.char-list li {
    height: 19px;
    display: inline;
    cursor: pointer;
    font-size: 14px;
  }
  .char-list .active {
    color: #E6A23C;
  }
  .no-warp{
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    cursor: pointer;
  }
</style>
