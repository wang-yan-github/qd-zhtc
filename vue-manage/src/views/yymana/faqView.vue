<template>
    <div class="container" ref="viewpage">
        <div class="content-con" v-show="showViews">
            <span v-html="contentView"></span>
            <!--<p>-->
            <!--<span style="color: #595959"-->
            <!--&gt;<strong>&nbsp; &nbsp; &nbsp;如使用智慧泊车？&nbsp;</strong></span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p>-->
            <!--<span style="color: #595959"-->
            <!--&gt;1.首次使用扫描二维码进入民和智慧停车小程序手机号码登入注册界面;同时绑定您的车牌号码，即可完成操作。&nbsp;</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p>-->
            <!--<span style="color: #595959"-->
            <!--&gt;2.之后车辆驶入驶离“民和智慧停车”智能泊位，均可在小程序内查询入位和驶离信息，点击缴费完成操作。（绑定成功后系统将会向您微信推送停车信息）&nbsp;</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p>-->
            <!--<span style="color: #595959"><br/></span>-->
            <!--</p>-->
            <!--<p>-->
            <!--<span style="color: #595959"></span>-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"-->
            <!--&gt;<span style="font-weight: 700"-->
            <!--&gt;&nbsp;&nbsp;如何绑定车牌？&nbsp;</span-->
            <!--&gt;</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"-->
            <!--&gt;1.进入界面后再右下角点击个人中心→我的车牌→绑定您的车牌。</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"-->
            <!--&gt;&nbsp;2。如果您遇到车牌已被绑定的情况，您可以联系我们的公众号客服，向其提供行驶证和驾驶证照片即可进行申诉。&nbsp;</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"><br/></span>-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"></span>-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"-->
            <!--&gt;<span style="font-weight: 700"-->
            <!--&gt;&nbsp; &nbsp; 如何缴费？&nbsp;</span-->
            <!--&gt;</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"-->
            <!--&gt;1.关注并已经绑定车牌的车辆，可使用微信公众号或者微信小程序直接微信支付，完成缴费；</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"-->
            <!--&gt;&nbsp;2.通过路面巡检人员手持PDA，现金或扫码支付单次订单。</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<br/>-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"-->
            <!--&gt;<span style="font-weight: 700"-->
            <!--&gt;&nbsp; &nbsp; 怎样替亲朋好友缴费？不绑定车牌也可以缴费</span-->
            <!--&gt;</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p style="white-space: normal">-->
            <!--<span style="color: rgb(89, 89, 89)"-->
            <!--&gt;&nbsp;您可以在自己的小程序账号上进行操作：进入主页-点击按钮“停车缴费”-输入您朋友的车牌号，这时页面只会显示当面该车牌的停车订单信息，点击支付即可，不会影响他人正常绑定同时也可以完成缴费。</span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p>-->
            <!--<span style="color: #595959"><br/></span>-->
            <!--</p>-->
            <!--<p>-->
            <!--<span style="color: #595959"-->
            <!--&gt;<strong>&nbsp; &nbsp; 怎样查询订单？&nbsp;</strong></span-->
            <!--&gt;-->
            <!--</p>-->
            <!--<p>-->
            <!--<span style="color: #595959">进入微信小程序→→缴费即可查询&nbsp;</span>-->
            <!--</p>-->
            <!--<p>-->
            <!--<br/>-->
            <!--</p>-->
        </div>

        <div class="content-con" v-show="showContent">
            <Ueditor
                    ref="ue"
                    :value="form.content"
                    :ueditorConfig="uform.config"
            ></Ueditor>
        </div>
        <div class="mt20"></div>
        <el-button type="primary" @click="goTo" v-permission="['road_aqview_edit', 'park_aqview_edit']">{{message}}</el-button>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import Ueditor from "../../components/UE.vue";
    import {
        editSysResource,
        questDetails,
    } from "../../api/sysResourceIndex.js";

    export default {
        name: "faqview",
        components: {
            Ueditor,
        },
        setup() {
            let uform = reactive({
                config: {initialFrameHeight: 500, initialFrameWidth: "100%"},
            });

            const contentView = ref("");
            const form = ref({});

            const message = ref("编辑");
            const isShow = ref(true);
            const showViews = ref(true);//true为显示
            const showContent = ref(false);//false为隐藏

            questDetails(reactive({category: "2"})).then((res) => {
                form.value = res.data;
                contentView.value = res.data.content;
            });

            return {
                contentView,
                form,
                uform,
                isShow,
                message,
                showViews,
                showContent,
            };
        },
        methods: {
            goTo() {
                this.isShow = !this.isShow;
                if (this.isShow) {
                    let flag = 0;
                    //获取文本编辑器内容
                    this.form.content = this.$refs.ue.getUEContent();
                    //提交数据
                    editSysResource(this.form)
                        .then((res) => {
                            flag = res.code;
                        })
                        .then(() => {
                            if (flag == 0) {
                                ElMessage.success("保存成功");

                                this.contentView = this.$refs.ue.getUEContent();

                                this.message = "编辑";
                                this.showViews = true;//false隐藏
                                //显示文本编辑器
                                this.showContent = false;//true为显示
                            } else if (flag == -1) {
                                ElMessage.error("操作失败!");
                            }
                        });
                } else {
                    //跳转到文本编辑器
                    questDetails(reactive({category: "2"})).then((res) => {
                        this.message = "保存";
                        this.showViews = false;//false隐藏
                        //显示文本编辑器
                        this.showContent = true;//true为显示

                        this.form = res.data;
                        //重新渲染赋值文本编辑器内容
                        this.$refs.ue.setText(res.data.content);
                        this.contentView = res.data.content;
                    });

                }
            },
        }
    };
</script>
<style scoped>
</style>