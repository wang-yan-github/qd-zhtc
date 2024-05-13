<template>
  <div>
    <!-- <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 用户管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div> -->
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleEdit(0, null, true)"
            >添加</el-button
          >
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="handleDeleteAll()"
            >批量删除</el-button
          >
        </div>
        <div class="right-panel">
          <el-input
            @keyup.enter="handleSearch()"
            size="small"
            v-model="query.label"
            placeholder="字典值"
            class="handle-input mr10"
          ></el-input>
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
          prop="dict_type"
          label="字典类型"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="label"
          label="字典值"
          width="100"
          align="center"
        ></el-table-column>
        <!-- <el-table-column label="头像(查看大图)" align="center">
                    <template #default="scope">
                        <el-image class="table-td-thumb" :src="scope.row.thumb" :preview-src-list="[scope.row.thumb]">
                        </el-image>
                    </template>
                </el-table-column> -->

        <el-table-column
          prop="dc_value"
          label="鼎驰键值"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="dq_value"
          label="鼎器键值"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="js_value"
          label="捷顺键值"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="jk_value"
          label="交控键值"
          align="center"
          width="180"
        ></el-table-column>
       <el-table-column
          prop="dict_desc"
          label="描述"
          align="center"
        ></el-table-column>

        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row, false)"
              >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
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

    <!-- 新增弹出框 -->
    <el-dialog  title="新增" v-model="editVisible" width="30%">
          <el-form label-width="70px">
            <el-form-item label="字典类型">
              <el-input v-model="form.dict_type" placeholder="请输入字典类型"></el-input>
            </el-form-item>
            <el-form-item label="字典值">
              <el-input v-model="form.label" placeholder="请输入字典值"></el-input>
            </el-form-item>
             <el-form-item label="鼎驰键值">
              <el-input v-model="form.dc_value" placeholder="请输入鼎驰键值"></el-input>
            </el-form-item>
            <el-form-item label="鼎器键值">
              <el-input v-model="form.dq_value" placeholder="请输入鼎器键值"></el-input>
            </el-form-item>
            <el-form-item label="捷顺键值">
              <el-input v-model="form.js_value" placeholder="请输入捷顺键值"></el-input>
            </el-form-item>
            <el-form-item label="交控键值">
              <el-input v-model="form.jk_value" placeholder="请输入交控键值"></el-input>
            </el-form-item>
            <el-form-item label="字典描述">
              <el-input v-model="form.dict_desc" placeholder="请输入字典描述"></el-input>
            </el-form-item>
          </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { dictList,addDict,dictData,editDict } from "../../api/index";

export default {
  name: "dictlist",
  data() {
    return {
      tableH:0,
    };
  },
  setup() {
    const query = reactive({
      label: "",
      pageIndex: 1,
      pageSize: 15,
    });


    let result = reactive({
      area_levels:[]
    });


    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      dictList(query).then((res) => {
        console.log(res.data);
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


 // 查询区域等级
    let dict = reactive({
      is_del:"0",
      dict_type:"areaLevel",

    });
    const getAreaLevel = () => {
      dictData(dict).then((res) => {
          console.log(res);
          result.area_levels=res.data;
        });
    };

    getAreaLevel();



    // 删除操作
    const handleDelete = (index,row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      }).then(() => {
          let edit_dict= reactive({
            is_del:"1",
            id:row.id
          });
          editDict(edit_dict).then((res) => {
            console.log(res);
            ElMessage.success("删除成功");
          }).then(() =>{
            getData();
          });
        })
        .catch(() => {});
    };

    // 停用操作
    const handleStop = (index,row) => {
      // 二次确认停用
      ElMessageBox.confirm("确定要停用吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          let edit_area= reactive({
            status:"1",
            id:row.id
          });
          editArea(edit_area).then((res) => {
            console.log(res);
            ElMessage.success("停用成功");
          }).then(() =>{
          getData();
        });
        })
        .catch(() => {});
    };


    const dialogT = "新增";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);

    let form = reactive({
      id:"",
      dict_type: "",
      label:"",
      dict_desc: "",
      dc_value: "",
      dq_value: "",
      js_value: "",
      jk_value: "",
    });




    let idx = "";
    const handleEdit = (index, row, type) => {
      // 获取区域信息

      if (type) {
        //dialogT='新增'
        idx = "";
        form.id="";
        form.dict_type ="";
        form.label  ="";
        form.dict_desc  ="";
        form.dc_value  ="";
        form.dq_value  ="";
        form.js_value  ="";
        form.jk_value  ="";
      } else {
        //dialogT='编辑';
        idx = row.id;
        Object.keys(form).forEach((item) => {
          form[item] = row[item];
        });
      }

      editVisible.value = true;
    };

    const saveEdit = () => {
      editVisible.value = false;
      // 新增
      if(!idx){
        addDict(form).then((res) => {
          console.log(res.data);
        }).then(()=>{
          query.pageIndex = 1;
          getData();
        });
      }else{ // 编辑
          // let edit_Dict= reactive({
          //   id:idx,
          //   dict_type: form.dict_type,
          //   label:form.label,
          //   dict_desc: form.dict_desc,
          //   dc_value: form.dc_value,
          //   dq_value: form.dq_value,
          //   js_value: form.js_value,
          //   jk_value: form.jk_value,
          // });
          editDict(form).then((res) => {
            console.log(res);
            ElMessage.success("修改成功");
          }).then(()=>{
            getData();
          });


      }

    };

    return {
      query,
      tableData,
      pageTotal,
      editVisible,
      result,
      form,
      dict,
      dialogT,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleEdit,
      handleStop,
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
  },
  mounted() {
    // this.getData();
    //this.initMap();
  },
};
</script>
