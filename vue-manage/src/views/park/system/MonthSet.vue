<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd(0, null, true)"
                               v-permission="'park_monthSetA_add'">添加
                    </el-button>
                    <el-button type="danger" size="small" icon="el-icon-delete" @click="handleDeleteAll()"
                               v-permission="'park_monthSetA_deleteAll'">批量删除
                    </el-button>
                </div>
                <div class="right-panel">
                    <el-input size="small" v-model="query.name" placeholder="名称" class="handle-input mr10"></el-input>
                    <el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
                </div>
                <div class="clear"></div>
            </div>
            <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column label="序号" width="55" align="center">
                    <template #default="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="名称" width="300" align="center"></el-table-column>
                <el-table-column label="价格(元/月)" width="100" align="center">
                    <template #default="scope">{{ scope.row.price }}</template>
                </el-table-column>
                <el-table-column prop="monthType" label="包月类型" align="center" width="100">
                    <template #default="scope">
                        <p v-if="scope.row.monthType == 1" style="color: #00a0e9">个人</p>
                        <p v-if="scope.row.monthType == 2" style="color: #2da96d">公司</p>
                    </template>
                </el-table-column>
                <el-table-column prop="category" label="车辆类型" align="center" width="100">
                    <template #default="scope">
                        <p v-if="scope.row.category == 0" style="color: #2da96d">大车</p>
                        <p v-if="scope.row.category == 1" style="color: #00a0e9">小车</p>
                    </template>
                </el-table-column>
                <el-table-column prop="price_type" label="价格类型" align="center" width="100">
                    <template #default="scope">
                        <p v-if="scope.row.price_type == 1" style="color: #00a0e9">永久</p>
                        <p v-if="scope.row.price_type == 2" style="color: #2da96d">期限</p>
                    </template>
                </el-table-column>
                <el-table-column prop="roads" label="停车场" align="center">
                    <template #default="scope">
                        <div class="no-warp">
                            <el-tag size="small" @click="handleView(scope.$index, scope.row)"
                                    v-for="(item, index) in scope.row.parkList" :key="index" class="mar5">{{
                                item.park_name==null?"":item.park_name }}
                            </el-tag>
                        </div>
                    </template>
                </el-table-column>
                <!--<el-table-column prop="user_name" label="添加人" align="center" width="120"></el-table-column>-->
                <!--<el-table-column prop="create_time" label="添加时间" align="center" width="170"></el-table-column>-->
                <el-table-column label="操作" width="180" align="center">
                    <template #default="scope">
                        <el-button size="mini" type="text" icon="el-icon-edit"
                                   @click="handleEdit(scope.$index, scope.row, false)"
                                   v-permission="'park_monthSetA_edit'">编辑
                        </el-button>
                        <el-button size="mini" type="text" icon="el-icon-delete" class="red"
                                   @click="handleDelete(scope.$index, scope.row)"
                                   v-permission="'park_monthSetA_delete'">删除
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
        <el-dialog :title="dialogT" v-model="editVisible" width="800px" top="5vh">
            <el-form label-width="70px" label-position="top" size="small" :rules="formRules" ref="editform"
                     :model="form">
                <el-form-item label="名称" prop="name">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="包月类型" prop="name">
                    <el-radio v-model="form.monthType" label="1" border>个人包月</el-radio>
                    <el-radio v-model="form.monthType" label="2" border>公司包月</el-radio>
                </el-form-item>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="车辆类型" paop="category">
                            <el-select v-model="form.category" placeholder="请选择" class="w100p">
                                <el-option v-for="item in categorys" :key="item.dc_value" :label="item.label"
                                           :value="item.dc_value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col class="line" :span="2"></el-col>
                    <el-col :span="11">
                        <el-form-item label="价格(元/月)" prop="price">
                            <el-input type="number" v-model="form.price"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="价格类型" prop="price_type">
                            <el-radio-group v-model="form.price_type" @change="radioBtnPriceType">
                                <el-radio label="1">永久</el-radio>
                                <el-radio label="2">期限</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col class="line" :span="2"></el-col>
                    <el-col :span="11" v-if="newPriceShow">
                        <el-form-item label="明年价格(元/月)" prop="new_price">
                            <el-input type="number" v-model="form.new_price"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="停车区">
                    <el-transfer
                            filterable
                            :filter-method="filterMethod"
                            filter-placeholder="请输入名称"
                            :titles="['待选择区域', '已选择区域']"
                            v-model="parkingIds"
                            :data="roaddatas"
                    ></el-transfer>
                </el-form-item>
            </el-form>

            <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
            </template>
        </el-dialog>

        <!-- 新增弹出框 -->
        <el-dialog :title="dialogT" v-model="addVisible" width="800px" top="5vh">
            <el-form label-width="70px" label-position="top" size="small" :rules="formRules" ref="addform"
                     :model="form">
                <el-form-item label="名称" prop="name">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="包月类型" prop="monthType">
                    <el-radio v-model="form.monthType" label="1" border>个人包月</el-radio>
                    <el-radio v-model="form.monthType" label="2" border>公司包月</el-radio>
                </el-form-item>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="车辆类型" prop="category">
                            <el-select v-model="form.category" placeholder="请选择" class="w100p">
                                <el-option v-for="item in categorys" :key="item.dc_value" :label="item.label"
                                           :value="item.dc_value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col class="line" :span="2"></el-col>
                    <el-col :span="11">
                        <el-form-item label="价格(元/月)" prop="price">
                            <el-input type="number" v-model="form.price"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="价格类型" prop="price_type">
                            <el-radio-group v-model="form.price_type" @change="radioBtnPriceType">
                                <el-radio label="1">永久</el-radio>
                                <el-radio label="2">期限</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col class="line" :span="2"></el-col>
                    <el-col :span="11" v-if="newPriceShow">
                        <el-form-item label="明年价格(元/月)" prop="new_price">
                            <el-input type="number" v-model="form.new_price"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="停车区">
                    <el-transfer
                            filterable
                            :filter-method="filterMethod"
                            filter-placeholder="请输入城市拼音"
                            :titles="['待选择区域', '已选择区域']"
                            v-model="parkingIds"
                            :data="roaddatas"
                    ></el-transfer>
                </el-form-item>
            </el-form>

            <template #footer>
        <span class="dialog-footer">
          <el-button @click="addVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
            </template>
        </el-dialog>
        <!-- 详情弹出框 -->
        <el-dialog title="停车场列表" v-model="viewVisible" width="840px" @close="closeAddress">
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
    </div>
