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

          <el-button type="text" size="mini" @click="edit(data)">
            Edit
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

    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <!--Form-->
      <el-form :model="category">
        <el-form-item label="Category">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <el-form :model="category">
        <el-form-item label="Icon">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <el-form :model="category">
        <el-form-item label="Unit of Measurement">
          <el-input
            v-model="category.productUnit"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitData">Confirm</el-button>
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
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        catId: null,
        productUnit: "",
        icon: ""
      },
      dialogType: "", //add or edit
      title: ""
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
      this.dialogType = "add";
      this.title = "Add a new category";
      console.log("append", data);
      this.dialogVisible = true;
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1; //in case catLevel is string, we convert it to int by *1

      //清空回显数据
      this.category.catId = null;
      this.category.name="";
      this.category.icon = "";
      this.category.productUnit = "";
      this.category.showStatus = 1;
      this.category.sort = 0;
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

    edit(data) {
      this.dialogType = "edit";
      this.title = "Edit a category";
      console.log("要修改的数据：", data);
      this.dialogVisible = true;

      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: "get"
      }).then(({ data }) => {
        console.log("要回显的数据：", data);
        this.category.name = data.data.name;
        this.category.catId = data.data.catId;
        this.category.icon = data.data.icon;
        this.category.productUnit = data.data.productUnit;

        //不是为了回传修改的信息给数据库，而是因为提交更新后父菜单要保持展开
        //this.expandedKey = [this.category.parentCid];
        //this.category.parentCid 要获取值，否则还是默认值0
        this.category.parentCid = data.data.parentCid;

        // this.category.catLevel=data.data.catLevel;
        // this.category.showStatus=data.data.showStatus;
        // this.category.sort=data.data.sort;
      });
    },

    editCategory() {
      var { catId, name, icon, productUnit } = this.category;
      this.$http({
        url: this.$http.adornUrl("/product/category/update"),
        method: "post",
        data: this.$http.adornData({ catId, name, icon, productUnit }, false)
        // data: this.$http.adornData(this.category, false)
      }).then(({ data }) => {
        this.$message({
          message: "Update completed.",
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

    submitData() {
      if (this.dialogType == "add") {
        this.addCategory();
      }
      if (this.dialogType == "edit") {
        this.editCategory();
      }
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
