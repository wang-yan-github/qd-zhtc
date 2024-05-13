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
            size="small"
            v-model="query.name"
            placeholder="公司名称"
            class="handle-input mr10"
          ></el-input>
          <el-input
            size="small"
            v-model="query.name"
            placeholder="联系人"
            class="handle-input mr10"
          ></el-input>
		  <el-input
		    size="small"
		    v-model="query.name"
		    placeholder="联系方式"
		    class="handle-input mr10"
		  ></el-input>
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
		:max-height="tableH"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column pro="ID" label="ID" width="55" align="center">
          <template #default="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="公司名称"
          align="center"
        ></el-table-column>
        <el-table-column label="联系人" width="180" align="center">
          <template #default="scope">{{ scope.row.money }}</template>
        </el-table-column>
        <el-table-column
          prop="addtimes"
          label="联系方式"
          align="center"
          width="180"
        ></el-table-column>
        <el-table-column
          prop="logintimes"
          label="公司地址"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="280" align="center">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.$index, scope.row)"
              >查看
            </el-button>
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
			  icon="el-icon-check"
			  @click="bindCar(scope.$index, scope.row, false)"
			  >绑定
			</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-close"
              @click="cancelBind(scope.$index, scope.row, false)"
              >解绑
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

    <!-- 编辑弹出框 -->
    <el-dialog :title="dialogT" v-model="editVisible" width="30%">
      <el-form label-width="70px">
        <el-form-item label="公司名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
		<el-form-item label="联系方式">
		  <el-input v-model="form.name"></el-input>
		</el-form-item>
		<el-form-item label="公司地址">
		  <el-input v-model="form.name"></el-input>
		</el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

	<!-- 绑定车辆 -->
	<el-dialog title="绑定车辆" v-model="bindCarVisible" width="50%">
		<div class="handle-box">
			<div class="right-panel">
				<el-button
				  type="primary"
				  size="small"
				  icon="el-icon-download"

				  >批量导入</el-button
				>
			</div>
			<div class="clear"></div>
		</div>

		<div class="mt20"></div>
		<table class="w desctable" ref="addTable">
            <colgroup>
              <col />
              <col />
              <col class="w100" />
            </colgroup>
            <thead>
              <th>车牌号</th>
              <th>车牌类型</th>
              <th>
                <el-button
                  round
                  size="mini"
                  type="primary"
                  icon="el-icon-plus"
                  @click="addItem()"
                ></el-button>
              </th>
            </thead>
            <tbody>
              <tr v-for="(item, index) in formArr" :key="index">
                <td>
                  <el-input size="mini"
                    ></el-input
                  >
                </td>
                <td>
                  <el-input size="mini"
                    ></el-input
                  >
                </td>
                <td align="center">
                  <el-button
                    round
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    @click="delItem(index)"
                  ></el-button>
                </td>
              </tr>
            </tbody>
          </table>
		  <template #footer>
		    <span class="dialog-footer">
		      <el-button @click="bindCarVisible = false">取 消</el-button>
		      <el-button type="primary" @click="saveEdit">确 定</el-button>
		    </span>
		  </template>
	</el-dialog>
	<el-dialog title="解绑车辆" v-model="cancelBindCar" width="50%">

		<div class="handle-box">
			<div class="left-panel">
				<el-button
				  type="danger"
				  size="small"
				  icon="el-icon-circle-close"

				  >批量解绑</el-button
				>
			</div>
			<div class="right-panel">
			  <el-input
			    size="small"
			    v-model="query.name"
			    placeholder="车牌号"
			    class="handle-input mr10"
			  ></el-input>
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
		      :data="tableData2"
		      border
		      class="table"
		      ref="multipleTable"
		      header-cell-class-name="table-header"
		    >
				<el-table-column
				  type="selection"
				  width="55"
				  align="center"
				></el-table-column>
		      <el-table-column pro="ID" label="ID" width="55" align="center">
		        <template #default="scope">
		          {{ scope.row.id }}
		        </template>
		      </el-table-column>
		      <el-table-column prop="money" label="车牌号" width="160" align="center">

		      </el-table-column>
		      <el-table-column
		        prop="roles"
		        label="车牌类型"
		        align="center"
		      ></el-table-column>
					<el-table-column label="操作" width="160" align="center">
					  <template #default="scope">
					    <el-button
					      size="mini"
					      type="text"
					      icon="el-icon-close"
					      @click="carDelete(scope.$index, scope.row, false)"
					      >解绑
					    </el-button>
					  </template>
					</el-table-column>


		    </el-table>
		    <div class="pagination">
		      <el-pagination
		        background
		        layout="total, prev, pager, next"
		        :current-page="query.pageIndex"
		        :page-size="query.pageSize2"
		        :total="pageTotal"
		        @current-change="handlePageChange"
		      ></el-pagination>
		    </div>

	</el-dialog>
    <!-- 查看弹出框 -->
    <el-dialog title="查看详情" v-model="viewVisible" width="50%">
		<el-tabs type="card" v-model="activeName">
		  <el-tab-pane label="基本信息" name="first">
		    <div class="mt20"></div>
		    <table class="desctable mgb20 w">
		      <tr>
		        <td class="tit is-bordered-label" width="100"><i class="el-icon-office-building"></i> 公司名称</td>
		        <td>老城湟水湾</td>
		        <td class="tit is-bordered-label" width="100"><i class="el-icon-connection"></i> 绑定车辆数</td>
		        <td>26</td>
		      </tr>
		      <tr>
		        <td class="tit" width="100"><i class="el-icon-user"></i> 联系人</td>
		        <td>王文文</td>
		        <td class="tit" width="100"><i class="el-icon-mobile-phone"></i> 联系方式</td>
		        <td>13023212321</td>
		      </tr>
		      <tr>
				  <td class="tit" width="100"><i class="el-icon-location-information"></i> 公司地址</td>
				  <td colspan="3">老城湟水湾街道24号</td>
		      </tr>
		    </table>
		  </el-tab-pane>
		  <el-tab-pane label="绑定车辆信息" name="second">
		    <div class="mt20"></div>
		    <el-row>
		      <el-col :span="24">
		        <el-form inline size="small"
		          ><el-form-item><el-input placeholder="车牌号"></el-input></el-form-item
		          ><el-form-item
		            ><el-button type="primary">查询</el-button></el-form-item
		          ></el-form
		        >
		        <el-table
		          :data="tableData2"
		          border
		          class="table"
		          ref="multipleTable"
		          header-cell-class-name="table-header"
		        >
		          <el-table-column pro="ID" label="ID" width="55" align="center">
		            <template #default="scope">
		              {{ scope.row.id }}
		            </template>
		          </el-table-column>
		          <el-table-column prop="money" label="车牌号" width="160" align="center">

		          </el-table-column>
		          <el-table-column
		            prop="roles"
		            label="车牌类型"
		            align="center"
		          ></el-table-column>
		    			<el-table-column label="操作" width="160" align="center">
		    			  <template #default="scope">
		    			    <el-button
		    			      size="mini"
		    			      type="text"
		    			      icon="el-icon-close"
		    			      @click="carDelete(scope.$index, scope.row, false)"
		    			      >解绑
		    			    </el-button>
		    			  </template>
		    			</el-table-column>


		        </el-table>
		        <div class="pagination">
		          <el-pagination
		            background
		            layout="total, prev, pager, next"
		            :current-page="query.pageIndex"
		            :page-size="query.pageSize2"
		            :total="pageTotal"
		            @current-change="handlePageChange"
		          ></el-pagination>
		        </div>
		      </el-col>
		    </el-row>
		  </el-tab-pane>
		</el-tabs>

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
import { fetchData } from "../../../api/index";

