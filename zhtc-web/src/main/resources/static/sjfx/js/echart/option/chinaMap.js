/** Created by vszq on 2017/7/8. */

var XZData = [
    [{name: '都匀'}, {name: '南京', value: 36980}],
    [{name: '都匀'}, {name: '杭州', value: 35500}],
    [{name: '都匀'}, {name: '广州', value: 32060}],
    [{name: '都匀'}, {name: '上海', value: 28840}],
    [{name: '都匀'}, {name: '济南', value: 20540}],
    [{name: '都匀'}, {name: '昆明', value: 16301}],
    [{name: '都匀'}, {name: '重庆', value: 13602}],
    [{name: '都匀'}, {name: '成都', value: 14580}],
    [{name: '都匀'}, {name: '太原', value: 15749}],
    [{name: '都匀'}, {name: '北京', value: 9398}],
];


var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';
var convertData = function (data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
        var dataItem = data[i];
        var fromCoord = geoCoordMap[dataItem[1].name];
        var toCoord = geoCoordMap[dataItem[0].name];
        if (fromCoord && toCoord) {
            res.push({fromName: dataItem[1].name, toName: dataItem[0].name, coords: [fromCoord, toCoord], value: dataItem[1].value});
            /*2018.12.26 修改从哪儿到哪儿的方向*/
        }
    }
    return res;
};
var color = ['#9fddea', '#9fddea', '#9fddea'];
var series = [];
[['都匀', XZData]].forEach(function (item, i) {
    series.push({
            name: item[0],
            type: 'lines',
            zlevel: 1,
            effect: {show: true, period: 6, trailLength: 0.7, color: '#fff', symbolSize: 3},
            lineStyle: {normal: {color: color[i], width: 0, curveness: 0.2}},
            data: convertData(item[1])
        },
        {
            name: item[0],
            type: 'lines',
            zlevel: 2,
            symbol: ['none', 'arrow'],
            symbolSize: 10,
            effect: {show: true, period: 6, trailLength: 0, symbol: planePath, symbolSize: 15},
            lineStyle: {normal: {color: color[i], width: 1, opacity: 0.6, curveness: 0.2}},
            data: convertData(item[1])
        },
        {
            name: item[0],
            type: 'effectScatter',
            coordinateSystem: 'geo',
            zlevel: 2,
            rippleEffect: {brushType: 'stroke'},
            label: {normal: {show: true, position: 'right', formatter: '{b}'}},
            symbolSize: function (val) {
                // return val[2] / 8;
                return 16;//出发点直径
            },
            itemStyle: {normal: {color: color[i]}},
            data: item[1].map(function (dataItem) {
                return {name: dataItem[1].name, value: geoCoordMap[dataItem[1].name].concat([dataItem[1].value])};
            })
        });
});
var chinaMapOption = {
    grid: {
        show: false,
        left: '0px',
        top: '0px',
        right: '0px',
        bottom: '0px',
        containLabel: true,
        backgroundColor: 'white', /*show: true的时候才显示*/
        borderColor: '#ccc',
        borderWidth: 1, /*更多属性访问http://echarts.baidu.com/columnChartOption.html#grid*/
    },
    tooltip: {
        trigger: 'item',
        formatter: function (a) {

            /*获取移入节点的数据-------------------------------------------------------------------------*/
            //use strict
			var tpl = '<b>[from]</b> → <b>[to]</b> （[num]人）';
            console.log(a);
            return top.bind.a(tpl, {
                from: a.name || a.data.fromName,
                to: a.seriesName || a.data.toName,
                num: a.value instanceof Array ? a.value[a.value.length - 1] : a.value                
            });
        }
    },
    geo: {

        map: 'china',
        label: {
            emphasis: {
                show: false,
            }
        },
        roam: true,
        itemStyle: {
            normal: {
                areaColor: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{offset: 0, color: '#467da6' /* 0% 处的颜色*/},
                        {offset: 1, color: '#2b4d79' /* 100% 处的颜色*/}],
                    globalCoord: false /* 缺省为 false*/
                }, borderColor: '#558bbb', borderWidth: 1
            },
            emphasis: {
                areaColor: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{offset: 0, color: '#62c6ec' /* 0% 处的颜色*/},
                        {offset: 1, color: '#3f6c98' /* 100% 处的颜色*/}],
                    globalCoord: false /* 缺省为 false*/
                }
            }
        },
    },
    series: series
};
