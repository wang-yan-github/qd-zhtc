import{f as e,b as a,r as t,o as l,h as r,i as n,m as o,t as d,k as s,l as i,w as u,v as c,F as p,j as g,c as m}from"./vendor.ac52fd94.js";import{_ as h,aa as b,a5 as f,a6 as I,l as k,ab as y}from"./index.1d57ac4a.js";const C={name:"xunjianTongji",data:()=>({tableH:0}),setup(){const t=e({startTime:"",endTime:"",areaId:"",streetId:"",roadId:"",pageIndex:1,pageSize:15}),l=a([]),r=a(0),n=a(0),o=a(0);let d=e({time:"",areaId:"",streetId:"",roadId:"",areas:[],streets:[],roads:[]});(()=>{var e=new Date,a=e.getMonth(),l=e.getFullYear(),r=new Date(l,a,1),n=new Date(l,a+1,0);d.time=[r,n];var o=r.getDate(),s=r.getMonth()+1;s<10&&(s="0"+s),o<10&&(o="0"+o),t.startTime=r.getFullYear().toString()+"-"+s.toString()+"-"+o.toString()})();var s,i,u,c,p,g;s=new Date,i=s.getMonth(),u=s.getFullYear(),c=new Date(u,i+1,0),p=c.getDate(),(g=c.getMonth()+1)<10&&(g="0"+g),p<10&&(p="0"+p),t.endTime=c.getFullYear().toString()+"-"+g.toString()+"-"+p.toString();const m=()=>{b(t).then((e=>{l.value=e.data.list,r.value=e.data.page.total}))};m();const h=e({}),C=()=>{f(h).then((e=>{d.areas=e.data}))};C();let v=e({areaId:t.areaId});const x=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`;return{query:t,tableData:l,form:d,pageTotal:r,queryArea:h,queryStreet:v,userType:n,model:o,getArea:C,getStreet:()=>{""==t.areaId?(d.streets=[],d.parks=[],t.streetId="",t.parkId=""):(v.areaId=t.areaId,t.parkId="",t.streetId="",d.parks=[],I(v).then((e=>{d.streets=e.data})))},getQueryDate:()=>{null==d.time||[]==d.time||""==d.time?(t.startTime="",t.endTime=""):(t.startTime=x(d.time[0]),t.endTime=x(d.time[1]))},getPark:()=>{""==t.streetId?(d.parks=[],t.parkId=""):(d.parks=[],t.parkId="",k(e({streetId:t.streetId})).then((e=>{d.parks=e.data})))},handleSearch:()=>{t.pageIndex=1,m()},handlePageChange:e=>{t.pageIndex=e,m()},exportExcel:()=>{y(t).then((e=>{const a=window.URL.createObjectURL(new Blob([e])),t=document.createElement("a");t.href=a,t.setAttribute("download","导出收费员统计报表.xls"),document.body.appendChild(t),t.click()}))}}},methods:{},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"}},v={class:"container"},x={class:"top-panel"},S=n("span",{class:"mar5 color999"},null,-1),_=n("span",{class:"mar5 color999"},null,-1),w={class:"pagination"};var D=h(C,[["render",function(e,a,h,b,f,I){const k=t("el-date-picker"),y=t("el-form-item"),C=t("el-option"),D=t("el-select"),T=t("el-button"),q=t("el-form"),V=t("el-table-column"),z=t("el-table"),j=t("el-pagination");return l(),r("div",null,[n("div",v,[n("div",x,[o(d(b.form.date1)+" ",1),s(q,{inline:"",size:"small"},{default:i((()=>[s(y,{label:"时间"},{default:i((()=>[s(k,{size:"small",modelValue:b.form.time,"onUpdate:modelValue":a[0]||(a[0]=e=>b.form.time=e),type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:b.getQueryDate},null,8,["modelValue","onChange"])])),_:1}),u(s(y,{label:"选择层级"},{default:i((()=>[s(D,{clearable:"",modelValue:b.query.areaId,"onUpdate:modelValue":a[1]||(a[1]=e=>b.query.areaId=e),filterable:"",placeholder:"所有区域",class:"w100",onChange:b.getStreet},{default:i((()=>[(l(!0),r(p,null,g(b.form.areas,(e=>(l(),m(C,{key:e.id,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),S,s(D,{clearable:"",modelValue:b.query.streetId,"onUpdate:modelValue":a[2]||(a[2]=e=>b.query.streetId=e),filterable:"",placeholder:"所有街道",class:"w100",onChange:b.getPark},{default:i((()=>[(l(!0),r(p,null,g(b.form.streets,(e=>(l(),m(C,{key:e.id,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),_,s(D,{clearable:"",modelValue:b.query.parkId,"onUpdate:modelValue":a[3]||(a[3]=e=>b.query.parkId=e),filterable:"",placeholder:"所有停车场",class:"w100"},{default:i((()=>[(l(!0),r(p,null,g(b.form.parks,(e=>(l(),m(C,{key:e.id,label:e.park_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1},512),[[c,"0"==b.userType]]),s(y,null,{default:i((()=>[s(T,{size:"small",type:"primary",icon:"el-icon-search",onClick:b.handleSearch},{default:i((()=>[o("查询")])),_:1},8,["onClick"])])),_:1}),s(y,null,{default:i((()=>[s(T,{size:"small",type:"success",icon:"el-icon-upload2",onClick:b.exportExcel},{default:i((()=>[o("导出")])),_:1},8,["onClick"])])),_:1})])),_:1})]),s(z,{data:b.tableData,border:"",class:"table","max-height":f.tableH,ref:"multipleTable","header-cell-class-name":"table-header",onSelectionChange:e.handleSelectionChange},{default:i((()=>[s(V,{type:"index",label:"序号",width:"55",align:"center"}),s(V,{prop:"jobNo",label:"巡检工号",align:"center"}),s(V,{prop:"name",label:"巡检姓名",align:"center"}),s(V,{prop:"roadCount",label:"停车场数量",align:"center"}),s(V,{prop:"deviceCount",label:"泊位数量",align:"center"}),s(V,{prop:"orderCount",label:"创建订单数",align:"center"}),s(V,{prop:"cash",label:"现金收入",align:"center"}),s(V,{prop:"qr",label:"扫码收入",align:"center"})])),_:1},8,["data","max-height","onSelectionChange"]),n("div",w,[s(j,{background:"",layout:"total, prev, pager, next","current-page":b.query.pageIndex,"page-size":b.query.pageSize,total:b.pageTotal,onCurrentChange:b.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])])])}]]);export{D as default};