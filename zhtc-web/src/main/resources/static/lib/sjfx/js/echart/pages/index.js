window.parent.showLoading();
window.parent.showoverlay();
var fontSize = 12;
var color1 = "#15eded";
var color2 = "#029cf5";

var color3 = "#80da9a";
var color4 = "#1bb1a1";

var color5 = "#fb7d64";
var color6 = "#f93b67";

var color7 = "#50b055";
var color8 = "#daf163";

//各路段收费总额排名TOP5
var totalChargesTop5Data = {};
//今日收费总额趋势分析
var totalChargesData = {};
//各路段报警数量排名TOP5
var errorParkData = {};
//各路段在停车辆总数排名TOP5
var carTop5Data = {};
//昨日停车时长占比分析
var parkTimeData = [];
//近五日车位占用量及趋势
var participatingData = [];


//加载图表数据接口
$.ajax({
    type: "POST",
    url: "/sjfx/getDataList.json",
    data: {type: "0"},
    async: false,
    contentType: "application/x-www-form-urlencoded; charset=utf-8",
    dataType: "JSON",
    success: function (data) {
        console.log(data)
        //各路段收费总额排名TOP5
        totalChargesTop5Data = data.data.totalChargesTop5Data;
        //今日收费总额趋势分析
        totalChargesData = data.data.totalChargesData;
        //各路段报警数量排名TOP5
        errorParkData = data.data.errorParkData;
        // 各路段在停车辆总数排名TOP5
        carTop5Data = data.data.carTop5Data;
        //昨日停车时长占比分析
        parkTimeData = data.data.parkTimeData;
        //近五日车位占用量及趋势
        participatingData = data.data.participatingData;
    },
    error: function (data) {
        // layer.msg(data.message, {time: 1500});
    }
})

var scrWidth = window.innerWidth;
// 各路段在停车辆总数排名TOP5
var grid0 = {
    left: '20px',
    top: '28px',
    right: '10px',
    bottom: '0px',
    containLabel: true
};

var spe_DIV = document.getElementById("zzt1");
var columnChart = echarts.init(spe_DIV);
var columnChartOption = JSON.parse(JSON.stringify(columnChartOption));
columnChartOption.yAxis.name = "车辆数/路段";
columnChartOption.grid = grid0;
columnChartOption.title.show = false;
columnChartOption.tooltip.formatter = "路段：{b}<br>数量：{c}辆";
// columnChartOption.xAxis.data = ["A区", "B区", "C区", "D区", "E区"];
// columnChartOption.series[0].data = [835, 452, 530, 540, 835];
columnChartOption.xAxis.data = carTop5Data.xData;
columnChartOption.series[0].data = carTop5Data.yData;
columnChartOption.series[0].itemStyle.normal.barBorderRadius = 5;
columnChartOption.series[0].itemStyle.normal.borderWidth = 0;
columnChartOption.series[0].itemStyle.normal.borderColor = color1;
columnChartOption.series[0].itemStyle.normal.color.colorStops[0].color = color1;  //柱状图上渐变颜色
columnChartOption.series[0].itemStyle.normal.color.colorStops[1].color = color2;  //柱状图下渐变颜色
//columnChartOption.series[0].itemStyle.normal.color = function (params) {
//    return linearGradientColor[params.dataIndex];
//}
columnChartOption.series[0].label = {
    normal: {
        show: true,
        position: 'top',
        formatter: '{c}辆',
        textBorderWidth: 0,
        color: '#fff'
    }
};

//柱状图底部文字2行显示，一行显示4个字
columnChartOption.xAxis.axisLabel = {
    textStyle: {color: "#26a8e6"},
    formatter: function(value) {
        var str = "";
        var num = 4; //每行显示字数
        var valLength = value.length; //该项x轴字数
        var rowNum = Math.ceil(valLength / num); // 行数
        if(rowNum > 1) {
            for(var i = 0; i < rowNum; i++) {
                var temp = "";
                var start = i * num;
                var end = start + num;

                temp = value.substring(start, end) + "\n";
                str += temp;
            }
            return str;
        } else {
            return value;
        }
    },
};

