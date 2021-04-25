<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> {{title}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input placeholder="服务名称" v-model="name"  class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="rest">重置</el-button>
               
            </div>
            <el-table :data="tableData" border class="table_service" ref="multipleTable" :span-method="objectSpanMethod"  @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" ></el-table-column>
                <el-table-column type="index" label="序号" width="55" align="center" >
                   <template slot-scope="scope">
                         <span >{{(page-1)*pageSize+scope.$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="服务名称"  align="center" width="200"></el-table-column>
                <el-table-column prop="groupName" label="分组名称"  align="center" width="150"></el-table-column>
                <el-table-column prop="clusterCount" label="实例数" align="center" width="100"></el-table-column>
                <el-table-column prop="healthyInstanceCount" label="健康实例数"  align="center" width="100"></el-table-column>
                <el-table-column prop="ip" label="IP"  align="center" width="100"  ></el-table-column>
                <el-table-column prop="port" label="端口"  align="center" width="100"></el-table-column>
                <el-table-column prop="weight" label="权重"  align="center" width="100" ></el-table-column>
                <el-table-column prop="healthy" label="健康状态"  align="center" width="200"></el-table-column>
                <el-table-column prop="source" label="元数据"  align="center" ></el-table-column>
                
            </el-table>
           
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
                editVisible:false,
                titleName:'',
                title:'',
                ztreeTitleName:'',
                ztreeEditVisible:false,
                role_id:'',
                button_role:{},
                name:'',
                mergeArray:[],
                position:0
            }
        },
        created() {
            this.title=this.$route.meta.title;

            this.getData();
        },
        methods: {

            rest(){
               this.name='';
               this.getData();
            },
            search(){
               this.getData();
            },
            
             handleSelectionChange(val) {
                this.multipleSelection = val;
            },
             
            // 初始化数据
            async getData() {
                const role = await this.$http.get(baseURL_.sysUrl+'/sys/services/list',{ 
                    params: {'name':this.name}
                });
                if(role.data.code==200){
                  this.tableData=role.data.data;
                  this.mergeArray = [];
                    for (let index2 in this.tableData) {
                    if (index2 === "0") {
                        this.mergeArray.push(1);
                        this.position = 0;
                    } else {
                        if (this.tableData[index2].name === this.tableData[index2 - 1].name) {
                        this.mergeArray.push(0);
                        this.mergeArray[this.position] += 1;
                        } else {
                        this.mergeArray.push(1);
                        this.position = index2;
                        }
                    }
                    }
                }




            },
           
            objectSpanMethod({ row, column, rowIndex, columnIndex }) {
                if (columnIndex === 2||columnIndex===3||columnIndex===4||columnIndex===5) {
                const row =this.mergeArray[rowIndex];
                const col = row > 0 ? 1 : 0;
                return {
                    rowspan: row,
                    colspan: col
                };
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
    .table_service{
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

<style>
.table_service .el-table_1_column_7{
     background-color: #e4fdda;
    }
    .table_service .el-table_1_column_8{
     background-color: #e4fdda;
    }
    .table_service .el-table_1_column_9{
     background-color: #e4fdda;
    }
    .table_service .el-table_1_column_10{
     background-color: #e4fdda;
    }
    .table_service .el-table_1_column_11{
     background-color: #e4fdda;
    }
</style>