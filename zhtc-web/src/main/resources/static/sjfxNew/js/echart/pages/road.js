// window.parent.showLoading();

window.onresize = function () {
    // resizeDiv();
}
// window.onload = function () {
// 	window.parent.hideLoading();

// 	//loadPage();
//     resizeDiv();

// }
let color = [
    "#16dada",
    "#0090FF",
    "#FFC005",
    '#0064ff',
    '#0ec1ff',
    "#f4a762",
    "#8B5CFF",
    "#00CA69",
    "#36CE9E",
    "#f25306"
];
const hexToRgbaLineOption = (hex, opacity) => {
    let rgbaColor = "";
    let reg = /^#[\da-f]{6}$/i;
    if (reg.test(hex)) {
        rgbaColor = `rgba(${parseInt("0x" + hex.slice(1, 3))},${parseInt(
            "0x" + hex.slice(3, 5)
        )},${parseInt("0x" + hex.slice(5, 7))},${opacity})`;
    }
    return rgbaColor;
}
// 昨日停车时长占比--饼图--开始
const yesterdayStopCarPie = echarts.init(document.getElementById('yesterdayStopCarPie'));
var tccsNameList = ["0-2小时", "2-4小时", "4-6小时", "6-8小时", "8小时以上"];
var yesterdayStopCarPieOption = {
    legend: {
        orient: 'vertical',
        top: "center",
        right: "5%",
        data: tccsNameList,
        textStyle: {
            color: "#fff",
            fontSize: 12
        }
    },
    tooltip: {
        trigger: 'item',
        axisPointer: {
            type: 'shadow'
        },
        extraCssText: 'background: #05183a; border-radius: 0;box-shadow: 0 0 3px rgba(13, 44, 100, 0.8);color: #fff;border:1px solid #041129',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    series: [{
        name: '停车时长',
        type: 'pie',
        radius: ['30%', '80%'],
        center: ['40%', '50%'],
        // roseType: 'radius',
        label: {
            show: false,
            normal: {
                position: 'outside',
                fontSize: 12
            }
        },
        labelLine: {
            length: 1,
            length2: 20,
            smooth: true
        },
        data: [{
            value: 235,
            name: '0-2小时',
            label: {
                color: '#fff'
            },
            itemStyle: {
                color: "rgba(50,123,250,0.7)",
                borderColor: "rgba(50,123,250,1)",
                borderWidth: 1
            }
        },
            {
                value: 535,
                name: '2-4小时',
                label: {
                    color: '#fff'
                },
                itemStyle: {
                    color: "rgba(244,201,7,0.7)",
                    borderColor: "rgba(244,201,7,1)",
                    borderWidth: 1
                }
            },
            {
                value: 324,
                name: '4-6小时',
                label: {
                    color: '#fff'
                },
                itemStyle: {
                    color: "rgba(23,216,161,0.7)",
                    borderColor: "rgba(23,216,161,1)",
                    borderWidth: 1
                }
            },
            {
                value: 678,
                name: '6-8小时',
                label: {
                    color: '#fff'
                },
                itemStyle: {
                    color: "rgba(122,60,235,0.7)",
                    borderColor: "rgba(122,60,235,1)",
                    borderWidth: 1
                }
            },
            {
                value: 567,
                name: '8小时以上',
                label: {
                    color: '#fff'
                },
                itemStyle: {
                    color: "rgba(15,197,243,0.7)",
                    borderColor: "rgba(15,197,243,1)",
                    borderWidth: 1
                }
            }
        ]
    }]
};
yesterdayStopCarPie.setOption(yesterdayStopCarPieOption, true)

/**
 * 停车场/路侧
 * 昨日停车时长占比分析
 */
getParkingTimeData();

function getParkingTimeData() {
    $.ajax({
        url: '/sjfxNew/getParkingTimeData.json',
        type: "POST",
        data: {type: "0"},
        success: function (res) {
            console.log("路侧 昨日停车时长占比")
            console.log(res)

            //饼状图： 停车时长：停车时长状态
            for (var i = 0; i < res.data.yesterday_parkTimeData.length; i++) {
                yesterdayStopCarPieOption.series[0].data[i].value = res.data.yesterday_parkTimeData[i];
            }

            yesterdayStopCarPie.setOption(yesterdayStopCarPieOption, true)
        }
    })
}

// 昨日停车时长占比--饼图--结束

// 日均车位周转率 -- 柱形图 -- 开始
const turnoverClum = echarts.init(document.getElementById('turnoverClum'));
var turnoverOption = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        },
        formatter: function (params) {
            let html = '';
            params.forEach(v => {
                html += `<div style="color: #fff;font-size: 14px;line-height: 24px">
                <span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color[v.dataIndex]};"></span>
                ${v.name}
                <span style="color:${color[v.dataIndex]};font-weight:700;font-size: 18px">${v.value}</span>
                %`;
            })
            return html
        },
        extraCssText: 'background: #05183a; border-radius: 0;box-shadow: 0 0 3px rgba(13, 44, 100, 0.8);color: #fff;border:1px solid #041129',
    },
    grid: {
        top: '14',
        left: '3%',
        right: '4%',
        bottom: '5%',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        data: ['08-01', '08-02', '08-03', '08-04', '08-05', '08-06', '08-07'],
        axisLine: {
            lineStyle: {
                color: 'rgba(255,255,255,0.12)'
            }
        },
        axisLabel: {
            margin: 10,
            color: '#fff',
            textStyle: {
                fontSize: 12
            },
        },
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value}%',
            color: '#fff',
        },
        axisLine: {
            show: false,
            lineStyle: {
                color: 'rgba(255,255,255,1)'
            }
        },
        splitLine: {
            lineStyle: {
                color: 'rgba(255,255,255,0.12)'
            }
        },

    },
    series: [
        {
            data: [20, 40, 50, 80, 70, 60, 30],
            type: 'bar',
            showBackground: true,
            barWidth: '20px',
            backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
            },
            label: {
                normal: {
                    show: false,
                    position: "insideRight"
                }
            },
            itemStyle: {
                normal: {
                    // color: "rgb(59, 161, 148)",
                    color: function (params) {
                        return color[params.dataIndex]
                    },
                }
            },

        }
    ]
};
turnoverClum.setOption(turnoverOption, true)
// 日均车位周转率 -- 柱形图 -- 结束

