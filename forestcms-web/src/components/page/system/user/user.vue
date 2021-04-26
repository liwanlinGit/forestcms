<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> {{title}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-plus" class="handle-del mr10" v-if="button_role&&button_role.add"    @click="add">添加</el-button>
                <el-input placeholder="用户名或登录名" v-model="name"  class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="rest">重置</el-button>
            </div>
            <el-table  :data="tableData" border class="table" ref="multipleTable"  @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" ></el-table-column>
                <el-table-column type="index" label="序号" width="55" align="center" >
                   <template slot-scope="scope">
                         <span >{{(page-1)*pageSize+scope.$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="loginName" label="登录名"  align="center" width="80"></el-table-column>
                <el-table-column prop="name" label="姓名"  align="center" width="80"></el-table-column>
                <el-table-column prop="phone" label="手机"  align="center" width="100"></el-table-column>
                <el-table-column prop="email" label="邮箱" align="center" width="150"> </el-table-column>
                <el-table-column prop="createTime" label="创建时间" align="center"  width="150"></el-table-column>
               
                <el-table-column prop="roleNames" label="角色" show-overflow-tooltip align="center"  width="100"></el-table-column>
                <el-table-column prop="isStatus" label="是否启用" align="center" width="70">
                  <template slot-scope="scope">
                         <span v-if="scope.row.isStatus=='0'">启用</span>
                         <span v-if="scope.row.isStatus=='1'">禁用</span>
                    </template>
                </el-table-column>
                <el-table-column prop="loginTime" label="登录时间"  align="center" width="130"></el-table-column>
                <el-table-column prop="ip" label="登录ip"  align="center" ></el-table-column>
                <el-table-column label="操作" width="" align="center"  v-if="button_role&&(button_role.delete||button_role.edit||button_role.look||button_role.setting)">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-setting" v-if="button_role&&button_role.setting" @click="ztreeEdit(scope.$index, scope.row)">设置角色</el-button>
                        <el-button type="text" icon="el-icon-edit"  v-if="button_role&&button_role.edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red"  v-if="button_role&&button_role.delete"   @click="handleDelete(scope.$index, scope.row)" >删除</el-button>
                        <el-button type="text" icon="el-icon-document"  v-if="button_role&&button_role.look" @click="handleLook(scope.$index, scope.row)">查看</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="page"
                    :page-sizes="[10, 15, 30, 50, 100]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total" >
                 </el-pagination>
            </div>
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog :title="titleName"  :visible.sync="editVisible" width="30%" @close="closeDilog('form')">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" class="input"></el-input>
                </el-form-item>
                <el-form-item label="登录名" prop="loginName">
                    <el-input v-model="form.loginName" class="input"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password" v-show="show_password">
                    <el-input v-model="form.password" type="password"  class="input"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" class="input"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form.email" class="input"></el-input>
                </el-form-item>
                
                <el-form-item label="角色" prop="roles_value">
                   <el-select v-model="form.roles_value" filterable placeholder="请选择角色" style="width:300px;">
                        <el-option
                        v-for="item in options_role"
                        :key="item.id"
                        :label="item.roleName"
                        :value="item.id+'@'+item.roleName">
                        </el-option>
                   </el-select>
                </el-form-item>
                
                <el-form-item label="是否启用">
                   <el-radio v-model="form.isStatus" label="0">启用</el-radio>
                   <el-radio v-model="form.isStatus" label="1">禁用</el-radio>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 添加 修改-->
        <el-dialog :title="ztreeTitleName" class="dialog-ztree"  :visible.sync="ztreeEditVisible" width="25%">
             <ul id="ztree" class="ztree"></ul>
             <span slot="footer" class="dialog-footer">
                <el-button @click="ztreeEditVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRole">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import { fetchData } from '../../../../api/index';
    import baseURL_ from '@/utils/baseUrl.js';
    export default {
        name: 'basetable',
        data() {
            return {
                editVisible: false,
                page:1,
                total:1000,
                pageSize:10,
                tableData: [],
                multipleSelection: [],
                title:'',
                titleName:'',
                ztreeEditVisible:false,
                ztreeTitleName:'',
                user_id:'',
                show_password:false,
                button_role:{},
                rules: {
                    name: [
                        { required: true, message: '请填写用户姓名', trigger: 'blur' }
                    ],
                    loginName:[
                        { required: true, message: '请填写登录名', trigger: 'blur' }
                    ],
                    password:[
                        { required: true, message: '请填写登录密码', trigger: 'blur' }
                    ],
                     phone:[
                        { required: true, message: '请填写手机号码', trigger: 'blur' }
                    ],
                    email:[
                        { required: true, message: '请填写邮箱', trigger: 'blur' }
                    ],
                   
                    roles_value:[
                        { required: true, message: '请选择角色', trigger: 'change' }
                    ],
                },
                form:{
                   id:'',
                   name:'',
                   loginName:'',
                   phone:'',
                   email:'',
                   isStatus:'',
                   type:'',
                   typeId:'',
                   typeName:'',
                   password:''
                },
                options_org:[],
                //options_value:'',
                options_role:[],
                //roles_value:'',
                 setting:{
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "parentId"
                        },
                        key: {
                            name: "roleName"
                        }
                    },
                    callback: {
                        
                    },
                    check: {
                        enable: true,
                        chkStyle: "checkbox",
                        chkboxType: { Y: "ps", N: "ps" }
                    },
                },
                name:''
            }
        },
        created() {
            this.title=this.$route.meta.title;
            this.button();
            this.getData();
        },
        computed: {
           
        },
        methods: {
            handleSizeChange(val){
                   this.pageSize=val;
                   this.getData();
            },
            handleCurrentChange(val){
                   this.page=val;
                   this.getData();
            },
             handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleDelete(index, row){
                this.$confirm('确认删除？')
                .then( e=> {
                  
                   this.delete(row.id);

                }).catch(_ => {});
                 
            },
            search(){
                this.getData();
            },
            rest(){
               this.name="";
               this.getData();
            },
            selectOrg(){
             this.$forceUpdate();
            },
            async button(){
                var but=await this.$http.get(baseURL_.sysUrl+'/sys/permission/button',{ 
                    params: {'code':this.$route.path}
                });
                this.button_role=but.data.data;
            },
            handleLook(index, row){
                 this.$router.push({ path:'/user_details',query: {id: row.id}})
            },
            async saveEdit(){
                var addOrEdit={};
                var flg=true;
                
                this.$refs['form'].validate((valid) => {
                if (!valid) {
                       flg=false;
                     } 
                })
                if(flg){
                   
                    if(this.form.roles_value){
                        this.form.roleIds=this.form.roles_value.split("@")[0];
                    }
                    if(this.form.id!=null){
                        
                        addOrEdit= await this.$http.post(baseURL_.sysUrl+'/sys/user/update',this.$qs.stringify(this.form));
                    }else{
                        addOrEdit= await this.$http.post(baseURL_.sysUrl+'/sys/user/add',this.$qs.stringify(this.form));
                    }
                    this.$message(addOrEdit.data.message);
                    if(addOrEdit.data.code==200){
                        this.editVisible=false;
                    }
                    this.getData();
                }

                
            },
            async delete(id){
                const del = await this.$http.get(baseURL_.sysUrl+'/sys/user/delete',{ 
                    params: {'id':id}
                });
                this.$message(del.data.message);
                if(del.data.code==200){
                   this.getData();
                }
                  
            },
            async handleEdit(index, row) {
                this.titleName="修改";  
                const role1 = await this.$http.get(baseURL_.sysUrl+'/sys/user/listAll');
                this.options_role=role1.data.data;
                
               
                const user = await this.$http.get(baseURL_.sysUrl+'/sys/user/getById',{ 
                    params: {'id':row.id}
                });
                if(user.data.code==200){
                    this.form.id=user.data.data.id;
                    this.form.name=user.data.data.name;
                    this.form.loginName=user.data.data.loginName;
                    this.form.phone=user.data.data.phone;
                    this.form.email=user.data.data.email;
                    this.form.isStatus=user.data.data.isStatus+'';
                    this.form.password=user.data.data.password;
                   
                    if(user.data.data.orgIds){
                        var orgids=user.data.data.orgIds.split(",");
                        var ov=[];
                        for(var i=0;i<orgids.length;i++){
                           ov.push(parseInt(orgids[i]));
                        }
                        
                        this.form.options_value=ov;
                        
                    }
                    
                   if(user.data.data.sysRoleList){
                     var role = user.data.data.sysRoleList[0];
                     this.form.roles_value=user.data.data.sysRoleList[0].id+'@'+user.data.data.sysRoleList[0].roleName;
                   }
                    
                }
                 this.editVisible=true;
                 this.show_password=false;
            },
            closeDilog:function(form){
        
                this.$refs[form].resetFields();//将form表单重置
            },
            add(){
               this.form={};
               this.getRole();
               this.editVisible=true;
               this.show_password=true;
               this.titleName="添加";
               
            },
            async getRole(){
                const role = await this.$http.get(baseURL_.sysUrl+'/sys/user/listAll');
                this.options_role=role.data.data;
            },
            
            // 获取 easy-mock 的模拟数据
            async getData() {
                const user = await this.$http.get(baseURL_.sysUrl+'/sys/user/list',{ 
                    params: {'page':this.page,'pageSize':this.pageSize,'name':this.name}
                    });
                if(user.data.code==200){
                  this.tableData=user.data.data.records;
                  this.total=user.data.data.total;
                  this.page=user.data.data.current;
                }
            },
            async ztreeEdit(index, row){
                this.ztreeTitleName="角色";
                this.ztreeEditVisible=true;
                const permiss = await this.$http.get(baseURL_.sysUrl+'/sys/user/getRoleByUserId',{ 
                    params: {'userId':row.id}
                });
                this.user_id=row.id;
                $.fn.zTree.init($("#ztree"), this.setting, permiss.data.data);
            },
            async saveRole(){
               var treeObj = $.fn.zTree.getZTreeObj("ztree");
               var nodes = treeObj.getCheckedNodes(true);
               if(nodes.length==0){
                 this.$message("请选择角色！");
                 return;
               }
               var permissArr=[];
               for(var i=0;i<nodes.length;i++){
                  permissArr.push(nodes[i].id);
               }
               var obj={};
               obj.userId=this.user_id;
               obj.roleIds=permissArr.join(",");
               var result= await this.$http.post(baseURL_.sysUrl+'/sys/user/saveRole',this.$qs.stringify(obj));
               this.$message(result.data.message);
               if(result.data.code==200){
                 this.ztreeEditVisible=false;
                 this.role_id="";
                 this.getData();
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
    .input{
        width:300px;
    }
   ul.ztree{
        margin-top: 10px;
        border: 1px solid #617775;
        background: #f0f6e4;
        width: 250px;
        height: 180px;
        overflow-y: scroll;
        overflow-x: auto;
        margin:auto;
        
    }
</style>