'use strict';
let __QYVIDEOIDX = 0;
let __QYVIDEOLOGENABLED = true;

/**
 * 芊熠视频组件
 * @Author 邓振祥
 * @Date 2022-01-10
 * @LastUpdate 2022-08-03
 * @baseUrlParam 连接URL前缀参数，一般为：https://qy-vds.com
 * @properties JSON,定义视频属性{"onconnectionstatechange":function(target,state){},"style":{}}
 */
function QyVideo(baseUrlParam,properties) {
    //this.baseUrl = baseUrlParam || (window.location.protocol+":"+window.location.host+":"+window.location.port);
    this.baseUrl = baseUrlParam || "";
    this.videoId = "v"+(new Date()).getTime()+"_"+(++__QYVIDEOIDX); //+"_"+Math.round(Math.random()*9999);
    this.video = null;
    this.playing = false;
    this.videoContainerId = null;
    this.videoInfo = null;

    //webrtc
    this.session = null;

    //wsmjpeg
    this.webSocket = null;
    this.wsVideoReader = null;
    this.wsLastReceiveData = (new Date()).getTime();

    //httpflv
    this.player = null;

    //连接状态变更事件监听
    this.onconnectionstatechange = null;

    //视频控件(html5 video)样式
    this.style = {
        "style":"object-fit:fill;display: block;width: 100%;height: 100%;",
        "autoplay":true,
        "controls":false,
        "controlsList":"nodownload",
        "muted":true,
        "mode":"scaleToFill",
        "x5-video-player-fullscreen":"true",
        "x5-video-player-type":"h5",
        "x5-playsinline":true,
        "preload":"auto",
        "playsinline":"true",
        "webkit-playsinline":"true",
        "poster":"data:image/gif;base64,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"
    };

    if(properties){
        if(properties.onconnectionstatechange){
            this.onconnectionstatechange = properties.onconnectionstatechange;
        }

        if(properties.style) {
            for (const key in properties.style) {
                this.style[key] = properties.style[key];
            }
        }
        if(typeof(properties.logEnabled) != "undefined"){
            __QYVIDEOLOGENABLED = properties.logEnabled;
        }
    }


    //QyVideo.Log(this.style);

    const tmpLoop = (function(evt){
        this.loop();
    }).bind(this);
    this.timer = window.setInterval(tmpLoop,5000);
};

/**
 * 视频编码格式
 * @type {{H264: string, MJPEG: string}}
 */
QyVideo.Encoder = {
    "MJPEG":"MJPEG",
    "H264":"H264"
};

/**
 * 视频播放类别
 */
QyVideo.PlayType = {
    "AUTO":"auto",
    "WEBRTC":"webrtc",
    "HTTPFLV":"httpflv",
    "HTTPM3U8":"httpm3u8",
    "WSMJPEG":"wsmjpeg",
    "RTMP":"rtmp"
};

/**
 * 视频信息
 * @constructor
 */
QyVideo.VideoInfo = function(){
    this.encoder = null;
    this.playUrl = null;
    this.poster = null;
    this.width = -1;
    this.height = -1;

    this.getEncoder = function () {
        return this.encoder;
    };

    this.getPlayUrl = function () {
        return this.playUrl;
    };

    this.getWidth = function () {
        return this.width;
    };

    this.getHeight = function () {
        return this.height;
    }
};

QyVideo.Log = function(formatstr,obj) {
    if(__QYVIDEOLOGENABLED) {
        var title = '[' + QyVideo.FormatDate(new Date(), 'yyyy-MM-dd hh:mm:ss') + '] [QyVideo] ' + formatstr;
        if (obj) {
            console.log(title, obj);
        } else {
            console.log(title);
        }
    }
};

/**
 * 静态函数：ajax调用
 * @param method
 * @param url
 * @param data
 * @param successCallback
 * @param failureCallback
 * @param async 是否异步操作，true:异常，false:同步
 */
