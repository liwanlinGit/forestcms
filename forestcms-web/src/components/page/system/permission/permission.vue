<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> 菜单管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <!-- <el-button type="primary" icon="el-icon-delete" class="handle-del mr10">批量删除</el-button> -->
                <el-button type="primary" icon="el-icon-plus" class="handle-del mr10" v-if="button_role&&button_role.add" @click="add">添加</el-button>
                <el-input placeholder="菜单名称"  v-model="name" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="rest">重置</el-button>
            </div>
            <el-table  row-key="id" lazy :data="tableData" :load="load" :tree-props="{children: 'children', hasChildren: 'hasChildren'}"    border id="table_id" ref="multipleTable"  @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" ></el-table-column>
                <el-table-column type="index" label="序号" prop="num" width="55" align="center" >
                    <template slot-scope="scope">
                         <span >{{(page-1)*pageSize+scope.$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="菜单名称"  align="left" width="200"></el-table-column>
                <el-table-column prop="type" label="类别"  align="center" width="150">
                    <template slot-scope="scope">
                         <span v-if="scope.row.type=='0'" style="display: inline-block;background-color: #D2F1DB;width: 50px;color:#48D972;">菜单</span>
                         <span v-if="scope.row.type=='1'" style="display: inline-block;background-color: #B7D8F9;width: 50px;color:#167CE3;">按钮</span>
                         <span v-if="scope.row.type=='2'" style="display: inline-block;background-color: #EEDCC6;width: 50px;color:#E78915;">查询</span>
                         <span v-if="scope.row.type=='3'" style="display: inline-block;background-color: #EDCEF1;width: 50px;color:#C71DDD;">其他</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" align="left"  width="200"></el-table-column>
                <el-table-column prop="url" label="请求地址" align="left" >
                    <!-- <template slot-scope="scope">
                         <span v-if="scope.row.isParent=='true'">-</span>
                         <span v-if="scope.row.isParent=='false'">{{scope.row.url}}</span>
                    </template> -->

                </el-table-column>
                <el-table-column prop="urlType" label="操作编码"  align="left" width="100"></el-table-column>
                <el-table-column prop="priority" label="排序"  align="center" width="80"></el-table-column>
                <el-table-column prop="iconName" label="图标"  align="left" ></el-table-column>
                <el-table-column label="操作" width="" align="center"  v-if="button_role&&(button_role.delete||button_role.edit)">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-edit" v-if="button_role&&button_role.edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red"  v-if="button_role&&button_role.delete" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
        <el-dialog :title="titleName"  :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="类别">
                    <el-select v-model="form.type" placeholder="请选择"  @change="selectType">
                        <el-option label="菜单" :value="0" ></el-option>
                        <el-option label="按钮" :value="1"></el-option>
                        <el-option label="列表" :value="2"></el-option>
                        <el-option label="其他" :value="3"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="菜单名称">
                <el-input v-model="form.name" class="input"></el-input>
                </el-form-item>
                <el-form-item label="父菜单" v-show="parentNameShow">
                    <el-input v-model="form.parentName" readOnly class="input"></el-input>
                </el-form-item>
                <el-form-item label="访问地址">
                <el-input v-model="form.url" class="input"></el-input>
                </el-form-item>
                <el-form-item label="排序">
                    <el-input v-model="form.priority" class="input"></el-input>
                </el-form-item>
                <el-form-item  label="图标">
                    <el-input v-model="form.iconName" class="input"></el-input>
                </el-form-item>
                <el-form-item  label="操作编码">
                    <el-input v-model="form.urlType" class="input"></el-input>
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
                parentNameShow:false,
                button_role:{},
                maps:new Map(),
                parent_id:'',
                form:{
                   id:'',
                   name:'',
                   parentId:'',
                   type:'',
                   treeDepth:'',
                   url:'',
                   urlType:'',
                   priority:'',
                   iconName:'',
                   parentName:'',
                   isParent:''
                },
                name:''
            }
        },
        created() {
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
            selectType(val){

            //    if(val===0){
            //      this.iconShow=true;
            //      this.urlTypeShow=false;
            //      this.form.urlType='';
            //    }else{
            //      this.iconShow=false;
            //      this.urlTypeShow=true;
            //      this.form.iconName='';
            //    }
            },
             async button(){
                var but=await this.$http.get(baseURL_.sysUrl+'/sys/permission/button',{ 
                    params: {'code':this.$route.path}
                });
                this.button_role=but.data.data;
            },
            // 初始化数据
            async getData() {
                const permissions = await this.$http.get(baseURL_.sysUrl+'/sys/permission/list',{ 
                    params: {'page':this.page,'pageSize':this.pageSize,'name':this.name}
                    });
                if(permissions.data.code==200){
                  this.tableData=permissions.data.data.records;
                  this.total=permissions.data.data.total;
                  this.page=permissions.data.data.current;
                }
            },
            async load(tree, treeNode, resolve){
                const permissions = await this.$http.get(baseURL_.sysUrl+'/sys/permission/list',{ 
                    params: {'parentId':tree.id}
                    });
                    this.maps.set(tree.id,{tree,treeNode,resolve})
                    resolve(permissions.data.data)
            },
            add(){
               if(this.multipleSelection.length>1){
                  this.$message("不能选择多条数据进行操作");
                  return;
               }
            
               this.form={};
               if(this.multipleSelection.length==0){
                    this.parentNameShow=false;
               }else{
                    this.parentNameShow=true;
                    this.form.parentName=this.multipleSelection[0].name
                    this.form.parentId=this.multipleSelection[0].id;
                    this.parent_id=this.multipleSelection[0].id;
                    
                    
               }
               this.editVisible=true;
               this.titleName="添加";
            },
            async saveEdit(){
                var addOrEdit={};
                if(this.form.id!=null){
                   addOrEdit= await this.$http.post(baseURL_.sysUrl+'/sys/permission/update',this.$qs.stringify(this.form));
                }else{
                    if(this.form.parentId==null){
                      this.form.parentId=0;
                      this.form.treeDepth=1;
                    }
                    this.form.isParent='false';
                    addOrEdit= await this.$http.post(baseURL_.sysUrl+'/sys/permission/add',this.$qs.stringify(this.form));
                }
                this.$message(addOrEdit.data.message);
                if(addOrEdit.data.code==200){
                    this.editVisible=false;
                }
                var obj= this.maps.get(this.parent_id)
                    if(obj!=null){
                       this.load(obj.tree,obj.treeNode,obj.resolve)
                    }else{
                        this.multipleSelection[0].hasChildren=true;
                    }
            },
            handleDelete(index, row){
                if(row.isParent=='true'){
                   this.$message("该节点下有数据，不允许删除！");
                   return;
                }
                this.$confirm('确认删除？')
                .then( e=> {
                  
                   this.delete(row.id,row.parentId);

                }).catch(_ => {});
                 
            },
           async delete(id,pId){
                const del = await this.$http.get(baseURL_.sysUrl+'/sys/permission/delete',{ 
                    params: {'id':id}
                });
                this.$message(del.data.message);
                if(del.data.code==200){
                    var obj= this.maps.get(pId)
                    if(obj!=null){
                       this.load(obj.tree,obj.treeNode,obj.resolve)
                    }else{
                        this.getData();
                    }
                    
                }
                  
            },
            async handleEdit(index, row) {
                this.parent_id=row.parentId;
                this.titleName="修改";
                const permiss = await this.$http.get(baseURL_.sysUrl+'/sys/permission/getById',{ 
                    params: {'id':row.id}
                });
                if(permiss.data.code==200){
                    this.form.id=permiss.data.data.id;
                    this.form.name=permiss.data.data.name;
                    this.form.parentId=permiss.data.data.parentId;
                    this.form.type=permiss.data.data.type;
                    this.form.treeDepth=permiss.data.data.treeDepth;
                    this.form.url=permiss.data.data.url;
                    this.form.priority=permiss.data.data.priority;
                    this.form.iconName=permiss.data.data.iconName;
                    this.form.isParent=permiss.data.data.isParent;
                    this.form.parentName=permiss.data.data.parentName;
                    this.form.urlType=permiss.data.data.urlType;
                    this.selectType(this.form.type)
                    if(this.form.treeDepth=='1'){
                        this.parentNameShow=false;
                    }else{
                        this.parentNameShow=true;
                    }
                   
                }
                 this.editVisible=true;
            },
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
     
</style>
<style>
#table_id .el-table__body .cell .el-table__expand-icon{
     display:inline-block !important;
    }
</style>