export default {
  name: "streelist",
	data() {
		return {
			tableH:0,
		};
	},
  setup() {
    const query = reactive({
      address: "",
      name: "",
      pageIndex: 1,
      pageSize: 15,
      pageSize2: 6,
    });
    const tableData = ref([]);
    const tableData2 = ref([]);
    const pageTotal = ref(0);
    const pageTotal2 = ref(0);
    // 获取表格数据
    const getData = () => {
      fetchData(query).then((res) => {
        tableData.value = res.list;
        pageTotal.value = res.pageTotal || 50;
		tableData2.value = res.list;
		pageTotal2.value = res.pageTotal || 50;
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

	// 解除绑定询问
	const carDelete = (index) => {
	  // 二次确认删除
	  ElMessageBox.confirm("确定要解除绑定吗？", "提示", {
	    type: "warning",
	  })
	    .then(() => {
	      ElMessage.success("解绑成功");
	      tableData.value.splice(index, 1);
	    })
	    .catch(() => {});
	};



    // 删除操作
    const handleDelete = (index) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          ElMessage.success("删除成功");
          tableData.value.splice(index, 1);
        })
        .catch(() => {});
    };
    const dialogT = "新增";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const viewVisible = ref(false);
    const bindCarVisible = ref(false);
	const cancelBindCar = ref(false);

    let form = reactive({
      name: "",
      address: "",
    });
    let idx = -1;
    const handleEdit = (index, row, type) => {
      if (type) {
        //dialogT='新增'
      } else {
        //dialogT='编辑';
        idx = index;
        Object.keys(form).forEach((item) => {
          form[item] = row[item];
        });
      }

      editVisible.value = true;
    };

    const handleView = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        form[item] = row[item];
      });

      viewVisible.value = true;
    };

	const bindCar = (index, row) => {
	  idx = index;
	  Object.keys(form).forEach((item) => {
	    form[item] = row[item];
	  });

	  bindCarVisible.value = true;
	};

	const cancelBind = (index, row) => {
	  idx = index;
	  Object.keys(form).forEach((item) => {
	    form[item] = row[item];
	  });

	  cancelBindCar.value = true;
	};

    const saveEdit = () => {
      editVisible.value = false;
      ElMessage.success(`修改第 ${idx + 1} 行成功`);
      Object.keys(form).forEach((item) => {
        tableData.value[idx][item] = form[item];
      });
    };

	const formArr=[{ value1: "",value2: "" }];

    return {
      query,
      tableData,
      tableData2,
      pageTotal,
      pageTotal2,
      editVisible,
      viewVisible,
	  bindCarVisible,
	  cancelBindCar,
	  formArr,
      form,
      dialogT,
      handleSearch,
      handlePageChange,
      handleDelete,
      carDelete,
      handleEdit,
      handleView,
      bindCar,
	  cancelBind,
      saveEdit,
      multipleSelection: [],
      value: true,
      activeName: "first",
    };
  },
  methods: {
	  addItem() {
	    var that=this;
	    that.formArr.push({
	      value1: "",
	      value2: "",
	    });
	    that.$refs.addTable.clearSelection();
	    //console.log(this.formArr);
	    // this.$nextTick(function () {
	    //   this.formArr;
	    // });
	    //this.$forceUpdate
	  },
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
	created() {
		let h = document.documentElement.clientHeight || document.body.clientHeight;
		this.tableH = h - 308 + 'px';
	},
};
</script>
