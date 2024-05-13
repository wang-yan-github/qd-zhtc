var curveChartOption = {
    tooltip: {
        trigger: 'axis',
        extraCssText: 'box-shadow: 10 10 10px 30px rgba(0, 0, 0, 0.8);',
        axisPointer: {
            type: 'line',
            lineStyle: {
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{offset: 1, color: '#5d76da' /* 0% 处的颜色*/}, {offset: 0, color: '#2668cc' /* 100% 处的颜色*/}],
                    globalCoord: false, /* 缺省为 false*/
                },
            }
        },
        backgroundColor: 'rgba(0,0,0,0.6)',
        padding: 10,
        textStyle: {color: 'white'}
    },
    title: {x: 'left', text: '近一周退票额', textStyle: {color: 'white',}},
    xAxis: {
        position: 'bottom',
        type: 'category',
        axisLabel: {textStyle: {color: "#26a8e6"}, /* formatter: function (val) { return val.split("").join("\n"); }*/},
        axisLine: {lineStyle: {color: '#26a8e6', width: 2,}},
        splitLine: {show: true, lineStyle: {opacity: 0.2, color: '#26a8e6'}},
        boundaryGap: false,
        data: ['01/01', '01/02', '01/03', '01/04', '01/05', '01/06', '01/07']
    },
    yAxis: {
        position: 'left',
        type: 'value',
        name: '金额(元)/日期',
        min: 0,
        minInterval: 1,
        nameLocation: 'end',
        nameTextStyle: {color: '#26a8e6', fontSize: '12'},
        axisLabel: {textStyle: {color: "#26a8e6"}, /* formatter: function (val) { return val.split("").join("\n"); }*/},
        axisLine: {lineStyle: {color: '#26a8e6', width: 2,}},
        splitLine: {show: true, lineStyle: {opacity: 0.2, color: '#26a8e6'}}
    },
    series: [{
        name: '模拟数据',
        type: 'line',
        smooth: true,
        symbol: 'none',
        sampling: 'average',
        itemStyle: {normal: {color: '#5be8e2', width: 20, borderWidth: 6, borderColor: 'rgba(108, 231, 231,0.5)', lineStyle: {color: '#5be8e2', width: 4,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: '#3d7aff'}, {offset: 1, color: 'rgba(108, 231, 231,0)'}])}},		
        data: [111, 222, 444, 333, 555, 666, 111, 777]
    }]
};


/*2018.12.26最近日期-------------------------------------------------------------------------*/
curveChartOption.xAxis.data = testDate.getLastDates(7,true);