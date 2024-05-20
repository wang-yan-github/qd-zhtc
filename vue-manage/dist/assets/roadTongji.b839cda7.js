import{f as e,b as a,r as t,o as l,h as r,i as n,k as d,l as o,F as s,j as i,c as u,m as c,t as g,w as m,v as p,q as h,L as b}from"./vendor.ac52fd94.js";import{_ as f,a8 as I,a5 as y,a6 as v,U as w,a9 as x}from"./index.1d57ac4a.js";const _={name:"roadTongji",data:()=>({tableH:0}),setup(){const t=e({time:"",startTime:"",endTime:"",areaId:"",streetId:"",roadId:"",pageIndex:1,pageSize:18}),l=a([]),r=a(0),n=a(0),d=a(0);let o=e({time:"",areaId:"",streetId:"",roadId:"",areas:[],streets:[],roads:[]});(()=>{var e=new Date,a=e.getMonth(),l=e.getFullYear(),r=new Date(l,a,1),n=new Date(l,a+1,0);o.time=[r,n];var d=r.getDate(),s=r.getMonth()+1;s<10&&(s="0"+s),d<10&&(d="0"+d),t.startTime=r.getFullYear().toString()+"-"+s.toString()+"-"+d.toString()})();var s,i,u,c,g,m;s=new Date,i=s.getMonth(),u=s.getFullYear(),c=new Date(u,i+1,0),g=c.getDate(),(m=c.getMonth()+1)<10&&(m="0"+m),g<10&&(g="0"+g),t.endTime=c.getFullYear().toString()+"-"+m.toString()+"-"+g.toString();const p=()=>{if(""==t.startTime||""==t.endTime)return h.error("请选择日期"),!1;I(t).then((e=>{l.value=e.data.list,r.value=e.data.page.total,n.value=e.data.user_type,d.value=e.data.model}))};p();const f=e({}),_=()=>{y(f).then((e=>{o.areas=e.data}))};_();let T=e({areaId:t.areaId});const C=e({streetId:t.streetId}),D=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`;return{query:t,tableData:l,pageTotal:r,form:o,queryArea:f,queryStreet:T,queryRoad:C,userType:n,model:d,getArea:_,getStreet:()=>{""==t.areaId?(o.streets=[],o.roads=[],t.streetId="",t.roadId=""):(T.areaId=t.areaId,t.roadId="",t.streetId="",o.roads=[],v(T).then((e=>{o.streets=e.data})))},getQueryDate:()=>{null==o.time||[]==o.time||""==o.time?(t.startTime="",t.endTime=""):(t.startTime=D(o.time[0]),t.endTime=D(o.time[1]))},getRoad:()=>{""==t.streetId?(o.roads=[],t.roadId=""):(o.roads=[],t.roadId="",C.streetId=t.streetId,w(C).then((e=>{o.roads=e.data})))},handleSearch:()=>{t.pageIndex=1,p()},handlePageChange:e=>{t.pageIndex=e,p()},exportExcel:()=>{b.confirm("是否导出当页数据？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((()=>{x(t).then((e=>{t.pageSize=18;const a=window.URL.createObjectURL(new Blob([e])),l=document.createElement("a");l.href=a,l.setAttribute("download","导出报表.xls"),document.body.appendChild(l),l.click()}))})).catch((()=>{h.info("已取消导出")}))}}},methods:{},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"}},T={class:"container"},C={class:"top-panel"},D=n("span",{class:"mar5 color999"},null,-1),S=n("span",{class:"mar5 color999"},null,-1),q={class:"pagination"};var k=f(_,[["render",function(e,a,h,b,f,I){const y=t("el-date-picker"),v=t("el-form-item"),w=t("el-option"),x=t("el-select"),_=t("el-button"),k=t("el-form"),V=t("el-col"),z=t("el-row"),R=t("el-table-column"),M=t("el-table"),U=t("el-pagination");return l(),r("div",null,[n("div",T,[d(z,null,{default:o((()=>[d(V,{span:24},{default:o((()=>[n("div",C,[d(k,{inline:"",size:"small"},{default:o((()=>[d(v,{label:"时间"},{default:o((()=>[d(y,{size:"small",modelValue:b.form.time,"onUpdate:modelValue":a[0]||(a[0]=e=>b.form.time=e),type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:b.getQueryDate},null,8,["modelValue","onChange"])])),_:1}),d(v,{label:"选择层级"},{default:o((()=>[d(x,{clearable:"",modelValue:b.query.areaId,"onUpdate:modelValue":a[1]||(a[1]=e=>b.query.areaId=e),filterable:"",placeholder:"所有区域",class:"w100",onChange:b.getStreet},{default:o((()=>[(l(!0),r(s,null,i(b.form.areas,(e=>(l(),u(w,{key:e.id,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),D,d(x,{clearable:"",modelValue:b.query.streetId,"onUpdate:modelValue":a[2]||(a[2]=e=>b.query.streetId=e),filterable:"",placeholder:"所有街道",class:"w100",onChange:b.getRoad},{default:o((()=>[(l(!0),r(s,null,i(b.form.streets,(e=>(l(),u(w,{key:e.id,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"]),S,d(x,{clearable:"",modelValue:b.query.roadId,"onUpdate:modelValue":a[3]||(a[3]=e=>b.query.roadId=e),filterable:"",placeholder:"所有路内",class:"w100"},{default:o((()=>[(l(!0),r(s,null,i(b.form.roads,(e=>(l(),u(w,{key:e.id,label:e.road_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),d(v,null,{default:o((()=>[d(_,{size:"small",type:"primary",icon:"el-icon-search",onClick:b.handleSearch},{default:o((()=>[c("查询")])),_:1},8,["onClick"])])),_:1}),d(v,null,{default:o((()=>[d(_,{size:"small",type:"success",icon:"el-icon-upload2",onClick:b.exportExcel},{default:o((()=>[c("导出")])),_:1},8,["onClick"])])),_:1})])),_:1})])])),_:1})])),_:1}),d(M,{data:b.tableData,border:"",class:"table","max-height":f.tableH,ref:"multipleTable","header-cell-class-name":"table-header"},{default:o((()=>[d(R,{type:"index",label:"序号",width:"55",align:"center"},{default:o((e=>[c(g(e.$index+1),1)])),_:1}),d(R,{prop:"roadName",label:"路内",align:"center"}),m(d(R,{prop:"area",label:"区域",align:"center",width:"300"},null,512),[[p,"0"==b.userType]]),d(R,{prop:"timeLong",label:"停车总时长",align:"center",width:"200"}),d(R,{prop:"utilizationRate",label:"利用率(%)",align:"center",width:"200"}),d(R,{prop:"turnoverRate",label:"周转率(%)",align:"center",width:"200"}),d(R,{prop:"turnoverAverRate",label:"周转均比",align:"center",width:"200"})])),_:1},8,["data","max-height"]),n("div",q,[d(U,{background:"",layout:"total, prev, pager, next","current-page":b.query.pageIndex,"page-size":b.query.pageSize,total:b.pageTotal,onCurrentChange:b.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])])])}]]);export{k as default};
