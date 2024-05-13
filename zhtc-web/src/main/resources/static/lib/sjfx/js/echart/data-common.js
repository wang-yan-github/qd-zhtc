var random = {
    /**获取两个数之间的随机小数(fixed是有效数字位数)*/ getDecimal: function (a, b, fixed) {
        var num = Math.random() * (a - b) + b;
        fixed && (num = Math.round((num) * fixed) / fixed);
        return num;
    }, /**获取两个数之间的随机整数*/ getInteger: function (a, b) {
        return this.getDecimal(a, b, 1);
    }, get: function (a, b) { /**返回a到b且包含a和b的随机整数(其中a,b不能为负数)*/
        if (arguments.length == 1) {
            (b = a, a = 0);
        } else {
            a < 0 && (a = 0);
            b < 0 && (b = 0);
            a = a || 0;
            b = b || 9;
        }
        return Math.round(Math.random() * b + a)
    }, valueFromArray: function (arr) { /**获取数组中的随机一个元素*/
        if (arr && arr.length > 0) {
            return arr[random.get(arr.length - 1)];
        } else {
            return arr;
        }
    }
};


/**禁止拖拽保存图片*/function donotDrag() {
    window.ondragstart = window.ontouchmove = document.ondragstart = document.ontouchmove = function (e) {
        e.preventDefault();
        return false;
    };
};donotDrag();


/**禁止使用右键菜单-------------------------------------------------------------------------*/
oncontextmenu = function () {
    return true;
};


/**绑定数据神器*/ 
var bind = {
    set: function (h, l, v) {
        if (!h) return console.log("html传参为空", l, v);
        return h.replace(new RegExp("{" + l + "}", "g"), v).replace(new RegExp("\\[" + l + "\\]", "g"), v);
    }, object: function (h, o) {
        for (var a in o) var b = o[a], h = this.set(h, a, b);
        return h;
    }, a: function (html, obj) {
        return this.object(html, obj);
    }
};

/**【测试专用】获取昨天、明天日期、任意距离今天N天的日期-------------------------------------------------------------------------*/
var testDate = {
    __getDate: function (day) {
        day = day == undefined ? -1 : day;
        var now = new Date();
        var nowDay = now.getDate(); //当前日
        var nowMonth = now.getMonth(); //当前月
        var nowYear = now.getFullYear(); //当前年
        var lastDate = new Date(nowYear, nowMonth, nowDay + day);
        var year_Month_Day = ("0" + (lastDate.getMonth() + 1)).slice(-2) + "/" + ("0" + lastDate.getDate()).slice(-2)
        return year_Month_Day;
    },
    getLastDates: function (day, isIncludeToday) {
        day || (day = 7);
        var thisWeek = []
        for (var i = (isIncludeToday ? 1 : 0) - 1 * day; i < (isIncludeToday ? 1 : 0); i++) {
            thisWeek.push(this.__getDate(i));
        }
        return thisWeek;
        /*最近7天________________________*/
        // lineChartOption.xAxis.data = thisWeek;//["01/01", "01/02", "01/03", "01/04", "01/05", "01/06", "01/07"]
        /**-------------------------------------------------------------------------*/
    }
}

