<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline label-width="80" size="small" class="lineH0">
                        <el-form-item label="停车场">
                            <el-select clearable v-model="query.placeId" filterable size="small" placeholder="所有停车场"
                                       class="w100">
                                <el-option v-for="item in formqjl.parks" :key="item.id" :label="item.park_name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </el-form-item>
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
                        <el-form-item label="订单号" class="search-mb0">
                            <el-input
                                    @keyup.enter="handleSearch()"
                                    size="small"
                                    placeholder="订单号"
                                    v-model="query.orderNo"
                            ></el-input>
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
                        <el-form-item label="免单类型" class="search-mb0">
                            <el-select
                                    clearable
                                    v-model="query.free_type"
                                    filterable
                                    size="small"
                                    class="w100"
                            >
                                <el-option
                                        v-for="item in freeTypeList"
                                        :key="item.dc_value"
                                        :label="item.label"
                                        :value="item.dc_value"
                                ></el-option>
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
                        <el-form-item label="停入时间" class="">
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
                        <el-form-item label="" class="">
                            <el-button
                                    size="small"
                                    type="primary"
                                    icon="el-icon-search"
                                    @click="handleSearch"
                            >查询
                            </el-button
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
                                class=""
                        >
                            <!--<el-button-->
                                    <!--type="success"-->
                                    <!--size="small"-->
                                    <!--icon="el-icon-document"-->
                                    <!--@click="handleSpecial()"-->
                                    <!--v-permission="'park_parkingorder_rgkz'"-->
                            <!--&gt;人工开闸-->
                            <!--</el-button-->
                            <!--&gt;-->
                            <el-button size="small"  icon="el-icon-upload2" type="success" @click="exportParking"
                                       v-permission="'park_parkingorder_excel'"
                            >导出
                            </el-button
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

                <!-- <div class="clear"></div> -->
            </div>
            <el-table
                    :data="tableData"
                    border
                    class="table"
                    :max-height="tableH"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
                    @row-dblclick="rowclick"
            >
                <el-table-column label="序号" width="55" align="center">
                    <template #default="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="orderNo"
                        label="订单号"
                        width="200"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="placeName"
                        label="停车场"
                        min-width="200"
                        align="center"
                ></el-table-column>
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
                        prop="phone"
                        label="手机号"
                        width="120"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="driveinTime"
                        label="停入时间"
                        width="180"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="driveoutTime"
                        label="离场时间"
                        align="center"
                        width="180"
                ></el-table-column>
                <el-table-column
                        prop="resitime"
                        width="130"
                        align="center"
                        label="停留时间"
                ></el-table-column>
                <el-table-column
                        prop="sum_amount"
                        label="停车费用"
                        width="100"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="free_type"
                        label="免单类型"
                        width="100"
                        align="center"
                ></el-table-column>
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
                        >合并记录
                        </el-button
                        >
                        <el-button
                                type="text"
                                size="mini"
                                icon="el-icon-tickets"
                                disabled
                                v-else
                        >合并记录
                        </el-button
                        >
                    </template>
                </el-table-column>
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
                <!--<el-table-column-->
                        <!--prop="operation_type_in"-->
                        <!--label="驶入操作"-->
                        <!--align="center"-->
                <!--&gt;-->
                    <!--<template #default="scope">-->
                        <!--<el-tag size="small" v-if="scope.row.operation_type_in == '0'"-->
                        <!--&gt;正常-->
                        <!--</el-tag-->
                        <!--&gt;-->
                        <!--<el-tag-->
                                <!--size="small"-->
                                <!--type="danger"-->
                                <!--v-else-if="scope.row.operation_type_in == '1'"-->
                        <!--&gt;遥控-->
                        <!--</el-tag-->
                        <!--&gt;-->
                    <!--</template>-->
                <!--</el-table-column>-->
                <!--<el-table-column-->
                        <!--prop="operation_type_out"-->
                        <!--label="驶离操作"-->
                        <!--align="center"-->
                <!--&gt;-->
                    <!--<template #default="scope">-->
                        <!--<el-tag size="small" v-if="scope.row.operation_type_out == '0'"-->
                        <!--&gt;正常-->
                        <!--</el-tag-->
                        <!--&gt;-->
                        <!--<el-tag-->
                                <!--size="small"-->
                                <!--type="danger"-->
                                <!--v-else-if="scope.row.operation_type_out == '1'"-->
                        <!--&gt;遥控-->
                        <!--</el-tag-->
                        <!--&gt;-->
                    <!--</template>-->
                <!--</el-table-column>-->
                <el-table-column prop="car_type" v-if="liHide"></el-table-column>
                <el-table-column prop="create_time" v-if="liHide"></el-table-column>
                <el-table-column prop="source_type" v-if="liHide"></el-table-column>
                <el-table-column prop="car_id" v-if="liHide"></el-table-column>
                <el-table-column label="操作" width="220" fixed="right" align="center">
                    <template #default="scope">
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-set-up"
                                :disabled="scope.row.status == '2' ? false : true"
                                @click="autoGoBtn(scope.$index, scope.row)"
                        >人工放行
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-view"
                                @click="closeBtn(scope.$index, scope.row)"
                                v-permission="'park_parkingorder_details'"
                        >详情
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-delete"
                                @click="handleDelete(scope.$index, scope.row)"
                                class="red"
                                v-permission="'park_parkingorder_del'"
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
                        prop="parkName"
                        label="停车场"
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
                                        :src="imgurl(item.file_url)"
                                        class="ml5"
                                        hide-on-click-modal="true"
			                            preview-teleported="true"
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
                                        :src="imgurl(item.file_url)"
                                        class="ml5"
                                        hide-on-click-modal="true"
			                            preview-teleported="true"
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
                            <td class="tit" width="80">费用</td>
                            <td>
                                <el-tag type="danger">{{
                                    parkDetails.sum_amount ? parkDetails.sum_amount : "0"
                                    }}
                                </el-tag>
                            </td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">停车地点</td>
                            <td colspan="3">{{ parkDetails.road_name }}</td>
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
                                    >修改车牌号码
                                    </el-button
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
                                    >取消
                                    </el-button
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
                            <td v-if="parkDetails.source == 1">视频采集</td>
                            <td v-else>人工</td>
                            <td class="tit" width="80">创建时间</td>
                            <td>{{ parkDetails.create_time }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">进场时间</td>
                            <td>{{ parkDetails.drivein_time }}</td>
                            <td class="tit" width="80">离场时间</td>
                            <td>{{ parkDetails.driveout_time }}</td>
                        </tr>
                        <tr>
                            <td class="tit" width="60">放行理由</td>
                            <td colspan="3">{{ parkDetails.fxReason }}</td>
                        </tr>
                    </table>
                </el-tab-pane>
                <el-tab-pane label="进出场信息" name="second">
                    <div class="mt20"></div>
                    <table class="desctable mgb20 w">
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
                                            :src="imgurl(item.file_url)"
                                            class="ml5"
                                            hide-on-click-modal="true"
			                                preview-teleported="true"
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
                                            :src="imgurl(item.file_url)"
                                            class="ml5"
                                            hide-on-click-modal="true"
			                                preview-teleported="true"
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

        <el-dialog title="人工开闸" v-model="specialVisible" width="80%">
            <div class="handle-box">
                <div class="left-panel">
                    <el-form inline label-width="80" size="small">
                        <el-form-item label="订单号">
                            <el-input
                                    @keyup.enter="handleSearch1()"
                                    size="small"
                                    v-model="parkOrderOpengate.order_no"
                                    placeholder="订单号"
                                    class="handle-input mr10"
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="车牌号">
                            <el-input
                                    @keyup.enter="handleSearch1()"
                                    size="small"
                                    v-model="parkOrderOpengate.car_no"
                                    placeholder="车牌号"
                                    class="handle-input mr10"
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="开闸类型">
                            <el-select
                                    v-model="parkOrderOpengate.opengate_type"
                                    placeholder="请选择"
                                    class="w"
                                    clearable
                            >
                                <el-option
                                        v-for="(item, i) in result.openGateList"
                                        :key="i"
                                        :label="item.label"
                                        :value="item.dc_value"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button
                                    size="small"
                                    type="primary"
                                    icon="el-icon-search"
                                    @click="handleSearch1"
                            >查询
                            </el-button
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
                        prop="opengate_type"
                        label="开闸类型"
                        width="200"
                        align="center"
                >
                    <template #default="scope">
                        <div v-if="scope.row.opengate_type == '1'">公务开闸</div>
                        <div v-else-if="scope.row.opengate_type == '2'">故障开闸</div>
                        <div v-else-if="scope.row.opengate_type == '3'">临时通行</div>
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
                            {{ scope.row.resi_time }}
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="logintimes" label="人工开闸人" align="center">
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
                <el-table-column prop="logintimes" label="所属公司" align="center">
                    <template #default="scope">
                        <div class="todo-item">
                            {{ scope.row.companyName }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="logintimes" label="拍照" align="center">
                    <template #default="scope">
                        <el-image
                                class="table-td-thumb"
                                :src="imgurl(scope.row.file_url)"
                                hide-on-click-modal="true" preview-teleported="true"
                                :preview-src-list="[imgurl(scope.row.file_url)]"
                                v-if="scope.row.opengate_type == '1'"
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

        <el-dialog title="人工放行" v-model="autoVisible" width="500px">
            <el-form label-width="80" size="small">
                <el-form-item label="理由">
                    <el-input
                            size="small"
                            placeholder="请输入放行理由"
                            type="textarea"
                            row="4"
                            v-model="fxReason"
                    ></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                  <el-button @click="autoVisible = false">取 消</el-button>
                  <el-button @click="handleFx" type="primary">确 定</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    import {reactive, ref} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import Ueditor from "../../../components/UE.vue";
    import {
        dictData,
        isModel,
        orderOpengateList,
        queryAreaData,
        queryStreetData,
        queryParkData
    } from "../../../api/index.js";
    import {
        parkOrderCarData,
        parkOrderClosureData,
        parkOrderDetailsData,
        parkOrderListData,
        exportRoadData,
        updStatus,
        parkOrderDel
    } from "../../../api/roadParkingOrder";
    import {mergeOrder} from "../../../api/parkingOrder";
    import {
        getDictsByType,
    } from "../../../api/fapiao.js";

    import File_URL from "../../../file_url";

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
                tableH: 0,
            };
        },
        setup() {
            const query = reactive({
                phone: "",
                status: "",
                orderNo: "",
                carNo: "",
                startTime: "",
                endTime: "",
                checked: false,
                pageIndex: 1,
                pageSize: 15,
                type: "",
                streetId: "",
                areaId: "",
                placeId: "",
                free_type: "",
            });
            let formqjl = reactive({

                areas: [],
                streets: [],
                parks: []
            });
            const tableData = ref([]);
            const pageTotal = ref(0);
            const pageTotal1 = ref(0);
            const todoList = ref([]);
            const specialVisible = ref(false);

            // 获取表格数据
            const getData = () => {
                parkOrderListData(query).then((res) => {
                    console.log(res.data);
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
            };

            getData();
            const rowclick = (row) => {
                var carNo = row.carNo;
                query.carNo = carNo;
                getData();
            };
            const mergeVisible = ref(false);
            const mergeData = ref([]);
            const orderDetail = ref(false);
            const getMergeOrder = (index, row) => {
                mergeOrder(reactive({orderId: row.id})).then((res) => {
                    mergeData.value = res.data;
                });
                mergeVisible.value = true;
                orderDetail.value = false;
            };

            const mergeDetail = ref({});
            // 选择行
            const handleCurrentChange = (val) => {
                if (val) {
                    parkOrderDetailsData(reactive({id: val.parking_order_id})).then(
                        (res) => {
                            mergeDetail.value = res.data;
                        }
                    );
                    // currentRow = val;
                    orderDetail.value = true;
                }
            };
            let timeData = ref({
                time: "",
                time1: "",
            });
            //日期控件change方法
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
                    form.driveout_time = dateFormat2(timeData.value.time1);
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

            const dateFormat2 = (date) => {
                if (date != undefined && date != null) {
                    var t = new Date(date); //row 表示一行数据, updateTime 表示要格式化的字段名称
                    var year = t.getFullYear(),
                        month = t.getMonth() + 1,
                        day = t.getDate(),
                        hour = t.getHours(),
                        min = t.getMinutes(),
                        sec = t.getSeconds();
                    var newTime =
                        year +
                        "-" +
                        (month < 10 ? "0" + month : month) +
                        "-" +
                        (day < 10 ? "0" + day : day) +
                        " " +
                        (hour < 10 ? "0" + hour : hour) +
                        ":" +
                        (min < 10 ? "0" + min : min) +
                        ":" +
                        (sec < 10 ? "0" + sec : sec);
                    return newTime;
                }
            };

            let result = reactive({
                model: "",
                parkingOrderOpengateList: [],
                openGateList: [],
            });

            // 特殊开闸记录

            let dict = reactive({
                is_del: "0",
                dict_type: "openGateType",
            });
            const getopenGateType = () => {
                dictData(dict).then((res) => {
                    console.log(res);
                    result.openGateList = res.data;
                });
            };

            const imgViewUrl = File_URL.file_hx_img_url;
            console.log(imgViewUrl);
            const parkOrderOpengate = reactive({
                order_no: "",
                car_no: "",
                opengate_type: "",
                pageIndex: 1,
                pageSize: 10,
            });

            const imgurl = (url) => {
                if (url != "" && url != null) {
                    return url;
                }
            };

            const getOpengate = () => {
                orderOpengateList(parkOrderOpengate).then((res) => {
                    console.log(res.data);
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
                getopenGateType();
                getOpengate();
                specialVisible.value = true;
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
                time: "",
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
                areaId: query.areaId,
            });
            const getStreet = () => {
                if (query.areaId == "") {
                    formqjl.streets = [];
                    formqjl.parks = [];
                    query.streetId = "";
                    query.placeId = "";
                } else {
                    query.streetId = "";
                    query.placeId = "";
                    formqjl.parks = [];
                    queryStreet.areaId = query.areaId;
                    queryStreetData(queryStreet).then((res) => {
                        formqjl.streets = res.data;
                    });
                }
            };
            //获取停车场下拉框数据
            const getPark = () => {
                if (query.streetId == "") {
                    formqjl.parks = [];
                    query.placeId = "";
                } else {
                    formqjl.parks = [];
                    query.placeId = "";
                    queryParkData(reactive({streetId: query.streetId})).then((res) => {
                        formqjl.parks = res.data;
                    });
                }
            };

            const carType = ref([]);
            //字典列表
            dictData(reactive({dict_type: "car_type"})).then((res) => {
                carType.value = res.data;
            });
            //详情获取
            const handleEdit = (index, row) => {
                parkOrderDetailsData(reactive({id: row.id})).then((res) => {
                    parkDetails.value = res.data;
                });
                editVisible.value = true;
            };

            const autoVisible = ref(false);
            const autoGoBtn = (index, row) => {
                fxReason.value = "";
                orderId.value = row.id;
                autoVisible.value = true;
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
                    parkOrderDel(edit_resource).then((res) => {
                        console.log(res);
                        ElMessage.success("删除成功");
                    }).then(() => {
                        getData();
                    });
                }).catch(() => {
                });
            };

            // 人工放行
            const fxReason = ref("");
            const orderId = ref("");
            const handleFx = (index, row) => {
                let edit_resource = reactive({
                    fxReason: fxReason.value,
                    id: orderId
                });
                console.log(edit_resource)
                if ("" == fxReason.value) {
                    ElMessage.error("请填写放行理由");
                    return false;
                }
                updStatus(edit_resource).then((res) => {
                    console.log(res);
                    ElMessage.success("放行成功");
                }).then(() => {
                    autoVisible.value = false;
                    getData();
                });
            };

            const freeTypeList = ref([]);
            // 获取表格数据
            const getDictsFreeType = () => {
                getDictsByType(reactive({ type: "free_type" })).then((res) => {
                    freeTypeList.value = res.data;
                });
            };
            getDictsFreeType();
            return {
                query,
                imgViewUrl,
                carType,
                tableData,
                pageTotal,
                pageTotal1,
                parkOrderOpengate,
                editVisible,
                form,
                mergeVisible,
                mergeData,
                orderDetail,
                mergeDetail,
                imgurl,
                todoList,
                specialVisible,
                autoVisible,
                fxReason,
                orderId,
                autoGoBtn,
                parkDetails,
                result,
                timeData,
                rowclick,
                getMergeOrder,
                getData,
                getQueryDate,
                getQueryDate1,
                handleEdit,
                handleSearch,
                handleSearch1,
                handleSpecial,
                handlePageChange,
                handlePageChange1,
                handleCurrentChange,
                multipleSelection: [],
                dialogImageUrl: "",
                ppVisible: false,
                isActive: false,
                activeName: "first",
                formqjl,
                getArea,
                getStreet,
                getPark,
                handleDelete,
                freeTypeList,
                getDictsFreeType,
                handleFx,
            };
        },
        methods: {
            //上传图片操作
            handleRemove(file, fileList) {
            },
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
                    driveout_time: _this.form.driveout_time,
                });
                parkOrderClosureData(query).then((res) => {
                    if (res.code == 0) {
                        ElMessage.success(res.msg);
                        _this.editVisible = false;
                        _this.getData();
                    } else {
                        ElMessage.success(res.msg);
                    }
                });
            },
            carUpdDate() {
                this.isShow = !this.isShow; //取反
                this.isShowInfo = !this.isShowInfo; //取反
            },
            changeisShowInfo(val) {
                var _this = this;
                this.isShow = !this.isShow; //取反
                this.isShowInfo = !this.isShowInfo; //取反
                if (val == 1) {
                    parkOrderCarData(
                        reactive({
                            id: _this.parkDetails.id,
                            car_id: _this.parkDetails.car_id,
                            car_no: _this.parkDetails.car_no,
                            car_type: _this.parkDetails.car_type,
                        })
                    ).then((res) => {
                        if (res.code == 0) {
                            ElMessage.success(res.msg);
                        } else {
                            ElMessage.error(res.msg);
                        }
                    });
                }
            },
            //图片回显

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

            exportParking() {
                ElMessage.success("正在下载中·····");
                this.query.type = "1";
                exportRoadData(this.query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "停车订单.xls");
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
