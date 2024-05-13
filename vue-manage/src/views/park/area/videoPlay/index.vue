<template>
  <div class="row">
    <div :span="3" class="left-col">
      <el-card class="box-card" shadow="never">
        <!-- @node-click="nodeClick" -->
        <el-tree
          :data="tree"
          node-key="id"
          :check-on-click-node="true"
          
          :props="defaultProps"
          show-checkbox
          default-expand-all
          ref="rootTree"
          :default-checked-keys="defaultPermTreeSelect"
          @check="handleTreeCheck"
          :filter-node-method="filterNode"
          :check-strictly="true"
        >
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <svg
                t="1691473351510"
                class="icon"
                viewBox="0 0 1024 1024"
                p-id="18300"
                width="25"
                height="25"
              >
                <path
                  d="M769.57 479.34l-37.72 140.8-407.09-109.08c-100.7-27-160.46-130.49-133.47-231.19 27-100.69 130.48-160.45 231.18-133.47l536.86 145.41s-9.97 151.08-189.76 187.53zM728.52 749.84a24.71 24.71 0 0 1-6.43-0.85L194.5 606.49A24.58 24.58 0 0 1 207.32 559l527.6 142.5a24.58 24.58 0 0 1-6.4 48.31z"
                  p-id="18301"
                ></path>
                <path
                  d="M394.07 766.54H206.66a24.58 24.58 0 0 1 0-49.15H383.3l71.75-77.78a24.58 24.58 0 0 1 36.13 33.33l-79.05 85.69a24.56 24.56 0 0 1-18.06 7.91z"
                  p-id="18302"
                ></path>
                <path
                  d="M64.67 754.16a72.96 141.85 0 1 0 145.92 0 72.96 141.85 0 1 0-145.92 0Z"
                  p-id="18303"
                ></path>
                <path
                  d="M803.06 507.17l-21.3 80.46L824.24 599a25.52 25.52 0 0 0 31.26-18l29.06-109.41c-13.86 12.19-48.72 28.93-81.5 35.58z"
                  p-id="18304"
                ></path>
              </svg>
              <span>{{ node.label }}</span>
            </span>
          </template>
        </el-tree>
        <div style="margin-top: 20px; text-align: center">
          <el-button @click="getKeys" size="mini" type="primary">播放选中</el-button
          ><el-button @click="clearTreeSelect" size="mini">全部清除</el-button>
        </div>
      </el-card>
    </div>
    <div :span="21" class="right-col">
      <el-card class="box-card" shadow="never">
        <el-radio-group
          v-model="isActive"
          size="small"
          style="margin-bottom: 12px"
          @change="changeGrid"
        >
          <el-radio-button label="1">
            <svg
              t="1691465483294"
              class="icon"
              viewBox="0 0 1024 1024"
              p-id="11969"
              width="20"
              height="20"
            >
              <path
                d="M960 920c0 22-18 40-40 40H104c-22 0-40-18-40-40V104c0-22 18-40 40-40h816c22 0 40 18 40 40v816z"
                p-id="11970"
              ></path>
            </svg>
          </el-radio-button>
          <el-radio-button label="4">
            <svg
              t="1691465802986"
              class="icon"
              viewBox="0 0 1024 1024"
              p-id="13362"
              width="20"
              height="20"
            >
              <path
                d="M547 437c0 22 18 40 40 40h333c22 0 40-18 40-40V104c0-22-18-40-40-40H587c-22 0-40 18-40 40v333zM437 477c22 0 40-18 40-40V104c0-22-18-40-40-40H104c-22 0-40 18-40 40v333c0 22 18 40 40 40h333zM477 587c0-22-18-40-40-40H104c-22 0-40 18-40 40v333c0 22 18 40 40 40h333c22 0 40-18 40-40V587zM587 547c-22 0-40 18-40 40v333c0 22 18 40 40 40h333c22 0 40-18 40-40V587c0-22-18-40-40-40H587z"
                p-id="13363"
              ></path>
            </svg>
          </el-radio-button>
          <el-radio-button label="9">
            <svg
              t="1691472793077"
              class="icon"
              viewBox="0 0 1024 1024"
              p-id="14385"
              width="20"
              height="20"
            >
              <path
                d="M598 960c22 0 40-18 40-40V748c0-22-18-40-40-40H426c-22 0-40 18-40 40v172c0 22 18 40 40 40h172zM638 426c0-22-18-40-40-40H426c-22 0-40 18-40 40v172c0 22 18 40 40 40h172c22 0 40-18 40-40V426zM64 598c0 22 18 40 40 40h172c22 0 40-18 40-40V426c0-22-18-40-40-40H104c-22 0-40 18-40 40v172zM708 276c0 22 18 40 40 40h172c22 0 40-18 40-40V104c0-22-18-40-40-40H748c-22 0-40 18-40 40v172zM960 426c0-22-18-40-40-40H748c-22 0-40 18-40 40v172c0 22 18 40 40 40h172c22 0 40-18 40-40V426zM748 708c-22 0-40 18-40 40v172c0 22 18 40 40 40h172c22 0 40-18 40-40V748c0-22-18-40-40-40H748zM316 748c0-22-18-40-40-40H104c-22 0-40 18-40 40v172c0 22 18 40 40 40h172c22 0 40-18 40-40V748zM426 64c-22 0-40 18-40 40v172c0 22 18 40 40 40h172c22 0 40-18 40-40V104c0-22-18-40-40-40H426zM276 316c22 0 40-18 40-40V104c0-22-18-40-40-40H104c-22 0-40 18-40 40v172c0 22 18 40 40 40h172z"
                p-id="14386"
              ></path>
            </svg>
          </el-radio-button>

          <el-radio-button label="16">
            <svg
              t="1691472841774"
              class="icon"
              viewBox="0 0 1024 1024"
              p-id="15441"
              width="20"
              height="20"
            >
              <path
                d="M876.088889 1024a56.888889 56.888889 0 0 1-56.888889-56.888889v-91.022222a56.888889 56.888889 0 0 1 56.888889-56.888889H967.111111a56.888889 56.888889 0 0 1 56.888889 56.888889V967.111111a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066667 0a56.888889 56.888889 0 0 1-56.888889-56.888889v-91.022222a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889V967.111111a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066666 0a56.888889 56.888889 0 0 1-56.888889-56.888889v-91.022222a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889V967.111111a56.888889 56.888889 0 0 1-56.888889 56.888889zM56.888889 1024a56.888889 56.888889 0 0 1-56.888889-56.888889v-91.022222a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889V967.111111a56.888889 56.888889 0 0 1-56.888889 56.888889z m819.2-273.066667a56.888889 56.888889 0 0 1-56.888889-56.888889v-91.022222a56.888889 56.888889 0 0 1 56.888889-56.888889H967.111111a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066667 0a56.888889 56.888889 0 0 1-56.888889-56.888889v-91.022222a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066666 0a56.888889 56.888889 0 0 1-56.888889-56.888889v-91.022222a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066667 0a56.888889 56.888889 0 0 1-56.888889-56.888889v-91.022222a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m819.2-273.066666a56.888889 56.888889 0 0 1-56.888889-56.888889V329.955556a56.888889 56.888889 0 0 1 56.888889-56.888889H967.111111a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066667 0a56.888889 56.888889 0 0 1-56.888889-56.888889V329.955556a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066666 0a56.888889 56.888889 0 0 1-56.888889-56.888889V329.955556a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889zM56.888889 477.866667a56.888889 56.888889 0 0 1-56.888889-56.888889V329.955556a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m819.2-273.066667a56.888889 56.888889 0 0 1-56.888889-56.888889V56.888889a56.888889 56.888889 0 0 1 56.888889-56.888889H967.111111a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066667 0a56.888889 56.888889 0 0 1-56.888889-56.888889V56.888889a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z m-273.066666 0a56.888889 56.888889 0 0 1-56.888889-56.888889V56.888889a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889zM56.888889 204.8a56.888889 56.888889 0 0 1-56.888889-56.888889V56.888889a56.888889 56.888889 0 0 1 56.888889-56.888889h91.022222a56.888889 56.888889 0 0 1 56.888889 56.888889v91.022222a56.888889 56.888889 0 0 1-56.888889 56.888889z"
                p-id="15442"
              ></path>
            </svg>
          </el-radio-button>
        </el-radio-group>

        <div class="main2">
          <div
            v-bind:class="[
              isActive == 1 ? 'quarter-div' : '',
              isActive == 4 ? 'quarter-div4' : '',
              isActive == 9 ? 'quarter-div9' : '',
              isActive == 16 ? 'quarter-div16' : '',
              'black',
            ]"
          >
            <h1 class="empty" v-if="rtsplist.length == 0">{{ prompt }}</h1>
            <div class="name" v-else>{{ rtsplist[0].channelName }}</div>
            <div
              :id="'videoElement' + 1"
              ref="refsEl"
              class="video-class"
              :key="isActive + '1'"
            ></div>
            <!-- <video
              controls
              @click="videoHandel(1)"
              :id="'videoElement' + 1"
              class="video-class"
              :key="isActive + '1'"
            ></video> -->
          </div>

          <div
            v-show="isActive != 1"
            v-for="n in [2, 3, 4]"
            :key="n + 'isActive == 4'"
            v-bind:class="[
              isActive == 4 ? 'quarter-div4' : '',
              isActive == 9 ? 'quarter-div9' : '',
              isActive == 16 ? 'quarter-div16' : '',
              'black',
            ]"
          >
            <h1 class="empty" v-if="rtsplist.length < n">{{ prompt }}</h1>
            <div class="name" v-else>{{ rtsplist[n - 1].channelName }}</div>
            <!-- <video
              controls
              @click="videoHandel(n)"
              :id="'videoElement' + n"
              class="video-class"
              :key="isActive + String(n)"
            ></video> -->
            <div
              :id="'videoElement' + n"
              class="video-class"
              :key="isActive + String(n)"
            ></div>
          </div>

          <div
            v-show="isActive != 4 && isActive != 1"
            v-for="n in [5, 6, 7, 8, 9]"
            :key="n + 'isActive == 9'"
            v-bind:class="[
              isActive == 9 ? 'quarter-div9' : '',
              isActive == 16 ? 'quarter-div16' : '',
              'black',
            ]"
          >
            <h1 class="empty" v-if="rtsplist.length < n">{{ prompt }}</h1>
            <div class="name" v-else>{{ rtsplist[n - 1].channelName }}</div>
            <!-- <video
              controls
              @click="videoHandel(n)"
              :id="'videoElement' + n"
              class="video-class"
              :key="isActive + String(n)"
            ></video> -->
            <div
              :id="'videoElement' + n"
              class="video-class"
              :key="isActive + String(n)"
            ></div>
          </div>
          <div
            v-show="isActive == 16"
            v-for="n in [10, 11, 12, 13, 14, 15, 16]"
            :key="n + 'isActive == 16'"
            v-bind:class="[
              isActive == 16 ? 'quarter-div16' : '',
              isActive == 16 ? 'quarter-div16' : '',
              'black',
            ]"
          >
            <h1 class="empty" v-if="rtsplist.length < n">{{ prompt }}</h1>
            <div class="name" v-else>{{ rtsplist[n - 1].channelName }}</div>
            <!-- <video
              controls
              @click="videoHandel(n)"
              :id="'videoElement' + n"
              class="video-class"
              :key="isActive + String(n)"
            ></video> -->
            <div
              :id="'videoElement' + n"
              class="video-class"
              :key="isActive + String(n)"
            ></div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import flvjs from "flv.js";
