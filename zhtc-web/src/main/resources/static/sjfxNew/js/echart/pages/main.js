// window.parent.showLoading();
var scrWidth = window.innerWidth;
window.onresize = function () {
    // resizeDiv();
}
// window.onload = function () {
// 	window.parent.hideLoading();

// 	//loadPage();
//     resizeDiv();

// }

// 今日营收--折线图--开始
var revenueLine = echarts.init(document.getElementById('revenueLine'));

let color = [
    "#16dada",
    "#0090FF",
    "#FFC005",
    '#30d3cc',
    '#0ec1ff',
    "#f4a762",
    "#8B5CFF",
    "#00CA69",
    "#36CE9E"
];
let revenueData = [{
    name: "1",
    value1: 100,
},
    {
        name: "2",
        value1: 138,
    },
    {
        name: "3",
        value1: 350,
    },
    {
        name: "4",
        value1: 173,
    },
    {
        name: "5",
        value1: 180,
    },
    {
        name: "6",
        value1: 150,
    },
    {
        name: "7",
        value1: 180,
    },
    {
        name: "8",
        value1: 230,
    }
];

let xAxisDataR = revenueData.map(v => v.name);
//  ["1", "2", "3", "4", "5", "6", "7", "8"]
let yAxisDataR1 = revenueData.map(v => v.value1);
// [100, 138, 350, 173, 180, 150, 180, 230]
// let yAxisData2 = echartData.map(v => v.value2);
// [233, 233, 200, 180, 199, 233, 210, 180]
const hexToRgba = (hex, opacity) => {
    let rgbaColor = "";
    let reg = /^#[\da-f]{6}$/i;
    if (reg.test(hex)) {
        rgbaColor = `rgba(${parseInt("0x" + hex.slice(1, 3))},${parseInt(
            "0x" + hex.slice(3, 5)
        )},${parseInt("0x" + hex.slice(5, 7))},${opacity})`;
    }
    return rgbaColor;
}

var revenueLineOption = {
    color: color,
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
        top: '24',
        left: '3%',
        right: '4%',
        bottom: '20px',
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
        data: xAxisDataR
    }],
    yAxis: [{
        type: "value",
        name: '金额/元',
        axisLabel: {
            textStyle: {
                color: "#fff"
            }
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
        name: "收费",
        type: "line",
        smooth: true,
        // showSymbol: false,/
        symbolSize: 4,
        zlevel: 3,
        lineStyle: {
            normal: {
                color: color[0],
                shadowBlur: 3,
                shadowColor: hexToRgba(color[0], 0.5),
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
                        color: hexToRgba(color[0], 0.3)
                    },
                        {
                            offset: 1,
                            color: hexToRgba(color[0], 0.1)
                        }
                    ],
                    false
                ),
                shadowColor: hexToRgba(color[0], 0.1),
                shadowBlur: 10
            }
        },
        data: yAxisDataR1
    }]
};

revenueLine.setOption(revenueLineOption);

/**
 * 左图1
 * 今日营收 折线图
 * 24小时、金额
 */
getMainLeft1LineChartData();

function getMainLeft1LineChartData() {
    $.ajax({
        url: '/sjfxNew/getMainLeft1LineChartData.json',
        type: "POST",
        data: {},
        success: function (res) {
            console.log("今日营收 折线图")
            console.log(res)
            revenueLineOption.xAxis[0].data = res.data.nameList;
            revenueLineOption.series[0].data = res.data.valueList;
            revenueLine.setOption(revenueLineOption);
        }
    })
}

// 今日营收--折线图--结束

// 车场盘点--折线图--开始
var inventoryLine = echarts.init(document.getElementById('inventoryLine'));

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

