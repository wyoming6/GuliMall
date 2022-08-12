<template>
  <div>
    <el-switch
      v-model="draggable"
      active-text="Enable Drag-and-Drop"
      inactive-text="Unenable Drag-and-Drop"
    >
    </el-switch>
    <el-button v-if="draggable" @click="batchSave">Batch Save</el-button>
    <el-button type="danger" @click="batchDelete">Batch Delete</el-button>
    <el-tree
      :data="menus"
      :props="defaultProps"
      :expand-on-click-node="false"
      show-checkbox
      node-key="catId"
      :default-expanded-keys="expandedKey"
      :draggable="draggable"
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
      ref="menuTree"
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
      title: "",
      maxLevel: 0, //子节点的最大level
      updateNodes: [],
      draggable: false,
      pCid: []
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
      this.category.name = "";
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
    },

    allowDrop(draggingNode, dropNode, type) {
      console.log("allowDrop:", draggingNode, dropNode, type);

      // 求子节点的最大level，结果存放在maxLevel
      this.countNodeLevel(draggingNode.data);

      // 当前节点的深度 = 当前拖动的节点的子节点的最大level - 当前拖动的节点的level + 1
      // let depth = this.maxLevel - draggingNode.data.catLevel + 1;
      let depth = Math.abs(this.maxLevel - draggingNode.level) + 1;

      if (type == "inner") {
        return depth + dropNode.level <= 3;
      } else {
        return depth + dropNode.parent.level <= 3;
      }
    },

    // 求出node的子节点的最大level
    countNodeLevel(node) {
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > this.maxLevel) {
            this.maxLevel = node.childNodes[i].level;
          }
          this.countNodeLevel(node.childNodes[i]);
        }
      }
    },

    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log("handleDrop: ", draggingNode, dropNode, dropType);
      //the new parent of the draggingNode
      let pCid = 0;

      let siblings = 0;

      if (dropType == "before" || dropType == "after") {
        pCid =
          dropNode.parent.data.catId == undefined
            ? 0
            : dropNode.parent.data.catId;
        siblings = dropNode.parent.childNodes;
      } else {
        pCid = dropNode.data.catId;
        siblings = dropNode.childNodes;
      }
      this.pCid.push(pCid); //即使二者同名，但是是不同的

      //find the new sorting order of the draggingNode
      for (let i = 0; i < siblings.length; i++) {
        //如果遍历的是当前正在拖拽的节点
        if (siblings[i].data.catId == draggingNode.data.catId) {
          let catLevel = draggingNode.level;
          if (siblings[i].level != draggingNode.level) {
            //当前节点的层级发生变化
            catLevel = siblings[i].level;
            //更新当前被拖动节点这一支的所有层级（包括分支）
            this.updateChildNodeLevel(siblings[i]);
          }
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid: pCid,
            catLevel: catLevel
          });
        } else {
          this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
        }
      }
      console.log("updateNodes:", this.updateNodes);
    },

    updateChildNodeLevel(node) {
      if (node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          var cNode = node.childNodes[i].data;
          this.updateNodes.push({
            catId: cNode.catId,
            catLevel: node.childNodes[i].level
          });
          this.updateChildNodeLevel(node.childNodes[i]);
        }
      }
    },

    batchSave() {
      this.$http({
        url: this.$http.adornUrl("/product/category/update/sort"),
        method: "post",
        data: this.$http.adornData(this.updateNodes, false)
      }).then(({ data }) => {
        this.$message({
          message: "Menu reordered.",
          type: "success"
        });
        //refresh webpage after removing items
        this.getMenus();
        //expand parent node after refreshing the page
        this.expandedKey = this.pCid;
        //reset
        this.updateNodes = [];
        this.maxLevel = 0;
        // this.pCid = 0;
      });
    },
    batchDelete() {
      let catIds = [];
      let checkNodesNames = [];
      let checkedNodes = this.$refs.menuTree.getCheckedNodes();
      console.log("checkedNodes", checkedNodes);

      for (let i = 0; i < checkedNodes.length; i++) {
        catIds.push(checkedNodes[i].catId);
      }

      for(let i = 0; i< checkedNodes.length;i++){
        checkNodesNames.push(checkedNodes[i].name);
      }
      //A message box
      this.$confirm(
        `This will permanently delete the item: ${checkNodesNames}. Continue?`,
        "Warning",
        {
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
          type: "warning"
        }
      )
        .then(() => {
          //if you choose "OK"
          //logical deletion
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData(catIds, false)
          }).then(({ data }) => {
            //pop up a message if operation is successful
            this.$message({
              message: "Delete completed.",
              type: "success"
            });
            //refresh webpage after removing items
            this.getMenus();
            //expand parent node after refreshing the page
            // this.expandedKey = [node.parent.data.catId];
          });
        })
        .catch(() => {}); //if you choose "Cancel"
    }
  },

  created() {
    this.getMenus();
  }
};
</script>

<style lang="scss" scoped></style>
