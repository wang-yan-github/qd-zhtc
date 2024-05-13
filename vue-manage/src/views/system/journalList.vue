<template>
  <div>
    <div class="container">
      <el-row>
        <el-col :span="24">
          <div class="top-panel">
            <el-form label-width="80px" inline size="small">
              <el-form-item label="日志内容">
                <el-input
                  v-model="query.log_content"
                  @keyup.enter="handleSearch()"
                  placeholder="请输入关键字"
                ></el-input>
              </el-form-item>
              <el-form-item label="操作人">
                <el-select
                  v-model="query.log_user_type"
                  clearable
                  filterable
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in users"
                    :key="item.id"
                    :label="item.user_name"
                    :value="item.id+'-'+item.type"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="时间">
                <!-- <el-date-picker
      v-model="form.selvalue"
      type="datetimerange"
      align="right"
      start-placeholder="开始日期"
      end-placeholder="结束日期"
      :default-time="['12:00:00', '08:00:00']">
    </el-date-picker> -->
                <el-date-picker
                  size="small"
                  v-model="form.time"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  class="datepicker"
                  @change="getQueryDate"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button
                  size="small"
                  type="primary"
                  icon="el-icon-search"
                  @click="handleSearch"
                  >查询</el-button
                >
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>

      <el-table
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        :max-height="tableH"
        header-cell-class-name="table-header"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column
          prop="user_name"
          label="操作人"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="role_name"
          label="用户类型"
          width="120"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="log_content" label="日志内容"> </el-table-column>

        <el-table-column
          prop="create_time"
          label="操作时间"
          width="200"
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
import {reactive, ref} from "vue";
import {queryLog, getUserList} from "../../api/index";

export default {
  name: "journalList",
  data() {
    return {
      tableH:0,
    };
  },
  setup() {
    const query = reactive({
      time: "",
      log_content: "",
      log_user_type: "",
      start_time: "",
      end_time: "",
      pageIndex: 1,
      pageSize: 15,
    });
    const form = ref({
      time: "",
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    const users = ref([]);

    // 获取表格数据
    const getData = () => {
      queryLog(query).then((res) => {
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

    //查询用户下拉选项
    const getAllUser = () => {
      getUserList(reactive({})).then((res) => {
        users.value = res.data;
      });
    };
    getAllUser();

    //日期控件change方法
    const getQueryDate = () => {
      if (
        null == form.value.time ||
        [] == form.value.time ||
        "" == form.value.time
      ) {
        query.start_time = "";
        query.end_time = "";
      } else {
        query.start_time = dateFormat(form.value.time[0]);
        query.end_time = dateFormat(form.value.time[1]);
      }
    };
    //日期格式化
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${
        time.getMonth() + 1 >= 10
          ? time.getMonth() + 1
          : "0" + (time.getMonth() + 1)
      }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
    };

    return {
      query,
      tableData,
      pageTotal,
      users,
      form,
      getAllUser,
      getQueryDate,
      handleSearch,
      handlePageChange,
    };
  },
   created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
  },
  methods: {},
};
</script>