var inventoryLineOption = {
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
        top: '32',
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

inventoryLine.setOption(inventoryLineOption);

// 车场盘点--折线图--结束

//占用率--饼图--开始
var occupyPie = echarts.init(document.getElementById('occupyPie'));
let angle = 0; //角度，用来做简单的动画效果的
let value = 80;
var timerId;
var occupyPieOption = {
    title: {
        text: '{a|' + value + '}{c|%}',
        x: 'center',
        y: 'center',
        textStyle: {
            rich: {
                a: {
                    fontSize: 22,
                    color: '#29EEF3'
                },

                c: {
                    fontSize: 12,
                    color: '#ffffff',
                    // padding: [5,0]
                }
            }
        }
    },

    series: [
        // 紫色 0
        {
            name: "ring5",
            type: 'custom',
            coordinateSystem: "none",
            renderItem: function (params, api) {
                return {
                    type: 'arc',
                    shape: {
                        cx: api.getWidth() / 2,
                        cy: api.getHeight() / 2,
                        r: Math.min(api.getWidth(), api.getHeight()) / 2 * 0.6,
                        startAngle: (0 + angle) * Math.PI / 180,
                        endAngle: (90 + angle) * Math.PI / 180
                    },
                    style: {
                        stroke: "#8383FA",
                        fill: "transparent",
                        lineWidth: 1
                    },
                    silent: true
                };
            },
            data: [0]
        }, {
            name: "ring5", //紫点 1
            type: 'custom',
            coordinateSystem: "none",
            renderItem: function (params, api) {
                let x0 = api.getWidth() / 2;
                let y0 = api.getHeight() / 2;
                let r = Math.min(api.getWidth(), api.getHeight()) / 2 * 0.6;
                let point = getCirlPoint(x0, y0, r, (90 + angle))
                return {
                    type: 'circle',
                    shape: {
                        cx: point.x,
                        cy: point.y,
                        r: 4
                    },
                    style: {
                        stroke: "#8450F9", //绿
                        fill: "#8450F9"
                    },
                    silent: true
                };
            },
            data: [0]
        },
        // 蓝色 2

        {
            name: "ring5",
            type: 'custom',
            coordinateSystem: "none",
            renderItem: function (params, api) {
                return {
                    type: 'arc',
                    shape: {
                        cx: api.getWidth() / 2,
                        cy: api.getHeight() / 2,
                        r: Math.min(api.getWidth(), api.getHeight()) / 2 * 0.6,
                        startAngle: (180 + angle) * Math.PI / 180,
                        endAngle: (270 + angle) * Math.PI / 180
                    },
                    style: {
                        stroke: "#4386FA",
                        fill: "transparent",
                        lineWidth: 1
                    },
                    silent: true
                };
            },
            data: [0]
        },
        {
            name: "ring5", // 蓝色 3
            type: 'custom',
            coordinateSystem: "none",
            renderItem: function (params, api) {
                let x0 = api.getWidth() / 2;
                let y0 = api.getHeight() / 2;
                let r = Math.min(api.getWidth(), api.getHeight()) / 2 * 0.6;
                let point = getCirlPoint(x0, y0, r, (180 + angle))
                return {
                    type: 'circle',
                    shape: {
                        cx: point.x,
                        cy: point.y,
                        r: 4
                    },
                    style: {
                        stroke: "#4386FA", //绿
                        fill: "#4386FA"
                    },
                    silent: true
                };
            },
            data: [0]
        },
        // 4
        {
            name: "ring5",
            type: 'custom',
            coordinateSystem: "none",
            renderItem: function (params, api) {
                return {
                    type: 'arc',
                    shape: {
                        cx: api.getWidth() / 2,
                        cy: api.getHeight() / 2,
                        r: Math.min(api.getWidth(), api.getHeight()) / 2 * 0.65,
                        startAngle: (270 + -angle) * Math.PI / 180,
                        endAngle: (40 + -angle) * Math.PI / 180
                    },
                    style: {
                        stroke: "#0CD3DB",
                        fill: "transparent",
                        lineWidth: 1
                    },
                    silent: true
                };
            },
            data: [0]
        },
        // 橘色 5

        {
            name: "ring5",
            type: 'custom',
            coordinateSystem: "none",
            renderItem: function (params, api) {
                return {
                    type: 'arc',
                    shape: {
                        cx: api.getWidth() / 2,
                        cy: api.getHeight() / 2,
                        r: Math.min(api.getWidth(), api.getHeight()) / 2 * 0.65,
                        startAngle: (90 + -angle) * Math.PI / 180,
                        endAngle: (220 + -angle) * Math.PI / 180
                    },
                    style: {
                        stroke: "#FF8E89",
                        fill: "transparent",
                        lineWidth: 1
                    },
                    silent: true
                };
            },
            data: [0]
        }, { //6
            name: "ring5",
            type: 'custom',
            coordinateSystem: "none",
            renderItem: function (params, api) {
                let x0 = api.getWidth() / 2;
                let y0 = api.getHeight() / 2;
                let r = Math.min(api.getWidth(), api.getHeight()) / 2 * 0.65;
                let point = getCirlPoint(x0, y0, r, (90 + -angle))
                return {
                    type: 'circle',
                    shape: {
                        cx: point.x,
                        cy: point.y,
                        r: 4
                    },
                    style: {
                        stroke: "#FF8E89", //粉
                        fill: "#FF8E89"
                    },
                    silent: true
                };
            },
            data: [0]
        }, {
            name: "ring5", //绿点 7
            type: 'custom',
            coordinateSystem: "none",
            renderItem: function (params, api) {
                let x0 = api.getWidth() / 2;
                let y0 = api.getHeight() / 2;
                let r = Math.min(api.getWidth(), api.getHeight()) / 2 * 0.65;
                let point = getCirlPoint(x0, y0, r, (270 + -angle))
                return {
                    type: 'circle',
                    shape: {
                        cx: point.x,
                        cy: point.y,
                        r: 4
                    },
                    style: {
                        stroke: "#0CD3DB", //绿
                        fill: "#0CD3DB"
                    },
                    silent: true
                };
            },
            data: [0]
        }, { //8
            name: '吃猪肉频率',
            type: 'pie',
            radius: ['52%', '40%'],
            silent: true,
            clockwise: true,
            startAngle: 90,
            z: 0,
            zlevel: 0,
            label: {
                normal: {
                    position: "center",

                }
            },
            data: [{
                value: value,
                name: "",
                itemStyle: {
                    normal: {
                        color: { // 完成的圆环的颜色
                            colorStops: [{
                                offset: 0,
                                color: '#A098FC' // 0% 处的颜色
                            },
                                {
                                    offset: 0.3,
                                    color: '#4386FA' // 0% 处的颜色
                                },
                                {
                                    offset: 0.6,
                                    color: '#4FADFD' // 0% 处的颜色
                                },
                                {
                                    offset: 0.8,
                                    color: '#0CD3DB' // 100% 处的颜色
                                }, {
                                    offset: 1,
                                    color: '#646CF9' // 100% 处的颜色
                                }
                            ]
                        },
                    }
                }
            },
                {
                    value: 100 - value,
                    name: "",
                    label: {
                        normal: {
                            show: false
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: "#173164"
                        }
                    }
                }
            ]
        },
        {
            name: '吃猪肉频率',
            type: 'pie',
            radius: ['32%', '35%'],
            silent: true,
            clockwise: true,
            startAngle: 270,
            z: 0,
            zlevel: 0,
            label: {
                normal: {
                    position: "center",

                }
            },
            data: [{
                value: value,
                name: "",
                itemStyle: {
                    normal: {
                        color: { // 完成的圆环的颜色
                            colorStops: [{
                                offset: 0,
                                color: '#00EDF3' // 0% 处的颜色
                            }, {
                                offset: 1,
                                color: '#646CF9' // 100% 处的颜色
                            }]
                        },
                    }
                }
            },
                {
                    value: 100 - value,
                    name: "",
                    label: {
                        normal: {
                            show: false
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: "#173164"
                        }
                    }
                }
            ]
        },

    ]
};

//获取圆上面某点的坐标(x0,y0表示坐标，r半径，angle角度)
function getCirlPoint(x0, y0, r, angle) {
    let x1 = x0 + r * Math.cos(angle * Math.PI / 180)
    let y1 = y0 + r * Math.sin(angle * Math.PI / 180)
    return {
        x: x1,
        y: y1
    }
}

/**
 * 左图2
 * 车场盘点 折线图、柱状图
 * 3折线图：单位：车位满位率（在停数量/订单总数量） 今日/昨天/上周同天 从0时到当前时间
 * 满位率、在停车辆
 * 柱状图：停车场/路侧名称 在停数量top5
 */
getMainLeft2Data();

function getMainLeft2Data() {
    $.ajax({
        url: '/sjfxNew/getMainLeft2Data.json',
        type: "POST",
        data: {},
        success: function (res) {
            console.log("车场盘点")
            console.log(res)

            inventoryLineOption.xAxis[0].data = res.data.today_data.map(v => v.hour);
            inventoryLineOption.series[0].data = res.data.today_data.map(v => v.mwl);
            inventoryLineOption.series[1].data = res.data.yesterday_data.map(v => v.mwl);
            inventoryLineOption.series[2].data = res.data.last_week_data.map(v => v.mwl);
            inventoryLine.setOption(inventoryLineOption);

            var ztCount = new CountUp("ztCount", 0, res.data.ztCount, 0, 2, options);
            ztCount.start();

            $("#ztTopList").empty();
            var _html = "";
            for (var i = 0; i < res.data.top5Data.length; i++) {
                _html += "<div class='num-sort-item num-sort-item-" + (i + 1) + "'>" +
                    "<p title='" + res.data.top5Data[i].placeName + "'>" + res.data.top5Data[i].placeName + "</p>" +
                    "<span class='shuzifont'>" + res.data.top5Data[i].ztCount + "<i>次</i></span>" +
                    "</div>";
            }
            $("#ztTopList").html(_html);

            value = res.data.mwl;
            occupyPieOption.title.text = '{a|' + value + '}{c|%}';
            occupyPieOption.series[8].data[0] = value;
            // occupyPieOption.series[8].data[1] = 100 - value;
            occupyPieOption.series[9].data[0] = value;
            // occupyPieOption.series[9].data[1] = 100 - value;
            occupyPie.setOption(occupyPieOption, true);
        }
    })
}

function draw() {
    angle = angle + 3
    occupyPie.setOption(occupyPieOption, true)
    //window.requestAnimationFrame(draw);
}

if (timerId) {
    clearInterval(timerId);
}
timerId = setInterval(function () {
    //用setInterval做动画感觉有问题
    draw()
}, 100);


//占用率--饼图--结束

//本地占车比 -- 水球 --开始
var loaclCar = echarts.init(document.getElementById('loaclCar'));
var loaclCarvalue = 0.45;
var loaclCarOption = {
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
            data: [loaclCarvalue, loaclCarvalue], // data个数代表波浪数
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
loaclCar.setOption(loaclCarOption, true)

//本地占车比 -- 水球 --结束

//绑定占车比 -- 水球 --开始
var bindCar = echarts.init(document.getElementById('bindCar'));
var bindCarvalue = 0.75;
var bindCarOption = {
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
                            color: '#2aa1e3',
                        },
                        {
                            offset: 1,
                            color: '#08bbc9',
                        },
                    ],
                    globalCoord: false,
                },
            ],
            data: [bindCarvalue, bindCarvalue], // data个数代表波浪数
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
bindCar.setOption(bindCarOption, true)

