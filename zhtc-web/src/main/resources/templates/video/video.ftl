<!DOCTYPE html>
<html>

<head>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
    <video muted="muted"></video>
    <title>监控视频</title>
    <style>
        .mainContainer {
            display: block;
            width: 1024px;
            margin-left: auto;
            margin-right: auto;
        }

        .urlInput {
            display: block;
            width: 100%;
            margin-left: auto;
            margin-right: auto;
            margin-top: 8px;
            margin-bottom: 8px;
        }

        .centeredVideo {
            display: block;
            width: 100%;
            height: 576px;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: auto;
        }

        .controls {
            display: block;
            width: 100%;
            text-align: left;
            margin-left: auto;
            margin-right: auto;
        }
    </style>
</head>
<body>
<video id="my-player" preload="auto" muted autoplay type="rtmp/flv" style="width: 60%">
    <source src="">
</video>
<script src="/video/js/flv.min.js"></script>
<script>
    // 获取video节点
    videoElement = document.getElementById('my-player');
    if (flvjs.isSupported()) {
        flvPlayer = flvjs.createPlayer({
            type: 'flv',
            isLive: true,
            url: 'https://zhtc.aldwxa.top/live?port=1935&app=live&stream=${deviceCode}',//flv格式流地址
        },{
            enableWorker: false, //不启用分离线程
            enableStashBuffer: false, //关闭IO隐藏缓冲区
            reuseRedirectedURL: true, //重用301/302重定向url，用于随后的请求，如查找、重新连接等。
            autoCleanupSourceBuffer: true //自动清除缓存
        });
        flvPlayer.attachMediaElement(videoElement);
        flvPlayer.load(); //加载
        flvPlayer.play();//播放
    }
</script>
</body>

</html>
