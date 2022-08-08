<template>
<el-tree :data="menus" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
</template>

<script>
export default {
    name: 'RenrenFastVueCategory',

    data() {
      return {
        menus: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      };
    },
    methods: {
      handleNodeClick(data) {
        console.log(data);
      },
      getMenus(){
        this.$http({
            url:this.$http.adornUrl('/product/category/list/tree'),
            method:'get'
        }).then(({data}) => {
            console.log("Retrieved data succesfully!", data.data)
            this.menus = data.data;
        })
      }
    },

    created() {
        this.getMenus();
    }
};
</script>

<style lang="scss" scoped>

</style>