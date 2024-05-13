<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">智慧停车运营管理平台</div>
      <el-form
        :model="param"
        :rules="rules"
        ref="login"
        label-width="0px"
        class="ms-content"
      >
        <el-form-item prop="username">
          <el-input
            v-model="param.consumer"
            placeholder="username"
            tabindex="1"
          >
            <template #prepend>
              <el-button icon="el-icon-user"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            type="password"
            placeholder="password"
            v-model="param.cypher"
            tabindex="2"
            @keyup.enter="submitForm()"
            show-password
          >
            <template #prepend>
              <el-button icon="el-icon-lock"></el-button>
            </template>
          </el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm()">登录</el-button>
        </div>
        <p class="login-tips">Tips : 请输入用户名、密码</p>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { queryLogin } from "../api/index";

export default {
  setup() {
    const router = useRouter();
    const param = reactive({
      consumer: "admin",
      cypher: "123456",
    });

    const rules = {
      consumer: [
        {
          required: true,
          message: "请输入用户名",
          trigger: "blur",
        },
      ],
      cypher: [{ required: true, message: "请输入密码", trigger: "blur" }],
    };
    const login = ref(null);
    const submitForm = () => {
      login.value.validate((valid) => {
        if (valid) {
          queryLogin(param).then((res) => {
            if (res.success) {
              ElMessage.success("登录成功");
              localStorage.setItem("token_name", res.token.tokenName);
              localStorage.setItem("token_value", res.token.tokenValue);
              localStorage.setItem("user_name", res.user.user_name);
              localStorage.setItem("user_type", res.user.user_type);
              router.push("/");
            } else {
              ElMessage.error(res.msg);
            }
          });

          // localStorage.setItem("ms_username", param.username);
          // router.push("/");
        } else {
          ElMessage.error("登录成功");
          return false;
        }
      });
    };

    const store = useStore();
    store.commit("clearTags");

    return {
      param,
      rules,
      login,
      submitForm,
    };
  },
};
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url(../assets/img/login-bg.jpg);
  background-size: 100%;
}
.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #fff;
  border-bottom: 1px solid #ddd;
}
.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 350px;
  margin: -190px 0 0 -175px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.3);
  overflow: hidden;
}
.ms-content {
  padding: 30px 30px;
}
.login-btn {
  text-align: center;
}
.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}
.login-tips {
  font-size: 12px;
  line-height: 30px;
  color: #fff;
}
</style>