//绑定占车比 -- 水球 --结束

//支付方式--立体柱形--开始
// const payStyle = echarts.init(document.getElementById('payStyle'));
// const CubeLeft = echarts.graphic.extendShape({
//     shape: {
//         x: 0,
//         y: 0
//     },
//     buildPath: function (ctx, shape) {
//         const xAxisPoint = shape.xAxisPoint
//         const c0 = [shape.x, shape.y]
//         const c1 = [shape.x - 9, shape.y - 9]
//         const c2 = [xAxisPoint[0] - 9, xAxisPoint[1] - 9]
//         const c3 = [xAxisPoint[0], xAxisPoint[1]]
//         ctx.moveTo(c0[0], c0[1]).lineTo(c1[0], c1[1]).lineTo(c2[0], c2[1]).lineTo(c3[0], c3[1]).closePath()
//     }
// })
// const CubeRight = echarts.graphic.extendShape({
//     shape: {
//         x: 0,
//         y: 0
//     },
//     buildPath: function (ctx, shape) {
//         const xAxisPoint = shape.xAxisPoint
//         const c1 = [shape.x, shape.y]
//         const c2 = [xAxisPoint[0], xAxisPoint[1]]
//         const c3 = [xAxisPoint[0] + 18, xAxisPoint[1] - 9]
//         const c4 = [shape.x + 18, shape.y - 9]
//         ctx.moveTo(c1[0], c1[1]).lineTo(c2[0], c2[1]).lineTo(c3[0], c3[1]).lineTo(c4[0], c4[1]).closePath()
//     }
// })
// const CubeTop = echarts.graphic.extendShape({
//     shape: {
//         x: 0,
//         y: 0
//     },
//     buildPath: function (ctx, shape) {
//         const c1 = [shape.x, shape.y]
//         const c2 = [shape.x + 18, shape.y - 9]
//         const c3 = [shape.x + 9, shape.y - 18]
//         const c4 = [shape.x - 9, shape.y - 9]
//         ctx.moveTo(c1[0], c1[1]).lineTo(c2[0], c2[1]).lineTo(c3[0], c3[1]).lineTo(c4[0], c4[1]).closePath()
//     }
// })
// echarts.graphic.registerShape('CubeLeft', CubeLeft)
// echarts.graphic.registerShape('CubeRight', CubeRight)
// echarts.graphic.registerShape('CubeTop', CubeTop)
// const payStyleValue = [2012, 1230, 3790, 2349];
// var payStyleOption = {
//     title: {
//         text: '',
//         top: 32,
//         left: 18,
//         textStyle: {
//             color: '#00F6FF',
//             fontSize: 24
//         }
//     },
//     grid: {
//         left: 10,
//         right: 10,
//         bottom: 10,
//         top: 40,
//         containLabel: true
//     },
//     tooltip: {
//         trigger: 'axis',
//         formatter: function (params) {
//             let html = '';
//             params.forEach(v => {
//                 html += `<div style="color: #fff;font-size: 14px;line-height: 24px">
//                 <span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color[4]};"></span>
//                 ${v.name}
//                 <span style="color:${color[4]};font-weight:700;font-size: 18px">${v.value}</span>
//                 元`;
//             })
//             return html
//         },
//         extraCssText: 'background: #05183a; border-radius: 0;box-shadow: 0 0 3px rgba(13, 44, 100, 0.8);color: #fff;border:1px solid #041129',
//
//
//     },
//     xAxis: {
//         type: 'category',
//         data: ['支付宝', '微信', '钱包', '现金'],
//         axisLine: {
//             show: true,
//             lineStyle: {
//                 color: 'white'
//             }
//         },
//         offset: 4,
//         axisTick: {
//             show: false,
//             length: 9,
//             alignWithLabel: true,
//             lineStyle: {
//                 color: '#7DFFFD'
//             }
//         },
//         axisLabel: {
//             fontSize: 12
//         }
//     },
//     yAxis: {
//         type: 'value',
//         name: '金额/元',
//         axisLine: {
//             show: true,
//             lineStyle: {
//                 color: 'white'
//             }
//         },
//         splitLine: {
//             show: false
//         },
//         axisTick: {
//             show: false
//         },
//         axisLabel: {
//             fontSize: 12
//         },
//         boundaryGap: ['20%', '20%']
//     },
//     series: [{
//         type: 'custom',
//         renderItem: (params, api) => {
//             const location = api.coord([api.value(0), api.value(1)])
//             return {
//                 type: 'group',
//                 children: [{
//                     type: 'CubeLeft',
//                     shape: {
//                         api,
//                         xValue: api.value(0),
//                         yValue: api.value(1),
//                         x: location[0],
//                         y: location[1],
//                         xAxisPoint: api.coord([api.value(0), 0])
//                     },
//                     style: {
//                         fill: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
//                             offset: 0,
//                             color: '#3B80E2'
//                         },
//                             {
//                                 offset: 1,
//                                 color: '#49BEE5'
//                             }
//                         ])
//                     }
//                 }, {
//                     type: 'CubeRight',
//                     shape: {
//                         api,
//                         xValue: api.value(0),
//                         yValue: api.value(1),
//                         x: location[0],
//                         y: location[1],
//                         xAxisPoint: api.coord([api.value(0), 0])
//                     },
//                     style: {
//                         fill: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
//                             offset: 0,
//                             color: '#3B80E2'
//                         },
//                             {
//                                 offset: 1,
//                                 color: '#49BEE5'
//                             }
//                         ])
//                     }
//                 }, {
//                     type: 'CubeTop',
//                     shape: {
//                         api,
//                         xValue: api.value(0),
//                         yValue: api.value(1),
//                         x: location[0],
//                         y: location[1],
//                         xAxisPoint: api.coord([api.value(0), 0])
//                     },
//                     style: {
//                         fill: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
//                             offset: 0,
//                             color: '#3B80E2'
//                         },
//                             {
//                                 offset: 1,
//                                 color: '#49BEE5'
//                             }
//                         ])
//                     }
//                 }]
//             }
//         },
//         data: payStyleValue
//     },]
// };
// payStyle.setOption(payStyleOption, true)


