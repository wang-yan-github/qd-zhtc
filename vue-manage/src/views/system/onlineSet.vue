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
            v-permission="['road_online_add', 'park_online_add']"
            >添加</el-button
          >
          <!-- <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            >批量删除</el-button
          > -->
        </div>
        <div class="right-panel">
          <el-form inline label-width="80" size="small">
            <el-form-item label="区域">
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
              <el-button
                size="small"
                type="primary"
                icon="el-icon-search"
                @click="getData"
                >查询</el-button
              >
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
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
            {{ (query.pageIndex - 1) * query.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" align="center">
          <template #default="scope">
            <div class="no-warp">
            <el-tag
              size="small"
              v-for="(item, index) in scope.row.places"
              @click="handleView(scope.$index, scope.row)"
              :key="index"
              class="mar5"
              >{{ item.road_name }}</el-tag
            >
          </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="start_time"
          :formatter="formdate"
          label="开始时间"
          align="center"
          width="190"
        >
        </el-table-column>
        <el-table-column prop="status" width="120" label="状态" align="center">
          <template #default="scope">
            <el-tag size="small" v-if="scope.row.status == 1">启用</el-tag>
            <el-tag size="small" v-else>禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="create_time"
          width="190"
          label="创建时间"
          align="center"
        >
        </el-table-column>
        <el-table-column label="操作" width="190" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              v-permission="['road_online_edit', 'park_online_edit']"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              class="red"
              v-if="scope.row.status == 1"
              @click="handleDelete(scope.$index, scope.row, 1)"
              v-permission="['road_online_status', 'park_online_status']"
              >禁用</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              v-else
              @click="handleDelete(scope.$index, scope.row, 1)"
              v-permission="['road_online_status', 'park_online_status']"
              >启用</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row, 2)"
              v-permission="['road_online_detele', 'park_online_detele']"
              >删除</el-button
            >
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
    <el-dialog :title="dialogT" v-model="editVisible" width="800px">
      <el-form label-width="70px" label-position="top" size="small">
        <el-form-item label="上线收费">
          <el-row>
            <el-col :span="11"
              ><el-date-picker
                placeholder="选择时间"
                v-model="form.start_time"
                style="width: 299px"
                type="datetime"
              ></el-date-picker
            ></el-col>
          </el-row>
        </el-form-item>
        <!--        @change="handleChange"-->
        <!--        :filter-method="filterMethod"-->
        <el-form-item label="停车区">
          <el-transfer
            filterable
            filter-placeholder="请输入城市拼音"
            :titles="['待选择区域', '已选择区域']"
            v-model="parkingIds"
            :data="form.cardatas"
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
import { ref, reactive, getCurrentInstance, nextTick,onMounted,computed } from "vue";
import { ElMessage, ElMessageBox,ElScrollbar} from "element-plus";
import pinyin from "js-pinyin"
import {
  parkingReleaseDataList,
  parkingReleaseDelete,
  parkingReleaseListData,
  parkingReleaseSave,
  queryAreaData,
  queryRoadData,
  queryParkData,
  queryStreetData,
} from "../../api/index";
import Tags from "../../components/Tags.vue";

export default {
  components: { Tags,ElScrollbar },
  name: "parkingRelease",
  data() {
    return {
      tableH: 0,
    };
  },
  setup() {
    const menuType = localStorage.getItem("menuType");

    const query = reactive({
      pageIndex: 1,
      pageSize: 15,
      road_id: "",
      menuType: menuType,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      parkingReleaseDataList(query).then((res) => {
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
    const handleDelete = (index, row, type) => {
      let falg = 1;
      if (type == 1) {
        parkingReleaseDelete(reactive({ id: row.id, status: row.status }))
          .then((res) => {
            falg = res;
          })
          .then(() => {
            if (falg == 1) {
              ElMessage.success("操作成功！");
              editVisible.value = false;
              query.pageIndex = 1;
              getData();
            } else {
              ElMessage.success("操作失败！");
            }
          });
      } else {
        ElMessageBox.confirm("确定要删除吗？", "提示", { type: "warning" })
          .then(() => {
            parkingReleaseDelete(reactive({ id: row.id }))
              .then((res) => {
                falg = res;
              })
              .then(() => {
                if (falg == 1) {
                  ElMessage.success("操作成功！");
                  editVisible.value = false;
                  query.pageIndex = 1;
                  getData();
                } else {
                  ElMessage.success("操作失败！");
                }
              });
          })
          .catch(() => {});
      }
    };
    let dialogT = ref("编辑");
    const parkingIds = ref([]);
    const roleName = ref({});
    //获取列表数据
    const generateData = () => {
      const cardata = [];
      parkingReleaseListData(query).then((res) => {
        const list = res.data.list;
        let name = res.data.name;
        roleName.value = res.data.name;
        list.forEach((item, index, array) => {
          if (name == "road") {
            cardata.push({
              key: item.id,
              label: item.road_name,
            });
          } else {
            cardata.push({
              key: item.id,
              label: item.park_name,
            });
          }
        });
      });
      return cardata;
    };
    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    let form = reactive({
      id: "",
      cardatas: [],
      stopcarvalue: [2, 4],
      start_time: "",
      parkingplace_id: "",
    });
    //新增或修改
    const handleEdit = (index, row, type) => {
      parkingIds.value = [];
      form.start_time = "";
      form.id = "";
      if (type) {
        dialogT.value = "新增";
        let cardata = [];
        parkingReleaseListData(query)
          .then((res) => {
            const list = res.data.list;
            let name = res.data.name;
            roleName.value = res.data.name;
            list.forEach((item, index, array) => {
              if (name == "road") {
                cardata.push({
                  key: item.id,
                  label: item.road_name,
                });
              } else {
                cardata.push({
                  key: item.id,
                  label: item.park_name,
                });
              }
            });
            form.cardatas = cardata;
          })
          .then(() => {
            editVisible.value = true;
          });
      } else {
        dialogT.value = "编辑";
        //数据存储
        parkingReleaseListData(reactive({ id: row.id }))
          .then((res) => {
            let list = res.data.list;
            let cardata = [];
            list.forEach((item, index, array) => {
              if (res.data.name == "road") {
                cardata.push({
                  key: item.id,
                  label: item.road_name,
                });
              } else {
                cardata.push({
                  key: item.id,
                  label: item.park_name,
                });
              }
            });
            form.cardatas = cardata;
            parkingIds.value = res.data.parkings;
            var release = res.data.release;
            form.start_time = release.start_time;
            form.id = release.id;
          })
          .then(() => {
            editVisible.value = true;
          });
      }
    };
    let formqjl = reactive({
      time: "",
      areaId: "",
      streetId: "",
      roadId: "",
      areas: [],
      streets: [],
      roads: [],
    });
    //保存功能
    const saveEdit = () => {
      let falg = 0;
      let msg = "";
      parkingReleaseSave(
        reactive({
          id: form.id,
          start_time: form.start_time,
          parkingplace_id: parkingIds.value.toString(),
        })
      )
        .then((res) => {
          falg = res.code;
          msg = res.msg;
        })
        .then(() => {
          if (falg == 0) {
            ElMessage.success(msg);
            editVisible.value = false;
            query.pageIndex = 1;
            getData();
          } else if (falg == -1) {
            ElMessage.error(msg);
          }
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
    //获取街道下拉框数据
    let queryStreet = reactive({
      areaId: query.area_id,
    });
    const getStreet = () => {
      queryStreet.areaId = query.area_id;
      query.street_id = "";
      query.road_id = "";
      queryStreetData(queryStreet).then((res) => {
        formqjl.streets = res.data;
      });
    };
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
    const handleView = (index,row) => {
        nextTick(() => {
            calculateHeight()
            handleScroll()
        });

        // 获取roadsList 对象中的road_name 并生成新的数组 键值对 为 name：road_name
        let roadsList = [];
        row.places.forEach(function (item) {
            roadsList.push(item.road_name);
        });

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
      parkingIds,
      roleName,
      queryStreet,
      queryRoad,
      formqjl,
      getData,
      getArea,
      getStreet,
      getRoad,
      getPark,
      saveEdit,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleEdit,
      multipleSelection: [],
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
    this.tableH = h - 308 + "px";
  },
  methods: {
    handleSelectionChange(data) {
      this.selectedData = data;
    },

    formdate(date) {
      var json_date = new Date(date.start_time).toJSON();
      return new Date(+new Date(json_date) + 8 * 3600 * 1000)
        .toISOString()
        .replace(/T/g, " ")
        .replace(/\.[\d]{3}Z/, "");
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
  ::v-deep .el-transfer-panel{
      width: 299px !important;
    }
</style>
