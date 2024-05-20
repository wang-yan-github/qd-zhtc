import axios from 'axios';
import { useRouter } from 'vue-router'

const service = axios.create({
    // process.env.NODE_ENV === 'development' 来判断是否开发环境
    // easy-mock服务挂了，暂时不使用了
    //baseURL: 'https://zhtc.jkqzhtc.cn/api',
    // baseURL: 'https://zhtc.aldwxa.top/api',
    //baseURL: 'http://lby.dincher.cn:8001/zhtc_api',
    baseURL: '/dev',
    timeout: 50000
});
service.interceptors.request.use(
    config => {
        if (('null' == localStorage.getItem("token_value") || null == localStorage.getItem("token_value")) && config.url != '/login.do') {
            window.location.href = "/login"
        }
        else {
            config.headers.Authorization = localStorage.getItem("token_value");
        }
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);

service.interceptors.response.use(
    response => {
        if (response.data === 500) {
            //window.location.href = "/login"
            localStorage.removeItem("token_value");
            window.location.href = "/web";
        } else {
            return response.data;
        }

    },
    error => {
        return Promise.reject();
    }
);

export default service;
