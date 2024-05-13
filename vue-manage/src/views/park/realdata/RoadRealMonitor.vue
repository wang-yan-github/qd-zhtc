<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="5">
				<el-card shadow="hover" style="min-height: 100%;">
					<template #header>
						<div class="clearfix">
							<span>停车场实时监控</span>
							<p>(该页面以支付时间统计收款金额)</p>
						</div>
					</template>

					<div class="text item">
						<el-tree :data="treeTable" :props="defaultProps" @node-click="handleNodeClick" :expand-on-click-node="false"></el-tree>

					</div>
				</el-card>


			</el-col>
			<el-col :span="19">
				<div class="container">

					<el-row class='mt-20'>
						<el-col :span="24">
			 			<el-card shadow="hover">
								<!-- <schart ref="bar" class="schart" canvasId="bar" :options="options"></schart> -->
								<div id="cardata" class="schart"></div>
							</el-card>
						</el-col>
					</el-row>
					<el-row :gutter="20">
						<el-col :span="12" class="card-bgimg-box">
							<el-card shadow="hover">
								<el-row :gutter="20" class="card-bgimg">
									<el-col :span="12">
										<div class="li-data">
											<h4>{{census.receivable}}</h4>
											<h6>应收款(元)</h6>
										</div>
									</el-col>
									<el-col :span="12">
										<div class="li-data">
											<h4>{{census.sumAmount}}</h4>
											<h6>实收款(元)</h6>
										</div>
									</el-col>
								</el-row>
								<el-row :gutter="20" class="card-bgimg">
									<el-col :span="8">
										<div class="li-data li-data-b">
											<h4>{{census.memberOrderNum}}</h4>
											<h6>会员停车数(个)</h6>
										</div>
									</el-col>
									<el-col :span="8">
										<div class="li-data li-data-b">
											<h4>{{census.outsiderOrderNum}}</h4>
											<h6>非会员停车数(个)</h6>
										</div>
									</el-col>
									<el-col :span="8">
										<div class="li-data li-data-b">
											<h4>{{census.arrearsBerthNum}}</h4>
											<h6>欠费停车数(个)</h6>
										</div>
									</el-col>
								</el-row>
								<el-row :gutter="20" class="card-bgimg">
									<el-col :span="6">
										<div class="li-data li-data-c">
											<h4>{{census.utilizeRatio}}</h4>
											<h6>泊位使用率(%)</h6>
										</div>
									</el-col>
									<el-col :span="6">
										<div class="li-data li-data-c">
											<h4>{{census.turnover}}</h4>
											<h6>泊位周转率(%)</h6>
										</div>
									</el-col>
									<el-col :span="6">
										<div class="li-data li-data-c">
											<h4>{{census.ky}}</h4>
											<h6>空余泊位(个)</h6>
										</div>
									</el-col>
									<el-col :span="6">
										<div class="li-data li-data-c">
											<h4>{{census.berthCount}}</h4>
											<h6>总泊位(个)</h6>
										</div>
									</el-col>
								</el-row>
							</el-card>
						</el-col>
						<!--<el-col :span="12">
							<el-card shadow="hover">

								<div id="main" class="schart"></div>
							</el-card>
						</el-col>-->
						<el-col :span="12">
							<el-card shadow="hover">
								<!-- <schart
	  	 	        ref="line"
	  	 	        class="schart"
	  	 	        canvasId="line"
	  	 	        :options="options2"
	  	 	      ></schart> -->
								<div id="mainline" class="schart"></div>
							</el-card>
						</el-col>
					</el-row>
				</div>
			</el-col>
		</el-row>

	</div>
</template>

