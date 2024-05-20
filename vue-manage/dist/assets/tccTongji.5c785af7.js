import{f as e,b as a,r as t,o as l,h as r,i as n,k as d,l as o,w as s,v as i,F as u,j as p,c,m as g,p as m,q as h}from"./vendor.ac52fd94.js";import{_ as b,a8 as f,a5 as k,a6 as I,l as y,a9 as v}from"./index.1d57ac4a.js";const _={name:"roadTongji",data:()=>({tableH:0}),setup(){const t=e({time:"",startTime:"",endTime:"",areaId:"",streetId:"",parkId:"",pageIndex:1,pageSize:15}),l=a([]),r=a(0),n=a(0),d=a(0);let o=e({time:"",areaId:"",streetId:"",parkId:"",areas:[],streets:[],parks:[]});(()=>{var e=new Date,a=e.getMonth(),l=e.getFullYear(),r=new Date(l,a,1),n=new Date(l,a+1,0);o.time=[r,n];var d=r.getDate(),s=r.getMonth()+1;s<10&&(s="0"+s),d<10&&(d="0"+d),t.startTime=r.getFullYear().toString()+"-"+s.toString()+"-"+d.toString()})();var s,i,u,p,c,g;s=new Date,i=s.getMonth(),u=s.getFullYear(),p=new Date(u,i+1,0),c=p.getDate(),(g=p.getMonth()+1)<10&&(g="0"+g),c<10&&(c="0"+c),t.endTime=p.getFullYear().toString()+"-"+g.toString()+"-"+c.toString();const m=()=>{if(""==t.startTime||""==t.endTime)return h.error("请选择日期"),!1;f(t).then((e=>{l.value=e.data.list,r.value=e.data.page.total,n.value=e.data.user_type,d.value=e.data.model}))};m();const b=e({}),_=()=>{k(b).then((e=>{o.areas=e.data}))};_();let C=e({areaId:t.areaId});const T=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`;return{query:t,tableData:l,pageTotal:r,form:o,queryArea:b,queryStreet:C,userType:n,model:d,getArea:_,getStreet:()=>{""==t.areaId?(o.streets=[],o.parks=[],t.streetId="",t.parkId=""):(t.streetId="",t.parkId="",o.parks=[],C.areaId=t.areaId,I(C).then((e=>{o.streets=e.data})))},getPark:()=>{""==t.streetId?(o.parks=[],t.parkId=""):(o.parks=[],t.parkId="",y(e({streetId:t.streetId})).then((e=>{o.parks=e.data})))},getQueryDate:()=>{null==o.time||[]==o.time||""==o.time?(t.startTime="",t.endTime=""):(t.startTime=T(o.time[0]),t.endTime=T(o.time[1]))},handleSearch:()=>{t.pageIndex=1,m()},handlePageChange:e=>{t.pageIndex=e,m()},exportExcel:()=>{v(t).then((e=>{const a=window.URL.createObjectURL(new Blob([e])),t=document.createElement("a");t.href=a,t.setAttribute("download","导出报表.xls"),document.body.appendChild(t),t.click()}))}}},methods:{},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"}},C={class:"container"},T={class:"top-panel"},x=n("span",{class:"mar5 color999"},null,-1),w=n("span",{class:"mar5 color999"},null,-1),D={class:"pagination"};var S=b(_,[["render",function(e,a,h,b,f,k){const I=t("el-date-picker"),y=t("el-form-item"),v=t("el-option"),_=t("el-select"),S=t("el-button"),q=t("el-form"),V=t("el-col"),z=t("el-row"),M=t("el-table-column"),F=t("el-table"),U=t("el-pagination");return l(),r("div",null,[n("div",C,[d(z,null,{default:o((()=>[d(V,{span:24},{default:o((()=>[n("div",T,[d(q,{inline:"",size:"small"},{default:o((()=>[d(y,{label:"时间"},{default:o((()=>[d(I,{size:"small",modelValue:b.form.time,"onUpdate:modelValue":a[0]||(a[0]=e=>b.form.time=e),type:"daterange",clearable:"false","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:b.getQueryDate},null,8,["modelValue","onChange"])])),_:1}),s(d(y,{label:"选择层级"},{default:o((()=>[d(_,{clearable:"",modelValue:b.query.areaId,"onUpdate:modelValue":a[1]||(a[1]=e=>b.query.areaId=e),filterable:"",placeholder:"所有区域",class:"w100",onChange:b.getStreet},{default:o((()=>[(l(!0),r(u,null,p(b.form.areas,(e=>(l(),c(v,{key:e.id,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),x,d(_,{clearable:"",modelValue:b.query.streetId,"onUpdate:modelValue":a[2]||(a[2]=e=>b.query.streetId=e),filterable:"",placeholder:"所有街道",class:"w100",onChange:b.getPark},{default:o((()=>[(l(!0),r(u,null,p(b.form.streets,(e=>(l(),c(v,{key:e.id,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),w,d(_,{clearable:"",modelValue:b.query.parkId,"onUpdate:modelValue":a[3]||(a[3]=e=>b.query.parkId=e),filterable:"",placeholder:"所有停车场",class:"w100"},{default:o((()=>[(l(!0),r(u,null,p(b.form.parks,(e=>(l(),c(v,{key:e.id,label:e.park_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1},512),[[i,"0"==b.userType]]),d(y,null,{default:o((()=>[d(S,{size:"small",type:"primary",icon:"el-icon-search",onClick:b.handleSearch},{default:o((()=>[g("查询")])),_:1},8,["onClick"])])),_:1}),d(y,null,{default:o((()=>[d(S,{size:"small",type:"success",icon:"el-icon-upload2",onClick:b.exportExcel},{default:o((()=>[g("导出")])),_:1},8,["onClick"])])),_:1})])),_:1})])])),_:1})])),_:1}),d(F,{data:b.tableData,border:"",class:"table","max-height":f.tableH,ref:"multipleTable","header-cell-class-name":"table-header"},{default:o((()=>[d(M,{type:"index",label:"序号",width:"55",align:"center"}),"0"==b.userType?(l(),c(M,{key:0,prop:"area",label:"区域",align:"center"})):m("",!0),d(M,{prop:"roadName",label:"停车场",align:"center"}),d(M,{prop:"timeLong",label:"停车总时长",align:"center"}),d(M,{prop:"utilizationRate",label:"利用率",align:"center"}),d(M,{prop:"turnoverRate",label:"周转率",align:"center"}),d(M,{prop:"turnoverAverRate",label:"周转均比",align:"center"})])),_:1},8,["data","max-height"]),n("div",D,[d(U,{background:"",layout:"total, prev, pager, next","current-page":b.query.pageIndex,"page-size":b.query.pageSize,total:b.pageTotal,onCurrentChange:b.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])])])}]]);export{S as default};
