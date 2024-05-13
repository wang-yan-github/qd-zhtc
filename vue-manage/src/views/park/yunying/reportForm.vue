<template>
  <div>
    <div class="container">
      <el-tabs v-model="activeTab" @tab-click="handleTab">
        <el-tab-pane label="营收总览" name="yszlSor">
          <el-row class="mt20">
            <el-col :span="24">
              <div class="top-panel">
                <el-form inline size="small">
                  <el-form-item label="停车场">
                    <el-select
                      v-model="yszlQuery.variance"
                      filterable
                      size="small"
                      placeholder="所有停车场"
                      class="w100"
                    >
                      <el-option value="">全部</el-option>
                      <el-option
                        v-for="(item, i) in result.park_list"
                        :key="i"
                        :label="item.park_name"
                        :value="item.id"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="时间">
                    <el-date-picker
                      size="small"
                      v-model="yszlQuery.yszlTime"
                      type="daterange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      class="datepicker"
                      @change="getYszlQueryDate"
                    >
                    </el-date-picker>
                  </el-form-item>
                  <el-form-item>
                    <el-button
                      size="small"
                      type="primary"
                      icon="el-icon-search"
                      @click="getSummaryOfRevenue"
                      >查询
                    </el-button>
                  </el-form-item>
                  <el-form-item>
                    <el-button
                      size="small"
                      type="success"
                      icon="el-icon-upload2"
                      @click="downSORExcel"
                      >导出
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-col>
          </el-row>
          <el-table style="width: 100%" :data="yszlList">
            <el-table-column type="index" label="序号" align="center">
              <template #default="scope">
                {{ scope.$index + 1}}
              </template>
            </el-table-column>
            <el-table-column
              prop="parkName"
              label="停车场"
              align="center"
            ></el-table-column>
            <el-table-column prop="" label="月租车卡" align="center">
              <el-table-column
                prop="yzCarNum"
                label="车辆"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="yzTotalCost"
                label="营收"
                align="center"
              ></el-table-column>
            </el-table-column>
            <el-table-column label="流动车" align="center">
              <!--<el-table-column-->
                <!--prop="ldCarNum"-->
                <!--label="车次"-->
                <!--align="center"-->
              <!--&gt;</el-table-column>-->
              <el-table-column label="支付方式" align="center">
                <el-table-column
                  prop="ldWxAmount"
                  label="微信"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="ldZfbAmount"
                  label="支付宝"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="ldDsfAmount"
                  label="聚合支付"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="ldXjAmount"
                  label="现金支付"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="ldQbAmount"
                  label="钱包支付"
                  align="center"
                ></el-table-column>
              </el-table-column>
              <el-table-column
                prop="ldSumPaidAmount"
                label="营收"
                align="center"
              ></el-table-column>
            </el-table-column>
            <el-table-column prop="" label="人工抬杆" align="center">
              <el-table-column prop="" label="公务" align="center">
                <el-table-column
                  prop="ldGwOrderCount"
                  label="单数"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="ldGwAmount"
                  label="费用"
                  align="center"
                ></el-table-column>
              </el-table-column>
              <el-table-column prop="" label="故障" align="center">
                <el-table-column
                  prop="ldGzOrderCount"
                  label="单数"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="ldGzAmount"
                  label="费用"
                  align="center"
                ></el-table-column>
              </el-table-column>
            </el-table-column>
            <el-table-column prop="" label="欠费" align="center">
              <el-table-column
                prop="tdCounts"
                label="单数"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="tdSumAmount"
                label="费用"
                align="center"
              ></el-table-column>
            </el-table-column>
            <el-table-column prop="" label="免费车/辆" align="center">
              <el-table-column
                prop="mfNbCount"
                label="内部车"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="mfQyCount"
                label="企业（税免）车"
                align="center"
              ></el-table-column>
            </el-table-column>
            <el-table-column label="总计" align="center">
              <!--<el-table-column-->
                <!--prop="zjCarNum"-->
                <!--label="车次"-->
                <!--align="center"-->
              <!--&gt;</el-table-column>-->
              <el-table-column
                prop="zjAmount"
                label="营收"
                align="center"
              ></el-table-column>
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="yszlQuery.pageNum"
              :page-size="yszlQuery.pageSize"
              :total="yszlTotal"
              @current-change="handlePageChangeyszl"
            ></el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="月租车明细" name="yzcmxDetail">
          <div class="handle-box mt20">
            <div class="left-panel">
              <el-form inline size="small">
                <el-form-item label="停车场">
                  <el-select
                    v-model="yszlQuery.variance"
                    filterable
                    size="small"
                    placeholder="所有停车场"
                    class="w100"
                  >
                    <el-option value="">全部</el-option>
                    <el-option
                      v-for="(item, i) in result.park_list"
                      :key="i"
                      :label="item.park_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="时间">
                  <el-date-picker
                    size="small"
                    v-model="yszlQuery.yszlTime"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    class="datepicker"
                    @change="getYszlQueryDate"
                  >
                  </el-date-picker>
                </el-form-item>

                <el-form-item>
                  <el-button
                    size="small"
                    type="primary"
                    icon="el-icon-search"
                    @click="getMCRDQuery"
                    >查询</el-button
                  >
                </el-form-item>
                <el-form-item>
                  <el-button
                    size="small"
                    type="success"
                    icon="el-icon-upload2"
                    @click="downMCRExcel"
                    >导出</el-button
                  >
                </el-form-item>
              </el-form>
            </div>
            <div class="right-panel">
              <div class="amount-box">
                <h6>总计</h6>
                <div class="amount-des">
                  <p>
                    车辆：<span>{{ yzcmxStatistics.carCount }}</span
                    >(辆)
                  </p>
                  <p>
                    金额：<span>{{ yzcmxStatistics.sumAmount }}</span
                    >(元)
                  </p>
                </div>
                <div class="clear"></div>
              </div>
            </div>
            <div class="clear"></div>
          </div>

          <el-table :data="yzcmxList" border style="width: 100%">
            <el-table-column type="index" label="序号" align="center"  width="80">
              <template #default="scope">
                {{ scope.$index + 1}}
              </template>
            </el-table-column>
            <el-table-column
              prop="name"
              label="申请人姓名"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="phone"
              label="申请人电话"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="carNo"
              label="车牌号码"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="parkNames"
              label="停车场"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="startTime"
              label="启用日期"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="endTime"
              label="结束日期"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="months"
              label="申请数量"
              align="center"
              width="100"
            ></el-table-column>
            <el-table-column
              prop="price"
              label="产品金额"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="totalCost"
              label="订单金额"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="createTime"
              label="支付时间"
              align="center"
              width="160"
            ></el-table-column>
            <el-table-column
              prop="companyName"
              label="企业名称"
              align="center"
            ></el-table-column>
            <el-table-column
              prop=""
              label="备注"
              align="center"
            ></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="yszlQuery.pageNum"
                    :page-size="yszlQuery.pageSize"
                    :total="yszlTotal"
                    @current-change="handlePageChangeYzcmx"
            ></el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="流动车支付流水" name="flowPay">
          <div class="handle-box mt20">
            <div class="left-panel">
              <el-form inline size="small">
                <el-form-item label="停车场">
                  <el-select
                    v-model="query.park_id"
                    filterable
                    size="small"
                    placeholder="所有停车场"
                    class="w100"
                  >
                    <el-option value="">全部</el-option>
                    <el-option
                      v-for="(item, i) in result.park_list"
                      :key="i"
                      :label="item.park_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="时间">
                  <el-date-picker
                    size="small"
                    v-model="query.time"
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
                    @click="handleSearch"
                    icon="el-icon-search"
                    >查询</el-button
                  >
                </el-form-item>
                <el-form-item>
                  <el-button
                    size="small"
                    type="success"
                    @click="exportOrderExcel"
                    icon="el-icon-upload2"
                    >导出
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="right-panel">
              <div class="amount-box">
                <h6>总计</h6>
                <div class="amount-des">
                  <!--<p>车次：<span v-text="result.orderZj.car_num"></span>(辆)</p>-->
                  <p style="margin-top: 10px;">金额：<span v-text="result.orderZj.car_price"></span>(元)</p>
                </div>
                <div class="clear"></div>
              </div>
            </div>
            <div class="clear"></div>
          </div>
          <div class="small-title">
            <div class="table-title">流动车流水明细（微信）</div>
            <div class="amount-samll-box">
              <b>合</b>
              <!--<p class="mr-20">-->
                <!--车次：<span v-text="result.orderWx.car_num"></span>(辆)-->
              <!--</p>-->
              <p>
                金额：<span
                  v-text="
                    result.orderWx.car_price == null
                      ? 0
                      : result.orderWx.car_price
                  "
                ></span
                >(元)
              </p>
            </div>
            <div class="clear"></div>
          </div>

          <el-table :data="tableData3Wx" border style="width: 100%">
            <el-table-column
              prop="pay_time"
              label="支付时间"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="order_type"
              label="缴费类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="paid_amount"
              label="支付金额"
              align="center"
            ></el-table-column>
            <el-table-column label="支付方式" align="center">
              微信支付
            </el-table-column>
            <el-table-column
              prop="pay_channel"
              label="支付通道"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="pay_terminal"
              label="支付终端"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="pay_scene"
              label="支付场景"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="park_name"
              label="停车场名称"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="payment_serialno"
              label="移动支付编号"
              align="center"
              width="250"
            ></el-table-column>
            <el-table-column
              prop="account_type"
              label="结算账户类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="inspect_name"
              label="收费员"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="shopee"
              label="运营商户"
              align="center"
            ></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="query3Wx.pageIndex"
              :page-size="query3Wx.pageSize"
              :total="pageTotal3Wx"
              @current-change="handlePageChange3Wx"
            ></el-pagination>
          </div>
          <div class="mt20"></div>
          <div class="small-title">
            <div class="table-title">流动车流水明细（支付宝）</div>
            <div class="amount-samll-box">
              <b>合</b>
              <!--<p class="mr-20">-->
                <!--车次：<span v-text="result.orderZfb.car_num"></span>(辆)-->
              <!--</p>-->
              <p>
                金额：<span
                  v-text="
                    result.orderZfb.car_price == null
                      ? 0
                      : result.orderZfb.car_price
                  "
                ></span
                >(元)
              </p>
            </div>
            <div class="clear"></div>
          </div>

          <el-table :data="tableData3Zfb" border style="width: 100%">
            <el-table-column
              prop="pay_time"
              label="支付时间"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="order_type"
              label="缴费类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="paid_amount"
              label="支付金额"
              align="center"
            ></el-table-column>
            <el-table-column label="支付方式" align="center">
              支付宝支付
            </el-table-column>
            <el-table-column
              prop="pay_channel"
              label="支付通道"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="pay_terminal"
              label="支付终端"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="pay_scene"
              label="支付场景"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="park_name"
              label="停车场名称"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="payment_serialno"
              label="移动支付编号"
              align="center"
              width="250"
            ></el-table-column>
            <el-table-column
              prop="account_type"
              label="结算账户类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="inspect_name"
              label="收费员"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="shopee"
              label="运营商户"
              align="center"
            ></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="query3Zfb.pageIndex"
              :page-size="query3Zfb.pageSize"
              :total="pageTotal3Zfb"
              @current-change="handlePageChange3Zfb"
            ></el-pagination>
          </div>
          <div class="mt20"></div>
          <div class="small-title">
            <div class="table-title">流动车流水明细（聚合支付）</div>
            <div class="amount-samll-box">
              <b>合</b>
              <!--<p class="mr-20">-->
                <!--车次：<span v-text="result.orderSf.car_num"></span>(辆)-->
              <!--</p>-->
              <p>
                金额：<span
                  v-text="
                    result.orderSf.car_price == null
                      ? 0
                      : result.orderSf.car_price
                  "
                ></span
                >(元)
              </p>
            </div>
            <div class="clear"></div>
          </div>

          <el-table :data="tableData3Other" border style="width: 100%">
            <el-table-column
              prop="pay_time"
              label="支付时间"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="order_type"
              label="缴费类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="paid_amount"
              label="支付金额"
              align="center"
            ></el-table-column>
            <el-table-column prop="type_name" label="支付方式" align="center">
            </el-table-column>
            <el-table-column
              prop="pay_channel"
              label="支付通道"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="pay_terminal"
              label="支付终端"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="pay_scene"
              label="支付场景"
              align="center"
            ></el-table-column>
            <el-table-column
                    prop="park_name"
                    label="停车场名称"
                    align="center"
            ></el-table-column>
            <el-table-column
              prop="payment_serialno"
              label="移动支付编号"
              align="center"
              width="250"
            ></el-table-column>
            <el-table-column
              prop="account_type"
              label="结算账户类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="inspect_name"
              label="收费员"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="shopee"
              label="运营商户"
              align="center"
            ></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="query3Other.pageIndex"
              :page-size="query3Other.pageSize"
              :total="pageTotal3Other"
              @current-change="handlePageChange3Other"
            ></el-pagination>
          </div>
          <div class="mt20"></div>
          <div class="small-title">
            <div class="table-title">流动车流水明细（现金）</div>
            <div class="amount-samll-box">
              <b>合</b>
              <!--<p class="mr-20">-->
                <!--车次：<span v-text="result.orderXj.car_num"></span>(辆)-->
              <!--</p>-->
              <p>
                金额：<span
                  v-text="
                    result.orderXj.car_price == null
                      ? 0
                      : result.orderXj.car_price
                  "
                ></span
                >(元)
              </p>
            </div>
            <div class="clear"></div>
          </div>

          <el-table :data="tableData3Xj" border style="width: 100%">
            <el-table-column
              prop="pay_time"
              label="支付时间"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="order_type"
              label="缴费类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="paid_amount"
              label="支付金额"
              align="center"
            ></el-table-column>
            <el-table-column prop="pay_type" label="支付方式" align="center">
              现金支付
            </el-table-column>
            <el-table-column
              prop="pay_channel"
              label="支付通道"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="pay_terminal"
              label="支付终端"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="pay_scene"
              label="支付场景"
              align="center"
            ></el-table-column>
            <el-table-column
                    prop="park_name"
                    label="停车场名称"
                    align="center"
            ></el-table-column>
            <el-table-column
              prop="payment_serialno"
              label="移动支付编号"
              align="center"
              width="250"
            ></el-table-column>
            <el-table-column
              prop="account_type"
              label="结算账户类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="inspect_name"
              label="收费员"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="shopee"
              label="运营商户"
              align="center"
            ></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="query3Xj.pageIndex"
              :page-size="query3Xj.pageSize"
              :total="pageTotal3Xj"
              @current-change="handlePageChange3Xj"
            ></el-pagination>
          </div>
        </el-tab-pane>

        <el-tab-pane label="人工抬杆、欠费明细" name="openGateDetails">
          <div class="handle-box mt20">
            <div class="left-panel">
              <el-form inline size="small">
                <el-form-item label="停车场">
                  <el-select
                    clearable
                    v-model="query.park_id"
                    filterable
                    size="small"
                    placeholder="所有停车场"
                    class="w100"
                  >
                    <el-option value="">全部</el-option>
                    <el-option
                      v-for="(item, i) in result.park_list"
                      :key="i"
                      :label="item.park_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="时间">
                  <el-date-picker
                    size="small"
                    v-model="query.time"
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
                    @click="handleSearch4"
                    >查询</el-button
                  >
                </el-form-item>
                <el-form-item>
                  <el-button
                    size="small"
                    type="success"
                    icon="el-icon-upload2"
                    @click="exportExcelTG"
                    >导出
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="right-panel">
              <div class="amount-box">
                <h6>合计</h6>
                <div class="amount-des">
                  <p>
                    车辆：<span>{{ Data4.cheliang }}</span
                    >(辆)
                  </p>
                  <p>
                    欠费金额：<span>{{ Data4.jine }}</span
                    >(元)
                  </p>
                </div>
                <div class="clear"></div>
              </div>
            </div>
            <div class="clear"></div>
          </div>

          <el-table :data="tableData4MX" border style="width: 100%">
            <el-table-column
              prop="car_no"
              label="车牌号码"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="drivein_time"
              label="停车开始时间"
              align="center"
              width="160"
            >
              <template #default="scope">
                {{ dateFormat2(scope.row.drivein_time) }}
              </template>
            </el-table-column>
            <el-table-column
              prop="driveout_time"
              label="停车结束时间"
              align="center"
              width="160"
            >
              <template #default="scope">
                {{ dateFormat2(scope.row.driveout_time) }}
              </template>
            </el-table-column>
            <el-table-column
              prop="artificial_open"
              label="业务类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="resitime"
              label="停车时长"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="sum_amount"
              label="总计金额"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="discount_amount"
              label="优惠金额"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="paid_amount"
              label="已付金额"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="unpaid_amount"
              label="待付金额"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="status"
              label="结算状态"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="memo"
              label="登记记录"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="memo"
              label="备注"
              align="center"
            ></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="query4.pageIndex"
              :page-size="query4.pageSize"
              :total="pageTotal4"
              @current-change="handlePageChange4"
            ></el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="内部车、免费车明细" name="detailFree">
          <div class="handle-box mt20">
            <div class="left-panel">
              <el-form inline size="small">
                <el-form-item label="停车场">
                  <el-select
                    v-model="query.park_id"
                    filterable
                    size="small"
                    placeholder="所有停车场"
                    class="w100"
                  >
                    <el-option value="">全部</el-option>
                    <el-option
                      v-for="(item, i) in result.park_list"
                      :key="i"
                      :label="item.park_name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="驶入时间">
                  <el-date-picker
                    size="small"
                    v-model="query.time"
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
                    @click="handleSearchNm"
                    icon="el-icon-search"
                    >查询</el-button
                  >
                </el-form-item>
                <el-form-item>
                  <el-button
                    size="small"
                    type="success"
                    @click="exportOrderExcelNm"
                    icon="el-icon-upload2"
                    >导出
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <div class="clear"></div>
          </div>
          <div class="small-title">
            <div class="table-title">内部车明细</div>
            <div class="right-panel">
              <div class="amount-box">
                <h6>总计</h6>
                <div class="amount-des">
                  <p>
                    车辆：<span>{{ nmStatistics.nbc.carCount }}</span
                    >(辆)
                  </p>
                  <p>
                    金额：<span>{{ nmStatistics.nbc.sumAmount }}</span
                    >(元)
                  </p>
                </div>
                <div class="clear"></div>
              </div>
            </div>

            <div class="clear"></div>
          </div>

          <el-table :data="tableDataNb" border style="width: 100%">
            <el-table-column
              prop="prType"
              label="业务类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="prName"
              label="停车场/路内"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="carNo"
              label="车牌号码"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="driveinTime"
              label="驶入日期"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="driveoutTime"
              label="驶出日期"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="carOwner"
              label="车主/单位名称"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="phone"
              label="联系电话"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="enterpriseName"
              label="企业/单位名称"
              align="center"
            ></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="queryNb.pageNum"
              :page-size="queryNb.pageSize"
              :total="pageTotalNb"
              @current-change="handlePageChangeNb"
            ></el-pagination>
          </div>
          <div class="mt40"></div>
          <div class="small-title">
            <div class="table-title">企业（税免）车明细</div>
            <div class="right-panel">
              <div class="amount-box">
                <h6>总计</h6>
                <div class="amount-des">
                  <p>
                    车辆：<span>{{ nmStatistics.mfc.carCount }}</span
                    >(辆)
                  </p>
                  <p>
                    金额：<span>{{ nmStatistics.mfc.sumAmount }}</span
                    >(元)
                  </p>
                </div>
                <div class="clear"></div>
              </div>
            </div>
            <div class="clear"></div>
          </div>

          <el-table :data="tableDataMs" border style="width: 100%">
            <el-table-column
              prop="prType"
              label="业务类型"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="prName"
              label="停车场/路内"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="carNo"
              label="车牌号码"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="driveinTime"
              label="开始日期"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="driveoutTime"
              label="结束日期"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="carOwner"
              label="车主/单位名称"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="phone"
              label="联系电话"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="enterpriseName"
              label="企业/单位名称"
              align="center"
            ></el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :current-page="queryMs.pageNum"
              :page-size="queryMs.pageSize"
              :total="pageTotalMs"
              @current-change="handlePageChangeMs"
            ></el-pagination>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  journalData,
  parkingOrderData,
  queryParkData,
  getInnerDetailData,
  getFreeDetailData,
  parkingOrderExcel,
  parkingOrderExcelNm,
  iflqStatistics,
  getOpenGateDetails,
  getOpenGateCarNums,
  getOpenGateOrderArrears,
  parkingOrderExcelTG,
  parkingOrderCount,
  summaryOfRevenueList,
  summaryOfRevenueExcel,
  monthCarRentalDetailList,
  monthCarRentalStatistics,
  monthCarRentalExcel,
} from "../../../api/index";

