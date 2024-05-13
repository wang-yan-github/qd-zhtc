//巡检员使用验证规则
import request from '../utils/request';
//设置密码为6-12位，字母+数字
export const logpwdMethod = (rule, value, callback) => {
    var y='^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$';
    if (!value) {
      return callback(new Error("请输入登陆密码"));
    } else {
      if(!y.test(value)){
        return callback(new Error("登陆密码必须是6-12位，字母+数字"));
      }else{
        callback();
      }
    }
  };
export const validatePhone = (rule, value, callback) => {
  var length = value.length;
  var mobile = /^(((13[0-9]{1})|(14[0-9]{1})|(19[0-9]{1})|(16[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
  if(!value){
    callback();
  }else{
    if(value.length != 11 || !mobile.test(value)){
      return callback(new Error("请输入正确的手机号"))
    }
  }

};

export const validatePrice = (rule, value, callback) => {
  if(!value){
    return callback(new Error("请输入价格"))
  }else{
    if (!Number.isInteger(value)) {
      callback(new Error('请输入数字值'));
    }
  }

};

