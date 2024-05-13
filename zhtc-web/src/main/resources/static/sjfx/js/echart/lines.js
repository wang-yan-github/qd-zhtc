///////////////////////////////// 折线图1开始： 每日出口统计 ///////////////////////////////////
var lineChart_DIV = document.getElementById("lineChart");
var lineChart = echarts.init(lineChart_DIV);

lineChartOption.title.text = "";
lineChartOption.legend.show = true;
lineChartOption.legend.data[0].name = lineChartOption.series[0].name = "今日";
lineChartOption.legend.data[1].name = lineChartOption.series[1].name = "昨日";
lineChartOption.legend.data[0].icon = lineChartOption.legend.data[1].icon = "roundRect";
lineChartOption.grid={left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true};

lineChartOption.yAxis.name = "人数/时间";
lineChartOption.tooltip.formatter = '<span style="display:inline-block;text-align: left;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#ff4dff"></span>' + lineChartOption.series[0].name + ': {c0}人<br><span style="display:inline-block;text-align: left;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:#87ffd1"></span>' + lineChartOption.series[1].name + ': {c1}人';

var lineChartValues1 = [856,923,951,934,874,1023];
var lineChartValues2 = [956,823,751,634,1074,1250];
var lineChartTexts = ['8:00', '10:00', '12:00', '14:00', '16:00', '18:00'];
lineChartOption.series[0].data = lineChartValues1;
lineChartOption.series[1].data = lineChartValues2;
lineChartOption.xAxis.data = lineChartTexts;
lineChart.setOption(lineChartOption);

///////////////////////////////// 折线图1结束 ///////////////////////////////////