import {
  defineProps,
  ref,
  reactive,
  onMounted,
  watch,
  nextTick,
  getCurrentInstance,
} from "vue";
import { getDeviceTreeApi } from "../../../../api/park/park";
import { ElMessage, ElMessageBox } from "element-plus";
import md5 from "js-md5";
// 声明props
const props = defineProps({
  //rtsplist: Array,
  default: [],
});

// 声明变量
let url = ref(""); // 视频地址
let list = ref(props.rtsplist); // 视频列表
let count = ref(1); // 宫格数
let isActive = ref(4); // 当前宫格数
let prompt = "暂无视频信息"; // 视频元素
let appid = ref(getCurrentInstance().appContext.config.globalProperties.$appid);
let appSecret = ref(getCurrentInstance().appContext.config.globalProperties.$appSecret);
let requestId = getCurrentInstance().appContext.config.globalProperties.$randomString;
let timestamp = getCurrentInstance().appContext.config.globalProperties.$timestampToTime;

// 树形结构
const defaultProps = {
  children: "children",
  label: "label",
};

// 初始化
onMounted(() => {
  getDeviceTree();
});

const tree = ref([]);
const rtsplist = ref([]);
const alllist = ref([]);
const serialArray = ref([]);
const signArray = ref([]);
const qyVideoParams = ref([]);
const qyVideos = ref([]);