export default {
  name: "reportForm",
  setup() {
    // 流动车
    const query = reactive({
      name: "",
      time: "",
      park_id: "",
    });

    const result = reactive({
      park_list: [],
      orderWx: {},
      orderZfb: {},
      orderXj: {},
      orderSf: {},
      orderZj: { car_num: 0, car_price: 0 },
    });
    const tableData1 = reactive([{}, {}, {}, {}, {}, {}]);
    const tableData2 = reactive([{}, {}, {}, {}, {}, {}]);
    const tableData3 = reactive([{}, {}, {}, {}, {}, {}]);
    const tableData4 = reactive([{}, {}, {}, {}, {}, {}]);
    const tableData5 = reactive([{}, {}, {}, {}, {}, {}]);
    const tableData6 = reactive([{}, {}, {}, {}, {}, {}]);
    const options = reactive([
      {
        value: "选项1",
        label: "停车场名称1",
      },
      {
        value: "选项2",
        label: "停车场名称2",
      },
      {
        value: "选项3",
        label: "停车场名称3",
      },
      {
        value: "选项4",
        label: "停车场名称4",
      },
      {
        value: "选项5",
        label: "停车场名称5",
      },
    ]);

    //日期格式化

    const getQueryDate = () => {
      if (query.time == null || query.time == "") {
        query3Wx.start_time = "";
        query3Wx.end_time = "";
        query3Zfb.start_time = "";
        query3Zfb.end_time = "";
        query3Other.start_time = "";
        query3Other.end_time = "";
        query3Xj.start_time = "";
        query3Xj.end_time = "";
        query4.startTime = "";
        query4.endTime = "";
        query3ldc.start_time = "";
        query3ldc.end_time = "";
        //
        queryNb.startTime = "";
        queryNb.endTime = "";
        queryMs.startTime = "";
        queryMs.endTime = "";
        queryNm.startTime = "";
        queryNm.endTime = "";
        return false;
      }

      query3Wx.start_time = dateFormat(query.time[0]) + " ";
      query3Wx.end_time = dateFormat(query.time[1]) + " ";
      query3Zfb.start_time = dateFormat(query.time[0]) + " ";
      query3Zfb.end_time = dateFormat(query.time[1]) + " ";
      query3Other.start_time = dateFormat(query.time[0]) + " ";
      query3Other.end_time = dateFormat(query.time[1]) + " ";
      query3Xj.start_time = dateFormat(query.time[0]) + " ";
      query3Xj.end_time = dateFormat(query.time[1]) + " ";
      query4.startTime = dateFormat(query.time[0]) + " ";
      query4.endTime = dateFormat(query.time[1]) + " ";
      query3ldc.start_time = dateFormat(query.time[0]) + " ";
      query3ldc.end_time = dateFormat(query.time[1]) + " ";
      //
      queryNb.startTime = dateFormat(query.time[0]) + " ";
      queryNb.endTime = dateFormat(query.time[1]) + " ";
      queryMs.startTime = dateFormat(query.time[0]) + " ";
      queryMs.endTime = dateFormat(query.time[1]) + " ";
      queryNm.startTime = dateFormat(query.time[0]) + " ";
      queryNm.endTime = dateFormat(query.time[1]) + " ";

      console.log(query3Wx.start_time);
    };

    const dateFormat = (time) => {
      return `${time.getFullYear()}-${
        time.getMonth() + 1 >= 10
          ? time.getMonth() + 1
          : "0" + (time.getMonth() + 1)
      }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
    };
    // 流动车流水订单
    const query3ldc = reactive({
      start_time: "",
      end_time: "",
      park_id: "",
    });

    const handleSearch = () => {
      query3Wx.pageIndex = 1;
      query3Wx.park_id = query.park_id;
      query3Zfb.pageIndex = 1;
      query3Zfb.park_id = query.park_id;
      query3Other.pageIndex = 1;
      query3Other.park_id = query.park_id;
      query3Xj.pageIndex = 1;
      query3Xj.park_id = query.park_id;

      result.orderZj.car_num = 0;
      result.orderZj.car_price = 0.0;

      getData3Wx();
      getData3Zfb();
      getData3Other();
      getData3Xj();
    };
    const getParkList = () => {
      queryParkData(query).then((res) => {
        result.park_list = res.data;
      });
    };

    // 流动车流水订单微信
    const query3Wx = reactive({
      pay_type: "2",
      start_time: "",
      end_time: "",
      park_id: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData3Wx = ref([]);
    const pageTotal3Wx = ref(0);
    const handlePageChange3Wx = (val) => {
      query3Wx.pageIndex = val;
      getData3Wx();
    };
    const getData3Wx = () => {
      parkingOrderData(query3Wx).then((res) => {
        tableData3Wx.value = res.data.list;
        pageTotal3Wx.value = res.data.total;
      });
      parkingOrderCount(query3Wx).then((res) => {
        console.log(res.data);
        result.orderWx = res.data;
        result.orderZj.car_num += parseInt(result.orderWx.car_num);
        result.orderZj.car_price += parseFloat(
          result.orderWx.car_price == null ? 0.0 : result.orderWx.car_price
        );
      });
    };

    // 流动车流水订单支付宝
    const query3Zfb = reactive({
      pay_type: "3",
      start_time: "",
      end_time: "",
      park_id: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData3Zfb = ref([]);
    const pageTotal3Zfb = ref(0);
    const handlePageChange3Zfb = (val) => {
      query3Zfb.pageIndex = val;
      getData3Zfb();
    };
    const getData3Zfb = () => {
      parkingOrderData(query3Zfb).then((res) => {
        tableData3Zfb.value = res.data.list;
        pageTotal3Zfb.value = res.data.total;
      });
      parkingOrderCount(query3Zfb).then((res) => {
        console.log(res.data);
        result.orderZfb = res.data;
        result.orderZj.car_num += parseInt(result.orderZfb.car_num);
        result.orderZj.car_price += parseFloat(
          result.orderZfb.car_price == null ? 0 : result.orderZfb.car_price
        );
      });
    };

    // 流动车流水订单其他
    const query3Other = reactive({
      pay_type: "8",
      start_time: "",
      end_time: "",
      park_id: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData3Other = ref([]);
    const pageTotal3Other = ref(0);
    const handlePageChange3Other = (val) => {
      query3Other.pageIndex = val;
      getData3Other();
    };
    const getData3Other = () => {
      parkingOrderData(query3Other).then((res) => {
        tableData3Other.value = res.data.list;
        pageTotal3Other.value = res.data.total;
      });
      parkingOrderCount(query3Other).then((res) => {
        console.log(res.data);
        result.orderSf = res.data;
        result.orderZj.car_num += parseInt(result.orderSf.car_num);
        result.orderZj.car_price += parseFloat(
          result.orderSf.car_price == null ? 0 : result.orderSf.car_price
        );
      });
    };

    // 流动车流水订单现金
    const query3Xj = reactive({
      pay_type: "5",
      start_time: "",
      end_time: "",
      park_id: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData3Xj = ref([]);
    const pageTotal3Xj = ref(0);
    const handlePageChange3Xj = (val) => {
      query3Xj.pageIndex = val;
      getData3Xj();
    };
    const getData3Xj = () => {
      parkingOrderData(query3Xj).then((res) => {
        tableData3Xj.value = res.data.list;
        pageTotal3Xj.value = res.data.total;
      });
      parkingOrderCount(query3Xj).then((res) => {
        console.log(res.data);
        result.orderXj = res.data;
        result.orderZj.car_num += parseInt(result.orderXj.car_num);
        result.orderZj.car_price += parseFloat(
          result.orderXj.car_price == null ? 0 : result.orderXj.car_price
        );
      });
    };

    const exportOrderExcel = () => {
      // console.log(12233);
      query3ldc.park_id = query.park_id;
      console.log(query3ldc);
      parkingOrderExcel(query3ldc).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "流动车支付流水.xlsx");
        document.body.appendChild(link);
        link.click();
      });
    };

    // ******************** 内部车、免费车明细 ********************
    // ----------- 内部车、免费车明细查询按钮 -----------
    const handleSearchNm = () => {
      queryNb.pageNum = 1;
      queryMs.pageNum = 1;
      getDataNb();
      getDataMs();
      getIFLQStatistics();
    };
    // ----------- 内部车、免费车明细Excel -----------
    const queryNm = reactive({
      startTime: "",
      endTime: "",
      variance: "",
      variance2: "1",
    });
    const exportOrderExcelNm = () => {
      queryNm.variance = query.park_id;
      parkingOrderExcelNm(queryNm).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "内部车、免费车明细.xlsx");
        document.body.appendChild(link);
        link.click();
      });
    };
    // ----------- 内部车、免费车明细统计 -----------
    const nmStatistics = reactive({
      nbc: {},
      mfc: {},
    });
    const getIFLQStatistics = () => {
      queryNm.variance = query.park_id;
      iflqStatistics(queryNm).then((res) => {
        nmStatistics.nbc = res.data.nbc;
        nmStatistics.mfc = res.data.mfc;
      });
    };
    // ----------- 内部车明细 -----------
    const queryNb = reactive({
      variance: "",
      variance2: "1",
      startTime: "",
      endTime: "",
      pageNum: 1,
      pageSize: 10,
    });
    const tableDataNb = ref([]);
    const pageTotalNb = ref(0);
    const handlePageChangeNb = (val) => {
      queryNb.pageNum = val;
      getDataNb();
    };
    const getDataNb = () => {
      queryNb.variance = query.park_id;
      getInnerDetailData(queryNb).then((res) => {
        tableDataNb.value = res.data.list;
        pageTotalNb.value = res.data.total;
      });
    };
    // ----------- 免费免税车明细 -----------
    const queryMs = reactive({
      variance: "",
      variance2: "1",
      startTime: "",
      endTime: "",
      pageNum: 1,
      pageSize: 10,
    });
    const tableDataMs = ref([]);
    const pageTotalMs = ref(0);
    const handlePageChangeMs = (val) => {
      queryMs.pageNum = val;
      getDataMs();
    };
    const getDataMs = () => {
      queryMs.variance = query.park_id;
      getFreeDetailData(queryMs).then((res) => {
        tableDataMs.value = res.data.list;
        pageTotalMs.value = res.data.total;
      });
    };
    // ******************** 内部车、免费车明细 ********************

    const handleSearch4 = () => {
      query4.pageIndex = 1;
      query4.park_id = query.park_id;
      getData4();
      getData4a();
      getData4b();
    };
    // 人工抬杆/车辆明细
    const query4 = reactive({
      startTime: "",
      endTime: "",
      park_id: "",
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData4MX = ref([]);
    const pageTotal4 = ref(0);
    const handlePageChange4 = (val) => {
      query4.pageIndex = val;
      getData4();
      getData4a;
      getData4b;
    };
    const getData4 = () => {
      getOpenGateDetails(query4).then((res) => {
        tableData4MX.value = res.data.list;
        pageTotal4.value = res.data.total;
      });
    };
    let Data4 = reactive({
      cheliang: "",
      jine: "",
    });
    const getData4a = () => {
      getOpenGateCarNums(query4).then((res) => {
        Data4.cheliang = res.data;
      });
    };
    const getData4b = () => {
      getOpenGateOrderArrears(query4).then((res) => {
        Data4.jine = res.data;
      });
    };
    const exportExcelTG = () => {
      // query3ldc.park_id=query4.park_id;
      parkingOrderExcelTG(query4).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "人工抬杆、欠费明细.xls");
        document.body.appendChild(link);
        link.click();
      });
    };

    const yszlQuery = reactive({
      yszlTime: "",
      startTime: "",
      pageNum: 1,
      pageSize: 15,
      endTime: "",
      variance: "",
      variance2: "1",
    });
    const yszlList = ref([]);
    const yszlTotal = ref(0);

    // 分页导航
    const handlePageChangeyszl = (val) => {
      yszlQuery.pageNum = val;
      getRoadSORList();
    };
    // 分页导航
    const handlePageChangeYzcmx = (val) => {
      yszlQuery.pageNum = val;
      getMonthCarRentalDetail();
    };
    // 查询操作
    const handleSearchyszl = () => {
      yszlQuery.pageNum = 1;
      getRoadSORList();
    };
    // 营收总览查询
    const getSummaryOfRevenue = () => {
      summaryOfRevenueList(yszlQuery).then((res) => {
        var data = res.data;
        yszlList.value = data.list;
        yszlTotal.value = data.total;
      });
    };

    // 营收总览Excel
    const downSORExcel = () => {
      summaryOfRevenueExcel(yszlQuery).then((res) => {
        console.log(res.data);
        console.log(document.createElement("a"));
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "停车场营收报表.xlsx");
        document.body.appendChild(link);
        link.click();
      });
    };

    getSummaryOfRevenue();
    getParkList();

    const yzcmxList = ref([]);
    // 月租车明细
    const getMonthCarRentalDetail = () => {
      monthCarRentalDetailList(yszlQuery).then((res) => {
        var data = res.data;
        yzcmxList.value = data.list;
        yszlTotal.value = data.total;
      });
    };
    const yzcmxStatistics = reactive({
      carCount: "",
      sumAmount: "",
    });
    // 月租车统计
    const getMonthCarRentalStatistics = () => {
      monthCarRentalStatistics(yszlQuery).then((res) => {
        var data = res.data;
        yzcmxStatistics.carCount = data.carCount;
        yzcmxStatistics.sumAmount = data.sumAmount;
      });
    };
    const getMCRDQuery = () => {
      getMonthCarRentalDetail();
      getMonthCarRentalStatistics();
    };
    // 月租车明细Excel
    const downMCRExcel = () => {
      monthCarRentalExcel(yszlQuery).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "月租车明细.xlsx");
        document.body.appendChild(link);
        link.click();
      });
    };

    const handleTab = (tab, event) => {
      // 清数据
      query.time = "";
      query.park_id = "";
      yszlQuery.yszlTime = "";
      yszlQuery.startTime = "";
      yszlQuery.endTime = "";
      yszlQuery.variance = "";

      if (tab.props.name == "flowPay") {
        // 流动车支付流水
        result.orderZj.car_num = 0;
        result.orderZj.car_price = 0.0;
        getData3Wx();
        getData3Zfb();
        getData3Other();
        getData3Xj();
        getParkList();
      } else if (tab.props.name == "detailFree") {
        //内部车、免费车明细
        getDataNb();
        getDataMs();
        getIFLQStatistics();
        getParkList();
      } else if (tab.props.name == "openGateDetails") {
        //人工抬杆、欠费明细
        getData4();
        getData4a();
        getData4b();
        getParkList();
      } else if (tab.props.name == "yszlSor") {
        //营收总览
        getSummaryOfRevenue();
        getParkList();
      } else if (tab.props.name == "yzcmxDetail") {
        //月租车明细
        getMonthCarRentalDetail();
        getMonthCarRentalStatistics();
        getParkList();
      }
    };

    //日期控件change方法
    const getYszlQueryDate = () => {
      console.log(dataFormat(yszlQuery.yszlTime[0]));
      if (null != yszlQuery.yszlTime) {
        yszlQuery.startTime = dataFormat(yszlQuery.yszlTime[0]);
        yszlQuery.endTime = dataFormat(yszlQuery.yszlTime[1]);
      } else {
        yszlQuery.startTime = "";
        yszlQuery.endTime = "";
      }
    };
    //日期格式化 yyyy-MM-dd
    const dataFormat = (time) => {
      return `${time.getFullYear()}-${
        time.getMonth() + 1 >= 10
          ? time.getMonth() + 1
          : "0" + (time.getMonth() + 1)
      }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
    };
    //日期格式化 yyyy-MM-dd HH:mm:ss
    const dateFormat2 = (time) => {
      if (time == null || time == "" || time == undefined) {
        return "";
      }
      var date = new Date(time);
      return `${date.getFullYear()}-${
        date.getMonth() + 1 >= 10
          ? date.getMonth() + 1
          : "0" + (date.getMonth() + 1)
      }-${date.getDate() >= 10 ? date.getDate() : "0" + date.getDate()} ${
        date.getHours() >= 10 ? date.getHours() : "0" + date.getHours()
      }:${
        date.getMinutes() >= 10 ? date.getMinutes() : "0" + date.getMinutes()
      }:${
        date.getSeconds() >= 10 ? date.getSeconds() : "0" + date.getSeconds()
      }`;
    };

    return {
      query,
      result,
      getQueryDate,
      getParkList,
      handleSearch,
      handleSearchNm,
      handleSearch4,
      exportOrderExcel,
      exportExcelTG,
      exportOrderExcelNm,
      handlePageChangeyszl,
      handlePageChangeYzcmx,
      handleSearchyszl,
      tableData1,
      tableData2,
      tableData3,
      tableData4,
      tableData5,
      tableData6,
      tableData3Wx,
      pageTotal3Wx,
      handlePageChange3Wx,
      getData3Wx,
      query3Wx,
      tableData3Zfb,
      pageTotal3Zfb,
      handlePageChange3Zfb,
      getData3Zfb,
      query3Zfb,
      tableData3Other,
      pageTotal3Other,
      handlePageChange3Other,
      getData3Other,
      query3Other,
      tableData3Xj,
      pageTotal3Xj,
      pageTotal4,
      Data4,
      handlePageChange3Xj,
      handlePageChange4,
      getData3Xj,
      query3Xj,
      tableDataNb,
      pageTotalNb,
      handlePageChangeNb,
      getDataNb,
      queryNb,
      queryNm,
      query4,
      tableData4MX,
      handleTab,
      tableDataMs,
      pageTotalMs,
      handlePageChangeMs,
      getDataMs,
      queryMs,
      options,

      yszlQuery,
      yszlList,
      yszlTotal,
      getSummaryOfRevenue,
      downSORExcel,
      getYszlQueryDate,

      yzcmxList,
      getMonthCarRentalDetail,
      yzcmxStatistics,
      getMonthCarRentalStatistics,
      getMCRDQuery,
      downMCRExcel,
      getIFLQStatistics,
      nmStatistics,
      dateFormat2,
    };
  },
  methods: {},
  data() {
    return {
      activeTab: "yszlSor",
    };
  },
};
</script>
<style scoped>
.amount-box {
  padding: 0.375rem 1rem;
  background-color: #f2f5fb;
  color: #4d5f77;
}

