<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> {{title}}</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container" style="font-size: 12px;position: relative;line-height: 2;">
            <section class="content">
              <pre style="font-size:12px;position:relative;" >
                <div id="logConsole"></div>
                
            </pre>
        </section>

        </div>
           
        
    </div>
</template>
<script>
import { fetchData } from "../../../../api/index";
import baseURL_ from "@/utils/baseUrl.js";

export default {
  data() {
    return {
      id:'',
      fromLineNum:1,
      logConsole:'',
      title:'',
      timer:''
    };
  },
  created() {
    this.title=this.$route.meta.title;
    this.id=this.$route.query.id; 
    this.getData();
  },
  computed: {

  },
  methods: {
     async getData(){
       const res = await this.$http.get(baseURL_.jobUrl + "/job/joblog/logDetailCat",{
         params: {
          id: this.id,
          fromLineNum:this.fromLineNum
        }
       });
       if(res.data.code=="200"){
         if (this.fromLineNum > res.data.content.toLineNum ) {
          clearInterval(this.timer);
          return;
         }
             
          $("#logConsole").append(res.data.content.logContent);
         
          this.fromLineNum = res.data.content.toLineNum + 1;
       }
       
     }

  },
  mounted() {
      this.timer = setInterval(this.getData, 3000);
    },
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