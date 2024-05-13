<template>
  <div class="container" id="viewpage" ref="viewpage">
    <el-form label-width="170px" label-position="left">
      <!-- <el-form-item label="设置申请发票时间：">
        <el-tag type="danger">{{form.invoiceApplyTime}}小时</el-tag>
      </el-form-item>
      <el-form-item>
        <el-alert type="info"
          >1、未申请开发票的订单超过该设置时间，不可再申请发票，该订单已结束。<br />2、设置为
          0，则视为未设置。</el-alert
        >
      </el-form-item>
      <el-form-item label="缴费后驶离时间：">
        <el-tag type="danger">{{form.leaveTime}}分钟</el-tag>
      </el-form-item>
      <el-form-item>
        <el-alert type="info"
          >车主在驾车离场前可以先缴费后驾车离场，设置“缴费后驶离时间”为缴费后免费停车时长，超出免费时长未离场时，订单将继续计时计费</el-alert
        >
      </el-form-item> -->
      <el-form-item label="合并重复订单间隔时间：">
        <el-tag type="danger">{{ form.mergeTime }}分钟</el-tag>
      </el-form-item>
      <el-form-item>
        <el-alert type="info"
          >1、由于行人经过遮挡或者闪光造成的视频杆设备误判，造成一个车在同一个泊位内频繁驶入驶出的假象，合并假象生成的订单<br />
          2、本项设置为设置合并重复订单间隔时间，保存后即刻生效</el-alert
        >
      </el-form-item>
      <el-form-item label="关注后赠送金额：">
        <el-tag type="danger">{{ form.giftMoney }}元</el-tag>
      </el-form-item>
      <el-form-item>
        <el-alert type="info"
          >车主关注公众号后，赠送停车费，每个微信号仅限一次，赠送的停车费存入钱包，可供停车缴费使用，0表示不赠送</el-alert
        >
      </el-form-item>
        <el-form-item label="残疾人折扣：">
          <el-tag type="danger">{{ form.Discount }}%</el-tag>
        </el-form-item>
        <el-form-item>
          <el-alert type="info">残疾人名单按此折扣计费</el-alert>
        </el-form-item>
      <el-form-item>
        <el-button type="primary" v-permission="['road_systempara_edit','park_systempara_edit']" class="mt20" @click="goTo()">编辑</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { querySysConfig } from "../../api/index";
export default {
  name: "systemparameter",
  setup() {
    const form = ref({});
    const queryParams = () => {
      querySysConfig(reactive({})).then((res) => {
        form.value = res.data;
      });
    };
    const getData = () => {
      queryParams();
    };
    getData();
    return {
      form,
    };
  },
  methods: {
    goTo() {
      //直接跳转
      this.$router.push("/systemparaedit");
      //带参数跳转
      // this.$router.push({ path: "/systemparaedit", query:{pk_refinfo:'test',value:'test1'}});
      // this.$router.push({ name: "testDemo", params: { setid: 111222 } });
    },
  },
};
</script>
<style scoped>
</style>