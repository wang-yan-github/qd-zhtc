import{f as e,b as a,L as l,q as t,r as n,o as r,h as o,i as d,k as i,I as s,l as u,m as p,F as c,j as m,c as _,t as g,p as h}from"./vendor.ac52fd94.js";import{_ as y,aT as b,a5 as f,a6 as v,U as w}from"./index.1d57ac4a.js";const V={name:"providerecord",data:()=>({tableH:0}),setup(){const l=e({area_id:"",street_id:"",road_id:"",order_no:"",pageIndex:1,pageSize:15,start_time:"",end_time:""});let t=e({area_list:[],area_levels:[],query_street_list:[],query_road_list:[]});const n=a([]),r=a(0),o=()=>{b(l).then((e=>{console.log(e.data),n.value=e.data.list,r.value=e.data.total}))};o();f(l).then((e=>{t.area_list=e.data,console.log(e.data)}));let d=e({areaId:l.area_id});const i=e({streetId:l.street_id}),s=a(!1);let u=e({order_no:"",area_level:"",status:"0",is_del:"0",time:""});const p=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()}`;return{query:l,tableData:n,pageTotal:r,editVisible:s,result:t,form:u,getQueryDate:()=>{if(null==u.time||""==u.time)return l.start_time="",l.end_time="",!1;l.start_time=p(u.time[0])+" ",console.log(l.start_time),l.end_time=p(u.time[1])+" "},dialogT:"新增",handleSearch:()=>{l.pageIndex=1,o()},handlePageChange:e=>{l.pageIndex=e,o()},getStreetList:()=>{t.query_road_list=[],d.areaId=l.area_id,l.road_id="",l.street_id="",v(d).then((e=>{t.query_street_list=e.data}))},getRoadList:e=>{i.streetId=l.street_id,l.road_id="",w(i).then((e=>{console.log(e.data),t.query_road_list=e.data}))},getData:o,handleDelete:(e,a)=>{},handleEdit:(e,a,l)=>{},handleStop:(e,a)=>{},saveEdit:()=>{},multipleSelection:[],value:!0,activeName:"first"}},methods:{handleSelectionChange(e){this.selectedData=e},handleDeleteAll(){l.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{var e=this,a=this.selectedData;console.log(a);var l="";a?(a.forEach((function(e,a){l=l+e.id+","})),delAreaAll({areaIds:l}).then((a=>{t.success("删除成功"),e.getData()}))):t.warning("请选择一条记录")})).catch((()=>{}))}},mounted(){},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"}},k={class:"container"},C={class:"handle-box"},q=d("span",{class:"dispinline ml5"},null,-1),x=d("span",{class:"dispinline ml5"},null,-1),z=d("span",{class:"dispinline ml5"},null,-1),D=d("span",{class:"dispinline ml5 font14 color666"},"派放时间：",-1),S=d("span",{class:"dispinline ml5"},null,-1),I=d("div",{class:"clear"},null,-1),U={key:0},E={key:1},H={key:2},L={key:3},T={key:0},j={key:1},A={key:2},M={key:3},$={key:4},F={class:"pagination"},P={class:"dialog-footer"};var Q=y(V,[["render",function(e,a,l,t,y,b){const f=n("el-input"),v=n("el-option"),w=n("el-select"),V=n("el-date-picker"),Q=n("el-button"),R=n("el-table-column"),K=n("el-tag"),N=n("el-table"),Y=n("el-pagination"),B=n("el-form-item"),G=n("el-form"),J=n("el-dialog");return r(),o("div",null,[d("div",k,[d("div",C,[d("div",null,[i(f,{onKeyup:a[0]||(a[0]=s((e=>t.handleSearch()),["enter"])),size:"small",modelValue:t.query.order_no,"onUpdate:modelValue":a[1]||(a[1]=e=>t.query.order_no=e),placeholder:"请输入订单号/发票派放人",class:"handle-input"},null,8,["modelValue"]),q,i(w,{modelValue:t.query.area_id,"onUpdate:modelValue":a[2]||(a[2]=e=>t.query.area_id=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100",onChange:a[3]||(a[3]=e=>t.getStreetList())},{default:u((()=>[i(v,{value:""},{default:u((()=>[p("全部")])),_:1}),(r(!0),o(c,null,m(t.result.area_list,((e,a)=>(r(),_(v,{key:a,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),x,i(w,{modelValue:t.query.street_id,"onUpdate:modelValue":a[4]||(a[4]=e=>t.query.street_id=e),filterable:"",size:"small",placeholder:"所有街道",class:"w100",onChange:a[5]||(a[5]=e=>t.getRoadList())},{default:u((()=>[i(v,{value:""},{default:u((()=>[p("全部")])),_:1}),(r(!0),o(c,null,m(t.result.query_street_list,((e,a)=>(r(),_(v,{key:a,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),z,i(w,{modelValue:t.query.road_id,"onUpdate:modelValue":a[6]||(a[6]=e=>t.query.road_id=e),filterable:"",size:"small",placeholder:"所有路内",class:"w100"},{default:u((()=>[i(v,{value:""},{default:u((()=>[p("全部")])),_:1}),(r(!0),o(c,null,m(t.result.query_road_list,((e,a)=>(r(),_(v,{key:a,label:e.road_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),D,i(V,{size:"small",modelValue:t.form.time,"onUpdate:modelValue":a[7]||(a[7]=e=>t.form.time=e),type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",class:"datepicker",onChange:t.getQueryDate},null,8,["modelValue","onChange"]),S,i(Q,{size:"small",type:"primary",icon:"el-icon-search",onClick:t.handleSearch},{default:u((()=>[p("查询")])),_:1},8,["onClick"])]),I]),i(N,{data:t.tableData,border:"",class:"table",ref:"multipleTable","max-height":y.tableH,"header-cell-class-name":"table-header",onSelectionChange:b.handleSelectionChange},{default:u((()=>[i(R,{type:"index",label:"序号",width:"55",align:"center"}),i(R,{prop:"order_no",label:"订单号",width:"190",align:"center"}),i(R,{prop:"car_no",label:"车牌号",width:"120",align:"center"},{default:u((e=>["1"==e.row.car_type?(r(),_(K,{key:0,size:"small",textContent:g(e.row.car_no)},null,8,["textContent"])):"2"==e.row.car_type?(r(),_(K,{key:1,size:"small",type:"success",textContent:g(e.row.car_no)},null,8,["textContent"])):"3"==e.row.car_type?(r(),_(K,{key:2,size:"small",type:"warning",textContent:g(e.row.car_no)},null,8,["textContent"])):h("",!0)])),_:1}),i(R,{prop:"road_name",label:"所属路内","min-width":"200",align:"center"}),i(R,{prop:"drivein_time",label:"驶入时间",width:"170",align:"center"}),i(R,{prop:"driveout_time",label:"驶出时间",width:"170",align:"center"}),i(R,{prop:"resitime",label:"停留时间(分)",width:"110",align:"center"}),i(R,{prop:"sum_amount",label:"应收金额",align:"center"}),i(R,{prop:"paid_amount",label:"已付金额",align:"center"}),i(R,{prop:"status",label:"订单状态",align:"center"},{default:u((e=>[1==e.row.status?(r(),o("p",U,"在停")):h("",!0),2==e.row.status?(r(),o("p",E,"待缴费")):h("",!0),3==e.row.status?(r(),o("p",H,"已缴费")):h("",!0),4==e.row.status?(r(),o("p",L,"已完成")):h("",!0)])),_:1}),i(R,{prop:"pay_type",label:"支付方式",align:"center",width:"100"},{default:u((e=>[1==e.row.pay_type?(r(),o("p",T,"包月")):h("",!0),2==e.row.pay_type?(r(),o("p",j,"微信")):h("",!0),3==e.row.pay_type?(r(),o("p",A,"支付宝")):h("",!0),4==e.row.pay_type?(r(),o("p",M,"钱包")):h("",!0),5==e.row.pay_type?(r(),o("p",$,"现金")):h("",!0)])),_:1}),i(R,{prop:"name",label:"发票派放人",width:"100",align:"center"}),i(R,{prop:"invoice_amount",label:"派放金额",width:"100",align:"center"}),i(R,{prop:"provide_time",label:"派放时间",width:"170",align:"center"})])),_:1},8,["data","max-height","onSelectionChange"]),d("div",F,[i(Y,{background:"",layout:"total, prev, pager, next","current-page":t.query.pageIndex,"page-size":t.query.pageSize,total:t.pageTotal,onCurrentChange:t.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])]),i(J,{title:"新增",modelValue:t.editVisible,"onUpdate:modelValue":a[11]||(a[11]=e=>t.editVisible=e),width:"30%"},{footer:u((()=>[d("span",P,[i(Q,{onClick:a[10]||(a[10]=e=>t.editVisible=!1)},{default:u((()=>[p("取 消")])),_:1}),i(Q,{type:"primary",onClick:t.saveEdit},{default:u((()=>[p("确 定")])),_:1},8,["onClick"])])])),default:u((()=>[i(G,{"label-width":"70px"},{default:u((()=>[i(B,{label:"区域名称"},{default:u((()=>[i(f,{modelValue:t.form.order_no,"onUpdate:modelValue":a[8]||(a[8]=e=>t.form.order_no=e)},null,8,["modelValue"])])),_:1}),i(B,{label:"区域等级"},{default:u((()=>[i(w,{modelValue:t.form.area_level,"onUpdate:modelValue":a[9]||(a[9]=e=>t.form.area_level=e),placeholder:"无",class:"w"},{default:u((()=>[(r(!0),o(c,null,m(t.result.area_levels,((e,a)=>(r(),_(v,{key:a,label:e.label,value:e.dc_value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1})])),_:1})])),_:1},8,["modelValue"])])}]]);export{Q as default};
