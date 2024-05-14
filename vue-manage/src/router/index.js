import { createRouter, createWebHashHistory } from "vue-router";
import Home from "../views/Home.vue";

const routes = [
    {
        path: '/',
        redirect: '/dashboard'
    }, {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/dashboard",
                name: "dashboard",
                meta: {
                    title: '路内概览'
                },
                component: () => import("../views/Dashboard.vue")
            }, {
                path: "/usermanager",
                name: "usermanager",
                meta: {
                    title: '用户管理'
                },
                component: () => import("../views/system/userList.vue")
            }, {
                path: "/rolemanager",
                name: "rolemanager",
                meta: {
                    title: '角色管理'
                },
                component: () => import("../views/system/rolesList.vue")
            }, {
                path: "/dictlist",
                name: "dictlist",
                meta: {
                    title: '字典管理'
                },
                component: () => import("../views/system/dictList.vue")
            }, {
                path: "/sysPosappManager",
                name: "sysPosappManager",
                meta: {
                    title: '版本管理'
                },
                component: () => import("../views/system/sysPosappManager.vue")
            }, {
                path: "/rizhimanager",
                name: "rizhimanager",
                meta: {
                    title: '操作日志'
                },
                component: () => import("../views/system/journalList.vue")
            }, {
                path: "/filemanager",
                name: "filemanager",
                meta: {
                    title: '文档管理'
                },
                component: () => import("../views/system/fileList.vue")
            }, {
                path: "/systempara",
                name: "systempara",
                meta: {
                    title: '系统参数'
                },
                component: () => import("../views/system/systemView.vue")
            }, {
                path: "/systemparaedit",
                name: "systemparaedit",
                meta: {
                    title: '编辑系统参数'
                },
                component: () => import("../views/system/systemEdit.vue")
            }, {
                path: "/monthset",
                name: "monthset",
                meta: {
                    title: '包月配置管理'
                },
                component: () => import("../views/system/MonthSet.vue")
            }, {
                path: "/recharge",
                name: "rechargelist",
                meta: {
                    title: '充值活动'
                },
                component: () => import("../views/system/RechargeList.vue")
            }, {
                path: "/notice",
                name: "noticelist",
                meta: {
                    title: '公告资讯'
                },
                component: () => import("../views/system/noticeList.vue")
            }, {
                path: "/movepic",
                name: "movepiclist",
                meta: {
                    title: '轮播图管理'
                },
                component: () => import("../views/system/movePicList.vue")
            }, {
                path: "/sysMaterial",
                name: "sysMateriallist",
                meta: {
                    title: '素材管理'
                },
                component: () => import("../views/system/sysMaterial.vue")
            }, {
                path: "/online",
                name: "onlinelist",
                meta: {
                    title: '上线收费配置'
                },
                component: () => import("../views/system/onlineSet.vue")
            }, {
                path: "/screen",
                name: "screenlist",
                meta: {
                    title: '诱导屏管理'
                },
                component: () => import("../views/system/screenList.vue")
            }, {
                path: "/actionzn",
                name: "actionlist",
                meta: {
                    title: '操作指南'
                },
                component: () => import("../views/system/actionList.vue")
            }, {
                path: "/quyutongji",
                name: "quyutongji",
                meta: {
                    title: '区域统计'
                },
                component: () => import("../views/yunying/areaTongji.vue")
            }, {
                path: "/luduantongji",
                name: "luduantongji",
                meta: {
                    title: '路内统计'
                },
                component: () => import("../views/yunying/roadTongji.vue")
            }, {
                path: "/onsitestatic",
                name: "onsitestatic",
                meta: {
                    title: '巡检统计'
                },
                component: () => import("../views/yunying/xunjianTongji.vue")
            }, {
                path: "/orderstatic",
                name: "orderstatic",
                meta: {
                    title: '订单统计'
                },
                component: () => import("../views/yunying/orderTongjione.vue")
            }, {
                path: "/orderstatictwo",
                name: "orderstatictwo",
                meta: {
                    title: '订单增长'
                },
                component: () => import("../views/yunying/orderTongjitwo.vue")
            }, {
                path: "/orderstaticthree",
                name: "orderstaticthree",
                meta: {
                    title: '订单状态占比'
                },
                component: () => import("../views/yunying/orderTongjithree.vue")
            }, {
                path: "/cashstatic",
                name: "cashstatic",
                meta: {
                    title: '现金统计'
                },
                component: () => import("../views/yunying/cashTongjione.vue")
            }, {
                path: "/cashstatictwo",
                name: "cashstatictwo",
                meta: {
                    title: '充值增长'
                },
                component: () => import("../views/yunying/cashTongjitwo.vue")
            }, {
                path: "/cashstaticthree",
                name: "cashstaticthree",
                meta: {
                    title: '缴费方式统计'
                },
                component: () => import("../views/yunying/cashTongjithree.vue")
            }, {
                path: "/cashstaticfour",
                name: "cashstaticfour",
                meta: {
                    title: '免单统计'
                },
                component: () => import("../views/yunying/cashTongjifour.vue")
            }, {
                path: "/invoicestatic",
                name: "invoicestatic",
                meta: {
                    title: '发票统计'
                },
                component: () => import("../views/yunying/fapiaoTongji.vue")
            }, {
                path: "/arrearsstatic",
                name: "arrearsstatic",
                meta: {
                    title: '欠费统计'
                },
                component: () => import("../views/yunying/qianfeiTongji.vue")
            }, {
                path: "/onsitemanaxj",
                name: "onsitemanaxj",
                meta: {
                    title: '巡检列表'
                },
                component: () => import("../views/xunjian/xunjianList.vue")
            }, {
                path: "/onsitefeedbackxj",
                name: "onsitefeedbackxj",
                meta: {
                    title: '巡检反馈'
                },
                component: () => import("../views/xunjian/xzfankuiList.vue")
            }, {
                path: "/yingshoumana",
                name: "yingshoumana",
                meta: {
                    title: '营收管理'
                },
                component: () => import("../views/caiwu/yingshouList.vue")
            }, {
                path: "/paymentOrderList",
                name: "paymentOrderList",
                meta: {
                    title: '支付流水明细'
                },
                component: () => import("../views/caiwu/paymentOrderList.vue")
            }, {
                path: "/xxchongzhilist",
                name: "xxchongzhilist",
                meta: {
                    title: '线下充值'
                },
                component: () => import("../views/caiwu/xxchongzhiList.vue")
            }, {
                path: "/chongzhilist",
                name: "chongzhilist",
                meta: {
                    title: '充值记录'
                },
                component: () => import("../views/caiwu/chongzhiList.vue")
            }, {
                path: "/tklist",
                name: "tklist",
                meta: {
                    title: '退款记录'
                },
                component: () => import("../views/caiwu/tuikuanList.vue")
            }, {
                path: "/monthusermana",
                name: "monthusermana",
                meta: {
                    title: '包月用户管理'
                },
                component: () => import("../views/caiwu/baoyueuserList.vue")
            }, {
                path: "/xunjianczmana",
                name: "xunjianczmana",
                meta: {
                    title: '发票充值管理'
                },
                component: () => import("../views/caiwu/xjczList.vue")
            }, {
                path: "/shoufeiczmana",
                name: "shoufeiczmana",
                meta: {
                    title: '发票充值管理'
                },
                component: () => import("../views/caiwu/xjczList.vue")
            }, {
                path: "/xsytj",
                name: "xsytj",
                meta: {
                    title: '发票充值统计'
                },
                component: () => import("../views/caiwu/xjczTonji.vue")
            }, {
                path: "/xunjiantj",
                name: "xunjiantj",
                meta: {
                    title: '发票充值统计'
                },
                component: () => import("../views/caiwu/xjczTonji.vue")
            }, {
                path: "/providerecord",
                name: "providerecord",
                meta: {
                    title: '发票发放记录'
                },
                component: () => import("../views/caiwu/providerecord.vue")
            }, {
                path: "/baoyuetj",
                name: "baoyuetj",
                meta: {
                    title: '包月充值统计'
                },
                component: () => import("../views/caiwu/baoyueTonji.vue")
            }, {
                path: "/dkfplist",
                name: "dkfplist",
                meta: {
                    title: '发票申请管理'
                },
                component: () => import("../views/caiwu/fapiaoList.vue")
            }, {
                path: "/arealist",
                name: "arealist",
                meta: {
                    title: '区域列表'
                },
                component: () => import("../views/tcmana/areaList.vue")
            }, {
                path: "/roadposition",
                name: "roadposition",
                meta: {
                    title: '车位坐标'
                },
                component: () => import("../views/tcmana/roadPosition.vue")
            }, {
                path: "/equipmentedit",
                name: "equipmentedit",
                meta: {
                    title: '设备编辑'
                },
                component: () => import("../views/tcmana/equipmentEdit.vue")
            }, {
                path: "/parkinglist",
                name: "parkinglist",
                meta: {
                    title: '停车场管理'
                },
                component: () => import("../views/tcmana/parkingList.vue")
            }, {
                path: "/fanganlist",
                name: "fanganlist",
                meta: {
                    title: '收费方案列表'
                },
                component: () => import("../views/tcmana/fanganList.vue")
            }, {
                path: "/shensuenter",
                name: "shensuenter",
                meta: {
                    title: '申诉录入'
                },
                component: () => import("../views/yymana/shensuEnter.vue")
            }, {
                path: "/shensuorder",
                name: "shensuorder",
                meta: {
                    title: '申诉订单'
                },
                component: () => import("../views/yymana/shensuOrder.vue")
            }, {
                path: "/shensuApprove",
                name: "shensuApprove",
                meta: {
                    title: '申诉审核'
                },
                component: () => import("../views/yymana/shensuApprove.vue")
            }, {
                path: "/carnumenter",
                name: "carnumenter",
                meta: {
                    title: '车牌录入'
                },
                component: () => import("../views/yymana/carnumEnter.vue")
            }, {
                path: "/feedbacklist",
                name: "feedbacklist",
                meta: {
                    title: '反馈建议'
                },
                component: () => import("../views/yymana/feedbackList.vue")
            }, {
                path: "/carnumshensu",
                name: "carnumshensu",
                meta: {
                    title: '车牌申诉'
                },
                component: () => import("../views/yymana/carNumshensu.vue")
            }, {
                path: "/sbwarning",
                name: "equipmentwarn",
                meta: {
                    title: '设备报警'
                },
                component: () => import("../views/yymana/equipmentWarn.vue")
            }, {
                path: "/monitorEquipment",
                name: "monitorEquipment",
                meta: {
                    title: '监控设备'
                },
                component: () => import("../views/yunying/monitorEquipment.vue")
            }, {
                path: "/fenpeiroad",
                name: "fenpeiroad",
                meta: {
                    title: '分配路内'
                },
                component: () => import("../views/yymana/fenpeiRoad.vue")
            }, {
                path: "/fenpeipark",
                name: "fenpeipark",
                meta: {
                    title: '分配停车场'
                },
                component: () => import("../views/yymana/fenpeiRoad.vue")
            }, {
                path: "/equipmentrizhi",
                name: "equipmentrizhi",
                meta: {
                    title: '设备离线日志'
                },
                component: () => import("../views/yymana/equipmentRizhi.vue")
            }, {
                path: "/faqview",
                name: "faqview",
                meta: {
                    title: '常见问题'
                },
                component: () => import("../views/yymana/faqView.vue")
            }, {
                path: "/parkingorder",
                name: "parkingorder",
                meta: {
                    title: '停车订单'
                },
                component: () => import("../views/park/yydata/parkingOrder.vue")
            },
            {
                path: "/roadorder",
                name: "roadorder",
                meta: {
                    title: '路内订单'
                },
                component: () => import("../views/yydata/roadOrder.vue")
            }, {
                path: "/faqorder",
                name: "faqorder",
                meta: {
                    title: '问题订单'
                },
                component: () => import("../views/yydata/faqOrder.vue")
            }, {
                path: "/caruser",
                name: "caruser",
                meta: {
                    title: '车主用户'
                },
                component: () => import("../views/yydata/carUser.vue")
            }, {
                path: "/carnummana",
                name: "carnummana",
                meta: {
                    title: '车牌管理'
                },
                component: () => import("../views/yydata/carnumMana.vue")
            }, {
                path: "/trafficcarnummana",
                name: "trafficcarnummana",
                meta: {
                    title: '市交车牌管理'
                },
                component: () => import("../views/yydata/trafficCarnumMana.vue")
            }
            , {
                path: "/roadrealmonitor",
                name: "roadrealmonitor",
                meta: {
                    title: '路内实时监控'
                },
                component: () => import("../views/realdata/RoadRealMonitor.vue")
            }, {
                path: "/roadmap",
                name: "roadmap",
                meta: {
                    title: '地图路内'
                },
                //component: resolve => require(['../views/realdata/roadMap.vue'],resolve)
                component: () => import("../views/realdata/roadMap.vue")
            },
            {
                path: "/realparking",
                name: "realparking",
                meta: {
                    title: '路内实时停车'
                },
                component: () => import("../views/realdata/realParking.vue")
            },
            {
                path: "/parkinglistA",
                name: "parkinglistA",
                meta: {
                    title: '路内管理'
                },
                component: () => import("../views/park/area/parkingList.vue")
            }, {
                path: "/equipmentlistA",
                name: "equipmentlistA",
                meta: {
                    title: '停车场设备管理'
                },
                component: () => import("../views/park/area/equipmentList.vue")
            }, {
                path: "/monitorlist",
                name: "monitorlist",
                meta: {
                    title: '监控设备管理'
                },
                component: () => import("../views/park/area/monitorList.vue")
            }, {
                path: "/onsitemana",
                name: "onsitemana",
                meta: {
                    title: '收费员列表'
                },
                component: () => import("../views/xunjian/xunjianList.vue")
            },
            //  {
            //     path: "/onsitefeedback",
            //     name: "onsitefeedback",
            //     meta: {
            //         title: '收费员反馈'
            //     },
            //     component: () => import ("../views/xunjian/xzfankuiList.vue")
            // },
            {
                path: "/shensuenterA",
                name: "shensuenterA",
                meta: {
                    title: '申诉录入'
                },
                component: () => import("../views/park/yymana/shensuEnter.vue")
            }, {
                path: "/shensuApproveA",
                name: "shensuApproveA",
                meta: {
                    title: '申诉审核'
                },
                component: () => import("../views/park/yymana/shensuApprove.vue")
            }, {
                path: "/shensuorderA",
                name: "shensuorderA",
                meta: {
                    title: '申诉订单'
                },
                component: () => import("../views/park/yymana/shensuOrder.vue")
            }, {
                path: "/carnumenterA",
                name: "carnumenterA",
                meta: {
                    title: '申诉录入'
                },
                component: () => import("../views/park/yymana/carnumEnter.vue")
            }, {
                path: "/sbwarningA",
                name: "equipmentwarnA",
                meta: {
                    title: '设备报警'
                },
                component: () => import("../views/park/yymana/equipmentWarn.vue")
            }, {
                path: "/monitorEquipment",
                name: "monitorEquipment",
                meta: {
                    title: '视频监控'
                },
                component: () => import("../views/yunying/monitorEquipment.vue")
            }, {
                path: "/fenpeiroadA",
                name: "fenpeiroadA",
                meta: {
                    title: '分配停车场'
                },
                component: () => import("../views/park/yymana/fenpeiRoad.vue")
            }, {
                path: "/videoView",
                name: "videoView",
                meta: {
                    title: '监控视频查看'
                },
                component: () => import("../views/park/yymana/videoView.vue")
            }, {
                path: "/equipmentrizhiA",
                name: "equipmentrizhiA",
                meta: {
                    title: '设备离线日志'
                },
                component: () => import("../views/park/yymana/equipmentRizhi.vue")
            }, {
                path: "/yingshoumanaA",
                name: "yingshoumanaA",
                meta: {
                    title: '营收管理'
                },
                component: () => import("../views/park/caiwu/yingshouList.vue")
            }, {
                path: "/tklistA",
                name: "tklistA",
                meta: {
                    title: '退款记录'
                },
                component: () => import("../views/park/caiwu/tuikuanList.vue")
            }, {
                path: "/sfyczmanaA",
                name: "sfyczmanaA",
                meta: {
                    title: '收费员充值管理'
                },
                component: () => import("../views/park/caiwu/xjczList.vue")
            }, {
                path: "/cashList",
                name: "cashList",
                meta: {
                    title: '路内现金核销'
                },
                component: () => import("../views/caiwu/cashList.vue")
            }, {
                path: "/cashListA",
                name: "cashListA",
                meta: {
                    title: '停车场现金核销'
                },
                component: () => import("../views/park/caiwu/cashList.vue")
            }, {
                //     path: "/roadxjhxlist",
                //     name: "roadxjhxlist",
                //     meta: {
                //         title: '路内现金核销'
                //     },
                //     component: () => import ("../views/caiwu/roadxjhexiaoList.vue")
                // },{
                //     path: "/parkxjhxlist",
                //     name: "parkxjhxlist",
                //     meta: {
                //         title: '停车场现金核销'
                //     },
                //     component: () => import ("../views/caiwu/parkxjhexiaoList.vue")
                // },{
                path: "/quyutongjiA",
                name: "quyutongjiA",
                meta: {
                    title: '区域统计'
                },
                component: () => import("../views/park/yunying/areaTongji.vue")
            }, {
                path: "/tcctongjiA",
                name: "tcctongjiA",
                meta: {
                    title: '停车场统计'
                },
                component: () => import("../views/park/yunying/tccTongji.vue")
            }, {
                path: "/onsitestaticA",
                name: "onsitestaticA",
                meta: {
                    title: '收费员统计'
                },
                component: () => import("../views/park/yunying/sfyTongji.vue")
            }, {
                path: "/orderstaticA",
                name: "orderstaticA",
                meta: {
                    title: '订单基础数据'
                },
                component: () => import("../views/park/yunying/orderTongjione.vue")
            }, {
                path: "/orderstatictwoA",
                name: "orderstatictwoA",
                meta: {
                    title: '订单增长'
                },
                component: () => import("../views/park/yunying/orderTongjitwo.vue")
            }, {
                path: "/orderstaticthreeA",
                name: "orderstaticthreeA",
                meta: {
                    title: '订单状态占比'
                },
                component: () => import("../views/park/yunying/orderTongjithree.vue")
            }, {
                path: "/cashstaticA",
                name: "cashstaticA",
                meta: {
                    title: '现金统计'
                },
                component: () => import("../views/park/yunying/cashTongjione.vue")
            }, {
                path: "/cashstatictwoA",
                name: "cashstatictwoA",
                meta: {
                    title: '充值增长'
                },
                component: () => import("../views/park/yunying/cashTongjitwo.vue")
            }, {
                path: "/cashstaticthreeA",
                name: "cashstaticthreeA",
                meta: {
                    title: '缴费方式统计'
                },
                component: () => import("../views/park/yunying/cashTongjithree.vue")
            }, {
                path: "/cashstaticfourA",
                name: "cashstaticfourA",
                meta: {
                    title: '免单统计'
                },
                component: () => import("../views/park/yunying/cashTongjifour.vue")
            }, {
                path: "/invoicestaticA",
                name: "invoicestaticA",
                meta: {
                    title: '发票统计'
                },
                component: () => import("../views/park/yunying/fapiaoTongji.vue")
                // }, {
                //     path: "/arrearsstaticA",
                //     name: "arrearsstaticA",
                //     meta: {
                //         title: '欠费统计'
                //     },
                //     component: () => import ("../views/park/yunying/qianfeiTongji.vue")
            }, {
                path: "/parkingorderA",
                name: "parkingorderA",
                meta: {
                    title: '停车场订单'
                },
                component: () => import("../views/park/yydata/parkingOrder.vue")
            },
            {
                path: "/parkingOpengate",
                name: "parkingOpengate",
                meta: {
                    title: '遥控日志'
                },
                component: () => import("../views/park/yydata/parkingOpengate.vue")
            },
            {
                path: "/provisionalPasslist",
                name: "provisionalPasslist",
                meta: {
                    title: '临时通行证'
                },
                component: () => import("../views/park/yydata/provisionalPass.vue")
            }, {
                path: "/companyUsers",
                name: "companyUsers",
                meta: {
                    title: '公司管理'
                },
                component: () => import("../views/yydata/gsUser.vue")
            }, {
                path: "/companyUsersA",
                name: "companyUsersA",
                meta: {
                    title: '公司管理'
                },
                component: () => import("../views/park/yydata/companyUsersList.vue")
            }, {
                path: "/charge",
                name: "charge",
                meta: {
                    title: '出场收费'
                },
                component: () => import("../views/park/yydata/chargeList.vue")
            }, {
                path: "/providerecordA",
                name: "providerecordA",
                meta: {
                    title: '发票发放记录'
                },
                component: () => import("../views/park/caiwu/providerecord.vue")
            }, {
                path: "/dashboardA",
                name: "dashboardA",
                meta: {
                    title: '停车场概览'
                },
                component: () => import("../views/park/realdata/Dashboard.vue")
            }, {
                path: "/roadrealmonitorA",
                name: "roadrealmonitorA",
                meta: {
                    title: '停车场实时监控'
                },
                component: () => import("../views/park/realdata/RoadRealMonitor.vue")
            }, {
                path: "/roadmapA",
                name: "roadmapA",
                meta: {
                    title: '地图停车场'
                },
                //component: resolve => require(['../views/realdata/roadMap.vue'],resolve)
                component: () => import("../views/park/realdata/roadMap.vue")
            }, {
                path: "/realparkingA",
                name: "realparkingA",
                meta: {
                    title: '停车场实时停车'
                },
                component: () => import("../views/park/realdata/realParking.vue")
            }, {
                path: "/monthSetA",
                name: "monthSetA",
                meta: {
                    title: '包月配置'
                },
                component: () => import("../views/park/system/MonthSet.vue")
            }, {
                path: "/reportForm",
                name: "reportForm",
                meta: {
                    title: '运营报表'
                },
                component: () => import("../views/park/yunying/reportForm.vue")
            },
            {
                path: "/reportFormL",
                name: "reportFormL",
                meta: {
                    title: '运营报表'
                },
                component: () => import("../views/yunying/reportForm.vue")
            },
            {
                path: "/reportFormLNew",
                name: "reportFormLNew",
                meta: {
                    title: '运营统计'
                },
                component: () => import("../views/yunying/reportFormNew.vue")
            },
            {
                path: "/roadReportJfl",
                name: "roadReportJfl",
                meta: {
                    title: '路内缴费率统计'
                },
                component: () => import("../views/yunying/roadReportJfl.vue")
            }, {
                path: "/monthusermanaA",
                name: "monthusermanaA",
                meta: {
                    title: '包月用户管理'
                },
                component: () => import("../views/park/caiwu/baoyueuserList.vue")
            }, {
                path: "/baoyuetjA",
                name: "baoyuetjA",
                meta: {
                    title: '包月充值统计'
                },
                component: () => import("../views/park/caiwu/baoyueTonji.vue")
            },
            {
                path: "/business",
                name: "business",
                meta: {
                    title: '商家审核'
                },
                component: () => import("../views/business/examine.vue")
            },
            {
                path: "/businessFirm",
                name: "businessFirm",
                meta: {
                    title: '商家列表'
                },
                component: () => import("../views/business/firm.vue")
            },
            {
                path: "/businessOrder",
                name: "businessOrder",
                meta: {
                    title: '订单列表'
                },
                component: () => import("../views/business/order.vue")
            },
            {
                path: "/businessRecord",
                name: "businessRecord",
                meta: {
                    title: '充值记录'
                },
                component: () => import("../views/business/record.vue")
            },
            {
                path: "/businessDiscount",
                name: "businessDiscount",
                meta: {
                    title: '优惠配置'
                },
                component: () => import("../views/business/discount.vue")
            },
            {
                path: "/businessInvoicing",
                name: "businessInvoicing",
                meta: {
                    title: '开票管理'
                },
                component: () => import("../views/business/invoicing.vue")
            },
            {
                path: "/attendance",
                name: "attendance",
                meta: {
                    title: '考勤管理'
                },
                component: () => import("../views/xunjian/attendance.vue")
            },
            {
                path: "/attendancePerson",
                name: "attendancePerson",
                meta: {
                    title: '考勤月统计'
                },
                component: () => import("../views/xunjian/attendancePerson.vue")
            },
            {
                path: "/attendance1",
                name: "attendance1",
                meta: {
                    title: '考勤管理'
                },
                component: () => import("../views/xunjian/attendance1.vue")
            },
            {
                path: "/attendancePerson1",
                name: "attendancePerson1",
                meta: {
                    title: '考勤月统计'
                },
                component: () => import("../views/xunjian/attendancePerson1.vue")
            }
        ]
    }, {
        path: "/login",
        name: "Login",
        meta: {
            title: '登录'
        },
        component: () => import("../views/Login.vue")
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    let token = localStorage.getItem("token_value");
    if (to.name === 'Login') {
        next();
    } else {
        //debugger
        if ('null' == token || null == token) {

            next('/login');
        } else {
            next();
        }
    }
    // document.title = `${to.meta.title} | 智慧停车管理平台`;
    // const role = localStorage.getItem('ms_username');
    // //if (to.matched.length == 0){router.push(to.path);}
    // if (!role && to.path !== '/login') {
    //     next('/login');
    // } else if (to.meta.permission) {
    //     // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
    //     role === 'admin'
    //         ? next()
    //         : next('/403');
    // } else {
    //     next();
    // }
});

export default router;
