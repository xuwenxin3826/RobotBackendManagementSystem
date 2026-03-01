package com.ruoyi.data.domain.bo;

import com.ruoyi.data.domain.context.DataContext;
import com.ruoyi.data.domain.enums.*;
import com.ruoyi.data.domain.strategy.CleanStrategy;
import com.ruoyi.data.domain.strategy.StrategyFactory;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class CleanRule {

    private Long id;

    private String ruleName;

    private String description;

    private ExecuteMode executeMode;

    private LocalTime runTime;

    private List<DataSourceType> dataSources;

    private CleanRuleConfig config;

    /* ========= 领域行为 ========= */

    public boolean isScheduled() {
        return ExecuteMode.SCHEDULED.equals(this.executeMode);
    }

    public void execute(DataContext context) {
        List<CleanStrategy> strategies =
                StrategyFactory.build(this);
        System.out.println("策略数量: " + strategies.size());

        for (CleanStrategy strategy : strategies) {
            System.out.println("执行策略: " + strategy.getClass().getSimpleName());
            strategy.execute(context);
        }
    }

}