import{f as e,b as a,r as l,o as t,h as o,k as s,l as n,i,m as r,t as d}from"./vendor.ac52fd94.js";import"./index.9abe9c67.js";import{_ as c,bD as p}from"./index.1d57ac4a.js";const u={name:"orderTongjiA",setup(){const l=e({address:"",name:"",pageIndex:1,pageSize:8}),t=a([]),o=a(0);let s=e({name:"",czroptions:[{value:"1",label:"平台管理员"},{value:"2",label:"管理员"},{value:"3",label:"车场"}],selvalue:"",date1:"",date2:""});const n=()=>{p(l).then((e=>{t.value=e.list,o.value=e.pageTotal||50}))};n();return{query:l,tableData:t,form:s,pageTotal:o,options4:{title:{text:"近八天订单增长分析",show:!0,textStyle:{fontSize:16,fontWeight:"normal",color:"#303133"}},tooltip:{trigger:"axis",axisPointer:{type:"cross",label:{backgroundColor:"#6a7985"}}},legend:{data:["订单总数","缴费订单数"]},grid:{left:"10px",right:"10px",bottom:"3%",containLabel:!0},xAxis:[{type:"category",boundaryGap:!1,data:["12-17","12-18","12-19","12-20","12-21","12-22","12-23"]}],yAxis:[{type:"value"}],series:[{name:"订单总数",type:"line",stack:"Total",areaStyle:{},emphasis:{focus:"series"},data:[120,132,101,134,90,230,210]},{name:"缴费订单数",type:"line",stack:"Total",areaStyle:{},emphasis:{focus:"series"},data:[220,182,191,234,290,330,310]}]},handleSearch:()=>{l.pageIndex=1,n()},handlePageChange:e=>{l.pageIndex=e,n()}}},methods:{goTo(){this.$router.push("/cashstaticA")},goTo2(){this.$router.push("/cashstaticthreeA")},goTo3(){this.$router.push("/cashstatictwoA")}},mounted(){}},m={class:"top-panel f-cb"},h={class:"left-panel"},f={class:"right-panel _mtp10"},g=i("span",{class:"mar5 color999"},"-",-1),b=i("div",{class:"clearfix"},null,-1);var y=c(u,[["render",function(e,a,c,p,u,y){const _=l("el-button"),v=l("el-button-group"),x=l("el-form-item"),w=l("el-date-picker"),T=l("el-form"),k=l("el-card"),S=l("el-table-column"),C=l("el-table"),z=l("el-col"),A=l("el-row");return t(),o("div",null,[s(k,{class:"mgb20",shadow:"hover"},{default:n((()=>[i("div",m,[i("div",h,[s(x,{class:"mb0"},{default:n((()=>[s(v,null,{default:n((()=>[s(_,{icon:"el-icon-s-grid",onClick:a[0]||(a[0]=e=>y.goTo())},{default:n((()=>[r("收入报表")])),_:1}),s(_,{icon:"el-icon-s-marketing",onClick:a[1]||(a[1]=e=>y.goTo3())},{default:n((()=>[r("充值增长")])),_:1}),s(_,{icon:"el-icon-money",onClick:a[2]||(a[2]=e=>y.goTo2())},{default:n((()=>[r("缴费方式统计")])),_:1}),s(_,{type:"primary",icon:"el-icon-s-custom"},{default:n((()=>[r("免单统计")])),_:1})])),_:1})])),_:1})]),i("div",f,[s(T,{"label-width":"80px",inline:"",size:"small"},{default:n((()=>[s(x,{label:"时间",class:"mb0"},{default:n((()=>[s(w,{modelValue:p.form.date1,"onUpdate:modelValue":a[3]||(a[3]=e=>p.form.date1=e),type:"datetime",placeholder:"开始日期",class:"w170"},null,8,["modelValue"]),g,s(w,{modelValue:p.form.date2,"onUpdate:modelValue":a[4]||(a[4]=e=>p.form.date2=e),type:"datetime",placeholder:"结束日期",class:"w170"},null,8,["modelValue"])])),_:1}),s(x,{class:"mb0"},{default:n((()=>[s(_,{size:"small",type:"primary",icon:"el-icon-search"},{default:n((()=>[r("查询")])),_:1})])),_:1}),s(x,{class:"mb0"},{default:n((()=>[s(_,{size:"small",type:"success",icon:"el-icon-upload2"},{default:n((()=>[r("导出")])),_:1})])),_:1})])),_:1})]),b])])),_:1}),s(A,{gutter:20},{default:n((()=>[s(z,{span:24},{default:n((()=>[s(k,{shadow:"hover"},{default:n((()=>[s(C,{data:p.tableData,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header",onSelectionChange:e.handleSelectionChange},{default:n((()=>[s(S,{pro:"ID",label:"ID",width:"55",align:"center"},{default:n((e=>[r(d(e.row.id),1)])),_:1}),s(S,{prop:"name",label:"操作人",width:"120",align:"center"}),s(S,{prop:"purviews",label:"角色",width:"120",align:"center"}),s(S,{prop:"rizhiconent",label:"日志内容"}),s(S,{prop:"addtimes",label:"操作时间",width:"160",align:"center"}),s(S,{prop:"ip",label:"IP地址",width:"160",align:"center"})])),_:1},8,["data","onSelectionChange"])])),_:1})])),_:1})])),_:1})])}]]);export{y as default};