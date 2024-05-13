<template>
  <div class="container">
    <el-form
      label-width="170px"
      label-position="left"
      :rules="formRules"
      ref="editform"
      :model="form"
    >
      <!-- <el-form-item label="设置申请发票时间：" prop="invoiceApplyTime">
                <el-input v-model="form.invoiceApplyTime" placeholder="单位：小时" type="number"></el-input>
            </el-form-item>
            <el-form-item>
                <el-alert type="info"
                >1、未申请开发票的订单超过该设置时间，不可再申请发票，该订单已结束。<br />2、设置为
                    0，则视为未设置。</el-alert
                >
            </el-form-item>
            <el-form-item label="缴费后驶离时间：" prop="leaveTime">
                <el-input v-model="form.leaveTime" placeholder="单位：分钟" type="number"> </el-input>
            </el-form-item>
            <el-form-item>
                <el-alert type="info"
                >车主在驾车离场前可以先缴费后驾车离场，设置“缴费后驶离时间”为缴费后免费停车时长，超出免费时长未离场时，订单将继续计时计费</el-alert
                >
            </el-form-item> -->
      <el-form-item label="合并订单间隔时间：" prop="mergeTime">
        <el-input
          v-model="form.mergeTime"
          placeholder="单位：分钟"
          type="number"
        >
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-alert type="info"
          >1、由于行人经过遮挡或者闪光造成的视频杆设备误判，造成一个车在同一个泊位内频繁驶入驶出的假象，合并假象生成的订单<br />
          2、本项设置为设置合并重复订单间隔时间，保存后即刻生效</el-alert
        >
      </el-form-item>
      <el-form-item label="关注后赠送金额：" prop="giftMoney">
        <el-input v-model="form.giftMoney" placeholder="单位：元" type="number">
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-alert type="info"
          >车主关注公众号后，赠送停车费，每个微信号仅限一次，赠送的停车费存入钱包，可供停车缴费使用，0表示不赠送</el-alert
        >
      </el-form-item>
       <el-form-item label="残疾人折扣：" prop="Discount">
              <el-input v-model="form.Discount" placeholder="单位：%" type="number">
              </el-input>
            </el-form-item>
            <el-form-item>
                <el-alert type="info">残疾人名单按此折扣计费</el-alert>
      </el-form-item>
      <el-form-item
        ><el-button type="primary" class="mt20" @click="save()"
          >保存</el-button
        ></el-form-item
      >
    </el-form>
  </div>
</template>

<script>
import { ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { editSysConfig } from "../../api/index";
import { useRouter } from "vue-router";
import { querySysConfig } from "../../api/index";
export default {
  name: "systemparameteredit",
  setup() {
    const form = ref({
      invoiceApplyTime: "",
      leaveTime: "",
      mergeTime: "",
      giftMoney: "",
      Discount:""
    });
    const editform = ref(null);
    const router = useRouter();
    const queryParams = () => {
      querySysConfig().then((res) => {
        form.value = res.data;
      });
    };
    const getData = () => {
      queryParams();
    };
    getData();

    const editParams = () => {
      editform.value.validate((v) => {
        if (v) {
          editSysConfig(form.value).then((res) => {
            if (res.code === 0) {
              ElMessage.success("更新成功");
              router.push("/systempara");
            }
          });
        }
      });
    };

    const save = () => {
      editParams();
    };
    const formRules = {
      invoiceApplyTime: [
        { required: true, message: "请输入申请发票时间", trigger: "blur" },
      ],
      leaveTime: [
        { required: true, message: "请输入驶离时间", trigger: "blur" },
      ],
      mergeTime: [
        {
          required: true,
          message: "请输入合并重复订单间隔时间",
          trigger: "blur",
        },
      ],
      giftMoney: [
        { required: true, message: "请输入关注后赠送金额", trigger: "blur" },
      ],
      Discount: [
        { required: true, message: "请输入残疾人折扣", trigger: "blur" },
      ],
    };
    return {
      form,
      editform,
      formRules,
      save,
    };
  },
  methods: {
    goTo() {
      //直接跳转
      this.$router.push("/systempara");

      //带参数跳转
      // this.$router.push({ path: "/testDemo", query: { setid: 123456 } });
      // this.$router.push({ name: "testDemo", params: { setid: 111222 } });
    },
  },
};
</script>
