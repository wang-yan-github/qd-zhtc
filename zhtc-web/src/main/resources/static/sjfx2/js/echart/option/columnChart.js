var columnChartOption = {
    title: {text: '季度销售额', textStyle: {color: "white"}},
    grid: {containLabel: true},
    tooltip: {
        extraCssText: 'box-shadow: 10 10 10px 30px rgba(0, 0, 0, 0.8);',
        trigger: 'axis',
        axisPointer: {
            type: 'line',
            lineStyle: {
                /*                    color:'blue',//设置提示框线条颜色*/
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{offset: 0, color: '#5d76da' /* 0% 处的颜色*/}, {
                        offset: 1,
                        color: '#2668cc' /* 100% 处的颜色*/
                    }],
                    globalCoord: false /* 缺省为 false*/
                }
            },
            shadowStyle: {
                opacity: .2,
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{offset: 0, color: '#5d76da' /* 0% 处的颜色*/}, {
                        offset: 1,
                        color: '#2668cc' /* 100% 处的颜色*/
                    }],
                    globalCoord: false /* 缺省为 false*/
                }
            }
        },
        backgroundColor: 'rgba(0,0,0,0.6)',
        padding: 10,
        textStyle: {color: 'white'}
    },
    xAxis: {
        data: ["01/01", "01/02", "01/03", "01/04", "01/05", "01/06", "01/07"],
        axisLabel: {textStyle: {color: "#26a8e6"}, /* formatter: function (val) { return val.split("").join("\n"); }*/},
        axisLine: {lineStyle: {color: '#26a8e6', width: 2,}},
        splitLine: {show: true, lineStyle: {opacity: 0.2, color: '#26a8e6'}}
    },
    yAxis: {
        type: 'value',
        name: '售额/季度',
        min: 0,
        minInterval: 1,
        nameLocation: 'end',
        nameTextStyle: {color: '#26a8e6', fontSize: '12'},
        axisLabel: {textStyle: {color: "#26a8e6"}},
        axisLine: {lineStyle: {width: 2, color: '#26a8e6',}},
        splitLine: {show: true, lineStyle: {opacity: 0.2, color: '#26a8e6'}}
    },
    series: [{
        name: '售额',
        type: 'bar',
        barWidth: '50%',
        data: [5, 20, 36, 10, 10, 20, 30],
        itemStyle: {
            normal: {
                barBorderRadius: 0,
                /*echart渐变*/
// 线性渐变，前四个参数分别是 x0, y0, x2, y2, 范围从 0 - 1，相当于在图形包围盒中的百分比，如果 globalCoord 为 `true`，则该四个值是绝对的像素位置
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                        offset: 0, color: '#26a8e6' // 0% 处的颜色
                    }, {
                        offset: 1, color: '#1ec5af' // 100% 处的颜色
                    }],
                    globalCoord: false // 缺省为 false
                },
                shadowColor: 'rgba(0, 0, 0, 0.4)',
                shadowBlur: 20,
            }
        },
        label: {
            normal: {
                show: false,            //显示数字
                position: 'top',       //这里可以自己选择位置
                formatter: function (e) {
                    return e.data + "元"
                }
            }
        }
    }]
};


/*最近日期-------------------------------------------------------------------------*/
columnChartOption.xAxis.data = testDate.getLastDates(7, true);