// 停车时长--环形图 --开始
const stopTimePie = echarts.init(document.getElementById('stopTimePie'));

var colorList = ['#afa3f5', '#00d488', '#3feed4', '#3bafff', '#f1bb4c', "rgba(250,250,250,0.5)"];


var colorList = ['#afa3f5', '#00d488', '#3feed4', '#3bafff', '#f1bb4c', "rgba(250,250,250,0.5)"];

var stopTimeOption = {

    grid: {
        bottom: 10,
        left: 0,
        right: '10%'
    },
    tooltip: {
        trigger: 'item',
        axisPointer: {
            type: 'shadow'
        },
        extraCssText: 'background: #05183a; border-radius: 0;box-shadow: 0 0 3px rgba(13, 44, 100, 0.8);color: #fff;border:1px solid #041129',
        formatter: "{b} : {c} ({d}%)"
    },
    legend: {
        show: true,
        orient: 'vertical',
        top: "middle",
        right: "5%",
        textStyle: {
            color: '#fff',
            fontSize: 12,

        },
        icon: 'roundRect'
    },
    series: [
        // 主要展示层的
        {
            radius: ['25%', '64%'],
            center: ['50%', '50%'],
            type: 'pie',
            itemStyle: {
                normal: {
                    color: function (params) {
                        return colorList[params.dataIndex]
                    }
                }
            },
            label: {
                fontSize: 12,
                color: '#fff'
            },
            data: [
                {value: 17, name: '1h-2h',},
                {value: 23, name: '2h-4h'},
                {value: 27, name: '4h-6h'},
                {value: 33, name: '6h-8h'},
                {value: 9, name: '8h-10h'}],
        },
        // 边框的设置
        {
            radius: ['57%', '64%'],
            center: ['50%', '50%'],
            type: 'pie',
            label: {
                normal: {
                    show: false
                },
                emphasis: {
                    show: false
                }
            },
            labelLine: {
                normal: {
                    show: false
                },
                emphasis: {
                    show: false
                }
            },
            animation: false,
            tooltip: {
                show: false
            },
            itemStyle: {
                normal: {
                    color: 'rgba(250,250,250,0.5)'
                }
            },
            data: [{
                value: 1,
            }],
        }
    ]
};

stopTimePie.setOption(stopTimeOption, true)
// 停车时长--环形图--结束

// 出入趋势--折线图--开始
var trendLine = echarts.init(document.getElementById('trendLine'));

let trendData = [{
    name: "08-01",
    value1: 10,
    value2: 14,
},
    {
        name: "08-02",
        value1: 18,
        value2: 16,
    },
    {
        name: "08-03",
        value1: 50,
        value2: 34,
    },
    {
        name: "08-04",
        value1: 73,
        value2: 64,
    },
    {
        name: "08-05",
        value1: 80,
        value2: 66,
    },
    {
        name: "08-06",
        value1: 50,
        value2: 34,
    },
    {
        name: "08-07",
        value1: 80,
        value2: 35,
    }
];

let xAxisDatatrend = trendData.map(v => v.name);
//  ["1", "2", "3", "4", "5", "6", "7", "8"]
let yAxisDatatrend1 = trendData.map(v => v.value1);
let yAxisDatatrend2 = trendData.map(v => v.value2);


var trendLineOption = {
    color: color,
    legend: {
        right: 10,
        top: 0,
        textStyle: {
            color: color
        },
    },
    tooltip: {
        trigger: "axis",
        formatter: function (params) {
            let html = '';
            params.forEach(v => {
                html += `<div style="color: #fff;font-size: 14px;line-height: 24px">
                <span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color[v.componentIndex]};"></span>
                ${v.name}号${v.seriesName}
                <span style="color:${color[v.componentIndex]};font-weight:700;font-size: 18px">${v.value}</span>
                车次`;
            })


            return html
        },
        extraCssText: 'background: #05183a; border-radius: 0;box-shadow: 0 0 3px rgba(13, 44, 100, 0.8);color: #fff;border:1px solid #041129',
        axisPointer: {
            type: 'shadow',
            shadowStyle: {
                color: '#091f47',
                shadowColor: 'rgba(63,91,144,0.3)',
                shadowBlur: 8
            }
        }
    },
    grid: {
        top: '32',
        left: '3%',
        right: '4%',
        bottom: '5%',
        containLabel: true
    },
    xAxis: [{
        type: "category",
        boundaryGap: false,
        axisLabel: {
            formatter: '{value}号',
            textStyle: {
                color: "#fff"
            }
        },
        axisLine: {
            lineStyle: {
                color: "#09526f"
            }
        },
        data: xAxisDatatrend
    }],
    yAxis: [{
        type: "value",
        name: '数量/车次',
        axisLabel: {
            textStyle: {
                color: "#fff"
            },
            formatter: '{value}',
        },
        nameTextStyle: {
            color: "#fff",
            fontSize: 12,
            lineHeight: 0
        },
        splitLine: {
            lineStyle: {
                type: "dashed",
                color: "#09526f"
            }
        },
        axisLine: {
            show: false
        },
        axisTick: {
            show: false
        }
    }],
    series: [{
        name: "出库",
        type: "line",
        // smooth: true,
        // showSymbol: false,/
        symbolSize: 4,
        zlevel: 3,
        lineStyle: {
            normal: {
                color: color[0],
                shadowBlur: 3,
                shadowColor: hexToRgbaLineOption(color[0], 0.5),
                shadowOffsetY: 2
            }
        },
        itemStyle: {
            normal: {
                lineStyle: {
                    width: 1
                },
            }
        },
        areaStyle: {
            normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                    [{
                        offset: 0,
                        color: hexToRgbaLineOption(color[0], 0.3)
                    },
                        {
                            offset: 1,
                            color: hexToRgbaLineOption(color[0], 0.1)
                        }
                    ],
                    false
                ),
                shadowColor: hexToRgbaLineOption(color[0], 0.1),
                shadowBlur: 10
            }
        },
        data: yAxisDatatrend1
    }, {
        name: "入库",
        type: "line",
        // smooth: true,
        // showSymbol: false,/
        symbolSize: 4,
        zlevel: 3,
        lineStyle: {
            normal: {
                color: color[1],
                shadowBlur: 3,
                shadowColor: hexToRgbaLineOption(color[1], 0.5),
                shadowOffsetY: 2
            }
        },
        itemStyle: {
            normal: {
                lineStyle: {
                    width: 1
                },
            }
        },
        areaStyle: {
            normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                    [{
                        offset: 0,
                        color: hexToRgbaLineOption(color[1], 0.3)
                    },
                        {
                            offset: 1,
                            color: hexToRgbaLineOption(color[1], 0.1)
                        }
                    ],
                    false
                ),
                shadowColor: hexToRgbaLineOption(color[1], 0.1),
                shadowBlur: 10
            }
        },
        data: yAxisDatatrend2
    }]
};

