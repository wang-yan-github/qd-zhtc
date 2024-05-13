<template>
	<div>
		<el-row>
			<el-col :span="24">
				<div class="container">
					<div class="handle-box">
						<div class="left-panel">
							<el-form inline label-width="80" size="small">
								<el-form-item label="市区">
									<el-select clearable v-model="query.variance" filterable size="small" placeholder="所有区域"
										class="w100" @change="getStreet">
										<el-option v-for="item in formqjl.areas" :key="item.id" :label="item.area_name"
											:value="item.id"></el-option>
									</el-select>
								</el-form-item>
								<el-form-item label="街道">
									<el-select clearable v-model="query.variance2" filterable size="small" placeholder="所有街道"
										class="w100" @change="getPark">
										<el-option v-for="item in formqjl.streets" :key="item.value" :label="item.street_name"
											:value="item.id"></el-option>
									</el-select>
						  </el-form-item>
								<el-form-item label="停车场">
									<el-select clearable v-model="query.variance3" filterable size="small" placeholder="所有停车场"
										class="w100">
										<el-option v-for="item in formqjl.parks" :key="item.id" :label="item.park_name"
											:value="item.id"></el-option>
									</el-select>
								</el-form-item>
								<el-form-item label="车牌号">
									<el-input
                    @keyup.enter="handleSearch()"
									  size="small"
									  v-model="query.carNo"
									  placeholder="车牌号"
									  class="w100"
									></el-input>
								</el-form-item>

								<el-form-item label="">
									<el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
								</el-form-item>
								<el-form-item label="">
						        	<el-button size="small" type="warning" icon="el-icon-refresh" @click="qctj" >清空</el-button>
						    	</el-form-item>


							</el-form>
						</div>
						<div class="clear"></div>
					</div>

					<ul class="card-bgimg">
						<li>
							<h4>{{census.berthCount}}</h4>
							<h6>泊位总数</h6>
						</li>
						<li>
							<h4>{{census.orderCount}}</h4>
							<h6>在停车辆数</h6>
						</li>
						<!--<li>
							<h4>66</h4>
							<h6>在线泊位数</h6>
						</li>-->
						<li>
							<h4>{{census.ky}}</h4>
							<h6>空余泊位数</h6>
						</li>
						<!--<li>
							<h4>4236</h4>
							<h6>断线泊位数</h6>
						</li>-->
						<li>
							<h4>{{census.utilizeRatio}}</h4>
							<h6>泊位饱和度(%)</h6>
						</li>
					</ul>
					<el-table :show-header="true" :data="todoList" border class="table" ref="multipleTable"
						header-cell-class-name="table-header">
						<el-table-column type="index" label="序号" width="55" align="center"> </el-table-column>

						<el-table-column  label="订单号" align="center" width="300">
							<template #default="scope">
								<div class="todo-item" :class="{ 'todo-item-del': scope.row.status }">
									{{ scope.row.order_no }}
								</div>
							</template>
						</el-table-column>
						<el-table-column  label="是否会员" width="100" align="center">
							<template #default="scope">
								<div class="todo-item" :class="{ 'todo-item-del': scope.row.status }">
									{{ scope.row.memberFlag==1?'是':'否' }}
								</div>
							</template>
						</el-table-column>
						<el-table-column   label="车牌号" align="center" width="100">
							<template #default="scope">
								<div>{{ scope.row.car_no }}</div>
							</template>
						</el-table-column>
						<el-table-column align="center"  label="停车场">
							<template #default="scope">
								<div>{{ scope.row.packName }}</div>
							</template>
						</el-table-column>
						<el-table-column  label="入场时间" width="200" align="center">
							<template #default="scope">
								<div>{{ scope.row.driveinTime }}</div>
							</template>
						</el-table-column>
						<el-table-column   label="是否欠费"  width="100" align="center">
							<template #default="scope">
								<div class="todo-item" :class="{ 'todo-item-del': scope.row.status }">
									{{ scope.row.arrearageFlag==1?'是':'否' }}
								</div>
							</template>
						</el-table-column>
						<el-table-column   label="欠费金额(元)" width="160" align="center">
							<template #default="scope">
								<div class="todo-item" :class="{ 'todo-item-del': scope.row.status }">
									{{ scope.row.arrearage }}
								</div>
							</template>
						</el-table-column>

					</el-table>

					<div class="pagination">
						<el-pagination
						background
						layout="total, prev, pager, next"
						:current-page="query.pageNum"
						:page-size="query.pageSize"
						:total="pageTotal"
						@current-change="handlePageChange"
						></el-pagination>
					</div>

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
	import { queryAreaData , queryStreetData , queryParkData , psmcParkingStatistics , parkDeviceInfo } from "../../../api/index";

	export default {
		name: "realparkingA",
		components: {
			Schart
		},

		setup() {
			const name = localStorage.getItem("ms_username");
			const role = name === "admin" ? "超级管理员" : "普通用户";

			let formqjl = reactive({

				areas: [],
				streets: [],
				parks: []
			});
			const query = reactive({
				carNo:'',
				variance:'',
				variance2:'',
				variance3:'',
				pageNum: 1,
				pageSize: 10,
			});
			const census = reactive({
				berthCount:'',
				orderCount:'',
				ky:'',
				utilizeRatio:'',
			});

			//表格数据
			const todoList = ref([]);
			const pageTotal = ref(0);

			//获取区域下拉框数据
			const queryArea = reactive({});
			const getArea = () => {
				queryAreaData(queryArea).then((res) => {
					formqjl.areas = res.data;
				})
			}
			getArea();
			//获取街道下拉框数据
			let queryStreet = reactive({
				areaId: query.variance
			});
			const getStreet = () => {
				queryStreet.areaId = query.variance;
				query.variance2 = "";
				query.variance3 = "";
				queryStreetData(queryStreet).then((res) => {
					formqjl.streets = res.data;
				})
			}
			//获取路内下拉框数据
			const queryPark = reactive({
				streetId: query.variance2
			});
			const getPark = () => {
				queryPark.streetId = query.variance2;
				query.variance3 = "";
				queryParkData(queryPark).then((res) => {
					formqjl.parks = res.data;
				})
			}
			//实施停车统计
			const getParkingStatistics = () => {
				psmcParkingStatistics(query).then((res) => {
					census.berthCount = res.data.berthCount;
					census.orderCount = res.data.orderCount;
					census.ky = res.data.ky;
					census.utilizeRatio = res.data.utilizeRatio;

				})
			}
			getParkingStatistics();

			//实施停车统计
			const getParkDeviceInfo = () => {
				parkDeviceInfo(query).then((res) => {
					console.log(res.data);
					todoList.value = res.data.list;
					pageTotal.value = res.data.total;
				})
			}
			getParkDeviceInfo();


			// 查询操作
			const handleSearch = () => {
				query.pageNum = 1;
				getParkingStatistics();
				getParkDeviceInfo();
			};
			// 分页导航
			const handlePageChange = (val) => {
				query.pageNum = val;
				getParkDeviceInfo();
			};
			// 清楚条件
			const qctj = () => {
				query.carNo = '',
				query.variance = '',
				query.variance2 = '',
				query.variance3 = '',
				query.pageNum = 1;
				getParkingStatistics();
				getParkDeviceInfo();
			};



			return {
				name,
				query,
				todoList,
				pageTotal,
				census,

				handlePageChange,
				handleSearch,
				qctj,

				formqjl,
				queryArea,
				queryStreet,
				queryPark,
				getArea,
				getStreet,
				getPark,

				role,
				url: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
				srcList: [
					"https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg",
				]
			};
		},
		methods: {
			init() {

			},
		},
		mounted() {
			this.init();
		},
	};