// 昨日停车时长占比分析
var grid4 = {
    containLabel: true,
    left: 0,
    right: 0,
    top: 300,
    bottom: 0
};
var pieChart_DIV = document.getElementById("bt1");
var pieCharts = echarts.init(pieChart_DIV);
pieChartOption.grid = grid4;
pieChartOption.title.show = false;
pieChartOption.title.text = "昨日停车时长占比分析";
pieChartOption.tooltip.formatter = "时长：{b}<br>占比：{c}%";
pieChartOption.series.radius = scrWidth > 1400 ? "52%" : "40%";
pieChartOption.series.center = scrWidth > 1400 ? ['50%', '56%'] : ['50%', '56%'];
pieChartOption.series.minAngle = 15;//最小角度
pieChartOption.series.startAngle = 270;//起始角度
pieChartOption.legend.show = scrWidth > 1400 ? "true" : "false";
pieChartOption.legend.data = scrWidth > 1400 ? ['0~2小时', '2~4小时', '4~6小时', '6~8小时', '8小时以上'] : "false";
pieChartOption.legend.top = scrWidth > 1400 ? "5%" : "3%";
pieChartOption.legend.right = scrWidth > 1400 ? "8%" : "0px";
pieChartOption.legend.textStyle.fontSize = 12;
pieChartOption.legend.left = 'center';
pieChartOption.legend.orient = "horizontal";

pieChartOption.series.label = {
    normal: {
        show: true,
        position: 'outside',
        textStyle: {fontSize: '14', color: 'white'},
        formatter: '{b}'
    }
};
var valueNumber1 = parkTimeData;
var nameText1 = ['0~2小时', '2~4小时', '4~6小时', '6~8小时', '8小时以上'];

pieChartOption.series.data = [];
for (var i = 0; i < valueNumber1.length; i++) {
    pieChartOption.series.data.push({value: valueNumber1[i], name: nameText1[i], itemStyle: itemStyle2[i]});
}


// 各路段违规停车量
var grid1 = {
    left: '20px',
    top: '30px',
    right: '10px',
    bottom: '0px',
    containLabel: true
};
var spe_DIV1 = document.getElementById("zzt2");
var columnChart1 = echarts.init(spe_DIV1);
var columnChartOption1 = JSON.parse(JSON.stringify(columnChartOption));
columnChartOption1.yAxis.name = "数量/路段";
columnChartOption1.yAxis.axisLabel.textStyle.color = color4;  //Y轴字颜色
columnChartOption1.xAxis.axisLabel.textStyle.color = color4;  //X轴字颜色
columnChartOption1.yAxis.axisLine.lineStyle.color = color4;   //Y轴线颜色
columnChartOption1.xAxis.axisLine.lineStyle.color = color4;   //X轴线颜色
columnChartOption1.grid = grid1;
columnChartOption1.title.show = false;
columnChartOption1.tooltip.formatter = "路段：{b}<br>数量：{c}辆";
// columnChartOption1.xAxis.data = ["A区", "B区", "C区", "D区", "E区"];
// columnChartOption1.series[0].data = [835, 452, 530, 540, 835];
columnChartOption1.xAxis.data = errorParkData.xData;
columnChartOption1.series[0].data = errorParkData.yData;
//columnChartOption1.series[0].itemStyle.normal.color = function (params) {
//    return linearGradientColor[params.dataIndex];
//}
columnChartOption1.series[0].itemStyle.normal.barBorderRadius = 5;
columnChartOption1.series[0].itemStyle.normal.borderWidth = 0;
columnChartOption1.series[0].itemStyle.normal.borderColor = color1;
columnChartOption1.series[0].itemStyle.normal.color.colorStops[0].color = color3;  //柱状图上渐变颜色
columnChartOption1.series[0].itemStyle.normal.color.colorStops[1].color = color4;  //柱状图下渐变颜色
columnChartOption1.series[0].label = {normal: {show: true, position: 'top', formatter: '{c}辆'}};

