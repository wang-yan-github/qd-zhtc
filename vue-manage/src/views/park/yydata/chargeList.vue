<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<div class="left-panel">
					<el-input
							size="small"
							v-model="query.carNo"
							placeholder="车牌号"
							class="handle-input mr10"
							@keyup.enter="handleSearch()"
					></el-input>
					<el-input
							size="small"
							v-model="query.orderNo"
							placeholder="订单号"
							class="handle-input mr10"
							@keyup.enter="handleSearch()"
					></el-input>
					<el-button
							size="small"
							type="primary"
							icon="el-icon-search"
							@click="handleSearch()"
					>查询</el-button
					>
				</div>
				<div class="clear"></div>
			</div>
			<el-table :data="todoList" border class="table" ref="multipleTable"  :max-height="tableH"
			header-cell-class-name="table-header">
        <el-table-column label="序号" width="55" align="center">
          <template #default="scope">
            {{scope.$index + 1}}
          </template>
        </el-table-column>
				<el-table-column prop="name" label="订单号" align="center">
					<template #default="scope">
						<div class="todo-item">
							{{ scope.row.orderNo }}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="addtimes" label="车牌号" align="center" width="180">
					<template #default="scope">
						<div class="todo-item">
							{{ scope.row.carNo }}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="logintimes" label="停车时长" align="center">
					<template #default="scope">
						<div class="todo-item">
							{{ scope.row.resitime }}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="logintimes" label="停车费用(元)" align="center">
					<template #default="scope">
						<div class="todo-item">
							{{ scope.row.sum_amount }}
						</div>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="280" align="center">
					<template #default="scope">
						<el-button size="mini" type="text" icon="el-icon-wallet"
							@click="handleView(scope.row.orderNo)">现金收款
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination
						background
						layout="total, prev, pager, next"
						:current-page="query.pageIndex"
						:page-size="query.pageSize"
						:total="pageTotal"
						@current-change="handlePageChange"
				></el-pagination>
			</div>
		</div>


		<!-- 查看弹出框 -->
		<el-dialog title="查看详情" v-model="viewVisible" width="50%" :close-on-click-modal="false">
			<table class="desctable mgb20 w">
				<tr>
					<td class="tit is-bordered-label" width="100"><i class="el-icon-tickets"></i> 订单号</td>
					<td colspan="3" >{{viewVisible.orderNo}}</td>
				</tr>
				<tr>
					<td class="tit is-bordered-label" width="100"><i class="el-icon-c-scale-to-original"></i> 车牌号</td>
					<td>{{viewVisible.carNo}}</td>
					<td class="tit" width="100"><i class="el-icon-postcard"></i> 车辆类型</td>
					<td v-if="viewVisible.carType == 1">蓝牌</td>
					<td v-else-if="viewVisible.carType == 2">绿牌</td>
					<td v-else-if="viewVisible.carType == 3">黄牌</td>
				</tr>
				<tr>
					<td class="tit" width="100"><i class="el-icon-timer"></i> 停车时长</td>
					<td>{{viewVisible.resitime}}</td>
					<td class="tit" width="100"><i class="el-icon-money"></i> 停车费用(元)</td>
					<td colspan="3">{{viewVisible.sum_amount}}</td>
				</tr>
				<tr>
					<td class="tit" width="100"><i class="el-icon-time"></i> 驶入时间</td>
					<td>{{viewVisible.driveinTime}}</td>
					<td class="tit" width="100"><i class="el-icon-time"></i> 驶离时间</td>
					<td>{{viewVisible.driveoutTime}}</td>
				</tr>

			</table>
			<ul class="money-num-box">
				<li>
					<span>应收(元)：</span>
					<b >{{viewVisible.sum_amount}}</b>
				</li>
				<li>
					<span>已付(元)：</span>
					<b class="give-coral">{{viewVisible.paid_amount}}</b>
				</li>
                <li>
                    <span>优惠(元)：</span>
                    <b class="give-red">{{viewVisible.discount_amount}}</b>
                </li>
				<li>
					<span>待付(元)：</span>
					<b class="give-change">{{viewVisible.unpaid_amount}}</b>
				</li>
				<!--<li>-->
					<!--<span>实收(元)：</span>-->
					<!--<el-input  v-model="realMoney" placeholder="金额" class="handle-input" @input="changeMoney()" ></el-input>-->
				<!--</li>-->
				<!--<li>-->
					<!--<span>找零(元)：</span>-->
					<!--<b class="give-change" >{{zl}}</b>-->
				<!--</li>-->
			</ul>
			<div class="clear"></div>
			<template #footer>

				<span class="dialog-footer">
					<el-button  class="mr10" @click="viewVisible = false">关 闭</el-button>
				</span>
				<span class="dialog-footer" >
					<el-button type="primary" @click="submit(viewVisible.id)">收 费</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script>
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {appearanceFee, appearanceFeeInfo, cashShoufei} from "../../../api/parkingOrder";