QyVideo.Ajax = function(method, url, data, successCallback, failureCallback,async,callbackParam) {
    if(typeof(async) == "undefined"){
        async = true;
    }
    //QyVideo.Log("ajax call: %s => %o",url,data);
    // 异步对象
    let ajax = new XMLHttpRequest();

    if(async) {
        ajax.timeout = 60000;
    }

    // 注册事件
    ajax.onreadystatechange = function () {
        // 在事件中 获取数据 并修改界面显示
        let status = ajax.status;
        //QyVideo.Log("ajax.readyState = "+ajax.readyState);
        if (status === 200) {
            //QyVideo.Log("RESULT : %s",ajax.responseText);
            if(ajax.readyState === 4) {
                successCallback(JSON.parse(ajax.responseText),callbackParam);
            }
        }
        else if(status >=400 && status <= 505){
            let msg = status;
            if(ajax.responseText){
                msg += ":"+ajax.responseText;
            }
            failureCallback(msg,callbackParam);
        }
    }

    if (method === 'get') {
        if (data) {
            url+='?';
            url+=data;
        }
        ajax.open(method,url,async);
        ajax.send();
    }
    else{
        ajax.open(method,url,async);
        if (data) {
            let isjson = typeof(data) == "object" && Object.prototype.toString.call(data).toLowerCase() == "[object object]" && !data.length;

            if(isjson){
                ajax.setRequestHeader("Content-type","application/json;charset=UTF-8");
                ajax.send(JSON.stringify(data));
            }
            else{
                ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                ajax.send(data);
            }
        }else{
            ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            ajax.send();
        }
    }
};

/**
 * 静态函数：按需动态加载JavasScript库
 * @param url JS文件URL
 * @param callback 加载成功后的回调函数
 * @param scope 作用域，将作为参数传递给回调函数
 */
QyVideo.LoadJs = function(url, callback, scope) {
    const script = document.createElement('script'), fn = callback || function () {};
    script.type = 'text/javascript';
    //IE
    if(script.readyState){
        script.onreadystatechange = function(){
            if( script.readyState == 'loaded' || script.readyState == 'complete' ){
                script.onreadystatechange = null;
                fn(scope);
            }
        };
    }else{
        //其他浏览器
        script.onload = function(){
            fn(scope);
        };
    }
    //QyVideo.Log("load:"+this.baseUrl);
    let tmpurl = scope.baseUrl || "";
    tmpurl += url;
    script.src = tmpurl;

    document.getElementsByTagName('head')[0].appendChild(script);
};

