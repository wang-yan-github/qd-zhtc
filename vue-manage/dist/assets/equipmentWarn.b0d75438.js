import{f as e,b as l,q as a,r as t,o as i,h as n,i as o,k as s,l as d,F as r,j as u,c,m,t as p,L as h}from"./vendor.ac52fd94.js";import{_ as b,bB as f}from"./index.1d57ac4a.js";import{U as g}from"./UE.013b43f7.js";const v={name:"equipmentwarnA",components:{Ueditor:g},data:()=>({tableH:0}),setup(){const t=e({address:"",name:"",checked:!1,pageIndex:1,pageSize:15}),i=l([]),n=l(0),o=()=>{f(t).then((e=>{i.value=e.list,n.value=e.pageTotal||50}))};o();const s=l(!1);let d=e({name:"",address:"",czroptions:[{value:"1",label:"平台管理员"},{value:"2",label:"管理员"},{value:"3",label:"车场"}]}),r=-1;return{query:t,tableData:i,pageTotal:n,editVisible:s,form:d,dialogT:"新增",handleSearch:()=>{t.pageIndex=1,o()},handlePageChange:e=>{t.pageIndex=e,o()},handleDelete:e=>{h.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{a.success("删除成功"),i.value.splice(e,1)})).catch((()=>{}))},handleEdit:(e,l)=>{r=e,Object.keys(d).forEach((e=>{d[e]=l[e]})),s.value=!0},handleView:(e,l)=>{h.confirm("将解除车牌现有绑定者，重新绑定到申诉用户，确定审核通过吗？","提示",{type:"warning"}).then((()=>{a.success("解除成功"),that.tableData.splice(indexI,1)})).catch((()=>{a.info("取消解除")}))},saveEdit:()=>{s.value=!1,a.success(`修改第 ${r+1} 行成功`),Object.keys(d).forEach((e=>{i.value[r][e]=d[e]}))},multipleSelection:[],dialogImageUrl:"",ppVisible:!1}},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"},methods:{handleRemove(e,l){console.log(e,l)},handlePictureCardPreview(e){this.dialogImageUrl=e.url,this.ppVisible=!0},handleSelectionChange(e){this.selectedData=e},handleDeleteAll(){var e=this,l=this.selectedData;l?(l.forEach((function(l,a){e.tableData.forEach((function(a,t){l===a&&e.tableData.splice(t,1)}))})),a.success("删除成功"),this.$refs.multipleTable.clearSelection()):a.warning("请选择一条记录")}}},w={class:"container"},V={class:"handle-box"},_={class:"left-panel"},y=o("div",{class:"clear"},null,-1),C={class:"pagination"},k={class:"dialog-footer"},z=["src"];var x=b(v,[["render",function(e,l,a,h,b,f){const g=t("el-input"),v=t("el-form-item"),x=t("el-option"),U=t("el-select"),D=t("el-button"),I=t("el-form"),S=t("el-table-column"),E=t("el-image"),q=t("el-tag"),j=t("el-table"),T=t("el-pagination"),H=t("el-dialog");return i(),n("div",null,[o("div",w,[o("div",V,[o("div",_,[s(I,{inline:"","label-width":"80"},{default:d((()=>[s(v,{label:""},{default:d((()=>[s(g,{size:"small",modelValue:h.query.name,"onUpdate:modelValue":l[0]||(l[0]=e=>h.query.name=e),class:"handle-input",placeholder:"输入查询内容"},null,8,["modelValue"])])),_:1}),s(v,{label:""},{default:d((()=>[s(U,{modelValue:h.form.selvalue,"onUpdate:modelValue":l[1]||(l[1]=e=>h.form.selvalue=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100"},{default:d((()=>[(i(!0),n(r,null,u(h.form.czroptions,(e=>(i(),c(x,{key:e.value,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),s(v,{label:""},{default:d((()=>[s(U,{modelValue:h.form.selvalue,"onUpdate:modelValue":l[2]||(l[2]=e=>h.form.selvalue=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100"},{default:d((()=>[(i(!0),n(r,null,u(h.form.czroptions,(e=>(i(),c(x,{key:e.value,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),s(v,{label:""},{default:d((()=>[s(U,{modelValue:h.form.selvalue,"onUpdate:modelValue":l[3]||(l[3]=e=>h.form.selvalue=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100"},{default:d((()=>[(i(!0),n(r,null,u(h.form.czroptions,(e=>(i(),c(x,{key:e.value,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),s(v,{label:""},{default:d((()=>[s(D,{size:"small",type:"primary",icon:"el-icon-search",onClick:h.handleSearch},{default:d((()=>[m("查询")])),_:1},8,["onClick"])])),_:1})])),_:1})]),y]),s(j,{data:h.tableData,border:"",class:"table",ref:"multipleTable","max-height":b.tableH,"header-cell-class-name":"table-header",onSelectionChange:f.handleSelectionChange},{default:d((()=>[s(S,{pro:"ID",label:"ID",width:"55",align:"center"},{default:d((e=>[m(p(e.row.id),1)])),_:1}),s(S,{prop:"name",label:"车牌号",width:"130"}),s(S,{prop:"name",label:"申诉人手机",width:"130"}),s(S,{label:"报警图片",align:"center",width:"180"},{default:d((e=>[s(E,{class:"table-td-thumb","hide-on-click-modal":"true","preview-teleported":"true",src:e.row.thumb,"preview-src-list":[e.row.thumb]},null,8,["src","preview-src-list"])])),_:1}),s(S,{label:"驾驶证",align:"center",width:"180"},{default:d((e=>[s(E,{class:"table-td-thumb","hide-on-click-modal":"true","preview-teleported":"true",src:e.row.thumb,"preview-src-list":[e.row.thumb]},null,8,["src","preview-src-list"])])),_:1}),s(S,{prop:"name",label:"备注"}),s(S,{label:"申诉时间",prop:"addtimes",width:"180",align:"center"}),s(S,{label:"启用",align:"center",width:"100"},{default:d((e=>[s(q,{size:"small",type:"启用"===e.row.state?"success":"禁用"===e.row.state?"danger":""},{default:d((()=>[m(p(e.row.state),1)])),_:2},1032,["type"])])),_:1}),s(S,{label:"审核人",prop:"name",width:"120",align:"center"})])),_:1},8,["data","max-height","onSelectionChange"]),o("div",C,[s(T,{background:"",layout:"total, prev, pager, next","current-page":h.query.pageIndex,"page-size":h.query.pageSize,total:h.pageTotal,onCurrentChange:h.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])]),s(H,{title:"填写拒绝原因",modelValue:h.editVisible,"onUpdate:modelValue":l[6]||(l[6]=e=>h.editVisible=e),width:"35%"},{footer:d((()=>[o("span",k,[s(D,{onClick:l[5]||(l[5]=e=>h.editVisible=!1)},{default:d((()=>[m("取 消")])),_:1}),s(D,{type:"primary",onClick:h.saveEdit},{default:d((()=>[m("确 定")])),_:1},8,["onClick"])])])),default:d((()=>[s(I,{"label-width":"70px",size:"small"},{default:d((()=>[s(g,{modelValue:h.form.name,"onUpdate:modelValue":l[4]||(l[4]=e=>h.form.name=e),type:"textarea",rows:6},null,8,["modelValue"])])),_:1})])),_:1},8,["modelValue"]),s(H,{modelValue:h.ppVisible,"onUpdate:modelValue":l[7]||(l[7]=e=>h.ppVisible=e)},{default:d((()=>[o("img",{width:"100%",src:h.dialogImageUrl,alt:""},null,8,z)])),_:1},8,["modelValue"])])}]]);export{x as default};
