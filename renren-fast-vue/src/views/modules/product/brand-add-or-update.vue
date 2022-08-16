<template>
  <el-dialog
    :title="!dataForm.brandId ? 'Add' : 'Edit'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="210px">
    <el-form-item label="Brand Name" prop="name">
      <el-input v-model="dataForm.name" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="Brand Logo Storage Directory" prop="logo">
      <single-upload v-model="dataForm.logo"></single-upload>
      <!-- <el-input v-model="dataForm.logo" placeholder=""></el-input> -->
    </el-form-item>
    <el-form-item label="Introduction" prop="descript">
      <el-input v-model="dataForm.descript" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="Show or Not" prop="showStatus">
      <!-- <el-input v-model="dataForm.showStatus" placeholder=""></el-input> -->
      <el-switch v-model="dataForm.showStatus" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
    </el-form-item>
    <el-form-item label="Initial" prop="firstLetter">
      <el-input v-model="dataForm.firstLetter" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="Order" prop="sort">
      <el-input v-model="dataForm.sort" placeholder=""></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmit()">Confirm</el-button>
    </span>
  </el-dialog>
</template>

<script>
import singleUpload from "@/components/upload/singleUpload"
// import singleUpload from '../../../components/upload/singleUpload.vue'
  export default {
  components: { singleUpload },
    data () {
      return {
        visible: false,
        dataForm: {
          brandId: 0,
          name: '',
          logo: '',
          descript: '',
          showStatus: '',
          firstLetter: '',
          sort: ''
        },
        dataRule: {
          name: [
            { required: true, message: 'Field required.', trigger: 'blur' }
          ],
          logo: [
            { required: true, message: 'Field required.', trigger: 'blur' }
          ],
          descript: [
            { required: true, message: 'Field required.', trigger: 'blur' }
          ],
          showStatus: [
            { required: true, message: 'Field required.', trigger: 'blur' }
          ],
          firstLetter: [
            { required: true, message: 'Field required.', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: 'Field required.', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.brandId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.brandId) {
            this.$http({
              url: this.$http.adornUrl(`/product/brand/info/${this.dataForm.brandId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.brand.name
                this.dataForm.logo = data.brand.logo
                this.dataForm.descript = data.brand.descript
                this.dataForm.showStatus = data.brand.showStatus
                this.dataForm.firstLetter = data.brand.firstLetter
                this.dataForm.sort = data.brand.sort
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/product/brand/${!this.dataForm.brandId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'brandId': this.dataForm.brandId || undefined,
                'name': this.dataForm.name,
                'logo': this.dataForm.logo,
                'descript': this.dataForm.descript,
                'showStatus': this.dataForm.showStatus,
                'firstLetter': this.dataForm.firstLetter,
                'sort': this.dataForm.sort
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
