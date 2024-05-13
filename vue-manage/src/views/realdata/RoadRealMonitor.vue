<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="5">
                <el-card shadow="hover" style="min-height: 100%;">
                    <template #header>
                        <div class="clearfix">
                            <span>路内实时监控</span>
                        </div>
                    </template>

                    <div class="text item">
                        <el-tree :data="treeTable" :props="defaultProps" @node-click="handleNodeClick"
                                 :expand-on-click-node="false"></el-tree>
                    </div>
                </el-card>


            </el-col>
            <el-col :span="19">
                <div class="container">

                    <el-row class='mt-20'>
                        <el-col :span="24">
                            <el-card shadow="hover">
                                <div id="cardata" class="schart"></div>
                            </el-card>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col :span="12" class="card-bgimg-box">
                            <el-card shadow="hover">
                                <el-row :gutter="20" class="card-bgimg">
                                    <el-col :span="12">
                                        <div class="li-data">
                                            <h4>{{ census.dueMoney }}</h4>
                                            <h6>应收款(元)</h6>
                                        </div>
                                    </el-col>
                                    <el-col :span="12">
                                        <div class="li-data">
                                            <h4>{{ census.receiveMoney }}</h4>
                                            <h6>实收款(元)</h6>
                                        </div>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20" class="card-bgimg">
                                    <el-col :span="8">
                                        <div class="li-data li-data-b">
                                            <h4>{{ census.memberCount }}</h4>
                                            <h6>会员停车数(个)</h6>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div class="li-data li-data-b">
                                            <h4>{{ census.outsiderCount }}</h4>
                                            <h6>非会员停车数(个)</h6>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div class="li-data li-data-b">
                                            <h4>{{ census.arrearagePark }}</h4>
                                            <h6>欠费停车数(个)</h6>
                                        </div>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20" class="card-bgimg">
                                    <el-col :span="6">
                                        <div class="li-data li-data-c">
                                            <h4>{{ census.utilizeRatio }}</h4>
                                            <h6>泊位使用率(%)</h6>
                                        </div>
                                    </el-col>
                                    <el-col :span="6">
                                        <div class="li-data li-data-c">
                                            <h4>{{ census.velocityRatio }}</h4>
                                            <h6>泊位周转率(%)</h6>
                                        </div>
                                    </el-col>
                                    <el-col :span="6">
                                        <div class="li-data li-data-c">
                                            <h4>{{ census.free }}</h4>
                                            <h6>空余泊位(个)</h6>
                                        </div>
                                    </el-col>
                                    <el-col :span="6">
                                        <div class="li-data li-data-c">
                                            <h4>{{ census.normal }}</h4>
                                            <h6>总泊位(个)</h6>
                                        </div>
                                    </el-col>
                                </el-row>
                            </el-card>
                        </el-col>
                        <el-col :span="12">
                            <el-card shadow="hover">
                                <div id="mainline44" class="schart"></div>
                            </el-card>
                        </el-col>

                    </el-row>
                </div>
            </el-col>
        </el-row>

    </div>
</template>

<script>
import {reactive, ref} from "vue";
import {monitoringCount, treeStructureList,} from "../../api/index";

import Schart from "vue-schart";
// 全局引入
import * as echarts from "echarts";