trendLine.setOption(trendLineOption);

/**
 * 车位周转率 柱状图、饼状图、双折线图
 * 柱状图：日均车位周转率：最近7天/日均车位周转率
 * 饼状图： 停车时长：停车时长状态暂定
 * 折线图： 出入趋势双折线：7天内出入次数
 */
getMainRight1Data();

function getMainRight1Data() {
    $.ajax({
        url: '/sjfxNew/getMainRight1Data.json',
        type: "POST",
        data: {type: "0"},
        success: function (res) {
            console.log("路侧 车位周转率")
            console.log(res)

            //柱状图：日均车位周转率：最近7天/日均车位周转率 cwZzlData.map(v => v.zzl); time
            turnoverOption.xAxis.data = res.data.cwZzlData.map(v => v.time.substring(v.time.indexOf("-") + 1));
            turnoverOption.series[0].data = res.data.cwZzlData.map(v => v.zzl);
            turnoverClum.setOption(turnoverOption, true);

            //饼状图： 停车时长：停车时长状态
            stopTimeOption.series[0].data = res.data.today_parkTimeData;
            stopTimePie.setOption(stopTimeOption, true);

            //折线图： 出入趋势双折线：7天内出入次数
            trendLineOption.xAxis[0].data = res.data.outList.map(v => v.name.substring(v.name.indexOf("-") + 1));
            trendLineOption.series[0].data = res.data.outList.map(v => v.amount);
            trendLineOption.series[1].data = res.data.inList.map(v => v.amount);
            trendLine.setOption(trendLineOption);
        }
    })
}

// 出入趋势-折线图--结束


//在停车辆前五排名 -- 柱状图 -- 开始
const stopCarFiveLine = echarts.init(document.getElementById('stopCarFiveLine'));
var charts = { // 按顺序排列从大到小
    cityList: ['蟠桃山路南侧', '广德路东边从南到北', '福泽路西段南从西边到东边', '洞山路北侧', '蟠桃山路北侧'],
    cityData: [7500, 6200, 5700, 4200, 3500],
    cityData2: [3500, 4200, 5700, 6200, 7500]
}
var top5CarStopList = charts.cityList
var top5CarStopData = charts.cityData
var top5CarStopData2 = charts.cityData2
var colorTop = ['#ff9500', '#02d8f9', '#027fff']
var colorTop1 = ['#ffb349', '#70e9fc', '#4aa4ff']

let lineY = []
let lineT = []
for (var i = 0; i < charts.cityList.length; i++) {
    var x = i
    if (x > 1) {
        x = 2
    }
    var data = {
        name: charts.cityList[i],
        color: colorTop[x],
        value: top5CarStopData[i],
        barGap: '-100%',
        itemStyle: {
            normal: {
                show: true,
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                    offset: 0,
                    color: colorTop[x]
                }, {
                    offset: 1,
                    color: colorTop1[x]
                }], false),
                barBorderRadius: 10
            },
            emphasis: {
                shadowBlur: 15,
                shadowColor: 'rgba(0, 0, 0, 0.1)'
            }
        }
    }
    var data1 = {
        value: top5CarStopData[0],
        itemStyle: {
            color: '#001235',
            barBorderRadius: 10
        }
    }
    lineY.push(data)
    lineT.push(data1)
}

stopCarFiveLineOption = {
    title: {
        show: false
    },
    tooltip: {
        trigger: 'item',
        formatter: (p) => {
            if (p.seriesName === 'total') {
                return ''
            }
            return `${p.name}<br/>${p.value}`
        }
    },
    grid: {
        borderWidth: 0,
        top: '10%',
        left: '5%',
        right: '15%',
        bottom: '3%'
    },
    color: color,
    yAxis: [{
        type: 'category',
        inverse: true,
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        axisLabel: {
            show: false,
            inside: false
        },
        data: top5CarStopList
    }, {
        type: 'category',
        axisLine: {
            show: false
        },
        axisTick: {
            show: false
        },
        axisLabel: {
            show: true,
            inside: false,
            verticalAlign: 'center',
            lineHeight: '40',
            textStyle: {
                color: '#b3ccf8',
                fontSize: '14',
                fontFamily: 'PingFangSC-Regular'
            },
            formatter: function (val) {
                return `${val}辆`
            }
        },
        splitArea: {
            show: false
        },
        splitLine: {
            show: false
        },
        data: top5CarStopData2
    }],
    xAxis: {
        type: 'value',
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        splitLine: {
            show: false
        },
        axisLabel: {
            show: false
        }
    },
    series: [{
        name: 'total',
        type: 'bar',
        zlevel: 1,
        barGap: '-100%',
        barWidth: '10px',
        data: lineT,
        legendHoverLink: false
    }, {
        name: 'bar',
        type: 'bar',
        zlevel: 2,
        barWidth: '10px',
        data: lineY,
        label: {
            normal: {
                color: '#b3ccf8',
                show: true,
                position: [0, '-24px'],
                textStyle: {
                    fontSize: 16
                },
                formatter: function (a) {
                    let num = ''
                    let str = ''
                    if (a.dataIndex + 1 < 10) {
                        num = '0' + (a.dataIndex + 1);
                    } else {
                        num = (a.dataIndex + 1);
                    }
                    if (a.dataIndex === 0) {
                        str = `{color1|${num}} {color4|${a.name}}`
                    } else if (a.dataIndex === 1) {
                        str = `{color2|${num}} {color4|${a.name}}`
                    } else {
                        str = `{color3|${num}} {color4|${a.name}}`
                    }
                    return str;
                },
                rich: {
                    color1: {
                        color: '#ff9500',
                        fontWeight: 700
                    },
                    color2: {
                        color: '#02d8f9',
                        fontWeight: 700
                    },
                    color3: {
                        color: '#027fff',
                        fontWeight: 700
                    },
                    color4: {
                        color: '#e5eaff'
                    }
                }
            }
        }
    }],
}
stopCarFiveLine.setOption(stopCarFiveLineOption, true)

