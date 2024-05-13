
export const timestampToTime = () => {
    var today = new Date();
  
    //日期
    var DD = String(today.getDate()).padStart(2, "0"); // 获取日
    var MM = String(today.getMonth() + 1).padStart(2, "0"); //获取月份，1 月为 0
    var yyyy = today.getFullYear(); // 获取年
  
    // 时间
    var hh = String(today.getHours()).padStart(2, "0"); //获取当前小时数(0-23)
    var mm = String(today.getMinutes()).padStart(2, "0"); //获取当前分钟数(0-59)
    var ss = String(today.getSeconds()).padStart(2, "0"); //获取当前秒数(0-59)
    today = yyyy + MM + DD + hh + mm + ss;
    return today;
  }

  export const randomString = (len) => {
    len = len || 32;
    var $chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    var maxPos = $chars.length;
    var pwd = "";
    for (var i = 0; i < len; i++) {
      //0~32的整数
      pwd += $chars.charAt(Math.floor(Math.random() * (maxPos + 1)));
    }
    return pwd;
  }