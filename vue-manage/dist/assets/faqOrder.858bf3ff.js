import{f as e,b as l,q as t,r as a,o as i,h as s,i as o,k as n,l as r,I as d,m as u,w as c,v as m,F as p,j as h,c as g,t as _,p as f}from"./vendor.ac52fd94.js";import{U as b}from"./UE.013b43f7.js";import{I as v,_ as w,F as y}from"./index.1d57ac4a.js";const k={name:"abnormalParking",components:{Ueditor:b},data:()=>({liHide:!1,isShow:!1,isShowInfo:!0,stopTime:!1,surevb:!1,tableH:0}),setup(){const t=e({phone:"",order_no:"",berth:"",car_no:"",create_time:"",checked:0,pageIndex:1,pageSize:15}),a=l([]),i=l(0),s=()=>{(e=>v({url:"/abnormalParking/selectPageList.json",method:"post",params:e}))(t).then((e=>{console.log(e.data),a.value=e.data.list,i.value=e.data.total}))};s();const o=l(!1),n=l({}),r=l({});let d=e({id:"",car_id:"",resi_time:"",order_no:"",road_name:"",berth:"",car_no:"",car_type:"",drivein_time:"",driveout_time:"",resitime:"",sum_amount:"",state_name:"",checked:!1,startTime:"",endTime:"",source_type:"",company_name:"",create_time:""}),u=l({time:"",time1:""});const c=e=>`${e.getFullYear()}-${e.getMonth()+1>=10?e.getMonth()+1:"0"+(e.getMonth()+1)}-${e.getDate()>=10?e.getDate():"0"+e.getDate()} ${e.getHours()>=10?e.getHours():"0"+e.getHours()}:${e.getMinutes()>=10?e.getMinutes():"0"+e.getMinutes()}:${e.getSeconds()>=10?e.getSeconds():"0"+e.getSeconds()}`,m=l([]);return y(e({dict_type:"car_type"})).then((e=>{m.value=e.data})),{query:t,tableData:a,pageTotal:i,editVisible:o,form:d,scList:n,carType:m,slList:r,timeData:u,getData:s,getQueryDate1:()=>{null==u.value.time1||[]==u.value.time1||""==u.value.time1?t.driveout_time="":d.driveout_time=c(u.value.time1)},handleEdit:(l,t)=>{Object.keys(d).forEach((e=>{d[e]=t[e]})),o.value=!0,(e=>v({url:"/abnormalParking/details.json",method:"post",params:e}))(e({id:t.id})).then((e=>{n.value=e.data.scList,r.value=e.data.slList}))},handleSearch:()=>{t.pageIndex=1,s()},handlePageChange:e=>{t.pageIndex=e,s()},getQueryDate:()=>{null==u.value.time||[]==u.value.time||""==u.value.time?(t.startTime="",t.endTime=""):(t.startTime=c(u.value.time[0]),t.endTime=c(u.value.time[1]))},multipleSelection:[],dialogImageUrl:"",ppVisible:!1,isActive:!1,activeName:"first"}},methods:{handleRemove(e,l){console.log(e,l)},handlePictureCardPreview(e){this.dialogImageUrl=e.url,this.ppVisible=!0},handleSelectionChange(e){this.selectedData=e},endTimeTK(){this.stopTime=!this.stopTime,this.surevb=!0},endTimeMethod(){let l=this;(e=>v({url:"/abnormalParking/closureParkingOrder.json",method:"post",params:e}))(e({id:l.form.id,driveout_time:l.form.driveout_time,drivein_time:l.form.drivein_time})).then((e=>{0==e.code?(t.success(e.msg),l.editVisible=!1,l.getData()):t.success(e.msg)}))},carUpdDate(){this.isShow=!this.isShow,this.isShowInfo=!this.isShowInfo},changeisShowInfo(l){if(this.isShow=!this.isShow,this.isShowInfo=!this.isShowInfo,1==l){let l=this;(a=e({id:l.form.id,car_id:l.form.car_id,car_no:l.form.car_no,car_type:l.form.car_type}),v({url:"/abnormalParking/updateByCar.json",method:"post",params:a})).then((e=>{0==e.code?(l.editVisible=!1,t.success(e.msg),l.getData()):t.error(e.msg)}))}var a},imgurl:function(e){if(""!=e&&null!=e)return e},closeBtn(e,l){var t=this;t.stopTime=!1,t.liHide=!1,t.isShow=!1,t.isShowInfo=!0,t.timeData.time1="",t.surevb=!1,t.handleEdit(e,l)},exportAbnormalParking(){var e;t.success("正在下载中·····"),(e=this.query,v({url:"/abnormalParking/exportAbnormalParking.do",method:"post",params:e,responseType:"blob"})).then((e=>{const l=window.URL.createObjectURL(new Blob([e])),t=document.createElement("a");t.href=l,t.setAttribute("download","问题订单.xls"),document.body.appendChild(t),t.click()}))}},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"}},C={class:"container"},V={class:"handle-box"},x={class:"left-panel"},S=o("div",{class:"clear"},null,-1),z={class:"pagination"},D=o("div",{class:"mt20"},null,-1),T={class:"desctable mgb20 w"},U=o("td",{class:"tit",width:"60"},"订单号",-1),I=o("td",{class:"tit",width:"80"},"订单状态",-1),H=o("td",{class:"tit",width:"60"},"停留时间",-1),P=o("td",{class:"tit",width:"80"},"费用",-1),q=o("td",{class:"tit",width:"60"},"停车地点",-1),L=o("td",{class:"tit",width:"80"},"泊位号",-1),j=o("td",{class:"tit",width:"60"},"车牌号码",-1),M={colspan:"3"},$=o("td",{class:"tit",width:"60"},"车牌类型",-1),A=o("td",{class:"tit",width:"80"},"车主手机号",-1),E=o("td",{class:"tit",width:"60"},"订单来源",-1),B=o("td",{class:"tit",width:"80"},"创建时间",-1),K=o("div",{class:"mt20"},null,-1),Q={class:"desctable mgb20 w"},F=o("td",{class:"tit",width:"60"},"进场时间",-1),N=o("td",{class:"tit",width:"80"},"离场时间",-1),O={key:0},R=o("td",{class:"tit",width:"60"},"进场照片",-1),Y={colspan:"3"},G={key:1},J=o("td",{class:"tit",width:"60"},"离场照片",-1),W={colspan:"3"},X={class:"dialog-footer"};var Z=w(k,[["render",function(e,l,t,b,v,w){const y=a("el-input"),k=a("el-form-item"),Z=a("el-date-picker"),ee=a("el-button"),le=a("el-option"),te=a("el-select"),ae=a("el-form"),ie=a("el-table-column"),se=a("el-tag"),oe=a("el-table"),ne=a("el-pagination"),re=a("el-tab-pane"),de=a("el-image"),ue=a("el-tabs"),ce=a("el-dialog");return i(),s("div",null,[o("div",C,[o("div",V,[o("div",x,[n(ae,{inline:"","label-width":"80",size:"small",class:"lineH0"},{default:r((()=>[n(k,{label:"订单号",class:"search-mb0"},{default:r((()=>[n(y,{onKeyup:l[0]||(l[0]=d((e=>b.handleSearch()),["enter"])),size:"small",placeholder:"订单号",modelValue:b.query.order_no,"onUpdate:modelValue":l[1]||(l[1]=e=>b.query.order_no=e)},null,8,["modelValue"])])),_:1}),n(k,{label:"手机号",class:"search-mb0"},{default:r((()=>[n(y,{onKeyup:l[2]||(l[2]=d((e=>b.handleSearch()),["enter"])),size:"small",modelValue:b.query.phone,"onUpdate:modelValue":l[3]||(l[3]=e=>b.query.phone=e),placeholder:"手机号",class:"w100"},null,8,["modelValue"])])),_:1}),n(k,{label:"泊位号",class:"search-mb0"},{default:r((()=>[n(y,{onKeyup:l[4]||(l[4]=d((e=>b.handleSearch()),["enter"])),size:"small",modelValue:b.query.berth,"onUpdate:modelValue":l[5]||(l[5]=e=>b.query.berth=e),placeholder:"泊位号",class:"w170"},null,8,["modelValue"])])),_:1}),n(k,{label:"时间",class:"search-mb0"},{default:r((()=>[n(Z,{modelValue:b.timeData.time,"onUpdate:modelValue":l[6]||(l[6]=e=>b.timeData.time=e),placeholder:"",type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",onChange:b.getQueryDate},null,8,["modelValue","onChange"])])),_:1}),n(k,{label:"",class:"search-mb0"},{default:r((()=>[n(ee,{size:"small",type:"primary",icon:"el-icon-search",onClick:b.handleSearch},{default:r((()=>[u("查询")])),_:1},8,["onClick"]),n(ee,{size:"small",type:"success",icon:"el-icon-upload2",onClick:w.exportAbnormalParking},{default:r((()=>[u("导出")])),_:1},8,["onClick"])])),_:1}),c(o("div",null,[n(k,{label:"",class:"search-mb0"},{default:r((()=>[n(te,{modelValue:b.form.selvalue,"onUpdate:modelValue":l[7]||(l[7]=e=>b.form.selvalue=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100"},{default:r((()=>[(i(!0),s(p,null,h(b.form.czroptions,(e=>(i(),g(le,{key:e.value,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),n(k,{label:"",class:"search-mb0"},{default:r((()=>[n(te,{modelValue:b.form.selvalue,"onUpdate:modelValue":l[8]||(l[8]=e=>b.form.selvalue=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100"},{default:r((()=>[(i(!0),s(p,null,h(b.form.czroptions,(e=>(i(),g(le,{key:e.value,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),n(k,{label:"",class:"search-mb0"},{default:r((()=>[n(te,{modelValue:b.form.selvalue,"onUpdate:modelValue":l[9]||(l[9]=e=>b.form.selvalue=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100"},{default:r((()=>[(i(!0),s(p,null,h(b.form.czroptions,(e=>(i(),g(le,{key:e.value,label:e.label,value:e.value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1})],512),[[m,b.isActive]])])),_:1})]),S]),n(oe,{data:b.tableData,border:"",class:"table","max-height":v.tableH,ref:"multipleTable","header-cell-class-name":"table-header",onSelectionChange:w.handleSelectionChange},{default:r((()=>[n(ie,{label:"序号",width:"55",align:"center"},{default:r((e=>[u(_(e.$index+1),1)])),_:1}),n(ie,{prop:"order_no",label:"订单号",width:"200",align:"center"}),n(ie,{prop:"road_name",label:"路内",align:"center"}),n(ie,{prop:"berth",label:"泊位编号",align:"center",width:"200"}),n(ie,{prop:"car_no",label:"车牌号",width:"100",align:"center"},{default:r((e=>["蓝牌"==e.row.car_type?(i(),g(se,{key:0,size:"small",textContent:_(e.row.car_no)},null,8,["textContent"])):"绿牌"==e.row.car_type?(i(),g(se,{key:1,size:"small",type:"success",textContent:_(e.row.car_no)},null,8,["textContent"])):"黄牌"==e.row.car_type?(i(),g(se,{key:2,size:"small",type:"warning",textContent:_(e.row.car_no)},null,8,["textContent"])):f("",!0)])),_:1}),n(ie,{prop:"drivein_time",label:"停入时间",width:"180",align:"center"}),n(ie,{prop:"driveout_time",label:"离场时间",width:"180",align:"center"}),n(ie,{prop:"resi_time",width:"120",label:"停留时间",align:"center"}),n(ie,{prop:"sum_amount",label:"停车费用",width:"80",align:"center"}),n(ie,{prop:"state_name",label:"状态",width:"80",align:"center"},{default:r((e=>["1"==e.row.status?(i(),g(se,{key:0,size:"small",textContent:_(e.row.state_name)},null,8,["textContent"])):"2"==e.row.status?(i(),g(se,{key:1,size:"small",type:"success",textContent:_(e.row.state_name)},null,8,["textContent"])):"3"==e.row.status?(i(),g(se,{key:2,size:"small",type:"warning",textContent:_(e.row.state_name)},null,8,["textContent"])):"4"==e.row.status?(i(),g(se,{key:3,size:"small",type:"danger",textContent:_(e.row.state_name)},null,8,["textContent"])):f("",!0)])),_:1}),v.liHide?(i(),g(ie,{key:0,prop:"car_type"})):f("",!0),v.liHide?(i(),g(ie,{key:1,prop:"create_time"})):f("",!0),v.liHide?(i(),g(ie,{key:2,prop:"source_type"})):f("",!0),v.liHide?(i(),g(ie,{key:3,prop:"car_id"})):f("",!0),n(ie,{label:"操作",width:"100",align:"center"},{default:r((e=>[n(ee,{size:"mini",type:"text",icon:"el-icon-view",onClick:l=>w.closeBtn(e.$index,e.row)},{default:r((()=>[u("详情 ")])),_:2},1032,["onClick"])])),_:1})])),_:1},8,["data","max-height","onSelectionChange"]),o("div",z,[n(ne,{background:"",layout:"total, prev, pager, next","current-page":b.query.pageIndex,"page-size":b.query.pageSize,total:b.pageTotal,onCurrentChange:b.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])]),n(ce,{title:"查看详情",modelValue:b.editVisible,"onUpdate:modelValue":l[17]||(l[17]=e=>b.editVisible=e),width:"40%"},{footer:r((()=>[o("span",X,[n(ee,{onClick:l[16]||(l[16]=e=>b.editVisible=!1)},{default:r((()=>[u("取 消")])),_:1}),c(n(ee,{type:"primary",onClick:w.endTimeMethod},{default:r((()=>[u("确 定")])),_:1},8,["onClick"]),[[m,v.surevb]])])])),default:r((()=>[n(ue,{type:"card",modelValue:b.activeName,"onUpdate:modelValue":l[14]||(l[14]=e=>b.activeName=e)},{default:r((()=>[n(re,{label:"基本信息",name:"first"},{default:r((()=>[D,o("table",T,[o("tr",null,[U,o("td",null,_(b.form.order_no),1),I,o("td",null,[n(se,{type:"success"},{default:r((()=>[u(_(b.form.state_name),1)])),_:1})])]),o("tr",null,[H,o("td",null,_(b.form.resi_time),1),P,o("td",null,[n(se,{type:"danger"},{default:r((()=>[u(_(b.form.sum_amount),1)])),_:1})])]),o("tr",null,[q,o("td",null,_(b.form.road_name),1),L,o("td",null,_(b.form.berth),1)]),o("tr",null,[j,o("td",M,[c(o("div",null,[o("span",null,_(b.form.car_no),1),n(ee,{size:"small",class:"ml5",type:"primary",icon:"el-icon-edit",onClick:w.carUpdDate},{default:r((()=>[u("修改车牌号码")])),_:1},8,["onClick"])],512),[[m,v.isShowInfo]]),c(o("div",null,[n(y,{modelValue:b.form.car_no,"onUpdate:modelValue":l[10]||(l[10]=e=>b.form.car_no=e),class:"w100",size:"small"},null,8,["modelValue"]),n(te,{modelValue:b.form.car_type,"onUpdate:modelValue":l[11]||(l[11]=e=>b.form.car_type=e),filterable:"",size:"small",placeholder:"车牌分类",class:"w100 ml5"},{default:r((()=>[(i(!0),s(p,null,h(b.carType,(e=>(i(),g(le,{key:e.dc_value,label:e.label,value:e.dc_value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),n(ee,{size:"small",class:"ml5",type:"primary",icon:"el-icon-check",onClick:l[12]||(l[12]=e=>w.changeisShowInfo(1))},{default:r((()=>[u("确定 ")])),_:1}),n(ee,{size:"small",icon:"el-icon-close",onClick:l[13]||(l[13]=e=>w.changeisShowInfo(2))},{default:r((()=>[u("取消")])),_:1})],512),[[m,v.isShow]])])]),o("tr",null,[$,o("td",null,_(b.form.car_type),1),A,o("td",null,_(b.form.company_name),1)]),o("tr",null,[E,o("td",null,_(b.form.source_type),1),B,o("td",null,_(b.form.create_time),1)])])])),_:1}),n(re,{label:"进出场信息",name:"second"},{default:r((()=>[K,o("table",Q,[o("tr",null,[F,o("td",null,_(b.form.drivein_time),1),N,o("td",null,_(b.form.driveout_time),1)]),b.scList.length>0?(i(),s("tr",O,[R,o("td",Y,[(i(!0),s(p,null,h(b.scList,((e,l)=>(i(),g(de,{key:l,style:{width:"100px",height:"100px"},"hide-on-click-modal":"true","preview-teleported":"true",src:w.imgurl(e.file_url),class:"ml5","preview-src-list":[w.imgurl(e.file_url)]},null,8,["src","preview-src-list"])))),128))])])):f("",!0),b.slList.length>0?(i(),s("tr",G,[J,o("td",W,[(i(!0),s(p,null,h(b.slList,((e,l)=>(i(),g(de,{key:l,style:{width:"100px",height:"100px"},"hide-on-click-modal":"true","preview-teleported":"true",src:w.imgurl(e.file_url),class:"ml5","preview-src-list":[w.imgurl(e.file_url)]},null,8,["src","preview-src-list"])))),128))])])):f("",!0)])])),_:1})])),_:1},8,["modelValue"]),c(n(ae,null,{default:r((()=>[n(k,{label:"结束计时时间"},{default:r((()=>[n(Z,{modelValue:b.timeData.time1,"onUpdate:modelValue":l[15]||(l[15]=e=>b.timeData.time1=e),type:"datetime",onChange:b.getQueryDate1,placeholder:"选择日期时间"},null,8,["modelValue","onChange"])])),_:1})])),_:1},512),[[m,v.stopTime]])])),_:1},8,["modelValue"])])}]]);export{Z as default};
