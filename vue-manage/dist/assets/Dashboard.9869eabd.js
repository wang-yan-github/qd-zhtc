var e=Object.defineProperty,a=Object.defineProperties,t=Object.getOwnPropertyDescriptors,l=Object.getOwnPropertySymbols,n=Object.prototype.hasOwnProperty,r=Object.prototype.propertyIsEnumerable,i=(a,t,l)=>t in a?e(a,t,{enumerable:!0,configurable:!0,writable:!0,value:l}):a[t]=l,o=(e,a)=>{for(var t in a||(a={}))n.call(a,t)&&i(e,t,a[t]);if(l)for(var t of l(a))r.call(a,t)&&i(e,t,a[t]);return e},s=(e,l)=>a(e,t(l));import{S as d}from"./vue-schart.efd232c2.js";import{f as u,b as p,r as c,o as m,h as g,k as h,l as b,i as f,t as y,m as w,I as _,c as v,p as x,q as C,s as k,x as T}from"./vendor.ac52fd94.js";import{u as j}from"./ColSpan.be2d1729.js";import{i as S}from"./index.9abe9c67.js";import{p as z,_ as V,e as A,a as I,b as N,d as O,c as P,g as L,f as D}from"./index.1d57ac4a.js";const q=function(e){e.directive("permission",z)};window.Vue&&(window.permission=z,Vue.use(q)),z.install=q;const $={name:"dashboardA",directives:{permission:z},components:{Schart:d},setup(){const e=localStorage.getItem("ms_username"),a="admin"===e?"超级管理员":"普通用户",t=u({pageNum:1,pageSize:10,name:"",startTime:"",endTime:"",time:"",parkId:"",type:"1",parking_type:"1"}),l=p({mpCount:0,sumCost:0,soCount:0,bl:"",money:0,sumAmount:0,paidAmount:0,unpaidAmount:0,byAmount:0,czAmount:0}),n=p([]),{objectSpanMethod:r,getSpanArr:i}=j(),d=p(0);(()=>{var e=new Date;t.time=[e,e];var a=e.getDate(),l=e.getMonth()+1;l<10&&(l="0"+l),a<10&&(a="0"+a),t.startTime=e.getFullYear().toString()+"-"+l.toString()+"-"+a.toString(),t.endTime=t.startTime})();const c=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`,m=()=>{t.pageNum=1,""!=t.name&&null!=t.name&&null!=t.name||(t.parkId=""),h()},g=e=>{null!=e&&null!=e&&""!=e&&(t.parkId=e.id,t.name=e.title),m(),b(),f()},h=()=>{N(t).then((e=>{const a=[];e.data.data.list.forEach((e=>{0==e.paymentOrderList.length?a.push(s(o({},e),{months_unit_price:"0.00",amount:"0.00",months:0})):e.paymentOrderList.forEach((t=>{const{months_unit_price:l,amount:n,months:r}=t;a.push(s(o({},e),{months_unit_price:l,amount:n,months:r}))}))})),n.value=a,i(a),d.value=e.data.data.total,l.value=e.data.vo}))},b=()=>{O(t).then((e=>{F.xAxis[0].data=e.data.dates,F.series[0].data=e.data.monthlyCount,F.series[1].data=e.data.monthlyIncome;var a=document.getElementById("main");S(a).setOption(F)}))},f=()=>{P(t).then((e=>{Q.xAxis[0].data=e.data.dates,Q.series[0].data=e.data.orderCount,Q.series[1].data=e.data.receivable,Q.series[2].data=e.data.actual;var a=document.getElementById("mainline");S(a).setOption(Q)}))};h(),b(),f();const y=p(!1),w=p("订单列表"),_=p([]),v=u({pageNum:1,pageSize:10,startTime:"",endTime:"",parkId:"",pageType:"",parking_type:"1"}),x=p(0);let k=setInterval((()=>{m()}),6e4);const T=p(!1),z=p("包月收入明细"),V=p([]),q=u({pageNum:1,pageSize:10,startTime:"",endTime:"",parkId:"",pageType:"",parking_type:"1"}),$=p(0),F={title:{text:"近十天车辆包月数趋势",show:!0,textStyle:{fontSize:16,fontWeight:"normal",color:"#303133"}},tooltip:{trigger:"axis",axisPointer:{type:"cross",label:{backgroundColor:"#6a7985"}}},legend:{data:["包月数","包月收入"]},toolbox:{feature:{saveAsImage:{}}},grid:{left:"10px",right:"16px",bottom:"3%",containLabel:!0},xAxis:[{type:"category",boundaryGap:!1,data:[]}],yAxis:[{type:"value"}],series:[{name:"包月数",type:"line",areaStyle:{},emphasis:{focus:"series"},data:[]},{name:"包月收入",type:"line",areaStyle:{},emphasis:{focus:"series"},data:[]}]},Q={title:{text:"流动车近十天订单趋势",show:!0,textStyle:{fontSize:16,fontWeight:"normal",color:"#303133"}},tooltip:{trigger:"axis",axisPointer:{type:"cross",label:{backgroundColor:"#6a7985"}}},legend:{data:["订单总数","应缴费","已缴费"]},toolbox:{feature:{saveAsImage:{}}},grid:{left:"10px",right:"16px",bottom:"3%",containLabel:!0},xAxis:[{type:"category",boundaryGap:!1,data:[]}],yAxis:[{type:"value"}],series:[{name:"订单总数",type:"line",areaStyle:{},emphasis:{focus:"series"},data:[]},{name:"应缴费",type:"line",areaStyle:{},emphasis:{focus:"series"},data:[]},{name:"已缴费",type:"line",areaStyle:{},emphasis:{focus:"series"},data:[]}]};return{name:e,census:l,options3:F,options4:Q,todoList:n,role:a,query:t,pageTotal:d,handlePageChange:e=>{t.pageNum=e,h()},getQueryDate:()=>{null==t.time||[]==t.time||""==t.time?(t.startTime="",t.endTime=""):(t.startTime=c(t.time[0]),t.endTime=c(t.time[1]))},handleSearch:m,exportSearch:()=>{C.success("正在下载中·····"),A(t).then((e=>{const a=window.URL.createObjectURL(new Blob([e])),t=document.createElement("a");t.href=a,t.setAttribute("download","停车场概览.xls"),document.body.appendChild(t),t.click()}))},exportBymx:()=>{C.success("正在下载中·····"),I(t).then((e=>{const a=window.URL.createObjectURL(new Blob([e])),t=document.createElement("a");t.href=a,t.setAttribute("download","包月收入明细.xls"),document.body.appendChild(t),t.click()}))},parkLink:g,clearPark:()=>{t.parkId="",g()},viewVisible:y,viewVisible2:T,dialogT:w,dialogT2:z,handleView:(e,a,l)=>{w.value=a.title+"订单列表",v.pageNum=1,v.startTime=t.startTime,v.endTime=t.endTime,v.parkId=a.id,v.pageType=l,L(v).then((e=>{_.value=e.data.list,x.value=e.data.total})),y.value=!0},handleView2:()=>{q.pageNum=1,q.startTime=t.startTime,q.endTime=t.endTime,q.parkId=t.parkId,q.pageType=t.type,D(q).then((e=>{V.value=e.data.list,$.value=e.data.total})),T.value=!0},orderData:_,byData:V,orderQuery:v,byQuery:q,pageTotal2:x,pageTotal3:$,handlePageChange2:e=>{v.pageNum=e,v.startTime=t.startTime,v.endTime=t.endTime,L(v).then((e=>{_.value=e.data.list,x.value=e.data.total}))},handlePageChange3:e=>{q.pageNum=e,q.startTime=t.startTime,q.endTime=t.endTime,q.parkId=t.parkId,q.pageType=t.type,D(q).then((e=>{V.value=e.data.list,$.value=e.data.total}))},timerSearch:k,objectSpanMethod:r}},methods:{moneyLink(){this.$router.push({path:"/arrearsstatic",query:{menuType:"1",startTime:this.query.startTime,endTime:this.query.endTime}})},towNumber:e=>Number(e).toFixed(2)},mounted(){}},F=e=>(k("data-v-d38c7a3e"),e=e(),T(),e),Q={class:"grid-content grid-con-1"},U=F((()=>f("i",{class:"el-icon-s-custom grid-con-icon"},null,-1))),E={class:"grid-cont-right"},M={class:"grid-num"},B=F((()=>f("div",null,"总营收(元)",-1))),R={class:"grid-content grid-con-2"},G=F((()=>f("i",{class:"el-icon-s-finance grid-con-icon"},null,-1))),K={class:"grid-cont-right"},W={class:"grid-num"},Y=F((()=>f("div",null,"包月收入(元)",-1))),H={class:"grid-content grid-con-6"},J=F((()=>f("i",{class:"el-icon-s-order grid-con-icon"},null,-1))),X={class:"grid-cont-right"},Z={class:"grid-num"},ee=F((()=>f("div",null,"临停收入(元)",-1))),ae={class:"grid-content grid-con-4"},te=F((()=>f("i",{class:"el-icon-circle-plus grid-con-icon"},null,-1))),le={class:"grid-cont-right"},ne={class:"grid-num"},re=F((()=>f("div",null,"充值收入(元)",-1))),ie=F((()=>f("div",{class:"clearfix"},[f("span",null,"今日停车订单数排名(该页面以车辆停入时间统计停车订单收入金额)")],-1))),oe={class:"handle-box"},se={class:"left-panel"},de=F((()=>f("div",{class:"clear"},null,-1))),ue={class:"pagination"},pe=F((()=>f("div",{id:"main",class:"schart"},null,-1))),ce=F((()=>f("div",{id:"mainline",class:"schart"},null,-1))),me={class:"pagination"},ge={class:"dialog-footer"},he={class:"handle-box"},be={class:"left-panel"},fe=F((()=>f("div",{class:"clear"},null,-1))),ye={class:"pagination"},we={class:"dialog-footer"};var _e=V($,[["render",function(e,a,t,l,n,r){const i=c("el-card"),o=c("el-col"),s=c("el-link"),d=c("el-tooltip"),u=c("el-row"),p=c("el-input"),C=c("el-form-item"),k=c("el-date-picker"),T=c("el-button"),j=c("el-form"),S=c("el-table-column"),z=c("el-table"),V=c("el-pagination"),A=c("el-tag"),I=c("el-dialog");return m(),g("div",null,[h(u,{gutter:20},{default:b((()=>[h(o,{span:24},{default:b((()=>[h(u,{gutter:20,class:"mgb20"},{default:b((()=>[h(o,{span:6},{default:b((()=>[h(i,{shadow:"hover","body-style":{padding:"0px"}},{default:b((()=>[f("div",Q,[U,f("div",E,[f("div",M,y(l.census.money),1),B])])])),_:1})])),_:1}),h(o,{span:6},{default:b((()=>[h(i,{shadow:"hover","body-style":{padding:"0px"}},{default:b((()=>[f("div",R,[G,f("div",K,[h(s,{type:"danger",href:"javascript:;",onClick:a[0]||(a[0]=e=>l.handleView2())},{default:b((()=>[f("div",W,y(l.census.byAmount)+"元 ",1)])),_:1}),Y])])])),_:1})])),_:1}),h(o,{span:6},{default:b((()=>[h(i,{shadow:"hover","body-style":{padding:"0px"}},{default:b((()=>[f("div",H,[J,f("div",X,[f("div",Z,y(l.census.paidAmount),1),ee,h(d,{class:"item",effect:"dark",content:"临停欠费金额(元)",placement:"bottom"},{default:b((()=>[h(s,{type:"danger",href:"javascript:;",onClick:a[1]||(a[1]=e=>r.moneyLink())},{default:b((()=>[w("欠费 "+y(l.census.unpaidAmount)+"元 ",1)])),_:1})])),_:1})])])])),_:1})])),_:1}),h(o,{span:6},{default:b((()=>[h(i,{shadow:"hover","body-style":{padding:"0px"}},{default:b((()=>[f("div",ae,[te,f("div",le,[f("div",ne,y(l.census.czAmount),1),re])])])),_:1})])),_:1})])),_:1}),h(i,{shadow:"hover"},{header:b((()=>[ie])),default:b((()=>[f("div",oe,[f("div",se,[h(j,{inline:"","label-width":"80",size:"small",class:"lineH0"},{default:b((()=>[h(C,{label:"停车场名称",class:"search-mb0"},{default:b((()=>[h(p,{modelValue:l.query.name,"onUpdate:modelValue":a[2]||(a[2]=e=>l.query.name=e),onKeyup:_(l.handleSearch,["enter"]),size:"small",placeholder:"停车场名称",clearable:"",onClear:l.clearPark},null,8,["modelValue","onKeyup","onClear"])])),_:1}),h(C,{label:"停入时间",class:"search-mb0"},{default:b((()=>[h(k,{modelValue:l.query.time,"onUpdate:modelValue":a[3]||(a[3]=e=>l.query.time=e),placeholder:"",type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",onChange:l.getQueryDate,clearable:""},null,8,["modelValue","onChange"])])),_:1}),h(C,{label:"",class:"search-mb0"},{default:b((()=>[h(T,{size:"small",type:"primary",icon:"el-icon-search",onClick:l.handleSearch},{default:b((()=>[w("查询 ")])),_:1},8,["onClick"])])),_:1}),h(C,{label:"",class:"search-mb0"},{default:b((()=>[h(T,{size:"small",type:"success",icon:"el-icon-upload2",onClick:l.exportSearch},{default:b((()=>[w("导出 ")])),_:1},8,["onClick"])])),_:1})])),_:1})]),de]),h(z,{"show-header":!0,height:"380",data:l.todoList,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header","span-method":l.objectSpanMethod},{default:b((()=>[h(S,{label:"停车场名称",prop:"title",align:"center"},{default:b((e=>[h(s,{type:"primary",href:"javascript:;",onClick:a=>l.parkLink(e.row)},{default:b((()=>[w(y(e.row.title),1)])),_:2},1032,["onClick"])])),_:1}),h(S,{label:"泊位数",width:"100",align:"center",prop:"stopcarnum"}),h(S,{label:"月租车辆",align:"center"},{default:b((()=>[h(S,{label:"单价",width:"100",align:"center",prop:"months_unit_price"},{default:b((e=>[f("p",null,y(e.row.months_unit_price?e.row.months_unit_price:"0.00"),1)])),_:1}),h(S,{label:"数量",width:"100",align:"center",prop:"months"},{default:b((e=>[f("p",null,y(e.row.months?e.row.months:0),1)])),_:1}),h(S,{label:"金额",width:"100",align:"center",prop:"amount"},{default:b((e=>[f("p",null,y(e.row.amount?e.row.amount:"0.00"),1)])),_:1})])),_:1}),h(S,{label:"临停车辆",align:"center"},{default:b((()=>[h(S,{prop:"counts",label:"订单数",width:"100",align:"center"},{default:b((e=>[h(s,{type:"primary",href:"javascript:;",onClick:a=>l.handleView(e.$index,e.row)},{default:b((()=>[w(y(e.row.counts),1)])),_:2},1032,["onClick"])])),_:1}),h(S,{prop:"counts2",label:"非零订单数",width:"100",align:"center"},{default:b((e=>[h(s,{type:"primary",href:"javascript:;",onClick:a=>l.handleView(e.$index,e.row,0)},{default:b((()=>[w(y(e.row.counts2),1)])),_:2},1032,["onClick"])])),_:1}),h(S,{prop:"sumAmount",label:"应收金额",width:"100",align:"center"},{default:b((e=>[h(s,{type:"primary",href:"javascript:;",onClick:a=>l.handleView(e.$index,e.row,1)},{default:b((()=>[w(y(e.row.sumAmount),1)])),_:2},1032,["onClick"])])),_:1}),h(S,{prop:"paidAmount",label:"实收金额",width:"100",align:"center"},{default:b((e=>[h(s,{type:"primary",href:"javascript:;",onClick:a=>l.handleView(e.$index,e.row,2)},{default:b((()=>[w(y(e.row.paidAmount),1)])),_:2},1032,["onClick"])])),_:1}),h(S,{prop:"unpaidAmount",label:"在停金额",width:"100",align:"center"},{default:b((e=>[h(s,{type:"primary",href:"javascript:;",onClick:a=>l.handleView(e.$index,e.row,3)},{default:b((()=>[w(y(e.row.unpaidAmount),1)])),_:2},1032,["onClick"])])),_:1}),h(S,{prop:"unpaidAmount2",label:"待缴费金额",width:"100",align:"center"},{default:b((e=>[h(s,{type:"primary",href:"javascript:;",onClick:a=>l.handleView(e.$index,e.row,4)},{default:b((()=>[w(y(e.row.unpaidAmount2),1)])),_:2},1032,["onClick"])])),_:1})])),_:1}),h(S,{label:"补缴",align:"center"},{default:b((()=>[h(S,{prop:"bjcs",label:"补缴次数",width:"100",align:"center"},{default:b((e=>[f("p",null,y(e.row.bjcs),1)])),_:1}),h(S,{prop:"bjje",label:"补缴金额",width:"100",align:"center"},{default:b((e=>[f("p",null,y(e.row.bjje),1)])),_:1})])),_:1}),h(S,{label:"收费率",align:"center"},{default:b((()=>[h(S,{prop:"jfs",label:"缴费数",width:"100",align:"center"},{default:b((e=>[f("p",null,y(e.row.jfs),1)])),_:1}),h(S,{prop:"jfl",label:"缴费率",width:"100",align:"center"},{default:b((e=>[f("p",null,y(e.row.jfl),1)])),_:1}),h(S,{prop:"zzl",label:"周转率",width:"100",align:"center"},{default:b((e=>[f("p",null,y(e.row.zzl),1)])),_:1})])),_:1})])),_:1},8,["data","span-method"]),f("div",ue,[h(V,{background:"",layout:"total, prev, pager, next","current-page":l.query.pageNum,"page-size":l.query.pageSize,total:l.pageTotal,onCurrentChange:l.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])])),_:1})])),_:1})])),_:1}),h(u,{gutter:20},{default:b((()=>[h(o,{span:12},{default:b((()=>[h(i,{shadow:"hover"},{default:b((()=>[pe])),_:1})])),_:1}),h(o,{span:12},{default:b((()=>[h(i,{shadow:"hover"},{default:b((()=>[ce])),_:1})])),_:1})])),_:1}),h(I,{title:l.dialogT,modelValue:l.viewVisible,"onUpdate:modelValue":a[5]||(a[5]=e=>l.viewVisible=e),width:"1200px"},{footer:b((()=>[f("span",ge,[h(T,{onClick:a[4]||(a[4]=e=>l.viewVisible=!1)},{default:b((()=>[w("取 消")])),_:1})])])),default:b((()=>[h(z,{data:l.orderData,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header"},{default:b((()=>[h(S,{prop:"order_no",label:"订单号",align:"center"}),h(S,{prop:"car_no",label:"车牌号",width:"120",align:"center"},{default:b((e=>["1"==e.row.car_type?(m(),v(A,{key:0,size:"small",textContent:y(e.row.car_no)},null,8,["textContent"])):"2"==e.row.car_type?(m(),v(A,{key:1,size:"small",type:"success",textContent:y(e.row.car_no)},null,8,["textContent"])):"3"==e.row.car_type?(m(),v(A,{key:2,size:"small",type:"warning",textContent:y(e.row.car_no)},null,8,["textContent"])):x("",!0)])),_:1}),h(S,{prop:"drivein_time",label:"驶入时间",width:"180",align:"center"}),h(S,{prop:"driveout_time",label:"驶离时间",width:"180",align:"center"}),h(S,{prop:"resTime",label:"停车时长",width:"100",align:"center"}),h(S,{prop:"sum_amount",width:"100",label:"应收金额",align:"center"},{default:b((e=>[f("p",null,y(Number(e.row.sum_amount).toFixed(2)),1)])),_:1}),h(S,{prop:"paid_amount",width:"100",label:"实收金额",align:"center"},{default:b((e=>[f("p",null,y(Number(e.row.paid_amount).toFixed(2)),1)])),_:1}),h(S,{prop:"unpaid_amount",width:"100",label:"在停/待缴费金额",align:"center"},{default:b((e=>[f("p",null,y(Number(e.row.unpaid_amount).toFixed(2)),1)])),_:1}),h(S,{prop:"status_name",label:"状态",width:"100",align:"center"},{default:b((e=>["1"==e.row.status?(m(),v(A,{key:0,size:"small",textContent:y(e.row.state_name)},null,8,["textContent"])):"2"==e.row.status?(m(),v(A,{key:1,size:"small",type:"success",textContent:y(e.row.state_name)},null,8,["textContent"])):"3"==e.row.status?(m(),v(A,{key:2,size:"small",type:"warning",textContent:y(e.row.state_name)},null,8,["textContent"])):"4"==e.row.status?(m(),v(A,{key:3,size:"small",type:"danger",textContent:y(e.row.state_name)},null,8,["textContent"])):x("",!0)])),_:1})])),_:1},8,["data"]),f("div",me,[h(V,{background:"",layout:"total, prev, pager, next","current-page":l.orderQuery.pageNum,"page-size":l.orderQuery.pageSize,total:l.pageTotal2,onCurrentChange:l.handlePageChange2},null,8,["current-page","page-size","total","onCurrentChange"])])])),_:1},8,["title","modelValue"]),h(I,{title:l.dialogT2,modelValue:l.viewVisible2,"onUpdate:modelValue":a[7]||(a[7]=e=>l.viewVisible2=e),width:"1200px"},{footer:b((()=>[f("span",we,[h(T,{onClick:a[6]||(a[6]=e=>l.viewVisible2=!1)},{default:b((()=>[w("取 消")])),_:1})])])),default:b((()=>[f("div",he,[f("div",be,[h(T,{size:"small",type:"success",icon:"el-icon-upload2",onClick:l.exportBymx},{default:b((()=>[w(" 导出 ")])),_:1},8,["onClick"])]),fe]),h(z,{data:l.byData,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header"},{default:b((()=>[h(S,{prop:"carNo",label:"车牌",width:"100",align:"center"}),h(S,{prop:"create_time",label:"包月时间",align:"center"}),h(S,{prop:"months_unit_price",label:"单价",align:"center"},{default:b((e=>[f("p",null,y(Number(e.row.months_unit_price).toFixed(2)),1)])),_:1}),h(S,{prop:"months",label:"月数",align:"center"}),h(S,{prop:"amount",label:"总费用",align:"center"},{default:b((e=>[f("p",null,y(Number(e.row.amount).toFixed(2)),1)])),_:1}),h(S,{prop:"configName",label:"包月方案",align:"center"})])),_:1},8,["data"]),f("div",ye,[h(V,{background:"",layout:"total, prev, pager, next","current-page":l.byQuery.pageNum,"page-size":l.byQuery.pageSize,total:l.pageTotal3,onCurrentChange:l.handlePageChange3},null,8,["current-page","page-size","total","onCurrentChange"])])])),_:1},8,["title","modelValue"])])}],["__scopeId","data-v-d38c7a3e"]]);export{_e as default};