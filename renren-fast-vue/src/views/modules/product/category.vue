<template>
  <el-tree
    :data="menus"
    :props="defaultProps"
    :expand-on-click-node="false"
    show-checkbox
    node-key="catId"
    :default-expanded-keys="expandedKey"
  >
    <span class="custom-tree-node" slot-scope="{ node, data }">
      <span>{{ node.label }}</span>
      <span>
        <el-button
          v-if="node.level <= 2"
          type="text"
          size="mini"
          @click="() => append(data)"
        >
          Append
        </el-button>
        <el-button
          v-if="node.childNodes.length == 0"
          type="text"
          size="mini"
          @click="() => remove(node, data)"
        >
          Delete
        </el-button>
      </span>
    </span>
  </el-tree>
</template>

<script>
export default {
  name: "RenrenFastVueCategory",

  data() {
    return {
      menus: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      expandedKey:[]
    };
  },
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

    append(data) {
      console.log("append", data);
    },

    remove(node, data) {
      var ids = [data.catId];
      //A message box
      this.$confirm(
        `This will permanently delete the item: ${data.name}. Continue?`,
        "Warning",
        {
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
          type: "warning"
        }
      )
        .then(() => {
          //logical deletion
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData(ids, false)
          }).then(({ data }) => {
            //pop up a message if operation is successful
            this.$message({
              message: "Delete completed.",
              type: "success"
            });
            //refresh webpage after removing items
            this.getMenus(); 
            //expand parent node after refreshing the page
            this.expandedKey = [node.parent.data.catId]
          });
        })
        .catch(() => {});

      console.log("remove", node, data);
    }
  },

  created() {
    this.getMenus();
  }
};
</script>

<style lang="scss" scoped></style>