var cookie = {
    getAll: function () {
        var arr = unescape(document.cookie).split("; "), o = {};
        for (var i = 0, len = arr.length; i < len; i++) {
            var a = arr[i], index = a.indexOf("="), b = a.substr(0, index), c = a.substr(index + 1);
            o[b] = c
        }
        return o;
    }, save: function (obj, day) {
        for (var a in obj) {
            var b = obj[a];
            this.set(a, b, day);
        }
    }, dels: function (arr) {
        for (var i in arr) {
            var a = arr[i];
            this.del(a);
        }
    }, set: function (key, val, day) {
        if (val == null || val == undefined) return;
        var k = key, d = day, d = d || 1, e = new Date();
        e.setDate(e.getDate() + d);
        val instanceof Array && (val = val.join("||")), val instanceof Object && (val = JSON.stringify(val));
        document.cookie = k + "=" + escape(val.toString()) + ((d == null) ? "" : ";expires=" + e.toGMTString()) + "; path=/";
    }, get: function (key) {
        var k = key;
        if (document.cookie.length > 0) {
            var s = document.cookie.indexOf(k + "=");
            if (s != -1) {
                s = s + k.length + 1;
                var e = document.cookie.indexOf(";", s);
                if (e == -1) e = document.cookie.length;
                return unescape(document.cookie.substring(s, e));
            }
        }
        return "";
    }, getJOSN: function (key) {
        return JSON.parse(this.get(key));
    }, getArray: function (key) {
        return this.get(key).split("||");
    }, del: function (key) {
        var k = key, e = new Date(), val = this.get(k)
        e.setTime(e.getTime() - 1);
        if (val != null) document.cookie = k + "=" + val + ";expires=" + e.toGMTString() + "; path=/";
    }, clearAll: function () {
        var k = document.cookie.match(/[^ =;]+(?=\=)/g);
        if (k) {
            for (var i = k.length; i--;) document.cookie = k[i] + '=0;expires=' + new Date(0).toUTCString() + "; path=/";
        }
    }, setToken: function (val, day) {
        this.set("token", val, day)
    }, getToken: function () {
        return this.get("token")
    }, setTempToken: function (val, day) {
        this.set("tempToken", val, day)
    }, getTempToken: function () {
        return this.get("tempToken")
    }
};
/*测试用例*/// cookie.save({username: "admin", password: "123456"}, 365);/*批量存储*/// cookie.dels(["username", "password"]);/*批量删除*/// console.log(cookie.getAll());//获取所有缓存数据转换为对象
/*
/!*测试用例*!/
cookie.save({username: "admin", password: "123456"}, 365);
/!*批量存储*!/
cookie.dels(["username", "password"]);
/!*批量删除*!/
console.log(cookie.getAll());//获取所有缓存数据转换为对象
*/

var broswer = {
    /**浏览器可视区域宽度高度（不含纵向滚动条宽度和水平滚动条高度）*/ visibleWidth: function () {
        return document.documentElement.clientWidth || document.body.clientWidth
    }, visibleHeight: function () {
        return document.documentElement.clientHeight || document.body.clientHeight
    }, update: function () {
        /**升级浏览器*/ var s = this.version(), b = s.browser, v = s.version.split('.')[0], a = function () {
            alert('朋友！\n您还在用大家都抛弃了的浏览器版本吗？\n淘宝都不支持这个浏览器了！\n还是点击确定更新下吧！\n与世界接轨很有必要！\n祝您早日富可敌国！');
            location.replace("https://down.360safe.com/se/360se_setup.exe");
        };
        b == "chrome" && v < 55 && a();
        b == "firefox" && v < 60 && a();
        b == "msie" && v < 10 && a();
        b == "opera" && v < 55 && a();
        /*document.write(b + "的版本是" + s.version);*/
    }, version: function () {
        /**获取浏览器版本*/ var s = {}, m = navigator.userAgent.toLocaleLowerCase().match(/(msie|firefox|chrome|opera|version).*?([\d.]+)/);
        s.browser = m[1].replace(/version/, "'safari"), s.version = m[2];
        return s;
    }
};
/*测试用例*/
/*
broswer.update();*/

/**自定义AJAX 2.0.2*/var $G = {
    ajax: function (o) {
        var p = o.post, g = o.get, d = p.data || {}, a = p.async, J = 'json', j = p[J], s = g.success, e = g.error;
        if (!p.noToken) {
            var url = p.url.toLocaleLowerCase();
            if (/select/.test(url) || /get/.test(url) || /list/.test(url) || /find/.test(url)) {
                d.token = d.token || cookie.getTempToken();
                if (!d.token) return console.log("tempToken为空");
            } else {
                d.token = d.token || cookie.getToken();
                if (!d.token) return toLogin();
            }
        }
        d = {
            async: a == undefined || a, /* false(解决手机端同步多次请求接口报错的问题)*/ timeout: 60000, /*超时时间:60秒*/ type: p.type || "post", url: p.url, data: d, success: function (r) {
                s && s(r);
            }, error: function (r) {
                e && e(r);
            },
        };
        j ? (d.data = JSON.stringify(d.data), d.contentType = 'application/' + J) : (d.dataType = J), $.ajax(d);
    }
};

var tempToken = {
    init: function (api) {
        if (!cookie.getTempToken()) {
            $G.ajax({
                post: {url: api, data: {access_token: "tempToken"}, json: true, noToken: true}, get: {
                    success: function (d) {
                        if (d.code == 0) {
                            cookie.setTempToken("tempToken", 365);
                        }
                    },
                }
            });
        }
    }
};


