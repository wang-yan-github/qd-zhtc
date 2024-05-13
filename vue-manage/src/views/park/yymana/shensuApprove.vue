<template>
    <div>
        <div class="container">
            <div class="handle-box f-cb">
                <div class="left-panel">
                    <el-form inline size="small">
                        <el-form-item label="关键词" class="mb0">
                            <el-input
                                    @keyup.enter="handleSearch()"
                                    size="small"
                                    v-model="query.keys"
                                    placeholder="订单号/车牌号"
                                    class="handle-input"
                            ></el-input>
                        </el-form-item>
                        <!-- <el-form-item label="申诉状态" class="mb0">
                          <el-select
                            v-model="query.appeal_status"
                            filterable
                            size="small"
                            placeholder="申诉状态"
                            class="w100"
                          >
                            <el-option value="">全部</el-option>
                            <el-option key="1" label="待处理" value="1"></el-option>
                            <el-option key="2" label="通过" value="2"></el-option>
                            <el-option key="3" label="驳回" value="3"></el-option>
                            <el-option key="4" label="完成" value="4"></el-option>
                          </el-select>
                        </el-form-item> -->

                        <el-form-item label="申诉时间" class="mb0">
                            <el-date-picker
                                    v-model="timeData.time1"
                                    placeholder=""
                                    type="daterange"
                                    @change="getQueryDate1"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                            ></el-date-picker>
                        </el-form-item>
                        <el-form-item class="mb0">
                            <el-button
                                    size="small"
                                    type="primary"
                                    icon="el-icon-search"
                                    @click="handleSearch"
                            >查询
                            </el-button>
                            <el-button size="small" type="success" icon="el-icon-upload2" @click="exportshensu"
                                       v-permission="'park_shensuApproveA_excel'"
                            >导出
                            </el-button>
                        </el-form-item>
                    </el-form>
                </div>
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
                <el-table-column label="序号" width="55" align="center">
                    <template #default="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="appeal_time"
                        label="申诉时间"
                        align="center"
                        width="180"
                        :formatter="formdate"
                ></el-table-column>
                <el-table-column
                        prop="order_no"
                        align="center"
                        label="订单号"
                ></el-table-column>
                <el-table-column
                        prop="car_no"
                        label="车牌号"
                        width="200"
                        align="center"
                >
                    <template #default="scope">
                        <el-tag
                                size="small"
                                v-if="scope.row.car_type == '1'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="success"
                                v-else-if="scope.row.car_type == '2'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="warning"
                                v-else-if="scope.row.car_type == '3'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="resitime"
                        align="center"
                        label="停留时长"
                        width="120"
                ></el-table-column>
                <el-table-column
                        prop="unpaid_amount"
                        align="center"
                        label="停车费用(元)"
                        width="120"
                ></el-table-column>
                <el-table-column
                        prop="appeal_order_status"
                        align="center"
                        label="申诉时订单状态"
                        width="140"
                >
                    <template #default="scope">
                        <el-tag type="success">{{ scope.row.appeal_order_status }}</el-tag>
                    </template>
                </el-table-column>
                <!-- <el-table-column
                  prop="appeal_status"
                  align="center"
                  label="申诉状态"
                  width="80"
                >
                  <template #default="scope">
                    <el-tag>{{ scope.row.appeal_status }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="appeal_status"
                  align="center"
                  label="核实状态"
                  width="80"
                >
                  <template #default="scope">
                    <el-tag v-if="scope.row.is_verify == '1'">是</el-tag>
                    <el-tag v-else>否</el-tag>
                  </template>
                </el-table-column> -->
                <el-table-column label="操作" width="120" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-check"
                                @click="handleEdit(scope.$index, scope.row, false)"
                                v-permission="'park_shensuApproveA_sh'"
                        >审核
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

        <!-- 查看弹出框 -->
        <el-dialog title="查看申诉订单" v-model="editVisible" width="40%">
            <el-tabs type="card" v-model="activeName">
                <el-tab-pane label="基本信息" name="first">
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <tr>
                            <td class="tit" width="60">订单号</td>
                            <td>{{ detailsData.order_no }}</td>
                            <td class="tit" width="80">订单状态</td>
                            <td>
                                <el-tag type="success">{{ detailsData.statusName }}</el-tag>
                            </td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">停留时间</td>
                            <td>{{ detailsData.resitime }}</td>
                            <td class="tit" width="80">费用</td>
                            <td>
                                <el-tag type="danger">{{ detailsData.sum_amount }}</el-tag>
                            </td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">停车地点</td>
                            <td>{{ detailsData.road_name }}</td>
                            <td class="tit" width="80">泊位号</td>
                            <td>{{ detailsData.berth }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">车牌号码</td>
                            <td colspan="3">
                                <el-tag>{{ detailsData.car_no }}</el-tag>
                            </td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">车牌属性</td>
                            <td>{{ detailsData.carType }}</td>
                            <td class="tit" width="80">车主手机号</td>
                            <td>{{ detailsData.phone }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">订单来源</td>
                            <td v-if="detailsData.source == '1'">视频采集</td>
                            <td v-else>人工</td>
                            <td class="tit" width="80">创建时间</td>
                            <td>{{ detailsData.create_time }}</td>
                        </tr>
                    </table>
                </el-tab-pane>
                <el-tab-pane label="进出场信息" name="second">
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <tr>
                            <td class="tit" width="60">进场时间</td>
                            <td width="230">{{ detailsData.drivein_time }}</td>
                            <td class="tit" width="60">离场时间</td>
                            <td>
                                {{
                                detailsData.driveout_time == null
                                ? "暂未离场"
                                : detailsData.driveout_time
                                }}
                            </td>
                        </tr>
                        <tr
                                v-if="
                detailsData.scList != undefined && detailsData.scList.length > 0
              "
                        >
                            <td class="tit" width="60">进场照片</td>
                            <td colspan="3">
                                <template v-for="(item, i) in detailsData.scList" :key="i">
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
                detailsData.slList != undefined && detailsData.slList.length > 0
              "
                        >
                            <td class="tit" width="60">离场照片</td>
                            <td colspan="3">
                                <template v-for="(item, i) in detailsData.slList" :key="i">
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
                <el-tab-pane
                        label="申诉信息"
                        name="third"
                        v-if="
            detailsData.appeals != undefined && detailsData.appeals.length > 0
          "
                >
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <template v-for="(item, i) in detailsData.appeals" :key="i">
                            <tr>
                                <td class="tit" width="60">申诉时间</td>
                                <td>{{ item.appeal_time }}</td>
                                <td class="tit" width="80">申诉状态</td>
                                <td>{{ item.appeal_status }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="60">联系方式</td>
                                <td colspan="3">{{ item.phone }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="60">申诉事件</td>
                                <td colspan="3">{{ item.content }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="60">核实状态</td>
                                <td colspan="3">{{ item.is_verify == "1" ? "是" : "否" }}</td>
                            </tr>
                        </template>
                        <tr v-if="detailsData.records != null">
                            <td class="tit" width="60">处理结果</td>
                            <td>{{ detailsData.records.handle_status }}</td>
                            <td class="tit" width="80">处理类型</td>
                            <td v-if="detailsData.records.handle_type == 1">
                                按结束时间处理
                            </td>
                            <td v-if="detailsData.records.handle_type == 2">
                                按订单费用处理
                            </td>
                            <td v-if="detailsData.records.handle_type == 3">按退款处理</td>
                            <!-- <td  v-if="detailsData.records.handle_type==4">按订单状态处理</td> -->
                            <td v-if="detailsData.records.handle_type == 5">
                                按修正车牌处理
                            </td>
                            <td v-if="detailsData.records.handle_type == null"></td>
                        </tr>
                        <tr v-if="detailsData.records != null">
                            <td class="tit" width="60">处理说明</td>
                            <td colspan="3" v-if="detailsData.records.handle_type == 1">
                                修正后的驶离时间为：{{ detailsData.records.end_time }}
                            </td>
                            <td colspan="3" v-if="detailsData.records.handle_type == 2">
                                修正后的订单费用为：{{ detailsData.records.refund_amount }}.00元
                            </td>
                            <td colspan="3" v-if="detailsData.records.handle_type == 3">
                                退款费用为：{{ detailsData.records.refund_amount }}.00元
                            </td>
                            <td colspan="3" v-if="detailsData.records.handle_type == 5">
                                修正后的车牌为：{{ detailsData.car_no }}, 车牌颜色为：{{
                                detailsData.carType
                                }}
                            </td>
                        </tr>
                        <tr v-if="detailsData.records != null">
                            <td class="tit" width="60">驳回原因</td>
                            <td colspan="3">{{ detailsData.records.reject_reason }}</td>
                        </tr>
                        <tr v-if="detailsData.records != null">
                            <td class="tit" width="60">处理时间</td>
                            <td colspan="3">{{ detailsData.records.create_time }}</td>
                        </tr>
                        <tr
                                v-if="
                detailsData.records != null &&
                detailsData.records.handle_status == 3
              "
                        >
                            <td class="tit" width="80">处理说明</td>
                            <td colspan="3">{{ detailsData.records.reject_reason }}</td>
                        </tr>
                        <tr
                                v-if="
                detailsData.bhpzList != undefined &&
                detailsData.bhpzList.length > 0
              "
                        >
                            <td class="tit" width="60">凭证</td>
                            <td colspan="3">
                                <template v-for="(item, i) in detailsData.bhpzList" :key="i">
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
                <el-tab-pane
                        label="反馈信息"
                        name="third2"
                        v-if="feedbacks != undefined && feedbacks.length > 0"
                >
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <template v-for="(item, i) in feedbacks" :key="i">
                            <tr>
                                <td class="tit" width="80">反馈状态</td>
                                <td v-if="item.status == 1" style="color: red">未反馈</td>
                                <td v-if="item.status == 2" style="color: green">已反馈</td>
                                <td class="tit" width="80">通知时间</td>
                                <td>{{ item.notice_time }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="60">通知内容</td>
                                <td colspan="3">{{ item.notice_content }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="80">反馈内容</td>
                                <td colspan="3">{{ item.feedback_content }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="60">反馈时间</td>
                                <td>{{ item.feedback_time }}</td>
                                <td class="tit" width="80">反馈人</td>
                                <td>{{ item.inspect_name }}</td>
                            </tr>
                        </template>
                    </table>
                </el-tab-pane>
                <el-tab-pane
                        label="支付信息"
                        name="third1"
                        v-if="
            detailsData.payments != undefined && detailsData.payments.length > 0
          "
                >
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <template v-for="(item, i) in detailsData.payments" :key="i">
                            <tr>
                                <td class="tit" width="60">缴费金额</td>
                                <td>{{ item.amount == null ? "0" : item.amount }}(元)</td>
                                <td class="tit" width="80">缴费时间</td>
                                <td>{{ item.create_time }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="80">支付方式</td>
                                <td>{{ item.payment_type }}</td>
                                <td class="tit" width="60">支付来源</td>
                                <td>{{ item.payment_resource }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="60">收款人</td>
                                <td>徐州经济技术开发区金瑞房地产经营有限公司</td>
                                <!--                                <td>{{item.inspect_name}}</td>-->
                                <td class="tit" width="80">支付流水号</td>
                                <td>{{ item.payment_serialno }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="60">支付状态</td>
                                <td>{{ item.status }}</td>
                                <td class="tit" width="80">支付订单号</td>
                                <td>{{ item.payment_no }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="60">备注</td>
                                <td colspan="3">{{ item.remarks }}</td>
                            </tr>
                        </template>
                    </table>
                </el-tab-pane>
                <el-tab-pane
                        label="退款信息"
                        name="third3"
                        v-if="
            detailsData.refunds != undefined && detailsData.refunds.length > 0
          "
                >
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <template v-for="(item, i) in detailsData.refunds" :key="i">
                            <tr>
                                <td class="tit" width="80">退款方式</td>
                                <td>{{ item.refund_channel }}</td>
                                <td class="tit" width="80">申请时间</td>
                                <td>{{ item.refund_time }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="80">退款订单号</td>
                                <td>{{ item.payment_no ? item.payment_no : "无" }}</td>
                                <td class="tit" width="80">退款流水号</td>
                                <td>{{ item.payment_serialno }}</td>
                            </tr>
                            <tr>
                                <td class="tit" width="80">退款金额</td>
                                <td>{{ item.refund_amount }}</td>
                                <td class="tit" width="80">退款渠道</td>
                                <td>{{ item.refund_channel }}</td>
                            </tr>
                        </template>
                    </table>
                </el-tab-pane>
                <el-tab-pane
                        label="开票信息"
                        name="third4"
                        v-if="detailsData.management != undefined"
                >
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
                        <tr>
                            <td class="tit" width="80">开票方式</td>
                            <td v-if="detailsData.management.invoice_type == 0">个人</td>
                            <td v-else>企业</td>
                            <td class="tit" width="80">发票金额</td>
                            <td>{{ detailsData.management.amount }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="80">申请时间</td>
                            <td>{{ detailsData.management.application_time }}</td>
                            <td class="tit" width="80">收票人</td>
                            <td>{{ detailsData.management.receiver }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="80">税号</td>
                            <td>{{ detailsData.management.duty_paragraph }}</td>
                            <td class="tit" width="80">银行账户</td>
                            <td>{{ detailsData.management.bank_account }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="80">电话</td>
                            <td>{{ detailsData.management.contact_info }}</td>
                            <td class="tit" width="80">开票状态</td>
                            <td>{{ detailsData.management.invoice_mode }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="80">地址</td>
                            <td colspan="3">{{ detailsData.management.address }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="80">开户行</td>
                            <td colspan="3">{{ detailsData.management.bank_of_deposit }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="90">收票人手机号</td>
                            <td colspan="3">{{ detailsData.management.receive_phone }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="80">收票人地址</td>
                            <td colspan="3">{{ detailsData.management.receive_address }}</td>
                        </tr>
                    </table>
                </el-tab-pane>
            </el-tabs>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="danger" @click="saveokk(detailsData.records)" :disabled="isDisabl"
          >通过</el-button
          >
          <el-button type="info" @click="reject" :disabled="isDisabl">驳回</el-button>
        </span>
            </template>
        </el-dialog>
        <!-- 申诉处理 -->
        <el-dialog title="处理申诉" v-model="viewVisible" width="40%">
            <table class="desctable mgb20 w">
                <tr>
                    <td class="tit" width="60">订单号</td>
                    <td>{{ detailsData.order_no }}</td>
                    <td class="tit" width="60">订单状态</td>
                    <td>
                        <el-tag type="success">{{ detailsData.statusName }}</el-tag>
                    </td>
                </tr>
                <tr>
                    <td class="tit" width="60">停留时间</td>
                    <td>{{ detailsData.resitime }}</td>
                    <td class="tit" width="80">费用</td>
                    <td>
                        <el-tag type="danger">{{ detailsData.sum_amount }}</el-tag>
                    </td>
                </tr>
                <tr>
                    <td class="tit" width="60">开始时间</td>
                    <td>{{ detailsData.drivein_time }}</td>
                    <td class="tit" width="80">结束时间</td>
                    <td>{{ detailsData.driveout_time }}</td>
                </tr>
            </table>
            <el-form>
                <el-form-item>
                    <el-radio-group
                            v-model="form.handle_status"
                            size="small"
                            @change="handleStatusWay"
                    >
                        <el-radio-button v-model="form.handle_status" label="2"
                        >通过
                        </el-radio-button
                        >
                        <el-radio-button v-model="form.handle_status" label="3"
                        >驳回
                        </el-radio-button
                        >
                    </el-radio-group>
                </el-form-item>
                <div v-if="form.handle_status == 2">
                    <el-form-item>
                        <el-radio-group
                                v-model="form.handle_type"
                                @change="radioBtn"
                                v-if="hideType"
                        >
                            <el-radio v-model="form.handle_type" :label="1"
                            >按结束时间处理
                            </el-radio
                            >
                            <el-radio v-model="form.handle_type" :label="2"
                            >按订单费用处理
                            </el-radio
                            >
                            <el-radio v-model="form.handle_type" :label="3"
                            >按退款处理
                            </el-radio
                            >
                            <!-- <el-radio :label="4">按订单状态处理</el-radio> -->
                            <el-radio v-model="form.handle_type" :label="5"
                            >按修正车牌处理
                            </el-radio
                            >
                        </el-radio-group>
                    </el-form-item>
                    <!-- 按结束时间处理  -->
                    <el-form-item label="结束时间" v-if="form.handle_type == 1">
                        <el-date-picker
                                v-model="timeData.time"
                                type="datetime"
                                class="w"
                                size="small"
                                @change="getQueryDate"
                        >
                        </el-date-picker>
                        修改结束时间会重新计费，如果订单已支付，会将差额退还给用户
                    </el-form-item>
                    <!-- 按订单费用处理   -->
                    <el-form-item label="订单费用" v-if="form.handle_type == 2">
                        <el-input
                                size="small"
                                v-model="form.refund_amount"
                                placeholder=""
                        ></el-input>
                        <span v-if="showTitle">修改订单费用会将差额退还给用户</span>
                    </el-form-item>
                    <!-- 按退款处理   -->
                    <el-form-item label="退款金额" v-if="form.handle_type == 3">
                        <el-input
                                size="small"
                                v-model="form.refund_amount"
                                placeholder=""
                        ></el-input>
                    </el-form-item>
                    <!-- 按订单状态处理  -->
                    <el-form-item label="订单状态" v-if="showLine3">
                        <el-select
                                v-model="form.order_status"
                                placeholder="请选择"
                                class="w"
                        >
                            <el-option
                                    v-for="(item, i) in orderStatus"
                                    :key="i"
                                    :label="item.label"
                                    :value="item.dc_value"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <!-- 按修正车牌处理 -->
                    <el-form-item v-if="form.handle_type == 5">
                        <el-row :gutter="20">
                            <el-col :span="12">
                                <el-input
                                        size="small"
                                        v-model="form.car_no"
                                        placeholder="请输入车牌"
                                ></el-input>
                            </el-col>
                            <el-col :span="12">
                                <el-select
                                        size="small"
                                        v-model="form.carno_type"
                                        placeholder="请选择车牌类型"
                                        class="w"
                                >
                                    <el-option
                                            v-for="(item, i) in carTypes"
                                            :key="i"
                                            :label="item.label"
                                            :value="item.dc_value"
                                    ></el-option>
                                </el-select>
                            </el-col>
                        </el-row>
                        <span v-if="showTitle1"
                        >*如果订单已经缴费，修正车牌后给把费用退还给车主。已开票的订单不允许操作</span
                        >
                    </el-form-item>
                </div>
                <div v-if="form.handle_status == 3">
                    <el-form-item v-if="showStates">
                        <el-input
                                type="textarea"
                                v-model="form.reject_reason"
                                size="small"
                                :rows="5"
                                placeholder="填写驳回原因或指导意见"
                        ></el-input>
                    </el-form-item>
                    <el-form-item prop="pic" v-if="showStates">
                        <el-upload
                                :action="fileurl"
                                list-type="picture-card"
                                limit="5"
                                :on-preview="handlePictureCardPreview"
                                :on-remove="handleRemove"
                                :on-success="handleSuccess"
                                :file-list="fileList2"
                        >
                            <i class="el-icon-plus"></i>
                        </el-upload>
                        上传凭证图片（可选）
                    </el-form-item>
                </div>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewVisibleBtn">取 消</el-button>
          <el-button type="danger" @click="saveEdit" :disabled="isDisabl">确定</el-button>
        </span>
            </template>
        </el-dialog>
        <!-- 通知巡检员反馈 -->
        <el-dialog title="通知巡检员反馈" v-model="appealVisible" width="40%">
            <el-form label-width="80px" :rules="formRules" ref="formId" :model="form">
                <el-form-item label="通知内容" prop="notice_content">
                    <el-input
                            v-model="form.notice_content"
                            type="textarea"
                            :rows="8"
                    ></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="appealVisible = false">取 消</el-button>
          <el-button type="danger" @click="saveAppeal('formId')" :disabled="isDisabl"
          >确定</el-button
          >
        </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    import {reactive, ref} from "vue";
    import {ElMessage} from "element-plus";
    import {dictData} from "../../../api/index";
    import {delImgFile} from "../../../api/sysResourceIndex.js";
    import File_URL from "../../../file_url";
    import {
        appealNoticeFeedbackSave,
        diversePattern,
        operateappealDetailsData,
        operateappealPageData,
        exportshensuData,
        saveok,
        parkingApprove,
    } from "../../../api/operateAppeal";

    export default {
        name: "appealPage",
        data() {
            return {
                isDisabl: false,
                tableH: 0,
                fileurl: File_URL.file_img_url,
                lableTitle: "",
                showStates: false,
                showLine1: true,
                showTitle: false,
                showLine2: false,
                showLine3: false,
                showLine4: false,
                hideType: true,
                showTitle1: false,
                // 表单验证
                formRules: {
                    notice_content: [
                        {required: true, message: "必填项", trigger: "blur"},
                    ],
                },
            };
        },
        setup() {
            const query = reactive({
                pageIndex: 1,
                pageSize: 15,
                appeal_status: 2,
                startTime: "",
                endTime: "",
                keys: "",
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            const listCount = ref({
                sumData: 0,
                yclData: 0,
                wclData: 0,
            });

            // 获取表格数据
            const getData = () => {
                console.log(query);
                operateappealPageData(query).then((res) => {
                    console.log(res.data);
                    tableData.value = res.data.page.list;
                    pageTotal.value = res.data.page.total;
                    //申述总数
                    listCount.value.sumData = pageTotal.value;
                    //已处理
                    listCount.value.yclData =
                        parseInt(pageTotal.value) - parseInt(res.data.wcl_count);
                    //未处理
                    listCount.value.wclData = res.data.wcl_count;
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

            // 表格编辑时弹窗和保存
            const isDisabl = ref(false);
            const editVisible = ref(false);
            const viewVisible = ref(false);
            const appealVisible = ref(false);
            const fileList2 = ref([]);
            const fileIds = ref([]);
            const detailsData = ref({});
            const feedbacks = ref({});
            let form = ref({
                end_time: "",
                reject_reason: "",
                handle_type: 1,
                parking_order_id: "",
                handle_status: "",
                notice_content: "",
                order_amount: "",
                order_status: "",
                car_no: "",
                carno_type: "",
                operate_appeal_id: "",
                is_invoice: 0,
                showhanstatus: true,
                user_type: "",
                yc_car_type: "",
                yc_car_no: "",
            });
            const activeName = ref("first");
            // 详情数据
            const handleEdit = (index, row, type) => {
                activeName.value = "first";
                operateappealDetailsData(
                    reactive({
                        parking_order_id: row.parking_order_id,
                        operate_id: row.id,
                    })
                )
                    .then((res) => {
                        detailsData.value = res.orders;
                        feedbacks.value = res.feedbacks;
                        //停车场id
                        let parking_order_id = row.parking_order_id;
                        form.value = {
                            parking_order_id: parking_order_id,
                            operate_appeal_id: row.id,
                            is_invoice: row.is_invoice,
                            user_type: res.user_type,
                        };
                        //获取订单状态
                        orderStatusList();
                    })
                    .then(() => {
                        editVisible.value = true;
                    });
            };
            //处理申诉
            const handleView = (row) => {
                form.value.is_invoice = 0;
                form.value.handle_type = 1;
                form.value.handle_status = "2";
                editVisible.value = false;
                viewVisible.value = true;
            };

            const saveokk = (data) => {
                isDisabl.value = true;
                console.log(form.value.operate_appeal_id);
                var q = reactive({
                    appealId: form.value.operate_appeal_id,
                    type: 1,
                });
                parkingApprove(q).then((res) => {
                    if (res.code == 0) {
                        setTimeout(() => {
                            isDisabl.value = false;
                        }, 2000);
                        ElMessage.success("操作成功");
                        editVisible.value = false;
                        getData();
                    } else {
                        setTimeout(() => {
                            isDisabl.value = false;
                        }, 2000);
                        ElMessage.error(res.msg);
                    }
                });
            };

            const reject = () => {
                isDisabl.value = true;
                var q = reactive({
                    appealId: form.value.operate_appeal_id,
                    type: 2,
                });
                parkingApprove(q).then((res) => {
                    if (res.code == 0) {
                        setTimeout(() => {
                            isDisabl.value = false;
                        }, 2000);
                        ElMessage.success("操作成功");
                        editVisible.value = false;
                        getData();
                    } else {
                        setTimeout(() => {
                            isDisabl.value = false;
                        }, 2000);
                        ElMessage.error(res.msg);
                    }
                });
            };

            const orderStatus = ref({});
            const carTypes = ref({});

            //字典获取订单状态类别
            const orderStatusList = (val) => {
                dictData(reactive({dict_type: "order_status"})).then((res) => {
                    orderStatus.value = res.data;
                });
                dictData(reactive({dict_type: "car_type"})).then((res) => {
                    carTypes.value = res.data;
                });
            };

            //通知巡检员反馈
            const noticeFeedback = (index, row) => {
                editVisible.value = false;
                viewVisible.value = false;
                appealVisible.value = true;
            };

            let timeData = ref({
                time: "",
                time1: "",
            });
            //日期控件change方法
            const getQueryDate1 = () => {
                if (
                    null == timeData.value.time1 ||
                    [] == timeData.value.time1 ||
                    "" == timeData.value.time1
                ) {
                    query.startTime = "";
                    query.endTime = "";
                } else {
                    query.startTime = dateFormat1(timeData.value.time1[0]);
                    query.endTime = dateFormat1(timeData.value.time1[1]);
                }
            };
            //日期控件change方法
            const getQueryDate = () => {
                if (
                    null == timeData.value.time ||
                    [] == timeData.value.time ||
                    "" == timeData.value.time
                ) {
                    form.value.end_time = "";
                } else {
                    form.value.end_time = dateFormat(timeData.value.time);
                }
            };

            //日期格式化
            const dateFormat1 = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
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

            return {
                query,
                tableData,
                pageTotal,
                editVisible,
                isDisabl,
                viewVisible,
                appealVisible,
                form,
                timeData,
                getQueryDate,
                getQueryDate1,
                fileIds,
                fileList2,
                listCount,
                feedbacks,
                detailsData,
                carTypes,
                orderStatus,
                getData,
                reject,
                saveokk,
                noticeFeedback,
                handleSearch,
                handlePageChange,
                handleEdit,
                handleView,
                multipleSelection: [],
                value: true,
                activeName,
                url: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
                srcList: [
                    "https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg",
                    "https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg",
                ],
            };
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 308 + "px";
        },
        methods: {
            //上传图片操作
            handleSuccess(response, file, fileList) {
                this.fileIds.push(response.id + "");
                file.id = response.id;
            },
            //删除图片
            handleRemove(file, fileList) {
                let index = this.fileIds.indexOf(file.id + "");
                if (index == -1) {
                    index = this.fileIds.indexOf(file.id);
                }
                let flag = 0;
                if (index != -1) {
                    delImgFile(reactive({id: file.id}))
                        .then((res) => {
                            flag = res.success;
                        })
                        .then(() => {
                            if (flag == 0) {
                                ElMessage.success("操作成功");
                                //移除删除id
                                this.fileIds.splice(index, 1);
                            } else if (flag == -1) {
                                ElMessage.error("操作失败!");
                            }
                        });
                }
            },
            //图片预览
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            //图片回显
            imgurl: function (url) {
                if (url != "" && url != null) {
                    return url;
                }
            },
            handleSelectionChange(data) {
                this.selectedData = data;
            },
            formdate(date) {
                var json_date = new Date(date.appeal_time).toJSON();
                return new Date(+new Date(json_date) + 8 * 3600 * 1000)
                    .toISOString()
                    .replace(/T/g, " ")
                    .replace(/\.[\d]{3}Z/, "");
            },
            saveAppeal(formName) {
                let _that = this;
                //通知巡检员保存方法
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        _that.isDisabl = true;
                        let falg = 0;
                        let query = reactive({
                            operate_appeal_id: _that.detailsData.id,
                            notice_content: _that.form.notice_content,
                            parking_order_id: _that.form.parking_order_id,
                            type: 0,
                        });
                        appealNoticeFeedbackSave(query).then((res) => {
                            if (res.code == 0) {
                                setTimeout(() => {
                                    _that.isDisabl = false;
                                }, 2000);
                                ElMessage.success("操作成功");
                                _that.appealVisible = false;
                            } else {
                                setTimeout(() => {
                                    _that.isDisabl = false;
                                }, 2000);
                                ElMessage.error(res.msg);
                            }
                        });
                    }
                });
            },
            radioBtn(val) {
                var _this = this;
                if (val == 1) {
                    _this.timeData.time = "";
                    _this.form.end_time = "";
                    _this.showLine1 = true;
                    _this.showLine2 = false;
                    _this.showLine3 = false;
                    _this.showLine4 = false;
                } else if (val == 2 || val == 3) {
                    _this.form.refund_amount = "";
                    if (val == 2) {
                        // _this.lableTitle="订单费用";
                        _this.showTitle = true;
                    } else {
                        // _this.lableTitle="退款金额";
                        _this.showTitle = false;
                    }
                    _this.showLine2 = true;
                    _this.showLine1 = false;
                    _this.showLine3 = false;
                    _this.showLine4 = false;
                } else if (val == 4) {
                    _this.form.order_status = "";
                    _this.showLine3 = true;
                    _this.showLine1 = false;
                    _this.showLine2 = false;
                    _this.showLine4 = false;
                } else if (val == 5) {
                    _this.form.car_no = "";
                    _this.form.carno_type = "";
                    _this.showLine4 = true;
                    _this.showLine1 = false;
                    _this.showLine2 = false;
                    _this.showLine3 = false;
                    _this.showTitle1 = true;
                }
            },
            handleStatusWay(val) {
                var _this = this;
                if (val == 3) {
                    _this.showStates = true;
                    _this.hideType = false;
                    _this.showLine1 = false;
                    _this.showLine2 = false;
                    _this.showLine3 = false;
                    _this.showLine4 = false;
                } else {
                    _this.showStates = false;
                    _this.hideType = true;
                    _this.showLine1 = true;
                }
            },
            saveEdit() {
                var _this = this;
                _this.isDisabl = true;
                _this.form.fileList2 = [];
                if (_this.fileIds.length > 0) {
                    _this.form.fileIds = _this.fileIds.join(",");
                } else {
                    _this.form.fileIds = "";
                }
                _this.form.yc_car_type = _this.detailsData.car_type;
                _this.form.yc_car_no = _this.detailsData.car_no;
                diversePattern(_this.form).then((res) => {
                    console.log(res);
                    if (res.code == 0) {
                        setTimeout(() => {
                            _this.isDisabl = false;
                        }, 2000);
                        ElMessage.success(res.msg);
                        _this.showStates = false;
                        _this.hideType = true;
                        _this.showLine1 = true;
                        _this.form.fileList2 = []; //图片文件数组
                        _this.fileIds = []; //图片上传ids
                        _this.query.pageIndex = 1;
                        _this.getData();
                        _this.viewVisible = false;
                        this.viewVisibleBtn();
                    } else if (res.code == -1) {
                        setTimeout(() => {
                            _this.isDisabl = false;
                        }, 2000);
                        ElMessage.error(res.msg);
                    }
                });
            },
            viewVisibleBtn() {
                var _this = this;
                _this.viewVisible = false;
                _this.showStates = false;
                _this.showLine1 = true;
                _this.showTitle = false;
                _this.showLine2 = false;
                _this.showLine3 = false;
                _this.showLine4 = false;
                _this.hideType = true;
                _this.showTitle1 = false;
            },
            exportshensu() {
                ElMessage.success("正在下载中·····");
                exportshensuData(this.query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "路内申诉订单.xls");
                    document.body.appendChild(link);
                    link.click();
                });
            },
        },
    };
</script>
