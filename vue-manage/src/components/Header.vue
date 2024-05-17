<template>
  <div>
    <div class="header">
      <!-- 折叠按钮 -->

      <div class="logo">{{ systemtitle }}</div>
      <div class="collapse-btn" @click="collapseChage">
        <i v-if="!collapse" class="el-icon-s-fold"></i>
        <i v-else class="el-icon-s-unfold"></i>
      </div>
      <ul class="topmenu f-cb" v-show="isshow">
        <li
          :class="activeClass === index ? 'active' : ''"
          v-for="(item, index) in mainmenu"
          :key="index"
          @click="menuCommand(index, item.id)"
        >
          {{ item.title }}
        </li>
        <li class="activeClass">
          <a
            class="a-link"
            href="https://zhtc.aldwxa.top/sjfxNew/index.do"
            target="_blank"
            >数据分析</a
          >
          <!-- <a
            class="a-link"
            href="http://192.168.0.172:8081/sjfxNew/index.do"
            target="_blank"
            >数据分析</a
          > -->
        </li>
      </ul>
      <ul class="topmenu f-cb" v-show="ishide">
        <li
          :class="activeClass === index ? 'active' : ''"
          v-for="(item, index) in mainmenupark"
          :key="index"
          @click="menuCommandPark(index, item.id)"
        >
          {{ item.title }}
        </li>
        <li class="activeClass">
          <a
            class="a-link"
            href="https://zhtc.aldwxa.top/sjfxNew/index.do"
            target="_blank"
            >数据分析</a
          >
          <!-- <a
            class="a-link"
            href="http://192.168.0.172:8081/sjfxNew/index.do"
            target="_blank"
            >数据分析</a
          > -->
        </li>
      </ul>
      <div class="header-right">
        <div class="header-user-con">
          <!--<div-->
            <!--class="swichbtn"-->
            <!--@click="systemChage('r_close')"-->
            <!--v-show="btnisshow"-->
          <!--&gt;-->
            <!--<el-tooltip-->
              <!--class="item"-->
              <!--effect="dark"-->
              <!--content="切换至停车场后台管理"-->
              <!--placement="bottom"-->
            <!--&gt;-->
              <!--<p><i class="el-icon-sort"></i>至停车场</p>-->
            <!--</el-tooltip>-->
          <!--</div>-->
          <!--<div-->
            <!--class="swichbtn"-->
            <!--@click="systemChageroad('p_close')"-->
            <!--v-show="btnishide"-->
          <!--&gt;-->
            <!--<el-tooltip-->
              <!--class="item"-->
              <!--effect="dark"-->
              <!--content="切换至路内后台管理"-->
              <!--placement="bottom"-->
            <!--&gt;-->
              <!--<p><i class="el-icon-sort"></i>至路内</p>-->
            <!--</el-tooltip>-->
          <!--</div>-->

          <!-- 消息中心 -->
          <el-dropdown trigger="click">
            <div class="btn-bell">
              <div class="btn-bell-icon">
                <i class="el-icon-bell" @click="clickMsg()"></i>
              </div>
              <span
                class="btn-bell-badge"
                v-if="
                  result.msgManager.operate_feedback_num ||
                  result.msgManager.operate_order_num ||
                  result.msgManager.operate_car_no_num ||
                  result.msgManager.inspect_feedback_num
                "
              ></span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  @click="toOperate('a')"
                  icon="el-icon-chat-dot-square"
                  v-if="
                    result.msgManager.card_no === '1' ||
                    result.msgManager.card_no === '2' ||
                    result.msgManager.card_no === '0'
                  "
                >
                  订单申诉待处理<span class="warning-bel">{{
                    result.msgManager.operate_order_num
                  }}</span>
                </el-dropdown-item>
                <el-dropdown-item
                  @click="toOperate('b')"
                  icon="el-icon-edit-outline"
                  v-if="
                    result.msgManager.card_no === '1' ||
                    result.msgManager.card_no === '2'
                  "
                >
                  车牌申诉待处理
                  <span class="warning-bel">{{
                    result.msgManager.operate_car_no_num
                  }}</span>
                </el-dropdown-item>
                <el-dropdown-item
                  @click="toOperate('d')"
                  icon="el-icon-place"
                  v-if="result.msgManager.card_no === '1'"
                  >巡检员上报待处理
                  <span class="warning-bel">{{
                    result.msgManager.inspect_feedback_num
                  }}</span>
                </el-dropdown-item>
                <el-dropdown-item
                  @click="toOperate('c')"
                  icon="el-icon-document"
                  v-if="
                    result.msgManager.card_no === '1' ||
                    result.msgManager.card_no === '2'
                  "
                >
                  车主反馈建议待处理
                  <span class="warning-bel">{{
                    result.msgManager.operate_feedback_num
                  }}</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <!-- 用户头像 -->
          <div class="user-avator">
            <img src="../assets/img/img.jpg" />
          </div>
          <!-- 用户名下拉菜单 -->
          <el-dropdown
            class="user-name"
            trigger="click"
            @command="handleCommand"
          >
            <span class="el-dropdown-link">
              {{ username }}
              <i class="el-icon-caret-bottom"></i>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="pwd">密码修改</el-dropdown-item>
                <!--<el-dropdown-item command="user">个人中心</el-dropdown-item>-->
                <el-dropdown-item divided command="loginout"
                  >退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <el-dialog title="密码修改" v-model="pwdVisible" width="30%">
      <el-form label-width="70px" ref="passform" :model="form">
        <el-form-item label="旧密码">
          <el-input
            type="password"
            v-model="form.old"
            prop="old"
            autocomplete="off"
            placeholder="请输入旧密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input
            type="password"
            v-model="form.newPass"
            prop="newPass"
            autocomplete="off"
            placeholder="请输入新密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input
            type="password"
            v-model="form.newsure"
            prop="newsure"
            autocomplete="off"
            placeholder="请再次输入新密码"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="pwdVisible = false">取 消</el-button>
          <el-button type="primary" @click="onUpdPass">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <div class="info-tips-con" v-show="showTips">
      <!--控制showTips为false 即关闭此提醒框-->
      <div class="info-tips-head">
        <span class="text-t">信息设备报警</span>
        <span class="play-t"
          ><i
            @click="showDetail()"
            :class="[isShowDetail ? 'el-icon-arrow-down' : 'el-icon-arrow-up']"
          ></i
        ></span>
      </div>
      <div class="info-tips-body" v-show="isShowDetail">
        <ul>
          <li
            v-for="item in events"
            :key="item.id"
            ref="contlist"
            :class="{ 'animate-up': animateUp }"
            @mouseenter="Stop()"
            @mouseleave="Up()"
            @click="toDeviceList()"
          >
            <p class="info-tips-type">
              {{ item.device_type_name }} <span>{{ item.device_code }}</span>
            </p>
            <p class="info-tips-des" v-if="item.road_name">
              {{ item.road_name }}
            </p>
            <p class="info-tips-des" v-else>{{ item.park_name }}</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>