/**
 * 停车场/路侧
 * 在停车辆排名TOP5
 */
getCarTop5Data();

function getCarTop5Data() {
    $.ajax({
        url: '/sjfxNew/getCarTop5Data.json',
        type: "POST",
        data: {type: "0"},
        success: function (res) {
            console.log("路侧 在停车辆排名TOP5")
            console.log(res)

            charts = { // 按顺序排列从大到小
                cityList: res.data.carTop5Data.xData,
                cityData: res.data.carTop5Data.yData,
                cityData2: res.data.carTop5Data.yData2,
            }
            top5CarStopList = charts.cityList
            top5CarStopData = charts.cityData
            top5CarStopData2 = charts.cityData2

            lineY = []
            lineT = []
            for (var i = 0; i < charts.cityList.length; i++) {
                var x = i
                if (x > 1) {
                    x = 2
                }
                var data = {
                    name: charts.cityList[i],
                    color: colorTop[x],
                    value: top5CarStopData[i],
                    barGap: '-100%',
                    itemStyle: {
                        normal: {
                            show: true,
                            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                                offset: 0,
                                color: colorTop[x]
                            }, {
                                offset: 1,
                                color: colorTop1[x]
                            }], false),
                            barBorderRadius: 10
                        },
                        emphasis: {
                            shadowBlur: 15,
                            shadowColor: 'rgba(0, 0, 0, 0.1)'
                        }
                    }
                }
                var data1 = {
                    value: top5CarStopData[0],
                    itemStyle: {
                        color: '#001235',
                        barBorderRadius: 10
                    }
                }
                lineY.push(data)
                lineT.push(data1)
            }

            stopCarFiveLineOption.yAxis[0].data = top5CarStopList;//top5CarStopList
            stopCarFiveLineOption.yAxis[1].data = top5CarStopData2;//top5CarStopData2

            stopCarFiveLineOption.series[0].data = lineT;
            stopCarFiveLineOption.series[1].data = lineY;

            stopCarFiveLine.setOption(stopCarFiveLineOption, true)
        }
    })
}

//在停车辆前五排名 -- 柱状图 -- 结束


//收费金额前五排名 -- 柱状图 -- 开始
const chargeFiveLine = echarts.init(document.getElementById('chargeFiveLine'));
var chartsCharge = { // 按顺序排列从大到小
    cityList: ['蟠桃山路南侧', '广德路东边从南到北', '福泽路西段南从西边到东边', '洞山路北侧', '蟠桃山路北侧'],
    cityData: [7500, 6200, 5700, 4200, 3500],
    cityData2: [3500, 4200, 5700, 6200, 7500]
}
var top5ChargeList = chartsCharge.cityList
var top5ChargeData = chartsCharge.cityData
var top5ChargeData2 = chartsCharge.cityData2
// var colorTop = ['#ff9500', '#02d8f9', '#027fff']
// var colorTop1 = ['#ffb349', '#70e9fc', '#4aa4ff']

let lineChargeY = []
let lineChargeT = []
for (var i = 0; i < chartsCharge.cityList.length; i++) {
    var x = i
    if (x > 1) {
        x = 2
    }
    var data = {
        name: chartsCharge.cityList[i],
        color: colorTop[x],
        value: top5ChargeData[i],
        barGap: '-100%',
        itemStyle: {
            normal: {
                show: true,
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                    offset: 0,
                    color: colorTop[x]
                }, {
                    offset: 1,
                    color: colorTop1[x]
                }], false),
                barBorderRadius: 10
            },
            emphasis: {
                shadowBlur: 15,
                shadowColor: 'rgba(0, 0, 0, 0.1)'
            }
        }
    }
    var data1 = {
        value: top5ChargeData[0],
        itemStyle: {
            color: '#001235',
            barBorderRadius: 10
        }
    }
    lineChargeY.push(data)
    lineChargeT.push(data1)
}

chargeFiveLineOption = {
    title: {
        show: false
    },
    tooltip: {
        trigger: 'item',
        formatter: (p) => {
            if (p.seriesName === 'total') {
                return ''
            }
            return `${p.name}<br/>${p.value}`
        }
    },
    grid: {
        borderWidth: 0,
        top: '10%',
        left: '5%',
        right: '15%',
        bottom: '3%'
    },
    color: color,
    yAxis: [{
        type: 'category',
        inverse: true,
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        axisLabel: {
            show: false,
            inside: false
        },
        data: top5ChargeList
    }, {
        type: 'category',
        axisLine: {
            show: false
        },
        axisTick: {
            show: false
        },
        axisLabel: {
            show: true,
            inside: false,
            verticalAlign: 'center',
            lineHeight: '40',
            textStyle: {
                color: '#b3ccf8',
                fontSize: '14',
                fontFamily: 'PingFangSC-Regular'
            },
            formatter: function (val) {
                return `${val}元`
            }
        },
        splitArea: {
            show: false
        },
        splitLine: {
            show: false
        },
        data: top5ChargeData2
    }],
    xAxis: {
        type: 'value',
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        splitLine: {
            show: false
        },
        axisLabel: {
            show: false
        }
    },
    series: [{
        name: 'total',
        type: 'bar',
        zlevel: 1,
        barGap: '-100%',
        barWidth: '10px',
        data: lineChargeT,
        legendHoverLink: false
    }, {
        name: 'bar',
        type: 'bar',
        zlevel: 2,
        barWidth: '10px',
        data: lineChargeY,
        label: {
            normal: {
                color: '#b3ccf8',
                show: true,
                position: [0, '-24px'],
                textStyle: {
                    fontSize: 16
                },
                formatter: function (a) {
                    let num = ''
                    let str = ''
                    if (a.dataIndex + 1 < 10) {
                        num = '0' + (a.dataIndex + 1);
                    } else {
                        num = (a.dataIndex + 1);
                    }
                    if (a.dataIndex === 0) {
                        str = `{color1|${num}} {color4|${a.name}}`
                    } else if (a.dataIndex === 1) {
                        str = `{color2|${num}} {color4|${a.name}}`
                    } else {
                        str = `{color3|${num}} {color4|${a.name}}`
                    }
                    return str;
                },
                rich: {
                    color1: {
                        color: '#ff9500',
                        fontWeight: 700
                    },
                    color2: {
                        color: '#02d8f9',
                        fontWeight: 700
                    },
                    color3: {
                        color: '#027fff',
                        fontWeight: 700
                    },
                    color4: {
                        color: '#e5eaff'
                    }
                }
            }
        }
    }],
}
chargeFiveLine.setOption(chargeFiveLineOption, true)

