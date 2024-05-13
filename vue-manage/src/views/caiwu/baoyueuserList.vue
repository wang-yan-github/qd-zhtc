<template >
  <div>
    <!--<el-row class="mgb20">-->
    <!--<el-col :span="24">-->
    <!--<el-button size="small" plain type="danger"-->
    <!--&gt;包月车辆总数：{{ bycl }}-->
    <!--</el-button-->
    <!--&gt;-->
    <!--</el-col>-->
    <!--</el-row>-->
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleEdit(0, null, true)"
            v-permission="'road_monthusermanaA_add'"
            >添加
          </el-button>
          <el-button
            type="success"
            size="small"
            @click="byexport(query.isTheCompany, 0)"
            icon="el-icon-upload2"
            v-permission="'road_monthusermanaA_excel'"
            >导出
          </el-button>
        </div>
        <div class="right-panel">
          <el-input
            @keyup.enter="handleSearch()"
            size="small"
            v-model="query.str"
            placeholder="车牌查询"
            class="handle-input mr10"
          ></el-input>
          <el-input
            @keyup.enter="handleSearch()"
            size="small"
            v-model="query.dwName"
            placeholder="单位名称"
            class="handle-input mr10"
          ></el-input>
          <el-select
            v-model="query.isTheCompany"
            filterable
            size="small"
            placeholder="包月类型"
            class="w100"
            @change="handleSearch"
          >
            <el-option
              key="1"
              label="个人包月"
              value="1"
              aria-checked="true"
            ></el-option>
            <el-option key="2" label="企业包月" value="2"></el-option>
          </el-select>
          <span class="dispinline ml5"></span>
          <el-select
            clearable
            v-model="query.variance"
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
          <span class="dispinline ml5"></span>
          <el-select
            clearable
            v-model="query.variance2"
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
          <span class="dispinline ml5"></span>
          <el-select
            clearable
            v-model="query.variance3"
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

          <span class="dispinline ml5"></span>
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
        :max-height="tableH"
        header-cell-class-name="table-header"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column
          prop="isTheCompany"
          label="类型"
          width="100"
          align="center"
          ><template #default="scope">
            <p v-if="scope.row.isTheCompany == 1">个人</p>
            <p v-if="scope.row.isTheCompany == 2">公司</p>
          </template></el-table-column
        >

        <el-table-column
                prop="name"
                label="姓名"
                v-if="query.isTheCompany == 1"
                width="100"
                align="center"
        ></el-table-column>
        <el-table-column
                prop="phone"
                label="手机号码"
                v-if="query.isTheCompany == 1"
                width="120"
                align="center"
        ></el-table-column>
        <el-table-column
                prop="dwName"
                label="单位名称"
                v-if="query.isTheCompany == 1"
                width="120"
                align="center"
        ></el-table-column>

        <el-table-column
          prop="company_name"
          label="公司"
          v-if="query.isTheCompany == 2"
          width="100"
          align="center"
        ></el-table-column>

        <el-table-column
          prop="carNo"
          v-if="query.isTheCompany == 1"
          label="车牌"
          width="100"
          align="center"
        ></el-table-column>

        <el-table-column prop="roadNameList" label="包月路内" align="center">
          <template #default="scope">
            <el-tag
              size="small"
              v-for="(item, index) in scope.row.roadNameList"
              :key="index"
              class="mar5 mb5"
              >{{ item }}</el-tag
            >
          </template>
        </el-table-column>

        <el-table-column
          prop="start_time"
          label="开始时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="end_time"
          label="结束时间"
          align="center"
        ></el-table-column>

        <el-table-column
          prop="userName"
          label="添加人"
          align="center"
        ></el-table-column>
        <!--<el-table-column-->
        <!--prop="create_time"-->
        <!--label="添加时间"-->
        <!--align="center"-->
        <!--&gt;</el-table-column>-->
        <el-table-column label="操作" width="170" align="right">
          <template #default="scope">
            <div class="edit-btn-box">
              <!--<el-button-->
              <!--size="mini"-->
              <!--type="text"-->
              <!--icon="el-icon-info"-->
              <!--@click="handleInfo(scope.$index, scope.row)"-->
              <!--&gt;详情-->
              <!--</el-button>-->
              <el-button
                v-if="Date.parse(scope.row.end_time) > new Date()"
                size="mini"
                type="text"
                icon="el-icon-bank-card"
                @click="xfInfo(scope.$index, scope.row, false)"
                v-permission="'road_monthusermanaA_xf'"
                >续费
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleEdit(scope.$index, scope.row, false)"
                v-permission="'road_monthusermanaA_edit'"
                >编辑
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                class="red"
                @click="handleDelete(scope.$index, scope.row)"
                v-permission="'road_monthusermanaA_delete'"
                >删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="query.pageNum"
          :page-size="query.pageSize"
          :total="pageTotal"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>

    <!-- 编辑弹出框 -->
    <el-dialog
      :title="dialogT"
      v-model="editVisible"
      width="40%"
      top="5vh"
      :close-on-click-modal="false"
      :showClose="false"
    >
      <el-form label-width="70px" size="small">
        <el-form-item label="类型">
          <el-radio-group v-model="form.isTheCompany" @change="agreeChange">
            <el-radio :label="1">个人</el-radio>
            <el-radio :label="2">公司</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="姓名" v-show="form.isTheCompany == 1">
          <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" v-show="form.isTheCompany == 1">
          <el-input v-model="form.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item label="单位名称" v-show="form.isTheCompany == 1">
          <el-input v-model="form.dwName" placeholder="请输入单位名称"></el-input>
        </el-form-item>
        <el-form-item label="车牌类型" v-show="form.isTheCompany == 1">
          <el-select v-model="form.carType" placeholder="请选择" class="w">
            <el-option key="1" label="蓝牌" value="1"></el-option>
            <el-option key="2" label="绿牌" value="2"></el-option>
            <el-option key="3" label="黄牌" value="3"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="车牌" v-show="form.isTheCompany == 1">
          <el-input v-model="form.carNo" placeholder="请输入车牌号"></el-input>
        </el-form-item>
        <el-form-item label="公司" v-show="form.isTheCompany != 1">
          <el-select
            v-model="form.companyId"
            filterable
            size="small"
            placeholder="所有公司"
            class="w"
            @change="getComCars"
          >
            <el-option
              v-for="item in companyData"
              :key="item.id"
              :label="item.company_name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-table
          v-show="form.isTheCompany != 1"
          :data="tableData3"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
          @selection-change="handleSelectionChange"
          row-key="id"
        >
          <el-table-column
            type="selection"
            width="50"
            align="center"
            reserve-selection
          ></el-table-column>

          <el-table-column
            prop="car_no"
            label="车牌号"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="carTypeName"
            label="车牌类型"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="rosterTypeName"
            label="名单类型"
            align="center"
          >
          </el-table-column>
        </el-table>
        <div class="pagination" v-show="form.isTheCompany != 1">
          <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="query.pageIndex"
            :page-size="5"
            :total="pageTotal2"
            @current-change="handlePageChange2"
          ></el-pagination>
        </div>
        <el-form-item label="包月方案">
          <el-select
            v-if="disabledFlag == true"
            v-model="form.bean.paymonthly_config_id"
            filterable
            size="small"
            placeholder="所有路内"
            class="w"
            @change="findPaymonthlyConfig"
          >
            <el-option
              v-for="item in paymonthlyData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
          <el-select
            v-if="disabledFlag == false"
            v-model="form.bean.paymonthly_config_id"
            filterable
            size="small"
            placeholder="所有路内"
            class="w"
            :disabled="true"
            @change="findPaymonthlyConfig"
          >
            <el-option
              v-for="item in paymonthlyData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <table class="desctable w mgb20" v-show="true">
          <tr>
            <td class="tit" width="60" align="center">价格</td>
            <td class="tit" width="60" align="center">类型</td>
            <td class="tit" width="60" align="center">开始时间</td>
            <td class="tit" width="60" align="center">结束时间</td>
            <td class="tit" align="center">路内</td>
          </tr>
          <tr>
            <td align="center">{{ pcinfo.price }}</td>
            <td align="center">{{ pcinfo.type }}</td>
            <td align="center">{{ pcinfo.startTime }}</td>
            <td align="center">{{ pcinfo.endTime }}</td>
            <td>{{ pcinfo.roadNames }}</td>
          </tr>
        </table>

        <el-form-item label="起始时间">
          <el-date-picker
            v-if="disabledFlag == true"
            v-model="form.bean.start_time"
            type="datetime"
            placeholder="选择日期"
            class="w"
          >
          </el-date-picker>
          <el-date-picker
            :disabled="true"
            v-if="disabledFlag == false"
            v-model="form.bean.start_time"
            type="datetime"
            placeholder="选择日期"
            class="w"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item label="月数">
          <el-select
            v-model="form.bean.months"
            placeholder="请选择"
            class="w"
            v-if="disabledFlag == true"
            @change="ys"
          >
            <el-option key="1" label="1" value="1"></el-option>
            <el-option key="2" label="2" value="2"></el-option>
            <el-option key="3" label="3" value="3"></el-option>
            <el-option key="4" label="4" value="4"></el-option>
            <el-option key="5" label="5" value="5"></el-option>
            <el-option key="6" label="6" value="6"></el-option>
            <el-option key="7" label="7" value="7"></el-option>
            <el-option key="8" label="8" value="8"></el-option>
            <el-option key="9" label="9" value="9"></el-option>
            <el-option key="10" label="10" value="10"></el-option>
            <el-option key="11" label="11" value="11"></el-option>
            <el-option key="12" label="12" value="12"></el-option>
            <el-option key="18" label="18" value="18"></el-option>
            <el-option key="24" label="24" value="24"></el-option>
          </el-select>
          <el-select
            v-model="form.bean.months"
            placeholder="请选择"
            class="w"
            v-if="disabledFlag == false"
            :disabled="true"
          >
            <el-option key="1" label="1" value="1"></el-option>
            <el-option key="2" label="2" value="2"></el-option>
            <el-option key="3" label="3" value="3"></el-option>
            <el-option key="4" label="4" value="4"></el-option>
            <el-option key="5" label="5" value="5"></el-option>
            <el-option key="6" label="6" value="6"></el-option>
            <el-option key="7" label="7" value="7"></el-option>
            <el-option key="8" label="8" value="8"></el-option>
            <el-option key="9" label="9" value="9"></el-option>
            <el-option key="10" label="10" value="10"></el-option>
            <el-option key="11" label="11" value="11"></el-option>
            <el-option key="12" label="12" value="12"></el-option>
            <el-option key="18" label="18" value="18"></el-option>
            <el-option key="24" label="24" value="24"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="支付方式">
          <el-select
            v-model="form.str"
            placeholder="请选择"
            class="w"
            v-if="disabledFlag == true"
          >
            <el-option key="5" label="现金" value="5"></el-option>
            <el-option key="6" label="银行卡" value="6"></el-option>
          </el-select>
          <el-select
            v-model="form.str"
            placeholder="请选择"
            class="w"
            v-if="disabledFlag == false"
            :disabled="true"
          >
            <el-option key="5" label="现金" value="5"></el-option>
            <el-option key="6" label="银行卡" value="6"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="转账流水号" v-if="form.str == 6">
          <el-input
            v-model="form.transferSerialNumber"
            placeholder="请输入转账流水号"
            oninput="if(isNaN(value)) { value = null } if(value.indexOf('.')>0){value=value.slice(0,value.indexOf('.')+3)}"
            v-if="disabledFlag == true"
          ></el-input>
          <el-input
            v-model="form.transferSerialNumber"
            placeholder="0"
            v-if="disabledFlag == false"
          ></el-input>
        </el-form-item>
        <el-form-item label="支付金额">
          <el-input
            v-model="form.amount"
            placeholder="请输入支付金额"
            oninput="if(isNaN(value)) { value = null } if(value.indexOf('.')>0){value=value.slice(0,value.indexOf('.')+3)}"
            v-if="disabledFlag == true"
          ></el-input>
          <el-input
            v-model="form.amount"
            placeholder="0"
            v-if="disabledFlag == false"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancle">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
          <!--<el-button type="danger" @click="computed" icon="el-icon-s-platform"-->
          <!--&gt;计算费用</el-button>-->
        </span>
      </template>
    </el-dialog>

    <!-- 续费弹出框 -->
    <el-dialog
      title="续费"
      v-model="xfIsShow"
      width="40%"
      top="5vh"
      :close-on-click-modal="false"
      :showClose="false"
    >
      <el-form label-width="70px" size="small">
        <el-form-item label="姓名" v-show="form.isTheCompany == 1">
          <el-input
            v-model="form.name"
            placeholder="请输入姓名"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="电话" v-show="form.isTheCompany == 1">
          <el-input
            v-model="form.phone"
            placeholder="请输入电话"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="单位名称" v-show="form.isTheCompany == 1">
          <el-input
            v-model="form.dwName"
            placeholder="请输入单位名称"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="车牌类型" v-show="form.isTheCompany == 1">
          <el-select
            v-model="form.carType"
            placeholder="请选择"
            class="w"
            :disabled="true"
          >
            <el-option key="1" label="蓝牌" value="1"></el-option>
            <el-option key="2" label="绿牌" value="2"></el-option>
            <el-option key="3" label="黄牌" value="3"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="车牌" v-show="form.isTheCompany == 1">
          <el-input
            v-model="form.carNo"
            placeholder="请输入车牌号"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="公司" v-show="form.isTheCompany != 1">
          <el-select
            v-model="form.companyId"
            filterable
            size="small"
            placeholder="所有公司"
            class="w"
            @change="getComCars"
            :disabled="true"
          >
            <el-option
              v-for="item in companyData"
              :key="item.id"
              :label="item.company_name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-table
          v-show="form.isTheCompany != 1"
          :data="tableData4"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
          @selection-change="handleSelectionChange"
          row-key="id"
        >
          <el-table-column
            prop="car_no"
            label="车牌号"
            width="200"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="carTypeName"
            label="车牌类型"
            align="center"
            width="200"
          ></el-table-column>
          <el-table-column
            prop="rosterTypeName"
            label="名单类型"
            align="center"
            width="230"
          >
          </el-table-column>
        </el-table>
        <div class="pagination" v-show="form.isTheCompany != 1">
          <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="query.pageIndex"
            :page-size="5"
            :total="pageTotal3"
            @current-change="handlePageChange2"
          ></el-pagination>
        </div>
        <el-form-item label="包月方案">
          <el-select
            v-if="disabledFlag == true"
            v-model="form.bean.paymonthly_config_id"
            filterable
            size="small"
            placeholder="所有路内"
            class="w"
            @change="findPaymonthlyConfig"
            :disabled="true"
          >
            <el-option
              v-for="item in paymonthlyData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
          <el-select
            v-if="disabledFlag == false"
            v-model="form.bean.paymonthly_config_id"
            filterable
            size="small"
            placeholder="所有路内"
            class="w"
            :disabled="true"
            @change="findPaymonthlyConfig"
          >
            <el-option
              v-for="item in paymonthlyData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <table class="desctable w mgb20" v-show="true">
          <tr>
            <td class="tit" width="60" align="center">价格</td>
            <td class="tit" width="60" align="center">类型</td>
            <td class="tit" width="60" align="center">开始时间</td>
            <td class="tit" width="60" align="center">结束时间</td>
            <td class="tit" align="center">路内</td>
          </tr>
          <tr>
            <td align="center">{{ pcinfo.price }}</td>
            <td align="center">{{ pcinfo.type }}</td>
            <td align="center">{{ pcinfo.startTime }}</td>
            <td align="center">{{ pcinfo.endTime }}</td>
            <td>{{ pcinfo.roadNames }}</td>
          </tr>
        </table>

        <el-form-item label="起始时间">
          <el-date-picker
            v-if="disabledFlag == true"
            v-model="form.bean.start_time"
            type="datetime"
            placeholder="选择日期"
            :disabled="true"
            class="w"
          >
          </el-date-picker>
          <el-date-picker
            :disabled="true"
            v-if="disabledFlag == false"
            v-model="form.bean.start_time"
            type="datetime"
            placeholder="选择日期"
            class="w"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-if="disabledFlag == true"
            v-model="form.bean.end_time"
            type="datetime"
            placeholder="选择日期"
            :disabled="true"
            class="w"
          >
          </el-date-picker>
          <el-date-picker
            :disabled="true"
            v-if="disabledFlag == false"
            v-model="form.bean.end_time"
            type="datetime"
            placeholder="选择日期"
            class="w"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="月数">
          <el-select
            v-model="form.bean.months"
            placeholder="请选择"
            class="w"
            @change="ys2"
          >
            <el-option key="1" label="1" value="1"></el-option>
            <el-option key="2" label="2" value="2"></el-option>
            <el-option key="3" label="3" value="3"></el-option>
            <el-option key="4" label="4" value="4"></el-option>
            <el-option key="5" label="5" value="5"></el-option>
            <el-option key="6" label="6" value="6"></el-option>
            <el-option key="7" label="7" value="7"></el-option>
            <el-option key="8" label="8" value="8"></el-option>
            <el-option key="9" label="9" value="9"></el-option>
            <el-option key="10" label="10" value="10"></el-option>
            <el-option key="11" label="11" value="11"></el-option>
            <el-option key="12" label="12" value="12"></el-option>
            <el-option key="18" label="18" value="18"></el-option>
            <el-option key="24" label="24" value="24"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="支付方式">
          <el-select v-model="form.str" placeholder="请选择" class="w">
            <el-option key="5" label="现金" value="5"></el-option>
            <el-option key="6" label="银行卡" value="6"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="转账流水号" v-if="form.str == 6">
          <el-input
            v-model="form.transferSerialNumber"
            placeholder="请输入转账流水号"
            oninput="if(isNaN(value)) { value = null } if(value.indexOf('.')>0){value=value.slice(0,value.indexOf('.')+3)}"
            v-if="disabledFlag == true"
          ></el-input>
          <el-input
            v-model="form.transferSerialNumber"
            v-if="disabledFlag == false"
          ></el-input>
        </el-form-item>
        <el-form-item label="支付金额">
          <el-input
            v-model="form.amount"
            placeholder="请输入支付金额"
            oninput="if(isNaN(value)) { value = null } if(value.indexOf('.')>0){value=value.slice(0,value.indexOf('.')+3)}"
            v-if="disabledFlag == true"
          ></el-input>
          <el-input
            v-model="form.amount"
            placeholder="0"
            v-if="disabledFlag == false"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancle">取 消</el-button>
          <el-button type="primary" @click="xfSave">确 定</el-button>
          <!--<el-button type="danger" @click="computed" icon="el-icon-s-platform"-->
          <!--&gt;计算费用</el-button>-->
        </span>
      </template>
    </el-dialog>
    <!-- 详情弹出框 -->
    <el-dialog :title="dialogT" v-model="infoVisible" width="40%" top="5vh">
      <table class="desctable mgb20 w">
        <tr v-if="query.isTheCompany == 1">
          <td class="tit" width="160">车牌</td>
          <td colspan="3">{{ form.carNo }}</td>
        </tr>
        <tr>
          <td class="tit" width="160">包月路内</td>
          <td colspan="3">{{ pcinfo.configName }}</td>
        </tr>
        <tr>
          <td class="tit" width="80">价格</td>
          <td>{{ pcinfo.price }}</td>
          <td class="tit" width="80">类型</td>
          <td>{{ pcinfo.type }}</td>
        </tr>
        <tr>
          <td class="tit" width="80">开始时间</td>
          <td>{{ pcinfo.startTime }}</td>
          <td class="tit" width="80">结束时间</td>
          <td>{{ pcinfo.endTime }}</td>
        </tr>
        <tr>
          <td class="tit" width="80">路内</td>
          <td colspan="3">{{ pcinfo.roadNames }}</td>
        </tr>
        <tr>
          <td class="tit" width="80">起始时间</td>
          <td>{{ form.bean.start_time }}</td>
          <td class="tit" width="80">月数</td>
          <td>{{ form.bean.months }}</td>
        </tr>
        <tr>
          <td class="tit" width="80">支付方式</td>
          <td>{{ form.paymentTypeName }}</td>
          <td class="tit" width="80">支付金额</td>
          <td>{{ form.amount }}</td>
        </tr>
      </table>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  baoyueyonghuglList,
  baoyueyonghuglList2,
  paymonthlyAllList,
  baoyueyonghuglSave,
  paymonthlyDel,
  paymonthlyDelList,
  queryAreaData,
  queryStreetData,
  queryRoadData,
  normalMPCount,
  paymonthlyConfig,
  xf,
} from "../../api/index";
import {
  getList,
  getCompanyCars2,
  getCompanyCars3,
  info,
  num,
  allNum,
  getByCompanyCars,
  byExport,
  dowload,
} from "../../api/companyManage";

