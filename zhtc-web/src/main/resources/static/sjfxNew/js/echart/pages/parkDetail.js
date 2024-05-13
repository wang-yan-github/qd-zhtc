// window.parent.showLoading();


window.onresize = function () {
    // resizeDiv();
}
// window.onload = function () {
//  window.parent.hideLoading();

//  //loadPage();
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
        bottom: '2%',
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

/**
 * 车位周转率 柱状图、饼状图
 * 柱状图：日均车位周转率：最近7天/日均车位周转率
 * 饼状图： 停车时长：停车时长状态暂定
 */
getMainRight1Data();

function getMainRight1Data() {
    $.ajax({
        url: '/sjfxNew/getMainRight1Data.json',
        type: "POST",
        data: {id: $("#id").val(), type: "1"},
        success: function (res) {
            console.log("停车场详情 车位周转率")
            console.log(res)

            //柱状图：日均车位周转率：最近7天/日均车位周转率 cwZzlData.map(v => v.zzl); time
            turnoverOption.xAxis.data = res.data.cwZzlData.map(v => v.time.substring(v.time.indexOf("-") + 1));
            turnoverOption.series[0].data = res.data.cwZzlData.map(v => v.zzl);
            turnoverClum.setOption(turnoverOption, true)

            //饼状图： 停车时长：停车时长状态
            stopTimeOption.series[0].data = res.data.today_parkTimeData;
            stopTimePie.setOption(stopTimeOption, true);
        }
    })
}

// 停车时长--环形图--结束

//发票发放率 -- 水球 --开始
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
//发票发放率 -- 水球 --结束

//问题订单率 -- 水球 --开始
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

//问题订单率 -- 水球 --结束

//手动开闸率 -- 水球 --开始
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

//手动开闸率 -- 水球 --结束

//免费订单率 -- 水球 --开始
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

/**
 * 路边停车
 * 发票发放率、问题订单率、手动开闸率、免费订单率
 */
getParkRight3Data();

function getParkRight3Data() {
    $.ajax({
        url: '/sjfxNew/getParkRight3Data.json',
        type: "POST",
        data: {id: $("#id").val()},
        success: function (res) {
            console.log("停车场详情 车场停车")
            console.log(res)

            //发票发放率、
            roadsideAOption.series[0].data = [res.data.fpffl, res.data.fpffl];
            roadsideA.setOption(roadsideAOption, true)

            // 问题订单率、
            // roadsideBOption.series[0].data = [res.data.zztcl, res.data.zztcl];
            // roadsideB.setOption(roadsideBOption, true)

            // 手动开闸率、
            roadsideCOption.series[0].data = [res.data.sdkzl, res.data.sdkzl];
            roadsideC.setOption(roadsideCOption, true)

            // 免费订单率
            roadsideDOption.series[0].data = [res.data.mfddl, res.data.mfddl];
            roadsideD.setOption(roadsideDOption, true)

        }
    })
}

//免费订单率 -- 水球 --结束

// 今日进出--折线图--开始
var todayOutLine = echarts.init(document.getElementById('todayOutLine'));

let todayOutLineData = [{
    name: "06:00",
    value1: 10,
    value2: 14,
},
    {
        name: "08:00",
        value1: 18,
        value2: 16,
    },
    {
        name: "10:00",
        value1: 50,
        value2: 34,
    },
    {
        name: "12:00",
        value1: 73,
        value2: 64,
    },
    {
        name: "14:00",
        value1: 80,
        value2: 66,
    },
    {
        name: "16:00",
        value1: 50,
        value2: 34,
    },
    {
        name: "18:00",
        value1: 80,
        value2: 35,
    }
];

let xAxisDatatodayOutLine = todayOutLineData.map(v => v.name);
//  ["1", "2", "3", "4", "5", "6", "7", "8"]
let yAxisDatatodayOutLine1 = todayOutLineData.map(v => v.value1);
let yAxisDatatodayOutLine2 = todayOutLineData.map(v => v.value2);

