import axios from 'axios';



const service = {}
service.install = function fn(Vue) {

    Vue.prototype.$http = axios;

}
export default service;