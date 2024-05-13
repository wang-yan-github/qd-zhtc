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
        <!--<div class="left-panel">-->
          <!-- <el-button
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
          > -->
        <!--</div>-->
        <div >
          <el-input
            @keyup.enter="handleSearch()"
            size="small"
            v-model="query.order_no"
            placeholder="请输入订单号/发票派放人"
            class="handle-input"
          ></el-input>
          <span class="dispinline ml5"></span>
          <el-select
              v-model="query.area_id"
              filterable
              size="small"
              placeholder="所有区域"
              class="w100"
              @change="getStreetList()"
            >
             <el-option value="">全部</el-option>
              <el-option v-for="(item,i) in result.area_list" :key="i" :label="item.area_name" :value="item.id"></el-option>
            </el-select>
            <span class="dispinline ml5"></span>
            <el-select
              v-model="query.street_id"
              filterable
              size="small"
              placeholder="所有街道"
              class="w100"
              @change="getRoadList()"
            >
             <el-option value="">全部</el-option>
              <el-option v-for="(item,i) in result.query_street_list" :key="i" :label="item.street_name" :value="item.id"></el-option>
            </el-select>
            <span class="dispinline ml5"></span>
            <el-select
              v-model="query.road_id"
              filterable
              size="small"
              placeholder="所有路内"
              class="w100"
            >
             <el-option value="">全部</el-option>
              <el-option v-for="(item,i) in result.query_road_list" :key="i" :label="item.road_name" :value="item.id"></el-option>
            </el-select>
            <span class="dispinline ml5 font14 color666">派放时间：</span>
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
          <span class="dispinline ml5"></span>

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
        <!-- <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column> -->
        <el-table-column type="index" label="序号" width="55" align="center">

        </el-table-column>
        <el-table-column
          prop="order_no"
          label="订单号"
          width="190"
          align="center"
        ></el-table-column>
        <el-table-column
            prop="car_no"
            label="车牌号"
            width="120"
            align="center"
        >
          <template #default="scope">
            <el-tag
                size="small"
                v-if="scope.row.car_type == '1'"
                v-text = "scope.row.car_no"
            ></el-tag
            >
            <el-tag
                size="small"
                type="success"
                v-else-if="scope.row.car_type == '2'"
                v-text = "scope.row.car_no"
            ></el-tag
            >
            <el-tag
                size="small"
                type="warning"
                v-else-if="scope.row.car_type == '3'"
                v-text = "scope.row.car_no"
            ></el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="road_name"
          label="所属路内"
          min-width="200"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="drivein_time"
          label="驶入时间"
          width="170"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="driveout_time"
          label="驶出时间"
          width="170"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="resitime"
          label="停留时间(分)"
          width="110"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="sum_amount"
          label="应收金额"

          align="center"
        ></el-table-column>
        <el-table-column
          prop="paid_amount"
          label="已付金额"

          align="center"
        ></el-table-column>
        <el-table-column
          prop="status"
          label="订单状态"

          align="center"
        >
        <template #default="scope">
          <p v-if="scope.row.status==1">在停</p>
          <p v-if="scope.row.status==2">待缴费</p>
          <p v-if="scope.row.status==3">已缴费</p>
          <p v-if="scope.row.status==4">已完成</p>
        </template>
        </el-table-column>
        <!-- <el-table-column label="头像(查看大图)" align="center">
                    <template #default="scope">
                        <el-image class="table-td-thumb" :src="scope.row.thumb" :preview-src-list="[scope.row.thumb]">
                        </el-image>
                    </template>
                </el-table-column> -->
        <el-table-column
          prop="pay_type"
          label="支付方式"
          align="center"
          width="100"
        >
         <template #default="scope">
          <p v-if="scope.row.pay_type==1">包月</p>
          <p v-if="scope.row.pay_type==2">微信</p>
          <p v-if="scope.row.pay_type==3">支付宝</p>
          <p v-if="scope.row.pay_type==4">钱包</p>
          <p v-if="scope.row.pay_type==5">现金</p>
        </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="发票派放人"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="invoice_amount"
          label="派放金额"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="provide_time"
          label="派放时间"
          width="170"
          align="center"
        ></el-table-column>

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
            <el-form-item label="区域名称">
              <el-input v-model="form.order_no"></el-input>
            </el-form-item>
            <el-form-item label="区域等级">
              <el-select v-model="form.area_level" placeholder="无" class="w">

                <el-option v-for="(item,i) in result.area_levels" :key="i" :label="item.label" :value="item.dc_value"></el-option>

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
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { InvoicesProvideRecordList,queryAreaData,queryStreetData,queryRoadData } from "../../api/index";

export default {
  name: "providerecord",
  data() {
    return {
      tableH:0,
    };
  },
  setup() {
    const query = reactive({
      area_id:"",
      street_id:"",
       road_id:"" ,
      order_no: "",
      pageIndex: 1,
      pageSize: 15,
      start_time:"",
      end_time:""
    });


    let result = reactive({
       area_list:[],
      area_levels:[],
      query_street_list:[],
      query_road_list:[]
    });


    const tableData = ref([]);
    const pageTotal = ref(0);
    // 获取表格数据
    const getData = () => {
      InvoicesProvideRecordList(query).then((res) => {
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


  // 区域
    const getAreaData = () => {
      queryAreaData(query).then((res) => {
        // console.log(res);
         result.area_list= res.data;
        console.log(res.data);
      });
    };
    getAreaData();

    // 获取街道
    let queryStreet = reactive({
      areaId: query.area_id
    });

    const getStreetList = () => {

      result.query_road_list=[];
        queryStreet.areaId = query.area_id;
         query.road_id = "";
        query.street_id = "";

      queryStreetData(queryStreet).then((res) => {
        // console.log(res);

          result.query_street_list= res.data;

      });
    };

// 获取路内
     const queryRoad = reactive({
      streetId: query.street_id
    });
    const getRoadList = (type) => {


        queryRoad.streetId = query.street_id;
        query.road_id="";

      queryRoadData(queryRoad).then((res) => {
        console.log(res.data);

          result.query_road_list=res.data;


      });
    };






    // 删除操作
    const handleDelete = (index,row) => {

    };

    // 停用操作
    const handleStop = (index,row) => {

    };


    const dialogT = "新增";

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);

    let form = reactive({
      order_no: "",
      area_level:"",
      status: "0",
      is_del: "0",
      time:"",
    });

    //日期控件change方法
    const getQueryDate = () => {


     if(form.time==null||form.time==""){
       query.start_time="";
       query.end_time ="";
      return false;
     }

      query.start_time= dateFormat(form.time[0])+" ";

      console.log(query.start_time);

      query.end_time = dateFormat(form.time[1])+" ";

    }


    //日期格式化
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()}`
    }


    let idx = "";
    const handleEdit = (index, row, type) => {
      // 获取区域信息


    };

    const saveEdit = () => {





    };

    return {
      query,
      tableData,
      pageTotal,
      editVisible,
      result,
      form,
      getQueryDate,
      dialogT,
      handleSearch,
      handlePageChange,
      getStreetList,
      getRoadList,
      getData,
      handleDelete,
      handleEdit,
      handleStop,
      saveEdit,
      multipleSelection: [],
      value: true,
      activeName: "first",
    };
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
        delAreaAll({ areaIds: ids }).then((res) => {
          ElMessage.success("删除成功");
          that.getData();
        })
      } else {
        ElMessage.warning(`请选择一条记录`);
      }
      }).catch(() => {});

    },
  },
  mounted() {
    // this.getData();
    //this.initMap();
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + 'px';
  },
};
</script>
