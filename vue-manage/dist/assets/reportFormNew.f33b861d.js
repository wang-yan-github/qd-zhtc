import{f as e,b as a,r as l,o as t,h as r,i as n,k as o,l as d,F as s,j as i,c as u,m as c,t as m}from"./vendor.ac52fd94.js";import{_ as p,a5 as g,a6 as _,ce as b,cf as y,cg as f}from"./index.1d57ac4a.js";const z={class:"container"},h={class:"top-panel"},v={class:"pagination"};var T=p({name:"reportFormL",setup(){const l=e({yszlTime:"",startTime:"",pageNum:1,pageSize:15,endTime:"",variance:"",variance2:"0",area_id:"",street_id:""}),t=e({road_id:"",name:"",time:""}),r=e({road_list:[],orderWx:{},orderZfb:{},orderXj:{},orderSf:{},orderZj:{car_num:0,car_price:0}}),n=e([{},{},{},{},{},{}]),o=e([{style:"运营中心"},{style:"移动端"}]),d=e([{},{},{},{},{},{}]),s=e([{},{},{},{},{},{}]),i=e([{},{},{},{},{},{}]),u=e([{},{},{},{},{},{}]),c=e([{value:"选项1",label:"停车场名称1"},{value:"选项2",label:"停车场名称2"},{value:"选项3",label:"停车场名称3"},{value:"选项4",label:"停车场名称4"},{value:"选项5",label:"停车场名称5"}]),m=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`,p=e({start_time:"",end_time:"",road_id:"",area_id:"",street_id:""});let z=e({time:"",areaId:"",streetId:"",roadId:"",areas:[],streets:[],roads:[]});const h=e({}),v=()=>{g(h).then((e=>{z.areas=e.data}))};v();let T=e({areaId:l.area_id});const C=e({street_id:l.street_id}),j=a([]),D=a(0),Q=()=>{y(l).then((e=>{var a=e.data;j.value=a.list,D.value=a.total}))};Q();const S=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`;return{query:t,result:r,getQueryDate:()=>{if(null==t.time||""==t.time)return p.start_time="",p.end_time="",!1;p.start_time=m(t.time[0])+" ",p.end_time=m(t.time[1])+" "},formqjl:z,getArea:v,getStreet:()=>{T.areaId=l.area_id,l.street_id="",l.variance="",t.road_id="",_(T).then((e=>{z.streets=e.data}))},getRoad:()=>{C.street_id=l.street_id,l.variance="",t.road_id="",b(C).then((e=>{z.roads=e.data}))},handleSearch:()=>{r.orderZj.car_num=0,r.orderZj.car_price=0,console.log(r.orderZj),setTimeout((function(){r.orderZj.car_price=parseFloat(null==r.orderWx.car_price?0:r.orderWx.car_price)+parseFloat(null==r.orderZfb.car_price?0:r.orderZfb.car_price)+parseFloat(null==r.orderSf.car_price?0:r.orderSf.car_price)+parseFloat(null==r.orderXj.car_price?0:r.orderXj.car_price)+".00"}),500)},handlePageChangeyszl:e=>{l.pageNum=e,Q()},handleSearchyszl:()=>{l.pageNum=1,Q()},tableData1:n,query3ldc:p,tableData2:o,tableData3:d,tableData4:s,tableData5:i,tableData6:u,handleTab:(e,a)=>{t.time="",t.road_id="",l.yszlTime="",l.startTime="",l.endTime="",l.variance="",l.street_id="",l.area_id="",console.log(e.props.name),Q()},options:c,yszlQuery:l,yszlList:j,yszlTotal:D,getRoadSORList:Q,downSORExcel:()=>{f(l).then((e=>{console.log(e.data),console.log(document.createElement("a"));const a=window.URL.createObjectURL(new Blob([e])),l=document.createElement("a");l.href=a,l.setAttribute("download","路内营收统计.xlsx"),document.body.appendChild(l),l.click()}))},getYszlQueryDate:()=>{console.log(l.yszlTime),null==l.yszlTime||""==l.yszlTime||null==l.yszlTime?(l.startTime="",l.endTime=""):(l.startTime=S(l.yszlTime[0]),l.endTime=S(l.yszlTime[1]))},handlePageChangeYzcmx:e=>{l.pageNum=e}}},methods:{},data:()=>({activeTab:"yszlSor"})},[["render",function(e,a,p,g,_,b){const y=l("el-option"),f=l("el-select"),T=l("el-form-item"),C=l("el-date-picker"),j=l("el-button"),D=l("el-form"),Q=l("el-col"),S=l("el-row"),x=l("el-table-column"),k=l("el-table"),V=l("el-pagination");return t(),r("div",null,[n("div",z,[o(S,{class:"mt20"},{default:d((()=>[o(Q,{span:24},{default:d((()=>[n("div",h,[o(D,{inline:"",size:"small"},{default:d((()=>[o(T,{label:"区域"},{default:d((()=>[o(f,{clearable:"",modelValue:g.yszlQuery.area_id,"onUpdate:modelValue":a[0]||(a[0]=e=>g.yszlQuery.area_id=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100",onChange:g.getStreet},{default:d((()=>[(t(!0),r(s,null,i(g.formqjl.areas,(e=>(t(),u(y,{key:e.id,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"])])),_:1}),o(T,{label:"街道"},{default:d((()=>[o(f,{clearable:"",modelValue:g.yszlQuery.street_id,"onUpdate:modelValue":a[1]||(a[1]=e=>g.yszlQuery.street_id=e),filterable:"",size:"small",placeholder:"所有街道",class:"w100",onChange:g.getRoad},{default:d((()=>[(t(!0),r(s,null,i(g.formqjl.streets,(e=>(t(),u(y,{key:e.id,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"])])),_:1}),o(T,{label:"路内"},{default:d((()=>[o(f,{modelValue:g.yszlQuery.variance,"onUpdate:modelValue":a[2]||(a[2]=e=>g.yszlQuery.variance=e),filterable:"",size:"small",placeholder:"所有路内",class:"w100"},{default:d((()=>[o(y,{value:""},{default:d((()=>[c("全部")])),_:1}),(t(!0),r(s,null,i(g.formqjl.roads,((e,a)=>(t(),u(y,{key:a,label:e.name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),o(T,{label:"时间"},{default:d((()=>[o(C,{size:"small",modelValue:g.yszlQuery.yszlTime,"onUpdate:modelValue":a[3]||(a[3]=e=>g.yszlQuery.yszlTime=e),type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:g.getYszlQueryDate},null,8,["modelValue","onChange"])])),_:1}),o(T,null,{default:d((()=>[o(j,{size:"small",type:"primary",icon:"el-icon-search",onClick:g.handleSearchyszl},{default:d((()=>[c("查询 ")])),_:1},8,["onClick"])])),_:1}),o(T,null,{default:d((()=>[o(j,{size:"small",type:"success",icon:"el-icon-upload2",onClick:g.downSORExcel},{default:d((()=>[c("导出 ")])),_:1},8,["onClick"])])),_:1})])),_:1})])])),_:1})])),_:1}),o(k,{data:g.yszlList,style:{width:"100%"}},{default:d((()=>[o(x,{type:"index",label:"序号",align:"center"},{default:d((e=>[c(m(e.$index+1),1)])),_:1}),o(x,{prop:"roadName",label:"路内",align:"center"}),o(x,{prop:"",label:"月租车卡",align:"center"},{default:d((()=>[o(x,{prop:"yzCarNum",label:"车辆",align:"center"}),o(x,{prop:"yzTotalCost",label:"营收",align:"center"})])),_:1}),o(x,{label:"流动车",align:"center"},{default:d((()=>[o(x,{label:"支付方式",align:"center"},{default:d((()=>[o(x,{prop:"ldWxAmount",label:"微信",align:"center"}),o(x,{prop:"ldZfbAmount",label:"支付宝",align:"center"}),o(x,{prop:"ldDsfAmount",label:"第三方",align:"center"}),o(x,{prop:"ldXjAmount",label:"现金支付",align:"center"}),o(x,{prop:"ldQbAmount",label:"钱包支付",align:"center"})])),_:1}),o(x,{prop:"ldSumPaidAmount",label:"营收",align:"center"})])),_:1}),o(x,{prop:"",label:"欠费",align:"center"},{default:d((()=>[o(x,{prop:"tdCounts",label:"单数",align:"center"}),o(x,{prop:"tdSumAmount",label:"费用",align:"center"})])),_:1}),o(x,{prop:"",label:"免费车/辆",align:"center"},{default:d((()=>[o(x,{prop:"mfNbCount",label:"内部车",align:"center"}),o(x,{prop:"mfQyCount",label:"企业（税免）车",align:"center"})])),_:1}),o(x,{label:"总计",align:"center"},{default:d((()=>[o(x,{prop:"zjAmount",label:"营收",align:"center"})])),_:1})])),_:1},8,["data"]),n("div",v,[o(V,{background:"",layout:"total, prev, pager, next","current-page":g.yszlQuery.pageNum,"page-size":g.yszlQuery.pageSize,total:g.yszlTotal,onCurrentChange:g.handlePageChangeyszl},null,8,["current-page","page-size","total","onCurrentChange"])])])])}],["__scopeId","data-v-94b7c2d2"]]);export{T as default};