export default {
    name: "roadrealmonitor",
    components: {
        Schart
    },
    setup() {
        const name = localStorage.getItem("ms_username");
        const role = name === "admin" ? "超级管理员" : "普通用户";

        const treeTable = ref([]);
        const query = reactive({
            variance: '',
            variance2: '',
            variance3: '',

        });
        const census = reactive({
            normal: '',
            free: '',
            utilizeRatio: '',
            dueMoney: '',
            receiveMoney: '',
            velocityRatio: '',
            memberCount: '',
            outsiderCount: '',
            arrearagePark: '',

        });

        // 获取表格数据
        const getTreeStructure = () => {
            treeStructureList(reactive({})).then((res) => {
                var data = res.data;
                console.log(data);
                treeTable.value = data;
            });
        };
        getTreeStructure();

        //泊位使用率
        function getPercent(num, total) {
            num = parseFloat(num);
            total = parseFloat(total);
            if (isNaN(num) || isNaN(total)) {
                return "-";
            }
            return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00) + "%";
        }

        const getMonitoringCount = () => {
            monitoringCount(query).then((res) => {
                console.log(res);
                var data = res.data;
                //驶入驶出车辆
                options2.xAxis[0].data = data.leaveEntryHour.hours;
                options2.series[0].data = data.leaveEntryHour.entrys;
                options2.series[1].data = data.leaveEntryHour.leaves;
                var chartDom = document.getElementById("cardata");
                var myChart1 = echarts.init(chartDom);
                myChart1.setOption(options2);
                //收款统计
                options44.series[0].data = res.data.collection;
                var chartDoms44 = document.getElementById("mainline44");
                var myCharts44 = echarts.init(chartDoms44);
                myCharts44.setOption(options44);
                //应收款
                census.dueMoney = res.data.dueMoney;
                //实收款
                census.receiveMoney = res.data.receiveMoney;
                //会员停车数(个) 非会员停车数(个)
                census.memberCount = res.data.memberOutsider.memberCount;
                census.outsiderCount = res.data.memberOutsider.outsiderCount;
                //欠费停车数(个)
                census.arrearagePark = res.data.arrears;
                //泊位使用率(%) 空余泊位(个) 总泊位(个)
                console.log("------------------------------------")
                census.normal = res.data.deviceStatistics.normal;//总泊位数
                census.parked = res.data.deviceStatistics.abnormal ;//在停车辆
                census.free = res.data.deviceStatistics.inuse;//空闲车位
                census.utilizeRatio = getPercent(census.parked, census.normal);
                //泊位周转率(%)
                census.velocityRatio = res.data.utilizeRatio;
            });
        };
        getMonitoringCount();

        // 树形菜单点击事件
        const handleNodeClick = (data, Node) => {
            console.log(data);
            if (data.grade === 0) {
                query.variance = '';
                query.variance2 = '';
                query.variance3 = '';
            } else if (data.grade === 1) {
                query.variance = data.id;
                query.variance2 = '';
                query.variance3 = '';
            } else if (data.grade === 2) {
                query.variance = data.areaId;
                query.variance2 = data.id;
                query.variance3 = '';
            } else {
                query.variance = data.areaId;
                query.variance2 = data.streetId;
                query.variance3 = data.id;
            }
            getMonitoringCount();
        };

        const options2 = {
            color: ['#80FFA5', '#00DDFF'],
            title: {
                text: '驶入驶出车辆',
                textStyle: {
                    fontSize: 16,
                    fontWeight: "normal",
                    color: "#303133",
                },
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                data: ['驶入车辆', '驶出车辆']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [{
                type: 'category',
                boundaryGap: false,
                data: []
            }],
            yAxis: [{
                type: 'value'
            }],
            series: [{
                name: '驶入车辆',
                type: 'line',
                stack: 'Total',
                smooth: true,
                lineStyle: {
                    width: 0
                },
                showSymbol: false,
                areaStyle: {
                    opacity: 0.8,
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: 'rgb(128, 255, 165)'
                    },
                        {
                            offset: 1,
                            color: 'rgb(1, 191, 236)'
                        }
                    ])
                },
                emphasis: {
                    focus: 'series'
                },
                data: []
            },
                {
                    name: '驶出车辆',
                    type: 'line',
                    stack: 'Total',
                    smooth: true,
                    lineStyle: {
                        width: 0
                    },
                    showSymbol: false,
                    areaStyle: {
                        opacity: 0.8,
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgb(0, 221, 255)'
                        },
                            {
                                offset: 1,
                                color: 'rgb(77, 119, 255)'
                            }
                        ])
                    },
                    emphasis: {
                        focus: 'series'
                    },
                    data: []
                }
            ]
        };

        const options44 = {
            title: {
                text: '订单收款统计',
                textStyle: {
                    fontSize: 16,
                    fontWeight: "normal",
                    color: "#303133",
                },
            },
            legend: {
                orient: 'vertical',
                right: 'left'
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c}元 ({d}%)'
            },
            series: [
                {
                    name: '订单收款统计',
                    type: 'pie',
                    radius: [30, 110],
                    center: ['50%', '50%'],
                    roseType: 'area',
                    itemStyle: {
                        borderRadius: 8
                    },
                    data: [
                        {value: 40, name: '微信'},
                        {value: 38, name: '支付宝'},
                        {value: 32, name: '现金'},
                        {value: 30, name: '钱包'},
                        {value: 28, name: '包月'},
                        {value: 26, name: '扫码付'},
                        {value: 22, name: '月结'}
                    ]
                }
            ]
        };


        return {
            treeTable,
            query,
            handleNodeClick,
            census,

            name,
            options2,
            //options3,
            options44,
            role,
        };
    },
    methods: {},
    mounted() {
        //this.init();
    },
};
</script>

<style scoped>
.card-bgimg .li-data {
    flex: 1;
    text-align: center;
    color: #fff;
    padding: 13px 0;
    margin-bottom: 1.25rem;
    background-color: #e6edfe;
    border: 1px solid #a7bffd;
    border-radius: 6px;
    color: #5583fd;
}

.card-bgimg .li-data.li-data-b {
    background-color: #fff6dc;
    border: 1px solid #f8ce4b;
    color: #d8a404;
}

.card-bgimg .li-data.li-data-c {
    background-color: #e8f5e2;
    border: 1px solid #a7d492;
    color: #598445;
    margin-bottom: 3px;
}

.card-bgimg .li-data h4 {
    font-size: 24px;
    height: 34px;
    line-height: 34px;
}

.card-bgimg .li-data h6 {
    font-size: 14px;
    font-weight: normal;
    height: 24px;
    line-height: 24px;
}

.mt-20 {
    margin-bottom: 20px;
}


.circal {
    background: #409eff;
    color: #fff;
    border-radius: 10px;
    width: 20px;
    margin: auto;
}

.circal-top {
    background: #f56c6c;
}

.schart {
    width: 100%;
    height: 300px;
}
</style>