// var grid3 = {
//     containLabel: true,
//     left: 0,
//     right: 30,
//     top:'10%',
//     bottom: 0
// };
// var pieChart_DIV = document.getElementById("payStyle");
// var pieCharts = echarts.init(pieChart_DIV);
// pieChartOption.grid=grid3;
// pieChartOption.title.show = false;
// pieChartOption.title.text = "支付方式/金额";
// pieChartOption.tooltip.formatter = "支付方式：{b}<br>占比：{c}%";
// pieChartOption.series.radius = scrWidth > 1400 ? "50%" : "50%";
// pieChartOption.series.center = scrWidth > 1400 ? ['50%', '55%'] : ['45%', '53%'];
// pieChartOption.legend.show = scrWidth > 1400 ? "true" : "false";
// pieChartOption.legend.data=scrWidth > 1400 ? ['包月', '支付宝', '现金'] : "false";
// pieChartOption.legend.orient='horizontal';
// pieChartOption.legend.top = scrWidth > 1400 ? "10%" : "0px";
// pieChartOption.legend.left = scrWidth > 1400 ? "center" : "0px";
// pieChartOption.legend.textStyle.fontSize=12;
// var valueNumber1 = ['20','60','80'];
// var nameText1 = ['包月', '支付宝', '现金'];
// pieChartOption.series.data = [];
// for (var i = 0; i < valueNumber1.length; i++) {
//     pieChartOption.series.data.push({value: valueNumber1[i], name: nameText1[i], itemStyle: itemStyle2[i]});
// }
// pieCharts.setOption(pieChartOption);