const rootTree = ref([]);
//树 默认选中
const defaultPermTreeSelect = ref([]);

const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

const getKeys = () => {
  const permNodes = rootTree.value.getCheckedNodes();
  // console.log(JSON.stringify(permNodes));
  // const filterData = ref([]);
  // console.log("permNodes.length:", permNodes.length);
  if (permNodes.length > 0) {
    rtsplist.value = [];
    for (var i = 0; i < permNodes.length; i++) {
      if (permNodes[i].children && permNodes[i].children.length > 0) {
        // filterData.value.push(...permNodes[i].children);
        // for (var k = 0; k < permNodes[i].children.length; k++) {
        //   rtsplist.value.push({
        //     code: permNodes[i].children[k].code,
        //     rtsp: "",
        //     channelName: permNodes[i].children[k].label,
        //     player: null,
        //     serialNo: permNodes[i].children[k].serialNo,
        //   });
        // }
      } else {
        // console.log("code:",permNodes[i].code)
        // filterData.value.push(permNodes[i]);
        rtsplist.value.push({
          code: permNodes[i].code,
          rtsp: "",
          channelName: permNodes[i].label,
          player: null,
          serialNo: permNodes[i].serialNo,
        });
      }
    }

    let currentindex = ref(rtsplist.value.length);
    // console.log("rtsplist:", JSON.stringify(rtsplist.value));
    nextTick().then(() => {
      clickhandleitem(currentindex.value);
    });
  } else {
    ElMessage.warning("至少选择一个！");
  }
};
const handleTreeCheck = (currNode, checkedInfo) => {
  // defaultPermTreeSelect.value = checkedInfo.checkedKeys;
  let checkedKeys = checkedInfo.checkedKeys || [];
  let treeNodesMap = rootTree.value.store?.nodesMap || {};
  let status = checkedKeys.length >= 16;
  if (status) {
    ElMessage.warning("最多只能选择16个！");
  }
  Object.keys(treeNodesMap).forEach((key) => {
    let item = treeNodesMap[key] || {};
    if (!checkedKeys.includes(key)) {
      let data = item.data || {};
      data.disabled = status;
      rootTree.value.setCurrentNode(data);
    }
  });
};

