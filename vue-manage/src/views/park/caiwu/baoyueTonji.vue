<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<div class="left-panel">
					<el-button size="small" plain type="danger"
					>车牌数(个)：{{ census.licenceCount }}
					</el-button
					>
					<el-button size="small" plain type="danger"
					>充值笔数(笔)：{{ census.rechargeCount }}
					</el-button
					>
					<el-button size="small" plain type="danger"
					>充值金额(元)：{{ census.RechargeAmount }}
					</el-button
					>
				</div>
				<div class="right-panel">
					<el-button-group>
						<el-button icon="el-icon-time" size="small" @click="butTime">按时间</el-button>
						<el-button icon="el-icon-user" size="small" @click="butLicence">按车牌</el-button>
					</el-button-group>
					<span class="dispinline ml5"></span>
					<el-input
							size="small"
							v-model="query.carNo"
							placeholder="车牌号"
							class="handle-input w100"
					></el-input>
					<span class="dispinline ml5"></span>
					<el-select
							clearable
							v-model="query.variance"
							filterable
							size="small"
							placeholder="所有区域"
							class="w100"
							@change="getStreet"
					>
						<el-option v-for="item in formqjl.areas" :key="item.id" :label="item.area_name"
						           :value="item.id"></el-option>
					</el-select>

					<span class="dispinline ml5"></span>
					<el-select
							clearable
							v-model="query.variance2"
							filterable
							size="small"
							placeholder="所有街道"
							class="w100"
							@change="getPark"
					>
						<el-option v-for="item in formqjl.streets" :key="item.id" :label="item.street_name"
						           :value="item.id"></el-option>
					</el-select>

					<span class="dispinline ml5"></span>
					<el-select
							clearable
							v-model="query.variance3"
							filterable
							size="small"
							placeholder="所有停车场"
							class="w100"
					>
						<el-option v-for="item in formqjl.parks" :key="item.id" :label="item.park_name"
						           :value="item.id"></el-option>
					</el-select>

					<span class="dispinline ml5"></span>

					<span class="dispinline ml5 font14 color666">创建时间：</span>
					<el-date-picker
							v-model="form.time"
							type="daterange"
							start-placeholder="开始日期"
							end-placeholder="结束日期"
							size="small"
							class="datepicker"
							@change="getQueryDate"
					></el-date-picker>
					<span class="dispinline ml5"></span>
					<el-button
							size="small"
							type="primary"
							icon="el-icon-search"
							@click="handleSearch"
					>查询
					</el-button
					>
				</div>
				<div class="clear"></div>
			</div>

			<el-table
					v-show="timeVisible"
					:data="tableData"
					border
					class="table"
					:max-height="tableH"
					ref="multipleTable"
					header-cell-class-name="table-header"
			>
				<el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
				<el-table-column prop="gpby" label="日期" align="center"></el-table-column>
				<el-table-column prop="carnoNum" label="充值车牌数(个)" align="center"></el-table-column>
				<el-table-column prop="quantum" label="充值笔数(笔)" align="center"></el-table-column>
				<el-table-column prop="amount" label="充值金额(元)" align="center"></el-table-column>
			</el-table>
			<el-table
					v-show="licenceVisible"
					:data="tableData"
					border
					class="table"
					:max-height="tableH"
					ref="multipleTable"
					header-cell-class-name="table-header"
			>
				<el-table-column type="index" label="序号" width="55" align="center"></el-table-column>
				<el-table-column prop="gpby" label="车牌" align="center"></el-table-column>
				<el-table-column prop="carTypeName" label="车牌类型" align="center"></el-table-column>
				<el-table-column prop="quantum" label="充值笔数(笔)" align="center"></el-table-column>
				<el-table-column prop="amount" label="充值金额(元)" align="center"></el-table-column>
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

		<!-- 编辑弹出框 -->
		<el-dialog :title="dialogT" v-model="editVisible" width="30%">
			<el-form label-width="90px" size="small">
				<el-form-item label="充值密码">
					<el-input v-model="form.name"></el-input>
				</el-form-item>
				<el-form-item label="短信验证码">
					<el-input v-model="form.name">
						<template v-slot:append>
							<el-button type="primary" icon="el-icon-s-promotion"
							>获取验证码
							</el-button
							>
						</template>
					</el-input>
				</el-form-item>
			</el-form>
			<template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">去充值</el-button>
        </span>
			</template>
		</el-dialog>
	</div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
	timeLicenceCensusList, licenceCountData, rechargeCountData, rechargeAmountData,
	queryAreaData, queryStreetData, queryParkData
} from "../../../api/index";