const yesterdayStopCarPie = echarts.init(document.getElementById('payStyle'));
var tccsNameList = ['包月', '微信', '支付宝', '钱包', '现金', '商家支付'];
var yesterdayStopCarPieOption = {
    legend: {
        // orient: 'vertical',
        orient: 'horizontal',
        top: "4%",
        left: "center",
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
        name: '支付方式/金额',
        type: 'pie',
        radius: ['30%', '60%'],
        center: ['50%', '65%'],
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
            name: '包月',
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
                name: '微信',
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
                name: '支付宝',
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
                value: 324,
                name: '钱包',
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
                value: 324,
                name: '现金',
                label: {
                    color: '#fff'
                },
                itemStyle: {
                    color: "rgba(15,197,243,0.7)",
                    borderColor: "rgba(15,197,243,1)",
                    borderWidth: 1
                }
            },
            {
                value: 324,
                name: '商家支付',
                label: {
                    color: '#fff'
                },
                itemStyle: {
                    color: "rgba(43,179,177,0.7)",
                    borderColor: "rgba(43,179,177,1)",
                    borderWidth: 1
                }
            }
        ]
    }]
};
yesterdayStopCarPie.setOption(yesterdayStopCarPieOption, true)

/**
 * 左图3
 * 车主分析
 * 用户总数、今日活跃度、7日活跃度（活跃度=车主表绑定openid数量）
 * 本地车占比、绑定车占比（车主表绑定openid的数量）
 * 柱状图 支付方式/金额
 */
