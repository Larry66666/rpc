package com.yupi.yurpc.fault.tolerant;

import com.yupi.yurpc.spi.SpiLoader;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 容错策略工厂（工厂模式，用于获取容错策略对象）
 */
public class TolerantStrategyFactory {
    /**
     * 使用 AtomicReference 来确保线程安全的懒加载
     */
    private static final AtomicReference<TolerantStrategy> tolerantStrategyRef = new AtomicReference<>();

    /**
     * 默认序列化器
     */
    private static final TolerantStrategy DEFAULT_RETRY_STRATEGY = new FailFastTolerantStrategy();

    /**
     * 获取实例(双重检查锁定)
     * @param key
     * @return
     */
    public static TolerantStrategy getInstance(String key) {
        TolerantStrategy tolerantStrategy = tolerantStrategyRef.get();
        if (tolerantStrategy == null) {
            synchronized (TolerantStrategyFactory.class) {
                tolerantStrategy = tolerantStrategyRef.get();
                if (tolerantStrategy == null) {
                    tolerantStrategy = SpiLoader.getInstance(TolerantStrategy.class, key);
                    tolerantStrategyRef.set(tolerantStrategy);
                }
            }
        }
        return tolerantStrategy;
    }
}
