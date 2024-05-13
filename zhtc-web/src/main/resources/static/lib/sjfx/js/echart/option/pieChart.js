var pieChartOption = {
    title: {text: '订单来源分析', x: 'center', y: "80", textStyle: {color: 'white'}, subtextStyle: {color: 'gray'},},
    tooltip: {backgroundColor: 'rgba(0,0,0,0.8)', trigger: 'item', formatter: "{a} <br/>{b} : {c} ({d}%)"},
	legend: {
        //data中的名字要与series-data中的列名对应，方可点击操控
        data: ['底盘','车身','五金','电气','通用件'],
        orient: 'vertical', //图例垂直排列
        right: '2%',
        top: '4%',
        // orient: 'vertical', //图例垂直排列
        // left: '8%',  //图例组件离容器左侧的距离
        // align: 'right',  //图例标记和文本的对齐
        // padding: [80, 0],  //设置上下左右的内边距
        // itemWidth: 50,  //图例标记的图形宽度
        // itemHeight: 18,  //图例标记的图形高度
        // itemGap: 30,  //图例每项之间的间隔
        // 图例横向排列图标对齐
        // formatter: function( name ) {
        //      return '{a|' + name + '}';
        // },
        // textStyle: {
        //     rich:{
        //         a: {
        //             width: 100
        //         }
        //     },
        //     color : '#',
        //     fontSize : 16,
        // }
        textStyle : {
            color : '#fff',
            fontSize : 16,
        },
    },
    series: {
        name: '订单来源分析',
        type: 'pie',
        radius: '55%',
        center: ['50%', '60%'],
        label: {
            normal: {show: true, position: 'outside', textStyle: {fontSize: '14', color: 'white'}},
            emphasis: {show: true, textStyle: {fontSize: 14}}
        },
        data: [{
            value: 60,
            name: '第一个参数名',
            itemStyle: {
                normal: {
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{offset: 0, color: '#d453f6' /* 0% 处的颜色*/}, {
                            offset: 1,
                            color: '#5f66f3' /* 100% 处的颜色*/
                        }],
                        globalCoord: false /* 缺省为 false*/
                    }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
        }, {
            value: 30,
            name: '第二个参数名',
            itemStyle: {
                normal: {
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{offset: 0, color: '#51c6ee' /* 0% 处的颜色*/}, {
                            offset: 1,
                            color: '#52f199' /* 100% 处的颜色*/
                        }],
                        globalCoord: false /* 缺省为 false*/
                    }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
        }, {
            value: 20,
            name: '第三个参数名',
            itemStyle: {
                normal: {
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{offset: 0, color: '#50b055' /* 0% 处的颜色*/}, {
                            offset: 1,
                            color: '#daf163' /* 100% 处的颜色*/
                        }],
                        globalCoord: false /* 缺省为 false*/
                    }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
        }, {
            value: 40,
            name: '第四个参数名',
            itemStyle: {
                normal: {
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{offset: 0, color: '#ffb199' /* 0% 处的颜色*/}, {
                            offset: 1,
                            color: '#ff0844' /* 100% 处的颜色*/
                        }],
                        globalCoord: false /* 缺省为 false*/
                    }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
        }, {
            value: 30,
            name: '第五个参数名',
            itemStyle: {
                normal: {
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{offset: 0, color: '#ff6c69' /* 0% 处的颜色*/}, {
                            offset: 1,
                            color: '#eb9c60' /* 100% 处的颜色*/
                        }],
                        globalCoord: false /* 缺省为 false*/
                    }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
        }, {
            value: 80,
            name: '第六个参数名',
            itemStyle: {
                normal: {
                    color: {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{offset: 0, color: '#ff6c69' /* 0% 处的颜色*/}, {
                            offset: 1,
                            color: '#eb9c60' /* 100% 处的颜色*/
                        }],
                        globalCoord: false /* 缺省为 false*/
                    }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            },
        }],
        itemStyle: {emphasis: {shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)'}}
    }
};