const BASEURL = "http://172.168.10.63:8080/";//项目路径
//const BASEURL = "https://zhtc.jkqzhtc.cn/api/";//项目路径
//const BASEURL = "http://192.168.0.172:8081/";//项目路径
const File_URL = {
    file_down: BASEURL,//文件下载
    file_upload: BASEURL,//文件上传
    file_img_url: BASEURL + 'fileManage/imgFileSave.json',//图片上传路径
    file_hx_img_url: 'http://172.168.95.35:8010/' //图片回显路径
    //file_hx_img_url: 'https://zhtc.jkqzhtc.cn/file/' //图片回显路径
};
export default File_URL