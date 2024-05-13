<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button
          v-permission="['park_screen_add','road_screen_add']"
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleEdit(0, null, true)"
            >添加
          </el-button>
          <el-button
          v-permission="['road_screen_release','park_screen_release']"
            type="success"
            size="small"
            icon="el-icon-document-checked"
            @click="handlenewRelease()"
            >发布管理
          </el-button>
        </div>
        <div class="right-panel">
          <el-input
            @keyup.enter="handleSearch()"
            size="small"
            v-model="query.name"
            placeholder="名称"
            class="handle-input mr10"
          ></el-input>
          <el-button
            size="small"
            type="primary"
            icon="el-icon-search"
            @click="handleSearch"
            >查询
          </el-button>
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
        :row-key="getRowKeys"
        highlight-current-row
        @current-change="handleSelectionChange"
      >
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="code"
          label="编码"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="ip"
          label="IP"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column prop="roadNames" label="路内">
          <template #default="scope">
            <p class="no-warp" @click="handleView(scope.$index, scope.row,0)">{{ scope.row.roadNames }}</p>
          </template>
        </el-table-column>
        <el-table-column prop="parkNames" label="停车场">
          <template #default="scope">
            <p class="no-warp" @click="handleView(scope.$index, scope.row,1)">{{ scope.row.parkNames }}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="play_type"
          align="center"
          width="100"
          label="播放类型"
        >
          <template #default="scope">
            <el-tag
              size="small"
              :type="
                scope.row.play_type == '1'
                  ? 'success'
                  : scope.row.play_type == '2'
                  ? 'danger'
                  : ''
              "
              v-if="scope.row.play_type == '1'"
              >停车信息
            </el-tag>
            <el-tag
              size="small"
              :type="
                scope.row.play_type == '1'
                  ? 'success'
                  : scope.row.play_type == '2'
                  ? 'danger'
                  : ''
              "
              v-else
              >多媒体
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="create_time"
          label="添加时间"
          width="170"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="update_time"
          label="更新时间"
          width="170"
          align="center"
        >
        </el-table-column>
        <el-table-column label="操作" align="center" width="140">
          <template #default="scope">
            <el-button
            v-permission="['road_screen_edit','park_screen_edit']"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              >编辑
            </el-button>
            <el-button
            v-permission="['park_screen_delete','road_screen_delete']"
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
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
    <el-dialog :title="dialogT" v-model="editVisible" width="800px" top="2vh">
      <el-form
        label-width="70px"
        label-position="top"
        size="small"
        :rules="formRules"
        :model="form"
        ref="editform"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入编码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="IP" prop="ip">
              <el-input v-model="form.ip" placeholder="请输入IP"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="停车场">
              <el-transfer
                filterable
                filter-placeholder="请输入名称"
                :titles="['待选择区域', '已选择区域']"
                v-model="form.parkIds"
                :data="parkdatas"
              ></el-transfer>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="路内">
              <el-transfer
                filterable
                filter-placeholder="请输入名称"
                :titles="['待选择区域', '已选择区域']"
                v-model="form.roadIds"
                :data="roaddatas"
              ></el-transfer>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 发布 新弹窗 -->
    <el-dialog
      title="发布管理"
      v-model="newreleaseVisible"
      :close-on-click-modal="false"
      width="1000px"
    >
      <el-form
        ref="form"
        size="small"
        :model="form"
        class="lineH0"
        label-width="80px"
      >
        <el-row :gutter="14">
          <el-col :span="24">
            <el-button size="small" type="primary" @click="addRule()"
              >添加规则</el-button
            >
          </el-col>
          <el-col :span="24">
            <el-table
              :data="tableData2"
              ref="multipleTable2"
              border
              class="mt2"
              header-cell-class-name="table-header"
              highlight-current-row
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="index" align="center" width="50" label="序号">
                <!--<template #default="scope">-->
                  <!--{{-->
                    <!--(query2.pageIndex - 1) * query2.pageSize + scope.$index + 1-->
                  <!--}}-->
                <!--</template>-->
              </el-table-column>
              <el-table-column prop="task_name" align="center" label="任务名称">
              </el-table-column>
              <el-table-column prop="play_type" align="center" label="任务类型">
                <template #default="scope">
                  <p v-if="scope.row.play_type == '1'">停车信息</p>
                  <p v-if="scope.row.play_type == '2'">多媒体</p>
                </template>
              </el-table-column>
              <el-table-column label="内容类型" align="center">
                <template #default="scope">
                  <p v-if="scope.row.type == '1'">图片</p>
                  <p v-if="scope.row.type == '2'">视频</p>
                </template>
              </el-table-column>
              <el-table-column prop="title" align="center" label="素材名称">
              </el-table-column>
              <el-table-column
                prop="start_time"
                align="center"
                label="开始时间"
              >
              </el-table-column>
              <el-table-column align="center" label="操作">
                <template #default="scope">
                  <!-- <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="editRule(scope.$index, scope.row, false)"
                    >编辑
                  </el-button> -->
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    class="red"
                    @click="deleteRule(scope.$index, scope.row)"
                    >删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination">
              <el-pagination
                background
                layout="total, prev, pager, next"
                :current-page="query2.pageIndex"
                :page-size="query2.pageSize"
                :total="pageTotal2"
                @current-change="handlePageChange2"
              ></el-pagination>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button size="small" @click="newreleaseVisible = false"
            >取 消</el-button
          >
          <!-- <el-button size="small" type="primary" @click="fbUpd"
            >发 布</el-button
          > -->
        </span>
      </template>
    </el-dialog>
    <!-- 发布 弹窗 -->
    <el-dialog
      title="发布"
      v-model="fabuVisible"
      :close-on-click-modal="false"
      width="700px"
    >
      <el-form
        ref="publishform"
        size="small"
        :model="publishform"
        class="lineH0"
        label-width="80px"
      >
        <el-row :gutter="14">
          <el-col :span="24">
            <el-form-item label="发布类型" prop="type">
              <el-radio-group v-model="play_type">
                <el-radio :label="1">停车信息</el-radio>
                <el-radio :label="2">多媒体</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="24" v-show="play_type == 2">
            <el-card class="box-card" shadow="never">
              <el-row :gutter="10">
                <el-col :span="8">
                  <el-form-item class="search-mb0">
                    <el-input
                      size="small"
                      placeholder="素材标题"
                      v-model="query3.title"
                      class="mr10"
                      clearable
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item class="search-mb0">
                    <el-select
                      v-model="query3.type"
                      placeholder="素材类型"
                      clearable
                    >
                      <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      >
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="" class="search-mb0">
                    <el-button
                      size="small"
                      type="primary"
                      icon="el-icon-search"
                      @click="handleSearch2"
                      >查询
                    </el-button>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-table
                    :data="tableData3"
                    ref="multipleTable2"
                    border
                    class="mt2"
                    header-cell-class-name="table-header"
                    @current-change="updateCurrentGateway"
                    highlight-current-row
                  >
                    <el-table-column align="left" width="50" label="单选">
                      <template #default="scope">
                        <el-radio
                          class="radio"
                          :label="scope.row.id"
                          v-model="radio"
                          @change.native="getCurrentRow(scope.row)"
                          >&nbsp;
                        </el-radio>
                      </template>
                    </el-table-column>
                    <el-table-column prop="title" align="center" label="标题">
                    </el-table-column>
                    <el-table-column width="100" label="类型" align="center">
                      <template #default="scope">
                        <p v-if="scope.row.type == '1'">图片</p>
                        <p v-if="scope.row.type == '2'">视频</p>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div class="pagination">
                    <el-pagination
                      background
                      layout="total, prev, pager, next"
                      :current-page="query3.pageIndex"
                      :page-size="query3.pageSize"
                      :total="pageTotal3"
                      @current-change="handlePageChange3"
                    ></el-pagination>
                  </div>
                </el-col>
              </el-row>
            </el-card>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任务名称" prop="ip">
              <el-input
                size="small"
                placeholder="任务名称"
                v-model="taskName"
                class="mr10"
                clearable
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="ip">
              <el-time-picker
                value-format="HH:mm:ss"
                v-model="start_time"
                placeholder="开始时间"
                style="width: 100% !important"
              ></el-time-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="fabuVisible = false">取 消</el-button>
          <el-button type="primary" @click="fbUpd">确 定</el-button>
        </span>
      </template>
    </el-dialog>

     <!-- 详情弹出框 -->
     <el-dialog title="地址列表" v-model="viewVisible" width="840px" @close="closeAddress">
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
import { ref, reactive, getCurrentInstance, nextTick,onMounted,computed  } from "vue";
import { ElMessage, ElMessageBox,ElScrollbar} from "element-plus";
import pinyin from "js-pinyin"
import {
  screenList,
  detailsScreen,
  editScreen,
  fbUpd,
  publishTask,
  getTask,
  getTaskEntity,
  deleteTask,
} from "../../api/screen";
import { queryParkData, queryRoadData } from "../../api/index";