/**
 * 停车场/路侧
 * 收费金额排名TOP5
 */
getTotalChargesTop5Data();

function getTotalChargesTop5Data() {
    $.ajax({
        url: '/sjfxNew/getTotalChargesTop5Data.json',
        type: "POST",
        data: {type: "0"},
        success: function (res) {
            console.log("路侧 收费金额排名TOP5")
            console.log(res)

            chartsCharge = { // 按顺序排列从大到小
                cityList: res.data.totalChargesTop5Data.xData,
                cityData: res.data.totalChargesTop5Data.yData,
                cityData2: res.data.totalChargesTop5Data.yData2,
            }
            top5ChargeList = chartsCharge.cityList;
            top5ChargeData = chartsCharge.cityData;
            top5ChargeData2 = chartsCharge.cityData2;

            lineChargeY = []
            lineChargeT = []
            for (var i = 0; i < chartsCharge.cityList.length; i++) {
                var x = i
                if (x > 1) {
                    x = 2
                }
                var data = {
                    name: chartsCharge.cityList[i],
                    color: colorTop[x],
                    value: top5ChargeData[i],
                    barGap: '-100%',
                    itemStyle: {
                        normal: {
                            show: true,
                            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                                offset: 0,
                                color: colorTop[x]
                            }, {
                                offset: 1,
                                color: colorTop1[x]
                            }], false),
                            barBorderRadius: 10
                        },
                        emphasis: {
                            shadowBlur: 15,
                            shadowColor: 'rgba(0, 0, 0, 0.1)'
                        }
                    }
                }
                var data1 = {
                    value: top5ChargeData[0],
                    itemStyle: {
                        color: '#001235',
                        barBorderRadius: 10
                    }
                }
                lineChargeY.push(data)
                lineChargeT.push(data1)
            }

            chargeFiveLineOption.yAxis[0].data = top5ChargeList;
            chargeFiveLineOption.yAxis[1].data = top5ChargeData2;

            chargeFiveLineOption.series[0].data = lineChargeT;
            chargeFiveLineOption.series[1].data = lineChargeY;

            chargeFiveLine.setOption(chargeFiveLineOption, true)
        }
    })
}

//收费金额前五排名 -- 柱状图 -- 结束


// 欠费车辆前十排名 -- 柱形图 -- 开始
const arrearsTen = echarts.init(document.getElementById('arrearsTen'));
var arrearsTenOption = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        },
        formatter: function (params) {
            let html = '';
            params.forEach(v => {
                html += `<div style="color: #fff;font-size: 14px;line-height: 24px">
                <span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color[v.dataIndex]};"></span>
                ${v.name}
                <span style="color:${color[v.dataIndex]};font-weight:700;font-size: 18px">${v.value}</span>
                元`;
            })
            return html
        },
        extraCssText: 'background: #05183a; border-radius: 0;box-shadow: 0 0 3px rgba(13, 44, 100, 0.8);color: #fff;border:1px solid #041129',
    },
    grid: {
        top: '14',
        left: '3%',
        right: '4%',
        bottom: '5%',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        data: ['苏CT2038', '苏CT2039', '苏CT5038', '苏CT2G38', '苏CT5638', '苏CT2654', '苏CT5778', '苏CTGGG8', '苏CTGGR8', '苏CTJK78'],
        axisLine: {
            lineStyle: {
                color: 'rgba(255,255,255,0.12)'
            }
        },

        axisLabel: {
            margin: 10,
            color: '#fff',
            textStyle: {
                fontSize: 12
            },
            interval: 0,
        },
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value}元',
            color: '#fff',
        },
        axisLine: {
            show: false,
            lineStyle: {
                color: 'rgba(255,255,255,1)'
            }
        },
        splitLine: {
            lineStyle: {
                color: 'rgba(255,255,255,0.12)'
            }
        },

    },
    series: [
        {
            data: [120, 110, 90, 80, 70, 60, 50, 30, 20, 10],
            type: 'bar',
            showBackground: true,
            barWidth: '20px',
            backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
            },
            label: {
                normal: {
                    show: false,
                    position: "insideRight"
                }
            },
            itemStyle: {
                normal: {
                    // color: "rgb(59, 161, 148)",
                    color: function (params) {
                        return color[params.dataIndex]
                    },
                }
            },

        }
    ]
};
arrearsTen.setOption(arrearsTenOption, true)

/**
 * 欠费车辆排名TOP10
 */
getSumAmountTop10ByCarNo();

function getSumAmountTop10ByCarNo() {
    $.ajax({
        url: '/sjfxNew/getSumAmountTop10ByCarNo.json',
        type: "POST",
        data: {type: "0"},
        success: function (res) {
            console.log("路侧 欠费车辆排名TOP10")
            console.log(res)

            //柱状图：欠费车辆排名TOP10
            arrearsTenOption.xAxis.data = res.data.sumAmountTop10Data.list.map(v => v.car_no);
            arrearsTenOption.series[0].data = res.data.sumAmountTop10Data.list.map(v => v.sum_amount);
            arrearsTen.setOption(arrearsTenOption, true)
        }
    })
}

// 欠费车辆前十排名 -- 柱形图 -- 结束


// 车位满位率--折线图--开始
var inventoryRoadLine = echarts.init(document.getElementById('inventoryRoadLine'));

