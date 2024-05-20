import{P as e,b as l,f as a,L as t,q as o,r,M as d,o as n,h as i,i as u,w as s,c,l as p,m,k as f,I as h,t as g,p as _,F as v,j as y,n as b,N as V,s as w,x as k}from"./vendor.ac52fd94.js";import{j as C}from"./index.8c41d9e3.js";import{_ as x,N as S,O as T,P as U,Q as I,R as q,S as z,T as D,U as P}from"./index.1d57ac4a.js";const A={name:"userList",components:{ElScrollbar:e},data:()=>({tableH:0}),setup(){const e=l(!1),r=l(null),d=l(null),n=a({pageIndex:1,pageSize:15}),i=l([]),u=l(0),s=()=>{T(n).then((e=>{i.value=e.data.list,console.info(i),u.value=e.data.total}))};s();const c=l(!1),p=l(!1);l("");const m=l([]),f=l([]),h=()=>{P(a({})).then((e=>{const l=e.data,a=[];for(let t=0;t<l.length;t++){let e=l[t];a.push({key:e.id,label:e.road_name}),m.value=a}}))};let g=l({});const _=l(""),v=l([]),y=()=>{D(a({})).then((e=>{v.value=e.data}))};y();let b=l(null),w=l(null),k=l([]),x=l([]),S=l(0),A=l(1),L=l(24),E=l({});const H=l(!1),$=()=>{x=[],V((()=>{const e=b.value;let l=0;if(x.push(l),e)for(let a=0;a<e.length;a++){l+=e[a].clientHeight,x.push(l)}}))},j=()=>{V((()=>{const e=w.value.$el.querySelector(".el-scrollbar__wrap");e.onscroll=()=>{let l=e.scrollTop;const a=x;for(let e=0;e<a.length-1;e++){let t=a[e],o=a[e+1];if(t<=l&&l<o){S.value=e;let l=Math.floor(e/L)+1;return void(A=l)}}}}))};return{query:n,tableData:i,pageTotal:u,editVisible:c,form:g,dialogT:_,addVisible:p,categorys:v,roaddatas:m,parkingIds:f,formRules:{name:[{required:!0,message:"请输入名称",trigger:"blur"}],category:[{required:!0,message:"请选择车辆类型",trigger:"blur"}],monthType:[{required:!0,message:"请选择包月类型",trigger:"blur"}],price_type:[{required:!0,message:"请选择价格类型",trigger:"blur"}],price:[{required:!0,message:"请输入价格",trigger:["blur","change"]}]},addform:r,editform:d,queryDict:y,handleSearch:()=>{n.pageIndex=1,s()},handlePageChange:e=>{n.pageIndex=e,s()},handleDelete:(e,l)=>{t.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{U(a({id:l.id})).then((l=>{0===l.code?(o.success("删除成功"),s(),i.value.splice(e,1)):(o.error(l.msg),s())}))})).catch((()=>{}))},handleEdit:(l,t,o)=>{_.value="编辑",g.value={},c.value=!0,h(),I(a({configId:t.id})).then((l=>{g.value=l.data,f.value=l.data.roadIds,"1"==g.value.price_type?e.value=!1:e.value=!0})),V((()=>{d.value.clearValidate()}))},handleAdd:(l,a,t)=>{_.value="新增",g.value={},g.value.price_type="1",g.value.category="1",g.value.monthType="1",e.value=!1,p.value=!0,f.value=[],h(),V((()=>{r.value.clearValidate()}))},saveEdit:()=>{d.value.validate((e=>{if(e){if(""===f.value.toString())return o.error("请选择路内"),!1;if("2"==g.value.price_type&&(""==g.value.new_price||null==g.value.new_price||null==g.value.new_price))return o.error("请输入明年价格"),!1;c.value=!1,g.value.roadIds=null,g.value.parkingIds=f.value.toString(),q(g.value).then((e=>{0===e.code?(c.value=!1,o.success(e.msg),s()):o.error(e.msg)}))}}))},save:()=>{r.value.validate((e=>{if(e){if(""===f.value.toString())return o.error("请选择路内"),!1;if("2"==g.value.price_type&&(""==g.value.new_price||null==g.value.new_price||null==g.value.new_price))return o.error("请输入明年价格"),!1;g.value.parking_type="0",g.value.parkingIds=f.value.toString(),g.value.roadIds=null,z(g.value).then((e=>{0===e.code?(p.value=!1,o.success(e.msg),s()):o.error(e.msg)}))}}))},getData:s,multipleSelection:[],viewVisible:H,handleView:(e,l)=>{V((()=>{$(),j()}));let a=[];l.roadList.forEach((function(e){null!=e&&a.push(e.road_name)})),a=a.map((function(e){return{name:e}})),C.setOptions({checkPolyphone:!1,charCase:0});let t=[],o=[];for(let i=0;i<a.length;i++){let e=a[i].name,l=C.getCamelChars(e).substring(0,1).toUpperCase();a[i].initial=l,-1===t.indexOf(l)&&t.push(l)}t.sort();for(var r=0;r<t.length;r++)o.push({id:r,key:t[r]});k.value=[];for(r=0;r<26;r++)k.value.push({id:"",key:String.fromCharCode(65+r)});for(r=0;r<o.length;r++)for(var d=0;d<k.value.length;d++)o[r].key===k.value[d].key&&(k.value[d].id=o[r].id);let n={};for(let i=0;i<t.length;i++)n[t[i]]=a.filter((e=>e.initial===t[i]));E.value=n,H.value=!0},charList:k,listHeight:x,currentIndex:S,indexPage:A,indexLimit:L,indexData:E,listGroup:b,scrollbar:w,calculateHeight:$,scrollToLetter:e=>{""!==e.id&&void 0!==e.id&&V((()=>{w.value.$el.querySelector(".el-scrollbar__wrap").scrollTop=x[e.id],S.value=e.id}))},handleScroll:j,closeAddress:()=>{w.value.$el.querySelector(".el-scrollbar__wrap").scrollTop=0,S.value=0,H.value=!1},newPriceShow:e}},computed:{indexList(){return this.charList}},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"},methods:{radioBtnPriceType(e){this.newPriceShow="1"!=e},handleSelectionChange(e){this.selectedData=e},handleDeleteAll(){var e=this,l=this.selectedData;t.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{var t="";l?(l.forEach((function(e,l){t=t+e.id+","})),S(a({ids:t})).then((l=>{0===l.code?(o.success(l.msg),e.getData()):o.error(l.msg)}))):o.warning("请选择一条记录")})).catch((()=>{}))}}},L={class:"container"},E={class:"handle-box"},H={class:"left-panel"},$={class:"right-panel"},j=(e=>(w("data-v-297e296e"),e=e(),k(),e))((()=>u("div",{class:"clear"},null,-1))),M={key:0,style:{color:"#00a0e9"}},R={key:1,style:{color:"#2da96d"}},B={key:0,style:{color:"#2da96d"}},O={key:1,style:{color:"#00a0e9"}},G={key:0,style:{color:"#00a0e9"}},N={key:1,style:{color:"#2da96d"}},F={class:"no-warp"},K={class:"pagination"},Q={class:"dialog-footer"},J={class:"dialog-footer"},W={class:"index-bar-content"},X=["id"],Y={class:"title-key"},Z={class:"content-container"},ee={class:"char-list"},le=["onClick"];var ae=x(A,[["render",function(e,l,a,t,o,V){const w=r("el-button"),k=r("el-input"),C=r("el-table-column"),x=r("el-tag"),S=r("el-table"),T=r("el-pagination"),U=r("el-form-item"),I=r("el-radio"),q=r("el-option"),z=r("el-select"),D=r("el-col"),P=r("el-row"),A=r("el-radio-group"),ae=r("el-transfer"),te=r("el-form"),oe=r("el-dialog"),re=r("el-scrollbar"),de=d("permission");return n(),i("div",null,[u("div",L,[u("div",E,[u("div",H,[s((n(),c(w,{type:"primary",size:"small",icon:"el-icon-plus",onClick:l[0]||(l[0]=e=>t.handleAdd(0,null,!0))},{default:p((()=>[m("添加 ")])),_:1})),[[de,"road_monthSetA_add"]]),s((n(),c(w,{type:"danger",size:"small",icon:"el-icon-delete",onClick:l[1]||(l[1]=e=>V.handleDeleteAll())},{default:p((()=>[m("批量删除 ")])),_:1})),[[de,"road_monthSetA_deleteAll"]])]),u("div",$,[f(k,{onKeyup:l[2]||(l[2]=h((e=>t.handleSearch()),["enter"])),size:"small",modelValue:t.query.name,"onUpdate:modelValue":l[3]||(l[3]=e=>t.query.name=e),placeholder:"名称",class:"handle-input mr10"},null,8,["modelValue"]),f(w,{size:"small",type:"primary",icon:"el-icon-search",onClick:t.handleSearch},{default:p((()=>[m("查询 ")])),_:1},8,["onClick"])]),j]),f(S,{data:t.tableData,border:"",class:"table",ref:"multipleTable","max-height":o.tableH,"header-cell-class-name":"table-header",onSelectionChange:V.handleSelectionChange},{default:p((()=>[f(C,{type:"selection",width:"55",align:"center"}),f(C,{type:"index",label:"序号",width:"55",align:"center"}),f(C,{prop:"name",label:"名称",width:"300",align:"center"}),f(C,{label:"价格(元/月)",width:"100",align:"center"},{default:p((e=>[m(g(e.row.price),1)])),_:1}),f(C,{prop:"monthType",label:"包月类型",align:"center",width:"100"},{default:p((e=>[1==e.row.monthType?(n(),i("p",M,"个人")):_("",!0),2==e.row.monthType?(n(),i("p",R,"公司")):_("",!0)])),_:1}),f(C,{prop:"category",label:"车辆类型",align:"center",width:"100"},{default:p((e=>[0==e.row.category?(n(),i("p",B,"大车")):_("",!0),1==e.row.category?(n(),i("p",O,"小车")):_("",!0)])),_:1}),f(C,{prop:"price_type",label:"价格类型",align:"center",width:"100"},{default:p((e=>[1==e.row.price_type?(n(),i("p",G,"永久")):_("",!0),2==e.row.price_type?(n(),i("p",N,"期限")):_("",!0)])),_:1}),f(C,{prop:"roads",label:"路内",align:"center"},{default:p((e=>[u("div",F,[(n(!0),i(v,null,y(e.row.roadList,((l,a)=>(n(),i("span",null,[null!=l?(n(),c(x,{size:"small",key:a,onClick:l=>t.handleView(e.$index,e.row),class:"mar5 mb5"},{default:p((()=>[m(g(null==l||null==l.road_name?"":l.road_name),1)])),_:2},1032,["onClick"])):_("",!0)])))),256))])])),_:1}),f(C,{label:"操作",width:"180",align:"center"},{default:p((e=>[s((n(),c(w,{size:"mini",type:"text",icon:"el-icon-edit",onClick:l=>t.handleEdit(e.$index,e.row,!1)},{default:p((()=>[m("编辑 ")])),_:2},1032,["onClick"])),[[de,"road_monthSetA_edit"]]),s((n(),c(w,{size:"mini",type:"text",icon:"el-icon-delete",class:"red",onClick:l=>t.handleDelete(e.$index,e.row)},{default:p((()=>[m("删除 ")])),_:2},1032,["onClick"])),[[de,"road_monthSetA_delete"]])])),_:1})])),_:1},8,["data","max-height","onSelectionChange"]),u("div",K,[f(T,{background:"",layout:"total, prev, pager, next","current-page":t.query.pageIndex,"page-size":t.query.pageSize,total:t.pageTotal,onCurrentChange:t.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])]),f(oe,{title:t.dialogT,modelValue:t.editVisible,"onUpdate:modelValue":l[13]||(l[13]=e=>t.editVisible=e),width:"800px",top:"5vh"},{footer:p((()=>[u("span",Q,[f(w,{onClick:l[12]||(l[12]=e=>t.editVisible=!1)},{default:p((()=>[m("取 消")])),_:1}),f(w,{type:"primary",onClick:t.saveEdit},{default:p((()=>[m("确 定")])),_:1},8,["onClick"])])])),default:p((()=>[f(te,{"label-width":"70px","label-position":"top",size:"small",rules:t.formRules,ref:"editform",model:t.form},{default:p((()=>[f(U,{label:"名称",prop:"name"},{default:p((()=>[f(k,{modelValue:t.form.name,"onUpdate:modelValue":l[4]||(l[4]=e=>t.form.name=e),placeholder:"请输入名称"},null,8,["modelValue"])])),_:1}),f(U,{label:"包月类型",prop:"name"},{default:p((()=>[f(I,{modelValue:t.form.monthType,"onUpdate:modelValue":l[5]||(l[5]=e=>t.form.monthType=e),label:"1",border:""},{default:p((()=>[m("个人包月")])),_:1},8,["modelValue"]),f(I,{modelValue:t.form.monthType,"onUpdate:modelValue":l[6]||(l[6]=e=>t.form.monthType=e),label:"2",border:""},{default:p((()=>[m("公司包月")])),_:1},8,["modelValue"])])),_:1}),f(P,null,{default:p((()=>[f(D,{span:11},{default:p((()=>[f(U,{label:"车辆类型",paop:"category"},{default:p((()=>[f(z,{modelValue:t.form.category,"onUpdate:modelValue":l[7]||(l[7]=e=>t.form.category=e),placeholder:"请选择",class:"w100p"},{default:p((()=>[(n(!0),i(v,null,y(t.categorys,(e=>(n(),c(q,{key:e.dc_value,label:e.label,value:e.dc_value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1})])),_:1}),f(D,{class:"line",span:2}),f(D,{span:11},{default:p((()=>[f(U,{label:"价格(元/月)",prop:"price"},{default:p((()=>[f(k,{type:"number",modelValue:t.form.price,"onUpdate:modelValue":l[8]||(l[8]=e=>t.form.price=e)},null,8,["modelValue"])])),_:1})])),_:1})])),_:1}),f(P,null,{default:p((()=>[f(D,{span:11},{default:p((()=>[f(U,{label:"价格类型",prop:"price_type"},{default:p((()=>[f(A,{modelValue:t.form.price_type,"onUpdate:modelValue":l[9]||(l[9]=e=>t.form.price_type=e),onChange:V.radioBtnPriceType},{default:p((()=>[f(I,{label:"1"},{default:p((()=>[m("永久")])),_:1}),f(I,{label:"2"},{default:p((()=>[m("期限")])),_:1})])),_:1},8,["modelValue","onChange"])])),_:1})])),_:1}),f(D,{class:"line",span:2}),t.newPriceShow?(n(),c(D,{key:0,span:11},{default:p((()=>[f(U,{label:"明年价格(元/月)",prop:"new_price"},{default:p((()=>[f(k,{type:"number",modelValue:t.form.new_price,"onUpdate:modelValue":l[10]||(l[10]=e=>t.form.new_price=e)},null,8,["modelValue"])])),_:1})])),_:1})):_("",!0)])),_:1}),f(U,{label:"停车区"},{default:p((()=>[f(ae,{filterable:"","filter-method":e.filterMethod,"filter-placeholder":"请输入城市拼音",titles:["待选择区域","已选择区域"],modelValue:t.parkingIds,"onUpdate:modelValue":l[11]||(l[11]=e=>t.parkingIds=e),data:t.roaddatas},null,8,["filter-method","modelValue","data"])])),_:1})])),_:1},8,["rules","model"])])),_:1},8,["title","modelValue"]),f(oe,{title:t.dialogT,modelValue:t.addVisible,"onUpdate:modelValue":l[23]||(l[23]=e=>t.addVisible=e),width:"800px",top:"5vh"},{footer:p((()=>[u("span",J,[f(w,{onClick:l[22]||(l[22]=e=>t.addVisible=!1)},{default:p((()=>[m("取 消")])),_:1}),f(w,{type:"primary",onClick:t.save},{default:p((()=>[m("确 定")])),_:1},8,["onClick"])])])),default:p((()=>[f(te,{"label-width":"70px","label-position":"top",size:"small",rules:t.formRules,ref:"addform",model:t.form},{default:p((()=>[f(U,{label:"名称",prop:"name"},{default:p((()=>[f(k,{modelValue:t.form.name,"onUpdate:modelValue":l[14]||(l[14]=e=>t.form.name=e),placeholder:"请输入名称"},null,8,["modelValue"])])),_:1}),f(U,{label:"包月类型",prop:"monthType"},{default:p((()=>[f(I,{modelValue:t.form.monthType,"onUpdate:modelValue":l[15]||(l[15]=e=>t.form.monthType=e),label:"1",border:""},{default:p((()=>[m("个人包月")])),_:1},8,["modelValue"]),f(I,{modelValue:t.form.monthType,"onUpdate:modelValue":l[16]||(l[16]=e=>t.form.monthType=e),label:"2",border:""},{default:p((()=>[m("公司包月")])),_:1},8,["modelValue"])])),_:1}),f(P,null,{default:p((()=>[f(D,{span:11},{default:p((()=>[f(U,{label:"车辆类型",prop:"category"},{default:p((()=>[f(z,{modelValue:t.form.category,"onUpdate:modelValue":l[17]||(l[17]=e=>t.form.category=e),placeholder:"请选择",class:"w100p"},{default:p((()=>[(n(!0),i(v,null,y(t.categorys,(e=>(n(),c(q,{key:e.dc_value,label:e.label,value:e.dc_value},null,8,["label","value"])))),128))])),_:1},8,["modelValue"])])),_:1})])),_:1}),f(D,{class:"line",span:2}),f(D,{span:11},{default:p((()=>[f(U,{label:"价格(元/月)",prop:"price"},{default:p((()=>[f(k,{type:"number",modelValue:t.form.price,"onUpdate:modelValue":l[18]||(l[18]=e=>t.form.price=e)},null,8,["modelValue"])])),_:1})])),_:1})])),_:1}),f(P,null,{default:p((()=>[f(D,{span:11},{default:p((()=>[f(U,{label:"价格类型",prop:"price_type"},{default:p((()=>[f(A,{modelValue:t.form.price_type,"onUpdate:modelValue":l[19]||(l[19]=e=>t.form.price_type=e),onChange:V.radioBtnPriceType},{default:p((()=>[f(I,{label:"1"},{default:p((()=>[m("永久")])),_:1}),f(I,{label:"2"},{default:p((()=>[m("期限")])),_:1})])),_:1},8,["modelValue","onChange"])])),_:1})])),_:1}),f(D,{class:"line",span:2}),t.newPriceShow?(n(),c(D,{key:0,span:11},{default:p((()=>[f(U,{label:"明年价格(元/月)",prop:"new_price"},{default:p((()=>[f(k,{type:"number",modelValue:t.form.new_price,"onUpdate:modelValue":l[20]||(l[20]=e=>t.form.new_price=e)},null,8,["modelValue"])])),_:1})])),_:1})):_("",!0)])),_:1}),f(U,{label:"停车区"},{default:p((()=>[f(ae,{filterable:"","filter-method":e.filterMethod,"filter-placeholder":"请输入城市拼音",titles:["待选择区域","已选择区域"],modelValue:t.parkingIds,"onUpdate:modelValue":l[21]||(l[21]=e=>t.parkingIds=e),data:t.roaddatas},null,8,["filter-method","modelValue","data"])])),_:1})])),_:1},8,["rules","model"])])),_:1},8,["title","modelValue"]),f(oe,{title:"路内列表",modelValue:t.viewVisible,"onUpdate:modelValue":l[24]||(l[24]=e=>t.viewVisible=e),width:"840px",onClose:t.closeAddress},{default:p((()=>[u("div",W,[f(re,{style:{height:"100%"},ref:"scrollbar"},{default:p((()=>[(n(!0),i(v,null,y(t.indexData,((e,l)=>(n(),i("div",{id:l,class:"main-list",key:l,ref_for:!0,ref:"listGroup"},[u("div",Y,g(l),1),u("div",Z,[(n(!0),i(v,null,y(e,((e,l)=>(n(),i("div",{class:"content-item",key:l},[f(x,{size:"small"},{default:p((()=>[m(g(e.name),1)])),_:2},1024)])))),128))])],8,X)))),128))])),_:1},512),u("ul",ee,[(n(!0),i(v,null,y(V.indexList,((e,l)=>(n(),i("li",{key:e.index,onClick:l=>t.scrollToLetter(e),class:b({active:t.currentIndex===e.id})},g(e.key)+" ",11,le)))),128))])])])),_:1},8,["modelValue","onClose"])])}],["__scopeId","data-v-297e296e"]]);export{ae as default};