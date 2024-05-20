<template>
  <div>
    <div class="container">
      <div class="handle-box" v-show="result.sysUser.user_type == 0">
        <div class="left-panel">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleEdit()"
            v-permission="'park_parkinglist_add'"
            >添加
          </el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            v-permission="'park_parkinglist_deleteAll'"
            >批量删除
          </el-button>
        </div>
        <div class="right-panel">
          <el-form inline size="small">
            <el-input
              @keyup.enter="handleSearch()"
              size="small"
              v-model="query.park_name"
              placeholder="请输入停车场名称"
              class="handle-input mr10"
            ></el-input>
            <span class="dispinline ml5"></span>

            <el-button
              size="small"
              type="primary"
              icon="el-icon-search"
              @click="handleSearch"
              >查询
            </el-button>
            <el-button size="small" icon="el-icon-upload2" type="success"  @click="exportParking"
              >导出</el-button
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
        :max-height="tableH"
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
          label="停车场编号"
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
          prop="park_name"
          label="名称"
          align="center"
        ></el-table-column>
        <!-- <el-table-column
          prop="city"
          label="城市"
          width="100"
          align="center"
        ></el-table-column> -->
        <el-table-column
          prop="area_name"
          label="区域"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="street_name"
          label="街道"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="brand"
          label="厂商"
          align="center"
          width="50"
        ></el-table-column>

        <el-table-column label="状态" align="center" width="100">
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
        <el-table-column
          prop="park_num"
          label="总泊位"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="free_count"
          label="空闲泊位"
          width="100"
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
          prop="create_time"
          label="时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column label="限流开关" align="center" width="100">
          <template #default="scope">
            <el-tag size="small" :type="
                scope.row.on_off === '0'
                  ? 'success'
                  : scope.row.on_off === '1'
                  ? 'danger'
                  : ''
              "
                    v-if="scope.row.on_off == 0">开启
            </el-tag>
            <el-tag size="small" :type="
                scope.row.on_off === '0'
                  ? 'success'
                  : scope.row.on_off === '1'
                  ? 'danger'
                  : ''
              "
                    v-else>关闭
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="320"
          align="center"
          v-if="result.sysUser.user_type == '0'"
        >
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              v-permission="'park_parkinglist_details'"
              >查看设备
            </el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
              v-permission="'park_parkinglist_edit'"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              @click="handleStop(scope.$index, scope.row, '1')"
              v-if="scope.row.status == '0'"
              class="red"
              v-permission="'park_parkinglist_stop'"
              >停用
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleStop(scope.$index, scope.row, '0')"
              v-if="scope.row.status == '1'"
              v-permission="'park_parkinglist_stop'"
              >启用
            </el-button>

            <el-dropdown class="ml10 red" @command="handleCommand">
              <span class="el-dropdown-link">
                更多操作<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <template v-slot:dropdown>
                <el-dropdown-menu>
                  <!-- <el-dropdown-item icon="el-icon-view" command="a"  @click="handleView(scope.$index, scope.row)"
                  >查看设备</el-dropdown-item
                  > -->

                  <el-dropdown-item
                    icon="el-icon-delete"
                    command="a"
                    @click="handleDelete(scope.$index, scope.row)"
                    v-permission="'park_parkinglist_delete'"
                    >删除
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.on_off == 0" icon="el-icon-turn-off" command="a"
                        @click="handleOnOff(scope.$index, scope.row)"
                          v-permission="'park_parkinglist_onoff'"
                  >限流关闭</el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.on_off == 1" icon="el-icon-open" command="a"
                          @click="handleOnOff(scope.$index, scope.row)"
                          v-permission="'park_parkinglist_onoff'"
                  >限流开启</el-dropdown-item>
                  <el-dropdown-item
                    icon="el-icon-money"
                    command="b"
                    @click="handleRemark(scope.$index, scope.row)"
                    v-permission="'park_parkinglist_sfsm'"
                    >收费说明
                  </el-dropdown-item>

                  <el-dropdown-item icon="el-icon-refresh-right" command="e"   v-permission="'park_parkinglist_sbcq'"
                    >批量重启设备
                  </el-dropdown-item>
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
    <!-- 停车场收费说明 -->
    <el-dialog title="停车场收费说明" v-model="ppVisible" width="560px">
      <el-form label-width="90px">
        <el-form-item label="停车场名称">
          <el-tag>{{ result.park.park_name }}</el-tag>
        </el-form-item>
        <el-form-item label="收费说明">
          <el-input
            v-model="result.park.charge_remark"
            type="textarea"
            rows="10"
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
    <!-- 新增编辑弹窗 -->
    <edit ref="edit"></edit>
  </div>