//清除
const clearTreeSelect = () => {
  rootTree.value.setCheckedKeys([]);
  let treeNodesMap = tree.value;
  // console.log("treedata:", JSON.stringify(tree));
  // for (var i = 0; i < tree.value.length; i++) {
  //   console.log("tree[i]Value:", JSON.stringify(tree[i].value.children));
  //   if (tree[i].value.children && tree[i].value.children.length > 0) {
      
  //     for (var k = 0; k < tree[i].value.children.length; k++) {
  //       tree[i].value.children[k].disabled = false;
  //     }
  //   }
  // }
  // Object.keys(treeNodesMap).forEach((key) => {
  //   let item = treeNodesMap[key] || {};
  //   let data = item.data || {};
  //   data.disabled = false;
  // });
};

const getDeviceTree = () => {
  getDeviceTreeApi().then((res) => {
    if (res.code === 0) {
      tree.value = res.data.treeData;
      rtsplist.value = res.data.rtsplist;
      alllist.value = res.data.rtsplist;
      // console.log("tree：",JSON.stringify(tree.value))

      // serialArray.value=res.data.rtsplist;
      // console.log("rtsplist", JSON.stringify(rtsplist.value));
      for (var i = 0; i < rtsplist.value.length; i++) {
        signArray.value[i] = md5(
          appid.value +
            requestId +
            rtsplist.value[i].serialNo +
            appSecret.value +
            timestamp
        ).toLowerCase();
        // console.log("signArray.value[i]",signArray.value[i])
        qyVideoParams.value.push({
          appId: appid.value,
          playType: "auto",
          requestId: requestId,
          serial: rtsplist.value[i].serialNo,
          sign: signArray.value[i],
          timestamp: timestamp,
        });
      }
      let videoNum = qyVideoParams.value.length;
      // console.log("videoNum",videoNum)
      for (var i = 0; i < videoNum; i++) {
        qyVideos.value[i] = new QyVideo("https://qy-vds.com", {
          //视频控件样式
          style: {
            //关闭播放控制工具栏
            controls: true,

            //关闭画中画
            //"disablePictureInPicture":true,

            //不显示下载、全屏、回放按钮
            //"controlsList":"nodownload nofullscreen noremoteplayback"
          },

          /**
           * 当视频连接状态发送变化时的事件回调，如果不关注该事件，可以忽略
           * target   连接状态发送变化时的视频对象:QyVideo
           * state    connected:连接成功  disconnected：断开连接，如果连接断开，每隔5秒回调一次，外部不需要做定时器轮询连接状态。
           */
          onconnectionstatechange: function (target, state) {
            console.log(
              "qyVideo container id :%s is %s!",
              target.getVideoContainerId(),
              state
            );
            switch (state) {
              //连接成功
              case "connected":
                console.log("playing ... ");
                break;
              //连接断开，重新播放
              case "disconnected":
                console.log("disconnected %d ... ", i);
                play(i + 1);
                break;
            }
          },
        });

        //添加自定义属性：记录当前视频编号(注意自定义参数不要跟内部属性重复，建议加特定前缀）
        // qyVideos[i]._videoIndex = i;
      }

      // console.log(JSON.stringify(rtsplist.value));
      clickhandleitem(4);
    }
  });
};

