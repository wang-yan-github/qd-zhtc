<template>
  <div class="main2">
    <div class="quarter-div black">
      <!-- <video controls  id="videoElement" class="video-class"></video> -->
      <div id="videoElement" style="border: 0; width: 100%; height: 500px"></div>
      <div class="name">{{ channelName }}</div>
    </div>
  </div>
</template>

<script setup>
// import flvjs from "flv.js";
import { defineProps, ref, reactive, onMounted, watch, getCurrentInstance } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import md5 from "js-md5";
// 声明props
const props = defineProps({
  rtspData: Object,
  default: {},
});

// 声明变量
let url = ref(""); // 视频地址
let channelName=ref("")
// 初始化
onMounted(() => {
  console.log("初始化", JSON.stringify(props.rtspData.value));
  channelName.value=props.rtspData.value.channelName
  // createVideo(props.rtspData.value.rtsp);
  play();
});

const instance = getCurrentInstance()
const { $appid, $appSecret,$timestampToTime,$randomString} = instance.appContext.config.globalProperties;
let timestamp = $timestampToTime;
let appid = $appid
let appSecret = $appSecret
var requestId = $randomString;
var serial = ref(props.rtspData.value.sn);
var signStr = md5(appid + requestId + serial.value + appSecret + timestamp).toLowerCase();

const param = {
  appId: appid,
  requestId: requestId,
  serial: serial.value,
  sign: signStr,
  timestamp: timestamp,
};

// 初始化视频
const qyVideo = new QyVideo("https://qy-vds.com", {
  //是否开启日志
  logEnabled: true,

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
    //console.log("qyVideo container id :%s is %s!",target.getvideoElementId(),state);
    switch (state) {
      //连接成功
      case "connected":
        //console.log("playing ... ");
        break;
      //连接断开，重新播放（连接断开后，每5秒回调一次disconnected状态，不需要在外部使用定时器）
      case "disconnected":
        //console.log("disconnected！");
        play();
        break;
    }
  },
});

const play = () => {
  if (qyVideo) {
    qyVideo.destroy();
  }
  qyVideo.readVideoInfo(
    param,
    function (response) {
      if (response.success) {
        const videoInfo = response.data;
        //videoInfo：返回的视频信息
        //videoElement：视频容器DOM ID
        qyVideo.play(videoInfo, "videoElement");
      } else {
        // alert("视频播放失败：" + response.msg);
        ElMessage.error("视频播放失败：" + response.msg);
      }
    },
    function (msg) {
      // alert("视频播放失败:" + msg);
      ElMessage.error("视频播放失败：" + msg);
    }
  );
}

/**
 * 销毁视频，回收资源
 */
// function destroy() {
//   // debugger
//   if (qyVideo) {
//     qyVideo.destroy();
//   }
// }
const destroy = () => {
  if (qyVideo) {
    qyVideo.destroy();
  }
}
//抛出需要回调变量及方法
defineExpose({ destroy,play,serial });
// function createVideo(url) {
//   debugger;

// }
</script>

<style scoped>
div {
  object-fit: initial;
}

* {
  margin: 0;
  padding: 0;
}

.main2 {
  width: 100%;
  height: 100%;
  /* position: absolute; */
}
.quarter-div{position:relative;}
.quarter-div .name{position:absolute;top:0;right:0;line-height:40px;z-index:1;color:#fff;width:100%;box-sizing:border-box;padding-right:10px;text-align:right;}

.black {
  background-color: black;
  border: 1px solid #ccc;
  box-sizing: border-box;
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
