<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="字段完整性校验" prop="fieldIntegrity">
        <el-input
          v-model="queryParams.fieldIntegrity"
          placeholder="请输入字段完整性校验"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间格式统一" prop="timeFormat">
        <el-input
          v-model="queryParams.timeFormat"
          placeholder="请输入时间格式统一"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态码映射规则" prop="statusMapping">
        <el-input
          v-model="queryParams.statusMapping"
          placeholder="请输入状态码映射规则"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="异常值过滤策略" prop="outlierFilter">
        <el-input
          v-model="queryParams.outlierFilter"
          placeholder="请输入异常值过滤策略"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文本数据清洗" prop="textCleaning">
        <el-input
          v-model="queryParams.textCleaning"
          placeholder="请输入文本数据清洗"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="重复数据处理" prop="duplicateHandling">
        <el-input
          v-model="queryParams.duplicateHandling"
          placeholder="请输入重复数据处理"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行方式" prop="executeMode">
        <el-input
          v-model="queryParams.executeMode"
          placeholder="请输入执行方式"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="应用数据源" prop="applyData">
        <el-input
          v-model="queryParams.applyData"
          placeholder="请输入应用数据源"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行时间" prop="runTime">
        <el-input
          v-model="queryParams.runTime"
          placeholder="请输入执行时间"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['dataClean:dataCleanConfig:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['dataClean:dataCleanConfig:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['dataClean:dataCleanConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['dataClean:dataCleanConfig:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataCleanConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="字段完整性校验" align="center" prop="fieldIntegrity" />
      <el-table-column label="时间格式统一" align="center" prop="timeFormat" />
      <el-table-column label="状态码映射规则" align="center" prop="statusMapping" />
      <el-table-column label="异常值过滤策略" align="center" prop="outlierFilter" />
      <el-table-column label="文本数据清洗" align="center" prop="textCleaning" />
      <el-table-column label="重复数据处理" align="center" prop="duplicateHandling" />
      <el-table-column label="执行方式" align="center" prop="executeMode" />
      <el-table-column label="应用数据源" align="center" prop="applyData" />
      <el-table-column label="执行时间" align="center" prop="runTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <!-- Vue 2 插槽语法：使用 slot-scope -->
        <template slot-scope="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['dataClean:dataCleanConfig:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['dataClean:dataCleanConfig:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改数据清洗规则配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="dataCleanConfigRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字段完整性校验" prop="fieldIntegrity">
          <el-input v-model="form.fieldIntegrity" placeholder="请输入字段完整性校验" />
        </el-form-item>
        <el-form-item label="时间格式统一" prop="timeFormat">
          <el-input v-model="form.timeFormat" placeholder="请输入时间格式统一" />
        </el-form-item>
        <el-form-item label="状态码映射规则" prop="statusMapping">
          <el-input v-model="form.statusMapping" placeholder="请输入状态码映射规则" />
        </el-form-item>
        <el-form-item label="异常值过滤策略" prop="outlierFilter">
          <el-input v-model="form.outlierFilter" placeholder="请输入异常值过滤策略" />
        </el-form-item>
        <el-form-item label="文本数据清洗" prop="textCleaning">
          <el-input v-model="form.textCleaning" placeholder="请输入文本数据清洗" />
        </el-form-item>
        <el-form-item label="重复数据处理" prop="duplicateHandling">
          <el-input v-model="form.duplicateHandling" placeholder="请输入重复数据处理" />
        </el-form-item>
        <el-form-item label="执行方式" prop="executeMode">
          <el-input v-model="form.executeMode" placeholder="请输入执行方式" />
        </el-form-item>
        <el-form-item label="应用数据源" prop="applyData">
          <el-input v-model="form.applyData" placeholder="请输入应用数据源" />
        </el-form-item>
        <el-form-item label="执行时间" prop="runTime">
          <el-input v-model="form.runTime" placeholder="请输入执行时间" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDataCleanConfig, getDataCleanConfig, delDataCleanConfig, addDataCleanConfig, updateDataCleanConfig } from "@/api/dataClean/dataCleanConfig";

export default {
  name: "DataCleanConfig",
  data() {
    return {
      // 列表数据
      dataCleanConfigList: [],
      // 是否显示弹窗
      open: false,
      // 是否加载中
      loading: true,
      // 是否显示搜索框
      showSearch: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 弹窗标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fieldIntegrity: null,
        timeFormat: null,
        statusMapping: null,
        outlierFilter: null,
        textCleaning: null,
        duplicateHandling: null,
        executeMode: null,
        applyData: null,
        runTime: null
      },
      // 表单参数
      form: {},
      // 表单校验规则
      rules: {
        fieldIntegrity: [
          { required: true, message: "字段完整性校验不能为空", trigger: "blur" }
        ],
        timeFormat: [
          { required: true, message: "时间格式统一不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询数据清洗规则配置列表 */
    getList() {
      this.loading = true;
      listDataCleanConfig(this.queryParams).then(response => {
        this.dataCleanConfigList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        fieldIntegrity: null,
        timeFormat: null,
        statusMapping: null,
        outlierFilter: null,
        textCleaning: null,
        duplicateHandling: null,
        executeMode: null,
        applyData: null,
        runTime: null
      };
      // 重置表单校验
      this.$refs.dataCleanConfigRef?.resetFields();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.$refs.queryRef?.resetFields();
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据清洗规则配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getDataCleanConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数据清洗规则配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs.dataCleanConfigRef.validate(valid => {
        if (valid) {
          if (this.form.id) {
            updateDataCleanConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDataCleanConfig(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除数据清洗规则配置编号为"' + ids + '"的数据项？').then(() => {
        return delDataCleanConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('dataClean/dataCleanConfig/export', {
        ...this.queryParams
      }, `dataCleanConfig_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>