</script>

<style scoped>
	.handle-box {
		border-bottom: 2px solid #e4e5e6;
	}

	.card-row .el-col {
		margin-bottom: 1.25rem;
	}

	.p-card {
		padding: 8px;
		border-radius: 4px;
		border: 1px solid #f6f8fc;
		background-color: #FFF;
		overflow: hidden;
		color: #303133;
		-webkit-transition: .3s;
		transition: .3s;
		box-shadow: 0 0px 4px 0 rgba(0, 0, 0, .1);
		cursor: pointer;
	}

	.p-card:hover {
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
	}

	.p-card .p-card-con {
		padding-top: 3.5rem;
		position: relative;
		border: 1px solid #eff0f2;
	}

	.p-card-mark {
		padding: 1px;
		position: absolute;
		top: -2px;
		right: 0.75rem;
		font-size: 16px;
	}

	.normal {
		color: #67C23A;
	}

	.abnormal {
		color: #F56C6C;
	}

	.artificial {
		color: #a2a8f7;
	}

	.p-card-meb {
		position: absolute;
		left: 0;
		top: 0.5rem;
		min-width: 3rem;
		padding: 6px 0.875rem;
		font-size: 14px;
		line-height: 1rem;
		color: #fff;
		text-align: center;
		border-top-right-radius: 1rem;
		border-bottom-right-radius: 1rem;
	}

	.member {
		background-color: #f2c423;
	}

	.nonmember {
		background-color: #d2b860;
	}

	.Arrears {
		background-color: #91886a;
	}

	.car-num {
		position: relative;
		margin: 0 auto;
		width: 8.375rem;
		padding: 0.0625rem;
		text-align: center;
		border: 2px solid #c9d3eb;
		border-radius: 4px;
	}

	.car-num p {
		height: 2.5rem;
		line-height: 2.3rem;
		border-radius: 0.125rem;
		background-color: #8091bf;
		color: #fff;
		font-weight: bold;
		letter-spacing: 0.0625rem;
	}

	.car-num span {
		display: inline-block;
		position: absolute;
		background-color: #fff;
	}

	.car-num-a,
	.car-num-b {
		width: 0.25rem;
		height: 0.25rem;
		border-radius: 50%;
		top: -1px;
	}

	.car-num-a {
		left: 2.25rem;
	}

	.car-num-b {
		right: 2.25rem;
	}

	.car-num-c,
	.car-num-d {
		width: 0.75rem;
		height: 0.1875rem;
		border-radius: 0.125rem;
		bottom: 0.25rem;
	}

	.car-num-c {
		left: 2rem;
	}

	.car-num-d {
		right: 2rem;
	}

	.park-num {
		margin: .8rem 0;
		width: 100%;
		text-align: center;
		font-size: 1.5rem;
		color: #656d82;
	}

	.tips-icon {
		line-height: 2.4;
		overflow: hidden;
	}

	.tips-icon li {
		float: right;
		margin-right: 20px;
		font-size: 14px;
	}



	.card-bgimg {
		width: 100%;
		padding: 10px 0;
		display: flex;
		margin-bottom: 1.25rem;
		background: linear-gradient(125deg, #f4f6fd 35%, #f3f6ff 65%);
		overflow: hidden;
		border-radius: 4px;
	}

	.card-bgimg li {
		flex: 1;
		text-align: center;
		color: #656d82;
		border-right: 1px solid #fff;
	}

	.card-bgimg li:last-child {
		border-right: none;
	}

	.card-bgimg li h4 {
		font-size: 24px;
	}

	.card-bgimg li h6 {
		font-size: 14px;
		font-weight: normal;
	}

	.mt-20 {
		margin-bottom: 20px;
	}
</style>