import { sysMaterialList } from "../../api/sysMaterial";

export default {
  name: "screelist",
  components: { ElScrollbar },
  provide() {
    return {
      reload: this.reload,
    };
  },
  data() {
    return {
      times: "",
      start_time: "",
      taskName: "",
      tableH: 0,
      play_type: 1,
      // 表单验证
      formRules: {
        name: [{ required: true, message: "必填项", trigger: "blur" }],
        code: [{ required: true, message: "必填项", trigger: "blur" }],
        ip: [{ required: true, message: "必填项", trigger: "blur" }],
      },
      publishform: {},
      releaseVisible: false,
      newreleaseVisible: false,
      fabuVisible: false,
      selectedData: {},
      showRouterView: true,
      selectedMaterialId: "",
    };
  },
  setup() {
    const publishform = ref({});
    const editform = ref(null);
    const query = reactive({
      code: "",
      name: "",
      pageIndex: 1,
      pageSize: 15,
    });
    const query2 = reactive({
      title: "",
      type: "",
      state: "1",
      pageIndex: 1,
      pageSize: 10,
    });
    const query3 = reactive({
      id: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    const tableData2 = ref([]);
    const pageTotal2 = ref(0);
    const tableData3 = ref([]);
    const pageTotal3 = ref(0);
    // 获取表格数据
    const getData = () => {
      screenList(query).then((res) => {
        // console.log(res);
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
          // ElMessage.success("删除成功");
          // tableData.value.splice(index, 1);
          let edit_screen = reactive({
            is_del: "1",
            id: row.id,
          });
          editScreen(edit_screen)
            .then((res) => {
              // console.log(res);
              ElMessage.success("删除成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => {});
    };

    //路内列表数据
    const roaddatas = ref([]);
    const getRoadData = () => {
      queryRoadData(reactive({})).then((res) => {
        const roadList = res.data;
        const cardata = [];
        for (let i = 0; i < roadList.length; i++) {
          let road = roadList[i];
          cardata.push({
            key: road.id,
            label: road.road_name,
          });
          roaddatas.value = cardata;
        }
      });
    };
    //停车场列表数据
    const parkdatas = ref([]);
    const getParkData = () => {
      queryParkData(reactive({})).then((res) => {
        const parkList = res.data;
        const cardata = [];
        for (let i = 0; i < parkList.length; i++) {
          let park = parkList[i];
          cardata.push({
            key: park.id,
            label: park.park_name,
          });
          parkdatas.value = cardata;
        }
      });
    };

    // 发布

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    let form = ref({});
    const dialogT = ref("");
    const handleEdit = (index, row, type) => {
      editVisible.value = true;
      form.value = {};

      //路内列表数据
      getRoadData();
      //停车场列表数据
      getParkData();

      if (type) {
        dialogT.value = "新增";
      } else {
        dialogT.value = "编辑";
        //获取数据信息
        detailsScreen(reactive({ id: row.id })).then((res) => {
          form.value = res.data;
          form.value.roadIds = res.data.roadIdList;
          form.value.parkIds = res.data.parkIdList;
        });
      }
      nextTick(() => {
        editform.value.clearValidate();
      });
    };

    const saveEdit = () => {
      editform.value.validate((v) => {
        if (v) {
          if (
            "" === form.value.parkIds.toString() &&
            "" === form.value.roadIds.toString()
          ) {
            ElMessage.error("请选择停车场/路内");
            return false;
          }
          editVisible.value = false;
          form.value.parkIds = form.value.parkIds.toString();
          form.value.roadIds = form.value.roadIds.toString();
          editScreen(form.value).then((res) => {
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

        // 循环遍历字符串 并生成新的数组 键值对 为 name：road_name
        let roadsList = [];
        if(type == 0){
          let roadNames = row.roadNames.split(',');
          roadNames.forEach(function (item) {
              roadsList.push({
                  name: item
              })
          })
        }else{
          let parkNames = row.parkNames.split(',');
          parkNames.forEach(function (item) {
              roadsList.push({
                  name: item
              })
          })
        }

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
            // charList.value = _charList

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
      pageTotal,
      editVisible,
      form,
      dialogT,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleEdit,
      saveEdit,
      multipleSelection: [],
      roaddatas,
      parkdatas,
      editform,
      publishform,
      getData,
      tableData2,
      query2,
      query3,
      pageTotal2,
      tableData3,
      pageTotal3,
      templateSelection: {},
      radio: "",
      options: [
        {
          value: "1",
          label: "图片",
        },
        {
          value: "2",
          label: "视频",
        },
      ],
      value: "",
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
    this.tableH = h - 308 + "px";
  },
  methods: {
    //诱导屏列表 多选事件
    handleSelectionChange(data) {
      var that = this;
      that.selectedData = data;
    },
    // 单选
    getCurrentRow(row) {
      // 获取选中数据 row表示选中这一行的数据，可以从里面提取所需要的值
      this.templateSelection = row;
      // console.log("点击")
      // console.log(this.templateSelection)
    },
    //点击选中的行也可以选中单选按钮
    updateCurrentGateway(row) {
      //如果没有row,终止
      if (!row) return;
      //把当前行label绑定的值和v-model绑定的值相同时,单选按钮就可以选中
      this.radio = row.id;
      this.selectedMaterialId = row.id;
      this.templateSelection = row;
    },

    //点击发布按钮 弹窗事件
    handlenewRelease() {
      var that = this;
      var val = that.selectedData;

      if (val.length != 0) {
        this.getTasks();
      } else {
        ElMessage.warning(`请至少选择一条记录`);
      }
    },
    getTasks() {
      var that = this;
      var val = that.selectedData;
      this.query2.id = val.id;
      getTask(this.query2).then((res) => {
        if (res.code == 0) {
          that.tableData2 = res.data.list;
          that.pageTotal2 = res.data.total;
        }
        that.newreleaseVisible = true;
      });
    },
    addRule() {
      var that = this;
      that.query3.pageIndex = 1;
      sysMaterialList(that.query3).then((res) => {

        that.tableData3 = res.data.list;
        that.pageTotal3 = res.data.total;
      });
      this.fabuVisible = true;
    },
    editRule(index, row) {
      var that = this;
      var id = row.id;
      var q = { id: id };
      getTaskEntity(q).then((res) => {
        that.play_type = res.data.play_type;
        //that.$refs.multipleTable2.setCurrentRow
        that.taskName = res.data.task_name;
        that.start_time = res.data.start_time;
        that.fabuVisible = true;
      });
    },
    deleteRule(index, row) {
      var that = this;
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      }).then(() => {
        deleteTask({ id: row.id }).then((res) => {
          if (res.code == 0) {
            ElMessage.success("删除成功");
            that.getTasks();
          }
        });
      });
    },
    handlenewAdd() {
      this.fabuVisible = true;
    },
    //素材分页查询
    handleSearch2() {
      var that = this;
      that.query3.pageIndex = 1;
      sysMaterialList(that.query3).then((res) => {
        that.tableData2 = res.data.list;
        that.pageTotal2 = res.data.total;
      });
    },
    //素材分页
    handlePageChange2(val) {
      var that = this;
      that.query2.pageIndex = val;
      that.query2.id = that.selectedData.id;
      getTask(this.query2).then((res) => {
        if (res.code == 0) {
          that.tableData2 = res.data.list;
          that.pageTotal2 = res.data.total;
        }
        that.newreleaseVisible = true;
      });
    },
    handlePageChange3(val) {
      var that = this;
      that.query3.pageIndex = val;
      sysMaterialList(that.query3).then((res) => {
        that.tableData3 = res.data.list;
        that.pageTotal3 = res.data.total;
      });
    },

    getRowKeys(row) {
      return row.id;
    },
    reload() {
      // 改变this.showRouterView的状态，控制路由出口的显示隐藏
      this.showRouterView = false;
      this.$nextTick(() => {
        // 必须使用nextTick，否则最新dom可能未更新，导致刷新失败
        this.showRouterView = true;
      });
    },
    //发布 提交
    fbUpd() {
      //保存方法start_time
      let falg = 0;
      let that = this;
      if (this.play_type == 2) {
        if (that.radio == "" || that.radio == null || that.radio == undefined) {
          ElMessage.error("请选择素材!");
        }
      }
      publishTask(
        reactive({
          pid: that.radio,
          id: that.selectedData.id,
          materialId: that.selectedMaterialId,
          play_type: that.play_type,
          start_time: that.start_time,
          taskName: that.taskName,
        })
      )
        .then((res) => {
          falg = res.code;
        })
        .then(() => {
          if (falg == 0) {
            ElMessage.success("发布成功，播放过程可能需要几分钟...");
            that.fabuVisible = false;
            that.getTasks();
            that.taskName = "";
            that.start_time = "";
            that.reload();
          } else if (falg == -1) {
            ElMessage.error("操作失败!");
          }
        });
    },
  },
};
</script>
<style scoped>
.datepicker {
  width: 100% !important;
}

.handle-input {
  width: 180px;
  vertical-align: middle;
}

.mt2 {
  margin-top: 10px;
}

.lineH0 {
  margin-bottom: 20px;
}

::v-deep .search-mb0 .el-form-item__content {
  margin-left: 0 !important;
}
</style>
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
    color: #409EFF;
  }
  .no-warp:hover{
    text-decoration: underline;
    cursor: pointer;
  }
  ::v-deep .el-transfer-panel{
    width: 299px !important;
  }
</style>
