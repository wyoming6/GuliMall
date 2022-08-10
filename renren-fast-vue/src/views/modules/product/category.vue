<template>
  <div>
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
      </span> </el-tree
    >,

    <el-dialog title="Tips" :visible.sync="dialogVisible" width="30%">
      <!--Form-->
      <el-form :model="category">
        <el-form-item label="Category">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="addCategory">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
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
      expandedKey: [],
      dialogVisible: false,
      //category的键和CategoryEntity中的成员变量名一致
      category: { name: "", parentCid: 0, catLevel: 0, showStatus: 1, sort: 0 }
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
      this.dialogVisible = true;
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1; //in case catLevel is string, we convert it to int by *1
    },

    addCategory() {
      console.log("提交的三级分类的数据：", this.category);
      this.$http({
        url: this.$http.adornUrl("/product/category/save"),
        method: "post",
        data: this.$http.adornData(this.category, false)
      }).then(({ data }) => {
        //pop up a message if operation is successful
        this.$message({
          message: "Category saved.",
          type: "success"
        });
        //Close the dialogbox
        this.dialogVisible = false;
        //refresh webpage after removing items
        this.getMenus();
        //expand parent node after refreshing the page
        this.expandedKey = [this.category.parentCid];
      });
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
            this.expandedKey = [node.parent.data.catId];
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