const nodeClick = (data, node, obj) => {
  // console.log("index:",data)

  let currentindex = ref(1);
  const serialNo = ref(data.serialNo);
  if (data.serialNo) {
    // for (var item of alllist.value) {
    //   if (item.code == data.code) {

    //     rtsplist.value = [];
    //     rtsplist.value.push(item);
    //     // console.log(JSON.stringify(rtsplist.value))
    //     break;
    //   }
    // }

    for (var i = 0; i < alllist.value.length; i++) {
      if (alllist.value[i].serialNo == serialNo.value) {
        currentindex.value = i;
        break;
      }
    }
    // console.log("currentindex：",currentindex.value)

    isActive.value = 1;
    rtsplist.value = [alllist.value[currentindex.value]];

    nextTick().then(() => {
      clickhandleitemOne(1, currentindex.value);
    });
  }
};

// 改变网格
function changeGrid(value) {
  isActive.value = value;
  rtsplist.value = alllist.value;
  nextTick().then(() => {
    clickhandleitem(value);
  });
}
// 点击宫格
function videoHandel(index) {
  console.log(index);
  isActive.value = 1;
  rtsplist.value = [rtsplist.value[index]];
  nextTick().then(() => {
    clickhandleitem(index);
  });
}

// 根据宫格数创建视频
function clickhandleitem(count) {
  // 根据宫格数量创建视频
  for (let index = 0; index < count; index++) {
    // 判断是否有视频
    if (rtsplist.value.length >= index + 1) {
      // 创建视频
      play(index + 1);
    }
  }
}

