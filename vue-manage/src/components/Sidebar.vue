<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
            text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index" :key="item.index">
                        <template #title>
                            <i :class="item.icon"></i>
                            <span>{{ item.title }}</span>
                            <i class="el-icon el-sub-menu__icon-arrow"></i>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                                <template #title>{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                                    {{ threeItem.title }}</el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.index" :key="subItem.index">{{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i>
                        <template #title>{{ item.title }}</template>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
import { computed, watch } from "vue";
import { useStore } from "vuex";
import { useRoute } from "vue-router";
export default {
    setup() {
        const items = [
            {
                icon: "el-icon-s-home",
                index: "0",
                title: "实时数据",
                subs:[{
                  index:"/dashboard",
                  title:"平台概览"
                },{
                  index:"/dashboard",
                  title:"路内实时监控"
                },{
                  index:"/dashboard",
                  title:"地图路内"
                },{
                  index:"/dashboard",
                  title:"实时停车"
                }]
            },
            {
                icon: "el-icon-s-data",
                index: "1",
                title: "运营数据",
                subs:[{
                  index:"/table",
                  title:"停车订单"
                },{
                  index:"/table",
                  title:"问题订单"
                },{
                  index:"/table",
                  title:"车主用户"
                },{
                  index:"/table",
                  title:"车牌管理"
                }]
            },
            {
                icon: "el-icon-s-grid",
                index: "2",
                title: "运营管理",
                subs:[{
                  index:"/tabs",
                  title:"申诉录入"
                },{
                  index:"/tabs",
                  title:"申诉订单"
                },{
                  index:"/tabs",
                  title:"车牌录入"
                },{
                  index:"/tabs",
                  title:"反馈建议"
                },{
                  index: "/chepaishensu",
                  title: "车牌申诉"
                },{
                  index:"/tabs",
                  title:"设备报警"
                },{
                  index:"/monitorEquipment",
                  title:"设备监控"
                },{
                  index:"/tabs",
                  title:"分配路内"
                },{
                  index:"/tabs",
                  title:"设备离线日志"
                },{
                  index:"/tabs",
                  title:"常见问题"
                },{
                  index:"/tabs",
                  title:"电池设备特性"
                }]
            },
            {
                icon: "el-icon-video-camera-solid",
                index: "3",
                title: "停车场管理",
                subs: [
                    {
                        index: "/form",
                        title: "区域列表",
                    },
                    {
                        index: "/upload",
                        title: "街道列表",
                    },
                    {
                        index: "/upload",
                        title: "路内列表",                        
                    },
                    {
                        index: "/form",
                        title: "设备列表",                        
                    },
                    {
                        index: "/form",
                        title: "停车场管理",                        
                    },
                    {
                        index: "/form",
                        title: "收费方案列表",                        
                    }
                ],
            },
            {
                icon: "el-icon-s-opportunity",
                index: "4",
                title: "财务管理",
                subs: [
                    {
                        index: "/revenuemana",
                        title: "营收管理",
                    },
                    {
                        index: "/czrecord",
                        title: "充值记录",
                    },
                    {
                        index: "/upload",
                        title: "退款记录",                        
                    },
                    {
                        index: "/monthusermana",
                        title: "包月用户管理",                        
                    },
                    {
                        index: "/form",
                        title: "巡检充值管理",                        
                    },
                    {
                        index: "/form",
                        title: "巡检充值统计",                        
                    },
                    {
                        index: "/form",
                        title: "包月充值统计",                        
                    },
                    {
                        index: "/form",
                        title: "待开发票",                        
                    }
                ],
            },
            {
                icon: "el-icon-camera-solid",
                index: "5",
                title: "巡检管理",
                subs: [
                    {
                        index: "/onsitemana",
                        title: "巡检列表",
                    },                    
                    {
                        index: "/onsitefeedback",
                        title: "巡检反馈",
                    },
                    
                ],
            },
            {
                icon: "el-icon-s-marketing",
                index: "6",
                title: "运营分析",
                subs: [
                    {
                        index: "/quyutongji",
                        title: "区域统计",
                    },
                    {
                        index: "/luduantongji",
                        title: "路内统计",
                    },
                    {
                        index: "/onsitestatic",
                        title: "巡检统计",                        
                    },
                    {
                        index: "/orderstatic",
                        title: "订单统计",                        
                    },
                    {
                        index: "/cashstatic",
                        title: "资金统计",                        
                    },
                    {
                        index: "/invoicestatic",
                        title: "开票统计",                        
                    },
                    {
                        index: "/arrearsstatic",
                        title: "大额欠费",                        
                    },                    
                ],
            },
            {
                icon: "el-icon-s-tools",
                index: "7",
                title: "系统管理",
                subs: [
                    {
                        index: "/usermanager",
                        title: "用户管理",
                    },
                    {
                        index: "/rolemanager",
                        title: "角色管理",
                    },
                    {
                        index: "/rizhimanager",
                        title: "操作日志",
                    },
                    {
                        index: "/filemanager",
                        title: "文档管理",
                    },
                    {
                        index: "/systempara",
                        title: "系统参数",
                    },
                    {
                        index: "/monthset",
                        title: "包月配置管理",
                    },
                    {
                        index: "/notice",
                        title: "公告资讯",
                    },
                    {
                        index: "/movepic",
                        title: "轮播图管理",
                    },
                    {
                        index: "/recharge",
                        title: "充值活动",
                    },
                    {
                        index: "/online",
                        title: "上线收费配置",
                    },
                    {
                        index: "/screen",
                        title: "诱导屏管理",
                    },
                    {
                        index: "/actionzn",
                        title: "操作指南",
                    },
                ],
            },
            {
                icon: "el-icon-suitcase",
                index: "8",
                title: "商家管理",
                subs: [
                    {
                        index: "/business",
                        title: "商家审核",
                    },
                    {
                        index: "/businessFirm",
                        title: "商家列表",
                    },
                    {
                        index: "/businessOrder",
                        title: "订单列表",
                    },
                    {
                        index: "/businessRecord",
                        title: "充值记录",
                    },
                    {
                        index: "/businessDiscount",
                        title: "优惠配置",
                    },
                    {
                        index: "/businessInvoicing",
                        title: "开票管理",
                    },

                ],
            }
        ];

        const route = useRoute();

        const onRoutes = computed(() => {
            return route.path;
        });

        const store = useStore();
        const collapse = computed(() => store.state.collapse);

        return {
            items,
            onRoutes,
            collapse,
        };
    },
};
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
    width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
    width: 200px;
}
.sidebar > ul {
    height: 100%;
}
</style>
