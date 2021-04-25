import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import ElementUI from 'element-ui';
import VueI18n from 'vue-i18n';
import echarts from 'echarts'
import { messages } from './components/common/i18n';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import './assets/css/theme-green/index.css';  // 浅绿色主题
import './assets/css/icon.css';
import './components/common/directives';
import "babel-polyfill";
import '../zTree_v3/js/jquery-1.4.4.min.js';
import '../zTree_v3/css/zTreeStyle/zTreeStyle.css';
import '../zTree_v3/js/jquery.ztree.core.js';
import '../zTree_v3/js/jquery.ztree.excheck.js';
import '../zTree_v3/js/jquery.ztree.exedit.js';


import http from '@/utils/request.js';

import qs from 'qs'
Vue.prototype.$qs = qs;


Vue.use(echarts)
Vue.prototype.$echarts = echarts

Vue.config.productionTip = false
Vue.use(VueI18n);
Vue.use(http);
Vue.use(ElementUI, {
    size: 'small'
});
Vue.prototype.$axios = axios;

const i18n = new VueI18n({
    locale: 'zh',
    messages
})

axios.interceptors.request.use(
    config => {
      let url = config.url;
      var addres = localStorage.getItem("forestToken_address");
      if(url.indexOf("oauth/token")==-1){
        const token = localStorage.getItem('forestToken');
         if(token){
             config.headers["Authorization"] = "Bearer "+token;
         }
      }
     
      if(addres){
        var ip=addres.split("_")[0];
        var city= encodeURIComponent(addres.split("_")[1]);
        config.headers["addressIp"] = ip;
        config.headers["addressCity"] = city;
     }
      return config;
    },
    err => {
      return Promise.reject(err);
    });
 
axios.interceptors.response.use( response => {
   
    if(response.data.code==401){
        Vue.prototype.$message(response.data.message);
                setTimeout(() => {                    
                    router.replace({                   
                        path: "/login",                  
                        query: {                
                          redirect: router.currentRoute.fullPath               
                        }                    
                      });                   
                    }, 1000);
    }
    if(response.data.code==403){
      Vue.prototype.$message(response.data.message);
      setTimeout(() => {                    
        router.replace({                   
            path: "/403",                  
                                
          });                   
        }, 1000);
    }
    return response;
   },
//    error => {
//        alert(error.response.code)
//     if(error.response.code==401){
//         Vue.prototype.$message(error.response.data);
//         setTimeout(() => {                    
//             router.replace({                   
//                 path: "/login",                  
//                 query: {                
//                   redirect: router.currentRoute.fullPath               
//                 }                    
//               });                   
//             }, 1000);
       
//     }
   
//         if(error.response.code==500){
             
//             Vue.prototype.$message(error.response.data.message);
//         }
//    }
)

//使用钩子函数对路由进行权限跳转
 router.beforeEach((to, from, next) => {
     const role = localStorage.getItem('forestToken');
     if (!role && to.path.indexOf("/login") ==-1) {
         next('/login');
     } else {
         next();
     }
    
 })

new Vue({
    router,
    i18n,
    render: h => h(App)
}).$mount('#app')