QyVideo.FormatDate = function(date,fmt) {
    var o = {
        "M+" : date.getMonth()+1,                 //月份
        "d+" : date.getDate(),                    //日
        "h+" : date.getHours(),                   //小时
        "m+" : date.getMinutes(),                 //分
        "s+" : date.getSeconds(),                 //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S"  : date.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}

QyVideo.AppendHTML = function(ele,html){
    let divTemp = document.createElement("div");
    divTemp.innerHTML = html;
    let nodes = divTemp.childNodes;
    let fragment = document.createDocumentFragment();
    for (var i=0, length=nodes.length; i<length; i+=1) {
        fragment.appendChild(nodes[i].cloneNode(true));
    }
    ele.appendChild(fragment);
    nodes = null;
    fragment = null;
};

QyVideo.SrsRtcPlayerAsync = function (connectionStateChangeHandle,schema) {
    const self = {};
    self.connectionStateChangeHandle = connectionStateChangeHandle || function(){};
    self.schema = schema;
    self.play = async function(url) {
        const conf = self.__internal.prepareUrl(url);
        //self.pc.addTransceiver("audio", {direction: "recvonly"});
        self.pc.addTransceiver("video", {direction: "recvonly"});

        const offer = await self.pc.createOffer();
        await self.pc.setLocalDescription(offer);
        const session = await new Promise(function (resolve, reject) {
            // @see https://github.com/rtcdn/rtcdn-draft
            var data = {
                api: conf.apiUrl, tid: conf.tid, streamurl: conf.streamUrl,
                clientip: null, sdp: offer.sdp
            };
            //QyVideo.Log("Generated offer: ", data);

            var tmpurl = conf.apiUrl.replace("file://", "http://");
            //QyVideo.Log(tmpurl);
            QyVideo.Ajax('post', tmpurl, data, function (response) {
                //QyVideo.Log("Got answer: ", response);
                if (response.code) {
                    reject(response);
                    return;
                }

                resolve(response);
            }, function (reason) {
                reject(reason);
            });

        });
        if(self.pc && self.pc.setRemoteDescription) {
            await self.pc.setRemoteDescription(
                new RTCSessionDescription({type: 'answer', sdp: session.sdp})
            );
        }
        session.simulator = conf.schema + '//' + conf.urlObject.server + ':' + conf.port + '/rtc/v1/nack/';

        return session;
    };

    // Close the player.
    self.close = function() {
        self.pc && self.pc.close();
        self.pc = null;
    };

    // The callback when got remote track.
    // Note that the onaddstream is deprecated, @see https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnection/onaddstream
    self.ontrack = function (event) {
        // https://webrtc.org/getting-started/remote-streams
        self.stream.addTrack(event.track);
    };

    // Internal APIs.
    self.__internal = {
        defaultPath: '/rtc/v1/play/',
        prepareUrl: function (webrtcUrl) {
            //alert(webrtcUrl);
            const urlObject = self.__internal.parse(webrtcUrl);

            // If user specifies the schema, use it as API schema.
            let schema = urlObject.user_query.schema;
            schema = schema ? schema + ':' : window.location.protocol;
            schema = self.schema || schema;

            var port = urlObject.port || 1985;
            if (schema === 'https:') {
                port = 443;
            }

            //alert(schema+"\t"+port);

            // @see https://github.com/rtcdn/rtcdn-draft
            var api = urlObject.user_query.play || self.__internal.defaultPath;
            if (api.lastIndexOf('/') !== api.length - 1) {
                api += '/';
            }

            let apiUrl = schema + '//' + urlObject.server + ':' + port + api;
            for (var key in urlObject.user_query) {
                if (key !== 'api' && key !== 'play') {
                    apiUrl += '&' + key + '=' + urlObject.user_query[key];
                }
            }
            // Replace /rtc/v1/play/&k=v to /rtc/v1/play/?k=v
            apiUrl = apiUrl.replace(api + '&', api + '?');

            let streamUrl = urlObject.url;

            return {
                apiUrl: apiUrl, streamUrl: streamUrl, schema: schema, urlObject: urlObject, port: port,
                tid: Number(parseInt(new Date().getTime()*Math.random()*100)).toString(16).substr(0, 7)
            };
        },
        parse: function (url) {
            // @see: http://stackoverflow.com/questions/10469575/how-to-use-location-object-to-parse-url-without-redirecting-the-page-in-javascri
            const a = document.createElement("a");
            a.href = url.replace("rtmp://", "http://")
                .replace("webrtc://", "http://")
                .replace("rtc://", "http://");

            let vhost = a.hostname;
            let app = a.pathname.substr(1, a.pathname.lastIndexOf("/") - 1);
            let stream = a.pathname.substr(a.pathname.lastIndexOf("/") + 1);

            // parse the vhost in the params of app, that srs supports.
            app = app.replace("...vhost...", "?vhost=");
            if (app.indexOf("?") >= 0) {
                var params = app.substr(app.indexOf("?"));
                app = app.substr(0, app.indexOf("?"));

                if (params.indexOf("vhost=") > 0) {
                    vhost = params.substr(params.indexOf("vhost=") + "vhost=".length);
                    if (vhost.indexOf("&") > 0) {
                        vhost = vhost.substr(0, vhost.indexOf("&"));
                    }
                }
            }

            // when vhost equals to server, and server is ip,
            // the vhost is __defaultVhost__
            if (a.hostname === vhost) {
                var re = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/;
                if (re.test(a.hostname)) {
                    vhost = "__defaultVhost__";
                }
            }

            // parse the schema
            let schema = "rtmp";
            if (url.indexOf("://") > 0) {
                schema = url.substr(0, url.indexOf("://"));
            }

            let port = a.port;
            if (!port) {
                if (schema === 'http') {
                    port = 80;
                } else if (schema === 'https') {
                    port = 443;
                } else if (schema === 'rtmp') {
                    port = 1935;
                }
            }

            const ret = {
                url: url,
                schema: schema,
                server: a.hostname, port: port,
                vhost: vhost, app: app, stream: stream
            };
            self.__internal.fill_query(a.search, ret);

            // For webrtc API, we use 443 if page is https, or schema specified it.
            if (!ret.port) {
                if (schema === 'webrtc' || schema === 'rtc') {
                    if (ret.user_query.schema === 'https') {
                        ret.port = 443;
                    } else if (window.location.href.indexOf('https://') === 0) {
                        ret.port = 443;
                    } else {
                        // For WebRTC, SRS use 1985 as default API port.
                        ret.port = 1985;
                    }
                }
            }

            return ret;
        },
        fill_query: function (query_string, obj) {
            // pure user query object.
            obj.user_query = {};

            if (query_string.length === 0) {
                return;
            }

            // split again for angularjs.
            if (query_string.indexOf("?") >= 0) {
                query_string = query_string.split("?")[1];
            }

            let queries = query_string.split("&");
            for (let i = 0; i < queries.length; i++) {
                const elem = queries[i];

                var query = elem.split("=");
                obj[query[0]] = query[1];
                obj.user_query[query[0]] = query[1];
            }

            // alias domain for vhost.
            if (obj.domain) {
                obj.vhost = obj.domain;
            }
        }
    };

    self.pc = new RTCPeerConnection(null);

    // Create a stream to add track to the stream, @see https://webrtc.org/getting-started/remote-streams
    self.stream = new MediaStream();

    // https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnection/ontrack
    self.pc.ontrack = function(event) {
        if (self.ontrack) {
            self.ontrack(event);
        }
    };

    self.pc.onconnectionstatechange = function(event) {
        self.connectionStateChangeHandle(self.pc.connectionState);
    };

    return self;
};

QyVideo.prototype.isIos = function () {
    let ua = window.navigator.userAgent.toLowerCase();
    let isIOS = /iphone|ipad|ipod/.test(ua);
    return isIOS;
};

QyVideo.prototype.isMobile = function () {
    let ua = window.navigator.userAgent.toLowerCase();
    let isMobile = /ipad|iphone|midp|rv:1.2.3.4|ucweb|android|windows ce|windows mobile/.test(ua);
    return isMobile;
};

QyVideo.prototype.isWx = function () {
    let ua = window.navigator.userAgent.toLowerCase();
    let isWx = ua.match(/MicroMessenger/i)==="micromessenger";
    return isWx;
};

/**
 * 自动播放（移动端）
 */
QyVideo.prototype.autoplay = function () {
    if (window.wx) {
        const autoPlay = (function () {
            this.video.load();
            this.video.play();
        }).bind(this);

        wx.ready(autoPlay);
    } else {
        QyVideo.LoadJs("http://res.wx.qq.com/open/js/jweixin-1.6.0.js", function (scope) {
            //alert('scope video = '+scope.video);
            wx.config({
                debug: false,
                appId: '1',
                timestamp: '1',
                nonceStr: '1',
                signature: '1',
                jsApiList: []
            });
            wx.ready(() => {
                scope.video.load();
                scope.video.play();
            });
        }, this);
    }
};

/**
 * 读取视频信息
 * @param param JSON对象，格式：{"appId":"","serial":"","timestamp":"20220103183925","requestId":"","sign":"","playType":""}
 * appId 项目ID
 * serial 设备SN编号
 * timestamp 时间戳，格式：yyyyMMddHHmmss，时区：UTC+8（北京时间），与标准时间相差超过30分钟会返回错误
 * requestId 请求ID，每次请求保持唯一，推荐使用UUID
 * sign 签名，生成方式：md5(appId+requestId+serial+appSecret+timestamp).toLowerCase()
 * playType 播放类型，auto:自动根据设备端辅码流编码方式返回播流地址; webrtc: 设备端辅码流编码方式为H264时有效，返回webrtc播流地址;rtmp:设备端辅码流编码方式为H264时有效，返回rtmp播流地址,wsmjpeg:设备端辅码流编码方式为MJPEG时有效，返回websocket播流地址.
 * @param successCallback function,读取视频信息接口调用成功后的回调函数，返回JSON对象，格式：{"success":true,"msg":"","data":{"encoder":"","playType":"","playUrl":"","poster":""}}
 * @param failureCallback function,读取视频信息接口调用失败后的回调函数
 */
QyVideo.prototype.readVideoInfo = function (param, successCallback, failureCallback) {
    //如果当前访问的页面为https,则强制使用ssl
    let protocol = window.location.protocol;
    if ('https:' === protocol) {
        param.ssl = true;
    }
    else{
        //如果未定义ssl,则缺省ssl为true
        if(typeof(param.ssl) == "undefined"){
            param.ssl = true;
        }
    }

    if(typeof(param.playType) == "undefined"){
        param.playType = QyVideo.PlayType.AUTO;
    }

    const url = param.url || (this.baseUrl + "/api/video/stream?r="+Math.random());
    QyVideo.Log("请求视频参数：%o",param);

    QyVideo.Ajax("post"
        ,url
        //,{"serial":param.serial,"appId":param.appId,"timestamp":param.timestamp,"requestId":param.requestId,"sign":param.sign,"ssl":param.ssl}
        ,param
        ,function(response){
            QyVideo.Log("读取视频信息成功：%o",response);
            successCallback(response);
        }
        ,function(response){
            QyVideo.Log("读取视频信息失败：%o",response);
            failureCallback(response);
        });
};

/**
 * @param videoInfo {"encoder":"","playType":"","playUrl":"","poster":""}
 * @param videoContainerId
 */
QyVideo.prototype.createVideo = function(videoInfo, videoContainerId){
    this.video = document.getElementById(this.videoId);
    if(this.video) {
        //QyVideo.Log("exist:%s",this.videoId);
    }
    else{
        let videoContainer = document.getElementById(videoContainerId);
        if(videoContainer == null){
            console.error("未找到视频容器："+videoContainerId);
            return false;
        }

        let v = [];
        v.push("<video id=\""+this.videoId+"\"");

        //（兼容旧版本）如果外部定义了h5 video元素的属性，则使用外部属性
        if(videoInfo.control){
            for(const key in videoInfo.control){
                this.style[key] = videoInfo.control[key];
            }
        }

        for(const key in this.style){
            let val = this.style[key];
            if(typeof val === 'boolean'){
                if(val){
                    v.push(key);
                }
            }
            else {
                v.push(key + "=\"" + this.style[key] + "\"");
            }
        }

        if(videoInfo.poster) {
            v.push("poster=\"" + this.baseUrl + videoInfo.poster + "\"");
        }
        v.push("/>");

        QyVideo.AppendHTML(videoContainer,v.join(" "));
        this.video = document.getElementById(this.videoId);

        //移动端添加点击播放,解决某些OS不自动播放需要人工触发的情况
        if(this.isMobile()) {
            const clickEvent = (function() {
                if ('ontouchend' in document.documentElement === true)
                    return 'touchend';
                else
                    return 'click';
            })();

            this.video.addEventListener(clickEvent, function (event) {
                event.target.load();
                event.target.play();
                /*if(window.layer) {
                    layer.msg(event.type, {icon: 1, time: 1500});
                }*/
            });
        }
    }

    return true;
};

/**
 * 播放视频
 * @param videoInfo 通过readVideoInfo读取到的视频信息
 * @param videoContainerId 视频容器ID，一般为div元素的id
 * @param style 控件属性，用于定义视频控件的样式，缺省样式为：
 */
QyVideo.prototype.play = function (videoInfo, videoContainerId) {
    this.destroy();
    this.videoInfo = videoInfo;
    this.videoContainerId = videoContainerId;

    if(QyVideo.Encoder.MJPEG == videoInfo.encoder){
        this.playMjpeg(videoInfo,videoContainerId);
    }
    else{
        //if(videoInfo.playUrl.match(new RegExp("^webrtc://.*$"))){
        if(QyVideo.PlayType.WEBRTC == videoInfo.playType){
            if (window.QyVideoAdapterLoadCheck){
                this.playH264UseWebrtc(videoInfo, videoContainerId);
            }
            else{
                QyVideo.LoadJs("/public/js/qyvideo-adapter-7.4.0.min.js", function (scope) {
                    scope.playH264UseWebrtc(videoInfo, videoContainerId);
                }, this);
            }
        }
        else if(QyVideo.PlayType.HTTPM3U8 == videoInfo.playType){
            this.playH264UseHttpm3u8(videoInfo,videoContainerId);
        }
        else {
            //有引入flvjs
            if (window.flvjs) {
                this.playH264UseFlvjs(videoInfo, videoContainerId);
            } else {
                //尝试自动加载flvjs
                //alert('请在页面中引入flvjs！');
                QyVideo.LoadJs("/plugins/flvjs-v2/flv.min.js", function (scope) {
                    flvjs.LoggingControl.enableVerbose = false;
                    flvjs.LoggingControl.enableDebug = false;
                    flvjs.LoggingControl.enableInfo = false;
                    flvjs.LoggingControl.enableWarn = false;
                    scope.playH264UseFlvjs(videoInfo, videoContainerId);
                }, this);
            }
        }
    }
};

/**
 * 使用flvjs播放H264编码的HTTP-FLV视频
 * @param videoInfo QyVideo.VideoInfo
 * @param videoContainerId 视频容器ID
 */
QyVideo.prototype.playH264UseFlvjs = function(videoInfo, videoContainerId){
    this.createVideo(videoInfo,videoContainerId);

    var mediaDataSource = {
        type: 'flv',
        isLive: true,
        withCredentials: false,
        hasAudio: false,
        hasVideo: true,
        url: videoInfo.playUrl
    };

    if (this.player) {
        try {
            this.player.unload();
        }catch (e) {}
        try {
            this.player.detachMediaElement();
        }catch (e) {}
        try {
            this.player.destroy();
        }catch (e) {}
        this.player = null;
    }

    this.player = flvjs.createPlayer(mediaDataSource, {
        enableWorker: false,
        enableStashBuffer: false,
        stashInitialSize: 64,
        lazyLoad: false,
        lazyLoadMaxDuration: 1,
        lazyLoadRecoverDuration: 1,
        deferLoadAfterSourceOpen: false,
        autoCleanupMaxBackwardDuration: 1,
        autoCleanupMinBackwardDuration: 1,
        statisticsInfoReportInterval: 1,
        fixAudioTimestampGap: false
    });
    this.player.attachMediaElement(this.video);
    try {
        this.player.load();

        const delayPlay = (function(evt){
            this.player.play();
        }).bind(this);
        window.setTimeout(delayPlay,200);
    }catch (e) {
        QyVideo.Log(e);
    }

    const flvjsEventError = (function(errorType,errorDetail, event){
        this.playing = false;
        console.warn(arguments);
    }).bind(this);

    this.player.on(flvjs.Events.ERROR, flvjsEventError);
    this.playing = true;
    return true;
};

/**
 * 使用WS播放MJPEG视频
 * @param videoInfo QyVideo.VideoInfo
 * @param videoContainerId 视频容器ID
 * @returns {boolean}
 */
QyVideo.prototype.playMjpeg = function(videoInfo, videoContainerId){
    if (window.WebSocket) {
        this.wsLastReceiveData = (new Date()).getTime();
    }
    else{
        console.error("当前浏览器不支持WebSocket，无法播放MJPEG视频！");
        //console.error("当前浏览器不支持WebSocket，无法播放视频！");
        return false;
    }

    //<img id="j_video" name="videoElement" frameborder class="centeredVideo" style="object-fit:fill;" src="/public/img/loading.png"/>
    this.video = document.getElementById(this.videoId);
    if(this.video) {
        //QyVideo.Log("exist:%s",this.videoId);
    }
    else{
        const v = document.createElement("img");
        v.id = this.videoId;
        v.style = "object-fit: fill;width: 100%;height: 100%;";
        if(videoInfo.poster){
            v.src = this.baseUrl + videoInfo.poster;
        }
        else {
            v.src = this.baseUrl + "/public/img/loading.png";
        }

        //自定义img属性
        if(videoInfo.control){
            for(const key in videoInfo.control){
                v[key] = videoInfo.control[key];
            }
        }

        var videoContainer = document.getElementById(videoContainerId);
        videoContainer.appendChild(v);
        this.video = document.getElementById(this.videoId);
    }

    if(this.wsVideoReader){
        this.wsVideoReader.onload = null;
        this.wsVideoReader.abort();
        this.wsVideoReader = null;
    }

    if(this.webSocket){
        this.webSocket.onopen = null;
        this.webSocket.onmessage = null;
        this.webSocket.onclose = null;
        this.webSocket.onerror = null;
        this.webSocket.close();
    }

    this.wsVideoReader = new FileReader();
    const mjpegVideoRead = (function(evt){
        if (evt.target.readyState == FileReader.DONE) {
            var result = this.wsVideoReader.result;
            if(result) {
                this.video.src = result;
            }
        }
    }).bind(this);
    this.wsVideoReader.onload = mjpegVideoRead;

    this.webSocket = new WebSocket(videoInfo.playUrl);
    const webSocketOnOpen = (function(evt){
        if (this.webSocket.readyState == 1) {
            this.playing = true;
            //QyVideo.Log("MJPEG视频服务器连接成功！");
        } else {
            this.playing = false;
            console.warn("MJPEG视频服务器连接失败！");
        }
    }).bind(this);
    const webSocketOnMessage = (function(event){
        try {
            this.wsLastReceiveData = (new Date()).getTime();
            this.wsVideoReader.readAsDataURL(event.data);
        }catch (e) {}
    }).bind(this);
    const webSocketOnClose = (function(event){
        this.playing = false;
        console.info("mjpeg ws closed!")
    }).bind(this);
    const webSocketOnError = (function(event){
        this.playing = false;
        console.info("mjpeg ws error!")
    }).bind(this);

    this.webSocket.onopen = webSocketOnOpen;
    this.webSocket.onmessage = webSocketOnMessage;
    this.webSocket.onclose = webSocketOnClose;
    this.webSocket.onerror = webSocketOnError;

    return true;
};

/**
 * 播放WEBRTC视频流
 * @param videoInfo QyVideo.VideoInfo
 * @param videoContainerId 视频容器ID
 * @returns {boolean}
 */
QyVideo.prototype.playH264UseWebrtc = function(videoInfo, videoContainerId){
    this.createVideo(videoInfo,videoContainerId);
    //this.video.show();

    if(this.webrtcSdk){
        this.webrtcSdk.close();
        this.webrtcSdk.cache = null;
    }

    const onConnectionstateChangeHandle = (function(state){
        QyVideo.Log("onconnectionstatechange = %s ！",state);
        switch(state) {
            case "connected":
                this.playing = true;
                if(this.onconnectionstatechange) {
                    this.onconnectionstatechange(this,state);
                }
                break;
            case "disconnected":
                this.playing = false;
                break;
            case "failed":
                this.playing = false;
                break;
            case "closed":
                this.playing = false;
                break;
        }
    }).bind(this);

    this.webrtcSdk = new QyVideo.SrsRtcPlayerAsync(onConnectionstateChangeHandle,this.baseUrl.startsWith("https:")?"https:":null);

    this.video.srcObject = this.webrtcSdk.stream;

    const webrtcPlaySuccess = (function(session){
        //QyVideo.Log("sessionid = %s , simulator = %s",session.sessionid,session.simulator);
        this.session = session;
        this.playing = true;

        //IOS下自动播放
        if(this.isMobile()) {
            this.autoplay();
        }
    }).bind(this);
    const webrtcPlayFailure = (function(reason){
        if (reason) {
            console.error(reason);
        }
        this.playing = false;
    }).bind(this);

    this.webrtcSdk.play(videoInfo.playUrl).then(webrtcPlaySuccess).catch(webrtcPlayFailure);

    return true;
};

/**
 * 使用video元素播放HTTP-M3U8视频流
 * @param videoInfo QyVideo.VideoInfo
 * @param videoContainerId 视频容器ID
 * @returns {boolean}
 */
QyVideo.prototype.playH264UseHttpm3u8 = function(videoInfo, videoContainerId){
    var success = false;
    this.createVideo(videoInfo,videoContainerId);
    if(videoInfo.playUrl && ""!=videoInfo.playUrl) {
        this.video.src = videoInfo.playUrl;

        //this.video.load();
        this.video.play();

        if (this.isMobile()) {
            this.autoplay();
        }
        success = true;
    }
    return success;
};

/**
 * 重新播放
 */
QyVideo.prototype.replay = function(){
    //QyVideo.Log(this.videoInfo);
    if(this.videoInfo != null){
        this.play(this.videoInfo,this.videoContainerId);
    }
};

/**
 * 定时器
 */
QyVideo.prototype.loop = function(){
    //QyVideo.Log("loop run:"+this.playing);
    if(this.videoInfo == null){
        return;
    }

    if(this.playing){
        if (QyVideo.PlayType.HTTPFLV == this.videoInfo.playType) {
            if (this.player) {
                try {
                    var buffered = this.player.buffered;
                    if (buffered.length > 0) {
                        var end = buffered.end(0);
                        var delaymillis = (end - this.player.currentTime) * 1000;

                        if (delaymillis > 5000) {
                            //document.getElementById('h_video').controls = false;
                            this.player.currentTime = end - 0.1;
                            console.info("延迟:%d毫秒，跳转到：%f", delaymillis, (end - 0.1));
                        }
                    }
                } catch (e) {
                    console.error(e);
                }
            } else {
                this.playing = false;
            }
        } else if (QyVideo.PlayType.WSMJPEG == this.videoInfo.playType) {
            //超过10秒无数据
            if (((new Date()).getTime() - this.wsLastReceiveData) > 10 * 1000) {
                //QyVideo.Log('超过10秒无数据！')
                this.playing = false;
            }
        }
    }
    else {
        //客户在外部已关注了事件，则由外部处理
        if(this.onconnectionstatechange){
            this.onconnectionstatechange(this,"disconnected");
        }
        else {
            QyVideo.Log('video play conn disconnected!');
        }
    }
};

/**
 * 获取当前视频容器ID
 * @returns {null|*}
 */
QyVideo.prototype.getVideoContainerId = function(){
    return this.videoContainerId;
};

/**
 * 获取当前创建的视频元素ID，针对wsmjpeg返回的是img元素的id，其它则为video元素id
 * @returns {string|*}
 */
QyVideo.prototype.getVideoId = function(){
    return this.videoId;
};

/**
 * 获取当前视频元素对象，针对wsmjpeg返回的是img元素，其它则为video元素
 * @returns {null|HTMLElement}
 */
QyVideo.prototype.getVideo = function(){
    return this.video;
};

/**
 * 清理与关闭
 */
QyVideo.prototype.destroy = function(){
    if(this.wsVideoReader){
        try{
            this.wsVideoReader.abort();
            this.wsVideoReader.onload = null;
            this.wsVideoReader = null;
        }catch (e) {console.error(e);}
    }

    if(this.webSocket){
        try {
            this.webSocket.onopen = null;
            this.webSocket.onmessage = null;
            this.webSocket.onclose = null;
            this.webSocket.onerror = null;
            this.webSocket.close();
        }catch (e) {console.error(e);}
    }

    if (this.session != null) {
        let url = null;
        try {
            url = this.session.simulator + '?drop=1&username=' + this.session.sessionid;
            url = url.replace("file://", "http://");
            //QyVideo.Log('url = %s', url);
            QyVideo.Ajax("get", url, null, function (response) {
                //QyVideo.Log(response);
            }, function (response) {
                QyVideo.Log(response);
            }, false);
        }catch (e) {
            try {
                if ("sendBeacon" in navigator) {
                    navigator.sendBeacon(url);
                }
            }catch (e2) {
                console.error(e2);
            }
        }
    }

    if(this.webrtcSdk){
        try {
            this.webrtcSdk.close();
        }catch (e) {console.error(e);}
    }

    if (this.player) {
        try {
            this.player.unload();
        }catch (e) {}
        try {
            this.player.detachMediaElement();
        }catch (e) {}
        try {
            this.player.destroy();
        }catch (e) {}
        this.player = null;
    }

    if(this.videoContainerId){
        if(this.video){
            try{
                let videoContainer = document.getElementById(this.videoContainerId);
                if(videoContainer != null) {
                    videoContainer.removeChild(this.video);
                }

                this.video = null;
            }catch (e) {console.error(e);}
        }
    }

    this.playing = false;
    this.videoInfo = null;
    this.session = null;
    QyVideo.Log("destroy!");
};