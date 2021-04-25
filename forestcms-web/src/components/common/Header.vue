<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseChage">
            <i class="el-icon-menu"></i>
        </div>
        <div class="logo">管理系统</div>
        <div class="header-right">
            <div class="header-user-con">
                <!-- 全屏显示 -->
                <div class="btn-fullscreen" @click="handleFullScreen">
                    <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                        <i class="el-icon-rank"></i>
                    </el-tooltip>
                </div>
                <!-- 消息中心 -->
                
                <!-- 用户头像 -->
                <div class="user-avator"><img src="../../assets/img/people.png"></div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{username}} <i class="el-icon-caret-bottom"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item divided  command="pass">修改密码</el-dropdown-item>
                        <el-dropdown-item divided  command="loginout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
        <!-- 编辑弹出框 -->
        <el-dialog title="修改密码"  :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="旧密码">
                <el-input v-model="form.oldPass" class="input" type="password"></el-input>
                </el-form-item>
                <el-form-item label="新密码">
                    <el-input v-model="form.newPass" class="input" type="password"></el-input>
                </el-form-item>
                <el-form-item label="确认密码">
                <el-input v-model="form.passTow" class="input" type="password"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
    import bus from '../common/bus';
    import baseURL_ from '@/utils/baseUrl.js';
    import axios from 'axios'
    import router from './../../router'
    export default {
        data() {
            return {
                collapse: false,
                fullscreen: false,
                name: 'linxin',
                message: 0,
                editVisible:false,
                username:'',
                form:{
                  oldPass:'',
                  newPass:'',
                  passTow:'',

                }

            }
        },
        computed:{
            
        },
        methods:{
            async user(){
                const user = await this.$http.post(baseURL_.loginUrl+'/oauth/user');
                this.username=user.data.data
            },
            // 用户名下拉菜单选择事件
            handleCommand(command) {
                if(command == 'loginout'){
                   this.loginout();
                }if(command == 'pass'){
                   this.editPass();
                   
                }
            },
            editPass(){
               this.form.oldPass='';
               this.form.newPass='';
               this.form.passTow='';
               this.editVisible=true;
            },
           saveEdit(){
                if(this.form.newPass!=this.form.passTow){
                    this.$message("两次密码输入不一致！");
                    return;
                }
                var that = this;
                var ress=axios.post(baseURL_.loginUrl+'/oauth/editPass',this.$qs.stringify(this.form)).then(function (res) {
                        that.$message(res.data.message);
                        if(res.data.code=='200'){
                            localStorage.removeItem('forestToken');
                            
                            router.replace({                   
                                path: "/login",                    
                                query: {                
                                redirect: router.currentRoute.fullPath               
                                }                    
                            });
                        }
                  });

            },
           loginout(){
                this.$confirm('确认退出？')
                .then( e=> {
                    var res=axios.get(baseURL_.loginUrl+'/oauth/loginOut').then(function (res) {
                        if(res.data.code=='200'){
                            localStorage.removeItem('forestToken');
                            
                            router.replace({                   
                                path: "/login",                    
                                query: {                
                                redirect: router.currentRoute.fullPath               
                                }                    
                            });
                        }
                  });
                }).catch(_ => {});
                 
            },
            // 侧边栏折叠
            collapseChage(){
                this.collapse = !this.collapse;
                bus.$emit('collapse', this.collapse);
            },
            // 全屏事件
            handleFullScreen(){
                let element = document.documentElement;
                if (this.fullscreen) {
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                } else {
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.webkitRequestFullScreen) {
                        element.webkitRequestFullScreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.msRequestFullscreen) {
                        // IE11
                        element.msRequestFullscreen();
                    }
                }
                this.fullscreen = !this.fullscreen;
            }
        },
        mounted(){
            if(document.body.clientWidth < 1500){
                this.collapseChage();
            }
            this.user();
        }
    }
</script>
<style scoped>
    .header {
        position: relative;
        box-sizing: border-box;
        width: 100%;
        height: 70px;
        font-size: 22px;
        color: #fff;
    }
    .collapse-btn{
        float: left;
        padding: 0 21px;
        cursor: pointer;
        line-height: 70px;
    }
    .header .logo{
        float: left;
        width:250px;
        line-height: 70px;
    }
    .header-right{
        float: right;
        padding-right: 50px;
    }
    .header-user-con{
        display: flex;
        height: 70px;
        align-items: center;
    }
    .btn-fullscreen{
        transform: rotate(45deg);
        margin-right: 5px;
        font-size: 24px;
    }
    .btn-bell, .btn-fullscreen{
        position: relative;
        width: 30px;
        height: 30px;
        text-align: center;
        border-radius: 15px;
        cursor: pointer;
    }
    .btn-bell-badge{
        position: absolute;
        right: 0;
        top: -2px;
        width: 8px;
        height: 8px;
        border-radius: 4px;
        background: #f56c6c;
        color: #fff;
    }
    .btn-bell .el-icon-bell{
        color: #fff;
    }
    .user-name{
        margin-left: 10px;
    }
    .user-avator{
        margin-left: 20px;
    }
    .user-avator img{
        display: block;
        width:40px;
        height:40px;
        border-radius: 50%;
    }
    .el-dropdown-link{
        color: #fff;
        cursor: pointer;
    }
    .el-dropdown-menu__item{
        text-align: center;
    }
</style>
