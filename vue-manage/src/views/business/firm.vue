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
				<!--<div class="right-panel">-->
					<!---->
				<!--</div>-->
				<!-- <div class="clear"></div> -->
			</div>
			<el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<!--<el-table-column pro="ID" label="ID" width="55" align="center" >-->
					<!--<template #default="scope">-->
						<!--{{ scope.row.id }}-->
					<!--</template>-->
				<!--</el-table-column>-->
				<!--<el-table-column type="index" label="序号" width="55" align="center">-->
				<!--</el-table-column>-->

				<el-table-column pro="ID" label="序号" width="55" align="center">
					<template #default="scope">
						{{ scope.$index + 1 }}
					</template>
				</el-table-column>
				<el-table-column prop="name" label="法人" align="center" width="120"></el-table-column>
				<el-table-column prop="phone" label="电话" align="center" width="120"></el-table-column>
				<el-table-column prop="shop_name" label="店铺名称" align="center"></el-table-column>
				<el-table-column prop="duty_par" label="税号" align="center" width="180"></el-table-column>
				<el-table-column prop="address" label="地址" align="center" width="240"></el-table-column>
				<el-table-column prop="park_names" label="停车区" align="center">
					<template #default="scope">
						<p class="no-warp" @click="handleView2(scope.$index, scope.row)">{{ scope.row.park_names }}</p>
					</template>
				</el-table-column>
				<el-table-column prop="examine_time" label="审核时间" align="center" width="180"></el-table-column>
				<el-table-column prop="examine_name" label="审核人" align="center" width="130"></el-table-column>
				<el-table-column prop="wallet_balance" label="钱包余额(元)" align="center" width="120"></el-table-column>
				<!--<el-table-column prop="discount_card_balance" label="折扣卡余额(元)" align="center" width="120"></el-table-column>-->
				<el-table-column label="操作" width="160" align="center" fixed="right">
					<template #default="scope">
						<el-button size="mini" type="text" icon="el-icon-view"   v-permission="['road_businessFirm_details', 'park_businessFirm_details']"
							@click="handleView(scope.$index, scope.row)">详情
						</el-button>
						<el-button size="mini" type="text" icon="el-icon-edit"  v-permission="['road_businessFirm_kkjl', 'park_businessFirm_kkjl']"
							@click="handleRecord(scope.$index, scope.row)">扣款记录
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
					:page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
			</div>
		</div>



		<!-- 查看弹出框 -->
		<el-dialog title="查看详情" v-model="viewVisible" width="40%">
			<table class="desctable mgb20 w" >
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
						<el-image hide-on-click-modal="true" preview-teleported="true" style="width: 140px; height: 100px" :src="imgurl(tempBussiness.pic_url)" :preview-src-list="[imgurl(tempBussiness.pic_url)]">
						</el-image>
					</td>
				</tr>
				<tr>
					<td class="tit" width="60">停车区</td>
					<td colspan="3">
						<el-tag v-for="tag in tempBussiness.name_list" :key="tag">{{tag}}</el-tag>
					</td>
				</tr>
				<tr>
					<td class="tit" width="60">钱包余额</td>
					<td colspan="3">{{tempBussiness.wallet_balance}}元</td>
				</tr>
			</table>

			<template #footer>
				<span class="dialog-footer">
					<el-button @click="viewVisible = false">关 闭</el-button>
				</span>
			</template>
		</el-dialog>
		<!-- 扣款记录 -->
		<el-dialog title="查看扣款记录" v-model="recordVisible" width="980px">
			<el-button type="success" size="small" class="export" icon="el-icon-upload2" @click="exportEx">导出</el-button>
			<el-table :data="tableData1" border class="table mt20" ref="multipleTable"
				header-cell-class-name="table-header">
				<!--<el-table-column pro="ID" label="序号" width="55" align="center">-->
					<!--<template #default="scope">-->
						<!--{{ scope.row.id }}-->
					<!--</template>-->
				<!--</el-table-column>-->
				<!--<el-table-column type="index" label="序号" width="55" align="center">-->
				<!--</el-table-column>-->
				<el-table-column type="index" label="序号" width="55" align="center">
					<!--<template #default="scope">-->
						<!--{{ (query1.pageIndex - 1) * query1.pageSize2 + scope.$index + 1 }}-->
					<!--</template>-->
				</el-table-column>
				<el-table-column prop="order_no"  label="订单号" align="center" minWidth="180"></el-table-column>
				<el-table-column prop="car_no_name"  label="车牌号" align="center" minWidth="130"></el-table-column>
				<el-table-column prop="cost"  label="停车费用" align="center" width="80"></el-table-column>
				<el-table-column prop="actual_deduct"  label="实际扣款" align="center" width="80"></el-table-column>
				<!--<el-table-column prop="name"  label="扣款类型" align="center" minWidth="130">-->
					<!--<template #default="scope">-->
						<!--<el-tag type="success">标签二</el-tag>-->
					<!--</template>-->
				<!--</el-table-column>-->
				<el-table-column prop="wallet_balance" label="钱包余额" align="center" width="80"></el-table-column>
				<el-table-column prop="create_time" label="时间" align="center" width="200"></el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination background layout="total, prev, pager, next" :current-page="query1.pageIndex"
							   :page-size="query1.pageSize2" :total="pageTotal1" @current-change="handlePageChange1"></el-pagination>
			</div>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="recordVisible = false">关 闭</el-button>
				</span>
			</template>
		</el-dialog>
		<!-- 详情弹出框 -->
		<el-dialog title="地址列表" v-model="viewVisible2" width="840px" @close="closeAddress">
        <div class="index-bar-content">
            <el-scrollbar style="height: 100%" ref="scrollbar">
                <div :id="key" class="main-list" v-for="(value, key) in indexData" :key="key" ref="listGroup">
                <div class="title-key">{{ key }}</div>
                <div class="content-container">
                    <div class="content-item" v-for="(val, index) in value" :key="index">
						<el-tag size="small">{{ val.name }}</el-tag>
                    </div>
                </div>
                </div>
            </el-scrollbar>
            <!-- 右侧字母列表 -->
            <ul class="char-list">
                    <li
                    v-for="(item,index) in indexList"
                    :key="item.index"
                    @click="scrollToLetter(item)"
                    :class="{'active':currentIndex === item.id}"
                    >
                    {{ item.key }}
                    <!-- {{ item.id }}
                    {{currentIndex}} -->
                    </li>

                </ul>
            </div>
      </el-dialog>
	</div>