.amount-box h6 {
  display: block;
  margin-right: 0.625rem;
  float: left;
  width: 2.5rem;
  text-align: center;
  line-height: 2.5rem;
  font-size: 0.85rem;
  background-color: #b5d1ee;
  color: #fff;
  border-radius: 50%;
}

.amount-box .amount-des {
  float: left;
  font-size: 0.875rem;
}

.amount-box p {
  line-height: 1.08rem;
  font-size: 0.875rem;
  font-weight: bold;
  margin-top: 1px;
}

.amount-box p span {
  margin-right: 0.25rem;
  color: #409eff;
  font-size: 1.2rem;
}

.handle-box {
  margin-bottom: 0;
}

.right-panel {
  margin-top: -1.25rem;
}

.table-title {
  float: left;
  font-weight: bold;
  font-size: 16px;
}

.table-title:before {
  content: "";
  display: inline-block;
  width: 3px;
  height: 16px;
  margin-right: 10px;
  background-color: #409eff;
  vertical-align: middle;
}

.amount-samll-box {
  float: right;
}

.amount-samll-box b {
  display: inline-block;
  margin-right: 0.625rem;
  text-align: center;
  width: 1.875rem;
  line-height: 1.875rem;
  font-size: 0.875rem;
  background-color: #b5d1ee;
  color: #fff;
  border-radius: 50%;
}

.amount-samll-box p {
  display: inline-block;
  font-weight: bold;
  font-size: 14px;
  color: #4d5f77;
}

.amount-samll-box p span {
  margin-right: 6px;
  color: #409eff;
  font-size: 18px;
}

.small-title {
  margin: 0.625rem 0;
}

.mr-20 {
  margin-right: 20px;
}

.mt40 {
  margin-top: 2.5rem;
}
</style>
