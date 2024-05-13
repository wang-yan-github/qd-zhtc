window.parent.showLoading();
window.parent.showoverlay();

/////////////////////////////////// 柱状图 开始：近五日售票额////////////////////////////////////////////////
var fontSize = 12;
var color1 = "#15eded";
var color2 = "#029cf5";

var color3 = "#80da9a";
var color4 = "#1bb1a1";

var color5 = "#fb7d64";
var color6 = "#f93b67";

var color7 = "#50b055";
var color8 = "#daf163";


var grid1 = {
    left: '0px',
    top: '10px',
    right: '10px',
    bottom: '0px',
    containLabel: true
};
columnChartOption.grid = grid1;
columnChartOption.title.show = false;
columnChartOption.xAxis.axisLabel.textStyle.fontSize = fontSize;
columnChartOption.xAxis.axisLabel.textStyle.color = color1;
columnChartOption.xAxis.axisLine.lineStyle.color = color2;
columnChartOption.yAxis.axisLabel.textStyle.fontSize = fontSize;
columnChartOption.yAxis.axisLabel.textStyle.color = color2;
columnChartOption.yAxis.axisLine.lineStyle.color = color2;
columnChartOption.yAxis.nameTextStyle.fontSize = fontSize;
columnChartOption.yAxis.nameTextStyle.color = color1;
columnChartOption.tooltip.axisPointer.shadowStyle.color.colorStops[0].color = color1;
columnChartOption.tooltip.axisPointer.shadowStyle.color.colorStops[1].color = color2;
columnChartOption.series[0].itemStyle.normal.color.colorStops[0].color = color3;
columnChartOption.series[0].itemStyle.normal.color.colorStops[1].color = color4;
columnChartOption.series[0].itemStyle.normal.barBorderRadius = 0;
columnChartOption.series[0].itemStyle.normal.borderWidth = 0;
columnChartOption.series[0].itemStyle.normal.borderColor = color1;
columnChartOption.series[0].label = {normal: {show: true, position: 'top', formatter: '{c}家'}};
//columnChartOption.series[0].itemStyle.normal.color.colorStops.push({offset: 0, color: color3}, {offset: 1, color: color4});
columnChartOption.tooltip.axisPointer.type = 'shadow';
columnChartOption.yAxis.name = "商家数/停车场";
columnChartOption.tooltip.textStyle.align = "left";
columnChartOption.tooltip.formatter = "停车场：{b}<br>人数：{c}人";

var columnChart = echarts.init(document.getElementById("speDiv"));

parkBusinessCount();
function parkBusinessCount(){
    $.ajax({
        url: '/sjfx/parkBusinessCount.do',
        data: {},
        success: function (res){
            columnChartOption.xAxis.data = res.data.parks;
            columnChartOption.series[0].data = res.data.count;
            columnChart.setOption(columnChartOption);
        }
    })
}
/////////////////////////////////// 柱状图 近五日售票额结束 ////////////////////////////////////////////////

var scrWidth = window.innerWidth;


///////////////////////////////// 柱形图2开始： 旅客来源统计 ///////////////////////////////////
var grid2 = {
    containLabel: true,
    left: 0,
    right: 30,
    top:30,
    bottom: 0
};
var columnChart2_DIV = document.getElementById("columnChart2");
var columnChart2 = echarts.init(columnChart2_DIV);
var columnChartOption2 = JSON.parse(JSON.stringify(columnChartOption));
columnChartOption2.yAxis.name="金额/停车场";
columnChartOption2.grid=grid2;
columnChartOption2.tooltip.formatter = "停车场：{b}<br>费用：{c}元";
columnChartOption2.title.text = "停车场收费排名";



parkChargeRank();
//停车场收费排行
function parkChargeRank() {
    $.ajax({
        url: '/sjfx/parkChargeRank.do',
        data: {},
        success: function (res){
            columnChartOption2.xAxis.data =res.data.parks;
            columnChartOption2.series[0].data = res.data.amount;
            columnChartOption2.series[0].itemStyle.normal.color = function (params) {
                return linearGradientColor1[params.dataIndex];
            }
            columnChartOption2.series[0].label = {normal: {show: true, position: 'top', formatter: '{c}元'}};
            columnChart2.setOption(columnChartOption2);
        }
    })
}

// 昨日停车时长占比分析
var grid3 = {
   containLabel: true,
   left: 0,
   right: 30,
   top:300,
   bottom: 0
};
var pieChart_DIV = document.getElementById("hxDiv");
var pieCharts = echarts.init(pieChart_DIV);
pieChartOption.grid=grid3;
pieChartOption.title.show = false;
pieChartOption.title.text = "昨日停车时长占比分析";
pieChartOption.tooltip.formatter = "停车时长：{b}<br>占比：{c}%";
pieChartOption.series.radius = scrWidth > 1400 ? "65%" : "50%";
pieChartOption.series.center = scrWidth > 1400 ? ['35%', '53%'] : ['45%', '53%'];
pieChartOption.legend.show = scrWidth > 1400 ? "true" : "false";
pieChartOption.legend.data=scrWidth > 1400 ? ['0-2小时', '2-4小时', '4-6小时', '6-8小时', '8小时以上'] : "false";
pieChartOption.legend.top = scrWidth > 1400 ? "15%" : "0px";
pieChartOption.legend.right = scrWidth > 1400 ? "8%" : "0px";
pieChartOption.legend.textStyle.fontSize=12;

//pieChartOption.series.label = {normal: {show: true, position: 'outside', formatter: '{b}'}};

