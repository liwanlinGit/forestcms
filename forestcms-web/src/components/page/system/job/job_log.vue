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
                执行器<el-select v-model="jobGroup" placeholder="请选择执行器" class="mr10 ml5" style="width:180px;">
                  <el-option label="全部" value="0"></el-option>
                  <el-option v-for="item in jobGroupList" :key="item.id" :label="item.title" :value="item.id">
                  </el-option>
                </el-select>
                 任务 <el-select v-model="jobId" placeholder="请选择任务" class="mr10 ml5" style="width:170px;">
                  <el-option label="全部" value="0"></el-option>
                  <el-option v-for="item in jobInfoList" :key="item.id" :label="item.jobDesc" :value="item.id">
                  </el-option>
                </el-select>

                状态 <el-select v-model="logStatus" placeholder="请选择状态" class="mr10 ml5" style="width:100px;">
                  <el-option label="全部" value="-1"></el-option>
                  <el-option label="成功" value="1"></el-option>
                  <el-option label="失败" value="2"></el-option>
                  <el-option label="进行中" value="3"></el-option>
                </el-select>
                
                
                调度时间<el-input placeholder="请输入内容" v-model="filterTime" class="input-with-select ml5" style="width:410px;">
                    <el-select v-model="selectTime" slot="prepend" placeholder="请选择" style="width:120px;" @change="changeSelectTime" >
                        <el-option label="最近一小时" value="1"></el-option>
                        <el-option label="今日" value="2"></el-option>
                        <el-option label="昨日" value="3"></el-option>
                        <el-option label="本月" value="4"></el-option>
                        <el-option label="上个月" value="5"></el-option>
                        <el-option label="最近一周" value="6"></el-option>
                        <el-option label="最近一月" value="7"></el-option>
                    </el-select>
                    </el-input>
                <el-button type="primary" icon="el-icon-search" class="ml5" @click="search">搜索</el-button>
                <el-button type="primary" icon="el-icon-search" @click="rest">重置</el-button>
            </div>
            <el-table  :data="tableData" border class="table" ref="multipleTable"  @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" ></el-table-column>
                <el-table-column type="index" label="序号" width="55" align="center" >
                   <template slot-scope="scope">
                         <span >{{(page-1)*pageSize+scope.$index+1}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="triggerTime" label="调度时间"  align="center" width="150"></el-table-column>
                <el-table-column prop="triggerCode" label="调度结果"  align="center" width="150">
                 <template slot-scope="scope">
                          <span v-if="scope.row.triggerCode=='200'">成功</span>
                          <span v-if="scope.row.triggerCode=='500'">失败</span>
                          <span v-if="scope.row.triggerCode=='0'"></span>
                    </template>

                </el-table-column>
                <el-table-column prop="addressType" label="调度备注"  align="center" >
                    
                </el-table-column>
                <el-table-column prop="handleTime" label="执行时间" align="center" > </el-table-column>
                <el-table-column prop="handleCode" label="执行结果" align="center" > 
                  <template slot-scope="scope">
                          <span v-if="scope.row.handleCode=='200'">成功</span>
                          <span v-if="scope.row.handleCode=='500'">失败</span>
                          <span v-if="scope.row.handleCode=='502'">失败（超时）</span>
                          <span v-if="scope.row.handleCode=='0'"></span>
                    </template>

                </el-table-column>
                <el-table-column prop="addressList" label="执行备注" align="center" > </el-table-column>
                <el-table-column label="操作" width="" align="center"  v-if="button_role&&(button_role.LogDetail)">
                    <template slot-scope="scope">
                        <!-- <el-button type="text" icon="el-icon-setting" @click="ztreeEdit(scope.$index, scope.row)">设置角色</el-button>  -->
                        <el-button type="text" icon="el-icon-edit"  v-if="button_role&&button_role.LogDetail" @click="handleLogDetail(scope.$index, scope.row)">执行日志</el-button>
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
import { fetchData } from "../../../../api/index";
import baseURL_ from "@/utils/baseUrl.js";

export default {
  data() {
    return {
      page: 1,
      total: 0,
      pageSize: 10,
      tableData: [],
      multipleSelection: [],
      button_role: {},
      form: {},
      jobGroupList:[],
      jobInfoList:[],
      selectTime:'2',
      filterTime:'',
      title:'',
      jobGroup:'0',
      jobId:'0',
      logStatus:'-1'
    };
  },
  created() {
    this.title=this.$route.meta.title;
     this.button();
     this.data();
     this.getData();
     
  },
  computed: {},
  methods: {
    handleSizeChange(val) {
      this.pageSize = val;
      this.getData();
    },
    handleCurrentChange(val) {
      this.page = val;
      this.getData();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    search() {
      this.getData();
    },
    rest() {
      //this.filterTime='2021-04-21 00:00:00 - 2021-04-21 23:59:59',
      this.changeSelectTime('2');
      this.selectTime='2';
      this.jobGroup='0';
      this.jobId='0';
      this.logStatus='-1'
      this.getData();
    },
    formatDate (date) {
        let y = date.getFullYear();
        let MM = date.getMonth() + 1;
        MM = MM < 10 ? ('0' + MM) : MM;
        let d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        let h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        let m = date.getMinutes();
        m = m < 10 ? ('0' + m) : m;
        let s = date.getSeconds();
        s = s < 10 ? ('0' + s) : s;
        return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
      },
      formatDateY (date) {
        let y = date.getFullYear();
        let MM = date.getMonth() + 1;
        MM = MM < 10 ? ('0' + MM) : MM;
        let d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        return y + '-' + MM + '-' + d;
      },
      
    changeSelectTime(val){
       if(val!=''){
          if(val=='1'){ //最近一小时
            var sdtime1 = new Date();
            var sdtime2 = sdtime1.setHours(sdtime1.getHours() -1)//小时
            var end = new Date();
            this.filterTime=this.formatDate(new Date(sdtime2))+" - "+this.formatDate(end)
          }else if(val=='2'){ //今日
            var sdtime1 = new Date();
            this.filterTime=this.formatDateY(sdtime1)+" 00:00:00 - "+this.formatDateY(sdtime1)+" 23:59:59"
          }else if(val=='3'){ //昨日
            var sdtime2 = new Date().setDate((new Date().getDate() -1))//天
            this.filterTime=this.formatDateY(new Date(sdtime2))+" 00:00:00 - "+this.formatDateY(new Date(sdtime2))+" 23:59:59"
          }else if(val=='4'){ //本月
             var date = new Date();
             var d = new Date(date.getFullYear(), (date.getMonth() + 1), 0);
             this.filterTime=date.getFullYear()+"-"+(date.getMonth() + 1)+"-01 00:00:00 - "+date.getFullYear()+"-"+(date.getMonth() + 1)+"-"+d.getDate()+" 23:59:59"
           }else if(val=='5'){ //上个月
               var sdtime2 = new Date(new Date().setMonth((new Date().getMonth() -1)))//月份
               var d = new Date(sdtime2.getFullYear(), (sdtime2.getMonth() + 1), 0);
               this.filterTime=sdtime2.getFullYear()+"-"+(sdtime2.getMonth() + 1)+"-01 00:00:00 - "+sdtime2.getFullYear()+"-"+(sdtime2.getMonth() + 1)+"-"+d.getDate()+" 23:59:59"

           }else if(val=='6'){ //近一周
               var sdtime2 = new Date().setDate((new Date().getDate() -6))//天
               this.filterTime=this.formatDateY(new Date(sdtime2))+" 00:00:00 - "+this.formatDateY(new Date())+" 23:59:59"
           }else if(val=='7'){ //近一个月
               var sdtime2 = new Date().setMonth((new Date().getMonth() -1))//一个月
               this.filterTime=this.formatDateY(new Date(sdtime2))+" 00:00:00 - "+this.formatDateY(new Date())+" 23:59:59"
           }
       }
    },
    async data(){
       const res = await this.$http.get(baseURL_.jobUrl + "/job/joblog/data");
       this.jobGroupList=res.data.jobGroupList;
       this.jobInfoList=res.data.jobsInfoList;

    },
     async button() {
      var but = await this.$http.get(baseURL_.sysUrl + "/sys/permission/button", {
        params: { code: this.$route.path }
      });
      this.button_role = but.data.data;
    },
    async getData() {
      const log = await this.$http.get(baseURL_.jobUrl + "/job/joblog/list", {
        params: {
          page: this.page,
          pageSize: this.pageSize,
          filterTime:this.filterTime,
          jobGroup:this.jobGroup,
          jobId:this.jobId,
          logStatus:this.logStatus
        }
      });
      if (log.data.data) {
        this.tableData = log.data.data;
        this.total = log.data.recordsTotal;
        this.page = log.data.page;
      }
    },
    handleLogDetail(index, row){
       this.$router.push({ path: '/logDetailPage', query: { id: row.id} });
    }


  }
}
</script>

<style scoped>
.wd200 {
  width: 200px;
}
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
.del-dialog-cnt {
  font-size: 12px;
  text-align: center;
}
.table {
  width: 100%;
  font-size: 12px;
}

.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}
.ml5 {
  margin-left: 5px;
}
.container {
  min-height: 500px;
}
.input {
  width: 300px;
}
ul.ztree {
  margin-top: 10px;
  border: 1px solid #617775;
  background: #f0f6e4;
  width: 250px;
  height: 180px;
  overflow-y: scroll;
  overflow-x: auto;
  margin: auto;
}
</style>