var lineChartOption = {
    title: {x: 'left', text: '丰县景区客流量统计', textStyle: {color: 'white'},},
    tooltip: {
        trigger: 'axis',
        formatter: '',
        /*formatter:function(value){
            console.log(value);
        },*/
        axisPointer: {type: 'cross', label: {backgroundColor: '#6a7985'}}
    },
    legend: {
        data: [{
            name: '丰县景区总客流量统计',
            icon: 'none',
            textStyle: {color: 'rgb(255, 77, 255)', backgroundColor: 'rgb(255, 77, 255)',},
        }, {
            name: '汉皇祖陵客流量统计',
            icon: 'none',
            textStyle: {color: 'rgb(135, 255, 209)', backgroundColor: 'rgb(135, 255, 209)',},
        }, {
            name: '汉皇祖陵客流量统计',
            icon: 'none',
            textStyle: {color: 'rgb(77, 127, 255)', backgroundColor: 'rgb(77, 127, 255)',},
        }, {
            name: '汉皇祖陵客流量统计',
            icon: 'none',
            textStyle: {color: 'rgb(255, 224, 77)', backgroundColor: 'rgb(255, 224, 77)',},
        }, {
            name: '汉皇祖陵客流量统计',
            icon: 'none',
            textStyle: {color: 'rgb(77, 255, 85)', backgroundColor: 'rgb(77, 255, 85)',},
        }, {
            name: '汉皇祖陵客流量统计',
            icon: 'none',
            textStyle: {color: 'rgb(255, 115, 77)', backgroundColor: 'rgb(255, 115, 77)',},
        }, {
            name: '汉皇祖陵客流量统计',
            icon: 'none',
            textStyle: {color: 'rgb(91, 233, 225)', backgroundColor: 'rgb(91, 233, 225)',},
        }],
        textStyle: {color: 'white'},
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['01/01', '01/02', '01/03', '01/04', '01/05', '01/06', '01/07'],
        axisLabel: {textStyle: {color: 'rgb(135, 255, 209)'},},
        splitLine: {show: true, lineStyle: {color: ['rgba(255, 255, 255,0.1)'],}},
        axisLine: {lineStyle: {color: 'rgb(135, 255, 209)', width: 2,},},
    },
    yAxis: {
        type: 'value',
        name: '人数/年份(年)',
        min: 0,
        minInterval: 1,
        nameLocation: 'end',
        nameTextStyle: {color: 'rgb(135, 255, 209)', fontSize: '12'},
        axisLabel: {textStyle: {color: 'rgb(135, 255, 209)'},},
        splitLine: {show: true, lineStyle: {color: ['rgba(255, 255, 255,0.1)'],}},
        axisLine: {lineStyle: {color: 'rgb(135, 255, 209)', width: 2,},},
    },
    series: [{
        name: '丰县景区总客流量统计',
        smooth: false, /*平滑*/
        type: 'line',
        data: [320, 280, 260, 340, 380, 360, 210],
        itemStyle: {normal: {color: 'rgb(255, 70, 131)', width: 20, borderWidth: 6, borderColor: 'rgba(255, 77, 255,0.5)', lineStyle: {color: 'rgb(255, 77, 255)', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: 'rgb(255, 77, 255)'}, {offset: 1, color: 'rgba(122, 38, 252,0)'}])}},
    }, {
        name: '汉皇祖陵客流量统计',
        smooth: false, /*平滑*/
        type: 'line',
        stack: '总数',
        data: [550, 460, 580, 520, 460, 560, 690],
		itemStyle: {normal: {color: 'white', width: 20, borderWidth: 6, borderColor: 'rgba(135, 255, 209,0.5)', lineStyle: {color: 'rgb(135, 255, 209)', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: 'rgb(135, 255, 209)'}, {offset: 1, color: 'rgba(48, 178, 48,0)'}])}},        
    }, {
        name: '果都大观园客流量统计',
        smooth: false, /*平滑*/
        type: 'line',
        stack: '总数',
        data: [550, 460, 580, 520, 460, 560, 690],
        itemStyle: {normal: {color: '#4fadfc', width: 20, borderWidth: 6, borderColor: 'rgba(135, 200, 255,0.5)', lineStyle: {color: '#4fadfc', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: '#4fadfc'}, {offset: 1, color: 'rgba(122, 38, 252,0)'}])}},
    }, {
        name: '丰县农家乐客流量统计',
        smooth: false, /*平滑*/
        type: 'line',
        stack: '总数',
        data: [550, 460, 580, 520, 460, 560, 690],
        itemStyle: {normal: {color: 'rgb(252, 183, 36)', width: 20, borderWidth: 6, borderColor: 'rgba(253, 200, 85,0.5)', lineStyle: {color: 'rgb(253, 200, 85)', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: 'rgb(252, 183, 36)'}, {offset: 1, color: 'rgba(122, 38, 252,0)'}])}},
    }, {
        name: '百年犁园客流量统计',
        smooth: false, /*平滑*/
        type: 'line',
        stack: '总数',
        data: [550, 460, 580, 520, 460, 560, 690],
        itemStyle: {normal: {color: 'rgb(32, 219, 32)', width: 20, borderWidth: 6, borderColor: 'rgba(85, 232, 85,0.5)', lineStyle: {color: 'rgb(32, 219, 32)', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: 'rgb(32, 219, 32)'}, {offset: 1, color: 'rgba(122, 38, 252,0)'}])}},
    }, {
        name: '渊子湖客流量统计',
        smooth: false, /*平滑*/
        type: 'line',
        stack: '总数',
        data: [550, 460, 580, 520, 460, 560, 690],
        itemStyle: {normal: {color: 'rgb(250, 54, 54)', width: 20, borderWidth: 6, borderColor: 'rgba(249, 91, 91,0.5)', lineStyle: {color: 'rgb(250, 54, 54)', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: 'rgb(250, 54, 54)'}, {offset: 1, color: 'rgba(122, 38, 252,0)'}])}},
    }, {
        name: '国庆节',
        smooth: false, /*平滑*/
        type: 'line',
        stack: '总数',
        data: [550, 460, 580, 520, 460, 560, 690],
        itemStyle: {normal: {color: 'rgb(250, 54, 54)', width: 20, borderWidth: 6, borderColor: 'rgba(81, 212, 204,0.5)', lineStyle: {color: 'rgb(59, 199, 191)', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: 'rgb(59, 199, 191)'}, {offset: 1, color: 'rgba(122, 38, 252,0)'}])}},
    }]
};


/*2018.12.26最近日期-------------------------------------------------------------------------*/
//lineChartOption.xAxis.data = top.testDate.getLastDates(7,true);