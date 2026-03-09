<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="模板名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入模板名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="模板状态" clearable size="small">
          <el-option label="正常" :value="0" />
          <el-option label="停用" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="small"
          @click="handleAdd"
          v-hasPermi="['taskmgt:template:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['taskmgt:template:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" prop="id" width="80" align="center" />
      <el-table-column label="模板名称" prop="name" width="150" show-overflow-tooltip />
      <el-table-column label="描述" prop="description" show-overflow-tooltip />
      <el-table-column label="适用机器人组" width="180">
        <template slot-scope="scope">
          <el-tag v-for="groupId in scope.row.robotGroupIds" :key="groupId" size="small" style="margin-right: 4px;">
            {{ getRobotGroupName(groupId) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="字段数量" align="center" width="80">
        <template slot-scope="scope">
          <el-tag size="small">{{ scope.row.fields?.length || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="步骤数量" align="center" width="80">
        <template slot-scope="scope">
          <el-tag size="small" type="warning">{{ scope.row.steps?.length || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="0"
            :inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button size="small" type="text" @click="handleView(scope.row)">查看</el-button>
          <el-button size="small" type="text" @click="handleUpdate(scope.row)" v-hasPermi="['taskmgt:template:edit']">修改</el-button>
          <el-button size="small" type="text" danger @click="handleDelete(scope.row)" v-hasPermi="['taskmgt:template:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="适用机器人组" prop="robotGroupIds">
          <el-select v-model="form.robotGroupIds" multiple placeholder="请选择" style="width: 100%">
            <el-option v-for="item in robotGroups" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-tabs type="border-card">
          <el-tab-pane label="表单字段">
            <div style="margin-bottom: 10px">
              <el-button type="primary" size="small" @click="addField">添加字段</el-button>
            </div>
            <el-table :data="form.fields" style="width: 100%">
              <el-table-column label="字段ID" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.id" size="small" placeholder="如：start_location" />
                </template>
              </el-table-column>
              <el-table-column label="字段标签" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.label" size="small" placeholder="如：起始位置" />
                </template>
              </el-table-column>
              <el-table-column label="字段类型" width="120">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type" size="small">
                    <el-option label="文本" value="text" />
                    <el-option label="数字" value="number" />
                    <el-option label="日期" value="date" />
                    <el-option label="时间" value="time" />
                    <el-option label="下拉" value="select" />
                    <el-option label="位置" value="location" />
                    <el-option label="图片" value="image" />
                    <el-option label="音频" value="audio" />
                    <el-option label="视频" value="video" />
                    <el-option label="文件" value="file" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="必填" width="60">
                <template slot-scope="scope">
                  <el-checkbox v-model="scope.row.required"></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" danger @click="removeField(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="标准作业流程">
            <div style="margin-bottom: 10px">
              <el-button type="primary" size="small" @click="addStep">添加步骤</el-button>
            </div>
            <el-table :data="form.steps" style="width: 100%">
              <el-table-column label="步骤名称" width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name" size="small" placeholder="如：前往药房" />
                </template>
              </el-table-column>
              <el-table-column label="描述" min-width="250">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description" type="textarea" :rows="1" placeholder="步骤描述" />
                </template>
              </el-table-column>
              <el-table-column label="预估时间(分)" width="120">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.estimatedTime" :min="1" :max="120" size="small" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" danger @click="removeStep(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTemplate, getTemplate, addTemplate, updateTemplate, delTemplate } from '@/api/taskmgt'

export default {
  name: 'TaskTemplate',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      templateList: [],
      robotGroups: [
        { id: 1, name: '配送机器人组' },
        { id: 2, name: '巡检机器人组' },
        { id: 3, name: '清洁机器人组' },
        { id: 4, name: '安防机器人组' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        status: ''
      },
      title: '',
      open: false,
      form: {
        id: null,
        name: '',
        description: '',
        robotGroupIds: [],
        fields: [],
        steps: []
      },
      rules: {
        name: [{ required: true, message: '模板名称不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTemplate(this.queryParams).then(response => {
        this.templateList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => this.loading = false)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    getRobotGroupName(id) {
      const group = this.robotGroups.find(g => g.id === id)
      return group ? group.name : id
    },
    handleStatusChange(row) {
      let text = row.status === 0 ? '启用' : '停用'
      this.$modal.confirm('确认要' + text + '“' + row.name + '”模板吗？').then(() => {
        return updateTemplate(row)
      }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === 0 ? 1 : 0
      })
    },
    handleAdd() {
      this.reset()
      this.title = '添加模板'
      this.open = true
    },
    handleUpdate(row) {
      this.reset()
      getTemplate(row.id).then(response => {
        this.form = response.data
        this.title = '修改模板'
        this.open = true
      })
    },
    handleView(row) {
      // 简单查看弹窗，可自行扩展
      this.$alert(JSON.stringify(row, null, 2), '模板详情', { dangerouslyUseHTMLString: true })
    },
    handleDelete(row) {
      const ids = row.id ? [row.id] : this.ids
      this.$modal.confirm('是否确认删除所选模板？').then(() => {
        return delTemplate(ids.join(','))
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    addField() {
      this.form.fields.push({ id: '', label: '', type: 'text', required: false })
    },
    removeField(index) {
      this.form.fields.splice(index, 1)
    },
    addStep() {
      this.form.steps.push({ name: '', description: '', estimatedTime: 5 })
    },
    removeStep(index) {
      this.form.steps.splice(index, 1)
    },
    reset() {
      this.form = {
        id: null,
        name: '',
        description: '',
        robotGroupIds: [],
        fields: [],
        steps: []
      }
      this.resetForm('form')
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id) {
            updateTemplate(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addTemplate(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    }
  }
}
</script>
