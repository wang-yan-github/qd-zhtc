import {createStore} from 'vuex'

export default createStore({
    state: {
        tagsList: [],
        collapse: false,
        menuItem:[{
            icon: "el-icon-s-home",
            index: "/dashboard",
            title: "平台概览",
          },
          {
            icon: "el-icon-s-data",
            index: "/rizhimanager",
            title: "路内实时监控",
          },
          {
            icon: "el-icon-s-grid",
            index: "/rolemanager",
            title: "地图路内",
          },
          {
            icon: "el-icon-video-camera-solid",
            index: "/filemanager",
            title: "实时停车",
          }],
          jk_appid:"0962fd55f4f24e89855b361a8dbde196",
          jk_appSecret:"CBCE1238E87241A681E76492606A11C2",
          authoritys: []
    },
    mutations: {
        delTagsItem(state, data) {
            state
                .tagsList
                .splice(data.index, 1);
        },
        setTagsItem(state, data) {
            state
                .tagsList
                .push(data)
        },
        clearTags(state) {
            state.tagsList = []
        },
        closeTagsOther(state, data) {
            state.tagsList = data;
        },
        closeCurrentTag(state, data) {
            for (let i = 0, len = state.tagsList.length; i < len; i++) {
                const item = state.tagsList[i];
                if (item.path === data.$route.fullPath) {
                    if (i < len - 1) {
                        data
                            .$router
                            .push(state.tagsList[i + 1].path);
                    } else if (i > 0) {
                        data
                            .$router
                            .push(state.tagsList[i - 1].path);
                    } else {
                        data
                            .$router
                            .push("/");
                    }
                    state
                        .tagsList
                        .splice(i, 1);
                    break;
                }
            }
        },
        // 侧边栏折叠
        handleCollapse(state, data) {
            state.collapse = data;
        },
        // 菜单传值
        handleMenu(state, data) {
            state.menuItem = data;
        },
        // 按钮传值
        handleButton(state, data) {
            state.buttons = data;
        },
        changeAuthority(state, value){
            state.authoritys = value
        }
    },
    actions: {},
    modules: {},
    getters: {
        getAuthority: function(state){
            return state.authoritys
        }
    }
})