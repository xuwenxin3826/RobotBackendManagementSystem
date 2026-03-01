package com.ruoyi.data.domain.strategy;

import com.ruoyi.data.domain.bo.CleanRule;
import com.ruoyi.data.domain.context.DataContext;

public interface CleanStrategy {

    void execute(DataContext context);

}