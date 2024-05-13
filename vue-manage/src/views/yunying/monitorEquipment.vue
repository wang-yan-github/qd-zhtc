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
        <div class="left-panel">
          <el-button
                  type="primary"
                  size="small"
                  icon="el-icon-plus"
                  @click="handleEdit(null, null, true)"
                  v-permission="['road_equipment_add', 'park_equipment_add']"
          >添加</el-button
          >
          <!--          <el-button-->
          <!--            type="danger"-->
          <!--            size="small"-->
          <!--            icon="el-icon-delete"-->
          <!--            @click="handleDeleteAll()"-->
          <!--            >批量删除</el-button-->
          <!--          >-->
        </div>
        <div class="right-panel">
          <el-form inline label-width="80" class="lineH0">
            <el-form-item label="" class="search-mb0">
              <el-input
                      @keyup.enter="handleSearch()"
                      size="small"
                      v-model="query.equipName"
                      class="handle-input"
                      placeholder="输入设备名称"
              ></el-input>
            </el-form-item>

            <el-form-item label="" class="search-mb0">
              <el-select
                      v-model="query.areaId"
                      filterable
                      size="small"
                      placeholder="所有区域"
              >
                <el-option key="" label="选择区域" value=""></el-option>
                <el-option
                        v-for="(item,i) in areas"
                        :key="i"
                        :label="item.area_name"
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
            </el-form-item>
          </el-form>

        </div>
        <div class="clear"></div>
      </div>
      <!-- <el-row>
                <el-col :span="24">
                    <div class="top-panel">
                        <el-form ref="form" :model="form" label-width="80px" inline size="small">
                            <el-form-item label="姓名">
                               <el-input v-model="form.name" size="small"></el-input>
                            </el-form-item>
                            <el-form-item label="登陆账号">
                               <el-input v-model="form.name" size="small"></el-input>
                            </el-form-item>
                            <el-form-item label="登陆账号">
                               <el-button size="small" type="primary" icon="el-icon-search">查询</el-button>
                               <el-button size="small" type="text" icon="el-icon-arrow-up">合并</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
            </el-row> -->
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
                prop="equipment_name"
                label="设备名称"
                width="300"
                align="center"
        ></el-table-column>
        <el-table-column
                prop="equipment_code"
                label="设备编码"
                width="300"
                align="center"
        ></el-table-column>
        <!-- <el-table-column label="头像(查看大图)" align="center">
                    <template #default="scope">
                        <el-image class="table-td-thumb" :src="scope.row.thumb" :preview-src-list="[scope.row.thumb]">
                        </el-image>
                    </template>
                </el-table-column> -->
        <el-table-column
                prop="areaName"
                label="区域"
                align="center"
                width="200"
        ></el-table-column>
        <el-table-column
                prop="install_address"
                label="安装地址"
                align="center"
        ></el-table-column>
        <el-table-column
                prop="rtsp_address"
                label="rtsp地址"
                width="400"
                align="center"
        ></el-table-column>
        <!--        <el-table-column prop="roles" label="角色"></el-table-column>-->
        <!--        <el-table-column label="状态" align="center" width="100">-->
        <!--          <template #default="scope">-->
        <!--            <el-tag-->
        <!--              size="small"-->
        <!--              :type="-->
        <!--                scope.row.state === '启用'-->
        <!--                  ? 'success'-->
        <!--                  : scope.row.state === '禁用'-->
        <!--                  ? 'danger'-->
        <!--                  : ''-->
        <!--              "-->
        <!--              >{{ scope.row.state }}</el-tag-->
        <!--            >-->
        <!--            &lt;!&ndash; <el-switch-->
        <!--              :value="-->
        <!--                scope.row.state === '成功'-->
        <!--                  ? true-->
        <!--                  : scope.row.state === '失败'-->
        <!--                  ? false-->
        <!--                  : ''-->
        <!--              "-->
        <!--              active-color="#13ce66"-->
        <!--              inactive-color="#ff4949"-->
        <!--            >-->
        <!--            </el-switch> &ndash;&gt;-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleEdit(scope.id, scope.row, false)"
                    v-permission="['road_equipment_edit', 'park_equipment_edit']"
            >编辑
            </el-button>
            <!--            <el-button-->
            <!--              size="mini"-->
            <!--              type="text"-->
            <!--              icon="el-icon-close"-->
            <!--              @click="handleEdit(scope.$index, scope.row, false)"-->
            <!--              >停用-->
            <!--            </el-button>-->
            <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    class="red"
                    @click="handleDelete(scope.$index, scope.row)"
                    v-permission="['road_equipment_delete', 'park_equipment_delete']"
            >删除</el-button
            >
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

    <!-- 编辑弹出框 -->
    <el-dialog :title="dialogT" v-model="editVisible" width="30%" v-loading="loading">
      <!--<el-tabs type="card" v-model="activeName">-->
        <!--<el-tab-pane label="设备管理" name="first">-->
          <!--<div class="mt20"></div>-->
          <el-form label-width="100px" :rules="formRules" ref="formId" :model="form">
            <el-form-item label="设备名称" prop="equipment_name">
              <el-input v-model="form.equipment_name" placeholder="请输入设备名称"></el-input>
            </el-form-item>
            <el-form-item label="设备编号" prop="equipment_code">
              <el-input v-model="form.equipment_code" placeholder="请输入设备编号"></el-input>
            </el-form-item>
            <el-form-item label="Token" prop="token">
              <el-input v-model="form.token" placeholder="请输入Token"></el-input>
            </el-form-item>
            <el-form-item label="设备账号" prop="equipment_account">
              <el-input v-model="form.equipment_account" placeholder="请输入设备账号"></el-input>
            </el-form-item>
            <el-form-item label="设备密码" prop="equipment_pwd">
              <el-input v-model="form.equipment_pwd" placeholder="请输入设备密码"></el-input>
            </el-form-item>
            <el-form-item label="RTSP地址" prop="rtsp_address">
              <el-input v-model="form.rtsp_address" placeholder="请输入RTSP地址"></el-input>
            </el-form-item>
            <el-form-item label="安装地址">
              <el-input v-model="form.install_address" placeholder="请输入安装地址"></el-input>
            </el-form-item>
            <el-form-item label="区域" class="w" prop="area_id">
              <el-select v-model="form.area_id" placeholder="请选择" class="w100p">
                <el-option
                        v-for="(item,i) in areas"
                        :key="i"
                        :label="item.area_name"
                        :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
        <!--</el-tab-pane>-->
        <!--        <el-tab-pane label="巡检充值信息" name="second">-->
        <!--          <div class="mt20"></div>-->
        <!--          <el-form label-width="100px">-->
        <!--            <el-form-item label="手机号">-->
        <!--              <el-input v-model="form.name"></el-input>-->
        <!--            </el-form-item>-->

        <!--            <el-form-item label="充值密码">-->
        <!--              <el-input v-model="form.mima"></el-input>-->
        <!--            </el-form-item>-->
        <!--            <el-form-item label="确认充值密码">-->
        <!--              <el-input v-model="form.remima"></el-input>-->
        <!--            </el-form-item>-->
        <!--          </el-form>-->
        <!--        </el-tab-pane>-->
      <!--</el-tabs>-->

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit()">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { ref, reactive } from "vue";
  import { ElMessage, ElMessageBox } from "element-plus";
  import {monitorList,getAreas,addMonitorEquipment,getMonitorEquipment,delMonitorEquipment} from "../../api/monitorEquipment.js";

  export default {
    name: "monitorEquipment",
    data() {
      return {
        tableH:0
      };
    },
    setup(props,context) {
      const query = reactive({
        equipName: "",
        areaId:"",
        pageIndex: 1,
        pageSize: 15,
      });
      const tableData = ref([]);
      const pageTotal = ref(0);
      let equipId = ref("");
      let areas = ref([]);
      let dialogT = ref("新增");
      const editVisible = ref(false);
      let form = ref({
      });
      // 获取表格数据
      const getData = () => {
        monitorList(query).then((res) => {
          tableData.value = res.data.list;
          pageTotal.value = res.data.total;
        });
      };

      const getAreas_ = () => {
        getAreas(query).then((res) => {
          areas.value = res.data;
        });
      };
      getData();
      getAreas_();
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
      const handleDelete = (index,row) => {
        // 二次确认删除
        ElMessageBox.confirm("确定要删除吗？", "提示", {
          type: "warning",
        })
                .then(() => {
                  const detailQuery = reactive({
                    id: row.id,
                  });
                  delMonitorEquipment(detailQuery).then((res) => {
                    if(res.code == 0){
                      ElMessage.success(`删除成功`);
                      tableData.value.splice(index, 1);
                    }else{
                      ElMessage.error(res.msg)
                    }


                  });

                })
                .catch(() => {});
      };


      // 表格编辑时弹窗和保存
      const formId = ref(null);
      const handleEdit = (index, row, type) => {
        if (type) {
          form.value= {};
          equipId = "";
          dialogT.value='新增'
        } else {
          dialogT.value='编辑';

          form.value.id = row.id;
          equipId = row.id;
          // Object.keys(form).forEach((item) => {
          //   form[item] = row[item];
          // });
          const detailQuery = reactive({
            id: equipId,
          });
          getMonitorEquipment(detailQuery).then((res) => {
            form.value = res.data;
            // form.value.areas=areas;
            // form.value.area_id = res.data.area_id;

          });

        }
        editVisible.value = true;
        formId.value.clearValidate();
      };

      const saveEdit = () => {
        formId.value.validate((valid) => {
          if(valid){
            if(equipId != null && equipId != ""){
              addMonitorEquipment(form.value).then((res) => {
                if(res.code == 0){
                  ElMessage.success(`修改成功`)
                  editVisible.value = false;
                }else{
                  ElMessage.error(res.msg)
                }

              });
            }else{
              addMonitorEquipment(form.value).then((res) => {
                if(res.code == 0){
                  ElMessage.success(`新增成功`)
                  editVisible.value = false;
                }else{
                  ElMessage.error(res.msg)
                }

              });
            }
          }else{
            ElMessage.error("请输入必填项！");
            return false;
          }
        });


        // editVisible.value = false;
        // ElMessage.success(`修改第 ${idx + 1} 行成功`);
        // Object.keys(form).forEach((item) => {
        //   tableData.value[idx][item] = form[item];
        // });
      };

      //验证
      const formRules = {
        equipment_name: [
          { required: true, message: "请输入设备名称", trigger: "blur"},
        ],
        equipment_code: [
          { required: true, message: "请输入设备编码", trigger: "blur" },
        ],
        token: [
          { required: true, message: "请输入设备token", trigger: "blur" },
        ],
        equipment_account: [
          { required: true, message: "请输入设备账号", trigger: "blur" },
        ],
        equipment_pwd: [
          { required: true, message: "请输入设备密码", trigger: "blur" },
        ],
        rtsp_address: [
          { required: true, message: "请输入rtsp地址", trigger: "blur" },
        ],
        area_id: [
          { required: true, message: "请选择区域", trigger: "blur" },
        ]
      };

      return {
        query,
        tableData,
        pageTotal,
        editVisible,
        form,
        formId,
        areas,
        equipId,
        formRules,
        dialogT,
        handleSearch,
        handlePageChange,
        handleDelete,
        handleEdit,
        saveEdit,
        multipleSelection: [],
        value: true,
        activeName: "first",
      };
    },
    created() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.tableH = h - 308 + 'px';
    },
    methods: {
      handleSelectionChange(data) {
        this.selectedData = data;
      },
      handleDeleteAll() {
        var that = this;
        var val = this.selectedData;

        if (val) {
          val.forEach(function (item, index) {
            that.tableData.forEach(function (itemI, indexI) {
              if (item === itemI) {
                // ElMessageBox.confirm("确定要删除吗？", "提示", {
                //   type: "warning",
                // })
                //   .then(() => {
                //     ElMessage.success("删除成功");
                //     that.tableData.splice(indexI, 1);
                //   })
                //   .catch(() => {
                //     ElMessage.info("取消删除");
                //   });
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
    },
  };
</script>