export default {
		name: "charge",
		 data() {
			return {
				tableH:0
			};
		},
		setup() {
			const query = reactive({
				carNo: "",
				orderNo: "",
				pageIndex: 1,
				pageSize: 15,
			});
			//表格数据
			const todoList = ref([]);
			const pageTotal = ref(0);
			const realMoney = ref(0.00);
			const zl = ref(0.00);
			const ys = ref(0.00);
			// 现金收款
			const viewVisible = ref(false);


			const handleView = (orderNo) => {
				appearanceFeeInfo(reactive({orderNo:orderNo})).then((res) => {
					viewVisible.value = res.data;
					// todoList.value = res.data.list;
					// pageTotal.value = res.data.total;
				});

			};

			const submit = (id) => {
				// 二次确认
				ElMessageBox.confirm("确定要收费吗？", "提示", {
					type: "warning",
				}).then(() => {
					viewVisible.value = false ;
					cashShoufei(reactive({id:id})).then((res) => {
						if(res.code == 0 ){
							getData();
							ElMessage.success("操作成功");
						}else {
							ElMessage.warning(res.msg);
						}
					});
				})
			};



			const getData = () => {
				appearanceFee(query).then((res) => {
					console.log("====="+res);
					todoList.value = res.data.list;
					pageTotal.value = res.data.total;
				});
			};
			getData();
			// 查询操作
			const handleSearch = () => {
				query.pageIndex = 1;
				getData();
			};
			// 分页导航
			const handlePageChange = (val) => {
				query.pageIndex = val;
				getData();
			};
			const changeMoney = () => {
				if(parseInt(viewVisible.value.unpaid_amount) > parseInt(realMoney.value)){
					zl.value = 0.00 ;
				}else {
					zl.value = realMoney.value -viewVisible.value.unpaid_amount ;
				}

			};
			return {
				query,
				viewVisible,
				handleView,
				todoList,
				multipleSelection: [],
				value: true,
				pageTotal,
				getData,
				handlePageChange,
				handleSearch,
				changeMoney,
				realMoney,
				zl,
				ys,
				submit

			};
		},
		created() {
			let h = document.documentElement.clientHeight || document.body.clientHeight;
			this.tableH = h - 308 + 'px';
		},
		methods: {


		},
	};
</script>
<style scoped>
	.handle-input {
		width: 180px;
		vertical-align: middle;
	}
	.money-num-box{
		float: right;
		width: 200px;
	}
	.money-num-box li{
		line-height: 2;
	}
	.money-num-box li span{
		vertical-align: middle;
	}
	.money-num-box li b{
		margin-left: 12px;
		font-size: 20px;
		color: #0555ee;
		vertical-align: middle;
	}
	.money-num-box li b.give-change{
		color: #67C23A;
	}

	.money-num-box li b.give-red{
		color: red;
	}
	.money-num-box li b.give-coral{
		color: coral;
	}
</style>
