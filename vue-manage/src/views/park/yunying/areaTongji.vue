<template>
  <div>
    <div class="container">

      <el-table
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column pro="ID" label="ID" width="55" align="center">
          <template #default="scope">
            {{ scope.row.id }}
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="操作人"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="purviews"
          label="角色"
          width="120"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="rizhiconent" label="日志内容"> </el-table-column>

        <el-table-column
          prop="addtimes"
          label="操作时间"
          width="160"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="ip" label="IP地址" width="160" align="center">
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
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { journalData } from "../../../api/index";

export default {
  name: "areaTongjiA",
  setup() {
    const query = reactive({
      address: "",
      name: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    let form = reactive({
      name: "",
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
    selvalue:'',
    date1:'',
    date2:'',

    });
    
    // 获取表格数据
    const getData = () => {
      journalData(query).then((res) => {
        tableData.value = res.list;
        pageTotal.value = res.pageTotal || 50;
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

    return {
      query,
      tableData,
      form,
      pageTotal,
      handleSearch,
      handlePageChange,
    };
  },
  methods: {},
};
</script>

