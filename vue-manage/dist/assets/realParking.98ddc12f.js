import{S as e}from"./vue-schart.efd232c2.js";import{b as a,f as l,q as n,r as t,o,h as r,k as s,l as i,i as u,I as d,F as c,j as p,c as m,m as f,t as h,p as g,w as _,v as b,s as y,x as v}from"./vendor.ac52fd94.js";import{_ as k,F as I,a5 as w,a6 as S,U as z,by as x,bz as C,bA as V}from"./index.1d57ac4a.js";import{g as q,k as N}from"./roadParkingOrder.721e77c8.js";import{F as j}from"./file_url.ec979fed.js";const T={name:"realparking",components:{Schart:e},data:()=>({}),setup(){const e=a(!1),n=l({berth_code:"",str2:"",variance:"",variance2:"",variance3:"",area_id:"",street_id:"",road_id:"",pageNum:1,pageSize:18}),t=l({pageIndex:1,pageSize:10,total:0,carnoId:""}),o=localStorage.getItem("ms_username"),r="admin"===o?"超级管理员":"普通用户";let s=l({czroptions:[{value:"1",label:"在线"},{value:"2",label:"断线"}]}),i=l({time:"",areaId:"",streetId:"",roadId:"",areas:[],streets:[],roads:[]});const u=l({inuse:"",free:"",parked:"",zxamount:"",dxamount:"",utilizeRatio:""}),d=a([]),c=a({});let p=a(!1),m=a(!0);const f=a(!1),h=a(0),g=l({berth_code:"",pageType:"",str2:"",car_no_title:""}),_=a([]),b=a("");I(l({dict_type:"car_type"})).then((e=>{_.value=e.data,b.value=e.data[0].value}));const y=a([]),v=()=>{N(t).then((e=>{y.value=e.data.list,t.total=e.data.total}))},k=l({}),q=()=>{w(k).then((e=>{i.areas=e.data}))};q();let j=l({areaId:n.area_id});const T=l({streetId:n.street_id});const P=()=>{x(n).then((e=>{var a,l;u.normal=e.data.normal,u.parked=e.data.abnormal,u.free=e.data.inuse,u.zxamount=e.data.zxamount,u.dxamount=e.data.dxamount,u.utilizeRatio=(a=u.parked,l=u.normal,a=parseFloat(a),l=parseFloat(l),isNaN(a)||isNaN(l)?"-":l<=0?"0%":Math.round(a/l*1e4)/100+"%")}))},U=()=>{C(n).then((e=>{d.value=e.data.list,h.value=e.data.total}))};U(),P();return{name:o,query:n,form:s,carType:_,dialogVisible:e,pageTotal:h,arrears:y,qctj:()=>{n.berth_code="",n.str2="",n.variance="",n.variance2="",n.variance3="",n.area_id="",n.street_id="",n.road_id="",n.pageNum=1,U(),P()},handleSearch:()=>{n.pageNum=1,U(),P()},handlePageChange:e=>{n.pageNum=e,U()},getArrearages:v,handleArrearages:()=>{f.value=!f.value,v()},deviceList:d,census:u,formqjl:i,queryArea:k,queryStreet:j,queryRoad:T,getArea:q,getStreet:()=>{j.areaId=n.area_id,n.street_id="",n.road_id="",S(j).then((e=>{i.streets=e.data}))},getDeviceInfo:U,getRoad:()=>{T.streetId=n.street_id,n.road_id="",z(T).then((e=>{i.roads=e.data}))},getRoadParkingOrderInfo:(a,l)=>{p.value=!1,m.value=!0,f.value=!1,t.pageIndex=1,t.total=0,null!=a.rpId&&null!=a.rpId?(g.berth_code=a.order_no,g.str2=a.rpId,g.pageType=a.type,V(g).then((e=>{var l=e.data;l.arrearage=a.amount,null!=l.carNo&&null!=l.carNo&&""!=l.carNo||(l.carNo=""),c.value=l,g.car_no_title=c.value.carNo,null==c.value.car_type||""==c.value.car_type||null==c.value.car_type?c.value.car_type=b.value:c.value.car_type=c.value.car_type+"",t.carnoId=l.carnoId})),e.value=!0,f.value=!1):(g.berth_code="",g.str2="",g.pageType="",e.value=!1,f.value=!0)},handleInfoPageChange:e=>{t.pageIndex=e,v()},rpodInfo:c,queryInfo:g,infoPage:t,isShow:p,isShowInfo:m,showMoneyH:f,role:r,url:"https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",srcList:["https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg","https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg"]}},methods:{init(){},imgurl:function(e){if(console.log(e),""!=e&&null!=e)return j.file_hx_img_url+e},carUpdDate(){this.isShow=!this.isShow,this.isShowInfo=!this.isShowInfo},changeisShowInfo(e){console.log(e);let a=this;this.isShow=!this.isShow,this.isShowInfo=!this.isShowInfo,1==e&&q(l({id:a.rpodInfo.id,car_id:a.rpodInfo.carnoId,car_no:a.rpodInfo.carNo,car_type:a.rpodInfo.car_type})).then((e=>{console.log(e),0==e.code?(n.success(e.msg),a.dialogVisible=!1,a.getDeviceInfo()):n.error(e.msg)}))}},mounted(){this.init()}},P=e=>(y("data-v-49405db1"),e=e(),v(),e),U={class:"container"},A={class:"handle-box"},R={class:"left-panel"},F={class:"right-panel"},D={class:"tips-icon"},L=P((()=>u("span",null,"设备状态：",-1))),M=P((()=>u("i",{class:"status-i kx-status"},"空闲",-1))),O=P((()=>u("i",{class:"status-i sy-status"},"使用中",-1))),H=P((()=>u("i",{class:"status-i yc-status"},"异常",-1))),K=P((()=>u("li",null,[u("span",null,"设备/车牌识别异常："),u("i",{class:"fa fa-bookmark abnormal"})],-1))),B=P((()=>u("li",null,[u("span",null,"设备正常："),u("i",{class:"fa fa-bookmark normal"})],-1))),E=P((()=>u("div",{class:"clear"},null,-1))),G={class:"card-bgimg"},J=P((()=>u("h6",null,"泊位总数",-1))),Q=P((()=>u("h6",null,"在停车辆数",-1))),W=P((()=>u("h6",null,"在线泊位数",-1))),X=P((()=>u("h6",null,"空余泊位数",-1))),Y=P((()=>u("h6",null,"断线泊位数",-1))),Z=P((()=>u("h6",null,"泊位饱和度(%)",-1))),$={class:"p-card"},ee={class:"p-card-con"},ae={key:0,class:"p-card-meb member"},le={key:1,class:"p-card-meb nonmember"},ne={key:2,class:"arrears"},te={key:3,class:"fa fa-bookmark p-card-mark normal","aria-hidden":"true"},oe={key:4,class:"fa fa-bookmark p-card-mark abnormal","aria-hidden":"true"},re={class:"car-num"},se=P((()=>u("span",{class:"car-num-a"},null,-1))),ie=P((()=>u("span",{class:"car-num-b"},null,-1))),ue=P((()=>u("span",{class:"car-num-c"},null,-1))),de=P((()=>u("span",{class:"car-num-d"},null,-1))),ce={key:0},pe={key:1},me={key:2},fe={key:5,class:"park-num"},he={key:6,class:"park-num park-num-kx"},ge={key:7,class:"park-num park-num-yc"},_e={key:8,class:"month-tip"},be={class:"pagination"},ye=P((()=>u("i",{class:"el-icon-tickets"},null,-1))),ve=P((()=>u("i",{class:"el-icon-collection-tag"},null,-1))),ke=P((()=>u("i",{class:"el-icon-time"},null,-1))),Ie=P((()=>u("i",{class:"el-icon-money"},null,-1))),we=P((()=>u("i",{class:"el-icon-location-information"},null,-1))),Se=P((()=>u("i",{class:"el-icon-place"},null,-1))),ze=P((()=>u("i",{class:"el-icon-c-scale-to-original"},null,-1))),xe=P((()=>u("i",{class:"el-icon-postcard"},null,-1))),Ce=P((()=>u("i",{class:"el-icon-mobile-phone"},null,-1))),Ve=P((()=>u("i",{class:"el-icon-user"},null,-1))),qe=P((()=>u("i",{class:"el-icon-time"},null,-1))),Ne=P((()=>u("i",{class:"el-icon-bank-card"},null,-1))),je=P((()=>u("i",{class:"el-icon-time"},null,-1))),Te=P((()=>u("i",{class:"el-icon-picture-outline"},null,-1))),Pe=P((()=>u("i",{class:"el-icon-wallet"},null,-1))),Ue={style:{"margin-top":"20px"}},Ae={class:"history-box"},Re=P((()=>u("h4",{class:"history-tit"},[u("i",{class:"el-icon-menu"}),f("历史欠费 ")],-1))),Fe={class:"pagination"};var De=k(T,[["render",function(e,a,l,n,y,v){const k=t("el-input"),I=t("el-form-item"),w=t("el-option"),S=t("el-select"),z=t("el-button"),x=t("el-form"),C=t("el-tooltip"),V=t("el-col"),q=t("el-row"),N=t("el-pagination"),j=t("el-descriptions-item"),T=t("el-image"),P=t("el-descriptions"),De=t("el-divider"),Le=t("el-table-column"),Me=t("el-table"),Oe=t("el-collapse-transition"),He=t("el-dialog");return o(),r("div",null,[s(q,null,{default:i((()=>[s(V,{span:24},{default:i((()=>[u("div",U,[u("div",A,[u("div",R,[s(x,{inline:"","label-width":"80",size:"small"},{default:i((()=>[s(I,{label:"泊位号"},{default:i((()=>[s(k,{onKeyup:a[0]||(a[0]=d((e=>n.handleSearch()),["enter"])),size:"small",modelValue:n.query.berth_code,"onUpdate:modelValue":a[1]||(a[1]=e=>n.query.berth_code=e),placeholder:"泊位号",class:"w100"},null,8,["modelValue"])])),_:1}),s(I,{label:"市区"},{default:i((()=>[s(S,{clearable:"",modelValue:n.query.area_id,"onUpdate:modelValue":a[2]||(a[2]=e=>n.query.area_id=e),filterable:"",size:"small",placeholder:"所有区域",class:"w100",onChange:n.getStreet},{default:i((()=>[(o(!0),r(c,null,p(n.formqjl.areas,(e=>(o(),m(w,{key:e.id,label:e.area_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"])])),_:1}),s(I,{label:"街道"},{default:i((()=>[s(S,{clearable:"",modelValue:n.query.street_id,"onUpdate:modelValue":a[3]||(a[3]=e=>n.query.street_id=e),filterable:"",size:"small",placeholder:"所有街道",class:"w100",onChange:n.getRoad},{default:i((()=>[(o(!0),r(c,null,p(n.formqjl.streets,(e=>(o(),m(w,{key:e.id,label:e.street_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue","onChange"])])),_:1}),s(I,{label:"路内"},{default:i((()=>[s(S,{clearable:"",modelValue:n.query.road_id,"onUpdate:modelValue":a[4]||(a[4]=e=>n.query.road_id=e),filterable:"",size:"small",placeholder:"所有路内",class:"w100"},{default:i((()=>[(o(!0),r(c,null,p(n.formqjl.roads,(e=>(o(),m(w,{key:e.id,label:e.road_name,value:e.id},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1}),s(I,{label:""},{default:i((()=>[s(z,{size:"small",type:"primary",icon:"el-icon-search",onClick:n.handleSearch},{default:i((()=>[f("查询 ")])),_:1},8,["onClick"])])),_:1}),s(I,{label:""},{default:i((()=>[s(z,{size:"small",type:"warning",icon:"el-icon-refresh",onClick:n.qctj},{default:i((()=>[f("清空 ")])),_:1},8,["onClick"])])),_:1})])),_:1})]),u("div",F,[u("ul",D,[u("li",null,[L,s(C,{class:"item",effect:"dark",content:"设备空闲",placement:"top"},{default:i((()=>[M])),_:1}),s(C,{class:"item",effect:"dark",content:"设备使用中",placement:"top"},{default:i((()=>[O])),_:1}),s(C,{class:"item",effect:"dark",content:"设备异常",placement:"top"},{default:i((()=>[H])),_:1})]),K,B])]),E]),u("ul",G,[u("li",null,[u("h4",null,h(n.census.normal),1),J]),u("li",null,[u("h4",null,h(n.census.parked),1),Q]),u("li",null,[u("h4",null,h(n.census.zxamount),1),W]),u("li",null,[u("h4",null,h(n.census.free),1),X]),u("li",null,[u("h4",null,h(n.census.dxamount),1),Y]),u("li",null,[u("h4",null,h(n.census.utilizeRatio),1),Z])]),s(q,{gutter:20,class:"card-row"},{default:i((()=>[(o(!0),r(c,null,p(n.deviceList,(e=>(o(),m(V,{span:4,key:e,onClick:a=>n.getRoadParkingOrderInfo(e,a)},{default:i((()=>[u("div",$,[u("div",ee,[1==e.ishy?(o(),r("p",ae,"会员")):g("",!0),0==e.ishy?(o(),r("p",le," 非会员 ")):g("",!0),null!=e.amount?(o(),r("p",ne," 历史欠费："+h(e.amount)+"元 ",1)):g("",!0),"1"==e.sbstatu||"2"==e.sbstatu?(o(),r("i",te)):g("",!0),"3"==e.sbstatu||"2"==e.type?(o(),r("i",oe)):g("",!0),u("div",re,[se,ie,ue,de,null==e.order_no&&null==e.car_no?(o(),r("p",ce)):g("",!0),null!=e.order_no&&null!=e.car_no?(o(),r("p",pe,h(e.car_no),1)):g("",!0),null!=e.order_no&&null==e.car_no?(o(),r("p",me," 未知车牌 ")):g("",!0)]),"2"==e.sbstatu?(o(),r("h4",fe,h(e.berth_code),1)):g("",!0),"1"==e.sbstatu?(o(),r("h4",he,h(e.berth_code),1)):g("",!0),"3"==e.sbstatu?(o(),r("h4",ge,h(e.berth_code),1)):g("",!0),"1"==e.isby?(o(),r("div",_e)):g("",!0)])])])),_:2},1032,["onClick"])))),128))])),_:1}),u("div",be,[s(N,{background:"",layout:"total, prev, pager, next","current-page":n.query.pageNum,"page-size":n.query.pageSize,total:n.pageTotal,onCurrentChange:n.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])])])),_:1})])),_:1}),s(He,{title:n.queryInfo.car_no_title+" 驶入信息",modelValue:n.dialogVisible,"onUpdate:modelValue":a[10]||(a[10]=e=>n.dialogVisible=e),width:"990px"},{default:i((()=>[s(P,{class:"margin-top",title:"",column:2,border:""},{default:i((()=>[s(j,null,{label:i((()=>[ye,f(" 订单号 ")])),default:i((()=>[f(" "+h(n.rpodInfo.orderNo),1)])),_:1}),s(j,null,{label:i((()=>[ve,f(" 订单状态 ")])),default:i((()=>[f(" "+h(n.rpodInfo.statusStr),1)])),_:1}),s(j,null,{label:i((()=>[ke,f(" 停留时间 ")])),default:i((()=>[f(" "+h(n.rpodInfo.resitime),1)])),_:1}),s(j,null,{label:i((()=>[Ie,f(" 费用 ")])),default:i((()=>[f(" "+h(n.rpodInfo.sumAmount)+"元 ",1)])),_:1}),s(j,null,{label:i((()=>[we,f(" 停车地点 ")])),default:i((()=>[f(" "+h(n.rpodInfo.roadName),1)])),_:1}),s(j,null,{label:i((()=>[Se,f(" 泊位号 ")])),default:i((()=>[f(" "+h(n.rpodInfo.berth),1)])),_:1}),s(j,null,{label:i((()=>[ze,f(" 车牌号码 ")])),default:i((()=>[_(u("div",null,[u("span",null,h(n.rpodInfo.carNo),1),s(z,{size:"small",class:"ml5",type:"primary",icon:"el-icon-edit",onClick:v.carUpdDate},{default:i((()=>[f("修改车牌号码")])),_:1},8,["onClick"])],512),[[b,n.isShowInfo]]),_(u("div",null,[s(k,{modelValue:n.rpodInfo.carNo,"onUpdate:modelValue":a[5]||(a[5]=e=>n.rpodInfo.carNo=e),class:"w100",size:"small"},null,8,["modelValue"]),s(S,{modelValue:n.rpodInfo.car_type,"onUpdate:modelValue":a[6]||(a[6]=e=>n.rpodInfo.car_type=e),filterable:"",size:"small",placeholder:"车牌分类",class:"w100 ml5"},{default:i((()=>[(o(!0),r(c,null,p(n.carType,(e=>(o(),m(w,{key:e.dc_value,label:e.label,value:e.dc_value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"]),s(z,{size:"small",class:"ml5",type:"primary",icon:"el-icon-check",onClick:a[7]||(a[7]=e=>v.changeisShowInfo(1))},{default:i((()=>[f("确定 ")])),_:1}),s(z,{size:"small",icon:"el-icon-close",onClick:a[8]||(a[8]=e=>v.changeisShowInfo(2))},{default:i((()=>[f("取消")])),_:1})],512),[[b,n.isShow]])])),_:1}),s(j,null,{label:i((()=>[xe,f(" 车牌属性 ")])),default:i((()=>[f(" "+h(n.rpodInfo.rosterTypeStr),1)])),_:1}),s(j,null,{label:i((()=>[Ce,f(" 车主手机号 ")])),default:i((()=>[f(" "+h(n.rpodInfo.phone),1)])),_:1}),s(j,null,{label:i((()=>[Ve,f(" 巡检员 ")])),default:i((()=>[f(" "+h(n.rpodInfo.inspectName),1)])),_:1}),s(j,null,{label:i((()=>[qe,f(" 创建时间 ")])),default:i((()=>[f(" "+h(n.rpodInfo.createTime),1)])),_:1}),s(j,null,{label:i((()=>[Ne,f(" 订单来源 ")])),default:i((()=>[f(" "+h(n.rpodInfo.sourceStr),1)])),_:1}),s(j,null,{label:i((()=>[je,f(" 入场时间 ")])),default:i((()=>[f(" "+h(n.rpodInfo.driveinTime),1)])),_:1}),s(j,null,{label:i((()=>[Te,f(" 车辆进场照片 ")])),default:i((()=>[(o(!0),r(c,null,p(n.rpodInfo.images,((e,a)=>(o(),m(T,{key:a,style:{width:"60px",height:"60px","margin-right":"10px"},"hide-on-click-modal":"true","preview-teleported":"true",src:v.imgurl(e),class:"ml5","preview-src-list":[v.imgurl(e)]},null,8,["src","preview-src-list"])))),128))])),_:1}),null!=n.rpodInfo.arrearage&&n.rpodInfo.arrearage>0?(o(),m(j,{key:0},{label:i((()=>[Pe,f(" 历史欠费 ")])),default:i((()=>[f(" "+h(n.rpodInfo.arrearage)+"元 ",1),u("span",{class:"linktext",onClick:a[9]||(a[9]=(...e)=>n.handleArrearages&&n.handleArrearages(...e))},"（查看列表）")])),_:1})):g("",!0)])),_:1}),u("div",Ue,[s(Oe,null,{default:i((()=>[_(u("div",Ae,[s(De,{"content-position":"left"},{default:i((()=>[Re])),_:1}),s(Me,{data:n.arrears,style:{width:"100%"}},{default:i((()=>[s(Le,{prop:"orderNo",label:"订单号",width:"180"}),s(Le,{prop:"roadName",label:"欠费路内"}),s(Le,{prop:"driveinTime",width:"180",label:"驶入时间"}),s(Le,{prop:"driveoutTime",width:"180",label:"驶出时间"}),s(Le,{prop:"unpaidAmount",width:"100",label:"欠费金额"})])),_:1},8,["data"]),u("div",Fe,[s(N,{background:"",layout:"total, prev, pager, next","current-page":n.infoPage.pageIndex,"page-size":n.infoPage.pageSize,total:n.infoPage.total,onCurrentChange:n.handleInfoPageChange},null,8,["current-page","page-size","total","onCurrentChange"])])],512),[[b,n.showMoneyH]])])),_:1})])])),_:1},8,["title","modelValue"])])}],["__scopeId","data-v-49405db1"]]);export{De as default};
