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
                <el-input placeholder="AppName" name="search" v-model="appName"  class="handle-input mr10"></el-input>
                <el-input placeholder="名称" name="search" v-model="name"  class="handle-input mr10"></el-input>
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
                <el-table-column prop="appname" label="AppName"  align="center" width="150"></el-table-column>
                <el-table-column prop="title" label="名称"  align="center" width="150"></el-table-column>
                <el-table-column prop="addressType" label="注册方式"  align="center" >
                    <template slot-scope="scope">
                          <span v-if="scope.row.addressType=='1'" >手动录入</span>
                          <span v-if="scope.row.addressType=='0'" >自动注册</span>
                    </template>
                </el-table-column>
                <el-table-column prop="addressList" label="OnLine 机器地址" align="center" > </el-table-column>
                <el-table-column label="操作" width="" align="center"  v-if="button_role&&(button_role.delete||button_role.edit)">
                    <template slot-scope="scope">
                        <!-- <el-button type="text" icon="el-icon-setting" @click="ztreeEdit(scope.$index, scope.row)">设置角色</el-button>  -->
                        <el-button type="text" icon="el-icon-edit"  v-if="button_role&&button_role.edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red"  v-if="button_role&&button_role.delete"   @click="handleDelete(scope.$index, scope.row)" >删除</el-button>
                        
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
        <el-dialog title="执行器"  :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form"  label-width="100px">
                <el-form-item label="AppName">
                   <el-input v-model="form.appname" class="input input-width"></el-input>
                </el-form-item>
                <el-form-item label="执行器名称" >
                    <el-input v-model="form.title"  class="input input-width"></el-input>
                </el-form-item>
                <el-form-item label="注册方式" >
                  <el-radio-group v-model="form.addressType" @change="changeHandler">
                    <el-radio label="0">自动注册</el-radio>
                    <el-radio label="1">手动录入</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="机器地址">
                   <el-input type="textarea" :disabled="textarea_disabled" placeholder="请输入执行器地址列表，多地址用逗号隔开" v-model="form.addressList"></el-input>
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
export default {
  data() {
    return {
      textarea_disabled: false,
      editVisible: false,
      page: 1,
      total: 0,
      pageSize: 10,
      tableData: [],
      title:'',
      multipleSelection: [],
      button_role: {},
      name: "",
      appName: "",
      form: {
        appname: "",
        title: "",
        addressType: ""
      }
    };
  },
  created() {
    this.title=this.$route.meta.title;
    this.button();
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
    handleDelete(index, row) {
      this.$confirm("确认删除？")
        .then(e => {
          this.delete(row.id);
        })
        .catch(_ => {});
    },
    async handleEdit(index, row) {
      this.editVisible = true;
      const getById = await this.$http.get(
        baseURL_.jobUrl + "/job/jobGroup/getById",
        {
          params: { id: row.id }
        }
      );
      this.form = getById.data.data;
      this.form.addressType = getById.data.data.addressType + "";
    },
    search() {
      this.getData();
    },
    rest() {
      this.getData();
    },

    async button() {
      var but = await this.$http.get(baseURL_.sysUrl + "/sys/permission/button", {
        params: { code: this.$route.path }
      });
      this.button_role = but.data.data;
    },
    changeHandler() {
      if (this.form.addressType == "0") {
        this.textarea_disabled = true;
        this.form.addressList = "";
      } else {
        this.textarea_disabled = false;
      }
    },
    add() {
      this.form = {};
      this.editVisible = true;
    },
    async saveEdit() {
      if (this.form.id != null) {
          var res = await this.$http.post(
            baseURL_.jobUrl + "/job/jobGroup/update",
            this.$qs.stringify(this.form)
          );
          if (res.data.status == "success") {
          this.$message({ message: "修改成功", type: "success" });
          this.editVisible = false;
          this.getData();
        } else {
          this.$message({ message: "修改失败", type: "error" });
        }


      } else {
        var res = await this.$http.post(
          baseURL_.jobUrl + "/job/jobGroup/save",
          this.$qs.stringify(this.form)
        );
        if (res.data.status == "success") {
          this.$message({ message: "添加成功", type: "success" });
          this.editVisible = false;
          this.getData();
        } else {
          this.$message({ message: "添加失败", type: "error" });
        }
      }
    },
    async delete(id) {
      const del = await this.$http.get(baseURL_.jobUrl + "/job/jobGroup/delete", {
        params: { id: id }
      });

      if (del.data.status == "success") {
        this.$message({ message: "删除成功", type: "success" });
        this.getData();
      } else {
        this.$message({ message: "删除失败", type: "error" });
      }
    },

    // 获取 easy-mock 的模拟数据
    async getData() {
      const user = await this.$http.get(baseURL_.jobUrl + "/job/jobGroup/list", {
        params: {
          page: this.page,
          pageSize: this.pageSize,
          name: this.name,
          appName: this.appName
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