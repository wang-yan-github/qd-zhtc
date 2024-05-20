import{f as e,b as a,r as l,o as t,h as r,i as n,k as d,l as o,m as s,c as u,F as i,j as c,p as m}from"./vendor.ac52fd94.js";import{_ as p,a5 as g,a6 as h,U as b,l as y,ac as f,ad as I}from"./index.1d57ac4a.js";const k={name:"orderTongji",data:()=>({tableH:0}),setup(){const l=localStorage.getItem("menuType"),t=e({time:"",areaId:"",streetId:"",roadId:"",parkId:"",startTime:"",endTime:"",pageIndex:1,pageSize:16}),r=a([]),n=a(0);let d=e({time:"",areaId:"",streetId:"",roadId:"",areas:[],streets:[],roads:[]});const o=e({}),s=()=>{g(o).then((e=>{d.areas=e.data}))};s();let u=e({areaId:t.areaId});const i=e({streetId:t.streetId}),c=()=>{f(t).then((e=>{r.value=e.data.list,n.value=e.data.total}))};c();const m=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`;return{query:t,tableData:r,form:d,pageTotal:n,queryArea:o,queryStreet:u,queryRoad:i,menuType:l,getArea:s,getStreet:()=>{u.areaId=t.areaId,t.roadId="",t.streetId="",h(u).then((e=>{d.streets=e.data}))},getQueryDate:()=>{null==d.time||[]==d.time||""==d.time?(t.startTime="",t.endTime=""):(t.startTime=m(d.time[0]),t.endTime=m(d.time[1]))},getRoad:()=>{t.roadId="",i.streetId=t.streetId,b(i).then((e=>{d.roads=e.data}))},getParks:()=>{i.streetId=t.streetId,t.parkId="",y(i).then((e=>{d.roads=e.data}))},handleSearch:()=>{t.pageIndex=1,c()},handlePageChange:e=>{t.pageIndex=e,c()},exportExcel:()=>{I(t).then((e=>{const a=window.URL.createObjectURL(new Blob([e])),l=document.createElement("a");l.href=a,l.setAttribute("download","订单统计.xls"),document.body.appendChild(l),l.click()}))}}},methods:{goTo(){this.$router.push("/orderstatictwo")},goTo2(){this.$router.push("/orderstaticthree")}},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"}},C={class:"container"},_={class:"top-panel"},v={class:"left-panel"},V={class:"right-panel"},T=n("span",{class:"mar5 color999"},null,-1),q=n("span",{class:"mar5 color999"},null,-1),w=n("span",{class:"mar5 color999"},null,-1),x=n("span",{class:"mar5 color999"},null,-1),S=n("div",{class:"clear"},null,-1),U={class:"pagination"};var z=p(k,[["render",function(e,a,p,g,h,b){const y=l("el-button"),f=l("el-button-group"),I=l("el-date-picker"),k=l("el-form-item"),z=l("el-option"),D=l("el-select"),j=l("el-form"),H=l("el-table-column"),P=l("el-table"),R=l("el-pagination");return t(),r("div",null,[n("div",C,[n("div",_,[n("div",v,[d(f,null,{default:o((()=>[d(y,{type:"primary",icon:"el-icon-s-grid"},{default:o((()=>[s("基础数据 ")])),_:1}),d(y,{icon:"el-icon-s-marketing",onClick:a[0]||(a[0]=e=>b.goTo())},{default:o((()=>[s("订单增长 ")])),_:1}),d(y,{icon:"el-icon-s-order",onClick:a[1]||(a[1]=e=>b.goTo2())},{default:o((()=>[s("订单状态占比 ")])),_:1})])),_:1})]),n("div",V,[d(j,{"label-width":"80px",inline:"",size:"small"},{default:o((()=>[d(k,{label:"停入时间"},{default:o((()=>[d(I,{size:"small",modelValue:g.form.time,"onUpdate:modelValue":a[2]||(a[2]=e=>g.form.time=e),type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:g.getQueryDate},null,8,["modelValue","onChange"])])),_:1}),"0"==g.menuType?(t(),u(k,{key:0,label:"选择层级"},{default:o((()=>[d(D,{clearable:"",modelValue:g.query.areaId,"onUpdate:modelValue":a[3]||(a[3]=e=>g.query.areaId=e),filterable:"",placeholder:"所有区域",class:"w100",onChange:g.getStreet},{default:o((()=>[(t(!0),r(i,null,c(g.form.areas,(e=>(t(),u(z,{key:e.id,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),T,d(D,{clearable:"",modelValue:g.query.streetId,"onUpdate:modelValue":a[4]||(a[4]=e=>g.query.streetId=e),filterable:"",placeholder:"所有街道",class:"w100",onChange:g.getRoad},{default:o((()=>[(t(!0),r(i,null,c(g.form.streets,(e=>(t(),u(z,{key:e.id,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),q,d(D,{clearable:"",modelValue:g.query.roadId,"onUpdate:modelValue":a[5]||(a[5]=e=>g.query.roadId=e),filterable:"",placeholder:"所有路内",class:"w100"},{default:o((()=>[(t(!0),r(i,null,c(g.form.roads,(e=>(t(),u(z,{key:e.id,label:e.road_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1})):m("",!0),"1"==g.menuType?(t(),u(k,{key:1,label:"选择层级"},{default:o((()=>[d(D,{clearable:"",modelValue:g.query.areaId,"onUpdate:modelValue":a[6]||(a[6]=e=>g.query.areaId=e),filterable:"",placeholder:"所有区域",class:"w100",onChange:g.getStreet},{default:o((()=>[(t(!0),r(i,null,c(g.form.areas,(e=>(t(),u(z,{key:e.id,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),w,d(D,{clearable:"",modelValue:g.query.streetId,"onUpdate:modelValue":a[7]||(a[7]=e=>g.query.streetId=e),filterable:"",placeholder:"所有街道",class:"w100",onChange:g.getParks},{default:o((()=>[(t(!0),r(i,null,c(g.form.streets,(e=>(t(),u(z,{key:e.id,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),x,d(D,{clearable:"",modelValue:g.query.parkId,"onUpdate:modelValue":a[8]||(a[8]=e=>g.query.parkId=e),filterable:"",placeholder:"所有停车场",class:"w100"},{default:o((()=>[(t(!0),r(i,null,c(g.form.roads,(e=>(t(),u(z,{key:e.id,label:e.park_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1})):m("",!0),d(k,null,{default:o((()=>[d(y,{size:"small",type:"primary",icon:"el-icon-search",onClick:g.handleSearch},{default:o((()=>[s("查询 ")])),_:1},8,["onClick"])])),_:1}),d(k,null,{default:o((()=>[d(y,{size:"small",type:"success",icon:"el-icon-upload2",onClick:g.exportExcel},{default:o((()=>[s("导出 ")])),_:1},8,["onClick"])])),_:1})])),_:1})]),S]),d(P,{data:g.tableData,border:"",class:"table",ref:"multipleTable","max-height":h.tableH,"header-cell-class-name":"table-header",onSelectionChange:e.handleSelectionChange},{default:o((()=>[d(H,{type:"index",label:"序号",width:"55",align:"center"}),d(H,{prop:"cityName",label:"区域",width:"300",align:"center"}),d(H,{prop:"areaName",label:"街道",width:"300",align:"center"}),"1"==g.menuType?(t(),u(H,{key:0,prop:"roadName",align:"center",label:"停车场"})):m("",!0),"0"==g.menuType?(t(),u(H,{key:1,prop:"roadName",align:"center",label:"路内"})):m("",!0),d(H,{prop:"totalOrders",label:"订单总量",width:"200",align:"center"}),d(H,{prop:"totalPayment",label:"缴费总量",width:"200",align:"center"})])),_:1},8,["data","max-height","onSelectionChange"]),n("div",U,[d(R,{background:"",layout:"total, prev, pager, next","current-page":g.query.pageIndex,"page-size":g.query.pageSize,total:g.pageTotal,onCurrentChange:g.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])])])}]]);export{z as default};