</template>

<script>
    import {ref, reactive, getCurrentInstance, nextTick, onMounted, computed} from "vue";
    import {ElMessage, ElMessageBox, ElScrollbar} from "element-plus";
    import pinyin from "js-pinyin"
    import {
        addMonthly,
        delMonthly,
        delMonthlyAll,
        editMonthly,
        queryCategory,
        queryConfig,
        queryMonthly,
        queryParkData,
    } from "../../../api/index";

    export default {
        name: "userList",
        components: {ElScrollbar},
        setup() {
            const newPriceShow = ref(false);

            const addform = ref(null);
            const editform = ref(null);
            const query = reactive({
                pageIndex: 1,
                pageSize: 15,
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            // 获取表格数据
            const getData = () => {
                queryMonthly(query).then((res) => {
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
                        delMonthly(reactive({id: row.id})).then((res) => {
                            if (res.code === 0) {
                                ElMessage.success("删除成功");
                                getData();
                            }
                        });

                        tableData.value.splice(index, 1);
                    })
                    .catch(() => {
                    });
            };
            // 表格编辑时弹窗和保存
            const editVisible = ref(false);
            const addVisible = ref(false);
            const configId = ref("");
            const roaddatas = ref([]);
            const parkingIds = ref([]);
            const getTransfer = () => {
                queryParkData(reactive({})).then((res) => {
                    const parkList = res.data;
                    const cardata = [];
                    for (let i = 0; i < parkList.length; i++) {
                        let park = parkList[i];
                        cardata.push({
                            key: park.id,
                            label: park.park_name,
                        });
                        roaddatas.value = cardata;
                    }
                });
            };
            let form = ref({});
            let idx = -1;
            const dialogT = ref("");
            const handleEdit = (index, row, type) => {
                dialogT.value = "编辑";
                form.value = {};
                editVisible.value = true;
                getTransfer();
                queryConfig(reactive({configId: row.id})).then((res) => {
                    form.value = res.data;
                    parkingIds.value = res.data.roadIds;

                    if (form.value.price_type == "1") {
                        newPriceShow.value = false;
                    } else {
                        newPriceShow.value = true;
                    }
                });

                nextTick(() => {
                    editform.value.clearValidate();
                });
            };

            const handleAdd = (index, row, type) => {
                dialogT.value = "新增";
                form.value = {};
                form.value.price_type = "1"; // 价格类型 1永久 2期限
                form.value.category = "1"; // 车辆类型 1：小车 0：大车
                form.value.monthType = "1"; // 包月类型 1：个人 2： 公司
                newPriceShow.value = false;
                addVisible.value = true;
                parkingIds.value = [];
                getTransfer();
                nextTick(() => {
                    addform.value.clearValidate();
                });
            };

            const saveEdit = () => {
                editform.value.validate((v) => {
                    if (v) {
                        if ("" === parkingIds.value.toString()) {
                            ElMessage.error("请选择停车场");
                            return false;
                        }
                        if (parkingIds.value.length > 1) {
                            ElMessage.error("每个包月配置方案最多绑定一个停车场");
                            return false;
                        }
                        // 价格类型 1永久 2期限
                        if (form.value.price_type == "2") {
                            if (form.value.new_price == "" || form.value.new_price == null || form.value.new_price == undefined) {
                                ElMessage.error("请输入明年价格");
                                return false;
                            }
                        }
                        editVisible.value = false;
                        form.value.roadIds = null;
                        form.value.parkingIds = parkingIds.value.toString();
                        editMonthly(form.value).then((res) => {
                            if (res.code === 0) {
                                editVisible.value = false;
                                ElMessage.success(res.msg);
                                getData();
                            } else {
                                ElMessage.error(res.msg);
                            }
                        });
                    }
                });
            };

            const save = () => {
                addform.value.validate((v) => {
                    if (v) {
                        // if (
                        //   form.value.start_time === "" ||
                        //   form.value.start_time === undefined ||
                        //   form.value.end_time === null ||
                        //   form.value.end_time === undefined
                        // ) {
                        //   ElMessage.error("请选择开始、结束时间");
                        //   return false;
                        // }
                        if ("" === parkingIds.value.toString()) {
                            ElMessage.error("请选择停车场");
                            return false;
                        }
                        if (parkingIds.value.length > 1) {
                            ElMessage.error("每个包月配置方案最多绑定一个停车场");
                            return false;
                        }
                        // 价格类型 1永久 2期限
                        if (form.value.price_type == "2") {
                            if (form.value.new_price == "" || form.value.new_price == null || form.value.new_price == undefined) {
                                ElMessage.error("请输入明年价格");
                                return false;
                            }
                        }
                        form.value.parking_type = "1";
                        form.value.parkingIds = parkingIds.value.toString();
                        form.value.roadIds = null;
                        addMonthly(form.value).then((res) => {
                            if (res.code === 0) {
                                addVisible.value = false;
                                ElMessage.success(res.msg);
                                getData();
                            } else {
                                ElMessage.error(res.msg);
                            }
                        });
                    }
                });
            };
            const categorys = ref([]);
            const queryDict = () => {
                queryCategory(reactive({})).then((res) => {
                    categorys.value = res.data;
                });
            };
            queryDict();
            const formRules = {
                name: [{required: true, message: "请输入名称", trigger: "blur"}],
                category: [{required: true, message: "请选择车辆类型", trigger: "blur"}],
                monthType: [{required: true, message: "请选择包月类型", trigger: "blur"}],
                price_type: [{required: true, message: "请选择价格类型", trigger: "blur"}],
                price: [
                    {required: true, message: "请输入价格", trigger: ["blur", "change"]},
                ],
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
            const handleView = (index, row) => {
                nextTick(() => {
                    calculateHeight()
                    handleScroll()
                });

                // 获取roadsList 对象中的road_name 并生成新的数组 键值对 为 name：road_name
                let roadsList = [];
                row.parkList.forEach(function (item) {
                    roadsList.push(item.park_name);
                });

                // 使得roadsList 为对象为name：road_name
                roadsList = roadsList.map(function (item) {
                    return {name: item}
                });
                pinyin.setOptions({checkPolyphone: false, charCase: 0})
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
                // charList.value = _charList

                // 生成 charList.value 为id 从0开始， key 从A-Z  的数组
                charList.value = []
                for (var i = 0; i < 26; i++) {
                    charList.value.push({
                        id: '',
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
            const closeAddress = () => {
                scrollbar.value.$el.querySelector('.el-scrollbar__wrap').scrollTop = 0
                currentIndex.value = 0
                viewVisible.value = false;
            };

            return {
                query,
                tableData,
                pageTotal,
                editVisible,
                form,
                dialogT,
                addVisible,
                categorys,
                roaddatas,
                parkingIds,
                formRules,
                addform,
                editform,
                queryDict,
                handleSearch,
                handlePageChange,
                handleDelete,
                handleEdit,
                handleAdd,
                saveEdit,
                save,
                getData,
                multipleSelection: [],
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
                newPriceShow,
            };
        },
        computed: {
            indexList() {
                return this.charList
            }
        },
        methods: {
            radioBtnPriceType(val) {
                if (val == "1") {
                    this.newPriceShow = false;
                } else {
                    this.newPriceShow = true;
                }
            },
            handleSelectionChange(data) {
                this.selectedData = data;
            },
            handleDeleteAll() {
                var that = this;
                var val = this.selectedData;
                ElMessageBox.confirm("确定要删除吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        var ids = "";
                        if (val) {
                            val.forEach(function (item, index) {
                                ids = ids + item.id + ",";
                            });
                            delMonthlyAll(reactive({ids: ids})).then((res) => {
                                if (res.code === 0) {
                                    ElMessage.success(res.msg);
                                    that.getData();
                                } else {
                                    ElMessage.error(res.msg);
                                }
                            });
                        } else {
                            ElMessage.warning(`请选择一条记录`);
                        }
                    })
                    .catch(() => {
                    });
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

    .no-warp {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        cursor: pointer;
    }

    ::v-deep .el-transfer-panel {
        width: 299px !important;
    }
</style>
