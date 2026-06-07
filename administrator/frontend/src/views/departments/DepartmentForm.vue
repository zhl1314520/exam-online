<template>
  <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%">
    <el-form ref="form" :model="formData" label-width="100px">
      <el-form-item label="院系名称" prop="name"
        :rules="[{ required: true, message: '请输入院系名称', trigger: 'blur' }]">
        <el-input v-model="formData.name" placeholder="请输入院系名称" />
      </el-form-item>
      <el-form-item label="院系编码" prop="code">
        <el-input v-model="formData.code" placeholder="请输入院系编码" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  props: {
    visible: Boolean,
    departmentData: Object
  },
  data() {
    return {
      dialogVisible: this.visible,
      formData: {
        id: null,
        name: '',
        code: ''
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.formData.id ? '编辑院系' : '新增院系'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    departmentData: {
      immediate: true,
      handler(val) {
        if (val) {
          this.formData = { ...val }
        } else {
          this.resetForm()
        }
      }
    }
  },
  methods: {
    resetForm() {
      this.formData = {
        id: null,
        name: '',
        code: ''
      }
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$emit('submit', this.formData)
          this.dialogVisible = false
        }
      })
    }
  }
}
</script>