//柱状图底部文字2行显示，一行显示4个字
columnChartOption1.xAxis.axisLabel = {
    textStyle: {color: "#26a8e6"},
    formatter: function(value) {
        var str = "";
        var num = 4; //每行显示字数
        var valLength = value.length; //该项x轴字数
        var rowNum = Math.ceil(valLength / num); // 行数
        if(rowNum > 1) {
            for(var i = 0; i < rowNum; i++) {
                var temp = "";
                var start = i * num;
                var end = start + num;

                temp = value.substring(start, end) + "\n";
                str += temp;
            }
            return str;
        } else {
            return value;
        }
    },
};

/////////////////////////////////// 近五日车位占用量及趋势 开始////////////////////////////////////////////////
// var lineChart2 = echarts.init(document.getElementById("zxt1"));
// var grid2 = {
//     left: '10px',
//     top: '40px',
//     right: '20px',
//     bottom: '0px',
//     containLabel: true
// };
// lineChartOption.grid = grid2;
// lineChartOption.title.text = "近五日车位占用量及趋势";
// lineChartOption.title.show=false;
// lineChartOption.legend.show = false;
// lineChartOption.legend.data[0].name = lineChartOption.series[0].name = "去年";
// lineChartOption.legend.data[1].name = lineChartOption.series[1].name = "今年";
// lineChartOption.yAxis.name = "数量/日期";
// lineChartOption.tooltip.formatter = '{c0}辆';
// var lineChartValues1 = [200, 110, 320, 120, 160];

// // var lineChartTexts = ['1月', '2月', '3月', '4月', '5月', '6月'];
// lineChartOption.series[0].data = lineChartValues1;
// lineChartOption.series[1].data = [];
// lineChartOption.xAxis.data = getDates(5, true);

// //近五日车位占用量及趋势
// var curveChart = echarts.init(document.getElementById("zxt1"));
// var grid2 = {
//     left: '18px',
//     top: '40px',
//     right: '33px',
//     bottom: '0px',
//     containLabel: true
// };
// curveChartOption.grid = grid2;
// curveChartOption.title.show = false;
// curveChartOption.yAxis.name = "数量/日期";
// curveChartOption.tooltip.formatter = '{c0}辆';
// var lineChartValues1 = participatingData;
// //var lineChartValues2 = [101, 255, 70, 160, 440, 415, 534, 233, 265, 137, 356, 306];
// // var lineChartTexts = ['1月', '2月', '3月', '4月', '5月', '6月'];
// curveChartOption.series[0].data = lineChartValues1;
// // curveChartOption.series[1].data = [];
// curveChartOption.xAxis.data = getDates(5, true);

// 各路段收费总额排名TOP5
var rankingChart = echarts.init(document.getElementById("bt2"));
var gridranking = {
    left: '18px',
    top: '40px',
    right: '52px',
    bottom: '0px',
    containLabel: true
};
rankingChartOption.grid = gridranking;
rankingChartOption.title.text = "各路段收费总额排名TOP5";
rankingChartOption.title.show = false;
rankingChartOption.series[0].label = {normal: {show: true, position: 'right', formatter: '{c}元'}};
rankingChartOption.series[0].itemStyle.normal.barBorderRadius = 20;
rankingChartOption.xAxis.show = false;
rankingChartOption.yAxis.axisLine.show = false;
rankingChartOption.yAxis.splitLine.show = false;
rankingChartOption.yAxis.axisTick = {show: false,};
rankingChartOption.tooltip.axisPointer.type = 'shadow';

