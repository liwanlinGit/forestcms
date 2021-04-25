<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title" style="color: rgb(0, 209, 178);">管理系统</div>
            <el-form :model="ruleForm"  ref="ruleForm" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="ruleForm.username" placeholder="username">
                        <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="password" v-model="ruleForm.password" @keyup.enter.native="submitForm()">
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">登录</el-button>
                </div>
                
            </el-form>
        </div>
        <remote-js src="http://pv.sohu.com/cityjson?ie=utf-8" ></remote-js>
    </div>
    
</template>

<script>
import Vue from "vue";
import baseURL_ from '@/utils/baseUrl.js';
import JsEncrypt from 'jsencrypt/bin/jsencrypt';
Vue.prototype.$jsEncrypt=JsEncrypt;
    export default {
        data: function(){
            return {
                ruleForm: {
                    username: 'admin',
                    password: 'admin'
                },
                
            }
        },
        created() {
        
          
        },
        mounted(){
        
        },
        components: {
            'remote-js': {
                render(createElement) {
                    return createElement('script', { attrs: { type: 'text/javascript', src: this.src }});
                },
                props: {
                    src: { type: String, required: true },
                },
            },
        },
        methods: {
           
            async submitForm() {
                var ip = returnCitySN["cip"];
                var name = returnCitySN["cname"];
                localStorage.setItem('forestToken_address',ip+"_"+name);
                let jse=this.$jsEncrypt;
                jse.prototype.setPublicKey(baseURL_.public_key);
                //let passwordDeg = jse.prototype.encrypt(this.ruleForm.password);
                let passwordDeg = this.ruleForm.password;
                var obj={'username':this.ruleForm.username,
                        'password':passwordDeg,
                        'grant_type':'password',
                        'client_id':'client-app',
                        'client_secret':'123456'}
                const login = await this.$http.post(baseURL_.loginUrl+'/oauth/token', this.$qs.stringify(obj));
                if(login.data.code==200){
                    localStorage.setItem('forestToken',login.data.data.access_token);
                    this.$router.push('/');
                }else{
                    localStorage.removeItem('forestToken');
                    Vue.prototype.$message(login.data.message);
                    
                }
            }
        }
    }
</script>

<style scoped>
    .login-wrap{
        position: relative;
        width:100%;
        height:100%;
        background-image: url(../../../assets/img/login-bg.jpg);
        background-size: 100%;
    }
    .ms-title{
        width:100%;
        line-height: 50px;
        text-align: center;
        font-size:20px;
        color: #fff;
        border-bottom: 1px solid #ddd;
    }
    .ms-login{
        position: absolute;
        left:50%;
        top:50%;
        width:350px;
        margin:-190px 0 0 -175px;
        border-radius: 5px;
        background: rgba(255,255,255, 0.3);
        overflow: hidden;
    }
    .ms-content{
        padding: 30px 30px;
    }
    .login-btn{
        text-align: center;
    }
    .login-btn button{
        width:100%;
        height:36px;
        margin-bottom: 10px;
    }
    .login-tips{
        font-size:12px;
        line-height:30px;
        color:#fff;
    }
</style>