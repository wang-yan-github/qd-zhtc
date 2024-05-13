import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
import './assets/css/icon.css'
import './assets/css/css.css'
import './assets/Uediter/ueditor.config.js'
import './assets/uediter/ueditor.all.min.js'
import './assets/Uediter/lang/zh-cn/zh-cn.js'
// import './assets/js/bootstrap.js'
import './assets/js/adapter.js'
import './assets/js/platform.js'
import './assets/js/h5splayer.js'
import './assets/js/h5splayerhelper.js'
import './assets/css/h5splayer.css'
import permission from './directive/permission/permission.js'
// import './assets/Uediter/ueditor.parse.min.js'
import './assets/Uediter/themes/default/css/ueditor.css'
import {timestampToTime,randomString} from './utils/tools'
//Vue2.0全局方法挂载
// Vue.prototype.timestampToTime = timestampToTime
// Vue.prototype.randomString = randomString

const app = createApp(App)

// 全局方法挂载
app.config.globalProperties.$timestampToTime = timestampToTime()
app.config.globalProperties.$randomString = randomString()
app.config.globalProperties.$appid="0962fd55f4f24e89855b361a8dbde196"
app.config.globalProperties.$appSecret="CBCE1238E87241A681E76492606A11C2"

installElementPlus(app)
app

    .use(store)
    .directive('permission', permission)
    // .use(permission)
    .use(router)
    .mount('#app')