let inventoryData = [{
    name: "1",
    value1: 10,
    value2: 14,
    value3: 13,
},
    {
        name: "2",
        value1: 18,
        value2: 16,
        value3: 12,
    },
    {
        name: "3",
        value1: 50,
        value2: 34,
        value3: 43,
    },
    {
        name: "4",
        value1: 73,
        value2: 64,
        value3: 54,
    },
    {
        name: "5",
        value1: 80,
        value2: 66,
        value3: 53,
    },
    {
        name: "6",
        value1: 50,
        value2: 34,
        value3: 24,
    },
    {
        name: "7",
        value1: 80,
        value2: 35,
        value3: 32,
    },
    {
        name: "8",
        value1: 30,
        value2: 57,
        value3: 40,
    }
];

let xAxisDataInventory = inventoryData.map(v => v.name);
//  ["1", "2", "3", "4", "5", "6", "7", "8"]
let yAxisDataInventory1 = inventoryData.map(v => v.value1);
let yAxisDataInventory2 = inventoryData.map(v => v.value2);
let yAxisDataInventory3 = inventoryData.map(v => v.value3);

var inventoryRoadLineOption = {
    color: color,
    legend: {
        right: 10,
        top: 0,
        textStyle: {
            color: color
        },
    },
    tooltip: {
        trigger: "axis",
        formatter: function (params) {
            let html = '';
            params.forEach(v => {
                console.log(v)
                html += `<div style="color: #fff;font-size: 14px;line-height: 24px">
                <span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color[v.componentIndex]};"></span>
                ${v.name}点${v.seriesName}
                <span style="color:${color[v.componentIndex]};font-weight:700;font-size: 18px">${v.value}</span>
                %`;
            })


            return html
        },
        extraCssText: 'background: #05183a; border-radius: 0;box-shadow: 0 0 3px rgba(13, 44, 100, 0.8);color: #fff;border:1px solid #041129',
        axisPointer: {
            type: 'shadow',
            shadowStyle: {
                color: '#091f47',
                shadowColor: 'rgba(63,91,144,0.3)',
                shadowBlur: 8
            }
        }
    },
    grid: {
        top: '18%',
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [{
        type: "category",
        boundaryGap: false,
        axisLabel: {
            formatter: '{value}点',
            textStyle: {
                color: "#fff"
            }
        },
        axisLine: {
            lineStyle: {
                color: "#09526f"
            }
        },
        data: xAxisDataInventory
    }],
    yAxis: [{
        type: "value",
        name: '车位满位率/%',
        min: 0,
        max: 100,
        axisLabel: {
            textStyle: {
                color: "#fff"
            },
            formatter: '{value}%',
        },
        nameTextStyle: {
            color: "#fff",
            fontSize: 12,
            lineHeight: 0
        },
        splitLine: {
            lineStyle: {
                type: "dashed",
                color: "#09526f"
            }
        },
        axisLine: {
            show: false
        },
        axisTick: {
            show: false
        }
    }],
    series: [{
        name: "今天",
        type: "line",
        // smooth: true,
        // showSymbol: false,/
        symbolSize: 4,
        zlevel: 3,
        lineStyle: {
            normal: {
                color: color[0],
                shadowBlur: 3,
                shadowColor: hexToRgbaLineOption(color[0], 0.5),
                shadowOffsetY: 2
            }
        },
        itemStyle: {
            normal: {
                lineStyle: {
                    width: 1
                },
            }
        },
        areaStyle: {
            normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                    [{
                        offset: 0,
                        color: hexToRgbaLineOption(color[0], 0.3)
                    },
                        {
                            offset: 1,
                            color: hexToRgbaLineOption(color[0], 0.1)
                        }
                    ],
                    false
                ),
                shadowColor: hexToRgbaLineOption(color[0], 0.1),
                shadowBlur: 10
            }
        },
        data: yAxisDataInventory1
    }, {
        name: "昨天",
        type: "line",
        // smooth: true,
        // showSymbol: false,/
        symbolSize: 4,
        zlevel: 3,
        lineStyle: {
            normal: {
                color: color[1],
                shadowBlur: 3,
                shadowColor: hexToRgbaLineOption(color[1], 0.5),
                shadowOffsetY: 2
            }
        },
        itemStyle: {
            normal: {
                lineStyle: {
                    width: 1
                },
            }
        },
        areaStyle: {
            normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                    [{
                        offset: 0,
                        color: hexToRgbaLineOption(color[1], 0.3)
                    },
                        {
                            offset: 1,
                            color: hexToRgbaLineOption(color[1], 0.1)
                        }
                    ],
                    false
                ),
                shadowColor: hexToRgbaLineOption(color[1], 0.1),
                shadowBlur: 10
            }
        },
        data: yAxisDataInventory2
    }, {
        name: "上周同天",
        type: "line",
        // smooth: true,
        // showSymbol: false,/
        symbolSize: 4,
        zlevel: 3,
        lineStyle: {
            normal: {
                color: color[2],
                shadowBlur: 3,
                shadowColor: hexToRgbaLineOption(color[2], 0.5),
                shadowOffsetY: 2
            }
        },
        itemStyle: {
            normal: {
                lineStyle: {
                    width: 1
                },
            }
        },
        areaStyle: {
            normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                    [{
                        offset: 0,
                        color: hexToRgbaLineOption(color[2], 0.3)
                    },
                        {
                            offset: 1,
                            color: hexToRgbaLineOption(color[2], 0.1)
                        }
                    ],
                    false
                ),
                shadowColor: hexToRgbaLineOption(color[2], 0.1),
                shadowBlur: 10
            }
        },
        data: yAxisDataInventory3
    }]
};

inventoryRoadLine.setOption(inventoryRoadLineOption);

/**
 * 车位满位率
 */
getCwMwlDatas();

function getCwMwlDatas() {
    $.ajax({
        url: '/sjfxNew/getCwMwlDatas.json',
        type: "POST",
        data: {type: "0"},
        success: function (res) {
            console.log("路侧 车位满位率")
            console.log(res)

            //柱状图：欠费车辆排名TOP10
            inventoryRoadLineOption.xAxis[0].data = res.data.cwMwlDatas.today_data.map(v => v.hour);
            inventoryRoadLineOption.series[0].data = res.data.cwMwlDatas.today_data.map(v => v.mwl);
            inventoryRoadLineOption.series[1].data = res.data.cwMwlDatas.yesterday_data.map(v => v.mwl);
            inventoryRoadLineOption.series[2].data = res.data.cwMwlDatas.last_week_data.map(v => v.mwl);
            inventoryRoadLine.setOption(inventoryRoadLineOption);
        }
    })
}

