<template>
	<div>
		<div class="container">
			<div class="handle-box">
				<div class="left-panel">
					<el-form inline label-width="80" size="small">
						<el-form-item label="法人">
							<el-input size="small" v-model="query.name" placeholder="法人"  @keyup.enter="handleSearch()"></el-input>
						</el-form-item>
						<el-form-item label="电话">
							<el-input size="small" v-model="query.phone" placeholder="电话"  @keyup.enter="handleSearch()"></el-input>
						</el-form-item>
						<el-form-item label="店铺名称">
							<el-input size="small" v-model="query.shop_name" placeholder="店铺名称"  @keyup.enter="handleSearch()"></el-input>
						</el-form-item>

						<el-form-item label="">
							<el-button size="small" type="primary" icon="el-icon-search" @click="handleSearch">查询
							</el-button>
						</el-form-item>
					</el-form>
				</div>

				<!-- <div class="clear"></div> -->
			</div>
			<el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<!--<el-table-column pro="ID" label="ID" width="55" align="center">-->
					<!--<template #default="scope">-->
						<!--{{ scope.row.id }}-->
					<!--</template>-->
				<!--</el-table-column>-->
				<!--<el-table-column type="index" label="序号" width="55" align="center">-->
				<!--</el-table-column>-->

				<el-table-column pro="ID" label="序号" width="55" align="center">
					<template #default="scope">
						{{ (query.pageIndex - 1) * query.pageSize + scope.$index + 1 }}
					</template>
				</el-table-column>
				<el-table-column prop="name" label="法人" align="center" width="130"></el-table-column>
				<el-table-column prop="phone" label="电话" align="center" width="120"></el-table-column>
				<el-table-column prop="shop_name" label="店铺名称" align="center" width="200">
				</el-table-column>
				<el-table-column prop="duty_par" label="税号" align="center" width="160">
				</el-table-column>
				<el-table-column prop="address" label="地址" align="center" width="240">
				</el-table-column>
				<el-table-column prop="park_names" label="停车区" align="center"></el-table-column>
				<el-table-column  prop="create_time" label="申请时间" align="center" width="180">
				</el-table-column>
				<el-table-column label="操作项" width="190" align="center" fixed="right">
					<template #default="scope">
						<el-button size="mini" type="text" icon="el-icon-view"   v-permission="['road_business_details', 'park_business_details']"
							@click="handleEdit(scope.$index, scope.row)">详情
						</el-button>
						<el-button size="mini" type="text" icon="el-icon-circle-check" v-permission="['park_business_tg', 'road_business_tg']"
							@click="handleView(scope.$index, scope.row)">通过
						</el-button>
						<el-button size="mini" type="text" icon="el-icon-circle-close" v-permission="['road_business_bh', 'park_business_bh']"
							@click="handleReject(scope.$index, scope.row)" class="red">驳回 
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
					:page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
			</div>
		</div>

		<el-dialog title="查看详情" v-model="editVisible" width="40%">
			<table class="desctable mgb20 w">
				<tr>
					<td class="tit" width="60">法人</td>
					<td>{{tempBussiness.name}}</td>
					<td class="tit" width="80">电话</td>
					<td>{{tempBussiness.phone}}</td>
				</tr>
				<tr>
					<td class="tit" width="60">店铺名称</td>
					<td>{{tempBussiness.shop_name}}</td>
					<td class="tit" width="80">税号</td>
					<td>{{tempBussiness.duty_par}}</td>
				</tr>
				<tr>
					<td class="tit" width="60">账号</td>
					<td colspan="3">{{tempBussiness.user_name}}</td>
				</tr>
				<tr>
					<td class="tit" width="60">营业执照</td>
					<td colspan="3">
						<el-image 
						    style="width: 140px; height: 100px"
						    :src="imgurl(tempBussiness.pic_url)"
							hide-on-click-modal="true"
			                            preview-teleported="true"
						    :preview-src-list="[imgurl(tempBussiness.pic_url)]">
						  </el-image>
					</td>
				</tr>
				<tr>
					<td class="tit" width="60">停车区</td>
					<td colspan="3">
						<el-tag v-for="tag in tempBussiness.name_list" :key="tag">{{tag}}</el-tag>
					</td>
				</tr>
			</table>

			<template #footer>
				<span class="dialog-footer">
					<el-button @click="handleView1(tempBussiness.id)" type="primary">通 过</el-button>
					<el-button @click="handleReject1(tempBussiness.id)" type="danger">驳 回</el-button>
					<el-button @click="editVisible = false">关 闭</el-button>
				</span>
			</template>
		</el-dialog>
		
		<!-- 编辑弹出框 -->
		<el-dialog title="填写驳回原因" v-model="rejectVisible" width="35%" @close="outReject">
		  <el-form label-width="70px" size="small">
		    <el-input v-model="formReject.reason" type="textarea" :rows="6"></el-input>
		  </el-form>
		
		  <template #footer>
		    <span class="dialog-footer">
		      <el-button @click="outReject">取 消</el-button>
		      <el-button type="primary" @click="saveReject">确 定</el-button>
		    </span>
		  </template>
		</el-dialog>
	</div>
