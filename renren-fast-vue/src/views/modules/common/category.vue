<template>
  <el-tree :data="menus" :props="defaultProps" node-key="catId" ref="menuTree" @node-click="nodeclick">
  </el-tree>
</template>

<script>
export default {
  name: "RenrenFastVueCategory",

  data() {
    return {
      menus: [],
      expandedKey: [],
      defaultProps: {
        children: "children",
        label: "name"
      }
    };
  },

  mounted() {},

  methods: {
    getMenus() {
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get"
      }).then(({ data }) => {
        console.log("Retrieved data succesfully!", data.data);
        this.menus = data.data;
      });
    },
    nodeclick(data,node,component){
        console.log("Tree node has been clicked.", data,node,component)
        //向父组件发送事件tree-node-click，携带了data，node，component。事件名随便写。
        this.$emit("tree-node-click",data,node,component)
    }
  },
  created(){
    this.getMenus();
  }
};
</script>

<style lang="scss" scoped></style>
