package com.ruoyi.data.service.impl;

import com.ruoyi.data.domain.bo.CleanRule;
import com.ruoyi.data.domain.context.DataContext;
import com.ruoyi.data.domain.enums.ExecuteMode;
import com.ruoyi.data.domain.RuleRegistry;
import com.ruoyi.data.mapper.CleanRuleMapper;
import com.ruoyi.data.mapper.po.CleanRulePo;
import com.ruoyi.data.service.ICleanRuleExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CleanRuleExecuteServiceImpl implements ICleanRuleExecuteService {

    @Autowired
    private CleanRuleMapper cleanRuleMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeTask(Long id) {
        //测试
        System.out.println("===== executeTask 开始，id: " + id + " =====");

        // 1️⃣ 查询任务
        CleanRulePo po = cleanRuleMapper.selectById(id);

        if (po == null) {
            throw new RuntimeException("清洗任务不存在");
        }

        // 2️⃣ 构建领域对象
        CleanRule rule = RuleRegistry.build(po);
        System.out.println("数据源数量: " + rule.getDataSources().size());

        // 3️⃣ 校验执行模式
        if (!ExecuteMode.IMMEDIATE.equals(rule.getExecuteMode())) {
            throw new RuntimeException("当前版本仅支持立即执行模式");
        }

        // 4️⃣ 构建上下文
        DataContext context = new DataContext(rule.getDataSources());

        // ⭐⭐⭐ 关键新增逻辑 ⭐⭐⭐
        // 5️⃣ 查询每个表的字段（排除id）
        rule.getDataSources().forEach(source -> {

            String tableName = source.getTableName();
            System.out.println("正在处理表: " + tableName);

            List<String> columns = cleanRuleMapper.getTableColumns(tableName);
            System.out.println("表 " + tableName + " 的字段（原始）: " + columns);

            if (columns == null || columns.isEmpty()) {
                throw new RuntimeException("表 " + tableName + " 未获取到字段信息");
            }

            context.setTableColumns(tableName, columns);
        });

        // 6️⃣ 领域规则生成SQL
        rule.execute(context);

        System.out.println("rule.execute 执行完毕，当前 context 中 SQL 数量: " + context.getSqlList().size());

        // 7️⃣ 执行SQL
        executeSql(context);
    }

    private void executeSql(DataContext context) {
        System.out.println("executeSql 被调用，SQL 列表大小: " + (context.getSqlList() == null ? 0 : context.getSqlList().size()));
        if (context.getSqlList() == null || context.getSqlList().isEmpty()) {
            return;
        }

        for (String sql : context.getSqlList()) {

            System.out.println("执行SQL: " + sql); // 建议调试阶段打开

            jdbcTemplate.execute(sql);

            System.out.println("SQL 执行完成");
        }
    }
}