getMainLeft3Data();

function getMainLeft3Data() {
    $.ajax({
        url: '/sjfxNew/getMainLeft3Data.json',
        type: "POST",
        data: {},
        success: function (res) {
            console.log("车主分析")
            console.log(res)

            //用户总数、今日活跃度、7日活跃度
            var user_count = new CountUp("user_count", 0, res.data.user_count, 0, 2, options);
            var today_count = new CountUp("today_count", 0, res.data.today_count, 0, 2, options);
            var days_count = new CountUp("days_count", 0, res.data.days_count, 0, 2, options);
            user_count.start();
            today_count.start();
            days_count.start();

            //本地车占比、
            loaclCarOption.series[0].data = [res.data.car_count, res.data.car_count];
            loaclCar.setOption(loaclCarOption, true);
            // 绑定车占比
            loaclCarOption.series[0].data = [res.data.bind_count, res.data.bind_count];
            bindCar.setOption(loaclCarOption, true);

            //饼状图 支付方式/金额
            tccsNameList = res.data.nameList;
            yesterdayStopCarPieOption.legend.data = tccsNameList;
            for (var i = 0; i < tccsNameList.length; i++) {
                yesterdayStopCarPieOption.series[0].data[i].value = res.data.valueList[i];
            }
            yesterdayStopCarPie.setOption(yesterdayStopCarPieOption, true);
        }
    })
}