<script>
	import Schart from "vue-schart";
	// 全局引入
	import * as echarts from "echarts";
	import { ref,reactive } from "vue";
	import { ElMessage, ElMessageBox } from "element-plus";
	import { parkingTreeStructure , orderReceivable , orderSumAmount , utilizeInfo ,
	hyFhyParkingOrderCount, parkLeaveEntryHourCount , parkCollectionsStatistics  } from "../../../api/index";

	export default {
		name: "roadrealmonitorA",
		components: {
			Schart
		},

		setup() {
			const name = localStorage.getItem("ms_username");
			const role = name === "admin" ? "超级管理员" : "普通用户";

			const query = reactive({
				variance:'',
				variance2:'',
				variance3:'',

			});

			const treeTable = ref([]);
			const census = reactive({
				receivable:'',
				sumAmount:'',
				berthCount:'',
				ky:'',
				utilizeRatio:'',
				turnover:'',
				memberOrderNum:'',
				outsiderOrderNum:'',
				arrearsBerthNum:'',
			});

			// 获取表格数据
			const getParkingTreeStructure = () => {
				parkingTreeStructure(reactive({ })).then((res) => {
					var data = res.data;
					treeTable.value = data;
				});
			};
			getParkingTreeStructure();

			// 查询订单应收
			const getOrderReceivable = () => {
				orderReceivable( query ).then((res) => {
					var data = res.data;
					census.receivable = data;
				});
			};
			// 查询订单总收入
			const getOrderSumAmount = () => {
				orderSumAmount( query ).then((res) => {
					var data = res.data;
					census.sumAmount = data;
				});
			};
			//查询车位总数、剩余数、使用率、周转率
			const berthUtilizeInfo = () => {
				utilizeInfo( query ).then((res) => {
					var data = res.data;
					census.arrearsBerthNum = data.arrearsBerthNum;
					census.berthCount = data.berthCount;
					census.ky = data.ky;
					census.utilizeRatio = data.utilizeRatio;
					census.turnover = data.turnover;

				});
			};
			//查询会员停车书非会员停车数
			const getHyFhyParkingOrderCount = () => {
				hyFhyParkingOrderCount( query ).then((res) => {
					var data = res.data;
					census.memberOrderNum = data.memberOrderNum;
					census.outsiderOrderNum = data.outsiderOrderNum;
				});
			};
			//查询会员停车书非会员停车数
			const getLeaveEntryHourCount = () => {
				parkLeaveEntryHourCount( query ).then((res) => {
					var data = res.data;
					console.log(data);
					optionsPK.xAxis[0].data = res.data.hours;
					optionsPK.series[0].data = res.data.entrys;
					optionsPK.series[1].data = res.data.leaves;
					var chartDomPK = document.getElementById("cardata");
					var myChartPK = echarts.init(chartDomPK);
					myChartPK.setOption(optionsPK);
				});
			};
			//查询会员停车书非会员停车数
			const getCollectionsStatistics = () => {
				parkCollectionsStatistics( query ).then((res) => {
					var data = res.data;
					console.log(data);
					optionspk4.series[0].data = res.data
					var chartDomsPK = document.getElementById("mainline");
					var myChartPK = echarts.init(chartDomsPK);
					myChartPK.setOption(optionspk4);

				});
			};
			getOrderReceivable();
			getOrderSumAmount();
			berthUtilizeInfo();
			getHyFhyParkingOrderCount();
			getLeaveEntryHourCount();
			getCollectionsStatistics();

			// 树形菜单点击事件
			const handleNodeClick = (data,Node) =>{
				console.log(data);
				if(data.grade === 0){
					query.variance = '';
					query.variance2 = '';
					query.variance3 = '';
				}else if(data.grade === 1){
					query.variance = data.id;
					query.variance2 = '';
					query.variance3 = '';
				}else if(data.grade === 2){
					query.variance = data.areaId;
					query.variance2 = data.id;
					query.variance3 = '';
				}else{
					query.variance = data.areaId;
					query.variance2 = data.streetId;
					query.variance3 = data.id;
				}

				getOrderReceivable();
				getOrderSumAmount();
				berthUtilizeInfo();
				getHyFhyParkingOrderCount();
				getLeaveEntryHourCount();
				getCollectionsStatistics();

			};

			const optionsPK = {
				color: ['#80FFA5', '#00DDFF'],
				title: {
					text: '驶入驶出车辆',
					textStyle: {
                        fontSize: 16,
                        fontWeight: "normal",
                        color: "#303133",
                    },
				},
				tooltip: {
					trigger: 'axis',
					axisPointer: {
						type: 'cross',
						label: {
							backgroundColor: '#6a7985'
						}
					}
				},
				legend: {
					data: ['驶入车辆', '驶出车辆']
				},
				grid: {
					left: '3%',
					right: '4%',
					bottom: '3%',
					containLabel: true
				},
				xAxis: [{
					type: 'category',
					boundaryGap: false,
					data: []
				}],
				yAxis: [{
					type: 'value'
				}],
				series: [{
						name: '驶入车辆',
						type: 'line',
						stack: 'Total',
						smooth: true,
						lineStyle: {
							width: 0
						},
						showSymbol: false,
						areaStyle: {
							opacity: 0.8,
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgb(128, 255, 165)'
								},
								{
									offset: 1,
									color: 'rgb(1, 191, 236)'
								}
							])
						},
						emphasis: {
							focus: 'series'
						},
						data: []
					},
					{
						name: '驶出车辆',
						type: 'line',
						stack: 'Total',
						smooth: true,
						lineStyle: {
							width: 0
						},
						showSymbol: false,
						areaStyle: {
							opacity: 0.8,
							color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: 'rgb(0, 221, 255)'
								},
								{
									offset: 1,
									color: 'rgb(77, 119, 255)'
								}
							])
						},
						emphasis: {
							focus: 'series'
						},
						data: []
					}
				]
			};

			// const options3 = {
			// 	title: {
			// 		text: '泊位统计'
			// 	},
			// 	  tooltip: {
			// 		trigger: 'item',
			// 		formatter: '{a} <br/>{b} : {c} ({d}%)'
			// 	  },
			// 	  legend: {
			// 		orient: 'vertical',
			// 		right: 'left'
			// 	  },
			// 	  series: [
			// 		{
			// 		  name: '泊位统计',
			// 		  type: 'pie',
			// 		  radius: ['40%', '70%'],
			// 		  avoidLabelOverlap: false,
			// 		  itemStyle: {
			// 			borderRadius: 10,
			// 			borderColor: '#fff',
			// 			borderWidth: 2,
			// 		  },
			// 		  data: [
			// 			{ value: 1048, name: '高位视频' },
			// 			{ value: 735, name: '地磁' },
			// 			{ value: 580, name: '视频杆' }
			// 		  ]
			// 		}
			// 	  ]
			// 	};
			const optionspk4 = {
				  title: {
					text: '订单收款统计',
					textStyle: {
                        fontSize: 16,
                        fontWeight: "normal",
                        color: "#303133",
                    },
				  },
				   legend: {
					orient: 'vertical',
					right: 'left'
				  },
				  tooltip: {
					trigger: 'item',
					formatter: '{a} <br/>{b} : {c}元 ({d}%)'
				  },
				  series: [
					{
					  name: '订单收款统计',
					  type: 'pie',
					  radius: [30, 110],
					  center: ['50%', '50%'],
					  roseType: 'area',
					  itemStyle: {
						borderRadius: 8
					  },
					  data: []
					}
				  ]
				};



			return {
				name,

				treeTable,
				census,
				handleNodeClick,

				optionsPK,
				//options3,
				optionspk4,
				role,
			};
		},
		methods: {
			init() {
				// var chartDomCar = document.getElementById("cardata");
				// var myChartCar = echarts.init(chartDomCar);
				// myChartCar.setOption(this.options2);
				// var chartDom = document.getElementById("main");
				// var myChart = echarts.init(chartDom);
				// myChart.setOption(this.options3);
				// var chartDoms = document.getElementById("mainline");
				// var myCharts = echarts.init(chartDoms);
				// myCharts.setOption(this.options4);
			},
		},
		mounted() {
			//this.init();
		},
	};
</script>

<style scoped>
	.card-bgimg .li-data {
		flex: 1;
		text-align: center;
		color: #fff;
		padding: 13px 0;
		margin-bottom: 1.25rem;
		background-color: #e6edfe;
		border: 1px solid #a7bffd;
		border-radius: 6px;
		color: #5583fd;
	}
	.card-bgimg .li-data.li-data-b{
		background-color: #fff6dc;
		border: 1px solid #f8ce4b;
		color: #d8a404;
	}
	.card-bgimg .li-data.li-data-c{
		background-color: #e8f5e2;
		border: 1px solid #a7d492;
		color: #598445;
		margin-bottom: 3px;
	}

	.card-bgimg .li-data h4 {
		font-size: 24px;
		height: 34px;
		line-height: 34px;
	}

	.card-bgimg .li-data h6 {
		font-size: 14px;
		font-weight: normal;
		height: 24px;
		line-height: 24px;
	}

	.mt-20 {
		margin-bottom: 20px;
	}


	.circal {
		background: #409eff;
		color: #fff;
		border-radius: 10px;
		width: 20px;
		margin: auto;
	}

	.circal-top {
		background: #f56c6c;
	}

	.schart {
		width: 100%;
		height: 300px;
	}
</style>
