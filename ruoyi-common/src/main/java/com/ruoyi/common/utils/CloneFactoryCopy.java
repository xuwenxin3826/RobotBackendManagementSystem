package com.ruoyi.common.utils;

@FunctionalInterface
public interface CloneFactoryCopy<T,K> {
    T apply(K k);
}
