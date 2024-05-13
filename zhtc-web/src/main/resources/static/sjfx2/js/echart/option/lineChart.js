var lineChartOption = {
    title: {x: 'left', text: '近一周售票额', textStyle: {color: 'white'},},
    tooltip: {
        trigger: 'axis',
        formatter: '<span style="display:inline-block;text-align: left;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#87ffd1"></span>总销售额: {c1}万<br><span style="display:inline-block;text-align: left;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#ff4dff"></span>支付宝扫码销售额: {c0}万',
        /*formatter:function(value){
            console.log(value);
        },*/
        axisPointer: {type: 'cross', label: {backgroundColor: '#6a7985'}}
    },
    legend: {
        data: [{
            name: '总销售额',
            icon: 'none',
            textStyle: {color: 'rgb(255, 77, 255)', backgroundColor: 'rgb(255, 77, 255)',},
        }, {
            name: '支付宝扫码销售额',
            icon: 'none',
            textStyle: {color: 'rgb(135, 255, 209)', backgroundColor: 'rgb(135, 255, 209)',},
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
        name: '金额(元)/日期',
        min: 0,
        minInterval: 1,
        nameLocation: 'end',
        nameTextStyle: {color: 'rgb(135, 255, 209)', fontSize: '12'},
        axisLabel: {textStyle: {color: 'rgb(135, 255, 209)'},},
        splitLine: {show: true, lineStyle: {color: ['rgba(255, 255, 255,0.1)'],}},
        axisLine: {lineStyle: {color: 'rgb(135, 255, 209)', width: 2,},},
    },
    series: [{
        name: '支付宝扫码销售额',
        smooth: false, /*平滑*/
        type: 'line',
        data: [320, 280, 260, 340, 380, 360, 210],
        itemStyle: {normal: {color: 'white', width: 20, borderWidth: 6, borderColor: 'rgba(135, 255, 209,0.5)', lineStyle: {color: 'rgb(135, 255, 209)', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: 'rgb(135, 255, 209)'}, {offset: 1, color: 'rgba(48, 178, 48,0)'}])}},
    }, {
        name: '总销售额',
        smooth: false, /*平滑*/
        type: 'line',
        stack: '总量',
        data: [550, 460, 580, 520, 460, 560, 690],
        itemStyle: {normal: {color: 'rgb(255, 70, 131)', width: 20, borderWidth: 6, borderColor: 'rgba(255, 77, 255,0.5)', lineStyle: {color: 'rgb(255, 77, 255)', width: 3,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: 'rgb(255, 77, 255)'}, {offset: 1, color: 'rgba(122, 38, 252,0)'}])}},
    }]
};


/*2018.12.26最近日期-------------------------------------------------------------------------*/
lineChartOption.xAxis.data = testDate.getLastDates(7,true);