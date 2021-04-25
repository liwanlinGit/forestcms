<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> {{titled}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input placeholder="用户名或登录名" v-model="name" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="rest">重置</el-button>
            </div>
            <el-table  :data="tableData"  border class="table" ref="multipleTable"  @selection-change="handleSelectionChange">
                <el-table-column type="index" label="序号" width="55" align="center" >
                   <template slot-scope="scope">
                         <span >{{(page-1)*pageSize+scope.$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="用户名"  align="center" width="80"></el-table-column>
                <el-table-column prop="loginName" label="登录名"  align="center" width="80"></el-table-column>
                <el-table-column prop="roleName" label="角色名"  align="center" width="80"></el-table-column>
                <el-table-column prop="createTime" label="操作时间" align="center"  width="150"></el-table-column>
                <el-table-column prop="ip" label="IP" align="center" width="100"></el-table-column>
                <el-table-column prop="modelName" label="操作模块" align="center" ></el-table-column>
                <el-table-column prop="description" show-overflow-tooltip label="异常日志" align="center" ></el-table-column>
                <el-table-column prop="url" label="地址" align="center" width="150"></el-table-column>
                <el-table-column prop="methodType" label="请求类型" align="center" width="100"></el-table-column>
                <el-table-column prop="method" label="请求方法" align="center" width="100"></el-table-column>
                <el-table-column prop="args" label="参数" align="center" width="150"></el-table-column>
                <el-table-column label="操作"  align="center" width="80"  v-if="button_role&&button_role.look">
                    <template slot-scope="scope">
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

        
    </div>
</template>
  
<script>
    import { fetchData } from '../../../../api/index';
    import baseURL_ from '@/utils/baseUrl.js';
    export default {
        name: 'basetable',
        data() {
            return {
                button_role:{},
                page:1,
                total:1000,
                pageSize:10,
                tableData: [],
                multipleSelection: [],
                title:'',
                name:''
            }
        },
        created() {
            this.title=this.$route.meta.title;
            this.getData();
            this.button();
        },
        methods: {

            async button(){
                var but=await this.$http.get(baseURL_.sysUrl+'/sys/permission/button',{ 
                    params: {'code':this.$route.path}
                });
                this.button_role=but.data.data;
            },

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
            // 初始化数据
            async getData() {
                const sysExceptionLogs = await this.$http.get(baseURL_.sysUrl+'/sys/exceptionLogs/list',{ 
                    params: {'page':this.page,'pageSize':this.pageSize,'name':this.name}
                    });
                if(sysExceptionLogs.data.code==200){
                  this.tableData=sysExceptionLogs.data.data.records;    
                  this.total=sysExceptionLogs.data.data.total;
                  this.page=sysExceptionLogs.data.data.current;
                }
            },

            handleLook(index, row){
                this.$router.push({ path:'/sys_exception_detail',query: {id: row.id}})

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
</style>