var todayOutLineOption = {
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
                ${v.name}点${v.seriesName}
                <span style="color:${color[v.componentIndex]};font-weight:700;font-size: 18px">${v.value}</span>
                辆`;
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
        right: '8%',
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
        data: xAxisDatatodayOutLine
    }],
    yAxis: [{
        type: "value",
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
        name: "出场",
        type: "line",
        smooth: true,
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
        data: yAxisDatatodayOutLine1
    }, {
        name: "入库",
        type: "line",
        smooth: true,
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
        data: yAxisDatatodayOutLine2
    }]
};

todayOutLine.setOption(todayOutLineOption);

/**
 * 路侧/停车场详情
 * 今日进出 折线图
 */
getInOutData();

function getInOutData() {
    $.ajax({
        url: '/sjfxNew/getInOutData.json',
        type: "POST",
        data: {id: $("#id").val(), type: "1"},
        success: function (res) {
            console.log("停车场详情 今日进出")
            console.log(res)

            //今日进出 折线图
            todayOutLineOption.xAxis[0].data = res.data.nameList;
            todayOutLineOption.series[0].data = res.data.outValueList;
            todayOutLineOption.series[1].data = res.data.inValueList;
            todayOutLine.setOption(todayOutLineOption);
        }
    })
}

// 今日进出--折线图--结束

// 近30日进出--折线图--开始
var thirtyOutLine = echarts.init(document.getElementById('thirtyOutLine'));

let thirtyOutLineData = [{
    name: "12-08",
    value1: 10,
    value2: 14,
},
    {
        name: "12-09",
        value1: 18,
        value2: 16,
    },
    {
        name: "12-10",
        value1: 50,
        value2: 34,
    },
    {
        name: "12-11",
        value1: 73,
        value2: 64,
    },
    {
        name: "12-12",
        value1: 80,
        value2: 66,
    },
    {
        name: "12-13",
        value1: 50,
        value2: 34,
    },
    {
        name: "12-14",
        value1: 80,
        value2: 35,
    }
];

let xAxisDatathirtyOutLine = thirtyOutLineData.map(v => v.name);
//  ["1", "2", "3", "4", "5", "6", "7", "8"]
let yAxisDatathirtyOutLine1 = thirtyOutLineData.map(v => v.value1);
let yAxisDatathirtyOutLine2 = thirtyOutLineData.map(v => v.value2);

var thirtyOutLineOption = {
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
                辆`;
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
        right: '8%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [{
        type: "category",
        boundaryGap: false,
        axisLabel: {
            formatter: '{value}',
            textStyle: {
                color: "#fff"
            }
        },
        axisLine: {
            lineStyle: {
                color: "#09526f"
            }
        },
        data: xAxisDatathirtyOutLine
    }],
    yAxis: [{
        type: "value",
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
        name: "出场",
        type: "line",
        smooth: true,
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
        data: yAxisDatathirtyOutLine1
    }, {
        name: "入库",
        type: "line",
        smooth: true,
        // showSymbol: false,/
        symbolSize: 4,
        zlevel: 3,
        lineStyle: {
            normal: {
                color: color[3],
                shadowBlur: 3,
                shadowColor: hexToRgbaLineOption(color[3], 0.5),
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
                        color: hexToRgbaLineOption(color[3], 0.3)
                    },
                        {
                            offset: 1,
                            color: hexToRgbaLineOption(color[3], 0.1)
                        }
                    ],
                    false
                ),
                shadowColor: hexToRgbaLineOption(color[3], 0.1),
                shadowBlur: 10
            }
        },
        data: yAxisDatathirtyOutLine2
    }]
};

thirtyOutLine.setOption(thirtyOutLineOption);

/**
 * 路侧/停车场详情
 * 近30日进出 折线图
 */
