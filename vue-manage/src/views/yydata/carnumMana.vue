<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <!-- 车牌录入 start-->
        <el-dialog v-model="dialogVisible">
            <div class="customWidth">
                <img class="imgWidth" :src="dialogImageUrl" alt=""/>
            </div>
        </el-dialog>

        <el-card shadow="hover" v-permission="['road_carnummana_cplr', 'park_carnummana_cplr']">
            <template #header>
                <div class="clearfix">
                    <span>车牌录入</span>
                </div>
            </template>
            <el-form label-width="110px" :rules="formRules" ref="formId" :model="addForm" :show-message="false">
                <el-row :gutter="40">
                    <el-col :span="8">
                        <el-form-item label="车牌号码" prop="car_no">
                            <el-input
                                    @blur="upperCase"
                                    placeholder="车牌号"
                                    v-model="addForm.car_no"

                            ></el-input>
                        </el-form-item>
                        <el-form-item label="车牌类型">
                            <el-radio-group v-model="addForm.car_type">
                                <el-radio
                                        v-for="item in carTypeData"
                                        :key="item.id"
                                        :label="item.dc_value"
                                >{{ item.label }}
                                </el-radio
                                >
                            </el-radio-group>
                        </el-form-item>
                        <!--         <el-form-item label="车牌性质" prop="car_natures">-->
                        <!--          <el-select v-model="addForm.car_natures" placeholder="请选择"  class="handle-input">-->
                        <!--            <el-option v-for="(item,i) in carNaturesData" :key="i" :label="item.label" :value="item.dc_value"></el-option>-->
                        <!--          </el-select>-->
                        <!--        </el-form-item>-->
                        <el-form-item label="名单类型">
                            <el-radio-group v-model="addForm.roster_type" @change="radioBtns">
                                <el-radio
                                        v-for="item in rosterTypeData"
                                        :key="item.id"
                                        :label="item.dc_value"
                                >{{ item.label }}
                                </el-radio
                                >
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item>
                            <el-button
                                    type="primary"
                                    @click="saveCar('formId')"
                                    :disabled="isDisabl"
                            >提交
                            </el-button
                            >
                            <el-button @click="resetForm('formId')">重置</el-button>
                            <!--<el-button type="primary" plain @click="backPage()">返回</el-button>-->
                        </el-form-item>
                    </el-col>
                    <el-col :span="16">
                        <el-row :gutter="40">
                            <el-col :span="12" v-show="whiteStas">
                                <el-form-item label="白名单类型">
                                    <el-radio-group v-model="addForm.whitelist_type" @change="radioBtnsbmd">
                                        <el-radio
                                                v-for="item in whitelistTypeData"
                                                :key="item.id"
                                                :label="item.dc_value"
                                        >{{ item.label }}
                                        </el-radio
                                        >
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" v-show="whiteStasName">
                                <el-form-item label="姓名" prop="name">
                                    <el-input
                                            placeholder="姓名"
                                            v-model="addForm.name"
                                            required
                                    ></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" v-show="cj_whiteStas">
                                <el-form-item
                                        label="残疾证号"
                                        prop="deformity_cert"
                                        required
                                >
                                    <el-input
                                            placeholder="残疾证号"
                                            v-model="addForm.deformity_cert"
                                    ></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" v-show="whiteStas">
                                <el-form-item label="电话" prop="phone" required>
                                    <el-input
                                            placeholder="电话"
                                            v-model="addForm.phone"
                                    ></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" v-show="whiteStas" >
                                <el-form-item label="公司名称" prop="company_name" required>
                                    <el-input
                                            placeholder="公司名称"
                                            v-model="addForm.company_name"
                                    ></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" v-show="whiteStas">
                                <el-form-item label="停车场">
                                    <el-select v-model="addForm.parkIds" multiple placeholder="请选择"
                                               style="width: 100%;">
                                        <el-option
                                                v-for="item in parks"
                                                :key="item.id"
                                                :label="item.park_name"
                                                :value="item.id"
                                        >
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" v-show="whiteStas">
                                <el-form-item label="路内">
                                    <el-select v-model="addForm.roadIds" multiple placeholder="请选择"
                                               style="width: 100%;">
                                        <el-option
                                                v-for="item in roads"
                                                :key="item.id"
                                                :label="item.road_name"
                                                :value="item.id"
                                        >
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" v-show="whiteTime3">
                                <el-form-item label="免费类型">
                                    <el-radio-group v-model="addForm.white_time_type" @change="radioBtnWhiteTimeType">
                                        <el-radio label="1">永久</el-radio>
                                        <el-radio label="2">期限</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>

                            <el-col :span="12" v-show="whiteTime2">
                                <el-form-item label="免费时间段" prop="free_time">
                                    <el-date-picker
                                            v-model="addForm.free_time"
                                            placeholder=""
                                            type="daterange"
                                            range-separator="至"
                                            start-placeholder="开始日期"
                                            end-placeholder="结束日期"
                                            @change="getQueryDate2"
                                            clearable
                                            style="width: 100%"
                                    ></el-date-picker>
                                </el-form-item>
                            </el-col>


                            <el-col :span="12" v-show="whiteTime">
                                <el-form-item label="免费截止时间" prop="time_pro">
                                    <el-date-picker
                                            v-model="addForm.time_pro"
                                            @change="getQueryDate"
                                            type="date"
                                            style="width: 100%"
                                            placeholder="选择日期"
                                            clearable
                                    >
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12" v-show="cj_whiteStas">
                                <el-form-item label="上传照片" class="small-lineheight">
                                    <el-upload class="avatar-uploader" :action="fileurl" :show-file-list="false"
                                               :on-preview="handlePictureCardPreview2" :on-remove="handleRemove2"
                                               :on-success="handleSuccess2">
                                        <img v-if="imageUrl2" :src="imageUrl2" class="avatar">
                                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                                    </el-upload>

                                    <!--<el-upload-->
                                    <!--:action="fileurl"-->
                                    <!--list-type="picture-card"-->
                                    <!--limit="1"-->
                                    <!--:on-preview="handlePictureCardPreview2"-->
                                    <!--:on-remove="handleRemove2"-->
                                    <!--:on-success="handleSuccess2"-->
                                    <!--&gt;-->
                                    <!--<i class="el-icon-plus"></i>-->
                                    <!--</el-upload>-->
                                </el-form-item>
                            </el-col>

                        </el-row>
                    </el-col>
                </el-row>
            </el-form>
        </el-card>
        <!-- 车牌录入 end-->
        <div class="container mt20">
            <div class="handle-box">
                <!--<div class="left-panel">-->
                    <!--<el-button-->
                            <!--type="primary"-->
                            <!--size="small"-->
                            <!--icon="el-icon-plus"-->
                            <!--@click="batchAdd()"-->
                            <!--v-permission="['road_carnummana_pljrbmd', 'park_carnummana_pljrbmd']"-->
                    <!--&gt;批量加入白名单-->
                    <!--</el-button>-->
                    <!--<el-button-->
                            <!--type="primary"-->
                            <!--size="small"-->
                            <!--icon="el-icon-document"-->
                            <!--@click="excelImport()"-->
                            <!--v-permission="['road_carnummana_bmddr', 'park_carnummana_bmddr']"-->
                    <!--&gt;白名单Excel导入-->
                    <!--</el-button>-->
                    <!--&lt;!&ndash;<el-button&ndash;&gt;-->
                    <!--&lt;!&ndash;type="primary"&ndash;&gt;-->
                    <!--&lt;!&ndash;size="small"&ndash;&gt;-->
                    <!--&lt;!&ndash;icon="el-icon-plus"&ndash;&gt;-->
                    <!--&lt;!&ndash;@click="carAdd()"&ndash;&gt;-->
                    <!--&lt;!&ndash;v-permission="['road_carnummana_cplr', 'park_carnummana_cplr']"&ndash;&gt;-->
                    <!--&lt;!&ndash;&gt;车牌录入&ndash;&gt;-->
                    <!--&lt;!&ndash;</el-button>&ndash;&gt;-->
                <!--</div>-->
                <!--<div class="right-panel">-->
                    <el-form inline label-width="80" size="small" class="lineH0">
                        <el-form-item label="姓名" class="search-mb0">
                            <el-input
                                    @keyup.enter="handleSearch()"
                                    size="small"
                                    placeholder="姓名"
                                    v-model="query.name"
                                    class="w140"
                                    clearable
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="录入人" class="search-mb0">
                            <el-input
                                    @keyup.enter="handleSearch()"
                                    size="small"
                                    placeholder="录入人"
                                    v-model="query.userName"
                                    class="w140"
                                    clearable
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="公司名称" class="search-mb0">
                            <el-input
                                    @keyup.enter="handleSearch()"
                                    size="small"
                                    placeholder="公司名称"
                                    v-model="query.companyName"
                                    class="w140"
                                    clearable
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="车牌号" class="search-mb0">
                            <el-input
                                    @keyup.enter="handleSearch()"
                                    size="small"
                                    placeholder="车牌号"
                                    v-model="query.car_no"
                                    class="w140"
                                    clearable
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="" class="search-mb0">
                            <el-checkbox v-model="query.isCheck">黄牌</el-checkbox>
                        </el-form-item>

                        <el-form-item label="手机号" class="search-mb0">
                            <el-input
                                    @keyup.enter="handleSearch()"
                                    size="small"
                                    placeholder="车主绑定手机号"
                                    class="w140"
                                    v-model="query.phone"
                                    clearable
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="" class="search-mb0">
                            <el-select
                                    clearable
                                    v-model="query.roster_type"
                                    filterable
                                    size="small"
                                    class="w140"
                                    placeholder="选择名单类型"
                            >
                                <el-option key="99" label="白名单(待生效)" value="99"></el-option>
                                <el-option
                                        v-for="item in form.czroptions"
                                        :key="item.dc_value"
                                        :label="item.label"
                                        :value="item.dc_value"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="" class="search-mb0">
                            <el-select
                                    clearable
                                    v-model="query.whitelist_type"
                                    filterable
                                    size="small"
                                    class="w140"
                                    placeholder="选择白名单类型"
                            >
                                <el-option key="0" label="内部车" value="0"></el-option>
                                <el-option key="1" label="企业(免税)车" value="1"></el-option>
                                <el-option key="2" label="救护车" value="2"></el-option>
                            </el-select>
                        </el-form-item>


                        <el-form-item label="停车场" v-if="menuType == '1'">
                            <el-select
                                    clearable
                                    v-model="query.parkId"
                                    filterable
                                    size="small"
                                    placeholder="所有停车场"
                                    class="w100"
                            >
                                <el-option
                                        v-for="item in formqjl.roads"
                                        :key="item.id"
                                        :label="item.park_name"
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
                            >查询
                            </el-button>
                        </el-form-item>
                         <el-form-item label="" class="search-mb0">
                          <el-button
                            size="small"
                            type="success"
                            icon="el-icon-upload2"
                            @click="exportCar"
                            >导出
                          </el-button>
                        </el-form-item>
                    </el-form>
                <!--</div>-->
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
                <el-table-column type="index" label="序号" width="55" align="center">
                </el-table-column>

                <el-table-column
                        prop="name"
                        width="80"
                        align="center"
                        label="姓名"
                ></el-table-column>
                <el-table-column
                        prop="phone2"
                        width="110"
                        align="center"
                        label="手机号码"
                ></el-table-column>
                <el-table-column
                        prop="company_name"
                        width="100"
                        align="center"
                        label="公司名称"
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
                                class="tag-nums"
                                v-if="scope.row.car_type == '1'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="success"
                                class="tag-nums"
                                v-else-if="scope.row.car_type == '2'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                        <el-tag
                                size="small"
                                type="warning"
                                class="tag-nums"
                                v-else-if="scope.row.car_type == '3'"
                                v-text="scope.row.car_no"
                        ></el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="carTypeName"
                        label="车牌样式"
                        width="80"
                        align="center"
                ></el-table-column>
                <el-table-column
                        prop="member_id"
                        label="会员车牌"
                        width="80"
                        align="center"
                        :formatter="formatCarNo"
                ></el-table-column>
                <el-table-column
                        prop="bindName"
                        label="绑定账号"
                        align="center"
                        width="140"
                ></el-table-column>
                <el-table-column
                        prop="phone"
                        width="110"
                        align="center"
                        label="绑定手机号"
                ></el-table-column>
                <el-table-column
                        prop="bind_date"
                        label="绑定时间"
                        align="center"
                        width="170"
                        :formatter="dateFormat"
                ></el-table-column>
                <el-table-column
                        prop="rosterTypeName"
                        label="名单类型"
                        width="120"
                        align="center"
                ></el-table-column>

                <el-table-column
                        prop="cut_off_date"
                        label="企业(免税)车免费截止时间"
                        align="center"
                        width="120"
                >
                    <template #default="scope">
                        <p v-if="scope.row.whitelist_type==1">{{ scope.row.cut_off_date }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="free_time_start"
                        label="内部车免费开始时间"
                        align="center"
                        width="120"
                >
                    <template #default="scope">
                        <p v-if="scope.row.white_time_type==2">{{ scope.row.free_time_start }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="free_time_end"
                        label="内部车免费截止时间"
                        align="center"
                        width="120"
                >
                    <template #default="scope">
                        <p v-if="scope.row.white_time_type==2">{{ scope.row.free_time_end }}</p>
                    </template>
                </el-table-column>

                <el-table-column prop="parkNames" label="停车场" align="center" width="180">
                    <template #default="scope">
                        <div class="no-warp">
                            <el-tag
                                size="small"
                                v-for="(item, index) in scope.row.parkNames"
                                :key="index"
                                class="mar5"
                                @click="handleViewAddress(scope.$index, scope.row,0)"
                            >{{ item}}
                            </el-tag>
                        </div>

                    </template>

                </el-table-column>
                <el-table-column prop="roadNames" label="路内" align="center" width="180">
                    <template #default="scope">
                        <div class="no-warp">
                            <el-tag
                                size="small"
                                v-for="(item, index) in scope.row.roadNames"
                                :key="index"
                                class="mar5"
                                @click="handleViewAddress(scope.$index, scope.row,1)"
                            >{{ item}}
                            </el-tag>
                        </div>

                    </template>

                </el-table-column>
                <el-table-column
                        prop="userName"
                        label="录入人"
                        align="center"
                        width="120"
                ></el-table-column>
                <el-table-column
                        prop="reason"
                        label="理由说明"
                        align="center"
                        min-width="120"
                ></el-table-column>
                <el-table-column
                        v-if="isControl"
                        label="操作"
                        width="220"
                        align="center"
                        fixed="right"
                >
                    <template #default="scope">
                        <!--<el-button-->
                                <!--size="mini"-->
                                <!--type="text"-->
                                <!--icon="el-icon-circle-close"-->
                                <!--@click="bindRoad(scope.$index, scope.row)"-->
                                <!--v-permission="['road_carnummana_jb', 'park_carnummana_jb']"-->
                        <!--&gt;解绑-->
                        <!--</el-button>-->
                        <el-button
                                size="mini"
                                type="text"
                                icon="el-icon-edit"
                                @click="editCarno(scope.$index, scope.row)"
                                v-permission="['road_carnummana_edit', 'park_carnummana_edit']"
                        >编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="text"
                                class="red"
                                icon="el-icon-delete"
                                @click="delCarno(scope.$index, scope.row)"
                                v-permission="['road_carnummana_del', 'park_carnummana_del']"
                        >删除
                        </el-button>

                        <!--<el-dropdown class="ml10 blue">-->
              <!--<span class="el-dropdown-link" style="cursor: pointer">-->
                <!--更多操作<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
              <!--</span>-->
                            <!--<template v-slot:dropdown>-->
                                <!--<el-dropdown-menu>-->
                                    <!--<el-dropdown-item-->
                                            <!--icon="el-icon-circle-close"-->
                                            <!--@click="bindRoad(scope.$index, scope.row)"-->
                                            <!--v-permission="['road_carnummana_jb', 'park_carnummana_jb']"-->
                                    <!--&gt;解绑-->
                                    <!--</el-dropdown-item>-->
                                    <!--<el-dropdown-item-->
                                            <!--icon="el-icon-plus"-->
                                            <!--@click="handleCommand('1', scope.row)"-->
                                            <!--v-permission="['road_carnummana_ptmd', 'park_carnummana_ptmd']"-->
                                    <!--&gt;普通名单-->
                                    <!--</el-dropdown-item>-->
                                    <!--<el-dropdown-item-->
                                            <!--icon="el-icon-plus"-->
                                            <!--@click="handleCommand('2', scope.row)"-->
                                            <!--v-permission="['road_carnummana_jhmd', 'park_carnummana_jhmd']"-->
                                    <!--&gt;加黑名单-->
                                    <!--</el-dropdown-item>-->
                                    <!--<el-dropdown-item-->
                                            <!--icon="el-icon-plus"-->
                                            <!--@click="handleCommand('3', scope.row)"-->
                                            <!--v-permission="['road_carnummana_jbmd', 'park_carnummana_jbmd']"-->
                                    <!--&gt;加白名单-->
                                    <!--</el-dropdown-item>-->
                                    <!--<el-dropdown-item-->
                                            <!--icon="el-icon-plus"-->
                                            <!--@click="handleCommand('4', scope.row)"-->
                                            <!--v-permission="['road_carnummana_jcjrmd', 'park_carnummana_jcjrmd']"-->
                                    <!--&gt;加残疾人名单-->
                                    <!--</el-dropdown-item>-->
                                <!--</el-dropdown-menu>-->
                            <!--</template>-->
                        <!--</el-dropdown>-->
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
                <!--<div id="mapDiv"></div>-->
            </div>
        </div>

        <!--白名单模板下载上传-->
        <el-dialog
                width="650px"
                title="白名单模板"
                v-model="viewVisibleExcel"
                :close-on-click-modal="false"
        >
            <el-form label-width="110px" class="excel-form">
                <el-form-item label="白名单模板下载">
                    <el-button
                            type="success"
                            size="small"
                            icon="el-icon-download"
                            @click="download()"
                    >Excel 下载
                    </el-button>
                </el-form-item>
                <el-form-item label="白名单批量导入">
                    <input type="file" id="inputFile" @change="exportImport()" hidden/>
                    <el-button size="small" ref="inputFile" @click="inputFileClick()"
                    >Excel导入
                    </el-button
                    >
                </el-form-item>
            </el-form>
            <template #footer>
                <!-- <span class="dialog-footer">
                                <el-button @click="viewVisibleExcel = false">取 消</el-button>
                                <el-button type="primary" @click="viewVisibleExcel = false">确 定</el-button>
                            </span> -->
            </template>
        </el-dialog>
        <!-- 加入白名单弹出框 -->
        <el-dialog :title="msginfo.title" v-model="bmdVisible" width="638px">
            <div align="center" class="font16 mgb20">
                将车牌号<span class="red">【{{ msginfo.carNo }} 】</span
            >{{ msginfo.msg }}
            </div>
            <el-form label-width="90px">
                <el-form-item label="姓 名" v-show="whiteType || disableType">
                    <el-input
                            ref="focusName"
                            v-model="operateCarnoParams.name"
                            v-on:keydown.enter="focusPhoneInput"
                            placeholder="请输入姓名"
                    ></el-input>
                </el-form-item>
                <el-form-item label="手机号" v-show="whiteType || disableType">
                    <el-input
                            ref="focusPhone"
                            v-model="operateCarnoParams.phone"
                            v-on:keydown.enter="carInfoSure"
                            placeholder="请输入手机号"
                    ></el-input>
                </el-form-item>
                <el-form-item
                        label="白名单类型"
                        class="font16 mgb20"
                        v-show="whiteType"
                >
                    <!--                    <el-radio v-model="operateCarnoParams.whitelist_type" label="0" size="large" >内部车</el-radio>-->
                    <!--                    <el-radio v-model="operateCarnoParams.whitelist_type" label="1" size="large" >企业(免税)车</el-radio>-->

                    <el-radio v-model="operateCarnoParams.whitelist_type" label="0" border
                    >内部车
                    </el-radio
                    >
                    <el-radio v-model="operateCarnoParams.whitelist_type" label="1" border
                    >企业(免税)车
                    </el-radio
                    >
                    <el-radio v-model="operateCarnoParams.whitelist_type" label="2" border
                    >救护车
                    </el-radio
                    >
                    <!-- <el-radio v-model="operateCarnoParams.whitelist_type" label="3" border
                      >军用车</el-radio
                    > -->
                </el-form-item>

                <el-form-item label="企业名称" v-show="disableType">
                    <el-input
                            v-model="operateCarnoParams.company_name"
                            placeholder="请输入内容"
                    ></el-input>
                </el-form-item>

                <el-form-item
                        label="残疾证编码"
                        class="font16 mgb20"
                        v-show="disableType"
                >
                    <el-input
                            v-model="operateCarnoParams.deformity_cert"
                            placeholder="请输入内容"
                    ></el-input>
                </el-form-item>

                <el-form-item label="上传照片" v-show="disableType">
                    <el-upload
                            class="avatar-uploader"
                            :action="uploadUrl"
                            :show-file-list="false"
                            :on-preview="handlePictureCardPreview"
                            :on-remove="handleRemove"
                            :on-success="handleSuccess"
                    >
                        <img v-if="imageUrl" :src="imageUrl" class="avatar"/>
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <!-- <el-form-item  label="上传照片(最多一张)" class="font16 mgb20" v-show="disableType">
                            <el-upload
                                :action="fileurl"
                                list-type="picture-card"
                                limit="1"
                                ref="upload"
                                :on-preview="handlePictureCardPreview"
                                :on-remove="handleRemove"
                                :on-success="handleSuccess"
                            >
                             <i class="el-icon-plus"></i>
                             </el-upload>
                        </el-form-item> -->

                <el-form-item
                        label="企业名称"
                        v-show="operateCarnoParams.whitelist_type == 1 ? true : false"
                >
                    <el-input
                            v-model="operateCarnoParams.company_name"
                            placeholder="请输入内容"
                    ></el-input>
                </el-form-item>
                <el-form-item
                        label="免费截止日期"
                        v-show="operateCarnoParams.whitelist_type == 1 ? true : false"
                >
                    <el-date-picker
                            v-model="operateCarnoParams.cut_off_date"
                            type="date"
                            style="width: 100%"
                            placeholder="选择日期"
                    >
                    </el-date-picker>
                </el-form-item>

                <el-form-item label="免费类型" v-show="operateCarnoParams.whitelist_type == 0 && whiteType ? true : false">
                    <el-radio-group v-model="operateCarnoParams.white_time_type">
                        <el-radio label="1">永久</el-radio>
                        <el-radio label="2">期限</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="免费时间段"
                              v-show="operateCarnoParams.whitelist_type == 0 && operateCarnoParams.white_time_type == 2 ? true : false">
                    <el-date-picker
                            v-model="operateCarnoParams.free_time"
                            placeholder=""
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            @change="getQueryDate3"
                            clearable
                            style="width: 100%"
                    ></el-date-picker>
                </el-form-item>

                <el-form-item label="停车场" v-show="whiteType">
                    <el-select
                            v-model="operateCarnoParams.parkIds"
                            multiple
                            filterable
                            placeholder="请选择"
                            style="width: 100%"
                    >
                        <el-option
                                v-for="item in parks"
                                :key="item.id"
                                :label="item.park_name"
                                :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="路内" v-show="whiteType">
                    <el-select
                            v-model="operateCarnoParams.roadIds"
                            multiple
                            filterable
                            placeholder="请选择"
                            style="width: 100%"
                    >
                        <el-option
                                v-for="item in roads"
                                :key="item.id"
                                :label="item.road_name"
                                :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="理由说明">
                    <el-input
                            type="textarea"
                            v-model="operateCarnoParams.reason"
                            :rows="6"
                            placeholder="请说明理由"
                    ></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="bmdVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">提 交</el-button>
        </span>
            </template>
        </el-dialog>

        <!-- 批量加入白名单弹出框 -->
        <el-dialog title="批量加入白名单" v-model="batchVisible" width="638px">
            <el-dialog
                    width="650px"
                    top="28vh"
                    title="车牌人信息"
                    v-model="viewVisibleInner"
                    :close-on-click-modal="false"
                    :close-on-press-escape="false"
                    :show-close="false"
                    append-to-body
            >
                <el-form :inline="true" label-position="top" class="innerDg">
                    <el-form-item label="姓名">
                        <el-input
                                ref="focusName"
                                v-model="carName"
                                v-on:keydown.enter="focusPhoneInput"
                                placeholder="请输入姓名"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="手机号">
                        <el-input
                                ref="focusPhone"
                                v-model="carPhone"
                                v-on:keydown.enter="carInfoSure"
                                placeholder="请输入手机号"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="&nbsp;">
                        <el-button type="primary" @click="carInfoSure()">确 定</el-button>
                        <el-button @click="innerClose()">取 消</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
            <el-form label-width="90px" @submit.native.prevent>
                <el-form-item label="蓝牌">
                    <el-tag
                            :key="tag"
                            v-for="tag in blueTags"
                            closable
                            :disable-transitions="false"
                            @close="handleClose(tag)"
                    >
                        {{ tag }}
                    </el-tag>
                    <el-input
                            class="input-new-tag"
                            v-if="inputVisible"
                            v-model="inputValue"
                            ref="saveTagInput"
                            placeholder="车牌号"
                            size="small"
                            v-on:keydown.enter="handleInputConfirm"
                            @blur="handleInputConfirmBlur"
                    >
                    </el-input>
                    <el-button
                            v-else
                            class="button-new-tag"
                            size="small"
                            @click="showInput"
                    >+ 车牌号
                    </el-button
                    >
                </el-form-item>
                <el-form-item label="黄牌">
                    <el-tag
                            :key="tag"
                            v-for="tag in yellowTags"
                            type="warning"
                            closable
                            :disable-transitions="false"
                            @close="handleCloseY(tag)"
                    >
                        {{ tag }}
                    </el-tag>
                    <el-input
                            class="input-new-tag"
                            v-if="inputVisibleY"
                            v-model="inputValueY"
                            placeholder="车牌号"
                            ref="saveTagInputY"
                            size="small"
                            v-on:keydown.enter="handleInputConfirmY"
                            @blur="handleInputConfirmYBlur"
                    >
                    </el-input>
                    <el-button
                            v-else
                            class="button-new-tag"
                            size="small"
                            @click="showInputY"
                    >+ 车牌号
                    </el-button
                    >
                </el-form-item>
                <el-form-item label="绿牌">
                    <el-tag
                            :key="tag"
                            type="success"
                            v-for="tag in greenTags"
                            closable
                            :disable-transitions="false"
                            @close="handleCloseG(tag)"
                    >
                        {{ tag }}
                    </el-tag>
                    <el-input
                            class="input-new-tag"
                            v-if="inputVisibleG"
                            v-model="inputValueG"
                            ref="saveTagInputG"
                            placeholder="车牌号"
                            size="small"
                            v-on:keydown.enter="handleInputConfirmG"
                            @blur="handleInputConfirmGBlur"
                    >
                    </el-input>
                    <el-button
                            v-else
                            class="button-new-tag"
                            size="small"
                            @click="showInputG"
                    >+ 车牌号
                    </el-button
                    >
                </el-form-item>

                <el-form-item label="白名单类型" class="font16 mgb20">
                    <el-radio v-model="batchForm.eg1" label="0" border>内部车</el-radio>
                    <el-radio v-model="batchForm.eg1" label="1" border
                    >企业(免税)车
                    </el-radio
                    >
                    <el-radio v-model="batchForm.eg1" label="2" border>救护车</el-radio>
                    <!-- <el-radio v-model="batchForm.eg1" label="3" border>军用车</el-radio> -->
                </el-form-item>

                <el-form-item label="免费类型" v-show="batchForm.eg1 == 0 ? true : false">
                    <el-radio-group v-model="batchForm.white_time_type">
                        <el-radio label="1">永久</el-radio>
                        <el-radio label="2">期限</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="免费时间段"
                              v-show="batchForm.eg1 == 0 && batchForm.white_time_type == 2 ? true : false">
                    <el-date-picker
                            v-model="batchForm.free_time"
                            placeholder=""
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            @change="getQueryDate4"
                            clearable
                            style="width: 100%"
                    ></el-date-picker>
                </el-form-item>

                <el-form-item
                        label="企业名称"
                        v-show="batchForm.eg1 == 1 ? true : false"
                >
                    <el-input v-model="valueC" placeholder="请输入内容"></el-input>
                </el-form-item>
                <el-form-item
                        label="有效时间"
                        v-show="batchForm.eg1 == 1 ? true : false"
                >
                    <el-date-picker
                            v-model="timeC"
                            type="date"
                            style="width: 100%"
                            placeholder="选择日期"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="停车场">
                    <el-select
                    style="width: 100%"
                            v-model="batchForm.parkIds"
                            multiple
                            filterable
                            placeholder="请选择"
                    >
                        <el-option
                                v-for="item in parks"
                                :key="item.id"
                                :label="item.park_name"
                                :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="路内">
                    <el-select
                    style="width: 100%"
                            v-model="batchForm.roadIds"
                            multiple
                            filterable
                            placeholder="请选择"
                    >
                        <el-option
                                v-for="item in roads"
                                :key="item.id"
                                :label="item.road_name"
                                :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="理由说明">
                    <el-input
                            type="textarea"
                            v-model="batchForm.eg2"
                            :rows="6"
                            placeholder="请说明理由"
                    ></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="batchVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveBatch">提 交</el-button>
        </span>
            </template>
        </el-dialog>

        <!-- 详情弹出框 -->
        <el-dialog title="地址列表" v-model="viewVisibleAddress" @close="closeAddress" width="840px">
            <div class="index-bar-content">
                <el-scrollbar style="height: 100%" ref="scrollbar">
                    <div :id="key" class="main-list" v-for="(value, key) in indexData" :key="key" ref="listGroup">
                    <div class="title-key">{{ key }}</div>
                    <div class="content-container">
                        <div class="content-item" v-for="(val, index) in value" :key="index">
                            <el-tag size="small">{{ val.name }}</el-tag>

                        </div>
                    </div>
                    </div>
                </el-scrollbar>
                <!-- 右侧字母列表 -->
                <ul class="char-list">
                    <li
                    v-for="(item,index) in indexList"
                    :key="item.index"
                    @click="scrollToLetter(item)"
                    :class="{'active':currentIndex === item.id}"
                    >
                    {{ item.key }}
                    <!-- {{ item.id }}
                    {{currentIndex}} -->
                    </li>

                </ul>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {ref, reactive, nextTick} from "vue";
    import {ElMessage, ElMessageBox,ElScrollbar} from "element-plus";
    import {
        operateCarnoList,
        editOperateCarno,
        getDictsByType,
        relieveBind,
        viewOperateCarno,
        delOperateCarno,
        downloadWhite,
        saveBeathWhiteCarNo,
        exportCarNo,
    } from "../../api/operateCar.js";
    import {useRouter} from "vue-router";
    import axios from "axios";
    import File_URL from "../../file_url";
    import pinyin from "js-pinyin"
    import {
        dictData, queryAreaData,
        queryRoadData,
        queryParkData,
        queryStreetData,
    } from "../../api/index";
    import {operatecarnoSaveData, getOneTraffic} from "../../api/car";

    export default {
        name: "carnummana",
        components: {ElScrollbar},
        data() {
            // 车牌录入 start
            var checkName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error("必填项"));
                } else {
                    var regEn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
                    var regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;
                    if (regEn.test(value) == true || regCn.test(value) == true) {
                        return callback(new Error("车牌号码不能包含特殊字符"));
                    } else {
                        callback();
                    }
                }
            };
            // 车牌录入 end
            return {
                uploadUrl: File_URL.file_img_url, //图片上传路径
                imgViewUrl: File_URL.file_hx_img_url, //图片回显路径
                tableH: 0,
                // 车牌录入 start
                isDisabl: false,
                whiteStas: false,
                whiteTime: false,
                whiteTime2: false,
                whiteTime3: false,
                whiteStasName: false,
                cj_whiteStas: false,
                fileurl: File_URL.file_img_url,
                // 表单验证
                formRules: {
                    // car_no: [{validator: checkName, required: true, trigger: "blur"}],
                    // car_natures: [{ required: true, message: "请选择", trigger: "blur" }],
                    // name: [{required: true, message: "必填项", trigger: "blur"}],
                    // phone: [{required: true, message: "必填项", trigger: "blur"}],
                    // deformity_cert: [
                    //     {required: true, message: "必填项", trigger: "blur"},
                    // ],
                    // company_name: [{required: true, message: "必填项", trigger: "blur"}],
                    // time_pro: [{required: true, message: "请选择免费截止时间", trigger: "change"}],
                    // free_time: [{required: true, message: "请选择免费时间段", trigger: "change"}],
                },
                // 车牌录入 end
            };
        },
        setup() {
            const menuType = localStorage.getItem("menuType");

            // 车牌录入 start
            const addForm = reactive({
                id: "",
                car_no: "",
                car_type: "1",
                roster_type: "1",
                // car_natures: "",
                whitelist_type: "0",
                deformity_cert: "",
                deformity_picture_id: "",
                company_name: "",
                name: "",
                phone: "",
                cut_off_date: "",
                parkIds: [],
                roadIds: [],
                time_pro: "",
                free_time_start: "",
                free_time_end: "",
                free_time: [],
                white_time_type: "1",
            });
            const carTypeData = ref({});
            const rosterTypeData = ref({});
            const carNaturesData = ref({});
            const whitelistTypeData = ref({});
            const fileIds2 = ref([]);
            const file_img2 = ref([]);
            // let timeData = ref({
            //   time_pro: "",
            // });
            const router2 = useRouter();
            const getData2 = () => {
                const id = router2.currentRoute.value.query.id;
                if (id == null || id == "") {
                } else {
                    getOneTraffic(id).then((res) => {
                        console.log(res.data.car_no);
                        addForm.car_no = res.data.car_no;
                        addForm.roster_type = res.data.roster_type;
                    });
                }
            };
            getData2();

            //日期控件change方法 选择免费截止时间
            const getQueryDate = () => {
                if (null == addForm.value.time_pro || [] == addForm.value.time_pro || "" == addForm.value.time_pro) {
                    addForm.cut_off_date = "";
                } else {
                    addForm.cut_off_date = dateFormat2(addForm.value.time_pro);
                }
            };
            //日期格式化
            const dateFormat2 = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
            };

            //日期控件change方法 选择免费时间段 车牌录入
            const getQueryDate2 = () => {
                if (null == addForm.free_time || [] == addForm.free_time || "" == addForm.free_time) {
                    addForm.free_time_start = "";
                    addForm.free_time_end = "";
                } else {
                    console.log(dateFormat2(addForm.free_time[0]));
                    addForm.free_time_start = dateFormat2(addForm.free_time[0]);
                    addForm.free_time_end = dateFormat2(addForm.free_time[1]);
                }
            };

            //日期控件change方法 选择免费时间段 变更白名单
            const getQueryDate3 = () => {
                if (null == operateCarnoParams.free_time || [] == operateCarnoParams.free_time || "" == operateCarnoParams.free_time) {
                    operateCarnoParams.free_time_start = "";
                    operateCarnoParams.free_time_end = "";
                } else {
                    console.log(dateFormat2(operateCarnoParams.free_time[0]));
                    operateCarnoParams.free_time_start = dateFormat2(operateCarnoParams.free_time[0]);
                    operateCarnoParams.free_time_end = dateFormat2(operateCarnoParams.free_time[1]);
                }
            };

            //日期控件change方法 选择免费时间段 批量加入白名单
            const getQueryDate4 = () => {
                if (null == batchForm.free_time || [] == batchForm.free_time || "" == batchForm.free_time) {
                    batchForm.free_time_start = "";
                    batchForm.free_time_end = "";
                } else {
                    console.log(dateFormat2(batchForm.free_time[0]));
                    batchForm.free_time_start = dateFormat2(batchForm.free_time[0]);
                    batchForm.free_time_end = dateFormat2(batchForm.free_time[1]);
                }
            };

            //获取当前车牌样式
            dictData(reactive({dict_type: "car_type"})).then((res) => {
                carTypeData.value = res.data;
            });
            //名单类型
            dictData(reactive({dict_type: "rosterType"})).then((res) => {
                rosterTypeData.value = res.data;
            });
            // 车辆性质
            dictData(reactive({dict_type: "carNatures"})).then((res) => {
                carNaturesData.value = res.data;
            });
            //白名单类型
            dictData(reactive({dict_type: "whitelist_type"})).then((res) => {
                whitelistTypeData.value = res.data;
            });

            // 车牌录入 end

            const query = reactive({
                phone: "",
                isCheck: false,
                car_type: "",
                car_no: "",
                roster_type: "",
                whitelist_type: "",
                pageIndex: 1,
                pageSize: 10,
                parkId: "",
                roadId: "",
                street_id: "",
                area_id: "",
                companyName: "",
            });

            const file_img = ref([]);

            const viewVisibleExcel = ref(false);
            const excelImport = () => {
                viewVisibleExcel.value = true;
            };
            const valueC = ref("");
            const timeC = ref("");
            const tableData = ref([]);
            const pageTotal = ref(0);
            const isControl = ref(true);
            var user_type = localStorage.getItem("user_type");
            if (user_type == 1) {
                isControl.value = false;
            }
            const router = useRouter();

            const parks = ref([]);
            //获取停车场下拉框数据
            const getPark = () => {
                queryParkData().then((res) => {
                    parks.value = res.data;
                });
            };
            getPark();

            //获取路内下拉框数据
            const roads = ref([]);
            const getRoads = () => {
                queryRoadData().then((res) => {
                    roads.value = res.data;
                });
            };
            getRoads();
            // 获取表格数据
            const getData = () => {
                if (query.isCheck) {
                    query.car_type = "3";
                } else {
                    query.car_type = "";
                }
                operateCarnoList(query).then((res) => {
                    tableData.value = res.data.list;
                    pageTotal.value = res.data.total;
                });
            };

            getData();
            const exportCar = () => {
                ElMessage.success("正在下载中·····");
                if (query.isCheck) {
                    query.car_type = "3";
                } else {
                    query.car_type = "";
                }
                exportCarNo(query).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "车牌管理.xls");
                    document.body.appendChild(link);
                    link.click();
                });
            };
            const formatCarNo = (row) => {
                if (row.member_id != null && row.member_id) {
                    return "是";
                } else {
                    return "否";
                }
            };
            const dateFormat = (row, column) => {
                var date = row.bind_date;
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
                    .catch(() => {
                    });
            };
            const dialogT = "编辑";

            // 批量加入白名单
            const batchVisible = ref(false);
            let tagsType = ref([]);
            let blueTags = ref([]);
            let blueTagsValue = ref([]);
            let yellowTags = ref([]);
            let yellowTagsValue = ref([]);
            let greenTags = ref([]);
            let greenTagsValue = ref([]);
            let inputVisible = ref(false);
            let inputValue = ref("");
            let inputVisibleY = ref(false);
            let inputValueY = ref("");
            let inputVisibleG = ref(false);
            let inputValueG = ref("");
            let saveTagInputY = ref(null);
            let saveTagInput = ref(null);
            let saveTagInputG = ref(null);
            // 添加姓名手机号--start
            const viewVisibleInner = ref(false);
            const carName = ref("");
            const carPhone = ref("");
            let focusName = ref(null);
            let focusPhone = ref(null);
            let tags = ref("");
            const focusPhoneInput = () => {
                nextTick((_) => {
                    focusPhone.value.input.focus();
                });
            };
            const carInfoSure = () => {
                if (carName.value && carPhone.value) {
                    viewVisibleInner.value = false;
                    if (tagsType.value == "blueTags") {
                        blueTagsValue.value.push(
                            blueTags.value + "-" + carName.value + "-" + carPhone.value
                        );
                    } else if (tagsType.value == "yellowTags") {
                        yellowTagsValue.value.push(
                            yellowTags.value + "-" + carName.value + "-" + carPhone.value
                        );
                    } else if (tagsType.value == "greenTags") {
                        greenTagsValue.value.push(
                            greenTags.value + "-" + carName.value + "-" + carPhone.value
                        );
                    }
                } else {
                    ElMessage({
                        showClose: true,
                        message: "姓名和手机号不能为空！",
                        type: "error",
                    });
                }
            };
            const innerClose = () => {
                viewVisibleInner.value = false;
                tags.value.pop();
                ElMessage({
                    showClose: true,
                    message: "取消添加车牌信息！",
                    type: "error",
                });
            };
            // 添加姓名手机号--end

            let batchForm = reactive({
                eg1: "",
                eg2: "",
                free_time_start: "",
                free_time_end: "",
                free_time: [],
                white_time_type: "1",
            });

            const batchAdd = () => {
                batchVisible.value = true;
                yellowTags.value = [];
                blueTags.value = [];
                greenTags.value = [];
                batchForm.eg1 = "0";
                batchForm.eg2 = "";
                (batchForm.parkIds = []),
                    (batchForm.roadIds = []),
                    (inputVisible.value = false);
                inputVisibleY.value = false;
                inputVisibleG.value = false;
                valueC.value = "";
                timeC.value = "";
                blueTagsValue.value = [];
                yellowTagsValue.value = [];
                greenTagsValue.value = [];

                batchForm.free_time_start = "";
                batchForm.free_time_end = "";
                batchForm.free_time = [];
                batchForm.white_time_type = "1";

                getPark();
            };

            const download = () => {
                downloadWhite(reactive({})).then((res) => {
                    const url = window.URL.createObjectURL(new Blob([res]));
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", "白名单登记表.xlsx");
                    document.body.appendChild(link);
                    link.click();
                });
            };

            let file = reactive({
                excel: "",
            });

            //模拟上传按钮点击
            const inputFileClick = () => {
                var e = document.createEvent("MouseEvents");
                e.initEvent("click", true, true);
                document.getElementById("inputFile").dispatchEvent(e);
            };

            //导入excel
            const exportImport = () => {
                var formData = new FormData();
                formData.append("file", document.getElementById("inputFile").files[0]);
                axios({
                    url: File_URL.file_down + "operatecarno/importWhiteList",
                    method: "POST",
                    data: formData,
                    headers: {
                        "Content-Type": "multipart/form-data",
                        Authorization: localStorage.getItem("token_value"),
                    },
                }).then((res) => {
                    var data = res.data;
                    if (data.code == 0) {
                        ElMessage({
                            showClose: true,
                            message: data.data,
                            type: "success",
                        });

                        viewVisibleExcel.value = false;
                        getData();
                    } else {
                        ElMessage({
                            showClose: true,
                            message: data.msg,
                            type: "error",
                        });
                    }
                    //清空
                    document.getElementById("inputFile").value = "";
                });
            };

            //blue
            const handleClose = (tag) => {
                blueTags.value.splice(blueTags.value.indexOf(tag), 1);
                if (tagsType.value == "blueTags") {
                    blueTagsValue.value.splice(blueTagsValue.value.indexOf(tag), 1);
                } else if (tagsType.value == "yellowTags") {
                    yellowTagsValue.value.splice(yellowTagsValue.value.indexOf(tag), 1);
                } else if (tagsType.value == "greenTags") {
                    greenTagsValue.value.splice(greenTagsValue.value.indexOf(tag), 1);
                }
            };

            const showInput = () => {
                inputVisible.value = true;
                nextTick((_) => {
                    saveTagInput.value.input.focus();
                });
            };

            const handleInputConfirm = () => {
                tagsType.value = "blueTags";
                //let inputValue = inputValue.value;
                if (inputValue.value) {
                    blueTags.value.push(inputValue.value);
                }
                inputVisible.value = true;
                inputValue.value = "";
                viewVisibleInner.value = true; //填写姓名手机弹窗
                carName.value = ""; //清空姓名手机号
                carPhone.value = ""; //清空手机号
                tags = blueTags;
                setTimeout(() => {
                    nextTick((_) => {
                        focusName.value.input.focus();
                    });
                }, 0);
            };
            const handleInputConfirmBlur = () => {
                //let inputValue = inputValue.value;
                tagsType.value = "blueTags";
                if (inputValue.value) {
                    blueTags.value.push(inputValue.value);
                    inputVisible.value = true;
                    inputValue.value = "";
                    viewVisibleInner.value = true; //填写姓名手机弹窗
                    carName.value = ""; //清空姓名手机号
                    carPhone.value = ""; //清空手机号
                    tags = blueTags;
                    setTimeout(() => {
                        nextTick((_) => {
                            focusName.value.input.focus();
                        });
                    }, 0);
                } else {
                    return false;
                }
            };
            // yellow
            const handleCloseY = (tag) => {
                yellowTags.value.splice(yellowTags.value.indexOf(tag), 1);
            };

            const showInputY = () => {
                inputVisibleY.value = true;
                nextTick((_) => {
                    saveTagInputY.value.input.focus();
                });
            };

            const handleInputConfirmY = () => {
                tagsType.value = "yellowTags";
                let inputValue = inputValueY.value;
                if (inputValue) {
                    yellowTags.value.push(inputValue);
                }
                inputVisibleY.value = true;
                inputValueY.value = "";
                viewVisibleInner.value = true; //填写姓名手机弹窗
                carName.value = ""; //清空姓名手机号
                carPhone.value = ""; //清空手机号
                tags = yellowTags;
                setTimeout(() => {
                    nextTick((_) => {
                        focusName.value.input.focus();
                    });
                }, 0);
            };
            const handleInputConfirmYBlur = () => {
                tagsType.value = "yellowTags";
                let inputValue = inputValueY.value;
                if (inputValue != "") {
                    yellowTags.value.push(inputValue);
                    inputVisibleY.value = true;
                    inputValueY.value = "";
                    viewVisibleInner.value = true; //填写姓名手机弹窗
                    carName.value = ""; //清空姓名手机号
                    carPhone.value = ""; //清空手机号
                    tags = yellowTags;
                    setTimeout(() => {
                        nextTick((_) => {
                            focusName.value.input.focus();
                        });
                    }, 0);
                } else {
                    return false;
                }
            };
            // Green
            const handleCloseG = (tag) => {
                greenTags.value.splice(greenTags.value.indexOf(tag), 1);
            };

            const showInputG = () => {
                inputVisibleG.value = true;
                nextTick((_) => {
                    saveTagInputG.value.input.focus();
                });
            };

            const handleInputConfirmG = () => {
                tagsType.value = "greenTags";
                let inputValue = inputValueG.value;
                if (inputValue) {
                    greenTags.value.push(inputValue);
                }
                inputVisibleG.value = true;
                inputValueG.value = "";
                viewVisibleInner.value = true; //填写姓名手机弹窗
                carName.value = ""; //清空姓名手机号
                carPhone.value = ""; //清空手机号
                tags = greenTags;
                setTimeout(() => {
                    nextTick((_) => {
                        focusName.value.input.focus();
                    });
                }, 0);
            };
            const handleInputConfirmGBlur = () => {
                tagsType.value = "greenTags";
                let inputValue = inputValueG.value;
                if (inputValue != "") {
                    greenTags.value.push(inputValue);
                    inputVisibleG.value = true;
                    inputValueG.value = "";
                    viewVisibleInner.value = true; //填写姓名手机弹窗
                    carName.value = ""; //清空姓名手机号
                    carPhone.value = ""; //清空手机号
                    tags = greenTags;
                    setTimeout(() => {
                        nextTick((_) => {
                            focusName.value.input.focus();
                        });
                    }, 0);
                } else {
                    return false;
                }
            };

            // 表格编辑时弹窗和保存
            const editVisible = ref(false);
            const viewVisible = ref(false);
            const bmdVisible = ref(false);
            let form = reactive({
                name: "",
                address: "",
                czroptions: [],
            });
            let idx = -1;
            const handleEdit = (index, row, type) => {
                router.push("/roadedit");
            };

            const handleView = (index, row) => {
                idx = index;
                Object.keys(form).forEach((item) => {
                    form[item] = row[item];
                });
                viewVisible.value = true;
            };
            const getDicts = () => {
                getDictsByType({type: "rosterType"}).then((res) => {
                    console.log("名单类型", res)
                    form.czroptions = res.data;
                });
            };
            getDicts();
            // 编辑 名单修改
            let operateCarnoParams = reactive({
                id: "",
                roster_type: "",
                whitelist_type: "0",
                reason: "",
                name: "",
                phone: "",
                company_name: "",
                cut_off_date: "",
                deformity_cert: "",
                deformity_picture_id: "",
                parkIds: [],
                roadIds: [],
                white_time_type: "1",
                free_time_start: "",
                free_time_end: "",
                free_time: [],
            });
            const whiteType = ref(false);
            const disableType = ref(false);

            const msginfo = reactive({
                title: "",
                carNo: "",
                msg: "",
            });
            let upload = ref(null);
            const handleCommand = (type, row) => {
                nextTick((_) => {
                    upload.value.clearFiles();
                });
                operateCarnoParams.id = row.id;
                operateCarnoParams.roster_type = type;
                msginfo.carNo = row.car_no;
                operateCarnoParams.whitelist_type = "0";
                operateCarnoParams.reason = "";
                operateCarnoParams.name = "";
                operateCarnoParams.phone = "";
                operateCarnoParams.company_name = "";
                operateCarnoParams.cut_off_date = "";
                operateCarnoParams.deformity_cert = "";
                operateCarnoParams.parkIds = [];
                operateCarnoParams.roadIds = [];
                operateCarnoParams.white_time_type = "1";
                operateCarnoParams.free_time_start = "";
                operateCarnoParams.free_time_end = "";
                operateCarnoParams.free_time = [];

                fileList2.value = []; //图片上传列表 初始化
                imageUrl.value = ""; //图片回显路径 初始化
                imageId.value = ""; //图片id 初始化
                fileIds.value = []; //图片上传ids 初始化

                if ("3" == type) {
                    whiteType.value = true;
                    disableType.value = false;
                    operateCarnoParams.whitelist_type = row.whitelist_type;
                    msginfo.title = "加入白名单";
                    msginfo.msg = "加入白名单，停车将不会再产生费用";
                } else if ("4" == type) {
                    whiteType.value = false;
                    disableType.value = true;
                    msginfo.title = "加入残疾人名单";
                    msginfo.msg = "加入残疾人名单";
                } else {
                    whiteType.value = false;
                    disableType.value = false;
                    switch (type) {
                        case "1":
                            msginfo.title = "加入普通名单";
                            msginfo.msg = "恢复为普通名单";
                            break;
                        case "2":
                            msginfo.title = "加入黑名单";
                            msginfo.msg = "加入黑名单";
                            break;
                    }
                }
                bmdVisible.value = true;
            };

            //日期格式化
            const dateToFormat = (time) => {
                return `${time.getFullYear()}-${
                    time.getMonth() + 1 >= 10
                        ? time.getMonth() + 1
                        : "0" + (time.getMonth() + 1)
                    }-${time.getDate() >= 10 ? time.getDate() : "0" + time.getDate()}`;
            };
            const saveEdit = () => {
                // whitelist_type 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                // white_time_type 白名单免费类型 1永久 2期限
                if (operateCarnoParams.whitelist_type == "0" && operateCarnoParams.white_time_type == "2") {
                    if ("" == operateCarnoParams.free_time_start && "" == operateCarnoParams.free_time_end) {
                        ElMessage.error("请选择免费时间段");
                        return false;
                    }
                }

                // 变更白名单
                ElMessageBox.confirm("确定变更名单类型吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {

                        if (operateCarnoParams.cut_off_date != "") {
                            operateCarnoParams.cut_off_date = dateToFormat(
                                operateCarnoParams.cut_off_date
                            );
                        }
                        editOperateCarno(operateCarnoParams).then((res) => {
                            if (res.code == 0) {
                                ElMessage.success("操作成功！");
                                bmdVisible.value = false;
                                getData();
                            } else {
                                ElMessage.error(res.msg);
                            }
                        });
                    })
                    .catch(() => {
                    });
            };

            // 解绑
            const bindRoad = (index, row) => {
                ElMessageBox.confirm("确定解绑该车牌吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        let edit_OperateCarno = reactive({
                            carnoId: row.id,
                        });
                        relieveBind(edit_OperateCarno).then((res) => {
                            if (res.code == 0) {
                                ElMessage.success("解绑成功");
                                getData();
                            } else {
                                ElMessage.error(res.msg);
                            }
                        });
                    })
                    .then(() => {
                        getData();
                    })
                    .catch(() => {
                    });
            };

            // 删除
            const delCarno = (index, row) => {
                ElMessageBox.confirm("确定删除该车牌吗？", "提示", {
                    type: "warning",
                })
                    .then(() => {
                        let edit_OperateCarno = reactive({
                            id: row.id,
                        });
                        delOperateCarno(edit_OperateCarno).then((res) => {
                            if (res.code == 0) {
                                ElMessage.success("删除成功");
                                getData();
                            } else {
                                ElMessage.error(res.msg);
                            }
                        });
                    })
                    .then(() => {
                        getData();
                    })
                    .catch(() => {
                    });
            };

            const fileIds = ref([]);
            const fileList2 = ref([]);
            const imageUrl = ref("");
            const imageId = ref("");

            const whiteStas = ref(false);
            const whiteTime = ref(false);
            const whiteTime2 = ref(false);
            const whiteTime3 = ref(false);
            const whiteStasName = ref(false);
            const cj_whiteStas = ref(false);

            // 编辑数据回显
            const editCarno = (index, row) => {
                let edit_OperateCarno = reactive({
                    id: row.id,
                });
                viewOperateCarno(edit_OperateCarno).then((res) => {
                    if (res.code == 0) {
                        Reflect.ownKeys(addForm).forEach(key => {
                            Reflect.set(addForm, key, res.data[key])
                        });

                        // 名单类型 roster_type
                        // addForm.whitelist_type = "0";
                        // addForm.white_time_type = "1";
                        if (res.data.roster_type == "3") {
                            // addForm.name = "";
                            // addForm.deformity_cert = "";
                            // addForm.cut_off_date = "";
                            // addForm.time_pro = "";
                            // addForm.free_time = [];
                            // addForm.free_time_start = "";
                            // addForm.free_time_end = "";
                            // fileIds = []; //图片上传ids 初始化
                            whiteStas.value = true;
                            whiteTime2.value = false;
                            whiteTime3.value = true;
                            whiteStasName.value = true;
                            cj_whiteStas.value = false;

                            // whitelist_type 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                            if (res.data.whitelist_type == "1") {
                                whiteTime.value = true;//企业白名单 免费截止日期
                                whiteTime2.value = false;//内部车白名单 免费日期段
                                whiteTime3.value = false;////内部车白名单 免费类型

                                //免费截止日期
                                addForm.time_pro = addForm.cut_off_date;
                            } else {
                                if (res.data.whitelist_type == "0") {
                                    whiteTime3.value = true;
                                } else {
                                    whiteTime3.value = false;
                                }
                                whiteTime.value = false;
                                whiteTime2.value = false;

                                // 白名单 内部车 免费类型 1永久 2期限 white_time_type
                                if (res.data.white_time_type == "1") {
                                    whiteTime2.value = false;
                                } else if (res.data.white_time_type == "2") {
                                    whiteTime2.value = true;
                                    addForm.free_time = [addForm.free_time_start, addForm.free_time_end];
                                }
                            }
                        } else if (res.data.roster_type == "4") {
                            // addForm.name = "";
                            // addForm.phone = "";
                            // addForm.company_name = "";
                            whiteStas.value = false;
                            whiteStasName.value = true;
                            cj_whiteStas.value = true;
                            whiteTime.value = false;
                            whiteTime2.value = false;
                            whiteTime3.value = false;
                        } else {
                            whiteStas.value = false;
                            whiteStasName.value = false;
                            cj_whiteStas.value = false;
                            whiteTime.value = false;
                            whiteTime3.value = false;
                        }


                    } else {
                        ElMessage.error(res.msg);
                    }
                });
            };

            //批量添加白名单
            const saveBatch = (index) => {
                if (null == blueTagsValue.value) {
                    blueTagsValue = blueTags.value;
                    yellowTagsValue = yellowTags.value;
                    greenTagsValue = greenTags.value;
                }
                if (
                    "" == blueTags.value &&
                    "" == yellowTags.value &&
                    "" == greenTags.value
                ) {
                    ElMessage({
                        showClose: true,
                        message: "车牌、姓名、手机号不能为空！",
                        type: "error",
                    });
                } else {
                    // whitelist_type 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                    // white_time_type 白名单免费类型 1永久 2期限
                    console.log("batchForm",batchForm)
                    if (batchForm.eg1 == "0" && batchForm.white_time_type == "2") {
                        if ("" == batchForm.free_time_start && "" == batchForm.free_time_end) {
                            ElMessage.error("请选择免费时间段");
                            return false;
                        }
                    }
                    let bindInfoParams = reactive({
                        blueCars: blueTagsValue.value,
                        yellowCars: yellowTagsValue.value,
                        greenCars: greenTagsValue.value,
                        whitelist_type: batchForm.eg1,
                        company_name: valueC,
                        cut_off_date: timeC.value,
                        reason: batchForm.eg2,
                        roadIds: batchForm.roadIds,
                        parkIds: batchForm.parkIds,
                        white_time_type: batchForm.white_time_type,
                        free_time_start: batchForm.free_time_start,
                        free_time_end: batchForm.free_time_end,
                    });
                    console.log(bindInfoParams);

                    saveBeathWhiteCarNo(bindInfoParams).then((res) => {
                        console.log(res);
                        if (res.code == 0) {
                            ElMessage({
                                showClose: true,
                                message: "新增成功！",
                                type: "success",
                            });
                            getData();
                            batchVisible.value = false;
                        } else {
                            ElMessage({
                                showClose: true,
                                message: res.msg,
                                type: "error",
                            });
                            batchVisible.value = true;
                        }
                    });
                }
            };

            // const verifyShow = (row) => {
            //     if (row.member_id != null && row.member_id) {
            //         return true;
            //     } else {
            //         return false;
            //     }
            //
            //     // console.info(memberId);
            //     // if(memberId != null){
            //     //     return true;
            //     // }else{
            //     //     return false;
            //     // }
            // };


            let formqjl = reactive({
                areaId: "",
                streetId: "",
                roadId: "",
                areas: [],
                streets: [],
                roads: [],
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
                query.roadId = "";
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
                query.roadId = "";
                queryRoadData(queryRoad).then((res) => {
                    formqjl.roads = res.data;
                });
            };
            //获取停车场下拉框数据
            const getParks = () => {
                queryRoad.streetId = query.street_id;
                query.parkId = "";
                queryParkData(queryRoad).then((res) => {
                    formqjl.roads = res.data;
                });
            };

            const dialogVisible = ref(false);
            const imageUrl2 = ref("");
            const imageId2 = ref("");

            // 详情地址
            let listGroup = ref(null);
            let scrollbar = ref(null);
            let charList = ref([]);
            let listHeight = ref([]);
            let currentIndex = ref(0);
            let indexPage = ref(1);
            let indexLimit = ref(24);
            let indexData = ref({});

            const viewVisibleAddress = ref(false);
            const handleViewAddress = (index,row,type) => {
                nextTick(() => {
                    calculateHeight()
                    handleScroll()
                });

                // 循环遍历字符串 并生成新的数组 键值对 为 name：road_name
                let roadsList = [];

                if(type == 0){
                    let parkNames = row.parkNames.toString().split(',');
                    parkNames.forEach(function (item) {
                        roadsList.push({
                            name: item
                        })
                    })
                }else{
                    let roadNames = row.roadNames.toString().split(',');
                    roadNames.forEach(function (item) {
                        roadsList.push({
                            name: item
                        })
                    })
                }

                pinyin.setOptions({ checkPolyphone: false, charCase: 0 })
                    let alphabet = []
                    let _charList = []
                    for (let i = 0; i < roadsList.length; i++) {
                        // 获取原数组每一项的 name 值
                        let name = roadsList[i].name
                        // 获取每一个name值第一个字的大写首字母（传入的 name 是中文时默认得到大写字母，name 是英文时按照原字符串输出，可能是小写）
                        let initial = pinyin.getCamelChars(name).substring(0, 1).toUpperCase()
                        // 给数组每一项增加名为 initial 的 key，值就是第一个字的大写首字母
                        roadsList[i].initial = initial
                        // 获取用于索引的字母
                        if (alphabet.indexOf(initial) === -1) {
                        alphabet.push(initial)
                        }
                    }
                    // 按字母表顺序排序
                    alphabet.sort()

                    // 给每个字母增加唯一标识，后面定位时会用到
                    for (var i = 0; i < alphabet.length; i++) {
                        _charList.push({
                        id: i,
                        key: alphabet[i]
                        })
                    }



                    // 生成 charList.value 为id 从0开始， key 从A-Z  的数组
                    charList.value = []
                    for (var i = 0; i < 26; i++) {
                        charList.value.push({
                        id:'',
                        key: String.fromCharCode(65 + i)
                        })
                    }

                    // 对比charList.value 和 _charList，如果_charList中的key值和charList.value中的key值相同，则将_charList中的id值赋值给charList.value 中的id值
                    for (var i = 0; i < _charList.length; i++) {
                        for (var j = 0; j < charList.value.length; j++) {
                        if (_charList[i].key === charList.value[j].key) {
                            charList.value[j].id = _charList[i].id
                        }
                        }
                    }

                    let resultData = {}
                    // 将源数据按照首字母分类
                    for (let i = 0; i < alphabet.length; i++) {
                        resultData[alphabet[i]] = roadsList.filter((item) => {
                        return item.initial === alphabet[i]
                        })
                    }
                    // 得到最终结果
                    indexData.value = resultData

                    // getDataList(sourdata);

                    viewVisibleAddress.value = true;
                };

                const calculateHeight = () => {
                    listHeight = []
                    nextTick(() => {
                        const list = listGroup.value
                        let height = 0
                        listHeight.push(height)
                        if (list) {
                            for (let i = 0; i < list.length; i++) {
                                let item = list[i]
                                height += item.clientHeight
                                listHeight.push(height)
                            }
                        }

                    })

                };
                const scrollToLetter = (item) => {
                    // 判断item.id是否为空，为空则不执行
                    if (item.id === '' || item.id === undefined) {
                        return
                    }
                    nextTick(() => {
                        scrollbar.value.$el.querySelector('.el-scrollbar__wrap').scrollTop = listHeight[item.id]
                        currentIndex.value = item.id
                    })

                };
                const handleScroll = () => {
                    nextTick(() => {
                        const scrollEle = scrollbar.value.$el.querySelector('.el-scrollbar__wrap');
                        scrollEle.onscroll = () => {
                            let newY = scrollEle.scrollTop
                            const listHeightSet = listHeight
                            // 在中间部分滚动
                            for (let i = 0; i < listHeightSet.length - 1; i++) {
                            let height1 = listHeightSet[i]
                            let height2 = listHeightSet[i + 1]
                            if (height1 <= newY && newY < height2) {
                                currentIndex.value = i
                                // console.log(this.currentIndex, "currentIndex")
                                let currentPage = Math.floor(i / indexLimit) + 1
                                indexPage = currentPage
                                return
                            }
                            }
                        }

                    })

                };

                const closeAddress = () =>{
                    scrollbar.value.$el.querySelector('.el-scrollbar__wrap').scrollTop = 0
                    currentIndex.value = 0
                    viewVisibleAddress.value = false;
                };


            return {
                exportCar,
                dateToFormat,
                fileIds,
                fileList2,
                imageUrl,
                imageUrl2,
                imageId,
                imageId2,
                file_img,
                fileurl: File_URL.file_img_url,
                query,
                upload,
                tableData,
                pageTotal,
                isControl,
                editVisible,
                viewVisible,
                msginfo,
                whiteType,
                disableType,
                handleCommand,
                form,
                formatCarNo,
                dateFormat,
                operateCarnoParams,
                dialogT,
                handleSearch,
                handlePageChange,
                handleDelete,
                handleEdit,
                inputFileClick,
                handleView,
                bindRoad,
                editCarno,
                delCarno,
                // verifyShow,
                saveEdit,
                multipleSelection: [],
                dialogImageUrl: "",
                ppVisible: false,
                activeName: "first",
                map: null,
                saveBatch,
                bmdVisible,
                batchVisible,
                batchForm,
                batchAdd,
                blueTags,
                yellowTags,
                greenTags,
                inputVisible,
                inputValue,
                inputVisibleY,
                inputValueY,
                inputVisibleG,
                inputValueG,
                saveTagInput,
                saveTagInputY,
                saveTagInputG,
                handleClose,
                showInput,
                download,
                handleInputConfirm,
                handleCloseY,
                showInputY,
                handleInputConfirmY,
                handleCloseG,
                showInputG,
                exportImport,
                handleInputConfirmG,
                viewVisibleInner,
                carName,
                carPhone,
                focusPhoneInput,
                focusName,
                focusPhone,
                carInfoSure,
                innerClose,
                tags,
                handleInputConfirmBlur,
                handleInputConfirmYBlur,
                handleInputConfirmGBlur,
                valueC,
                timeC,
                viewVisibleExcel,
                excelImport,
                parks,
                roads,
                // 车牌录入 start
                addForm,
                carTypeData,
                rosterTypeData,
                carNaturesData,
                whitelistTypeData,
                // timeData,
                fileIds2,
                file_img2,
                getQueryDate,
                getQueryDate2,
                getQueryDate3,
                getQueryDate4,
                // 车牌录入 end
                formqjl,
                getArea,
                getStreet,
                getRoad,
                getParks,
                menuType,
                getData,
                dialogVisible,
                // 详情地址
                viewVisibleAddress,
                handleViewAddress,
                charList,
                listHeight,
                currentIndex,
                indexPage,
                indexLimit,
                indexData,
                listGroup,
                scrollbar,
                calculateHeight,
                scrollToLetter,
                handleScroll,
                closeAddress,

                whiteStas,
                whiteTime,
                whiteTime2,
                whiteTime3,
                whiteStasName,
                cj_whiteStas,
            };
        },
        computed: {
			indexList() {
				return this.charList
			}
		},
        methods: {
            // 车牌录入 start
            //上传图片操作
            handleSuccess2(response, file, fileList) {
                this.fileIds2.push(response.id + "");
                this.imageId2 = response.id + "";
                file.id = response.id;
                this.imageUrl2 = URL.createObjectURL(file.raw);
                console.log("imageUrl2", this.imageUrl2);
                console.log(response, file, fileList);
            },
            handlePictureCardPreview2(file) {
                //图片预览
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            //删除图片
            handleRemove2(file, fileList) {
                let index = this.fileIds2.indexOf(file.id + "");
                if (index == -1) {
                    index = this.fileIds2.indexOf(file.id);
                } else {
                    //移除删除id
                    this.fileIds2.splice(index, 1);
                }
            },
            // saveEdit(formName) {
            //   //保存方法
            //   let that = this;
            //   if (roster_type == "4") {
            //     //残疾人车辆
            //     if (that.fileIds2.length == 0 && null != that.fileIds2) {
            //       ElMessage.error("残疾证照片");
            //       return false;
            //     }
            //   }
            //   that.isDisabl = true;
            //   this.saveCar(formName);
            // },
            saveCar(formName) {
                let that = this;
                // name: [{required: true, message: "必填项", trigger: "blur"}],
                // phone: [{required: true, message: "必填项", trigger: "blur"}],
                // deformity_cert: [
                //     {required: true, message: "必填项", trigger: "blur"},
                // ],
                // company_name: [{required: true, message: "必填项", trigger: "blur"}],
                // time_pro: [{required: true, message: "请选择免费截止时间", trigger: "change"}],
                // free_time: [{required: true, message: "请选择免费时间段", trigger: "change"}],

                if (!that.addForm.car_no) {
                    ElMessage.error("请输入车牌号码");
                    return false;
                } else {
                    var regEn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
                    var regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;
                    if (regEn.test(that.addForm.car_no) == true || regCn.test(that.addForm.car_no) == true) {
                        ElMessage.error("车牌号码不能包含特殊字符");
                        return false;
                    }
                }

                //名单类型(1.普通名单、2.黑名单、3.白名单、4.残疾人车辆)
                if (that.addForm.roster_type == "3") {
                    if ("" == that.addForm.name) {
                        ElMessage.error("请输入姓名");
                        return false;
                    }
                    if ("" == that.addForm.phone) {
                        ElMessage.error("请输入电话");
                        return false;
                    }
                    if ("" == that.addForm.company_name) {
                        ElMessage.error("请输入公司名称");
                        return false;
                    }
                    // 白名单类型 0：内部车 1：企业(免税)车 2：救护车
                    if (that.addForm.whitelist_type == "0") {
                        // 白名单免费类型 1永久 2期限
                        if (that.addForm.white_time_type == "2") {
                            if (null == that.addForm.free_time || [] == that.addForm.free_time || "" == that.addForm.free_time) {
                                ElMessage.error("请选择免费时间段");
                                return false;
                            }
                        }
                    } else if (that.addForm.whitelist_type == "1") {
                        // 白名单类型 1：企业(免税)车
                        if (null == that.addForm.time_pro || [] == that.addForm.time_pro || "" == that.addForm.time_pro) {
                            ElMessage.error("请选择免费时间段");
                            return false;
                        }
                    }
                } else if (that.addForm.roster_type == "4") {
                    if ("" == that.addForm.name) {
                        ElMessage.error("请输入姓名");
                        return false;
                    }
                    if ("" == that.addForm.deformity_cert) {
                        ElMessage.error("请输入残疾证号");
                        return false;
                    }
                }

                if (that.fileIds2.length > 0) {
                    that.addForm.deformity_picture_id = that.fileIds2.join(",");
                }

                that.addForm.deformity_picture_id = that.imageId2;


                // that.$refs[formName].validate((valid) => {
                //     if (valid) {
                        operatecarnoSaveData(that.addForm).then((res) => {
                            if (res.code == 0) {
                                ElMessage.success(res.msg);
                                setTimeout(function () {
                                    that.isDisabl = false;

                                    that.resetForm('formId');
                                    that.query.pageIndex = 1;
                                    that.getData();

                                    // that.$router.push("/carnummana");
                                }, 3000);
                            } else if (res.code == -1) {
                                ElMessage.error(res.msg);
                            }
                        });
                    // } else {
                    //     that.isDisabl = false;
                    // }
                // });
            },
            upperCase() {
                let that = this;
                that.addForm.car_no = that.addForm.car_no.toUpperCase();
            },
            radioBtns(val) {
                console.log("val", val)
                this.addForm.whitelist_type = "0";
                this.addForm.white_time_type = "1";
                if (val == "3") {
                    this.addForm.name = "";
                    this.addForm.deformity_cert = "";
                    this.addForm.cut_off_date = "";
                    this.addForm.time_pro = "";
                    this.addForm.free_time = [];
                    this.addForm.free_time_start = "";
                    this.addForm.free_time_end = "";
                    this.fileIds = []; //图片上传ids 初始化
                    this.whiteStas = true;
                    this.whiteTime2 = false;
                    this.whiteTime3 = true;
                    this.whiteStasName = true;
                    this.cj_whiteStas = false;
                } else if (val == "4") {
                    this.addForm.name = "";
                    this.addForm.phone = "";
                    this.addForm.company_name = "";
                    this.whiteStas = false;
                    this.whiteStasName = true;
                    this.cj_whiteStas = true;
                    this.whiteTime = false;
                    this.whiteTime2 = false;
                    this.whiteTime3 = false;
                } else {
                    this.whiteStas = false;
                    this.whiteStasName = false;
                    this.cj_whiteStas = false;
                    this.whiteTime = false;
                    this.whiteTime3 = false;
                }
            },
            radioBtnsbmd(val) {
                this.addForm.white_time_type = "1";
                // console.log("白名单类型：",val)
                if (val == "1") {
                    this.whiteTime = true;//企业白名单 免费截止日期
                    this.whiteTime2 = false;//内部车白名单 免费日期段
                    this.whiteTime3 = false;////内部车白名单 免费类型
                } else {
                    if (val == "0") {
                        this.whiteTime3 = true;
                    } else {
                        this.whiteTime3 = false;
                    }
                    this.whiteTime = false;
                    this.whiteTime2 = false;
                }
            },
            radioBtnWhiteTimeType(val) {
                // console.log("白名单类型：",val)
                if (val == "1") {
                    this.whiteTime2 = false;
                } else {
                    this.whiteTime2 = true;
                }
            },
            resetForm(formName) {
                this.fileIds2 = [];//图片上传ids
                this.dialogImageUrl = "";
                this.imageId2 = "";
                this.imageUrl2 = "";

                this.addForm.time_pro = "";
                this.addForm.parkIds = [];
                this.addForm.roadIds = [];
                this.addForm.white_time_type = "1";
                this.addForm.free_time = [];
                this.addForm.free_time_start = "";
                this.addForm.free_time_end = "";
                this.whiteTime2 = false;
                this.$refs["formId"].resetFields();
            },
            // 车牌录入 end

            handleSelectionChange(data) {
                this.selectedData = data;
            },
            carAdd() {
                //路由跳转，至车牌录入
                this.$router.push("/carnumenter");
            },
            handleDeleteAll() {
                var that = this;
                var val = this.selectedData;
                if (val) {
                    val.forEach(function (item, index) {
                        that.tableData.forEach(function (itemI, indexI) {
                            if (item === itemI) {
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

            //上传图片操作
            handleSuccess(response, file, fileList) {
                this.fileIds.push(response.id + "");
                this.imageId = response.id + "";
                file.id = response.id;
                this.operateCarnoParams.deformity_picture_id = response.id;
                this.imageUrl = URL.createObjectURL(file.raw);
                console.log("imageUrl", this.imageUrl);
                console.log(response, file, fileList);
            },
            //删除图片
            handleRemove(file, fileList) {
                console.log(file, fileList);
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
            // //图片回显
            // imgurl: function (url) {
            //     if (url != "" && url != null) {
            //         return url;
            //     }
            // },
            //
            //     //上传图片操作
            //     handleSuccess(response, file, fileList) {
            //         this.fileIds.push(response.id + "");
            //         file.id = response.id;
            //         this.file_img = file;
            //     },

            //     handlePictureCardPreview(file) {//图片预览
            //         this.dialogImageUrl = file.url;
            //         this.dialogVisible = true;
            //    },
            //   //删除图片
            //     handleRemove(file, fileList) {
            //         let index = this.fileIds.indexOf(file.id + "");
            //         if (index == -1) {
            //             index = this.fileIds.indexOf(file.id);
            //         }else{
            //        //移除删除id
            //        this.fileIds.splice(index, 1);
            //       }
            //    },
            // 地图初始化函数，本例取名为init，开发者可根据实际情况定义
            /*initMap() {
                        //console.log(window); // 通过window获取
                        var center = new window.TMap.LatLng(39.98412, 116.307484);
                        //初始化地图
                        var map = new window.TMap.Map("mapDiv", {
                            zoom: 18, //设置地图缩放级别
                            center: center, //设置地图中心点坐标
                            baseMap: {
                                // 设置卫星地图
                                //type: "satellite",
                            },
                        });
                        //console.log(map);
                    },*/
        },
        mounted() {
            //this.initMap();
        },
        created() {
            let h = document.documentElement.clientHeight || document.body.clientHeight;
            this.tableH = h - 488 + "px";
        },
    };
</script>

<style>
    .blue{
        color: #409eff !important;
    }
    .customWidth {
        padding: 20px;
    }

    .imgWidth {
        vertical-align: middle;
        display: inline-block;
        width: 100%;
    }

    .el-upload--text {
        width: 178px;
    }

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        /*width: 100%;*/
        /*height: 100%;*/
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }

    .avatar {
        /*width: 100%;*/
        /*height: 100%;*/
        width: 178px;
        height: 178px;
        display: block;
    }
</style>
<style scoped>
    .el-tag--light {
        margin-right: 10px;
    }

    .button-new-tag {
        height: 32px;
        line-height: 30px;
        padding-top: 0;
        padding-bottom: 0;
    }

    .input-new-tag {
        width: 90px;
        vertical-align: middle;
    }

    .txtcenter {
        text-align: center;
    }

    .excel-form .el-form-item {
        border: 1px dashed #ddd;
        padding: 10px;
        border-radius: 2px;
    }

    .el-radio {
        margin-right: 11px;
    }

    .small-lineheight .el-form-item__label {
        line-height: 24px;
    }

    .handle-box {
        margin-bottom: 0;
    }

    .w140 {
        width: 148px;
    }

    .left-panel {
        margin-bottom: 14px;
    }

.index-bar-content {
  position: relative;
  width: 800px;
  height: 600px;
}
.el-scrollbar__wrap {
    overflow-x: hidden;

  }
  .el-scrollbar__view {
      padding: 0 20px;
    }
    .main-list {
    padding-top: 10px;

  }
  .title-key {
      padding-bottom: 12px;
      font-size: 14px;
      font-weight: bold;
    }
    .content-container {
      display: flex;
      flex-wrap: wrap;
      padding-right: 80px;
      box-sizing: border-box;
    }
    .content-item {
        margin-right: 16px;
        margin-bottom: 12px;
        font-size: 12px;
      }
.el-popover {
  padding: 0;
}
.char-list {
  z-index: 99;
  width: 24px;
  height: 100%;
  background: #fafbfe;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  list-style: none;
  display: flex;
  flex-direction: column;
  text-align: center;

}
.char-list li {
    height: 19px;
    display: inline;
    cursor: pointer;
    font-size: 14px;
  }
  .char-list .active {
    color: #E6A23C;
  }
  .no-warp{
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    cursor: pointer;
  }
  ::v-deep .el-transfer-panel{
      width: 299px !important;
    }
    .tag-nums.el-tag--light {
    margin-right: 0;
    }

</style>
