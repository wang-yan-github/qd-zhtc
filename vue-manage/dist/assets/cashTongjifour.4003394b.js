import{f as e,b as a,r as t,o as l,h as o,i as n,k as r,l as i,m as s}from"./vendor.ac52fd94.js";import"./index.9abe9c67.js";import{_ as d,an as c}from"./index.1d57ac4a.js";const u={name:"orderTongji",setup(){const t=e({startTime:"",endTime:"",address:"",name:"",pageIndex:1,pageSize:8}),l=a([]),o=a(0);let n=e({time:"",name:"",czroptions:[{value:"1",label:"平台管理员"},{value:"2",label:"管理员"},{value:"3",label:"车场"}],selvalue:"",date1:"",date2:""});const r=()=>{c(t).then((e=>{l.value=e.data.list,o.value=e.data.total}))};r();const i=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`;return{query:t,tableData:l,form:n,pageTotal:o,options4:{title:{text:"近八天订单增长分析",show:!0,textStyle:{fontSize:16,fontWeight:"normal",color:"#303133"}},tooltip:{trigger:"axis",axisPointer:{type:"cross",label:{backgroundColor:"#6a7985"}}},legend:{data:["订单总数","缴费订单数"]},grid:{left:"10px",right:"10px",bottom:"3%",containLabel:!0},xAxis:[{type:"category",boundaryGap:!1,data:["12-17","12-18","12-19","12-20","12-21","12-22","12-23"]}],yAxis:[{type:"value"}],series:[{name:"订单总数",type:"line",stack:"Total",areaStyle:{},emphasis:{focus:"series"},data:[120,132,101,134,90,230,210]},{name:"缴费订单数",type:"line",stack:"Total",areaStyle:{},emphasis:{focus:"series"},data:[220,182,191,234,290,330,310]}]},getQueryDate:()=>{null==n.time||[]==n.time||""==n.time?(t.startTime="",t.endTime=""):(t.startTime=i(n.time[0]),t.endTime=i(n.time[1]))},handleSearch:()=>{t.pageIndex=1,r()},handlePageChange:e=>{t.pageIndex=e,r()}}},methods:{goTo(){this.$router.push("/cashstatic")},goTo2(){this.$router.push("/cashstaticthree")},goTo3(){this.$router.push("/cashstatictwo")}},mounted(){}},p={class:"container"},m={class:"top-panel f-cb"},g={class:"left-panel"},h={class:"right-panel"},f=n("span",{class:"dispinline mlr5"},null,-1),b=n("div",{class:"clear"},null,-1);var y=d(u,[["render",function(e,a,d,c,u,y){const T=t("el-button"),x=t("el-button-group"),v=t("el-form-item"),_=t("el-date-picker"),k=t("el-form"),C=t("el-table-column"),S=t("el-table"),D=t("el-card"),w=t("el-col"),z=t("el-row");return l(),o("div",null,[n("div",p,[n("div",m,[n("div",g,[r(v,{class:"mb0"},{default:i((()=>[r(x,null,{default:i((()=>[r(T,{icon:"el-icon-s-grid",onClick:a[0]||(a[0]=e=>y.goTo())},{default:i((()=>[s("收入报表")])),_:1}),r(T,{icon:"el-icon-s-marketing",onClick:a[1]||(a[1]=e=>y.goTo3())},{default:i((()=>[s("充值增长")])),_:1}),r(T,{icon:"el-icon-money",onClick:a[2]||(a[2]=e=>y.goTo2())},{default:i((()=>[s("缴费方式统计")])),_:1}),r(T,{type:"primary",icon:"el-icon-s-custom"},{default:i((()=>[s("免单统计")])),_:1})])),_:1})])),_:1})]),n("div",h,[r(k,{"label-width":"80px",inline:"",size:"small"},{default:i((()=>[r(v,{label:"时间"},{default:i((()=>[f,r(_,{size:"small",modelValue:c.form.time,"onUpdate:modelValue":a[3]||(a[3]=e=>c.form.time=e),type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:c.getQueryDate},null,8,["modelValue","onChange"])])),_:1}),r(v,null,{default:i((()=>[r(T,{size:"small",type:"primary",icon:"el-icon-search",onClick:c.handleSearch},{default:i((()=>[s("查询")])),_:1},8,["onClick"])])),_:1})])),_:1})]),b]),r(z,{gutter:20},{default:i((()=>[r(w,{span:24},{default:i((()=>[r(D,{shadow:"hover"},{default:i((()=>[r(S,{data:c.tableData,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header",onSelectionChange:e.handleSelectionChange},{default:i((()=>[r(C,{type:"index",label:"序号",width:"55",align:"center"}),r(C,{prop:"freeType",label:"免单类型",align:"center"}),r(C,{prop:"carNum",label:"免单车牌数",align:"center"}),r(C,{prop:"freeAmount",label:"免单金额",align:"center"}),r(C,{prop:"freeNum",label:"免单数量",align:"center"}),r(C,{prop:"freeProportion",label:"占总订单数比例",align:"center"})])),_:1},8,["data","onSelectionChange"])])),_:1})])),_:1})])),_:1})])])}]]);export{y as default};