// 支付方式--立体柱形--结束

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
        bottom: '3%',
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
                console.log(v)
                html += `<div style="color: #fff;font-size: 14px;line-height: 24px">
                <span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${color[v.componentIndex]};"></span>
                ${v.name}${v.seriesName}
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
 * 右图1
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
        data: {type: "2"},
        success: function (res) {
            console.log("车位周转率")
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

// 近七日服务类型趋势--折线图--开始
var serveLine = echarts.init(document.getElementById('serveLine'));

let serveLineData = [{
    name: "08-01",
    value1: 10,
    value2: 14,
    value3: 13,
},
    {
        name: "08-02",
        value1: 18,
        value2: 16,
        value3: 12,
    },
    {
        name: "08-03",
        value1: 50,
        value2: 34,
        value3: 43,
    },
    {
        name: "08-04",
        value1: 73,
        value2: 64,
        value3: 54,
    },
    {
        name: "08-05",
        value1: 80,
        value2: 66,
        value3: 53,
    },
    {
        name: "08-06",
        value1: 50,
        value2: 34,
        value3: 24,
    },
    {
        name: "08-07",
        value1: 80,
        value2: 35,
        value3: 32,
    }
];

let xAxisDataserveLine = serveLineData.map(v => v.name);
//  ["1", "2", "3", "4", "5", "6", "7", "8"]
let yAxisDataserveLine1 = serveLineData.map(v => v.value1);
let yAxisDataserveLine2 = serveLineData.map(v => v.value2);
let yAxisDataserveLine3 = serveLineData.map(v => v.value3);

var serveLineOption = {
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
                ${v.name}${v.seriesName}
                <span style="color:${color[v.componentIndex]};font-weight:700;font-size: 18px">${v.value}</span>
                条`;
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
        data: xAxisDataserveLine
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
        name: "车主反馈",
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
        data: yAxisDataserveLine1
    }, {
        name: "订单申诉",
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
        data: yAxisDataserveLine2
    }, {
        name: "车牌申诉",
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
        data: yAxisDataserveLine3
    }]
};

serveLine.setOption(serveLineOption);

/**
 * 右图2
 * 客户服务 折线图
 * 车主反馈：总咨询量 今日/昨日
 * 订单申诉：总咨询量 今日/昨日
 * 车牌申诉：总咨询量 今日/昨日
 * 订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 4.修正车牌处理
 * 折线图：近七日服务类型趋势 车主反馈/订单申诉/车牌申诉 最近7日/数量
 */
getMainRight2Data();

function getMainRight2Data() {
    $.ajax({
        url: '/sjfxNew/getMainRight2Data.json',
        type: "POST",
        data: {type: "2"},
        success: function (res) {
            console.log("客户服务")
            console.log(res)

            // 车主反馈：总咨询量 今日/昨日
            // 订单申诉：总咨询量 今日/昨日
            // 车牌申诉：总咨询量 今日/昨日
            var today_feedbackcount = new CountUp("today_feedbackcount", 0, res.data.today_feedbackcount, 0, 2, options);
            var yesterday_feedbackcount = new CountUp("yesterday_feedbackcount", 0, res.data.yesterday_feedbackcount, 0, 2, options);
            var today_appealcount = new CountUp("today_appealcount", 0, res.data.today_appealcount, 0, 2, options);
            var yesterday_appealcount = new CountUp("yesterday_appealcount", 0, res.data.yesterday_appealcount, 0, 2, options);
            var today_carnocount = new CountUp("today_carnocount", 0, res.data.today_carnocount, 0, 2, options);
            var yesterday_carnocount = new CountUp("yesterday_carnocount", 0, res.data.yesterday_carnocount, 0, 2, options);

            today_feedbackcount.start();
            yesterday_feedbackcount.start();
            today_appealcount.start();
            yesterday_appealcount.start();
            today_carnocount.start();
            yesterday_carnocount.start();

            //订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 4.修正车牌处理
            $("#appealHandleRecordData").empty();
            var _html = "";
            for (var i = 0; i < res.data.appealHandleRecordData.length; i++) {
                _html += "<div class='serve-num-sort-item serve-num-sort-item-" + (i + 1) + "'>" +
                    "<p>" + res.data.appealHandleRecordData[i].name + "</p>" +
                    "<span class='shuzifont'>" + res.data.appealHandleRecordData[i].counts + "<i>条</i></span>" +
                    "</div>";
            }
            $("#appealHandleRecordData").html(_html);

            //折线图：近七日服务类型趋势
            serveLineOption.xAxis[0].data = res.data.operateFeedbackData.map(v => v.name.substring(v.name.indexOf("-") + 1));
            serveLineOption.series[0].data = res.data.operateFeedbackData.map(v => v.counts);//车主反馈 最近7日/数量
            serveLineOption.series[1].data = res.data.operateAppealData.map(v => v.counts);//订单申诉 最近7日/数量
            serveLineOption.series[2].data = res.data.carnoAppealData.map(v => v.counts);//车牌申诉 最近7日/数量
            serveLine.setOption(serveLineOption);
        }
    })
}

