<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon blue"><i class="el-icon-s-order"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ totalTasks }}</div>
            <div class="stat-label">总任务数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon green"><i class="el-icon-video-play"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ executingCount }}</div>
            <div class="stat-label">执行中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon orange"><i class="el-icon-time"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ readyCount }}</div>
            <div class="stat-label">准备中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon red"><i class="el-icon-video-pause"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ pausedCount }}</div>
            <div class="stat-label">已暂停</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="mt20">
      <div slot="header" class="clearfix">
        <span>所有任务</span>
        <el-button style="float: right;" type="text" @click="refreshList"><i class="el-icon-refresh"></i> 刷新</el-button>
      </div>

      <div style="margin-bottom: 15px; display: flex; align-items: center; gap: 15px;">
        <el-select v-model="filter.robotId" placeholder="选择机器人" clearable style="width: 180px;" @change="onRobotChange">
          <el-option v-for="r in robots" :key="r.id" :label="r.name" :value="r.id" />
        </el-select>
        <el-select v-model="filter.status" placeholder="任务状态" clearable style="width: 150px;">
          <el-option label="未开始" value="pending" />
          <el-option label="准备中" value="ready" />
          <el-option label="执行中" value="executing" />
          <el-option label="已暂停" value="paused" />
        </el-select>
        <el-switch
          v-if="filter.robotId"
          v-model="filter.sortable"
          active-text="启用拖拽排序"
          @change="onSortableChange"
        ></el-switch>
        <div style="margin-left: auto; color: #999; font-size: 13px;">
          <i class="el-icon-info"></i> 选择机器人后，准备中任务可拖拽调整顺序
        </div>
      </div>

      <!-- 使用 Sortable 实现拖拽 -->
      <el-table
        ref="table"
        v-loading="loading"
        :data="filteredTasks"
        row-key="id"
        :row-class-name="rowClassName"
        :key="tableKey"
      >
        <el-table-column label="排序" width="60" align="center" v-if="filter.robotId && filter.sortable">
          <template slot-scope="scope">
            <div v-if="scope.row.status === 'ready'" class="drag-handle">
              <i class="el-icon-rank"></i>
              <span class="queue-index">{{ getReadyIndex(scope.row) + 1 }}</span>
            </div>
            <div v-else style="color: #ccc;">
              <i v-if="scope.row.status === 'executing'" class="el-icon-video-play" style="color:#52c41a;"></i>
              <i v-else-if="scope.row.status === 'paused'" class="el-icon-video-pause" style="color:#faad14;"></i>
              <i v-else-if="scope.row.status === 'pending'" class="el-icon-time" style="color:#999;"></i>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="任务名称" width="180" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <span :class="'status-tag status-' + scope.row.status">{{ getStatusText(scope.row.status) }}</span>
            <span v-if="scope.row.isHighRisk" class="status-tag status-high-risk">高风险</span>
          </template>
        </el-table-column>
        <el-table-column prop="robotName" label="机器人" width="140" />
        <el-table-column label="优先级" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.priority === 1" type="danger" size="small">高</el-tag>
            <el-tag v-else-if="scope.row.priority === 2" type="warning" size="small">中</el-tag>
            <el-tag v-else type="info" size="small">低</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="进度" width="150">
          <template slot-scope="scope">
            <div class="step-progress">
              <span v-for="i in scope.row.totalSteps" :key="i" :class="['step-dot',
                i <= scope.row.completedSteps ? 'completed' :
                (i === scope.row.completedSteps + 1 && scope.row.status === 'executing' ? 'executing' : '')]"></span>
              <span style="margin-left: 8px;">{{ scope.row.completedSteps }}/{{ scope.row.totalSteps }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="最后更新" width="150" />
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="small" type="text" @click="viewDetail(scope.row)">详情</el-button>

            <template v-if="scope.row.status === 'executing' && !scope.row.isHighRisk">
              <el-button size="small" type="text" @click="pauseTask(scope.row)">暂停</el-button>
            </template>
            <template v-if="scope.row.status === 'paused'">
              <el-button size="small" type="text" @click="resumeTask(scope.row)" :disabled="!canResume(scope.row)">继续</el-button>
            </template>
            <template v-if="scope.row.status !== 'disabled' && scope.row.status !== 'aborted' && scope.row.status !== 'executing' && scope.row.status !== 'paused'">
              <el-button size="small" type="text" @click="disableTask(scope.row)">禁用</el-button>
            </template>
            <template v-if="scope.row.status === 'disabled'">
              <el-button size="small" type="text" @click="enableTask(scope.row)">启用</el-button>
            </template>
            <template v-if="scope.row.status === 'executing' || scope.row.status === 'paused'">
              <el-button size="small" type="text" @click="stopTask(scope.row)">停止</el-button>
            </template>
            <template v-if="scope.row.status === 'ready'">
              <el-button size="small" type="text" @click="cancelTask(scope.row)">取消</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import Sortable from 'sortablejs'
import { listTask, pauseTask, resumeTask, disableTask, enableTask, stopTask, cancelTask } from '@/api/taskmgt'

export default {
  name: 'TaskSchedule',
  data() {
    return {
      loading: false,
      tasks: [],
      robots: [
        { id: 1, name: '机器人A' },
        { id: 2, name: '机器人B' },
        { id: 3, name: '机器人C' }
      ],
      filter: {
        robotId: '',
        status: '',
        sortable: false
      },
      sortableInstance: null,
      tableKey: 0,
      statusMap: {
        pending: '未开始',
        ready: '准备中',
        executing: '执行中',
        paused: '已暂停'
      }
    }
  },
  computed: {
    totalTasks() {
      return this.tasks.length
    },
    executingCount() {
      return this.tasks.filter(t => t.status === 'executing').length
    },
    readyCount() {
      return this.tasks.filter(t => t.status === 'ready').length
    },
    pausedCount() {
      return this.tasks.filter(t => t.status === 'paused').length
    },
    filteredTasks() {
      let result = [...this.tasks]
      if (this.filter.robotId) {
        result = result.filter(t => t.robotId === this.filter.robotId)
      }
      if (this.filter.status) {
        result = result.filter(t => t.status === this.filter.status)
      }
      // 排序：执行中 > 已暂停 > 准备中 > 未开始
      const order = { executing: 0, paused: 1, ready: 2, pending: 3 }
      result.sort((a, b) => {
        if (order[a.status] !== order[b.status]) return order[a.status] - order[b.status]
        if (a.status === 'ready' && b.status === 'ready') {
          if (this.filter.sortable && a.sortOrder !== undefined && b.sortOrder !== undefined) {
            return a.sortOrder - b.sortOrder
          }
          return a.priority - b.priority
        }
        return a.priority - b.priority
      })
      return result
    },
    readyTasks() {
      return this.filteredTasks.filter(t => t.status === 'ready')
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTask({}).then(response => {
        this.tasks = (response.rows || []).map(t => ({
          ...t,
          totalSteps: t.totalSteps || 5,
          completedSteps: t.completedSteps || 0,
          sortOrder: t.sortOrder
        }))
        this.loading = false
        this.$nextTick(() => this.initSortable())
      }).catch(() => this.loading = false)
    },
    refreshList() {
      this.getList()
    },
    getStatusText(status) {
      return this.statusMap[status] || status
    },
    rowClassName({ row }) {
      if (row.isHighRisk) return 'abnormal-row'
      return ''
    },
    getReadyIndex(row) {
      return this.readyTasks.findIndex(t => t.id === row.id)
    },
    onRobotChange() {
      // 切换机器人时清除 sortOrder，恢复默认排序
      this.tasks.forEach(t => { delete t.sortOrder })
      this.filter.sortable = false
      this.destroySortable()
      this.tableKey++
    },
    onSortableChange(val) {
      if (val) {
        this.initSortable()
      } else {
        this.destroySortable()
      }
    },
    initSortable() {
      this.destroySortable()
      if (!this.filter.robotId || !this.filter.sortable) return

      const tbody = this.$refs.table.$el.querySelector('.el-table__body-wrapper tbody')
      if (!tbody) return

      // 为所有准备中任务赋予初始sortOrder（按当前顺序）
      const readyTasks = this.filteredTasks.filter(t => t.status === 'ready')
      readyTasks.forEach((t, idx) => {
        if (t.sortOrder === undefined) t.sortOrder = idx
      })

      this.sortableInstance = Sortable.create(tbody, {
        animation: 150,
        ghostClass: 'sortable-ghost',
        dragClass: 'sortable-drag',
        handle: '.drag-handle',
        filter: (evt, target) => {
          const row = target.closest('tr')
          if (!row) return true
          const index = row.rowIndex - 1 // 表头占一行
          const task = this.filteredTasks[index]
          return task && task.status !== 'ready'
        },
        onMove: evt => {
          const relatedRow = evt.related
          const relatedIndex = relatedRow.rowIndex - 1
          const relatedTask = this.filteredTasks[relatedIndex]
          return relatedTask && relatedTask.status === 'ready'
        },
        onEnd: evt => {
          const { oldIndex, newIndex } = evt
          if (oldIndex === newIndex) return

          const movedTask = this.filteredTasks[oldIndex]
          const targetTask = this.filteredTasks[newIndex]
          if (movedTask.status !== 'ready' || targetTask.status !== 'ready') return

          // 更新同一机器人下的准备中任务顺序
          const robotReadyTasks = this.tasks.filter(t => t.robotId === this.filter.robotId && t.status === 'ready')
          robotReadyTasks.sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))

          const movedIdx = robotReadyTasks.findIndex(t => t.id === movedTask.id)
          const targetIdx = robotReadyTasks.findIndex(t => t.id === targetTask.id)
          if (movedIdx === -1 || targetIdx === -1) return

          // 移动数组
          const [removed] = robotReadyTasks.splice(movedIdx, 1)
          robotReadyTasks.splice(targetIdx, 0, removed)

          // 重新赋值 sortOrder
          robotReadyTasks.forEach((t, idx) => { t.sortOrder = idx })

          this.$message.success(`任务“${movedTask.name}”已移至第${targetIdx + 1}位`)
          this.tableKey++ // 强制刷新表格
        }
      })
    },
    destroySortable() {
      if (this.sortableInstance) {
        this.sortableInstance.destroy()
        this.sortableInstance = null
      }
    },
    // 操作
    pauseTask(row) {
      pauseTask(row.id).then(() => {
        this.$message.success('任务已暂停')
        row.status = 'paused'
      })
    },
    resumeTask(row) {
      resumeTask(row.id).then(() => {
        this.$message.success('任务已继续')
        row.status = 'executing'
      })
    },
    disableTask(row) {
      disableTask(row.id).then(() => {
        this.$message.success('任务已禁用')
        row.status = 'disabled'
      })
    },
    enableTask(row) {
      enableTask(row.id).then(() => {
        this.$message.success('任务已启用')
        row.status = 'pending'
      })
    },
    stopTask(row) {
      this.$confirm('停止后任务将重置为未开始，确定吗？', '提示', { type: 'warning' }).then(() => {
        stopTask(row.id).then(() => {
          this.$message.success('任务已停止')
          row.status = 'pending'
          row.completedSteps = 0
          if (row.steps) row.steps.forEach(s => { s.status = 'pending' })
        })
      })
    },
    cancelTask(row) {
      this.$confirm('取消准备后任务变为未开始，确定吗？', '提示', { type: 'warning' }).then(() => {
        cancelTask(row.id).then(() => {
          this.$message.success('任务已取消')
          row.status = 'pending'
          delete row.sortOrder
        })
      })
    },
    canResume(row) {
      // 检查机器人是否有正在执行的任务
      return !this.tasks.some(t => t.id !== row.id && t.robotId === row.robotId && t.status === 'executing')
    },
    viewDetail(row) {
      // 可复用任务详情对话框，这里简单alert
      this.$alert(JSON.stringify(row, null, 2), '任务详情')
    }
  },
  beforeDestroy() {
    this.destroySortable()
  }
}
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  padding: 15px;
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 15px;
}
.stat-icon.blue { background-color: #e6f7ff; color: #1890ff; }
.stat-icon.green { background-color: #f6ffed; color: #52c41a; }
.stat-icon.orange { background-color: #fff7e6; color: #fa8c16; }
.stat-icon.red { background-color: #fff1f0; color: #ff4d4f; }
.stat-info { flex: 1; }
.stat-value { font-size: 24px; font-weight: bold; line-height: 1.2; }
.stat-label { font-size: 14px; color: #666; }
.mt20 { margin-top: 20px; }
.status-tag { padding: 2px 6px; border-radius: 10px; font-size: 12px; }
.status-pending { background-color: #f0f0f0; color: #666; }
.status-ready { background-color: #e6f7ff; color: #1890ff; }
.status-executing { background-color: #f6ffed; color: #52c41a; }
.status-paused { background-color: #fffbe6; color: #faad14; }
.status-high-risk { background-color: #fff1f0; color: #ff4d4f; border: 1px solid #ff4d4f; }
.step-progress { display: flex; align-items: center; gap: 4px; }
.step-dot { width: 8px; height: 8px; border-radius: 50%; background-color: #d9d9d9; }
.step-dot.completed { background-color: #52c41a; }
.step-dot.executing { background-color: #1890ff; animation: pulse 1.5s infinite; }
@keyframes pulse { 0% { opacity: 1; } 50% { opacity: 0.5; } 100% { opacity: 1; } }
.drag-handle { cursor: move; color: #999; padding: 4px 8px; border-radius: 4px; user-select: none; }
.drag-handle:hover { color: #1890ff; background-color: #f0f0f0; }
.queue-index { margin-left: 4px; background-color: #1890ff; color: white; width: 18px; height: 18px; border-radius: 50%; display: inline-block; text-align: center; line-height: 18px; font-size: 12px; }
.sortable-ghost { opacity: 0.4; background-color: #e6f7ff !important; border: 2px dashed #1890ff !important; }
.sortable-drag { opacity: 0.9; box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
.abnormal-row { background-color: #fff1f0 !important; }
</style>