export default {
	name: "baoyuetj",
	data() {
		return {
			tableH:0,
		};
	},
	setup() {

		const query = reactive({
			flag: true,
			carNo: '',
			str2: '1',
			variance: '',
			variance2: '',
			variance3: '',
			startTime: '',
			endTime: '',
			pageNum: 1,
			pageSize: 15,
		});
		const tableData = ref([]);
		const pageTotal = ref(0);

		const census = reactive({
			licenceCount: 0,
			rechargeCount: 0,
			RechargeAmount: 0,
		});

		let formqjl = reactive({
			time: "",
			areaId: "",
			streetId: "",
			roadId: "",
			areas: [],
			streets: [],
			parks: []
		});

		//获取区域下拉框数据
		const queryArea = reactive({});
		const getArea = () => {
			queryAreaData(queryArea).then((res) => {
				formqjl.areas = res.data;
			})
		};
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
		};
		//按时间和按车牌统计切换table
		const timeVisible = ref(true);
		const licenceVisible = ref(false);



		//获取路内下拉框数据
		const queryPark = reactive({
			streetId: query.variance2
		});
		const getPark = () => {
			query.variance3 = "";
			queryPark.streetId = query.variance2;
			queryParkData(queryPark).then((res) => {
				formqjl.parks = res.data;
			})
		}

		//获取路内下拉框数据
		// const queryRoad = reactive({
		// 	streetId: query.variance2
		// });
		//
		// const getRoad = () => {
		// 	queryRoad.streetId = query.variance2;
		// 	queryRoadData(queryRoad).then((res) => {
		// 		formqjl.roads = res.data;
		// 	})
		// };

		// 车牌总数
		const getLicenceCount = () => {
			licenceCountData(query).then((res) => {
				census.licenceCount = res.data;
			});
		};

		// 充值笔数
		const getRechargeCount = () => {
			rechargeCountData(query).then((res) => {
				census.rechargeCount = res.data;
			});
		};

		// 充值总额
		const getRechargeAmount = () => {
			rechargeAmountData(query).then((res) => {
				census.RechargeAmount = res.data;
			});
		};
		getLicenceCount();//车牌总数
		getRechargeCount();//充值笔数
		getRechargeAmount();//充值总额

		// 获取表格数据 query.flag：true按日期 false按车牌
		const getData = () => {
			timeLicenceCensusList(query).then((res) => {
				console.log("data------>:" + res.data.list);
				tableData.value = res.data.list;
				pageTotal.value = res.data.total;
			});
		};
		getData();

		//按时间统计
		const butTime = () => {
			query.flag = true;
			getData();
			getLicenceCount();//车牌总数
			getRechargeCount();//充值笔数
			getRechargeAmount();//充值总额
			licenceVisible.value = false;
			timeVisible.value = true;
		}

		//按车牌统计
		const butLicence = () => {
			query.flag = false;
			getData();
			getLicenceCount();//车牌总数
			getRechargeCount();//充值笔数
			getRechargeAmount();//充值总额
			timeVisible.value = false;
			licenceVisible.value = true;
		}

		// 查询操作
		const handleSearch = () => {
			query.pageNum = 1;
			getData();
			getLicenceCount();//车牌总数
			getRechargeCount();//充值笔数
			getRechargeAmount();//充值总额
		};
		// 分页导航
		const handlePageChange = (val) => {
			query.pageNum = val;
			getData();
		};

		// 删除操作
		const handleDelete = (index) => {
			// 二次确认删除
			ElMessageBox.confirm("确定要删除吗？", "提示", {
				type: "warning",
			})
					.then(() => {
						ElMessage.success("删除成功");
						tableData.value.splice(index, 1);
					})
					.catch(() => {
					});
		};
		const dialogT = "新增";

		// 表格编辑时弹窗和保存
		const editVisible = ref(false);
		let form = reactive({
			name: "",
			address: "",
			czroptions: [
				{
					value: "1",
					label: "平台管理员",
				},
				{
					value: "2",
					label: "管理员",
				},
				{
					value: "3",
					label: "车场",
				},
			],
		});
		let idx = -1;
		const handleEdit = (index, row, type) => {
			if (type) {
				//dialogT='新增'
			} else {
				//dialogT='编辑';
				idx = index;
				Object.keys(form).forEach((item) => {
					form[item] = row[item];
				});
			}

			editVisible.value = true;
		};

		const saveEdit = () => {
			editVisible.value = false;
			ElMessage.success(`修改第 ${idx + 1} 行成功`);
			Object.keys(form).forEach((item) => {
				tableData.value[idx][item] = form[item];
			});
		};

		//日期控件change方法
		const getQueryDate = () => {
			console.log(dataFormat(form.time[0]));
			query.startTime = dataFormat(form.time[0]);
			query.endTime = dataFormat(form.time[1]);
		}
		//日期格式化 yyyy-MM-dd
		const dataFormat = (time) => {

			return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()}`;
		}
		//日期格式化 yyyy-MM-dd HH:mm:ss
		const dateFormat = (time) => {
			return `${time.getFullYear()}-${time.getMonth() + 1 >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1)}-${time.getDate() >= 10 ? time.getDate() : '0' + time.getDate()} ${time.getHours() >= 10 ? time.getHours() : '0' + time.getHours()} : ${time.getMinutes() >= 10 ? time.getMinutes() : '0' + time.getMinutes()} : ${time.getSeconds() >= 10 ? time.getSeconds() : '0' + time.getSeconds()}`;
		}
		return {
			query,
			census,
			tableData,
			pageTotal,
			editVisible,
			form,
			dialogT,
			handleSearch,
			handlePageChange,
			handleDelete,
			handleEdit,
			saveEdit,
			multipleSelection: [],
			value: true,
			activeName: "first",

			getData,

			getQueryDate,

			formqjl,
			queryArea,
			queryStreet,
			queryPark,
			getArea,
			getStreet,
			getPark,

			licenceVisible,
			timeVisible,
			butLicence,
			butTime,
		};
	},
	methods: {
		handleSelectionChange(data) {
			this.selectedData = data;
		},
		handleDeleteAll() {
			var that = this;
			var val = this.selectedData;

			if (val) {
				val.forEach(function (item, index) {
					that.tableData.forEach(function (itemI, indexI) {
						if (item === itemI) {
							// ElMessageBox.confirm("确定要删除吗？", "提示", {
							//   type: "warning",
							// })
							//   .then(() => {
							//     ElMessage.success("删除成功");
							//     that.tableData.splice(indexI, 1);
							//   })
							//   .catch(() => {
							//     ElMessage.info("取消删除");
							//   });
							that.tableData.splice(indexI, 1);
						}
					});
				});
				ElMessage.success("删除成功");
				this.$refs.multipleTable.clearSelection();
			} else {
				ElMessage.warning(`请选择一条记录`);
			}
		},
	},
	created() {
		let h = document.documentElement.clientHeight || document.body.clientHeight;
		this.tableH = h - 308 + 'px';
	},
};
</script>