export default {
  name: "monthusermana",
  data() {
    return {
      tableH: 0,
    };
  },
  setup() {
    const query = reactive({
      pageNum: 1,
      pageSize: 15,
      str: "",
      str2: "0",
      variance: "",
      variance2: "",
      variance3: "",
      isTheCompany: "1",
      dwName: "",
    });
    const query2 = reactive({
      bean: {
        company_name: "",
      },
    });
    const paymonthlyData = ref([]);
    const companyData = ref([]);
    const tableData = ref([]);
    const pageTotal = ref(0);
    // const pageTotal2 = ref(0);
    const bycl = ref(0);
    let formqjl = reactive({
      time: "",
      areaId: "",
      streetId: "",
      roadId: "",
      areas: [],
      streets: [],
      roads: [],
    });

    // 获取包月列表数据
    // const getPaymonthlyAllList = () => {
    // 	paymonthlyAllList().then((res) => {
    // 		paymonthlyData.value = res.data;
    // 	});
    // };
    // 公司下拉列表接口
    const getCompanyAllList = () => {
      getList(query2).then((res) => {
        companyData.value = res.data;
      });
    };
    // 获取一个包月配置信息
    const pcquery = ref({
      id: "",
    });
    const pcinfo = reactive({
      configName: "",
      price: "",
      type: "",
      startTime: "",
      endTime: "",
      roadNames: "",
    });
    // const findPaymonthlyConfig = () => {
    // 	pcquery.id = form.bean.paymonthly_config_id;
    // 	paymonthlyConfig(pcquery).then((res) => {
    // 		pcinfo.configName = res.data.configName;
    // 		pcinfo.price = res.data.price;
    // 		pcinfo.type = res.data.type;
    // 		pcinfo.startTime = res.data.startTime;
    // 		pcinfo.endTime = res.data.endTime;
    // 		pcinfo.roadNames = res.data.roadNames;
    // 	});
    // };

    // 获取车牌信息
    // const getComCars = () => {
    // 	comCarsParams.companyId = form.companyId ;
    // 	getCompanyCars(comCarsParams).then((res) => {
    // 		let data = res.data;
    // 		tableData2.value = data.list;
    // 		pageTotal2.value = data.total;
    // 	});
    // };

    // getPaymonthlyAllList();
    getCompanyAllList();

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
      areaId: query.variance,
    });
    const getStreet = () => {
      queryStreet.areaId = query.variance;
      query.variance2 = "";
      query.variance3 = "";
      queryStreetData(queryStreet).then((res) => {
        formqjl.streets = res.data;
      });
    };

    //获取路内下拉框数据
    const queryRoad = reactive({
      streetId: query.variance2,
    });
    const getRoad = () => {
      query.variance3 = "";
      queryRoad.streetId = query.variance2;
      queryRoadData(queryRoad).then((res) => {
        formqjl.roads = res.data;
      });
    };

    // 分页导航
    const handlePageChange = (val) => {
      query.pageNum = val;
      getData();
    };

    const dialogT = ref("新增");

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    // 详情弹窗
    const infoVisible = ref(false);
    let form = reactive({
      id: "",
      carNo: "",
      carNos: "",
      isTheCompany: 1,
      carType: "",
      carTypes: "",
      carTypeName: "",
      str: "",
      paymentTypeName: "",
      amount: "",
      bean: {
        id: "",
        paymonthly_config_id: "",
        start_time: "",
        end_time: "",
        months: "",
        transact_style: "0",
      },
      companyId: "",
      monthly_code: "",
      transferSerialNumber: "",
      name: "",
      phone: "",
      months: "",
      dwName: "",
    });
    let comCarsParams = reactive({
      companyId: "",
      pageIndex: 1,
      pageSize: 5,
    });
    let idx = -1;
    const disabledFlag = ref(false);
    // const handleEdit = (index, row, type) => {
    //
    // };

    return {
      query,
      query2,
      bycl,
      paymonthlyData,
      companyData,
      tableData,
      pageTotal,
      editVisible,
      infoVisible,
      form,
      formqjl,
      dialogT,
      handlePageChange,
      pcquery,
      pcinfo,
      queryArea,
      queryStreet,
      queryRoad,
      getArea,
      getStreet,
      getRoad,
      multipleSelection: [],
      value: true,
      activeName: "first",
      disabledFlag,
      companyId: "",
      carNoList: [],
      isTheCompany: 1,
      str2: 0,
      num: 0,
      pageTotal2: "",
    };
  },
  created: function () {
    this.getPaymonthlyAllList(1, this.str2);
    this.getData();
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
  data() {
    return {
      tableData3: [],
      selectedData: [],
      index: 1,
      size: 10000,
      xfIsShow: false,
      carNum: "",
      tableData4: [],
      pageTotal3: 0,
      xf: {},
    };
  },
  methods: {
    handleSelectionChange(data) {
      var that = this;
      that.selectedData = data;
      that.computed();
    },

    handleDeleteAll() {
      var that = this;
      var val = that.selectData;
      let strs = "";
      if (val) {
        val.forEach(function (item, index) {
          strs += item.id + ",";
        });
        paymonthlyDelList(reactive({ str: strs })).then((res) => {
          if (res.code == 0) {
            ElMessage.success(res.logMsg);
            that.getData();
          } else {
            ElMessage.error(res.logMsg);
          }
        });
      } else {
        ElMessage.warning(`请选择一条记录`);
      }
    },
    handleEdit(index, row, type) {
      var that = this;
      setTimeout(function () {
        that.$refs["multipleTable"].clearSelection();
      }, 300);
      that.carNoList = [];
      that.disabledFlag = type;
      if (type) {
        that.dialogT = "新增";
        that.form.monthly_code = "";
        that.form.name = "";
        that.form.phone = "";
        that.form.carNo = "";
        that.form.carType = "";
        that.form.carTypeName = "";
        that.form.str = "";
        that.form.paymentTypeName = "";
        that.form.amount = "";
        that.form.bean.id = "";
        that.form.bean.paymonthly_config_id = "";
        that.form.bean.start_time = "";
        that.form.bean.months = "";
        that.form.dwName = "";
        that.pcinfo.price = "";
        that.pcinfo.type = "";
        that.pcinfo.startTime = "";
        that.pcinfo.endTime = "";
        that.pcinfo.roadNames = "";
        that.form.companyId = "";
        that.tableData3 = [];
        that.pageTotal2 = 0;
        that.getPaymonthlyAllList(1, this.str2);
      } else {
        that.size = 10000;
        that.dialogT = "编辑";
        that.form.carNo = row.carNo;
        that.form.name = row.name;
        that.form.phone = row.phone;
        that.form.isTheCompany = parseInt(row.isTheCompany);
        if (row.companyId != null && row.companyId != ""  && row.companyId != undefined) {
          that.form.companyId = parseInt(row.companyId);
        }
        that.form.monthly_code = row.monthly_code;
        that.form.carType = row.carType;
        that.form.carTypeName = row.carTypeName;
        that.form.str = row.paymentType;
        that.form.paymentTypeName = row.paymentTypeName;
        that.form.amount = row.amount;
        that.form.bean.id = row.id;
        that.form.bean.paymonthly_config_id = row.paymonthly_config_id;
        that.pcquery.id = row.paymonthly_config_id;
        that.form.bean.start_time = row.start_time;
        that.form.bean.months = row.months;
        that.form.dwName = row.dwName;
        that.info(row.monthly_code);
        that.getComCars();
        that.findPaymonthlyConfig();
        // that.getComCars2();
        // setTimeout(function () {
        // 	that.toggleSelection(that.tableData3,row.carNo,row.carTypeName) ;
        // },500)
      }
      that.editVisible = true;
    },
    handleInfo(index, row, type) {
      var that = this;
      that.dialogT = "详情";
      that.form.carNo = row.carNo;
      that.form.carType = row.carType;
      that.form.carTypeName = row.carTypeName;
      that.form.str = row.paymentType;
      that.form.paymentTypeName = row.paymentTypeName;
      that.form.amount = row.amount;
      that.form.bean.id = row.id;
      that.form.bean.paymonthly_config_id = row.paymonthly_config_id;
      that.pcquery.id = row.paymonthly_config_id;
      that.form.bean.start_time = row.start_time;
      that.form.bean.months = row.months;
      that.form.dwName = row.dwName;
      that.findPaymonthlyConfig();
      that.infoVisible = true;
    },
    // 分页导航
    handlePageChange2(val) {
      var that = this;
      that.index = val;
      that.getComCars();
      // setTimeout(function () {
      // 	that.toggleSelection(that.tableData2) ;
      // },150)
    },
    getComCars() {
      var that = this;

      getCompanyCars2(that.form.companyId, that.index, that.size).then(
        (res) => {
          that.tableData3 = res.data.list;
          that.pageTotal2 = res.data.total;

          that.toggleSelection(that.tableData3);
          if (that.size != 5) {
            console.log(that.selectedData);
          }

          setTimeout(function () {
            if (that.size == 10000) {
              that.size = 5;
              that.getComCars();
            }
          }, 300);
        }
      );
    },
    // getComCars2(){
    // 	var that = this ;
    // 	getCompanyCars3(that.form.companyId).then((res) => {
    // 		console.log(res.data);
    // 		let data = res.data;
    // 		that.tableData3 = data.list;
    // 	});
    // },
    findPaymonthlyConfig() {
      var that = this;
      that.pcquery.id = that.form.bean.paymonthly_config_id;
      paymonthlyConfig(that.pcquery).then((res) => {
        that.pcinfo.configName = res.data.configName;
        that.pcinfo.price = res.data.price;
        that.pcinfo.type = res.data.type;
        that.pcinfo.startTime = res.data.startTime;
        that.pcinfo.endTime = res.data.endTime;
        that.pcinfo.roadNames = res.data.roadNames;
      });
      that.computed();
    },
    info(monthly_code) {
      var that = this;

      info(monthly_code).then((res) => {
        that.carNoList = res.data;
      });
    },
    toggleSelection(rows) {
      var that = this;

      if (rows) {
        that.$nextTick(() => {
          rows.forEach((row) => {
            that.carNoList.forEach((car) => {
              if (row.car_no == car.carNo && row.car_type == car.carType) {
                setTimeout(function () {
                  that.$refs.multipleTable.toggleRowSelection(row, true);
                }, 300);
              }
            });
          });
        });
      }
    },
    onTableSelect(rows, row) {
      let selected = rows.length && rows.indexOf(row) !== -1;
      console.log(selected); // true就是选中，0或者false是取消选中
    },
    getPaymonthlyAllList(data, str2) {
      var that = this;
      paymonthlyAllList(data, str2).then((res) => {
        that.paymonthlyData = res.data;
      });
    },
    agreeChange: function (val) {
      let that = this;
      that.getPaymonthlyAllList(val, that.str2);
    },
    computed() {
      var that = this;
      // 月份
      //新增计算价格
      var catNum = 0;
      let sumAmount;
      if (
        that.form.monthly_code == "" ||
        that.form.monthly_code == null ||
        that.form.monthly_code == undefined
      ) {
        if (that.form.isTheCompany == 1) {
          //个人
          let months = that.form.bean.months;
          // 单价
          let price = that.pcinfo.price;
          sumAmount = months * price;
          that.form.amount = sumAmount + "";
        } else {
          var val = that.selectedData;
          catNum = val.length;
          let months = that.form.bean.months;
          // 单价
          let price = that.pcinfo.price;
          sumAmount = months * price * catNum;
          that.form.amount = sumAmount + "";
        }
      } else {
        //修改计算价格
        //湖区需要包月的数量
        if (that.form.isTheCompany == 1) {
          //个人
          let months = that.form.bean.months;
          // 单价
          let price = that.pcinfo.price;
          sumAmount = months * price;
          that.form.amount = sumAmount + "";
        } else {
          var val = that.selectedData;
          if (val.length > 0) {
            var strs = 0;
            val.forEach(function (item, index) {
              strs = strs + 1;
            });
            var carnum;
            num(that.form.monthly_code, strs).then((res) => {
              carnum = res.data;
              let months = that.form.bean.months;
              let price = that.pcinfo.price;
              // 单价
              sumAmount = months * price * carnum;
              console.log(sumAmount);
              that.form.amount = sumAmount + "";
            });
          }
        }
      }
      // that.form.amount = sumAmount + "";
    },
    ys() {
      var that = this;
      that.computed();
    },
    cancle() {
      var that = this;
      setTimeout(function () {
        that.$refs["multipleTable"].clearSelection();
      }, 300);
      that.editVisible = false;
      that.xfIsShow = false;
    },
    saveEdit() {
      var that = this;
      //editVisible.value = false;
      let carNo = "";
      let carType = "";
      let name = "";
      let phone = "";
      if (that.form.isTheCompany == 1) {
        carNo = that.form.carNo;
        carType = that.form.carType;
        name = that.form.name;
        phone = that.form.phone;
      } else {
        console.log(that.selectedData);
        var val = that.selectedData;
        val.forEach(function (item, index) {
          carNo += item.id + ",";
        });
        carNo = carNo.substring(0, carNo.lastIndexOf(","));
        console.log(carType);
      }

      let startTime = that.form.bean.start_time;
      let str = that.form.str;
      let amount = that.form.amount;
      let isTheCompany = that.form.isTheCompany;
      if (isTheCompany == 1) {
        if (carType == "" || carType == undefined) {
          ElMessage.error("请选择车牌类型");
          return;
        }
        if (name == "" || name == undefined) {
          ElMessage.error("请输入姓名");
          return;
        }
        if (phone == "" || phone == undefined) {
          ElMessage.error("请输入手机号");
          return;
        }
      }

      if (carNo == "" || carNo == undefined) {
        ElMessage.error("请输入车牌");
        return;
      }
      if (startTime == "" || startTime == undefined) {
        ElMessage.error("请选择起始时间");
        return;
      }
      if (that.dialogT == "新增" && (str == "" || str == undefined)) {
        ElMessage.error("请选择支付方式");
        return;
      }
      if (that.disabledFlag) {
        if (amount == "" || amount == undefined || amount < 0) {
          ElMessage.error("请先点击计算费用");
          return;
        }
      }

      let months = that.form.bean.months;
      // 方案
      let pcid = that.form.bean.paymonthly_config_id;
      // 单价

      if (pcid == "" || pcid == undefined || pcid < 0) {
        ElMessage.error("请先选择包月方案");
        return;
      }
      if (months == "" || months == undefined || months < 0) {
        ElMessage.error("请先选择包月数");
        return;
      }

      var dateTime = that.form.bean.start_time;
      var dstr = dateTime + " ";
      if (dstr.search("GMT") != -1) {
        var Year = dateTime.getFullYear();
        var month =
          dateTime.getMonth() + 1 >= 10
            ? dateTime.getMonth() + 1
            : "0" + (dateTime.getMonth() + 1);
        var day =
          dateTime.getDate() >= 10
            ? dateTime.getDate()
            : "0" + dateTime.getDate();
        var hours =
          dateTime.getHours() < 10
            ? "0" + dateTime.getHours()
            : dateTime.getHours();
        var minutes =
          dateTime.getMinutes() < 10
            ? "0" + dateTime.getMinutes()
            : dateTime.getMinutes();
        var seconds =
          dateTime.getSeconds() < 10
            ? "0" + dateTime.getSeconds()
            : dateTime.getSeconds();
        that.form.bean.start_time =
          Year +
          "-" +
          month +
          "-" +
          day +
          " " +
          hours +
          ":" +
          minutes +
          ":" +
          seconds;
      }
      that.form.carNos = carNo;
      that.form.carTypes = carType;

      baoyueyonghuglSave(that.form).then((res) => {
        if (res.code != 0) {
          ElMessage.error(res.logMsg);
        } else {
          // getData();
          that.handleSearch();
          // that.size =10000 ;
          // that.$router.go(0);
        }
        that.editVisible = false;
      });
    },
    // 查询操作
    handleSearch() {
      var that = this;
      that.query.pageNum = 1;
      if (that.query.isTheCompany == 1) {
        that.getData();
      } else {
        that.getData2();
      }
    },
    // 获取表格数据
    getData() {
      var that = this;
      baoyueyonghuglList(that.query).then((res) => {
        that.tableData = res.data.list;
        that.pageTotal = res.data.total;
        that.getNormalMPCount();
      });
    },
    getData2() {
      var that = this;
      baoyueyonghuglList2(that.query).then((res) => {
        that.tableData = res.data.list;
        that.pageTotal = res.data.total;
        that.getNormalMPCount();
      });
    },
    // 包月车辆总数
    getNormalMPCount() {
      var that = this;
      normalMPCount(that.query).then((res) => {
        that.bycl = res.data;
      });
    },
    // 删除操作
    handleDelete(index, row) {
      var that = this;
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          paymonthlyDel(
            reactive({
              monthly_code: row.monthly_code,
              id: row.id,
              isTheCompany: row.isTheCompany,
            })
          ).then((res) => {
            if (res.code == 0) {
              if (that.query.isTheCompany == 1) {
                that.getData();
              } else if (that.query.isTheCompany == 2) {
                that.getData2();
              }
              ElMessage.success(res.msg);
            } else {
              ElMessage.error(res.msg);
            }
          });
        })
        .catch(() => {});
    },
    xfInfo(index, row, type) {
      var that = this;
      that.form.carNo = row.carNo;
      that.form.name = row.name;
      that.form.phone = row.phone;
      that.form.isTheCompany = parseInt(row.isTheCompany);
      if (row.companyId != null && row.companyId != ""  && row.companyId != undefined) {
        that.form.companyId = parseInt(row.companyId);
      }
      that.form.monthly_code = row.monthly_code;
      that.form.carType = row.carType;
      that.form.carTypeName = row.carTypeName;
      that.form.str = row.paymentType;
      that.form.paymentTypeName = row.paymentTypeName;
      that.form.bean.id = row.id;
      that.form.bean.paymonthly_config_id = row.paymonthly_config_id;
      that.pcquery.id = row.paymonthly_config_id;
      that.form.bean.start_time = row.start_time;
      that.form.bean.end_time = row.end_time;
      that.form.bean.months = 1;
      that.form.dwName = row.dwName;

      // that.info(row.monthly_code) ;
      that.index = 1;
      that.byCar(row.monthly_code);
      that.findPaymonthlyConfig();
      setTimeout(function () {
        var price;
        var months;
        var carnum;
        price = that.pcinfo.price;
        months = that.form.bean.months;
        if (row.isTheCompany == 1) {
          setTimeout(function () {
            // 单价
            let sumAmount = months * price;
            that.form.amount = sumAmount + "";
          }, 300);
        } else if (row.isTheCompany == 2) {
          var sumAmount;
          setTimeout(function () {
            sumAmount = months * price * that.pageTotal3;
            that.form.amount = sumAmount + "";
          }, 300);
          // allNum(that.form.monthly_code).then((res) => {
          //     carnum = res.data;
          //     that.carNum = carnum ;
          //
          // });
        }
      }, 500);
      // that.getComCars();
      that.xfIsShow = true;
    },
    ys2() {
      var that = this;
      // 月份
      //新增计算价格
      var catNum = 0;
      let sumAmount;
      if (that.form.isTheCompany == 1) {
        //个人
        let months = that.form.bean.months;
        // 单价
        let price = that.pcinfo.price;
        sumAmount = months * price;
        that.form.amount = sumAmount + "";
      } else {
        catNum = that.pageTotal3;
        let months = that.form.bean.months;
        let price = that.pcinfo.price;
        // 单价
        sumAmount = months * price * catNum;
        that.form.amount = sumAmount + "";
      }
    },
    byCar(monthly_code) {
      var that = this;

      getByCompanyCars(monthly_code, that.index, 5).then((res) => {
        that.tableData4 = res.data.list;
        that.pageTotal3 = res.data.total;
      });
    },
    xfSave() {
      var that = this;
      xf(that.form).then((res) => {
        if (res.code != 0) {
          ElMessage.error(res.logMsg);
        } else {
          that.handleSearch();
        }
        that.xfIsShow = false;
      });
    },
    byexport(isTheCompany, str) {
      var that = this;

      byExport(isTheCompany, str).then((res) => {
        if (res.code == 0) {
          ElMessage.success("正在下载中·····");
          dowload(res.data).then((res) => {});
        }
      });
    },
  },
};
</script>
<style scoped>
.pagination {
  margin-bottom: 20px;
}
.edit-btn-box {
  text-align: right;
}
</style>