</template>

<script>
import { ref, reactive, nextTick,getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import edit from './parkingList/edit.vue'
import {
  parkList,
  delParkAll,
  editPark,
  loginUser,
  exportParkingData,
} from "../../api/index";
import { useRouter } from "vue-router";

export default {
  name: "parkinglist",
  components: {edit},
  data() {
    return {
      tableH: 0,
      charge: {
        blue_charge_id: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        green_charge_id: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        yellow_charge_id: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
      },
    };
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const query = reactive({
      park_name: "",
      area_id: "",
      street_id: "",
      pageIndex: 1,
      pageSize: 15,
    });
    let form = reactive({
      park_code: "",
      traffic_park_code: "",
      park_name: "",
      park_num: "",
      park_grade: "",
      area_id: "",
      street_id: "",
      address: "",
      brand: "",
      coordinate: "",
      longitude: "",
      latitude: "",
      blue_charge_id: "",
      green_charge_id: "",
      yellow_charge_id: "",
      id: "",
      searchkey: "",
      limit_monthly: "",
      free_count: "",
    });
    // 结果集
    const result = reactive({
      area_list: [],
      park_grades: [],
      street_list: [],
      query_street_list: [],
      chargeProgramme_list: [],
      sysUser: {},
      park: {},
      dialogT: "",
    });

    const getLoginUser = () => {
      loginUser().then((res) => {
        result.sysUser = res.data;
      });
    };
    getLoginUser();

    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      parkList(query).then((res) => {
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

    // 停用操作
    const handleStop = (index, row, sta) => {
      // 二次确认停用
      ElMessageBox.confirm("确定当前操作吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_park = reactive({
            status: sta,
            id: row.id,
          });
          editPark(edit_park)
            .then((res) => {
              // console.log(res);
              ElMessage.success("停用成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => {});
    };

    // 删除操作
    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_park = reactive({
            is_del: "1",
            id: row.id,
          });
          editPark(edit_park)
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
    // 限流开启/关闭
    const handleOnOff = (index, row) => {
        var msg = "开启";
        var on_off = "0";
        if (row.on_off == "0") {
            msg = "关闭";
            on_off = "1";
        } else {
            msg = "开启";
            on_off = "0";
        }
      ElMessageBox.confirm("确定要"+msg+"吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_park = reactive({
            on_off: on_off,
            id: row.id,
          });
          editPark(edit_park)
            .then((res) => {
              // console.log(res);
              ElMessage.success(msg+"成功");
            })
            .then(() => {
              getData();
            });
        })
        .catch(() => {});
    };
    const dialogT = "编辑";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const ppVisible = ref(false);
    const router = useRouter();
    let idx = "";

    const handleEdit = (row) => {
      proxy.$refs.edit.open(row);
    };

    // 收费说明
    const handleRemark = (index, row) => {
      result.park = row;

      ppVisible.value = true;
    };

    const handleView = (index, row) => {
      router.push({
        path: "equipmentListA",
        query: {
          park_id: row.id,
          area_id: row.area_id,
          street_id: row.street_id,
          routerAlive: false,
        },
      });
    };
    const saveRemark = () => {
      ppVisible.value = false;

      let edit_park = reactive({
        id: result.park.id,
        charge_remark: result.park.charge_remark,
      });

      editPark(edit_park).then((res) => {
        // console.log(res);
        ElMessage.success("修改成功");
      });
    };

    return {
      query,
      result,
      tableData,
      pageTotal,
      viewVisible,
      handleRemark,
      saveRemark,
      handleStop,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleOnOff,
      handleEdit,
      handleView,
      getData,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible,
    };
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
  methods: {
    //上传图片操作

    handleRemove(file, fileList) {
      // console.log(file, fileList);
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
      //this.$message("click on item " + command);
    },
    handleDeleteAll() {
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          var that = this;
          var val = this.selectedData;
          // console.log(val);
          var ids = "";
          if (val) {
            val.forEach(function (item, index) {
              //alert(item.id);
              ids = ids + item.id + ",";
            });
            delParkAll({ parkIds: ids }).then((res) => {
              ElMessage.success("删除成功");
              that.getData();
            });
          } else {
            ElMessage.warning(`请选择一条记录`);
          }
        })
        .catch(() => {});
    },
    exportParking() {
      ElMessage.success("正在下载中·····");
      exportParkingData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "停车场管理.xls");
        document.body.appendChild(link);
        link.click();
      });
    },
  },
};
</script>
