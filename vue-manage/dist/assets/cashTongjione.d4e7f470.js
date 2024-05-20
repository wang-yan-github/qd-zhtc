import{f as e,b as a,r as l,o as t,h as r,i as n,k as o,l as d,m as s,F as i,j as c,c as u}from"./vendor.ac52fd94.js";import{_ as p,a5 as m,a6 as g,l as h,ai as b,aj as f}from"./index.1d57ac4a.js";const k={name:"orderTongji",data:()=>({tableH:0}),setup(){const l=e({areaId:"",streetId:"",roadId:"",startTime:"",endTime:"",pageIndex:1,pageSize:16}),t=a([]),r=a(0);let n=e({time:"",areaId:"",streetId:"",parkId:"",areas:[],streets:[],parks:[]});const o=e({}),d=()=>{m(o).then((e=>{n.areas=e.data}))};d();let s=e({areaId:l.areaId});const i=e({streetId:l.streetId}),c=()=>{f(l).then((e=>{t.value=e.data.list,r.value=e.data.total}))};c();const u=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`;return{query:l,tableData:t,form:n,pageTotal:r,getArea:d,getStreet:()=>{s.areaId=l.areaId,l.parkId="",l.streetId="",g(s).then((e=>{n.streets=e.data}))},getQueryDate:()=>{null==n.time||[]==n.time||""==n.time?(l.startTime="",l.endTime=""):(l.startTime=u(n.time[0]),l.endTime=u(n.time[1]))},getPark:()=>{l.parkId="",i.streetId=l.streetId,h(i).then((e=>{n.parks=e.data}))},handleSearch:()=>{l.pageIndex=1,c()},handlePageChange:e=>{l.pageIndex=e,c()},exportExcel:()=>{b(l).then((e=>{const a=window.URL.createObjectURL(new Blob([e])),l=document.createElement("a");l.href=a,l.setAttribute("download","收入报表.xls"),document.body.appendChild(l),l.click()}))}}},methods:{goTo(){this.$router.push("/cashstatictwo")},goTo2(){this.$router.push("/cashstaticthree")},goTo3(){this.$router.push("/cashstaticfour")}},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"}},I={class:"container"},y={class:"top-panel f-cb"},C={class:"left-panel"},_={class:"right-panel"},v=n("span",{class:"mar5 color999"},null,-1),x=n("span",{class:"mar5 color999"},null,-1),w=n("div",{class:"clear"},null,-1),T={class:"pagination"};var V=p(k,[["render",function(e,a,p,m,g,h){const b=l("el-button"),f=l("el-button-group"),k=l("el-date-picker"),V=l("el-form-item"),q=l("el-option"),S=l("el-select"),z=l("el-form"),D=l("el-table-column"),j=l("el-table"),U=l("el-pagination"),$=l("el-card"),H=l("el-col"),E=l("el-row");return t(),r("div",null,[n("div",I,[n("div",y,[n("div",C,[o(f,null,{default:d((()=>[o(b,{type:"primary",icon:"el-icon-s-grid"},{default:d((()=>[s("收入报表")])),_:1}),o(b,{icon:"el-icon-s-marketing",onClick:a[0]||(a[0]=e=>h.goTo())},{default:d((()=>[s("充值增长")])),_:1}),o(b,{icon:"el-icon-money",onClick:a[1]||(a[1]=e=>h.goTo2())},{default:d((()=>[s("缴费方式统计")])),_:1}),o(b,{icon:"el-icon-s-custom",onClick:a[2]||(a[2]=e=>h.goTo3())},{default:d((()=>[s("免单统计")])),_:1})])),_:1})]),n("div",_,[o(z,{"label-width":"80px",inline:"",size:"small"},{default:d((()=>[o(V,{label:"时间",class:"mb0"},{default:d((()=>[o(k,{size:"small",modelValue:m.form.time,"onUpdate:modelValue":a[3]||(a[3]=e=>m.form.time=e),type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:m.getQueryDate},null,8,["modelValue","onChange"])])),_:1}),o(V,{label:"选择层级"},{default:d((()=>[o(S,{clearable:"",modelValue:m.query.areaId,"onUpdate:modelValue":a[4]||(a[4]=e=>m.query.areaId=e),filterable:"",placeholder:"所有区域",class:"w100",onChange:m.getStreet},{default:d((()=>[(t(!0),r(i,null,c(m.form.areas,(e=>(t(),u(q,{key:e.id,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),v,o(S,{clearable:"",modelValue:m.query.streetId,"onUpdate:modelValue":a[5]||(a[5]=e=>m.query.streetId=e),filterable:"",placeholder:"所有街道",class:"w100",onChange:m.getPark},{default:d((()=>[(t(!0),r(i,null,c(m.form.streets,(e=>(t(),u(q,{key:e.id,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),x,o(S,{clearable:"",modelValue:m.query.parkId,"onUpdate:modelValue":a[6]||(a[6]=e=>m.query.parkId=e),filterable:"",placeholder:"所有路内",class:"w100"},{default:d((()=>[(t(!0),r(i,null,c(m.form.parks,(e=>(t(),u(q,{key:e.id,label:e.park_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),o(V,{class:"mb0"},{default:d((()=>[o(b,{size:"small",type:"primary",icon:"el-icon-search",onClick:m.handleSearch},{default:d((()=>[s("查询")])),_:1},8,["onClick"])])),_:1}),o(V,{class:"mb0"},{default:d((()=>[o(b,{size:"small",type:"success",icon:"el-icon-upload2",onClick:m.exportExcel},{default:d((()=>[s("导出")])),_:1},8,["onClick"])])),_:1})])),_:1})]),w]),o(E,{gutter:20},{default:d((()=>[o(H,{span:24},{default:d((()=>[o($,{shadow:"hover"},{default:d((()=>[o(j,{data:m.tableData,border:"",class:"table","max-height":g.tableH,ref:"multipleTable","header-cell-class-name":"table-header",onSelectionChange:e.handleSelectionChange},{default:d((()=>[o(D,{type:"index",label:"序号",width:"55",align:"center"}),o(D,{prop:"areaName",label:"区域",width:"300",align:"center"}),o(D,{prop:"streetName",label:"街道",width:"300",align:"center"}),o(D,{prop:"roadName",label:"路内",align:"center"}),o(D,{prop:"receivable",label:"应收",width:"200",align:"center"}),o(D,{prop:"netReceipts",label:"实收",width:"200",align:"center"})])),_:1},8,["data","max-height","onSelectionChange"]),n("div",T,[o(U,{background:"",layout:"total, prev, pager, next","current-page":m.query.pageIndex,"page-size":m.query.pageSize,total:m.pageTotal,onCurrentChange:m.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])])),_:1})])),_:1})])),_:1})])])}]]);export{V as default};