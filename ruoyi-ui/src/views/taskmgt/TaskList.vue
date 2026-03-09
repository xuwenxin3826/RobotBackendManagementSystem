<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="任务名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入" clearable size="small" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable size="small">
          <el-option label="未开始" value="pending" />
          <el-option label="准备中" value="ready" />
          <el-option label="执行中" value="executing" />
          <el-option label="已暂停" value="paused" />
          <el-option label="已完成" value="completed" />
          <el-option label="已禁用" value="disabled" />
          <el-option label="已终止" value="aborted" />
        </el-select>
      </el-form-item>
      <el-form-item label="机器人" prop="robotId">
        <el-select v-model="queryParams.robotId" placeholder="请选择" clearable size="small">
          <el-option v-for="r in robots" :key="r.id" :label="r.name" :value="r.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="任务类型" prop="taskType">
        <el-select v-model="queryParams.taskType" placeholder="请选择" clearable size="small">
          <el-option label="定时任务" value="scheduled" />
          <el-option label="电量任务" value="battery" />
          <el-option label="闲时任务" value="idle" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd">新建</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taskList">
      <el-table-column label="ID" prop="id" width="80" align="center" />
      <el-table-column label="任务名称" prop="name" width="180" show-overflow-tooltip />
      <el-table-column label="模板" prop="templateName" width="140" />
      <el-table-column label="类型" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.type === 'process' ? 'primary' : 'info'">
            {{ scope.row.type === 'process' ? '流程' : '简单' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="触发方式" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getTriggerTag(scope.row.triggerType)">
            {{ getTriggerText(scope.row.triggerType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <span :class="'status-tag status-' + scope.row.status">{{ getStatusText(scope.row.status) }}</span>
          <span v-if="scope.row.isHighRisk" class="status-tag status-high-risk" style="margin-left:4px;">高风险</span>
          <span v-else-if="scope.row.isRisk" class="status-tag status-risk">风险</span>
        </template>
      </el-table-column>
      <el-table-column label="机器人" prop="robotName" width="120" />
      <el-table-column label="优先级" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.priority === 1" type="danger" size="small">高</el-tag>
          <el-tag v-else-if="scope.row.priority === 2" type="warning" size="small">中</el-tag>
          <el-tag v-else type="info" size="small">低</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="组任务" width="70" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isGroupTask" size="small" type="success">是</el-tag>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="small" type="text" @click="handleView(scope.row)">详情</el-button>
          <el-button size="small" type="text" @click="handleUpdate(scope.row)" :disabled="!canEdit(scope.row)">修改</el-button>
          <el-button size="small" type="text" danger @click="handleDelete(scope.row)" :disabled="!canDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 任务对话框，复用TaskDialog组件 -->
    <task-dialog ref="taskDialog" @ok="getList" />
  </div>
</template>

<script>
import { listTask, delTask } from '@/api/taskmgt'
import TaskDialog from './components/TaskDialog'

export default {
  name: 'TaskList',
  components: { TaskDialog },
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      taskList: [],
      robots: [
        { id: 1, name: '机器人A' },
        { id: 2, name: '机器人B' },
        { id: 3, name: '机器人C' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        status: '',
        robotId: '',
        taskType: ''
      },
      statusMap: {
        pending: '未开始',
        ready: '准备中',
        executing: '执行中',
        paused: '已暂停',
        completed: '已完成',
        disabled: '已禁用',
        aborted: '已终止'
      },
      triggerMap: {
        scheduled: { text: '定时', tag: 'primary' },
        battery: { text: '电量', tag: 'success' },
        idle: { text: '闲时', tag: 'warning' }
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTask(this.queryParams).then(response => {
        this.taskList = response.rows || []
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
    getStatusText(status) {
      return this.statusMap[status] || status
    },
    getTriggerText(type) {
      return this.triggerMap[type]?.text || type
    },
    getTriggerTag(type) {
      return this.triggerMap[type]?.tag || 'info'
    },
    canEdit(row) {
      return ['pending', 'ready', 'disabled'].includes(row.status)
    },
    canDelete(row) {
      return ['pending', 'ready', 'disabled', 'completed', 'aborted'].includes(row.status)
    },
    handleAdd() {
      this.$refs.taskDialog.open('create')
    },
    handleUpdate(row) {
      this.$refs.taskDialog.open('edit', row)
    },
    handleView(row) {
      this.$refs.taskDialog.open('view', row)
    },
    handleDelete(row) {
      this.$modal.confirm('确认删除任务“' + row.name + '”吗？').then(() => {
        return delTask(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      })
    }
  }
}
</script>

<style scoped>
.status-tag {
  display: inline-block;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
}
.status-pending { background-color: #f0f0f0; color: #666; }
.status-ready { background-color: #e6f7ff; color: #1890ff; }
.status-executing { background-color: #f6ffed; color: #52c41a; }
.status-paused { background-color: #fffbe6; color: #faad14; }
.status-completed { background-color: #f6ffed; color: #52c41a; }
.status-disabled { background-color: #f5f5f5; color: #999; }
.status-aborted { background-color: #fff1f0; color: #ff4d4f; }
.status-risk { background-color: #fff7e6; color: #fa8c16; border: 1px solid #ff7c2a; }
.status-high-risk { background-color: #fff1f0; color: #ff4d4f; border: 1px solid #ff4d4f; }
</style>
