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
            v-permission="['road_streelist_add', 'park_streelist_edit']"
            >添加</el-button
          >
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            v-permission="['road_streelist_deleteAll', 'park_streelist_deleteAll']"
            >批量删除</el-button
          >
        </div>
        <div class="right-panel">
          <el-input
            @keyup.enter="handleSearch()"
            size="small"
            v-model="query.street_name"
            placeholder="街道名称"
            class="handle-input mr10"
          ></el-input>
          <el-select v-model="query.area_id" placeholder="所有区域" size="small">
            <el-option value="">全部</el-option>
            <el-option v-for="(item,i) in result.area_list" :key="i" :label="item.area_name" :value="item.id"></el-option>
          </el-select>
          <span class="ml5"></span>
          <el-button
            size="small"
            type="primary"
            icon="el-icon-search"
            @click="handleSearch"
            >查询</el-button
          >
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
          prop="street_name"
          label="街道名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="street_level"
          label="街道等级"
          width="100"
          align="center"
        >
        <template #default="scope">
          <p v-if="scope.row.street_level==0">一类</p>
          <p v-if="scope.row.street_level==1">二类</p>
          <p v-if="scope.row.street_level==2">三类</p>
        </template>
        </el-table-column>
        <!-- <el-table-column
          prop="city"
          label="所属城市"
          width="160"
          align="center"
        ></el-table-column> -->
        <el-table-column
          prop="area_name"
          label="所属区域"

          align="center"
        ></el-table-column>
        <el-table-column
          prop="road_num"
          label="路内数量"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="create_time"
          label="添加时间"
          align="center"
          width="180"
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
              v-if="scope.row.status==0">启用</el-tag
            >
            <el-tag
              size="small"
              :type="
                scope.row.status === '0'
                  ? 'success'
                  : scope.row.status === '1'
                  ? 'danger'
                  : ''
              "
              v-else>禁用</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              v-permission="['road_streelist_details', 'park_streelist_details']"
              >查看
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              v-permission="['road_streelist_edit', 'park_streelist_edit']"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-close"
              @click="handleStop(scope.$index, scope.row,'1')"
              v-if="scope.row.status=='0'"
              v-permission="['road_streelist_status', 'park_streelist_status']"
              class="red"
              >禁用
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleStop(scope.$index, scope.row,'0')"
              v-if="scope.row.status=='1'"
              v-permission="['road_streelist_status', 'park_streelist_status']"
              >启用
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              v-permission="['road_streelist_delete', 'park_streelist_delete']"
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
    <el-dialog :title="result.dialogT" v-model="editVisible" width="30%">
      <el-form label-width="90px" :rules="formRules"  :model="form">
        <el-form-item label="街道名称" prop="street_name">
          <el-input v-model="form.street_name" placeholder="请输入街道名称"></el-input>
        </el-form-item>
        <el-form-item label="所属区域" prop="area_id">
          <el-select v-model="form.area_id" placeholder="所有区域" class="w">


            <el-option v-for="(item,i) in result.area_list" :key="i" :label="item.area_name" :value="item.id"></el-option>

          </el-select>
        </el-form-item>
        <el-form-item label="区域等级" prop="street_level">
          <el-select v-model="form.street_level" placeholder="请选择" class="w">
            <el-option v-for="(item,i) in result.street_levels" :key="i" :label="item.label" :value="item.dc_value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看弹出框 -->
    <el-dialog title="查看详情" v-model="viewVisible" width="50%">
      <table class="desctable mgb20 w">
        <tr>
          <td class="tit" width="60">街道名称</td>
          <td>{{result.street.street_name}}</td>
        </tr>
        <tr>
          <td class="tit" width="60">所属区域</td>
          <td>{{result.street.area_name}}</td>
        </tr>
        <tr>
          <td class="tit" width="60">区域等级</td>

          <td>
            <p v-if="result.street.street_level==0">一类</p>
            <p v-if="result.street.street_level==1">二类</p>
            <p v-if="result.street.street_level==2">三类</p>
          </td>
        </tr>
      </table>
      <div class="handle-title mb5"><i class="el-icon-set-up"></i>包含路内</div>
      <el-row>
        <el-col :span="24">
          <el-form @submit.prevent inline size="small"
            ><el-form-item>
              <el-input
            size="small"
            v-model="road.road_name"
            placeholder="路内名称"
            class="handle-input mr10"
          ></el-input></el-form-item
            ><el-form-item
              ><el-button type="primary" @submit.prevent   @click="handleRoad">查询</el-button></el-form-item
            ></el-form
          >
          <el-table
            :data="tableData2"
            border
            class="table"
            ref="multipleTable"
            header-cell-class-name="table-header"
          >

            <el-table-column type="index" label="序号" width="55" align="center">

        </el-table-column>

            <el-table-column label="路内名称">
              <template #default="scope">{{ scope.row.road_name }}</template>
            </el-table-column>
            <!-- <el-table-column
              prop="city"
              label="所属城市"
              align="center"
              width="100"
            ></el-table-column> -->
            <el-table-column
              prop="area_name"
              label="所属区域"
              width="100"
              align="center"
            ></el-table-column>
            <el-table-column prop="longitude,latitude" align="center" label="坐标" width="180">
              <template #default="scope">
                {{scope.row.longitude}},{{scope.row.latitude}}
              </template>
            </el-table-column>
            <el-table-column prop="create_time" label="添加时间" align="center" width="180">
            </el-table-column>

          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="road.pageIndex"
              :page-size="road.pageSize"
              :total="pageTotal2"
              @current-change="roadPageChange"
            ></el-pagination>
          </div>
        </el-col>
      </el-row>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { streetList,delStreetAll,addStreet,queryAreaData,dictData,editStreet,roadList } from "../../api/index";

