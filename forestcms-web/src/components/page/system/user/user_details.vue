<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> {{title}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        
        <div class="container">
            <el-row style="margin-bottom:20px;">
                <table width="100%;" cellspacing="0" class="Companyform" border="1">
                    <tbody>
                    <tr>
                        <td class="companytitle">姓名</td>
                        <td class="companytitle1">
                            {{userData.name}}
                        </td>
                        <td class="companytitle">登录名</td>
                        <td class="companytitle1" >
                        {{userData.loginName}}
                        </td>
                    </tr>
                    <tr>
                        <td class="companytitle">创建时间</td>
                        <td class="companytitle1" >
                        {{userData.createTime}}
                        </td>                                
                        <td class="companytitle">手机号</td>
                        <td class="companytitle1" >
                            {{userData.phone}}
                        </td>
                    </tr>
                        <tr>
                        <td class="companytitle">邮箱</td>
                        <td class="companytitle1" >
                          {{userData.email}}
                        </td>                                
                        <td class="companytitle">角色</td>
                        <td class="companytitle1" >{{roel_name}}</td>     
                    </tr>
                    <tr>
                        <tr>
                        <td class="companytitle">是否启用</td>
                        <td class="companytitle1" colspan='3'>
                            {{userData.isStatus}}
                        </td>                                
                       
                    </tr>
                    
                        </tbody>
                </table>
            </el-row>
        </div>
    </div>
</template>

<script>
    import { fetchData } from '../../../../api/index';
    import baseURL_ from '@/utils/baseUrl.js';
    export default {
        name: 'basetable',
        data() {
            return {
                title:'',
                userData:{},
                user_id:'',
                roel_name:''
            }
        },
        created() {
           this.user_id=this.$route.query.id;
           this.title=this.$route.meta.title;
           this.getUserById();
        },
        methods: {
           async getUserById(){
               const  user = await this.$http.get(baseURL_.sysUrl+'/sys/user/getById',{ 
                    params: {'id':this.user_id}
                });
                this.userData=user.data.data;
                var roles=user.data.data.sysRoleList;
                if(roles){
                    var rolesName=[];
                    for(var i=0;i<roles.length;i++){
                         rolesName.push(roles[i].roleName);
                    }
                    this.roel_name=rolesName.join(",");
                }
            }
        }
        
    }

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }
    .del-dialog-cnt{
        font-size: 12px;
        text-align: center
    }
    .table{
        width: 100%;
        font-size: 12px;
    }
  
    .red{
        color: #ff0000;
    }
    .mr10{
        margin-right: 10px;
    }
   .container{
        min-height:700px;
    }
 
 .bigcard{
  position: relative;
    height: 100%;
    background: #fff;
    padding: 20px;
}
.formmb .el-form-item {
    margin-bottom: 0px;
}
.Companyform tr {
    width: 100%;
    text-align: center;
}
.Companyform td {
    text-align: center;
  border: 1px solid #E2E2E2 ;
}
.Companyform{
 /* margin-top:40px; */
 color: #323232;
 border: 0px solid #E2E2E2 ;
}
.Companyform .companytitle{
   width: 150px;
   padding-right: 10px;
   text-align: right;
   background: #F9FAFC;
}
.Companyform .companytitle1{
   /* width: 176px; */
   text-align: left;
   padding-left: 10px;
}

.Companyform td{
   height: 32px;
   line-height: 32px;
}
.round{
    width: 16px;
    height: 16px;
    border-radius: 50%;
    background: #009B63;
    text-align: center;
    line-height: 16px;
    color: #fff;
    float: left;
    margin-bottom: 20px;
}


</style>