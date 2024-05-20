import{f as e,b as a,r as l,o as t,h as n,i as r,k as i,l as o,m as s,t as p,I as u,w as c,v as d}from"./vendor.ac52fd94.js";import{_ as m,aS as g,aM as h}from"./index.1d57ac4a.js";const b={name:"xjcztj",data:()=>({tableH:0}),setup(){const l=e({areaId:"",nameOrPhone:"",receivePaperInvoice:"",group:"",pageIndex:1,pageSize:15}),t=a([]),n=a({}),r=a(0),i=()=>{g(l).then((e=>{t.value=e.data.list,r.value=e.data.total}))};i();const o=()=>{h(l).then((e=>{n.value=e.data}))};o();const s=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`,p=a(!0),u=a(!1);let c=e({time:"",areas:[],inspectManages:[]});return{query:l,tableData:t,tableCountData:n,pageTotal:r,form:c,getQueryDate:()=>{null==c.time||[]==c.time||""==c.time?(l.startTime="",l.endTime=""):(l.startTime=s(c.time[0]),l.endTime=s(c.time[1]))},butTime:()=>{l.group="time",i(),o(),u.value=!1,p.value=!0},butPeople:()=>{l.group="people",i(),o(),p.value=!1,u.value=!0},handleSearch:()=>{l.pageIndex=1,i(),o()},handlePageChange:e=>{l.pageIndex=e,i()},timeVisible:p,peopleVisible:u}},methods:{},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"}},v={class:"container"},f={class:"handle-box"},_={class:"left-panel"},y={class:"right-panel"},C=r("span",{class:"dispinline ml5"},null,-1),x=r("span",{class:"dispinline ml5"},null,-1),z=r("span",{class:"dispinline ml5 font14 color666"},"创建时间：",-1),V=r("span",{class:"dispinline ml5"},null,-1),k=r("div",{class:"clear"},null,-1),D={class:"pagination"};var P=m(b,[["render",function(e,a,m,g,h,b){const P=l("el-button"),T=l("el-button-group"),I=l("el-input"),q=l("el-option"),H=l("el-select"),S=l("el-date-picker"),M=l("el-table-column"),j=l("el-table"),O=l("el-pagination");return t(),n("div",null,[r("div",v,[r("div",f,[r("div",_,[i(P,{size:"small",plain:"",type:"danger"},{default:o((()=>[s("充值人数(人)： "+p(g.tableCountData.recharge_pens),1)])),_:1}),i(P,{size:"small",plain:"",type:"danger"},{default:o((()=>[s("充值笔数(笔)： "+p(g.tableCountData.transactions_number),1)])),_:1}),i(P,{size:"small",plain:"",type:"danger"},{default:o((()=>[s("充值金额(元)： "+p(g.tableCountData.recharge_amount),1)])),_:1})]),r("div",y,[i(T,null,{default:o((()=>[i(P,{icon:"el-icon-time",size:"small",onClick:g.butTime},{default:o((()=>[s("按时间")])),_:1},8,["onClick"]),i(P,{icon:"el-icon-user",size:"small",onClick:g.butPeople},{default:o((()=>[s("按人员")])),_:1},8,["onClick"])])),_:1}),C,i(I,{onKeyup:a[0]||(a[0]=u((e=>g.handleSearch()),["enter"])),size:"small",modelValue:g.query.nameOrPhone,"onUpdate:modelValue":a[1]||(a[1]=e=>g.query.nameOrPhone=e),placeholder:"请输入姓名或手机号",class:"handle-input mr10"},null,8,["modelValue"]),x,i(H,{modelValue:g.query.receivePaperInvoice,"onUpdate:modelValue":a[2]||(a[2]=e=>g.query.receivePaperInvoice=e),filterable:"",size:"small",placeholder:"全部",class:"w100"},{default:o((()=>[i(q,{value:""},{default:o((()=>[s("全部")])),_:1}),i(q,{key:"1",label:"已发派",value:"1"}),i(q,{key:"0",label:"未派发",value:"0"})])),_:1},8,["modelValue"]),z,i(S,{size:"small",modelValue:g.form.time,"onUpdate:modelValue":a[3]||(a[3]=e=>g.form.time=e),type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:g.getQueryDate},null,8,["modelValue","onChange"]),V,i(P,{size:"small",type:"primary",icon:"el-icon-search",onClick:g.handleSearch},{default:o((()=>[s("查询 ")])),_:1},8,["onClick"])]),k]),c(i(j,{data:g.tableData,border:"",class:"table","max-height":h.tableH,ref:"multipleTable","header-cell-class-name":"table-header"},{default:o((()=>[i(M,{prop:"recharge_time",label:"日期",align:"center"}),i(M,{prop:"recharge_pens",label:"充值人数(人)",align:"center"}),i(M,{prop:"transactions_number",label:"充值笔数(笔)",align:"center"}),i(M,{prop:"recharge_amount",label:"充值金额(元)",align:"center"})])),_:1},8,["data","max-height"]),[[d,g.timeVisible]]),c(i(j,{data:g.tableData,border:"",class:"table","max-height":h.tableH,ref:"multipleTable","header-cell-class-name":"table-header"},{default:o((()=>[i(M,{prop:"name",label:"收费员姓名",align:"center"}),i(M,{prop:"phone",label:"帐号",align:"center"}),i(M,{prop:"transactions_number",label:"充值笔数(笔)",align:"center"}),i(M,{prop:"recharge_amount",label:"充值金额(元)",align:"center"})])),_:1},8,["data","max-height"]),[[d,g.peopleVisible]]),r("div",D,[i(O,{background:"",layout:"total, prev, pager, next","current-page":g.query.pageIndex,"page-size":g.query.pageSize,total:g.pageTotal,onCurrentChange:g.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])])])}]]);export{P as default};