</template>

<script>
	import {
		ref,
		reactive
	} from "vue";
	import {
		ElMessage,
		ElMessageBox
	} from "element-plus";
	import {
		roleData
	} from "../../api/index";
	import {getAuditInfoByPage,approved,reject,getOneById} from "../../api/business.js";
    import File_URL from '../../file_url';

	export default {
		name: "examine",
		components: {
		},

        data() {
            return {
                imgViewUrl: File_URL.file_hx_img_url,//图片回显路径
            };
        },
		setup() {
			const query = reactive({
				address: "",
				name: "",
				checked: false,
				pageIndex: 1,
				pageSize: 10,
			});
			const tableData = ref([]);
			const pageTotal = ref(0);
			const  tempBussiness =  ref({});
			const  tempName =  ref({});
			// 获取表格数据
			const getData = () => {
				getAuditInfoByPage(query).then((res) => {
					tableData.value = res.data.list;
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
			

			// 表格编辑时弹窗和保存
			const editVisible = ref(false);
			const viewVisible = ref(false);
			let form = reactive({
				name: "",
				address: "",
				czroptions: [{
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
				checked: false,
				startTime: "",
				endTime: "",
			});
			let idx = -1;
			const handleEdit = (index, row) => {
				idx = index;
				Object.keys(form).forEach((item) => {
					form[item] = row[item];
				});

				getOneById(row.id).then((res) => {
					tempBussiness.value = res.data;
					tempName.value = res.data.name_list;
				});

				editVisible.value = true;
			};

			// 驳回
			const rejectVisible = ref(false);
			let formReject = reactive({
				reason: "",
			});
			// let idr = -1;

			let tempData = {};
			const handleReject = (data ,dd) => {
                tempData = dd;
				rejectVisible.value = true;
			};

			const handleReject1 = (data ) => {
				tempData.id = data;
				rejectVisible.value = true;
			};
			
			const handleView = (data, dd) => {

				var id = dd.id;

				ElMessageBox.confirm("确定审核通过吗？", "提示", {
				  type: "warning",
				})
				  .then(() => {

					  approved(id).then((res) => {
						  ElMessage.success("通过审核");
						  getData();
					  });


				    // tableData.value.splice(index, 1);
				  })
				  .catch(() => {
					  ElMessage.info("取消审核");
				  });
				
			};


			const handleView1 = (data, dd) => {
               console.log(data);
				var id = data;
				ElMessageBox.confirm("确定审核通过吗？", "提示", {
					type: "warning",
				})
						.then(() => {

							approved(id).then((res) => {
								ElMessage.success("通过审核");
								getData();
								editVisible.value = false;
							});


							// tableData.value.splice(index, 1);
						})
						.catch(() => {
							ElMessage.info("取消审核");
						});

			};

			
			const saveReject = () => {
			  rejectVisible.value = false;
			  editVisible.value = false;
			  var reason = formReject.reason;
			  tempData.reason = reason;
				reject(tempData).then((res) => {
					formReject.reason = "";
				  ElMessage.success("驳回成功");
				  getData();
				});

			};


			const outReject = () => {
				rejectVisible.value = false;
				editVisible.value = false;
				formReject.reason = "";

			};


			return {
				query,
				tableData,
				pageTotal,
				editVisible,
				viewVisible,
				rejectVisible,
				form,
				formReject,
				tempBussiness,
				tempName,
				handleSearch,
				handlePageChange,
				handleEdit,
				handleView,
				handleView1,
				handleReject,
				handleReject1,
				saveReject,
				outReject,
				multipleSelection: [],
				dialogImageUrl: "",
				ppVisible: false,
				isActive: false,
				activeName: "first",
				url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
				srcList: [
				  'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
				  'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
				]
			};
		},
		methods: {
			//上传图片操作
			handleRemove(file, fileList) {
				console.log(file, fileList);
			},
			//图片预览
			handlePictureCardPreview(file) {
				this.dialogImageUrl = file.url;
				this.ppVisible = true;
			},

            //图片回显
            imgurl: function (url) {
                if (url != "" && url != null) {
                    return url;
                }
            },
			
			bindRoad() {
				var that = this;
				var val = this.selectedData;
				if (val) {
					val.forEach(function(item, index) {
						that.tableData.forEach(function(itemI, indexI) {
							if (item === itemI) {}
						});
					});
					this.viewVisible = true;
					//ElMessage.success("分配成功");
					this.$refs.multipleTable.clearSelection();
				} else {
					ElMessage.warning(`请选择一条记录`);
				}
			},
		},
	};
</script>
<style scoped="">
	.el-tag{
		margin: 4px 5px 4px 0;
	}
</style>
