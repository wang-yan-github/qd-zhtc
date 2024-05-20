import{f as e,b as a,L as l,q as t,r as d,M as r,o,h as n,i,w as s,c as u,l as c,m,k as h,I as p,p as f,O as k,N as v}from"./vendor.ac52fd94.js";import{_ as g,x as _,y,z as b,A as C,B as V,C as w,D as x}from"./index.1d57ac4a.js";const S={name:"rolesList",setup(){const d=e({role_name:""}),r=a([]),o=a(0),n=()=>{b(d).then((e=>{r.value=e.data.list,o.value=e.data.total}))};n();const i=a([]),s=a([]);a([]);const u=a(null),c=a(null),m=a(null),h=a(null),p=a("新增"),f=a(!1),k=a(!1);let g=a({});const y=a(!1),S=a(null),T=a(null);_(e({})).then((e=>{i.value=e.data.roadTree,s.value=e.data.parkTree}));return{query:d,tableData:r,addRoadTree:u,addParkTree:c,editRoadTree:m,editParkTree:h,pageTotal:o,editVisible:f,addVisible:k,form:g,parktreedata:s,radioChange:e=>{u.value.setCheckedNodes([])},checkStrictly:y,dialogT:p,addform:S,editform:T,formRules:{role_name:[{required:!0,message:"请输入角色名称",trigger:"blur"}]},handleSearch:()=>{d.pageIndex=1,n()},handlePageChange:e=>{d.pageIndex=e,n()},handleDelete:(a,d)=>{l.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{C(e({roleId:d.id})).then((e=>{0==e.code?(t.success(e.msg),n()):t.error(e.msg)}))})).catch((()=>{}))},handleEdit:(a,l,t)=>{p.value="编辑",f.value=!0,y.value=!0,v((()=>{x(e({roleId:l.id})).then((e=>{m.value.setCheckedNodes(e.data.roadMenuList),h.value.setCheckedNodes(e.data.parkMenuList),g.value=e.data,y.value=!1,T.value.clearValidate()}))}))},handleadd:(e,a,l)=>{g.value={role_type:"0"},p.value="新增",k.value=!0,y.value=!1,v((()=>{S.value.clearValidate()}))},saveEdit:()=>{f.value=!1,t.success("修改第 0 行成功"),Object.keys(g).forEach((e=>{r.value[-1][e]=g[e]}))},save:()=>{S.value.validate((e=>{if(e){let e=u.value.getCheckedKeys().concat(u.value.getHalfCheckedKeys()).concat(c.value.getCheckedKeys()).concat(c.value.getHalfCheckedKeys());if(g.value.menuIds=e.toString(),null===g.value.menuIds||""===g.value.menuIds)return t.error("请选择权限"),!1;V(g.value).then((e=>{0===e.code?(k.value=!1,n()):t.error(e.msg)}))}}))},edit:()=>{T.value.validate((e=>{if(e){let e=m.value.getCheckedKeys().concat(m.value.getHalfCheckedKeys()).concat(h.value.getCheckedKeys()).concat(h.value.getHalfCheckedKeys());if(g.value.menuIds=e.toString(),g.value.roadMenuList=null,g.value.parkMenuList=null,null===g.value.menuIds||""===g.value.menuIds)return t.error("请选择权限"),!1;g.value.menus=null,g.value.menuIdList=null,w(g.value).then((e=>{0===e.code?(f.value=!1,n()):t.error(e.msg)}))}}))},queryMenuTree:_,getData:n,multipleSelection:[],value:!0,activeName:"first",treedata:i,defaultProps:{children:"children",label:"label"}}},methods:{handleSelectionChange(e){this.selectedData=e},handleDeleteAll(){var e=this,a=this.selectedData;l.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{var l="";a?(a.forEach((function(e,a){l=l+e.id+","})),y({roleIds:l}).then((a=>{t.success("删除成功"),e.getData()}))):t.warning("请选择一条记录")})).catch((()=>{}))}}},T={class:"container"},I={class:"handle-box"},D={class:"left-panel"},z={class:"right-panel"},K=i("div",{class:"clear"},null,-1),L={class:"pagination"},q={class:"treeborder"},M={class:"treeborder"},P={class:"dialog-footer"},R={class:"treeborder"},U={class:"treeborder"},A={class:"dialog-footer"};var E=g(S,[["render",function(e,a,l,t,v,g){const _=d("el-button"),y=d("el-input"),b=d("el-table-column"),C=d("el-table"),V=d("el-pagination"),w=d("el-form-item"),x=d("el-col"),S=d("el-tree"),E=d("el-row"),N=d("el-form"),H=d("el-dialog"),j=r("permission");return o(),n("div",null,[i("div",T,[i("div",I,[i("div",D,[s((o(),u(_,{type:"primary",size:"small",icon:"el-icon-plus",onClick:a[0]||(a[0]=e=>t.handleadd(0,null,!0))},{default:c((()=>[m("添加")])),_:1})),[[j,["road_rolemanager_add","park_rolemanager_add"]]]),s((o(),u(_,{type:"danger",size:"small",icon:"el-icon-delete",onClick:a[1]||(a[1]=e=>g.handleDeleteAll())},{default:c((()=>[m("批量删除")])),_:1})),[[j,["road_rolemanager_deleteAll","park_rolemanager_deleteAll"]]])]),i("div",z,[h(y,{size:"small",modelValue:t.query.role_name,"onUpdate:modelValue":a[2]||(a[2]=e=>t.query.role_name=e),placeholder:"角色名",class:"handle-input mr10",onKeyup:a[3]||(a[3]=p((e=>t.handleSearch()),["enter"]))},null,8,["modelValue"]),h(_,{size:"small",type:"primary",icon:"el-icon-search",onClick:t.handleSearch},{default:c((()=>[m("查询")])),_:1},8,["onClick"])]),K]),h(C,{data:t.tableData,border:"",class:"table",ref:"multipleTable","header-cell-class-name":"table-header",onSelectionChange:g.handleSelectionChange},{default:c((()=>[h(b,{type:"selection",width:"55",align:"center"}),h(b,{type:"index",label:"序号",width:"55",align:"center"}),h(b,{prop:"role_name",label:"角色名称","min-width":"120",align:"center"}),h(b,{prop:"remark",label:"备注","min-width":"120",align:"center"}),h(b,{label:"操作",width:"220",align:"center"},{default:c((e=>["road"==e.row.role_type?s((o(),u(_,{key:0,size:"mini",type:"text",icon:"el-icon-edit",onClick:a=>t.handleEdit(e.$index,e.row,!1)},{default:c((()=>[m("编辑 ")])),_:2},1032,["onClick"])),[[j,["park_rolemanager_edit","road_rolemanager_edit"]]]):f("",!0),"road"==e.row.role_type?s((o(),u(_,{key:1,size:"mini",type:"text",icon:"el-icon-delete",class:"red",onClick:a=>t.handleDelete(e.$index,e.row)},{default:c((()=>[m("删除")])),_:2},1032,["onClick"])),[[j,["park_rolemanager_delete","road_rolemanager_delete"]]]):f("",!0)])),_:1})])),_:1},8,["data","onSelectionChange"]),i("div",L,[h(V,{background:"",layout:"total, prev, pager, next","current-page":t.query.pageIndex,"page-size":t.query.pageSize,total:t.pageTotal,onCurrentChange:t.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])]),h(H,{title:t.dialogT,modelValue:t.addVisible,"onUpdate:modelValue":a[8]||(a[8]=e=>t.addVisible=e),width:"30%",top:"2vh"},{footer:c((()=>[i("span",P,[h(_,{onClick:a[7]||(a[7]=e=>t.addVisible=!1)},{default:c((()=>[m("取 消")])),_:1}),h(_,{type:"primary",onClick:t.save},{default:c((()=>[m("确 定")])),_:1},8,["onClick"])])])),default:c((()=>[h(N,{"label-width":"100px",rules:t.formRules,ref:"addform","label-position":"top",model:t.form,onSubmit:a[6]||(a[6]=k((()=>{}),["prevent"]))},{default:c((()=>[h(E,{gutter:20},{default:c((()=>[h(x,{span:24},{default:c((()=>[h(w,{label:"角色名称",prop:"role_name"},{default:c((()=>[h(y,{modelValue:t.form.role_name,"onUpdate:modelValue":a[4]||(a[4]=e=>t.form.role_name=e),placeholder:"请输入角色名称"},null,8,["modelValue"])])),_:1})])),_:1}),h(x,{span:12},{default:c((()=>[h(w,{label:"路内权限"},{default:c((()=>[i("div",q,[h(S,{ref:"addRoadTree",data:t.treedata,"show-checkbox":"","node-key":"id","check-strictly":t.checkStrictly},null,8,["data","check-strictly"])])])),_:1})])),_:1}),h(x,{span:12},{default:c((()=>[h(w,{label:"停车场权限"},{default:c((()=>[i("div",M,[h(S,{ref:"addParkTree",data:t.parktreedata,"show-checkbox":"","node-key":"id","check-strictly":t.checkStrictly},null,8,["data","check-strictly"])])])),_:1})])),_:1}),h(x,{span:24},{default:c((()=>[h(w,{label:"备注"},{default:c((()=>[h(y,{modelValue:t.form.remark,"onUpdate:modelValue":a[5]||(a[5]=e=>t.form.remark=e),placeholder:"请输入备注内容",type:"textarea",rows:4},null,8,["modelValue"])])),_:1})])),_:1})])),_:1})])),_:1},8,["rules","model"])])),_:1},8,["title","modelValue"]),h(H,{title:t.dialogT,modelValue:t.editVisible,"onUpdate:modelValue":a[13]||(a[13]=e=>t.editVisible=e),width:"30%",top:"2vh"},{footer:c((()=>[i("span",A,[h(_,{onClick:a[12]||(a[12]=e=>t.editVisible=!1)},{default:c((()=>[m("取 消")])),_:1}),h(_,{type:"primary",onClick:t.edit},{default:c((()=>[m("确 定")])),_:1},8,["onClick"])])])),default:c((()=>[h(N,{"label-width":"100px",rules:t.formRules,ref:"editform",model:t.form,"label-position":"top",onSubmit:a[11]||(a[11]=k((()=>{}),["prevent"]))},{default:c((()=>[h(E,{gutter:20},{default:c((()=>[h(x,{span:24},{default:c((()=>[h(w,{label:"角色名称",prop:"role_name"},{default:c((()=>[h(y,{modelValue:t.form.role_name,"onUpdate:modelValue":a[9]||(a[9]=e=>t.form.role_name=e),placeholder:"请输入角色名称"},null,8,["modelValue"])])),_:1})])),_:1}),h(x,{span:12},{default:c((()=>[h(w,{label:"路内权限"},{default:c((()=>[i("div",R,[h(S,{ref:"editRoadTree",data:t.treedata,"show-checkbox":"","node-key":"id","default-checked-keys":t.form.roadMenuList,"check-strictly":t.checkStrictly},null,8,["data","default-checked-keys","check-strictly"])])])),_:1})])),_:1}),h(x,{span:12},{default:c((()=>[h(w,{label:"停车场权限"},{default:c((()=>[i("div",U,[h(S,{ref:"editParkTree",data:t.parktreedata,"show-checkbox":"","node-key":"id","default-checked-keys":t.form.parkMenuList,"check-strictly":t.checkStrictly},null,8,["data","default-checked-keys","check-strictly"])])])),_:1})])),_:1}),h(x,{span:24},{default:c((()=>[h(w,{label:"备注"},{default:c((()=>[h(y,{modelValue:t.form.remark,"onUpdate:modelValue":a[10]||(a[10]=e=>t.form.remark=e),placeholder:"请输入备注内容",type:"textarea",rows:4},null,8,["modelValue"])])),_:1})])),_:1})])),_:1})])),_:1},8,["rules","model"])])),_:1},8,["title","modelValue"])])}]]);export{E as default};
