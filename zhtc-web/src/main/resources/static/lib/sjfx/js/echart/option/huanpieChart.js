var huan_pieChartOption = {
    title: {text: '图表标题', x: 'center', y: "80", textStyle: {color: 'white'}, subtextStyle: {color: 'gray'},show:false},
	legend: {        
        data: ['第一个参数','第二个参数','第三个参数','第四个参数','第五个参数','第六个参数'],//data中的名字要与series-data中的列名对应，方可点击操控
        orient: 'vertical', //图例垂直排列
        right: '2%',
        top: '4%',
		show: true,
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
    tooltip: {backgroundColor: 'rgba(0,0,0,0.8)', trigger: 'item', formatter: "{b} : {d}%"},
    series: {

        type: 'pie',
        radius: ['30%', '50%'],
        center: ['50%', '60%'],
        label: {
            normal: {
                show: true, position: 'outside', textStyle: {fontSize: 10, color: 'white'},
            },
            emphasis: {show: true, textStyle: {fontSize: 14}},
        },
        data: [
            {
                value: 52,
                name: '第一个参数名',
                itemStyle: {
                    normal: {
                        color: "#31a3ca", shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },
            }, {
                value: 28,
                name: '第二个参数名',
                itemStyle: {
                    normal: {
                        color: "#dde00d", shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },
            }, {
                value: 16,
                name: '第三个参数名',
                itemStyle: {
                    normal: {
                        color: "#f19810", shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },
            }, {
                value: 4,
                name: '第四个参数名',
                itemStyle: {
                    normal: {
                        color: "#8fc023", shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },
            }, {
                value: 4,
                name: '第五个参数名',
                itemStyle: {
                    normal: {
                        color: "#8fc023", shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },
            }, {
                value: 4,
                name: '第六个参数名',
                itemStyle: {
                    normal: {
                        color: "#8fc023", shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },
            }],
        itemStyle: {emphasis: {shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)'}}
    }
};