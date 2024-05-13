let picmax = 8; //限制上传数量 
//var imgFile = []; //文件流
var imgSrc = []; //图片路径
//var imgName = []; //图片名字
function imgChange(obj) {
	let file = document.getElementById('file').files;
	let imglist = document.querySelectorAll('.upload-Picitem');
	let piclist = document.getElementsByClassName('upload-piclist')[0];
	let filelist = file.length + imglist.length > picmax ? 8 - imglist.length : file.length + imglist.length;
	if (file.length + imglist.length >= 8) {
		let uploadfile = document.getElementsByClassName('upload-file')[0]
		uploadfile.style.display = "none"
	}
	for (let i = 0; i < filelist; i++) {
		readerfile(file[i]).then(e => {
			let html = document.createElement('div');
			html.className = obj
			html.innerHTML = '<img src=' + e + ' alt="pic"><p class="guanbi" onclick="removeImg(this)"><i class="iconfont icon-cuohao"></i></p>'
			imgSrc.push(e);
			//imgFile.push(e);
			//imgName.push(e.name);
			//console.log(imgSrc);
			piclist.prepend(html);
		})
	}
	console.log(imgSrc);
	
}

function readerfile(file) {
	return new Promise((resolve, reject) => {
		let reader = new FileReader();
		reader.addEventListener("load", function() {
			resolve(reader.result);
		}, false)
		if (file) {
			reader.readAsDataURL(file)
		}
	})
}

//删除
function removeImg(obj,index) {
	$(obj).parent().remove();
	index =$(obj).parent().index();
	let imglist = document.querySelectorAll('.upload-Picitem');
	if(imglist.length<8){
		let uploadfile = document.getElementsByClassName('upload-file')[0]
		uploadfile.style.display = "block"
	}
	imgSrc.splice(index, 1);
}

function submit() {
	//let imglist = []
	//let piclist = document.querySelectorAll('.upload-Picitem');
	// for (let i = 0; i < imgSrc.length; i++) {
	// 	imglist.push(imgSrc[i])
	// }
	console.log("图片列表：", imgSrc)
}
