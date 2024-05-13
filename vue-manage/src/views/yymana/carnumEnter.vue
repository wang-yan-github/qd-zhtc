<template>
  <div>
    <div class="container">
      <el-form label-width="90px" :rules="formRules" ref="formId" :model="form">
        <el-form-item label="车牌号码" prop="car_no">
          <el-input
            @blur="upperCase"
            placeholder="车牌号"
            v-model="form.car_no"
            class="handle-input"
          ></el-input>
        </el-form-item>
        <el-form-item label="车牌类型">
          <el-radio-group v-model="form.car_type">
            <el-radio
              v-for="item in carTypeData"
              :key="item.id"
              :label="item.dc_value"
              >{{ item.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <!--         <el-form-item label="车牌性质" prop="car_natures">-->
        <!--          <el-select v-model="form.car_natures" placeholder="请选择"  class="handle-input">-->
        <!--            <el-option v-for="(item,i) in carNaturesData" :key="i" :label="item.label" :value="item.dc_value"></el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item label="名单类型">
          <el-radio-group v-model="form.roster_type" @change="radioBtns">
            <el-radio
              v-for="item in rosterTypeData"
              :key="item.id"
              :label="item.dc_value"
              >{{ item.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="白名单类型" v-if="whiteStas">
          <el-radio-group v-model="form.whitelist_type" @change="radioBtnsbmd">
            <el-radio
              v-for="item in whitelistTypeData"
              :key="item.id"
              :label="item.dc_value"
              >{{ item.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item label="姓名" prop="name" v-if="whiteStasName">
          <el-input
            placeholder="姓名"
            v-model="form.name"
            class="handle-input"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="残疾证号"
          prop="deformity_cert"
          v-if="cj_whiteStas"
        >
          <el-input
            placeholder="残疾证号"
            v-model="form.deformity_cert"
            class="handle-input"
          ></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone" v-if="whiteStas">
          <el-input
            placeholder="电话"
            v-model="form.phone"
            class="handle-input"
          ></el-input>
        </el-form-item>
        <el-form-item label="公司名称" prop="company_name" v-if="whiteStas">
          <el-input
            placeholder="公司名称"
            v-model="form.company_name"
            class="handle-input"
          ></el-input>
        </el-form-item>
        <el-form-item label="停车场" v-if="whiteStas">
          <el-select v-model="form.parkIds" multiple placeholder="请选择">
            <el-option
              v-for="item in parks"
              :key="item.id"
              :label="item.park_name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="路内" v-if="whiteStas">
          <el-select v-model="form.roadIds" multiple placeholder="请选择">
            <el-option
              v-for="item in roads"
              :key="item.id"
              :label="item.road_name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="免费时间" v-if="whiteTime" prop="time_pro">
          <el-date-picker
            v-model="timeData.time"
            @change="getQueryDate"
            type="date"
            style="width: 300px"
            placeholder="选择日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="上传照片(最多一张)" v-if="cj_whiteStas">
          <el-upload
            :action="fileurl"
            list-type="picture-card"
            limit="1"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-success="handleSuccess"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="saveCar('formId')"
            :disabled="isDisabl"
            >提交</el-button
          >
          <el-button @click="resetForm('formId')">重置</el-button>
          <el-button type="primary" plain @click="backPage()">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { dictData, queryParkData, queryRoadData } from "../../api/index";
import { operatecarnoSaveData, getOneTraffic } from "../../api/car";
import File_URL from "../../file_url";
import { useRouter } from "vue-router";
export default {
  name: "carList",
  data() {
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
    return {
      isDisabl: false,
      whiteStas: false,
      whiteTime: false,
      whiteStasName: false,
      cj_whiteStas: false,
      fileurl: File_URL.file_img_url,
      // 表单验证
      formRules: {
        car_no: [{ validator: checkName, required: true, trigger: "blur" }],
        // car_natures: [{ required: true, message: "请选择", trigger: "blur" }],
        name: [{ required: true, message: "必填项", trigger: "blur" }],
        phone: [{ required: true, message: "必填项", trigger: "blur" }],
        deformity_cert: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        company_name: [{ required: true, message: "必填项", trigger: "blur" }],
        time_pro: [{ required: true, message: "请选择时间", trigger: "blur" }],
      },
    };
  },
  setup() {
    const form = reactive({
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
    });
    const carTypeData = ref({});
    const rosterTypeData = ref({});
    const carNaturesData = ref({});
    const whitelistTypeData = ref({});
    const fileIds = ref([]);
    const file_img = ref([]);
    let timeData = ref({
      time: "",
    });
    const router = useRouter();
    const getData = () => {
      const id = router.currentRoute.value.query.id;
      if (id == null || id == "") {
      } else {
        getOneTraffic(id).then((res) => {
          console.log(res.data.car_no);
          form.car_no = res.data.car_no;
          form.roster_type = res.data.roster_type;
        });
      }
    };
    getData();
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

    //日期控件change方法
    const getQueryDate = () => {
      if (
        null == timeData.value.time ||
        [] == timeData.value.time ||
        "" == timeData.value.time
      ) {
        form.cut_off_date = "";
      } else {
        form.cut_off_date = dateFormat(timeData.value.time);
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

    //获取当前车牌样式
    dictData(reactive({ dict_type: "car_type" })).then((res) => {
      carTypeData.value = res.data;
    });
    //名单类型
    dictData(reactive({ dict_type: "rosterType" })).then((res) => {
      rosterTypeData.value = res.data;
    });
    // 车辆性质
    dictData(reactive({ dict_type: "carNatures" })).then((res) => {
      carNaturesData.value = res.data;
    });
    //白名单类型
    dictData(reactive({ dict_type: "whitelist_type" })).then((res) => {
      whitelistTypeData.value = res.data;
    });

    return {
      form,
      carTypeData,
      rosterTypeData,
      carNaturesData,
      whitelistTypeData,
      timeData,
      fileIds,
      file_img,
      parks,
      roads,
      getQueryDate,
    };
  },
  methods: {
    backPage() {
      this.$router.push("/carnummana");
    },
    //上传图片操作
    handleSuccess(response, file, fileList) {
      this.fileIds.push(response.id + "");
      file.id = response.id;
      this.file_img = file;
    },
    handlePictureCardPreview(file) {
      //图片预览
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //删除图片
    handleRemove(file, fileList) {
      let index = this.fileIds.indexOf(file.id + "");
      if (index == -1) {
        index = this.fileIds.indexOf(file.id);
      } else {
        //移除删除id
        this.fileIds.splice(index, 1);
      }
    },
    saveEdit(formName) {
      //保存方法
      let that = this;
      if (roster_type == "4") {
        //残疾人车辆
        if (that.fileIds.length == 0 && null != that.fileIds) {
          ElMessage.error("残疾证照片");
          return false;
        }
      }
      that.isDisabl = true;
      this.saveCar(formName);
    },
    saveCar: function (formName) {
      let that = this;
      if (that.fileIds.length > 0) {
        that.form.deformity_picture_id = that.fileIds.join(",");
      }
      that.$refs[formName].validate((valid) => {
        if (valid) {
          operatecarnoSaveData(that.form).then((res) => {

            if (res.code == 0) {
              ElMessage.success(res.msg);
              setTimeout(function () {
                that.isDisabl = false;
                that.$router.push("/carnummana");
              }, 3000);
            } else if (res.code == -1) {
              ElMessage.error(res.msg);
            }
          });
        } else {
          that.isDisabl = false;
        }
      });
    },
    upperCase() {
      let that = this;
      that.form.car_no = that.form.car_no.toUpperCase();
    },
    radioBtns(val) {
      this.form.whitelist_type = "0";
      if (val == "3") {
        this.form.name = "";
        this.form.deformity_cert = "";
        this.form.cut_off_date = "";
        this.timeData.time = "";
        this.fileIds = []; //图片上传ids 初始化
        this.whiteStas = true;
        this.whiteStasName = true;
        this.cj_whiteStas = false;
      } else if (val == "4") {
        this.form.name = "";
        this.form.phone = "";
        this.form.company_name = "";
        this.whiteStas = false;
        this.whiteStasName = true;
        this.cj_whiteStas = true;
        this.whiteTime = false;
      } else {
        this.whiteStas = false;
        this.whiteStasName = false;
        this.cj_whiteStas = false;
        this.whiteTime = false;
      }
    },
    radioBtnsbmd(val) {
      if (val == "1") {
        this.whiteTime = true;
      } else {
        this.whiteTime = false;
      }
    },
    resetForm(formName) {
      this.timeData.time = "";
      this.form.parkIds = "";
      this.form.roadIds = "";
      this.$refs["formId"].resetFields();
    },
  },
};
</script>