export default {
  name: "streelist",
   data(){
            return{
                tableH:0,
                formRules: {
                    street_name: [{ required: true, message: "必填项", trigger: "blur" }],
                    area_id: [{ required: true, message: "必填项", trigger: "blur" }],
                    street_level: [{ required: true, message: "必填项", trigger: "blur" }],
                },
            };
        },
  setup() {
    const query = reactive({
      street_name:"",
      area_id:"",
      pageIndex: 1,
      pageSize: 15,

    });
    const tableData = ref([]);
    const tableData2 = ref([]);
    const pageTotal = ref(0);
    const pageTotal2 = ref(0);

    // 结果集
    let result=reactive({
      area_list:[],
      street_levels:[],
      street:{},
      dialogT:""
    });
    // 区域
    const getAreaData = () => {
      queryAreaData(query).then((res) => {
        // console.log(res);
         result.area_list= res.data;
        // console.log(res.data);
      });
    };

    getAreaData();

    // 查询街道等级
    let dict = reactive({
      is_del:"0",
      dict_type:"streetLevel",
    });
    const getStreetLevel = () => {
      dictData(dict).then((res) => {
          console.log(res);
          result.street_levels=res.data;
        });
    };

    getStreetLevel();

    // 获取表格数据
    const getData = () => {
      streetList(query).then((res) => {
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
    const handleStop = (index,row,sta) => {
      // 二次确认停用
      ElMessageBox.confirm("请确定当前操作？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_street= reactive({
            status:sta,
            id:row.id
          });
          editStreet(edit_street).then((res) => {
            console.log(res);
            ElMessage.success("停用成功");
          }).then(() =>{
          getData();
        });
        })
        .catch(() => {});
    };


    // 删除操作
    const handleDelete = (index,row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      }).then(() => {
          let edit_street= reactive({
            is_del:"1",
            id:row.id
          });
          editStreet(edit_street).then((res) => {
            console.log(res);
            ElMessage.success("删除成功");
          }).then(() =>{
            getData();
          });
      })
        .catch(() => {});
    };


    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);

    let form = reactive({
      street_name:"",
      street_level:"",
      area_id:""
    });
    let idx = "";
    const handleEdit = (index, row, type) => {
      if (type) {
        result.dialogT='新增'
        idx = "";
        form.street_name="";
        form.area_id="";
        form.street_level="";

      } else {
        result.dialogT='编辑';
        idx = row.id;
        Object.keys(form).forEach((item) => {
          form[item] = row[item];
        });
      }
      getAreaData();


      editVisible.value = true;
    };
    // 查看
    let road=reactive({
        street_id:"",
        is_del:"0",
        road_name:"",
        pageIndex: 1,
        pageSize: 5,
      });
    const handleView = (index, row) => {
      console.log(row);
      // idx = index;
      result.street=row;
      road.street_id=row.id;
      road.road_name="",
      roadList(road).then((res) => {
          console.log(res);
          tableData2.value=res.data.list;
          pageTotal2.value = res.data.total;
      }).then(()=>{
         viewVisible.value = true;
        });
    };
    // 根据路内名进行查询
    const handleRoad = () => {
      roadList(road).then((res) => {
          console.log(res);
          tableData2.value=res.data.list;
          pageTotal2.value = res.data.total;
      })
    };

    const roadPageChange = (val) => {
      road.pageIndex = val;
      handleRoad();
    };



    const saveEdit = () => {


      // 不可为空
      if(form.street_name==null||form.street_name==""||form.street_level==null||form.street_level==""||form.area_id==null||form.area_id==""){
          ElMessage.error("参数不可为空！");
          return false;
      }

      // 新增
      if(!idx){
        addStreet(form).then((res) => {
          console.log(res.data);
        }).then(()=>{
          query.pageIndex = 1;
          getData();
        });
      }else{// 编辑
          let edit_street= reactive({
            area_id:form.area_id,
            street_name:form.street_name,
            street_level:form.street_level,
            id:idx
          });

          editStreet(edit_street).then((res) => {
            console.log(res);
            ElMessage.success("修改成功");
          }).then(() =>{
            getData();
          });



      }


      editVisible.value = false;

    };

    return {
      query,
      tableData,
      tableData2,
      pageTotal,
      pageTotal2,
      editVisible,
      viewVisible,
      form,

      result,
      road,
      handleRoad,
      handleSearch,
      handlePageChange,
      roadPageChange,
      handleDelete,
      handleEdit,
      handleView,
      handleStop,
      getData,
      saveEdit,
      multipleSelection: [],
      value: true,
      activeName: "first",
    };
  },
  created() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.tableH = h - 308 + 'px';
  },
  methods: {
    handleSelectionChange(data) {
      this.selectedData = data;
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
          ids = ids +  item.id + ",";
        });
        delStreetAll({ streetIds: ids }).then((res) => {
          ElMessage.success("删除成功");
          that.getData();
        })
      } else {
        ElMessage.warning(`请选择一条记录`);
      }
      }).catch(() => {});

    },
  },
};
</script>
