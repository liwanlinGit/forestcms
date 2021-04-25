<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse"
            unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index+''" :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :index="subItem.index+''" :key="subItem.index">
                                <template slot="title">{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" >
                                    {{ threeItem.title }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.index+''" :key="subItem.index">
                                {{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index+''" :key="item.index">
                        <i :class="[item.icon]"></i><span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import bus from '../common/bus';
    import baseURL_ from '@/utils/baseUrl.js';
    export default {
        data() {
            return {
                collapse: false,
                items:{}
            }
        },
        computed:{
            onRoutes(){
                return this.$route.path.replace('/','');
            }
        },
        created(){
            this.loadData();
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.$on('collapse', msg => {
                this.collapse = msg;
            })
        },
        methods:{
            async loadData(){
                const sysPermissions = await this.$http.get(baseURL_.sysUrl+'/sys/permission/getPermissionsByUserId');
                if(sysPermissions.data.code == 200){
                  var datas = sysPermissions.data.data;
                  var len = datas.length;
                  var permission = [];
                  for(var i=0; i<len; i++){
                      if(datas[i].treeDepth == 1){
                        var obj = new Object();
                        obj.title = datas[i].name;
                        obj.icon = datas[i].iconName;
                        obj.id =  datas[i].id;
                        obj.index = i+1;
                        var subs=[];
                        if(datas[i].isParent == "false"){
                            obj.index = datas[i].url;
                        }else{
                            for(var j=0; j<len; j++){
                               if(datas[j].treeDepth == 2 && datas[j].parentId==datas[i].id){
                                 var chil=new Object();
                                 chil.title = datas[j].name;
                                 chil.icon = datas[j].iconName;
                                 chil.index = datas[j].url;
                                 chil.id = (i+1)*(j+1);
                                 subs.push(chil);
                                }
                            }
                            obj.subs=subs;
                        }
                         permission.push(obj)
                    }
                  }
                  this.items=permission;
                  
                }
              
            }
        }
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom:0;
        overflow-y: scroll;
    }
    .sidebar::-webkit-scrollbar{
        width: 0;
    }
    .sidebar-el-menu:not(.el-menu--collapse){
        width: 170px;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