rankingChartOption.yAxis.name = "TOP5 路段";
rankingChartOption.tooltip.formatter = "路段：<b style='font-size: 16px;'>{b}</b><br>金额：<b style='line-height: 1.3;font-size: 16px;'>{c}元</b>";
// var rankingValues = ['30', '22', '20', '10', '5'];
// var rankingTexts = ['A区', 'B区', 'C区', 'D区', 'E区'];
var rankingValues = totalChargesTop5Data.yData;
var rankingTexts = totalChargesTop5Data.xData;
rankingValues.reverse();
rankingTexts.reverse();
rankingChartOption.xAxis.data = [];
rankingChartOption.yAxis.data = [];
rankingChartOption.series[0].data = [];
for (var i = 0; i < rankingValues.length; i++) {
    rankingChartOption.series[0].data.push({
        value: rankingValues[i],
        name: rankingTexts[i],
        itemStyle: itemStyle3_horizontal[i]
    });
    rankingChartOption.yAxis.data.push(rankingTexts[i]);
}


var grid3 = {
    left: '10px',
    top: '40px',
    right: '30px',
    bottom: '0px',
    containLabel: true
};
var lineChart3 = echarts.init(document.getElementById("zxt2"));
var lineChartOption1 = JSON.parse(JSON.stringify(lineChartOption));
lineChartOption1.grid = grid3;
lineChartOption1.title.text = "各路段今日收费总额趋势分析";
lineChartOption1.title.show = false;
lineChartOption1.legend.show = true;
lineChartOption1.legend.data[0].name = lineChartOption.series[0].name = "今日";
lineChartOption1.legend.data[1].name = lineChartOption.series[1].name = "昨日";
lineChartOption1.legend.top = scrWidth > 1400 ? "15%" : "0px";
lineChartOption1.legend.right = scrWidth > 1400 ? "8%" : "0px";
lineChartOption1.yAxis.name = "金额/时间";
lineChartOption1.tooltip.formatter = '今日：{c0}元<br>昨日：{c1}元';
// var lineChartValues1 = [200, 110, 320, 120, 160, 360];
// var lineChartValues2 = [101, 255, 70, 160, 440, 256];
var lineChartTexts = ['0~4点', '4~8点', '8~12点', '12~16点', '16~20点', '20~24点'];
var yDataToday = totalChargesData.yDataToday;
var yDataYesterday = totalChargesData.yDataYesterday;
lineChartOption1.series[0].data = yDataToday;
lineChartOption1.series[1].data = yDataYesterday;
lineChartOption1.xAxis.data = lineChartTexts;


// 获取最近一段日期（日期格式：2019-02-27）
function getDates(day, isIncludeToday) {
    day || (day = 7);

    var thisWeek = [],
        now = new Date(),
        nowDay = now.getDate(), //当前日,
        nowMonth = now.getMonth(), //当前月
        nowYear = now.getFullYear(), //当前年
        lastDate;

    for (var i = (isIncludeToday ? 1 : 0) - 1 * day; i < (isIncludeToday ? 1 : 0); i++) {
        lastDate = new Date(nowYear, nowMonth, nowDay + i);
        thisWeek.push(lastDate.getFullYear() + '-' + ("0" + (lastDate.getMonth() + 1)).slice(-2) + '-' + ("0" + lastDate.getDate()).slice(-2));
    }

    return thisWeek;
}

function loadPage() {
    var screenH = window.innerHeight;
    var screenW = window.innerWidth;
    document.querySelector('.leftbox').style.height = (screenH - 120) + 'px';
    document.querySelector('.midbox').style.height = (screenH - 145) + 'px';
}

/*chart自适应________________________*/
function resizeDiv() {
    var screenH = window.innerHeight;
    columnChart.resize();
    columnChart1.resize();
    //lineChart2.resize();
    lineChart3.resize();
    pieCharts.resize();
    // curveChart.resize();
    rankingChart.resize();

}

window.onresize = function () {
    resizeDiv();
}

window.onload = function () {
    window.parent.hideLoading();
    window.parent.hideoverlay();
    //loadPage();
    resizeDiv();
    //图标数据接口
    // getDataList();

    columnChart.setOption(columnChartOption);
    pieCharts.setOption(pieChartOption);
    columnChart1.setOption(columnChartOption1);
    // lineChart2.setOption(lineChartOption);
    lineChart3.setOption(lineChartOption1);
    // curveChart.setOption(curveChartOption);
    rankingChart.setOption(rankingChartOption);

}
