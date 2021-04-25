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
                <el-select class="mr10" name="search" v-model="triggerStatus" placeholder="请选择">
                    <el-option key="-1" label="全部" value="-1"> </el-option>
                    <el-option key="0" label="停止" value="0"> </el-option>
                    <el-option key="1" label="运行" value="1"> </el-option>
                </el-select>
                <el-input placeholder="任务描述" name="search" v-model="jobDesc"  class="handle-input mr10" style="width:200px;"></el-input>
                <el-input placeholder="JobHandler" name="search" v-model="executorHandler"  class="handle-input mr10" style="width:200px;"></el-input>
                <el-input placeholder="负责人" name="search" v-model="author"  class="handle-input mr10" style="width:200px;"></el-input>
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
                <el-table-column prop="jobDesc" label="任务描述"  align="center"></el-table-column>
                <el-table-column prop="executorHandler" label="JobHandler"  align="center"></el-table-column>
                <el-table-column  label="运行模式"  align="center" width="200">
                   <template slot-scope="scope">
                          <span>{{scope.row.glueType}}[{{scope.row.executorHandler}}]</span>
                          
                    </template>
                </el-table-column>
                <el-table-column prop="jobCron" label="Cron"  align="center" width="200"></el-table-column>
                <el-table-column prop="author" label="负责人" align="center" width="150"> </el-table-column>
                <el-table-column prop="triggerStatus" label="状态" align="center" width="80" >
                    <template slot-scope="scope">
                          <span v-if="scope.row.triggerStatus=='0'">停止</span>
                          <span v-if="scope.row.triggerStatus=='1'">运行</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作"  align="center"   v-if="button_role&&(button_role.delete||button_role.edit||button_role.start||button_role.stop||button_role.start||button_role.trigger||button_role.traggerTime)"   width="200" >
                    <template slot-scope="scope">
                        <!-- <el-button type="text" icon="el-icon-setting" @click="ztreeEdit(scope.$index, scope.row)">设置角色</el-button>  -->
                        <el-button type="text" icon="el-icon-edit"  v-if="button_role&&button_role.edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red"  v-if="button_role&&button_role.delete"   @click="handleDelete(scope.$index, scope.row)" >删除</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red"  v-if="button_role&&button_role.stop&&scope.row.triggerStatus=='1'"   @click="handleStop(scope.$index, scope.row)" >停止</el-button>
                        <el-button type="text" icon="el-icon-edit"   v-if="button_role&&button_role.start&&scope.row.triggerStatus=='0'"   @click="handleStart(scope.$index, scope.row)" >启动</el-button>
                        <el-button type="text" icon="el-icon-edit"   v-if="button_role&&button_role.trigger"   @click="handleTrigger(scope.$index, scope.row)" >执行一次</el-button>
                        <el-button type="text" icon="el-icon-edit"   v-if="button_role&&button_role.traggerTime"   @click="handleNextTriggerTime(scope.$index, scope.row)" >下次执行时间</el-button>
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
        <el-dialog title="任务管理"  :visible.sync="editVisible" width="50%">
            <el-form ref="form" :inline="true" :model="form"  label-width="100px">
                <el-form-item label="执行器">
                    <el-select v-model="form.jobGroup" placeholder="请选择">

                        <el-option  v-for="item in jobGroup" :key="item.id" :label="item.title" :value="item.id">
                           
                        </el-option>
                      </el-select>
                
                </el-form-item>
                <el-form-item label="任务描述" >
                    <el-input v-model="form.jobDesc"  class="input input-width" style="width:200px;"></el-input>
                </el-form-item>
                <el-form-item label="路由策略" >
                    <el-select v-model="form.executorRouteStrategy" placeholder="请选择">

                        <el-option  v-for="item in erse" :key="item.split('@')[0]" :label="item.split('@')[1]" :value="item.split('@')[0]">
                           
                        </el-option>
                    </el-select>
                    
                </el-form-item>
                <el-form-item label="Cron" class="cron">
                 
                   <el-popover v-model="cronPopover">
                    <el-input slot="reference" @click="cronPopover=true" v-model="form.jobCron" placeholder="请输入定时策略" style="width: 200px ;"></el-input>
                    <cron @change="changeCron" @close="cronPopover=false" i18n="cn"></cron>

                    </el-popover>
                    
                </el-form-item>
                 <el-form-item label="运行模式" >
                      <el-select v-model="form.glueType" placeholder="请选择" :disabled="glueTypeReadonly">
                        <el-option  v-for="item in glueType"  :key="item.split('@')[0]" :label="item.split('@')[1]" :value="item.split('@')[0]">
                        </el-option>
                      </el-select>
                </el-form-item>
                
                 <el-form-item label="JobHandler" >
                    <el-input v-model="form.executorHandler"  class="input input-width" style="width:200px;"></el-input>
                </el-form-item>
                 <el-form-item label="阻塞处理策略" >
                     <el-select v-model="form.executorBlockStrategy" placeholder="请选择">
                     <el-option   v-for="item in ebse" :key="item.split('@')[0]" :label="item.split('@')[1]" :value="item.split('@')[0]">
                     </el-option>
                    </el-select>
                </el-form-item>
                 <el-form-item label="子任务ID" >
                    <el-input v-model="form.childJobId"  class="input input-width" style="width:200px;"></el-input>
                </el-form-item>   
                <el-form-item label="任务超时时间" >
                    <el-input v-model="form.executorTimeout"  class="input input-width" style="width:200px;"></el-input>
                </el-form-item>   
                <el-form-item label="失败重试次数" >
                    <el-input v-model="form.executorFailRetryCount"  class="input input-width" style="width:200px;"></el-input>
                </el-form-item>   
                <el-form-item label="负责人" >
                    <el-input v-model="form.author"  class="input input-width" style="width:200px;"></el-input>
                </el-form-item>   
                <el-form-item label="报警邮件" >
                    <el-input v-model="form.alarmEmail"  class="input input-width" style="width:200px;"></el-input>
                </el-form-item>     
                <el-form-item label="任务参数" >
                    <el-input v-model="form.executorParam"  class="input input-width" style="width:510px;"></el-input>
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
import { fetchData } from "../../../../api/index";
import baseURL_ from "@/utils/baseUrl.js";
import {cron} from 'vue-cron';
export default {
    components: { cron },
  data() {
    return {
      glueTypeReadonly:false,
      showCronBox:false,
      cronPopover:false,
      editVisible: false,
      page: 1,
      total: 0,
      pageSize: 10,
      tableData: [],
      title:'',
      multipleSelection: [],
      button_role: {},
      jobDesc: "",
      executorHandler: "",
      author: "",
      erse: [],
      ebse: [],
      glueType:[],
      jobGroup:[],
      triggerStatus: "-1",
      form: {}
    };
  },
  created() {
    this.title=this.$route.meta.title;
    this.button();
    this.getData();
    this.data();
  },
  computed: {},
  methods: {
      changeCron(val){
        this.form.jobCron=val
      },
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
    async saveEdit(){
        if (this.form.id != null) {
            delete this.form.addTime;
            delete this.form.updateTime;
            delete this.form.glueUpdatetime;
           var res = await this.$http.post(
                baseURL_.jobUrl + "/job/jobinfo/update",
                this.$qs.stringify(this.form)
            );
            if(res.data.code=="200"){
                this.$message("修改成功");
                this.editVisible=false;
                this.getData();
             }else{
                this.$message.error(res.data.msg);
                
             }
        }else{
             var res = await this.$http.post(
                baseURL_.jobUrl + "/job/jobinfo/add",
                this.$qs.stringify(this.form)
             );
             if(res.data.code=="200"){
                this.$message("添加成功");
                this.editVisible=false;
                this.getData();
             }else{
                this.$message.error(res.data.msg);
                
             }
        }
    },
    handleDelete(index, row) {
      this.$confirm("确认删除？")
        .then(e => {
          this.delete(row.id);
        })
        .catch(_ => {});
    },
    async handleEdit(index, row){
         this.glueTypeReadonly=true;
         const res = await this.$http.get(baseURL_.jobUrl + "/job/jobinfo/getById",{
              params: { id: row.id }
         });
         this.editVisible=true;
         this.form=res.data;
    },
     handleStop(index, row){
        this.$confirm("确认停止？")
        .then(e => {
          this.stop(row.id);
        })
        .catch(_ => {});
    },
    async stop(id){
       const res = await this.$http.get(baseURL_.jobUrl + "/job/jobinfo/stop",{
              params: { id: id }
         });
         
        if (res.data.code == 200) {
         this.$message("暂停成功");
         this.getData();
        }
    },
    handleStart(index, row){
       this.$confirm("确认启动？")
        .then(e => {
          this.start(row.id);
        })
        .catch(_ => {});
    },
    async start(id){
        const res = await this.$http.get(baseURL_.jobUrl + "/job/jobinfo/start",{
              params: { id: id }
         });
         
        if (res.data.code == 200) {
         this.$message("启动成功");
         this.getData();
        }
    },
    async handleTrigger(index, row){
         const res = await this.$http.get(baseURL_.jobUrl + "/job/jobinfo/trigger",{
              params: { id: row.id }
         });
        if (res.data.code == 200) {
         this.$message("执行成功");
         this.getData();
        }

    },
    async handleNextTriggerTime(index, row){
        const res = await this.$http.get(baseURL_.jobUrl + "/job/jobinfo/nextTriggerTime",{
              params: { cron: row.jobCron }
         });
         if (res.data.code == 200) {
            var resdata=res.data.content;
            var html="";
             for (var i=0; i < resdata.length; i++) {
                 html+="<div>"+resdata[i]+"</div>";
                 
             }
             this.$alert(html, '下次执行时间', {
                dangerouslyUseHTMLString: true
            });
         }else{
             this.$message(res.data.msg);
         }
         
         
         


    },
    async data() {
      const res = await this.$http.get(baseURL_.jobUrl + "/job/jobinfo/data");
      this.erse = res.data.erse;
      this.ebse = res.data.ebse;
      this.glueType=res.data.glueType;
      this.jobGroup=res.data.jobGroup;
    },
    search() {
      this.getData();
    },
    rest() {
      this.jobDesc = "";
      this.executorHandler = "";
      this.author = "";
      this.triggerStatus = "-1";
      this.getData();
    },

    async button() {
      var but = await this.$http.get(baseURL_.sysUrl + "/sys/permission/button", {
        params: { code: this.$route.path }
      });
      this.button_role = but.data.data;
    },
    add() {
      this.glueTypeReadonly=false;
      this.editVisible = true;
    },
    async delete(id) {
      const del = await this.$http.get(baseURL_.jobUrl + "/job/jobinfo/remove", {
        params: { id: id }
      });
      this.$message("删除成功");
      if (del.data.code == 200) {
        this.getData();
      }
    },

    // 获取 easy-mock 的模拟数据
    async getData() {
      const user = await this.$http.get(baseURL_.jobUrl + "/job/jobinfo/pageList", {
        params: {
          page: this.page,
          pageSize: this.pageSize,
          jobDesc: this.jobDesc,
          executorHandler: this.executorHandler,
          author: this.author,
          triggerStatus: this.triggerStatus
        }
      });
      if (user.data.data) {
        this.tableData = user.data.data;
        this.total = user.data.recordsTotal;
        this.page = user.data.page;
      }
    }
  }
};
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