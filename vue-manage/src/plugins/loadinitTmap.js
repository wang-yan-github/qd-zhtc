export function loadInitTMap(domID, arr) {
  // 设置中心点坐标
  const center = new window.TMap.LatLng(arr[0], arr[1])
  const markersArray = [];
  // 初始化地图
  const map = new window.TMap.Map(document.getElementById(domID), {
    rotation: 0, // 设置地图旋转角度
    pitch: 0, // 设置俯仰角度（0~45）
    zoom: 16, // 设置地图缩放级别
    center: center // 设置地图中心点坐标
  })
    // 创建信息窗口
  const info = new window.TMap.InfoWindow({
    map,
    position: map.getCenter(),
    offset: { x: 0, y: -32 }
  })
  info.close()


  //点击地图事件
  map.on("click", (evt) => {
    //console.log(evt)
    info.open();
    info.setPosition(evt.latLng);
    var lat = evt.latLng.getLat().toFixed(6);
    var lng = evt.latLng.getLng().toFixed(6);
    info.setContent("当前坐标：" + lat + "," + lng);
    //console.log("您点击的的坐标是："+ lat + "," + lng);

    //clearOverlays(markersArray)
    //maketLyer.setMap(null);
    // maketLyer.add({
    //   position: evt.latLng
    // });
    // markersArray.push(maketLyer);
    //console.log("坐标集："+markersArray);
  });

  // 自执行中心点方法
  //setCenter(map, arr)

  //map.clearOverLays();
 
}
export function creatMarker(arr,pointJson) {   //console.log(pointJson);
  const center = new window.TMap.LatLng(arr[0], arr[1])
  const map = new window.TMap.Map(document.getElementById("mapDiv"), {
    rotation: 0, // 设置地图旋转角度
    pitch: 0, // 设置俯仰角度（0~45）
    zoom: 16, // 设置地图缩放级别
    center: center // 设置地图中心点坐标
  })



  setCenter(map, arr);

  const markergeometries=[];
  const labelgeometries=[];
  const sContent=[];
  
  pointJson.forEach((item,i)=>{   
    //var labelname=item.name+"【总泊位:"+item.totalparking+"】【空泊位:"+item.freeparking+"】";
    var labelname=item.name;
    sContent.push("<p style='margin:0px 0px 0px 0px;line-height:20px;font-size:14px;'>总泊位：<span style='color:#fe0808'>" + item.totalparking + "</span> <br>空泊位：<span style='color:#fe0808'>" + item.freeparking + "</span>");
    markergeometries.push({styleId: 'big',position: new window.TMap.LatLng(item.lat,item.lng),properties: {title: 'marker1'},contents:sContent[i]});
    labelgeometries.push({id: item.id,styleId: 'label',position: new window.TMap.LatLng(item.lat,item.lng),content: labelname,properties: {title: 'label'}});    
  })

  //pointJson.forEach((item,i)=>{
    //创建标注对象

    //标记
    //console.log(item.lng);
    const maketLyer = new window.TMap.MultiMarker({
      map: map,
      styles: {  
        small: new TMap.MarkerStyle({
          // 点标注的相关样式
          width: 34, // 宽度
          height: 46, // 高度
          anchor: { x: 17, y: 23 }, // 标注点图片的锚点位置
          src: 'https://mapapi.qq.com/web/lbs/visualizationApi/demo/img/small.png', // 标注点图片url或base64地址
          color: '#333', // 标注点文本颜色
          size: 16, // 标注点文本文字大小
          direction: 'bottom', // 标注点文本文字相对于标注点图片的方位
          offset: { x: 0, y: 8 }, // 标注点文本文字基于direction方位的偏移属性
          strokeColor: '#fff', // 标注点文本描边颜色
          strokeWidth: 2, // 标注点文本描边宽度
        }),      
        big: new window.TMap.MarkerStyle({
          width: 50,
          height: 64,
          anchor: { x: 26, y: 16 },
          src: '/src/assets/img/marker.png',
          color: '#333',
          size: 22,
          direction: 'bottom',
          strokeColor: '#fff',
          offset: { x: 0, y: 10 },
          strokeWidth: 2,
        }),
      },
      geometries: markergeometries
    })

    // 简单文本标记
    const label = new window.TMap.MultiLabel({
      id: "001",
      map: map,
      enableCollision:true, //是否开启图层内部的文本标注碰撞
      styles: {
        label: new window.TMap.LabelStyle({
          color: '#ff0000', // 颜色属性
          size: 14, // 文字大小属性
          offset: { x: 0, y: 58 }, // 文字偏移属性单位为像素
          angle: 0, // 文字旋转属性
          alignment: 'center', // 文字水平对齐属性
          verticalAlignment: 'center', // 文字垂直对齐属性
          // borderWidth:'1',
          // borderRadius:'5',
          // borderColor:'#000000',
          // backgroundColor:'#ffffff'
        })
      },
      geometries: labelgeometries
    })

    // 创建信息窗口
    const info = new window.TMap.InfoWindow({
      map,
      position: map.getCenter(),
      offset: { x: 0, y: -32 }
    })
    info.close()
    //console.log(sContent);

      maketLyer.on('click', (event) => {
        //console.log(event)
        info.open()
        info.setPosition(event.geometry.position)
        // var lat = event.latLng.getLat().toFixed(6);
        // var lng = event.latLng.getLng().toFixed(6);      
        info.setContent(event.geometry.contents);

    })
    

  //循环结束标记
  // });
}


