package com.ruoyi.common.core.model;

import javax.validation.constraints.NotNull;

public interface Stateful {

    /**
     * 状态是否允许迁移
     * @param status
     * @return
     */
    boolean allowTransitStatus(@NotNull Byte status);

    /**
     * 获得状态的名称
     * @return message bundles中的名称
     */
    String getStatusName();

}