getInOutDaysData();

function getInOutDaysData() {
    $.ajax({
        url: '/sjfxNew/getInOutDaysData.json',
        type: "POST",
        data: {id: $("#id").val(), type: "1"},
        success: function (res) {
            console.log("停车场详情 近30日进出")
            console.log(res)

            //今日进出 折线图
            thirtyOutLineOption.xAxis[0].data = res.data.outData.map(v => v.name.substring(v.name.indexOf("-") + 1));
            thirtyOutLineOption.series[0].data = res.data.outData.map(v => v.amount);
            thirtyOutLineOption.series[1].data = res.data.inData.map(v => v.amount);
            thirtyOutLine.setOption(thirtyOutLineOption);
        }
    })
}

// 近30日进出--折线图--结束

// 今日营收--折线图--开始
var todayRevenueLine = echarts.init(document.getElementById('todayRevenueLine'));

let todayRevenueLineData = [{
    name: "06:00",
    value1: 10,
},
    {
        name: "08:00",
        value1: 18,
    },
    {
        name: "10:00",
        value1: 50,
    },
    {
        name: "12:00",
        value1: 73,
    },
    {
        name: "14:00",
        value1: 80,
    },
    {
        name: "16:00",
        value1: 50,
    },
    {
        name: "18:00",
        value1: 80,
    }
];

let xAxisDatatodayRevenueLine = todayRevenueLineData.map(v => v.name);
//  ["1", "2", "3", "4", "5", "6", "7", "8"]
let yAxisDatatodayRevenueLine1 = todayRevenueLineData.map(v => v.value1);

var todayRevenueLineOption = {
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
                ${v.name}点${v.seriesName}
                <span style="color:${color[v.componentIndex]};font-weight:700;font-size: 18px">${v.value}</span>
                元`;
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
        right: '8%',
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
        data: xAxisDatatodayRevenueLine
    }],
    yAxis: [{
        type: "value",
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
        name: "营收",
        type: "line",
        smooth: true,
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
        data: yAxisDatatodayRevenueLine1
    }]
};

todayRevenueLine.setOption(todayRevenueLineOption);

/**
 * 路侧/停车场详情
 * 今日营收
 */
getAmountByHoursData();

function getAmountByHoursData() {
    $.ajax({
        url: '/sjfxNew/getAmountByHoursData.json',
        type: "POST",
        data: {id: $("#id").val(), type: "1"},
        success: function (res) {
            console.log("停车场详情 今日营收")
            console.log(res)

            //今日营收
            todayRevenueLineOption.xAxis[0].data = res.data.nameList;
            todayRevenueLineOption.series[0].data = res.data.valueList;
            todayRevenueLine.setOption(todayRevenueLineOption);
        }
    })
}

// 今日营收--折线图--结束

// 欠费分析--折线图--开始
var arrearsLine = echarts.init(document.getElementById('arrearsLine'));

let arrearsLineData = [{
    name: "06:00",
    value1: 10,
},
    {
        name: "08:00",
        value1: 18,
    },
    {
        name: "10:00",
        value1: 50,
    },
    {
        name: "12:00",
        value1: 73,
    },
    {
        name: "14:00",
        value1: 80,
    },
    {
        name: "16:00",
        value1: 50,
    },
    {
        name: "18:00",
        value1: 80,
    }
];

let xAxisDataarrearsLine = arrearsLineData.map(v => v.name);
//  ["1", "2", "3", "4", "5", "6", "7", "8"]
let yAxisDataarrearsLine1 = arrearsLineData.map(v => v.value1);

var arrearsLineOption = {
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
                ${v.name}点${v.seriesName}
                <span style="color:${color[v.componentIndex]};font-weight:700;font-size: 18px">${v.value}</span>
                元`;
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
        right: '8%',
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
        data: xAxisDataarrearsLine
    }],
    yAxis: [{
        type: "value",
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
        name: "欠费",
        type: "line",
        smooth: true,
        // showSymbol: false,/
        symbolSize: 4,
        zlevel: 3,
        lineStyle: {
            normal: {
                color: color[5],
                shadowBlur: 3,
                shadowColor: hexToRgbaLineOption(color[5], 0.5),
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
                        color: hexToRgbaLineOption(color[5], 0.3)
                    },
                        {
                            offset: 1,
                            color: hexToRgbaLineOption(color[5], 0.1)
                        }
                    ],
                    false
                ),
                shadowColor: hexToRgbaLineOption(color[5], 0.1),
                shadowBlur: 10
            }
        },
        data: yAxisDataarrearsLine1
    }]
};