import { computed, onMounted, ref, reactive } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import {
  queryMenus,
  switchModel,
  logout,
  updPass,
  toManager,
  parkDeviceList,
  roadDeviceList,
} from "../api/index";
import { ElMessage } from "element-plus";

export default {
  data() {
    return {
      // 表单验证
      // formRules: {
      //     old: [{required: true, message: "请输入旧密码", trigger: "blur"}],
      //     newPass: [{required: true, message: "请输入新密码", trigger: "blur"}],
      //     newsure: [{required: true, message: "请输入确认密码", trigger: "blur"}],
      // },
      showTips: false,
      isShowDetail: true,
      // 控制动画
      animateUp: false,
      // 计时器
      intNum: null,
      // 内容
      events: [
        {
          id: 1,
          type: "设备离线",
          time: "2020-06-15 09:22:15",
          des: "王伟伟操作了此设备",
        },
        {
          id: 2,
          type: "设备报警",
          time: "2020-06-15 10:11:09",
          des: "川垣二路北段西侧A",
        },
        {
          id: 3,
          type: "设备报警",
          time: "2020-06-17 11:22:15",
          des: "川垣二路北段西侧B",
        },
        {
          id: 4,
          type: "设备离线",
          time: "2020-06-18 12:22:15",
          des: "张三调试设备导致错误",
        },
      ],
    };
  },
  setup() {
    //菜单类型 0路内 1停车场
    const menuType = ref("1");

    const router = useRouter();
    const username = localStorage.getItem("user_name");
    const message = 2;
    const curTab = ref("yysj");
    const store = useStore();
    const isshow = ref(true);
    const ishide = ref(false);
    const btnisshow = ref(true);
    const btnishide = ref(false);
    const collapse = computed(() => store.state.collapse);
    // 侧边栏折叠
    const collapseChage = () => {
      store.commit("handleCollapse", !collapse.value);
    };

    // 修改密码弹窗
    const pwdVisible = ref(false);
    const form = ref({
      old: "",
      newPass: "",
      newsure: "",
    });

    //修改密码方法
    const passform = ref(null);
    const onUpdPass = () => {
      passform.value.validate((v) => {
        if (v) {
          if (
            form.value.old == "" ||
            form.value.old == null ||
            form.value.old == undefined
          ) {
            ElMessage.error("请输入旧密码");
          } else if (
            form.value.newPass == "" ||
            form.value.newPass == null ||
            form.value.newPass == undefined
          ) {
            ElMessage.error("请输入新密码");
          } else if (
            form.value.newsure == "" ||
            form.value.newsure == null ||
            form.value.newsure == undefined
          ) {
            ElMessage.error("请输入确认密码");
          } else if (form.value.newPass != form.value.newsure) {
            ElMessage.error("两次密码不一致");
          } else {
            updPass(form.value).then((res) => {
              if (res.code == "0") {
                ElMessage.success(res.msg);
                pwdVisible.value = false;
                //修改密码成功后，退出系统重新登陆
                logout(reactive({})).then((res) => {
                  if (res.code === 0) {
                    localStorage.removeItem("token_value");
                    router.push("/login");
                  }
                });
              } else {
                ElMessage.error(res.msg);
              }
            });
          }
        }
      });
    };

    // 消息
    const result = reactive({
      msgManager: {},
    });
    const msg = () => {
      const timer = setInterval(() => {
        clickMsg();
      }, 300000);
    };

    const clickMsg = () => {
      toManager().then((res) => {
        result.msgManager = res.data;
      });
    };
    clickMsg();
    msg();

    // 跳转
    const toOperate = (type) => {
      if (type === "a") {
        if (result.msgManager.model === "0") {
          router.push("/shensuorder");
        } else {
          router.push("/shensuorderA");
        }
      } else if (type === "b") {
        router.push("/carnumshensu");
      } else if (type === "c") {
        router.push("/feedbacklist");
      } else if (type === "d") {
        router.push("/onsitefeedbackxj");
      }
    };

    const mainmenu = ref([]);
    const childrenMenu = ref({});
    const parkchildrenmenu = ref({});
    const mainmenupark = ref([]);
    const firstRoadParentNode = ref(0);
    const firstRoadChildNode = ref(0);
    const firstParkParentNode = ref(0);
    const firstParkChildNode = ref(0);
    const authoritys = ref([]);

    const menuCommand = (inx, command) => {
      activeClass.value = inx;
      store.commit("handleMenu", childrenMenu.value[command]);
      store.commit('changeAuthority', authoritys.value);
      router.push(childrenMenu.value[command][0]["index"]);
    };

    const menuCommandPark = (inx, command) => {
      activeClass.value = inx;
      store.commit("handleMenu", parkchildrenmenu.value[command]);
      store.commit('changeAuthority', authoritys.value);
      router.push(parkchildrenmenu.value[command][0]["index"]);
    };
    const type = ref("road");
    const userType = ref("0");
    let menu_close = "";
    const getMainMenu = () => {
      queryMenus(reactive({})).then((res) => {
        mainmenu.value = res.data.roadmenu.top;
        childrenMenu.value = res.data.roadmenu.children;
        firstRoadParentNode.value = res.data.roadmenu.firstRoadParentNode;
        firstRoadChildNode.value = res.data.roadmenu.firstRoadChildNode;
        mainmenupark.value = res.data.parkmenu.top;
        parkchildrenmenu.value = res.data.parkmenu.children;
        firstParkParentNode.value = res.data.parkmenu.firstParkParentNode;
        firstParkChildNode.value = res.data.parkmenu.firstParkChildNode;
        userType.value = res.data.userType;
        authoritys.value = res.data.authoritys;
        let isRP = res.data.isRP;
        if (userType.value === "1") {
          systemChage(menu_close);
          btnisshow.value = false;
          btnishide.value = false;
        } else {
          systemChage(menu_close);
          // if (isRP != null) {
          //   if (isRP == "1") {
          //     systemChage(menu_close);
          //   } else {
          //     systemChageroad(menu_close);
          //   }
          // } else {
          //   if (mainmenu.value.length > 0) {
          //     systemChageroad(menu_close);
          //   } else {
          //     systemChage(menu_close);
          //   }
          // }
        }
      });
    };

    getMainMenu();
    // 用户名下拉菜单选择事件

    const handleCommand = (command) => {
      if (command == "loginout") {
        logout(reactive({})).then((res) => {
          if (res.code === 0) {
            localStorage.removeItem("token_value");
            router.push("/login");
          }
        });
        // localStorage.removeItem("ms_username");
        // router.push("/login");
      } else if (command == "pwd") {
        // router.push("/pwd");
        pwdVisible.value = true;
      }
    };

    //设备报警弹窗 变量
    const events = ref([]);
    const intNum = ref(0);
    const intWarnNum = ref(0);
    const showTips = ref(false);
    const isShowDetail = ref(false);
    const animateUp = ref(false);

    const systemtitle = ref("路内智慧停车");
    //切换至停车场系统
    const systemChage = (val) => {
      menu_close = val;
      isshow.value = false;
      ishide.value = true;
      btnisshow.value = false;
      btnishide.value = true;
      systemtitle.value = "停车场后台管理";
      queryMenus(reactive({})).then((res) => {
        authoritys.value = res.data.authoritys;
        menuCommandPark(0, firstParkParentNode.value);
      });

      switchModel(reactive({ type: "1" })).then((res) => {
        if (res.code === 0) {
          // if(val!=""){
          //   store.commit("clearTags");
          //   router.push("/");
          // }
          //ElMessage.success("切换成功");
          menuType.value = "1";
          localStorage.setItem("menuType", "1");
          ElMessage({ duration: 1000, message: "切换成功", type: "success" });
        } else {
          ElMessage({ duration: 2000, message: "切换失败", type: "error" });
        }
      });
      // parkDeviceList(reactive({ status: "3" })).then((res) => {
      //   console.log("获取离线报警设备列表 停车场");
      //   console.log(res);
      //   events.value = res.data;
      //   if (events.value.length == 0) {
      //     showTips.value = false;
      //     isShowDetail.value = false;
      //   } else {
      //     // todo
      //     //showTips.value = true;
      //     isShowDetail.value = true;
      //     clearInterval(intNum.value);
      //     if (events.value.length > 2) {
      //       ScrollUp();
      //     }
      //   }
      // });
      // clearInterval(intWarnNum.value);
      // //获取离线报警设备列表 停车场
      // intWarnNum.value = setInterval(() => {
      //   parkDeviceList(reactive({ status: "3" })).then((res) => {
      //     console.log("获取离线报警设备列表 停车场 定时");
      //     console.log(res);
      //     events.value = res.data;
      //     if (events.value.length == 0) {
      //       showTips.value = false;
      //       isShowDetail.value = false;
      //     } else {
      //       showTips.value = true;
      //       isShowDetail.value = true;
      //       clearInterval(intNum.value);
      //       if (events.value.length > 2) {
      //         ScrollUp();
      //       }
      //     }
      //   });
      // }, 60000);
    };
    //切换至路内系统
    const systemChageroad = (val) => {
      menu_close = val;
      isshow.value = true;
      ishide.value = false;
      btnisshow.value = true;
      btnishide.value = false;
      systemtitle.value = "路内后台管理";
      queryMenus(reactive({})).then((res) => {
        authoritys.value = res.data.authoritys;
        menuCommand(0, firstRoadParentNode.value);
      });
      switchModel(reactive({ type: "0" })).then((res) => {
        if (res.code === 0) {
          // if(val!=""){
          //   store.commit("clearTags");
          //   router.push("/");
          // }
          menuType.value = "0";
          localStorage.setItem("menuType", "0");
          ElMessage({ duration: 1000, message: "切换成功", type: "success" });
        } else {
          ElMessage({ duration: 2000, message: "切换失败", type: "error" });
        }
      });
      //router.push("/login");

      roadDeviceList(reactive({ status: "3" })).then((res) => {
        console.log("获取离线报警设备列表 路内");
        console.log(res);
        events.value = res.data;
        if (events.value.length == 0) {
          showTips.value = false;
          isShowDetail.value = false;
        } else {
          //todo
          showTips.value = false;
          isShowDetail.value = true;
          clearInterval(intNum.value);
          if (events.value.length > 2) {
            ScrollUp();
          }
        }
      });
      // clearInterval(intWarnNum.value);
      // //获取离线报警设备列表 路内
      // intWarnNum.value = setInterval(() => {
      //   roadDeviceList(reactive({ status: "3" })).then((res) => {
      //     console.log("获取离线报警设备列表 路内 定时");
      //     console.log(res);
      //     events.value = res.data;
      //     if (events.value.length == 0) {
      //       showTips.value = false;
      //       isShowDetail.value = false;
      //     } else {
      //       showTips.value = true;
      //       isShowDetail.value = true;
      //       clearInterval(intNum.value);
      //       if (events.value.length > 2) {
      //         ScrollUp();
      //       }
      //     }
      //   });
      // }, 60000);
    };
    const activeClass = ref(-1);

    // 侧边栏折叠
    const ScrollUp = () => {
      intNum.value = setInterval(() => {
        animateUp.value = true; // 向上滚动的时候需要添加css3过渡动画
        setTimeout(() => {
          events.value.push(events.value[0]); // 将数组的第一个元素添加到数组的
          events.value.shift(); // 删除数组的第一个元素
          animateUp.value = false;
        }, 500);
      }, 2000);
    };

    onMounted(() => {
      if (document.body.clientWidth < 1500) {
        collapseChage();
      }
      // if (events.value.length > 2) {
      //     ScrollUp()
      // }
    });

    const showDetail = () => {
      isShowDetail.value = !isShowDetail.value;
    };
    // 鼠标移上去停止
    const Stop = () => {
      clearInterval(intNum.value);
    };
    // 鼠标离开继续滚动
    const Up = () => {
      if (events.value.length > 2) {
        ScrollUp();
      }
      // ScrollUp()
    };

    const toDeviceList = () => {
      if (menuType.value == "0") {
        router.push("/equipmentlist");
      } else {
        router.push("/equipmentlistA");
      }
    };
    return {
      pwdVisible,
      form,
      onUpdPass,
      passform,
      username,
      message,
      collapse,
      collapseChage,
      handleCommand,
      systemChage,
      result,
      clickMsg,
      toOperate,
      //openmenu,
      menuCommand,
      menuCommandPark,
      parkchildrenmenu,
      curTab,
      mainmenu,
      childrenMenu,
      mainmenupark,
      activeClass,
      isshow,
      ishide,
      btnisshow,
      btnishide,
      systemtitle,
      systemChageroad,
      showTips,
      isShowDetail,
      // 控制动画
      animateUp,
      // 计时器
      intNum,
      // 内容
      events,
      intWarnNum,
      ScrollUp,
      showDetail,
      Stop,
      Up,
      menuType,
      toDeviceList,
    };
  },
  mounted() {
    // if (this.events.length > 2) {
    //     this.ScrollUp()
    // }
  },
  destroyed() {
    // clearInterval(this.intNum)
  },
  methods: {
    // toDeviceList() {
    //     this.$router.push('/carnumenter')
    // },
    // showDetail() {
    //     this.isShowDetail = !this.isShowDetail;
    // },
    // ScrollUp() {
    //     this.intNum = setInterval(() => {
    //         this.animateUp = true// 向上滚动的时候需要添加css3过渡动画
    //         setTimeout(() => {
    //             this.events.push(this.events[0])// 将数组的第一个元素添加到数组的
    //             this.events.shift() // 删除数组的第一个元素
    //             this.animateUp = false
    //         }, 500)
    //     }, 1000)
    // },
    // // 鼠标移上去停止
    // Stop() {
    //     clearInterval(this.intNum)
    // },
    // // 鼠标离开继续滚动
    // Up() {
    //     this.ScrollUp()
    // },
    getItem(index) {
      //alert(index);
      this.activeClass = index; // 把当前点击元素的index，赋值给activeClass
    },
    // ,onUpdPass(formName) {
    //     //保存方法
    //     let that = this;
    //     this.$refs[formName].validate((valid) => {
    //         if (valid) {
    //             if (that.form.newPass != that.form.newsure) {
    //                 ElMessage.error("两次密码不一致");
    //                 return false;
    //             } else {
    //                 updPass(that.form).then((res) => {
    //                     if (res.code == "0") {
    //                         ElMessage.success(res.msg);
    //                         that.addVisible = false;
    //                         //修改密码成功后，退出系统重新登陆
    //                         logout(reactive({})).then((res) => {
    //                             if (res.code === 0) {
    //                                 localStorage.removeItem("token_value");
    //                                 that.router.push("/login");
    //                             }
    //                         })
    //                     } else {
    //                         ElMessage.error(res.msg);
    //                     }
    //                 });
    //             }
    //         }
    //     });
    // },
  },
};
</script>
<style scoped>
.el-dropdown-link {
  color: #fff;
  cursor: pointer;
}

.el-dropdown-menu__item {
  text-align: center;
}

.a-link {
  color: #fff;
}

.header .topmenu li {
  padding: 0 18px;
}

.animate-up {
  transition: all 0.2s ease-in-out;
  transform: translateY(-60px);
}
</style>
