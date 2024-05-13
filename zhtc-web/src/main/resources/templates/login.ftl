﻿<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>登陆页面</title>
    <script type="text/javascript" src="../../libs/jquery.min.js" ></script>
    <script type="text/javascript" src="../../libs/js/html5.min.js" ></script>
    <link rel="stylesheet" href="../../libs/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="../../libs/css/login.css" type="text/css" />
    <style>
        body,html{position:relative;width:100%;height:100%;margin:0;padding:0;}
        .waves,.page_container,canvas {
            position:absolute;
            left:0;
            bottom:0;
            width:100%;
            height:100%;
            z-index:2;
            opacity:.3;
        }
        canvas {
            display:block;
        }

    </style>
</head>

<body>
<div class="fullpage-wrapper">
    <div class="fullpage-main"></div>
    <div class="validate-box" id="divSojump">
        <fieldset class="validate-wrapper" id="userlogin">
            <h1 class="validate-caption">登录</h1>
            <ul>
                <li>
                    <label class="icon mobile-icon"></label>
                    <input name="consumer" id="consumer" placeholder="" class="validate-input user-name" type="text"/>
                    <#--<input id="btnSendCode1" type="button" class="aui-btn-default" value="获取验证码" onclick="sendMessage1()">-->
                    <#--<div class="divclear"></div>-->
                </li>
                <li>
                    <label for="cypher" class="icon password-icon"></label>
                    <input name="cypher" type="password" id="cypher" placeholder="" class="validate-input" autocomplete="off"/>
                </li>
            </ul>
        </fieldset>
        <fieldset class="submit-wrapper" style="text-align: center; margin-top: 20px;" id="loginBox">
            <div class="subBtn">
                <button type="submit" name="LoginButton" text="登 录" id="LoginButton" class="submitbutton"style="color:White;" >登录</button>
            </div>
            <span style="color: red; line-height: 28px;" id="errormsg"></span>
        </fieldset>
    </div>
</form>
</div>
<canvas></canvas>
<script src="../../libs/layui/layui.js"></script>
<script src="../../libs/common.js"></script>
<script>
function LoginButton() {
    $.ajax({
        url:"../login.do",
        data:{"consumer":$("#consumer").val(),"cypher":$("#cypher").val()},
        type: 'POST',
        async: false,
        success: function (data) {
            var text = JSON.parse(data)
            if(text.success){
                layer.msg('登录成功！', {
                    icon: 1,
                    time: 2000
                }, function(){
                    window.location.href = "../main.do";
                });
            }else{
                layer.msg('系统问题！请联系技术人员调试！', {
                    icon: 2,
                    time: 2000
                }, function(){
                });
            }
        }
    })
}
</script>
</body>
</html>