// 近七日服务类型趋势--折线图--结束

//非现金支付占比 -- 水球 --开始
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
//非现金支付占比 -- 水球 --结束

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

//平均周转率 -- 水球 --开始
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

//平均周转率 -- 水球 --结束

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
getMainRight3Data();

function getMainRight3Data() {
    $.ajax({
        url: '/sjfxNew/getMainRight3Data.json',
        type: "POST",
        data: {},
        success: function (res) {
            console.log("路边停车")
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
    revenueLine.resize();
    inventoryLine.resize();
    payStyle.resize()
    turnoverClum.resize()
    stopTimePie.resize()
    trendLine.resize()
    serveLine.resize()
})

window.onload = function () {
    nextIntegralPointAfterLogin2();
}

/**
 * 定时任务
 * 每个整点重新数据渲染
 */
function nextIntegralPointAfterLogin2() {
    console.log("-----总览 js 定时任务------");

    // IntegralPointExecute();//在整点执行的函数，在每个整点都调用该函数
    setInterval("IntegralPointExecute2();", 60 * 60 * 1000);//一个小时执行一次，那么下一个整点，下下一个整点都会执行
}

function IntegralPointExecute2() {
    console.log("-----总览js 每个整点重新数据渲染------");
    console.log(new Date().getHours());

    /**
     * 左图1
     * 今日营收 折线图
     * 24小时、金额
     */
    getMainLeft1LineChartData();

    /**
     * 左图2
     * 车场盘点 折线图、柱状图
     * 3折线图：单位：车位满位率（在停数量/订单总数量） 今日/昨天/上周同天 从0时到当前时间
     * 占用率、在停车辆
     * 柱状图：停车场/路侧名称 在停数量top5
     */
    getMainLeft2Data();

    /**
     * 左图3
     * 车主分析
     * 用户总数、今日活跃度、7日活跃度（活跃度=车主表绑定openid数量）
     * 本地车占比、绑定车占比（车主表绑定openid的数量）
     * 柱状图 支付方式/金额
     */
    getMainLeft3Data();

    /**
     * 右图1
     * 车位周转率 柱状图、饼状图、双折线图
     * 柱状图：日均车位周转率：最近7天/日均车位周转率
     * 饼状图： 停车时长：停车时长状态暂定
     * 折线图： 出入趋势双折线：7天内出入次数
     */
    getMainRight1Data();

    /**
     * 右图2
     * 客户服务 折线图
     * 车主反馈：总咨询量 今日/昨日
     * 订单申诉：总咨询量 今日/昨日
     * 车牌申诉：总咨询量 今日/昨日
     * 订单申诉处置方式：1.结束时间处理 2.订单费用处理 3.退款处理 4.修正车牌处理
     * 折线图：近七日服务类型趋势 车主反馈/订单申诉/车牌申诉 最近7日/数量
     */
    getMainRight2Data();

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
    getMainRight3Data();

    var date = new Date();//现在时刻
    var dateIntegralPoint = new Date();//用户登录时刻的下一个整点，也可以设置成某一个固定时刻
    dateIntegralPoint.setHours(date.getHours() + 1);//小时数增加1
    dateIntegralPoint.setMinutes(0);
    dateIntegralPoint.setSeconds(0);
    setTimeout("nextIntegralPointAfterLogin2();", dateIntegralPoint - date);//用户登录后的下一个整点执行。
}