// 车位满位率--折线图--结束

//自助支付率 -- 水球 --开始
var roadsideA = echarts.init(document.getElementById('roadsideA'));
var roadsideAvalue = 0.45;
var roadsideAOption = {
    series: [
        {
            type: 'liquidFill',
            radius: '80%',
            center: ['50%', '50%'],
            color: [
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#f4a664',
                        },
                        {
                            offset: 1,
                            color: '#dd7820',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            data: [roadsideAvalue, roadsideAvalue], // data个数代表波浪数
            backgroundStyle: {
                borderWidth: 1,
                color: 'RGBA(51, 66, 127, 0.7)',
            },
            label: {
                normal: {
                    textStyle: {
                        fontSize: 20,
                        color: '#fff',
                    },
                }
            },
            outline: {
                show: false,
                borderDistance: 10,
                itemStyle: {
                    borderWidth: 2,
                    borderColor: '#112165',
                },
            },
        },


    ],
};
roadsideA.setOption(roadsideAOption, true)
//自助支付率 -- 水球 --结束

//自助停车率 -- 水球 --开始
var roadsideB = echarts.init(document.getElementById('roadsideB'));
var roadsideBvalue = 0.45;
var roadsideBOption = {
    series: [
        {
            type: 'liquidFill',
            radius: '80%',
            center: ['50%', '50%'],
            color: [
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#30b0f3',
                        },
                        {
                            offset: 1,
                            color: '#05adc6',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            data: [roadsideBvalue, roadsideBvalue], // data个数代表波浪数
            backgroundStyle: {
                borderWidth: 1,
                color: 'RGBA(51, 66, 127, 0.7)',
            },
            label: {
                normal: {
                    textStyle: {
                        fontSize: 20,
                        color: '#fff',
                    },
                }
            },
            outline: {
                show: false,
                borderDistance: 10,
                itemStyle: {
                    borderWidth: 2,
                    borderColor: '#112165',
                },
            },
        },


    ],
};
roadsideB.setOption(roadsideBOption, true)

//自助停车率 -- 水球 --结束

//订单收费率 -- 水球 --开始
var roadsideC = echarts.init(document.getElementById('roadsideC'));
var roadsideCvalue = 0.45;
var roadsideCOption = {
    series: [
        {
            type: 'liquidFill',
            radius: '80%',
            center: ['50%', '50%'],
            color: [
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#4af1e8',
                        },
                        {
                            offset: 1,
                            color: '#1aa49c',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            data: [roadsideCvalue, roadsideCvalue], // data个数代表波浪数
            backgroundStyle: {
                borderWidth: 1,
                color: 'RGBA(51, 66, 127, 0.7)',
            },
            label: {
                normal: {
                    textStyle: {
                        fontSize: 20,
                        color: '#fff',
                    },
                }
            },
            outline: {
                show: false,
                borderDistance: 10,
                itemStyle: {
                    borderWidth: 2,
                    borderColor: '#112165',
                },
            },
        },


    ],
};
roadsideC.setOption(roadsideCOption, true)

//订单收费率 -- 水球 --结束

//15分钟内自助 -- 水球 --开始
var roadsideD = echarts.init(document.getElementById('roadsideD'));
var roadsideDvalue = 0.45;
var roadsideDOption = {
    series: [
        {
            type: 'liquidFill',
            radius: '80%',
            center: ['50%', '50%'],
            color: [
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#469ced',
                        },
                        {
                            offset: 1,
                            color: '#1576d2',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            data: [roadsideDvalue, roadsideDvalue], // data个数代表波浪数
            backgroundStyle: {
                borderWidth: 1,
                color: 'RGBA(51, 66, 127, 0.7)',
            },
            label: {
                normal: {
                    textStyle: {
                        fontSize: 20,
                        color: '#fff',
                    },
                }
            },
            outline: {
                show: false,
                borderDistance: 10,
                itemStyle: {
                    borderWidth: 2,
                    borderColor: '#112165',
                },
            },
        },


    ],
};
roadsideD.setOption(roadsideDOption, true)

//15分钟内自助 -- 水球 --结束

//晚间满位率 -- 水球 --开始
var roadsideE = echarts.init(document.getElementById('roadsideE'));
var roadsideEvalue = 0.45;
var roadsideEOption = {
    series: [
        {
            type: 'liquidFill',
            radius: '80%',
            center: ['50%', '50%'],
            color: [
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#446bf5',
                        },
                        {
                            offset: 1,
                            color: '#2ca3e2',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            data: [roadsideEvalue, roadsideEvalue], // data个数代表波浪数
            backgroundStyle: {
                borderWidth: 1,
                color: 'RGBA(51, 66, 127, 0.7)',
            },
            label: {
                normal: {
                    textStyle: {
                        fontSize: 20,
                        color: '#fff',
                    },
                }
            },
            outline: {
                show: false,
                borderDistance: 10,
                itemStyle: {
                    borderWidth: 2,
                    borderColor: '#112165',
                },
            },
        },


    ],
};
roadsideE.setOption(roadsideEOption, true)

//晚间满位率 -- 水球 --结束

//超24H占位率 -- 水球 --开始
var roadsideF = echarts.init(document.getElementById('roadsideF'));
var roadsideFvalue = 0.45;
var roadsideFOption = {
    series: [
        {
            type: 'liquidFill',
            radius: '80%',
            center: ['50%', '50%'],
            color: [
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#58f895',
                        },
                        {
                            offset: 1,
                            color: '#1eac54',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            data: [roadsideFvalue, roadsideFvalue], // data个数代表波浪数
            backgroundStyle: {
                borderWidth: 1,
                color: 'RGBA(51, 66, 127, 0.7)',
            },
            label: {
                normal: {
                    textStyle: {
                        fontSize: 20,
                        color: '#fff',
                    },
                }
            },
            outline: {
                show: false,
                borderDistance: 10,
                itemStyle: {
                    borderWidth: 2,
                    borderColor: '#112165',
                },
            },
        },


    ],
};
roadsideF.setOption(roadsideFOption, true)

//超24H占位率 -- 水球 --结束

//发票发放率 -- 水球 --开始
var roadsideG = echarts.init(document.getElementById('roadsideG'));
var roadsideGvalue = 0.45;
var roadsideGOption = {
    series: [
        {
            type: 'liquidFill',
            radius: '80%',
            center: ['50%', '50%'],
            color: [
                {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [
                        {
                            offset: 0,
                            color: '#f1ef6a',
                        },
                        {
                            offset: 1,
                            color: '#a7a414',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            data: [roadsideGvalue, roadsideGvalue], // data个数代表波浪数
            backgroundStyle: {
                borderWidth: 1,
                color: 'RGBA(51, 66, 127, 0.7)',
            },
            label: {
                normal: {
                    textStyle: {
                        fontSize: 20,
                        color: '#fff',
                    },
                }
            },
            outline: {
                show: false,
                borderDistance: 10,
                itemStyle: {
                    borderWidth: 2,
                    borderColor: '#112165',
                },
            },
        },


    ],
};
roadsideG.setOption(roadsideGOption, true)

/**
 * 右图3
 * 路边停车 饼状图
 * 非现金支付占比
 * 自助停车率（排除人工订单和人工抬杆的所有状态订单）
 * 订单收费率（已缴费和已完成的订单数量/总数量）
 * 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）
 * 平均周转率（日均车位周转率总和/总天数）
 * 超24H占位率（已缴费=已完成的超24H停车时长/总订单数量）
 * 发票发放率
 */
getRoadRight3Data();

function getRoadRight3Data() {
    $.ajax({
        url: '/sjfxNew/getRoadRight3Data.json',
        type: "POST",
        data: {},
        success: function (res) {
            console.log("路侧 路边停车")
            console.log(res)

            //非现金支付占比
            roadsideAOption.series[0].data = [res.data.fxjzf, res.data.fxjzf];
            roadsideA.setOption(roadsideAOption, true)

            // 自助停车率（排除人工订单和人工抬杆的所有状态订单）
            roadsideBOption.series[0].data = [res.data.zztcl, res.data.zztcl];
            roadsideB.setOption(roadsideBOption, true)

            // 订单收费率（已缴费和已完成的订单数量/总数量）
            roadsideCOption.series[0].data = [res.data.ddsfl, res.data.ddsfl];
            roadsideC.setOption(roadsideCOption, true)

            // 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）
            roadsideDOption.series[0].data = [res.data.minzb, res.data.minzb];
            roadsideD.setOption(roadsideDOption, true)

            // 平均周转率（日均车位周转率总和/总天数）
            roadsideEOption.series[0].data = [res.data.avgzzl, res.data.avgzzl];
            roadsideE.setOption(roadsideEOption, true)

            // 超24H占位率（已缴费=已完成的超24H停车时长/总订单数量）
            roadsideFOption.series[0].data = [res.data.hourzb, res.data.hourzb];
            roadsideF.setOption(roadsideFOption, true)

            // 发票发放率
            roadsideGOption.series[0].data = [res.data.fpffl, res.data.fpffl];
            roadsideG.setOption(roadsideGOption, true)

        }
    })
}

//发票发放率 -- 水球 --结束


// const payStyle = echarts.init(document.getElementById('payStyle'));

// payStyle.setOption(payStyleOption, true)

window.addEventListener("resize", function () {
    yesterdayStopCarPie.resize();
    turnoverClum.resize()
    stopTimePie.resize()
    trendLine.resize()
    stopCarFiveLine.resize()
    chargeFiveLine.resize()
    arrearsTen.resize()
    inventoryRoadLine.resize()
})

window.onload = function () {
    nextIntegralPointAfterLogin2();
}

/**
 * 定时任务
 * 每个整点重新数据渲染
 */
function nextIntegralPointAfterLogin2() {
    console.log("-----定时任务 每个整点重新数据渲染------");

    // IntegralPointExecute();//在整点执行的函数，在每个整点都调用该函数
    setInterval("IntegralPointExecute2();", 60 * 60 * 1000);//一个小时执行一次，那么下一个整点，下下一个整点都会执行
}

function IntegralPointExecute2() {
    console.log(new Date().getHours());

    /**
     * 停车场/路侧
     * 昨日停车时长占比分析
     */
    getParkingTimeData();

    /**
     * 车位周转率 柱状图、饼状图、双折线图
     * 柱状图：日均车位周转率：最近7天/日均车位周转率
     * 饼状图： 停车时长：停车时长状态暂定
     * 折线图： 出入趋势双折线：7天内出入次数
     */
    getMainRight1Data();

    /**
     * 停车场/路侧
     * 在停车辆排名TOP5
     */
    getCarTop5Data();

    /**
     * 停车场/路侧
     * 收费金额排名TOP5
     */
    getTotalChargesTop5Data();

    /**
     * 欠费车辆排名TOP10
     */
    getSumAmountTop10ByCarNo();

    /**
     * 车位满位率
     */
    getCwMwlDatas();

    /**
     * 右图3
     * 路边停车 饼状图
     * 非现金支付占比
     * 自助停车率（排除人工订单和人工抬杆的所有状态订单）
     * 订单收费率（已缴费和已完成的订单数量/总数量）
     * 15分钟内支付占比（缴费时间-出场时间<15分钟的数量 /总已缴费、总已完成状态的订单）
     * 平均周转率（日均车位周转率总和/总天数）
     * 超24H占位率（已缴费=已完成的超24H停车时长/总订单数量）
     * 发票发放率
     */
    getRoadRight3Data();

    var date = new Date();//现在时刻
    var dateIntegralPoint = new Date();//用户登录时刻的下一个整点，也可以设置成某一个固定时刻
    dateIntegralPoint.setHours(date.getHours() + 1);//小时数增加1
    dateIntegralPoint.setMinutes(0);
    dateIntegralPoint.setSeconds(0);
    setTimeout("nextIntegralPointAfterLogin2();", dateIntegralPoint - date);//用户登录后的下一个整点执行。
}