export function addmarker(domID, arr) {
  // 设置中心点坐标
  const center = new window.TMap.LatLng(arr[0], arr[1])
  const markersArray = [];
  // 初始化地图
  const map = new window.TMap.Map(document.getElementById(domID), {
    rotation: 0, // 设置地图旋转角度
    pitch: 0, // 设置俯仰角度（0~45）
    zoom: 16, // 设置地图缩放级别
    center: center // 设置地图中心点坐标
  })

  //标记
  const maketLyer = new window.TMap.MultiMarker({
    map: map,
    style: {
      myStyle: new window.TMap.MarkerStyle({
        width: 25, // 点标记样式宽度（像素）
        height: 35 // 点标记样式高度（像素）
      })
      , small: new window.TMap.MarkerStyle({
        // 点标注的相关样式
        width: 34, // 宽度
        height: 46, // 高度
        anchor: { x: 17, y: 23 }, // 标注点图片的锚点位置
        src: 'https://mapapi.qq.com/web/lbs/visualizationApi/demo/img/small.png', // 标注点图片url或base64地址
        color: '#333', // 标注点文本颜色
        size: 16, // 标注点文本文字大小
        direction: 'bottom', // 标注点文本文字相对于标注点图片的方位
        offset: { x: 0, y: 8 }, // 标注点文本文字基于direction方位的偏移属性
        strokeColor: '#fff', // 标注点文本描边颜色
        strokeWidth: 2, // 标注点文本描边宽度
      }),
      big: new window.TMap.MarkerStyle({
        width: 58,
        height: 76,
        anchor: { x: 36, y: 32 },
        src: 'https://mapapi.qq.com/web/lbs/visualizationApi/demo/img/big.png',
        color: '#333',
        size: 22,
        direction: 'bottom',
        strokeColor: '#fff',
        offset: { x: 0, y: 10 },
        strokeWidth: 2,
      }),
    },
    geometries: [
      {
        //id: 'label-layer01', // 点标记唯一标识，后续如果有删除、修改位置等操作，都需要此id
        styleId: 'small', // 指定样式id
        position: new window.TMap.LatLng(arr[0], arr[1]), // 点标记坐标位置
        properties: {
          // 自定义属性
          title: 'marker1'
        },
      }
    ]
  })

  // 简单文本标记
  const label = new window.TMap.MultiLabel({
    id: '1',
    map: map,
    styles: {
      label: new window.TMap.LabelStyle({
        color: '#ff0000', // 颜色属性
        size: 14, // 文字大小属性
        offset: { x: 0, y: 0 }, // 文字偏移属性单位为像素
        angle: 0, // 文字旋转属性
        alignment: 'center', // 文字水平对齐属性
        verticalAlignment: 'middle' // 文字垂直对齐属性
      })
    },
    geometries: [
      {
        id: 'label', // 点图形数据的标志信息
        styleId: 'label', // 样式id
        position: center, // 标注点位置
        content: '江苏鼎驰', // 标注文本
        properties: {
          // 标注点的属性数据
          title: 'label'
        }
      }
    ]
  })
  // 创建信息窗口
  const info = new window.TMap.InfoWindow({
    map,
    position: map.getCenter(),
    offset: { x: 0, y: -32 }
  })
  info.close()


  //点击地图事件
  map.on('click', (evt) => {
    //console.log(evt)   
    info.open()
    info.setPosition(evt.latLng)
    var lat = evt.latLng.getLat().toFixed(6);
    var lng = evt.latLng.getLng().toFixed(6);
    info.setContent("当前坐标：" + lat + "," + lng)
    //console.log("您点击的的坐标是："+ lat + "," + lng);

    //clearOverlays(markersArray)
    //maketLyer.setMap(null);
    maketLyer.add({
      position: evt.latLng
    });
    markersArray.push(maketLyer);
    //console.log("坐标集："+markersArray);

  })

  // 自执行中心点方法
  //setCenter(map, arr)   

  //map.clearOverLays();


}

//清除覆盖层
function clearOverlays(markersArray) {
  var i = 0;
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }
  }
}

function setCenter(map, arr) {
  map.setCenter(new window.TMap.LatLng(arr[0], arr[1]))
}