restimeProportion();
function restimeProportion(){
    $.ajax({
        url: '/sjfx/restimeProportion.do',
        data: {type: '1'},
        success: function (res){
            var valueNumber1 = res.data;
            var nameText1 = ['0-2小时', '2-4小时', '4-6小时', '6-8小时', '8小时以上'];
            pieChartOption.series.data = [];
            for (var i = 0; i < valueNumber1.length; i++) {
                pieChartOption.series.data.push({value: valueNumber1[i], name: nameText1[i], itemStyle: itemStyle2[i]});
            }
            pieCharts.setOption(pieChartOption);
        }
    })
}


var hpieChart_DIV = document.getElementById("mydDiv");
var hpieCharts = echarts.init(hpieChart_DIV);
var huan_pieChartOption=JSON.parse(JSON.stringify(pieChartOption));
huan_pieChartOption.title.show = false;
huan_pieChartOption.title.text = "支付方式占比";
huan_pieChartOption.tooltip.formatter = "<b style='font-size: 12px;color:orange'>{b}：{d}%</b>";
huan_pieChartOption.series.radius = scrWidth > 1400 ? ['45%', '78%'] : ['35%', '58%'];
huan_pieChartOption.series.center = scrWidth > 1400 ? ['50%', '54%'] : ['45%', '56%'];
huan_pieChartOption.legend.show=true;
huan_pieChartOption.legend.textStyle.fontSize=14;
//huan_pieChartOption.legend.data=scrWidth > 1400 ? ['支付宝', '微信', '现金'] : "false";
huan_pieChartOption.legend.top = scrWidth > 1400 ? "15%" : "0px";
huan_pieChartOption.legend.right = scrWidth > 1400 ? "8%" : "0px";
//var valueNumber3 = [37.95, 50.53, 5.28];
//var nameText3 = ['支付宝', '微信', '现金'];
huan_pieChartOption.series.data = [];
huan_pieChartOption.series.label.normal.position = 'outside';
//huan_pieChartOption.series.label.normal.textStyle.fontSize=12;
// for (var i = 0; i < valueNumber3.length; i++) {
//    huan_pieChartOption.series.data.push({value: valueNumber3[i], name: nameText3[i], itemStyle: itemStyle3[i]});
// }
paymentTypeStatistics();
//支付方式占比
function paymentTypeStatistics() {
    $.ajax({
        url: '/sjfx/parkPaymentTypeStatistics.do',
        data: {},
        success: function (res){
            huan_pieChartOption.legend.data=scrWidth > 1400 ? res.data.types : "false";
            for (var i = 0; i < res.data.types.length; i++) {
                huan_pieChartOption.series.data.push({value: res.data.amount[i], name: res.data.types[i], itemStyle: itemStyle3[i]});
            }
            hpieCharts.setOption(huan_pieChartOption);
        }
    })
}


// 昨日车辆来源分布________________________
var rankingChart1_DIV = document.getElementById("carDiv");
var rankingChart1 = echarts.init(rankingChart1_DIV);

var grid6 = {
    left: '0px',
    top: '0px',
    right: '10px',
    bottom: '0px',
    containLabel: true
};
var rankingChartOption1=JSON.parse(JSON.stringify(rankingChartOption));
rankingChartOption1.grid = grid6;
rankingChartOption1.xAxis.data = [];
rankingChartOption1.yAxis.data = [];
rankingChartOption1.series[0].data = [];
rankingChartOption1.title.show = false;
rankingChartOption1.xAxis.axisLabel.textStyle.fontSize = fontSize;
rankingChartOption1.yAxis.axisLabel.textStyle.fontSize = fontSize;
rankingChartOption1.yAxis.nameTextStyle.fontSize = fontSize;
rankingChartOption1.tooltip.axisPointer.type = 'shadow';
rankingChartOption1.yAxis.name = "类型/数量";
rankingChartOption1.tooltip.textStyle.align = "left";
rankingChartOption1.tooltip.formatter = "停车场：{b}<br>车辆数量：{c}辆";
rankingChartOption1.series[0].label = {normal: {show: true, position: 'right', formatter: '{c}辆'}};
rankingChartOption1.series[0].itemStyle.normal.color.colorStops[0].color=color7;
rankingChartOption1.series[0].itemStyle.normal.color.colorStops[1].color=color8;
//rankingChartOption1.series[0].itemStyle.normal.color.colorStops.push({offset: 0, color: color7}, {offset: 1, color: color8});


parkParkingCount();
//支付方式占比
function parkParkingCount() {
    $.ajax({
        url: '/sjfx/parkParkingCount.do',
        data: {},
        success: function (res){
            var valueNumber6 = res.data.count;
            var nameText6 = res.data.parks;
            for (var i = 0; i < valueNumber6.length; i++) {
                rankingChartOption1.series[0].data.push(valueNumber6[i]);
                rankingChartOption1.yAxis.data.push(nameText6[i]);
            }
            rankingChart1.setOption(rankingChartOption1);
        }
    })
}


// 获取最近一段日期（日期格式：2019-02-27）
function getDates(day, isIncludeToday) {
    day || (day = 7);
    
    var thisWeek = [],
        now      = new Date(),
        nowDay   = now.getDate(), //当前日,
        nowMonth = now.getMonth(), //当前月
        nowYear  = now.getFullYear(), //当前年
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
            document.querySelector('.leftbox').style.height = (screenH-120) + 'px';
	        document.querySelector('.midbox').style.height = (screenH-145) + 'px';
        }
/*chart自适应________________________*/
function resizeDiv() {

    columnChart.resize();
	pieCharts.resize();
	columnChart2.resize();
	hpieCharts.resize();
	rankingChart1.resize();
}

window.onresize = function () {
    resizeDiv();
}
window.onload = function () {
	window.parent.hideLoading();
 	window.parent.hideoverlay();
    resizeDiv();

	pieCharts.setOption(pieChartOption);
	rankingChart1.setOption(rankingChartOption1);
}