</template>

<script>
	import { ref, reactive, getCurrentInstance, nextTick,onMounted,computed  } from "vue";
	import { ElMessage, ElMessageBox,ElScrollbar} from "element-plus";
	import pinyin from "js-pinyin"
	import {
		fetchData
	} from "../../api/index";
	import {toPassList,getBusinessDeductionById,exportBusinessDeduction,getOneById } from "../../api/business.js";
    import File_URL from '../../file_url';

	export default {
		name: "firm",
		components: { ElScrollbar },
        data() {
            return {
                imgViewUrl: File_URL.file_hx_img_url,//图片回显路径
            };
        },
		setup() {
			const query = reactive({
				address: "",
				name: "",
				pageIndex: 1,
				pageSize: 10,
				pageSize2: 6,
			});
			const query1 = reactive({
				address: "",
				name: "",
				pageIndex: 1,
				pageSize: 10,
				pageSize2: 6,
			});
			const tableData = ref([]);
			const tableData1 = ref([]);
			const tableData2 = ref([]);
			const pageTotal = ref(0);
			const pageTotal1 = ref(0);
			const pageTotal2 = ref(0);
			const  tempBussiness =  ref({});
			const  tempName =  ref({});
			// 获取表格数据
			const getData = () => {
				// fetchData(query).then((res) => {
				// 	tableData.value = res.list;
				// 	pageTotal.value = res.pageTotal || 50;
				// 	tableData2.value = res.list;
				// 	pageTotal2.value = res.pageTotal || 50;
				// });

				toPassList(query).then((res) => {
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

			const handlePageChange1 = (val) => {
				console.log(val)
				query1.pageIndex = val;
				var row = {};
				row.pageIndex = val;
				row.pageSize = 6;
				row.id = idsss;
				getBusinessDeductionById(row).then((res) => {
					console.log(res)
					tableData1.value = res.data.list;
					pageTotal1.value = res.data.total;
				});

			};

			// 解除绑定询问
			const carDelete = (index) => {
				// 二次确认删除
				ElMessageBox.confirm("确定要解除绑定吗？", "提示", {
						type: "warning",
					})
					.then(() => {
						ElMessage.success("解绑成功");
						tableData.value.splice(index, 1);
					})
					.catch(() => {});
			};

			// 表格编辑时弹窗和保存
			const addVisible = ref(false);
			const viewVisible = ref(false);
			const recordVisible  = ref(false)

			let form = reactive({
				name: "",
				address: "",
			});
			let idx = -1;
			let idsss  ;
			const handleRecord = (index, row) => {
				idx = index;
				query1.pageIndex = 1;
				row.pageIndex = 1;
				row.pageSize = 6;
				idsss = row.id;

				getBusinessDeductionById(row).then((res) => {
					console.log(res)
					tableData1.value = res.data.list;
					pageTotal1.value = res.data.total;
				});
				// Object.keys(form).forEach((item) => {
				// 	form[item] = row[item];
				// });

				recordVisible.value = true;
			};


			const handleView = (index, row) => {
				idx = index;

				Object.keys(form).forEach((item) => {
					form[item] = row[item];
				});
				// tempBussiness = row;
				getOneById(row.id).then((res) => {
					tempBussiness.value = res.data;
					tempName.value = res.data.name_list;
				});


				viewVisible.value = true;
			};

			const handleAdd = () =>{
				addVisible.value = true;
			}



			const saveAdd = () => {
				addVisible.value = false;
			};

			const exportEx = () => {
				console.log(idsss)
				var business_id = idsss;
				var businessDeduction = {};
				businessDeduction.business_id = business_id;
				exportBusinessDeduction(businessDeduction).then((res) => {
					const url = window.URL.createObjectURL(new Blob([res]))
					const link = document.createElement('a')
					link.href = url
					link.setAttribute('download', '扣款记录.xls')
					document.body.appendChild(link)
					link.click()
				});
			};

			// 详情地址
			let listGroup = ref(null);
    let scrollbar = ref(null);
    let charList = ref([]);
    let listHeight = ref([]);
    let currentIndex = ref(0);
    let indexPage = ref(1);
    let indexLimit = ref(24);
    let indexData = ref({});

    const viewVisible2 = ref(false);
    const handleView2 = (index,row,type) => {
        nextTick(() => {
            calculateHeight()
            handleScroll()
        });

        // 循环遍历字符串 并生成新的数组 键值对 为 name：road_name
        let roadsList = [];
        let roadNames = row.park_names.split(',');
		roadNames.forEach(function (item) {
			roadsList.push({
				name: item
			})
		})

        pinyin.setOptions({ checkPolyphone: false, charCase: 0 })
            let alphabet = []
            let _charList = []
            for (let i = 0; i < roadsList.length; i++) {
                // 获取原数组每一项的 name 值
                let name = roadsList[i].name
                // 获取每一个name值第一个字的大写首字母（传入的 name 是中文时默认得到大写字母，name 是英文时按照原字符串输出，可能是小写）
                let initial = pinyin.getCamelChars(name).substring(0, 1).toUpperCase()
                // 给数组每一项增加名为 initial 的 key，值就是第一个字的大写首字母
                roadsList[i].initial = initial
                // 获取用于索引的字母
                if (alphabet.indexOf(initial) === -1) {
                alphabet.push(initial)
                }
            }
            // 按字母表顺序排序
            alphabet.sort()

            // 给每个字母增加唯一标识，后面定位时会用到
            for (var i = 0; i < alphabet.length; i++) {
                _charList.push({
                id: i,
                key: alphabet[i]
                })
            }
            // charList.value = _charList

			// 生成 charList.value 为id 从0开始， key 从A-Z  的数组
            charList.value = []
              for (var i = 0; i < 26; i++) {
                  charList.value.push({
                  id:'',
                  key: String.fromCharCode(65 + i)
                  })
              }

              // 对比charList.value 和 _charList，如果_charList中的key值和charList.value中的key值相同，则将_charList中的id值赋值给charList.value 中的id值
              for (var i = 0; i < _charList.length; i++) {
                  for (var j = 0; j < charList.value.length; j++) {
                  if (_charList[i].key === charList.value[j].key) {
                      charList.value[j].id = _charList[i].id
                  }
                  }
              }



            let resultData = {}
            // 将源数据按照首字母分类
            for (let i = 0; i < alphabet.length; i++) {
                resultData[alphabet[i]] = roadsList.filter((item) => {
                return item.initial === alphabet[i]
                })
            }
            // 得到最终结果
            indexData.value = resultData

            // getDataList(sourdata);

            viewVisible2.value = true;
        };

        const calculateHeight = () => {
        listHeight = []
        nextTick(() => {
            const list = listGroup.value
            let height = 0
            listHeight.push(height)
            if (list) {

            for (let i = 0; i < list.length; i++) {
                let item = list[i]
                height += item.clientHeight
                listHeight.push(height)
            }
            }

        })

        };
        const scrollToLetter = (item) => {
            // 判断item.id是否为空，为空则不执行
            if (item.id === '' || item.id === undefined) {
                return
            }
            nextTick(() => {
                scrollbar.value.$el.querySelector('.el-scrollbar__wrap').scrollTop = listHeight[item.id]
                currentIndex.value = item.id
            })

        };
        const handleScroll = () => {
            nextTick(() => {
                const scrollEle = scrollbar.value.$el.querySelector('.el-scrollbar__wrap');
                scrollEle.onscroll = () => {
                    let newY = scrollEle.scrollTop
                    const listHeightSet = listHeight
                    // 在中间部分滚动
                    for (let i = 0; i < listHeightSet.length - 1; i++) {
                    let height1 = listHeightSet[i]
                    let height2 = listHeightSet[i + 1]
                    if (height1 <= newY && newY < height2) {
                        currentIndex.value = i
                        // console.log(this.currentIndex, "currentIndex")
                        let currentPage = Math.floor(i / indexLimit) + 1
                        indexPage = currentPage
                        return
                    }
                    }
                }

            })

        };

		const closeAddress = () =>{
            scrollbar.value.$el.querySelector('.el-scrollbar__wrap').scrollTop = 0
            currentIndex.value = 0
            viewVisible.value = false;
        };


			return {
				query,
				query1,
				tableData,
				tableData1,
				tableData2,
				pageTotal,
				pageTotal1,
				pageTotal2,
				addVisible,
				viewVisible,
				recordVisible,
				form,
				tempBussiness,
				tempName,
				handleSearch,
				handlePageChange,
				handlePageChange1,
				carDelete,
				handleView,
				handleAdd,
				handleRecord,
				saveAdd,
				exportEx,
				multipleSelection: [],
				value: true,
				activeName: "first",
				dialogImageUrl: "",
				ppVisible: false,
				url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
				srcList: [
				  'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
				  'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
				],
				// 详情地址
				viewVisible2,
				handleView2,
				charList,
				listHeight,
				currentIndex,
				indexPage,
				indexLimit,
				indexData,
				listGroup,
				scrollbar,
				calculateHeight,
				scrollToLetter,
				handleScroll,
				closeAddress,
			};
		},
		computed: {
			indexList() {
				return this.charList
			}
		},
		methods: {
			//上传图片操作
			handleRemove(file, fileList) {
			  console.log(file, fileList);
			},
            //图片回显
            imgurl: function (url) {
                if (url != "" && url != null) {
                    return url;
                }
            },
			//图片预览
			handlePictureCardPreview(file) {
			  this.dialogImageUrl = file.url;
			  this.ppVisible = true;
			},



		},
	};
</script>
<style scoped="">
	.el-tag{
		margin: 4px 5px 4px 0;
	}
	.export{
		float: right;
		margin-bottom: 10px;
		margin-top: -10px;
	}
</style>
<style scoped>
.index-bar-content {
  position: relative;
  width: 800px;
  height: 600px;
}
.el-scrollbar__wrap {
    overflow-x: hidden;

  }
  .el-scrollbar__view {
      padding: 0 20px;
    }
    .main-list {
    padding-top: 10px;

  }
  .title-key {
      padding-bottom: 12px;
      font-size: 14px;
      font-weight: bold;
    }
    .content-container {
      display: flex;
      flex-wrap: wrap;
      padding-right: 80px;
      box-sizing: border-box;
    }
    .content-item {
        margin-right: 16px;
        margin-bottom: 12px;
        font-size: 12px;
      }
.el-popover {
  padding: 0;
}
.char-list {
  z-index: 99;
  width: 24px;
  height: 100%;
  background: #fafbfe;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  list-style: none;
  display: flex;
  flex-direction: column;
  text-align: center;

}
.char-list li {
    height: 19px;
    display: inline;
    cursor: pointer;
    font-size: 14px;
  }
  .char-list .active {
    color: #E6A23C;
  }
  .no-warp{
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: #409EFF;
  }
  .no-warp:hover{
    text-decoration: underline;
    cursor: pointer;
  }
</style>
