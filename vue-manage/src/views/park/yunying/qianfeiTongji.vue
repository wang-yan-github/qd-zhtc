<template>
  <div>
    <div class="container">
      <el-row>
        <el-col :span="24">
          <div class="top-panel">
            <el-form label-width="80px" inline size="small">
                <el-form-item>
                    <el-input placeholder="" v-model="form.num1" class="w40"></el-input>
                    <span class="color666 ml5 dispinline">元以上欠费</span>
                </el-form-item>
              <el-form-item label="时间">
                <el-date-picker
                  v-model="form.date1"
                  type="datetime"
                  placeholder="开始日期"
                  class="w170"
                >
                </el-date-picker
                ><span class="mar5 color999">-</span
                ><el-date-picker
                  v-model="form.date2"
                  type="datetime"
                  placeholder="结束日期"
                  class="w170"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item label="选择层级">
                <el-select
                  clearable
                  v-model="form.selvalue"
                  filterable
                  placeholder="所有区域"
                  class="w100"
                >
                  <el-option
                    v-for="item in form.czroptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <span class="mar5 color999"></span>
                <el-select
                  clearable
                  v-model="form.selvalue"
                  filterable
                  placeholder="所有街道"
                  class="w100"
                >
                  <el-option
                    v-for="item in form.czroptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <span class="mar5 color999"></span>
                <el-select
                  clearable
                  v-model="form.selvalue"
                  filterable
                  placeholder="所有路内"
                  class="w100"
                >
                  <el-option
                    v-for="item in form.czroptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button size="small" type="primary" icon="el-icon-search"
                  >查询</el-button
                >
                <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
              </el-form-item>
              <el-form-item>
                <el-button size="small" type="success" icon="el-icon-upload2"
                  >导出</el-button
                >
                <!-- <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button> -->
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>

      <el-table
        :data="tableData"
        border
        class="table"
        :max-height="tableH"
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
  name: "roadTongjiA",
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
      selvalue: "",
      date1: "",
      date2: "",
      num1:50,
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
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
  },
};
</script>

