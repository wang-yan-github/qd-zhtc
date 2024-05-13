var lineChartBlueOption = {
    /*设置grid属性*/
    grid: {
        show: false,
        left: '14px',
        top: '60px',
        right: '16px',
        bottom: '0px',
        containLabel: true,
        backgroundColor: 'white', /*show: true的时候才显示*/
        borderColor: '#ccc',
        borderWidth: 1, /*更多属性访问http://echarts.baidu.com/option.html#grid*/
    },
    title: {x: 'left', text: '近一周检票量', textStyle: {color: 'white'},},
    tooltip: {
        trigger: 'axis',
        formatter: '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#5be8e2"></span>此年龄段人数: {c}人',
        axisPointer: {type: 'cross', label: {backgroundColor: '#6a7985'},}
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['学龄前儿童', '小学生', '中学生', '大学生', '青年人', '中年人', '老年人'],
        axisLabel: {textStyle: {color: '#5be8e2'},},
        splitLine: {show: true, lineStyle: {color: ['rgba(255, 255, 255,0.1)'],}},
        axisLine: {lineStyle: {color: '#5be8e2', width: 2,},},
    },
    yAxis: {
        type: 'value',
        name: '人数/年龄段',
        min: 0,
        minInterval: 1,
        nameLocation: 'end',
        nameTextStyle: {color: '#2484db', fontSize: '12'},
        axisLabel: {textStyle: {color: '#5be8e2'},},
        splitLine: {show: true, lineStyle: {color: ['rgba(255, 255, 255,0.1)'],}},
        axisLine: {lineStyle: {color: '#5be8e2', width: 2,},},
    },
    series: [{
        name: '此年龄段人数',
        smooth: false, /*平滑*/
        type: 'line',
        data: [320, 280, 260, 340, 380, 360, 210],
        itemStyle: {normal: {color: '#5be8e2', width: 20, borderWidth: 6, borderColor: 'rgba(108, 231, 231,0.5)', lineStyle: {color: '#5be8e2', width: 4,}}},
        areaStyle: {normal: {color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{offset: 0, color: '#3d7aff'}, {offset: 1, color: 'rgba(108, 231, 231,0)'}])}},
    },]
};

/*2018.12.26最近日期-------------------------------------------------------------------------*/
//lineChartBlueOption.xAxis.data = top.testDate.getLastDates(7,true);