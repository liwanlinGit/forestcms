<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> {{title}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-plus" class="handle-del mr10" v-if="button_role&&button_role.add"  @click="add">添加</el-button>
                <el-input placeholder="角色名称或编码" v-model="name"  class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="rest">重置</el-button>
               
            </div>
            <el-table :data="tableData" border class="table" ref="multipleTable"  @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" ></el-table-column>
                <el-table-column type="index" label="序号" width="55" align="center" >
                   <template slot-scope="scope">
                         <span >{{(page-1)*pageSize+scope.$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="roleName" label="角色名称"  align="center" ></el-table-column>
                <el-table-column prop="roleCode" label="CODE"  align="center" width="150"></el-table-column>
                <el-table-column prop="createTime" label="创建时间" align="center"  width="200"></el-table-column>
                <el-table-column prop="isAdmin" label="是否管理员" align="center" width="150">
                     <template slot-scope="scope">
                         <i v-if="scope.row.isAdmin=='0'"  class="el-icon-error red"></i>
                         <i v-if="scope.row.isAdmin!='0'" class="el-icon-success red"></i>
                    </template>
                </el-table-column>
                <el-table-column prop="isBuiltIn" label="是否内置"  align="center" width="150">
                    <template slot-scope="scope">
                         <i v-if="scope.row.isBuiltIn=='0'"  class="el-icon-error red"></i>
                         <i v-if="scope.row.isBuiltIn!='0'" class="el-icon-success red"></i>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="" align="center" v-if="button_role&&(button_role.delete||button_role.edit||button_role.add_role)">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-setting" v-if="button_role&&button_role.add_role" @click="ztreeEdit(scope.$index, scope.row)">设置权限</el-button>
                        <el-button type="text" icon="el-icon-edit" v-if="button_role&&button_role.edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red" v-if="button_role&&button_role.delete" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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

         <!-- 编辑弹出框 添加 修改-->
        <el-dialog :title="titleName"  :visible.sync="editVisible" width="25%">
            <el-form ref="form" :model="form"  :rules="rules" label-width="100px">
                <el-form-item label="角色名称" prop="roleName">
                   <el-input v-model="form.roleName" class="input input-width"></el-input>
                </el-form-item>
                <el-form-item label="角色编码" prop="roleCode">
                    <el-input v-model="form.roleCode"  class="input input-width"></el-input>
                </el-form-item>
                <el-form-item label="是否管理">
                   <el-radio v-model="form.isAdmin" label="0">否</el-radio>
                   <el-radio v-model="form.isAdmin" label="1">是</el-radio>
                </el-form-item>
                <el-form-item label="是否内置">
                    <el-radio v-model="form.isBuiltIn" label="0">否</el-radio>
                    <el-radio v-model="form.isBuiltIn" label="1">是</el-radio>
                    
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
                <el-button type="primary" @click="saveRolePermiss">确 定</el-button>
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
                page:1,
                total:1000,
                pageSize:10,
                tableData: [],
                multipleSelection: [],
                editVisible:false,
                titleName:'',
                title:'',
                ztreeTitleName:'',
                ztreeEditVisible:false,
                role_id:'',
                button_role:{},
                form:{
                   id:'',
                   roleName:'',
                   roleCode:'',
                   isAdmin:'',
                   isBuiltIn:''
                },
                rules: {
                    roleName: [
                        { required: true, message: '请填写角色名称', trigger: 'blur' }
                    ],
                    roleCode:[
                        { required: true, message: '请填写角色code', trigger: 'blur' }
                    ]
                },
                name:'',
                setting:{
                    data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "parentId"
                    }
                    },
                    callback: {
                        
                    },
                    check: {
                        enable: true,
                        chkStyle: "checkbox",
                        chkboxType: { Y: "s", N: "s" }
                    },
                }
            }
        },
        created() {
            this.title=this.$route.meta.title;
            this.button();
            this.getData();
        },
        methods: {
            //改变每页页数
            handleSizeChange(val){
                   this.pageSize=val;
                   this.getData();
            },
            //分页
            handleCurrentChange(val){
                   this.page=val;
                   this.getData();
            },
             handleSelectionChange(val) {
                this.multipleSelection = val;
            },
             search(){
                this.getData();
            },
            rest(){
               this.name="";
               this.getData();
            },
             async button(){
                var but=await this.$http.get(baseURL_.sysUrl+'/sys/permission/button',{ 
                    params: {'code':this.$route.path}
                });
                this.button_role=but.data.data;
            },
            // 初始化数据
            async getData() {
                const role = await this.$http.get(baseURL_.sysUrl+'/sys/role/list',{ 
                    params: {'page':this.page,'pageSize':this.pageSize,'name':this.name}
                });
                if(role.data.code==200){
                  this.tableData=role.data.data.records;
                  this.total=role.data.data.total;
                  this.page=role.data.data.current;
                }
            },
            add(){
               this.form={};
               this.editVisible=true;
               this.titleName="添加";
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
                    if(this.form.id!=null){
                        addOrEdit=await  this.$http.post(baseURL_.sysUrl+'/sys/role/update',this.$qs.stringify(this.form));
                    }else{
                        if(this.form.parentId==null){
                            this.form.parentId=0;
                            this.form.treeDepth=1;
                        }
                        this.form.isParent='false';
                        addOrEdit=await  this.$http.post(baseURL_.sysUrl+'/sys/role/add',this.$qs.stringify(this.form));
                    }
                    this.$message(addOrEdit.data.message);
                    if(addOrEdit.data.code==200){
                        this.editVisible=false;
                    }
                    this.getData();   
                    }
                        
            },
             handleDelete(index, row){
                if(row.isParent=='true'){
                   this.$message("该节点下有数据，不允许删除！");
                   return;
                }
                this.$confirm('确认删除？')
                .then( e=> {
                  
                   this.delete(row.id);

                }).catch(_ => {});
                 
            },
           async delete(id){
                const del = await this.$http.get(baseURL_.sysUrl+'/sys/role/delete',{ 
                    params: {'id':id}
                });
                this.$message(del.data.message);
                if(del.data.code==200){
                   this.getData();
                }
                  
            },
            async handleEdit(index, row) {
                this.titleName="修改";
                const permiss = await this.$http.get(baseURL_.sysUrl+'/sys/role/getById',{ 
                    params: {'id':row.id}
                });
                if(permiss.data.code==200){
                    this.form.id=permiss.data.data.id;
                    this.form.roleName=permiss.data.data.roleName;
                    this.form.roleCode=permiss.data.data.roleCode;
                    this.form.isAdmin=permiss.data.data.isAdmin;
                    this.form.isBuiltIn=permiss.data.data.isBuiltIn;
                }
                 this.editVisible=true;
            },
            async ztreeEdit(index, row){
                this.ztreeTitleName="权限菜单";
                this.ztreeEditVisible=true;
                const permiss = await this.$http.get(baseURL_.sysUrl+'/sys/role/getPermissByRoleId',{ 
                    params: {'roleId':row.id}
                });
                this.role_id=row.id;
                $.fn.zTree.init($("#ztree"), this.setting, permiss.data.data);
            },
            async saveRolePermiss(){
               var treeObj = $.fn.zTree.getZTreeObj("ztree");
               var nodes = treeObj.getCheckedNodes(true);
               if(nodes.length==0){
                 this.$message("请选择权限菜单！");
                 return;
               }
               var permissArr=[];
               for(var i=0;i<nodes.length;i++){
                  permissArr.push(nodes[i].id);
               }
               var obj={};
               obj.roleId=this.role_id;
               obj.permissionsIds=permissArr.join(",");
               var result= await this.$http.post(baseURL_.sysUrl+'/sys/role/saveRolePermiss',this.$qs.stringify(obj));
               this.$message(result.data.message);
               if(result.data.code==200){
                 this.ztreeEditVisible=false;
                 this.role_id="";
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
        min-height:500px;
    }
    .input-width{
        width:60%;
    }
    .dialog-ztree{
        align:center;
    }
     ul.ztree{
        margin-top: 10px;
        border: 1px solid #617775;
        background: #f0f6e4;
        width: 250px;
        height: 280px;
        overflow-y: scroll;
        overflow-x: auto;
        margin:auto;
        
    }
</style>