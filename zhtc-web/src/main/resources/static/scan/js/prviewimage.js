        var pimg=document.querySelector(".ximg");
        var oImg = document.querySelector(".picbigbox img");
        var oBox = document.querySelector(".picbigbox");
        pimg.onclick=function(){
            oBox.style.display="flex"
            oImg.src=pimg.src
        }
        oBox.onclick=function(){
            oBox.style.display="none"
            oImg.src=''
        }
        var hammer = new Hammer(oImg);//hammer实例化
        hammer.get('pan').set({direction: Hammer.DIRECTION_ALL});//激活pan(移动)功能
        hammer.get('pinch').set({enable: true});//激活pinch(双指缩放)功能
        hammer.on("panstart", function(event) {
            var left = oImg.offsetLeft;
            var tp = oImg.offsetTop;
            hammer.on("panmove", function(event) {
                oImg.style.left = left + event.deltaX + 'px';
                oImg.style.top = tp + event.deltaY + 'px';
            });
        })

        hammer.on("pinchstart", function(e) {
            hammer.on("pinchout", function(e) {
                oImg.style.transition = "-webkit-transform 300ms ease-out";
                oImg.style.webkitTransform = "scale(2.5)";
            });
            hammer.on("pinchin", function(e) {
                oImg.style.transition = "-webkit-transform 300ms ease-out";
                oImg.style.webkitTransform = "scale(1)";
            });
        })