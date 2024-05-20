import{f as e,b as t,L as a,q as l,r as i,M as n,o as r,h as d,i as o,w as s,c,l as u,m,k as h,I as p,t as g,p as _,v as y,F as f,j as v}from"./vendor.ac52fd94.js";import{_ as b,V as w,W as V,X as C,Y as k,Z as x,$ as z}from"./index.1d57ac4a.js";const D={name:"userList",data:()=>({tableH:0}),setup(){const i=e({address:"",name:"",pageIndex:1,pageSize:15}),n=t([]),r=t(0),d=()=>{V(i).then((e=>{n.value=e.data.list,r.value=e.data.total}))};d();const o=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`,s=t(!1),c=t(!0);let u=e({activity_name:"",start_time:"",end_time:"",sort:"",type:"",showtime:""});const m=t([{recharge_amount:"",additional_amount:""}]);let h="";return{query:i,tableData:n,formArr:m,pageTotal:r,editVisible:s,form:u,dialogT:"新增",formRules:{activity_name:[{required:!0,message:"请输入活动名称",trigger:"blur"}],start_time:[{required:!0,message:"请选择开始时间",trigger:"blur"}],end_time:[{required:!0,message:"请选择结束时间",trigger:"blur"}],sort:[{required:!0,message:"请输入排序",trigger:["blur","change"]}]},getData:d,getEndDate:()=>{u.end_time=o(u.end_time)},getStartDate:()=>{u.start_time=o(u.start_time)},handleSearch:()=>{i.pageIndex=1,d()},handlePageChange:e=>{i.pageIndex=e,d()},handleqiyong:(t,i)=>{a.confirm("确定要启用吗？","提示",{type:"warning"}).then((()=>{let t=e({status:"0",id:i.id});C(t).then((e=>{l.success("启用成功")})).then((()=>{d()}))})).catch((()=>{}))},handlejinyong:(t,i)=>{a.confirm("确定要禁用吗？","提示",{type:"warning"}).then((()=>{let t=e({status:"1",id:i.id});C(t).then((e=>{l.success("禁用成功")})).then((()=>{d()}))})).catch((()=>{}))},handleDelete:(t,i)=>{a.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{let t=e({is_del:"1",id:i.id});C(t).then((e=>{l.success("删除成功")})).then((()=>{d()}))})).catch((()=>{}))},handleEdit:(t,a,l)=>{l?(h="",u.activity_name="",u.start_time="",u.end_time="",u.sort="",u.type=1,u.showtime=!1,m.value=[{recharge_amount:"",additional_amount:""}]):(h=a.id,k(e({id:a.id})).then((e=>{u.activity_name=e.data.activity_name,u.sort=e.data.sort,u.type=parseInt(e.data.type),2==u.type?(u.showtime=!0,u.start_time=e.data.start_time,u.end_time=e.data.end_time):(u.showtime=!1,u.start_time="",u.end_time=""),m.value=e.data.rechargeActivityConfigs}))),s.value=!0,c.value=!1},saveEdit:()=>{if(c.value=!0,setTimeout((()=>{c.value=!1}),2e3),1==u.type){if(null==u.activity_name||""==u.activity_name||null==u.sort||""==u.sort)return l.error("参数不可为空！"),c.value=!1,!1}else if(null==u.activity_name||""==u.activity_name||null==u.start_time||""==u.start_time||null==u.end_time||""==u.end_time||null==u.sort||""==u.sort)return l.error("参数不可为空！"),c.value=!1,!1;var t=!0;if(m.value.forEach((function(e){null!=e.recharge_amount&&""!=e.recharge_amount&&null!=e.additional_amount&&""!=e.additional_amount||(t=!1)})),!t)return l.error("赠送规则不符合规则！"),c.value=!1,!1;if(2==u.type&&u.start_time>u.end_time)return l.error("充值活动时间不符合规则！"),c.value=!1,!1;let a;s.value=!1,a=1==u.type?{id:h,activity_name:u.activity_name,sort:u.sort,type:u.type,rechargeActivityConfigs:m.value}:{id:h,activity_name:u.activity_name,start_time:u.start_time,end_time:u.end_time,sort:u.sort,type:u.type,rechargeActivityConfigs:m.value},h?z(e({rechargeActivity:JSON.stringify(a)})).then((e=>{0==e.code?l.success(e.data):l.error(e.msg),c.value=!1})).then((()=>{i.pageIndex=1,d()})):x(e({rechargeActivity:JSON.stringify(a)})).then((e=>{0==e.code?l.success(e.data):l.error(e.msg),c.value=!0})).then((()=>{i.pageIndex=1,d()}))},changeHandler:e=>{1==e&&(u.showtime=!1),2==e&&(u.showtime=!0)},multipleSelection:[],value:!0,activeName:"first",saveVisible:c}},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"},methods:{handleSelectionChange(e){this.selectedData=e},handleDeleteAll(){a.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{var e=this,t=this.selectedData,a="";t?(t.forEach((function(e,t){a=a+e.id+","})),w({ids:a}).then((t=>{l.success("删除成功"),e.getData()}))):l.warning("请选择一条记录")})).catch((()=>{}))},addItem(){this.formArr.push({id:"",recharge_amount:"",additional_amount:""})},delItem(e){1==this.formArr.length?l.warning("至少保留一条赠送规则"):this.formArr.splice(e,1)}}},A={class:"container"},S={class:"handle-box"},q={class:"left-panel"},I={class:"right-panel"},U=o("div",{class:"clear"},null,-1),E={class:"pagination"},$=o("div",{class:"mt20"},null,-1),H={class:"w desctable"},T=o("colgroup",null,[o("col"),o("col"),o("col",{class:"w100"})],-1),j=o("th",null,"充值金额",-1),N=o("th",null,"赠送金额",-1),M={align:"center"},F={class:"dialog-footer"};var J=b(D,[["render",function(e,t,a,l,b,w){const V=i("el-button"),C=i("el-input"),k=i("el-table-column"),x=i("el-tag"),z=i("el-table"),D=i("el-pagination"),J=i("el-radio"),L=i("el-radio-group"),O=i("el-form-item"),P=i("el-date-picker"),R=i("el-lable"),Y=i("el-form"),K=i("el-tab-pane"),W=i("el-tabs"),X=i("el-dialog"),Z=n("permission");return r(),d("div",null,[o("div",A,[o("div",S,[o("div",q,[s((r(),c(V,{type:"primary",size:"small",icon:"el-icon-plus",onClick:t[0]||(t[0]=e=>l.handleEdit(0,null,!0))},{default:u((()=>[m("添加")])),_:1})),[[Z,["road_recharge_add","park_recharge_add"]]]),s((r(),c(V,{type:"danger",size:"small",icon:"el-icon-delete",onClick:t[1]||(t[1]=e=>w.handleDeleteAll())},{default:u((()=>[m("批量删除")])),_:1})),[[Z,["road_recharge_deleteAll","park_recharge_deleteAll"]]])]),o("div",I,[h(C,{onKeyup:t[2]||(t[2]=p((e=>l.handleSearch()),["enter"])),size:"small",modelValue:l.query.activity_name,"onUpdate:modelValue":t[3]||(t[3]=e=>l.query.activity_name=e),placeholder:"活动名称",class:"handle-input mr10"},null,8,["modelValue"]),h(V,{size:"small",type:"primary",icon:"el-icon-search",onClick:l.handleSearch},{default:u((()=>[m("查询")])),_:1},8,["onClick"])]),U]),h(z,{data:l.tableData,border:"",class:"table",ref:"multipleTable","max-height":b.tableH,"header-cell-class-name":"table-header",onSelectionChange:w.handleSelectionChange},{default:u((()=>[h(k,{type:"selection",width:"55",align:"center"}),h(k,{label:"序号",width:"55",align:"center"},{default:u((e=>[m(g(e.$index+1),1)])),_:1}),h(k,{prop:"activity_name",label:"活动名称",align:"center"}),h(k,{prop:"start_time",label:"开始时间",align:"center",width:"180"}),h(k,{prop:"end_time",label:"结束时间",align:"center",width:"180"}),h(k,{label:"活动类型",align:"center",width:"100"},{default:u((e=>["1"==e.row.type?(r(),c(x,{key:0,size:"small"},{default:u((()=>[m("普通充值 ")])),_:1})):_("",!0),"2"==e.row.type?(r(),c(x,{key:1,size:"small"},{default:u((()=>[m("活动充值 ")])),_:1})):_("",!0)])),_:1}),h(k,{label:"启用",align:"center",width:"100"},{default:u((e=>[0==e.row.status?(r(),c(x,{key:0,size:"small",type:"0"===e.row.status?"success":"1"===e.row.status?"danger":""},{default:u((()=>[m("启用 ")])),_:2},1032,["type"])):(r(),c(x,{key:1,size:"small",type:"0"===e.row.status?"success":"1"===e.row.status?"danger":""},{default:u((()=>[m("禁用 ")])),_:2},1032,["type"]))])),_:1}),h(k,{prop:"sort",label:"排序",align:"center",width:"170"}),h(k,{prop:"create_time",label:"创建时间",align:"center",width:"170"}),h(k,{label:"操作",width:"180",align:"center"},{default:u((e=>[s((r(),c(V,{size:"mini",type:"text",icon:"el-icon-edit",onClick:t=>l.handleEdit(e.$index,e.row,!1)},{default:u((()=>[m("编辑 ")])),_:2},1032,["onClick"])),[[Z,["road_recharge_edit","park_recharge_edit"]]]),0==e.row.status?s((r(),c(V,{key:0,size:"mini",type:"text",icon:"el-icon-circle-close",class:"red",onClick:t=>l.handlejinyong(e.$index,e.row)},{default:u((()=>[m(" 禁用 ")])),_:2},1032,["onClick"])),[[Z,["road_recharge_status","park_recharge_status"]]]):s((r(),c(V,{key:1,size:"mini",type:"text",icon:"el-icon-circle-check",onClick:t=>l.handleqiyong(e.$index,e.row)},{default:u((()=>[m(" 启用 ")])),_:2},1032,["onClick"])),[[Z,["road_recharge_status","park_recharge_status"]]]),s((r(),c(V,{size:"mini",type:"text",icon:"el-icon-delete",class:"red",onClick:t=>l.handleDelete(e.$index,e.row)},{default:u((()=>[m("删除")])),_:2},1032,["onClick"])),[[Z,["road_recharge_delete","park_recharge_delete"]]])])),_:1})])),_:1},8,["data","max-height","onSelectionChange"]),o("div",E,[h(D,{background:"",layout:"total, prev, pager, next","current-page":l.query.pageIndex,"page-size":l.query.pageSize,total:l.pageTotal,onCurrentChange:l.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])]),h(X,{title:l.dialogT,modelValue:l.editVisible,"onUpdate:modelValue":t[11]||(t[11]=e=>l.editVisible=e),width:"30%"},{footer:u((()=>[o("span",F,[h(V,{onClick:t[10]||(t[10]=e=>l.editVisible=!1)},{default:u((()=>[m("取 消")])),_:1}),h(V,{type:"primary",onClick:l.saveEdit,disabled:l.saveVisible},{default:u((()=>[m("确 定")])),_:1},8,["onClick","disabled"])])])),default:u((()=>[h(W,{type:"card",modelValue:l.activeName,"onUpdate:modelValue":t[9]||(t[9]=e=>l.activeName=e)},{default:u((()=>[h(K,{label:"活动信息",name:"first"},{default:u((()=>[$,h(Y,{"label-width":"80px",rules:l.formRules,model:l.form},{default:u((()=>[h(O,{prop:"type",label:"活动类型"},{default:u((()=>[h(L,{modelValue:l.form.type,"onUpdate:modelValue":t[4]||(t[4]=e=>l.form.type=e),onChange:l.changeHandler},{default:u((()=>[h(J,{label:1},{default:u((()=>[m("普通充值")])),_:1}),h(J,{label:2},{default:u((()=>[m("活动充值")])),_:1})])),_:1},8,["modelValue","onChange"])])),_:1}),h(O,{label:"活动名称",prop:"activity_name"},{default:u((()=>[h(C,{modelValue:l.form.activity_name,"onUpdate:modelValue":t[5]||(t[5]=e=>l.form.activity_name=e),placeholder:"请输入活动名称"},null,8,["modelValue"])])),_:1}),s(h(O,{label:"开始时间",prop:"start_time"},{default:u((()=>[h(P,{modelValue:l.form.start_time,"onUpdate:modelValue":t[6]||(t[6]=e=>l.form.start_time=e),placeholder:"请输入开始时间",style:{width:"100%"},onChange:l.getStartDate},null,8,["modelValue","onChange"])])),_:1},512),[[y,l.form.showtime]]),s(h(O,{label:"结束时间",prop:"end_time"},{default:u((()=>[h(P,{modelValue:l.form.end_time,"onUpdate:modelValue":t[7]||(t[7]=e=>l.form.end_time=e),placeholder:"请输入结束时间",style:{width:"100%"},onChange:l.getEndDate},null,8,["modelValue","onChange"])])),_:1},512),[[y,l.form.showtime]]),h(O,{label:"排序",prop:"sort"},{default:u((()=>[h(C,{type:"number",placeholder:"请输入排序",modelValue:l.form.sort,"onUpdate:modelValue":t[8]||(t[8]=e=>l.form.sort=e)},null,8,["modelValue"]),h(R,{style:{color:"red"}},{default:u((()=>[m("*按排序号，从小到大排列")])),_:1})])),_:1})])),_:1},8,["rules","model"])])),_:1}),h(K,{label:"赠送规则",name:"second"},{default:u((()=>[o("table",H,[T,o("thead",null,[j,N,o("th",null,[h(V,{round:"",size:"mini",type:"primary",icon:"el-icon-plus",onClick:w.addItem},null,8,["onClick"])])]),o("tbody",null,[(r(!0),d(f,null,v(l.formArr,((e,t)=>(r(),d("tr",{key:t},[o("td",null,[h(C,{type:"number",size:"mini",modelValue:e.recharge_amount,"onUpdate:modelValue":t=>e.recharge_amount=t},{append:u((()=>[m("元")])),_:2},1032,["modelValue","onUpdate:modelValue"])]),o("td",null,[h(C,{type:"number",size:"mini",modelValue:e.additional_amount,"onUpdate:modelValue":t=>e.additional_amount=t},{append:u((()=>[m("元")])),_:2},1032,["modelValue","onUpdate:modelValue"])]),o("td",M,[h(V,{round:"",size:"mini",type:"danger",icon:"el-icon-delete",onClick:e=>w.delItem(t)},null,8,["onClick"])])])))),128))])])])),_:1})])),_:1},8,["modelValue"])])),_:1},8,["title","modelValue"])])}]]);export{J as default};