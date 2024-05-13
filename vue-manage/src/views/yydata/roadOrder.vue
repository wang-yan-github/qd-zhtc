<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline label-width="80" size="small" class="lineH0">
            <el-form-item label="车牌号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.carNo"
                placeholder="车牌号"
                class="w100"
              ></el-input>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-checkbox v-model="query.checked">黄牌</el-checkbox>
            </el-form-item>
            <el-form-item label="" class="search-mb0">
              <el-checkbox v-model="query.checked2" @change="handleSearch()">车牌复核</el-checkbox>
            </el-form-item>
            <el-form-item label="订单号" class="search-mb0">
              <el-input @keyup.enter="handleSearch()" size="small" placeholder="订单号" v-model="query.orderNo"></el-input>
            </el-form-item>
            <el-form-item label="订单状态" class="search-mb0">
              <el-select
                clearable
                v-model="query.status"
                filterable
                size="small"
                class="w100"
              >
                <el-option key="1" label="在停 " value="1"></el-option>
                <el-option key="2" label="待缴费 " value="2"></el-option>
                <el-option key="3" label="已缴费 " value="3"></el-option>
                <el-option key="4" label="已完成 " value="4"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="手机号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.phone"
                placeholder="手机号"
                class="w100"
              ></el-input>
            </el-form-item>
            <el-form-item label="泊位号" class="search-mb0">
              <el-input
                @keyup.enter="handleSearch()"
                size="small"
                v-model="query.berth"
                placeholder="泊位号"
                class="w100"
              ></el-input>
            </el-form-item>
            <el-form-item label="停入时间" class="search-mb0">
              <el-date-picker
                v-model="timeData.time"
                placeholder=""
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="getQueryDate"
              ></el-date-picker>
            </el-form-item>
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
            <el-form-item label="街道">
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
            <el-form-item label="路内">
              <el-select
                clearable
                v-model="query.placeId"
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
            <el-form-item label="" class="search-mb0">
              <el-button
                size="small"
                type="primary"
                icon="el-icon-search"
                @click="handleSearch"
                >查询</el-button
              >
              <el-button size="small" type="success" @click="exportRoadPark" icon="el-icon-upload2" v-permission="'road_order_excel'"
                >导出</el-button
              >
            </el-form-item>
            <!-- <el-form-item label=""
              ><el-button
                size="small"
                type="text"
                icon="el-icon-arrow-up"
                @click="isActive = !isActive"
                >展开</el-button
              ></el-form-item
            > -->

            <el-form-item
              label=""
              v-show="result.model == 1"
              class="search-mb0"
            >
              <el-button
                type="success"
                size="small"
                icon="el-icon-document"
                @click="handleSpecial()"
                >特殊开闸</el-button
              >
            </el-form-item>
            <div v-show="isActive">
              <el-form-item label="" class="search-mb0">
                <el-select
                  v-model="form.selvalue"
                  filterable
                  size="small"
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
              </el-form-item>
              <el-form-item label="" class="search-mb0">
                <el-select
                  v-model="form.selvalue"
                  filterable
                  size="small"
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
              </el-form-item>
              <el-form-item label="" class="search-mb0">
                <el-select
                  v-model="form.selvalue"
                  filterable
                  size="small"
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
              </el-form-item>
            </div>
          </el-form>
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
        @row-dblclick="rowclick"
      >
        <!--<el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>-->
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
            {{scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="orderNo"
          label="订单号"
          width="200"
          align="center"
        >
          <template #default="scope">
            <el-tag
                    size="small"
                    type="warning"
                    v-if="(scope.row.status == 1 || scope.row.status == 2) && scope.row.carId2 != null && scope.row.carId != scope.row.carId2"
                    v-text="scope.row.orderNo"
            ></el-tag>
            <el-tag
                    size="small"
                    v-else
                    v-text="scope.row.orderNo"
            ></el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="placeName"
          label="路内"
          align="center"
          min-width="280"
        ></el-table-column>
        <el-table-column
          prop="berth"
          label="泊位编号"
          width="130"
          align="center"
        ></el-table-column>
        <!-- <el-table-column prop="carNo" label="车牌号码" width="100" align="center"></el-table-column> -->
        <el-table-column prop="carNo" label="车牌号" width="110" align="center">
          <template #default="scope">
            <el-tag
              size="small"
              v-if="scope.row.car_type == '1'"
              v-text="scope.row.carNo"
            ></el-tag>
            <el-tag
              size="small"
              type="success"
              v-else-if="scope.row.car_type == '2'"
              v-text="scope.row.carNo"
            ></el-tag>
            <el-tag
              size="small"
              type="warning"
              v-else-if="scope.row.car_type == '3'"
              v-text="scope.row.carNo"
            ></el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="driveinTime"
          label="停入时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="driveoutTime"
          label="离场时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="resitime"
          width="120"
          label="停留时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="sum_amount"
          label="停车费用"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="sum_amount"
          label="商家优惠"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag size="small" v-if="scope.row.is_discount == '0'">是</el-tag>
            <el-tag size="small" type="success" v-else>否</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="discount_money"
          label="优惠金额"
          width="100"
          align="center"
        >
          <template #default="scope">
            <template v-if="scope.row.discount_money == null"> 0.00 </template>
            <template v-else>
              {{ scope.row.discount_money }}
            </template>
          </template>
        </el-table-column>
        <el-table-column
          prop="is_merge"
          label="合并订单"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-tickets"
              v-if="scope.row.is_merge == '1'"
              @click="getMergeOrder(scope.$index, scope.row)"
              >合并记录</el-button
            >
            <el-button
              type="text"
              size="mini"
              icon="el-icon-tickets"
              disabled
              v-else
              >合并记录</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="free_type"
          label="免单类型"
          width="100"
          align="center"
        >
          <!--<template #default="scope">-->
            <!--<span v-if="scope.row.sum_amount > 0"></span>-->
            <!--<span v-else>{{ scope.row.free_type }}</span>-->
          <!--</template>-->
        </el-table-column>

        <!--<el-table-column prop="status_name" label="状态" width="80" align="center"> </el-table-column>-->
        <el-table-column
          prop="status_name"
          label="状态"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag
              size="small"
              v-if="scope.row.status == '1'"
              v-text="scope.row.status_name"
            ></el-tag>
            <el-tag
              size="small"
              type="success"
              v-else-if="scope.row.status == '2'"
              v-text="scope.row.status_name"
            ></el-tag>
            <el-tag
              size="small"
              type="warning"
              v-else-if="scope.row.status == '3'"
              v-text="scope.row.status_name"
            ></el-tag>
            <el-tag
              size="small"
              type="danger"
              v-else-if="scope.row.status == '4'"
              v-text="scope.row.status_name"
            ></el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="car_type" v-if="liHide"> </el-table-column>
        <el-table-column prop="create_time" v-if="liHide"></el-table-column>
        <el-table-column prop="source_type" v-if="liHide"></el-table-column>
        <el-table-column prop="car_id" v-if="liHide"></el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="closeBtn(scope.$index, scope.row)"  v-permission="'road_order_details'"
              >详情
            </el-button>
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDelete(scope.$index, scope.row)"
                    class="red"
                    v-permission="'road_order_del'"
            >删除
            </el-button>
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

    <!-- 合并订单信息 -->
    <el-dialog title="查看合并订单" v-model="mergeVisible" width="70%">
      <el-table
        :data="mergeData"
        border
        class="table"
        ref="multipleTable"
        height="260"
        highlight-current-row
        @current-change="handleCurrentChange"
        header-cell-class-name="table-header"
      >
        <el-table-column type="index" label="序号" width="55" align="center">
        </el-table-column>
        <el-table-column
          prop="orderNo"
          label="订单号"
          align="center"
          width="190"
        ></el-table-column>
        <el-table-column
          prop="roadName"
          label="路内"
          align="center"
          minWidth="180"
        ></el-table-column>
        <el-table-column
          prop="berth"
          label="泊位编号"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column prop="carNo" label="车牌号" width="120" align="center">
          <template #default="scope">
            <el-tag
              size="small"
              v-if="scope.row.car_type == '1'"
              v-text="scope.row.carNo"
            ></el-tag>
            <el-tag
              size="small"
              type="success"
              v-else-if="scope.row.car_type == '2'"
              v-text="scope.row.carNo"
            ></el-tag>
            <el-tag
              size="small"
              type="warning"
              v-else-if="scope.row.car_type == '3'"
              v-text="scope.row.carNo"
            ></el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="driveinTime"
          label="停入时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="driveoutTime"
          label="离场时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="resitime"
          width="100"
          label="停留时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="sum_amount"
          label="停车费用"
          width="90"
          align="center"
        ></el-table-column>
        <!-- <el-table-column
          prop="is_merge"
          label="是否为合并订单"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag size="small" type="success" v-if="scope.row.is_merge == '1'"
              >是</el-tag
            >
            <el-tag size="small" type="danger" v-else>否</el-tag>
          </template>
        </el-table-column> -->
        <!-- <el-table-column
          prop="status_name"
          label="状态"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-tag size="small" v-if="scope.row.status == '1'">在停</el-tag>
            <el-tag
              size="small"
              type="success"
              v-else-if="scope.row.status == '2'"
              >待支付</el-tag
            >
            <el-tag
              size="small"
              type="warning"
              v-else-if="scope.row.status == '3'"
              >已支付</el-tag
            >
            <el-tag
              size="small"
              type="danger"
              v-else-if="scope.row.status == '4'"
              >已完成</el-tag
            >
          </template>
        </el-table-column> -->
      </el-table>
      <div v-show="orderDetail" class="mgt20">
        <p class="order-title"><i class="el-icon-s-grid"></i>详情</p>
        <table class="desctable mgb20 w">
          <tr>
            <td class="tit" width="60">订单号</td>
            <td>{{ mergeDetail.order_no }}</td>
            <td class="tit" width="80">订单状态</td>
            <td>
              <el-tag type="success">{{ mergeDetail.statusName }}</el-tag>
            </td>
            <td class="tit" width="60">停留时间</td>
            <td>{{ mergeDetail.resitime }}</td>
            <td class="tit" width="80">费用</td>
            <td>
              <el-tag type="danger">{{ mergeDetail.sum_amount }}</el-tag>
            </td>
          </tr>
          <tr>
            <td class="tit" width="60">停车地点</td>
            <td>{{ mergeDetail.road_name }}</td>
            <td class="tit" width="60">泊位号</td>
            <td>{{ mergeDetail.berth }}</td>
            <td class="tit" width="60">车牌号码</td>
            <td>{{ mergeDetail.car_no }}</td>
            <td class="tit" width="60">车牌类型</td>
            <td>{{ mergeDetail.carType }}</td>
          </tr>

          <tr>
            <td class="tit" width="80">车主手机号</td>
            <td>{{ mergeDetail.phone }}</td>
            <td class="tit" width="60">订单来源</td>
            <td v-if="mergeDetail.source == '1'">视频采集</td>
            <td v-else>人工</td>
            <td class="tit" width="80">创建时间</td>
            <td>{{ mergeDetail.create_time }}</td>
            <td class="tit" width="80">免单类型</td>
            <td>{{ mergeDetail.free_type }}</td>
          </tr>
          <tr>
            <td class="tit" width="60">进场时间</td>
            <td>{{ mergeDetail.drivein_time }}</td>
            <td class="tit" width="80">进场照片</td>
            <td>
              <template v-for="(item, i) in mergeDetail.scList" :key="i">
                <el-image
                  style="width: 100px; height: 100px"
                  hide-on-click-modal="true" preview-teleported="true"
                  :src="imgurl(item.file_url)"
                  class="ml5"
                  :preview-src-list="[imgurl(item.file_url)]"
                >
                </el-image>
              </template>
            </td>
            <td class="tit" width="60">离场时间</td>
            <td>{{ mergeDetail.driveout_time }}</td>
            <td class="tit" width="80">离场照片</td>
            <td>
              <template v-for="(item, i) in mergeDetail.slList" :key="i">
                <el-image
                  style="width: 100px; height: 100px"
                  hide-on-click-modal="true" preview-teleported="true"
                  :src="imgurl(item.file_url)"
                  class="ml5"
                  :preview-src-list="[imgurl(item.file_url)]"
                >
                </el-image>
              </template>
            </td>
          </tr>
        </table>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="mergeVisible = false">取 消</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 详情 -->
    <el-dialog title="查看详情" v-model="editVisible" width="40%">
      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="基本信息" name="first">
          <div class="mt20"></div>
          <table class="desctable mgb20 w">
            <tr>
              <td class="tit" width="60">订单号</td>
              <td>{{ parkDetails.order_no }}</td>
              <td class="tit" width="80">订单状态</td>
              <td>
                <el-tag type="success">{{ parkDetails.statusName }}</el-tag>
              </td>
            </tr>
            <tr>
              <td class="tit" width="60">停留时间</td>
              <td>{{ parkDetails.resitime }}</td>
              <td class="tit" width="80">免单类型</td>
              <td colspan="3">{{ parkDetails.free_type }}</td>
            </tr>
            <tr>
              <td class="tit" width="60">费用</td>
              <td>
                <el-tag type="danger">{{
                  parkDetails.sum_amount ? parkDetails.sum_amount : "0"
                }}</el-tag>
              </td>
              <td class="tit" width="80">优惠金额</td>
              <td>
                <el-tag type="danger">{{
                  parkDetails.discount_money ? parkDetails.discount_money : "0"
                }}</el-tag>
              </td>
            </tr>
            <tr>
              <td class="tit" width="60">停车地点</td>
              <td>{{ parkDetails.road_name }}</td>
              <td class="tit" width="80">泊位号</td>
              <td>{{ parkDetails.berth }}</td>
            </tr>
            <tr v-if="parkDetails.status == 1 || parkDetails.status == 2">
              <td class="tit" width="60">车牌号码</td>
              <td colspan="3">
                <div v-show="isShowInfo">
                  <span>{{ parkDetails.car_no }}</span>
                  <el-button
                    size="small"
                    class="ml5"
                    type="primary"
                    icon="el-icon-edit"
                    @click="carUpdDate"
                    >修改车牌号码</el-button
                  >
                </div>
                <div v-show="isShow">
                  <el-input
                    v-model="parkDetails.car_no"
                    class="w100"
                    size="small"
                  ></el-input>
                  <el-select
                    v-model="parkDetails.car_type"
                    filterable
                    size="small"
                    placeholder="车牌分类"
                    class="w100 ml5"
                  >
                    <el-option
                      v-for="item in carType"
                      :key="item.dc_value"
                      :label="item.label"
                      :value="item.dc_value"
                    ></el-option>
                  </el-select>
                  <el-button
                    size="small"
                    class="ml5"
                    type="primary"
                    icon="el-icon-check"
                    @click="changeisShowInfo(1)"
                    >确定
                  </el-button>
                  <el-button
                    size="small"
                    icon="el-icon-close"
                    @click="changeisShowInfo(2)"
                    >取消</el-button
                  >
                </div>
              </td>
            </tr>
            <tr v-if="(parkDetails.status == 1 || parkDetails.status == 2) && parkDetails.car_no2 != null && parkDetails.car_no != parkDetails.car_no2 ">
              <td class="tit" width="60">车牌号码二次识别</td>
              <td colspan="3">
                <div v-show="isShowInfo">
                  <span>{{ parkDetails.car_no2 }}</span>
                  <el-button
                    size="small"
                    class="ml5"
                    type="primary"
                    icon="el-icon-check"
                    @click="carUpdDate2(parkDetails.id,parkDetails.order_no,parkDetails.car_no2)"
                    >选用此车牌号码</el-button
                  >
                </div>
              </td>
            </tr>
            <tr>
              <td class="tit" width="60">车牌类型</td>
              <td>{{ parkDetails.carType }}</td>
              <td class="tit" width="80">车主手机号</td>
              <td>{{ parkDetails.phone }}</td>
            </tr>
            <tr>
              <td class="tit" width="60">订单来源</td>
              <td v-if="parkDetails.source == '1'">视频采集</td>
              <td v-else>人工</td>
              <td class="tit" width="80">创建时间</td>
              <td>{{ parkDetails.create_time }}</td>
            </tr>
            <tr>
              <td class="tit" width="60">免单类型</td>
              <td colspan="3">{{ parkDetails.free_type }}</td>
            </tr>
          </table>
        </el-tab-pane>
        <el-tab-pane label="进出场信息" name="second">
          <div class="mt20"></div>
          <table class="desctable mgb20 w">
            <tr>
              <td class="tit" width="60">进场时间</td>
              <td>{{ parkDetails.drivein_time }}</td>
              <td class="tit" width="80">离场时间</td>
              <td>{{ parkDetails.driveout_time }}</td>
            </tr>
            <tr
              v-if="
                parkDetails.scList != undefined && parkDetails.scList.length > 0
              "
            >
              <td class="tit" width="60">进场照片</td>
              <td colspan="3">
                <template v-for="(item, i) in parkDetails.scList" :key="i">
                  <el-image
                    style="width: 100px; height: 100px"
                    hide-on-click-modal="true" preview-teleported="true"
                    :src="imgurl(item.file_url)"
                    class="ml5"
                    :preview-src-list="[imgurl(item.file_url)]"
                  >
                  </el-image>
                </template>
              </td>
            </tr>
            <tr
              v-if="
                parkDetails.slList != undefined && parkDetails.slList.length > 0
              "
            >
              <td class="tit" width="60">离场照片</td>
              <td colspan="3">
                <template v-for="(item, i) in parkDetails.slList" :key="i">
                  <el-image
                    style="width: 100px; height: 100px"
                    hide-on-click-modal="true" preview-teleported="true"
                    :src="imgurl(item.file_url)"
                    class="ml5"
                    :preview-src-list="[imgurl(item.file_url)]"
                  >
                  </el-image>
                </template>
              </td>
            </tr>
          </table>
        </el-tab-pane>
      </el-tabs>
      <el-form v-show="stopTime">
        <el-form-item label="结束计时时间">
          <el-date-picker
            v-model="timeData.time1"
            type="datetime"
            @change="getQueryDate1"
            placeholder="选择日期时间"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="endTimeMethod" v-show="surevb"
            >确 定</el-button
          >
          <el-button
            type="danger"
            @click="endTimeTK"
            v-if="
              (form.driveout_time == null ||
                form.driveout_time == undefined ||
                form.driveout_time == '') &&
              parkDetails.status == 1
            "
            v-show="!surevb"
            >结束计时</el-button
          >
        </span>
      </template>
    </el-dialog>

    <el-dialog title="特殊开闸" v-model="specialVisible" width="80%">
      <div class="handle-box">
        <div class="left-panel">
          <el-form inline label-width="80" size="small">
            <el-form-item label="订单号">
              <el-input
                size="small"
                v-model="parkOrderOpengate.order_no"
                placeholder="订单号"
                class="handle-input mr10"
              ></el-input>
            </el-form-item>
            <el-form-item label="车牌号">
              <el-input
                size="small"
                v-model="parkOrderOpengate.car_no"
                placeholder="车牌号"
                class="handle-input mr10"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                size="small"
                type="primary"
                icon="el-icon-search"
                @click="handleSearch1"
                >查询</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div class="clear"></div>
      </div>
      <el-table
        :data="todoList"
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
        <el-table-column prop="name" label="订单号" align="center">
          <template #default="scope">
            <div class="todo-item">
              {{ scope.row.order_no }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="addtimes"
          label="车牌号"
          align="center"
          width="180"
        >
          <template #default="scope">
            <div class="todo-item">
              {{ scope.row.car_no }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="addtimes"
          label="驶入时间"
          align="center"
          width="180"
        >
          <template #default="scope">
            <div class="todo-item">
              {{ scope.row.drivein_time }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="addtimes"
          label="驶出时间"
          align="center"
          width="180"
        >
          <template #default="scope">
            <div class="todo-item">
              {{ scope.row.driveout_time }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="logintimes" label="停车时长" align="center">
          <template #default="scope">
            <div class="todo-item">
              {{ scope.row.resitime }}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="logintimes" label="特殊开闸人" align="center">
          <template #default="scope">
            <div class="todo-item">
              {{ scope.row.inspect_name }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="logintimes" label="开闸备注" align="center">
          <template #default="scope">
            <div class="todo-item">
              {{ scope.row.memo }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="logintimes" label="拍照" align="center">
          <template #default="scope">
            <el-image
              class="table-td-thumb"
              hide-on-click-modal="true" preview-teleported="true"
              :src="imgurl(scope.row.file_url)"
              :preview-src-list="[imgurl(scope.row.file_url)]"
            >
            </el-image>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="parkOrderOpengate.pageIndex"
          :page-size="parkOrderOpengate.pageSize"
          :total="pageTotal1"
          @current-change="handlePageChange1"
        ></el-pagination>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="specialVisible = false">取 消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import Ueditor from "../../components/UE.vue";
import {
  dictData,
  isModel,
  orderOpengateList,
  queryAreaData,
  queryRoadData,
  queryStreetData,
} from "../../api/index.js";
import {
  roadParkingOrderCarData,
  roadParkingOrderClosureData,
  roadParkingOrderDetailsData,
  roadParkingOrderListData,
  mergeOrder,
  exportRoadData,
  roadParkOrderDel,
  updCarNo,
} from "../../api/roadParkingOrder.js";

export default {
  name: "parkingOrder",
  components: {
    Ueditor,
  },
  data() {
    return {
      liHide: false,
      isShow: false,
      isShowInfo: true,
      stopTime: false,
      value1: "",
      tableH: 0,
      orderDetail: false,
    };
  },
  setup() {
    const query = reactive({
      phone: "",
      status: "",
      orderNo: "",
      berth: "",
      carNo: "",
      startTime: "",
      endTime: "",
      checked: false,
      checked2: false,
      pageIndex: 1,
      pageSize: 15,
      type: "",
      area_id: "",
      street_id: "",
      placeId: "",
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    const pageTotal1 = ref(0);
    const todoList = ref([]);
    const orderDetail = ref(false);
    const specialVisible = ref(false);
    const mergeDetail = ref({});
    let formqjl = reactive({
      time: "",
      areaId: "",
      streetId: "",
      roadId: "",
      areas: [],
      streets: [],
      roads: [],
    });
    // 获取表格数据
    const getData = () => {
      roadParkingOrderListData(query).then((res) => {
        console.log(res.data);
        tableData.value = res.data.list;
        pageTotal.value = res.data.total;
      });
    };

    getData();

    let result = reactive({
      model: "",
      parkingOrderOpengateList: [],
    });

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
      query.placeId = "";
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
      query.placeId = "";
      queryRoadData(queryRoad).then((res) => {
        formqjl.roads = res.data;
      });
    };
    const rowclick = (row) => {
      var carNo = row.carNo;
      query.carNo = carNo;
      getData();
    };
    // 特殊开闸记录
    const parkOrderOpengate = reactive({
      order_no: "",
      car_no: "",
      pageIndex: 1,
      pageSize: 10,
    });

    const getOpengate = () => {
      orderOpengateList(parkOrderOpengate).then((res) => {
        todoList.value = res.data.list;
        pageTotal1.value = res.data.total;
      });
    };

    const getModel = () => {
      isModel().then((res) => {
        result.model = res.data;
      });
    };
    getModel();

    const handleSpecial = (index, row) => {
      getOpengate();
      specialVisible.value = true;
    };

    let timeData = ref({
      time: "",
      time1: "",
    });
    const getQueryDate = () => {
      if (
        null == timeData.value.time ||
        [] == timeData.value.time ||
        "" == timeData.value.time
      ) {
        query.startTime = "";
        query.endTime = "";
      } else {
        query.startTime = dateFormat(timeData.value.time[0]);
        query.endTime = dateFormat(timeData.value.time[1]);
      }
    };
    //日期控件change方法
    const getQueryDate1 = () => {
      if (
        null == timeData.value.time1 ||
        [] == timeData.value.time1 ||
        "" == timeData.value.time1
      ) {
        form.driveout_time = "";
      } else {
        form.driveout_time = dateFormat(timeData.value.time1);
      }
    };
    //日期格式化
    const dateFormat = (time) => {
      return `${time.getFullYear()}-${
        time.getMonth() + 1 >= 10
          ? time.getMonth() + 1
          : "0" + (time.getMonth() + 1)
      }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()} ${
        time.getHours() >= 10 ? time.getHours() : "0" + time.getHours()
      }:${
        time.getMinutes() >= 10 ? time.getMinutes() : "0" + time.getMinutes()
      }:${
        time.getSeconds() >= 10 ? time.getSeconds() : "0" + time.getSeconds()
      }`;
    };

    // 查询操作
    const handleSearch = () => {
      query.pageIndex = 1;
      getData();
    };
    const handleSearch1 = () => {
      parkOrderOpengate.pageIndex = 1;
      getOpengate();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageIndex = val;
      getData();
    };

    const handlePageChange1 = (val) => {
      parkOrderOpengate.pageIndex = val;
      getOpengate();
    };

    // 详情
    const editVisible = ref(false);
    const parkDetails = ref({});
    let form = reactive({
      id: "",
      car_id: "",
      order_no: "",
      car_type: "",
      road_name: "",
      berth: "",
      car_no: "",
      drivein_time: "",
      driveout_time: "",
      resitime: "",
      sum_amount: "",
      state_name: "",
      checked: false,
      startTime: "",
      endTime: "",
      source_type: "",
      company_name: "",
      create_time: "",
    });
    //详情获取
    const handleEdit = (index, row) => {
      roadParkingOrderDetailsData(reactive({ id: row.id })).then((res) => {
        parkDetails.value = res.data;
      });
      editVisible.value = true;
    };

    // 删除操作
    const handleDelete = (index, row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      }).then(() => {
        let edit_resource = reactive({
          is_del: "1",
          orderNo: row.orderNo,
          id: row.id
        });
        roadParkOrderDel(edit_resource).then((res) => {
          console.log(res);
          ElMessage.success("删除成功");
        }).then(() => {
          getData();
        });
      }).catch(() => {
      });
    };

    const carType = ref([]);
    //字典列表
    dictData(reactive({ dict_type: "car_type" })).then((res) => {
      carType.value = res.data;
    });

    const mergeVisible = ref(false);
    const mergeData = ref([]);
    const getMergeOrder = (index, row) => {
      mergeOrder(reactive({ orderId: row.id })).then((res) => {
        mergeData.value = res.data;
      });
      mergeVisible.value = true;
      orderDetail.value = false;
    };

    // 选择行
    const handleCurrentChange = (val) => {
      if (val) {
        roadParkingOrderDetailsData(
          reactive({ id: val.parking_order_id })
        ).then((res) => {
          mergeDetail.value = res.data;
        });
        // currentRow = val;
        orderDetail.value = true;
      }
    };
    return {
      query,
      carType,
      tableData,
      pageTotal,
      pageTotal1,
      parkOrderOpengate,
      editVisible,
      mergeVisible,
      mergeData,
      form,
      todoList,
      specialVisible,
      parkDetails,
      result,
      timeData,
      mergeDetail,
      formqjl,
      getArea,
      getStreet,
      getRoad,
      getData,
      rowclick,
      getQueryDate1,
      getQueryDate,
      handleEdit,
      handleDelete,
      handleSearch,
      handleSearch1,
      handleSpecial,
      handlePageChange,
      handlePageChange1,
      getMergeOrder,
      handleCurrentChange,
      multipleSelection: [],
      dialogImageUrl: "",
      ppVisible: false,
      isActive: false,
      activeName: "first",
      orderDetail,
      url: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
      srcList: [
        "https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg",
        "https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg",
      ],
    };
  },
  methods: {
    //上传图片操作
    handleRemove(file, fileList) {},
    //图片预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.ppVisible = true;
    },
    handleSelectionChange(data) {
      this.selectedData = data;
    },
    endTimeTK() {
      this.stopTime = !this.stopTime;
      this.surevb = true;
    },
    endTimeMethod() {
      let _this = this;
      let query = reactive({
        id: _this.parkDetails.id,
        driveout_time: this.form.driveout_time,
      });
      roadParkingOrderClosureData(query).then((res) => {
        if (res.code == 0) {
          ElMessage.success(res.msg);
          _this.editVisible = false;
          _this.getData();
        } else {
          ElMessage.success(res.msg);
        }
      });
    },
    //修改车牌(车牌id,新车牌号，车牌类型(下拉选项))
    carUpdDate2(id,orderNo,carNo) {
      ElMessageBox.confirm("确定要选用此车牌号码吗？", "提示", {
        type: "warning",
      }).then(() => {
        let edit_resource = reactive({
          orderNo: orderNo,
          carNo: carNo,
          id: id
        });
        updCarNo(edit_resource).then((res) => {
          console.log(res);
          ElMessage.success("选用成功");
        }).then(() => {
          this.editVisible = false;
          this.getData();
        });
      }).catch(() => {
      });
    },
    //图片回显
    imgurl: function (url) {
      if (url != "" && url != null) {
        return url;
      }
    },
    // 修改车牌确定取消
    changeisShowInfo(val) {
      let _this = this;
      console.log("修改车牌确定取消:", _this.parkDetails);
      this.isShow = !this.isShow; //取反
      this.isShowInfo = !this.isShowInfo; //取反
      if (val == 1) {
        roadParkingOrderCarData(
          reactive({
            id: _this.parkDetails.id,
            // car_id: _this.parkDetails.car_id,
            car_id: _this.parkDetails.carno_id,
            car_no: _this.parkDetails.car_no,
            car_type: _this.parkDetails.car_type,
          })
        ).then((res) => {
          if (res.code == 0) {
            ElMessage.success(res.msg);
            _this.editVisible = false;
            _this.getData();
          } else {
            ElMessage.error(res.msg);
          }
        });
      }
    },
    closeBtn(index, row) {
      var that = this;
      that.stopTime = false;
      (that.liHide = false),
        (that.isShow = false),
        (that.isShowInfo = true),
        (that.timeData.time1 = ""),
        (that.surevb = false);
      that.handleEdit(index, row);
    },
    exportRoadPark() {
      ElMessage.success("正在下载中·····");
      this.query.type = "0";
      exportRoadData(this.query).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "路内订单.xls");
        document.body.appendChild(link);
        link.click();
      });
    },
  },
  created() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    this.tableH = h - 308 + "px";
  },
};
</script>

<style scoped>
.mgt20 {
  margin-top: 20px;
}
.el-image {
  margin: 4px 4px 0 4px;
}
.order-title {
  margin: 8px 0;
  font-size: 14px;
  color: #5e6d82;
  line-height: 1.5em;
}
.order-title i {
  color: #919cac;
  font-size: 16px;
  vertical-align: middle;
  margin-right: 2px;
}
</style>
