import axios from 'axios'
import router from "@/router";
import { Notification } from 'element-ui'

// 创建一个新的axios实例
const request = axios.create({
    baseURL: process.env.NODE_ENV === 'production' ? 'http://localhost:9999' : '/api', // 后端接口基础地址
    timeout: 30000 // 请求超时时间：30秒（超过该时间未响应则视为请求失败）
})

// request 拦截器，可以自请求发送前对请求做一些处理，比如统一加 token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    let user = JSON.parse(localStorage.getItem("user") || '{}')
    // 将用户 token 添加到请求头（用于后端身份验证），只在 token 存在时添加
    console.log('=== 请求拦截器 ===')
    console.log('当前用户:', user)
    console.log('Token:', user?.token)
    console.log('请求 URL:', config.url)
    
    // 特别检查支付接口
    if (config.url.includes('/orders/pay')) {
        console.log('>>> 支付请求检测 <<<')
        console.log('localStorage 中的 user:', localStorage.getItem('user'))
        console.log('解析后的 user 对象:', user)
        console.log('user.token:', user?.token)
    }

    // 注册、登录、重置密码等公开接口无需 token，不应输出警告
    const publicPaths = ['/login', '/register', '/password', '/type', '/goods'];
    const isPublic = publicPaths.some(path => config.url.startsWith(path));

    if (isPublic) {
        console.log('i 公开接口无需 token', config.url)
    } else if (user.token) {
        config.headers['token'] = user.token
        console.log('✓ 已添加 token 到请求头:', config.headers['token'])
    } else {
        console.warn('⚠ 警告：请求未携带 token，URL:', config.url)
    }
    console.log('最终请求头:', config.headers)

    return config
}, error => {
    Notification.error({message: '网络连接超时', duration: 3000, showClose: false})
    return Promise.reject(error)
});

// 递归处理数据中的图片URL
function processImageUrls(data) {
    if (typeof data === 'string') {
        // 修复重复的路径
        let result = data.replace(/\/file\/download\/file\/download\//g, '/file/download/');
        // 将 localhost:9999 替换为相对路径，解决内网穿透时图片无法加载的问题
        result = result.replace(/http:\/\/localhost:9999/g, '');
        return result;
    }
    if (Array.isArray(data)) {
        return data.map(item => processImageUrls(item));
    }
    if (typeof data === 'object' && data !== null) {
        for (let key in data) {
            if (Object.prototype.hasOwnProperty.call(data, key)) {
                data[key] = processImageUrls(data[key]);
            }
        }
    }
    return data;
}

// 响应拦截器（接口响应后的处理）作用：统一处理响应数据、拦截错误状态码（如401未登录）等
request.interceptors.response.use(
    response => {
        let res = response.data;

        // 兼容服务端返回的字符串类型数据（如果后端返回JSON字符串，自动解析为对象）
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }

        // 处理图片URL，将 localhost:9999 替换为相对路径
        res = processImageUrls(res);

        if (res.code === '401' || res.code === 401) {
            // 只在后台管理页面清除用户信息并跳转登录页
            if (!router.currentRoute.path.startsWith('/front')) {
                localStorage.removeItem("user");
                Notification.error({message: res.msg, duration: 3000, showClose: false})
                router.push('/login')
            } else {
                // 前台页面只显示错误提示，不清除用户信息
                Notification.error({message: res.msg || '登录已过期，请重新登录', duration: 3000, showClose: false})
            }
        }
        return res;
    }, error => {
        // 处理HTTP 401未授权错误
        if (error.response && error.response.status === 401) {
            localStorage.removeItem("user");
            if (!router.currentRoute.path.startsWith('/front')) {
                Notification.error({message: '登录已过期，请重新登录', duration: 3000, showClose: false})
                router.push('/login')
            }
        } else if (!error.response) {
            Notification.error({message: '网络连接超时', duration: 3000, showClose: false})
        }
        return Promise.reject(error)
    }
)

export default request
