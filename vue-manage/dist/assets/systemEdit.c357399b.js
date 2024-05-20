import{b as e,u as l,r,o as a,h as t,k as o,l as u,m,i as s,q as i}from"./vendor.ac52fd94.js";import{_ as n,L as d,M as f}from"./index.1d57ac4a.js";const p={name:"systemparameteredit",setup(){const r=e({invoiceApplyTime:"",leaveTime:"",mergeTime:"",giftMoney:"",Discount:""}),a=e(null),t=l();d().then((e=>{r.value=e.data}));return{form:r,editform:a,formRules:{invoiceApplyTime:[{required:!0,message:"请输入申请发票时间",trigger:"blur"}],leaveTime:[{required:!0,message:"请输入驶离时间",trigger:"blur"}],mergeTime:[{required:!0,message:"请输入合并重复订单间隔时间",trigger:"blur"}],giftMoney:[{required:!0,message:"请输入关注后赠送金额",trigger:"blur"}],Discount:[{required:!0,message:"请输入残疾人折扣",trigger:"blur"}]},save:()=>{a.value.validate((e=>{e&&f(r.value).then((e=>{0===e.code&&(i.success("更新成功"),t.push("/systempara"))}))}))}}},methods:{goTo(){this.$router.push("/systempara")}}},g={class:"container"},c=s("br",null,null,-1);var y=n(p,[["render",function(e,l,s,i,n,d){const f=r("el-input"),p=r("el-form-item"),y=r("el-alert"),b=r("el-button"),v=r("el-form");return a(),t("div",g,[o(v,{"label-width":"170px","label-position":"left",rules:i.formRules,ref:"editform",model:i.form},{default:u((()=>[o(p,{label:"合并订单间隔时间：",prop:"mergeTime"},{default:u((()=>[o(f,{modelValue:i.form.mergeTime,"onUpdate:modelValue":l[0]||(l[0]=e=>i.form.mergeTime=e),placeholder:"单位：分钟",type:"number"},null,8,["modelValue"])])),_:1}),o(p,null,{default:u((()=>[o(y,{type:"info"},{default:u((()=>[m("1、由于行人经过遮挡或者闪光造成的视频杆设备误判，造成一个车在同一个泊位内频繁驶入驶出的假象，合并假象生成的订单"),c,m(" 2、本项设置为设置合并重复订单间隔时间，保存后即刻生效")])),_:1})])),_:1}),o(p,{label:"关注后赠送金额：",prop:"giftMoney"},{default:u((()=>[o(f,{modelValue:i.form.giftMoney,"onUpdate:modelValue":l[1]||(l[1]=e=>i.form.giftMoney=e),placeholder:"单位：元",type:"number"},null,8,["modelValue"])])),_:1}),o(p,null,{default:u((()=>[o(y,{type:"info"},{default:u((()=>[m("车主关注公众号后，赠送停车费，每个微信号仅限一次，赠送的停车费存入钱包，可供停车缴费使用，0表示不赠送")])),_:1})])),_:1}),o(p,{label:"残疾人折扣：",prop:"Discount"},{default:u((()=>[o(f,{modelValue:i.form.Discount,"onUpdate:modelValue":l[2]||(l[2]=e=>i.form.Discount=e),placeholder:"单位：%",type:"number"},null,8,["modelValue"])])),_:1}),o(p,null,{default:u((()=>[o(y,{type:"info"},{default:u((()=>[m("残疾人名单按此折扣计费")])),_:1})])),_:1}),o(p,null,{default:u((()=>[o(b,{type:"primary",class:"mt20",onClick:l[3]||(l[3]=e=>i.save())},{default:u((()=>[m("保存")])),_:1})])),_:1})])),_:1},8,["rules","model"])])}]]);export{y as default};
