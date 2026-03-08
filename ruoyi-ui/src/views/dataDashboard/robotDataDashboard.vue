<template>
  <div class="data-dashboard">

    <!-- ===== 顶部筛选 ===== -->
    <div class="dashboard-header">
      <el-select v-model="dashboardTimeRange" size="small" style="width:120px">
        <el-option label="今日" value="today"/>
        <el-option label="本周" value="week"/>
        <el-option label="本月" value="month"/>
        <el-option label="本年" value="year"/>
      </el-select>

      <el-button size="small" @click="refreshDashboard">
        刷新
      </el-button>
    </div>

    <!-- ===== 关键指标 ===== -->
    <div class="key-metrics">
      <div class="metric-card">
        <div class="metric-value">{{ robotStats.total }}</div>
        <div class="metric-label">总机器人数量</div>
      </div>

      <div class="metric-card">
        <div class="metric-value">{{ robotStats.online }}</div>
        <div class="metric-label">在线机器人</div>
      </div>



      <div class="metric-card">
        <div class="metric-value">{{ exceptionStats.today }}</div>
        <div class="metric-label">今日异常</div>
      </div>

      <div class="metric-card">
        <div class="metric-value">{{ feedbackStats.today }}</div>
        <div class="metric-label">今日反馈</div>
      </div>

      <div class="metric-card">
        <div class="metric-value">{{ taskStats.completed }}</div>
        <div class="metric-label">完成任务</div>
      </div>
    </div>

    <!-- ===== 主体区域 ===== -->
    <div class="dashboard-main">

      <!-- 左侧 -->
      <div class="left-panel">

        <!-- 机器人状态图 -->
        <div class="chart-card">
          <h3>在线机器人情况</h3>
          <div id="robotStatusChart" class="chart-box"></div>
        </div>

        <!-- 任务执行情况 -->
        <div class="chart-card">
          <h3>任务执行情况</h3>

          <div class="task-section">

            <!-- 执行中 -->
            <div class="task-category">
              <div class="category-title">
                执行中 ({{ taskStats.executing }})
              </div>

              <div
                class="task-item"
                v-for="item in executingTasks"
                :key="item.id"
              >
                <div class="task-info">
                  <div class="task-name">{{ item.name }}</div>
                  <div class="task-desc">{{ item.description }}</div>
                </div>

                <el-progress
                  :percentage="item.progress"
                  :stroke-width="6"
                />
              </div>
            </div>

            <!-- 待执行 -->
            <div class="task-category">
              <div class="category-title">
                待执行 ({{ taskStats.pending }})
              </div>

              <div
                class="task-item"
                v-for="item in pendingTasks"
                :key="item.id"
              >
                <div class="task-name">{{ item.name }}</div>
                <div class="task-time">{{ item.scheduledTime }}</div>
              </div>
            </div>

          </div>
        </div>

      </div>

      <!-- 右侧 -->
      <div class="right-panel">

        <!-- 机器人分组 -->
        <div class="chart-card">
          <h3>机器人分组状态</h3>

          <div class="group-item">
            客服组：
            {{ robotStats.byGroup.customerService }}
            /
            {{ robotStats.totalByGroup.customerService }}
          </div>

          <div class="group-item">
            运维组：
            {{ robotStats.byGroup.operations }}
            /
            {{ robotStats.totalByGroup.operations }}
          </div>

          <div class="group-item">
            质检组：
            {{ robotStats.byGroup.quality }}
            /
            {{ robotStats.totalByGroup.quality }}
          </div>
        </div>

        <!-- 最近上线 -->
        <div class="chart-card">
          <h3>最近上线机器人</h3>

          <div
            class="recent-item"
            v-for="robot in recentOnlineRobots"
            :key="robot.id"
          >
            {{ robot.name }} - {{ robot.time }}
          </div>
        </div>

        <!-- 地图 -->
        <div class="chart-card">
          <h3>机器人当前位置</h3>
          <div id="robotMapChart" class="chart-box"></div>
        </div>

      </div>

    </div>
  </div>
</template>

<script>
import * as echarts from "echarts"
import chinaJson from "@/assets/map/china.json"

export default {
  data() {
    return {
      dashboardTimeRange: "today",
      robotStatusChart: null,
      robotMapChart: null,

      robotStats: {
        total: 120,
        online: 80,
        busy: 25,
        offline: 15,
        byGroup: {
          customerService: 20,
          operations: 30,
          quality: 15
        },
        totalByGroup: {
          customerService: 30,
          operations: 40,
          quality: 20
        }
      },

      exceptionStats: {today: 5},
      feedbackStats: {today: 12},

      taskStats: {
        executing: 2,
        pending: 3,
        completed: 50
      },

      executingTasks: [
        {id: 1, name: "巡检任务A", description: "设备巡检", progress: 60},
        {id: 2, name: "清洁任务B", description: "区域清洁", progress: 30}
      ],

      pendingTasks: [
        {id: 3, name: "配送任务C", scheduledTime: "10:30"},
        {id: 4, name: "盘点任务D", scheduledTime: "11:00"}
      ],

      recentOnlineRobots: [
        {id: 1, name: "机器人001", time: "09:15"},
        {id: 2, name: "机器人002", time: "09:20"}
      ]
    }
  },

  mounted() {
    echarts.registerMap("china", chinaJson)
    this.initCharts()
    window.addEventListener("resize", this.resizeCharts)
  },

  beforeDestroy() {
    window.removeEventListener("resize", this.resizeCharts)
  },

  methods: {
    initCharts() {
      this.initStatusChart()
      this.initMapChart()
    },

    resizeCharts() {
      this.robotStatusChart && this.robotStatusChart.resize()
      this.robotMapChart && this.robotMapChart.resize()
    },

    refreshDashboard() {
      this.initCharts()
    },

    initStatusChart() {
      this.robotStatusChart = echarts.init(
        document.getElementById("robotStatusChart")
      )

      this.robotStatusChart.setOption({
        tooltip: {trigger: "item"},
        legend: {bottom: 0},
        series: [{
          type: "pie",
          radius: ["45%", "70%"],
          data: [
            {value: this.robotStats.online, name: "在线"},
            {value: this.robotStats.busy, name: "忙碌"},
            {value: this.robotStats.offline, name: "离线"}
          ]
        }]
      })
    },

    initMapChart() {
      this.robotMapChart = echarts.init(
        document.getElementById("robotMapChart")
      )

      this.robotMapChart.setOption({
        geo: {
          map: "china",
          roam: true
        },
        series: [{
          type: "effectScatter",
          coordinateSystem: "geo",
          rippleEffect: {brushType: "stroke"},
          symbolSize: 12,
          data: [
            {name: "北京", value: [116.4, 39.9, 5]},
            {name: "上海", value: [121.47, 31.23, 8]},
            {name: "广州", value: [113.23, 23.16, 3]}
          ]
        }]
      })
    }
  }
}
</script>

<style scoped>
.data-dashboard {
  padding: 20px;
  background: #f5f7fa;
}

.dashboard-header {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.key-metrics {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.metric-card {
  flex: 1;
  background: #fff;
  padding: 15px;
  text-align: center;
  border-radius: 6px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #1890ff;
}

.dashboard-main {
  display: flex;
  gap: 20px;
}

.left-panel {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.chart-card {
  background: #fff;
  padding: 15px;
  border-radius: 6px;
}

.chart-box {
  width: 100%;
  height: 300px;
}

.task-item {
  margin-bottom: 10px;
}

.group-item, .recent-item {
  margin-bottom: 8px;
}
</style>
