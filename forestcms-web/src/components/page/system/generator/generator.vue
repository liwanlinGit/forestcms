<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> {{title}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input placeholder="表名" v-model="tableName" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="rest">重置</el-button>
            </div>
            <el-table  :data="tableData"  border class="table" ref="multipleTable"  @selection-change="handleSelectionChange">
                <el-table-column type="index" label="序号" width="55" align="center" >
                     <template slot-scope="scope">
                         <span >{{(page-1)*pageSize+scope.$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="tableName" label="表名"  align="left" ></el-table-column>
                <el-table-column prop="tableComment" label="备注"  align="left" ></el-table-column>
                <el-table-column prop="engine" label="存储引擎"  align="center" ></el-table-column>
                <el-table-column label="操作" width="" align="center"  v-if="button_role&&(button_role.code)">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-setting"  v-if="button_role&&button_role.code" @click="handleCode(scope.$index, scope.row)">生成代码</el-button>
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
                page:1,
                total:1000,
                pageSize:10,
                tableData: [],
                multipleSelection: [],
                title:'',
                tableName:'',
                button_role:{}
            }
        },
        created() {
            this.title=this.$route.meta.title;
            this.getData();
            this.button();
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
               this.tableName="";
               this.getData();
            },
            async handleCode(index, row){
                var down=await this.$http.get(baseURL_.generatorUrl+'/generator/code',{ 
                    params: {'tables':row.tableName},
                    responseType: 'arraybuffer',
                });
                var fileName=down.headers['content-disposition'].split('filename=')[1];
                fileName=fileName.replace("\"","").replace("\"","");
                const url = window.URL.createObjectURL(new Blob([down.data]));                
                const link = document.createElement('a');                
                link.href = url;                
                link.setAttribute('download', fileName);                
                document.body.appendChild(link);                
                link.click();     
            },
            async button(){
                var but=await this.$http.get(baseURL_.sysUrl+'/sys/permission/button',{ 
                    params: {'code':this.$route.path}
                });
                this.button_role=but.data.data;
            },
            // 初始化数据
            async getData() {
                const sysLogs = await this.$http.get(baseURL_.generatorUrl+'/generator/list',{ 
                    params: {'page':this.page,'pageSize':this.pageSize,'tableName':this.tableName}
                    });
                if(sysLogs.data.code==200){
                  this.tableData=sysLogs.data.data.list;    
                  this.total=sysLogs.data.data.total;
                  this.page=sysLogs.data.data.pageNum;
                }
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