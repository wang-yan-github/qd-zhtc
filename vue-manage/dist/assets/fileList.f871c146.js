import{f as e,b as l,q as a,L as t,r as i,M as d,o,h as n,i as r,w as s,c,l as u,m,k as p,I as f,t as h,p as g}from"./vendor.ac52fd94.js";import{U as _}from"./UE.013b43f7.js";import{d as b,a as y,e as v,s as V,b as x}from"./sysResourceIndex.2c650a03.js";import{_ as C}from"./index.1d57ac4a.js";const I={name:"fileList",components:{Ueditor:_},data:()=>({tableH:0,formRules:{title:[{required:!0,message:"必填项",trigger:"blur"}]}}),setup(){const i=e({title:"",category:"0",pageIndex:1,pageSize:15}),d=l([]),o=l(0),n=()=>{V(i).then((e=>{console.log(e),d.value=e.data.list,o.value=e.data.total}))};n();let r=l("编辑");const s=l(!1),c=l(!1),u=l({}),m=l([]),p=l([]);let f=e({config:{initialFrameHeight:300,initialFrameWidth:"100%"}});return{query:i,tableData:d,pageTotal:o,editVisible:s,form:u,uform:f,fileIds:m,fileList2:p,dialogT:r,getData:n,handleSearch:()=>{i.pageIndex=1,n()},handlePageChange:e=>{i.pageIndex=e,n()},handleDelete:(l,i)=>{t.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{let l=e({is_del:"1",id:i.id});v(l).then((e=>{console.log(e),a.success("删除成功")})).then((()=>{n()}))})).catch((()=>{}))},handleEdit:(l,a,t)=>{s.value=!0,u.value={},m.value=[],p.value=[],t?(u.value={doc_type:0},r.value="新增"):(r.value="编辑",x(e({id:a.id})).then((e=>{e.data.doc_type=parseInt(e.data.doc_type),u.value=e.data,""!=e.data.picture_id&&null!=e.data.picture_id?m.value=e.data.picture_id.split(","):m.value=[],""!=e.data.fileList&&null!=e.data.fileList?p.value=e.data.fileList:p.value=[]})))},multipleSelection:[],dialogImageUrl:"",dialogVisible:c}},created(){let e=document.documentElement.clientHeight||document.body.clientHeight;this.tableH=e-308+"px"},methods:{handleSuccess(e,l,a){this.fileIds.push(e.id+""),l.id=e.id,console.log(e,l,a)},handleRemove(l,t){console.log(l,t);let i=this.fileIds.indexOf(l.id+"");-1==i&&(i=this.fileIds.indexOf(l.id));let d=0;-1!=i&&b(e({id:l.id})).then((e=>{d=e.success})).then((()=>{0==d?(a.success("操作成功"),this.fileIds.splice(i,1)):-1==d&&a.error("操作失败!")}))},handlePictureCardPreview(e){this.dialogImageUrl=e.url,this.dialogVisible=!0},handleSelectionChange(e){this.selectedData=e},handleDeleteAll(){t.confirm("确定要删除吗？","提示",{type:"warning"}).then((()=>{var e=this,l=this.selectedData;console.log(l);var t="";l?(l.forEach((function(e,l){t=t+e.id+","})),y({ids:t}).then((l=>{a.success("删除成功"),e.getData()}))):a.warning("请选择一条记录")})).catch((()=>{}))},editorReady(e){this.$refs.ue.setText(this.form.content)},saveEdit(e){let l=0,t=this;this.$refs[e].validate((e=>{e&&(t.form.content=this.$refs.ue.getUEContent(),""==t.form.content||null==t.form.content||null==t.form.content?a.error("请填写内容!"):(t.form.category="0",t.form.fileList=[],t.fileIds.length>0?t.form.picture_id=t.fileIds.join(","):t.form.picture_id="",v(t.form).then((e=>{l=e.code})).then((()=>{0==l?(a.success("操作成功"),t.editVisible=!1,t.query.pageIndex=1,t.form.fileList=[],t.fileIds=[],this.$refs.ue.setText(""),t.getData()):-1==l&&a.error("操作失败!")}))))}))}}},k={class:"container"},w={class:"handle-box"},U={class:"left-panel"},z={class:"right-panel"},D=r("div",{class:"clear"},null,-1),S={key:0},E={key:1},q={key:2},L={class:"pagination"},R={class:"dialog-footer"},T={class:"customWidth"},$=["src"];var H=C(I,[["render",function(e,l,a,t,_,b){const y=i("el-button"),v=i("el-input"),V=i("el-table-column"),x=i("el-table"),C=i("el-pagination"),I=i("el-form-item"),H=i("el-radio"),j=i("el-radio-group"),A=i("Ueditor"),P=i("el-form"),W=i("el-dialog"),F=d("permission");return o(),n("div",null,[r("div",k,[r("div",w,[r("div",U,[s((o(),c(y,{type:"primary",size:"small",icon:"el-icon-plus",onClick:l[0]||(l[0]=e=>t.handleEdit(0,null,!0,null))},{default:u((()=>[m("添加 ")])),_:1})),[[F,["road_filemanager_add","park_filemanager_add"]]]),s((o(),c(y,{type:"danger",size:"small",icon:"el-icon-delete",onClick:l[1]||(l[1]=e=>b.handleDeleteAll())},{default:u((()=>[m("批量删除 ")])),_:1})),[[F,["road_filemanager_deleteAll","park_filemanager_deleteAll"]]])]),r("div",z,[p(v,{onKeyup:l[2]||(l[2]=f((e=>t.handleSearch()),["enter"])),size:"small",modelValue:t.query.title,"onUpdate:modelValue":l[3]||(l[3]=e=>t.query.title=e),placeholder:"标题",class:"handle-input mr10"},null,8,["modelValue"]),p(y,{size:"small",type:"primary",icon:"el-icon-search",onClick:t.handleSearch},{default:u((()=>[m("查询 ")])),_:1},8,["onClick"])]),D]),p(x,{data:t.tableData,border:"",class:"table",ref:"multipleTable","max-height":_.tableH,"header-cell-class-name":"table-header",onSelectionChange:b.handleSelectionChange},{default:u((()=>[p(V,{type:"selection",width:"55",align:"center"}),p(V,{type:"index",label:"序号",width:"55",align:"center"},{default:u((e=>[m(h(e.$index+1),1)])),_:1}),p(V,{prop:"title",label:"标题",align:"center"}),p(V,{prop:"doc_type",label:"文档类别",align:"center",width:"200"},{default:u((e=>["0"==e.row.doc_type?(o(),n("p",S,"用户协议")):g("",!0),"1"==e.row.doc_type?(o(),n("p",E,"关于我们")):g("",!0),"2"==e.row.doc_type?(o(),n("p",q,"退款协议")):g("",!0)])),_:1}),p(V,{label:"创建时间",prop:"create_time",width:"180",align:"center"}),p(V,{prop:"update_time",label:"更新时间",align:"center",width:"180"}),p(V,{label:"操作",width:"220",align:"center"},{default:u((e=>[s((o(),c(y,{size:"mini",type:"text",icon:"el-icon-edit",onClick:l=>t.handleEdit(e.$index,e.row,!1)},{default:u((()=>[m("编辑 ")])),_:2},1032,["onClick"])),[[F,["road_filemanager_edit","park_filemanager_edit"]]]),s((o(),c(y,{size:"mini",type:"text",icon:"el-icon-delete",class:"red",onClick:l=>t.handleDelete(e.$index,e.row)},{default:u((()=>[m("删除 ")])),_:2},1032,["onClick"])),[[F,["road_filemanager_delete","park_filemanager_delete"]]])])),_:1})])),_:1},8,["data","max-height","onSelectionChange"]),r("div",L,[p(C,{background:"",layout:"total, prev, pager, next","current-page":t.query.pageIndex,"page-size":t.query.pageSize,total:t.pageTotal,onCurrentChange:t.handlePageChange},null,8,["current-page","page-size","total","onCurrentChange"])])]),p(W,{title:t.dialogT,modelValue:t.editVisible,"onUpdate:modelValue":l[9]||(l[9]=e=>t.editVisible=e),width:"35%",top:"2vh"},{footer:u((()=>[r("span",R,[p(y,{onClick:l[7]||(l[7]=e=>t.editVisible=!1)},{default:u((()=>[m("取 消")])),_:1}),p(y,{type:"primary",onClick:l[8]||(l[8]=e=>b.saveEdit("formId"))},{default:u((()=>[m("确 定")])),_:1})])])),default:u((()=>[p(P,{"label-width":"70px",size:"small",rules:_.formRules,ref:"formId","label-position":"top",model:t.form},{default:u((()=>[p(I,{label:"标题",prop:"title"},{default:u((()=>[p(v,{modelValue:t.form.title,"onUpdate:modelValue":l[4]||(l[4]=e=>t.form.title=e),placeholder:"请输入标题"},null,8,["modelValue"])])),_:1}),p(I,{label:"文档类别"},{default:u((()=>[p(j,{modelValue:t.form.doc_type,"onUpdate:modelValue":l[5]||(l[5]=e=>t.form.doc_type=e)},{default:u((()=>[p(H,{label:0},{default:u((()=>[m("用户协议")])),_:1}),p(H,{label:1},{default:u((()=>[m("关于我们")])),_:1}),p(H,{label:2},{default:u((()=>[m("退款协议")])),_:1})])),_:1},8,["modelValue"])])),_:1}),p(I,{label:"排序",prop:"sort"},{default:u((()=>[p(v,{modelValue:t.form.sort,"onUpdate:modelValue":l[6]||(l[6]=e=>t.form.sort=e),placeholder:"请输入排序",oninput:"value=value.replace(/[^\\d]/g,'')"},null,8,["modelValue"])])),_:1}),p(I,{label:"详情"},{default:u((()=>[p(A,{onReady:b.editorReady,ref:"ue",value:t.form.content,ueditorConfig:t.uform.config},null,8,["onReady","value","ueditorConfig"])])),_:1})])),_:1},8,["rules","model"])])),_:1},8,["title","modelValue"]),p(W,{modelValue:t.dialogVisible,"onUpdate:modelValue":l[10]||(l[10]=e=>t.dialogVisible=e)},{default:u((()=>[r("div",T,[r("img",{class:"imgWidth",src:t.dialogImageUrl,alt:""},null,8,$)])])),_:1},8,["modelValue"])])}]]);export{H as default};
