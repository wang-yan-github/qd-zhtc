<template>
	<div>
		<el-row :gutter="20">
		  <el-col :span="6">
			  <el-card shadow="hover">
			    <template #header>
			      <div class="clearfix">
			        <span>停车场</span>
			      </div>
			    </template>
				
			    <div class="text item">
			      <el-tree :data="dataTree" node-key="id"  :props="defaultProps" @node-click="handleNodeClick"></el-tree>
			    </div>
			  </el-card>
			  
			  
		  </el-col>
		  <el-col :span="18">
			 <div class="container">
			 	<ul>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
					<li></li>
				</ul>
			 	<div>
					
				</div>
			 </div>
		  </el-col>
		</el-row>
		
		
		
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
		fetchData
	} from "../../../api/index";
	
	
	export default {
		name: "videoView",
		data() {
		      return {
		        dataTree: [{
					id:1,
		          label: '停车场名称 1',
				  icon:'el-icon-video-camera'
		        }, {
					id:2,
		          label: '停车场名称 2',
				  icon:'el-icon-video-camera'
		        }, {
					id:3,
		          label: '停车场名称 3',
				  icon:'el-icon-video-camera'
		        }, {
					id:4,
		          label: '停车场名称 4',
				  icon:'el-icon-video-camera'
		        }, {
					id:5,
		          label: '停车场名称 5',
				  icon:'el-icon-video-camera'
		        }, {
					id:6,
		          label: '停车场名称 6',
				  icon:'el-icon-video-camera'
		        }, {
					id:7,
		          label: '停车场名称 7',
				  icon:'el-icon-video-camera'
		        }, {
					id:8,
		          label: '停车场名称 8',
				  icon:'el-icon-video-camera'
		        }, {
					id:9,
		          label: '停车场名称 9',
				  icon:'el-icon-video-camera'
		        }, {
					id:10,
		          label: '停车场名称 10',
				  icon:'el-icon-video-camera'
		        }, {
					id:11,
		          label: '停车场名称 11',
				  icon:'el-icon-video-camera'
		        }, {
					id:12,
		          label: '停车场名称 12',
				  icon:'el-icon-video-camera'
		        }, {
					id:13,
		          label: '停车场名称 13',
				  icon:'el-icon-video-camera'
		        }],
		        defaultProps: {
		          children: 'children',
		          label: 'label'
		        }
		      };
		    },
		setup() {
			const query = reactive({
				address: "",
				name: "",
				date1: "",
				pageIndex: 1,
				pageSize: 10,
			});
			const tableData = ref([]);
			const pageTotal = ref(0);
			// 获取表格数据
			const getData = () => {
				fetchData(query).then((res) => {
					tableData.value = res.list;
					pageTotal.value = res.pageTotal || 50;
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
					.catch(() => {});
			};
			const dialogT = "新增";

			// 表格编辑时弹窗和保存
			const editVisible = ref(false);
			const viewVisible = ref(false);
			let form = reactive({
				name: "",
				address: "",
				radio2: 2,
				radio3: "通过",
				date1: "",
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

			const handleView = (index, row) => {
				editVisible.value = false;
				viewVisible.value = true;
			};

			const saveEdit = () => {
				editVisible.value = false;
				ElMessage.success(`修改第 ${idx + 1} 行成功`);
				Object.keys(form).forEach((item) => {
					tableData.value[idx][item] = form[item];
				});
			};

			return {
				query,
				tableData,
				pageTotal,
				editVisible,
				viewVisible,
				form,
				dialogT,
				handleSearch,
				handlePageChange,
				handleDelete,
				handleEdit,
				handleView,
				saveEdit,
				multipleSelection: [],
				value: true,
				activeName: "first",
				
			};
		},
		methods: {
			handleNodeClick(data) {
			        console.log(data);
			      },
			handleSelectionChange(data) {
				this.selectedData = data;
			},
			handleDeleteAll() {
				var that = this;
				var val = this.selectedData;

				if (val) {
					val.forEach(function(item, index) {
						that.tableData.forEach(function(itemI, indexI) {
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
	};
</script>
