<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta content="telephone=no" name="format-detection"/>
    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover"
    />
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <title>临时通行证</title>
    <#--部署注意：以下引用文件在zhtc1服务器上html文件夹内部署更新-->
    <script src="/provisionalPass/vue.global.js"></script>
    <script src="/provisionalPass/axios.min.js"></script>
    <link href="/provisionalPass/style.css" rel="stylesheet" type="text/css"/>
    <link href="/provisionalPass/keyboard.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<input type="hidden" id="id" name="id" value="${id}">
<div id="app">
    <!-- 有优惠券情况 -->
    <div class="card" v-if="isYx == '1'">
        <h4 class="card-title">{{addForm.park_name}}</h4>
        <div class="card-center">
            <div class="card-desc">停车费用</div>
        </div>
        <div class="card-timer">{{addForm.expire_time}}前可用</div>
    </div>

    <!-- 免费停车券失效 -->
    <div class="card noCard" v-if="isYx == '0'">
        <div class="card-center">
            <div class="card-desc desc">{{ msg }}</div>
        </div>
    </div>

    <!-- 车牌号 -->
    <div class="form" v-if="isYx == '1'">
        <div class="title">输入车牌号查询订单</div>
        <div class="form-input">
            <div
                    :class="['input',index == activeIndex ? 'active':'']"
                    v-for="(item, index) in inputValue"
                    :key="index"
                    @click="() => {inputClick(index)}"
            >
                <span v-if="index < licensePlateLength">{{ item  }}</span>
            </div>
        </div>
        <div class="check-box">
            <input type="checkbox" name="黄牌" id="" @change="(e) => hanlderChecked(e)"/>
            <span>黄牌</span>
        </div>
        <button class="form-submit" @click="onSubmit">立即查询</button>
    </div>
    <!-- toast -->
    <transition name="fade" v-if="isYx == '1'">
        <div class="toast" v-if="toast.show">
            <img src="/provisionalPass/icon_happy.png" v-if="toast.type == 'success'" class="icon" alt="" srcset=""/>
            <img src="/provisionalPass/icon_angry.png" v-else-if="toast.type == 'fail'" class="icon" alt="" srcset=""/>
            <span class="content">{{toast.content}}</span>
        </div>
    </transition>
    <!--  软键盘 -->
    <div class="license-plate_container" v-if="isYx == '1'">
        <!-- 键盘 -->
        <transition name="keyboard">
            <div class="keyboard_container" v-if="visible">
                <div class="keyboard_header" @click.stop="visibleClick">
                    <i class="iconfont icon-jianpan"></i>
                </div>
                <div class="keyboard_body">
                    <div class="province_list" v-if="activeIndex==0">
                        <div
                                v-for="item in provinceList"
                                :key="item"
                                class="key_wrapper"
                                @click="btnClick(item)"
                        >
                            <div class="button">{{ item }}</div>
                        </div>
                    </div>
                    <div class="number_list" v-if="activeIndex!=0">
                        <div
                                class="numer_wapper"
                                v-for="(item,index) of numberList"
                                :key="item"
                                @click="btnClick(item,activeIndex==1)"
                        >
                            <div
                                    class="button"
                                    :class="activeIndex==1 && index<10?'disabled':''"
                            >
                                {{ item }}
                            </div>
                        </div>
                    </div>
                    <div class="number_list" v-if="activeIndex!=0">
                        <div
                                class="numer_wapper"
                                v-for="item of letterList"
                                :key="item"
                                @click="btnClick(item,item=='O'&&activeIndex>1)"
                        >
                            <div
                                    class="button"
                                    :class="item=='O' && activeIndex > 1?'disabled':''"
                            >
                                {{ item }}
                            </div>
                        </div>
                        <div class="numer_wapper delete" @click="delClick">
                            <div class="button img">
                                <i class="iconfont icon-backspace"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </transition>
    </div>
    <!-- 软键盘结束 -->

    <!-- 订车订单dialog -->

    <div class="dialog" :style="dialog.show ? 'display:block':'display:none'">
        <transition name="fade">
            <div class="dialog_bg" v-if="dialog.show"></div>
        </transition>
        <transition name="fade">
            <div class="dialog_main" v-if="dialog.show">
                <div class="car_order">
                    <span class="car_order_title">停车订单</span>
                    <#--<img src="" alt="" srcset=""/>-->
                    <div class="cell">
                        <div class="cell-item">
                            <span class="name">车牌号：</span>
                            <span class="value">{{orderDialog.carNo}}</span>
                        </div>
                        <div class="cell-item">
                            <span class="name">进场时间：</span>
                            <span class="value">{{orderDialog.drivein_time}}</span>
                        </div>
                        <div class="cell-item">
                            <span class="name">停车时长：</span>
                            <span class="value">{{orderDialog.stayTime}}</span>
                        </div>
                    </div>
                    <button type="button" class="order_btn" @click="handlerConfirmFree">使用免费停车券</button>
                </div>
                <div class="dialog_close">
                    <img
                            src="/provisionalPass/close_dialog.png"
                            @click="hide"
                            alt=""
                            srcset=""
                    />
                </div>
            </div>
        </transition>
    </div>
    <!-- 确认使用优惠券dialog -->
    <div
            class="dialog"
            :style="dialog2.show ? 'display:block':'display:none'"
    >
        <transition name="fade">
            <div class="dialog_bg" v-if="dialog2.show" @click="hide1"></div>
        </transition>
        <transition name="fade">
            <div class="dialog_main" v-if="dialog2.show">
                <div class="dialog_confirm">
                    <div>
                        <div class="confirm_title">停车订单</div>
                        <div class="confirm_content">
                            使用后超过{{overTime}}分钟未离场，需重新计费。
                        </div>
                    </div>
                    <div>
                        <button
                                type="button"
                                class="order_btn confirm"
                                @click="handlerConfirm"
                        >
                            确定使用
                        </button>
                        <button
                                type="button"
                                class="order_btn cancel"
                                @click="handlerCancel"
                        >
                            取消
                        </button>
                    </div>
                </div>
            </div>
        </transition>
    </div>