function clickhandleitemOne(count, currID) {
  // 根据宫格数量创建视频
  for (let index = 0; index < count; index++) {
    // 判断是否有视频
    // console.log("rtsplist:",JSON.stringify(rtsplist.value))

    if (rtsplist.value.length >= index + 1) {
      // 创建视频
      nextTick().then(() => {
        playOne(currID + 1);
      });
    }
  }
}
const playlist = ref([]);

//初始化多个视频组件

const play = (idx) => {
  destroy();
  qyVideos.value[idx - 1].readVideoInfo(
    qyVideoParams.value[idx - 1],
    function (response) {
      console.log("readVideoInfo success : %o", response);
      if (response.success) {
        const videoInfo = response.data;

        //videoInfo：返回的视频信息
        //v[idx]：视频容器DOM ID
        qyVideos.value[idx - 1].play(videoInfo, "videoElement" + idx);
      } else {
        console.error("视频[%d]播放失败：%s", idx - 1, response.msg);
      }
    },
    function (msg) {
      console.error("视频[%d]播放异常：%s", idx - 1, msg);
    }
  );
};

const playOne = (idx) => {
  destroy();
  qyVideos.value[idx - 1].readVideoInfo(
    qyVideoParams.value[idx - 1],
    function (response) {
      console.log("readVideoInfo success : %o", response);
      if (response.success) {
        const videoInfo = response.data;

        //videoInfo：返回的视频信息
        //v[idx]：视频容器DOM ID
        qyVideos.value[idx - 1].play(videoInfo, "videoElement1");
      } else {
        console.error("视频[%d]播放失败：%s", idx - 1, response.msg);
      }
    },
    function (msg) {
      console.error("视频[%d]播放异常：%s", idx - 1, msg);
    }
  );
};

const destroy = () => {
  let videoNum = qyVideoParams.value.length;
  console.log(videoNum);
  for (let i = 0; i < videoNum; i++) {
    if (qyVideos.value[i]) {
      qyVideos.value[i].destroy();
    }
  }
};
</script>

<style scoped>
div {
  /* object-fit: initial; */
}

/* 布局 */
.row {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  flex-direction: row;
}

.left-col {
  width: 260px;
  margin-right: 20px;
}

.right-col {
  flex: 1;
}

/* 自定义label */
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  font-size: 14px;
  padding-right: 8px;
}

.custom-tree-node svg {
  margin: 0px 8px 0 3px;
}

/* 卡片 */
.box-card {
  height: calc(100vh - 156px);
}

.icon {
  fill: currentColor;
  margin: -6px;
}

/* 视频容器 */
.main2 {
  width: 100%;
  height: calc(100vh - 250px);
  /* position: absolute; */
}

.quarter-div16 {
  width: 24.93%;
  height: 24.93%;
  float: left;
}

.quarter-div9 {
  width: 33.33%;
  height: 33.33%;
  float: left;
}

.quarter-div4 {
  width: 50%;
  height: 50%;
  float: left;
}

.quarter-div {
  width: 100%;
  height: 100%;
}

.black {
  background-color: black;
  border: 1px solid #ccc;
  box-sizing: border-box;
  position: relative;
}

.empty {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #ccc;
  z-index: 1;
}

.name {
  color: #ccc;
  top: 12px;
  right: 12px;
  position: absolute;
  z-index: 1;
  color:#fff;
}

.blue {
  background-color: #5bc0de;
}

.green {
  background-color: #5cb85c;
}

.orange {
  background-color: #f0ad4e;
}

.yellow {
  background-color: #ffc706;
}

.red {
  background-color: #ff0000;
}
.brown {
  background-color: brown;
}

.video-class {
  object-fit: contain;
  width: 100%;
  height: 100%;
}

/* // 播放按钮 */
video::-webkit-media-controls-play-button {
  display: none !important;
}
/* // 当前播放时间 */
video::-webkit-media-controls-current-time-display {
  display: none !important;
}
/* // 剩余时间 */
video::-webkit-media-controls-time-remaining-display {
  display: none !important;
}
/* // 音量按钮 */
video::-webkit-media-controls-volume-control-container {
  display: none !important;
}

/* // 时间轴 */
video::-webkit-media-controls-timeline {
  display: none !important;
}
</style>