arrearsLine.setOption(arrearsLineOption);

/**
 * 路侧/停车场详情
 * 欠费分析
 */
getArrearsData();

function getArrearsData() {
    $.ajax({
        url: '/sjfxNew/getArrearsData.json',
        type: "POST",
        data: {id: $("#id").val(), type: "1"},
        success: function (res) {
            console.log("停车场详情 欠费分析")
            console.log(res)

            $("#today_unpaid_amounts").html(res.data.today_unpaid_amount);

            //今日营收
            arrearsLineOption.xAxis[0].data = res.data.nameList;
            arrearsLineOption.series[0].data = res.data.valueList;
            arrearsLine.setOption(arrearsLineOption);
        }
    })
}

// 欠费分析--折线图--结束

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
        data: {id: $("#id").val(), type: "1"},
        success: function (res) {
            console.log("停车场详情 车位满位率")
            console.log(res)

            //车位满位率
            inventoryRoadLineOption.xAxis[0].data = res.data.cwMwlDatas.today_data.map(v => v.hour);
            inventoryRoadLineOption.series[0].data = res.data.cwMwlDatas.today_data.map(v => v.mwl);
            inventoryRoadLineOption.series[1].data = res.data.cwMwlDatas.yesterday_data.map(v => v.mwl);
            inventoryRoadLineOption.series[2].data = res.data.cwMwlDatas.last_week_data.map(v => v.mwl);
            inventoryRoadLine.setOption(inventoryRoadLineOption);
        }
    })
}

// 车位满位率--折线图--结束


// const payStyle = echarts.init(document.getElementById('payStyle'));

// payStyle.setOption(payStyleOption, true)

window.addEventListener("resize", function () {
    turnoverClum.resize()
    stopTimePie.resize()
    todayOutLine.resize()
    thirtyOutLine.resize()
    todayRevenueLine.resize()
    arrearsLine.resize()
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
     * 车位周转率 柱状图、饼状图
     * 柱状图：日均车位周转率：最近7天/日均车位周转率
     * 饼状图： 停车时长：停车时长状态暂定
     */
    getMainRight1Data();

    /**
     * 路边停车
     * 发票发放率、问题订单率、手动开闸率、免费订单率
     */
    getParkRight3Data();

    /**
     * 路侧/停车场详情
     * 今日进出 折线图
     */
    getInOutData();

    /**
     * 路侧/停车场详情
     * 近30日进出 折线图
     */
    getInOutDaysData();

    /**
     * 路侧/停车场详情
     * 今日营收
     */
    getAmountByHoursData();

    /**
     * 路侧/停车场详情
     * 欠费分析
     */
    getArrearsData();

    /**
     * 车位满位率
     */
    getCwMwlDatas();

    var date = new Date();//现在时刻
    var dateIntegralPoint = new Date();//用户登录时刻的下一个整点，也可以设置成某一个固定时刻
    dateIntegralPoint.setHours(date.getHours() + 1);//小时数增加1
    dateIntegralPoint.setMinutes(0);
    dateIntegralPoint.setSeconds(0);
    setTimeout("nextIntegralPointAfterLogin2();", dateIntegralPoint - date);//用户登录后的下一个整点执行。
}
