import{f as e,b as l,q as a,r as t,M as i,o as s,h as r,i as n,k as o,l as d,I as c,m as u,c as m,t as h,p,w as g,L as b}from"./vendor.ac52fd94.js";import{_ as f,bg as w,bh as _}from"./index.1d57ac4a.js";import{U as y}from"./UE.013b43f7.js";import{F as v}from"./file_url.ec979fed.js";const V={name:"noticeList",components:{Ueditor:y},data:()=>({tableH:0,imgViewUrl:v.file_hx_img_url}),setup(){const t=e({carNo:"",status:"",isYellowCard:!1,phone:"",pageIndex:1,pageSize:15}),i=l([]),s=l(0),r=()=>{w(t).then((e=>{console.log(e.data),i.value=e.data.list,s.value=e.data.total}))};r();const n=l(!1);let o=e({id:"",status:"",rejectReason:""}),d=-1;return{query:t,tableData:i,pageTotal:s,editVisible:n,form:o,dialogT:"新增",handleSearch:()=>{t.pageIndex=1,r()},handlePageChange:e=>{t.pageIndex=e,r()},handleDelete:e=>{b.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{a.success("删除成功"),i.value.splice(e,1)})).catch((()=>{}))},handleEdit:(e,l)=>{o.id=l.id,d=e,Object.keys(o).forEach((e=>{o[e]=l[e]})),n.value=!0},handleView:(e,l)=>{o.id=l.id,b.confirm("确定审核通过吗？","提示",{type:"warning"}).then((()=>{o.status=1,_(o).then((e=>{a.success("审核通过成功"),r()}))}))},saveEdit:()=>{o.status=0,_(o).then((e=>{a.success("审核不通过成功"),r()})),n.value=!1,Object.keys(o).forEach((e=>{i.value[d][e]=o[e]}))},multipleSelection:[],dialogImageUrl:"",ppVisible:!1}},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"},methods:{handleRemove(e,l){console.log(e,l)},handlePictureCardPreview(e){this.dialogImageUrl=e.url,this.ppVisible=!0},imgurl:function(e){if(""!=e&&null!=e)return this.imgViewUrl+e},handleSelectionChange(e){this.selectedData=e},handleDeleteAll(){var e=this,l=this.selectedData;l?(l.forEach((function(l,a){e.tableData.forEach((function(a,t){l===a&&e.tableData.splice(t,1)}))})),a.success("删除成功"),this.$refs.multipleTable.clearSelection()):a.warning("请选择一条记录")}}},k={class:"container"},C={class:"handle-box"},x={class:"left-panel"},z=n("div",{class:"clear"},null,-1),U={class:"pagination"},q={class:"dialog-footer"},S=["src"];var E=f(V,[["render",function(e,l,a,b,f,w){const _=t("el-input"),y=t("el-form-item"),v=t("el-checkbox"),V=t("el-option"),E=t("el-select"),j=t("el-button"),D=t("el-form"),I=t("el-table-column"),H=t("el-tag"),T=t("el-image"),P=t("el-table"),R=t("el-pagination"),N=t("el-dialog"),Y=i("permission");return s(),r("div",null,[n("div",k,[n("div",C,[n("div",x,[o(D,{inline:"","label-width":"80",size:"small",class:"lineH0"},{default:d((()=>[o(y,{label:"车牌号",class:"search-mb0"},{default:d((()=>[o(_,{onKeyup:l[0]||(l[0]=c((e=>b.handleSearch()),["enter"])),size:"small",modelValue:b.query.carNo,"onUpdate:modelValue":l[1]||(l[1]=e=>b.query.carNo=e),class:"handle-input w170",placeholder:"车牌号"},null,8,["modelValue"])])),_:1}),o(y,{label:"",class:"search-mb0"},{default:d((()=>[o(v,{modelValue:b.query.isYellowCard,"onUpdate:modelValue":l[2]||(l[2]=e=>b.query.isYellowCard=e)},{default:d((()=>[u("黄牌")])),_:1},8,["modelValue"])])),_:1}),o(y,{label:"状态",class:"search-mb0"},{default:d((()=>[o(E,{clearable:"",modelValue:b.query.status,"onUpdate:modelValue":l[3]||(l[3]=e=>b.query.status=e),filterable:"",size:"small",class:"w100"},{default:d((()=>[o(V,{key:"-1",label:"待审核 ",value:"-1"}),o(V,{key:"1",label:"通过 ",value:"1"}),o(V,{key:"0",label:"未通过 ",value:"0"})])),_:1},8,["modelValue"])])),_:1}),o(y,{label:"申诉人手机",class:"search-mb0"},{default:d((()=>[o(_,{onKeyup:l[4]||(l[4]=c((e=>b.handleSearch()),["enter"])),size:"small",modelValue:b.query.phone,"onUpdate:modelValue":l[5]||(l[5]=e=>b.query.phone=e),class:"handle-input w170",placeholder:"手机号"},null,8,["modelValue"])])),_:1}),o(y,{label:"",class:"search-mb0"},{default:d((()=>[o(j,{size:"small",type:"primary",icon:"el-icon-search",onClick:b.handleSearch},{default:d((()=>[u("查询 ")])),_:1},8,["onClick"])])),_:1})])),_:1})]),z]),o(P,{data:b.tableData,border:"",class:"table",ref:"multipleTable","max-height":f.tableH,"header-cell-class-name":"table-header",onSelectionChange:w.handleSelectionChange},{default:d((()=>[o(I,{type:"index",label:"序号",width:"55",align:"center"}),o(I,{prop:"car_no",label:"车牌号",width:"110",align:"center"},{default:d((e=>["1"==e.row.car_type?(s(),m(H,{key:0,size:"small",textContent:h(e.row.car_no)},null,8,["textContent"])):"2"==e.row.car_type?(s(),m(H,{key:1,size:"small",type:"success",textContent:h(e.row.car_no)},null,8,["textContent"])):"3"==e.row.car_type?(s(),m(H,{key:2,size:"small",type:"warning",textContent:h(e.row.car_no)},null,8,["textContent"])):p("",!0)])),_:1}),o(I,{prop:"phone",label:"申诉人手机",align:"center",width:"130"},{default:d((e=>[u(h(e.row.phone),1)])),_:1}),o(I,{label:"行驶证",align:"center",width:"130"},{default:d((e=>[o(T,{class:"table-td-thumb",src:w.imgurl(e.row.driveringlic_url),"hide-on-click-modal":"true","preview-teleported":"true","preview-src-list":[w.imgurl(e.row.driveringlic_url)]},null,8,["src","preview-src-list"])])),_:1}),o(I,{label:"驾驶证",align:"center",width:"130"},{default:d((e=>[o(T,{class:"table-td-thumb",src:w.imgurl(e.row.driverlic_url),"hide-on-click-modal":"true","preview-teleported":"true","preview-src-list":[w.imgurl(e.row.driverlic_url)]},null,8,["src","preview-src-list"])])),_:1}),o(I,{prop:"remark",label:"备注"},{default:d((e=>[u(h(e.row.remark),1)])),_:1}),o(I,{label:"申诉时间",prop:"create_time",width:"180",align:"center"},{default:d((e=>[u(h(e.row.create_time),1)])),_:1}),o(I,{label:"状态",align:"center",width:"100"},{default:d((e=>[0!=e.row.status&&1!=e.row.status?(s(),m(H,{key:0,size:"small"},{default:d((()=>[u("待审批")])),_:1})):p("",!0),1==e.row.status?(s(),m(H,{key:1,size:"small",type:"success"},{default:d((()=>[u("通过")])),_:1})):p("",!0),0==e.row.status?(s(),m(H,{key:2,size:"small",type:"warning"},{default:d((()=>[u("未通过")])),_:1})):p("",!0)])),_:1}),o(I,{label:"审核人",prop:"name",width:"120",align:"center"},{default:d((e=>[u(h(e.row.login_name),1)])),_:1}),o(I,{label:"操作",width:"150",align:"center"},{default:d((e=>[0!=e.row.status&&1!=e.row.status?g((s(),m(j,{key:0,size:"mini",type:"text",icon:"el-icon-circle-check",onClick:l=>b.handleView(e.$index,e.row)},{default:d((()=>[u("通过 ")])),_:2},1032,["onClick"])),[[Y,["road_carnumshensu_tg","park_carnumshensu_tg"]]]):p("",!0),0!=e.row.status&&1!=e.row.status?g((s(),m(j,{key:1,size:"mini",type:"text",icon:"el-icon-circle-close",onClick:l=>b.handleEdit(e.$index,e.row),class:"red"},{default:d((()=>[u("不通过 ")])),_:2},1032,["onClick"])),[[Y,["road_carnumshensu_bh","park_carnumshensu_bh"]]]):p("",!0)])),_:1})])),_:1},8,["data","max-height","onSelectionChange"]),n("div",U,[o(R,{background:"",layout:"total, prev, pager, next","current-page":b.query.pageIndex,"page-size":b.query.pageSize,total:b.pageTotal,onCurrentChange:b.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])]),o(N,{title:"填写拒绝原因",modelValue:b.editVisible,"onUpdate:modelValue":l[8]||(l[8]=e=>b.editVisible=e),width:"35%"},{footer:d((()=>[n("span",q,[o(j,{onClick:l[7]||(l[7]=e=>b.editVisible=!1)},{default:d((()=>[u("取 消")])),_:1}),o(j,{type:"primary",onClick:b.saveEdit},{default:d((()=>[u("确 定")])),_:1},8,["onClick"])])])),default:d((()=>[o(D,{"label-width":"70px",size:"small"},{default:d((()=>[o(_,{modelValue:b.form.rejectReason,"onUpdate:modelValue":l[6]||(l[6]=e=>b.form.rejectReason=e),type:"textarea",rows:6},null,8,["modelValue"])])),_:1})])),_:1},8,["modelValue"]),o(N,{modelValue:b.ppVisible,"onUpdate:modelValue":l[9]||(l[9]=e=>b.ppVisible=e)},{default:d((()=>[n("img",{width:"100%",src:b.dialogImageUrl,alt:""},null,8,S)])),_:1},8,["modelValue"])])}]]);export{E as default};