</div>
</body>
<script type="text/javascript">
    !(function (e, t) {
        function n() {
            t.body
                ? (t.body.style.fontSize = 12 * o + "px")
                : t.addEventListener("DOMContentLoaded", n);
        }

        function d() {
            var e = i.clientWidth / 10;
            i.style.fontSize = e + "px";
        }

        var i = t.documentElement,
            o = e.devicePixelRatio || 1;
        if (
            (n(),
                d(),
                e.addEventListener("resize", d),
                e.addEventListener("pageshow", function (e) {
                    e.persisted && d();
                }),
            o >= 2)
        ) {
            var a = t.createElement("body"),
                s = t.createElement("div");
            (s.style.border = ".5px solid transparent"),
                a.appendChild(s),
                i.appendChild(a),
            1 === s.offsetHeight && i.classList.add("hairlines"),
                i.removeChild(a);
        }
    })(window, document);
</script>
<script type="text/javascript">
    const {createApp, ref, reactive, toRefs} = Vue;
    const LICENSE_PLATE_RULE =
        /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4,5}[A-HJ-NP-Z0-9挂学警港澳]$/;
    const useRunJianPan = () => {

        const pageData = reactive({
            // 长度
            licensePlateLength: 8,
            // 城市
            provinceList: [
                "京",
                "津",
                "渝",
                "沪",
                "冀",
                "晋",
                "辽",
                "吉",
                "黑",
                "苏",
                "浙",
                "皖",
                "闽",
                "赣",
                "鲁",
                "豫",
                "鄂",
                "湘",
                "粤",
                "琼",
                "川",
                "贵",
                "云",
                "陕",
                "甘",
                "青",
                "蒙",
                "桂",
                "宁",
                "新",
                "藏",
                "临",
            ],
            // 软件盘数字列表
            numberList: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, "警"],
            letterList: [
                "Q",
                "W",
                "E",
                "R",
                "T",
                "Y",
                "U",
                "O",
                "P",
                "港",
                "澳",
                "A",
                "S",
                "D",
                "F",
                "G",
                "H",
                "J",
                "K",
                "L",
                "挂",
                "学",
                "Z",
                "X",
                "C",
                "V",
                "B",
                "N",
                "M",
                "领",
            ],
            activeIndex: 0,
            // 输入绑定值
            inputValue: ["", "", "", "", "", "", "", ""],
            // 显示软键盘
            visible: true,
            // 车牌号
            modelValue: "",
            //是否自动显示
            autoShow: false,
            // 输入框边框颜色
            borderColor: "#79aef3",
            // 输入框选中边框颜色
            borderActiveColor: "#330aec",
            // 边框圆角
            borderRadius: 6,
            // 边框宽度
            borderWidth: 1,
            // 文字颜色
            fontColor: "#333333",
            // 文字大小
            fontSize: 16,
        });

        const methods = {
            initLicensePlate() {
                if (
                    pageData.modelValue &&
                    LICENSE_PLATE_RULE.test(pageData.modelValue)
                ) {
                    pageData.inputValue = pageData.modelValue.split("");
                }
            },
            onSlotClick() {
                let length = pageData.inputValue.filter((it) => it).length;
                pageData.activeIndex = length == 0 ? 0 : length - 1;
                if (!pageData.visible) {
                    pageData.visible = true;
                }
            },
            inputClick(index) {
                pageData.activeIndex = index;
                if (!pageData.visible) {
                    pageData.visible = true;
                }
            },
            btnClick(val, disabled) {
                if (disabled) {
                    return false;
                }
                pageData.inputValue[pageData.activeIndex] = val;
                pageData.activeIndex++;
                if (pageData.activeIndex >= pageData.licensePlateLength) {
                    pageData.activeIndex = pageData.licensePlateLength - 1
                    pageData.visible = false;
                }
                methods.change();
            },
            // 删除
            delClick() {
                if (pageData.inputValue[pageData.activeIndex]) {
                    pageData.inputValue[pageData.activeIndex] = ``;
                } else {
                    pageData.inputValue[pageData.activeIndex - 1] = ``;
                    pageData.activeIndex--;
                }
                methods.change();
            },
            visibleClick() {
                pageData.visible = false;
            },
            change() {
                let value = pageData.inputValue.join("");
                pageData.modelValue = value;

            },
        };
        return {pageData, methods};
    };

    const useDialog = () => {
        const dialog = reactive({
            show: false,
        });

        const operations = {
            show() {
                dialog.show = true;
            },
            hide() {
                dialog.show = false;
            },
        };
        return {dialog, operations};
    };

    const useToast = () => {
        const toast = reactive({
            show: false,
            // success,fail
            type: 'success',
            // 内容
            content: '',
            // 显示时间
            duraton: 2000,
            // 定时器
            timer: null
        })

        const toastCommon = (type, content) => {
            if (toast.show && toast.timer) {
                clearTimeout(toast.timer)
            }
            toast.content = content
            toast.type = type
            toast.show = true
            toast.timer = setTimeout(() => {
                toast.show = false
            }, toast.duraton)
        }

        const success = (content) => {
            toastCommon('success', content)
        }

        const fail = (content) => {
            toastCommon('fail', content)
        }
        return {toast, toastCommon, success, fail}
    }

    const app = createApp({
        setup() {
            let addForm = reactive({
                id: "",
                park_id: "",
                hxCount: "",
                expire_time: "",
                type: "",
                repeat_type: "",
                limit_time: "",
                park_name: "",
            });
            let overTime = ref("");

            // let url = ref("http://192.168.0.104:8081/");
            let url = ref("https://zhtc.aldwxa.top/api/");

            const orderDialog = reactive({
                id: '',
                drivein_time: '',
                stayTime: '',
                carNo: ''
            })

            // 是否为黄牌
            const isYellowCard = ref(false)
            // 车牌号软键盘
            const {pageData, methods} = useRunJianPan();
            // 停车订单弹框控制
            const {dialog, operations: {show, hide}} = useDialog();

            // toast
            const {toast, success, fail} = useToast()

            // 确认使用优惠券弹框控制
            const {
                dialog: dialog2,
                operations: {show: show1, hide: hide1},
            } = useDialog();

            const handlerConfirm = () => {
                // 确认使用
                axios({
                    method: 'post',
                    url: url.value + "provisionalPassRecord/onSave",
                    headers: {'content-type': 'application/x-www-form-urlencoded'},
                    data: {
                        id: ${id},
                        orderId: orderDialog.id,
                    }
                }).then((res) => {
                    if (res.data.code == 0) {
                        hide1();
                        success('使用成功，请在' + overTime.value + '分钟内离场，否则将继续收取停车费用。')
                    } else {
                        return fail(res.data.msg)
                    }
                });
            };

            const handlerCancel = () => {
                hide1();
            };

            // 黄牌选中
            const hanlderChecked = (e) => {
                isYellowCard.value = e.target.checked
            }

            //立即查询
            const onSubmit = () => {
                console.log(pageData)
                if (pageData.modelValue.length < 7) {
                    return fail("请输入车牌号码")
                }
                if (LICENSE_PLATE_RULE.test(pageData.modelValue)) {
                    //车牌类型(1蓝牌、2绿牌、3黄牌)
                    let type = "1";
                    // 黄牌
                    if (pageData.modelValue.length == 7 && isYellowCard.value) {
                        type = "3";
                    } else if (pageData.modelValue.length == 7) {
                        // 蓝牌
                        type = "1";
                    } else if (pageData.modelValue.length == 8) {
                        // 绿牌
                        type = "2";
                    } else {
                        return fail("格式错误")
                    }
                    axios({
                        method: 'post',
                        url: url.value + "provisionalPassRecord/searchOrder",
                        headers: {'content-type': 'application/x-www-form-urlencoded'},
                        data: {
                            parkId: addForm.park_id,
                            carNo: pageData.modelValue,
                            type: type,
                        }
                    }).then((res) => {
                        if (res.data.code == 0) {
                            // 显示订单弹框
                            Reflect.ownKeys(orderDialog).forEach(item => {
                                Reflect.set(orderDialog, item, Reflect.get(res.data.data, item))
                            });
                            console.log(orderDialog)
                            show()
                        } else {
                            return fail(res.data.msg)
                        }
                    });
                } else {
                    return fail("格式错误")
                }
            }

            const handlerConfirmFree = () => {
                // hide()
                show1()
            }
            const isYx = ref("0");
            const msg = ref("本免费停车券已失效");
            /**
             * 验证该临时通行证
             * 单次核销停车券已被使用或多次核销停车券次数为0时，则提示本免费停车券已失效。
             */
            const getData = () => {
                axios({
                    method: 'post',
                    url: url.value + "provisionalPassRecord/checkCode",
                    headers: {'content-type': 'application/x-www-form-urlencoded'},
                    data: {
                        id: ${id}
                    }
                }).then((res) => {
                    if (res.data.code == 0) {
                        isYx.value = "1";
                        overTime.value = res.data.data.limit_time;
                        Reflect.ownKeys(addForm).forEach(item => {
                            Reflect.set(addForm, item, Reflect.get(res.data.data, item))
                        })
                    } else {
                        isYx.value = "0";
                        msg.value = res.data.msg;
                    }
                    console.log(addForm)
                });
            };
            getData();

            return {
                dialog,
                dialog2,
                toast,
                show1,
                hide1,
                show, hide,
                ...toRefs(pageData),
                ...methods,
                orderDialog,
                handlerConfirm,
                handlerCancel,
                hanlderChecked,
                success,
                fail,
                onSubmit,
                getData,
                isYx,
                msg,
                addForm,
                handlerConfirmFree,
                overTime,
                url,

            };
        },
    }).mount("#app");
</script>
</html>
